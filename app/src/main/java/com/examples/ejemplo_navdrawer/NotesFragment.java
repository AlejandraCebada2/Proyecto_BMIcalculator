package com.examples.ejemplo_navdrawer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
    private TextView textViewDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        editTextNote = view.findViewById(R.id.edit_text_note);
        buttonAddNote = view.findViewById(R.id.button_add_note);
        listViewNotes = view.findViewById(R.id.list_view_notes);
        textViewDate = view.findViewById(R.id.text_view_date);
        notesList = new ArrayList<>();

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();
        notesCollection = db.collection("notes");
        loadNotes();

        notesAdapter = new NotesAdapter();
        listViewNotes.setAdapter(notesAdapter);

        buttonAddNote.setOnClickListener(v -> addOrUpdateNote());

        // Configurar el botón para seleccionar fecha
        view.findViewById(R.id.button_select_date).setOnClickListener(v -> showDatePickerDialog());

        return view;
    }

    private void addOrUpdateNote() {
        String noteContent = editTextNote.getText().toString();
        String noteDate = textViewDate.getText().toString().replace("Fecha: ", "");
        boolean isCompleted = false;

        if (!noteContent.isEmpty()) {
            Log.d("NotesFragment", "Intentando agregar/actualizar nota: " + noteContent + ", " + noteDate);
            if (selectedNoteIndex == -1) {
                // Agregar nueva nota
                Map<String, Object> noteData = new HashMap<>();
                noteData.put("note", noteContent);
                noteData.put("date", noteDate);
                noteData.put("isCompleted", isCompleted);
                notesCollection.add(noteData).addOnSuccessListener(documentReference -> {
                    notesList.add(new Note(documentReference.getId(), noteContent, noteDate, isCompleted));
                    notesAdapter.notifyDataSetChanged();
                    editTextNote.setText("");
                    textViewDate.setText("Fecha: Ninguna");
                    Toast.makeText(getActivity(), "Nota agregada", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.e("NotesFragment", "Error al añadir nota: " + e.getMessage());
                });
            } else {
                // Actualizar nota existente
                String documentId = notesList.get(selectedNoteIndex).getId();
                notesCollection.document(documentId).update("note", noteContent, "date", noteDate, "isCompleted", isCompleted)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                notesList.get(selectedNoteIndex).setContent(noteContent);
                                notesList.get(selectedNoteIndex).setDate(noteDate);
                                notesAdapter.notifyDataSetChanged();
                                editTextNote.setText("");
                                textViewDate.setText("Fecha: Ninguna");
                                selectedNoteIndex = -1;
                                Toast.makeText(getActivity(), "Nota actualizada", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("NotesFragment", "Error al actualizar nota: " + task.getException());
                            }
                        });
            }
        } else {
            Toast.makeText(getActivity(), "La nota no puede estar vacía", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNotes() {
        notesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notesList.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    String note = document.getString("note");
                    String date = document.getString("date");
                    Boolean isCompleted = document.getBoolean("isCompleted");

                    if (note != null && date != null && isCompleted != null) {
                        notesList.add(new Note(document.getId(), note, date, isCompleted));
                    }
                }
                notesAdapter.notifyDataSetChanged();
                Log.d("NotesFragment", "Notas cargadas: " + notesList.size());
            } else {
                Log.e("NotesFragment", "Error al cargar notas: " + task.getException());
                Toast.makeText(getActivity(), "Error al cargar notas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteNote(int position) {
        if (position < 0 || position >= notesList.size()) {
            Log.e("NotesFragment", "Posición no válida para eliminar nota");
            return;
        }
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
        if (position < 0 || position >= notesList.size()) {
            Log.e("NotesFragment", "Posición no válida para editar nota");
            return;
        }
        selectedNoteIndex = position;
        editTextNote.setText(notesList.get(position).getContent());
        textViewDate.setText("Fecha: " + notesList.get(position).getDate());
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
            if (position < 0 || position >= notesList.size()) {
                return convertView;
            }

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.note_item, parent, false);
            }

            TextView textNote = convertView.findViewById(R.id.text_note);
            TextView textDate = convertView.findViewById(R.id.text_date);
            CheckBox checkBoxCompleted = convertView.findViewById(R.id.checkbox_completed);

            Note note = notesList.get(position);
            textNote.setText(note.getContent());
            textDate.setText(note.getDate());
            checkBoxCompleted.setChecked(note.isCompleted());

            checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                note.setCompleted(isChecked);
                String documentId = note.getId();
                notesCollection.document(documentId).update("isCompleted", isChecked);
            });

            convertView.findViewById(R.id.button_edit).setOnClickListener(v -> editNote(position));
            convertView.findViewById(R.id.button_delete).setOnClickListener(v -> deleteNote(position));

            return convertView;
        }
    }

    private static class Note {
        private String id;
        private String content;
        private String date;
        private boolean isCompleted;

        public Note(String id, String content, String date, boolean isCompleted) {
            this.id = id;
            this.content = content;
            this.date = date;
            this.isCompleted = isCompleted;
        }

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public String getDate() {
            return date;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setCompleted(boolean completed) {
            isCompleted = completed;
        }

        @Override
        public String toString() {
            return content + " - " + date + (isCompleted ? " (Completada)" : " (No completada)");
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(requireActivity(), (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            textViewDate.setText("Fecha: " + selectedDate);
        }, year, month, day).show();
    }
}
