package com.example.myapplication.Listeners;

import androidx.cardview.widget.CardView;
import com.example.myapplication.Models.Notes;

import java.util.Calendar;

public interface NotesClickListener {
    void onClick(Notes notes);

    void onLongClick(Notes notes, CardView cardView);
}
