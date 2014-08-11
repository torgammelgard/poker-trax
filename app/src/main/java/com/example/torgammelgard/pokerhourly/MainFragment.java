package com.example.torgammelgard.pokerhourly;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MainFragment extends Fragment implements
        AskForUserNameDialogFragment.NoticeDialogListener {

    private static final String mLOGMESSAGE = "LOGMESSAGE";
    private SharedPreferences mPrefs;
    private String userName;
    public List<Session> sessionList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getActivity().setContentView(view);

        //ask for account or get the account from last saved state
        mPrefs = getActivity().getSharedPreferences("mPrefs", 0);
        userName = mPrefs.getString("username", "");

        if (userName.equals("")) {
            askForUserName();
        }

        update(view);

        return view;
    }

    private void askForUserName() {
        /*AskForUserNameDialogFragment dialog = new AskForUserNameDialogFragment();
        dialog.show(getActivity().getFragmentManager(), "usernamedialog");*/
    }

    private void update(View view) {
        ((TextView)view.findViewById(R.id.welcomeTextView)).setText("User : " + userName);
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
            update(getView());
        }
    }

}
