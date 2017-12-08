package iot.fti.fpt.mqttamazonaws;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttCallback;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttCallbackListener;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttConfig;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttPubSubStatus;

public class MainActivity extends Activity
        implements AwsIotMqttCallbackListener {
    private static final String TAG = MainActivity.class.getSimpleName();


    private AwsIotMqttCallback awsIotMqttCallback;
    private AwsIotMqttConfig awsIotMqttConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        awsIotMqttConfig = new AwsIotMqttConfig("",
                "",
                "",
                "",
                "",
                "",
                "",
                1);
        awsIotMqttCallback = new AwsIotMqttCallback("clientid",
                this,this,awsIotMqttConfig);

    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        awsIotMqttCallback.connect();
    }
    @Override
    public void onAwsIotMqttPubSubStatus(String clientId,
                                         String topic,
                                         AwsIotMqttPubSubStatus awsIotMqttPubSubStatus) {

    }
    @Override
    public void onAWSIotMqttStatusChange(String clientId,
                                         AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus
                                                 status) {

        Log.d(TAG,"onAWSIotMqttStatusChange::"+status);
        switch (status)
        {
            case Connected:
                break;
            case Connecting:
                break;
            case Reconnecting:
                break;
            case ConnectionLost:
                break;
        }

    }

    @Override
    public void onMessageArrived(String clientId,
                                 String topic,
                                 byte[] data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        awsIotMqttCallback.disconnect();
    }

}
