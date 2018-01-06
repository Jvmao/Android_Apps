package com.josevicente.proyecto_4.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.josevicente.proyecto_4.Fragments.MenuDinamico;
import com.josevicente.proyecto_4.Fragments.MenuFragment;
import com.josevicente.proyecto_4.Modelo.DatosMenu;
import com.josevicente.proyecto_4.R;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

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
}
