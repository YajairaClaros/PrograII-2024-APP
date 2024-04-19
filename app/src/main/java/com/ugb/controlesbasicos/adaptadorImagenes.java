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
    ArrayList<peliculas> datosPeliculasArrayList;
    peliculas misPeliculas;
    LayoutInflater layoutInflater;
    public adaptadorImagenes(Context context, ArrayList<peliculas> datosPeliculasArrayList) {
        this.context = context;
        this.datosPeliculasArrayList = datosPeliculasArrayList;
    }
    @Override
    public int getCount() {
        return datosPeliculasArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return datosPeliculasArrayList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;//Long.parseLong(datosPeliculasArrayList.get(i).getIdPeli());
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_imagenes, viewGroup, false);
        try{
                misPeliculas = datosPeliculasArrayList.get(i);

            TextView tempVal = itemView.findViewById(R.id.lblTitulo);
            tempVal.setText(misPeliculas.getTitulo());

            tempVal = itemView.findViewById(R.id.lblSinopsis);
            tempVal.setText(misPeliculas.getSinopsis());

            tempVal = itemView.findViewById(R.id.lblDuracion);
            tempVal.setText(misPeliculas.getDuracion());

            ImageView imgView = itemView.findViewById(R.id.imgFoto);
            Bitmap imagenBitmap = BitmapFactory.decodeFile(misPeliculas.getFoto());
            imgView.setImageBitmap(imagenBitmap);
        }catch (Exception e){
            Toast.makeText(context, "Error en Adaptador Imagenes: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}