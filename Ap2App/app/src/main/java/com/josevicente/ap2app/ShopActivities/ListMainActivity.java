package com.josevicente.ap2app.ShopActivities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josevicente.ap2app.Activities.MenuActivity;
import com.josevicente.ap2app.Activities.StartActivity;
import com.josevicente.ap2app.EcoActivities.EcoActivity;
import com.josevicente.ap2app.GestActivities.GestMainActivity;
import com.josevicente.ap2app.MapsActivities.MapMainActivity;
import com.josevicente.ap2app.Model.Producto;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.Utils.BottomNavigationViewBehavior;
import com.josevicente.ap2app.Utils.Constants;
import com.josevicente.ap2app.Web.WebActivity;

public class ListMainActivity extends AppCompatActivity {
    //Variables
    private EditText editNewProducto,editCantidadItem;
    private Button btnNewItem,btnOutItem;
    private ImageButton imageBtnItem,imageBtnListID;

    //Variables Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String userItem = Constants.userItem;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    //Añadimos las Variables para el Alert Dialog
    private AlertDialog.Builder alertDialogBilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    //Variables BottomMenu
    private BottomNavigationView bottomNavigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        //Instanciamos las Variables
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child(userItem); //Creamos la nueva Tabla en la BBDD llamada Lista
        mAuth = FirebaseAuth.getInstance();
        imageBtnItem = findViewById(R.id.imageBtnItem);
        imageBtnListID = findViewById(R.id.imageBtnListID);

        //Listener imageBtnItem para que muestre el AlertDialog new_item.xml
        imageBtnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItem();
            }
        });

        bottomNavigationMenu = findViewById(R.id.navShop1);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)bottomNavigationMenu.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });

        navigation(); //Añadimos el navegador en la activity

        //Listener para pasar a la pantalla de la lista de la compra
        imageBtnListID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasamos a ListaProductoActivity
                Intent intentLista = new Intent(ListMainActivity.this,ListaProductoActivity.class);
                startActivity(intentLista);
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
                        DatabaseReference dbRef = databaseReference.push();
                        dbRef.setValue(p);

                        limpiar();
                        Toast.makeText(v.getContext(),"Producto Guardado Correctamente",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        Intent intentProductos = new Intent(ListMainActivity.this,ListaProductoActivity.class);
                        startActivity(intentProductos);
                    }
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

    //Método para limpiar los campos del AlertDialog
    private void limpiar(){
        editNewProducto.setText("");
        editCantidadItem.setText("");
    }


    //Añadimos el menu3.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mUser = mAuth.getCurrentUser();
        switch (item.getItemId()){
            case R.id.itemLogout:
                if(mUser !=null && mAuth !=null){
                    mAuth.signOut(); //Logout por parte del usuario para salir de la aplicación
                    Intent intentStart = new Intent(ListMainActivity.this,StartActivity.class);
                    startActivity(intentStart); //Nos devuelve a la pantalla Principal de Inicio
                    Toast.makeText(ListMainActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case R.id.itemMain:
                //Pasamos a MenuActivity
                Intent intentMain = new Intent(ListMainActivity.this,MenuActivity.class);
                startActivity(intentMain);
                break;

            case R.id.itemEco:
                //Nos devuelve a la pantalla de EcoActivity
                Intent intentEco = new Intent(ListMainActivity.this, EcoActivity.class);
                startActivity(intentEco);
                break;

            case R.id.itemMapa:
                //Nos devuelve a la pantalla MapMainActivity
                Intent intentMap = new Intent(ListMainActivity.this, MapMainActivity.class);
                startActivity(intentMap);
                break;

            case R.id.itemGest:
                //Nos devuelve a la pantalla GestMainActivity
                Intent intentGest = new Intent(ListMainActivity.this, GestMainActivity.class);
                startActivity(intentGest);
                break;

            case R.id.itemLista:
                Toast.makeText(ListMainActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_LONG).show();
                break;

            case R.id.itemWeb:
                //Nos devuelve a la pantalla WebActivity
                Intent intentWeb = new Intent(ListMainActivity.this,WebActivity.class);
                startActivity(intentWeb);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Agregamos el navigation del Menu Principal
    public void navigation(){
        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mUser = mAuth.getCurrentUser();
                switch (item.getItemId()){
                    case R.id.btnInicioMenuB:
                        Intent intentMenu = new Intent(ListMainActivity.this,MenuActivity.class);
                        startActivity(intentMenu);
                        break;
                    case R.id.btnSalirMenuB:
                        if(mUser !=null && mAuth !=null){
                            mAuth.signOut(); //Logout por parte del usuario para salir de la aplicación
                            Intent intentStart = new Intent(ListMainActivity.this,StartActivity.class);
                            startActivity(intentStart); //Nos devuelve a la pantalla Principal de Inicio
                            Toast.makeText(ListMainActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                            finish();
                        }
                        break;
                }
                return true;
            }
        });
    }
}
