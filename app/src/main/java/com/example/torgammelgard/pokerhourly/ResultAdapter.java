package com.example.torgammelgard.pokerhourly;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO: Class header comment. CRAP!
 */
public class ResultAdapter extends SimpleAdapter {

    public ResultAdapter(Context context, List<Map<String, String>> data,
                         final ArrayList<String> gameTypeList,
                         int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        setViewBinder(new ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String text) {

                switch (view.getId()) {
                    case R.id.text0:
                        return false;
                    case R.id.text1:
                        String str = gameTypeList.get(Integer.valueOf(text) - 1);
                        ((TextView) view).setText(str);
                        return true;
                    case R.id.text2:
                        ((TextView) view).setText(getTime(text));
                        return true;
                    /* sets the color of negative results to red */
                    case R.id.text3:
                        if (Integer.valueOf(text) < 0) {
                            ((TextView) view).setTextColor(Color.RED);
                        }
                        else {
                            ((TextView) view).setTextColor(Color.GREEN);
                        }
                        ((TextView) view).setText(String.format("%.2f", Double.valueOf(text) / 100));
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private String getTime(String minutes){
        int mins = Integer.valueOf(minutes);
        return String.format("%02d:%02d", mins / 60, mins % 60);
    }
}
/*

    @Override
    public void setViewText(TextView v, String text) {
        if (v.getId() == R.id.text1) {
            text = mGameStructureList.get(Integer.valueOf(text)-1);
        }
        v.setText(text);
    }
*/
