package com.teacai.activitypub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains all the common fields for all object types.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityStreamsObject {

    @JsonProperty("@context")
    private String context;

    private String id;

    private String name;

    private ObjectType type;


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType objectType) {
        this.type = objectType;
    }

}
