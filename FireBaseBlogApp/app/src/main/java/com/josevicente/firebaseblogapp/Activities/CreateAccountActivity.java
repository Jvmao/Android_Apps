package com.josevicente.firebaseblogapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.josevicente.firebaseblogapp.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

//9.-Creamos CreateAccountActivity para crear a un nuevo usuario y rellenamos los campos en activity_create_account.xml
public class CreateAccountActivity extends AppCompatActivity {
    //Declaramos las variables
    private EditText userName,userLastName,userMail,userPass;
    private Button btnCreate;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    //Variables para la imagen
    private StorageReference mFirebaseStorage;
    private ImageButton profilePic;
    private final static int GALLERY_CODE=1;
    private Uri resultUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Instanciamos las Variables
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers"); //Creamos una nueva tabla para los usuarios en la BBDD
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        userName= findViewById(R.id.userName);
        userLastName=findViewById(R.id.userLastName);
        userMail=findViewById(R.id.userMail);
        userPass=findViewById(R.id.userPass);
        btnCreate=findViewById(R.id.btnCreateUser);

        profilePic=findViewById(R.id.imageUser);
        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics"); //Indicamos la nueva carpeta donde guardar las fotos de perfil

        //Listener Botón Crear nuevo usuario
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

        //Listener cargar Imagenes
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });
    }

    //Creamos el método para crear un nuevo usuario
    private void createNewAccount(){
        final String name = userName.getText().toString().trim();
        final String last = userLastName.getText().toString().trim();
        String em = userMail.getText().toString().trim();
        String ps = userPass.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(last) && !TextUtils.isEmpty(em) && !TextUtils.isEmpty(ps)){
            mProgressDialog.setMessage("Creating New Account");
            mProgressDialog.show();

            //Firebase>>Authentication>>Email and Password authentication
            mAuth.createUserWithEmailAndPassword(em,ps)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if(authResult != null){
                                StorageReference imagePath = mFirebaseStorage.child("MBlog_Profile_Pics")
                                        .child(resultUri.getLastPathSegment());

                                imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String userid = mAuth.getCurrentUser().getUid();
                                        DatabaseReference currentUserDb = mDatabaseReference.child(userid);
                                        currentUserDb.child("firstname").setValue(name);
                                        currentUserDb.child("lastname").setValue(last);
                                        currentUserDb.child("image").setValue(resultUri.toString());

                                        mProgressDialog.dismiss(); //Cierra el progressDialog
                                        Toast.makeText(CreateAccountActivity.this,"Creando Usuario",Toast.LENGTH_LONG).show();
                                        //Envia a los Usuarios a PostActivity
                                        Intent intent = new Intent(CreateAccountActivity.this,PostListActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });


                            }
                        }
                    });
        }
    }

    //Generamos el Método onActivityResult para la carga de imágenes y adaptarlas con el tamaño que queramos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            //Añadimos una nueva dependencia en gradle desde la página https://github.com/ArthurHub/Android-Image-Cropper
            //dependencia = compile 'com.theartofdev.edmodo:android-image-cropper:2.6.+'
            //Añadimos los nuevos uses-permission a Manifest.xml
            //<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
            //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
            //También declaramos la siguiente Activity en AndroidManifest.xml
            //<activity android:name="com.theartofdev.edmodo.cropper.CropImageActivity"/>

            Uri mImageUri = data.getData();
            //Copiamos el siguiente código desde github y le añadimos el nombre de nuestra activity
            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

        //Copiamos el siguiente código desde Github
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                //Añadimos nuestra imagen
                profilePic.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
