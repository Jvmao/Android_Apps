package com.josevicente.ap2app.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josevicente.ap2app.Adapters.MenuMapaAdapter;
import com.josevicente.ap2app.Model.Mapa;
import com.josevicente.ap2app.R;

import java.util.ArrayList;


public class MenuMapaFragment extends Fragment {
    //Variables
    private ArrayList<Mapa>menuMapa;
    private RecyclerView recyclerView;


    public MenuMapaFragment() {}

    public static MenuMapaFragment newInstance(ArrayList<Mapa> opcionesMenuMapa) {
        MenuMapaFragment fragment = new MenuMapaFragment();
        if(opcionesMenuMapa != null){
            Bundle b = new Bundle();
            b.putParcelableArrayList("key_menuMapa", opcionesMenuMapa);
            fragment.setArguments(b);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            menuMapa = getArguments().getParcelableArrayList("key_menuMapa");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Añadimos el fragment a inflar desde el layout fragment_menu_mapa
        View view = inflater.inflate(R.layout.fragment_menu_mapa, container, false);

        recyclerView = view.findViewById(R.id.recyclerMapaID);
        recyclerView.setHasFixedSize(true); //Fijamos el tamaño en el layout del RecyclerView

        //Definimos el layout nuevo a adaptar
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Añadimos MenuMapaAdapter
        MenuMapaAdapter menuMapaAdapter = new MenuMapaAdapter(menuMapa);
        recyclerView.setAdapter(menuMapaAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
