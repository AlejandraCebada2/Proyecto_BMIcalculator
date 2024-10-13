package com.examples.ejemplo_navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class TipCalculatorFragment extends Fragment {
    private EditText billAmountEditText;
    private EditText tipPercentageEditText;
    private EditText peopleCountEditText;
    private Button calculateButton;
    private Button clearButton;
    private TextView tipTextView;


    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tip_calculate, container, false);

        billAmountEditText = view.findViewById(R.id.bill_amount_edit_text);
        tipPercentageEditText = view.findViewById(R.id.tip_percentage_edit_text);
        peopleCountEditText = view.findViewById(R.id.people_count_edit_text);
        calculateButton = view.findViewById(R.id.calculate_button);
        clearButton = view.findViewById(R.id.clear_button);
        tipTextView = view.findViewById(R.id.tip_text_view);

        // Obtener el ViewModel compartido
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateTip();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        return view;
    }

    private void calculateTip() {
        double billAmount = Double.parseDouble(billAmountEditText.getText().toString());
        String tipPercentageString = tipPercentageEditText.getText().toString();
        int peopleCount = Integer.parseInt(peopleCountEditText.getText().toString());

        double tipPercentage;
        if (tipPercentageString.contains("%")) {
            tipPercentage = Double.parseDouble(tipPercentageString.replace("%", "")) / 100;
        } else {
            tipPercentage = Double.parseDouble(tipPercentageString) / 100;
        }

        double tip = billAmount * tipPercentage;
        double totalAmount = billAmount + tip;
        double tipPerPerson = tip / peopleCount;
        double totalAmountPerPerson = totalAmount / peopleCount;

        String tipString = String.format("La propina es: $%.2f", tip);
        String totalString = String.format("El monto total es: $%.2f", totalAmount);
        String tipPerPersonString = String.format("La propina por persona es: $%.2f", tipPerPerson);
        String totalAmountPerPersonString = String.format("El monto total por persona es: $%.2f", totalAmountPerPerson);

        tipTextView.setText(tipString + "\n" + totalString + "\n" + tipPerPersonString + "\n" + totalAmountPerPersonString);
    }

    private void clearFields() {
        billAmountEditText.setText("");
        tipPercentageEditText.setText("");
        peopleCountEditText.setText("");
        tipTextView.setText("");
    }
}
