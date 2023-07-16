package com.teacai.activitypub.model;

import java.util.List;

public class OrderedCollectionPage extends CollectionPage {

    private Integer startIndex;

    private List<ActivityStreamsObject> orderedItems;

    public List<ActivityStreamsObject> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<ActivityStreamsObject> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }
}
