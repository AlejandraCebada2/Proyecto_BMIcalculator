package com.examples.ejemplo_navdrawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<BmiItem>> bmiList = new MutableLiveData<>(new ArrayList<>());
    private DatabaseReference databaseReference;
    private SharedPreferences prefs;

    private final String PREFS_NAME = "BMI_PREFS";
    private final String BMI_KEY = "BMI_LIST";

    public void init(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("bmiEntries");
        loadBMIListFromLocal();
        loadBMIListFromFirebase();
    }

    public LiveData<List<BmiItem>> getBMIList() {
        return bmiList;
    }

    // Método para agregar un BMI
    public void addBMI(double bmiValue) {
        // Obtener la fecha actual
        Date currentDate = new Date();

        // Determinar la categoría
        String categoria;
        if (bmiValue < 18.5) {
            categoria = "Bajo peso";
        } else if (bmiValue < 25) {
            categoria = "Peso normal";
        } else if (bmiValue < 30) {
            categoria = "Sobrepeso";
        } else {
            categoria = "Obesidad";
        }

        // Crear un nuevo objeto BmiItem con la categoría y la fecha
        BmiItem newBmiItem = new BmiItem(bmiValue, categoria, currentDate);

        // Guardar el BMI en Firebase
        String id = databaseReference.push().getKey();
        if (id != null) {
            databaseReference.child(id).setValue(newBmiItem).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("FirebaseSuccess", "Datos guardados correctamente en Firebase.");
                } else {
                    Log.e("FirebaseError", "Error al guardar datos: ", task.getException());
                }
            });
        }

        // Guardar localmente
        saveBMIListLocally(newBmiItem);

        // Actualizar la lista local
        List<BmiItem> currentList = bmiList.getValue();
        if (currentList != null) {
            currentList.add(newBmiItem);
            bmiList.setValue(currentList); // Actualizar LiveData
        }
    }

    // Guardar la lista localmente en SharedPreferences
    private void saveBMIListLocally(BmiItem newBmiItem) {
        SharedPreferences.Editor editor = prefs.edit();
        String existingEntries = prefs.getString(BMI_KEY, "");
        String newEntry = newBmiItem.getValue() + "," + newBmiItem.getCategory() + "," + new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newBmiItem.getDate()) + ";";
        editor.putString(BMI_KEY, existingEntries + newEntry);
        editor.apply();
    }

    // Cargar la lista de BMIs desde SharedPreferences
    private void loadBMIListFromLocal() {
        String bmiListString = prefs.getString(BMI_KEY, "");
        List<BmiItem> localBmiList = new ArrayList<>();

        if (!bmiListString.isEmpty()) {
            String[] bmiArray = bmiListString.split(";");
            for (String bmiEntry : bmiArray) {
                if (!bmiEntry.isEmpty()) {
                    String[] parts = bmiEntry.split(",");
                    double bmi = Double.parseDouble(parts[0]);
                    String categoria = parts[1];
                    String dateString = parts[2];

                    try {
                        Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString);
                        localBmiList.add(new BmiItem(bmi, categoria, date));
                    } catch (Exception e) {
                        Log.e("SharedPrefs", "Error al convertir la fecha: " + e.getMessage());
                    }
                }
            }
        }
        bmiList.setValue(localBmiList);
    }

    // Cargar la lista de Firebase
    private void loadBMIListFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BmiItem> firebaseBmiList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BmiItem entry = snapshot.getValue(BmiItem.class);
                    if (entry != null) {
                        firebaseBmiList.add(entry);
                    }
                }
                bmiList.postValue(firebaseBmiList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Error al cargar datos desde Firebase: " + databaseError.getMessage());
            }
        });
    }
}
