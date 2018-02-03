package com.josevicente.proyecto_5.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.josevicente.proyecto_5.R;


public class PerfilFragment extends Fragment {
    //Variables Objeto Jugador
    btnListener btnGuardar;
    String nombreUsuario;
    String apellidoUsuario;
    int edadUsuario;
    String nickUsuario;

    //private OnFragmentInteractionListener mListener;

    //Interface btnListener
    public interface btnListener{
        //Le pasamos los campos a recoger
        void onClickButton(String nombreUsuario,String nickUsuario);
    }

    //Constructor Vacío
    public PerfilFragment() {}


    /*public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el layout para este fragment
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Instanciamos las variables definidas en fragment_perfil.xml
        final EditText nameUser = v.findViewById(R.id.editNombre);
        final EditText lastNameUser = v.findViewById(R.id.editApellido);
        final EditText ageUser = v.findViewById(R.id.editEdad);
        final EditText nickUser = v.findViewById(R.id.editNick);
        final Button btnSave = v.findViewById(R.id.btnJugador);

        //Listener btnSave
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameUser.getText().toString().isEmpty() && !lastNameUser.getText().toString().isEmpty()
                        && !ageUser.getText().toString().isEmpty() && !nickUser.getText().toString().isEmpty()){
                    //Guardamos los valores
                    nombreUsuario = nameUser.getText().toString();
                    apellidoUsuario = lastNameUser.getText().toString();
                    edadUsuario = Integer.parseInt(ageUser.getText().toString());
                    nickUsuario = nickUser.getText().toString();

                    btnGuardar.onClickButton(nombreUsuario,nickUsuario); //Le pasamos los datos
                    Toast.makeText(v.getContext(),"Bienvenido: "+nombreUsuario+" tu Alias es: "+nickUsuario,Toast.LENGTH_LONG).show();

                    //Comprobamos que los datos se recogen correctamente
                    Log.i("NOMBRE ",nombreUsuario);
                    Log.i("NICK PERFIL ",nickUsuario);
                    Log.i("APELLIDO ",apellidoUsuario);
                    Log.i("EDAD ",String.valueOf(edadUsuario));

                }else{
                    Toast.makeText(getActivity(),"Faltan Campos por Rellenar",Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }

    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof btnListener) {
            btnGuardar = (btnListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement btnListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    /*public interface OnFragmentInteractionListener {
        //void onFragmentInteraction(Uri uri);
    }*/


}
