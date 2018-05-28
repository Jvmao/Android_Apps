package com.josevicente.ap2app.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josevicente.ap2app.R;

public class LoginActivity extends AppCompatActivity {
    //Variables activity_login.xml
    private EditText editMailLogin,editPassLogin;
    private Button btnStart;

    //Variables Firebase de Autenticacion
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instanciamos las Variables
        editMailLogin = findViewById(R.id.editMailLogin);
        editPassLogin = findViewById(R.id.editPassLogin);
        btnStart = findViewById(R.id.btnStart);
        mAuth = FirebaseAuth.getInstance();

        //Listener btnStart para autenticar el usuario y pasar al menu principal
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editMailLogin.getText().toString()) && !TextUtils.isEmpty(editPassLogin.getText().toString())){
                    loginUser(editMailLogin.getText().toString(),editPassLogin.getText().toString());
                }else{
                    Snackbar.make(v,"Error Autenticación",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    //Método para autenticar al usuario mediante email y password
    public void loginUser(String em,String pwd){
        mAuth.signInWithEmailAndPassword(em,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"BIENVENIDO A AP2APP",Toast.LENGTH_LONG).show();

                    //Pasamos a la pantalla Menu Principal si la autenticación es correcta
                    Intent intentMenu = new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(intentMenu);
                }else{
                    Toast.makeText(LoginActivity.this,"Datos Incorrectos",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Añadimos el menu1.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Agregamos el siguiente método para habilitar los elementos de menu1.xml
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.inicioMenu1:
                //Nos devuelve a StartActivity
                Intent intentStart = new Intent(LoginActivity.this,StartActivity.class);
                startActivity(intentStart);
                finish();
                break;

            case  R.id.resgistroMenu1:
                Intent intentRegistro = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intentRegistro);
                finish();
                break;

            case R.id.loginMenu1:
                //Nos devuelve a LoginActivity
                Toast.makeText(LoginActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
