package com.logicamente.mensajerosargentinos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity_Login extends AppCompatActivity {
    private Button btnIniciarSesion, btnRegistrarme;
    private EditText etUsuario, etContraseña;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        // Inicialización de Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        etUsuario = findViewById(R.id.etUsername);
        etContraseña = findViewById(R.id.etPassword);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarme = findViewById(R.id.btnRegistrarme);

        // Verificar si el usuario ya está logueado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToHome();
        }

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etUsuario.getText().toString().trim();
                String password = etContraseña.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity_Login.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity_Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Inicio de sesión exitoso
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    saveLoginState();
                                    navigateToHome();
                                } else {
                                    // Si el inicio de sesión falla, mostrar un mensaje al usuario.
                                    Toast.makeText(MainActivity_Login.this, "Fallo en la autenticación.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        btnRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPrincipal = new Intent(MainActivity_Login.this, MainActivity_Principal.class);
                startActivity(intentPrincipal);
            }
        });
    }

    private void saveLoginState() {
        // Guardar el estado de inicio de sesión en SharedPreferences
        getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("isLoggedIn", true)
                .apply();
    }

    private void navigateToHome() {
        Intent intentHome = new Intent(MainActivity_Login.this, MainActivity_Home.class);
        startActivity(intentHome);
        finish();
    }
}
