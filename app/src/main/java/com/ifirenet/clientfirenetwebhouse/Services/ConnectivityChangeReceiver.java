package com.ifirenet.clientfirenetwebhouse.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.ifirenet.clientfirenetwebhouse.Utils.NetworkUtil;

public class ConnectivityChangeReceiver extends BroadcastReceiver {
    public ConnectivityChangeReceiver() {
    }

    /*@Override
    public void onReceive(Context context, Intent intent) {

        // Explicitly specify that which service class will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                MyService.class.getName());
        intent.putExtra("isNetworkConnected",isConnected(context));
        context.startService((intent.setComponent(comp)));
    }*/
    @Override
    public void onReceive(Context context, Intent intent) {
        //here, check that the network connection is available. If yes, start your service. If not, stop your service.
       /* ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            if (info.isConnected()) {
                //start service
                Intent intent1 = new Intent(context, MyService.class);
                context.startService(intent1);
            }
            else {
                //stop service
                Intent intent1 = new Intent(context, MyService.class);
                context.stopService(intent1);
            }
        }*/
        String status = NetworkUtil.getConnectivityStatusString(context);

        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }

    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

}
