package com.examples.ejemplo_navdrawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private Button buttonBack;
    private ListView listViewDevelopers;
    private TextView textRightsReserved;

    private ImageView iconFacebook, iconTwitter, iconInstagram, iconYouTube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        buttonBack = findViewById(R.id.buttonBack);
        listViewDevelopers = findViewById(R.id.listViewDevelopers);
        textRightsReserved = findViewById(R.id.textRightsReserved);

        // Iconos de redes sociales
        iconFacebook = findViewById(R.id.iconFacebook);
        iconTwitter = findViewById(R.id.iconTwitter);
        iconInstagram = findViewById(R.id.iconInstagram);
        iconYouTube = findViewById(R.id.iconYouTube);

        // Acción para regresar
        buttonBack.setOnClickListener(v -> finish());

        // Cargar lista de desarrolladores
        loadDevelopers();

        // Configurar los clics para los íconos de redes sociales
        setSocialMediaLinks();
    }

    private void loadDevelopers() {
        ArrayList<String> developerList = new ArrayList<>();
        developerList.add("Evelyn A. Cebada Cortés - Desarrolladora Principal");
        developerList.add("Pedro Everardo Hernández Valerio - Desarrollador Backend");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, developerList);
        listViewDevelopers.setAdapter(adapter);
    }

    private void setSocialMediaLinks() {
        iconFacebook.setOnClickListener(v -> openLink("https://www.facebook.com"));
        iconTwitter.setOnClickListener(v -> openLink("https://www.twitter.com"));
        iconInstagram.setOnClickListener(v -> openLink("https://www.instagram.com"));
        iconYouTube.setOnClickListener(v -> openLink("https://www.youtube.com"));
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
