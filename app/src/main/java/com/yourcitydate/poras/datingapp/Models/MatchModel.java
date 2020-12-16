package com.yourcitydate.poras.datingapp.Models;

public class MatchModel {
    private String name;
    private String image;
    private String UID;
    private String Age;


    public MatchModel(String name, String image, String UID, String Age) {
        this.name = name;
        this.image = image;
        this.UID = UID;
        this.Age = Age;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }
}
