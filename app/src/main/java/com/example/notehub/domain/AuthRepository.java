package com.example.notehub.domain;

import com.example.notehub.data.datasources.remote_data_source.FirebaseAuth;
import com.example.notehub.presentation.listeners.AuthCallBack;

public class AuthRepository {
    private FirebaseAuth firebaseAuth;
    private static AuthRepository instance;
    private AuthRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public static AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    public void signUp(String fullname,String email, String password, AuthCallBack authCallBack) {
        firebaseAuth.registerWithEmailPassword(fullname,email, password, authCallBack);
    }

    public void signIn(String email, String password,AuthCallBack authCallBack) {
        firebaseAuth.signInWithEmailPassword(email, password, authCallBack);
    }

    public void signOut() {
        firebaseAuth.signOut();

    }

    public com.google.firebase.auth.FirebaseUser getCurrentUserId() {
        return firebaseAuth.getCurrentUser();
    }

    public boolean isUserSignedIn() {
        return firebaseAuth.isUserLoggedIn();
    }


}
