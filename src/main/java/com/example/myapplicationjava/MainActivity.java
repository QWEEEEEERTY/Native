package com.example.myapplicationjava;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.example.myapplicationjava.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    String myId, myUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);

        myId = getIntent().getStringExtra("UserId");
        myUsername = getIntent().getStringExtra("Username");

        replaceFragment(new HomePage("Hello, " + myUsername));
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item ->
        {
            switch (item.getItemId())
            {
                case R.id.home:
                    replaceFragment(new HomePage("Hello, " + myUsername));
                    break;
                case R.id.search:
                    replaceFragment(new SearchPage());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfilePage());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsPage());
                    break;
                case R.id.messages:
                    replaceFragment(new MessagePage());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
