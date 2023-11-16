package com.example.project.organization;

public class OrgDataModel {

    private String name;
    private String location;
    private String mob;
    private String mission;
    private String email;
    private String profileImageUrl;

    public OrgDataModel() {
        // Default constructor required for Firebase
    }

    public OrgDataModel(String location, String email,String mission,String mob,String name,String profileImageUrl) {
        this.location= location;
        this.email = email;
        this.mission = mission;
        this.mob = mob;
        this.name = name;
        this.profileImageUrl=profileImageUrl;






    }





    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getMob() {
        return mob;
    }

    public String getMission() {
        return mission;
    }

    public String getEmail() {
        return email;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
