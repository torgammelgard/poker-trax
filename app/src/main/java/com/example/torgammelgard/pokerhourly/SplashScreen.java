package com.example.torgammelgard.pokerhourly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SplashScreen  extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        ImageView splashImg = (ImageView)findViewById(R.id.imageView);
        splashImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this, Tab_main_fragactivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
