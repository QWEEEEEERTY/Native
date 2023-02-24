package com.example.myapplicationjava;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MessagePage extends Fragment {
    private ListView userListView;
    private ArrayList<User> users;
    private ArrayList<String> usernames;
    //private ArrayAdapter<String> usersAdapter;
    private ListAdapter usersAdapter;
    private String me;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragment = inflater.inflate(R.layout.fragment_message_page, container, false);
        me = getActivity().getIntent().getStringExtra("UserId");

        userListView = fragment.findViewById(R.id.list_view_messages);
        usernames = new ArrayList<>();
        users = new ArrayList<>();

        usersAdapter = new ListAdapter(getActivity(), users);
        userListView.setAdapter(usersAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("Users");
        usersReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                usernames.clear();
                users.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    User user = userSnapshot.getValue(User.class);
                    if( ! user.getId().equals(me) )
                    {
                        usernames.add(user.getUsername());
                        users.add(user);
                    }
                }
                usersAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        userListView.setOnItemClickListener((parent, view, position, id) -> {
            User partner = users.get(position);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new DirectMessage(partner.getUsername(), partner.getId()));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return fragment;
    }
}
