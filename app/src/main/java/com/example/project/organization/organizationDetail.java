package com.example.project.organization;
import android.os.Parcel;
import android.os.Parcelable;

public class organizationDetail implements Parcelable {

    String OrgName, Mail, Contact, Mission, Password, Location;

    public organizationDetail(String OrgName, String Mail, String Contact, String Mission, String Password, String Location) {
        this.OrgName = OrgName;
        this.Mail = Mail;
        this.Contact = Contact;
        this.Mission = Mission;
        this.Password = Password;
        this.Location = Location;
    }

    protected organizationDetail(Parcel in) {
        OrgName = in.readString();
        Location = in.readString();
        // Read your other properties
    }
    public static final Creator<organizationDetail> CREATOR = new Creator<organizationDetail>() {
        @Override
        public organizationDetail createFromParcel(Parcel in) {
            return new organizationDetail(in);
        }

        @Override
        public organizationDetail[] newArray(int size) {
            return new organizationDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OrgName);
        dest.writeString(Location);
        // Write your other properties
    }
    public organizationDetail() {
        // Default constructor required for Firebase
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
