package se.torgammelgard.pokertrax.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import se.torgammelgard.pokertrax.MainApp
import se.torgammelgard.pokertrax.R

/**
 * Contains an overview of some results.
 */
class SummaryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.summary_frag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val listView = activity?.findViewById<ListView>(R.id.listView_summary)
        val header = View.inflate(activity, R.layout.summary_list_item, null)
        listView?.addHeaderView(header)

        /* List */
        val gameTypes = (activity?.application as MainApp).mDataSource!!.allGameTypes
        val resultList = (activity!!.application as MainApp).mDataSource!!.resultsFromGameTypes
        listView!!.adapter = object : ArrayAdapter<String>(activity,
                R.layout.summary_list_item, R.id.text1, gameTypes) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView1 = view.findViewById<TextView>(R.id.text1)
                val textView2 = view.findViewById<TextView>(R.id.text2)

                textView1.text = gameTypes!![position]
                if (Integer.valueOf(resultList[position]) < 0) {
                    textView2.setTextColor(Color.RED)
                } else {
                    textView2.setTextColor(Color.GREEN)
                }
                textView2.text = String.format("%.2f",
                        java.lang.Double.valueOf(resultList[position])!! / 100)

                return view
            }
        }

        // Footer
        val footer = View.inflate(activity, R.layout.summary_list_item, null)
        val totalCents = resultList.sumBy { Integer.valueOf(it)!! }

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
        val minutes = (activity!!.application as MainApp).mDataSource!!.totalTimePlayed
        val str = String.format("%02d:%02d", minutes / 60, minutes % 60)
        (activity!!.findViewById<View>(R.id.textViewTotalTime) as TextView).text = str

        // profit per hour
        val profitPerHour = totalCents.toDouble() / 100.0 / minutes.toDouble() * 60
        val tv = activity!!.findViewById<TextView>(R.id.textViewTotalPerHour)
        if (profitPerHour < 0)
            tv.setTextColor(Color.RED)
        else
            tv.setTextColor(Color.GREEN)
        tv.text = String.format("%.2f", profitPerHour)

        // average per hour
        val averageBigBetsPerHour = (activity!!.application as MainApp).mDataSource!!.averageBigBetPerHour
        val averageBigBetsPerHourTextView = activity!!.findViewById<TextView>(R.id.avgbbPerHour)
        if (averageBigBetsPerHour < 0)
            averageBigBetsPerHourTextView.setTextColor(Color.RED)
        else
            averageBigBetsPerHourTextView.setTextColor(Color.GREEN)
        averageBigBetsPerHourTextView.text = String.format("%.2f", averageBigBetsPerHour)

    }
}