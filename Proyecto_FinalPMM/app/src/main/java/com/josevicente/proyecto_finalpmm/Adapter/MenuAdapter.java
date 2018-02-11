package com.josevicente.proyecto_finalpmm.Adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.josevicente.proyecto_finalpmm.Fragments.FotoFragment;
import com.josevicente.proyecto_finalpmm.Fragments.JuegoFragment;
import com.josevicente.proyecto_finalpmm.Fragments.MapaFragment;
import com.josevicente.proyecto_finalpmm.Fragments.PerfilFragment;
import com.josevicente.proyecto_finalpmm.Modelo.DatosMenu;
import com.josevicente.proyecto_finalpmm.Modelo.Jugador;
import com.josevicente.proyecto_finalpmm.R;

import java.util.ArrayList;


public class MenuAdapter extends RecyclerView.Adapter {
    //Variables
    private ArrayList<DatosMenu> listadoOpcionesDelMenu;
    public Jugador jugador = new Jugador();

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

        @Override
        public void onClick(View v) {
            int positon = getAdapterPosition();
            switch (positon){
                case 0:
                    //Cargamos el Fragment PerfilFragment desde el método creado
                    FPerfil(v);
                    break;
                case 1:
                    //Cargamos el Fragment JuegoFragment desde el método creado
                    FJuego(v);
                    break;
                case 2:
                    //Cargamos el Fragment FotoFragment desde el método creado
                    FFoto(v);
                    break;
                case 3:
                    //Cargamos el Fragment MapaFragment desde el método creado
                    FMapa(v);
                    break;
            }
        }
    }

    //Método para cargar el Fragment de Perfil
    public void FPerfil(View view){
        //Cargamos el Fragment Perfil
        AppCompatActivity activityPerfil = (AppCompatActivity) view.getContext();
        PerfilFragment perfilFragment = new PerfilFragment();
        activityPerfil.getFragmentManager().beginTransaction()
                .replace(R.id.menuDinamicoID, perfilFragment)
                .commit();
    }

    //Método para cargar el Fragment de Juego con sus correspondientes atributos
    public void FJuego(View view){
        //Cargamos el Fragment Juego
        AppCompatActivity activityJuego = (AppCompatActivity) view.getContext();
        JuegoFragment juegoFragment = JuegoFragment.newInstance(jugador.getNickname(), String.valueOf(jugador.getPuntos()));
        activityJuego.getFragmentManager().beginTransaction()
                .replace(R.id.menuDinamicoID, juegoFragment)
                .commit();

        Log.i("NICK JUEGO",jugador.getNickname());
    }

    //Método para cargar el Fragment Foto
    public void FFoto(View view){
        AppCompatActivity activityFoto = (AppCompatActivity) view.getContext();
        FotoFragment fotoFragment = new FotoFragment();
        activityFoto.getFragmentManager().beginTransaction()
                .replace(R.id.menuDinamicoID, fotoFragment)
                .addToBackStack(null)
                .commit();
    }

    //Método para cargar el Fragment Mapa
    public void FMapa(View view){
        AppCompatActivity activityMapa = (AppCompatActivity) view.getContext();
        MapaFragment mapaFragment = new MapaFragment();
        activityMapa.getFragmentManager().beginTransaction()
                .replace(R.id.menuDinamicoID, mapaFragment)
                .addToBackStack(null)
                .commit();
    }

}
