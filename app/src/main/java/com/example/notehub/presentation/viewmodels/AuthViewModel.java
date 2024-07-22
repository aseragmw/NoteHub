package com.example.notehub.presentation.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.notehub.data.datasources.local_data_source.NotesDatabase;
import com.example.notehub.domain.AuthRepository;
import com.example.notehub.presentation.listeners.AuthCallBack;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {

    private AuthRepository authRepository;
    private static AuthViewModel instance;

    public static AuthViewModel getInstance() {
        if (instance == null) {
            instance = new AuthViewModel();
        }
        return instance;
    }

    public AuthViewModel() {
        this.authRepository = AuthRepository.getInstance();
    }

    public void register(String username, String email,String password, AuthCallBack callBack) {
        authRepository.signUp(username, email,password, callBack);
    }

    public void login(String username, String password, AuthCallBack callBack) {
        authRepository.signIn(username, password,callBack);
    }

    public void logout(Context context) {
        authRepository.signOut();
        NotesDatabase.getInstance(context).notesDao().clearAllNotes();
    }

    public boolean isLoggedIn() {
        return authRepository.isUserSignedIn();
    }

    public FirebaseUser getCurrentUser() {
        return authRepository.getCurrentUserId();
    }








}
