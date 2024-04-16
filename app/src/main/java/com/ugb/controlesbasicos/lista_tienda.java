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

public class lista_tienda extends AppCompatActivity {
    Bundle parametros = new Bundle();
    FloatingActionButton btnAgregarProductos;
    ListView lts;
    Cursor cTienda;
    tienda misproductos;
    DB db;
    final ArrayList<tienda> alproductos = new ArrayList<tienda>();
    final ArrayList<tienda> alproductosCopy = new ArrayList<tienda>();
    JSONArray datosJSON;
    JSONObject jsonObject;
    obtenerDatosServidor datosServidor;
    detectarInternet di;
    int posicion = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_tienda);

        db = new DB(lista_tienda.this, "", null, 1);
        btnAgregarProductos = findViewById(R.id.fabAgregarproductos);
        btnAgregarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }
        });
        try {
            di = new detectarInternet(getApplicationContext());
            if (di.hayConexionInternet() ){ sincronizar();
                obtenerDatosServidor();
            }else{//offline
                obtenerDatosProductos();
            }
        }catch (Exception e){
            mostrarMsg("Error al cargar lista productos" + e.getMessage());
        }
        buscarProductos();
    }

    private void obtenerDatosServidor(){
        try {
            datosServidor = new obtenerDatosServidor();
            String data = datosServidor.execute().get();
            mostrarMsg(data);
            jsonObject = new JSONObject(data);
            datosJSON = jsonObject.getJSONArray("rows");
            mostrarDatosProductos();
        }catch (Exception e){
            mostrarMsg("Error al obtener datos del no jodas: "+e.getMessage());
        }
    }
    private void sincronizar() {
        if (di.hayConexionInternet() == true);{
            mostrarDatosProductos();
        }
    }

    private void mostrarDatosProductos(){
        try {
            if ( datosJSON.length()>0){
                lts = findViewById(R.id.ltsproductos);
                alproductos.clear();
                alproductosCopy.clear();

                JSONObject misDatosJSONObject;
                for (int i = 0; i < datosJSON.length(); i++){
                    misDatosJSONObject = datosJSON.getJSONObject(i).getJSONObject("value");
                    misproductos = new tienda(
                            misDatosJSONObject.getString("_id"),
                            misDatosJSONObject.getString("_rev"),
                            misDatosJSONObject.getString("idprod"),
                            misDatosJSONObject.getString("codigo"),
                            misDatosJSONObject.getString("descripcion"),
                            misDatosJSONObject.getString("marca"),
                            misDatosJSONObject.getString("presentacion"),
                            misDatosJSONObject.getString("precio"),
                            misDatosJSONObject.getString("urlCompletaFoto")
                    );
                    alproductos.add(misproductos);
                }
                alproductosCopy.addAll(alproductos);
                adaptadorImagenes adImagenes = new adaptadorImagenes(lista_tienda.this, alproductos);
                lts.setAdapter(adImagenes);
                registerForContextMenu(lts);
            }else {
                mostrarMsg("No hay datos que mostrar.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos: " + e.getMessage());
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
                parametros.putString("productos",datosJSON.getJSONObject(posicion).toString());
                abrirActividad(parametros);
            } else if (item.getItemId()==R.id.mnxEliminar) {
                eliminarProductos();
            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error al seleccionar una opcion del menu: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarProductos(){
        try {
            AlertDialog.Builder confirmar = new AlertDialog.Builder(lista_tienda.this);
            confirmar.setTitle("Estas segura de eliminar a: ");
            confirmar.setMessage(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("codigo")); // 1 es el nombre
            confirmar.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        String respuesta = db.administrar_tienda("eliminar",
                                new String[]{"", "", datosJSON.getJSONObject(posicion).getJSONObject("value").getString("idprod")});
                        if (respuesta.equals("ok")) {
                            mostrarMsg("Producto eliminado con exito");
                            obtenerDatosProductos();
                        } else {
                            mostrarMsg("Error al eliminar el producto: " + respuesta);
                        }
                    }catch (Exception e){
                        mostrarMsg("Error al intentar eliminar: "+ e.getMessage());
                    }
                }
            });
            confirmar.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            confirmar.create().show();
        }catch (Exception e){
            mostrarMsg("Error al eliminar producto: "+ e.getMessage());
        }
    }
    private void abrirActividad(Bundle parametros){
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }
    private void obtenerDatosProductos(){//offline
        try {

            cTienda = db.consultar_amigos();

            if (cTienda.moveToFirst() ){
                datosJSON = new JSONArray();
                do {
                    jsonObject = new JSONObject();
                    JSONObject jsonObjectValue = new JSONObject();
                    jsonObject.put("_id", cTienda.getString(0));
                    jsonObject.put("_rev", cTienda.getString(1));
                    jsonObject.put("idprod", cTienda.getString(2));
                    jsonObject.put("codigo", cTienda.getString(3));
                    jsonObject.put("descripcion", cTienda.getString(4));
                    jsonObject.put("marca", cTienda.getString(5));
                    jsonObject.put("presentacion", cTienda.getString(6));
                    jsonObject.put("precio", cTienda.getString(7));
                    jsonObject.put("urlCompletaFoto", cTienda.getString(8));
                    jsonObject.put("value", jsonObject);
                    datosJSON.put(jsonObjectValue);
                }while (cTienda.moveToFirst());
            }else {
                mostrarMsg("NO HAY DATOS DE PRODUCTOS QUE MOSTRAR.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos" + e.getMessage());
        }
    }
    private void buscarProductos(){
        TextView tempVal;
        tempVal = findViewById(R.id.txtBuscarproducto);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    alproductos.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if (valor.length()<=0 ){
                        alproductos.addAll(alproductosCopy);
                    }else {
                        for (tienda producto: alproductosCopy){
                            String codigo = producto.getCodigo();
                            String descripcion = producto.getDescripcion();
                            String marca = producto.getMarca();
                            String presentacion = producto.getPresentacion();
                            String precio = producto.getPrecio();
                            if (codigo.toLowerCase().trim().contains(valor) ||
                                descripcion.toLowerCase().trim().contains(valor) ||
                                marca.trim().contains(valor) ||
                                presentacion.trim().toLowerCase().contains(valor) ||
                                precio.trim().contains(valor)){
                                alproductos.add(producto);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alproductos);
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