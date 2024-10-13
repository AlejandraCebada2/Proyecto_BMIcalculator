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
    private final MutableLiveData<List<BMIEntry>> bmiList = new MutableLiveData<>(new ArrayList<>());
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

    public LiveData<List<BMIEntry>> getBMIList() {
        return bmiList;
    }

    public void addBMI(double bmiValue) {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        BMIEntry entry = new BMIEntry(bmiValue, currentDate);

        // Guardar en Firebase
        // Guardar en Firebase
        String id = databaseReference.push().getKey();
        if (id != null) {
            databaseReference.child(id).setValue(entry).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Datos guardados correctamente
                    Log.d("FirebaseSuccess", "Datos guardados correctamente en Firebase.");
                } else {
                    // Manejar el error
                    Log.e("FirebaseError", "Error al guardar datos: ", task.getException());
                }
            });
        }

// Guardar localmente
        saveBMIListLocally(entry);

// Agregar a la lista local
        List<BMIEntry> currentList = bmiList.getValue();
        if (currentList != null) {
            currentList.add(entry);
            bmiList.setValue(currentList);
        }

    }

    private void saveBMIListLocally(BMIEntry entry) {
        SharedPreferences.Editor editor = prefs.edit();
        String existingEntries = prefs.getString(BMI_KEY, "");
        String newEntry = entry.getBmi() + "," + entry.getDate() + ";";
        editor.putString(BMI_KEY, existingEntries + newEntry);
        editor.apply();
    }

    private void loadBMIListFromLocal() {
        String bmiListString = prefs.getString(BMI_KEY, "");
        List<BMIEntry> localBmiList = new ArrayList<>();

        if (!bmiListString.isEmpty()) {
            String[] bmiArray = bmiListString.split(";");
            for (String bmiEntry : bmiArray) {
                if (!bmiEntry.isEmpty()) {
                    String[] parts = bmiEntry.split(",");
                    double bmi = Double.parseDouble(parts[0]);
                    String date = parts[1];
                    localBmiList.add(new BMIEntry(bmi, date));
                }
            }
        }
        bmiList.setValue(localBmiList);
    }

    private void loadBMIListFromFirebase() {
        // Aquí puedes usar un ValueEventListener para cargar desde Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BMIEntry> firebaseBmiList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BMIEntry entry = snapshot.getValue(BMIEntry.class);
                    firebaseBmiList.add(entry);
                }
                bmiList.postValue(firebaseBmiList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar el error
            }
        });
    }
}
