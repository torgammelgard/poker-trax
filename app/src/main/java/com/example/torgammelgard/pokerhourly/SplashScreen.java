package com.example.torgammelgard.pokerhourly;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Tor on 2014-08-06.
 */
public class SplashScreen  extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        ImageView splashImg = (ImageView)findViewById(R.id.imageView);
        splashImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
