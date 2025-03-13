package com.example.stonksviewer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
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
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        User user = userDao.getUser(username);

        if (user == null) {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (EncryptionHelper.checkPassword(password, user.getPassword())) {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("username", username);
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
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