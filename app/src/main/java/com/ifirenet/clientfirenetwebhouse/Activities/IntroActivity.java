package com.ifirenet.clientfirenetwebhouse.Activities;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.ifirenet.clientfirenetwebhouse.Fragments.LoginFragment;
import com.ifirenet.clientfirenetwebhouse.R;
import com.ifirenet.clientfirenetwebhouse.Utils.Update.Version;
import com.ifirenet.clientfirenetwebhouse.Utils.Update.VersionList;
import com.ifirenet.clientfirenetwebhouse.Utils.Urls;
import com.ifirenet.clientfirenetwebhouse.Utils.UserInfo;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IntroActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentListener {

    public static LinearLayout ll_menu;
    private Class fragmentClass = null;
    private static String STEP_FINISH = "finish";
    private static String STEP_UPDATE = "update";
    private static String STEP_ERROR_APP = "errorApp";
    VersionList versionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        checkApkVersion();

        fragmentClass = LoginFragment.class;
        SetFragment();
    }

    private void checkApkVersion() {
        String fullUrl = Urls.baseURL + Urls.checkVersionURL;
        Ion.with(this)
                .load(fullUrl)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        boolean isVer = false;
                        if (e != null) {
                            showAlertDialog(STEP_ERROR_APP, "بروز رسانی برنامه", "در بروز برنامه خطایی رخ داده است، تا دقایقی دیگر مجددا وارد برنامه شوید.", "تلاش مجدد", "بستن برنامه");
                            return;
                        }
                        if (result.getHeaders().code() == 200) {
                            versionList = setVersionList(result.getResult());

                            try {
                                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

                                for (int i = 0; i < versionList.versionList.size(); i++) {
                                    Version version = versionList.versionList.get(i);
                                    if (pInfo.versionName.equals(version.versionName)) {
                                        isVer = true;
                                        break;
                                    }
                                }
                                String text = "برای به روز رسانی از نسخه "  + pInfo.versionName + " به نسخه "  + versionList.versionList.get(0).versionName + " آماده اید؟ ";
                                if (isVer != true) {
                                    showAlertDialog(STEP_UPDATE, "بروز رسانی برنامه", text, "بروز رسانی", "بستن برنامه");
                                    return;
                                } else return;

                            } catch (PackageManager.NameNotFoundException e1) {
                                e1.printStackTrace();
                            }
                        }

                        if (isVer != true) {
                            showAlertDialog(STEP_ERROR_APP, "بروز رسانی برنامه", "خطا در بررسی نسخه فعلی برنامه", "تلاش مجدد", "بستن برنامه");
                        }
                    }
                });
    }

    private VersionList setVersionList(String json_str) {
        Gson gson = new Gson();
        VersionList versionList = gson.fromJson(json_str, VersionList.class);
        return versionList;
    }

    private void SetFragment() {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ll_container_main, fragment).commit();
    }

    public void showAlertDialog(final String STEP, String title, String text, String yesSubmitText, String noSubmitText) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_popup_public);
        final TextView txt_title = (TextView) dialog.findViewById(R.id.txt_public_dialog_title);
        final TextView txt_text = (TextView) dialog.findViewById(R.id.txt_public_dialog_text);
        final TextView txt_yes_submit = (TextView) dialog.findViewById(R.id.txt_public_dialog_yes_submit_text);
        final TextView txt_no_submit = (TextView) dialog.findViewById(R.id.txt_public_dialog_no_submit_text);
        FrameLayout fl_accept_submit = (FrameLayout) dialog.findViewById(R.id.fl_public_dialog_accept_submit);
        FrameLayout fl_unAccept_submit = (FrameLayout) dialog.findViewById(R.id.fl_public_dialog_un_accept_submit);

        txt_title.setText(title);
        txt_text.setText(text);
        txt_yes_submit.setText(yesSubmitText);
        txt_no_submit.setText(noSubmitText);

        fl_accept_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (STEP.equals(STEP_FINISH)) {
                    finish();

                } else if (STEP.equals(STEP_ERROR_APP)) {
                    checkApkVersion();

                } else if (STEP.equals(STEP_UPDATE)) {
                    downloadApp();

                }
            }
        });
        fl_unAccept_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (STEP.equals(STEP_FINISH)) {
                } else if (STEP.equals(STEP_ERROR_APP)) {
                    finish();

                } else if (STEP.equals(STEP_UPDATE)) {
                    finish();

                }
            }
        });
        dialog.show();
    }

    private void downloadApp(){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionList.apkLink));
        request.setDescription("ifirenet.com");
        request.setTitle("Web House Client Portal");
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.ext");

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        finish();
    }

    @Override
    public void onLoginFragment(boolean isLogin, UserInfo userInfo) {
        if (isLogin) {

            Gson gson = new Gson();
            String myJson = gson.toJson(userInfo);

            Intent i = new Intent(this, MainActivity.class);
            i.putExtra(MainActivity.BUNDLE_KEY, myJson);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(STEP_FINISH, "بستن برنامه", "آیا از بستن برنامه مطمئن هستید؟", "بستن برنامه", "انصراف");
    }
}

