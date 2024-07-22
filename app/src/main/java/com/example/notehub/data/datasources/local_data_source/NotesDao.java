package com.example.notehub.data.datasources.local_data_source;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.DeleteTable;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notehub.domain.NoteModel;

import java.util.List;

@Dao
public interface NotesDao  {

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    LiveData<List<NoteModel>> getAll();

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    NoteModel getNoteById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(NoteModel note);

    @Delete
    void delete(NoteModel note);

    @Update
    void update(NoteModel note);

    @Query("DELETE FROM notes")
    void clearAllNotes();
}
