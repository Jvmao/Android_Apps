package com.josevicente.listadb.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.josevicente.listadb.R;

//14-Creamos una nueva Activity llamada DetailsActivity
public class DetailsActivity extends AppCompatActivity {
    private TextView itemName;
    private TextView quantity;
    private TextView dateAdded;
    private int groceryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Iniciamos las Variables
        itemName = findViewById(R.id.itemNameDetails);
        quantity = findViewById(R.id.itemCantidadDetails);
        dateAdded = findViewById(R.id.itemDateDetails);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            itemName.setText(bundle.getString("Artículo: ")); //Obtenemos el key desde RecyclerAdapter
            quantity.setText(bundle.getString("Cantidad: "));
            dateAdded.setText(bundle.getString("Date: "));
            groceryId = bundle.getInt("id");
        }
    }
}
