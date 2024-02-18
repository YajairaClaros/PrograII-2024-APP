package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

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
    TextView tempVal;
    Spinner spn;
    Button btnLongitud;
    Button btnVolumen;
    Button btnMasa;
    Button btnAlmacenamiento;
    Button btnTransmision;
    Button btnMonedas;
    Button btnTiempo;
    conversores miObj = new conversores();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(R.id.tbhConversor);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("LON").setContent(R.id.tabLongitud).setIndicator("LONGITUD", null));
        tbh.addTab(tbh.newTabSpec("ALM").setContent(R.id.tabAlmacenamiento).setIndicator("ALMACENAMIENTO", null));
        tbh.addTab(tbh.newTabSpec("MON").setContent(R.id.tabMonedas).setIndicator("MONEDAS", null));
        tbh.addTab(tbh.newTabSpec("VOL").setContent(R.id.tabVolumen).setIndicator("VOLUMEN",null));
        tbh.addTab(tbh.newTabSpec("MAS").setContent(R.id.tabMasa).setIndicator("MASA",null));
        tbh.addTab(tbh.newTabSpec("TRS").setContent(R.id.tabTransmision).setIndicator("TRANSMISION",null));
        tbh.addTab(tbh.newTabSpec("TM").setContent(R.id.tabTiempo).setIndicator("TIEMPO",null));

        // Botón para conversión de longitud
        btnLongitud = findViewById(R.id.btnConvertirLongitud);
        btnLongitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    spn = findViewById(R.id.spnDeLongitud);
                    int de = spn.getSelectedItemPosition();

                    spn = findViewById(R.id.spnALongitud);
                    int a = spn.getSelectedItemPosition();

                    tempVal = findViewById(R.id.txtCantidadLongitud);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());

                    double resp = miObj.convertir(0, de, a, cantidad);
                    Toast.makeText(getApplicationContext(), "Respuesta: " + resp, Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        // Botón para conversión de volumen
        btnVolumen = findViewById(R.id.btnConvertirVolumen);
        btnVolumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    spn = findViewById(R.id.spnDEVolumen);
                    int de = spn.getSelectedItemPosition();

                    spn = findViewById(R.id.spnAVolumen);
                    int a = spn.getSelectedItemPosition();

                    tempVal = findViewById(R.id.txtCantidadDeVolumen);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());
                    double resp = miObj.convertir(1, de, a, cantidad);
                    Toast.makeText(getApplicationContext(), "Respuesta:" + resp, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Botón para conversión de masa
        btnMasa = findViewById(R.id.btnConvertirMasa);
        btnMasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    spn = findViewById(R.id.spnDEMasa);
                    int de = spn.getSelectedItemPosition();

                    spn = findViewById(R.id.spnAMasa);
                    int a = spn.getSelectedItemPosition();

                    tempVal = findViewById(R.id.txtCantidadDeMasa);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());
                    double resp = miObj.convertir(2, de, a, cantidad);
                    Toast.makeText(getApplicationContext(), "Respuesta:" + resp, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Botón para conversión de almacenamiento
        btnAlmacenamiento = findViewById(R.id.btnConvertirAlmacenamiento);
        btnAlmacenamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    spn = findViewById(R.id.spnDEAlmacenamiento);
                    int de = spn.getSelectedItemPosition();

                    spn = findViewById(R.id.spnAAlmacenamiento);
                    int a = spn.getSelectedItemPosition();

                    tempVal = findViewById(R.id.txtCantidadDeAlmacenamiento);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());
                    double resp = miObj.convertir(3, de, a, cantidad);
                    Toast.makeText(getApplicationContext(), "Respuesta:" + resp, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Botón para conversión de transmisión
        btnTransmision = findViewById(R.id.btnConvertirTransmision);
        btnTransmision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    spn = findViewById(R.id.spnDETransmision);
                    int de = spn.getSelectedItemPosition();

                    spn = findViewById(R.id.spnATransmision);
                    int a = spn.getSelectedItemPosition();

                    tempVal = findViewById(R.id.txtCantidadDeTransmision);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());
                    double resp = miObj.convertir(4, de, a, cantidad);
                    Toast.makeText(getApplicationContext(), "Respuesta:" + resp, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Botón para conversión de monedas
        btnMonedas = findViewById(R.id.btnConvertirMonedas);
        btnMonedas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    spn = findViewById(R.id.spnDEMonedas);
                    int de = spn.getSelectedItemPosition();

                    spn = findViewById(R.id.spnAMonedas);
                    int a = spn.getSelectedItemPosition();

                    tempVal = findViewById(R.id.txtCantidadDeMonedas);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());
                    double resp = miObj.convertir(5, de, a, cantidad);
                    Toast.makeText(getApplicationContext(), "Respuesta:" + resp, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Botón para conversión de tiempo
        btnTiempo = findViewById(R.id.btnConvertirTiempo);
        btnTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    spn = findViewById(R.id.spnDETiempo);
                    int de = spn.getSelectedItemPosition();

                    spn = findViewById(R.id.spnATiempo);
                    int a = spn.getSelectedItemPosition();

                    tempVal = findViewById(R.id.txtCantidadDeTiempo);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());
                    double resp = miObj.convertir(6, de, a, cantidad);
                    Toast.makeText(getApplicationContext(), "Respuesta:" + resp, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

class conversores{
    double[][] valores ={
            // Longitud
            {1, 100, 39.3701, 3.28084, 1.193, 1.09361, 0.001, 0.000621371},
            // Volumen
            {1, 0.001, 4.16667, 1.05669, 0.264172, 1000, 3.51951, 35.1951, 0.0353147, 61.0237},
            // Masa
            {1, 1000, 0.00220462, 0.035274, 0.001, 1e+6, 9.8421e-7, 1.1023e-6},
            // Almacenamiento
            {1, 1000, 8e-6, 0.008, 8000, 0.001, 1e-6, 1e-9, 1e-12, 8.8818e-13},
            // Transmisión
            {1, 953.674, 125, 1000, 976563, 125000, 1e+6, 1e+9, 0.125, 0.931323, 0.001, 0.000125, 0.000909495},
            // Monedas
            {1, 17.10, 24.63, 7.81, 36.73, 516.29, 1.00, 2.01, 1.35, 0.93, 150.13, 36.27, 3914.96, 1.54},
            // Tiempo
            {1, 24, 1440, 86400, 8.64e+7, 0.142857, 0.0328767, 0.00273973, 0.000273973, 2.7397e-5},
    };
    public double convertir (int opcion, int de, int a, double cantidad){
        return valores[opcion][a]/valores[opcion][de]*cantidad;
    }
}