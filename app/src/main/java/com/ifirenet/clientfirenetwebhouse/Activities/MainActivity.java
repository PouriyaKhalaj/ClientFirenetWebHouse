package com.ifirenet.clientfirenetwebhouse.Activities;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ifirenet.clientfirenetwebhouse.Fragments.DetailTicketFragment;
import com.ifirenet.clientfirenetwebhouse.Fragments.LoginFragment;
import com.ifirenet.clientfirenetwebhouse.Fragments.ProfileFragment;
import com.ifirenet.clientfirenetwebhouse.Fragments.TicketListFragment;
import com.ifirenet.clientfirenetwebhouse.R;
import com.ifirenet.clientfirenetwebhouse.Utils.DetailTicket;
import com.ifirenet.clientfirenetwebhouse.Utils.Keys;
import com.ifirenet.clientfirenetwebhouse.Utils.SharedPreference;
import com.ifirenet.clientfirenetwebhouse.Utils.User;

public class MainActivity extends AppCompatActivity implements TicketListFragment.OnQuestionFragmentListener, ProfileFragment.onProfileFragmentListener
        ,LoginFragment.OnLoginFragmentListener, DetailTicketFragment.OnDetailTicketListener{
    public static final String FRAGMENT_NAME_KEY = "FragmentName";
    public static final String BUNDLE_KEY = "BundleKey";
    public static final String TAG = "LOG";
    public static Toolbar mToolbar;
    public static LinearLayout ll_menu;
    private Class fragmentClass = null;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    SharedPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preference = new SharedPreference(this);

        // Set a Toolbar to replace the ActionBar.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        //nvDrawer.getMenu().getItem(0).setChecked(true);
        setupDrawerContent(nvDrawer);

        fragmentClass = TicketListFragment.class;
        SetFragment();

        initHeaderNav();


        View logo = findViewById(R.id.include_logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(nvDrawer);
            }
        });

    }
    private void initHeaderNav(){
        // Inflate the header view at runtime
        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header);
        // We can now look up items within the header if needed
        TextView txt_header_login = (TextView) headerLayout.findViewById(R.id.txt_view_user_name_or_login);

        Gson gson = new Gson();
        User user = gson.fromJson(preference.Get(Keys.PREF_APP, Keys.User), User.class);
        txt_header_login.setText(user.username);

        Resources res = getResources();
        int color = res.getColor(R.color.green);
        txt_header_login.setTextColor(color);

        txt_header_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentClass = ProfileFragment.class;
                SetFragment();
                setTitle("پروفایل");
                mDrawer.closeDrawers();
            }
        });

    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

    }

    public void selectDrawerItem(MenuItem menuItem) {
        Log.d("Menu Item > ", menuItem.getItemId()+"");
        // Create a new fragment and specify the planet to show based on
        // position
        switch (menuItem.getItemId()) {
            case R.id.counter:
                break;
            case R.id.profile:
                fragmentClass = ProfileFragment.class;
                SetFragment();
                break;
            case R.id.ticketing:
                fragmentClass = TicketListFragment.class;
                SetFragment();
                break;
            case R.id.questions:
                break;
            case R.id.help:
                break;
            case R.id.regulation:
                break;
            case R.id.contacts:
                break;
            default:

        }

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private void _Toast (String msg)  {
        Toast toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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
    private void SetFragment(int nodeId) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt(DetailTicketFragment.ARG_NodeId, nodeId);
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ll_container_main, fragment).commit();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                Toast.makeText(getApplicationContext(), "Search button clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
        }*/
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fragmentClass.equals(TicketListFragment.class)) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public void onQuestionDetail(int nodeId) {
        fragmentClass = DetailTicketFragment.class;
        SetFragment(nodeId);
    }

    @Override
    public void onProfileFragment() {

    }

    @Override
    public void onLoginFragment(boolean isLogin) {

    }

    @Override
    public void onDetailTicket() {

    }
}
