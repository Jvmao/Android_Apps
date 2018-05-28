package com.josevicente.ap2app.Model;

import com.github.mikephil.charting.data.PieData;

/**
 * Created by JoseVicente on 20/02/2018.
 */

public class Gasto{
    private String id;
    private String concepto;
    private String mes;
    private String importe;

    public Gasto(){}

    public Gasto(String id, String concepto, String mes, String importe) {
        this.id = id;
        this.concepto = concepto;
        this.mes = mes;
        this.importe = importe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }
}
