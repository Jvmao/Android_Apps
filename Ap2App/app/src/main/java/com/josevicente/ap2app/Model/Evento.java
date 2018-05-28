package com.josevicente.ap2app.Model;

import java.util.Date;

/**
 * Created by JoseVicente on 28/02/2018.
 */

public class Evento {
    private String id;
    private String titulo;
    private String fecha;
    private String hora;
    private String descripcion;

    public Evento(){}

    public Evento(String id, String titulo, String fecha, String hora, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
