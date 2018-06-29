package se.torgammelgard.pokertrax.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.model.entities.Session
import java.text.SimpleDateFormat

class SessionAdapter(private val sessionsData: List<Session>) : RecyclerView.Adapter<SessionAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val result: TextView = view.findViewById(R.id.session_result_view)
        val location: TextView = view.findViewById(R.id.session_location)
        val date: TextView = view.findViewById(R.id.session_date)

        fun bind(session: Session) {
            val convertedResult = "${session.result / 100}"
            result.text = convertedResult
            location.text = session.location
            val formatter = SimpleDateFormat("yy-MM-dd")
            date.text = formatter.format(session.date)
            if (session.result >= 0) result.setTextColor(Color.GREEN) else result.setTextColor(Color.RED)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.session_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sessionsData[position])
        holder.view.setOnClickListener {
            Toast.makeText(holder.view.context, "Not implemented yet (item $position)", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return sessionsData.size
    }

}