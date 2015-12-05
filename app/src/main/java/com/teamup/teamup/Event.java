package com.teamup.teamup;

/**
 * Created by rohan on 4/12/15.
 */
public class Event {

    String eventName;
    String eventTag;

    public Event(String eventName,String eventTag){
        this.eventName = eventName;
        this.eventTag = eventTag;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTag() {
        return eventTag;
    }

    public void setEventTag(String eventTag) {
        this.eventTag = eventTag;
    }
}
