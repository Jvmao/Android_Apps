package com.josevicente.ap2app.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.josevicente.ap2app.Fragments.InfoMapaFragment;
import com.josevicente.ap2app.Fragments.LocMapaFragment;
import com.josevicente.ap2app.Model.Mapa;

import java.util.ArrayList;
import com.josevicente.ap2app.R;

public class MenuMapaAdapter extends RecyclerView.Adapter {
    //Variables
    private ArrayList<Mapa> opcionesMapa;

    //Constructor del Adapter
    public MenuMapaAdapter(ArrayList<Mapa> opcionesMapa) {
        this.opcionesMapa = opcionesMapa;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Añadimos el layout opciones_menu_mapa.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.opciones_menu_mapa,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        if(opcionesMapa == null){
            return 0;
        }else{
            return  opcionesMapa.size();
        }
    }

    //Definimos el Método ListViewHolder e implementamos el Listener
    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Definimos las variables a incluir creadas en opciones_menu_mapa.xml
        public ImageView iconoMenuMapa;
        public TextView tituloMenuMapa;


        public ListViewHolder(View itemView) {
            super(itemView);
            //Instanciamos las variables creadas en opciones_menu_mapa por ID
            iconoMenuMapa = itemView.findViewById(R.id.iconoMenuMapa);
            tituloMenuMapa = itemView.findViewById(R.id.tituloMenuMapa);
            itemView.setOnClickListener(this); //Inicializamos el Listener de cada elemento
        }

        //Creamos el Método bindView
        public void bindView(int position){
            //Le pasamos el texto y las imagenes creadas en la clase opciones_menu_mapa.xml y lo obtenemos por posición
            tituloMenuMapa.setText(opcionesMapa.get(position).getTitulo());
            iconoMenuMapa.setImageResource(opcionesMapa.get(position).getIconoMenu());
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (position){
                case 0:
                    //Toast.makeText(v.getContext(),"OPCIÓN 1",Toast.LENGTH_LONG).show();
                    FMapLoc(v);
                    break;
                case 1:
                    //Toast.makeText(v.getContext(),"OPCIÓN 2",Toast.LENGTH_LONG).show();
                    FMapInfo(v);
                    break;
            }
        }
    }

    //Método para cargar el Mapa de Localización
    public void FMapLoc(View view){
        AppCompatActivity mapLocActivity = (AppCompatActivity) view.getContext();
        LocMapaFragment locMapaFragment = new LocMapaFragment();
        mapLocActivity.getFragmentManager().beginTransaction()
                .replace(R.id.frameDinamicoID,locMapaFragment)
                .addToBackStack(null)
                .commit();
    }

    //Método para cargar el Mapa de Info
    public void FMapInfo(View view){
        AppCompatActivity mapInfoActivity = (AppCompatActivity) view.getContext();
        InfoMapaFragment infoMapaFragment = new InfoMapaFragment();
        mapInfoActivity.getFragmentManager().beginTransaction()
                .replace(R.id.frameDinamicoID,infoMapaFragment)
                .addToBackStack(null)
                .commit();
    }
}
