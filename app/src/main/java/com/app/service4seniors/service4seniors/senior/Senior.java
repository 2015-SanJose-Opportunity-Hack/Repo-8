package com.app.service4seniors.service4seniors.senior;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sumitvalecha on 10/3/15.
 */
public class Senior {
    private String name;
    private String phone;
    private String age;
    private String height;
    private String weight;
    private String address;
    private Set<String> diseaseList;
    private boolean isDiseaseNew;
    private boolean isAnyNew;
    private String pid;


    public Senior(String name, String phone, String age,
                  String height, String weight, String address, String pid) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.address = address;
        this.diseaseList = new HashSet<>();
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<String> getDiseaseList() {
        return diseaseList;
    }

    public void addDisease(String[] diseases) {

        if(diseaseList.size() != diseases.length) {
            isDiseaseNew = true;
            isAnyNew = true;
        } else {
            isDiseaseNew = false;
        }

        for(int i=0;i<diseases.length;i++) {
            diseaseList.add(diseases[i]);
        }

    }

    public void setIsDiseaseNew(boolean isNew) {
        isDiseaseNew = isNew;
    }

    public boolean isDiseaseNew() {
        return isDiseaseNew;
    }

    public void setIsAnyNew(boolean isNew) {
        isAnyNew = isNew;
    }

    public boolean isAnyNew() {
        return isAnyNew;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
