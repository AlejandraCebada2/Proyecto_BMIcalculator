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

import java.util.ArrayList;
import java.util.List;

public class BMIListFragment extends Fragment {

    private RecyclerView bmiRecyclerView;
    private BMIAdapter bmiAdapter;
    private List<BmiItem> bmiList; // Cambiar a BmiItem
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b_m_i_list, container, false);

        // Inicializar RecyclerView
        bmiRecyclerView = view.findViewById(R.id.bmi_recycler_view);
        bmiList = new ArrayList<>(); // Inicializar la lista de BmiItem
        bmiAdapter = new BMIAdapter(bmiList); // Pasar la lista de BmiItem al adapter
        bmiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bmiRecyclerView.setAdapter(bmiAdapter);

        // Obtener el ViewModel compartido
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observar la lista de BMIs
        sharedViewModel.getBMIList().observe(getViewLifecycleOwner(), new Observer<List<BMIEntry>>() {
            @Override
            public void onChanged(List<BMIEntry> bmiEntries) {
                bmiList.clear(); // Limpiar la lista de BmiItem
                for (BMIEntry entry : bmiEntries) {
                    // Crear un nuevo objeto BmiItem y agregarlo a la lista
                    bmiList.add(new BmiItem(entry.getBmi(), entry.getDate()));
                }
                bmiAdapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
            }
        });

        return view;
    }
}
