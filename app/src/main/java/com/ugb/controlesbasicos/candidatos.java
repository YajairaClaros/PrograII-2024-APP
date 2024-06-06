package com.ugb.controlesbasicos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class candidatos extends AppCompatActivity {
    private ImageButton btt;
    private Button vvv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_candidatos);

        btt = findViewById(R.id.imgCandidato1);
        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), can1.class);
                startActivity(intent);
            }
        });

        btt = findViewById(R.id.imgCandidato2);
        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), can2.class);
                startActivity(intent);
            }
        });

        btt = findViewById(R.id.imgCandidato2);
        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), can2.class);
                startActivity(intent);
            }
        });

        btt = findViewById(R.id.imgCandidato3);
        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), can3.class);
                startActivity(intent);
            }
        });

        btt = findViewById(R.id.imgCandidato4);
        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), can4.class);
                startActivity(intent);
            }
        });

        btt = findViewById(R.id.imgCandidato5);
        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), can5.class);
                startActivity(intent);
            }
        });

        btt = findViewById(R.id.imgCandidato6);
        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), can6.class);
                startActivity(intent);
            }
        });

        vvv = findViewById(R.id.bnvotar);
        vvv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), votar.class);
                startActivity(intent);
            }
        });

        vvv = findViewById(R.id.bnsalir);
        vvv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                startActivity(intent);
            }
        });



    }
}
