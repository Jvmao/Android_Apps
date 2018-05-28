package com.josevicente.ap2app.Model;

/**
 * Created by JoseVicente on 23/02/2018.
 */

public class Producto {
    private String id;
    private String producto;
    private String cantidad;

    public Producto(){}

    public Producto(String id, String producto, String cantidad) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
