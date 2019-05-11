package com.project.mobility.model.login.provider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.mobility.model.product.Product;
import com.project.mobility.model.user.User;
import com.project.mobility.storage.persistence.room.entities.ProductEntity;

import javax.inject.Inject;

import durdinapps.rxfirebase2.RxFirebaseAuth;
import io.reactivex.Maybe;
import io.reactivex.Single;
import timber.log.Timber;

public class GoogleAuthProvider extends BaseFirebaseAuthProvider {
    public static final String AUTH_PROVIDER_NAME = "Google";

    @Inject
    public GoogleAuthProvider() {
    }

    public String getProviderName() {
        return AUTH_PROVIDER_NAME;
    }

    @Override
    public Maybe<User> authenticate() {
        return RxFirebaseAuth.signInWithCredential(FirebaseAuth.getInstance(), com.google.firebase.auth.GoogleAuthProvider.getCredential(getToken(), null))
                .flatMap(authResult -> {
                    Timber.d("signInWithCredential:success");
                    return Maybe.just(convertFirebaseUserToUser(authResult.getUser()));
                });
    }

    @Override
    public Single<Boolean> logout() {
        FirebaseAuth.getInstance().signOut();

        return Single.fromObservable(RxFirebaseAuth.observeAuthState(FirebaseAuth.getInstance())
                .flatMapMaybe(firebaseAuth -> Maybe.just(firebaseAuth.getCurrentUser() != null)));
    }
}