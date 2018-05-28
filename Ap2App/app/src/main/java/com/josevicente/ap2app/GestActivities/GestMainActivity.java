package com.josevicente.ap2app.GestActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.josevicente.ap2app.Activities.MenuActivity;
import com.josevicente.ap2app.Activities.StartActivity;
import com.josevicente.ap2app.EcoActivities.EcoActivity;
import com.josevicente.ap2app.MapsActivities.MapMainActivity;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.ShopActivities.ListMainActivity;
import com.josevicente.ap2app.Utils.BottomNavigationViewBehavior;
import com.josevicente.ap2app.Web.WebActivity;

public class GestMainActivity extends AppCompatActivity {
    //Variables
    private Button btnMisContactos,btnMisEventos;
    private ImageButton imageBtnMisContactos,imageBntMisEventos;
    //Variables para Autenticar al Usuario
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    //Variables BottomMenu
    private BottomNavigationView bottomNavigationMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gest_main);

        //Instanciamos las Variables
        imageBtnMisContactos = findViewById(R.id.imageBtnMisContactos);
        imageBntMisEventos = findViewById(R.id.imageBtnMisEventos);
        mAuth = FirebaseAuth.getInstance();

        bottomNavigationMenu = findViewById(R.id.navGest1);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)bottomNavigationMenu.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });

        navigation(); //Añadimos el navegador

        //Listener imageBtnMisContactos
        imageBtnMisContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasamos a ListaContactoActivity
                Intent intentContacto = new Intent(GestMainActivity.this,ContactoActivity.class);
                startActivity(intentContacto);
            }
        });


        //Listener imageBtnMisEventos
        imageBntMisEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasamos a EventoActivity
                Intent intentEvento = new Intent(GestMainActivity.this,EventoActivity.class);
                startActivity(intentEvento);
            }
        });
    }

    //Añadimos el menu3.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mUser = mAuth.getCurrentUser();
        switch (item.getItemId()){
            case R.id.itemLogout:
                if(mUser !=null && mAuth !=null){
                    mAuth.signOut(); //Logout por parte del usuario para salir de la aplicación
                    Intent intentStart = new Intent(GestMainActivity.this,StartActivity.class);
                    startActivity(intentStart); //Nos devuelve a la pantalla Principal de Inicio
                    Toast.makeText(GestMainActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case R.id.itemMain:
                //Pasamos a MenuActivity
                Intent intentMain = new Intent(GestMainActivity.this,MenuActivity.class);
                startActivity(intentMain);
                break;

            case R.id.itemEco:
                //Nos devuelve a la pantalla de EcoActivity
                Intent intentEco = new Intent(GestMainActivity.this, EcoActivity.class);
                startActivity(intentEco);
                break;

            case R.id.itemMapa:
                //Nos devuelve a la pantalla MapMainActivity
                Intent intentMap = new Intent(GestMainActivity.this, MapMainActivity.class);
                startActivity(intentMap);
                break;

            case R.id.itemGest:
                Toast.makeText(GestMainActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_LONG).show();
                break;

            case R.id.itemLista:
                //Nos devuelve a la pantalla ListMainActivity
                Intent intentList = new Intent(GestMainActivity.this, ListMainActivity.class);
                startActivity(intentList);
                break;

            case R.id.itemWeb:
                //Nos devuelve a la pantalla WebActivity
                Intent intentWeb = new Intent(GestMainActivity.this,WebActivity.class);
                startActivity(intentWeb);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //Agregamos el navigation del Menu Principal
    public void navigation(){
        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mUser = mAuth.getCurrentUser();
                switch (item.getItemId()){
                    case R.id.btnInicioMenuB:
                        Intent intentMenu = new Intent(GestMainActivity.this,MenuActivity.class);
                        startActivity(intentMenu);
                        break;
                    case R.id.btnSalirMenuB:
                        if(mUser !=null && mAuth !=null){
                            mAuth.signOut(); //Logout por parte del usuario para salir de la aplicación
                            Intent intentStart = new Intent(GestMainActivity.this,StartActivity.class);
                            startActivity(intentStart); //Nos devuelve a la pantalla Principal de Inicio
                            Toast.makeText(GestMainActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                            finish();
                        }
                        break;
                }
                return true;
            }
        });
    }
}
