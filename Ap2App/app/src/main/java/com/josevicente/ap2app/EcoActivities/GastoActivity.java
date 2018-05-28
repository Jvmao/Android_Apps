package com.josevicente.ap2app.EcoActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josevicente.ap2app.Activities.StartActivity;
import com.josevicente.ap2app.Model.Gasto;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.Utils.BottomNavigationViewBehavior;
import com.josevicente.ap2app.Utils.Constants;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class GastoActivity extends AppCompatActivity {
    //Variables
    private EditText editConcepto,editImporte;
    private Button btnGasto;
    private Spinner spinnerMeses;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    //Variables BBDD
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    private String gasto = Constants.userExpenses;

    //Variables progressDialog
    ProgressDialog mDialog;

    //Variables BottomMenu
    private BottomNavigationView btnNavGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        //Instanciamos las Variables
        editConcepto = findViewById(R.id.editConcepto);
        editImporte = findViewById(R.id.editImporte);
        btnGasto = findViewById(R.id.btnGasto);
        spinnerMeses = findViewById(R.id.spinnerMeses);

        //Instanciamos el adapter para el spinner creado en activity_gasto.xml
        spinnerAdapter = ArrayAdapter.createFromResource(GastoActivity.this,R.array.spinnerMes,android.R.layout.simple_list_item_1);
        //Especificamos el layout a usar cuando desplegamos la lista
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        //Aplicamos el adapter a la lista
        spinnerMeses.setAdapter(spinnerAdapter);

        //Instanciamos las Variables Firebase
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child(gasto);
        mAuth = FirebaseAuth.getInstance();

        //Instanciamos las Variables del Menú de Navegación
        btnNavGastos = findViewById(R.id.btnNavGastos);


        btnNavGastos.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });

        //Añadimos el método de la barra de Navegación
        navigation();

        //Listener btnGasto
        btnGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Añadimos el método para añadir gastos
                addExpenses();
            }
        });
    }

    //Método para añadir los Gastos en la BBDD de Firebase
    public void addExpenses(){
        //Definimos las Variables a añadir en la BBDD
        final String concepto = editConcepto.getText().toString();
        final String mes = spinnerMeses.getSelectedItem().toString();
        final String importe = editImporte.getText().toString();

        if((!concepto.isEmpty()) && (!mes.isEmpty()) && (!importe.isEmpty())){
            //mDialog.setMessage("Creando Nuevo Artículo");
            //mDialog.show();

            if(!TextUtils.isEmpty(concepto) && !TextUtils.isEmpty(mes) && !TextUtils.isEmpty(importe)){
                mUser = mAuth.getCurrentUser();
                String id = mUser.getUid();

                //Guardamos los Datos en la BBDD
                Gasto gasto = new Gasto();
                gasto.setId(id);
                gasto.setConcepto(concepto);
                gasto.setMes(mes);
                gasto.setImporte(importe);
                DatabaseReference dbRef = databaseReference.push();
                dbRef.setValue(gasto);

                Toast.makeText(GastoActivity.this,"Gasto Guardado Correctamente",Toast.LENGTH_LONG).show();
                limpiar();
                Intent intentLista = new Intent(GastoActivity.this,GestGastoActivity.class);
                startActivity(intentLista);
            }
        }else{
            Toast.makeText(GastoActivity.this,"Faltan Datos por Completar",Toast.LENGTH_LONG).show();
        }
    }

    //Método para limpiar Campos de Texto cuando se añade un nuevo Gasto
    public void limpiar(){
        editConcepto.setText("");
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1); //Reinicia el spinner
        spinnerMeses.setAdapter(spinnerAdapter);
        editImporte.setText("");
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
                    Intent intentEco = new Intent(GastoActivity.this,StartActivity.class);
                    startActivity(intentEco); //Nos devuelve a la pantalla Principal de Inicio
                    Toast.makeText(GastoActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case R.id.ecoStart:
                Intent intentStartEco = new Intent(GastoActivity.this,EcoActivity.class);
                startActivity(intentStartEco);
                break;

            case R.id.ecoGasto:
                Toast.makeText(GastoActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_LONG).show();
                break;

            case R.id.ecoListaGasto:
                Intent intentListaGasto = new Intent(GastoActivity.this,GestGastoActivity.class);
                startActivity(intentListaGasto);
                break;

            case R.id.ecoCharts:
                Intent intentCharts = new Intent(GastoActivity.this,ChartsActivity.class);
                startActivity(intentCharts);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Agregamos el navigation del Menu de Navegación
    public void navigation(){
        btnNavGastos.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mUser = mAuth.getCurrentUser();
                switch (item.getItemId()){
                    case R.id.listEcoNav:
                        Intent intentListGasto = new Intent(GastoActivity.this,GestGastoActivity.class);
                        startActivity(intentListGasto); //Nos envia a la pantalla de Lista de Gastos
                        Toast.makeText(GastoActivity.this,"Lista de Gastos",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.chartEcoNav:
                        Intent intentChart = new Intent(GastoActivity.this,ChartsActivity.class);
                        startActivity(intentChart); //Nos envia a la Pantalla de Gráficos de Gastos
                        Toast.makeText(GastoActivity.this,"Gráfico de Gastos",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.exitEcoNav:
                        Intent intentGastos = new Intent(GastoActivity.this,EcoActivity.class);
                        startActivity(intentGastos); //Nos envia a la pantalla principal de Mis Gastos
                        Toast.makeText(GastoActivity.this,"Mis Gastos",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
}
