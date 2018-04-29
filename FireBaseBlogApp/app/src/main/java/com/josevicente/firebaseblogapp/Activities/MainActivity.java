package com.josevicente.firebaseblogapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.josevicente.firebaseblogapp.R;

public class MainActivity extends AppCompatActivity {
    //1.-Definimos las Variables
    private TextView email,pass;
    private Button btnLogin,btnCrear;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializamos las Variables
        email = findViewById(R.id.emailText);
        pass = findViewById(R.id.passText);
        btnLogin = findViewById(R.id.btnEntrar);
        btnCrear = findViewById(R.id.btnCrear);

        mAuth = FirebaseAuth.getInstance();
        //1.-Damos permiso al Usuario
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if(mUser != null){
                    Toast.makeText(MainActivity.this,"Signed In",Toast.LENGTH_LONG).show();
                    //6.-Pasamos a PostListActivity
                    startActivity(new Intent(MainActivity.this,PostListActivity.class));
                    finish();
                }else{
                    Toast.makeText(MainActivity.this,"No Signed In",Toast.LENGTH_LONG).show();
                }
            }
        };

        //1.- Listener para btnLogin
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(pass.getText().toString())){
                    String em = email.getText().toString();
                    String pwd = pass.getText().toString();

                    //Añadimos el método Login
                    login(em,pwd);
                }
            }
        });

        //9.-Añadimos el Listener del Botón Crear
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateAccountActivity.class)); //Pasamos a CreateAccountActivity
                finish();
            }
        });
    }

    //1.-Generamos el Método onStart
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //2.-Definimos el Método onStop >> Después en res creamos el paquete menu>>main_menu.xml
    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //1.-Creamos el Método Login
    private void login(String em,String pwd){
        mAuth.signInWithEmailAndPassword(em,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Signed In",Toast.LENGTH_LONG).show();
                    //6.-Pasamos a PostListActivity
                    startActivity(new Intent(MainActivity.this,PostListActivity.class));
                    finish();
                }else{
                    Toast.makeText(MainActivity.this,"No Signed In",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //2.-Definimos el método onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_signout){
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    //2.-Definimos el método onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
