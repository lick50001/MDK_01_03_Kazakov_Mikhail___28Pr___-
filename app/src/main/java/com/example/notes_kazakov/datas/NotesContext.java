package com.example.notes_kazakov.datas;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.notes_kazakov.domains.models.Note;

import java.util.ArrayList;

public class NotesContext {

    public static ArrayList<Note> AllNotes() {
        ArrayList<Note> allNotes = new ArrayList<>();
        Cursor cursor = DbContext.sqLiteDatabase.query("Notes",
                null, null, null,
                null, null, null);

        if (cursor.moveToFirst() == false) {
            return allNotes;
        }
        do {
            Note note = new Note();

            note.id = cursor.getInt(0);
            note.title = cursor.getString(1);
            note.text = cursor.getString(2);
            note.date = cursor.getString(3);
            note.color = cursor.getInt(4);
            note.isFavorite = cursor.getInt(5) == 1;

            allNotes.add(note);
        } while (cursor.moveToNext());

        cursor.close();
        return allNotes;
    }

    public static ArrayList<Note> FavoriteNotes() {
        ArrayList<Note> favoriteNotes = new ArrayList<>();
        Cursor cursor = DbContext.sqLiteDatabase.query("Notes",
                null, "isFavorite = 1", null,
                null, null, null);

        if (cursor.moveToFirst() == false) {
            return favoriteNotes;
        }
        do {
            Note note = new Note();

            note.id = cursor.getInt(0);
            note.title = cursor.getString(1);
            note.text = cursor.getString(2);
            note.date = cursor.getString(3);
            note.color = cursor.getInt(4);
            note.isFavorite = true;

            favoriteNotes.add(note);
        } while (cursor.moveToNext());

        cursor.close();
        return favoriteNotes;
    }

    public static void Save(Note note, boolean update) {
        ContentValues CV = new ContentValues();

        CV.put("Title", note.title);
        CV.put("Text", note.text);
        CV.put("Date", note.date);
        CV.put("Color", note.color);
        CV.put("IsFavorite", note.isFavorite ? 1 : 0);

        if (update == false) {
            DbContext.sqLiteDatabase.insert(
                    "Notes",
                    null,
                    CV);
        } else {
            DbContext.sqLiteDatabase.update(
                    "Notes",
                    CV,
                    "Id = ?",
                    new String[] {String.valueOf(note.id)});
        }
    }

    public static void Delete(Note note) {
        DbContext.sqLiteDatabase.delete(
                "Notes",
                "Id = ?",
                new String[] {String.valueOf(note.id)});
    }
}
