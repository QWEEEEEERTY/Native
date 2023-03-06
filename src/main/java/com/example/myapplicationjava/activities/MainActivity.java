package com.example.myapplicationjava.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.myapplicationjava.R;
import com.example.myapplicationjava.databinding.ActivityMainBinding;
import com.example.myapplicationjava.fragments.HomePage;
import com.example.myapplicationjava.fragments.MessagePage;
import com.example.myapplicationjava.fragments.ProfilePage;
import com.example.myapplicationjava.fragments.SearchPage;
import com.example.myapplicationjava.fragments.SettingsPage;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private String currentUserId, currentUsername;
    private int currentFragmentId = 1;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.init();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());//setContentView(R.layout.activity_main);
        replaceFragment(new HomePage(),1);

        this.navbarOnClickListener();
    }
    private void init(){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        currentUserId = getIntent().getStringExtra("UserId");
        currentUsername = getIntent().getStringExtra("Username");
    }

    private void replaceFragment(Fragment fragment, int newFragmentId)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(currentFragmentId>newFragmentId)
        {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if(currentFragmentId<newFragmentId)
        {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        currentFragmentId = newFragmentId;
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void navbarOnClickListener()
    {
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item ->
        {
            switch (item.getItemId())
            {
                case R.id.home:
                    replaceFragment(new HomePage(),1);
                    break;
                case R.id.search:
                    replaceFragment(new SearchPage(),2);
                    break;
                case R.id.messages:
                    replaceFragment(new MessagePage(),3);
                    break;
                case R.id.profile:
                    replaceFragment(new ProfilePage(),4);
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsPage(),5);
            }
            return true;
        });
    }
    public void exitProfile(View view)
    {
        finish();
    }
}
