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
    private List<BmiItem> bmiList; // Lista de BmiItem
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

        // Observar la lista de BmiItems
        sharedViewModel.getBMIList().observe(getViewLifecycleOwner(), new Observer<List<BmiItem>>() {
            @Override
            public void onChanged(List<BmiItem> bmiItems) {
                bmiList.clear(); // Limpiar la lista de BmiItems
                bmiList.addAll(bmiItems); // Agregar los nuevos BmiItems a la lista
                bmiAdapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
            }
        });

        return view;
    }
}
