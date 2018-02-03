package com.josevicente.proyecto_5.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.josevicente.proyecto_5.Fragments.MenuDinamico;
import com.josevicente.proyecto_5.Fragments.MenuFragment;
import com.josevicente.proyecto_5.Fragments.PerfilFragment;
import com.josevicente.proyecto_5.Modelo.DatosMenu;
import com.josevicente.proyecto_5.Modelo.Jugador;
import com.josevicente.proyecto_5.R;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements PerfilFragment.btnListener{
    //Variables
    public Jugador jugador = new Jugador();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Agregamos los componentes al menú
        ArrayList<DatosMenu> opcionesMenu=new ArrayList<>();
        opcionesMenu.add(new DatosMenu("PERFIL",R.drawable.businessmanx24));
        opcionesMenu.add(new DatosMenu("JUEGO",R.drawable.joystickx24));
        opcionesMenu.add(new DatosMenu("INSTRUCCIONES",R.drawable.mobilephonex24));
        opcionesMenu.add(new DatosMenu("INFORMACIÓN", R.drawable.questionx24));

        //Le pasamos el Fragment Estático definido en la clase MenuFragment
        MenuFragment menuFragment = MenuFragment.newInstance(opcionesMenu);
        //menuFragment.setArguments(getIntent().getExtras());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //Le pasamos el ID del layout principal del activity_menu.xml
        fragmentTransaction.add(R.id.menuEstaticoID,menuFragment);
        fragmentTransaction.commit(); //Lanzamos el fragment en nuestra Activity Menu_Activity

        menuInicio(); //Cargamos el método definido para el fragment dinámico del menú de inicio

    }

    //Método para cargar el Menú Dinámico de Inicio
    public void menuInicio(){
        //Agregamos el fragment Dinámico definido en la clase MenuDinamico
        MenuDinamico menuDinamico = new MenuDinamico();
        //menuDinamico.setArguments(getIntent().getExtras());
        FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
        //Reemplazamos el menuDinamico
        fTransaction.replace(R.id.menuDinamicoID,menuDinamico);
        fTransaction.addToBackStack(null);
        //Realizamos la transacción
        fTransaction.commit();
    }


    //Método onClickButton declarado en PerfilFragment e implementado desde esta clase
    public void onClickButton(String nombre,String nickName){
            //Recogemos los valores del Objeto Jugador
            jugador.setNombre(nombre);
            jugador.setNickname(nickName);
            menuInicio();//Cargamos la panrtalla de inicio

            //Comprobamos que los valores se han pasado
            Log.i("NICK MAIN",jugador.getNickname()+" NOMBRE "+jugador.getNombre());

            //Cargamos JuegoFragment y le asignamos los valores nuevos
            //FragmentTransaction ft = getFragmentManager().beginTransaction();
            //JuegoFragment NuevoJuegoFragment = JuegoFragment.newInstance(nickName, String.valueOf(jugador.getPuntos()));
            //ft.replace(R.id.menuDinamicoID, NuevoJuegoFragment).commit();
            Toast.makeText(this,"Bienvenido: "+jugador.getNombre()+ "tu Alias es: "+jugador.getNickname(),Toast.LENGTH_LONG).show(); //Necesario para recoger el Toast enviado desde PerfilFragment
    }

}
