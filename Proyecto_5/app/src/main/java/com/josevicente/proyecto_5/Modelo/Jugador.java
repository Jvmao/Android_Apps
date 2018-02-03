package com.josevicente.proyecto_5.Modelo;

import java.io.Serializable;

/**
 * Created by JoseVicente on 23/01/2018.
 */

public class Jugador implements Serializable{
    //Variables Jugador
    private String nombre ="GUEST";
    private String apellido ="GUEST";
    private String nickname="GUEST";
    private int edad=0;
    private int puntos=0;
    private int id=0;

    //Constructor Vacío
    public Jugador(){}

    //Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", nickname='" + nickname.toString() + '\'' +
                ", edad=" + edad +
                ", puntos=" + puntos +
                ", id=" + id +
                '}';
    }
}
