package com.project.mobility.repository.main.navigation;

import io.reactivex.Maybe;

public interface MainNavigationRepo {
    Maybe<Integer> getCartContentCount();
}
