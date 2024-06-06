package com.ugb.controlesbasicos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class votar extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText voteEditText;
    private Button addVoteButton;
    private Button boton;
    private SessionManager sessionManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votar);

        // Inicializar Firebase
        FirebaseApp.initializeApp(this);

        // Obtener referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Inicializar SessionManager
        sessionManager = new SessionManager(this);

        // Inicializar proveedor de ubicación
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtener referencias de los elementos de la interfaz de usuario
        voteEditText = findViewById(R.id.candidatoavotar);
        addVoteButton = findViewById(R.id.btagvotar);

        // Configurar el botón para agregar el voto en Firebase al hacer clic
        addVoteButton.setOnClickListener(v -> {
            String vote = voteEditText.getText().toString().trim().toLowerCase();
            if (!vote.isEmpty()) {
                String dui = sessionManager.getDUI();
                if (dui != null) {
                    if (vote.equalsIgnoreCase("nuevas ideas") || vote.equalsIgnoreCase("fmln") || vote.equalsIgnoreCase("arena") || vote.equalsIgnoreCase("nuestro tiempo") || vote.equalsIgnoreCase("fuerza solidaria") || vote.equalsIgnoreCase("fps")) {
                        getLocationPermission();
                        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(votar.this, "Agrega un partido valido.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(votar.this, "Error: No se pudo obtener el DUI del usuario.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(votar.this, "Por favor ingrese su voto.", Toast.LENGTH_SHORT).show();
            }
        });

        boton = findViewById(R.id.btcand);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), candidatos.class);
                startActivity(intent);
            }
        });
    }

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            // Si ya se concedieron los permisos, puedes iniciar la obtención de la ubicación
            obtenerUbicacionActual();
        } else {
            // Si los permisos no están concedidos, solicítalos al usuario
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void obtenerUbicacionActual() {
        if (locationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            // Una vez obtenida la ubicación, agrega el voto con las coordenadas
                            agregarVotoConCoordenadas(latitude, longitude);
                        } else {
                            Toast.makeText(votar.this, "No se pudo obtener la ubicación actual.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(votar.this, "Error al obtener la ubicación: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void agregarVotoConCoordenadas(double latitude, double longitude) {
        // Agregar el voto al usuario existente en la base de datos, junto con las coordenadas
        String vote = voteEditText.getText().toString().trim().toLowerCase();
        String dui = sessionManager.getDUI();
        if (dui != null) {
            databaseReference.child("usuarios").child(dui).child("voto").setValue(vote);
            databaseReference.child("usuarios").child(dui).child("ubicacion").child("latitud").setValue(latitude);
            databaseReference.child("usuarios").child(dui).child("ubicacion").child("longitud").setValue(longitude)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Operación exitosa
                            Toast.makeText(votar.this, "Voto agregado exitosamente.", Toast.LENGTH_SHORT).show();
                            voteEditText.setText("");
                        } else {
                            // Si falla la operación, mostrar un mensaje al usuario.
                            Toast.makeText(votar.this, "Error al agregar el voto: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                obtenerUbicacionActual();
            }
        }
    }
}
