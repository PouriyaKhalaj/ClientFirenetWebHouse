package com.ifirenet.clientfirenetwebhouse.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.ifirenet.clientfirenetwebhouse.Fragments.LoginFragment;
import com.ifirenet.clientfirenetwebhouse.R;

public class IntroActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentListener{

    public static LinearLayout ll_menu;
    private Class fragmentClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        fragmentClass = LoginFragment.class;
        SetFragment();

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

    @Override
    public void onLoginFragment(boolean isLogin) {
        if (isLogin){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}

