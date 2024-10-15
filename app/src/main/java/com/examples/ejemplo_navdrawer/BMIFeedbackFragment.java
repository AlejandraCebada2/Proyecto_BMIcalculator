package com.examples.ejemplo_navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class BMIFeedbackFragment extends Fragment {

    private EditText etBMI;
    private Button btnCompare;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> bmiComparisons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi_feedback, container, false);

        etBMI = view.findViewById(R.id.et_bmi);
        btnCompare = view.findViewById(R.id.btn_compare);
        listView = view.findViewById(R.id.list_view);

        bmiComparisons = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, bmiComparisons);
        listView.setAdapter(adapter);

        btnCompare.setOnClickListener(v -> compareBMI());

        return view;
    }

    private void compareBMI() {
        String bmiInput = etBMI.getText().toString().trim();

        if (bmiInput.isEmpty()) {
            // Manejar el caso en que no se ingresa BMI
            return;
        }

        double bmiValue = Double.parseDouble(bmiInput);
        fetchBMICategories(bmiValue);
    }

    private void fetchBMICategories(double bmi) {
        bmiComparisons.clear();
        bmiComparisons.add("Organización Mundial de la Salud (OMS): " + getBMIStatus(bmi));
        bmiComparisons.add("CDC: " + getBMIStatus(bmi));
        bmiComparisons.add("Harvard Medical School: " + getBMIStatus(bmi));

        adapter.notifyDataSetChanged();
    }

    private String getBMIStatus(double bmi) {
        if (bmi < 18.5) return "Bajo peso";
        else if (bmi < 24.9) return "Normal";
        else if (bmi < 29.9) return "Sobrepeso";
        else return "Obesidad";
    }
}
