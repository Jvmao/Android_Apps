package com.josevicente.ap2app.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.josevicente.ap2app.Model.Producto;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.Utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by JoseVicente on 23/02/2018.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    //Declaramos las Variables
    private Context context;
    private List<Producto> productoList;

    //Variables BBDD
    private DatabaseReference databaseReference;
    private String userLista = Constants.userItem;

    //Añadimos las Variables para el Alert Dialog
    private AlertDialog.Builder alertDialogBilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public ShopAdapter(Context context, List<Producto> productoList) {
        this.context = context;
        this.productoList = productoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Le pasamos el layout a adaptar, en este caso lista_productos.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_productos,parent,false);
        return new ShopAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Producto p = productoList.get(position);

        holder.textProductoLista.setText(p.getProducto());
        holder.textCantidadLista.setText(p.getCantidad());

        /*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date).toString();
        holder.textFechaLista.setText(strDate);*/ //Le añadimos la fecha del articulo creado
    }


    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Declaramos las Variables creadas en lista_productos.xml
        public TextView textProductoLista;
        public TextView textCantidadLista;
        //public TextView textFechaLista;
        public ImageButton imageEditLista;
        public ImageButton imageDeleteItem;


        public ViewHolder(final View itemView, final Context ctx) {
            super(itemView);

            //Instanciamos las Variables
            context = ctx;
            textProductoLista = itemView.findViewById(R.id.textProductoLista);
            textCantidadLista = itemView.findViewById(R.id.textCantidadLista);
            //textFechaLista = itemView.findViewById(R.id.textFechaList);
            imageEditLista = itemView.findViewById(R.id.imageEditLista);
            imageDeleteItem = itemView.findViewById(R.id.imageDeleteItem);
            databaseReference = FirebaseDatabase.getInstance().getReference(userLista);

            //Listener imageEditLista para modificar el producto añadido
            imageEditLista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Instanciamos el Alert Dialog
                    alertDialogBilder = new AlertDialog.Builder(context);
                    inflater = LayoutInflater.from(context);
                    final View view = inflater.inflate(R.layout.update_lista,null); //Le pasamos update_lista.xml

                    //Instanciamos los elementos creados en update_lista.xml
                    final EditText editUpdateConcepto = view.findViewById(R.id.editUpdateConcepto);
                    final EditText editUpdateCantidad = view.findViewById(R.id.editUpdateCantidad);
                    final Button btnUpdateLista = view.findViewById(R.id.btnUpdateLista);
                    final Button btnOutUpdate = view.findViewById(R.id.btnOutUpdate);

                    //Definimos el query para pasar los datos al AlertDialog update_lista.xml
                    final Query queryUpdateLista = databaseReference.orderByChild(Constants.userProduct)
                            .equalTo(textProductoLista.getText().toString());
                    queryUpdateLista.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                                //Recogemos las Variables a pasar al AlertDialog
                                Producto p = dataSnap.getValue(Producto.class);
                                editUpdateConcepto.setText(p.getProducto().toString());
                                editUpdateCantidad.setText(p.getCantidad().toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //Instanciamos el AlertDialog creado en update_lista.xml
                    alertDialogBilder.setView(view);
                    dialog = alertDialogBilder.create();
                    dialog.show();

                    //Declaramos el Listener de btnUpdateLista
                    btnUpdateLista.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Declaramos las Variables
                            final String prod = editUpdateConcepto.getText().toString();
                            final String cant = editUpdateCantidad.getText().toString();

                            if(!prod.isEmpty() && !cant.isEmpty()){
                                if(!TextUtils.isEmpty(prod) && !TextUtils.isEmpty(cant)){
                                    //Definimos la consulta para actulizar la lista
                                    Query queryUpdateProducto = databaseReference.orderByChild(Constants.userProduct)
                                            .equalTo(textProductoLista.getText().toString());

                                    queryUpdateProducto.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataS: dataSnapshot.getChildren()){
                                                String id = dataS.getKey();

                                                databaseReference.child(id).child(Constants.userProduct).setValue(prod);
                                                databaseReference.child(id).child(Constants.userCant).setValue(cant);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    dialog.dismiss();
                                    Toast.makeText(ctx,"Datos Actualizados Correctamente",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(ctx,"Faltan Datos por Rellenar",Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    //Declaramos el Listener de btnOutUpdate
                    btnOutUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss(); //Cerramos el AlertDialog
                        }
                    });
                }
            });

            //Listener imageDeleteItem para eliminar el producto
            imageDeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Definimos los elementos a eliminar
                    final String prod = textProductoLista.getText().toString();
                    final String cant = textCantidadLista.getText().toString();

                    if(!TextUtils.isEmpty(prod) && !TextUtils.isEmpty(cant)){
                        Query deleteQuery = databaseReference.orderByChild(Constants.userProduct).equalTo(prod);
                        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    //Recogemos las variables a eliminar
                                    String id = snap.getKey();
                                    databaseReference.child(id).child(Constants.userItemID).removeValue();
                                    databaseReference.child(id).child(Constants.userProduct).removeValue();
                                    databaseReference.child(id).child(Constants.userCant).removeValue();

                                    productoList.remove(getAdapterPosition()); //Eliminamos el elemento de la lista
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
