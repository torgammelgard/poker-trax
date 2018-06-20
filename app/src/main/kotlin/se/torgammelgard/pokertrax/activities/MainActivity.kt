package se.torgammelgard.pokertrax.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.new_layout.*
import se.torgammelgard.pokertrax.R

/**
 * Main activity, responsible to be informative and simple to use
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            setVisible(true)
             true
        }
    }

    @SuppressLint("UNUSED_PARAMETER")
    fun onClickFabAddSession(view: View) {
        val intent = Intent(this, AddSessionActivity::class.java)
        startActivity(intent)
    }
}