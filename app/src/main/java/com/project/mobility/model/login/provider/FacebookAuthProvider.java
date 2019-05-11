package com.project.mobility.model.login.provider;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.user.User;
import com.project.mobility.storage.Preferences;

import javax.inject.Inject;

import durdinapps.rxfirebase2.RxFirebaseAuth;
import io.reactivex.Maybe;
import io.reactivex.Single;
import timber.log.Timber;

public class FacebookAuthProvider extends BaseFirebaseAuthProvider {
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
    public Maybe<User> authenticate() {
        return RxFirebaseAuth.signInWithCredential(FirebaseAuth.getInstance(), com.google.firebase.auth.FacebookAuthProvider.getCredential(getToken()))
                .flatMap(authResult -> {
                    Timber.d("signInWithCredential:success");
                    return Maybe.just(convertFirebaseUserToUser(authResult.getUser()));
                });
    }

    @Override
    public Single<Boolean> logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

        return RxFirebaseAuth.observeAuthState(FirebaseAuth.getInstance())
                .flatMapSingle(firebaseAuth -> firebaseAuth.getCurrentUser() == null ? Single.just(true) : Single.just(false))
                .first(false);
    }
}
