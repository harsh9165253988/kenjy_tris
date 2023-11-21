package com.example.project.organization;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Vacancy {

    private String location;
    private String dateTime;
    private String preferredSkills;

    public Vacancy() {
        // Default constructor required for calls to DataSnapshot.getValue(Vacancy.class)
    }

    public Vacancy(String location, String dateTime, String preferredSkills) {
        this.location = location;
        this.dateTime = dateTime;
        this.preferredSkills = preferredSkills;
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
}