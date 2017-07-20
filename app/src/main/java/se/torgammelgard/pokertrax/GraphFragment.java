package se.torgammelgard.pokertrax;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import se.torgammelgard.pokertrax.model.Session;

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

        // get the graph view
        if (getView() == null) {
            return;
        }
        GraphView graph = getView().findViewById(R.id.graph);

        // get and construct the data
        ArrayList<Session> sessionList = ((MainApp) getActivity().getApplication()).getMDataSource().getLastSessions(20);

            DataPoint[] dataPoints = new DataPoint[sessionList.size() + 1];

        // start data at 0, 0
        dataPoints[0] = new DataPoint(0.0, 0.0);

        int index = 1;

        // add the rest of the results
        for (Session session : sessionList) {
            dataPoints[index] = new DataPoint(index, session.getResult() / 100);
            index++;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

        // style graph

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMaxX(dataPoints.length - 0.5);

        graph.addSeries(series);
    }
}