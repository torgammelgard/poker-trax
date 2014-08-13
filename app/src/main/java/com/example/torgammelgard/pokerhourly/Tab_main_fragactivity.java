package com.example.torgammelgard.pokerhourly;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.torgammelgard.pokerhourly.Adapters.TabsPagerAdapter;

import java.util.List;

public class Tab_main_fragactivity extends FragmentActivity implements
        AskForUserNameDialogFragment.NoticeDialogListener,
        AddSessionDialogFragment.AddSessionDialogListener {

    protected String userName;
    protected Menu mMenu;
    protected SessionsDAO sessionsDAO;
    private SharedPreferences mPrefs;
    private ViewPager viewPager = null;
    private ActionBar actionBar;
    private TabsPagerAdapter mAdapter = null;
    private String[] tab_names = {"Main", "Sessions", "Summary"};

    /*getter*/
    public SessionsDAO getSessionsDAO() {
        return sessionsDAO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);
        //ask for account or get the account from last saved state
        mPrefs = getSharedPreferences("mPrefs", 0);
        userName = mPrefs.getString("username", "");

        if (userName.equals("")) {
            askForUserName();
        }

        viewPager = (ViewPager)findViewById(R.id.pager);
        actionBar = getActionBar();
        actionBar.setTitle("ActionbarTitle");
        actionBar.setSubtitle("Subtitle");
        actionBar.setNavigationMode(actionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(false);
        //test

        mAdapter = new TabsPagerAdapter(this, viewPager);
        mAdapter.addTab(actionBar.newTab().setText(tab_names[0]));
        mAdapter.addTab(actionBar.newTab().setText(tab_names[1]));
        mAdapter.addTab(actionBar.newTab().setText(tab_names[2]));

        /*Database init*/
        sessionsDAO = new SessionsDAO(this);
        sessionsDAO.open(); //try catch SQLException here

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
        sessionsDAO.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        sessionsDAO.close();
        super.onPause();
    }



    private void askForUserName() {
        AskForUserNameDialogFragment dialog = new AskForUserNameDialogFragment();
        dialog.show(getFragmentManager(), "usernamedialog");
    }

    public void addSessionOnClick(View view) {
        new AddSessionDialogFragment().show(getFragmentManager(), "addsession");
    }

    /*Impl AskForUserNameDialogFragment.NoticeDialogListener*/
    @Override
    public void onDialogPositiveCheck(AskForUserNameDialogFragment dialog) {
        if (dialog.username.equals("")) {
            askForUserName();
            return;
        }
        else {
            userName = dialog.username;
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("username", userName);
            editor.apply();
            ((TextView)findViewById(R.id.welcomeTextView)).setText("User : " + userName);
        }
    }

    /*Impl AddSessionDialogFragment.AddSessionDialogListener*/
    @Override
    public void onDialogPositiveCheck(AddSessionDialogFragment addSessionDialogFragment) {
        //validate

        sessionsDAO.addSession(addSessionDialogFragment.mSession);
        List<Fragment> fragmentlist = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentlist) {
            if (fragment instanceof ResultsFragment)
                ((ResultsFragment) fragment).updateListView(fragment.getView());
        }
        //ListView listView = (ListView)findViewById(R.id.resultListView);
        //((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
    }
}
