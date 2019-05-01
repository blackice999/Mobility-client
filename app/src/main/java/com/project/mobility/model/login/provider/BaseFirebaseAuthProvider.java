package com.project.mobility.model.login.provider;

import com.google.firebase.auth.FirebaseUser;
import com.project.mobility.model.user.User;

public abstract class BaseFirebaseAuthProvider extends AuthProvider{
    public User convertFirebaseUserToUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setToken(getToken());
        user.setDisplayName(firebaseUser.getDisplayName());
        user.setEmail(firebaseUser.getEmail());
        user.setCouldLogIn(true);
        user.setProviderName(getProviderName());
        return user;
    }
}
