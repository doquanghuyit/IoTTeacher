package com.iot.fti.fpt.awscloudservice.iot;

import android.content.Context;
import android.util.Log;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import iot.fti.fpt.rogobase.callback.TimeoutCallback;


/**
 * Created by doquanghuy on 10/26/17.
 */

public class AwsIotMqttCallback implements AWSIotMqttNewMessageCallback, AWSIotMqttClientStatusCallback,AwsIotMqttClientKeyStore.OnClientKeyStoreListener {
    static final String LOG_TAG = "AwsIotMqttCallBack";
    private TimeoutCallback.OnTimeoutCallbackListener onTimeoutCallbackListener = new TimeoutCallback.OnTimeoutCallbackListener() {
        @Override
        public void endTimeout(int requestCode) {
            connect();
        }
    };

    // AWSConst Iot CLI describe-endpoint call returns: XXXXXXXXXX.iot.<region>.amazonaws.com
    // Cognito pool ID. For this app, pool needs to be unauthenticated pool with
    // Region of AWSConst IoT
    private String clientId;
    private AWSIotMqttManager mqttManager;
    private AwsIotMqttClientKeyStore awsIotMqttClientKeyStore;
    private AwsIotMqttCallbackListener awsIotMqttCallbackListener;
    private AwsIotMqttConfig awsIotMqttConfig;
    private AWSIotMqttClientStatus statusConnection;
    private TimeoutCallback timeoutCallback;
    private boolean initKeyStore;


    public AwsIotMqttCallback(String clientId, AwsIotMqttCallbackListener awsIotMqttCallbackListener, Context context, AwsIotMqttConfig awsIotMqttConfig)
    {
        this.clientId = clientId;
        this.awsIotMqttCallbackListener = awsIotMqttCallbackListener;
        this.awsIotMqttConfig = awsIotMqttConfig;
        this.statusConnection = AWSIotMqttClientStatus.ConnectionLost;
        this.initKeyStore = true;
        initMqtt(context);
    }

    public AwsIotMqttConfig getAwsIotMqttConfig()
    {
        return awsIotMqttConfig;
    }


    private void initMqtt(Context context){
        // Initialize the AWSConst Cognito credentials provider

        // MQTT Client
        mqttManager = new AWSIotMqttManager(clientId, awsIotMqttConfig.getEndpoint());
        // Set keepalive to 10 seconds.  Will recognize disconnects more quickly but will also send
        // MQTT pings every 10 seconds.
        mqttManager.setKeepAlive(30);
        mqttManager.setAutoReconnect(true);
        mqttManager.setOfflinePublishQueueEnabled(true);
        mqttManager.setMaxAutoReconnectAttepts(100);
        mqttManager.setAutoReconnect(true);

        // Set Last Will and Testament for MQTT.  On an unclean disconnect (loss of connection)
        // AWSConst IoT will publish this message to alert other clients.
//        AWSIotMqttLastWillAndTestament lwt = new AWSIotMqttLastWillAndTestament("my/lwt/topic",
//                "Android client lost connection", AWSIotMqttQos.QOS0);
//        mqttManager.setMqttLastWillAndTestament(lwt);

        awsIotMqttClientKeyStore = new AwsIotMqttClientKeyStore(
                this,
                context,
                awsIotMqttConfig
                );
        // IoT Client (for creation of certificate if needed)
        timeoutCallback = new TimeoutCallback(onTimeoutCallbackListener);
    }


    public void connect()
    {
        if(initKeyStore && awsIotMqttClientKeyStore.getClientKeyStore() == null)
        {
            awsIotMqttClientKeyStore.initCertificate();
        }else {
            mqttManager.connect(awsIotMqttClientKeyStore.getClientKeyStore(),this);
        }

    }

    public void disconnect()
    {
        mqttManager.disconnect();
    }

    public void publish(String topic, String message, AWSIotMqttQos qos)
    {
        Log.d(LOG_TAG,topic+"::publish()::"+message+"::qos::"+qos.toString());
        mqttManager.publishString(message, topic, qos);
    }

    public void publish(String topic, String message)
    {
        Log.d(LOG_TAG,topic+"::publish() with default qos::"+message);
        mqttManager.publishString(message, topic, AWSIotMqttQos.QOS0);
    }

    public void subscribeTopic(String topic)
    {
        Log.d(LOG_TAG,"::subscribeTopic::"+topic);
        mqttManager.subscribeToTopic(topic, AWSIotMqttQos.QOS0,this);
    }

    public void unSubcribeTopoc(String topic)
    {
        mqttManager.unsubscribeTopic(topic);
    }

    @Override
    public void onClientKeyStoreInit(boolean success) {
        if(success)
        {
            initKeyStore = false;
            mqttManager.connect(awsIotMqttClientKeyStore.getClientKeyStore(),this);
        }
    }

    @Override
    public void onStatusChanged(AWSIotMqttClientStatus status, Throwable throwable) {
        try {
            Log.d(LOG_TAG,"onStatusChanged::" + status);
            statusConnection = status;
            switch (status) {
                case Connecting:
                    break;
                case Connected:
                    break;
                case ConnectionLost:
                    if (awsIotMqttConfig.isKeepConnection())
                        timeoutCallback.startTimeOut(1000, 1);
                    break;
                case Reconnecting:
                    break;
            }
            awsIotMqttCallbackListener.onAWSIotMqttStatusChange(clientId, status);

        } catch (Exception e) {
            Log.e(LOG_TAG,"onStatusChanged::",e);
        }
    }

    @Override
    public void onMessageArrived(String topic, byte[] data) {
        awsIotMqttCallbackListener.onMessageArrived(clientId,topic,data);
    }


    public AWSIotMqttClientStatus getStatusConnection() {
        return statusConnection;
    }
}
