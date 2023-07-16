package com.teacai.activitypub.model;

public class CollectionPage extends ActivityStreamsCollection {

    private String partOf;
    private ActivityStreamsObject next;
    private ActivityStreamsObject prev;

    public String getPartOf() {
        return partOf;
    }

    public void setPartOf(String partOf) {
        this.partOf = partOf;
    }

    public ActivityStreamsObject getNext() {
        return next;
    }

    public void setNext(ActivityStreamsObject next) {
        this.next = next;
    }

    public ActivityStreamsObject getPrev() {
        return prev;
    }

    public void setPrev(ActivityStreamsObject prev) {
        this.prev = prev;
    }
}
