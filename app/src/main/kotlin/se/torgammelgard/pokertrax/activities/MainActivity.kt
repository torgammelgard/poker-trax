package se.torgammelgard.pokertrax.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.model.entities.Session
import java.util.*

/**
 * Main activity, responsible to be informative and simple to use
 */
class MainActivity : Activity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>


    private val mDummySessions = arrayOf(
            Session().apply {
                location = "Home game"
                result = 28
                date = Date(350_000_000)
            },
            Session().apply {
                location = "Commerce Casino"
                result = 2300
                date = Date(360_000_000)
            },
            Session().apply {
                location = "The Bike"
                result = -1100
                date = Date(370_000_000)
            })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // RecyclerView
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(mDummySessions)

        recyclerView = findViewById<RecyclerView>(R.id.sessions_recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // BottomNavigationView
        //val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)
    }

    @SuppressLint("UNUSED_PARAMETER")
    fun onClickFabAddSession(view: View) {
        val intent = Intent(this, AddSessionActivity::class.java)
        startActivity(intent)
    }
}

class MyAdapter(private val sessionsData: Array<Session>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    class ViewHolder(val constraintLayout: ConstraintLayout) : RecyclerView.ViewHolder(constraintLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val constraintLayout = LayoutInflater.from(parent.context).inflate(R.layout.session_view, parent,false) as ConstraintLayout
        return ViewHolder(constraintLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sessionResult = holder.constraintLayout.getViewById(R.id.session_result_view) as TextView
        sessionResult.text =  sessionsData[position].result.toString()
        val sessionLocation = holder.constraintLayout.getViewById(R.id.session_location) as TextView
        sessionLocation.text = sessionsData[position].location
        val sessionDate = holder.constraintLayout.getViewById(R.id.session_date) as TextView
        sessionDate.text = sessionsData[position].date.toString()
    }

    override fun getItemCount(): Int {
        return sessionsData.size
    }

}