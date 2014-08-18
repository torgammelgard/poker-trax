package com.example.torgammelgard.pokerhourly;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class SummaryFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.summary, container, false);

        GraphViewSeries graphViewSeries = new GraphViewSeries(new GraphView.GraphViewData[] {
                new GraphView.GraphViewData(1, 1.5d),
                new GraphView.GraphViewData(2, 2.5d),
                new GraphView.GraphViewData(3, 5.5d),
                new GraphView.GraphViewData(4, 13.5d),
        });
        GraphView graphView = new LineGraphView(getActivity(), "Summary");
        graphView.addSeries(graphViewSeries);
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.graphLayout);
        layout.addView(graphView);

        return view;
    }
}