package com.example.myapplicationjava.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.myapplicationjava.models.Time;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Message {
    private String content, sender, owner, time = "22.02.2023 12:27";

    public String getContent() { return content; }

    public String getSender() { return sender; }

    public String getOwner() { return owner; }

    public String getTime() { return time; }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTime() { time = Time.setUserTime(time); }

    public Message(String sender, String owner, String content)
    {
        this.sender = sender;
        this.owner = owner;
        this.content = content;
    }
    public Message() {}


    public void pushMessage()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Messages");
        //String userId = databaseReference.push().getKey();
        String id;
        if(sender.compareTo(owner) < 0)
            id = sender+owner;
        else
            id = owner+sender;
        this.time = Time.getCurrentTime();
        databaseReference.child(id+"/"+time).child("").setValue(this);
    }
}

