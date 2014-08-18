package com.example.torgammelgard.pokerhourly;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.torgammelgard.pokerhourly.Adapters.TabsPagerAdapter;

import java.util.List;

public class Tab_main_fragactivity extends FragmentActivity implements
        AskForUserNameDialogFragment.NoticeDialogListener {

    protected String userName;
    protected Menu mMenu;
    protected DataSource dataSource;
    private SharedPreferences mPrefs;
    private ViewPager viewPager = null;
    private ActionBar actionBar;
    private TabsPagerAdapter mAdapter = null;
    private String[] tab_names = {"Main", "Sessions", "Summary"};
    private Session sessionToAdd;

    static final int ADD_SESSION_REQUEST = 1;

    /*getter*/
    public DataSource getDataSource() {
        return dataSource;
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
        assert actionBar != null;                                       //TODO
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
        try {
            dataSource = new DataSource(this);
            dataSource.open(); //try catch SQLException here

        } catch (SQLException e){}

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
        try {
            dataSource.open();
            if (sessionToAdd != null) {
                addSession(sessionToAdd);
                sessionToAdd = null;
            }
        } catch (SQLException e){}
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }



    private void askForUserName() {
        AskForUserNameDialogFragment dialog = new AskForUserNameDialogFragment();
        dialog.show(getFragmentManager(), "usernamedialog");
    }

    public void addSessionOnClick(View view) {
        // new AddSessionDialogFragment().show(getFragmentManager(), "addsession");
        Intent intent = new Intent(this, AddSessionActivity.class);
        //intent.putStringArrayListExtra("gametypeslist", dataSource.getAllGameTypes());
        //intent.putStringArrayListExtra("gamestructurelist", dataSource.getAllGameStructures());
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
                sessionToAdd = (Session) data.getSerializableExtra("session");
                break;
            default: break;
        }
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

    public void addSession(Session session) {
        dataSource.addSession(session);
        List<Fragment> fragmentlist = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentlist) {
            if (fragment instanceof ResultsFragment)
                ((ResultsFragment) fragment).updateListView(fragment.getView());
        }
    }
}
