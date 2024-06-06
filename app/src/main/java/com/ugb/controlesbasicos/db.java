package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class db extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "usuarios.db";
    private static final int DATABASE_VERSION = 1;

    public db(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla(s)
        String CREATE_TABLE = "CREATE TABLE tablas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dui TEXT, " +
                "contra TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Actualizar la base de datos si es necesario
        db.execSQL("DROP TABLE IF EXISTS tabla");
        onCreate(db);
    }
}
