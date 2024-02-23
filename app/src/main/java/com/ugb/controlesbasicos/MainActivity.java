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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TabHost tbh;
    TextView tempval;
    Spinner spn;
    Button btnArea;
    conversores miObj = new conversores();
    TextView var1;
    Button btnMetros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnMetros = findViewById(R.id.btnConvertirMetros);
        btnMetros.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                var1 = findViewById(R.id.txtCantidadMetros);
                double mtsConsumidos = Double.parseDouble(var1.getText().toString());
                double valorPagar;

                if (mtsConsumidos >= 1 && mtsConsumidos <= 18) {
                    valorPagar = 6;
                } else if (mtsConsumidos >= 19 && mtsConsumidos <= 28) {
                    valorPagar = 6 + 0.45 * (mtsConsumidos - 18);
                } else {
                    valorPagar = 6 + 0.45 * 10 + 0.65 * (mtsConsumidos - 28);
                }

                var1 = findViewById(R.id.Resultado);
                var1.setText("Respuesta: " + valorPagar);
            }
        });


        tbh = findViewById(android.R.id.tabcontent); // Corrección aquí
        tbh.setup();
        tbh.addTab(tbh.newTabSpec("ARA").setContent(R.id.tabArea).setIndicator("AREA", null));

        btnArea = findViewById(R.id.btnConvertirArea);
        btnArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    spn = findViewById(R.id.spnDEArea);
                    int de = spn.getSelectedItemPosition();
                    spn = findViewById(R.id.spnAArea);
                    int a = spn.getSelectedItemPosition();
                    tempval = findViewById(R.id.txtCantidadArea);
                    double cantidad = Double.parseDouble(tempval.getText().toString());
                    double resp = miObj.convertir(0, de, a, cantidad);
                    Toast.makeText(getApplicationContext(), "Respuesta:" + resp, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    class conversores {
        double[][] valores = {
                //area
                {1, 0.1329421, 0.111111, 0.092903, 2.2957e-5, 0.0000132, 9.2903e-6}

        };

        public double convertir(int opcion, int de, int a, double cantidad) {
            return valores[opcion][a] / valores[opcion][de] * cantidad;
        }
    }

}


