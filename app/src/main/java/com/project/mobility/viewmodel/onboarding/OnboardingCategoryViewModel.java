package com.project.mobility.viewmodel.onboarding;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.onboarding.OnboardingCategoryModel;
import com.project.mobility.model.onboarding.category.Category;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OnboardingCategoryViewModel extends ViewModel {

    @Inject OnboardingCategoryModel onboardingCategoryModel;
    private MutableLiveData<List<Category>> onboardingCategoryLiveData = new MutableLiveData<>();

    public OnboardingCategoryViewModel() {
        Injection.inject(this);
    }

    public LiveData<List<Category>> getCategories() {
        onboardingCategoryLiveData.postValue(onboardingCategoryModel.getCategories());
        return onboardingCategoryLiveData;
    }
}