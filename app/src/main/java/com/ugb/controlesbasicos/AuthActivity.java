package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AuthActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText duiEditText;
    private EditText passwordEditText;
    private Button button;
    private Button boton;
    private Button chat;
    private db dbHelper;
    private SessionManager sessionManager;
    private TextView textViewNuevasIdeas;
    private TextView textViewFMLN;
    private TextView textViewARENA;
    private TextView textViewFuerzaSolidaria;
    private TextView textViewNuestroTiempo;
    private TextView textViewFPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        dbHelper = new db(this);
        sessionManager = new SessionManager(this);

        // Inicializar Firebase
        FirebaseApp.initializeApp(this);

        // Obtener referencia a la base de datos y al almacenamiento
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Obtener referencias de los elementos de la interfaz de usuario
        duiEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        button = findViewById(R.id.btregis);
        boton = findViewById(R.id.btacce);
        textViewNuevasIdeas = findViewById(R.id.textViewNuevasIdeas);
        textViewFMLN = findViewById(R.id.textViewFMLN);
        textViewARENA = findViewById(R.id.textarena);
        textViewFuerzaSolidaria = findViewById(R.id.textnuestrotiempo);
        textViewNuestroTiempo = findViewById(R.id.textViewfuerzasolidaria);
        textViewFPS = findViewById(R.id.textViewfps);
        chat = findViewById(R.id.floatingActionButton);

        // Configurar el botón para registrar datos en Firebase al hacer clic
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dui = duiEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (!dui.isEmpty() && !password.isEmpty()) {
                    writeToFirebase(dui, password);
                } else {
                    Toast.makeText(AuthActivity.this, "Por favor ingrese el DUI y la contraseña.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPredeterminedResponses();
                Intent intent = new Intent(getApplicationContext(), chatsoporte.class);
                startActivity(intent);
            }

            private void addPredeterminedResponses() {
                DatabaseReference responsesRef = databaseReference.child("predeterminedResponses");

                responsesRef.child("step1").setValue("Coméntanos cuál es tu problema.");
                responsesRef.child("step2").setValue("Agrega un número de teléfono o correo para contactarnos con usted.");
                responsesRef.child("step3").setValue("Nuestro equipo se pondrá en contacto con usted lo más pronto posible.");
            }
        });

        // Configurar el botón para iniciar sesión en Firebase al hacer clic
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dui = duiEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (!dui.isEmpty() && !password.isEmpty()) {
                    verifyLogin(dui, password);
                } else {
                    Toast.makeText(AuthActivity.this, "Por favor ingrese el DUI y la contraseña.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Contar los votos al inicio de la actividad
        contarVotosPorPartido();
    }

    private void writeToFirebase(String dui, String password) {
        // Verificar si el DUI ya está registrado
        databaseReference.child("usuarios").child(dui).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // El DUI ya está registrado, mostrar un mensaje al usuario
                    Toast.makeText(AuthActivity.this, "El DUI ya está registrado.", Toast.LENGTH_SHORT).show();
                } else {
                    // El DUI no está registrado, proceder a escribir los datos
                    DatabaseReference userRef = databaseReference.child("usuarios").child(dui);
                    userRef.child("contraseña").setValue(password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Escribir en la base de datos local
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("dui", dui);
                            values.put("contra", password);
                            long newRowId = db.insert("tabla", null, values);

                            // Operación exitosa
                            Toast.makeText(AuthActivity.this, "Datos escritos exitosamente en Firebase.", Toast.LENGTH_SHORT).show();
                            duiEditText.setText("");
                            passwordEditText.setText("");
                        } else {
                            // Si falla la operación, mostrar un mensaje al usuario.
                            Toast.makeText(AuthActivity.this, "Error al escribir en Firebase: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error
                Toast.makeText(AuthActivity.this, "Error al verificar el DUI en Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyLogin(String dui, String password) {
        // Verificar los datos en la base de datos
        databaseReference.child("usuarios").child(dui).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.child("contraseña").getValue(String.class);
                    String voto = dataSnapshot.child("voto").getValue(String.class);
                    if (storedPassword != null && storedPassword.equals(password)) {
                        if (voto == null) {
                            sessionManager.saveDUI(dui);

                            Toast.makeText(AuthActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), candidatos.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AuthActivity.this, "El usuario ya ha votado.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AuthActivity.this, "Contraseña incorrecta.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AuthActivity.this, "DUI no encontrado.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AuthActivity.this, "Error al verificar los datos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void contarVotosPorPartido() {
        DatabaseReference usuariosRef = databaseReference.child("usuarios");
        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int nuevasIdeas = 0;
                int fmln = 0;
                int arena = 0;
                int fuerzaSolidaria = 0;
                int nuestroTiempo = 0;
                int fps = 0;

                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    String voto = usuarioSnapshot.child("voto").getValue(String.class);
                    if (voto != null) {
                        switch (voto) {
                            case "nuevas ideas":
                                nuevasIdeas++;
                                break;
                            case "fmln":
                                fmln++;
                                break;
                            case "arena":
                                arena++;
                                break;
                            case "fuerza solidaria":
                                fuerzaSolidaria++;
                                break;
                            case "nuestro tiempo":
                                nuestroTiempo++;
                                break;
                            case "fps":
                                fps++;
                                break;
                            default:
                                break;
                        }
                    }
                }

                // Actualizar los TextViews con los resultados
                textViewNuevasIdeas.setText("Nuevas Ideas: " + nuevasIdeas);
                textViewFMLN.setText("FMLN: " + fmln);
                textViewARENA.setText("ARENA: " + arena);
                textViewFuerzaSolidaria.setText("Fuerza Solidaria: " + fuerzaSolidaria);
                textViewNuestroTiempo.setText("Nuestro Tiempo: " + nuestroTiempo);
                textViewFPS.setText("FPS: " + fps);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error
            }
        });
    }
}