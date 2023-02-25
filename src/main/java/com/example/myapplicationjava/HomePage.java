package com.example.myapplicationjava;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class HomePage extends Fragment {
    String username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_home_page, container, false);
        username = getActivity().getIntent().getStringExtra("Username");

        TextView textView = fragment.findViewById(R.id.welcome);
        ImageButton addPost = fragment.findViewById(R.id.fixed_button);
        addPost.setOnClickListener(v ->
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AddPost());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        textView.setText("Hello, " + username);
        return fragment;
    }
}