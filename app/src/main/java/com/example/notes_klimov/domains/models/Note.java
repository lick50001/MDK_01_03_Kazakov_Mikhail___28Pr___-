package com.example.notes_klimov.domains.models;

import android.view.View;
import android.widget.LinearLayout;

import com.example.notes_klimov.R;

public class Note {
    public int id;
    public String title;
    public String text;
    public String date;

    public int color;

    public static int[] colorChange = {
            0xFF2071F9,
            0xFF6BF2C1,
            0xFFD7F26B,
            0xFFF79525,
            0xFFF72525,
            0xFF25D1F7
    };

    public static void ChangeColor(int Color, LinearLayout linearLayout){
        linearLayout.setBackgroundColor(Color);
    }


    public int CurrentColor(boolean ifNow, int currentColor){
        int index = -1;
        for (int i = 0; i < colorChange.length; i++) {
            if (colorChange[i] == currentColor) {
                index = i;
                break;
            }
        }

        if (index == -1) index = 0;

        if (ifNow) {
            return colorChange[index];
        } else {
            return colorChange[(index + 1) % colorChange.length];
        }
    }
}
