package com.teacai.activitypub.model;

import java.util.List;

public class OrderedCollection extends BaseCollection {

    private List<ActivityStreamsObject> orderedItems;

    public List<ActivityStreamsObject> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<ActivityStreamsObject> orderedItems) {
        this.orderedItems = orderedItems;
    }
}
