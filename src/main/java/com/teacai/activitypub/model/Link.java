package com.teacai.activitypub.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An ActivityPub <b>Link</b> object.
 */
public class Link extends ActivityStreamsObject {

    private String href;

    @JsonProperty(ActivityStreamsFields.HREF_LANG)
    private String hrefLang;

    private String rel;
    private Integer height;
    private Integer width;
    private String preview;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getHrefLang() {
        return hrefLang;
    }

    public void setHrefLang(String hrefLang) {
        this.hrefLang = hrefLang;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}
