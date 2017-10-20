package com.henry.project.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemCollection {
    @SerializedName("results")
    private List<Item> results;

    public ItemCollection(List<Item> results) {
        this.results = results;
    }

    public List<Item> getResults() {
        return results;
    }

    public void setResults(List<Item> results) {
        this.results = results;
    }
}
