package se.torgammelgard.pokertrax.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import se.torgammelgard.pokertrax.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }
}