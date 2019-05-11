package com.project.mobility.repository.onboarding;

import com.project.mobility.model.onboarding.category.Category;
import com.project.mobility.util.server.ServerUtil;

import java.util.List;

import javax.inject.Inject;

public class OnboardingCategoryRepoImpl implements OnboardingCategoryRepo {

    @Inject
    public OnboardingCategoryRepoImpl() {

    }

    @Override
    public List<Category> getCategories() {
        return ServerUtil.createDummyCategoryList();
    }
}
