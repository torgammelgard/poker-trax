package com.example.torgammelgard.pokerhourly;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.torgammelgard.pokerhourly.Adapters.TabsPagerAdapter;

public class Tab_main_fragactivity extends FragmentActivity {

    private ViewPager viewPager = null;
    private ActionBar actionBar;
    private TabsPagerAdapter mAdapter = null;
    private String[] tab_names = {"Main", "Sessions", "Summary"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);

        viewPager = (ViewPager)findViewById(R.id.pager);
        actionBar = getActionBar();

        actionBar.setNavigationMode(actionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(false);

        mAdapter = new TabsPagerAdapter(this, viewPager);
        mAdapter.addTab(actionBar.newTab().setText(tab_names[0]));
        mAdapter.addTab(actionBar.newTab().setText(tab_names[1]));
        mAdapter.addTab(actionBar.newTab().setText(tab_names[2]));
    }

    public void addSessionOnClick(View view) {

    }
}
