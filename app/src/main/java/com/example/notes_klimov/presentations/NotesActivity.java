package com.example.notes_klimov.presentations;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notes_klimov.R;
import com.example.notes_klimov.datas.DbContext;
import com.example.notes_klimov.datas.NotesContext;
import com.example.notes_klimov.datas.RepoNotes;
import com.example.notes_klimov.domains.models.Note;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class NotesActivity extends AppCompatActivity {
    public static GridLayout itemsParent;
    View btnAddNotes;
    EditText etSearch;
    DbContext dbContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);

        btnAddNotes = findViewById(R.id.btn_add_notes);
        itemsParent = findViewById(R.id.gl_notes);
        etSearch = findViewById(R.id.et_search);

        btnAddNotes.setOnClickListener(v -> {
            Intent intentActivityNote = new Intent(this, NoteActivity.class);
            startActivity(intentActivityNote);
        });

        etSearch.setOnKeyListener(SearchListner);

        dbContext = new DbContext(this);
        LoadNotes(NotesContext.AllNotes());

        LoadNotes(RepoNotes.Notes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        LoadNotes(NotesContext.AllNotes());
    }

    public void LoadNotes(ArrayList<Note> notes){
        itemsParent.removeAllViews();

        for (int i = 0; i < notes.size(); i++) {
            View item_notes = LayoutInflater.from(this).inflate(R.layout.item_note, itemsParent, false);

            TextView tvTitle = item_notes.findViewById(R.id.tv_title);
            TextView tvText = item_notes.findViewById(R.id.tv_text);
            TextView tvDate = item_notes.findViewById(R.id.tv_date);
            LinearLayout linLay = item_notes.findViewById(R.id.linearLayoutItem);

            tvTitle.setText(notes.get(i).title);
            tvText.setText(notes.get(i).text);
            tvDate.setText(notes.get(i).date);
            linLay.setBackgroundColor(notes.get(i).color);

            int Position = i;

            item_notes.setOnClickListener(v -> {
                Intent intentActivityNote = new Intent(this, NoteActivity.class);
                intentActivityNote.putExtra("position", Position);
                startActivity(intentActivityNote);
            });

            itemsParent.addView(item_notes);
        }
    }

    View.OnKeyListener SearchListner = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            String Search = etSearch.getText().toString();

            ArrayList<Note> FindNotes = NotesContext.AllNotes().stream().filter(
                    item -> item.text.contains(Search)
            ).collect(Collectors.toCollection(ArrayList::new));

            LoadNotes(FindNotes);

            return false;
        }
    };
}