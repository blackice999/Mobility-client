package com.project.mobility.repository.onboarding;

import com.project.mobility.model.onboarding.category.Category;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

public class OnboardingCategoryRepoImpl implements OnboardingCategoryRepo {

    @Inject
    public OnboardingCategoryRepoImpl() {

    }

    @Override
    public List<Category> getCategories() {
        return createDummyItems();
    }

    private List<Category> createDummyItems() {
        List<String> images = getImages();
        List<Category> dummyItems = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            dummyItems.add(new Category(i, "Name " + i, getRandomElement(images)));
        }

        return dummyItems;
    }

    @NotNull
    private List<String> getImages() {
        List<String> images = new ArrayList<>();
        images.add("ic_battery");
        images.add("ic_charger");
        images.add("ic_headset");
        images.add("ic_phone");
        return images;
    }

    public String getRandomElement(List<String> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
