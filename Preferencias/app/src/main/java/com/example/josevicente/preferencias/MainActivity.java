package com.example.josevicente.preferencias;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Definimos las Variables
    private EditText editText1,editText2,editText3;
    private CheckBox check1,check2;
    private Button btn1,btn2;
    private SharedPreferences preferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 =  findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check1.isChecked()){
                    check2.setChecked(false);
                }
            }
        });

        check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check2.isChecked()){
                    check1.setChecked(false);
                }
            }
        });

        //Añadimos el Listener del Botón para Guardar los Datos
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarPreferencias(); //Añadimos el método para guardar las preferencias
                Toast.makeText(getApplicationContext(),"Datos Guardados con Éxito",Toast.LENGTH_LONG).show();
            }
        });

        //Añadimos el Listener del Botón para Cargar los Datos
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperarDatos();
            }
        });

    }

    //Definimos el Método para Guardar las Preferencias
    public void guardarPreferencias(){
        preferencia=getSharedPreferences("Datos Usuario", Context.MODE_PRIVATE); //Definimos la variable sharedPreferences
        SharedPreferences.Editor editor = preferencia.edit(); //definimos el editor de las etiquetas

        String nombre = editText1.getText().toString(); //definimos el string del nombre con el valor del editText1
        editor.putString("Nombre",nombre);//Añadimos el nombre al archivo XML

        String dni = editText2.getText().toString();//Definimos la variable dni para pasarla a String
        editor.putString("Dni",dni);//Introducimos las Variable en el archivo XML

        String date = editText3.getText().toString();//Definimos las Variable date como un string
        editor.putString("Fecha Nacimiento",date);//Añadimos el valor de date al archivo XML

        boolean ck1=check1.isChecked(); //boolean para comprobar el estado del checkbox
        if(ck1 != false){
            editor.putBoolean("Hombre",true); //Añadimos el checkbox al archivo XML
        }else{
            editor.putBoolean("Hombre",false); //Añadimos el checkbox al archivo XML
        }

        boolean ck2=check2.isChecked(); //boolean para comprobar el estado del checkbox
        if(ck2 !=false){
            editor.putBoolean("Mujer",true);
        }else{
            editor.putBoolean("Mujer",false);
        }
        editor.commit();//Lanzamos el Editor
    }

    public void recuperarDatos(){
        preferencia=getSharedPreferences("Datos Usuario", Context.MODE_PRIVATE);
        String nombre = preferencia.getString("Nombre"," ");
        String dni = preferencia.getString("Dni"," ");
        String date = preferencia.getString("Fecha Nacimiento"," ");
        boolean hombre = false;
        if(check1.isChecked()){
            hombre = preferencia.getBoolean("Hombre",true);
        }
        boolean mujer = false;
        if(check2.isChecked()){
            mujer = preferencia.getBoolean("Mujer",true);
        }
        Toast.makeText(getApplicationContext(),"--Datos Recuperados-- "+"\n"+"Nombre: "+nombre
                +"\n"+"DNI: "+dni+"\n"+"Fecha: "+date
                +"\n"+"Hombre: "+hombre+"\n"+"Mujer: "+mujer,Toast.LENGTH_LONG).show();
    }
}
