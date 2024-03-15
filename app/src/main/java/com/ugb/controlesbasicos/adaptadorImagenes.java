package com.ugb.controlesbasicos;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<amigos> datosAmigosArrayList;
    amigos misAmigos;
    LayoutInflater layoutInflater;

    @Override
    public int getCount() {
        return datosAmigosArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return datosAmigosArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(datosAmigosArrayList.get(position).getIdAmigo());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View itemView = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
      try {
          misAmigos = datosAmigosArrayList.get(position);
      }catch (Exception e){
          Toast.makeText(context, "Error en Adaptador Imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();

          TextView tempVal = itemView.findViewById(R.id.lblNombre);
          tempVal.setText(misAmigos.getNombre());

          tempVal = itemView.findViewById(R.id.lblTelefono);
          tempVal.setText(misAmigos.getTelefono());

          tempVal = itemView.findViewById(R.id.lblEmail);
          tempVal.setText(misAmigos.getEmail());
      }
      return itemView;
    }

    public adaptadorImagenes(Context context, ArrayList<amigos> datosAmigosArrayList) {
        this.context = context;
        this.datosAmigosArrayList = datosAmigosArrayList;
    }
}