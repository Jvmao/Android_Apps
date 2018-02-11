package com.josevicente.proyecto_finalpmm.Activities;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.josevicente.proyecto_finalpmm.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    //Declaramos las variables
    private static final String TAG = "LOAD ACTIVITY"; //Nos muestra el mensaje por consola cuando el Activity es cargado
    private long SPLASH_DELAY = 3000; //equivale a 3 segundos
    private ActionBar actionBar; //ActionBar para añadir nuevas funcionalidades en la barra de la Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Definimos el Timer Task
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(getApplicationContext(),MainMenuActivity.class); //Pasamos a la SplashActivity desde SplashActivity
                startActivity(mainIntent);
                Log.i(TAG,"Pasando a la Siguiente Pantalla");
                finish(); //Finalizamos la activity//
            }
        };
        Timer timer = new Timer(); //Importamos java.util.Timer
        timer.schedule(task,SPLASH_DELAY);

        //Añadimos un icono en ActionBar de la App
        actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.github1x24);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }
}
