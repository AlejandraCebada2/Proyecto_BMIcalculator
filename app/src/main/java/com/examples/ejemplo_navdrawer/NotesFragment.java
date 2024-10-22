package com.examples.ejemplo_navdrawer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
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

        // Solicitar permisos de calendario
        requestCalendarPermissions();

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

                    // Agregar evento al calendario
                    addEventToCalendar(noteContent, noteDate);
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

    private void addEventToCalendar(String noteContent, String noteDate) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Permisos de calendario no concedidos", Toast.LENGTH_SHORT).show();
            return;
        }

        long calendarId = getCalendarId(); // Obtener el calendar_id
        ContentValues values = new ContentValues();
        values.put("calendar_id", calendarId);
        values.put("title", noteContent);
        values.put("description", "Nota: " + noteContent);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(noteDate);
            long startMillis = date.getTime();
            values.put("dtstart", startMillis);
            values.put("dtend", startMillis + 60 * 60 * 1000); // Duración de 1 hora
            values.put("eventTimezone", Calendar.getInstance().getTimeZone().getID());

            Uri uri = requireActivity().getContentResolver().insert(Uri.parse("content://com.android.calendar/events"), values);
            if (uri != null) {
                Toast.makeText(getActivity(), "Evento añadido al calendario", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error al añadir evento", Toast.LENGTH_SHORT).show();
        }
    }

    private long getCalendarId() {
        Cursor cursor = requireActivity().getContentResolver().query(
                Uri.parse("content://com.android.calendar/calendars"),
                new String[]{"_id"},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            long calendarId = cursor.getLong(0);
            cursor.close();
            return calendarId;
        }

        if (cursor != null) {
            cursor.close();
        }

        throw new IllegalArgumentException("No se encontró un calendario");
    }

    private void requestCalendarPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                boolean readGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writeGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (readGranted && writeGranted) {
                    Toast.makeText(getActivity(), "Permisos concedidos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Permisos denegados", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
