package com.examples.ejemplo_navdrawer;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class UserListActivity extends AppCompatActivity {

    private UserDatabaseHelper dbHelper;
    private ListView listViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listViewUsers = findViewById(R.id.listViewUsers);
        dbHelper = new UserDatabaseHelper(this);

        // Cargar y mostrar los usuarios
        loadUsers();
    }

    private void loadUsers() {
        Cursor cursor = dbHelper.getAllUsers();

        // Verifica que el cursor no sea nulo
        if (cursor != null) {
            // Imprimir los nombres de las columnas en el log
            String[] columnNames = cursor.getColumnNames();
            for (String name : columnNames) {
                Log.d("Column Name", name);
            }

            // Verifica que el cursor tenga resultados
            if (cursor.getCount() > 0) {
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                        this,
                        android.R.layout.simple_list_item_1, // Cambié a simple_list_item_1
                        cursor,
                        new String[]{UserDatabaseHelper.COLUMN_USERNAME}, // Solo nombre de usuario
                        new int[]{android.R.id.text1},
                        0);
                listViewUsers.setAdapter(adapter);
            } else {
                // Manejo si no hay usuarios
                Log.d("UserListActivity", "No hay usuarios registrados.");
            }
        } else {
            // Manejo si el cursor es nulo
            Log.d("UserListActivity", "El cursor es nulo");
        }
    }
}
