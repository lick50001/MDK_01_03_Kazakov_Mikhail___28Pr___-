package com.example.notes_kazakov.presentations;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.notes_kazakov.R;
import com.example.notes_kazakov.datas.NotesContext;
import com.example.notes_kazakov.domains.models.Note;

import java.util.ArrayList;

public class NotesWidget extends AppWidgetProvider {
    String ACTION = "com.example.notes_kazakov.NEXT_NOTE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, -1
            );

            if (appWidgetId != -1) {
                ArrayList<Note> notes = NotesContext.AllNotes();
                if (!notes.isEmpty()) {
                    int currenIndex = getCurrentIndex(context, appWidgetId);
                    int nextIndex = (currenIndex + 1) % notes.size();

                    saveCurrentIndex(context, appWidgetId, nextIndex);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    updateWidget(context, appWidgetManager, appWidgetId);
                }
            }
        }
    }

    public void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        ArrayList<Note> notes = NotesContext.AllNotes();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        if (notes.isEmpty()) {
            views.setTextViewText(R.id.tv, "Нет заметок\nДобавьте заметку в приложении");
        } else {
            int currentIndex = getCurrentIndex(context, appWidgetId);
            Note note = notes.get(currentIndex);
            views.setTextViewText(R.id.tv, note.text);
            views.setInt(R.id.tv, "setBackgroundColor", note.color);
        }
        Intent intent = new Intent(context, NotesWidget.class);
        intent.setAction(ACTION);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.tv, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public int getCurrentIndex(Context context, int appWidgetId) {
        return context
                .getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
                .getInt("WidgetIndex_" + appWidgetId, 0);
    }

    public void saveCurrentIndex(Context context, int appWidgetId, int index) {
        context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("WidgetIndex_" + appWidgetId, index)
                .apply();
    }
}
