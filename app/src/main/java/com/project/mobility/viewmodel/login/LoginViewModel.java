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

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public LoginViewModel() {
        Injection.inject(this);
    }

    public LiveData<Boolean> getUserAuthenticate() {
        return userAuthenticateMutableLiveData;
    }

    public LiveData<Boolean> getUserLogout() {
        return userLogoutMutableLiveData;
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
                .subscribe(loggedOut -> {
                    Timber.d("User logged out %s", loggedOut);
                    userLogoutMutableLiveData.setValue(true);
                }, throwable -> {
                    Timber.d("User could not log out");
                    throwable.printStackTrace();
                    userAuthenticateMutableLiveData.setValue(false);
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
