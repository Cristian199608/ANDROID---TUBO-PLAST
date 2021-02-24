package com.example.fuerzaventaschema.genesys.adapters;

import java.util.ArrayList;
import java.util.HashMap;




import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.ProductosActivity;
import com.example.fuerzaventaschema.genesys.service.ImageLoader;

public class ProductosAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    
    public ProductosAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
    	 activity = a;
         data=d;
         inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         imageLoader=new ImageLoader(activity.getApplicationContext());
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
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.producto_item, null);

        TextView title = (TextView)vi.findViewById(R.id.productos_tv_nombre); // title
        TextView artist = (TextView)vi.findViewById(R.id.productos_tv_descripcion); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.productos_tv_precio); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.productos_imagen); // thumb image
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
       
        title.setText(song.get(ProductosActivity.KEY_NOMBRE));
        artist.setText(song.get(ProductosActivity.KEY_DESCRIPCION));
        duration.setText(song.get(ProductosActivity.KEY_PRECIO));
        imageLoader.DisplayImage(song.get(ProductosActivity.KEY_FOTO), activity, thumb_image);
        return vi;
    }
}