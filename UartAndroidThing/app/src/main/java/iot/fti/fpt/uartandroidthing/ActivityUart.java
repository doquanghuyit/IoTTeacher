package iot.fti.fpt.uartandroidthing;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;

import java.io.IOException;

public class ActivityUart extends Activity {
    private static final String TAG = ActivityUart.class.getSimpleName();
//    private static final int BAUD_RATE = 115200;

    
    // UART Configuration Parameters
    private static final int BAUD_RATE = 9600;
    private static final int DATA_BITS = 8;
    private static final int STOP_BITS = 1;
    private static final int CHUNK_SIZE = 1;


    private Handler handler;
    private Runnable readUartRunnable;
    private PeripheralManagerService mService;
    private UartDevice uartDevice;
    private UartDeviceCallback mCallback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void openUart(String name, int baudRate) throws IOException {
    }


    private void closeUart() throws IOException {
    }


    private void readUartDevice() {
    }


    private void writeUartDevice(byte[] dataWrite) {
    }


}
