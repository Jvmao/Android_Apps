package com.josevicente.ap2app.EcoActivities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.josevicente.ap2app.Activities.MenuActivity;
import com.josevicente.ap2app.Activities.StartActivity;
import com.josevicente.ap2app.Adapters.GastoAdapter;
import com.josevicente.ap2app.Model.Gasto;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.ShopActivities.ListaProductoActivity;
import com.josevicente.ap2app.Utils.BottomNavigationViewBehavior;
import com.josevicente.ap2app.Utils.Constants;

import java.util.ArrayList;

public class GestGastoActivity extends AppCompatActivity {
    //Declaramos las Variables
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String userEx = Constants.userExpenses;

    //Le pasamos las Variables del RecyclerView
    private RecyclerView recyclerView;
    private GastoAdapter gastoAdapter;
    private ArrayList<Gasto> gastoList;

    //Variable Spinner
    private Spinner spinnerGastoMeses;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    //Variables BottomMenu
    private BottomNavigationView bottomNavigationMenu;

    //Variables para el Alert Dialog
    private AlertDialog.Builder alertDialogBilder;
    private AlertDialog dialog;
    private ImageButton iBtnDeleteCostList,iBtnExitCostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gest_gasto);

        //Instanciamos las Variables
        spinnerGastoMeses = findViewById(R.id.spinnerGastoMesesID);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference(userEx);
        mAuth = FirebaseAuth.getInstance();

        bottomNavigationMenu = findViewById(R.id.bottomNavigation);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)bottomNavigationMenu.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });

        try{
            //Instanciamos el Adapter del Spinner
            spinnerAdapter = ArrayAdapter.createFromResource(GestGastoActivity.this,R.array.spinnerGastoMes, android.R.layout.simple_list_item_1);
            //Especificamos el layout a usar cuando desplegamos la lista
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            //Aplicamos el adapter a la lista
            spinnerGastoMeses.setAdapter(spinnerAdapter);
        }catch(Exception e){
            System.out.print(e);
        }

        //Instanciamos las Variables del Adapter
        gastoList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerGastoID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //listaGastos(); //Añadimos el método para listar los gastos
        spinnerMes(); //Añadimos el método para listar los gastos por mes

        //Cargamos el Método navigation()
        navigation();
    }

    //Método para listar los elementos
    public void listaGastos(){
        mUser = mAuth.getCurrentUser();
        String id = mUser.getUid();

        Query listItems = mDatabaseReference.orderByChild(Constants.userIDExpenses).equalTo(id);
        listItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gastoList.clear();
                for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                    Gasto gasto = dataSnap.getValue(Gasto.class);
                    gastoList.add(gasto);
                }
                //Añadimos el Recycler
                gastoAdapter = new GastoAdapter(GestGastoActivity.this,gastoList);
                recyclerView.setAdapter(gastoAdapter);
                gastoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Método para clasificar los gastos mediante el spinner de meses
    public void spinnerMes(){
        spinnerGastoMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Recogemos el nombre del elemento en el spinner por posición y se muestra en un Snackbar
                final String pos = (String) parent.getItemAtPosition(position);
                //Snackbar.make(getWindow().getDecorView(),"Gastos de "+pos,Snackbar.LENGTH_LONG).show();
                Toast.makeText(GestGastoActivity.this,"Gastos de "+pos,Toast.LENGTH_LONG).show();

                //Definimos el query para filtrar los resultados a partir del mes y la posición del elemento en el spinner
                mUser = mAuth.getCurrentUser();
                final String userId = mUser.getUid();

                Query queryFilter = mDatabaseReference.orderByChild(Constants.userMonth).equalTo(pos);
                queryFilter.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //listaGastos();
                        gastoList.clear();
                        //Adaptamos el recyclerview a la lista
                        for(DataSnapshot snap: dataSnapshot.getChildren()){
                            Gasto gasto = snap.getValue(Gasto.class);
                            gastoList.add(gasto);
                        }
                        gastoAdapter = new GastoAdapter(GestGastoActivity.this,gastoList);
                        recyclerView.setAdapter(gastoAdapter);
                        gastoAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    //Añadimos el menueco.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menueco,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mUser = mAuth.getCurrentUser();
        switch (item.getItemId()){
            case R.id.ecoLogout:
                if(mUser !=null && mAuth !=null){
                    mAuth.signOut(); //Logout por parte del usuario para salir de la aplicación
                    Intent intentEco = new Intent(GestGastoActivity.this,StartActivity.class);
                    startActivity(intentEco); //Nos devuelve a la pantalla Principal de Inicio
                    Toast.makeText(GestGastoActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case R.id.ecoStart:
                Intent intentStartEco = new Intent(GestGastoActivity.this,EcoActivity.class);
                startActivity(intentStartEco);
                break;

            case R.id.ecoGasto:
                Intent intentAddGasto = new Intent(GestGastoActivity.this,GastoActivity.class);
                startActivity(intentAddGasto);
                break;

            case R.id.ecoListaGasto:
                Toast.makeText(GestGastoActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_LONG).show();
                break;

            case R.id.ecoCharts:
                Intent intentCharts = new Intent(GestGastoActivity.this,ChartsActivity.class);
                startActivity(intentCharts);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //Agregamos el navigation del Menu de Navegación
    public void navigation(){
        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mUser = mAuth.getCurrentUser();
                switch (item.getItemId()){
                    case R.id.leftArrowEco1:
                        Intent intentAddGasto = new Intent(GestGastoActivity.this,GastoActivity.class);
                        startActivity(intentAddGasto);
                        Toast.makeText(GestGastoActivity.this,"Añadir Gastos",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.btnDeleteListEx:
                        deleteListaGastos();
                        //Toast.makeText(GestGastoActivity.this,"Under Construction",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.rightArrowEco1:
                        Intent intentChart = new Intent(GestGastoActivity.this,ChartsActivity.class);
                        startActivity(intentChart); //Nos devuelve a la pantalla Principal de Inicio
                        Toast.makeText(GestGastoActivity.this,"Gráfico de Gastos",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
    }

    //Método que permite eliminar la lista de gastos
    private void deleteListaGastos(){
        alertDialogBilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.delete_list_cost,null);

        //Instanciamos los ImageButton creados en delete_list_cost
        iBtnDeleteCostList = view.findViewById(R.id.iBtnDeleteCostList);
        iBtnExitCostList = view.findViewById(R.id.iBtnExitCostList);

        alertDialogBilder.setView(view);
        dialog = alertDialogBilder.create();
        dialog.show(); //Muestra delete_product.xml

        //Listener iBtnDeleteCostList
        iBtnDeleteCostList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser = mAuth.getCurrentUser();
                String id = mUser.getUid();
                //Consulta para Eliminar todos los elementos de la Lista de la Compra
                Query deleteAllCostList = mDatabaseReference.orderByChild(Constants.userIDExpenses).equalTo(id);
                deleteAllCostList.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            //Recogemos las variables a eliminar
                            String id = snap.getKey();
                            mDatabaseReference.child(id).child(Constants.userIDExpenses).removeValue();
                            mDatabaseReference.child(id).child(Constants.userConcept).removeValue();
                            mDatabaseReference.child(id).child(Constants.userMonth).removeValue();
                            mDatabaseReference.child(id).child(Constants.userAmount).removeValue();

                            gastoList.clear();//Eliminamos los elementos de la lista
                            gastoAdapter.notifyDataSetChanged();
                            dialog.dismiss(); //Cerramos el AlerDialog
                            Toast.makeText(GestGastoActivity.this,"Lista de Gastos Eliminada",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //Listener iBtnExitCostList
        iBtnExitCostList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); //Salimos del Alert Dialog
            }
        });
    }
}
