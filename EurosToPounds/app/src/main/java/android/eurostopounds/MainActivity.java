package android.eurostopounds;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Variables
    private EditText editTextInfo1,editTextInfo2;
    private Button buttonChange,buttonRestart;
    private TextView textViewResult;

    //Método para Reiniciar la Consulta
    public void restart(){
        editTextInfo1.setText("");
        editTextInfo2.setText("");
        textViewResult.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Definimos las Variables
        editTextInfo1 = (EditText) findViewById(R.id.editText1);
        editTextInfo2 = (EditText) findViewById(R.id.editText2);
        buttonChange = (Button) findViewById(R.id.button1);
        textViewResult = (TextView) findViewById(R.id.textView3);
        buttonRestart = (Button) findViewById(R.id.button2);

        //Añadimos el Action Listener al Botón
        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lógica Conversión
                // 1 euro = 0.8978 a 26/10/2017

                double valorPound = 0.8978;
                double result = 0.0; //Inicializamos la Variable Resultado

                //Estructura Condicional
                if(editTextInfo1.getText().toString().equals("")){
                    textViewResult.setText("Invalid Value");
                    textViewResult.setTextColor(Color.YELLOW);
                }else{
                    double exchange = Double.parseDouble(editTextInfo1.getText().toString()); //Pasamos el Valor a String
                    result = exchange*valorPound; //Operación a Realizar

                    editTextInfo2.setText(String.format("%.3f",+result)); //Indicamos el resultado con tres decimales
                }

            }
        });

        //Listener para el Botón Restart
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart(); //Añadimos el Método Restart para reiniciar la Aplicación
            }
        });
    }
}
