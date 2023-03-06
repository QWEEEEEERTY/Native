package com.example.myapplicationjava.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplicationjava.R;
import com.example.myapplicationjava.adapters.ContactListAdapter;
import com.example.myapplicationjava.models.Firebase;
import com.example.myapplicationjava.models.User;

import java.util.ArrayList;


public class MessagePage extends Fragment {
    private View fragment;
    private ListView userListView;
    private ArrayList<User> users;
    private ContactListAdapter usersAdapter;
    private String currentUserId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragment = inflater.inflate(R.layout.fragment_message_page, container, false);

        this.init();
        Firebase.getObjectList("Users", users, User.class, usersAdapter);
        this.itemClickListener();

        return fragment;
    }

    private void init()
    {
        currentUserId = getActivity().getIntent().getStringExtra("UserId");
        users = new ArrayList<>();
        usersAdapter = new ContactListAdapter(getActivity(), users);
        userListView = fragment.findViewById(R.id.list_view_messages);
        userListView.setAdapter(usersAdapter);
    }

    private void itemClickListener()
    {
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            User partner = users.get(position);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new DirectMessage(partner.getUsername(), partner.getId()));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }
}