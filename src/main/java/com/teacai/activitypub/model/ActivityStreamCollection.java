package com.teacai.activitypub.model;

import java.util.List;

public class ActivityStreamCollection extends BaseObject {

    private Integer totalItems;
    private String current;
    private String first;
    private String last;
    private List<ActivityStreamsObject> items;

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public List<ActivityStreamsObject> getItems() {
        return items;
    }

    public void setItems(List<ActivityStreamsObject> items) {
        this.items = items;
    }
}
