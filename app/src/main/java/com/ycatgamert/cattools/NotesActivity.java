package com.ycatgamert.cattools;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ycatgamert.cattools.R;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private ArrayList<String> notesList;
    private ArrayAdapter<String> adapter;
    private EditText noteInput;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        noteInput = findViewById(R.id.noteInput);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        ListView notesListView = findViewById(R.id.notesListView);

        dbHelper = new DatabaseHelper(this);
        notesList = dbHelper.getAllNotes();

        adapter = new ArrayAdapter<>(this, R.layout.list_item_note, R.id.noteText, notesList);
        notesListView.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = noteInput.getText().toString().trim();
                if (!text.isEmpty()) {
                    boolean inserted = dbHelper.addNote(text);
                    if (inserted) {
                        notesList.add(text);
                        adapter.notifyDataSetChanged();
                        noteInput.setText("");
                    } else {
                        Toast.makeText(NotesActivity.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        notesListView.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedNote = notesList.get(position);
            dbHelper.deleteNote(selectedNote);
            notesList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(NotesActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}

