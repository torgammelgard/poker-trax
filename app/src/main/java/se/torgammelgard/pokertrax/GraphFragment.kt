package se.torgammelgard.pokertrax

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

import java.util.ArrayList

import se.torgammelgard.pokertrax.model.Session

class GraphFragment : android.support.v4.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.graph_frag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateGraph()
    }

    private fun updateGraph() {

        // get the graph view
        if (view == null) {
            return
        }
        val graph = view!!.findViewById<GraphView>(R.id.graph)

        // get and construct the data
        val sessionList = (activity.application as MainApp).mDataSource!!.getLastSessions(20)

        val dataPoints = arrayOfNulls<DataPoint>(sessionList!!.size + 1)

        // start data at 0, 0
        dataPoints[0] = DataPoint(0.0, 0.0)

        var index = 1

        // add the rest of the results
        for (session in sessionList!!) {
            dataPoints[index] = DataPoint(index.toDouble(), (session.result / 100).toDouble())
            index++
        }

        val series = LineGraphSeries<DataPoint>(dataPoints)

        // style graph

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(dataPoints.size - 0.5)

        graph.addSeries(series)
    }
}