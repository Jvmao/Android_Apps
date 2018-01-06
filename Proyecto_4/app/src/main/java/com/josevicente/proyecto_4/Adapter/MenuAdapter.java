package com.josevicente.proyecto_4.Adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.josevicente.proyecto_4.Modelo.DatosMenu;
import com.josevicente.proyecto_4.R;
import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter{
    //Variables
    private ArrayList<DatosMenu> listadoOpcionesDelMenu;

    //Constructor del ArrayList
    public MenuAdapter (ArrayList<DatosMenu> param){
        this.listadoOpcionesDelMenu=param;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Le pasamos list_menu.xml
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
        if(listadoOpcionesDelMenu == null){
            return 0;
        }else{
            return listadoOpcionesDelMenu.size();
        }
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
            mItemText.setText(listadoOpcionesDelMenu.get(position).getTitulo());
            mItemImage.setImageResource(listadoOpcionesDelMenu.get(position).getIconoMenu());
        }

        //Generamos el método onClick
        @Override
        public void onClick(View v) {
            int positon = getAdapterPosition();
            switch (positon){
                case 0:
                    Log.d("CLICK","Posición 1");
                    Toast.makeText(v.getContext(),"PERFIL",Toast.LENGTH_LONG).show();
                    //  v.getContext().startActivity(new Intent(v.getContext(),listadoOpcionesDelMenu.get(position).getActivityACarregar()));
                    break;
                case 1:
                    Log.d("CLICK","Posición 2");
                    Toast.makeText(v.getContext(),"JUEGO",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Log.d("CLICK","Posición 3");
                    Toast.makeText(v.getContext(),"INSTRUCCIONES",Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Log.d("CLICK","Posición 4");
                    Toast.makeText(v.getContext(),"INFORMACIÓN",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

}
