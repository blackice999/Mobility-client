package com.project.mobility.viewmodel.onboarding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.onboarding.OnboardingCategoryModel;
import com.project.mobility.model.onboarding.category.Category;

import java.util.List;

import javax.inject.Inject;

public class OnboardingCategoryViewModel extends ViewModel {

    @Inject OnboardingCategoryModel onboardingCategoryModel;
    private MutableLiveData<List<Category>> onboardingCategoryLiveData = new MutableLiveData<>();

    public OnboardingCategoryViewModel() {
        Injection.inject(this);
        onboardingCategoryLiveData.postValue(onboardingCategoryModel.getCategories());
    }

    public LiveData<List<Category>> getCategories() {
        return onboardingCategoryLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Injection.closeScope(this);
    }
}
