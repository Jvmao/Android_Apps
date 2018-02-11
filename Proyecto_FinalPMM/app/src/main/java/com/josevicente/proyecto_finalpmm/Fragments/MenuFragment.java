package com.josevicente.proyecto_finalpmm.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josevicente.proyecto_finalpmm.Adapter.MenuAdapter;
import com.josevicente.proyecto_finalpmm.Modelo.DatosMenu;
import com.josevicente.proyecto_finalpmm.R;

import java.util.ArrayList;


public class MenuFragment extends Fragment {
    private ArrayList<DatosMenu> listadoMenu;
    private RecyclerView recyclerView;

    public MenuFragment() {}

    public static MenuFragment newInstance(ArrayList<DatosMenu> opcionesMenuFragment) {
        MenuFragment mf = new MenuFragment();
        if (opcionesMenuFragment != null) {
            Bundle b = new Bundle();
            b.putParcelableArrayList("key_listadoMenu", opcionesMenuFragment);
            mf.setArguments(b);
        }
        return mf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listadoMenu = getArguments().getParcelableArrayList("key_listadoMenu");
        }
    }

    //Desarrollamos el Método onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        //Añadimos el fragment a inflar desde el layout fragment_menu
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.recyclerMenuID);
        recyclerView.setHasFixedSize(true); //Fijamos el tamaño en el layout del RecyclerView

        //API>23
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL)); //Lineas Divisorias en el Layout

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
