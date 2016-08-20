package com.ifirenet.clientfirenetwebhouse.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.ObbInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ifirenet.clientfirenetwebhouse.Services.MyService;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

/**
 * Created by Pouriya.kh on 8/17/2016.
 */
public class PublicClass {
    private Context context;

    public PublicClass(Context context) {
        this.context = context;
    }

    public Object useIon (String url, JsonObject header){
        final Object[] o = new Object[1];
        Ion.with(context)
                .load(url)
            //    .setHeader(header.get("0"), header.g[1])
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        o[0] = e;
                        if (e == null){
                            o[0] = result;
                        }
                    }
                });
        return o;
    }

    public boolean isConnection(){
        /*
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            if (info.isConnected()) {
                //is Connection
                return true;
            }
            else {
                //is not Connection
                return false;
            }
        } else return false;*/
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    public void showToast (String msg)  {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
