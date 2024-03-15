package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class lista_amigos extends AppCompatActivity {
    FloatingActionButton btnAgregarAmigos;
    ListView lts;
    Cursor cAmigos;
    amigos misAmigos;
    DB db;
    final ArrayList<amigos> alAmigos = new ArrayList<amigos>();
    final ArrayList<amigos> alAmigosCopy = new ArrayList<amigos>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_amigos);

        btnAgregarAmigos = findViewById(R.id.fabAgregarAmigos);
        btnAgregarAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividad();
            }
        });
        obtenerDatosAmigos();
        buscarAmigos();
    }
    private void abrirActividad(){
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(abrirActividad);
    }
    private void obtenerDatosAmigos(){
        try {
            alAmigos.clear();
            alAmigosCopy.clear();

            db = new DB(lista_amigos.this, "", null, 1);
            cAmigos = db.consultar_amigos();

            if (cAmigos.moveToFirst() ){
                lts = findViewById(R.id.ltsAmigos);
                do {
                    misAmigos = new amigos(
                            cAmigos.getString(0), //idAmigos
                            cAmigos.getString(1), //idNombre
                            cAmigos.getString(2), //idDireccion
                            cAmigos.getString(3), //idTelefono
                            cAmigos.getString(4), //idEmail
                            cAmigos.getString(5) //idDui
                    );
                    alAmigos.add(misAmigos);
                }while (cAmigos.moveToFirst());
                alAmigosCopy.addAll(alAmigos);

                adaptadorImagenes adImagenes = new adaptadorImagenes(lista_amigos.this, alAmigos);
                lts.setAdapter(adImagenes);

                registerForContextMenu(lts);
            }else {
                mostrarMsg("NO HAY DATOS DE AMIGOS QUE MOSTRAR.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos" + e.getMessage());
        }
    }
    private void buscarAmigos(){
        TextView tempVal;
        tempVal = findViewById(R.id.txtBuscarAmigos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    alAmigos.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if (valor.length()<=0 ){
                        alAmigos.addAll(alAmigosCopy);
                    }else {
                        for (amigos amigo: alAmigosCopy){
                            String nombre = amigo.getNombre();
                            String direccion = amigo.getDireccion();
                            String tel = amigo.getTelefono();
                            String email = amigo.getEmail();
                            String dui = amigo.getDui();
                            if (nombre.toLowerCase().trim().contains(valor) ||
                                direccion.toLowerCase().trim().contains(valor) ||
                                tel.trim().contains(valor) ||
                                email.trim().toLowerCase().contains(valor) ||
                                dui.trim().contains(valor)){
                                alAmigos.add(amigo);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alAmigos);
                        lts.setAdapter(adImagenes);
                    }
                }catch (Exception e){
                    mostrarMsg("Error al buscar" + e.getMessage());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}