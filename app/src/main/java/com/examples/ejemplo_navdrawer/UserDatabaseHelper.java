package com.examples.ejemplo_navdrawer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db"; // Nombre de la base de datos
    private static final int DATABASE_VERSION = 1; // Versión de la base de datos

    // Tabla de usuarios
    private static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id"; // Cambiado a public
    public static final String COLUMN_USERNAME = "username"; // Cambiado a public
    public static final String COLUMN_PASSWORD = "password"; // Cambiado a public

    // Consulta SQL para crear la tabla
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT);";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecuta la consulta para crear la tabla
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Elimina la tabla anterior si existe y crea una nueva
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Método para agregar un nuevo usuario
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1; // Retorna true si la inserción fue exitosa
    }

    // Método para verificar si un usuario existe
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close(); // Asegúrate de cerrar el cursor
        db.close(); // Cierra la base de datos después de usarla
        return exists; // Retorna true si el usuario existe
    }

    // Método para obtener todos los usuarios
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }
}
