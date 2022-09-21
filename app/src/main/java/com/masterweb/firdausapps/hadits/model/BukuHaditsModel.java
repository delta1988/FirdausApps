package com.masterweb.firdausapps.hadits.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BukuHaditsModel {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("total")
    @Expose
    private String total;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getTotal() { return total; }
    public void setTotal(String total) { this.total = total; }
}
