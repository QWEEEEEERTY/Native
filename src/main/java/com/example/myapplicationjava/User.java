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


public class User {
    private String id;
    private String username;
    private String email;
    private String password;

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

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
        this.id = userId;
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
}
