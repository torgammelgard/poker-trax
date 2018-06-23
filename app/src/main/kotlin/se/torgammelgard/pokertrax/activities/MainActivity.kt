package se.torgammelgard.pokertrax.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.fragments.GraphFragment
import se.torgammelgard.pokertrax.fragments.SessionsFragment
import se.torgammelgard.pokertrax.fragments.ResultsFragment

/**
 * Main activity, responsible to be informative and simple to use
 */
class MainActivity : FragmentActivity(), AnkoLogger {

    private lateinit var addSessionFAB: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        addSessionFAB = findViewById(R.id.floatingActionButton)

        // BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)

        // init sessions fragment
        val sessionFragment = SessionsFragment()
        supportFragmentManager.beginTransaction().add(sessionFragment, "sessions").commit()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_item_sessions -> {
                    info { "bottom_nav_item_sessions clicked" }
                    replaceFragment(SessionsFragment())
                    addSessionFAB.show()
                }
                R.id.bottom_nav_item_graph -> {
                    replaceFragment(GraphFragment())
                    addSessionFAB.hide()
                }
                R.id.bottom_nav_item_summary -> {
                    replaceFragment(ResultsFragment())
                    addSessionFAB.hide()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        bottomNavigationView.selectedItemId = R.id.bottom_nav_item_sessions
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.commit()
    }

    @SuppressLint("UNUSED_PARAMETER")
    fun onClickFabAddSession(view: View) {
        val intent = Intent(this, AddSessionActivity::class.java)
        startActivity(intent)
    }
}
