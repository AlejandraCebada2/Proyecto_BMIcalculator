package com.examples.ejemplo_navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class BMIListFragment extends Fragment {

    private ListView bmiListView;
    private SharedViewModel sharedViewModel;
    private ArrayAdapter<String> bmiAdapter;
    private List<String> bmiStringList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b_m_i_list, container, false);

        bmiListView = view.findViewById(R.id.bmi_list_view);
        bmiStringList = new ArrayList<>();
        bmiAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, bmiStringList);
        bmiListView.setAdapter(bmiAdapter);

        // Obtener el ViewModel compartido
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observar la lista de BMIs
        sharedViewModel.getBMIList().observe(getViewLifecycleOwner(), new Observer<List<BMIEntry>>() {
            @Override
            public void onChanged(List<BMIEntry> bmiEntries) {
                bmiStringList.clear();
                for (BMIEntry entry : bmiEntries) {
                    bmiStringList.add(String.format("BMI: %.2f, Fecha: %s", entry.getBmi(), entry.getDate()));
                }
                bmiAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }}

