package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Producto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_MtaKardex;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Producto;
import com.example.sm_tubo_plast.genesys.datatypes.DBMta_Kardex;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class ProductosCursorAdapter extends AppCompatActivity {

    public static final String TAG = "ProductosCursorAdapter";
    DBclasses database;
    LayoutInflater inflater;
    ImageButton btn_scan;
    EditText edt_busqueda;
    ListView listView;
    DAO_Producto dao_Producto;

    ArrayList<Producto> listaProductos;
    ArrayList<Producto> listaBusqueda;
    ProductoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_cursor_adapter);

        btn_scan = (ImageButton) findViewById(R.id.productos_cursor_btnScan);
        edt_busqueda = (EditText) findViewById(R.id.myFilter);
        listView = (ListView) findViewById(R.id.productos_cursor_lv_prod);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        database = new DBclasses(getApplicationContext());

        dao_Producto = new DAO_Producto(getApplicationContext());
        btn_scan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // instantiate ZXing integration class
                IntentIntegrator scanIntegrator = new IntentIntegrator(
                        ProductosCursorAdapter.this);
                scanIntegrator.initiateScan();
            }
        });

        new async_MostrarProductos().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

                String codigoProducto = listaBusqueda.get(position).getCodigo();
                String descripcionProducto = listaBusqueda.get(position).getDescripcion();

                Intent i = new Intent(getApplicationContext(),CH_InformacionProducto.class);
                i.putExtra("codigoProducto", codigoProducto);
                i.putExtra("descripcionProducto", descripcionProducto);
                startActivity(i);

            }
        });

        /* ***************ENVIAR MENSAJE DE SINCRONIZACION************** */
        SharedPreferences preferencias_configuracion;
        preferencias_configuracion = getSharedPreferences(
                "preferencias_configuracion", Context.MODE_PRIVATE);
        boolean sincronizacionCorrecta = preferencias_configuracion.getBoolean(
                "preferencias_sincronizacionCorrecta", false);
        if (sincronizacionCorrecta == false) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(
                    ProductosCursorAdapter.this);
            alerta.setTitle("Sincronizacion incompleta");
            alerta.setMessage("Los datos estan incompletos, sincronice correctamente");
            alerta.setIcon(R.drawable.icon_warning);
            alerta.setCancelable(false);
            alerta.setPositiveButton("OK", null);
            alerta.show();
        }
        /****************************************************************/

        edt_busqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    listaBusqueda.clear();

                    String texto=charSequence.toString().replace(" ",".*").toLowerCase();
                    Pattern pattern = Pattern.compile(".*"+texto+".*");

                    for (int x = 0; x < listaProductos.size(); x++) {
                        String codigo = listaProductos.get(x).getCodigo();
                        String descripcion = listaProductos.get(x).getDescripcion();
                        String desc_comercial = listaProductos.get(x).getDesc_comercial();

                        if (pattern.matcher((codigo+descripcion+desc_comercial).toLowerCase()).matches()) {
                            listaBusqueda.add(listaProductos.get(x));
                        }

                    }
                    Log.d("ClientesActivity", "texto cambiado tama�o de la lista: " + listaBusqueda.size());
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    class async_MostrarProductos extends
            AsyncTask<Void, Void, ArrayList<Producto>> {
        ProgressDialog pDialog;

        protected void onPreExecute() {
            pDialog = new ProgressDialog(ProductosCursorAdapter.this);
            pDialog.setMessage("Cargando Productos...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<Producto> doInBackground(Void... params) {
            try {
                listaProductos = dao_Producto.getAllProducts();
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
                e.printStackTrace();
            }
            return listaProductos;
        }

        @Override
        protected void onPostExecute(ArrayList<Producto> lista) {
            pDialog.dismiss();
            listaBusqueda = new ArrayList<Producto>(lista);
            adapter = new ProductoAdapter(ProductosCursorAdapter.this, listaBusqueda);

            listView.setAdapter(adapter);
            btn_scan.setEnabled(true);
        }
    }

/*
	private void displayListView() {

		Cursor cursor = dbHelper.fetchAllProducts();
		// Cursor cursor2 = dbHelper.fetchAllProducts();

		// The desired columns to be bound
		String[] columns = new String[] { "codpro", "despro", "cod_rapido",
				"stock" };

		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.producto_info_codpro,
				R.id.producto_info_descripcion, R.id.producto_info_codrapido,
				R.id.producto_info_stock, };

		// create the adapter using the cursor pointing to the desired data
		// as well as the layout information
		dataAdapter = new SimpleCursorAdapter(this, R.layout.producto_info,
				cursor, columns, to, 0);


		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {

				Cursor cursor = (Cursor) listView.getItemAtPosition(position);

				String codpro = cursor.getString(cursor
						.getColumnIndexOrThrow("codpro"));
				String despro = cursor.getString(cursor
						.getColumnIndexOrThrow("despro"));

				Log.i("PRODUCTO CURSOR",
						"ean13: "
								+ cursor.getString(cursor
										.getColumnIndexOrThrow("ean13")));

				Intent i = new Intent(getApplicationContext(),
						CH_InformacionProducto.class);
				i.putExtra("codigoProducto", codpro);

				startActivity(i);

			}
		});

		EditText myFilter = (EditText) findViewById(R.id.myFilter);
		myFilter.addTextChangedListener(new TextWatcher() {

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				dataAdapter.getFilter().filter(s.toString());
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {

			public Cursor runQuery(CharSequence constraint) {

				if (verificarNumero(constraint.toString())) {
					return dbHelper.fetchProductsByCodigo(constraint.toString());
				} else {
					return dbHelper.fetchProductsByName(constraint.toString());
				}

			}

			private boolean verificarNumero(String pistaProducto) {
				try {
					Integer.parseInt(pistaProducto);
					return true;
				} catch (Exception e) {

					return false;
				}
			}
		});

	}
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Alternativa 1
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_productos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.productos_menu_lista:
                final Intent iproductos = new Intent(this,
                        ProductosPreciosActivity.class);
                this.startActivityForResult(iproductos, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // retrieve result of scanning - instantiate ZXing object
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, intent);
        // check we have a valid result
        if (scanningResult != null) {
            // get content from Intent Result
            String scanContent = scanningResult.getContents();
            // get format name of data scanned
            String scanFormat = scanningResult.getFormatName();

            // output to UI
            // formatTxt.setText("FORMAT: "+scanFormat);
            // contentTxt.setText("CONTENT: "+scanContent);

            if (scanContent != null && scanFormat != null) {
                //buscar_producto(scanFormat, scanContent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ningun dato recibido", Toast.LENGTH_SHORT);
                toast.show();
            }

        } else {
            // invalid scan data or scan canceled
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ningun dato recibido", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
/*
	public void buscar_producto(String format, String content) {

		if (format.equals("QR_CODE")) {

			// Cursor cursor = adapter2.getCursor();
			Cursor cursor = dataAdapter.getCursor();

			String codpro = "";
			String despro = "";
			boolean encontrado = false;

			while (!cursor.isAfterLast()) {

				codpro = cursor.getString(cursor.getColumnIndex("codpro"));
				despro = cursor.getString(cursor.getColumnIndex("despro"));

				if (content.equals(codpro)) {
					encontrado = true;
					break;
				}

				cursor.moveToNext();
			}

			if (encontrado) {
				Intent i = new Intent(getApplicationContext(),
						ProductosDetalleActivity.class);

				i.putExtra("NOMBRE", despro);
				i.putExtra("CODPRO", codpro);

				startActivity(i);
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"No se encontro producto", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		} else if (format.equals("EAN_13")) {

			// Cursor cursor = adapter2.getCursor();
			Cursor cursor = dataAdapter.getCursor();

			String ean13 = "";
			String codpro = "";
			String despro = "";
			boolean encontrado = false;

			while (!cursor.isAfterLast()) {

				ean13 = cursor.getString(cursor.getColumnIndex("ean13"));
				codpro = cursor.getString(cursor.getColumnIndex("codpro"));
				despro = cursor.getString(cursor.getColumnIndex("despro"));

				if (content.equals(ean13)) {
					encontrado = true;
					break;
				}

				cursor.moveToNext();
			}

			if (encontrado) {
				Intent i = new Intent(getApplicationContext(),
						ProductosDetalleActivity.class);

				i.putExtra("NOMBRE", despro);
				i.putExtra("CODPRO", codpro);

				startActivity(i);
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"No se encontro producto", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		} else {
			Toast.makeText(getApplicationContext(), "Formato Incorrecto",
					Toast.LENGTH_LONG).show();
		}

	}
	*/

    public class ProductoAdapter extends ArrayAdapter<Producto> {

        DAO_MtaKardex dao_mtaKardex=null;

        public ProductoAdapter(Context context, ArrayList<Producto> data) {
            super(context,R.layout.producto_info, data);
            dao_mtaKardex=new DAO_MtaKardex(context);
        }

        private class ViewHolder {
            TextView nombre, codigo, stock, tvProducto_desc_comercial;
        }

        ViewHolder viewHolder;

        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.producto_info, null);
                viewHolder = new ViewHolder();

                // cache the views
                viewHolder.nombre = (TextView) convertView.findViewById(R.id.producto_info_descripcion);
                viewHolder.codigo = (TextView) convertView.findViewById(R.id.producto_info_codpro);
                viewHolder.tvProducto_desc_comercial = (TextView) convertView.findViewById(R.id.tvProducto_desc_comercial);
                viewHolder.stock = (TextView) convertView.findViewById(R.id.producto_info_stock);
                convertView.setTag(viewHolder);

            } else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            try {
                viewHolder.nombre.setText(listaBusqueda.get(position).getDescripcion());
                viewHolder.tvProducto_desc_comercial.setText(listaBusqueda.get(position).getDesc_comercial().length()>0? listaBusqueda.get(position).getDesc_comercial() :"--");
                viewHolder.codigo.setText(listaBusqueda.get(position).getCodigo());

                DBMta_Kardex mta_kardex = dao_mtaKardex.GetStockProducto(  listaBusqueda.get(position).getCodigo());
                if (mta_kardex!=null){
                    viewHolder.stock.setText(""+mta_kardex.getStock()+", Separado: "+mta_kardex.getXtemp()+", Disponibe: "+(mta_kardex.getStock()-mta_kardex.getXtemp())+"");
                }else viewHolder.stock.setText("Sin stock");
            } catch (Exception e) {
                Log.e(TAG, "ProductoAdapter "+e.getMessage());
                e.printStackTrace();
            }
            return convertView;
        }

    }

}

