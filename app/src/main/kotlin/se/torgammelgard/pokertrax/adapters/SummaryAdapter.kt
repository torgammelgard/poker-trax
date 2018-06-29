package se.torgammelgard.pokertrax.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.model.entities.GameType

class SummaryAdapter(private val gameTypes: List<GameType>, private val resultList: List<Int>,
                     private val inflater: LayoutInflater) : BaseAdapter() {

    class ViewHolder {
        lateinit var textView1: TextView
        lateinit var textView2: TextView
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.summary_list_item, viewGroup, false)
            holder = ViewHolder()
            holder.textView1 = view.findViewById(R.id.text1)
            holder.textView2 = view.findViewById(R.id.text2)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        holder.textView1.text = (getItem(position) as GameType).type
        if (resultList[position] < 0) {
            holder.textView2.setTextColor(Color.RED)
        } else {
            holder.textView2.setTextColor(Color.GREEN)
        }
        holder.textView2.text = String.format("%.2f",
                resultList[position].toDouble() / 100)
        return view
    }

    override fun getItem(position: Int): Any = gameTypes[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = gameTypes.size
}
