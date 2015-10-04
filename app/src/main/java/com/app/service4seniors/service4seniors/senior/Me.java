package com.app.service4seniors.service4seniors.senior;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sumitvalecha on 10/3/15.
 */
public class Me {
    private static Me instance;

    private String careTaker;
    private String type;
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

    public Me(String pid) {
        this.pid = pid;
        this.diseaseList = new HashSet<>();

        instance = this;
    }

    public static Me getInstance() {
        return instance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public boolean isDiseaseNew() {
        return isDiseaseNew;
    }

    public void setIsDiseaseNew(boolean isDiseaseNew) {
        this.isDiseaseNew = isDiseaseNew;
    }

    public boolean isAnyNew() {
        return isAnyNew;
    }

    public void setIsAnyNew(boolean isAnyNew) {
        this.isAnyNew = isAnyNew;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getCareTaker() {
        return careTaker;
    }

    public void setCareTaker(String careTaker) {
        this.careTaker = careTaker;
    }
}
