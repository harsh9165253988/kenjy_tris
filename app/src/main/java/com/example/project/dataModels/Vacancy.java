package com.example.project.dataModels;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Vacancy {

    private String location;
    private String dateTime;
    private String preferredSkills;
    private String id;
    private String orgname;
    private String contact;

    public Vacancy() {
        // Default constructor required for calls to DataSnapshot.getValue(Vacancy.class)
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Vacancy(String location, String dateTime, String preferredSkills, String orgname, String contact) {
        this.location = location;
        this.dateTime = dateTime;
        this.preferredSkills = preferredSkills;
        this.orgname=orgname;
        this.contact=contact;
    }


    public boolean hasRequiredSkills(List<String> userSkills) {
        List<String> requiredSkills = getRequiredSkills();
        return userSkills.containsAll(requiredSkills);
    }

    public List<String> getRequiredSkills() {
        if (preferredSkills != null && !preferredSkills.isEmpty()) {
            return Arrays.asList(preferredSkills.split(","));
        } else {
            return Collections.emptyList();
        }
    }

    public String getLocation() {
        return location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getPreferredSkills() {
        return preferredSkills;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}