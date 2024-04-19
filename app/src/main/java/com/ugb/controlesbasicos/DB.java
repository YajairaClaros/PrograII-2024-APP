package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname="peliculas";
    private static final int v=1;
    private static final String SQLdb = "CREATE TABLE peliculas (id text, rev text, idPeli text, titulo text, " +
            "sinopsis text, duracion text, actor text, foto text)";
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
    public String administrar_peliculas(String accion, String[] datos){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "";
            if (accion.equals("nuevo")) {
                sql = "INSERT INTO peliculas(id,rev,idPeli,titulo,sinopsis,duracion,actor,foto) VALUES('"+ datos[0] +"','"+ datos[1] +"', '" + datos[2] +
                        "','" + datos[3] + "','" + datos[4] + "','" + datos[5] + "','" + datos[6] + "', '"+ datos[7] +"')";
            } else if (accion.equals("modificar")) {
                sql = "UPDATE peliculas SET id='"+datos[0]+"', rev='"+datos[1]+"', titulo='" + datos[3] + "',sinopsis='" + datos[4] + "',duracion='" +
                        datos[5] + "',actor='" + datos[6] + "', foto='"+ datos[7] +"' WHERE idPeli='" + datos[2] + "'";
            } else if (accion.equals("eliminar")) {
                sql = "DELETE FROM peliculas WHERE idPeli='" + datos[2] + "'";
            }
            db.execSQL(sql);
            return "ok";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    public Cursor consultar_peliculas(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM peliculas ORDER BY titulo", null);
        return cursor;
    }
}