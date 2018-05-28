package com.josevicente.ap2app.GestActivities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.josevicente.ap2app.Adapters.EventoAdapter;
import com.josevicente.ap2app.Model.Evento;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.Utils.BottomNavigationViewBehavior;
import com.josevicente.ap2app.Utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;

public class EventoActivity extends AppCompatActivity {
    //Declaramos las Variables
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String userEvent = Constants.userEvent;

    //Variables del RecyclerView
    private RecyclerView recyclerView;
    private EventoAdapter eventoAdapter;
    private ArrayList<Evento> eventoList;


    //Variables para el Alert Dialog
    private AlertDialog.Builder alertDialogBilder;
    private AlertDialog dialog;
    private EditText editTituloEvento,editDescEvento;
    private ImageButton imageBtnCalendar,imageBtnWatch,addNewEvent,outEvent;
    private TextView textoFechaEvento,textoHoraEvento;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private ImageButton iBtnDeleteEventList,iBtnExitEventList;

    //Vairables ItemTouchHelper
    private ItemTouchHelper itemTouchHelper;

    //Variables BottomMenu
    private BottomNavigationView btmNavEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        //Instanciamos las Variables
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference(userEvent);
        mAuth = FirebaseAuth.getInstance();

        //Instanciamos las Variables del Adapter y le añadimos el adapter creado en activity_evento.xml
        eventoList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerEventosID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Instanciamos las variables itemTouchHelper
        itemTouchHelper = new ItemTouchHelper(createHelperCallBack()); //Le pasamos el método creao
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //Instanciamos el NavigationBottom
        btmNavEvent = findViewById(R.id.btmNavEvent);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)btmNavEvent.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        btmNavEvent.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });

        //Le añadimos el método para listar eventos
        listarEventos();

        navigation(); //Añadimos el método del Navegador

    }

    //Definimos el método para listar todos los eventos
    private void listarEventos(){
        mUser = mAuth.getCurrentUser();
        String id = mUser.getUid();

        Query queryEventos = mDatabaseReference.orderByChild(Constants.eventID).equalTo(id);
        queryEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventoList.clear();
                for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                    Evento e = dataSnap.getValue(Evento.class);
                    eventoList.add(e);
                }
                eventoAdapter = new EventoAdapter(EventoActivity.this,eventoList);
                recyclerView.setAdapter(eventoAdapter);
                eventoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("CANCELADO","Listado Eventos Cancelado");
            }
        });
    }

    //Definimos el método para añadir un nuevo evento
    private void newEvent(){
        alertDialogBilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.new_event,null);

        //Instanciamos las Variables creadas en new_event.xml
        editTituloEvento = view.findViewById(R.id.editTituloEvento);
        imageBtnCalendar = view.findViewById(R.id.imageBtnCalendar);
        imageBtnWatch = view.findViewById(R.id.imageBtnWatch);
        textoFechaEvento = view.findViewById(R.id.textoFechaEvento);
        textoHoraEvento = view.findViewById(R.id.textoHoraEvento);
        editDescEvento = view.findViewById(R.id.editDescEvento);
        addNewEvent = view.findViewById(R.id.addNewEvent);
        outEvent = view.findViewById(R.id.outEvent);

        alertDialogBilder.setView(view);
        dialog = alertDialogBilder.create();
        dialog.show(); //Muestra new_event.xml

        //Evento para mostrar el calendario y seleccionar la fecha que queremos
        imageBtnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Instanciamos el Calendario
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(EventoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                c.set(Calendar.YEAR,year);
                                c.set(Calendar.MONTH,month);
                                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                                textoFechaEvento.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                            }
                        },mYear,mMonth,mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        //Evento para mostrar la hora a través de un Timepickerdialog
        imageBtnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Instaciamos las Variables
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(EventoActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textoHoraEvento.setText(hourOfDay+":"+minute);
                    }
                },hour,min,false);
                timePickerDialog.show();
            }
        });

        //Listener para añadir un elemento nuevo en el recycler
        addNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Variables a añadir en la BBDD
                final String titulo = editTituloEvento.getText().toString();
                final String fecha = textoFechaEvento.getText().toString();
                final String hora = textoHoraEvento.getText().toString();
                final String desc = editDescEvento.getText().toString();

                if(!titulo.isEmpty() && !fecha.isEmpty() && !hora.isEmpty() && !desc.isEmpty()){
                    if(!TextUtils.isEmpty(titulo) && !TextUtils.isEmpty(fecha) && !TextUtils.isEmpty(hora) && !TextUtils.isEmpty(desc)){
                        //Obtenemos el id del usuario
                        mUser = mAuth.getCurrentUser();
                        String id = mUser.getUid();

                        //Guardamos los datos en la BBDD
                        Evento ev = new Evento();
                        ev.setId(id);
                        ev.setTitulo(titulo);
                        ev.setFecha(fecha);
                        ev.setHora(hora);
                        ev.setDescripcion(desc);

                        DatabaseReference dbRef = mDatabaseReference.push(); //Los datos se guardan en la BBDD de Firebase
                        dbRef.setValue(ev);

                        limpiar(); //Limpiamos los campos después de guardar los datos en la BBDD
                        Toast.makeText(v.getContext(),"Nuevo Evento Guardado",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(v.getContext(),"Faltan Campos por Rellenar",Toast.LENGTH_LONG).show();
                }
            }
        });

        //Listener para salir del AlertDialog
        outEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Salimos del AlertDialog
            }
        });
    }

    private void deleteEventList(){
        alertDialogBilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.delete_list_events,null);

        //Instanciamos los ImageButton creados en delete_list_events
        iBtnDeleteEventList = view.findViewById(R.id.iBtnDeleteEventList);
        iBtnExitEventList = view.findViewById(R.id.iBtnExitEventList);

        alertDialogBilder.setView(view);
        dialog = alertDialogBilder.create();
        dialog.show(); //Muestra delete_list_events.xml

        //Listener Botón eliminar lista de eventos
        iBtnDeleteEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser = mAuth.getCurrentUser();
                String id = mUser.getUid();
                //Consulta para Eliminar todos los elementos de la Lista de la Compra
                Query deleteAllEventList = mDatabaseReference.orderByChild(Constants.eventID).equalTo(id);
                deleteAllEventList.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            //Recogemos las variables a eliminar
                            String id = snap.getKey();
                            mDatabaseReference.child(id).child(Constants.eventID).removeValue();
                            mDatabaseReference.child(id).child(Constants.eventHead).removeValue();
                            mDatabaseReference.child(id).child(Constants.eventDate).removeValue();
                            mDatabaseReference.child(id).child(Constants.eventHour).removeValue();
                            mDatabaseReference.child(id).child(Constants.eventDesc).removeValue();

                            eventoList.clear();//Eliminamos los elementos de la lista
                            eventoAdapter.notifyDataSetChanged();
                            dialog.dismiss(); //Cerramos el AlerDialog
                            Toast.makeText(EventoActivity.this,"Lista de Eventos Eliminada",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //Listener Botón salir eliminar lista eventos
        iBtnExitEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Salimos del dialog para eliminar la lista completa
                dialog.dismiss();
            }
        });
    }

    //Método para limpiar los campos del AlertDialog
    private void limpiar(){
        editTituloEvento.setText("");
        textoFechaEvento.setText("");
        textoHoraEvento.setText("");
        editDescEvento.setText("");
    }

    //Desarrollamos el Método para hacer que los items se desplacen y se puedan eliminar
    private ItemTouchHelper.Callback createHelperCallBack(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
                    //Elimina el item cuando se desplaza
                    String user = mUser.getUid();

                        Query deleteEvent = mDatabaseReference.orderByChild(Constants.eventID).equalTo(user);
                        deleteEvent.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnap: dataSnapshot.getChildren()) {
                                    //Recogemos el valor del item a eliminar guardado en la BBDD
                                    String id = dataSnap.getKey();
                                    mDatabaseReference.child(id).child(Constants.eventID).removeValue();
                                    mDatabaseReference.child(id).child(Constants.eventHead).removeValue();
                                    mDatabaseReference.child(id).child(Constants.eventDate).removeValue();
                                    mDatabaseReference.child(id).child(Constants.eventHour).removeValue();
                                    mDatabaseReference.child(id).child(Constants.eventDesc).removeValue();

                                    eventoList.remove(viewHolder.getAdapterPosition());
                                    eventoAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                    eventoAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }

        };
        return simpleCallback;
    }

    //Añadimos menugest.xml
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
                    Intent intentStart = new Intent(EventoActivity.this,StartActivity.class);
                    startActivity(intentStart); //Nos devuelve a la pantalla Principal de Inicio
                    Toast.makeText(EventoActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case R.id.gestStart:
                //Nos devuelve a la pantalla GestMainActivity
                Intent intentGestMain = new Intent(EventoActivity.this,GestMainActivity.class);
                startActivity(intentGestMain);
                break;

            case R.id.gestUser:
                //Pasamos a ContactoActivity
                Intent intentUser = new Intent(EventoActivity.this,ContactoActivity.class);
                startActivity(intentUser);
                break;

            case R.id.gestEvent:
                Toast.makeText(EventoActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Agregamos el navigation del Menu Principal
    public void navigation(){
        btmNavEvent.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mUser = mAuth.getCurrentUser();
                switch (item.getItemId()){
                    case R.id.addEventNav:
                        newEvent(); //Añadimos el método para crear un evento nuevo
                        break;
                    case R.id.deleteEventNav:
                         //Añadimos el método para eliminar la lista completa de eventos
                        deleteEventList();
                        break;
                    case R.id.contactNav:
                        Intent intentContact = new Intent(EventoActivity.this,ContactoActivity.class);
                        startActivity(intentContact); //Nos devuelve a la pantalla de Agenda
                        Toast.makeText(EventoActivity.this,"Contactos",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
    }
}
