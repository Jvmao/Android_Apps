package com.josevicente.firebaseblogapp.Activities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josevicente.firebaseblogapp.Data.BlogRecyclerAdapter;
import com.josevicente.firebaseblogapp.Model.Blog;
import com.josevicente.firebaseblogapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//2.-Creamos PostListActivity y añadimos un RecyclerView en activity_list_post.xml y creamos row.xml
public class PostListActivity extends AppCompatActivity {
    //Declaramos las Variables
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    //8.-Le pasamos las Variables del RecyclerView
    private RecyclerView recyclerView;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private List<Blog> blogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        //Instanciamos las Variables
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MBlog"); //Creamos una nueva tabla
        mDatabaseReference.keepSynced(true); //Mantiene la bbdd sincronizada

        //8.-Instanciamos las variables Recycler
        blogList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewID); //Desde activity_post_list.xml
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    //6.-Añadimos la opción Firebase>>Realtime Database>>Save and Retrieve Data>>Add the Realtime Database to your app
    //Posteriormente añadimos un Action Bar a nuestra App
    //Géneramos los métodos para el Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflamos el menu desde menu>>main_menu.xml
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Definimos los botones del Action Bar
        switch (item.getItemId()){
            case R.id.action_add:
                if(mUser != null && mAuth != null){
                    startActivity(new Intent(PostListActivity.this,AddPostActivity.class)); //Pasamos a la Activity siguiente
                    finish();
                }
                break;
            case  R.id.action_signout:
                if(mUser != null && mAuth != null){
                    mAuth.signOut(); //El usuario sale de la aplicación
                    startActivity(new Intent(PostListActivity.this,MainActivity.class)); //Pasamos a  MainActivity
                    finish();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //8.-Generamos el método onStart para listar todos los elementos generados en la Activity AddPostActivity
    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Blog blog = dataSnapshot.getValue(Blog.class); //Referenciamos el objeto Blog
                blogList.add(blog); //Añadimos blog a la lista
                Collections.reverse(blogList);

                blogRecyclerAdapter = new BlogRecyclerAdapter(PostListActivity.this,blogList);
                recyclerView.setAdapter(blogRecyclerAdapter);
                blogRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        //Instanciamos las Variables
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MBlog"); //Creamos una nueva tabla
        mDatabaseReference.keepSynced(true); //Mantiene la bbdd sincronizada
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Instanciamos las Variables
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MBlog"); //Creamos una nueva tabla
        mDatabaseReference.keepSynced(true); //Mantiene la bbdd sincronizada
    }
}
