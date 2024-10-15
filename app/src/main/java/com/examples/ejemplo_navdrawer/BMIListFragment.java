package com.examples.ejemplo_navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.examples.ejemplo_navdrawer.BMIAdapter;
import com.examples.ejemplo_navdrawer.BMIEntry;
import com.examples.ejemplo_navdrawer.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class BMIListFragment extends Fragment {

    private RecyclerView bmiRecyclerView;
    private BMIAdapter bmiAdapter;
    private List<String> bmiStringList;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b_m_i_list, container, false);

        // Inicializar RecyclerView
        bmiRecyclerView = view.findViewById(R.id.bmi_recycler_view);
        bmiStringList = new ArrayList<>();
        bmiAdapter = new BMIAdapter(bmiStringList);
        bmiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bmiRecyclerView.setAdapter(bmiAdapter);

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
    }
}
