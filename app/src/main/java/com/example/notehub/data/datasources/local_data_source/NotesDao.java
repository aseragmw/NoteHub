package com.example.notehub.data.datasources.local_data_source;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notehub.domain.NoteModel;

import java.util.List;

@Dao
public interface NotesDao  {

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    LiveData<List<NoteModel>> getAll();

    @Insert
    void insert(NoteModel note);

    @Delete
    void delete(NoteModel note);

    @Update
    void update(NoteModel note);
}
