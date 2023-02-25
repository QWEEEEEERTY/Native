package com.example.myapplicationjava;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.messaging.FirebaseMessaging;


public class SignUpActivity extends AppCompatActivity {
    Button registerButton;
    com.google.android.gms.common.SignInButton googleButton;
    String email, password, username, confirm;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

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

        registerButton = findViewById(R.id.register_btn);
        registerButton.setOnClickListener(v ->
        {
            email = ((EditText) findViewById(R.id.register_email)).getText().toString();
            password = ((EditText) findViewById(R.id.create_password)).getText().toString();
            username = ((EditText) findViewById(R.id.register_username)).getText().toString();
            confirm = ((EditText) findViewById(R.id.confirm_password)).getText().toString();

            String signUpError = isWrongSignUp(username, email, password, confirm);
            if( signUpError!=null)
            {
                Toast.makeText(SignUpActivity.this, signUpError, Toast.LENGTH_LONG).show();
            }
            else
            {
                User user = new User(username, email, password);
                user.getUserByEmail(task ->
                {
                    if (task.isSuccessful())
                    {
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.putExtra("UserId", user.getId());
                        intent.putExtra("Username", user.getUsername());
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                });
            }
        });
        
        googleButton = findViewById(R.id.sign_in_google);
        googleButton.setOnClickListener(v ->
        {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            gsc = GoogleSignIn.getClient(this, gso);
            Intent signInIntent = gsc.getSignInIntent();
            startActivityForResult(signInIntent, 123);
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

    //Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123)
        {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult();
                User user = new User(account.getDisplayName(), account.getEmail());
                user.getUserByEmail(task ->
                {
                    if (task.isSuccessful())
                    {
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.putExtra("UserId", user.getId());
                        intent.putExtra("Username", user.getUsername());
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                });
        }
    }
    public void signInNow(View view)
    {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}