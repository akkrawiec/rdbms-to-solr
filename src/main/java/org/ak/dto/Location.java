package org.ak.dto;

import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

public class Location {
    private String id;
    private String name;
    private String elevation;
    private String latitude;
    private String longitude;

    public Location() { }

    public String getId() {
        return id;
    }

    @Field("ID")
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Field
    public void setName(String name) {
        this.name = name;
    }

    public String getElevation() {
        return elevation;
    }

    @Field
    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getLatitude() {
        return latitude;
    }

    @Field
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() { return longitude; }

    @Field
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void withElevations(List<Elevation> data) {
        this.setElevation(data.toString()); //TODO - extract data from list
    }

    public void withLatitudes(List<Latitude> data) {
        this.setLatitude(data.toString()); //TODO - extract data from list
    }

    public void withLongitude(List<Longitude> data) {
        this.setLongitude(data.toString()); //TODO - extract data from list
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Location{");
        sb.append("name='").append(name).append('\'').append('}');
        return sb.toString();
    }
}

