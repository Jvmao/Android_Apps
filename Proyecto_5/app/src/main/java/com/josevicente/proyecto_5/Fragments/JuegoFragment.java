package com.josevicente.proyecto_5.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josevicente.proyecto_5.Modelo.Jugador;
import com.josevicente.proyecto_5.R;


public class JuegoFragment extends Fragment {
    //Declaramos los argumentos a pasar
    public static final String ARG_ALIAS = "param1";
    public static final String ARG_PUNTOS = "param2";
    private Jugador jugador = new Jugador();

    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public JuegoFragment() {}


    public static JuegoFragment newInstance(String param1, String param2) {
        JuegoFragment fragment = new JuegoFragment();
        if(param1 !=null && param2 !=null){
            Bundle args = new Bundle();
            args.putString(ARG_ALIAS, param1);
            args.putString(ARG_PUNTOS, param2);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_ALIAS);
            mParam2 = getArguments().getString(ARG_PUNTOS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_juego, container, false);
        //Declaramos las variables a recoger desde fragment_juego.xml
        final TextView alias = view.findViewById(R.id.nombreUsuario);
        final TextView puntos = view.findViewById(R.id.puntosUsuario);

        //Recogemos los parámetros en las variables declaradas
        //alias.setText(jugador.getNickname().toString());
        //puntos.setText(jugador.getPuntos()+" PUNTOS");
        alias.setText(mParam1.toString());
        puntos.setText(mParam2.toString()+" PUNTOS");

        return view;
    }

    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


    /*public interface OnFragmentInteractionListener {
        //void onFragmentInteraction(String nombre,String usario);
    }*/
}
