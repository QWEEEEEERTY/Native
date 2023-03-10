package com.example.myapplicationjava.models;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Firebase {
    public static void pushObject(String path, Object obj){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
        databaseReference.setValue(obj);
    }

    public static <T> void getObjectList(String path, ArrayList<T> list, Class<T> type, ArrayAdapter<T> adapter){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference(path);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                list.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    list.add(userSnapshot.getValue(type));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
    public static <T> void copyObject(String path, Class<T> className, OnCompleteListener<T> listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference(path);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Task task = Tasks.forResult(dataSnapshot.getValue(className));
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
