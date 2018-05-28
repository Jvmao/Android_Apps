package com.josevicente.ap2app.Adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.josevicente.ap2app.Model.Gasto;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.Utils.Constants;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by JoseVicente on 20/02/2018.
 */

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.ViewHolder>{
    //Declaramos las Variables
    private Context context;
    private List<Gasto> gastoList;

    //Añadimos las Variables para el Alert Dialog
    private AlertDialog.Builder alertDialogBilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    private DatabaseReference databaseReference;
    private String userExpenses = Constants.userExpenses;
    FirebaseUser mUser;
    FirebaseAuth mAuth;


    //Constructor
    public GastoAdapter(Context context, List<Gasto> gastoList) {
        this.context = context;
        this.gastoList = gastoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Le pasamos el layout a adaptar, en este caso getproductos.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gastos,parent,false);
        return new ViewHolder(view,context);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gasto gasto = gastoList.get(position);

        holder.textConcepto.setText(gasto.getConcepto());
        holder.textMes.setText(gasto.getMes());
        holder.textImporte.setText(gasto.getImporte());
    }


    @Override
    public int getItemCount() {
        return gastoList.size();
    }

    //Declaramos el método ViewHolder y extendemos el Recyclerview
    public class ViewHolder extends RecyclerView.ViewHolder {
        //Declaramos las variables creadas en gasto.xml
        public TextView textConcepto;
        public TextView textMes;
        public TextView textImporte;
        public ImageButton imageEdit;
        public ImageButton imageDelete;

        public ViewHolder(final View itemView, Context ctx) {
            super(itemView);

            //Instanciamos las Variables Declaradas
            context = ctx;
            textConcepto = itemView.findViewById(R.id.textConcepto);
            textMes = itemView.findViewById(R.id.textMes);
            textImporte = itemView.findViewById(R.id.textImporte);
            imageEdit = itemView.findViewById(R.id.imageEdit);
            imageDelete = itemView.findViewById(R.id.imageDelete);
            databaseReference = FirebaseDatabase.getInstance().getReference(userExpenses);
            mAuth = FirebaseAuth.getInstance();


            //Listener Botón Actualizar desde Adapter
            imageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Instanciamos el AlertDialog
                    alertDialogBilder = new AlertDialog.Builder(context);
                    inflater = LayoutInflater.from(context);
                    final View view = inflater.inflate(R.layout.update_item,null); //Le pasamos el layout update_item.xml

                    //Instanciamos las Variables creadas en update_item.xml
                    final EditText editConceptoUpdate = view.findViewById(R.id.editConceptoUpdate);
                    final EditText editMesUpdate = view.findViewById(R.id.editMesUpdate);
                    final EditText editImporteUpdate = view.findViewById(R.id.editImporteUpdate);
                    Button btnUpdate = view.findViewById(R.id.btnUpdate);
                    Button btnExitUpdate = view.findViewById(R.id.btnExitUpdate);

                    //Definimos el query para pasar los datos al AlertDialog
                    //Query getData = databaseReference.orderByChild(Constants.userConcept).equalTo(textConcepto.getText().toString());
                    //Query getData = databaseReference.orderByChild(Constants.userMonth).equalTo(textMes.getText().toString());
                    Query getData = databaseReference.orderByChild(Constants.userAmount).equalTo(textImporte.getText().toString());

                    getData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                                //Recogemos las Variables a pasar al AlertDialog
                                Gasto gasto = dataSnap.getValue(Gasto.class);
                                editConceptoUpdate.setText(gasto.getConcepto().toString());
                                editMesUpdate.setText(gasto.getMes().toString());
                                editImporteUpdate.setText(gasto.getImporte().toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    //Instanciamos el AlertDialog
                    alertDialogBilder.setView(view);
                    dialog = alertDialogBilder.create(); //Creamos el alertDialog
                    dialog.show(); //Nos muestra el AlertDialog

                    //Declaramos el Listener del botón btnUpdate, desde el AlertDialog
                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Declaramos las Variables
                            final String concepto = editConceptoUpdate.getText().toString();
                            final String mes = editMesUpdate.getText().toString();
                            final String importe = editImporteUpdate.getText().toString();

                            if(!TextUtils.isEmpty(concepto) && !TextUtils.isEmpty(mes) && !TextUtils.isEmpty(importe)){
                                //Definimos la consulta para actualizar los Gastos
                                Query updateExpense = databaseReference.orderByChild(Constants.userConcept)
                                        .equalTo(textConcepto.getText().toString());

                                updateExpense.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                                            //mUser = mAuth.getCurrentUser();
                                            String id = dataSnap.getKey();

                                            databaseReference.child(id).child(Constants.userConcept).setValue(concepto);
                                            databaseReference.child(id).child(Constants.userMonth).setValue(mes);
                                            databaseReference.child(id).child(Constants.userAmount).setValue(importe);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                dialog.dismiss();
                                Snackbar.make(view,"Datos Actualizados Correctamente",Snackbar.LENGTH_LONG).show();
                            }

                        }
                    });

                    //Declaramos el Listener del botón btnExitUpdate, desde el AlertDialog
                    btnExitUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss(); //Cerramos el AlertDialog
                        }
                    });
                }
            });

            //Listener Botón Borrar desde el Adapter
            imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String con = textConcepto.getText().toString();
                    final String mes = textMes.getText().toString();
                    final String imp = textImporte.getText().toString();

                    if(!TextUtils.isEmpty(con) && !TextUtils.isEmpty(mes) && !TextUtils.isEmpty(imp)){
                        Query deleteQuery = databaseReference.orderByChild(Constants.userConcept).equalTo(con);

                        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    //Recogemos las variables a eliminar
                                    String id = snap.getKey();
                                    databaseReference.child(id).child(Constants.userIDExpenses).removeValue();
                                    databaseReference.child(id).child(Constants.userConcept).removeValue();
                                    databaseReference.child(id).child(Constants.userMonth).removeValue();
                                    databaseReference.child(id).child(Constants.userAmount).removeValue();

                                    //Removemos el item
                                    gastoList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                }
                                Snackbar.make(itemView,"Gasto Eliminado",Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.i("ELIMINACIÓN","Error Eliminación "+databaseError.getMessage());
                            }
                        });
                    }
                }
            });
        }
    }
}
