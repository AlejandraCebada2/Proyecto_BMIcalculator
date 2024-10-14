package com.examples.ejemplo_navdrawer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotesFragment extends Fragment {
    private EditText editTextNote;
    private FloatingActionButton buttonAddNote;
    private ListView listViewNotes;
    private ArrayList<Note> notesList;
    private NotesAdapter notesAdapter;
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
        loadNotes();

        notesAdapter = new NotesAdapter();
        listViewNotes.setAdapter(notesAdapter);

        buttonAddNote.setOnClickListener(v -> addOrUpdateNote());

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
                }).addOnFailureListener(e -> {
                    Log.e("NotesFragment", "Error al añadir nota: " + e.getMessage());
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
                    } else {
                        Log.e("NotesFragment", "Error al actualizar nota: " + task.getException());
                    }
                });
            }
        }
    }

    private void loadNotes() {
        notesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notesList.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    String note = document.getString("note");
                    if (note != null) {
                        notesList.add(new Note(document.getId(), note));
                    }
                }
                notesAdapter.notifyDataSetChanged();
                Log.d("NotesFragment", "Notas cargadas: " + notesList.size()); // Añadir log
            } else {
                Log.e("NotesFragment", "Error al cargar notas: " + task.getException());
                Toast.makeText(getActivity(), "Error al cargar notas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteNote(int position) {
        String documentId = notesList.get(position).getId();
        notesCollection.document(documentId).delete().addOnSuccessListener(aVoid -> {
            notesList.remove(position);
            notesAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Nota eliminada", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Log.e("NotesFragment", "Error al eliminar nota: " + e.getMessage());
        });
    }

    private void editNote(int position) {
        selectedNoteIndex = position;
        editTextNote.setText(notesList.get(position).getContent());
    }

    private class NotesAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return notesList.size();
        }

        @Override
        public Object getItem(int position) {
            return notesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.note_item, parent, false);
            }

            TextView textNote = convertView.findViewById(R.id.text_note);
            Button buttonEdit = convertView.findViewById(R.id.button_edit);
            Button buttonDelete = convertView.findViewById(R.id.button_delete);

            Note note = notesList.get(position);
            textNote.setText(note.getContent());

            buttonEdit.setOnClickListener(v -> editNote(position));
            buttonDelete.setOnClickListener(v -> deleteNote(position));

            return convertView;
        }
    }

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
            return content;
        }
    }
}
