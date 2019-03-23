package com.project.mobility.model.onboarding.category;

public class Category {

    private long id;
    private String name;
    private String image;

    public Category(long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
