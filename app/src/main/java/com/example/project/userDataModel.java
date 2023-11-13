package com.example.project;

public class userDataModel {
    private String name;
    private String age;
    private String gender;
    private String mobile;
    private String skills;
    private String email;


    // Add getters and setters

    public userDataModel() {
        // Default constructor required for Firebase
    }

    public userDataModel(String age, String email,String gender,String mobile,String name,String skills) {
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.name = name;

   ;

        this.skills = skills;


    }
    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }
    public String getGender() {
        return gender;
    }
    public String getMobile() {
        return mobile;
    }
    public String getSkills() {
        return skills;
    }
    public String getEmail() {
        return email;
    }
}
