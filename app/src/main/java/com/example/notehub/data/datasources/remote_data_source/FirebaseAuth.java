package com.example.notehub.data.datasources.remote_data_source;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.notehub.presentation.listeners.AuthCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class FirebaseAuth {
    private  com.google.firebase.auth.FirebaseAuth auth = com.google.firebase.auth.FirebaseAuth.getInstance();
    private static FirebaseAuth instance;
    private FirebaseAuth(){}
    public static FirebaseAuth getInstance(){
        if(instance==null){
            instance = new FirebaseAuth();
        }
        return instance;
    }

    public void signInWithEmailPassword(String email, String password, AuthCallBack callBack){
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                callBack.onSuccess(authResult.getUser());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailure(e);
            }
        });
    }

    public void registerWithEmailPassword(String fullName,String email,String password,AuthCallBack callBack){
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                authResult.getUser().updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(fullName).build()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callBack.onSuccess(authResult.getUser());

                    }
                }).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callBack.onFailure(e);
                            }
                        }
                );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailure(e);
            }
        });
    }

    public boolean isUserLoggedIn(){
        return auth.getCurrentUser()!=null;
    }

    public com.google.firebase.auth.FirebaseUser getCurrentUser(){
        return auth.getCurrentUser();
    }

    public void signOut(){
        auth.signOut();
    }
}
