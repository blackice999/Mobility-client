package com.project.mobility.view.activities;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class OnboardingSectionAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public OnboardingSectionAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        fragmentList = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public Fragment getFragment(int position) {
        return fragmentList.get(position);
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }
}
