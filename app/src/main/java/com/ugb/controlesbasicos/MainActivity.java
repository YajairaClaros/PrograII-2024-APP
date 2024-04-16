package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView tempVal;
    Button btn;
    FloatingActionButton btnRegresar;
    String id="", rev="", idprod="",accion="nuevo";
    String urlCompletaFoto;
    Intent tomarFotoIntent;
    ImageView img;
    utilidades utls;
    DB db;
    detectarInternet di;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utls = new utilidades();
        db = new DB(getApplicationContext(), "", null, 1);
        di = new detectarInternet(getApplicationContext());
        btnRegresar = findViewById(R.id.fabListaproducto);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regresarLista = new Intent(getApplicationContext(), lista_tienda.class);
                startActivity(regresarLista);
            }
        });
        btn = findViewById(R.id.btnGuardarproducto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tempVal = findViewById(R.id.txtcodigo);
                    String codigo = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtdescripcion);
                    String descripcion = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtmarca);
                    String marca = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtpresentacion);
                    String presentacion = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtprecio);
                    String precio = tempVal.getText().toString();


                    String respuesta;
                    if (di.hayConexionInternet()) {
                        //obtener datos a enviar al servidor
                        JSONObject datostienda = new JSONObject();
                        if (accion.equals("modificar")) {
                            datostienda.put("_id", id);
                            datostienda.put("_rev", rev);
                        }
                        datostienda.put("idprod", idprod);
                        datostienda.put("codigo", codigo);
                        datostienda.put("descripcion", descripcion);
                        datostienda.put("marca", marca);
                        datostienda.put("presentacion", presentacion);
                        datostienda.put("precio", precio);
                        datostienda.put("urlCompletaFoto", urlCompletaFoto);
                        //enviamos los datos
                        respuesta = "";
                        enviarDatosServidor objGuardarDatosServidor = new enviarDatosServidor(getApplicationContext());
                        respuesta = objGuardarDatosServidor.execute(datostienda.toString()).get();
mostrarMsg(respuesta);
                        //comprobacion de la respuesta
                        JSONObject respuestaJSONObject = new JSONObject(respuesta);
                        if (respuestaJSONObject.getBoolean("ok")) {
                            id = respuestaJSONObject.getString("id");
                            rev = respuestaJSONObject.getString("rev");
                        } else {
                            respuesta = "Error al guardar en servidor: " + respuesta;
                        }
                    }
                    String[] datos = new String[]{id, rev, idprod, codigo, descripcion, marca, presentacion, precio, urlCompletaFoto};
                    respuesta = db.administrar_tienda(accion, datos);
                    if (respuesta.equals("ok")) {
                        mostrarMsg("Producto registrado con exito.");
                        listarProductos();
                    } else {
                        mostrarMsg("Error al intentar registrar el producto: " + respuesta);
                    }
                }catch (Exception e){
                    mostrarMsg("Error al guadar datos en el servidor o en SQLite: "+ e.getMessage());
                }
            }
        });
        img = findViewById(R.id.btnImg);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFotoproducto();
            }
        });
        mostrarDatosproductos();
    }
    private void sincronizar (){

    }
    private void tomarFotoproducto(){
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File fotoproducto = crearImagenProducto();
            if (fotoproducto!=null){
                Uri urifotoproducto = FileProvider.getUriForFile(MainActivity.this,
                        "com.ugb.controlesbasicos.fileprovider", fotoproducto);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, urifotoproducto);
                startActivityForResult(tomarFotoIntent, 1);
            }else{
                mostrarMsg("Error al crear la imagen");
            }
        }catch (Exception e){
            mostrarMsg("Error al crear la foto: " + e.getMessage());
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if( requestCode==1 && resultCode==RESULT_OK ){
                Bitmap imagenBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageBitmap(imagenBitmap);
            }else{
                mostrarMsg("Se cancelo la toma de la foto");
            }
        }catch (Exception e){
            mostrarMsg("Error al seleccionar la foto"+ e.getMessage());
        }
    }
    private File crearImagenProducto() throws Exception{
        String fechaHoraMs= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_" + fechaHoraMs + "";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if(dirAlmacenamiento.exists()==false){
            dirAlmacenamiento.mkdirs();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = image.getAbsolutePath();
        return image;
    }

    private void mostrarDatosproductos(){
        try{
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");

            if(accion.equals("modificar")){
                JSONObject jsonObject = new JSONObject(parametros.getString("productos")).getJSONObject("value");
                id = jsonObject.getString("_id");
                rev = jsonObject.getString("_rev");
                idprod = jsonObject.getString("idprod");

                tempVal = findViewById(R.id.txtcodigo);
                tempVal.setText(jsonObject.getString("codigo"));

                tempVal = findViewById(R.id.txtdescripcion);
                tempVal.setText(jsonObject.getString("descripcion"));

                tempVal = findViewById(R.id.txtmarca);
                tempVal.setText(jsonObject.getString("marca"));

                tempVal = findViewById(R.id.txtpresentacion);
                tempVal.setText(jsonObject.getString("presentacion"));

                tempVal = findViewById(R.id.txtprecio);
                tempVal.setText(jsonObject.getString("precio"));

                urlCompletaFoto = jsonObject.getString("urlCompletaFoto");
                Bitmap imagenBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageBitmap(imagenBitmap);
            }else{//nuevos registros
                idprod = utls.generarIdUnico();
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos amigos");
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void listarProductos(){
        Intent intent= new Intent(getApplicationContext(), lista_tienda.class);
        startActivity(intent);
    }
}









