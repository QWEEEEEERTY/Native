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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class AddPost extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_add_post, container, false);
        ImageButton cancelPost = fragment.findViewById(R.id.post_cancel);
        cancelPost.setOnClickListener(v ->
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new HomePage());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        ImageButton sharePost = fragment.findViewById(R.id.post_done);
        sharePost.setOnClickListener(v ->
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new HomePage());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return fragment;
    }
}