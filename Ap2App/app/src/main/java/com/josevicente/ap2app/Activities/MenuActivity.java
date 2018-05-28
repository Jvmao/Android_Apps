package com.josevicente.ap2app.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.josevicente.ap2app.EcoActivities.EcoActivity;
import com.josevicente.ap2app.GestActivities.GestMainActivity;
import com.josevicente.ap2app.MapsActivities.MapMainActivity;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.ShopActivities.ListMainActivity;
import com.josevicente.ap2app.Utils.BottomNavigationViewBehavior;
import com.josevicente.ap2app.Web.WebActivity;

public class MenuActivity extends AppCompatActivity {
    //Variables
    private ImageButton imageBtnEco,imageBtnMapa,imageBtnGest,imageBtnLista,imageBtnWeb;

    //Variables para Autenticar al Usuario
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Instanciamos las Variables
        imageBtnEco = findViewById(R.id.imageBtnEco);
        imageBtnMapa = findViewById(R.id.imageBtnMapa);
        imageBtnGest = findViewById(R.id.imageBtnGest);
        imageBtnLista = findViewById(R.id.imageBtnLista);
        imageBtnWeb = findViewById(R.id.imageBtnWeb);
        mAuth = FirebaseAuth.getInstance();


        //Listern imageBtnEco
        imageBtnEco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasamos a EcoActivity
                Intent intentEco = new Intent(MenuActivity.this, EcoActivity.class);
                startActivity(intentEco);
            }
        });

        //Listener imageBtnMapa
        imageBtnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasamos a MapMainActivity
                Intent intentMap = new Intent(MenuActivity.this, MapMainActivity.class);
                startActivity(intentMap);
            }
        });

        //Listener imageBtnGest
        imageBtnGest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasamos a GestMainActivity
                Intent intentGest = new Intent(MenuActivity.this, GestMainActivity.class);
                startActivity(intentGest);
            }
        });

        //Listener imageBntLista
        imageBtnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasamos a ListMainActivity
                Intent intentList = new Intent(MenuActivity.this, ListMainActivity.class);
                startActivity(intentList);
            }
        });

        //Listener imageBtnWeb
        imageBtnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasamos a WebActivity
                Intent intentWeb = new Intent(MenuActivity.this, WebActivity.class);
                startActivity(intentWeb);
            }
        });
    }


    //Agregamos el menu2.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mUser = mAuth.getCurrentUser();
        switch (item.getItemId()){
            case R.id.logoutMenu2:
                if(mUser !=null && mAuth !=null){
                    mAuth.signOut(); //Logout por parte del usuario para salir de la aplicación
                    Intent intentStart = new Intent(MenuActivity.this,StartActivity.class);
                    startActivity(intentStart); //Nos devuelve a la pantalla Principal de Inicio
                    Toast.makeText(MenuActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
