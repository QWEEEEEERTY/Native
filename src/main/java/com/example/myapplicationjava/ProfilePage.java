package com.example.myapplicationjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

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


public class ProfilePage extends Fragment {
    private View fragment;
    private User user;
    private ImageView profileImg;
    private TextView since, id, email;
    private EditText  username, name, surname, phone, currentPassword, newPassword, confirmPassword;
    private Spinner gender;
    private DatePicker birthday;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragment = inflater.inflate(R.layout.fragment_profile_page, container, false);
        String me = getActivity().getIntent().getStringExtra("UserId");
        this.init(fragment);
        this.loadUserProfile(me);
        this.saveProfileListener();
        this.resetPasswordListener();
        this.setImageListener();
        return fragment;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Uri uri = data.getData();
            profileImg.setImageURI(uri);
            // Do something with the chosen image
        }
    }
    private void init(View fragment)
    {
        since = fragment.findViewById(R.id.registration_date);
        id = fragment.findViewById(R.id.user_id);
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
            user.pushUser();
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
                user.pushUser();
                Toast.makeText(getContext(), "Password was changed successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setImageListener()
    {
        profileImg = fragment.findViewById(R.id.profile_picture);
        Button changePictureButton = fragment.findViewById(R.id.change_picture_button);
        changePictureButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Title"), 100);
        });
    }
    private void loadUserProfile(String searchId)
     {
        user.getUserById(task ->
        {
            if (task.isSuccessful())
            {
                since.setText(user.getRegistrationTime());
                id.setText(user.getId());
                email.setText(user.getEmail());
                username.setText(user.getUsername());
                name.setText(user.getName());
                surname.setText(user.getSurname());
                phone.setText(user.getPhone());
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) gender.getAdapter();
                int position = adapter.getPosition(user.getGender());
                gender.setSelection(position);
                if ( user.getBirthday() != null){
                    int[] date = Time.formatForPicker(user.getBirthday());
                    birthday.init(date[0], date[1], date[2], null);
                }
            }
        }, searchId);
    }
}