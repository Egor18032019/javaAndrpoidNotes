package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Создаватель ячеек для заметок
 */
public class NotesTakerActivity extends AppCompatActivity {
    EditText edit_text_notes;
    EditText edit_text_title;
    ImageView image_save;
    Notes note;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);
        image_save = findViewById(R.id.image_save);
        edit_text_notes = findViewById(R.id.edit_text_notes);
        edit_text_title = findViewById(R.id.edit_text_title);
        try {
            note = (Notes) getIntent().getSerializableExtra("old note");
            edit_text_title.setText(note.getTitle());
            edit_text_notes.setText(note.getNotes());
            isOldNote = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        image_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edit_text_title.getText().toString();
                String description = edit_text_notes.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(NotesTakerActivity.this, "Please enter description", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                if (!isOldNote) note = new Notes();
                note.setTitle(title);
                note.setNotes(description);
                note.setDate(formatter.format(date));

                Intent intent = new Intent();
                intent.putExtra("notes", note);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}