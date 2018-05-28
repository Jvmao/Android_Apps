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
import com.josevicente.ap2app.Model.Contacto;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.Utils.Constants;

import java.util.List;

/**
 * Created by JoseVicente on 01/03/2018.
 */

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ViewHolder>{
    //Variables
    private Context context;
    private List<Contacto> contactoList;

    //Variables BBDD
    private DatabaseReference databaseReference;
    private String userContact = Constants.userContact;

    //Añadimos las Variables para el Alert Dialog
    private AlertDialog.Builder alertDialogBilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public ContactoAdapter(Context context, List<Contacto> contactoList) {
        this.context = context;
        this.contactoList = contactoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Le pasamos el layout a adaptar, en este caso lista_contactos.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_contactos,parent,false);
        return new ContactoAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contacto c = contactoList.get(position);

        //Recogemos las Variables
        holder.contactName.setText(c.getNombre());
        holder.contactLast.setText(c.getApellido());
        holder.contactTel.setText(c.getTelefono());
        holder.contactMail.setText(c.getEmail());
    }


    @Override
    public int getItemCount() {
        return contactoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        //Declaramos las variables creadas en lista_contactos.xml
        public TextView contactName;
        public TextView contactLast;
        public TextView contactTel;
        public TextView contactMail;
        public ImageButton imageBtnEditContact, imageBtnOutContact;

        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);

            //Instanciamos las variables
            context = ctx;
            contactName = itemView.findViewById(R.id.contactName);
            contactLast = itemView.findViewById(R.id.contactLast);
            contactTel = itemView.findViewById(R.id.contactTel);
            contactMail = itemView.findViewById(R.id.contactMail);
            imageBtnEditContact = itemView.findViewById(R.id.imageBtnEditContact);
            imageBtnOutContact = itemView.findViewById(R.id.imageBtnOutContact);
            databaseReference = FirebaseDatabase.getInstance().getReference(userContact);

            //Listener imageBtnEditContact
            imageBtnEditContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Instanciamos el Alert Dialog
                    alertDialogBilder = new AlertDialog.Builder(context);
                    inflater = LayoutInflater.from(context);
                    final View view = inflater.inflate(R.layout.update_contact,null); //Le pasamos update_contact.xml

                    //Instanciamos los elementos creados en update_contact.xml
                    final EditText updateName = view.findViewById(R.id.upName);
                    final EditText updateLastName = view.findViewById(R.id.upLastName);
                    final EditText updateTel = view.findViewById(R.id.upTel);
                    final EditText updateMail = view.findViewById(R.id.upMail);
                    final ImageButton btnUpdateContact = view.findViewById(R.id.btnUpdateContact);
                    final ImageButton btnOutUpdate = view.findViewById(R.id.btnOutUpdateContact);

                    //Definimos la consulta para pasar los datos a update_contact.xml
                    Query queryUpdateContact = databaseReference.orderByChild(Constants.contactName)
                            .equalTo(contactName.getText().toString());
                    queryUpdateContact.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                                //Definimos los datos a recoger
                                Contacto c = dataSnap.getValue(Contacto.class);
                                updateName.setText(c.getNombre().toString());
                                updateLastName.setText(c.getApellido().toString());
                                updateTel.setText(c.getTelefono().toString());
                                updateMail.setText(c.getEmail().toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });

                    //Instanciamos el AlertDialog creado en update_contact.xml
                    alertDialogBilder.setView(view);
                    dialog = alertDialogBilder.create();
                    dialog.show();

                    //Listener btnUpdateContact
                    btnUpdateContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String upName = updateName.getText().toString();
                            final String upLast = updateLastName.getText().toString();
                            final String upTel = updateTel.getText().toString();
                            final String upMail = updateMail.getText().toString();

                            if(!upName.isEmpty() && !upLast.isEmpty() && !upTel.isEmpty() && !upMail.isEmpty()){
                                if(!TextUtils.isEmpty(upName) && !TextUtils.isEmpty(upLast) && !TextUtils.isEmpty(upTel) && !TextUtils.isEmpty(upMail)){
                                    Query updateContact = databaseReference.orderByChild(Constants.contactName)
                                            .equalTo(contactName.getText().toString());
                                    updateContact.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                                                String id = dataSnap.getKey();

                                                databaseReference.child(id).child(Constants.contactName).setValue(upName);
                                                databaseReference.child(id).child(Constants.contactLast).setValue(upLast);
                                                databaseReference.child(id).child(Constants.contactTel).setValue(upTel);
                                                databaseReference.child(id).child(Constants.contactMail).setValue(upMail);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {}
                                    });
                                    dialog.dismiss();
                                    Toast.makeText(ctx,"Contacto Actualizado",Toast.LENGTH_LONG).show();
                                }

                            }else{
                                Toast.makeText(ctx,"Faltan Datos por Rellenar",Toast.LENGTH_LONG);
                            }

                        }
                    });

                    //Listener btnOutUpdate
                    btnOutUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss(); //Salimos del Alert Dialog
                        }
                    });
                }
            });

            //Listener imageBtnOutContact para eliminar el contacto
            imageBtnOutContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Definimos los elementos a eliminar
                    final String name = contactName.getText().toString();
                    final String lastName = contactLast.getText().toString();
                    final String tel = contactTel.getText().toString();
                    final String mail = contactMail.getText().toString();

                    if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName)
                            && !TextUtils.isEmpty(tel) && !TextUtils.isEmpty(mail)){

                        Query queryDeleteContact = databaseReference.orderByChild(Constants.contactName).equalTo(name);
                        queryDeleteContact.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                                    //Recogemos los valores a recoger
                                    String id = dataSnap.getKey();
                                    databaseReference.child(id).child(Constants.contactID).removeValue();
                                    databaseReference.child(id).child(Constants.contactName).removeValue();
                                    databaseReference.child(id).child(Constants.contactLast).removeValue();
                                    databaseReference.child(id).child(Constants.contactTel).removeValue();
                                    databaseReference.child(id).child(Constants.contactMail).removeValue();

                                    contactoList.remove(getAdapterPosition());
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
