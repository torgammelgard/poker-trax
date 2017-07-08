package se.torgammelgard.pokertrax;

import android.app.Application;

/**
 * TODO: Class header comment.
 */
public class MainApp extends Application {

    public DataSource mDataSource;

    @Override
    public void onCreate() {
        super.onCreate();

        mDataSource = new DataSource(getApplicationContext());

    }
}
