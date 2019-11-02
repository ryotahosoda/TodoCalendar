package com.example.demo.model;

public class Event {
    private String title;
    private String start;
    private String end;
    private Boolean allDay;
    private String color;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }
    public String getEnd() {
        return end;
    }
    public void setEnd(String end) {
        this.end = end;
    }
    public Boolean getAllDay(){
        return allDay;
    }
    public void setAllDay(Boolean allDay){
        this.allDay=allDay;
    }
    public String getColor(){
        return color;
    }
    public void setColor(String color){
        this.color=color;
    }
}
