package com.example.myapplicationjava;


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

    public User(String username, String email, String password)
    {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public User(String username, String email)
    {
        this.username = username;
        this.email = email;
    }

    public User(String email) {
        this.email = email;
    }
    private void copyUser(User other){
        id = other.id;
        email = other.email;
        username = other.username;
        registrationTime = other.registrationTime;
        password = other.password;
        gender = other.gender;
        name = other.name;
        surname = other.surname;
        phone = other.phone;
        birthday = other.birthday;
    }

    public User() { }

    public void pushUser()
    {
        //socialnetjava.child(this.email).setValue(this);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        if(this.id == null)
        {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = dateFormat.format(calendar.getTime());
            String userId = databaseReference.push().getKey();
            this.id = userId;
            this.registrationTime = currentTime;

        }
        databaseReference.child(this.id).setValue(this);
    }


    public void getUserByEmail(OnCompleteListener<Void> listener)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    User userFromDb = userSnapshot.getValue(User.class);
                    if ( userFromDb.getEmail().equals(email) )
                    {
                        copyUser(userFromDb);
                        break;
                    }
                }
                Task<Void> task = Tasks.forResult(null);
                listener.onComplete(task);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.e("LoginActivity", "Error while checking username and password", databaseError.toException());
            }
        });
    }
    public void getUserById(OnCompleteListener<Void> listener, String searchId)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    if ( userSnapshot.getKey().equals(searchId) )
                    {
                        User userFromDb = userSnapshot.getValue(User.class);
                        copyUser(userFromDb);
                        break;
                    }
                }
                Task<Void> task = Tasks.forResult(null);
                listener.onComplete(task);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.e("LoginActivity", "Error while checking username and password", databaseError.toException());
            }
        });
    }
}

