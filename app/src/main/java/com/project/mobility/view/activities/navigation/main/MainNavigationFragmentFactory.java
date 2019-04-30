package com.project.mobility.view.activities.navigation.main;

import com.project.mobility.R;
import com.project.mobility.view.activities.navigation.FragmentNavigationFactory;
import com.project.mobility.view.activities.navigation.NavigationFragment;
import com.project.mobility.view.fragments.cart.CartFragment;
import com.project.mobility.view.fragments.FavoritesFragment;
import com.project.mobility.view.fragments.home.HomeFragment;
import com.project.mobility.view.fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainNavigationFragmentFactory implements FragmentNavigationFactory {
    private List<NavigationFragment> fragmentList;

    @Inject
    public MainNavigationFragmentFactory() {
        fragmentList = new ArrayList<>();
        setupList();
    }

    @Override
    public List<NavigationFragment> getList() {
        return fragmentList;
    }

    @Override
    public String getTagById(int id) {
        for (NavigationFragment navigationFragment : fragmentList) {
            if (navigationFragment.getId() == id) {
                return navigationFragment.getTag();
            }
        }

        return fragmentList.get(0).getTag();
    }

    private void setupList() {
        fragmentList.add(new NavigationFragment(R.id.action_home, HomeFragment.class.getName()));
        fragmentList.add(new NavigationFragment(R.id.action_cart, CartFragment.class.getName()));
        fragmentList.add(new NavigationFragment(R.id.action_settings, SettingsFragment.class.getName()));
        fragmentList.add(new NavigationFragment(R.id.action_favorites, FavoritesFragment.class.getName()));
    }
}
