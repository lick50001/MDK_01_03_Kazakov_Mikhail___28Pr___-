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
            0x2071F9,
            0x6bf2c1,
            0xd7f26b,
            0xf79525,
            0xf72525,
            0x25d1f7
    };

    public void ChangeColor(int Color, LinearLayout linearLayout){
        linearLayout.setBackgroundColor(Color);
    }


    public int CurrentColor(boolean ifNow, int id){
        for (int i = 0; i < colorChange.length; i++)
            if (colorChange[i] == id){
                id = i;
                break;
            }

        if(ifNow)
            return colorChange[id];
        else if(id == colorChange.length)
            return colorChange[0];
        else
            return colorChange[id + 1];
    }
}
