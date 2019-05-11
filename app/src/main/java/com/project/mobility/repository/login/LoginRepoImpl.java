package com.project.mobility.repository.login;

import com.project.mobility.app.MobilityApplication;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.login.provider.AuthProvider;
import com.project.mobility.model.user.User;
import com.project.mobility.storage.persistence.room.AppDatabase;
import com.project.mobility.storage.persistence.room.dao.UserDao;
import com.project.mobility.storage.persistence.room.entities.UserEntity;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.schedulers.Schedulers;

public class LoginRepoImpl implements LoginRepo {

    private UserDao userDao;

    @Inject
    public LoginRepoImpl() {
        Injection.inject(this);
        AppDatabase appDatabase = MobilityApplication.getInstance().getAppDatabase();
        userDao = appDatabase.userDao();
    }

    @Override
    public boolean login(AuthProvider authProvider) {
        return false;
    }

    @Override
    public Completable register(AuthProvider authProvider) {
        if (authProvider != null) {
            return authProvider.authenticate()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .flatMap(this::converUserToUserEntity)
                    .flatMapCompletable(userEntity -> userDao.insert(userEntity));
        }

        return Completable.error(new Throwable("Auth provider not set"));
    }

    private MaybeSource<UserEntity> converUserToUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.displayName = user.getDisplayName();
        userEntity.emailAddress = user.getEmail();
        userEntity.firebaseToken = user.getToken();
        userEntity.phoneNumber = "afasfa";

        return Maybe.just(userEntity);
    }

    @Override
    public Completable logout(AuthProvider authProvider) {
        return authProvider.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMapCompletable(loggedOut -> loggedOut ? userDao.deleteAll() : Completable.error(new Throwable("Could not log out")));
    }

    @Override
    public Maybe<Integer> getLoggedInUserCount() {
        return userDao.getLoggedInUserCount();
    }

    private void addUser(String firebaseId, String email, String displayName) {

    }
}
