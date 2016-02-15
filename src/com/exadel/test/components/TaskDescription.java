package com.exadel.test.components;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TaskDescription implements Serializable {
    private String colourSpaceType;
    private String description;

    public TaskDescription(String colourSpaceType, String description) {
        this.colourSpaceType = colourSpaceType;
        this.description = description;
    }

    @JsonProperty("Type")
    public String getColourSpaceType() {
        return colourSpaceType;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    public void setColourSpaceType(String colourSpaceType) {
        this.colourSpaceType = colourSpaceType;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
