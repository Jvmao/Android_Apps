package com.josevicente.listadb.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.josevicente.listadb.Data.DataBaseHandler;
import com.josevicente.listadb.Model.Grocery;
import com.josevicente.listadb.R;
import com.josevicente.listadb.UI.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    //13-Declaramos las Variables del RecyclerView, List y DB
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listIems;
    private DataBaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //13-Introducimos la BBDD
        db = new DataBaseHandler(this);
        recyclerView = findViewById(R.id.recyclerViewID); //Añadimos el recyclerView desde content_list.xml
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Instanciamos los Objetos
        groceryList = new ArrayList<>();
        listIems = new ArrayList<>();
        //Conseguimos los items desde la BBDD
        groceryList=db.getAllGroceries();

        for(Grocery c: groceryList){
            Grocery grocery = new Grocery();
            grocery.setName(c.getName()); //Obtenemos el nombre del artículo de la actividad anterior
            grocery.setQuantity(c.getQuantity()); //Obtenemos la cantidad del popup Activity
            grocery.setId(c.getId()); //Obtenemos el ID
            grocery.setDateItem(c.getDateItem());

            listIems.add(grocery); //Añadimos los elementos a la lista
        }

        //13-Instanciamos un nuevo recyclerViewAdapter
        recyclerViewAdapter = new RecyclerAdapter(this,listIems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

    }

}
