package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import androidx.activity.OnBackPressedDispatcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.myapplication.Adapter.NotesListAdapter;
import com.example.myapplication.DataBase.RoomDB;
import com.example.myapplication.Listeners.NotesClickListener;
import com.example.myapplication.Models.Notes;
import com.example.myapplication.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    FloatingActionButton plusButton;
    Button mainButton;
    SearchView search_view_home;
    RecyclerView recyclerView;
    Notes selectedNote;
    NotesListAdapter notesListAdapter;
    RoomDB database;
    List<Notes> notes = new ArrayList<>();
    public static final int PLUSBUTTONRESULTCODE = 101;
    public static final int MAINBUTTONRESULTCODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search_view_home = findViewById(R.id.open_search_view_home);
        recyclerView = findViewById(R.id.rec_home);
        plusButton = findViewById(R.id.plusButton);
        mainButton = findViewById(R.id.mainButton);

        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();
        updateRecycle(notes);

        plusButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            startActivityForResult(intent, PLUSBUTTONRESULTCODE);
        });
        mainButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
            System.out.println(" переход на вебвью");
            startActivityForResult(intent, 103);
        });

        search_view_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

    }

    private void filter(String newText) {
        List<Notes> filtereList = new ArrayList<>();
//todo или запрос делать в бд для обновление данных ?
        for (Notes note : notes) {
            if (note.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || note.getNotes().toLowerCase().contains(newText.toLowerCase())
            ) {
                filtereList.add(note);

            }
        }
        notesListAdapter.filterList(filtereList);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLUSBUTTONRESULTCODE) {

            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //todo придумать ошибку
                    System.out.println("Что пошло нет так");
                }
                Notes newNotes = (Notes) data.getSerializableExtra("notes");
                database.mainDAO().insert(newNotes);
                notes.clear();
                System.out.println("newNotes " + newNotes.getNotes().length());
                notes.addAll(database.mainDAO().getAll());
                //Уведомите всех зарегистрированных наблюдателей о том, что набор данных изменился.
                notesListAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == MAINBUTTONRESULTCODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //todo придумать ошибку
                    System.out.println("Что пошло нет так");
                }
                Notes newNotes = (Notes) data.getSerializableExtra("notes");
                database.mainDAO().update(newNotes.getID(), newNotes.getNotes(), newNotes.getTitle());
                System.out.println("newNotes " + newNotes.getNotes().length());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                //Уведомите всех зарегистрированных наблюдателей о том, что набор данных изменился.
                notesListAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * Внедряет notes_list в recyclerView
     * через notesListAdapter
     *
     * @param notes
     */
    private void updateRecycle(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {

        @Override
        public void onClick(Notes notes) {
            System.out.println("onClick NotesClickListener");
            //1 что закрываем, а 2 что открываем
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old note", notes);
            //Todo переделать
            startActivityForResult(intent, MAINBUTTONRESULTCODE);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            System.out.println("onLongClick NotesClickListener");
            selectedNote = new Notes();
            selectedNote = notes;
            showPopUp(cardView);

        }
    };

    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pin:
                if (selectedNote.isPinned()) {
                    database.mainDAO().pin(selectedNote.getID(), false);
                    Toast.makeText(MainActivity.this, "unpinned", Toast.LENGTH_SHORT).show();
                } else {
                    database.mainDAO().pin(selectedNote.getID(), true);
                    Toast.makeText(MainActivity.this, "pinned", Toast.LENGTH_SHORT).show();
                }
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                //Уведомите всех зарегистрированных наблюдателей о том, что набор данных изменился.
                notesListAdapter.notifyDataSetChanged();
                return true;
            case R.id.delete:
                database.mainDAO().delete(selectedNote);
                notes.remove(selectedNote);
                //Уведомите всех зарегистрированных наблюдателей о том, что набор данных изменился.
                Toast.makeText(MainActivity.this, "note remove", Toast.LENGTH_SHORT).show();
                notesListAdapter.notifyDataSetChanged();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("onBackPressed MainActivity");

    }
}