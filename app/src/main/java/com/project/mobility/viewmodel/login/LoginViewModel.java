package com.project.mobility.viewmodel.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.login.LoginModel;
import com.project.mobility.model.login.provider.AuthProvider;

import javax.inject.Inject;

import toothpick.Toothpick;

public class LoginViewModel extends ViewModel {

    @Inject LoginModel loginModel;

    private MutableLiveData<Boolean> userAuthenticateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> userLogoutMutableLiveData = new MutableLiveData<>();
    private AuthProvider authProvider;

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public LoginViewModel() {
        Injection.inject(this);
    }

    public LiveData<Boolean> authenticate() {
        userAuthenticateMutableLiveData.postValue(loginModel.login(authProvider));
        return userAuthenticateMutableLiveData;
    }

    public LiveData<Boolean> logout() {
        userLogoutMutableLiveData.postValue(loginModel.logout(authProvider));
        return userLogoutMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Toothpick.closeScope(this);
    }
}
