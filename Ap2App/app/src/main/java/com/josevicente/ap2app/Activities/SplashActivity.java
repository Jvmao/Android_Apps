package com.josevicente.ap2app.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.josevicente.ap2app.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    //Variables
    private long SPLASH_DELAY = 5000; //equivale a 5 segundos
    private ImageView imageSplash;
    private TextView tituloSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageSplash = findViewById(R.id.imageSplash);
        tituloSplash = findViewById(R.id.tituloSplash);

        //Le pasamos la animación creada al imageView
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        imageSplash.startAnimation(animation);
        //Le pasamos la animación creada al textView
        tituloSplash.startAnimation(animation);

        //Definimos el TimerTask
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intentStart = new Intent(SplashActivity.this,StartActivity.class);
                startActivity(intentStart);

                finish(); //Finaliza la pantalla Splash
            }
        };
        //Añadimos el TimerTask
        Timer timer = new Timer();
        timer.schedule(task,SPLASH_DELAY);
    }
}
