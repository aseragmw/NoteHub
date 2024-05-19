package com.example.notehub.presentation.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.notehub.data.datasources.remote_data_source.FirestoreSyncService;
import com.example.notehub.domain.NoteModel;
import com.example.notehub.domain.NotesRepository;
import com.example.notehub.presentation.listeners.OnNotesSyncCallBack;

import java.util.List;

public class NotesViewModel extends ViewModel {
    NotesRepository notesRepository;
    FirestoreSyncService firestoreSyncService;
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
        firestoreSyncService = new FirestoreSyncService();

    }
    public void insertNote(NoteModel note,OnNotesSyncCallBack callBack) {
        notesRepository.insertNote(note,callBack);
    }
    public void updateNote(NoteModel note,OnNotesSyncCallBack callBack) {
        notesRepository.updateNote(note,callBack);
    }
    public void deleteNote(NoteModel note,OnNotesSyncCallBack callBack) {
        notesRepository.deleteNote(note,callBack);

    }
    public LiveData<List<NoteModel>> getAllNotes() {
    return allNotes;
    }

    public void syncNotes(List<NoteModel> notes, Context context, OnNotesSyncCallBack callBack){
        firestoreSyncService.syncNotes( notes, context,callBack);
    }
}
