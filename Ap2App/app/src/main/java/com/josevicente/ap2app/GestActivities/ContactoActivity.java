package com.josevicente.ap2app.GestActivities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.josevicente.ap2app.Activities.StartActivity;
import com.josevicente.ap2app.Adapters.ContactoAdapter;
import com.josevicente.ap2app.EcoActivities.ChartsActivity;
import com.josevicente.ap2app.EcoActivities.GastoActivity;
import com.josevicente.ap2app.EcoActivities.GestGastoActivity;
import com.josevicente.ap2app.Model.Contacto;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.Utils.BottomNavigationViewBehavior;
import com.josevicente.ap2app.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ContactoActivity extends AppCompatActivity {
    //Declaramos las Variables
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String userContact = Constants.userContact;

    //Variables del RecyclerView
    private RecyclerView recyclerView;
    private ContactoAdapter contactoAdapter;
    private List<Contacto> contactoList;

    //Variables para el Alert Dialog
    private AlertDialog.Builder alertDialogBilder;
    private AlertDialog dialog;
    private EditText nombreContacto,apellidoContacto,telContacto,mailContacto;
    private Button btnAddContact,btnOutContact;
    private ImageButton iBtnDeleteContactList,iBtnExitContactList;

    //Vairables ItemTouchHelper
    private ItemTouchHelper itemTouchHelper;

    //Variables BottomMenu
    private BottomNavigationView btmNavContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        //Instanciamos las Variables
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference(userContact);
        mAuth = FirebaseAuth.getInstance();

        contactoList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerContactID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Instanciamos las variables itemTouchHelper
        itemTouchHelper = new ItemTouchHelper(createHelperCallBack()); //Le pasamos el método creado
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //Instanciamos el NavigationBottom
        btmNavContact = findViewById(R.id.btmNavContact);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)btmNavContact.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        btmNavContact.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });

        listaContactos(); //Le añadimos el método para listar los contactos


        navigation(); //Añadimos el método de la barra de navegación

    }

    //Definimos el método para listar los contactos en el recyclerview
    private void listaContactos(){
        mUser = mAuth.getCurrentUser();
        String id = mUser.getUid();

        Query queryContactos = mDatabaseReference.orderByChild(Constants.contactID).equalTo(id);
        queryContactos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactoList.clear();
                for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                    Contacto c = dataSnap.getValue(Contacto.class);
                    contactoList.add(c);
                }
                contactoAdapter = new ContactoAdapter(ContactoActivity.this, contactoList);
                recyclerView.setAdapter(contactoAdapter);
                contactoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("CANCELADO","Listado Contactos Cancelado");
            }
        });
    }

    //Método para añadir un contacto nuevo desde el float
    private void newContact(){
        alertDialogBilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.new_contact,null);

        //Instanciamos las variables creadas en new_contact.xml
        nombreContacto = view.findViewById(R.id.nombreContacto);
        apellidoContacto = view.findViewById(R.id.apellidoContacto);
        telContacto = view.findViewById(R.id.telContacto);
        mailContacto = view.findViewById(R.id.mailContacto);
        btnAddContact = view.findViewById(R.id.btnAddContact);
        btnOutContact = view.findViewById(R.id.btnOutContact);

        alertDialogBilder.setView(view);
        dialog = alertDialogBilder.create();
        dialog.show(); //Muestra new_contact.xml

        //Listener btnAddContact para añadir un nuevo contacto en la BBDD
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Variable a añadir en la BBDD
                final String nombre = nombreContacto.getText().toString();
                final String apellido = apellidoContacto.getText().toString();
                final String tel = telContacto.getText().toString();
                final String mail = mailContacto.getText().toString();

                if(!nombre.isEmpty() && !apellido.isEmpty() && !tel.isEmpty() && ! mail.isEmpty()){
                    if(!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellido)
                            && !TextUtils.isEmpty(tel) && !TextUtils.isEmpty(mail)){

                        //Obtenemos el id del usuario
                        mUser = mAuth.getCurrentUser();
                        String id = mUser.getUid();

                        //Establecemos los datos del objeto Contacto
                        Contacto contacto = new Contacto();
                        contacto.setId(id);
                        contacto.setNombre(nombre);
                        contacto.setApellido(apellido);
                        contacto.setTelefono(tel);
                        contacto.setEmail(mail);

                        DatabaseReference dbRef = mDatabaseReference.push(); //Los datos se guardan en Firebase
                        dbRef.setValue(contacto);

                        limpiar(); //Limpiamos los campos una vez se han guardado en la BBDD
                        Toast.makeText(v.getContext(),"Contacto Guardado Correctamente",Toast.LENGTH_LONG).show();
                        dialog.dismiss(); //Cierra el AlertDialog
                    }
                }else{
                    Toast.makeText(v.getContext(),"Faltan Campos por Rellenar",Toast.LENGTH_LONG).show();
                }

            }
        });

        //Listener para cerrar el alertDialog
        btnOutContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); //Cierra el AlertDialog
            }
        });
    }

    private void deleteContactList(){
        alertDialogBilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.delete_list_contacts,null);

        //Instanciamos los ImageButton creados en delete_list_cost
        iBtnDeleteContactList = view.findViewById(R.id.iBtnDeleteContactList);
        iBtnExitContactList = view.findViewById(R.id.iBtnExitContactList);

        alertDialogBilder.setView(view);
        dialog = alertDialogBilder.create();
        dialog.show(); //Muestra delete_list_contacts.xml

        //Listener para eliminat la lista completa
        iBtnDeleteContactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser = mAuth.getCurrentUser();
                String id = mUser.getUid();
                //Consulta para Eliminar todos los elementos de la Lista de la Compra
                Query deleteAllContactList = mDatabaseReference.orderByChild(Constants.contactID).equalTo(id);
                deleteAllContactList.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            //Recogemos las variables a eliminar
                            String id = snap.getKey();
                            mDatabaseReference.child(id).child(Constants.contactID).removeValue();
                            mDatabaseReference.child(id).child(Constants.contactName).removeValue();
                            mDatabaseReference.child(id).child(Constants.contactLast).removeValue();
                            mDatabaseReference.child(id).child(Constants.contactTel).removeValue();
                            mDatabaseReference.child(id).child(Constants.contactMail).removeValue();

                            contactoList.clear();//Eliminamos los elementos de la lista
                            contactoAdapter.notifyDataSetChanged();
                            dialog.dismiss(); //Cerramos el AlerDialog
                            Toast.makeText(ContactoActivity.this,"Lista de Contactos Eliminada",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //Listener para salir y no elminar la lista completa
        iBtnExitContactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); //Salimos del Dialog
            }
        });
    }

    //Método para limpiar los campos del AlertDialog
    private void limpiar(){
        nombreContacto.setText("");
        apellidoContacto.setText("");
        telContacto.setText("");
        mailContacto.setText("");
    }

    //Desarrollamos el Método para hacer que los items se desplacen y se puedan eliminar
    private ItemTouchHelper.Callback createHelperCallBack(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            //Método que permite eliminar el item cuando este se desplaza
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                //Obtenemos el ID del usuario
                String id = mUser.getUid();

                //Declaramos la posición
                final int position = viewHolder.getAdapterPosition();

                //Definimos la consulta a realizar para eliminar los elementos de la BBDD
                Query queryDeleteContact = mDatabaseReference.orderByChild(Constants.contactID).equalTo(id);
                queryDeleteContact.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        contactoList.clear();
                        for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                            String user = dataSnap.getKey();

                            //Le pasamos los datos a eliminar
                            mDatabaseReference.child(user).child(Constants.contactID).removeValue();
                            mDatabaseReference.child(user).child(Constants.contactName).removeValue();
                            mDatabaseReference.child(user).child(Constants.contactLast).removeValue();
                            mDatabaseReference.child(user).child(Constants.contactTel).removeValue();
                            mDatabaseReference.child(user).child(Constants.contactMail).removeValue();

                            try{
                                for(int i=0;i<contactoList.size();i++){
                                    Contacto c = contactoList.get(i);

                                    contactoList.remove(position);
                                    contactoAdapter.notifyItemRemoved(position);
                                    contactoAdapter.notifyItemRangeChanged(position,contactoList.size());
                                    contactoAdapter.notifyDataSetChanged();
                                }
                            }catch (IndexOutOfBoundsException io){
                                Log.i("INDEX","Error: "+io);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        };
        return simpleCallback;
    }

    //Añadimos el menú menugest.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menugest,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mUser = mAuth.getCurrentUser();
        switch (item.getItemId()){
            case R.id.gestLogout:
                if(mUser !=null && mAuth !=null){
                    mAuth.signOut(); //Logout por parte del usuario para salir de la aplicación
                    Intent intentStart = new Intent(ContactoActivity.this,StartActivity.class);
                    startActivity(intentStart); //Nos devuelve a la pantalla Principal de Inicio
                    Toast.makeText(ContactoActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case R.id.gestStart:
                //Nos devuelve a la pantalla GestMainActivity
                Intent intentGestMain = new Intent(ContactoActivity.this,GestMainActivity.class);
                startActivity(intentGestMain);
                break;

            case R.id.gestUser:
                Toast.makeText(ContactoActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_LONG).show();
                break;

            case R.id.gestEvent:
                //Pasamos e EventoActivity
                Intent intentEvent = new Intent(ContactoActivity.this,EventoActivity.class);
                startActivity(intentEvent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    //Agregamos el navigation del Menu Principal
    public void navigation(){
        btmNavContact.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mUser = mAuth.getCurrentUser();
                switch (item.getItemId()){
                    case R.id.addContactNav:
                        newContact();
                        break;
                    case R.id.deleteContactNav:
                        deleteContactList(); //Añadimos el método para eliminar la lista completa de contactos
                        //Toast.makeText(ContactoActivity.this,"Under Construction",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.agendaNav:
                        Intent intentEvent = new Intent(ContactoActivity.this,EventoActivity.class);
                        startActivity(intentEvent); //Nos devuelve a la pantalla de Agenda
                        Toast.makeText(ContactoActivity.this,"Mis Eventos",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
    }
}
