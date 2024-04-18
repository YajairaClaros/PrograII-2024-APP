package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname="amigos";
    private static final int v=1;
    private static final String SQLdb = "CREATE TABLE amigos (id text, rev text, idAmigo text, nombre text, " +
            "direccion text, telefono text, email text, dui text, foto text)";

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLdb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // actualizar la estructura de la BD
    }
    public String administrar_amigos(String accion, String[] datos){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "";
            if (accion.equals("nuevo")) {
                sql = "INSERT INTO amigos(id, rev, idAmigo, nombre,direccion,telefono,email,dui, foto) VALUES('" + datos[0] + "','" + datos[1] + "','" + datos[2] + "','" + datos[3] + "','" + datos[4] + "', '"+ datos[5] +"','" + datos[6] + "','" + datos[7] + "', '"+ datos[8] +"')";
            } else if (accion.equals("modificar")) {
                sql = "UPDATE amigos SET id='"+ datos[0]+ "', rev='"+ datos[1] + "', nombre='" + datos[2] + "',direccion='" + datos[3] + "',telefono='" + datos[4] + "',email='" + datos[5] + "',dui='" + datos[6] + "', foto='"+ datos[7] + "' WHERE idAmigo='" + datos[2] + "'";
            } else if (accion.equals("eliminar")) {
                sql = "DELETE FROM amigos WHERE idAmigo='" + datos[2] + "'";
            }
            db.execSQL(sql);
            return "ok";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    public Cursor consultar_amigos(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM amigos ORDER BY nombre", null);
        return cursor;
    }
}
