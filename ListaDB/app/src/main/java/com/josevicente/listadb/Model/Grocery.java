package com.josevicente.listadb.Model;

/**
 * Created by JoseVicente on 28/11/2017.
 */

//3-Creamos la nueva clase Grocery en la carpeta Model
public class Grocery {
    private String name;
    private String quantity;
    private String dateItem;
    private int id;

    //Constructor Vacío
    public Grocery(){
    }

    //Constructor con Generate
    public Grocery(String name, String quantity, String dateItem, int id) {
        this.name = name;
        this.quantity = quantity;
        this.dateItem = dateItem;
        this.id = id;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDateItem() {
        return dateItem;
    }

    public void setDateItem(String dateItem) {
        this.dateItem = dateItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
