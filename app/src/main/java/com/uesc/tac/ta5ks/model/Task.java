package com.uesc.tac.ta5ks.model;

/**
 * Created by levy on 12/05/18.
 */

public class Task {
    private int id;
    private String title;
    private String description;
    private int status;
    private Tag tag;

    public Task(){

    }

    //Used to UPDATES
    public Task(int id, String title, String description, int status, Tag tag){
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.tag = tag;
    }

    //Used to INSERTS
    public Task(String title, String description, int status, Tag tag){
        this.title = title;
        this.description = description;
        this.status = status;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
