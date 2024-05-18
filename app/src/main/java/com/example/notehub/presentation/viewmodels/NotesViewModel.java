package com.example.notehub.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.notehub.domain.NoteModel;
import com.example.notehub.domain.NotesRepository;

import java.util.List;

public class NotesViewModel extends ViewModel {
    NotesRepository notesRepository;
    public static LiveData<List<NoteModel>> allNotes;
    private static NotesViewModel instance;
    public static NotesViewModel getInstance(NotesRepository notesRepository) {
        if (instance == null) {
            instance = new NotesViewModel(notesRepository);
            allNotes = notesRepository.getAllNotes();
        }
        return instance;
    }
    private NotesViewModel(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;

    }
    public void insertNote(NoteModel note) {
        notesRepository.insertNote(note);
    }
    public void updateNote(NoteModel note) {
        notesRepository.updateNote(note);
    }
    public void deleteNote(NoteModel note) {
        notesRepository.deleteNote(note);

    }
    public LiveData<List<NoteModel>> getAllNotes() {
    return allNotes;
    }
}
