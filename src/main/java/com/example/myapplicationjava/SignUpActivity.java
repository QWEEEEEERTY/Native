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
import com.google.android.gms.common.api.ApiException;



public class SignUpActivity extends AppCompatActivity {
    Button registerButton;
    com.google.android.gms.common.SignInButton googleButton;
    EditText emailfield, password, username, confirm;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        registerButton = findViewById(R.id.register_btn);
        registerButton.setOnClickListener(v ->
        {
            emailfield = findViewById(R.id.register_email);
            password = findViewById(R.id.create_password);
            username = findViewById(R.id.register_username);
            confirm = findViewById(R.id.confirm_password);
            if(isValidSignUp(username.getText().toString(), emailfield.getText().toString(), password.getText().toString(), confirm.getText().toString())==null)
            {
                User user = new User(username.getText().toString(), emailfield.getText().toString(), password.getText().toString());
                user.checkEmailExists(task ->
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
            else{
                Toast.makeText(SignUpActivity.this, "Usernames must contain at least 5 symbols", Toast.LENGTH_LONG).show();
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

    private String isValidSignUp(String username, String email, String password, String confirmPassword)
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
            try
            {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                User user = new User(account.getDisplayName(), account.getEmail());
                user.checkEmailExists(task ->
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
            catch (ApiException e)
            {
                Toast.makeText(SignUpActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    public void signInNow(View view)
    {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }
}