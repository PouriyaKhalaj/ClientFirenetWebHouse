package com.ifirenet.clientfirenetwebhouse.Activities;

import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ifirenet.clientfirenetwebhouse.Fragments.DetailTicketFragment;
import com.ifirenet.clientfirenetwebhouse.Fragments.LoginFragment;
import com.ifirenet.clientfirenetwebhouse.Fragments.ProfileFragment;
import com.ifirenet.clientfirenetwebhouse.Fragments.SupportTicketFragment;
import com.ifirenet.clientfirenetwebhouse.Fragments.CustomerTicketFragment;
import com.ifirenet.clientfirenetwebhouse.R;
import com.ifirenet.clientfirenetwebhouse.Utils.Keys;
import com.ifirenet.clientfirenetwebhouse.Utils.PublicClass;
import com.ifirenet.clientfirenetwebhouse.Utils.SharedPreference;
import com.ifirenet.clientfirenetwebhouse.Utils.Urls;
import com.ifirenet.clientfirenetwebhouse.Utils.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomerTicketFragment.OnCustomerTicketFragmentListener,SupportTicketFragment.OnSupportTicketFragmentListener,
        ProfileFragment.onProfileFragmentListener, LoginFragment.OnLoginFragmentListener, DetailTicketFragment.OnDetailTicketListener{
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
    PublicClass publicClass;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preference = new SharedPreference(this);
        publicClass = new PublicClass(this);

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
        initHeaderNav();

        View logo = findViewById(R.id.include_logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(nvDrawer);
            }
        });

        switchTicketFragment();
    }
    private void initHeaderNav(){
        // Inflate the header view at runtime
        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header);
        // We can now look up items within the header if needed
        TextView txt_header_login = (TextView) headerLayout.findViewById(R.id.txt_view_user_name_or_login);

        Gson gson = new Gson();
        user = gson.fromJson(preference.Get(Keys.PREF_APP, Keys.User), User.class);
        txt_header_login.setText(user.username);

        Resources res = getResources();
        int color = res.getColor(R.color.green);
        txt_header_login.setTextColor(color);
        ImageView img_avatar = (ImageView) headerLayout.findViewById(R.id.img_drawable_header_avatar) ;

        int imgSize = (int) res.getDimension(R.dimen.img_header_avatar_size);
        Picasso.with(this)
                .load(Urls.baseURL + user.avatar)
                .resize(imgSize, imgSize)
                .centerCrop()
                .placeholder(R.drawable.ic_avatar_person)
                .error(R.drawable.ic_avatar_person)
                .into(img_avatar);

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
                switchTicketFragment();
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

    private void switchTicketFragment(){
        if (user.role.equals("Customer"))
            fragmentClass = CustomerTicketFragment.class;
        else if (user.role.equals("TicketingManager"))
            fragmentClass = SupportTicketFragment.class;
        SetFragment(user.id);
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
        fragmentManager.beginTransaction()
                .replace(R.id.ll_container_main, fragment)
                .addToBackStack(null)
                .commit();
    }
    private void SetFragment(int id) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            Bundle bundle = new Bundle();
            if (fragment instanceof DetailTicketFragment) {
                bundle.putInt(DetailTicketFragment.ARG_NodeId, id);
                bundle.putInt(DetailTicketFragment.ARG_UserID, user.id);
            }
            else if (fragment instanceof SupportTicketFragment)
                bundle.putInt(SupportTicketFragment.ARG_USER_ID, id);
            else if (fragment instanceof CustomerTicketFragment)
                bundle.putInt(CustomerTicketFragment.ARG_USER_ID, id);
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.ll_container_main, fragment)
                .addToBackStack(null)
                .commit();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    public void showAlertDialog(String title, String text, String yesSubmitText, String noSubmitText){
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
                finish();
            }
        });
        fl_unAccept_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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

    @Override
    public void onCustomerTicket(int nodeId) {
        fragmentClass = DetailTicketFragment.class;
        SetFragment(nodeId);
    }

    @Override
    public void onSupportTicket(int nodeId) {
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
/*    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            showAlertDialog("بستن برنامه", "آیا از بستن برنامه مطمئن هستید؟", "بستن برنامه", "انصراف");
        } else {
            super.onBackPressed();
        }
    }
*/
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        Fragment lastFrag = getLastNotNull(frags);
        //nothing else in back stack || nothing in back stack is instance of our interface
        if (count > 1 ) {
            super.onBackPressed();
        } else {
            showAlertDialog("بستن برنامه", "آیا از بستن برنامه مطمئن هستید؟", "بستن برنامه", "انصراف");
        }
    }

    private Fragment getLastNotNull(List<Fragment> list){
        for (int i= list.size()-1;i>=0;i--){
            Fragment frag = list.get(i);
            if (frag != null){
                return frag;
            }
        }
        return null;
    }

}
