package com.josevicente.proyecto_3.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.josevicente.proyecto_3.Fragment.MenuFragment;
import com.josevicente.proyecto_3.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //6-Le pasamos el Fragment definido en la clase MenuFragment
        MenuFragment fragment = new MenuFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Le pasamos el ID del Fragment del activity_menu.xml
        fragmentTransaction.add(R.id.frameMenu,fragment);
        fragmentTransaction.commit(); //Lanzamos el fragment en nuestra Activity Menu_Activity
    }
}
