package com.teacai.activitypub.model;

import java.time.Instant;

public class Tombstone extends BaseObject {

    private ObjectType formerType;

    private Instant deleted;

    public ObjectType getFormerType() {
        return formerType;
    }

    public void setFormerType(ObjectType formerType) {
        this.formerType = formerType;
    }

    public Instant getDeleted() {
        return deleted;
    }

    public void setDeleted(Instant deleted) {
        this.deleted = deleted;
    }
}
