package com.example.myapplication.DataBase;

import android.icu.text.Replaceable;
import androidx.room.*;
import com.example.myapplication.Models.Notes;

import java.util.List;

@Dao
public interface MainDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notes notes);

    @Query("Select * From notes Order By id Desc")
    List<Notes> getAll();

    @Query("Update notes Set title =:title ,notes=:notes Where id=:id")
    void update(int id, String notes, String title);

    @Delete
    void delete(Notes notes);
}
