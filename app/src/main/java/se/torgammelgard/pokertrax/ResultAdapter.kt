package se.torgammelgard.pokertrax

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.SimpleAdapter
import android.widget.TextView

import java.util.ArrayList

/**
 * TODO: Class header comment. CRAP!
 */
class ResultAdapter(context: Context, data: List<Map<String, String>>,
                    gameTypeList: ArrayList<String>,
                    resource: Int, from: Array<String>, to: IntArray) : SimpleAdapter(context, data, resource, from, to) {

    init {

        viewBinder = ViewBinder { view, data, text ->
            when (view.id) {
                R.id.text0 -> false
                R.id.text1 -> {
                    val str = gameTypeList[Integer.valueOf(text)!! - 1]
                    (view as TextView).text = str
                    true
                }
                R.id.text2 -> {
                    (view as TextView).text = getTime(text)
                    true
                }
            /* sets the color of negative results to red */
                R.id.text3 -> {
                    if (Integer.valueOf(text) < 0) {
                        (view as TextView).setTextColor(Color.RED)
                    } else {
                        (view as TextView).setTextColor(Color.GREEN)
                    }
                    view.text = String.format("%.2f", java.lang.Double.valueOf(text)!! / 100)
                    true
                }
                else -> false
            }
        }
    }

    private fun getTime(minutes: String): String {
        val mins = Integer.valueOf(minutes)!!
        return String.format("%02d:%02d", mins / 60, mins % 60)
    }
}
/*

    @Override
    public void setViewText(TextView v, String text) {
        if (v.getId() == R.id.text1) {
            text = mGameStructureList.get(Integer.valueOf(text)-1);
        }
        v.setText(text);
    }
*/
