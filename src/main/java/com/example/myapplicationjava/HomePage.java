package com.example.myapplicationjava;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomePage extends Fragment {
    String username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        TextView textView = view.findViewById(R.id.welcome);
        textView.setText(username);
        return view;
    }
    public HomePage(String username){
        this.username = username;
    }
}