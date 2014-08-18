package com.example.torgammelgard.pokerhourly;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Class header comment. CRAP!
 */
public class ResultAdapter extends SimpleAdapter {


    public ResultAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    ArrayList<Map<String, String>> makeDataList(ArrayList<Session> sessions) {

        ArrayList<Map<String, String>> dataList = new ArrayList();

        for (Session session : sessions) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("gameinfo", String.valueOf(session.getGame_type_ref()));
            map.put("minutes", String.valueOf(session.getDuration()));
            map.put("result", String.valueOf(session.getResult()));
            dataList.add(map);
        }

        return dataList;
    }
}
