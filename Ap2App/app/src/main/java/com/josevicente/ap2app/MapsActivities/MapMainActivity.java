package com.josevicente.ap2app.MapsActivities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.josevicente.ap2app.Activities.MenuActivity;
import com.josevicente.ap2app.Activities.StartActivity;
import com.josevicente.ap2app.EcoActivities.EcoActivity;
import com.josevicente.ap2app.Fragments.InicioMapaFragment;
import com.josevicente.ap2app.Fragments.MenuMapaFragment;
import com.josevicente.ap2app.GestActivities.GestMainActivity;
import com.josevicente.ap2app.Model.Mapa;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.ShopActivities.ListMainActivity;
import com.josevicente.ap2app.Web.WebActivity;

import java.util.ArrayList;

public class MapMainActivity extends AppCompatActivity {
    //Variables para Autenticar al Usuario
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ArrayList<Mapa> opcionesMenuMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);

        //Instanciamos las Variables
        mAuth = FirebaseAuth.getInstance();

        //Agregamos los componentes al menu mapa
        opcionesMenuMapa = new ArrayList<>();
        opcionesMenuMapa.add(new Mapa("CALCULAR RUTAS",R.drawable.mapmarker4x250));
        opcionesMenuMapa.add(new Mapa("LUGARES CERCANOS",R.drawable.mapmarker5x400));

        //Cargamos MenuMapaFragment desde el método creado
        menuMapa();

        //Cargamos InicioMapaFragment desde el método creado
        inicioMapa();
    }

    //Método para cargar el Fragment MenuMapaFragment
    public void menuMapa(){
        //Cargamos MenuMapaFragment
        MenuMapaFragment menuMapaFragment = MenuMapaFragment.newInstance(opcionesMenuMapa);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameEstaticoID,menuMapaFragment);
        fragmentTransaction.commit();
    }

    //Método para cargar el Fragment InicioMapaFragment
    public void inicioMapa(){
        //Cargamos InicioMapaFragment
        InicioMapaFragment inicioMapaFragment = new InicioMapaFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameDinamicoID,inicioMapaFragment);
        fragmentTransaction.commit();
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
                    Intent intentStart = new Intent(MapMainActivity.this,StartActivity.class);
                    startActivity(intentStart); //Nos devuelve a la pantalla Principal de Inicio
                    Toast.makeText(MapMainActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case R.id.itemMain:
                //Pasamos a MenuActivity
                Intent intentMain = new Intent(MapMainActivity.this,MenuActivity.class);
                startActivity(intentMain);
                break;

            case R.id.itemEco:
                //Nos devuelve a la pantalla de EcoActivity
                Intent intentEco = new Intent(MapMainActivity.this, EcoActivity.class);
                startActivity(intentEco);
                break;

            case R.id.itemMapa:
                Toast.makeText(MapMainActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_LONG).show();
                break;

            case R.id.itemGest:
                //Nos devuelve a la pantalla GestMainActivity
                Intent intentGest = new Intent(MapMainActivity.this, GestMainActivity.class);
                startActivity(intentGest);
                break;

            case R.id.itemLista:
                //Nos devuelve a la pantalla ListMainActivity
                Intent intentList = new Intent(MapMainActivity.this, ListMainActivity.class);
                startActivity(intentList);
                break;

            case R.id.itemWeb:
                //Nos devuelve a la pantalla WebActivity
                Intent intentWeb = new Intent(MapMainActivity.this,WebActivity.class);
                startActivity(intentWeb);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
