package com.example.notehub.data.datasources.remote_data_source;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.notehub.data.datasources.local_data_source.NotesDatabase;
import com.example.notehub.domain.NoteModel;
import com.example.notehub.domain.NotesRepository;
import com.example.notehub.presentation.listeners.OnNotesSyncCallBack;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreSyncService {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private NotesRepository notesRepository;

    public void syncNotes(List<NoteModel> localNotes, Context context, OnNotesSyncCallBack callBack) {
        Log.e("hola", auth.getCurrentUser().getUid());
        notesRepository = new NotesRepository(NotesDatabase.getInstance(context).notesDao());

        firestore.collection("users").document(auth.getCurrentUser().getUid()).collection("notes").get().addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("FirestoreSyncService", "Error syncing notes: " + e.getMessage());
                    callBack.onFailed(e.getMessage());
                })
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<NoteModel> firestoreNotes = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        NoteModel note = document.toObject(NoteModel.class);
                        firestoreNotes.add(note);
                    }

                    // Compare local notes with Firestore notes
                    for (NoteModel localNote : localNotes) {
                        boolean existsInFirestore = firestoreNotes.stream()
                                .anyMatch(note -> note.getId() == (localNote.getId()));

                        if (!existsInFirestore) {
                            // Note does not exist in Firestore, upload it
                            firestore.collection("users").document(auth.getCurrentUser().getUid()).collection("notes").add(localNote);
                            firestoreNotes.add(localNote);
                        }
                    }
                    notesRepository.notesDao.clearAllNotes();
                    // Compare Firestore notes with local notes
                    for (NoteModel firestoreNote : firestoreNotes) {
                        notesRepository.insertNote(firestoreNote, new OnNotesSyncCallBack() {
                            @Override
                            public void onSucess(String msg) {
                                Log.e("hola", msg);
                            }

                            @Override
                            public void onFailed(String msg) {
                                Log.e("holaa", msg);
                            }
                        });
                    }
                    callBack.onSucess("Notes synced successfully");
                })
        ;

    }


    public void updateNoteInFirestore(NoteModel note, OnNotesSyncCallBack callBack) {
        firestore.collection("users").document(auth.getCurrentUser().getUid()).collection("notes").whereEqualTo("id", note.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                queryDocumentSnapshots.getDocuments().get(0).getReference().set(note);
                callBack.onSucess("Note updated successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailed(e.getMessage());
            }
        });

    }

    public void deleteNoteFromFirestore(NoteModel note, OnNotesSyncCallBack callBack) {
        firestore.collection("users").document(auth.getCurrentUser().getUid()).collection("notes").whereEqualTo("id", note.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                queryDocumentSnapshots.getDocuments().get(0).getReference().delete();
                callBack.onSucess("Note deleted successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailed(e.getMessage());

            }
        });

    }

    public void addNoteToFirestore(NoteModel note, OnNotesSyncCallBack callBack) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Log.e("fromAddNoteToFirestore", String.valueOf(note.getId()));
        firestore.collection("users").document(auth.getCurrentUser().getUid()).collection("notes").add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                callBack.onSucess("Note added successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailed(e.getMessage());

            }
        });
    }


}

