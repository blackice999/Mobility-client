package com.project.mobility.model.onboarding;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.onboarding.category.Category;
import com.project.mobility.repository.onboarding.OnboardingCategoryRepo;

import java.util.List;

import javax.inject.Inject;

public class OnboardingCategoryModel {

    @Inject OnboardingCategoryRepo onboardingCategoryRepo;

    @Inject
    public OnboardingCategoryModel() {
        Injection.inject(this);
    }

    public List<Category> getCategories() {
        return onboardingCategoryRepo.getCategories();
    }
}
