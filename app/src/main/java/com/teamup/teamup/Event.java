package com.teamup.teamup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;

/**
 * Created by rohan on 4/12/15.
 */

public class Event {

    private String title;
    private String tag;
    private String location;
    private String eventId;
    private String creator;
    private String rawLoc;
    private int capacity;
    private String description;
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;
    private int enrolment;
    private int privacy;

    public Event() {

    }

    public Event(JSONObject eventJson) throws JSONException {
        if(eventJson.has("location")) {
            location = eventJson.getJSONObject("location").toString();
        }

        if(eventJson.has("capacity")) {
            capacity = eventJson.getInt("capacity");
        }

        if(eventJson.has("creator")) {
            creator = eventJson.getString("creator");
        }

        if(eventJson.has("description")) {
            description = eventJson.getString("description");
        }

        if(eventJson.has("end_time")) {
            endTime = new GregorianCalendar();
            long time = Long.parseLong(eventJson.getString("end_time"));
            endTime.setTimeInMillis(time*1000);        }

        if(eventJson.has("start_time")) {
            startTime = new GregorianCalendar();
            long time = Long.parseLong(eventJson.getString("start_time"));
            startTime.setTimeInMillis(time*1000);        }

        if(eventJson.has("enrolment")) {
            enrolment = eventJson.getJSONArray("enrolment").length();
        }

        if(eventJson.has("privacy")) {
            privacy = eventJson.getInt("privacy");
        }

        if(eventJson.has("tag")) {
            tag = eventJson.getString("tag");
        }

        if(eventJson.has("title")) {
            title = eventJson.getString("title");
        }

        if(eventJson.has("loc_raw")) {
            rawLoc = eventJson.getString("loc_raw");
        }
    }

    public String getTitle() {return title;}
    public void setTitle(String title) {
        this.title = title;
    }

    public String getRawLoc(){return rawLoc;}

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public void setStartTime(GregorianCalendar startTime) {
        this.startTime = startTime;
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public void setEndTime(GregorianCalendar endTime) {
        this.endTime = endTime;
    }

    public int getEnrolment() {
        return enrolment;
    }

    public void setEnrolment(int enrolment) {
        this.enrolment = enrolment;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }
}