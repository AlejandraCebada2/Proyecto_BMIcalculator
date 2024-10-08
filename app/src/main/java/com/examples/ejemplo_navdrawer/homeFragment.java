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

        tvIntroduccion.setText("Bienvenido a nuestra calculadora de Índice de Masa Corporal (BMI). \n\n" +
                "La calculadora de BMI es una herramienta que te permite calcular tu índice de masa corporal, \n" +
                "que es un indicador de tu peso en relación con tu altura. \n\n" +
                "Con nuestra calculadora, podrás calcular tu BMI de manera rápida y fácil, \n" +
                "y obtener información sobre tu categoría de peso y cómo mejorar tu salud.");

        return view;
    }
}
