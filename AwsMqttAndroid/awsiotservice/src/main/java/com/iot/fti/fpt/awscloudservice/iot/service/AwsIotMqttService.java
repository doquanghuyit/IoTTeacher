package com.iot.fti.fpt.awscloudservice.iot.service;

import android.content.Intent;
import android.util.Log;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttCallback;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttCallbackListener;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttConfig;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttPubSubStatus;

import java.util.HashMap;

import iot.fti.fpt.rogobase.base.IService;
import iot.fti.fpt.rogobase.utils.EnumCheck;

/**
 * Created by doquanghuy on 10/26/17.
 */

public class AwsIotMqttService extends IService implements AwsIotMqttCallbackListener {
    public static final String TAG = "AwsIotMqttService";
    public static final String SERVICE_FILTER_INTENT = "com.iot.fti.fpt.awscloudservice.";
    public static final String EXTRA_AWS_IOT_MQTT_ID = "awscloudservice.extra.id";
    public static final String EXTRA_AWS_IOT_MQTT_TOPIC = "awscloudservice.extra.topic";
    public static final String EXTRA_AWS_IOT_MQTT_MESSAGE = "awscloudservice.extra.message";
    public static final String EXTRA_AWS_IOT_MQTT_DATA_BYTES = "awscloudservice.extra.data_bytes";
    public static final String EXTRA_AWS_IOT_MQTT_CONFIG = "awscloudservice.extra.awsconf";
    public static final String EXTRA_AWS_IOT_MQTT_CONNECTION_STATUS = "awscloudservice.extra.connectionstatus";
    public static final String EXTRA_AWS_IOT_MQTT_PUBSUB_STATUS = "awscloudservice.extra.pubsubstatus";
    public static final String EXTRA_AWS_IOT_MQTT_RESPONSE_TYPE = "awscloudservice.extra.response.type";

    public enum AwsIoTMqttServiceResponse
    {
        ConnectionStatus,PubSubStatus,MesssageReceiver
    }

    private HashMap<String,AwsIotMqttCallback> awsIotMqttCallbacks;
    @Override
    public void onCreate() {
        super.onCreate();
        awsIotMqttCallbacks = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null  && intent.getAction() != null)
        {
            try {
                String action = intent.getAction();
                String awsIotMqttId = intent.getStringExtra(EXTRA_AWS_IOT_MQTT_ID);
                if(EnumCheck.contains(AwsIotMqttServiceAction.class,action) && awsIotMqttId != null)
                {
                    AwsIotMqttServiceAction awsIotMqttServiceAction = AwsIotMqttServiceAction.valueOf(action);
                    if(awsIotMqttServiceAction == AwsIotMqttServiceAction.CREATE)
                        createAwsIotMqttCallback(awsIotMqttId, intent);
                    else if(awsIotMqttCallbacks.containsKey(awsIotMqttId))
                    {
                        String topic = intent.getStringExtra(EXTRA_AWS_IOT_MQTT_TOPIC);
                        switch (awsIotMqttServiceAction)
                        {
                            case PUBLISH:
                                publishMessage(awsIotMqttId, topic, intent.getStringExtra(EXTRA_AWS_IOT_MQTT_MESSAGE));
                                break;
                            case SUBCRIBE:
                                subcribe(awsIotMqttId, topic);
                                break;
                            case UNSUBCRIBE:
                                unSubcribe(awsIotMqttId, topic);
                                break;
                            case CONNECTION_STATUS:
                                responseConnectionStatus(awsIotMqttId,awsIotMqttCallbacks.get(awsIotMqttId).getStatusConnection());
                                break;
                        }
                    }
                }
            }catch (Exception e)
            {
                Log.e(TAG,"onStartCommand::",e);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onAwsIotMqttPubSubStatus(String awsIotMqttId, String topic, AwsIotMqttPubSubStatus awsIotMqttPubSubStatus) {
        responsePubSubStatus(awsIotMqttId,topic, awsIotMqttPubSubStatus);
    }

    @Override
    public void onAWSIotMqttStatusChange(String awsIotMqttId, AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus status) {
        responseConnectionStatus(awsIotMqttId,status);
    }

    @Override
    public void onMessageArrived(String awsIotMqttId, String topic, byte[] data) {
        responseMessageReceiver(awsIotMqttId,topic,data);
    }

    private void createAwsIotMqttCallback(String awsIotMqttId, Intent intent)
    {
        if(!awsIotMqttCallbacks.containsKey(awsIotMqttId))
        {
            AwsIotMqttConfig awsIotMqttConfig = intent.getParcelableExtra(EXTRA_AWS_IOT_MQTT_CONFIG);
            if(awsIotMqttConfig != null)
            {
                AwsIotMqttCallback awsIotMqttCallback = new AwsIotMqttCallback(awsIotMqttId,this,this,awsIotMqttConfig);
                awsIotMqttCallback.connect();
                awsIotMqttCallbacks.put(awsIotMqttId, awsIotMqttCallback);
            }
        }
    }

    private void publishMessage(String awsIotMqttId, String topic, String message)
    {
        awsIotMqttCallbacks.get(awsIotMqttId).publish(topic,message);
    }

    private void subcribe(String awsIotMqttId, String topic)
    {
        awsIotMqttCallbacks.get(awsIotMqttId).subscribeTopic(topic);
    }

    private void unSubcribe(String awsIotMqttId, String topic)
    {
        awsIotMqttCallbacks.get(awsIotMqttId).unSubcribeTopoc(topic);
    }

    private void responseMessageReceiver(String awsIotMqttId, String topic, byte[] datas)
    {
        Intent intent = getIntentResponse(awsIotMqttId,AwsIoTMqttServiceResponse.MesssageReceiver);
        intent.putExtra(EXTRA_AWS_IOT_MQTT_DATA_BYTES, datas);
        intent.putExtra(EXTRA_AWS_IOT_MQTT_TOPIC, topic);
        sendBroadcast(intent);
    }

    private void responseConnectionStatus(String awsIotMqttId, AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus status)
    {
        Intent intent = getIntentResponse(awsIotMqttId,AwsIoTMqttServiceResponse.ConnectionStatus);
        intent.putExtra(EXTRA_AWS_IOT_MQTT_CONNECTION_STATUS, status);
        sendBroadcast(intent);
    }

    private void responsePubSubStatus(String awsIotMqttId, String topic, AwsIotMqttPubSubStatus awsIotMqttPubSubStatus)
    {
        Intent intent = getIntentResponse(awsIotMqttId,AwsIoTMqttServiceResponse.PubSubStatus);
        intent.putExtra(EXTRA_AWS_IOT_MQTT_PUBSUB_STATUS, awsIotMqttPubSubStatus);
        intent.putExtra(EXTRA_AWS_IOT_MQTT_TOPIC, topic);
        sendBroadcast(intent);
    }

    private Intent getIntentResponse(String awsIotMqttId,AwsIoTMqttServiceResponse awsIoTMqttServiceResponse)
    {
        Intent intentRes = new Intent(SERVICE_FILTER_INTENT+awsIotMqttId);
        intentRes.putExtra(EXTRA_AWS_IOT_MQTT_RESPONSE_TYPE, awsIoTMqttServiceResponse);
        return intentRes;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
