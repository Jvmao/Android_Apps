package com.josevicente.listadb.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.josevicente.listadb.Data.DataBaseHandler;
import com.josevicente.listadb.Model.Grocery;
import com.josevicente.listadb.R;

public class MainActivity extends AppCompatActivity {
    //1-Definimos las Variables
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText articulo;
    private EditText cantidad;
    private Button saveButton;

    //11-Definimos las Variables para manejar la BBDD
    private DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //11-Instanciamos la BBDD
        db = new DataBaseHandler(this);

        //15-Añadimos el método byPass
        //byPassActivity();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                //Añadimos el PopupDialog
                createPopupDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intentList = new Intent(MainActivity.this,ListActivity.class);
            startActivity(intentList);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //2- Creamos un Popup Dialog
    private void createPopupDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);

        //Añadimos los elementos al View
        articulo = view.findViewById(R.id.groceryItem);
        cantidad = view.findViewById(R.id.groceryQty);
        saveButton = view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        //Añadimos el Listener del Botón
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //11-Si los datos que pasamos no están vacios los guardamos en la BBDD
                if(!articulo.getText().toString().isEmpty() && !cantidad.getText().toString().isEmpty()){
                    //Guardamos los Datos en la Base de Datos con el método saveGroceryToDB
                    saveGroceryToDB(v);
                }
            }
        });
    }

    //4-Creamos el Método para saveButton los datos
    private void saveGroceryToDB(View v){
        //11-Creamos un nuevo objeto y las nuevas variables a pasar desde la opción popup al recyclerview
        Grocery grocery = new Grocery();

        String newItem = articulo.getText().toString();
        String newItemQty = cantidad.getText().toString();

        grocery.setName(newItem);
        grocery.setQuantity(newItemQty);
        //Guardamos el objeto en la BBDD
        db.addElement(grocery);

        //Mensajes de Confirmación de que la operación se ha realizado con Éxito
        Snackbar.make(v,"Artículo Guardado",Snackbar.LENGTH_LONG).show();
        Log.d("ID Artículo",String.valueOf(db.getGroceriesCount()));

        //12-Pasamos los Datos a la siguiente Actividad con un retraso de 1 segundo
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //Empezamos una nueva actividad y pasamos a la siguiente con un Intent
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },1000);
    }

    //15-Creamos un nuevo método para saber si tenemos elementos en la BBDD y existen nos lleva directamente a la lista de elementos y lo añadimos en onCreate
    public void byPassActivity(){
        if(db.getGroceriesCount()>0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }
}
