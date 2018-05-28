package com.josevicente.ap2app.ShopActivities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.josevicente.ap2app.Activities.StartActivity;
import com.josevicente.ap2app.Adapters.GastoAdapter;
import com.josevicente.ap2app.Adapters.ShopAdapter;
import com.josevicente.ap2app.GestActivities.ContactoActivity;
import com.josevicente.ap2app.GestActivities.EventoActivity;
import com.josevicente.ap2app.Model.Gasto;
import com.josevicente.ap2app.Model.Producto;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.Utils.BottomNavigationViewBehavior;
import com.josevicente.ap2app.Utils.Constants;

import java.util.ArrayList;

public class ListaProductoActivity extends AppCompatActivity {
    //Declaramos las Variables
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String userProducto = Constants.userItem;

    //Le pasamos las Variables del RecyclerView
    private RecyclerView recyclerView;
    private ShopAdapter shopAdapter;
    private ArrayList<Producto> productoList;


    //Variables para el Alert Dialog
    private AlertDialog.Builder alertDialogBilder;
    private AlertDialog dialog;

    //Variables AlertDialog new_item.xml
    private EditText editNewProducto,editCantidadItem;
    private Button btnNewItem,btnOutItem;
    //Variables AlertDialog delete_product.xml
    private Button btnDeleteAll,btnOutDeleteAll;

    //Variables BottomMenu
    private BottomNavigationView btmNavShopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto);

        //Instanciamos las Variables
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference(userProducto);
        mAuth = FirebaseAuth.getInstance();

        //Instanciamos las Variables del Adapter
        productoList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerListaID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Instanciamos el NavigationBottom
        btmNavShopList = findViewById(R.id.btmNavShopList);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)btmNavShopList.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        btmNavShopList.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });

        listaProductos(); //Añadimos el método para mostrar la lista de los productos
        navigation(); //Añadimos la barra de navegación inferior

    }

    //Método para Listar los productos creados por el usuario
    public void listaProductos(){
        mUser = mAuth.getCurrentUser();
        String id = mUser.getUid();

        Query listaProducto = mDatabaseReference.orderByChild(Constants.userItemID).equalTo(id);
        listaProducto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productoList.clear();
                for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                    Producto p = dataSnap.getValue(Producto.class);
                    productoList.add(p);
                }
                //Añadimos el Adapter
                shopAdapter = new ShopAdapter(ListaProductoActivity.this,productoList);
                recyclerView.setAdapter(shopAdapter);
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("CANCELADO","Listado Cancelado");
            }
        });
    }

    //Método para mostrar el AlertDialog y guardar los datos en Firebase
    public void addNewItem(){
        alertDialogBilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.new_item,null);

        //Instanciamos las variables creadas en new_item.xml
        editNewProducto = view.findViewById(R.id.editNewProducto);
        editCantidadItem = view.findViewById(R.id.editCantidadItem);
        btnNewItem = view.findViewById(R.id.btnNewItem);
        btnOutItem = view.findViewById(R.id.btnOutItem);

        alertDialogBilder.setView(view);
        dialog = alertDialogBilder.create();
        dialog.show(); //Muestra new_item.xml

        //Listener btnNewItem
        btnNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String producto = editNewProducto.getText().toString();
                final String cantidad = editCantidadItem.getText().toString();

                if(!producto.isEmpty() && !cantidad.isEmpty()){
                    if(!TextUtils.isEmpty(producto) && !TextUtils.isEmpty(cantidad)){
                        //Obtenemos el id del usuario
                        mUser = mAuth.getCurrentUser();
                        String id = mUser.getUid();

                        //Guardamos los datos en la BBDD
                        Producto p = new Producto();
                        p.setId(id);
                        p.setProducto(producto);
                        p.setCantidad(cantidad);
                        DatabaseReference dbRef = mDatabaseReference.push();
                        dbRef.setValue(p);

                        limpiar();
                        Toast.makeText(v.getContext(),"Producto Guardado Correctamente",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        Intent intentProductos = new Intent(ListaProductoActivity.this,ListaProductoActivity.class);
                        startActivity(intentProductos);
                    }
                }else{
                    Toast.makeText(v.getContext(),"Faltan Datos por Rellenar",Toast.LENGTH_LONG).show();
                }
            }
        });

        //Listener btnOutItem
        btnOutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); //Cerramos el AlertDialog
            }
        });
    }

    //Método para limpiar los campos del AlertDialog cuando añadimos un nuevo producto
    private void limpiar(){
        editNewProducto.setText("");
        editCantidadItem.setText("");
    }

    //Método para cargar el AlertDialog que permite eliminar la lista completa
    private void eliminarLista(){
        alertDialogBilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.delete_product,null);

        //Instanciamos los elementos creados en delete_product.xml
        btnDeleteAll = view.findViewById(R.id.btnDeleteAll);
        btnOutDeleteAll = view.findViewById(R.id.btnOutDeleteAll);

        alertDialogBilder.setView(view);
        dialog = alertDialogBilder.create();
        dialog.show(); //Muestra delete_product.xml

        //Listener btnDeleteAll
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser = mAuth.getCurrentUser();
                String id = mUser.getUid();
                //Consulta para Eliminar todos los elementos de la Lista de la Compra
                Query deleteAllQuery = mDatabaseReference.orderByChild(Constants.userItemID).equalTo(id);
                deleteAllQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            //Recogemos las variables a eliminar
                            String id = snap.getKey();
                            mDatabaseReference.child(id).child(Constants.userItemID).removeValue();
                            mDatabaseReference.child(id).child(Constants.userProduct).removeValue();
                            mDatabaseReference.child(id).child(Constants.userCant).removeValue();

                            productoList.clear();//Eliminamos los elementos de la lista
                            shopAdapter.notifyDataSetChanged();
                            dialog.dismiss(); //Cerramos el AlerDialog
                            Toast.makeText(ListaProductoActivity.this,"Lista Eliminada",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("INFO","Operación de Borrado de Lista Cancelada");
                    }
                });
            }
        });

        //Listener btnOutDeleteAll
        btnOutDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); //Cierra el AlertDialog delete_product.xml
            }
        });
    }

    //Añadimos el menu menulista.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menulista,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mUser = mAuth.getCurrentUser();
        switch (item.getItemId()){
            case R.id.listaLogout:
                if(mUser !=null && mAuth !=null){
                    mAuth.signOut(); //Logout por parte del usuario para salir de la aplicación
                    Intent intentStart = new Intent(ListaProductoActivity.this,StartActivity.class);
                    startActivity(intentStart); //Nos devuelve a la pantalla Principal de Inicio
                    Toast.makeText(ListaProductoActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case R.id.listaInicio:
                //Nos devuelve a la pantalla de inicio del menu de Lista
                Intent intentInicioLista = new Intent(ListaProductoActivity.this,ListMainActivity.class);
                startActivity(intentInicioLista);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Agregamos el navigation del Menu Principal
    public void navigation(){
        btmNavShopList.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mUser = mAuth.getCurrentUser();
                switch (item.getItemId()){
                    case R.id.addItemList:
                        addNewItem(); //Añadimos el método para añadir un nuevo producto
                        break;
                    case R.id.deleteShopList:
                        //Añadimos el método para eliminar la lista completa de eventos
                        eliminarLista(); //Eliminamos la lista de la compra por completo
                        break;
                    case R.id.exitShopList:
                        Intent intentMainShop = new Intent(ListaProductoActivity.this,ListMainActivity.class);
                        startActivity(intentMainShop); //Nos devuelve a la pantalla de Agenda
                        Toast.makeText(ListaProductoActivity.this,"Inicio Lista",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
    }
}