package com.robalascott.todolist.todolist;

/**
 * Created by robscott on 2017-10-22.
 */

public class TaskObject {

    /**
     * id : 2
     * title : Pick-up posters from post-office
     * description : They are only open from 9 am to 3 pm
     */

    private int id;
    private String title;
    private String description;

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

    public TaskObject(int id1, String title1, String description1 ){
        this.id = id1;
        this.title = title1;
        this.description = description1;
    }

    public String toStringAll(){
        return Integer.toString(this.id) + " " + this.title + " " + this.description;
    }
}
