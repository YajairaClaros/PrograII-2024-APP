package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_peliculas extends AppCompatActivity {
    Bundle parametros = new Bundle();
    FloatingActionButton btnAgregarAmigos;
    ListView lts;
    Cursor cAmigos;
    peliculas misPeliculas;
    DB db;
    final ArrayList<peliculas> alPeliculas=new ArrayList<peliculas>();
    final ArrayList<peliculas> alPeliculasCopy=new ArrayList<peliculas>();
    JSONArray datosJSON; //para los datos que vienen del servidor
    JSONObject jsonObject;
    obtenerDatosServidor datosServidor;
    detectarInternet di;
    int posicion=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_peliculas);

        db = new DB(getApplicationContext(),"", null, 1);
        btnAgregarAmigos = findViewById(R.id.fabAgregarPeliculas);
        btnAgregarAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parametros.putString("accion","nuevo");
                abrirActividad(parametros);
            }
        });
        try{
            di = new detectarInternet(getApplicationContext());
            if( di.hayConexionInternet() ){
                obtenerDatosPeliculasServidor();
            }else{//offline
                obtenerDatosPeliculas();
            }
        }catch (Exception e){
            mostrarMsg("Error al cargar lista peliculas: "+ e.getMessage());
        }
        buscarPeliculas();
    }
    private void obtenerDatosPeliculasServidor(){
        try {
            datosServidor = new obtenerDatosServidor();
            String data = datosServidor.execute().get();
            jsonObject = new JSONObject(data);
            datosJSON = jsonObject.getJSONArray("rows");
            mostrarDatosPeliculas();
        }catch (Exception e){
            mostrarMsg("Error al obtener datos del server: "+e.getMessage());
        }
    }
    private void mostrarDatosPeliculas(){
        try{
            if( datosJSON.length()>0 ){
                lts = findViewById(R.id.ltsPeliculas);
                alPeliculas.clear();
                alPeliculasCopy.clear();

                JSONObject misDatosJSONObject;
                for (int i=0; i<datosJSON.length();i++){
                    misDatosJSONObject = datosJSON.getJSONObject(i).getJSONObject("value");
                    misPeliculas = new peliculas(
                            misDatosJSONObject.getString("_id"),
                            misDatosJSONObject.getString("_rev"),
                            misDatosJSONObject.getString("idPeli"),
                            misDatosJSONObject.getString("titulo"),
                            misDatosJSONObject.getString("sinopsis"),
                            misDatosJSONObject.getString("duracion"),
                            misDatosJSONObject.getString("actor"),
                            misDatosJSONObject.getString("urlCompletaFoto")
                    );
                    alPeliculas.add(misPeliculas);
                }
                alPeliculasCopy.addAll(alPeliculas);
                adaptadorImagenes adImagenes = new adaptadorImagenes(lista_peliculas.this, alPeliculas);
                lts.setAdapter(adImagenes);
                registerForContextMenu(lts);
            }else{
                mostrarMsg("No hay datos que mostrar.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos: "+ e.getMessage());
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);

        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("nombre"));
        }catch (Exception e){
            mostrarMsg("Error al mostrar el menu: "+ e.getMessage());
        }
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try{
            if(item.getItemId()== R.id.mnxAgregar){
                parametros.putString("accion","nuevo");
                abrirActividad(parametros);
            }
            if(item.getItemId()==R.id.mnxModificar) {
                parametros.putString("accion", "modificar");
                parametros.putString("peliculas",datosJSON.getJSONObject(posicion).toString());
                abrirActividad(parametros);
            } else if (item.getItemId()==R.id.mnxEliminar) {
                eliminarPeliculas();
            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error al seleccionar una opcion del mennu: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarPeliculas(){
        try{
            AlertDialog.Builder confirmar = new AlertDialog.Builder(lista_peliculas.this);
            confirmar.setTitle("Estas seguro de eliminar a: ");
            confirmar.setMessage(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("titulo")); //1 es el nombre
            confirmar.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        String respuesta = db.administrar_peliculas("eliminar",
                                new String[]{"", "", datosJSON.getJSONObject(posicion).getJSONObject("value").getString("idPeli")});
                        if (respuesta.equals("ok")) {
                            mostrarMsg("Pelicula eliminado con exito");
                            obtenerDatosPeliculas();
                        } else {
                            mostrarMsg("Error al eliminar la pelicula: " + respuesta);
                        }
                    }catch (Exception e){
                        mostrarMsg("Error al intentar eliminar: "+ e.getMessage());
                    }
                }
            });
            confirmar.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            confirmar.create().show();
        }catch (Exception e){
            mostrarMsg("Error al eliminar pelicula: "+ e.getMessage());
        }
    }
    private void abrirActividad(Bundle parametros){
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }
    private void obtenerDatosPeliculas(){//offline
        try {
            cAmigos = db.consultar_peliculas();

            if( cAmigos.moveToFirst() ){
                datosJSON = new JSONArray();
                do{
                    jsonObject =new JSONObject();
                    JSONObject jsonObjectValue = new JSONObject();
                    jsonObject.put("_id", cAmigos.getString(0));
                    jsonObject.put("_rev", cAmigos.getString(1));
                    jsonObject.put("idAmigo", cAmigos.getString(2));
                    jsonObject.put("nombre", cAmigos.getString(3));
                    jsonObject.put("direccion", cAmigos.getString(4));
                    jsonObject.put("telefono", cAmigos.getString(5));
                    jsonObject.put("email", cAmigos.getString(6));
                    jsonObject.put("dui", cAmigos.getString(7));
                    jsonObject.put("urlCompletaFoto", cAmigos.getString(8));
                    jsonObjectValue.put("value", jsonObject);
                    datosJSON.put(jsonObjectValue);
                }while(cAmigos.moveToNext());
                mostrarDatosPeliculas();
            }else{
                mostrarMsg("No hay Datos de amigos que mostrar.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos: "+ e.getMessage());
        }
    }
    private void buscarPeliculas(){
        TextView tempVal;
        tempVal = findViewById(R.id.txtBuscarPeliculas);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    alPeliculas.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if( valor.length()<=0 ){
                        alPeliculas.addAll(alPeliculasCopy);
                    }else{
                        for (peliculas pelicula : alPeliculasCopy){
                            String titulo = pelicula.getTitulo();
                            String sinopsis = pelicula.getSinopsis();
                            String dur = pelicula.getDuracion();
                            String actor = pelicula.getActor();
                            if(titulo.toLowerCase().trim().contains(valor) ||
                                    sinopsis.toLowerCase().trim().contains(valor) ||
                                    dur.trim().contains(valor) ||
                                    actor.trim().toLowerCase().contains(valor)){
                                alPeliculas.add(pelicula);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alPeliculas);
                        lts.setAdapter(adImagenes);
                    }
                }catch (Exception e){
                    mostrarMsg("Error al buscar: "+ e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}