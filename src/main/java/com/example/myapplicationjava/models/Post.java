package com.example.myapplicationjava.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Post {
    private String id, userId, topic, text, imageUrl, time;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTopic() {
        return topic;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTime() {
        return time;
    }

    public Post(){}
    public void publishPost(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        String userId = databaseReference.push().getKey();
        this.id = userId;
        this.time = Time.getCurrentTime();
        databaseReference.child(this.id).setValue(this);
    }
}
