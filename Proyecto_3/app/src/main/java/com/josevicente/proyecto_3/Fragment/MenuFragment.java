package com.josevicente.proyecto_3.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josevicente.proyecto_3.Adpater.MenuAdapter;
import com.josevicente.proyecto_3.R;

//4-Creamos la clase Fragment
public class MenuFragment extends Fragment{

    //Generamos el Método onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Añadimos el fragment a inflar desde el layout fragment_menu
        View view = inflater.inflate(R.layout.fragment_menu,container,false);

        //Añadimos el RecyclerView desde fragment_menu.xml
        RecyclerView recyclerView = view.findViewById(R.id.recyclerMenuID);
        recyclerView.setHasFixedSize(true); //Fijamos el tamaño en el layout del RecyclerView
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL)); //Lineas Divisorias en el Layout
        //Definimos el layout nuevo a adaptar
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //Añadimos el Adaptador creado en la clase MenuAdapter
        MenuAdapter menuAdapter = new MenuAdapter();
        recyclerView.setAdapter(menuAdapter);

        return view;
    }
}
