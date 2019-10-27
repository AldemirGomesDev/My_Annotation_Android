package com.android.myannotations;

public class UserModel {

    private int id;
    private String name;
    private String date;
    private String annotation;

    public UserModel(int id, String name, String date, String annotation) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.annotation = annotation;
    }

    public UserModel (){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}
