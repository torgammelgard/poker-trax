package com.example.torgammelgard.pokerhourly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultsFragment extends android.support.v4.app.Fragment {
    private String[] NAMES = {"Bob", "Boris", "Bimbo", "Billy"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.results, container, false);
        ListView resultListView = (ListView)view.findViewById(R.id.resultListView);

        ArrayList<Session> sessionList = new ArrayList<Session>();
        sessionList.add(new Session("2-4NL", 3, 50));
        sessionList.add(new Session("3-6NL", 5, 120));
        ArrayList<Map<String, String>> list;
        list = makeDataList(sessionList);
        String[] from = {"gameinfo", "hours", "result"};
        int[] to = {R.id.text1, R.id.text2 , R.id.text3};

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.result_list_item,
                from, to);
        resultListView.setAdapter(adapter);

        return view;
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

}
