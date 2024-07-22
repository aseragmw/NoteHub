package com.example.notehub.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.notehub.data.datasources.local_data_source.NotesDao;
import com.example.notehub.data.datasources.remote_data_source.FirebaseAuth;
import com.example.notehub.data.datasources.remote_data_source.FirestoreSyncService;
import com.example.notehub.presentation.listeners.OnNotesSyncCallBack;

import java.util.List;

public class NotesRepository {
    private FirestoreSyncService firestoreSyncService;
    private FirebaseAuth firebaseAuth;
    public NotesDao notesDao;
    public NotesRepository(NotesDao notesDao) {
        this.notesDao = notesDao;
        firestoreSyncService = new FirestoreSyncService();
        firebaseAuth =  FirebaseAuth.getInstance();
    }

    public void insertNote(NoteModel note, OnNotesSyncCallBack callBack) {
        long newId = notesDao.insert(note);
        NoteModel newNote = notesDao.getNoteById(newId);
        Log.d("NotesRepository", "insertNote: "+newNote.getId());
        if(firebaseAuth.getCurrentUser() != null){
            firestoreSyncService.addNoteToFirestore(newNote, callBack);
        }
    }

    public void deleteNote(NoteModel note,OnNotesSyncCallBack callBack) {
        notesDao.delete(note);
        if(firebaseAuth.getCurrentUser() != null){
            firestoreSyncService.deleteNoteFromFirestore(note, callBack);
        }
    }

    public void updateNote(NoteModel note,OnNotesSyncCallBack callBack) {
        notesDao.update(note);
        if(firebaseAuth.getCurrentUser() != null){
            firestoreSyncService.updateNoteInFirestore(note, callBack);
        }
    }

    public LiveData<List<NoteModel>> getAllNotes() {
        return notesDao.getAll();
    }

}
