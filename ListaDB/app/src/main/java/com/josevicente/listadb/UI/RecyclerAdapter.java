package com.josevicente.listadb.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.josevicente.listadb.Activities.DetailsActivity;
import com.josevicente.listadb.Data.DataBaseHandler;
import com.josevicente.listadb.Model.Grocery;
import com.josevicente.listadb.R;

import java.util.List;

/**
 * Created by JoseVicente on 29/11/2017.
 */

//10-Creamos la clase RecyclerAdapter
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    //Creamos el Context
    private Context context;

    //Añadimos la lista de la clase Grocery en Model
    private List<Grocery> groceryList;

    //16-Añadimos las Variables para el Alert Dialog
    private AlertDialog.Builder alertDialogBilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    //Generamos el Constructor de los dos elementos anteriores
    public RecyclerAdapter(Context context, List<Grocery> groceryList) {
        this.context = context;
        this.groceryList = groceryList;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        Grocery grocery = groceryList.get(position);
        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItem());
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    //Definimos los Valores del layout e implementamos el listener
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(View v,Context ctx) {
            super(v);

            //Le pasamos el context
            context=ctx;

            //Inicializamos las Variables
            groceryItemName=v.findViewById(R.id.nameID);
            quantity=v.findViewById(R.id.cantidadID);
            dateAdded=v.findViewById(R.id.dateAdded);
            editButton=v.findViewById(R.id.editButton);
            deleteButton=v.findViewById(R.id.deleteButton);

            //Agregamos los botones al Listener
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            //Iniciamos el Listener
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //15-Pasamos a la siguiente ventana y obtenemos la posición de los items
                    int position = getAdapterPosition();
                    Grocery grocery = groceryList.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class); //Pasamos a la Activity DetailsActivity
                    intent.putExtra("Artículo: ",grocery.getName());
                    intent.putExtra("Catidad: ",grocery.getQuantity());
                    intent.putExtra("id",grocery.getId());
                    intent.putExtra("Date",grocery.getDateItem());

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.editButton:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryList.get(position);
                    editItem(grocery);
                    break;
                case R.id.deleteButton:
                    position = getAdapterPosition();
                    grocery = groceryList.get(position);
                    deleteItem(grocery.getId()); //Añadimos el método para eliminar elementos
                    break;
            }
        }

        //16-Creamos el método para eliminar elementos y también creamos un nuevo layout llamado confirmation_dialog
        public void deleteItem(final int id){
            //Insertamos el AlertDialog
            alertDialogBilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog,null);

            //Introducimos los Botones
            Button noButton = view.findViewById(R.id.noButton);
            Button yesButton = view.findViewById(R.id.yesButton);

            alertDialogBilder.setView(view);
            dialog = alertDialogBilder.create();
            dialog.show();

            //Listener para noButton
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss(); //No hace nada
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Eliminamos el item
                    DataBaseHandler db = new DataBaseHandler(context);
                    db.deleteGrocery(id);
                    groceryList.remove(getAdapterPosition()); //Removemos el item
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss(); //Cuando finaliza las operaciones cierra el Dialog
                }
            });

        }

        //17.-Creamos el Método para editar el elemento
        public void editItem(final Grocery grocery){
            //Insertamos el AlertDialog
            alertDialogBilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup,null);

            final EditText groceryItem = view.findViewById(R.id.groceryItem);
            final EditText quantity = view.findViewById(R.id.groceryQty);
            final TextView title = view.findViewById(R.id.titleProducto);

            title.setText("Editar Producto");
            Button saveButton = view.findViewById(R.id.saveButton);

            alertDialogBilder.setView(view);
            dialog = alertDialogBilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataBaseHandler db = new DataBaseHandler(context);

                    //Update item
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(quantity.getText().toString());

                    if(!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty()){
                        //Actualizamos el producto con el método update de la BBDD
                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(),grocery);
                    }else{
                        Snackbar.make(view,"Adñade Producto y Cantidad",Snackbar.LENGTH_LONG).show();
                    }

                    dialog.dismiss();
                }
            });

        }
    }
}
