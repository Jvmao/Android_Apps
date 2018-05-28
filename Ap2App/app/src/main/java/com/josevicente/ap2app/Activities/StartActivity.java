package com.josevicente.ap2app.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.josevicente.ap2app.R;

public class StartActivity extends AppCompatActivity {
    //Variables
    private Button btnRegisterStart;
    private Button btnLoginStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Instanciamos las Variables
        btnRegisterStart = findViewById(R.id.btnRegistroStart);
        btnLoginStart = findViewById(R.id.btnLoginStart);

        //Definimos el Listener de btnRegisterStart para pasar a la siguiente activity
        btnRegisterStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(intentRegister); //Pasamos a la Activity RegisterActivity
            }
        });

        //Definimos el Listener de btnLoginStart para pasar a la siguiente activity
        btnLoginStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(intentLogin); //Pasamos a la Activity LoginActivity
            }
        });
    }

    //Añadimos el menu1.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Agregamos el siguiente método para habilitar los elementos de menu1.xml
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.inicioMenu1:
                Toast.makeText(StartActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_LONG).show();
                break;

            case  R.id.resgistroMenu1:
                Intent intentRegistro = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(intentRegistro);
                finish();
                break;

            case R.id.loginMenu1:
                //Nos devuelve a LoginActivity
                Intent intentLogin = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(intentLogin);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
