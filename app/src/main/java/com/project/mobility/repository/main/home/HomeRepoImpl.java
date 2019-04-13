package com.project.mobility.repository.main.home;

import com.project.mobility.model.onboarding.category.Category;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HomeRepoImpl implements HomeRepo {
    @Inject
    public HomeRepoImpl() {

    }

    @Override
    public List<Category> getCategories() {
        return createDummyItems();
    }

    private List<Category> createDummyItems() {
        List<Category> dummyItems = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            dummyItems.add(new Category(i, "Name " + i, "Image " + i));
        }

        return dummyItems;
    }
}
