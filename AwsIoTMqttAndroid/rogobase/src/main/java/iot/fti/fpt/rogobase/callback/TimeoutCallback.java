package iot.fti.fpt.rogobase.callback;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by doquanghuy on 5/17/17.
 */

public class TimeoutCallback {
    public interface OnTimeoutCallbackListener
    {
        void endTimeout(int requestCode);
    }

    private OnTimeoutCallbackListener listenerTimeout;
    private Runnable runnable;
    private Handler handler;
    private int requestCode;
    private boolean running;
    private String LOG_TAG;
    public TimeoutCallback(OnTimeoutCallbackListener listener)
    {
        this(listener,"TimeoutCallback");
    }

    public TimeoutCallback(OnTimeoutCallbackListener listener, String LOG_TAG)
    {
        this.requestCode = -1;
        this.listenerTimeout=listener;
        handler = new Handler(Looper.getMainLooper());
        this.running = false;
        this.LOG_TAG = LOG_TAG+" TimeoutCallback";

        runnable = new Runnable() {
            @Override
            public void run() {
                running = false;
                Log.d(getLOG_TAG(),"endTimeout");
                if(listenerTimeout!=null)
                    listenerTimeout.endTimeout(requestCode);
            }
        };
    }

    public void startTimeOut(long timeout,int requestCode)
    {
        this.requestCode = requestCode;
        running = true;
        try
        {
            Log.d(LOG_TAG,"startTimeOut::"+timeout+":: RequestCode::"+requestCode);
            handler.removeCallbacks(runnable);
        }catch (Exception e)
        {

        }
        handler.postDelayed(runnable,timeout);
    }

    public void stopTimeOut()
    {
        Log.d(this.getClass().getSimpleName()+"::"+LOG_TAG,"stopTimeOut::RequestCode::"+requestCode);
        this.requestCode = -1;
        running = false;
        try
        {
            handler.removeCallbacks(runnable);
        }catch (Exception e)
        {

        }
    }

    public String getLOG_TAG() {
        return LOG_TAG;
    }

    public boolean isRunning() {
        return running;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
