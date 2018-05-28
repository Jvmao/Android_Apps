package com.josevicente.ap2app.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mapa implements Parcelable {
    //Datos de Texto
    private  String titulo;
    //Datos de Imagenes
    private int iconoMenu;

    public Mapa(String titulo, int iconoMenu) {
        this.titulo = titulo;
        this.iconoMenu = iconoMenu;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIconoMenu() {
        return iconoMenu;
    }

    public void setIconoMenu(int iconoMenu) {
        this.iconoMenu = iconoMenu;
    }

    // Métodos necesarios al hacer esta clase Parcelable
    protected Mapa(Parcel in) {
        titulo = in.readString();
        iconoMenu = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeInt(iconoMenu);
    }

    public static final Parcelable.Creator<Mapa> CREATOR = new Parcelable.Creator<Mapa>(){
        //Implementamos los siguiente métodos
        @Override
        public Mapa createFromParcel(Parcel in) {
            return new Mapa(in);
        }

        @Override
        public Mapa[] newArray(int size) {
            return new Mapa[size];
        }
    };
}
