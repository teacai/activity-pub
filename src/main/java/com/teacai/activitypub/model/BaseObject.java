package com.teacai.activitypub.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

/**
 * ActivityPub <b>Object</b> object.
 */
public class BaseObject extends ActivityStreamsObject {
    @JsonProperty(ActivityStreamsFields.ACTOR)
    private List<ActivityStreamsObject> actors;
    private ActivityStreamsObject attachment;
    private ActivityStreamsObject attributedTo;
    private ActivityStreamsObject audience;

    private String content;
    private ActivityStreamsObject context;
    private Instant endTime;
    private ActivityStreamsObject generator;
    private List<ActivityStreamsObject> icon;
    private List<ActivityStreamsObject> image;
    private List<ActivityStreamsObject> inReplyTo;
    private ActivityStreamsObject location;
    private ActivityStreamsObject preview;
    private Instant published;
    private BaseCollection replies;
    private Instant startTime;
    private String summary;
    private ActivityStreamsObject tag;
    private String updated;
    private List<ActivityStreamsObject> url;
    private ActivityStreamsObject to;
    private ActivityStreamsObject bto;
    private ActivityStreamsObject cc;
    private ActivityStreamsObject bcc;
    private String mediaType;
    private String duration;

    public List<ActivityStreamsObject> getActors() {
        return actors;
    }

    public void setActors(List<ActivityStreamsObject> actors) {
        this.actors = actors;
    }

    public ActivityStreamsObject getAttachment() {
        return attachment;
    }

    public void setAttachment(ActivityStreamsObject attachment) {
        this.attachment = attachment;
    }

    public ActivityStreamsObject getAttributedTo() {
        return attributedTo;
    }

    public void setAttributedTo(ActivityStreamsObject attributedTo) {
        this.attributedTo = attributedTo;
    }

    public ActivityStreamsObject getAudience() {
        return audience;
    }

    public void setAudience(ActivityStreamsObject audience) {
        this.audience = audience;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ActivityStreamsObject getContext() {
        return context;
    }

    public void setContext(ActivityStreamsObject context) {
        this.context = context;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public ActivityStreamsObject getGenerator() {
        return generator;
    }

    public void setGenerator(ActivityStreamsObject generator) {
        this.generator = generator;
    }

    public List<ActivityStreamsObject> getIcon() {
        return icon;
    }

    public void setIcon(List<ActivityStreamsObject> icon) {
        this.icon = icon;
    }

    public List<ActivityStreamsObject> getImage() {
        return image;
    }

    public void setImage(List<ActivityStreamsObject> image) {
        this.image = image;
    }

    public List<ActivityStreamsObject> getInReplyTo() {
        return inReplyTo;
    }

    public void setInReplyTo(List<ActivityStreamsObject> inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    public ActivityStreamsObject getLocation() {
        return location;
    }

    public void setLocation(ActivityStreamsObject location) {
        this.location = location;
    }

    public ActivityStreamsObject getPreview() {
        return preview;
    }

    public void setPreview(ActivityStreamsObject preview) {
        this.preview = preview;
    }

    public Instant getPublished() {
        return published;
    }

    public void setPublished(Instant published) {
        this.published = published;
    }

    public BaseCollection getReplies() {
        return replies;
    }

    public void setReplies(BaseCollection replies) {
        this.replies = replies;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ActivityStreamsObject getTag() {
        return tag;
    }

    public void setTag(ActivityStreamsObject tag) {
        this.tag = tag;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public List<ActivityStreamsObject> getUrl() {
        return url;
    }

    public void setUrl(List<ActivityStreamsObject> url) {
        this.url = url;
    }

    public ActivityStreamsObject getTo() {
        return to;
    }

    public void setTo(ActivityStreamsObject to) {
        this.to = to;
    }

    public ActivityStreamsObject getBto() {
        return bto;
    }

    public void setBto(ActivityStreamsObject bto) {
        this.bto = bto;
    }

    public ActivityStreamsObject getCc() {
        return cc;
    }

    public void setCc(ActivityStreamsObject cc) {
        this.cc = cc;
    }

    public ActivityStreamsObject getBcc() {
        return bcc;
    }

    public void setBcc(ActivityStreamsObject bcc) {
        this.bcc = bcc;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
