package se.torgammelgard.pokertrax

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

/**
 * Contains an overview of some results.
 */
class SummaryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.summary_frag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Handle the list*/

        val listView = activity.findViewById<ListView>(R.id.listView_summary)

        /* Header */
        val header = View.inflate(activity, R.layout.summary_list_item, null)
        listView.addHeaderView(header)

        /* List */
        val gameTypes = (activity.application as MainApp).mDataSource!!.allGameTypes
        val resultList = (activity.application as MainApp).mDataSource!!.resultFromGametypes
        listView.adapter = object : ArrayAdapter<String>(activity,
                R.layout.summary_list_item, R.id.text1, gameTypes) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView1 = view.findViewById<TextView>(R.id.text1)
                val textView2 = view.findViewById<TextView>(R.id.text2)

                textView1.text = gameTypes[position]
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

        /* Footer */
        val footer = View.inflate(activity, R.layout.summary_list_item, null)
        var totalCents = 0
        for (result in resultList) {
            totalCents += Integer.valueOf(result)!!
        }
        (footer.findViewById<View>(R.id.text1) as TextView).setText(R.string.summary_total)
        val result_textView = footer.findViewById<TextView>(R.id.text2)
        if (totalCents < 0) {
            result_textView.setTextColor(Color.RED)
        } else {
            result_textView.setTextColor(Color.GREEN)
        }

        result_textView.text = String.format("%.2f", totalCents.toDouble() / 100)

        listView.addFooterView(footer)
        listView.setFooterDividersEnabled(true)

        // total time played
        val mins = (activity.application as MainApp).mDataSource!!.totalTimePlayed
        val str = String.format("%02d:%02d", mins / 60, mins % 60)
        (activity.findViewById<View>(R.id.textViewTotalTime) as TextView).text = str

        // profit per hour
        val profitPerHour = totalCents.toDouble() / 100.0 / mins.toDouble() * 60
        val tv = activity.findViewById<TextView>(R.id.textViewTotalPerHour)
        if (profitPerHour < 0)
            tv.setTextColor(Color.RED)
        else
            tv.setTextColor(Color.GREEN)
        tv.text = String.format("%.2f", profitPerHour)

        // average per hour
        val avg_bb_per_hour = (activity.application as MainApp).mDataSource!!.avgbbPH
        val avgbbPerHour_textView = activity.findViewById<TextView>(R.id.avgbbPerHour)
        if (avg_bb_per_hour < 0)
            avgbbPerHour_textView.setTextColor(Color.RED)
        else
            avgbbPerHour_textView.setTextColor(Color.GREEN)
        avgbbPerHour_textView.text = String.format("%.2f", avg_bb_per_hour)

    }
}