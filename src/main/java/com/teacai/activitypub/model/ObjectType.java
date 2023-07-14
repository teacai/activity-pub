package com.teacai.activitypub.model;

public enum ObjectType {
    Activity, Article, Audio, Collection, CollectionPage, Document, Event,
    Image, Link, Note, Object, OrderedCollection, OrderedCollectionPage,
    Page, Person, Place, Profile, Relationship, Tombstone, Video,

    Accept, Add, Announce, Arrive, Block, Create, Delete, Dislike, Flag,
    Follow, Ignore, Invite, Join, Leave, Like, Listen, Move, Offer, Question,
    Reject, Read, Remove, TentativeReject, TentativeAccept, Travel, Undo,
    Update, View;

    public static ObjectType parse(String value) {
        if (value != null && value.length() > 0) {
            for(ObjectType type : values()) {
                if (type.name().equalsIgnoreCase(value)) {
                    return type;
                }
            }
        }
        return null;
    }
}