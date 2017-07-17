package com.example.taskstodo;

import java.io.Serializable;

/**
 * Created by Dhruv on 12-02-2017.
 */
public class Tasks implements Serializable {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
   private String title;
    private String description;
    private String date;
    private String time;

    public Tasks(String title, String description,String date, String time,long id){
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
