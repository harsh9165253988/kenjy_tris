package com.example.project.organization;

public class Vacancy {

        private String location;
        private String dateTime;
        private String preferredSkills;

        public Vacancy(String location, String dateTime, String preferredSkills) {
            this.location = location;
            this.dateTime = dateTime;
            this.preferredSkills = preferredSkills;
        }
    public Vacancy() {
        // Default constructor required for calls to DataSnapshot.getValue(Vacancy.class)
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


