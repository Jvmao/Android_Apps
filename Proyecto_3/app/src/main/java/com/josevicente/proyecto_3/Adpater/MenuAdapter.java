package com.josevicente.proyecto_3.Adpater;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.josevicente.proyecto_3.Activity.InfoActivity;
import com.josevicente.proyecto_3.Activity.InstActivity;
import com.josevicente.proyecto_3.Activity.JuegoActivity;
import com.josevicente.proyecto_3.Activity.MenuActivity;
import com.josevicente.proyecto_3.Activity.PerfilActivity;
import com.josevicente.proyecto_3.Modelo.DatosMenu;
import com.josevicente.proyecto_3.R;

//5-Creamos el Adaptador del Menu y generamos e implementamos los métodos
public class MenuAdapter extends RecyclerView.Adapter{
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflamos el list_menu.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_menu,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return DatosMenu.titulo.length;
    }

    //Definimos el Método ListViewHolder e implementamos el Listener
    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Definimos las variables a incluir ya creadas en el list_menu.xml
        private TextView mItemText;
        private ImageView mItemImage;

        //Declaramos el Constructor
        public ListViewHolder(View v){
            super(v);
            //Buscamos las variables por ID generadas list_menu.xml
            mItemText=v.findViewById(R.id.textMenu);
            mItemImage=v.findViewById(R.id.imageMenu);
            v.setOnClickListener(this); //Inicializamos el Listener de cada elemento
        }

        //Creamos el Método bindView
        public void bindView(int position){
            //Le pasamos el texto y las imagenes creadas en la clase DatosMenu y lo obtenemos por posición
            mItemText.setText(DatosMenu.titulo[position]);
            mItemImage.setImageResource(DatosMenu.iconoMenu[position]);
        }

        //Generamos el método onClick para los items del menu
        @Override
        public void onClick(View v) {
            int positon = getAdapterPosition();
            switch (positon){
                case 0:
                    Log.d("CLICK","Posición 1");
                    v.getContext().startActivity(new Intent(v.getContext(),PerfilActivity.class)); //Pasamos a la Activity PERFIL
                    break;
                case 1:
                    Log.d("CLICK","Posición 2");
                    v.getContext().startActivity(new Intent(v.getContext(), JuegoActivity.class)); //Pasamos a la Activity JUEGO
                    break;
                case 2:
                    Log.d("CLICK","Posición 3");
                    v.getContext().startActivity(new Intent(v.getContext(), InstActivity.class)); //Pasamos a la Activity INSTRUCCIONES
                    break;
                case 3:
                    Log.d("CLICK","Posición 4");
                    v.getContext().startActivity(new Intent(v.getContext(), InfoActivity.class)); //Pasamos a la Activity INFORMACIÓN
                    break;
            }
        }
    }
}
