package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname="tienda";
    private static final int v=1;
    private static final String SQLdb = "CREATE TABLE tienda (idpro integer primary key autoincrement, codigo text, " +
            "descripcion text, marca text, presentacion text, precio text, foto text)";

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLdb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public String administrar_tienda(String accion, String[] datos){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "";
            if (accion.equals("nuevo")) {
                sql = "INSERT INTO tienda(codigo, descripcion, marca, presentacion, precio, foto) VALUES('" + datos[1] + "','" + datos[2] + "','" + datos[3] + "','" + datos[4] + "','" + datos[5] + "', '"+ datos[6] +"')";
            } else if (accion.equals("modificar")) {
                sql = "UPDATE tienda SET codigo='" + datos[1] + "',descripcion='" + datos[2] + "',marca='" + datos[3] + "',presentacion='" + datos[4] + "',precio='" + datos[5] + "', foto='"+ datos[6] + "' WHERE idpro='" + datos[0] + "'";
            } else if (accion.equals("eliminar")) {
                sql = "DELETE FROM tienda WHERE idpro='" + datos[0] + "'";
            }
            db.execSQL(sql);
            return "ok";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    public Cursor consultar_tienda(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tienda ORDER BY codigo", null);
        return cursor;
    }
}
