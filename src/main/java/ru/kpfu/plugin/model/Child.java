package ru.kpfu.plugin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Child {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("children")
    @Expose
    private
    List<Child> children = null;

    @SerializedName("absoluteBoundingBox")
    @Expose
    private
    AbsoluteBoundingBox boundingBox = null;

    /*
    0 - widget
    1 - group
     */
    private Integer flutterType;

    private String flutterName;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public AbsoluteBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(AbsoluteBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Integer getFlutterType() {
        return flutterType;
    }

    public void setFlutterType(Integer flutterType) {
        this.flutterType = flutterType;
    }

    public String getFlutterName() {
        return flutterName;
    }

    public void setFlutterName(String flutterName) {
        this.flutterName = flutterName;
    }
}
