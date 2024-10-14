package com.examples.ejemplo_navdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonRegister;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        dbHelper = new UserDatabaseHelper(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString();

                // Validar que el nombre de usuario y la contraseña no estén vacíos
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Por favor, ingrese un nombre de usuario y una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Agregar el usuario a la base de datos
                if (dbHelper.addUser(username, password)) {
                    Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
