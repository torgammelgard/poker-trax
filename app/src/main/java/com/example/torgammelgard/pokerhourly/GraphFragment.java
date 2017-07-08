package com.example.torgammelgard.pokerhourly;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;

import java.util.ArrayList;

public class GraphFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graph_frag, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateGraph();
    }

    private void updateGraph() {
        /* Prepare the graphView data*/
        ArrayList<Session> sessionList = ((MainApp)getActivity().getApplication())
                .mDataSource.getLastSessions(20);
        int[] resultIntArray = new int[sessionList.size() + 1];
        int index = 0;
        resultIntArray[index++] = 0;
        for (Session session : sessionList) {
            resultIntArray[index++] = session.getResult();
        }
        GraphView.GraphViewData[] data = makeData(resultIntArray);

        /* style - nested class*/
        GraphViewSeries.GraphViewSeriesStyle g =
                new GraphViewSeries.GraphViewSeriesStyle(Color.GREEN, 7);

        /* init the graph */
        GraphViewSeries graphViewSeries = new GraphViewSeries("graphViewSeries", g, data);
        GraphView graphView = new LineGraphView(getActivity(), getString(R.string.graph_title));

        /* Style the graph */
        GraphViewStyle style = new GraphViewStyle();
        style.setNumHorizontalLabels(11);
        graphView.setGraphViewStyle(style);

        /* add the data and show*/
        graphView.addSeries(graphViewSeries);
        if (getView() != null) {
            LinearLayout layout = (LinearLayout) getView().findViewById(R.id.graphLayout);
            layout.addView(graphView);
        }
    }

    /** returns data for the GraphView */
    private GraphView.GraphViewData[] makeData(int[] dataList) {
        double sum = 0;
        GraphView.GraphViewData[] data = new GraphView.GraphViewData[dataList.length];
        for (int index = 0; index < data.length; index++) {
            sum += (double)dataList[index] / 100;
            data[index] = new GraphView.GraphViewData(index, sum);
        }
        return data;
    }
}