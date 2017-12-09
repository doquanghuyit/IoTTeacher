package com.iot.fti.fpt.awscloudservice.iot;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;

/**
 * Created by doquanghuy on 10/26/17.
 */

public interface AwsIotMqttCallbackListener {
    void onAwsIotMqttPubSubStatus(String awsIotPolicyName, String topic, AwsIotMqttPubSubStatus awsIotMqttPubSubStatus);
    void onAWSIotMqttStatusChange(String awsIotPolicyName,AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus status);
    void onMessageArrived(String awsIotPolicyName,String topic, byte[] data);
}
