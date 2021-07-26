package com.example.mvvvmexample;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Notes note);
    @Update
    void update(Notes note);
    @Delete
    void delete(Notes note);
    @Query("delete from notes_table")
    void deleteAllNotes();

    @Query("select * from notes_table order by priority asc")
    LiveData<List<Notes>> getAllNotes();

}
