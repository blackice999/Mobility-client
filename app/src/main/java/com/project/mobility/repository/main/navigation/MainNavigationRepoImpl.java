package com.project.mobility.repository.main.navigation;

import com.project.mobility.app.MobilityApplication;
import com.project.mobility.storage.persistence.room.AppDatabase;
import com.project.mobility.storage.persistence.room.dao.CartDao;

import javax.inject.Inject;

import io.reactivex.Maybe;

public class MainNavigationRepoImpl implements MainNavigationRepo {

    private CartDao cartDao;

    @Inject
    public MainNavigationRepoImpl() {
        AppDatabase appDatabase = MobilityApplication.getInstance().getAppDatabase();
        cartDao = appDatabase.cartDao();
    }

    @Override
    public Maybe<Integer> getCartContentCount() {
        return cartDao.getCartCount();
    }
}
