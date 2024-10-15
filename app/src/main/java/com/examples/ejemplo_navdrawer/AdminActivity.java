package com.examples.ejemplo_navdrawer;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private EditText editTextAdminUsername, editTextAdminPassword;
    private Button buttonLogin, buttonBack; // Agrega el botón de regreso
    private ListView listViewUsers;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        editTextAdminUsername = findViewById(R.id.editTextAdminUsername);
        editTextAdminPassword = findViewById(R.id.editTextAdminPassword);
        buttonLogin = findViewById(R.id.buttonAdminLogin);
        buttonBack = findViewById(R.id.buttonBack); // Inicializa el botón de regreso
        listViewUsers = findViewById(R.id.listViewUsers);
        dbHelper = new UserDatabaseHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextAdminUsername.getText().toString();
                String password = editTextAdminPassword.getText().toString();
                validateAdminCredentials(username, password);
            }
        });

        // Configura el click listener para el botón "Regresar"
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual y regresa a la anterior
            }
        });
    }

    private void validateAdminCredentials(String username, String password) {
        if (username.equals("admin") && password.equals("admin123")) {
            // Acceso concedido
            loadUsers();
        } else {
            // Acceso denegado
            Toast.makeText(this, "Acceso denegado", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUsers() {
        Cursor cursor = dbHelper.getAllUsers(); // Obtener todos los usuarios
        ArrayList<String> userList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                // Usa COLUMN_USERNAME en lugar de "username"
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USERNAME));
                userList.add(username);
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        listViewUsers.setAdapter(adapter);
    }
}
