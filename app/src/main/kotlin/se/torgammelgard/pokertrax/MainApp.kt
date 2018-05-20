package se.torgammelgard.pokertrax

import android.app.Application
import se.torgammelgard.pokertrax.database.DataSource

/**
 * Main application
 */
class MainApp : Application() {

    var mDataSource: DataSource?= null

    override fun onCreate() {
        super.onCreate()

        mDataSource = DataSource(applicationContext)

    }
}
