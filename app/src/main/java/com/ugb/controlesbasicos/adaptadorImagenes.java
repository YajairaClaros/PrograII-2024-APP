package com.ugb.controlesbasicos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<tienda> datosProductosArrayList;
    tienda misproductos;
    LayoutInflater layoutInflater;
    public adaptadorImagenes(Context context, ArrayList<tienda> datosAmigosArrayList) {
        this.context = context;
        this.datosProductosArrayList = datosAmigosArrayList;
    }
    @Override
    public int getCount() {
        return datosProductosArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return datosProductosArrayList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i; //Long.parseLong(datosAmigosArrayList.get(i).getIdAmigo());
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_imagenes, viewGroup, false);
        try{
            misproductos = datosProductosArrayList.get(i);

            TextView tempVal = itemView.findViewById(R.id.lblCodigo);
            tempVal.setText(misproductos.getCodigo());

            tempVal = itemView.findViewById(R.id.lblMarca);
            tempVal.setText(misproductos.getMarca());

            tempVal = itemView.findViewById(R.id.lblPresentacion);
            tempVal.setText(misproductos.getPresentacion());

            tempVal = itemView.findViewById(R.id.lblPrecio);
            tempVal.setText(misproductos.getPrecio());

            ImageView imgView = itemView.findViewById(R.id.imgFoto);
            Bitmap imagenBitmap = BitmapFactory.decodeFile(misproductos.getFoto());
            imgView.setImageBitmap(imagenBitmap);
        }catch (Exception e){
            Toast.makeText(context, "Error en Adaptador Imagenes: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}