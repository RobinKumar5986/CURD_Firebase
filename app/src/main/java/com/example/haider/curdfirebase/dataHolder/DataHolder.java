package com.example.haider.curdfirebase.dataHolder;

public class DataHolder {
    String name;
    String email;
    String course;
    String profile;
    public DataHolder(String name, String email, String course,String profile ) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.profile=profile;
    }



    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCourse() {
        return course;
    }
    public String getProfile() {
        return profile;
    }

}
