package com.example.stonksviewer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.stonksviewer.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences prefs;
    private TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        usernameText = findViewById(R.id.usernameText);
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Obtener nombre de usuario desde Intent o SharedPreferences
        String username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            username = prefs.getString("username", "");
        }

        // Mostrar el nombre de usuario si hay sesión activa
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        if (isLoggedIn && !username.isEmpty()) {
            usernameText.setText(username);
            usernameText.setVisibility(View.VISIBLE);
        } else {
            usernameText.setVisibility(View.GONE);
        }

        updateDrawerMenu();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_profile) {
                    return true;
                } else if (item.getItemId() == R.id.nav_settings) {
                    return true;
                } else if (item.getItemId() == R.id.nav_session_action) {
                    handleSessionAction();
                    return true;
                }
                return false;
            }
        });
    }


    public void openDrawer(View view) {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void updateDrawerMenu() {
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        MenuItem sessionItem = navigationView.getMenu().findItem(R.id.nav_session_action);
        MenuItem profileItem = navigationView.getMenu().findItem(R.id.nav_profile);

        if (isLoggedIn) {
            sessionItem.setTitle("Cerrar Sesión");
            sessionItem.setIcon(R.drawable.ic_logout);
            profileItem.setVisible(true);
        } else {
            sessionItem.setTitle("Iniciar Sesión");
            sessionItem.setIcon(R.drawable.ic_login);
            profileItem.setVisible(false);
        }
    }

    private void handleSessionAction() {
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        SharedPreferences.Editor editor = prefs.edit();
        if (isLoggedIn) {
            editor.putBoolean("isLoggedIn", false);
            editor.putString("username", "");
            editor.apply();

            // Reiniciar HomeActivity para aplicar cambios
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
