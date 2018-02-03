package com.josevicente.proyecto_5.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.josevicente.proyecto_5.Fragments.JuegoFragment;
import com.josevicente.proyecto_5.Fragments.PerfilFragment;
import com.josevicente.proyecto_5.Modelo.DatosMenu;
import com.josevicente.proyecto_5.Modelo.Jugador;
import com.josevicente.proyecto_5.R;
import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter{
    //Variables
    private ArrayList<DatosMenu> listadoOpcionesDelMenu;
    private Context mContext;
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


        //Generamos el método onClick
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
                    JuegoFragment.newInstance(jugador.getNickname(), String.valueOf(jugador.getPuntos()));
                    break;
                case 2:
                    Log.i("CLICK","Posición 3");
                    Toast.makeText(v.getContext(),"INSTRUCCIONES",Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Log.i("CLICK","Posición 4");
                    Toast.makeText(v.getContext(),"INFORMACIÓN",Toast.LENGTH_LONG).show();
                    break;
            }
        }

    }


    //Método para cargar el Fragment de Perfil
    public void FPerfil(View view){
        Log.i("CLICK","Posición 1");
        //Cargamos el Fragment Perfil
        AppCompatActivity activityPerfil = (AppCompatActivity) view.getContext();
        PerfilFragment perfilFragment = new PerfilFragment();
        activityPerfil.getFragmentManager().beginTransaction()
                .replace(R.id.menuDinamicoID, perfilFragment)
                .commit();
    }



    //Método para cargar el Fragment de Juego con sus correspondientes atributos
    public void FJuego(View view){
        Log.i("CLICK","Posición 2");
        //Cargamos el Fragment Juego
        String nick = jugador.getNickname();
        AppCompatActivity activityJuego = (AppCompatActivity) view.getContext();
        //JuegoFragment juegoFragment = new JuegoFragment();
        JuegoFragment juegoFragment = JuegoFragment.newInstance(nick, String.valueOf(jugador.getPuntos()));
        //Bundle bundle = new Bundle();
        //bundle.putString(JuegoFragment.ARG_ALIAS,nick);
        //bundle.putString(JuegoFragment.ARG_PUNTOS,String.valueOf(jugador.getPuntos()));
        //juegoFragment.setArguments(bundle); //Recogemos los parametros
        activityJuego.getFragmentManager().beginTransaction()
                .replace(R.id.menuDinamicoID, juegoFragment)
                .commit();


        Log.i("NICK JUEGO",jugador.getNickname());
    }

}
