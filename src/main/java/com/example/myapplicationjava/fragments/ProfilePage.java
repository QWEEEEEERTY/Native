package com.example.myapplicationjava.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationjava.R;
import com.example.myapplicationjava.models.Firebase;
import com.example.myapplicationjava.models.Time;
import com.example.myapplicationjava.models.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ProfilePage extends Fragment {
    private View fragment;
    private User user;
    private ImageView profileImg;
    private TextView since, email;
    private EditText  username, name, surname, phone, currentPassword, newPassword, confirmPassword;
    private Spinner gender;
    private Button changePictureButton;
    private DatePicker birthday;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragment = inflater.inflate(R.layout.fragment_profile_page, container, false);
        String currentUserId = getActivity().getIntent().getStringExtra("UserId");
        this.init(fragment);
        Firebase.copyObject("Users/"+currentUserId, User.class, task ->
        {
            user = task.getResult();
            this.showUserProfile();
        });
        this.saveProfileListener();
        this.resetPasswordListener();
        this.setImageListener();
        return fragment;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            profileImg.setImageURI(uri);
            // Do something with the chosen image
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + uri.getLastPathSegment());
            // Upload the image to Firebase Storage
            imageRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                // Get the download URL of the uploaded image
                imageRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                    user.setImageUrl(downloadUrl.toString());
                    Firebase.pushObject("Users/"+user.getId(), user);
                    Toast.makeText(getContext(), "Profile image have been saved", Toast.LENGTH_LONG).show();
                });
            });
        }
    }
    private void init(View fragment)
    {
        user = new User();
        profileImg = fragment.findViewById(R.id.profile_picture);
        changePictureButton = fragment.findViewById(R.id.change_picture_button);
        since = fragment.findViewById(R.id.registration_date);
        email = fragment.findViewById(R.id.user_email);
        username = fragment.findViewById(R.id.username);
        name = fragment.findViewById(R.id.name);
        surname = fragment.findViewById(R.id.surname);
        phone = fragment.findViewById(R.id.phone);
        gender = fragment.findViewById(R.id.spinner_gender);
        birthday = fragment.findViewById(R.id.DOB);
        currentPassword = fragment.findViewById(R.id.current_password);
        newPassword = fragment.findViewById(R.id.new_password);
        confirmPassword = fragment.findViewById(R.id.confirm_password);
    }
    private void saveProfileListener()
    {
        Button saveButton = fragment.findViewById(R.id.save_changes_button);
        saveButton.setOnClickListener(v ->
        {
            user.setUsername(username.getText().toString());
            user.setName(name.getText().toString());
            user.setSurname(surname.getText().toString());
            user.setPhone(phone.getText().toString());
            user.setGender(gender.getSelectedItem().toString());
            user.setBirthday(Time.formatForDb(
                    birthday.getYear(),
                    birthday.getMonth(),
                    birthday.getDayOfMonth()
            ));
            Firebase.pushObject("Users/"+user.getId(), user);
            Toast.makeText(getContext(), "Changes have been saved", Toast.LENGTH_LONG).show();
        });
    }
    private void resetPasswordListener()
    {
        Button resetButton = fragment.findViewById(R.id.reset_password);
        resetButton.setOnClickListener(view ->
        {
            if(user.getPassword() == null) {
                Toast.makeText(getContext(), "Your current password is 'Google'", Toast.LENGTH_LONG).show();
            }
            else if( ! user.getPassword().equals(currentPassword.getText().toString())) {
                Toast.makeText(getContext(), "Incorrect password", Toast.LENGTH_LONG).show();
            }
            else if (newPassword.getText().toString().length() < 8) {
                Toast.makeText(getContext(), "New password must contain at least 8 symbols", Toast.LENGTH_LONG).show();
            }
            else if ( ! newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                Toast.makeText(getContext(), "Please confirm password", Toast.LENGTH_LONG).show();
            }
            else {
                user.setPassword(newPassword.getText().toString());
                Firebase.pushObject("Users/"+user.getId(), user);
                Toast.makeText(getContext(), "Password was changed successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setImageListener()
    {
        changePictureButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Title"), 100);
        });
    }
    private void showUserProfile()
    {
        since.setText(user.getRegistrationTime());
        email.setText(user.getEmail());
        username.setText(user.getUsername());
        name.setText(user.getName());
        surname.setText(user.getSurname());
        phone.setText(user.getPhone());
        Picasso.get().load(user.getImageUrl()).into(profileImg);
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) gender.getAdapter();
        int position = adapter.getPosition(user.getGender());
        gender.setSelection(position);
        if ( user.getBirthday() != null){
            int[] date = Time.formatForPicker(user.getBirthday());
            birthday.init(date[0], date[1], date[2], null);
        }
    }
}
