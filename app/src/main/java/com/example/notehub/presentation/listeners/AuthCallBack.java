package com.example.notehub.presentation.listeners;

import com.google.firebase.auth.FirebaseUser;

public interface AuthCallBack {
    void onSuccess(FirebaseUser user);
    void onFailure(Exception e);
}
