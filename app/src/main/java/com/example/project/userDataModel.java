package com.example.project;

public class userDataModel {
    private String name;
    private String age;
    private String gender;
    private String mob;
    private String skl;
    private String email;
  private String profileImageUrl;

    // Add getters and setters

    public userDataModel() {
        // Default constructor required for Firebase
    }

    public userDataModel(String age, String email,String gender,String mob,String name,String skl,String profileImageUrl) {
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.mob = mob;
        this.name = name;
        this.profileImageUrl=profileImageUrl;



        this.skl = skl;


    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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
    public String getMob() {
        return mob;
    }
    public String getSkl() {
        return skl;
    }
    public String getEmail() {
        return email;
    }
}
