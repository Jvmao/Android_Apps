package com.josevicente.proyecto_finalpmm.Fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josevicente.proyecto_finalpmm.R;


public class JuegoFragment extends Fragment {
    //Declaramos los argumentos a pasar
    public static final String ARG_ALIAS = "param1";
    public static final String ARG_PUNTOS = "param2";

    private String mParam1;
    private String mParam2;

    MediaPlayer mediaPlayer = null;

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
        alias.setText(mParam1);
        puntos.setText(mParam2+" PUNTOS");

        //Añadimos una camción de fondo al Fragment Juego
        mediaPlayer = MediaPlayer.create(getActivity(),R.raw.song3);
        mediaPlayer.start();

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


    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.stop(); //Paramos la música de fondo
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop(); //Se detiene la música de fondo si se destruye la Actvity
        }
    }
}
