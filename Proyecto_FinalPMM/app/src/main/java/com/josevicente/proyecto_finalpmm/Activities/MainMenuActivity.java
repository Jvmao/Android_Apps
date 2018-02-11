package com.josevicente.proyecto_finalpmm.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.josevicente.proyecto_finalpmm.Fragments.FotoFragment;
import com.josevicente.proyecto_finalpmm.Fragments.JuegoFragment;
import com.josevicente.proyecto_finalpmm.Fragments.MapaFragment;
import com.josevicente.proyecto_finalpmm.Fragments.MenuDinamico;
import com.josevicente.proyecto_finalpmm.Fragments.MenuFragment;
import com.josevicente.proyecto_finalpmm.Fragments.PerfilFragment;
import com.josevicente.proyecto_finalpmm.Modelo.DatosMenu;
import com.josevicente.proyecto_finalpmm.Modelo.Jugador;
import com.josevicente.proyecto_finalpmm.R;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        PerfilFragment.btnListener {

    private Jugador jugador = new Jugador();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Agregamos los componentes al menú
        ArrayList<DatosMenu> opcionesMenu=new ArrayList<>();
        opcionesMenu.add(new DatosMenu("PERFIL",R.drawable.businessmanx24));
        opcionesMenu.add(new DatosMenu("JUEGO",R.drawable.joystickx24));
        opcionesMenu.add(new DatosMenu("FOTO",R.drawable.iphonex48));
        opcionesMenu.add(new DatosMenu("MAPA", R.drawable.map32));


        //Le pasamos el Fragment Estático definido en la clase MenuFragment
        MenuFragment menuFragment = MenuFragment.newInstance(opcionesMenu);
        //menuFragment.setArguments(getIntent().getExtras());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //Le pasamos el ID del layout principal del activity_menu.xml
        fragmentTransaction.add(R.id.menuEstaticoID,menuFragment);
        fragmentTransaction.commit(); //Lanzamos el fragment en nuestra Activity Menu_Activity

        //Cargamos el menu de inicio desde el método creado
        menuInicio();
    }

    //Método para cargar el Menú Dinámico de Inicio
    public void menuInicio(){
        //Agregamos el fragment Dinámico definido en la clase MenuDinamico
        MenuDinamico menuDinamico = new MenuDinamico();
        FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
        //Reemplazamos el menuDinamico
        fTransaction.replace(R.id.menuDinamicoID,menuDinamico);
        fTransaction.addToBackStack(null);
        //Realizamos la transacción
        fTransaction.commit();
    }

    //Método para cargar el Fragment Perfil
    public void opcionPerfil(){
        PerfilFragment perfilFragment = new PerfilFragment();
        FragmentTransaction fragTPerfil = getFragmentManager().beginTransaction();
        fragTPerfil.replace(R.id.menuDinamicoID,perfilFragment);
        fragTPerfil.addToBackStack(null);
        fragTPerfil.commit();
    }

    //Método para cargar el Fragment Juego
    public void opcionJuego(){
        JuegoFragment juegoFragment = new JuegoFragment();
        //JuegoFragment.newInstance(jugador.getNickname(),String.valueOf(jugador.getPuntos()));
        FragmentTransaction fragTJuego = getFragmentManager().beginTransaction();
        Bundle arg = new Bundle();
        arg.putString(JuegoFragment.ARG_ALIAS, jugador.getNickname());
        arg.putString(JuegoFragment.ARG_PUNTOS, String.valueOf(jugador.getPuntos()));
        juegoFragment.setArguments(arg);

        fragTJuego.replace(R.id.menuDinamicoID,juegoFragment);
        fragTJuego.addToBackStack(null);
        fragTJuego.commit();
    }

    //Método para cargar el Fragment Foto
    public void opcionFoto(){
        FotoFragment fotoFragment = new FotoFragment();
        FragmentTransaction fragTFoto = getFragmentManager().beginTransaction();
        fragTFoto.replace(R.id.menuDinamicoID,fotoFragment);
        fragTFoto.addToBackStack(null);
        fragTFoto.commit();
    }

    //Método para cargar el Fragment Mapa
    public void opcionMapa(){
        MapaFragment mapaFragment = new MapaFragment();
        FragmentTransaction fragTMapa = getFragmentManager().beginTransaction();
        fragTMapa.replace(R.id.menuDinamicoID,mapaFragment);
        fragTMapa.addToBackStack(null);
        fragTMapa.commit();
    }

    //Método onClickButton declarado en PerfilFragment e implementado desde esta clase
    public void onClickButton(String nombre,String nickName){
        //Recogemos los valores del Objeto Jugador
        jugador.setNombre(nombre);
        jugador.setNickname(nickName);
        menuInicio();

        Toast.makeText(this,"Bienvenido "+nombre+" tu Alias es: "+nickName,Toast.LENGTH_LONG).show(); //Necesario para recoger el Toast enviado desde PerfilFragment
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.pantallaInicio) {
            Toast.makeText(this, "Inicio", Toast.LENGTH_SHORT).show();
            menuInicio();
        }else if (id == R.id.pantallaPerfil) {
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
            opcionPerfil();
        } else if (id == R.id.pantallaJuego) {
            Toast.makeText(this, "Juego", Toast.LENGTH_SHORT).show();
            opcionJuego();
        } else if (id == R.id.pantallaFoto) {
            Toast.makeText(this, "Foto", Toast.LENGTH_SHORT).show();
            opcionFoto();
        }else if(id == R.id.pantallaMapa){
            Toast.makeText(this, "Mapa", Toast.LENGTH_SHORT).show();
            opcionMapa();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
