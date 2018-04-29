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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.josevicente.firebaseblogapp.Model.Blog;
import com.josevicente.firebaseblogapp.R;

import java.util.HashMap;
import java.util.Map;

//3.-Creamos la nueva Avtivity AddPostActivity, modificamos el layout activity_add_post y creamos en drawable>>input_outline.xml
//7.-Continuamos y desarrollamos la siguiente Activity
public class AddPostActivity extends AppCompatActivity {
    //Declaramos las Variables
    private ImageButton mPostImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitButton;
    private DatabaseReference mPostDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    //Definimos un Progress Dialog
    private ProgressDialog mDialog;

    private static final int GALLERY_CODE=1;
    private Uri mImageUri;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        //Instanciamos las Variables
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("MBlog");
        mPostImage =findViewById(R.id.imageButton);
        mPostTitle = findViewById(R.id.postTitle);
        mPostDesc = findViewById(R.id.postDescription);
        mSubmitButton = findViewById(R.id.btnPost);
        mDialog = new ProgressDialog(this);
        mStorage = FirebaseStorage.getInstance().getReference();

        //Listener para agregar imagenes
        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*"); //Recoge cualquier tipo de imagenes//
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });


        //Listener para enviar el post
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting(); //Añadimos el método creado
            }
        });
    }

    //Definimos el método para establecer las imagenes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mPostImage.setImageURI(mImageUri);
        }
    }

    //Método para cargar imágenes
    private void startPosting() {
        mDialog.setMessage("Posting to Blog"); //Muestra el siguiente mensaje mientras se está cargando
        mDialog.show();

        final String titleVal  = mPostTitle.getText().toString().trim();
        final String descVal = mPostDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal) && mImageUri != null){
            //Instanciamos el objeto Blog
            /*Blog blog = new Blog("title","description","imageurl","timestamp","id");

            //Comprobamos que la bbdd funciona
            mPostDatabase.setValue(blog).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"Item Added",Toast.LENGTH_LONG).show();
                }
            });*/

            //Seleccionamos Firebase>>Storage>>Upload and Download>>Add Firebase Storage to your App para guardar las imágenes

            StorageReference filePath = mStorage.child("MBlog_images").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getDownloadUrl();

                    //Creamos otra referencia a la BBDD
                    DatabaseReference newPost = mPostDatabase.push();
                    Map<String,String> dataToSave = new HashMap<>();

                    //Le pasamos los mismo valores creados en el objeto Blog
                    dataToSave.put("title",titleVal);
                    dataToSave.put("desc",descVal);
                    dataToSave.put("image",downloadurl.toString());
                    dataToSave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    dataToSave.put("userid",mUser.getUid()); //Recuperamos el ID del Usuario

                    //Otra Forma de hacer lo mismo pero sin HashMap
                    //newPost.child("title").setValue(titleVal);

                    newPost.setValue(dataToSave); //Pasamos todos los datos a la BBDD
                    mDialog.dismiss(); //Deshabilitamos el diálogo de progreso en la carga

                    //Si el proceso ha sido satisfactorio pasaremos a la siguiente Activity PostListActivity
                    startActivity(new Intent(AddPostActivity.this,PostListActivity.class));
                    finish();
                }
            });

        }
    }
}
