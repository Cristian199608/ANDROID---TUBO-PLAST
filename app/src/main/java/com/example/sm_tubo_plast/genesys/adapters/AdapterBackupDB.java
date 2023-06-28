package com.example.sm_tubo_plast.genesys.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;


import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.io.File;
import java.util.HashMap;

public class AdapterBackupDB extends BaseAdapter {

    private File[] data;
    private HashMap<String, Uri> hashMap;
    private LayoutInflater inflater;

    MyCallback myCallback;
    public interface MyCallback{
        void cantidadSeleccion(int cantidad);
    }

    public AdapterBackupDB(Context context, File[] data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
        hashMap=new HashMap<>();
    }
    public void setMyCallback(MyCallback myCallback){
        this.myCallback=myCallback;
        notificarCantidadSeleccionados();
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_backup_db, parent, false);
        }
        ImageView imvType = view.findViewById(R.id.imvType);
        TextView tvNombreArchivo = view.findViewById(R.id.tvNombreArchivo);
        TextView tvFecha = view.findViewById(R.id.tvFecha);
        CheckBox checkBox = view.findViewById(R.id.checkBox);

        view.setVisibility(View.VISIBLE);
        tvFecha.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);

        File file= data[position];
        if (file.isDirectory()) {
            tvNombreArchivo.setText(file.getPath());
            imvType.setImageDrawable(parent.getContext().getResources().getDrawable(R.drawable.folder_48));
        } else {
            imvType.setImageDrawable(parent.getContext().getResources().getDrawable(R.drawable.ic_play_list_24));
            Uri selectedUri = Uri.fromFile(file);
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
            if (fileExtension.equalsIgnoreCase("db")) {
                String fechaCreada= VARIABLES.convertirFromLongToString(file.lastModified());
                tvNombreArchivo.setText(file.getName());
                tvFecha.setText(""+fechaCreada);
                tvFecha.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.VISIBLE);
            }else{
                view.setVisibility(View.GONE);
            }
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    Uri newUri = FileProvider.getUriForFile(parent.getContext(), GlobalVar.PACKAGE_NAME, file);
                    hashMap.put(file.getName(), newUri);
                }
                else {
                    hashMap.remove(file.getName());
                }
                notificarCantidadSeleccionados();
            }
        });
        checkBox.setChecked(hashMap.containsKey(file.getName()));
        if (position==(data.length-1)){
            view.setPadding(0,0,0,150);
        }else{
            view.setPadding(0,0,0,0);
        }
        return view;
    }

    private void notificarCantidadSeleccionados() {
        if(myCallback!=null) myCallback.cantidadSeleccion(hashMap.size());
    }

    public HashMap<String, Uri> GetArchivosSeleccionados(){
        return hashMap;
    }
}