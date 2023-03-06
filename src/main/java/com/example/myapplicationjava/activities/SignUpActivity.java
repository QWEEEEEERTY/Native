package com.example.myapplicationjava.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.example.myapplicationjava.R;
import com.example.myapplicationjava.models.Firebase;
import com.example.myapplicationjava.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignUpActivity extends AppCompatActivity {
    private Button registerButton;
    private User user;
    private com.google.android.gms.common.SignInButton googleButton;
    private EditText email, password, username, confirm;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //notification
        FirebaseMessaging.getInstance().subscribeToTopic("web_app").addOnCompleteListener(task -> {
            String msg = "Done";
            if (!task.isSuccessful()) {
                msg = "Failed";
            }
            System.out.println(msg);
        });
        this.init();
        this.registerOnClickListener();
        this.setGoogleSignIn();
    }

    public void  init(){
        registerButton = findViewById(R.id.register_btn);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.create_password);
        username = findViewById(R.id.register_username);
        confirm = findViewById(R.id.confirm_password);
        googleButton = findViewById(R.id.sign_in_google);

    }

    private void registerOnClickListener()
    {
        registerButton.setOnClickListener(v ->
        {
            String signUpError = isWrongSignUp(
                    username.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    confirm.getText().toString());

            if( signUpError!=null)
            {
                Toast.makeText(SignUpActivity.this, signUpError, Toast.LENGTH_LONG).show();
                return;
            }

            user = new User(username.getText().toString(), email.getText().toString(), password.getText().toString());
            Firebase.copyObject("Users/"+user.getId(), User.class, task ->
            {
                if(task.getResult() == null)
                {
                    Firebase.pushObject("Users/"+user.getId(), user);
                    enter();
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_LONG).show();
                }
            });

        });
    }

    private String isWrongSignUp(String username, String email, String password, String confirmPassword)
    {
        if (username.length() < 5)
            return "Usernames must contain at least 5 symbols";
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "Please enter valid email";
        if (password.length() < 8)
            return "Password must contain at least 8 symbols";
        if (!password.equals(confirmPassword))
            return "The password and its confirmation must be the same";
        return null;
    }

    private void setGoogleSignIn()
    {
        googleButton.setOnClickListener(v ->
        {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            gsc = GoogleSignIn.getClient(this, gso);
            Intent signInIntent = gsc.getSignInIntent();
            startActivityForResult(signInIntent, 123);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123)
        {

            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult();
            user = new User(account.getDisplayName(), account.getEmail());
            Firebase.copyObject("Users/"+user.getId(), User.class, task ->
            {
                if(task.getResult() == null)
                {
                    Firebase.pushObject("Users/"+user.getId(), user);
                    enter();
                }
                else
                {
                    user = task.getResult();
                }
            });
        }
    }

    private void enter()
    {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.putExtra("UserId", user.getId());
        intent.putExtra("Username", user.getUsername());
        startActivity(intent);
    }

    public void signInNow(View view)
    {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

}