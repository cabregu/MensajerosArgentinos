package com.logicamente.mensajerosargentinos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity_Principal extends AppCompatActivity {
    private EditText etMail, etPass, etRepeatPass;
    private Button btnRegistrar, btnIrALogin;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_principal);

        // Inicialización de Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Verificar si el usuario ya está logueado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToHome();
        }

        // Inicialización de vistas
        etMail = findViewById(R.id.etCorreo);
        etPass = findViewById(R.id.etPassword);
        etRepeatPass = findViewById(R.id.etRepetirPassword);
        btnRegistrar = findViewById(R.id.btnRegistrarme);
        btnIrALogin = findViewById(R.id.btnIralogin);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etMail.getText().toString().trim();
                String password = etPass.getText().toString().trim();
                String repeatPassword = etRepeatPass.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                    Toast.makeText(MainActivity_Principal.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(repeatPassword)) {
                    Toast.makeText(MainActivity_Principal.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity_Principal.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(MainActivity_Principal.this, "Registro exitoso.",
                                            Toast.LENGTH_SHORT).show();
                                    // Redirigir al login
                                    Intent intent = new Intent(MainActivity_Principal.this, MainActivity_Login.class);
                                    startActivity(intent);
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity_Principal.this, "Fallo en la autenticación.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        btnIrALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_Principal.this, MainActivity_Login.class);
                startActivity(intent);
            }
        });
    }

    private void navigateToHome() {
        Intent intentHome = new Intent(MainActivity_Principal.this, MainActivity_Home.class);
        startActivity(intentHome);
        finish();
    }
}
