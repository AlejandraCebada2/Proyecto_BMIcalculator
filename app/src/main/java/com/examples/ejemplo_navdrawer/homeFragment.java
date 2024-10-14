package com.examples.ejemplo_navdrawer;

import android.content.Intent;
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
    private Button buttonExplore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvIntroduccion = view.findViewById(R.id.tv_introduccion);
        buttonExplore = view.findViewById(R.id.button_explore);

        tvIntroduccion.setText("Bienvenido a nuestra aplicación de herramientas útiles. \n\n" +
                "En nuestra aplicación, podrás acceder a una variedad de herramientas prácticas y fáciles de usar, \n" +
                "como nuestra pantalla de inicio, donde podrás encontrar información importante, \n" +
                "nuestra calculadora de propinas, para ayudarte a calcular la cantidad adecuada, \n" +
                "nuestra calculadora de Índice de Masa Corporal (BMI), para evaluar tu salud, \n" +
                "nuestra sección de clima, para mantenerse informado sobre el tiempo, \n" +
                "y nuestra herramienta de conversión de divisas, para ayudarte con tus necesidades de cambio de moneda. \n\n" +
                "Explora nuestra aplicación y descubre todas las formas en que podemos ayudarte.");

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
