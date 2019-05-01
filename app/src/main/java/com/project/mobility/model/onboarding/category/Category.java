package com.project.mobility.model.onboarding.category;

public class Category {

    private long id;
    private String name;
    private String imageName;

    public Category(long id, String name, String imageName) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }
}
