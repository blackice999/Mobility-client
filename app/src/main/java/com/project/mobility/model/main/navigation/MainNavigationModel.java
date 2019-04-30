package com.project.mobility.model.main.navigation;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.repository.main.navigation.MainNavigationRepo;

import javax.inject.Inject;

import io.reactivex.Maybe;

public class MainNavigationModel {
    @Inject MainNavigationRepo mainNavigationRepo;

    @Inject
    public MainNavigationModel() {
        Injection.inject(this);
    }

    public Maybe<Integer> getCartContentCount() {
        return mainNavigationRepo.getCartContentCount();
    }
}
