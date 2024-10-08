package com.examples.ejemplo_navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class BMIFragment extends Fragment {

    private EditText etPeso, etAltura;
    private Button btnCalcular;
    private TextView tvResultado;

    public BMIFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        etPeso = view.findViewById(R.id.et_peso);
        etAltura = view.findViewById(R.id.et_altura);
        btnCalcular = view.findViewById(R.id.btn_calcular);
        tvResultado = view.findViewById(R.id.tv_resultado);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularBMI();
            }
        });

        return view;
    }

    private void calcularBMI() {
        String pesoStr = etPeso.getText().toString();
        String alturaStr = etAltura.getText().toString();

        if (pesoStr.isEmpty() || alturaStr.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, ingresa ambos valores", Toast.LENGTH_SHORT).show();
            return;
        }

        double peso = Double.parseDouble(pesoStr);
        double altura = Double.parseDouble(alturaStr) / 100; // Convertir altura de cm a m

        double bmi = peso / (altura * altura);

        String resultado = String.format("Tu BMI es: %.2f", bmi);

        if (bmi < 18.5) {
            resultado += "\nCategoría: Bajo peso";
        } else if (bmi < 25) {
            resultado += "\nCategoría: Peso normal";
        } else if (bmi < 30) {
            resultado += "\nCategoría: Sobrepeso";
        } else {
            resultado += "\nCategoría: Obesidad";
        }

        tvResultado.setText(resultado);
    }
}
