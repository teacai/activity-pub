package com.teacai.activitypub.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Activity extends BaseObject {

    @JsonProperty("actor")
    private List<ActivityStreamsObject> actors;
    private String object;
    private String target;
    private String result;
    private String origin;
    private String instrument;

    public List<ActivityStreamsObject> getActors() {
        return actors;
    }

    public void setActors(List<ActivityStreamsObject> actors) {
        this.actors = actors;
    }
}
