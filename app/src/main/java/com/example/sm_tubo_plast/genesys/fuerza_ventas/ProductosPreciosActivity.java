package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.datatypes.DBFamilia;
import com.example.sm_tubo_plast.genesys.datatypes.DBProductos;
import com.example.sm_tubo_plast.genesys.datatypes.DBSub_Familia;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.datatypes.ListaPrecios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class ProductosPreciosActivity extends AppCompatActivity {

    DBclasses obj;
    static final String KEY_P1="precio1";
    static final String KEY_P2="precio2";
    static final String KEY_P3="precio3";
    static final String KEY_P4="precio4";
    static final String KEY_P5="precio5";
    static final String KEY_P6="precio6";
    ArrayList<HashMap<String, String>> lista_precios=new ArrayList<HashMap<String,String>>();
    ListView myListPrecios;
    ListView myListProductos;
    ArrayList<String> list ;
    ArrayList<DBProductos> al_productos;
    ArrayList<ListaPrecios> al_precios;
    ArrayList<DBFamilia> listfamilia ;
    View clickSource;
    View touchSource;
    private SimpleCursorAdapter dataAdapter;
    int offset = 0;
    int offset2 = 0;
    Spinner spn_familia, spn_subfamilia;
    public String cod_subFamilia="";
    String[] familia_desc ;
    String[] familia_codigo;
    int id_familia=0;
    DBclasses dbhelper;
    ArrayList<DBSub_Familia> al_subfamilias ;
    String[] subf_desc;
    String[] subf_codigo;
    int id_subFamilia=0;
    String art="";

    private ImageButton btnBuscar;
    private TextView inputSearch;
    private String pistaBusqueda;
    String vacio="x,xxxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_precios);


        list = new ArrayList<String>();
        obj = new DBclasses(getApplicationContext());
        al_productos = new ArrayList<DBProductos>();
        al_precios = new ArrayList<ListaPrecios>();
        dbhelper=new DBclasses(getApplicationContext());

        spn_familia=(Spinner)findViewById(R.id.productolyt_spnFiltroFamilia);
        spn_subfamilia = (Spinner)findViewById(R.id.productolyt_spnFiltroSubfamilia);

        inputSearch=(TextView)findViewById(R.id.edt_busqueda);
        btnBuscar=(ImageButton)findViewById(R.id.btn_buscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(inputSearch.getText().toString()== null || inputSearch.getText().toString() ==""){
                    pistaBusqueda=null;
                }else { pistaBusqueda=inputSearch.getText().toString(); }

                lista_precios.clear();
                list.clear();

                myListProductos.setAdapter(new MyCustomAdapterProductos(ProductosPreciosActivity.this, list));
                new asyncPrecios().execute("");

            }
        });

        cargarFamilia();
        spn_familia.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String text = spn_familia.getSelectedItem().toString();

                pistaBusqueda=null;

                for(int i=0; i<familia_desc.length; i++){
                    if(text.equals(familia_desc[i])){
                        id_familia=i;
                    }
                }
                mostrarSubfamilias(familia_codigo[id_familia]);
            }



            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spn_subfamilia.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String text = spn_subfamilia.getSelectedItem().toString();
                pistaBusqueda=null;
                int id_subFamilia=0;
                for(int i=0; i<subf_desc.length; i++){
                    if(text.equals(subf_desc[i])){
                        id_subFamilia=i;
                    }
                }

                Log.w("COD_SUBFAMILIA",subf_codigo[id_subFamilia]+ "codsufamilia");
                cod_subFamilia =subf_codigo[id_subFamilia];

                if(art==cod_subFamilia){

                }else{
                    Log.w("SPN_SUBFAMILIA","1 intento");
                    lista_precios.clear();
                    art=cod_subFamilia;
                    myListProductos.setAdapter(new MyCustomAdapterProductos(ProductosPreciosActivity.this, list));
                    new asyncPrecios().execute("");}
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        //cargarProductos();
        myListPrecios = (ListView)findViewById(R.id.productos_precios_lv_productos);

        // new asyncPrecios().execute("");
        //displayPrecios();
        //show the ListView on the screen
        // The adapter MyCustomAdapter is responsible for maintaining the data backing this list and for producing
        // a view to represent an item in that data set.

        myListProductos = (ListView)findViewById(R.id.productos_lista_lv_productos);
        //    displayProductos();
        //show the ListView on the screen
        // The adapter MyCustomAdapter is responsible for maintaining the data backing this list and for producing
        // a view to represent an item in that data set.
        //   myListProductos.setAdapter(new MyCustomAdapterProductos(ProductosPreciosActivity.this, list));

        myListProductos.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    myListPrecios.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }

                return false;
            }
        });



        myListProductos.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if(parent == clickSource) {
                    // Do something with the ListView was clicked
                }
            }
        });


        myListProductos.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                // TODO Auto-generated method stub
                if(scrollState == 0)
                    Log.i("a", "scrolling stopped...");

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)  {
                // TODO Auto-generated method stub
                //	myListPrecios.setSelection(firstVisibleItem);
                if(view == clickSource)
                    myListPrecios.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
            }
        });

        myListPrecios.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    myListProductos.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }

                return false;
            }
        });



        myListPrecios.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if(parent == clickSource) {
                    // Do something with the ListView was clicked
                }
            }
        });


        myListPrecios.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                // TODO Auto-generated method stub
                if(scrollState == 0)
                    Log.i("a", "scrolling stopped...");

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)  {
                // TODO Auto-generated method stub
                //	myListPrecios.setSelection(firstVisibleItem);
                if(view == clickSource)
                    myListProductos.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset2);
            }
        });

        myListPrecios.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                myListProductos.setSelection(firstVisibleItem);

            }
        });
    }

	/*
	private void displayPrecios() {
		// TODO Auto-generated method stub
		Cursor cursor = obj.getProductosCursor();

		al_productos= obj.getCodpro();
		Iterator<DBProductos> it= al_productos.iterator();
		while ( it.hasNext() ) {
			  Object objeto = it.next();
			  DBProductos dbproductos = (DBProductos)objeto;
	          al_precios=obj.getPreciosxCodpro(dbproductos.getCodpro());
			    Iterator<ListaPrecios> it2= al_precios.iterator();
			    int j=0;
			    HashMap<String, String> map = new HashMap<String, String>();
				while ( it2.hasNext() ) {
					j++;
					  Object objeto2 = it2.next();
					  ListaPrecios dbprecios = (ListaPrecios)objeto2;

						map.put("precio"+j,""+ dbprecios.getPrecio1());
				if(j==6){
					j=0;
					lista_precios.add(map);
				}


				}


		}


		  String[] columns = new String[] {
				  "despro"
		  };

		  // the XML defined views which the data will be bound to
		  int[] to = new int[] {
				  R.id.productos_lista_item_tv_nombre
		  };

		  // create the adapter using the cursor pointing to the desired data
		  //as well as the layout information
		  dataAdapter = new SimpleCursorAdapter(
		    this, R.layout.producto_lista_item,
		    cursor,
		    columns,
		    to,
		    0);


		  myListProductos.setAdapter(dataAdapter);

	}

	private void displayProductos() {
		Cursor cursor = obj.getProductosCursor();
		  // The desired columns to be bound
		  String[] columns = new String[] {
				  "despro"
		  };

		  // the XML defined views which the data will be bound to
		  int[] to = new int[] {
				  R.id.productos_lista_item_tv_nombre
		  };


		  // create the adapter using the cursor pointing to the desired data
		  //as well as the layout information
		  dataAdapter = new SimpleCursorAdapter(
		    this, R.layout.producto_lista_item,
		    cursor,
		    columns,
		    to,
		    0);


		  myListProductos.setAdapter(dataAdapter);

	} */

    public void cargarPrecios(){

        al_productos= obj.getCodproxSubFamilia(cod_subFamilia,pistaBusqueda);
        Iterator<DBProductos> it= al_productos.iterator();
        while ( it.hasNext() ) {
            Object objeto = it.next();
            DBProductos dbproductos = (DBProductos)objeto;

            //map.put(KEY_CODCLI,obtenerPersona[i].getCodcli());

            al_precios=obj.getPreciosxCodpro(dbproductos.getCodpro());

            Iterator<ListaPrecios> it2= al_precios.iterator();
            int j=0;
            HashMap<String, String> map = new HashMap<String, String>();
            while ( it2.hasNext() ) {
                j++;
                Object objeto2 = it2.next();


                ListaPrecios dbprecios = (ListaPrecios)objeto2;
                map.put("precio"+j,""+ dbprecios.getPrecio1());
                Log.d("item guardado","precio"+j+"--"+ dbprecios.getPrecio1());
                if(!(it2.hasNext())){
                    Log.d("itm ",""+j);
                    j=0;

                    lista_precios.add(map);
                    Log.d("tama�o lista 1",""+lista_precios.size());


                }


            }


        }

    }

    public class MyCustomAdapter extends BaseAdapter {
        private Context activity;
        private ArrayList<HashMap<String, String>> data;
        private LayoutInflater inflater;
        private int[] colors = new int[] { 0x30FF0000, 0x300000FF};

        public MyCustomAdapter(Context a, ArrayList<HashMap<String, String>> d){
            Log.w("CUstomAdapter ", "cantidad" + lista_precios.size());
            activity = a;
            data=lista_precios;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Log.w("CUstomAdapter ", "cantidad" + data.size());
        }

        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }



        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }


        ViewHolder holder;
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            View view=convertView;


            if(view==null){
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.productos_precios_item, null);


                holder.precio1 = (TextView)view.findViewById(R.id.productos_precios_item1);
                holder.precio2 = (TextView)view.findViewById(R.id.productos_precios_item2);
                holder.precio3 = (TextView)view.findViewById(R.id.productos_precios_item3);
                holder.precio4 = (TextView)view.findViewById(R.id.productos_precios_item4);
                holder.precio5 = (TextView)view.findViewById(R.id.productos_precios_item5);
                holder.precio6 = (TextView)view.findViewById(R.id.productos_precios_item6);

                // the setTag is used to store the data within this view
                view.setTag(holder);

            }
            else {
                // the getTag returns the viewHolder object set as a tag to the view
                holder = (ViewHolder)view.getTag();
            }

            HashMap<String, String> song = new HashMap<String, String>();
            song = data.get(position);

            Log.d(KEY_P1,""+song.get(ProductosPreciosActivity.KEY_P1));
            Log.d(KEY_P2,""+song.get(ProductosPreciosActivity.KEY_P2));
            Log.d(KEY_P3,""+song.get(ProductosPreciosActivity.KEY_P3));
            Log.d(KEY_P4,""+song.get(ProductosPreciosActivity.KEY_P4));
            Log.d(KEY_P5,""+song.get(ProductosPreciosActivity.KEY_P5));
            Log.d(KEY_P6,""+song.get(ProductosPreciosActivity.KEY_P6));

            holder.precio1.setText(song.get(ProductosPreciosActivity.KEY_P1));

            if(song.get(ProductosPreciosActivity.KEY_P2) != null){
                holder.precio2.setText(song.get(ProductosPreciosActivity.KEY_P2));
            }else {
                holder.precio2.setText(vacio);
            }

            if(song.get(ProductosPreciosActivity.KEY_P3) != null){
                holder.precio3.setText(song.get(ProductosPreciosActivity.KEY_P3));
            }else {
                holder.precio3.setText(vacio);

            }

            if(song.get(ProductosPreciosActivity.KEY_P4) != null){
                holder.precio4.setText(song.get(ProductosPreciosActivity.KEY_P4));
            }else {
                holder.precio4.setText(vacio);
            }

            if(song.get(ProductosPreciosActivity.KEY_P5) != null){
                holder.precio5.setText(song.get(ProductosPreciosActivity.KEY_P5));
            }else {
                holder.precio5.setText(vacio);
            }

            if(song.get(ProductosPreciosActivity.KEY_P6) != null){
                holder.precio6.setText(song.get(ProductosPreciosActivity.KEY_P6));
            }else {
                holder.precio6.setText(vacio);
            }









            int colorPos = position % colors.length;
            if (colorPos == 1) {
                view.setBackgroundColor(Color.argb(250, 255, 255, 255));
            } else {
                view.setBackgroundColor(Color.argb(250, 224, 243, 250));
            }

            return view;
            // create a ViewHolder reference


        }
        public  class ViewHolder {

            protected TextView precio1;
            protected TextView precio2;
            protected TextView precio3;
            protected TextView precio4;
            protected TextView precio5;
            protected TextView precio6;

        }

        /**
         * Static class used to avoid the calling of "findViewById" every time the getView() method is called,
         * because this can impact to your application performance when your list is too big. The class is static so it
         * cache all the things inside once it's created.
         */


    }


    public void cargarProductos(){
        al_productos= obj.getProductos();
        Iterator<DBProductos> it= al_productos.iterator();
        while ( it.hasNext() ) {
            Object objeto = it.next();
            DBProductos dbproductos = (DBProductos)objeto;
            list.add(""+dbproductos.getDespro());
        }

    }

    public class MyCustomAdapterProductos extends BaseAdapter {
        private ArrayList<String> mListItems;
        private LayoutInflater mLayoutInflater;
        Cursor c;
        private int[] colors = new int[] { 0x30FF0000, 0x300000FF};

        public MyCustomAdapterProductos(Context context, ArrayList<String> arrayList){
            Log.w("COD_SUBFAMILIA",cod_subFamilia);
            mListItems = arrayList;
            c= obj.getProductosCursorxFamilia(cod_subFamilia,pistaBusqueda);
            //get the layout inflater
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }



        @Override

        public View getView(int position, View convertview, ViewGroup viewGroup) {

            // create a ViewHolder reference
            View view=convertview;
            ViewHolder holder;

            c.moveToPosition(position);
            int col = c.getColumnIndex("despro");
            boolean isSet= c.getInt(col)==1;
            //check to see if the reused view is null or not, if is not null then reuse it
            if (view == null) {
                holder = new ViewHolder();

                view = mLayoutInflater.inflate(R.layout.producto_lista_item, null);

                holder.descripcion = (TextView) view.findViewById(R.id.productos_lista_item_tv_nombre);
                holder.codigo = (TextView) view.findViewById(R.id.productos_lista_item_tv_codigo);

                // the setTag is used to store the data within this view
                view.setTag(holder);
            } else {
                // the getTag returns the viewHolder object set as a tag to the view
                holder = (ViewHolder)view.getTag();
            }

            //get the string item from the position "position" from array list to put it on the TextView
            // String stringItem = mListItems.get(position);
            String stringItem = c.getString(col);

            if (stringItem != null) {
                if (holder.descripcion != null) {
                    //set the item name on the TextView
                    holder.descripcion.setText(stringItem);

                }
            }
            int colorPos = position % colors.length;
            if (colorPos == 1) {
                view.setBackgroundColor(Color.argb(250, 255, 255, 255));
            } else {
                view.setBackgroundColor(Color.argb(250, 224, 243, 250));
            }
            //this method must return the view corresponding to the data at the specified position.
            return view;

        }

        /**
         * Static class used to avoid the calling of "findViewById" every time the getView() method is called,
         * because this can impact to your application performance when your list is too big. The class is static so it
         * cache all the things inside once it's created.
         */
        public  class ViewHolder {

            protected TextView descripcion;
            protected TextView codigo;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return c.getCount();
        }



        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }



        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
    }

    class asyncPrecios extends AsyncTask< String, String, String > {


        private ProgressDialog pDialog;

        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(ProductosPreciosActivity.this);
            pDialog.setMessage("Cargando Precios....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            cargarPrecios();
            Log.d("tama�o lista 2",""+lista_precios.size());

            return "";
        }

        /*Una vez terminado doInBackground segun lo que halla ocurrido
        pasamos a la sig. activity
        o mostramos error*/
        protected void onPostExecute(String result) {

            pDialog.dismiss();//ocultamos progess dialog.
            Log.e("onPostExecute=",""+result);
            Log.d("tamaño lista 3",""+lista_precios.size());
            myListPrecios.setAdapter(new MyCustomAdapter(ProductosPreciosActivity.this, lista_precios));

        }
    }


    public void cargarFamilia(){


        listfamilia = dbhelper.getFamilia();
        familia_desc = new String [listfamilia.size()];
        familia_codigo = new String [listfamilia.size()];
        Iterator<DBFamilia> it=listfamilia.iterator();
        int i=0;
        while ( it.hasNext() ) {
            Object objeto = it.next();
            DBFamilia familia = (DBFamilia)objeto;

            familia_desc  [i] = familia.getFamilia();
            familia_codigo [i] = familia.getSecuencia();

            i++;

        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),
                android.R.layout.simple_spinner_item, familia_desc );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_familia.setAdapter(adapter);


        //     mostrarSubfamilias(familia_codigo[0]);


    }

    private void mostrarSubfamilias(String codfam) {
        // TODO Auto-generated method stub
        al_subfamilias= new ArrayList<DBSub_Familia>();
        al_subfamilias= dbhelper.getSubFamilias(codfam);

        Iterator<DBSub_Familia> it=al_subfamilias.iterator();
        int i=0;
        subf_desc = new String [al_subfamilias.size()];
        subf_codigo = new String [al_subfamilias.size()];

        while ( it.hasNext() ) {
            Object objeto = it.next();
            DBSub_Familia db_subf = (DBSub_Familia)objeto;
            subf_desc[i]=db_subf.getDes_familia();
            subf_codigo[i]=db_subf.getSub_familia();

            i++;
        }



        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),
                android.R.layout.simple_spinner_item, subf_desc);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_subfamilia.setAdapter(adapter);

        cod_subFamilia= 	subf_codigo[0];



    }
}