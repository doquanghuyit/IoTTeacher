package com.iot.fti.fpt.awscloudservice.iot;

import android.os.Parcel;
import android.os.Parcelable;

import com.amazonaws.regions.Regions;

/**
 * Created by doquanghuy on 10/26/17.
 */

public class AwsIotMqttConfig implements Parcelable {

    private String endpoint;
    private String cognitoPoolId;
    private String policyName;
    private String region;
    private String keystoreName;
    private String keystorePassword;
    private String certificateId;
    private int keepConnection;

    public AwsIotMqttConfig(String endpoint, String cognitoPoolId, String policyName,
                            String region, String keystoreName, String keystorePassword, String certificateId, int keepConnection)
    {
        setEndpoint(endpoint);
        setCognitoPoolId(cognitoPoolId);
        setPolicyName(policyName);
        setRegion(region);
        setKeystoreName(keystoreName);
        setKeystorePassword(keystorePassword);
        setCertificateId(certificateId);
        setKeepConnection(keepConnection);
    }

    public AwsIotMqttConfig(Parcel in ) {
        readFromParcel( in );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AwsIotMqttConfig createFromParcel(Parcel in ) {
            return new AwsIotMqttConfig( in );
        }
        public AwsIotMqttConfig[] newArray(int size) {
            return new AwsIotMqttConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(endpoint);
        dest.writeString(cognitoPoolId);
        dest.writeString(policyName);
        dest.writeString(region);
        dest.writeString(keystoreName);
        dest.writeString(keystorePassword);
        dest.writeString(certificateId);
        dest.writeInt(keepConnection);
    }

    private void readFromParcel(Parcel in ) {
        endpoint = in .readString();
        cognitoPoolId = in .readString();
        policyName = in .readString();
        region = in .readString();
        keystoreName = in .readString();
        keystorePassword = in .readString();
        certificateId = in .readString();
        keepConnection = in .readInt();
    }



    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getCognitoPoolId() {
        return cognitoPoolId;
    }

    public void setCognitoPoolId(String cognitoPoolId) {
        this.cognitoPoolId = cognitoPoolId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getKeystoreName() {
        return keystoreName;
    }

    public void setKeystoreName(String keystoreName) {
        this.keystoreName = keystoreName;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public boolean isKeepConnection()
    {
        return keepConnection == 1;
    }

    public int getKeepConnection() {
        return keepConnection;
    }

    public void setKeepConnection(int keepConnection) {
        this.keepConnection = keepConnection;
    }

    public Regions getRegionType() {
        return Regions.valueOf(getRegion());
    }
}
