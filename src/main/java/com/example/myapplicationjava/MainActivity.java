package com.example.myapplicationjava;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.example.myapplicationjava.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    String myId, myUsername;
    int currentFragment = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);

        myId = getIntent().getStringExtra("UserId");
        myUsername = getIntent().getStringExtra("Username");

        replaceFragment(new HomePage(),1);
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
    private void replaceFragment(Fragment fragment, int i)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(currentFragment>i)
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        else if(currentFragment<i)
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        currentFragment = i;
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    public void exitProfile(View view){
        finish();
    }
    public void sharePost(View view) {

    }
    public void cancelPost(View view) {

    }
}
