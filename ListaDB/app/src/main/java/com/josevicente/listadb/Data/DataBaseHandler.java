package com.josevicente.listadb.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.josevicente.listadb.Model.Grocery;
import com.josevicente.listadb.Util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JoseVicente on 29/11/2017.
 */

//8-Creamos la clase DataBaseHandler para manejar la BBDD
public class DataBaseHandler extends SQLiteOpenHelper {
    private Context ctx;

    public DataBaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROCERY_TABLE = "CREATE TABLE "+Constants.TABLE_NAME+" ("
                +Constants.KEY_ID+" INTEGER PRIMARY KEY,"
                +Constants.KEY_GROCERY_ITEM+" TEXT,"
                +Constants.KEY_QTY_NUMBER+" TEXT, "
                +Constants.KEY_DATE_NAME+ " LONG"+")";

        db.execSQL(CREATE_GROCERY_TABLE); //Ejecutamos la consulta SQL


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME); //Elimina la BBDD si ya existe
        onCreate(db);//Vuelve a crear la BBDD
    }

    //9-Generamos los siguientes métodos
    /**
     * CRUD: CREATE, READ, UPDATE, DELETE.
     */

    //Guardar un elemento
    public void addElement(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM,grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER,grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME,System.currentTimeMillis());

        //Insertar Fila
        db.insert(Constants.TABLE_NAME,null,values);

        Log.d("SAVED!!!","Saved to DB "+grocery.getId());
    }

    //Conseguir un elemento
    public Grocery getGrocery(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME,new String[]{Constants.KEY_ID,
        Constants.KEY_GROCERY_ITEM,Constants.KEY_QTY_NUMBER,Constants.KEY_DATE_NAME},
                Constants.KEY_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

            Grocery grocery = new Grocery();
            grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
            grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

            //Convertimos el elemento tiempo
            DateFormat dt = DateFormat.getDateInstance();
            String formatedDate = dt.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
            grocery.setDateItem(formatedDate);

        return grocery;
    }


    //Método para listar todos los elementos
    public List<Grocery> getAllGroceries(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Grocery> groceryList = new ArrayList<>();
        Cursor cursor=db.query(Constants.TABLE_NAME,new String[]{
                Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER, Constants.KEY_DATE_NAME},
                null,null,null,null,Constants.KEY_DATE_NAME+" DESC ");

        if(cursor.moveToFirst()){
            do {
                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

                //Definimos el DateFormat
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
                grocery.setDateItem(formatedDate);

                //Añadimos a la lista
                groceryList.add(grocery);
            }while (cursor.moveToNext());
        }
        return groceryList;
    }

    //Actualizar elementos
    public int updateGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM,grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER,grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME,System.currentTimeMillis()); //Hora del sistema

        //Actualizar filas
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID+" = ?",
                new String[]{String.valueOf(grocery.getId())});
    }

    //Eliminar elementos
    public void deleteGrocery(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Grocery grocery = new Grocery();
        db.delete(Constants.TABLE_NAME,Constants.KEY_ID+" = ?",
                new String[]{String.valueOf(id)});
        Log.d("DELETED","Element Deleted "+grocery.getId());
        db.close(); //Cerramos la BBDD
    }

    //Conseguir número de elementos
    public int getGroceriesCount(){
        String countQuery="SELECT * FROM "+Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }
}
