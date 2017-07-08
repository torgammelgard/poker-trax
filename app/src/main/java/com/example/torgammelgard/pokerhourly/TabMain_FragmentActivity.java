package com.example.torgammelgard.pokerhourly;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.torgammelgard.pokerhourly.Adapters.TabsPagerAdapter;

import java.util.List;

public class TabMain_FragmentActivity extends FragmentActivity {

//    protected String mUserName;
    protected Menu mMenu;
//    private SharedPreferences mPrefs;
    private String[] tab_names = {"Main", "Sessions", "Graph", "Summary"};

    private static final int ADD_SESSION_REQUEST = 1;
    private static final String LOG = "TabMain_FragmentActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
            actionBar.setSubtitle(R.string.app_subtitle);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setHomeButtonEnabled(false);

            TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, viewPager);
            tabsPagerAdapter.addTab(actionBar.newTab().setText(tab_names[0]));
            tabsPagerAdapter.addTab(actionBar.newTab().setText(tab_names[1]));
            tabsPagerAdapter.addTab(actionBar.newTab().setText(tab_names[2]));
            tabsPagerAdapter.addTab(actionBar.newTab().setText(tab_names[3]));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.delete_session).setVisible(false);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateLists();
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            ((MainApp) getApplication()).mDataSource.close();
        } catch (NullPointerException e) {
            Log.d(LOG, "Data source is null");
        }
    }

   /** Starts an activity where the user can add a Session */
    public void addSessionOnClick(View view) {
        Intent intent = new Intent(this, AddSessionActivity.class);
        startActivityForResult(intent, ADD_SESSION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // note : onActivityResult is run before onResume
        if (resultCode == RESULT_CANCELED)
            return;
        switch (requestCode) {
            case ADD_SESSION_REQUEST :
                break;
            default: break;
        }
    }

    /** Updates the listView in the ResultsFragment */
    public void updateLists() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof ResultsFragment)
                    ((ResultsFragment) fragment).updateListView();
            }
        }
    }

}
