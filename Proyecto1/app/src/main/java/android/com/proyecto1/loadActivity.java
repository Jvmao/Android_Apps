package android.com.proyecto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class loadActivity extends AppCompatActivity {
    private static final String TAG = "LOAD ACTIVITY"; //Nos muestra el mensaje por consola cuando el Activity es cargado
    private long SPLASH_DELAY = 3000; //equivale a 3 segundos
    //Definimos la Variable ProgressBarr
    //private ProgressBar progressBar =(ProgressBar) findViewById(R.id.progressBar);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Definimos el Timer Task
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(getApplicationContext(),mainActivity.class);
                startActivity(mainIntent);
                Log.i(TAG,"Pasando a la Siguiente Pantalla");
                finish(); //Finalizamos la activity//
            }
        };
        Timer timer = new Timer(); //Importamos java.util.Timer
        timer.schedule(task,SPLASH_DELAY);
    }

}
