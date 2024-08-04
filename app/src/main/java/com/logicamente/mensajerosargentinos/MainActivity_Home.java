package com.logicamente.mensajerosargentinos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity_Home extends AppCompatActivity {
    private TextView tvUsuario;
    private FirebaseAuth mAuth;
    private Button btnCrearCliente; // Asegúrate de que tienes este botón en tu layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        mAuth = FirebaseAuth.getInstance();
        tvUsuario = findViewById(R.id.tvUsuario);
        btnCrearCliente = findViewById(R.id.btnCrearCliente); // Asegúrate de que este ID es correcto

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            tvUsuario.setText(email);

            btnCrearCliente.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity_Home.this, MainActivity_NuevoCliente.class);
                intent.putExtra("USER_EMAIL", email); // Pasar el correo electrónico
                startActivity(intent);
            });
        } else {
            tvUsuario.setText("No hay usuario logueado");
        }
    }
}
