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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

public class BMIFragment extends Fragment {

    private EditText etPeso, etAltura;
    private Button btnCalcular, btnVerLista;
    private TextView tvResultado;
    private SharedViewModel sharedViewModel;

    public BMIFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        etPeso = view.findViewById(R.id.et_peso);
        etAltura = view.findViewById(R.id.et_altura);
        btnCalcular = view.findViewById(R.id.btn_calcular);
        btnVerLista = view.findViewById(R.id.btn_ver_lista);
        tvResultado = view.findViewById(R.id.tv_resultado);

        // Obtener el ViewModel compartido
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.init(getActivity()); // Asegúrate de pasar el contexto

        // Configurar el botón para calcular el BMI
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularBMI();
            }
        });

        // Configurar el botón para navegar a la lista de BMIs
        btnVerLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new BMIListFragment());
                transaction.addToBackStack(null);
                transaction.commit();
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

        try {
            double peso = Double.parseDouble(pesoStr);
            double altura = Double.parseDouble(alturaStr) / 100; // Convertir altura de cm a m

            double bmi = peso / (altura * altura);

            // Agregar el BMI a la lista en el ViewModel
            sharedViewModel.addBMI(bmi);

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
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Error en los valores ingresados", Toast.LENGTH_SHORT).show();
        }
    }
}
