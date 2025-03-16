package com.example.stonksviewer.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stonksviewer.R;
import com.example.stonksviewer.data.database.AppDatabase;
import com.example.stonksviewer.data.dao.UserDao;
import com.example.stonksviewer.model.User;
import com.example.stonksviewer.utils.EncryptionHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvGoToLogin;
    private UserDao userDao;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Referencias a las vistas
        scrollView = findViewById(R.id.scrollView);
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        // Desactivar modo de pantalla completa en teclado
        etUsername.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        etPassword.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        etConfirmPassword.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        // Obtener instancia de la base de datos
        AppDatabase db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        // Evento para ir a Login
        tvGoToLogin.setOnClickListener(v -> finish());

        // Evento para registrar usuario
        btnRegister.setOnClickListener(v -> registerUser());

        // Hacer scroll automático cuando el usuario toca un campo de texto
        setupAutoScroll();
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validaciones
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el usuario ya existe
        if (userDao.getUser(username) != null) {
            Toast.makeText(this, "Este usuario ya está registrado. Prueba otro nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        // Encriptar la contraseña antes de guardarla
        String hashedPassword = EncryptionHelper.hashPassword(password);

        // Guardar usuario en la base de datos
        User newUser = new User(username, hashedPassword);
        userDao.insertUser(newUser);

        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupAutoScroll() {
        etUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                scrollView.post(() -> scrollView.smoothScrollTo(0, etUsername.getTop()));
            }
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                scrollView.post(() -> scrollView.smoothScrollTo(0, etPassword.getTop()));
            }
        });

        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                scrollView.post(() -> scrollView.smoothScrollTo(0, etConfirmPassword.getTop()));
            }
        });
    }
}