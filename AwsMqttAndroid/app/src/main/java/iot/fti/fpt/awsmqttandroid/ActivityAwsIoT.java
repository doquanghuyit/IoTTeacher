package iot.fti.fpt.awsmqttandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.regions.Regions;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttCallback;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttCallbackListener;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttConfig;
import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttPubSubStatus;

public class ActivityAwsIoT extends AppCompatActivity implements AwsIotMqttCallbackListener {

    private AwsIotMqttConfig awsIotMqttConfig;
    private AwsIotMqttCallback awsIotMqttCallback;
    private boolean firsttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aws_io_t);
        firsttime = true;


        awsIotMqttConfig = new AwsIotMqttConfig(
                "",
                "",
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

