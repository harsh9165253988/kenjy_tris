package com.example.project.organization;

public class OrgDataModel {

    private String orgName;
    private String location;
    private String contact;
    private String mission;
    private String mail;
    private String profileImageUrl;

    public OrgDataModel() {
        // Default constructor required for Firebase
    }

    public OrgDataModel(String location, String mail,String mission,String contact,String orgName,String profileImageUrl) {
        this.location= location;
        this.mail = mail;
        this.mission = mission;
        this.contact=contact;
        this.orgName = orgName;
        this.profileImageUrl=profileImageUrl;






    }





    public String getorgName() {
        return orgName;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }

    public String getMission() {
        return mission;
    }

    public String getmail() {
        return mail;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
