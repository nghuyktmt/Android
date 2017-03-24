package com.example.huynguyen.note_customlistview;

import java.io.Serializable;

/**
 * Created by HUYNGUYEN on 11/9/2016.
 */

public class Note implements Serializable{
    public String title;
    public String content;
    public String time;

    public Note() {
    }

    public Note(String title, String content, String time) {
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
