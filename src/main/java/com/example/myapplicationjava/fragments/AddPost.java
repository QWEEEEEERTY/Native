package com.example.myapplicationjava.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.myapplicationjava.R;
import com.example.myapplicationjava.fragments.HomePage;
import com.example.myapplicationjava.models.Post;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddPost extends Fragment {
    private View fragment;
    private ImageView userImg;
    private EditText postText, postTopic;
    private TextView username;
    private Post post;
    private ImageButton cancel, publish, postImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_add_post, container, false);
        this.init();
        this.setImgListener();
        this.cancelPostListener();
        return fragment;
    }
    private void init()
    {
        post = new Post();
        postImg = fragment.findViewById(R.id.post_picture);
        userImg = fragment.findViewById(R.id.profile_picture);
        postText = fragment.findViewById(R.id.post_text);
        postTopic = fragment.findViewById(R.id.post_topic);
        username = fragment.findViewById(R.id.username);
        cancel = fragment.findViewById(R.id.post_cancel);
        publish = fragment.findViewById(R.id.post_done);
    }
    private void cancelPostListener()
    {
        cancel.setOnClickListener(v ->
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new HomePage());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }
    private void publishPostListener()
    {
        publish.setOnClickListener(v ->
        {
            post.setUserId(getActivity().getIntent().getStringExtra("UserId"));
            post.setTopic(postTopic.getText().toString());
            post.setText(postText.getText().toString());
            post.publishPost();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new HomePage());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }
    private void setImgListener()
    {
        postImg.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Title"), 100);
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            postImg.setImageURI(uri);
            // Do something with the chosen image
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("posts/" + uri.getLastPathSegment());

            // Upload the image to Firebase Storage
            imageRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                // Get the download URL of the uploaded image
                imageRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                    post.setImageUrl(downloadUrl.toString());
                    publishPostListener();
                });
            });
        }
    }
}