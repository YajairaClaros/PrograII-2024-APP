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
    ArrayList<tienda> datostiendaArrayList;
    tienda misAmigos;
    LayoutInflater layoutInflater;
    public adaptadorImagenes(Context context, ArrayList<tienda> datostiendaArrayList) {
        this.context = context;
        this.datostiendaArrayList = datostiendaArrayList;
    }
    @Override
    public int getCount() {
        return datostiendaArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return datostiendaArrayList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return Long.parseLong(datostiendaArrayList.get(i).getIdprod());
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_imagenes, viewGroup, false);
        try{
            misAmigos = datostiendaArrayList.get(i);

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
            Toast.makeText(context, "Error en Adaptador Imagenes: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}
