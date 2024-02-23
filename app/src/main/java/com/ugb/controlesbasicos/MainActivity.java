package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView var1;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnConvertirMetros);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var1 = findViewById(R.id.txtCantidadMetros);
                double mtsConsumidos = Double.parseDouble(var1.getText().toString());
                double valorPagar;

                if(mtsConsumidos >= 1 && mtsConsumidos <= 18){
                    valorPagar = 6;
                }else if(mtsConsumidos >= 19 && mtsConsumidos <=28){
                    valorPagar = 6 + 0.45 *(mtsConsumidos - 18);
                } else {
                    valorPagar = 6 + 0.45 * 10 + 0.65 *(mtsConsumidos - 28);
                }

                var1 = findViewById(R.id.Resultado);
                var1.setText("Respuesta: " + valorPagar);
            }
        });

    }
}


