package com.project.mobility.repository.main.home;

import com.project.mobility.model.onboarding.category.Category;

import java.util.List;

public interface HomeRepo {
    List<Category> getCategories();
}
