package com.josevicente.proyecto_finalpmm.Fragments;

import android.app.Activity;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josevicente.proyecto_finalpmm.R;

import java.util.HashMap;
import java.util.Map;


public class PerfilFragment extends Fragment {
    //Variables Objeto Jugador
    btnListener btnGuardar;
    String nombreUsuario;
    String apellidoUsuario;
    int edadUsuario;
    String nickUsuario;

    //Variables FireBase
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public static final String userIndex = "Usuarios"; //Nombre de la Tabla en la BBDD


    //Interface btnListener
    public interface btnListener{
        //Le pasamos los campos a recoger
        void onClickButton(String nombreUsuario,String nickUsuario);
    }

    public PerfilFragment() {} //Constructor Vacío

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

        //Instanciamos las Variables Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(userIndex);

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

                    //Comprobamos que los datos se recogen correctamente
                    Log.i("NOMBRE ",nombreUsuario);
                    Log.i("NICK PERFIL ",nickUsuario);
                    Log.i("APELLIDO ",apellidoUsuario);
                    Log.i("EDAD ",String.valueOf(edadUsuario));

                    //Guardamos los Datos en la BBDD
                    DatabaseReference databaseReference1 = databaseReference.push();
                    Map<String, String> dataToSave = new HashMap<>();

                    dataToSave.put("Nombre",nombreUsuario);
                    dataToSave.put("Apellido",apellidoUsuario);
                    dataToSave.put("Edad",String.valueOf(edadUsuario));
                    dataToSave.put("NickName",nickUsuario);
                    databaseReference1.setValue(dataToSave);

                }else{
                    Toast.makeText(getActivity(),"Faltan Campos por Rellenar",Toast.LENGTH_LONG).show();
                }
            }
        });
        return  v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof btnListener) {
            btnGuardar = (btnListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement btnListener");
        }
    }

    //Método necesario para API<23
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            btnGuardar = (btnListener) activity;
        }catch (ClassCastException ex){
            throw new ClassCastException(activity.toString()+" must implement btnListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
