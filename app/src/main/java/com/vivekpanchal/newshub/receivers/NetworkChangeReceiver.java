package com.vivekpanchal.newshub.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vivekpanchal.newshub.utility.Constants;
import com.vivekpanchal.newshub.utility.Utility;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private NetworkStateListener listener;

    public void setNetworkStateListener(NetworkStateListener networkStateListener){
        listener = networkStateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String intentAction = intent.getAction();
        if(intentAction != null && intent.getAction().equals(Constants.CONNECTIVITY_ACTION)){

            listener.networkStateConnected(Utility.checkInternetConnection(context));
        }

    }

    public interface NetworkStateListener{
        void networkStateConnected(boolean status);
    }
}
