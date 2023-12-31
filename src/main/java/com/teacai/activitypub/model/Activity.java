package com.teacai.activitypub.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Activity extends BaseObject {

    @JsonProperty(ActivityStreamsFields.OBJECT)
    private List<ActivityStreamsObject> objects;

    @JsonProperty(ActivityStreamsFields.TARGET)
    private List<ActivityStreamsObject> targets;

    private ActivityStreamsObject result;

    private ActivityStreamsObject origin;

    private ActivityStreamsObject instrument;

    public List<ActivityStreamsObject> getObjects() {
        return objects;
    }

    public void setObjects(List<ActivityStreamsObject> objects) {
        this.objects = objects;
    }

    public List<ActivityStreamsObject> getTargets() {
        return targets;
    }

    public void setTargets(List<ActivityStreamsObject> targets) {
        this.targets = targets;
    }

    public ActivityStreamsObject getResult() {
        return result;
    }

    public void setResult(ActivityStreamsObject result) {
        this.result = result;
    }

    public ActivityStreamsObject getOrigin() {
        return origin;
    }

    public void setOrigin(ActivityStreamsObject origin) {
        this.origin = origin;
    }

    public ActivityStreamsObject getInstrument() {
        return instrument;
    }

    public void setInstrument(ActivityStreamsObject instrument) {
        this.instrument = instrument;
    }
}
