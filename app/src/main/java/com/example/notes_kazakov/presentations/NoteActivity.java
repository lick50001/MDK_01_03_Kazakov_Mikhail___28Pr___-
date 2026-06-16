package com.example.notes_kazakov.presentations;

import static com.example.notes_kazakov.domains.models.Note.colorChange;
import static com.example.notes_kazakov.presentations.NotesActivity.itemsParent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notes_kazakov.R;
import com.example.notes_kazakov.datas.NotesContext;
import com.example.notes_kazakov.datas.RepoNotes;
import com.example.notes_kazakov.domains.models.Note;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {
    private static final String PREFS_FILE = "Note";
    private static final String PREF_TITLE = "Title";
    private static final String PREF_TEXT = "Text";
    Note note;
    SharedPreferences settings;
    EditText etTitle, etText;
    TextView tvDate;
    View btnSelectColor, btnBack, btnTrash, btnSave, btnImport, MainColor, btnFavorite;

    public View butSelColor;

    public int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);
        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);


        Date DateNow = new Date();
        SimpleDateFormat FormatForDateNow = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");

        btnSave = findViewById(R.id.btn_save_note);
        btnImport = findViewById(R.id.btn_import_note);
        btnSelectColor = findViewById(R.id.btn_select_color);
        btnBack = findViewById(R.id.btn_back);
        btnTrash = findViewById(R.id.btn_trash);
        etTitle = findViewById(R.id.et_title);
        etText = findViewById(R.id.et_text);
        tvDate = findViewById(R.id.tv_date);
        MainColor = findViewById(R.id.main);
        btnFavorite = findViewById(R.id.btn_favorite_note);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null){
            int Position = arguments.getInt("position");

            ArrayList<Note> allNotes = NotesContext.AllNotes();
            note = allNotes.get(Position);

            etTitle.setText(note.title);
            etText.setText(note.text);
            MainColor.setBackgroundColor(note.CurrentColor(true, note.color));
        } else {
            btnTrash.setVisibility(View.GONE);
        }

        tvDate.setText("Отредактировано: " + FormatForDateNow.format(DateNow));

        btnFavorite.setOnClickListener(v -> {
            if (note != null) {
                note.isFavorite = !note.isFavorite;
                String messge = note.isFavorite ? "Добавлено в избранное" : "Удалено из избранного";
                Toast.makeText(this, messge, Toast.LENGTH_SHORT).show();
            }
        });

        btnSelectColor.setOnClickListener(v -> {
            if (arguments != null && note == null) {
                int Position = arguments.getInt("position");
                note = RepoNotes.Notes.get(Position);
            }
            if (note == null) {
                note = new Note();
                note.color = Note.colorChange[0];
            }

            note.color = note.CurrentColor(false, note.color);

            int displayColor = note.CurrentColor(true, note.color);
            btnSelectColor.setBackgroundColor(displayColor);
            MainColor.setBackgroundColor(displayColor);
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
                }

                note.title = Title;
                note.text = Text;
                note.date = FormatForDateNow.format(DateNow);

                boolean isUpdate = note.id != 0;
                NotesContext.Save(note, isUpdate);
            }
            finish();
        });

        btnTrash.setOnClickListener(v -> {
            NotesContext.Delete(note);
            finish();
            Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String text = etText.getText().toString();
            if (title.isEmpty()){
                Toast.makeText(this, "Нет названия заметки", Toast.LENGTH_SHORT).show();
            } else if (text.isEmpty()) {
                Toast.makeText(this, "Нет текста заметки", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putString(PREF_TITLE, title);
                prefEditor.putString(PREF_TEXT, text);
                prefEditor.apply();
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            }
        });

        btnImport.setOnClickListener(v -> {
            String title = settings.getString(PREF_TITLE, "");
            String text = settings.getString(PREF_TEXT, "");
            etTitle.setText(title);
            etText.setText(text);
            Toast.makeText(this, "Импортировано", Toast.LENGTH_SHORT).show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}