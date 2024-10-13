package com.examples.ejemplo_navdrawer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotesFragment extends Fragment {
    private EditText editTextNote;
    private Button buttonAddNote;
    private ListView listViewNotes;
    private ArrayList<Note> notesList;  // Cambiar a una lista de objetos Note
    private ArrayAdapter<Note> notesAdapter;
    private int selectedNoteIndex = -1;

    private FirebaseFirestore db;
    private CollectionReference notesCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        editTextNote = view.findViewById(R.id.edit_text_note);
        buttonAddNote = view.findViewById(R.id.button_add_note);
        listViewNotes = view.findViewById(R.id.list_view_notes);
        notesList = new ArrayList<>();

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();
        notesCollection = db.collection("notes");
        CollectionReference databaseReference = db.collection("notes");
        // Cargar notas al inicio
        loadNotes();

        notesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notesList);
        listViewNotes.setAdapter(notesAdapter);

        buttonAddNote.setOnClickListener(v -> addOrUpdateNote());

        listViewNotes.setOnItemClickListener((parent, view1, position, id) -> {
            selectedNoteIndex = position;
            editTextNote.setText(notesList.get(position).getContent());
        });

        return view;
    }

    private void addOrUpdateNote() {
        String noteContent = editTextNote.getText().toString();
        if (!noteContent.isEmpty()) {
            if (selectedNoteIndex == -1) {
                // Agregar nueva nota
                Map<String, Object> noteData = new HashMap<>();
                noteData.put("note", noteContent);
                notesCollection.add(noteData).addOnSuccessListener(documentReference -> {
                    notesList.add(new Note(documentReference.getId(), noteContent));
                    notesAdapter.notifyDataSetChanged();
                    editTextNote.setText("");
                    Toast.makeText(getActivity(), "Nota agregada", Toast.LENGTH_SHORT).show();
                    Log.d("NotesFragment", "Nota añadida: " + noteContent); // Añadir log
                }).addOnFailureListener(e -> {
                    Log.e("NotesFragment", "Error al añadir nota: " + e.getMessage()); // Log de error
                });
            } else {
                // Actualizar nota existente
                String documentId = notesList.get(selectedNoteIndex).getId();
                notesCollection.document(documentId).update("note", noteContent).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notesList.get(selectedNoteIndex).setContent(noteContent);
                        notesAdapter.notifyDataSetChanged();
                        editTextNote.setText("");
                        selectedNoteIndex = -1;
                        Toast.makeText(getActivity(), "Nota actualizada", Toast.LENGTH_SHORT).show();
                        Log.d("NotesFragment", "Nota actualizada: " + noteContent); // Añadir log
                    } else {
                        Log.e("NotesFragment", "Error al actualizar nota: " + task.getException()); // Log de error
                    }
                });
            }
        }
    }


    private void loadNotes() {
        notesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notesList.clear(); // Limpiar la lista antes de cargar
                for (DocumentSnapshot document : task.getResult()) {
                    String note = document.getString("note");
                    if (note != null) { // Verificar si el campo existe
                        notesList.add(new Note(document.getId(), note)); // Guardar ID del documento
                        Log.d("NotesFragment", "Nota cargada: " + note); // Añadir log
                    }
                }
                notesAdapter.notifyDataSetChanged();
            } else {
                Log.e("NotesFragment", "Error al cargar notas: " + task.getException()); // Log de error
                Toast.makeText(getActivity(), "Error al cargar notas", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Clase interna para manejar notas con ID
    private static class Note {
        private String id;
        private String content;

        public Note(String id, String content) {
            this.id = id;
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return content; // Para mostrar el contenido en ListView
        }
    }
}
