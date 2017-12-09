package iot.fti.fpt.awsiotmqttandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.regions.Regions;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttCallback;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttCallbackListener;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttConfig;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttPubSubStatus;

public class ActivityAwsMqtt extends AppCompatActivity implements AwsIotMqttCallbackListener {

    private AwsIotMqttConfig awsIotMqttConfig;
    private AwsIotMqttCallback awsIotMqttCallback;
    private boolean firsttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aws_mqtt);
        firsttime = true;


        awsIotMqttConfig = new AwsIotMqttConfig(
                "a1x6wmsxy5q7hn.iot.us-east-1.amazonaws.com",
                "us-east-1:de32b928-5e20-42dc-8b5c-b47562960c78",
                "iot-starter-policy",
                Regions.US_EAST_1.toString(),
                "keystorename",
                "keystorepassword",
                "keycertid",1);

        awsIotMqttCallback = new AwsIotMqttCallback("clientId",this,this,awsIotMqttConfig);

    }


    @Override
    public void onAwsIotMqttPubSubStatus(String clientId, String topic, AwsIotMqttPubSubStatus awsIotMqttPubSubStatus) {

    }

    @Override
    public void onAWSIotMqttStatusChange(String clientId, AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus status) {
        Log.d(this.getClass().getSimpleName(),"onAWSIotMqttStatusChange::"+status);
    }

    @Override
    public void onMessageArrived(String clientId, String topic, byte[] data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        awsIotMqttCallback.disconnect();
        firsttime=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firsttime)
        {
            firsttime = false;
            awsIotMqttCallback.connect();
        }

    }
}
