package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname="tienda";
    private static final int v=1;
    private static final String SQLdb = "CREATE TABLE productos (id text, rev text, idprod text, codigo text, " +
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
                sql = "INSERT INTO productos(id, rev, idprod, codigo , descripcion,marca,presentacion,precio, foto) VALUES('" + datos[0] + "','" + datos[1] + "','" + datos[2] + "','" + datos[3] + "','" + datos[4] + "', '"+ datos[5] +"','" + datos[6] + "','" + datos[7] + "', '"+ datos[8] +"')";
            } else if (accion.equals("modificar")) {
                sql = "UPDATE productos SET id='"+ datos[0]+ "', rev='"+ datos[1] + "', codigo='" + datos[2] + "',descripcion='" + datos[3] + "',marca='" + datos[4] + "',presentacion='" + datos[5] + "',precio='" + datos[6] + "', foto='"+ datos[7] + "' WHERE idprod='" + datos[2] + "'";
            } else if (accion.equals("eliminar")) {
                sql = "DELETE FROM productos WHERE idprod='" + datos[2] + "'";
            }
            db.execSQL(sql);
            return "ok";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    public Cursor consultar_amigos(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM productos ORDER BY codigo", null);
        return cursor;
    }
}
