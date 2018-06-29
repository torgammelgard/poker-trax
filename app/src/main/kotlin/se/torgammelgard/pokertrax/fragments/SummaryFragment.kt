package se.torgammelgard.pokertrax.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.adapters.SummaryAdapter
import se.torgammelgard.pokertrax.model.repositories.GameTypeRepository
import se.torgammelgard.pokertrax.model.repositories.SessionRepository
import javax.inject.Inject

/**
 * Contains an overview of some results.
 */
class SummaryFragment : Fragment() {

    @Inject lateinit var sessionRepository: SessionRepository
    @Inject lateinit var gameTypeRepository: GameTypeRepository

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.summary_frag, container, false)
        val listView = view.findViewById<ListView>(R.id.listView_summary)
        val header = View.inflate(activity, R.layout.summary_list_item, null)
        listView?.addHeaderView(header)

        /* List */
        doAsync {
            val gameTypes = gameTypeRepository.getAll()
            val resultList = gameTypes.map{
                sessionRepository.resultForGameType(it.id)
            }
            val totalTimePlayedInMinutes = sessionRepository.totalTimePlayed()
            val averageBigBetsPerHour = sessionRepository.getAverageBigBlindPerHour()

            onComplete {

                listView.adapter = SummaryAdapter(gameTypes, resultList, inflater)

                // Footer
                val footer = View.inflate(activity, R.layout.summary_list_item, null)
                val totalCents = resultList.sumBy { it }

                (footer.findViewById<View>(R.id.text1) as TextView).setText(R.string.summary_total)
                val resultTextView = footer.findViewById<TextView>(R.id.text2)
                if (totalCents < 0) {
                    resultTextView.setTextColor(Color.RED)
                } else {
                    resultTextView.setTextColor(Color.GREEN)
                }

                resultTextView.text = String.format("%.2f", totalCents.toDouble() / 100)

                listView.addFooterView(footer)
                listView.setFooterDividersEnabled(true)

                // total time played
                val str = String.format("%02d:%02d", totalTimePlayedInMinutes / 60, totalTimePlayedInMinutes % 60)
                (view.findViewById<View>(R.id.textViewTotalTime) as TextView).text = str

                // profit per hour
                val profitPerHour = totalCents.toDouble() / 100.0 / totalTimePlayedInMinutes.toDouble() * 60
                val tv = view.findViewById<TextView>(R.id.textViewTotalPerHour)
                if (profitPerHour < 0)
                    tv.setTextColor(Color.RED)
                else
                    tv.setTextColor(Color.GREEN)
                tv.text = String.format("%.2f", profitPerHour)

                val averageBigBetsPerHourTextView = view.findViewById<TextView>(R.id.avgbbPerHour)
                if (averageBigBetsPerHour < 0)
                    averageBigBetsPerHourTextView.setTextColor(Color.RED)
                else
                    averageBigBetsPerHourTextView.setTextColor(Color.GREEN)
                averageBigBetsPerHourTextView.text = String.format("%.2f", averageBigBetsPerHour)
            }
        }
        return view
    }
}
