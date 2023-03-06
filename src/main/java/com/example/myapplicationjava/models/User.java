package com.example.myapplicationjava.models;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.util.Log;


import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class User {
    private String id;
    private String username;
    private String email;
    private String password = "Google";
    private String registrationTime;
    private String gender;
    private String name;
    private String surname;
    private String phone;
    private String birthday;
    private String imageUrl;





    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRegistrationTime() { return registrationTime; }
    public String getGender() { return gender; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getPhone() { return phone; }
    public String getBirthday() { return birthday; }
    public String getImageUrl() { return imageUrl; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public User(String username, String email, String password)
    {
        this.id = email.replace(".", ",");
        this.registrationTime = Time.getCurrentTime();
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public User(String username, String email)
    {
        this.id = email.replace(".", ",");
        this.registrationTime = Time.getCurrentTime();
        this.username = username;
        this.email = email;
    }

    public User() { }
}

