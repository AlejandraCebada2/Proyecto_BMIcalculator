package com.examples.ejemplo_navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {

    private TextView tvIntroduccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvIntroduccion = view.findViewById(R.id.tv_introduccion);

        tvIntroduccion.setText("Bienvenido a nuestra aplicación de herramientas útiles. \n\n" +
                "En nuestra aplicación, podrás acceder a una variedad de herramientas prácticas y fáciles de usar, \n" +
                "como nuestra pantalla de inicio, donde podrás encontrar información importante, \n" +
                "nuestra calculadora de propinas, para ayudarte a calcular la cantidad adecuada, \n" +
                "nuestra calculadora de Índice de Masa Corporal (BMI), para evaluar tu salud, \n" +
                "nuestra sección de clima, para mantenerse informado sobre el tiempo, \n" +
                "y nuestra herramienta de conversión de divisas, para ayudarte con tus necesidades de cambio de moneda. \n\n" +
                "Explora nuestra aplicación y descubre todas las formas en que podemos ayudarte.");

        return view;
    }
}
