package com.project.mobility.viewmodel.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.login.LoginModel;
import com.project.mobility.model.login.provider.AuthProvider;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LoginViewModel extends ViewModel {

    @Inject LoginModel loginModel;
    @Inject CompositeDisposable compositeDisposable;

    private MutableLiveData<Boolean> userAuthenticateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> userLogoutMutableLiveData = new MutableLiveData<>();
    private AuthProvider authProvider;
    private MutableLiveData<Boolean> userLoggedInMutableLiveData = new MutableLiveData<>();

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public LoginViewModel() {
        Injection.inject(this);
        getUserLogIn();
    }

    public LiveData<Boolean> getUserAuthenticate() {
        return userAuthenticateMutableLiveData;
    }

    public LiveData<Boolean> getUserLogout() {
        return userLogoutMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserLoggedIn() {
        return userLoggedInMutableLiveData;
    }

    public void authenticate() {
        compositeDisposable.add(loginModel.login(authProvider)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Timber.d("Successfully authenticated");
                    userAuthenticateMutableLiveData.setValue(true);
                }, throwable -> {
                    Timber.d("Authentication failed");
                    throwable.printStackTrace();
                    userAuthenticateMutableLiveData.setValue(false);
                })
        );
    }

    public void logout() {
        compositeDisposable.add(loginModel.logout(authProvider)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Timber.d("User logged out");
                    userLogoutMutableLiveData.setValue(true);
                }, throwable -> {
                    Timber.d("User could not log out");
                    throwable.printStackTrace();
                    userLogoutMutableLiveData.setValue(false);
                })
        );
    }

    private void getUserLogIn() {
        userLoggedInMutableLiveData.setValue(false);
        compositeDisposable.add(loginModel.getLoggedInUserCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    Timber.d("Logged in user count %s", count);
                    userLoggedInMutableLiveData.setValue(count != 0);
                }, throwable -> {
                    Timber.d("No logged in user");
                    throwable.printStackTrace();
                    userLoggedInMutableLiveData.setValue(false);
                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }

        Injection.closeScope(this);
    }
}
