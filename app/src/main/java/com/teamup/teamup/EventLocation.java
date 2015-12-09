package com.teamup.teamup;

import org.json.JSONObject;

/**
 * Created by Rushabh on 12/6/15.
 */
public class EventLocation {
    private String name;
    private Coordinate coordinate;

    public EventLocation(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Coordinate{
        private float latitude;
        private float longtitude;

        public Coordinate(float latitude, float longtitude) {
            this.latitude = latitude;
            this.longtitude = longtitude;
        }

        public float getLatitude() {
            return latitude;
        }

        public void setLatitude(float latitude) {
            this.latitude = latitude;
        }

        public float getLongtitude() {
            return longtitude;
        }

        public void setLongtitude(float longtitude) {
            this.longtitude = longtitude;
        }
    }
}
