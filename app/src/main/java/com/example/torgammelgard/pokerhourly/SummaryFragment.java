package com.example.torgammelgard.pokerhourly;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Fragment contains an overview of some results.
 */

public class SummaryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.summary_frag, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Handle the list*/

        ListView listView = (ListView) getActivity().findViewById(R.id.listView_summary);

        /* Header */
        View header = View.inflate(getActivity(), R.layout.summary_list_item, null);
        listView.addHeaderView(header);

        /* List */
        final ArrayList<String> gameTypes = ((MainApp) getActivity().getApplication())
                .mDataSource.getAllGameTypes();
        final ArrayList<String> resultList = ((MainApp) getActivity().getApplication())
                .mDataSource.getResultFromGametypes();
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.summary_list_item, R.id.text1, gameTypes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView1 = (TextView)view.findViewById(R.id.text1);
                TextView textView2 = (TextView)view.findViewById(R.id.text2);

                textView1.setText(gameTypes.get(position));
                if (Integer.valueOf(resultList.get(position)) < 0) {
                    textView2.setTextColor(Color.RED);
                }
                else {
                    textView2.setTextColor(Color.GREEN);
                }
                textView2.setText(String.format("%.2f",
                        Double.valueOf(resultList.get(position)) / 100));

                return view;
            }
        });

        /* Footer */
        View footer = View.inflate(getActivity(), R.layout.summary_list_item, null);
        int totalCents = 0;
        for (String result : resultList) {
            totalCents += Integer.valueOf(result);
        }
        ((TextView) footer.findViewById(R.id.text1)).setText(R.string.summary_total);
        TextView result_textView = ((TextView) footer.findViewById(R.id.text2));
        if (totalCents < 0) {
            result_textView.setTextColor(Color.RED);
        }
        else {
            result_textView.setTextColor(Color.GREEN);
        }

        result_textView.setText(String.format("%.2f", (double) totalCents / 100));

        listView.addFooterView(footer);
        listView.setFooterDividersEnabled(true);

        // total time played
        int mins = ((MainApp) getActivity().getApplication()).mDataSource.getTotalTimePlayed();
        String str = String.format("%02d:%02d", mins / 60, mins % 60);
        ((TextView) getActivity().findViewById(R.id.textViewTotalTime)).setText(str);

        // profit per hour
        double profitPerHour = (double)totalCents / 100 / mins * 60;
        TextView tv = ((TextView) getActivity().findViewById(R.id.textViewTotalPerHour));
        if (profitPerHour < 0)
            tv.setTextColor(Color.RED);
        else
            tv.setTextColor(Color.GREEN);
        tv.setText(String.format("%.2f", profitPerHour));

        // average per hour
        double avg_bb_per_hour = ((MainApp) getActivity().getApplication()).mDataSource.getAvgbbPH();
        TextView avgbbPerHour_textView = ((TextView) getActivity().findViewById(R.id.avgbbPerHour));
        if (avg_bb_per_hour < 0)
            avgbbPerHour_textView.setTextColor(Color.RED);
        else
            avgbbPerHour_textView.setTextColor(Color.GREEN);
        avgbbPerHour_textView.setText(String.format("%.2f", avg_bb_per_hour));

    }
}