package com.tech.smal.turkaf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView navigation;

    Fragment scoresFragment ;
    Fragment accountFragment ;
    Fragment settingsFragment ;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_scores:
                    scoresFragment = getSupportFragmentManager().findFragmentByTag(ScoresFragment.TAG);
                    if (scoresFragment == null)
                        scoresFragment = new ScoresFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            scoresFragment, ScoresFragment.TAG).commit();
                    return true;
                case R.id.navigation_person:
                    accountFragment = getSupportFragmentManager().findFragmentByTag(AccountFragment.TAG);
                    if (accountFragment == null)
                        accountFragment = new AccountFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            accountFragment, AccountFragment.TAG).commit();
                    return true;
                case R.id.navigation_settings:
                    settingsFragment = getSupportFragmentManager().findFragmentByTag(SettingsFragment.TAG);
                    if (settingsFragment == null)
                        settingsFragment = new SettingsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            settingsFragment, SettingsFragment.TAG).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.navigation_scores);
    }



}
