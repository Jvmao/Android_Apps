package com.josevicente.proyecto_3.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.josevicente.proyecto_3.Activity.MenuActivity;
import com.josevicente.proyecto_3.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "LOAD ACTIVITY"; //Nos muestra el mensaje por consola cuando el Activity es cargado
    private long SPLASH_DELAY = 3000; //equivale a 3 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Definimos el Timer Task
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(getApplicationContext(),MenuActivity.class); //Pasamos a la SplashActivity desde SplashActivity
                startActivity(mainIntent);
                Log.i(TAG,"Pasando a la Siguiente Pantalla");
                finish(); //Finalizamos la activity//
            }
        };
        Timer timer = new Timer(); //Importamos java.util.Timer
        timer.schedule(task,SPLASH_DELAY);
    }
}
