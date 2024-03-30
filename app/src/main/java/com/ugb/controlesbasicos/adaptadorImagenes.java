package com.ugb.controlesbasicos;

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
    ArrayList<tienda> datosTiendaArrayList;
    tienda misAmigos;
    LayoutInflater layoutInflater;
    public adaptadorImagenes(Context context, ArrayList<tienda> datosAmigosArrayList) {
        this.context = context;
        this.datosTiendaArrayList = datosAmigosArrayList;
    }
    @Override
    public int getCount() {
        return datosTiendaArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return datosTiendaArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return Long.parseLong(datosTiendaArrayList.get(position).getIdProd());
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View itemView = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
      try {
          misAmigos = datosTiendaArrayList.get(position);

          TextView tempVal = itemView.findViewById(R.id.lblpresentacion);
          tempVal.setText(misAmigos.getPresentacion());

          tempVal = itemView.findViewById(R.id.lbldescripcion);
          tempVal.setText(misAmigos.getDescripcion());

          tempVal = itemView.findViewById(R.id.lblprecio);
          tempVal.setText(misAmigos.getPrecio());

          ImageView imgView = itemView.findViewById(R.id.imgFoto);
          Bitmap imagenBitmap = BitmapFactory.decodeFile(misAmigos.getFoto());
          imgView.setImageBitmap(imagenBitmap);

      }catch (Exception e){
          Toast.makeText(context, "Error en Adaptador Imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
      }
      return itemView;
    }
}
