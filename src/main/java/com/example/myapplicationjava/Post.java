package com.example.myapplicationjava;

public class Post {
    private String id, userId, topic, text, image, time;
    public Post(String id, String topic, String image, String time ){
        this.id = id;
        this.topic = topic;
        this.image = image;
        this.time = time;
    }
}
