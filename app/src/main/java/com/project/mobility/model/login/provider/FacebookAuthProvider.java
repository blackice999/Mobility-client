package com.project.mobility.model.login.provider;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.storage.Preferences;

import javax.inject.Inject;

import timber.log.Timber;

public class FacebookAuthProvider extends AuthProvider {
    public static final String AUTH_PROVIDER_NAME = "Facebook";

    @Inject Preferences preferences;

    @Inject
    public FacebookAuthProvider() {
        Injection.inject(this);
    }

    public String getProviderName() {
        return AUTH_PROVIDER_NAME;
    }

    @Override
    public void authenticate() {
        AuthCredential credential = com.google.firebase.auth.FacebookAuthProvider.getCredential(getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Timber.d("signInWithCredential:success");
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        preferences.setString(Preferences.KEY_AUTH_PROVIDER_EMAIL, firebaseUser.getEmail());
                        preferences.setString(Preferences.KEY_AUTH_PROVIDER_DISPLAY_NAME, firebaseUser.getDisplayName());
                        preferences.setString(Preferences.KEY_AUTH_PROVIDER_NAME, AUTH_PROVIDER_NAME);
                    } else {
                        Timber.w(task.getException(), "signInWithCredential:failure");
                    }
                });
    }

    @Override
    public boolean logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        return true;
    }
}
