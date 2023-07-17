package com.teacai.activitypub.model;

import java.time.Instant;
import java.util.List;

public class Question extends Activity {

    private List<ActivityStreamsObject> oneOf;
    private List<ActivityStreamsObject> anyOf;
    private Instant closed;

    public List<ActivityStreamsObject> getOneOf() {
        return oneOf;
    }

    public void setOneOf(List<ActivityStreamsObject> oneOf) {
        this.oneOf = oneOf;
    }

    public List<ActivityStreamsObject> getAnyOf() {
        return anyOf;
    }

    public void setAnyOf(List<ActivityStreamsObject> anyOf) {
        this.anyOf = anyOf;
    }

    public Instant getClosed() {
        return closed;
    }

    public void setClosed(Instant closed) {
        this.closed = closed;
    }
}
