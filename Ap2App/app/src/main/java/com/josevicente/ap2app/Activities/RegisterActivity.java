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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josevicente.ap2app.Model.User;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.Utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    //Variables Componentes activity_register.xml
    private EditText editName,editApellido,editEdad,editMail,editPass1,editPass2;
    private Button btnRegister;

    //Variables FireBase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String userDB = Constants.userTable; //Nombre de la Tabla en la BBDD que le pasamos desde la clase constants
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Instanciamos las Variables
        editName = findViewById(R.id.editNameUser);
        editApellido = findViewById(R.id.editLastName);
        editEdad = findViewById(R.id.editAge);
        editMail = findViewById(R.id.editMail);
        editPass1 = findViewById(R.id.editPass1);
        editPass2 = findViewById(R.id.editPass2);
        btnRegister = findViewById(R.id.btnRegistroUsuario);

        //Instanciamos las Variables Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(userDB);
        mAuth = FirebaseAuth.getInstance();

        //Listener botón btnRegister para agregar usuarios a la BBDD de Firebase
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewUser(v); //Le pasamos el método creado para añadir usuarios
            }
        });
    }

    //Creamos el método para añadir nuevos Usuarios a la BBDD
    public void addNewUser(final View view){
        final String name = editName.getText().toString();
        final String lastName = editApellido.getText().toString();
        final String age = String.valueOf(editEdad.getText().toString());
        final String mail = editMail.getText().toString();
        final String pass1 = editPass1.getText().toString();
        final String pass2 = editPass2.getText().toString();

        if(!name.isEmpty() && !lastName.isEmpty() && !age.isEmpty() && !mail.isEmpty() && !pass1.isEmpty() && !pass2.isEmpty()){
            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(mail)
                    && !TextUtils.isEmpty(pass1)){

                //Comprobamos que los dos campos de password coincidan
                if(pass1.equals(pass2)){
                    //Creamos un nuevo usuario autenticado con email y password
                    mAuth.createUserWithEmailAndPassword(mail,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mUser = mAuth.getCurrentUser();
                                String id = mUser.getUid(); //Id creada por defecto en Firebase

                                //Guardamos los Datos en la BBDD
                                User user = new User();
                                user.setId(id);
                                user.setName(name);
                                user.setLastName(lastName);
                                user.setAge(Integer.parseInt(age));
                                user.setMail(mail);
                                DatabaseReference dbRef = databaseReference.push();
                                dbRef.setValue(user);

                                clearFields();
                                //Cuando se crea un nuevo usuario nos pasa directamente a la pantalla de Login
                                Intent intentLogin = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intentLogin);

                                //Snackbar que nos informa de que el usuario se ha creado correctamente
                                Snackbar.make(view,"Usuario Creado Correctamente",Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Snackbar.make(view,"Las Contraseñas no Coinciden",Snackbar.LENGTH_LONG).show();
                }

            }

        }else{
            Snackbar.make(view,"Faltan Datos Por Rellenar",Snackbar.LENGTH_LONG).show();
        }
    }

    //Método para limpiar los Campos de Texto
    public void clearFields(){
        editName.setText("");
        editApellido.setText("");
        editEdad.setText("");
        editMail.setText("");
        editPass1.setText("");
        editPass2.setText("");
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
                Intent intentStart = new Intent(RegisterActivity.this,StartActivity.class);
                startActivity(intentStart);
                finish();
                break;

            case  R.id.resgistroMenu1:
                Toast.makeText(RegisterActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_LONG).show();
                break;

            case R.id.loginMenu1:
                //Nos devuelve a LoginActivity
                Intent intentLogin = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intentLogin);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
