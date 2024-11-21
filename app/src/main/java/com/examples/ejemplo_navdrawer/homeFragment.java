package com.examples.ejemplo_navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {

    private TextView tvIntroduccion;
    private TextView tvWelcome; // TextView para el mensaje de bienvenida
    private Button buttonExplore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvWelcome = view.findViewById(R.id.tv_welcome); // Asegúrate de que este ID coincida
        tvIntroduccion = view.findViewById(R.id.tv_introduccion);
        buttonExplore = view.findViewById(R.id.button_explore);

        // Obtener el nombre de usuario desde los argumentos
        Bundle args = getArguments();
        if (args != null) {
            String username = args.getString("USERNAME");
            if (username != null && !username.isEmpty()) {
                tvWelcome.setText("WELCOME TO MEDICAL CARE, " + username);
            } else {
                tvWelcome.setText("WELCOME TO MEDICAL CARE"); // Mensaje por defecto si no hay usuario
            }
        }

        // Configura el mensaje de introducción
        // Configura el mensaje de introducción
        tvIntroduccion.setText("Bienvenido a Medical Care. \n\n" +
                "En nuestra aplicación, podrás acceder a herramientas prácticas y enfocadas en tu salud, \n" +
                "como nuestra calculadora de Índice de Masa Corporal (BMI), para evaluar tu peso y obtener feedback, \n" +
                "un menú intuitivo con opciones para registrar notas importantes en un calendario, \n" +
                "y un sistema de resultados que te ayudará a comprender mejor tu estado físico. \n\n" +
                "Explora Medical Care y descubre todas las maneras en que podemos ayudarte a llevar un estilo de vida más saludable.");

        // Configura el botón de explorar
        buttonExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre el menú del Drawer
                if (getActivity() instanceof MainActivity) {
                    DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
                    if (drawerLayout != null) {
                        drawerLayout.openDrawer(GravityCompat.START); // Abre el Drawer desde la izquierda
                    }
                }
            }
        });

        return view;
    }
}

