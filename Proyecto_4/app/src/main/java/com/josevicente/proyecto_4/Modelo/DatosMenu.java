package com.josevicente.proyecto_4.Modelo;


import android.os.Parcel;
import android.os.Parcelable;

//Creamos el objeto DatosMenu y lo implementamos a Parceable
public class DatosMenu implements Parcelable{
    //Datos de Texto
    private  String titulo;
    //Datos de Imagenes
    private int iconoMenu;

    //Constructor
    public DatosMenu(String titulo, int iconoMenu) {
        this.titulo = titulo;
        this.iconoMenu = iconoMenu;
    }

    //Getters and Setters
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
    protected DatosMenu(Parcel in) {
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

    public static final Parcelable.Creator<DatosMenu> CREATOR = new Parcelable.Creator<DatosMenu>(){
        //Implementamos los siguiente métodos
        @Override
        public DatosMenu createFromParcel(Parcel in) {
            return new DatosMenu(in);
        }

        @Override
        public DatosMenu[] newArray(int size) {
            return new DatosMenu[size];
        }
    };
}
