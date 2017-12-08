package com.iot.fti.fpt.awscloudservice.iot;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.iot.AWSIotKeystoreHelper;
import com.amazonaws.regions.Region;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.AttachPrincipalPolicyRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;

import java.security.KeyStore;

/**
 * Created by doquanghuy on 10/26/17.
 */

public class AwsIotMqttClientKeyStore {

    public interface  OnClientKeyStoreListener
    {
        void onClientKeyStoreInit(boolean success);
    }

    static final String LOG_TAG = "MqttClientKeyStore";
    private KeyStore clientKeyStore;
    private AwsIotMqttConfig awsIotMqttConfig;
    private String keystorePath;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private AWSIotClient mIotAndroidClient;
    private OnClientKeyStoreListener onClientKeyStoreListener;

    public AwsIotMqttClientKeyStore(OnClientKeyStoreListener onClientKeyStoreListener,
                                    Context context,
                                    AwsIotMqttConfig awsIotMqttConfig)
    {
        this.onClientKeyStoreListener = onClientKeyStoreListener;
        this.awsIotMqttConfig = awsIotMqttConfig;
        this.credentialsProvider = new CognitoCachingCredentialsProvider(context, awsIotMqttConfig.getCognitoPoolId(), awsIotMqttConfig.getRegionType());
        this.mIotAndroidClient = new AWSIotClient(credentialsProvider);
        this.mIotAndroidClient.setRegion(Region.getRegion(awsIotMqttConfig.getRegionType()));
        this.keystorePath = context.getFilesDir().getPath();
    }

    public void initCertificate()
    {
        // To load cert/key from keystore on filesystem
        if(clientKeyStore != null)
        {
            onClientKeyStoreListener.onClientKeyStoreInit(true);
        }else {

            try {
                if (AWSIotKeystoreHelper.isKeystorePresent(keystorePath, awsIotMqttConfig.getKeystoreName())) {
                    if (AWSIotKeystoreHelper.keystoreContainsAlias(awsIotMqttConfig.getCertificateId(), keystorePath,
                            awsIotMqttConfig.getKeystoreName(), awsIotMqttConfig.getKeystorePassword())) {
                        Log.i(LOG_TAG, "Certificate " + awsIotMqttConfig.getCertificateId()
                                + " found in keystore - using for MQTT.");
                        // load keystore from file into memory to pass on connection

                        clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(awsIotMqttConfig.getCertificateId(),
                                keystorePath, awsIotMqttConfig.getKeystoreName(), awsIotMqttConfig.getKeystorePassword());

                    } else {
                        Log.i(LOG_TAG, "Key/cert " + awsIotMqttConfig.getCertificateId() + " not found in keystore.");
                    }
                } else {
                    Log.i(LOG_TAG, "Keystore " + keystorePath + "/" + awsIotMqttConfig.getKeystoreName() + " not found.");
                }

            } catch (Exception e) {
                Log.e(LOG_TAG, "An error occurred retrieving cert/key from keystore.", e);
            }
            if (clientKeyStore == null) {
                Log.i(LOG_TAG, "Cert/key was not found in keystore - creating new key and certificate.");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Create a new private key and certificate. This call
                            // creates both on the server and returns them to the
                            // device.
                            CreateKeysAndCertificateRequest createKeysAndCertificateRequest =
                                    new CreateKeysAndCertificateRequest();
                            createKeysAndCertificateRequest.setSetAsActive(true);
                            final CreateKeysAndCertificateResult createKeysAndCertificateResult;
                            createKeysAndCertificateResult =
                                    mIotAndroidClient.createKeysAndCertificate(createKeysAndCertificateRequest);
                            Log.i(LOG_TAG,
                                    "Cert ID: " +
                                            createKeysAndCertificateResult.getCertificateId() +
                                            " created.");

                            // store in keystore for use in MQTT client
                            // saved as alias "default" so a new certificate isn't
                            // generated each run of this application
                            AWSIotKeystoreHelper.saveCertificateAndPrivateKey(awsIotMqttConfig.getCertificateId(),
                                    createKeysAndCertificateResult.getCertificatePem(),
                                    createKeysAndCertificateResult.getKeyPair().getPrivateKey(),
                                    keystorePath, awsIotMqttConfig.getKeystoreName(), awsIotMqttConfig.getKeystorePassword());

                            // load keystore from file into memory to pass on
                            // connection
                            clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(awsIotMqttConfig.getCertificateId(),
                                    keystorePath, awsIotMqttConfig.getKeystoreName(), awsIotMqttConfig.getKeystorePassword());

                            if(clientKeyStore != null)
                            {
                                // Attach a policy to the newly created certificate.
                                // This flow assumes the policy was already created in
                                // AWSConst IoT and we are now just attaching it to the
                                // certificate.
                                AttachPrincipalPolicyRequest policyAttachRequest =
                                        new AttachPrincipalPolicyRequest();
                                policyAttachRequest.setPolicyName(awsIotMqttConfig.getPolicyName());
                                policyAttachRequest.setPrincipal(createKeysAndCertificateResult
                                        .getCertificateArn());
                                mIotAndroidClient.attachPrincipalPolicy(policyAttachRequest);
                                onClientKeyStoreListener.onClientKeyStoreInit(true);
                            }else onClientKeyStoreListener.onClientKeyStoreInit(false);



                        } catch (Exception e) {
                            Log.d(LOG_TAG, e.toString());
                            Log.e(LOG_TAG,
                                    "Exception occurred when generating new private key and certificate.",
                                    e);

                            onClientKeyStoreListener.onClientKeyStoreInit(false);
                        }
                    }
                }).start();
            }else {
                onClientKeyStoreListener.onClientKeyStoreInit(true);
            }
        }
    }

    public KeyStore getClientKeyStore() {
        return clientKeyStore;
    }
}
