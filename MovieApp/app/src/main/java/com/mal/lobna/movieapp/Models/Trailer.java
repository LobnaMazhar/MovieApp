package com.mal.lobna.movieapp.Models;

/**
 * Created by Lobna on 18-Nov-16.
 */

public class Trailer{
    private String Id;
    private String name;
    private String key;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
