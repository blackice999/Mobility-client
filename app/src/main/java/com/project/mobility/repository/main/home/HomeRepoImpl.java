package com.project.mobility.repository.main.home;

import com.project.mobility.model.onboarding.category.Category;
import com.project.mobility.util.server.ServerUtil;

import java.util.List;

import javax.inject.Inject;

public class HomeRepoImpl implements HomeRepo {
    @Inject
    public HomeRepoImpl() {

    }

    @Override
    public List<Category> getCategories() {
        return ServerUtil.createDummyCategoryList();
    }
}
