package com.examples.ejemplo_navdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private View line1, line2; // Define las líneas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Encuentra las vistas de las líneas (asegúrate de que existan en tu layout)
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);

        Button buttonGetStarted = findViewById(R.id.button_get_started);
        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Opcional: cierra la actividad de bienvenida
            }
        });

        // Iniciar las animaciones
        startLineAnimation(line1);
        startLineAnimation(line2);
    }

    private void startLineAnimation(View line) {
        // Carga las animaciones de escala y de opacidad
        line.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_line));
        line.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_line));
    }
}
