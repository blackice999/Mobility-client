package com.project.mobility.viewmodel.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.main.home.HomeModel;
import com.project.mobility.model.onboarding.category.Category;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {
    @Inject HomeModel homeModel;
    private MutableLiveData<List<Category>> categoriesLiveData = new MutableLiveData<>();

    public HomeViewModel() {
        Injection.inject(this);
        categoriesLiveData.postValue(homeModel.getCategories());
    }

    public LiveData<List<Category>> getCategories() {
        return categoriesLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Injection.closeScope(this);
    }
}
