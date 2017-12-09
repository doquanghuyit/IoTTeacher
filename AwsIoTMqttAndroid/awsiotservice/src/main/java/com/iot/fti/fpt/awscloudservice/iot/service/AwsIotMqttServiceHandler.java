package com.iot.fti.fpt.awscloudservice.iot.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttCallbackListener;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttConfig;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttPubSubStatus;

import iot.fti.fpt.rogobase.base.IServiceHandler;

/**
 * Created by doquanghuy on 10/26/17.
 */

public class AwsIotMqttServiceHandler extends IServiceHandler {
    private final String TAG ="IotMqttServiceHandler";

    private AwsIotMqttCallbackListener awsIotMqttCallbackListener;
    private String idService;
    private AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus status;

    public AwsIotMqttServiceHandler(String idService, Context context, AwsIotMqttCallbackListener awsIotMqttCallbackListener) {
        super(context);
        this.idService = idService;
        this.awsIotMqttCallbackListener = awsIotMqttCallbackListener;
        this.status = AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.ConnectionLost;
    }

    public AwsIotMqttServiceHandler(String idService, Context context, AwsIotMqttCallbackListener awsIotMqttCallbackListener,AwsIotMqttConfig awsIotMqttConfig) {
        super(context);
        this.idService = idService;
        this.awsIotMqttCallbackListener = awsIotMqttCallbackListener;
        this.status = AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.ConnectionLost;
        initService(context,idService,awsIotMqttConfig);
    }


    public static void initService(Context context,String idService, AwsIotMqttConfig awsIotMqttConfig)
    {
        Intent intent = new Intent(context,AwsIotMqttService.class);
        intent.setAction(AwsIotMqttServiceAction.CREATE.toString());
        intent.putExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_ID,idService);
        intent.putExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_CONFIG,awsIotMqttConfig);
        context.startService(intent);
    }

    @Override
    protected void onServiceResponseReceiver(Intent intent) {

        try
        {
            AwsIotMqttService.AwsIoTMqttServiceResponse awsIoTMqttServiceResponse = (AwsIotMqttService.AwsIoTMqttServiceResponse) intent.getSerializableExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_RESPONSE_TYPE);
            switch (awsIoTMqttServiceResponse)
            {
                case ConnectionStatus:
                    status = (AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus) intent.getSerializableExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_CONNECTION_STATUS);
                    if(awsIotMqttCallbackListener != null)
                        awsIotMqttCallbackListener.onAWSIotMqttStatusChange(idService,status);
                    break;
                case MesssageReceiver:
                    String topicReceiver = intent.getStringExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_TOPIC);
                    byte[] data = intent.getByteArrayExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_DATA_BYTES);
                    if(awsIotMqttCallbackListener != null)
                        awsIotMqttCallbackListener.onMessageArrived(idService,topicReceiver,data);
                    break;
                case PubSubStatus:
                    String topicChangeStatus = intent.getStringExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_TOPIC);
                    AwsIotMqttPubSubStatus awsIotMqttPubSubStatus = (AwsIotMqttPubSubStatus) intent.getSerializableExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_PUBSUB_STATUS);
                    if(awsIotMqttCallbackListener != null)
                        awsIotMqttCallbackListener.onAwsIotMqttPubSubStatus(idService,topicChangeStatus, awsIotMqttPubSubStatus);
                    break;
            }
        }catch (Exception e)
        {
            Log.e(TAG,"onServiceResponseReceiver",e);
        }
    }


    @Override
    protected String getIntentFilter() {
        return AwsIotMqttService.SERVICE_FILTER_INTENT+idService;
    }

    @Override
    protected Class yourServiceClass() {
        return AwsIotMqttService.class;
    }

    private void createAwsIotCallback(AwsIotMqttConfig awsIotMqttConfig)
    {
        Intent intent = getIntent(AwsIotMqttServiceAction.CREATE.toString());
        intent.putExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_ID,idService);
        intent.putExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_CONFIG,awsIotMqttConfig);
        getContext().startService(intent);
    }

    public boolean isConnected()
    {
        return status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected;
    }

    public void publish(String topic,String message)
    {
        Intent intent = getIntent(AwsIotMqttServiceAction.PUBLISH.toString());
        intent.putExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_ID,idService);
        intent.putExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_TOPIC,topic);
        intent.putExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_MESSAGE,message);
        getContext().startService(intent);
    }

    public void subcribe(String topic)
    {
        Intent intent = getIntent(AwsIotMqttServiceAction.SUBCRIBE.toString());
        intent.putExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_ID,idService);
        intent.putExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_TOPIC,topic);
        getContext().startService(intent);
    }

    public void unSubcribe(String topic)
    {
        Intent intent = getIntent(AwsIotMqttServiceAction.SUBCRIBE.toString());
        intent.putExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_ID,idService);
        intent.putExtra(AwsIotMqttService.EXTRA_AWS_IOT_MQTT_TOPIC,topic);
        getContext().startService(intent);
    }
}
