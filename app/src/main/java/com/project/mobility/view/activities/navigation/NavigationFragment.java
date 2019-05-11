package com.project.mobility.view.activities.navigation;

public class NavigationFragment {
    private int id;
    private String tag;

    public NavigationFragment(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }
}
