package com.example.stonksviewer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.stonksviewer.R;
import com.example.stonksviewer.data.database.AppDatabase;
import com.example.stonksviewer.data.dao.UserDao;
import com.example.stonksviewer.model.User;
import com.example.stonksviewer.utils.EncryptionHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private UserDao userDao;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencias a las vistas
        scrollView = findViewById(R.id.scrollView);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        etUsername.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        etPassword.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        // Obtener instancia de la base de datos
        AppDatabase db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        // Evento para ir a la pantalla de registro
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Evento para iniciar sesión
        btnLogin.setOnClickListener(v -> loginUser());

        // Hacer scroll automático cuando el usuario toca un campo de texto
        setupAutoScroll();



    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buscar usuario en la base de datos
        User user = userDao.getUser(username);

        // Comprobar si la contraseña ingresada es correcta
        if (user != null && user.password.equals(EncryptionHelper.hashPassword(password))) {
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish(); // Cierra LoginActivity
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupAutoScroll() {
        etUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                scrollView.post(() -> scrollView.smoothScrollTo(0, etUsername.getBottom()));
            }
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                scrollView.post(() -> scrollView.smoothScrollTo(0, etPassword.getBottom()));
            }
        });
    }
}
