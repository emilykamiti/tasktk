package com.tasktk.app.entity;

import java.util.Date;

public class Message extends BaseEntity{
    //team_id
    //sender_id
    private String content;
    private Date timeStamp;
    private Boolean is_read;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Boolean getIs_read() {
        return is_read;
    }

    public void setIs_read(Boolean is_read) {
        this.is_read = is_read;
    }
}
