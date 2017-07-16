package se.torgammelgard.pokertrax

import android.app.Application

/**
 * Main application
 */
class MainApp : Application() {

    var mDataSource: DataSource ?= null

    override fun onCreate() {
        super.onCreate()

        mDataSource = DataSource(applicationContext)

    }
}
