package com.teamup.teamup;

/**
 * Created by rohan on 4/12/15.
 */
public class Event {

    String eventTitle;
    String eventTag;
    String eventLocation;
    String eventTime;
    String eventAttendance;

    public Event(String eventTag,String eventLocation,String eventTime,String eventTitle,String eventAttendance){
        this.eventTitle = eventTitle;
        this.eventTag = eventTag;
        this.eventLocation = eventLocation;
        this.eventTime = eventTime;
        this.eventAttendance = eventAttendance;
    }

    public String getEventName() {
        return eventTitle;
    }
    public void setEventName(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventTag() {
        return eventTag;
    }
    public void setEventTag(String eventTag) {
        this.eventTag = eventTag;
    }

    public String getEventTime() {
        return eventTime;
    }
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventAttendance() {
        return eventAttendance;
    }
    public void setEventAttendance(String eventAttendance) {
        this.eventAttendance = eventAttendance;
    }
}
