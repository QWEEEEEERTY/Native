package com.example.myapplicationjava.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplicationjava.R;
import com.example.myapplicationjava.adapters.PostListAdapter;
import com.example.myapplicationjava.models.Firebase;
import com.example.myapplicationjava.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomePage extends Fragment {
    String currentUsername;
    View fragment;
    ListView postListView;
    TextView textView;
    ImageButton addPostBtn;
    ArrayList<Post> posts;
    PostListAdapter postListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragment = inflater.inflate(R.layout.fragment_home_page, container, false);
        this.init();
        this.getPostList();
        this.addPostOnClick();
        textView.setText("Hello, " + currentUsername);
        return fragment;
    }

    private void init(){
        textView = fragment.findViewById(R.id.welcome);
        currentUsername = getActivity().getIntent().getStringExtra("Username");
        addPostBtn = fragment.findViewById(R.id.fixed_button);
        posts = new ArrayList<>();
        postListAdapter = new PostListAdapter(getActivity(), posts);
        postListView = fragment.findViewById(R.id.list_view_posts);
        postListView.setAdapter(postListAdapter);
    }

    private void addPostOnClick()
    {
        addPostBtn.setOnClickListener(v ->
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AddPost());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    private void getPostList()
    {
        Firebase.getObjectList("Posts", posts, Post.class, postListAdapter);
    }

}