package com.example.myapplicationjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class SignInActivity extends AppCompatActivity{
    Button loginButton;
    com.google.android.gms.common.SignInButton googleButton;
    EditText password, email;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener(v ->
        {
            password = findViewById(R.id.password);
            email = findViewById(R.id.email_login);
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            checkEmailPassword(emailText, passwordText);
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

    private void checkEmailPassword(String emailText, String passwordText)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String userId, username;
                boolean isDataMatch = false;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    User user = userSnapshot.getValue(User.class);
                    if (user.getEmail().equals(emailText) && user.getPassword().equals(passwordText))
                    {
                        isDataMatch = true;
                        userId = user.getId();
                        username = user.getUsername();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("UserId", userId);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        break;
                    }
                }
                if( !isDataMatch )
                    Toast.makeText(SignInActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.e("LoginActivity", "Error while checking username and password", databaseError.toException());
            }
        });
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
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("UserId", user.getId());
                        intent.putExtra("Username", user.getUsername());
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(SignInActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                });
            }
            catch (ApiException e)
            {
                Toast.makeText(SignInActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    public void signUpNow(View view)
    {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}