package com.example.myapplicationjava;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class User {
    private String id, username, email, password, registrationTime, gender;

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRegistrationTime() { return registrationTime; }
    public String getGender() { return gender; }

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
    public User() { }

    public void PushUser()
    {
        //socialnetjava.child(this.email).setValue(this);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        String userId = databaseReference.push().getKey();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(calendar.getTime());
        this.id = userId;
        this.registrationTime = currentTime;
        databaseReference.child(userId).setValue(this);
    }

    public void checkEmailExists(OnCompleteListener<Void> listener)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                boolean isUserExists = false;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    User user = userSnapshot.getValue(User.class);
                    if ( user.getEmail().equals(email) )
                    {
                        id = user.getId();
                        isUserExists = true;
                        break;
                    }
                }
                if (!isUserExists)
                    PushUser();

                Task<Void> task = Tasks.forResult(null);
                listener.onComplete(task);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.e("LoginActivity", "Error while checking username and password", databaseError.toException());
                // Call the listener to notify that the operation is completed with an error
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
                        User user = userSnapshot.getValue(User.class);
                        id = searchId;
                        username = user.getUsername();
                        email = user.getEmail();
                        registrationTime = user.getRegistrationTime();
                        if(user.getPassword() != null){
                            password = user.getPassword();
                        }
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
