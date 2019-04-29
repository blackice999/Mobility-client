package com.project.mobility.viewmodel.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.main.home.HomeModel;
import com.project.mobility.model.onboarding.category.Category;

import java.util.List;

import javax.inject.Inject;

import toothpick.Toothpick;

public class HomeViewModel extends ViewModel {
    @Inject HomeModel homeModel;
    private MutableLiveData<List<Category>> homeLiveData = new MutableLiveData<>();

    public HomeViewModel() {
        Injection.inject(this);
        homeLiveData.postValue(homeModel.getCategories());
    }

    public LiveData<List<Category>> getCategories() {
        return homeLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Toothpick.closeScope(this);
    }
}
