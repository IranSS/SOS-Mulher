package com.example.sos_mulher.telas;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sos_mulher.R;
import com.example.sos_mulher.fragments.AlertFragment;
import com.example.sos_mulher.fragments.ConfigsFragment;
import com.example.sos_mulher.fragments.ContatosFragment;
import com.example.sos_mulher.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        Bundle bundle = getIntent().getExtras();

        BottomNavigationView bottomNav = findViewById(R.id.BottomNavigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            if(item.getItemId() == R.id.nav_menu){
                Log.d("Teste", "Teste home");
                fragment = new HomeFragment();
                if(bundle != null){
                    fragment.setArguments(bundle);
                }
            }
            if(item.getItemId() == R.id.nav_alert){
                Log.d("Teste", "Teste contatos");
                fragment = new AlertFragment();
            }
            if(item.getItemId() == R.id.nav_contatos){
                Log.d("Teste", "Teste contatos");
                fragment = new ContatosFragment();
            }
            if(item.getItemId() == R.id.nav_configs){
                Log.d("Teste", "Teste contatos");
                fragment = new ConfigsFragment();
            }
            if(fragment != null){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
            return true;
        });
    }
}