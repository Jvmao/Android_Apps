package com.josevicente.proyecto_4.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josevicente.proyecto_4.Adapter.MenuAdapter;
import com.josevicente.proyecto_4.Modelo.DatosMenu;
import java.util.ArrayList;
import com.josevicente.proyecto_4.R;

public class MenuFragment extends Fragment {
    private ArrayList<DatosMenu> listadoMenu;
    private RecyclerView recyclerView;

    //Constructor vacío MenuFragment
    public MenuFragment() {
    }

    public static MenuFragment newInstance(ArrayList<DatosMenu> opcionesMenuFragment) {
        MenuFragment mf = new MenuFragment();
        if (opcionesMenuFragment != null) {
            Bundle b = new Bundle();
            b.putParcelableArrayList("key_listadoMenu", opcionesMenuFragment);
            mf.setArguments(b);
        }
        return mf;
    }

    //Generamos el método onCreate para recoger los elementos
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            listadoMenu = getArguments().getParcelableArrayList("key_listadoMenu");
        }
    }

    //Generamos el método onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Añadimos el fragment a inflar desde el layout fragment_menu
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.recyclerMenuID);
        recyclerView.setHasFixedSize(true); //Fijamos el tamaño en el layout del RecyclerView
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL)); //Lineas Divisorias en el Layout
        //Definimos el layout nuevo a adaptar
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //Añadimos el Adaptador creado en la clase MenuAdapter
        MenuAdapter menuAdapter = new MenuAdapter(listadoMenu);
        recyclerView.setAdapter(menuAdapter);

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
