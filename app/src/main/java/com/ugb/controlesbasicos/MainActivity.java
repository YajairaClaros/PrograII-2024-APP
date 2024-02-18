package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    TextView tempVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempVal = findViewById(R.id.lblSensorLuz);
        activarSensorLuz();

    }
    @Override
    protected void onResume() {
        iniciar();
        super.onResume();
    }
    @Override
    protected void onPause() {
        detener();
        super.onPause();
    }

    private void activarSensorLuz(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(sensor==null){
            tempVal.setText("Tu dispositivo NO cuenta con el sensor luz.");
            finish();
        }
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                double valor = event.values[0];
                tempVal.setText("Luz: " + valor);

                if(valor<=2){
                    getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                } else if (valor<=50) {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                } else {
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }
    private void iniciar(){
        sensorManager.registerListener(sensorEventListener, sensor,2000 * 1000);
    }
    private void detener(){
        sensorManager.unregisterListener(sensorEventListener);
    }
}