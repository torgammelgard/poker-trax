package se.torgammelgard.pokertrax.activities

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.model.entities.Session

class SessionAdapter(private val sessionsData: Array<Session>) : RecyclerView.Adapter<SessionAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val result: TextView = view.findViewById(R.id.session_result_view)
        val location: TextView = view.findViewById(R.id.session_location)
        val date: TextView = view.findViewById(R.id.session_date)

        fun bind(session: Session) {
            result.text = session.result.toString()
            location.text = session.location
            date.text = session.date.toString()
            if (session.result >= 0) result.setTextColor(Color.GREEN) else result.setTextColor(Color.RED)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionAdapter.ViewHolder {
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