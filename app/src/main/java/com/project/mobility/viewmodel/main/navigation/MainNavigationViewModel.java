package com.project.mobility.viewmodel.main.navigation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.main.navigation.MainNavigationModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainNavigationViewModel extends ViewModel {

    @Inject MainNavigationModel mainNavigationModel;
    @Inject CompositeDisposable compositeDisposable;

    private MutableLiveData<Integer> cartContentCountMutableLiveData = new MutableLiveData<>();

    public MainNavigationViewModel() {
        Injection.inject(this);
        getCartContentCount();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }

        Injection.closeScope(this);
    }

    public LiveData<Integer> getCartContentCountData() {
        return cartContentCountMutableLiveData;
    }

    public void getCartContentCount() {
        compositeDisposable.add(mainNavigationModel.getCartContentCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    Timber.d("Got cart count %s", count);
                    cartContentCountMutableLiveData.setValue(count);
                }, throwable -> {
                    Timber.d("Failed to get cart count");
                    throwable.printStackTrace();
                    cartContentCountMutableLiveData.setValue(0);
                })
        );
    }
}
