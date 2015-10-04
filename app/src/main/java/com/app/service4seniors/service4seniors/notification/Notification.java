package com.app.service4seniors.service4seniors.notification;

/**
 * Created by sumitvalecha on 10/4/15.
 */
public class Notification {
    private String task;
    private String date;
    private String sender;
    private String reciever;
    private String type;

    public Notification(String task, String date, String sender, String reciever, String type) {
        this.task = task;
        this.date = date;
        this.sender = sender;
        this.reciever = reciever;
        this.type = type;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
