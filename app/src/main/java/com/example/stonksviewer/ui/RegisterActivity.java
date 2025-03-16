package com.example.stonksviewer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.stonksviewer.R;
import com.example.stonksviewer.data.dao.UserDao;
import com.example.stonksviewer.data.database.AppDatabase;
import com.example.stonksviewer.model.User;
import com.example.stonksviewer.utils.EncryptionHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword, etPhone, etEmail;
    private Button btnRegister;
    private TextView btnGoToLogin;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etRegisterPhone);
        etEmail = findViewById(R.id.etRegisterEmail);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.tvGoToLogin);

        userDao = AppDatabase.getInstance(getApplicationContext()).userDao();

        btnRegister.setOnClickListener(view -> registerUser());

        btnGoToLogin.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorDialog("Campos vacíos", "Todos los campos son obligatorios.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showErrorDialog("Error en la contraseña", "Las contraseñas no coinciden.");
            return;
        }

        if (userDao.getUserByUsername(username) != null) {
            showErrorDialog("Usuario en uso", "Este usuario ya está registrado.");
            return;
        }

        String hashedPassword = EncryptionHelper.hashPassword(password);
        User newUser = new User(username, hashedPassword, phone, email);
        userDao.insertUser(newUser);

        showSuccessNotification("Registro exitoso", "¡Bienvenido a StonksViewer!");

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Método para mostrar un diálogo de error.
     */
    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .show();
    }

    /**
     * Método para mostrar una notificación local tras un registro exitoso.
     */
    private void showSuccessNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
    }
}
