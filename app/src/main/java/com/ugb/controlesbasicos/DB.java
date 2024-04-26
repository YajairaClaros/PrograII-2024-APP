package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname="productos";
    private static final int v=1;
    private static final String SQLdb = "CREATE TABLE productos (id text, rev text, idcod text, codigo text, " +
            "descrripcion text, marca text, presentacion text, precio text, foto text, actualizado text)";
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, v);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLdb);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //actualizar la estrucutra de la BD.
    }
    public String administrar_amigos(String accion, String[] datos){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "";
            if (accion.equals("nuevo")) {
                sql = "INSERT INTO productos(id,rev, idcod, codigo, descripcion, marca, presentacion, precio, foto, actualizado) VALUES('"+ datos[0] +"','"+ datos[1] +"', '" + datos[2] +
                        "','" + datos[3] + "','" + datos[4] + "','" + datos[5] + "','" + datos[6] + "', '"+ datos[7] +"', '"+ datos[8] +"', '"+ datos[9] +"')";
            } else if (accion.equals("modificar")) {
                sql = "UPDATE productos SET id='"+datos[0]+"', rev='"+datos[1]+"', codigo='" + datos[3] + "',descripcion='" + datos[4] + "',marca='" +
                        datos[5] + "',presentacion='" + datos[6] + "',precioo='" + datos[7] + "', foto='"+ datos[8] +"', actualizado='"+ datos[9] +"' WHERE idcod='" + datos[2] + "'";
            } else if (accion.equals("eliminar")) {
                sql = "DELETE FROM productos WHERE idcod='" + datos[2] + "'";
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
    public Cursor pendientesActualizar(){
        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM productos WHERE actualizado='no'", null);
        return cursor;
    }
}