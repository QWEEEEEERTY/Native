package com.example.myapplicationjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfilePage extends Fragment {
    ImageView profilePicture;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragment = inflater.inflate(R.layout.fragment_profile_page, container, false);
        String me = getActivity().getIntent().getStringExtra("UserId");
        User user = new User();
        user.getUserById(task ->
        {
            if (task.isSuccessful())
            {
                TextView since = fragment.findViewById(R.id.registration_date);
                TextView id = fragment.findViewById(R.id.user_id);
                TextView email = fragment.findViewById(R.id.user_email);
                EditText username = fragment.findViewById(R.id.username);

                since.setText(user.getRegistrationTime());
                id.setText(user.getId());
                email.setText(user.getEmail());
                username.setText(user.getUsername());
            }
        }, me);

        EditText name = fragment.findViewById(R.id.name);
        EditText surname = fragment.findViewById(R.id.surname);
        EditText phone = fragment.findViewById(R.id.phone);

        profilePicture = fragment.findViewById(R.id.profile_picture);
        Button changePictureButton = fragment.findViewById(R.id.change_picture_button);
        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Title"), 100);
            }
        });


        return fragment;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Uri uri = data.getData();
            profilePicture.setImageURI(uri);
            // Do something with the chosen image
        }
    }
}