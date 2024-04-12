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
    FloatingActionButton btnAgregarproductos;
    ListView lts;
    Cursor cTienda;
    tienda misproductos;
    DB db;
    final ArrayList<tienda> alproducto=new ArrayList<tienda>();
    final ArrayList<tienda> alproductosCopy=new ArrayList<tienda>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_amigos);
        btnAgregarproductos = findViewById(R.id.fabAgregarproductos);
        btnAgregarproductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parametros.putString("accion","nuevo");
                abrirActividad(parametros);
            }
        });
        obtenerDatosAmigos();
        buscarAmigos();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        cTienda.moveToPosition(info.position);
        menu.setHeaderTitle(cTienda.getString(1)); //1 es el nombre
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try{
            if (item.getItemId()==R.id.mnxAgregar) {

                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }else if (item.getItemId()==R.id.mnxModificar) {
                String[] amigos = {
                        cTienda.getString(0),
                        cTienda.getString(1),
                        cTienda.getString(2),
                        cTienda.getString(3),
                        cTienda.getString(4),
                        cTienda.getString(5),
                        cTienda.getString(6),
                };
                parametros.putString("accion", "modificar");
                parametros.putStringArray("tienda", amigos);
                abrirActividad(parametros);
            }else if (item.getItemId()==R.id.mnxEliminar){

                eliminarAmigos();

            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error al seleccionar una opcion del mennu: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarAmigos(){
        try{
            AlertDialog.Builder confirmar = new AlertDialog.Builder(lista_tienda.this);
            confirmar.setTitle("Estas seguro de eliminar a: ");
            confirmar.setMessage(cTienda.getString(1)); //1 es el nombre
            confirmar.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String respuesta = db.administrar_tienda("eliminar", new String[]{cTienda.getString(0)});
                    if(respuesta.equals("ok")){
                        mostrarMsg("eliminado con exito");
                        obtenerDatosAmigos();
                    }else{
                        mostrarMsg("Error al eliminar: "+ respuesta);
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
            mostrarMsg("Error al eliminar : "+ e.getMessage());
        }
    }
    private void abrirActividad(Bundle parametros){
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }
    private void obtenerDatosAmigos(){
        try {
            alproducto.clear();
            alproductosCopy.clear();

            db = new DB(lista_tienda.this, "", null, 1);
            cTienda = db.consultar_tienda();

            if( cTienda.moveToFirst() ){
                lts = findViewById(R.id.ltstienda);
                do{
                    misproductos = new tienda(
                            cTienda.getString(0),
                            cTienda.getString(1),
                            cTienda.getString(2),
                            cTienda.getString(3),
                            cTienda.getString(4),
                            cTienda.getString(5),
                            cTienda.getString(6)
                    );
                    alproducto.add(misproductos);
                }while(cTienda.moveToNext());
                alproductosCopy.addAll(alproducto);

                adaptadorImagenes adImagenes = new adaptadorImagenes(lista_tienda.this, alproducto);
                lts.setAdapter(adImagenes);

                registerForContextMenu(lts);
            }else{
                mostrarMsg("No hay Datos de tienda que mostrar.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos: "+ e.getMessage());
        }
    }
    private void buscarAmigos(){
        TextView tempVal;
        tempVal = findViewById(R.id.txtBuscarprod);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    alproducto.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if( valor.length()<=0 ){
                        alproducto.addAll(alproductosCopy);
                    }else{
                        for (tienda tienda : alproductosCopy){
                            String codigo = tienda.getCodigo();
                            String descripcion = tienda.getDescripcion();
                            String marca = tienda.getMarca();
                            String presentacion = tienda.getPresentacion();
                            String precio = tienda.getPrecio();
                            if(codigo.toLowerCase().trim().contains(valor) ||
                                    descripcion.toLowerCase().trim().contains(valor) ||
                                    marca.trim().contains(valor) ||
                                    presentacion.trim().toLowerCase().contains(valor) ||
                                    precio.trim().contains(valor)){
                                alproducto.add(tienda);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alproducto);
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