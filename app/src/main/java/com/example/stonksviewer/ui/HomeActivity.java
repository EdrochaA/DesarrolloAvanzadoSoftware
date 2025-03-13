package com.example.stonksviewer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
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
    private MenuItem sessionItem, profileItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        usernameText = findViewById(R.id.usernameText);
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        Menu menu = navigationView.getMenu();
        sessionItem = menu.findItem(R.id.nav_session_action);
        profileItem = menu.findItem(R.id.nav_profile);

        updateUI();

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

        // Configurar la barra de navegación inferior
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_graphs) {
                    Intent intent = new Intent(HomeActivity.this, MarketActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }


    private void updateUI() {
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        String username = prefs.getString("username", "");

        if (isLoggedIn && !username.isEmpty()) {
            usernameText.setText(username);
            usernameText.setVisibility(View.VISIBLE);
            profileItem.setVisible(true);
            sessionItem.setTitle("Cerrar Sesión");
            sessionItem.setIcon(R.drawable.ic_logout);
        } else {
            usernameText.setVisibility(View.GONE);
            profileItem.setVisible(false);
            sessionItem.setTitle("Iniciar Sesión");
            sessionItem.setIcon(R.drawable.ic_login);
        }
    }

    private void handleSessionAction() {
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        SharedPreferences.Editor editor = prefs.edit();

        if (isLoggedIn) {
            // Cerrar sesión correctamente
            editor.clear();
            editor.apply();

            // Reiniciar la actividad para actualizar UI
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    public void openDrawer(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}
