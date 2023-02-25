package com.example.myapplicationjava;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import java.text.SimpleDateFormat;


public class Message {
    private String content, sender, owner, time = "22.02.2023 12:27";

    public String getContent() { return content; }

    public String getSender() { return sender; }

    public String getOwner() { return owner; }

    public String getTime() { return time; }

    public Message(String sender, String owner, String content)
    {
        this.sender = sender;
        this.owner = owner;
        this.content = content;
    }
    public Message() {}

    public void pushMessage()
    {
        //socialnetjava.child(this.email).setValue(this);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Messages");
        //String userId = databaseReference.push().getKey();
        String id;
        if(sender.compareTo(owner) < 0)
            id = sender+owner;
        else
            id = owner+sender;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(calendar.getTime());
        this.time = currentTime;
        databaseReference.child(id).child(currentTime).setValue(this);
    }
}
