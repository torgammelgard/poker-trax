package se.torgammelgard.pokertrax.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import dagger.android.support.AndroidSupportInjection
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.model.repositories.SessionRepository
import javax.inject.Inject

class GraphFragment : Fragment() {

    @Inject lateinit var sessionRepository: SessionRepository

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.graph_frag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateGraph()
    }

    private fun updateGraph() {
        val graph = view?.findViewById<GraphView>(R.id.graph)

        // get and construct the data
        graph?.let {
            doAsync {
                val sessions = sessionRepository.getAllSessions()
                onComplete {
                    val dataPoints = arrayOfNulls<DataPoint>(sessions.size + 1)

                    // start data at 0, 0
                    dataPoints[0] = DataPoint(0.0, 0.0)

                    // add the rest of the results
                    sessions.forEachIndexed { index, session ->
                        dataPoints[index + 1] = DataPoint(index.toDouble(), (session.result / 100).toDouble())
                    }

                    val series = LineGraphSeries<DataPoint>(dataPoints)

                    // style graph
                    graph.viewport.isXAxisBoundsManual = true
                    graph.viewport.setMinX(0.0)
                    graph.viewport.setMaxX(dataPoints.size - 0.5)

                    graph.addSeries(series)
                }
            }
        }
    }
}