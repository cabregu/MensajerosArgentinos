package com.logicamente.mensajerosargentinos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_NuevoCliente extends AppCompatActivity {
        private TextView tvUsuario;
        private EditText etCliente, etCorreo, etDireccion, etRubro;
        private Button btnRegistrarEmpresa, btnCancelar;
        private FirebaseFirestore db;
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_nuevo_cliente);

            tvUsuario = findViewById(R.id.tvUsuario);
            etCliente = findViewById(R.id.etCliente);
            etCorreo = findViewById(R.id.etCorreo);
            etDireccion = findViewById(R.id.etDireccion);
            etRubro = findViewById(R.id.etRubro);
            btnRegistrarEmpresa = findViewById(R.id.btnRegistrarEmpresa);
            btnCancelar = findViewById(R.id.btnCancelar);

            // Inicializa Firestore y FirebaseAuth
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();

            // Mostrar el email del usuario logueado
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("USER_EMAIL")) {
                String email = intent.getStringExtra("USER_EMAIL");
                tvUsuario.setText(email);
            }

            // Configurar el botón de registrar
            btnRegistrarEmpresa.setOnClickListener(v -> registrarCliente());
        }




    private void registrarCliente() {
        String nombreCliente = etCliente.getText().toString().trim();
        String correoCliente = etCorreo.getText().toString().trim();
        String direccionCliente = etDireccion.getText().toString().trim();
        String rubroCliente = etRubro.getText().toString().trim();

        if (nombreCliente.isEmpty() || correoCliente.isEmpty() || direccionCliente.isEmpty() || rubroCliente.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            CollectionReference clientesRef = db.collection("users").document(userId).collection("clientes");

            clientesRef.whereEqualTo("nombre", nombreCliente).get().addOnCompleteListener(task -> {

                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    Toast.makeText(this, "El cliente ya existe", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> cliente = new HashMap<>();
                    cliente.put("nombre", nombreCliente);
                    cliente.put("email", correoCliente);
                    cliente.put("direccion", direccionCliente);
                    cliente.put("rubro", rubroCliente);


                    clientesRef.add(cliente).addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Cliente registrado exitosamente", Toast.LENGTH_SHORT).show();
                        // Limpiar campos
                        etCliente.setText("");
                        etCorreo.setText("");
                        etDireccion.setText("");
                        etRubro.setText("");
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al registrar cliente", Toast.LENGTH_SHORT).show();
                    });
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Error al verificar cliente", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "No hay usuario logueado", Toast.LENGTH_SHORT).show();
        }
    }

}


