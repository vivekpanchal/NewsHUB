package com.vivekpanchal.newshub.models;

public class UserInterestModel {

    private String name;
    private int imageResourceId;

    public UserInterestModel(String name, int id) {
        this.name = name;
        this.imageResourceId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
