package com.project.mobility.model.main.home;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.onboarding.category.Category;
import com.project.mobility.repository.main.home.HomeRepo;

import java.util.List;

import javax.inject.Inject;

public class HomeModel {
    @Inject HomeRepo mainCategoryRepo;

    @Inject
    public HomeModel() {
        Injection.inject(this);
    }

    public List<Category> getCategories() {
        return mainCategoryRepo.getCategories();
    }
}
