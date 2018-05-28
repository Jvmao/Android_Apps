package com.josevicente.ap2app.EcoActivities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.josevicente.ap2app.Activities.StartActivity;
import com.josevicente.ap2app.Model.Gasto;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.Utils.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChartsActivity extends AppCompatActivity {
    //Variables BBDD
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    private String gasto = Constants.userExpenses;

    //Variables Gráficos
    private PieChart pieChart;

    //Variable Spinner
    private Spinner spinnerChart;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        //Instanciamos las Variables Firebase
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child(gasto);
        mAuth = FirebaseAuth.getInstance();

        //Instanciamos las Variables PieChart
        pieChart = findViewById(R.id.pieChartID);

        //Instanciamos el Spinner
        spinnerChart = findViewById(R.id.spinnerChart);

        try{
            //Instanciamos el Adapter del Spinner
            spinnerAdapter = ArrayAdapter.createFromResource(ChartsActivity.this,R.array.spinnerGastoMes, android.R.layout.simple_list_item_1);
            //Especificamos el layout a usar cuando desplegamos la lista
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            //Aplicamos el adapter a la lista
            spinnerChart.setAdapter(spinnerAdapter);
        }catch(Exception e){
            System.out.print(e);
        }

        //setChart(); //Agregamos el método del Gráfico
        spinnerChartMes(); //Agregamos el método para que muestre los gráficos agrupados por mes
    }

    //Definimos el Método para establecer los datos en el gráfico sin el spinner
    /*public void setChart(){
        mUser = mAuth.getCurrentUser();
        String id = mUser.getUid();

        //Propiedades Gráfico
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(25);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.setCenterText("GASTOS");
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(100);
        pieChart.setRotationEnabled(true);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.animateY(500, Easing.EasingOption.EaseInElastic);

        //Propiedades Leyenda del Gráfico
        Legend legend = pieChart.getLegend();
        legend.setTextSize(15f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setXEntrySpace(5);
        legend.setYEntrySpace(5);


        //Consulta en la BBDD
        databaseReference.orderByChild(Constants.userIDExpenses).equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<PieEntry> values = new ArrayList<>();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Gasto g = data.getValue(Gasto.class);
                    try{
                        //Pasamos los valores guardados en Firebase al gráfico
                        values.add(new PieEntry(Float.parseFloat(g.getImporte()),g.getConcepto(),g.getMes()));
                        //values.add(new PieEntry(Float.parseFloat(g.getImporte()),g.getMes()));
                    }catch (NumberFormatException ex){
                        Log.e("FLOAT ERROR",ex.getMessage());
                    }

                }

                //Propiedades texto descripción
                Description description = new Description();
                description.setText("MIS GASTOS");
                description.setTextSize(15);
                pieChart.setDescription(description);

                final PieDataSet pieDataSet = new PieDataSet(values,"");
                pieDataSet.setSliceSpace(4f);
                pieDataSet.setSelectionShift(5f);
                pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);


                PieData pieData = new PieData(pieDataSet);
                pieData.setValueFormatter(new PercentFormatter());
                pieData.setValueTextSize(15f);
                pieData.setValueTextColor(Color.BLACK);

                pieChart.setData(pieData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    //Definimos el Método para establecer los datos en el gráfico con el spinner
    private void spinnerChartMes(){
        spinnerChart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Recogemos el nombre del elemento en el spinner por posición y se muestra en un Snackbar
                final String pos = (String) parent.getItemAtPosition(position);
                //Snackbar.make(getWindow().getDecorView(),"Gastos de "+pos,Snackbar.LENGTH_LONG).show();
                Toast.makeText(ChartsActivity.this,"Gastos de "+pos,Toast.LENGTH_SHORT).show();

                //Definimos el query para filtrar los resultados a partir del mes y la posición del elemento en el spinner
                mUser = mAuth.getCurrentUser();
                final String userId = mUser.getUid();

                //Propiedades Gráfico
                pieChart.setUsePercentValues(true);
                pieChart.getDescription().setEnabled(true);
                pieChart.setExtraOffsets(5,10,5,5);
                pieChart.setDragDecelerationFrictionCoef(0.95f);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleRadius(25);
                pieChart.setHoleColor(Color.BLACK);
                pieChart.setCenterText("GASTOS");
                pieChart.setCenterTextColor(Color.WHITE);
                pieChart.setTransparentCircleAlpha(100);
                pieChart.setRotationEnabled(true);
                pieChart.setTransparentCircleRadius(40f);
                pieChart.animateY(500, Easing.EasingOption.EaseInElastic);

                //Propiedades Leyenda del Gráfico
                Legend legend = pieChart.getLegend();
                legend.setTextSize(15f);
                legend.setForm(Legend.LegendForm.CIRCLE);
                legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setXEntrySpace(5);
                legend.setYEntrySpace(5);

                Query queryChartMonth = databaseReference.orderByChild(Constants.userMonth).equalTo(pos);
                queryChartMonth.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<PieEntry> values = new ArrayList<>();
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            Gasto g = data.getValue(Gasto.class);
                            try{
                                //Pasamos los valores guardados en Firebase al gráfico
                                values.add(new PieEntry(Float.parseFloat(g.getImporte()),g.getConcepto(),g.getMes()));
                            }catch (NumberFormatException ex){
                                Log.e("FLOAT ERROR",ex.getMessage());
                            }
                        }

                        //Propiedades texto descripción
                        Description description = new Description();
                        description.setText("Mis Gastos de "+pos);
                        description.setTextSize(10);
                        pieChart.setDescription(description);

                        final PieDataSet pieDataSet = new PieDataSet(values,"");
                        pieDataSet.setSliceSpace(4f);
                        pieDataSet.setSelectionShift(5f);
                        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);


                        PieData pieData = new PieData(pieDataSet);
                        pieData.setValueFormatter(new PercentFormatter());
                        pieData.setValueTextSize(15f);
                        pieData.setValueTextColor(Color.BLACK);

                        pieChart.setData(pieData);
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
                    Intent intentEco = new Intent(ChartsActivity.this,StartActivity.class);
                    startActivity(intentEco); //Nos devuelve a la pantalla Principal de Inicio
                    Toast.makeText(ChartsActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case R.id.ecoStart:
                Intent intentStartEco = new Intent(ChartsActivity.this,EcoActivity.class);
                startActivity(intentStartEco);
                break;

            case R.id.ecoGasto:
                Intent intentAddGasto = new Intent(ChartsActivity.this,GastoActivity.class);
                startActivity(intentAddGasto);
                break;

            case R.id.ecoListaGasto:
                Intent intentListaGasto = new Intent(ChartsActivity.this,GestGastoActivity.class);
                startActivity(intentListaGasto);
                break;

            case R.id.ecoCharts:
                Toast.makeText(ChartsActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
