package com.uesc.tac.ta5ks.model;

/**
 * Created by levy on 11/05/18.
 */

public class Tag {
    private int id;
    private String name;
    private int color;

    public Tag(){

    }

    //Using to INSERTS
    public Tag(int id, String name, int color){
        this.id = id;
        this.name = name;
        this.color = color;
    }

    //Using to UPDATES
    public Tag(String name, int color){
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
