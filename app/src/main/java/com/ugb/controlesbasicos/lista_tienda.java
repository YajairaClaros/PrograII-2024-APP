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

import java.util.ArrayList;

public class lista_tienda extends AppCompatActivity {
    Bundle parametros = new Bundle();
    FloatingActionButton btnAgregarProductos;
    ListView lts;
    Cursor cTienda;
    tienda misProductos;
    DB db;
    final ArrayList<tienda> alProducto = new ArrayList<tienda>();
    final ArrayList<tienda> alProductoCopy = new ArrayList<tienda>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_amigos);

        btnAgregarProductos = findViewById(R.id.fabAgregarProductos);
        btnAgregarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }
        });
        obtenerDatosAmigos();
        buscarTienda();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        cTienda.moveToPosition(info.position);
        menu.setHeaderTitle(cTienda.getString(1)); // 1 es el nombre
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try{
            if(item.getItemId()== R.id.mnxAgregar){
                    parametros.putString("accion","nuevo");
                    abrirActividad(parametros);
            }
            if(item.getItemId()==R.id.mnxModificar) {
                String[] amigos = {
                        cTienda.getString(0), //idAmigo
                        cTienda.getString(1), //nombre
                        cTienda.getString(2), //direccion
                        cTienda.getString(3), //tel
                        cTienda.getString(4), //email
                        cTienda.getString(5), //dui
                        cTienda.getString(6), //foto
                };
                parametros.putString("accion", "modificar");
                parametros.putStringArray("amigos", amigos);
                abrirActividad(parametros);
            } else if (item.getItemId()==R.id.mnxEliminar) {
                eliminarAmigos();
            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error al seleccionar una opcion del mennu: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarAmigos(){
        try {
            AlertDialog.Builder confirmar = new AlertDialog.Builder(lista_tienda.this);
            confirmar.setTitle("Estas segura de eliminar a: ");
            confirmar.setMessage(cTienda.getString(1)); // 1 es el nombre
            confirmar.create().show();
            confirmar.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String respuesta = db.administrar_tienda("eliminar", new String[]{cTienda.getString(0)});// 0 es el idAmigo
                    if (respuesta.equals("ok")){
                        mostrarMsg("Eliminado con exito");
                        obtenerDatosAmigos();
                    }else{
                        mostrarMsg("Error al eliminar: "+ respuesta);
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
            mostrarMsg("Error al eliminar: "+ e.getMessage());
        }
    }
    private void abrirActividad(Bundle parametros){
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }
    private void obtenerDatosAmigos(){
        try {
            alProducto.clear();
            alProductoCopy.clear();

            db = new DB(lista_tienda.this, "", null, 1);
            cTienda = db.consultar_tienda();

            if (cTienda.moveToFirst() ){
                lts = findViewById(R.id.ltsTienda);
                do {
                    misProductos = new tienda(
                            cTienda.getString(0), //idAmigos
                            cTienda.getString(1), //idNombre
                            cTienda.getString(2), //idDireccion
                            cTienda.getString(3), //idTelefono
                            cTienda.getString(4), //idEmail
                            cTienda.getString(5),//idDui
                            cTienda.getString(6)// foto
                    );
                    alProducto.add(misProductos);
                }while (cTienda.moveToNext());
                alProductoCopy.addAll(alProducto);

                adaptadorImagenes adImagenes = new adaptadorImagenes(lista_tienda.this, alProducto);
                lts.setAdapter(adImagenes);

                registerForContextMenu(lts);
            }else {
                mostrarMsg("NO HAY DATOS DE AMIGOS QUE MOSTRAR.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos" + e.getMessage());
        }
    }
    private void buscarTienda(){
        TextView tempVal;
        tempVal = findViewById(R.id.txtBuscarProductos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    alProducto.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if (valor.length()<=0 ){
                        alProducto.addAll(alProductoCopy);
                    }else {
                        for (tienda tienda: alProductoCopy){
                            String codigo = tienda.getCodigo();
                            String descripcion = tienda.getDescripcion();
                            String marca = tienda.getMarca();
                            String presentacion = tienda.getPresentacion();
                            String precio = tienda.getPrecio();
                            if (codigo.toLowerCase().trim().contains(valor) ||
                                descripcion.toLowerCase().trim().contains(valor) ||
                                marca.trim().contains(valor) ||
                                presentacion.trim().toLowerCase().contains(valor) ||
                                precio.trim().contains(valor)){
                                alProducto.add(tienda);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alProducto);
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