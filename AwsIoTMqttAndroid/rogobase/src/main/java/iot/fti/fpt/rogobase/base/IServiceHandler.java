package iot.fti.fpt.rogobase.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import iot.fti.fpt.rogobase.utils.EnumCheck;

/**
 * Created by doquanghuy on 10/26/17.
 */

public abstract class IServiceHandler {
    private final String TAG="IServiceHandler";
    private class ServiceReceiver extends BroadcastReceiver {

        public ServiceReceiver()
        {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            onServiceResponseReceiver(intent);
        }
    }

    private ServiceReceiver serviceReceiver;
    private Context context;
    public IServiceHandler(Context context)
    {
        this.serviceReceiver = new ServiceReceiver();
        this.context = context;
    }


    protected abstract void onServiceResponseReceiver(Intent intent);
    protected abstract String getIntentFilter();
    protected abstract Class yourServiceClass();


    protected Intent getIntent(String action)
    {
        Intent intent = new Intent(context,yourServiceClass());
        intent.setAction(action);
        return intent;
    }

    protected Intent getIntent()
    {
        return new Intent(context,yourServiceClass());
    }



    public Context getContext()
    {
        return context;
    }

    public void registerReceiver()
    {

        try
        {
            String intentFilter = getIntentFilter();
            Log.d(TAG,"registerReceiver::"+intentFilter);
            context.registerReceiver(serviceReceiver, new IntentFilter(intentFilter));
        }catch (Exception e)
        {
            Log.i(this.getClass().getSimpleName(),"registerReceiver exception::"+e.getMessage());
        }
    }

    public void unregisterReceiver()
    {
        Log.i(TAG,"unregisterReceiver");
        try
        {
            context.unregisterReceiver(serviceReceiver);
        }catch (Exception e)
        {
            Log.i(TAG,"unregisterReceiver exception::"+e.getMessage());
        }
    }

}
