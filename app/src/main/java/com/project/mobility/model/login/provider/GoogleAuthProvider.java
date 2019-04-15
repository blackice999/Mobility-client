package com.project.mobility.model.login.provider;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.mobility.storage.Preferences;

import javax.inject.Inject;

import timber.log.Timber;

public class GoogleAuthProvider extends AuthProvider {
    public static final String AUTH_PROVIDER_NAME = "Google";

    @Inject Preferences preferences;

    @Inject
    public GoogleAuthProvider() {
    }

    public String getProviderName() {
        return AUTH_PROVIDER_NAME;
    }

    @Override
    public void authenticate() {
        final AuthCredential credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(getToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("signInWithCredential:success");
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        preferences.setString(Preferences.KEY_AUTH_PROVIDER_EMAIL, firebaseUser.getEmail());
                        preferences.setString(Preferences.KEY_AUTH_PROVIDER_DISPLAY_NAME, firebaseUser.getDisplayName());
                        preferences.setString(Preferences.KEY_AUTH_PROVIDER_NAME, AUTH_PROVIDER_NAME);
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.w(task.getException(), "signInWithCredential:failure");
                    }
                });
    }

    @Override
    public boolean logout() {
        FirebaseAuth.getInstance().signOut();
        return true;
    }
}