package com.project.mobility.view.activities.navigation;

import java.util.List;

import androidx.fragment.app.Fragment;

public interface FragmentNavigationFactory {
    List<NavigationFragment> getList();

    String getTagById(int id);
}
