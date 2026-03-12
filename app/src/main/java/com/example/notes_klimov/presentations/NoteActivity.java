package com.example.notes_klimov.presentations;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notes_klimov.R;
import com.example.notes_klimov.datas.RepoNotes;
import com.example.notes_klimov.domains.models.Note;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {
    Note note;

    EditText etTitle, etText;
    TextView tvDate;
    View btnSelectColor, btnBack, btnTrash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);

        Date DateNow = new Date();
        SimpleDateFormat FormatForDateNow = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");

        btnSelectColor = findViewById(R.id.btn_select_color);
        btnBack = findViewById(R.id.btn_back);
        btnTrash = findViewById(R.id.btn_trash);
        etTitle = findViewById(R.id.et_title);
        etText = findViewById(R.id.et_text);
        tvDate = findViewById(R.id.tv_date);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null){
            int Position = arguments.getInt("position");

            note = RepoNotes.Notes.get(Position);

            etTitle.setText(note.title);
            etText.setText(note.text);
        } else{
            btnTrash.setVisibility(View.GONE);
        }

        tvDate.setText("Отредактировано: " + FormatForDateNow.format(DateNow));

        btnSelectColor.setOnClickListener(v -> {
            Toast.makeText(this,"Выбор цвета недоступен", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> {
            String Title = etTitle.getText().toString();
            String Text = etText.getText().toString();

            if (Text
                    .replace(" ", "")
                    .replace("\r", "")
                    .replace("\n", "")
                    .isEmpty()) {
                Toast.makeText(this, "Нечего сохранять", Toast.LENGTH_SHORT).show();
            } else{
                if (note == null){
                    note = new Note();
                    RepoNotes.Notes.add(note);
                }

                note.title = Title;
                note.text = Text;
                note.date = FormatForDateNow.format(DateNow);
            }
            finish();
        });

        btnTrash.setOnClickListener(v -> {
            RepoNotes.Notes.add(note);
            finish();
            Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}