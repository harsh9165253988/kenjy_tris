package com.example.project;

import java.net.PortUnreachableException;

public class humandetail {

    String name,email,mob,age,pswd,gender,skl;

    public humandetail(String name, String email, String mob, String age, String pswd,String gender,String skl) {
        this.name = name;
        this.email = email;
        this.mob = mob;
        this.age = age;
        this.pswd = pswd;
        this.gender=gender;
        this.skl=skl;
    }
    humandetail(){

    }

    public String getSkl() {
        return skl;
    }

    public void setSkl(String skl) {
        this.skl = skl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

}
