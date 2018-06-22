package se.torgammelgard.pokertrax.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        viewAdapter = SessionAdapter(mDummySessions)

        recyclerView = findViewById<RecyclerView>(R.id.sessions_recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            item ->
            when (item.itemId) {
                R.id.bottom_nav_item_sessions -> { TODO("Implement") }
                R.id.bottom_nav_item_graph -> { TODO("Implement") }
                R.id.bottom_nav_item_summary -> { TODO("Implement") }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    @SuppressLint("UNUSED_PARAMETER")
    fun onClickFabAddSession(view: View) {
        val intent = Intent(this, AddSessionActivity::class.java)
        startActivity(intent)
    }
}
