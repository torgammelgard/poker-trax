package com.example.torgammelgard.pokerhourly;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsActivity extends FragmentActivity {
    private String[] NAMES = {"Bob", "Boris", "Bimbo", "Billy"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        /*MyListFragment f = (MyListFragment)getFragmentManager().findFragmentById(R.id.gameFragment);
        TextView header = new TextView(this);
        header.setText("Game");
        f.getListView().addHeaderView(header);
        f.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, NAMES));*/
        ListView resultListView = (ListView)findViewById(R.id.resultListView);
        /*TextView header = new TextView(this);
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

        header.setText("Game");
        gameListView.addHeaderView(header);*/

        /* Ä*/
        ArrayList<Session> sessionList = new ArrayList<Session>();
        sessionList.add(new Session(new GameInfo("2-4NL"), 3, 50));
        sessionList.add(new Session(new GameInfo("3-6NL"), 5, 120));

        /*Map<String, String> map = new HashMap<String, String>();
        map.put("gameinfo", sessionList.get(0).getGameInfo().toString());
        map.put("hours", String.valueOf(sessionList.get(0).getHours()));
        map.put("result", String.valueOf(sessionList.get(0).getResult()));

        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list.add(map);
*/
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list = makeDataList(sessionList);
        String[] from = {"gameinfo", "hours", "result"};
        int[] to = {R.id.text1, R.id.text2 , R.id.text3};

        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.result_list_item,
                from, to);
        resultListView.setAdapter(adapter);




        /**/
        /*f = (MyListFragment)getFragmentManager().findFragmentById(R.id.daysFragment);
        TextView textView = new TextView(this);
        textView.setText("Results");
        f.getListView().addHeaderView(textView);
        Map<String, String> map = new HashMap<String, String>();
        map.put("winnings", "24dollar");
        map.put("time", "3 hours");
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list.add(map);
        String[] from = {"winnings", "time"};
        int[] to = {R.id.text1, R.id.text2};
        f.setListAdapter(new SimpleAdapter(this, list, R.layout.result_list_item, from, to));*/

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    ArrayList<Map<String, String>> makeDataList(ArrayList<Session> sessions) {

        ArrayList<Map<String, String>> dataList = new ArrayList();

        for (Session session : sessions) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("gameinfo", session.getGameInfo().toString());
            map.put("hours", String.valueOf(session.getHours()));
            map.put("result", String.valueOf(session.getResult()));
            dataList.add(map);
        }

        return dataList;
    }


    @Override
    public View onCreateView(String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
        
    }
}
