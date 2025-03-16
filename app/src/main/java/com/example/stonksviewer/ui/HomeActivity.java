package com.example.stonksviewer.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
                    String username = prefs.getString("username", null);
                    if (username != null) {
                        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(HomeActivity.this, "Debes iniciar sesión para acceder al perfil", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else if (item.getItemId() == R.id.nav_settings) {
                    Toast.makeText(HomeActivity.this, "Futura funcionalidad a implementar", Toast.LENGTH_SHORT).show(); // Para depuración
                    showSettingsNotification();  // Aseguramos que se llame a la función
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

    /**
     * Actualiza la interfaz de usuario en función del estado de inicio de sesión.
     */
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

    /**
     * Maneja la acción de inicio/cierre de sesión.
     */
    private void handleSessionAction() {
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            new AlertDialog.Builder(this)
                    .setTitle("Cerrar Sesión")
                    .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        // Cerrar sesión correctamente
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();
                        editor.apply();

                        // Reiniciar la actividad para actualizar UI
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    /**
     * Muestra una notificación local indicando que la funcionalidad de ajustes aún no está disponible.
     */
    @SuppressLint("MissingPermission")
    private void showSettingsNotification() {
        String channelId = "settings_channel";
        String channelName = "Notificaciones de Ajustes";

        // Crear el canal de notificación (para Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // Crear la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notifications)  // Icono de la notificación
                .setContentTitle("Funcionalidad en desarrollo")
                .setContentText("Esta funcionalidad será implementada en futuras versiones.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(2, builder.build());
    }

    /**
     * Abre el menú lateral al hacer clic en el botón de perfil.
     */
    public void openDrawer(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}
