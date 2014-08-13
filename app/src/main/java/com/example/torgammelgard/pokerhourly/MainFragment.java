package com.example.torgammelgard.pokerhourly;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MainFragment extends Fragment {

    private static final String mLOGMESSAGE = "LOGMESSAGE";

    public List<Session> sessionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String userName = ((Tab_main_fragactivity)getActivity()).userName;
        ((TextView)view.findViewById(R.id.welcomeTextView)).setText("User : " + userName);
        return view;
    }
}
