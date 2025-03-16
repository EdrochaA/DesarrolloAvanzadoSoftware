package com.example.stonksviewer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stonksviewer.R;
import com.example.stonksviewer.data.database.AppDatabase;
import com.example.stonksviewer.data.dao.UserDao;
import com.example.stonksviewer.model.User;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvPhone, tvEmail;
    private Button btnBack;
    private SharedPreferences prefs;
    private UserDao userDao;
    private String loggedInUsername; // Guardar el usuario logueado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inicializar vistas
        tvUsername = findViewById(R.id.tvUsername);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        btnBack = findViewById(R.id.btnBack);

        // Obtener preferencias compartidas
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        loggedInUsername = prefs.getString("username", null);

        // Obtener instancia de la base de datos
        userDao = AppDatabase.getInstance(this).userDao();

        if (loggedInUsername != null) {
            loadUserData(loggedInUsername);
        } else {
            Toast.makeText(this, "Error: No se encontró usuario logueado", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Botón para volver a HomeActivity
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserData(String username) {
        new Thread(() -> {
            User user = userDao.getUserByUsername(username);
            if (user != null) {
                runOnUiThread(() -> {
                    tvUsername.setText("Usuario: " + user.getUsername());
                    tvPhone.setText("Teléfono: " + (user.getPhone() != null && !user.getPhone().isEmpty() ? user.getPhone() : "No registrado"));
                    tvEmail.setText("Correo: " + (user.getEmail() != null && !user.getEmail().isEmpty() ? user.getEmail() : "No registrado"));
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Error: No se encontraron datos del usuario", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
