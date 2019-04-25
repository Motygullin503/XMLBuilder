package ru.kpfu.plugin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FigmaModel {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("lastModified")
    @Expose
    private String lastModified;

    @SerializedName("thumbnailUrl")
    @Expose
    private String thumbnailUrl;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("document")
    @Expose
    private Document document;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}