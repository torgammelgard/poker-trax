package com.example.torgammelgard.pokerhourly;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends FragmentActivity implements
        AskForUserNameDialogFragment.NoticeDialogListener {

    private static final String mLOGMESSAGE = "LOGMESSAGE";
    private SharedPreferences mPrefs;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //ask for account or get the account from last saved state
        mPrefs = getPreferences(Context.MODE_PRIVATE);
        userName = mPrefs.getString("username", "");

        if (userName.equals("")) {
            askForUserName();
        }

        update();
    }

    private void askForUserName() {
        AskForUserNameDialogFragment dialog = new AskForUserNameDialogFragment();
        dialog.show(getFragmentManager(), "usernamedialog");
    }

    private void update() {
        ((TextView)findViewById(R.id.welcomeTextView)).setText("User : " + userName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void addSessionOnClick(View view) {
    }

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
            editor.commit();
            update();
        }
    }
}
