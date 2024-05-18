package com.example.notehub.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.notehub.data.NotesDao;

import java.util.List;

public class NotesRepository {
    public NotesDao notesDao;
    public NotesRepository(NotesDao notesDao) {
        this.notesDao = notesDao;
    }

    public void insertNote(NoteModel note) {
        notesDao.insert(note);
    }

    public void deleteNote(NoteModel note) {
        notesDao.delete(note);
    }

    public void updateNote(NoteModel note) {
        notesDao.update(note);
    }

    public LiveData<List<NoteModel>> getAllNotes() {
        return notesDao.getAll();
    }

}
