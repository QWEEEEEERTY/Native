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



public class SignInActivity extends AppCompatActivity{
    Button loginButton;
    com.google.android.gms.common.SignInButton googleButton;
    String password, email;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener(v ->
        {
            email = ((EditText) findViewById(R.id.email_login)).getText().toString();
            password = ((EditText) findViewById(R.id.password)).getText().toString();

            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                Toast.makeText(SignInActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();

            }
            else if (password.length()<8)
            {
                Toast.makeText(SignInActivity.this, "Password must contain at least 8 symbols", Toast.LENGTH_SHORT).show();
            }
            else
            {
                User user = new User(email);
                user.getUserByEmail(task ->
                {
                    if (task.isSuccessful() && user.getPassword().equals(password))
                    {
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("UserId", user.getId());
                        intent.putExtra("Username", user.getUsername());
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SignInActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
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
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("UserId", user.getId());
                        intent.putExtra("Username", user.getUsername());
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(SignInActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                });
        }
    }
    public void signUpNow(View view)
    {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}