package com.project.mobility.repository.onboarding;

import com.project.mobility.model.onboarding.category.Category;

import java.util.List;

public interface OnboardingCategoryRepo {
    List<Category> getCategories();
}
