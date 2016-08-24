package com.ifirenet.clientfirenetwebhouse.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
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

    public void showSnackBar(String msg, final View coordinatorLayout){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("قبول", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Message is restored!", Snackbar.LENGTH_SHORT);
                      //  snackbar1.show();
                    }
                });

        snackbar.show();
    }

    public void hideKeyboard(EditText input, Activity activity){
        if (input != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }
    }
}
