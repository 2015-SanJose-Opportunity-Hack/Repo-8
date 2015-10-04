package com.app.service4seniors.service4seniors.task;

/**
 * Created by sumitvalecha on 10/4/15.
 */
public class Task {

    private String _id;
    private String task;
    private String date;
    private String status;
    private String type;

    public Task(String _id, String task, String date, String status, String type) {
        this.task = task;
        this.date = date;
        this.status = status;
        this.type = type;
        this._id = _id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
