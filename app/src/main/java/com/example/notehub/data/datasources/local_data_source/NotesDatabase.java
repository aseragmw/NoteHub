package com.example.notehub.data.datasources.local_data_source;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notehub.domain.NoteModel;


@Database(entities = {NoteModel.class}, version = 3, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NotesDao notesDao();
    private static NotesDatabase INSTANCE = null;

    public static NotesDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NotesDatabase.class, "notes_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public NotesDatabase() {
    }
}
