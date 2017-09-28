package com.pangaj.shruthi.newprojectimportsetup.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pangaj.shruthi.newprojectimportsetup.NPApplication;
import com.pangaj.shruthi.newprojectimportsetup.NPPreferences;
import com.pangaj.shruthi.newprojectimportsetup.R;
import com.pangaj.shruthi.newprojectimportsetup.fragments.NPFamilyFragment;
import com.pangaj.shruthi.newprojectimportsetup.fragments.NPHomeFragment;
import com.pangaj.shruthi.newprojectimportsetup.fragments.NPSettingsFragment;
import com.pangaj.shruthi.newprojectimportsetup.fragments.NPWorkFragment;

/**
 * Created by pangaj on 23/09/17.
 */
public class NPNavigationActivity extends NPBaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private int selectedMenuItemId;
    private NPPreferences mPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.np_activity_navigation);

        setToolBar();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {    //may produce null pointer exception, if the actionBar is null
            actionBar.setDisplayHomeAsUpEnabled(false);     //hide the home icon - back button
        }
        mPref = NPApplication.getInstance().getPrefs();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //set navigation header view
        View headerView = navigationView.inflateHeaderView(R.layout.np_menu_header);
        TextView tvUserEmail = headerView.findViewById(R.id.tv_email);
        tvUserEmail.setText(mPref.getLoginEmail());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //initialise batch notification for request - pangaj
//        TextView tvPressureMeter = (TextView) navigationView.getMenu().getItem(R.id.nav_work).getActionView();
//        tvPressureMeter.setText(R.string.ninty_nine_plus);

        navigationView.setNavigationItemSelectedListener(this);
        loadInitialFragment();
    }

    private void loadInitialFragment() {
        selectedMenuItemId = R.id.nav_home;
        navigationView.getMenu().getItem(0).setChecked(true);
        Fragment fragment = NPHomeFragment.newInstance();
        replaceFragment(NPNavigationActivity.this, fragment, false, R.id.fragment_container);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        if (item.getItemId() != R.id.nav_logout) {
            selectedMenuItemId = item.getItemId();
            drawerLayout.closeDrawers();
        }

        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.home:
                fragment = new NPHomeFragment();
                break;
            case R.id.nav_work:
                fragment = new NPWorkFragment();
                break;
            case R.id.nav_family:
                fragment = new NPFamilyFragment();
                break;
            case R.id.nav_settings:
                fragment = new NPSettingsFragment();
                break;
            case R.id.nav_logout:
                showAlertDialog(getString(R.string.logout), getString(R.string.logout_msg), true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPref.clearData();
                    }
                }, null);
                return true;
            default:
                return true;
        }
        replaceFragment(NPNavigationActivity.this, fragment, false, R.id.fragment_container);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}