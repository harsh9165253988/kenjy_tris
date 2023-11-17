package com.example.project;

public class organizationdetail {


    String OrgName,Mail,Contact,Mission,Password,Location;
    public organizationdetail(String OrgName,String Mail,String Contact,String Mission,String Password,String Location)
    {
        this.OrgName=OrgName;
        this.Mail=Mail;
        this.Contact=Contact;
        this.Mission=Mission;
        this.Password=Password;
        this.Location=Location;

    }

    public organizationdetail()
    {

    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getMission() {
        return Mission;
    }

    public void setMission(String mission) {
        Mission = mission;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }



    }

