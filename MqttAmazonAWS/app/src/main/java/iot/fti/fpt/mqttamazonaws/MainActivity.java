package iot.fti.fpt.mqttamazonaws;

import android.app.Activity;
import android.os.Bundle;

import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttCallback;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AwsIotMqttCallback awsIotMqttCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
