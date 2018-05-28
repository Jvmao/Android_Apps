package com.josevicente.ap2app.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.josevicente.ap2app.Model.Evento;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.Utils.Constants;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by JoseVicente on 28/02/2018.
 */

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {
    //Variables
    private Context context;
    private List<Evento> eventoList;

    //Variables BBDD
    private DatabaseReference databaseReference;
    private String userEvento = Constants.userEvent;

    //Añadimos las Variables para el Alert Dialog
    private AlertDialog.Builder alertDialogBilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    //Constructor
    public EventoAdapter(Context context, List<Evento> eventoList) {
        this.context = context;
        this.eventoList = eventoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Le pasamos el layout a adaptar, en este caso lista_productos.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_eventos,parent,false);
        return new EventoAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            Evento e = eventoList.get(position);

            //Recogemos las Variables
            holder.textTitEvento.setText(e.getTitulo());
            holder.textFechaPick.setText(e.getFecha());
            holder.textHoraPick.setText(e.getHora());
            holder.textDesc.setText(e.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Declaramos las Variables creadas en lista_producto.xml
        public TextView textTitEvento;
        public TextView textFechaPick;
        public TextView textHoraPick;
        public TextView textDesc;
        public ImageButton imageEditEvent;
        public ImageButton imageDeleteEvent;

        public ViewHolder(final View itemView, final Context ctx) {
            super(itemView);

            //Instanciamos las Variables
            context = ctx;
            textTitEvento = itemView.findViewById(R.id.textTitEvento);
            textFechaPick = itemView.findViewById(R.id.textFechaPick);
            textHoraPick = itemView.findViewById(R.id.textHoraPick);
            textDesc = itemView.findViewById(R.id.textDesc);
            imageEditEvent = itemView.findViewById(R.id.imageEditEvent);
            imageDeleteEvent = itemView.findViewById(R.id.imageDeleteEvent);
            databaseReference = FirebaseDatabase.getInstance().getReference(userEvento);

            //Listener imageEditEvent
            imageEditEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Instanciamos el Alert Dialog
                    alertDialogBilder = new AlertDialog.Builder(context);
                    inflater = LayoutInflater.from(context);
                    final View view = inflater.inflate(R.layout.update_event,null); //Le pasamos update_event.xml

                    //Declaramos e instanciamos las variables creadas en update_event.xml
                    final EditText editHeadEvent = view.findViewById(R.id.editHeadEvent);
                    final EditText editDateEvent = view.findViewById(R.id.editDateEvent);
                    final EditText editTimeEvent = view.findViewById(R.id.editTimeEvent);
                    final EditText editDescEvent = view.findViewById(R.id.editDescEvent);
                    final ImageButton imageOkEvent = view.findViewById(R.id.imageOkEvent);
                    final ImageButton imageOutEvent = view.findViewById(R.id.imageOutEvent);

                    //Definimos el query para pasar los datos al AlertDialog uppdate_event.xml
                    Query queryUpdateEvent = databaseReference.orderByChild(Constants.eventHead).equalTo(textTitEvento.getText().toString());
                    queryUpdateEvent.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                                //Definimos los datos a recoger
                                Evento e = dataSnap.getValue(Evento.class);
                                editHeadEvent.setText(e.getTitulo().toString());
                                editDateEvent.setText(e.getFecha().toString());
                                editTimeEvent.setText(e.getHora().toString());
                                editDescEvent.setText(e.getDescripcion().toString());

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //Instanciamos el AlertDialog creado en update_contact.xml
                    alertDialogBilder.setView(view);
                    dialog = alertDialogBilder.create();
                    dialog.show();

                    //Listener imageOkEvent
                    imageOkEvent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String upTituloEvento = editHeadEvent.getText().toString();
                            final String upFechaEvento = editDateEvent.getText().toString();
                            final String upHoraEvento = editTimeEvent.getText().toString();
                            final String upDescEvento = editDescEvent.getText().toString();

                            if(!upTituloEvento.isEmpty() && !upFechaEvento.isEmpty()
                                    && !upHoraEvento.isEmpty() && !upDescEvento.isEmpty()){

                                if(!TextUtils.isEmpty(upTituloEvento) && !TextUtils.isEmpty(upFechaEvento)
                                        && !TextUtils.isEmpty(upHoraEvento) && !TextUtils.isEmpty(upDescEvento)){

                                    Query queryUpEvent = databaseReference.orderByChild(Constants.eventHead)
                                            .equalTo(textTitEvento.getText().toString());
                                    queryUpEvent.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                                                String id = dataSnap.getKey();

                                                databaseReference.child(id).child(Constants.eventHead).setValue(upTituloEvento);
                                                databaseReference.child(id).child(Constants.eventDate).setValue(upFechaEvento);
                                                databaseReference.child(id).child(Constants.eventHour).setValue(upHoraEvento);
                                                databaseReference.child(id).child(Constants.eventDesc).setValue(upDescEvento);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                    dialog.dismiss();
                                    Toast.makeText(ctx,"Evento Actualizado",Toast.LENGTH_LONG).show();
                                }

                            }else {
                                Toast.makeText(ctx,"Faltan Campos por Rellenar",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    //Listener imageOutEvent
                    imageOutEvent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss(); //Cerramos el Alert Dialog update_event.xml
                        }
                    });
                }
            });

            //Listener imageDeleteEvent
            imageDeleteEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Definimos las Variables a Eliminar
                    final String titulo = textTitEvento.getText().toString();
                    final String fecha = textFechaPick.getText().toString();
                    final String hora = textHoraPick.getText().toString();
                    final String desc = textDesc.getText().toString();

                    if(!TextUtils.isEmpty(titulo) && !TextUtils.isEmpty(fecha) && !TextUtils.isEmpty(hora) && !TextUtils.isEmpty(desc)){
                        Query queryDeleteEvent = databaseReference.orderByChild(Constants.eventHead).equalTo(titulo);
                        queryDeleteEvent.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                                    //Recogemos los valores a recoger
                                    String id = dataSnap.getKey();

                                    databaseReference.child(id).child(Constants.eventID).removeValue();
                                    databaseReference.child(id).child(Constants.eventHead).removeValue();
                                    databaseReference.child(id).child(Constants.eventDate).removeValue();
                                    databaseReference.child(id).child(Constants.eventHour).removeValue();
                                    databaseReference.child(id).child(Constants.eventDesc).removeValue();

                                    eventoList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }
            });

        }
    }


}
