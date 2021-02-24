package com.example.fuerzaventaschema.genesys.fuerza_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fuerzaventaschema.R;


import java.util.ArrayList;
import java.util.HashMap;

import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaCuotaVendedorActivity extends AppCompatActivity {

    private DBclasses dbclass;
    private ListView lista_cuotas;
    private ArrayList<HashMap<String, String>> cuotas;
    private String codven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cuota_vendedor);


        dbclass = new DBclasses(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        codven = bundle.getString("codven");

        lista_cuotas = (ListView)findViewById(R.id.cuota_vendedor_lista);

        cuotas = dbclass.getCuotasxVendedor();

        ListaCuotasAdapter adaptador = new ListaCuotasAdapter(ListaCuotaVendedorActivity.this, cuotas);

        lista_cuotas.setAdapter(adaptador);


        lista_cuotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position,
                                    long id) {

                TextView txt_sec = (TextView)lista_cuotas.getAdapter().getView(position, null, null).findViewById(R.id.cuota_vendedor_lista_item_secuencia);

                Intent intent = new Intent(ListaCuotaVendedorActivity.this, CuotaVendedorActivity.class);
                intent.putExtra("SECUENCIA", txt_sec.getText().toString().trim());
                startActivity(intent);
            }
        });

    }


    private class ListaCuotasAdapter extends BaseAdapter {

        private Activity activity;
        private ArrayList<HashMap<String, String>> data;

        public ListaCuotasAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data=d;
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View item=convertView;
            ViewHolder holder;

            if(item==null){
                LayoutInflater inflater=activity.getLayoutInflater();
                item=inflater.inflate(R.layout.cuota_vendedor_lista_item, null);

                holder=new ViewHolder();

                holder.nomcuota=(TextView)item.findViewById(R.id.cuota_vendedor_lista_item_nomcuota);
                holder.secuencia=(TextView)item.findViewById(R.id.cuota_vendedor_lista_item_secuencia);

                item.setTag(holder);

            }else{

                holder=(ViewHolder)item.getTag();

            }

            HashMap<String, String> cuota = new HashMap<String, String>();
            cuota = data.get(position);

            holder.nomcuota.setText(cuota.get("NOMCUOTA"));
            holder.secuencia.setText(cuota.get("SECUENCIA"));

            return item;
        }


        public class ViewHolder{
            TextView nomcuota;
            TextView secuencia;
        }

    }

}
