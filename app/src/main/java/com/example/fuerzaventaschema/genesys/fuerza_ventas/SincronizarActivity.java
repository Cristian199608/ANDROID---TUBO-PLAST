package com.example.fuerzaventaschema.genesys.fuerza_ventas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.datatypes.DBSyncManager;
import com.example.fuerzaventaschema.genesys.datatypes.DBSync_soap_manager;
import com.example.fuerzaventaschema.genesys.datatypes.DB_Almacenes;
import com.example.fuerzaventaschema.genesys.datatypes.DB_Servidor;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.Dialog.DialogFragment_preferencias;
import com.example.fuerzaventaschema.genesys.service.ConnectionDetector;
import com.example.fuerzaventaschema.genesys.util.GlobalFunctions;
import com.example.fuerzaventaschema.genesys.util.GlobalVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@SuppressLint("LongLogTag")
public class SincronizarActivity extends AppCompatActivity implements DialogFragment_preferencias.DialogListener {

    ProgressDialog pDialog;
    RadioGroup rb_opciones;
    DBSync_soap_manager soap_manager;
    ConnectionDetector cd;
    DBSyncManager sync;
    String codven = "1", url = "1", catalog, userid, contrasenaid, servicio;
    String url_local = "1", catalog_local, userid_local, contrasenaid_local,
            servicio_local;
    boolean chk_principal_estado, chk_secundario_estado;
    String[] tablas;

    String servidorBD = "";
    String nombreBD = "";
    String usuarioBD = "";
    String contrasenaBD = "";

    String modulos_seleccionados;

    CharSequence[] digitList = { "Clientes", "Vendedores", "Productos",
            "Cobranza" };
    // lista para la sincronizacion desde loginActivity
    CharSequence[] digitList2 = { "servicios", "usuarios" };

    CharSequence[] lista = {};

    AlertDialog.Builder alt_bld;
    int opcion = 0;
    private static final String TAG = "com.genesys.fuerza_ventas.SincronizarActivity.WAKE_LOCK_TAG";
    private PowerManager.WakeLock wakeLock;
    SharedPreferences prefs, prefs2, preferencias_configuracion;
    SharedPreferences.Editor editor, editor2, editor_preferencias;
    Button btn_guardar, btn_cancelar2, btn_sincronizar;
    DBclasses _helper;
    String[] codServicio;
    String[] nomServicio;

    String[] codServicio_local;
    String[] nomServicio_local;

    Button btn_Preferencias;
    EditText edt_contrasenia, edt_contrasenia_local, edt_servidor,
    edt_servidor_local, edt_nombrebd, edt_nombrebd_local, edt_usuario,
    edt_usuario_local, edt_servicio;
    Spinner spn_servicio, spn_servicio_local;
    CheckBox chk_secundario;
    Switch settings_spn_servicio;
    ArrayList<DB_Servidor> al_servidor;
    String spn_Texto;
    String spn_Texto_local;
    public int result = 0;
    public int result_local = 0;
    String origen;
    DBclasses database;

    // Configuraciones adicionales
    ArrayList<DB_Almacenes> lista_Almacenes = new ArrayList<DB_Almacenes>();

    /*Enviar Backup al correo*/
    Button btn_backUp;

    @SuppressLint({"NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar);

        //getActionBar().setHomeButtonEnabled(true);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,TAG);

        database = new DBclasses(getApplicationContext());

        edt_nombrebd = (EditText) findViewById(R.id.settings_edt_nombreBD);
        edt_servidor = (EditText) findViewById(R.id.settings_edt_servidor);
        spn_servicio = (Spinner) findViewById(R.id.settings_spn_servicio);
        edt_usuario = (EditText) findViewById(R.id.settings_edt_usuario);
        //edt_servicio = (EditText) findViewById(R.id.settings_edt_servicio);
        edt_contrasenia = (EditText) findViewById(R.id.settings_edtContrasena);
        settings_spn_servicio = (Switch) findViewById(R.id.settings_chk_principal);

        spn_servicio_local = (Spinner) findViewById(R.id.settings_spn_servicio_local);
        edt_servidor_local = (EditText) findViewById(R.id.settings_edt_servidor_local);
        edt_nombrebd_local = (EditText) findViewById(R.id.settings_edt_nombreBD_local);
        edt_usuario_local = (EditText) findViewById(R.id.settings_edt_usuario_local);
        edt_contrasenia_local = (EditText) findViewById(R.id.settings_edtContrasena_local);
        chk_secundario = (CheckBox) findViewById(R.id.settings_chk_secundario);

        btn_backUp = (Button)findViewById(R.id.settings_btnBackup);

        // Configuraciones Adicionales

        edt_contrasenia.setEnabled(false);
        edt_servidor.setEnabled(false);
        edt_nombrebd.setEnabled(false);
        edt_usuario.setEnabled(false);

        edt_contrasenia_local.setEnabled(false);
        edt_servidor_local.setEnabled(false);
        edt_nombrebd_local.setEnabled(false);
        edt_usuario_local.setEnabled(false);

        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        prefs2 = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);

        codven = prefs.getString("codven", "0");

        url = prefs.getString("url", "0");
        catalog = prefs.getString("catalog", "0");
        userid = prefs.getString("userid", "0");
        contrasenaid = prefs.getString("contrasenaid", "0");
        servicio = prefs.getString("servicio", "0");
        chk_principal_estado = prefs.getBoolean("servicio_principal_activo",
                true);

        url_local = prefs.getString("url_local", "0");
        catalog_local = prefs.getString("catalog_local", "0");
        userid_local = prefs.getString("userid_local", "0");
        contrasenaid_local = prefs.getString("contrasenaid_local", "0");
        servicio_local = prefs.getString("servicio_local", "0");
        chk_secundario_estado = prefs.getBoolean("servicio_secundario_activo",
                true);

        settings_spn_servicio.setChecked(chk_principal_estado);
        chk_secundario.setChecked(chk_secundario_estado);

        // < Configuraciones de Preferencia

        btn_Preferencias = (Button) findViewById(R.id.settings_btnPreferencias);
        btn_Preferencias.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Clase para mostrar las preferencias
                new async_Preferencias().execute();

            }
        });
        // </ Configuraciones de Preferencia

        _helper = new DBclasses(getApplicationContext());
        cd = new ConnectionDetector(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        origen = bundle.getString("ORIGEN");

        settings_spn_servicio
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {

                            editor = prefs.edit();
                            editor.putBoolean("servicio_principal_activo", true);
                            GlobalVar.servicio_principal_activo = true;
                            editor.commit();

                            GlobalVar.urlService = GlobalVar.direccion_servicio;
                            GlobalVar.id_servicio = GlobalVar.INTERNET;

                            servidorBD = url;
                            nombreBD = catalog;
                            usuarioBD = userid;
                            contrasenaBD = contrasenaid;

                            // Toast.makeText(getApplicationContext(),
                            // "principal activo "+GlobalVar.servicio_principal_activo,
                            // Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getApplicationContext(),
                            // "urlService "+GlobalVar.urlService,
                            // Toast.LENGTH_SHORT).show();

                            Log.i("SincronizarActivity onCkecked",
                                    "principal activo "
                                            + GlobalVar.servicio_principal_activo);
                            Log.i("SincronizarActivity onCkecked",
                                    "urlService " + GlobalVar.urlService);
                            Log.i("SincronizarActivity onCkecked",
                                    "servicio (local=0,internet=1) "
                                            + GlobalVar.id_servicio);

                        } else {
                            editor = prefs.edit();
                            editor.putBoolean("servicio_principal_activo",
                                    false);
                            GlobalVar.servicio_principal_activo = false;
                            editor.commit();

                            if (chk_secundario.isChecked()) {

                                GlobalVar.urlService = GlobalVar.direccion_servicio_local;
                                GlobalVar.id_servicio = GlobalVar.LOCAL;
                                servidorBD = url_local;
                                nombreBD = catalog_local;
                                usuarioBD = userid_local;
                                contrasenaBD = contrasenaid_local;

                            }

                            // Toast.makeText(getApplicationContext(),
                            // "principal inactivo "+GlobalVar.servicio_principal_activo,
                            // Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getApplicationContext(),
                            // "urlService "+GlobalVar.urlService,
                            // Toast.LENGTH_SHORT).show();

                            Log.i("SincronizarActivity onCkecked",
                                    "principal inactivo "
                                            + GlobalVar.servicio_principal_activo);
                            Log.i("SincronizarActivity onCkecked",
                                    "urlService " + GlobalVar.urlService);
                            Log.i("SincronizarActivity onCkecked",
                                    "servicio (local=0,internet=1) "
                                            + GlobalVar.id_servicio);
                        }

                    }
                });

        chk_secundario
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            editor = prefs.edit();
                            editor.putBoolean("servicio_secundario_activo",
                                    true);
                            GlobalVar.servicio_secundario_activo = true;
                            editor.commit();

                            if (!settings_spn_servicio.isChecked()) {

                                GlobalVar.urlService = GlobalVar.direccion_servicio_local;
                                GlobalVar.id_servicio = GlobalVar.LOCAL;
                                servidorBD = url_local;
                                nombreBD = catalog_local;
                                usuarioBD = userid_local;
                                contrasenaBD = contrasenaid_local;

                            }

                            // Toast.makeText(getApplicationContext(),
                            // "secundario activo "
                            // +GlobalVar.servicio_secundario_activo,
                            // Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getApplicationContext(),
                            // "urlService "+GlobalVar.urlService,
                            // Toast.LENGTH_SHORT).show();

                            Log.i("SincronizarActivity onCkecked",
                                    "Secundario activo "
                                            + GlobalVar.servicio_principal_activo);
                            Log.i("SincronizarActivity onCkecked",
                                    "urlService " + GlobalVar.urlService);
                            Log.i("SincronizarActivity onCkecked",
                                    "servicio (local=0,internet=1) "
                                            + GlobalVar.id_servicio);

                        } else {
                            editor = prefs.edit();
                            editor.putBoolean("servicio_secundario_activo",
                                    false);
                            GlobalVar.servicio_secundario_activo = false;
                            editor.commit();

                            // Toast.makeText(getApplicationContext(),
                            // "secundario inactivo "
                            // +GlobalVar.servicio_secundario_activo ,
                            // Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getApplicationContext(),
                            // "urlService "+GlobalVar.urlService,
                            // Toast.LENGTH_SHORT).show();

                            Log.i("SincronizarActivity onCkecked",
                                    "Secundario inactivo "
                                            + GlobalVar.servicio_principal_activo);
                            Log.i("SincronizarActivity onCkecked",
                                    "urlService " + GlobalVar.urlService);
                            Log.i("SincronizarActivity onCkecked",
                                    "servicio (local=0,internet=1) "
                                            + GlobalVar.id_servicio);

                        }

                    }
                });

        // if(codven=="1"){ --> cambiado a if(url.lenght < 2)
        if (url != "0" && servicio != "0") {
            Log.d(TAG, "buscarServicios");
            buscarServicios();

        } else {
            Log.d(TAG, "mostrarServicios");
            mostrarServicios();

        }

        if (url_local != "0" && servicio_local != "0") {
            Log.d(TAG, "buscarServicios2");
            buscarServicios2();

        } else {
            Log.d(TAG, "mostrarServicios2");
            mostrarServicios2();
        }

        Log.w("PREFERENCIAS: ", codven + url + catalog + userid + contrasenaid+ servicio + GlobalVar.urlService);
        soap_manager = new DBSync_soap_manager(getApplicationContext());
        sync = new DBSyncManager(getApplicationContext());

        btn_sincronizar = (Button) findViewById(R.id.sincronizar_btn_sincronizar);
        rb_opciones = (RadioGroup) findViewById(R.id.sincronizar_rb_opciones);

        btn_guardar = (Button) findViewById(R.id.settings_btnGuardar);
        btn_cancelar2 = (Button) findViewById(R.id.settings_btnCancelar);
        edt_contrasenia = (EditText) findViewById(R.id.settings_edtContrasena);

        // Bloquear el boton sincronizar al iniciar activity
        btn_sincronizar.setEnabled(false);

        rb_opciones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub

                switch (arg1) {
                    case R.id.radio0:
                        opcion = 0;
                        break;
                    case R.id.radio1:
                        opcion = 1;

                    default:
                        break;
                }
            }
        });
        alt_bld = new AlertDialog.Builder(this);

        btn_sincronizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // DBSync_soap_manager soap_manager1 = new
                // DBSync_soap_manager(getApplicationContext());

                if (!settings_spn_servicio.isChecked() && !chk_secundario.isChecked()) {

                    AlertDialog.Builder dialogo = new AlertDialog.Builder(SincronizarActivity.this);
                    dialogo.setMessage("Debe seleccionar almenos un servicio");
                    dialogo.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            });
                    dialogo.create();
                    dialogo.show();

                } else {
                    new async_verificarServicio().execute();
                }
            }
        });

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setIcon(R.drawable.icon_photos_tab);
        alt_bld.setTitle("Seleccione tablas(alert)");

        alt_bld.setMultiChoiceItems(digitList, new boolean[] { false, true,
                false }, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int whichButton,
                                boolean isChecked) {

            }
        });

        alt_bld.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ListView list = ((AlertDialog) dialog).getListView();

                        StringBuilder sb = new StringBuilder();

                        for (int i = 0; i < list.getCount(); i++) {
                            boolean checked = list.isItemChecked(i);

                            if (checked) {
                                if (sb.length() > 0)
                                    sb.append(",");
                                sb.append(list.getItemAtPosition(i));
                            }
                        }

                        Toast.makeText(getApplicationContext(),
                                "Selected digit: " + sb.toString(),
                                Toast.LENGTH_SHORT).show();

                    }
                });
        alt_bld.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        btn_guardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (!settings_spn_servicio.isChecked() && !chk_secundario.isChecked()) {

                    AlertDialog.Builder dialogo = new AlertDialog.Builder(
                            SincronizarActivity.this);
                    dialogo.setMessage("Debe seleccionar almenos un servicio");
                    dialogo.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            });
                    dialogo.create();
                    dialogo.show();

                } else {
                    cargarDialogoPreferencias();
                }

            }

            private void cargarDialogoPreferencias() {
                // TODO Auto-generated method stub
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        SincronizarActivity.this);
                alertDialog.setTitle("PREFERENCIAS");
                alertDialog.setMessage("Se guardaran los siguientes datos: \n"
                                + "Servicio:  " + spn_Texto + "\n" + "Base Datos:  "
                                + edt_nombrebd.getText().toString() + "\n"
                                + "Usuario:  " + edt_usuario.getText().toString()
                                + "\n"
                        /* + "Contrase�a:  "+ edt_contrasenia.getText().toString() */);
                alertDialog.setIcon(R.drawable.ic_alert);
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                editor = prefs.edit();

                                editor.putString("url", edt_servidor.getText().toString());
                                editor.putString("catalog", edt_nombrebd.getText().toString());
                                editor.putString("userid", edt_usuario.getText().toString());
                                editor.putString("contrasenaid", edt_contrasenia.getText().toString());
                                editor.putString("servicio", spn_servicio.getSelectedItem().toString());

                                editor.putString("url_local",edt_servidor_local.getText().toString());
                                editor.putString("catalog_local",edt_nombrebd_local.getText().toString());
                                editor.putString("userid_local",edt_usuario_local.getText().toString());
                                editor.putString("contrasenaid_local",edt_contrasenia_local.getText().toString());
                                editor.putString("servicio_local",spn_servicio_local.getSelectedItem().toString());

                                editor.commit();

                                GlobalVar.NombreWEB = GlobalFunctions.obtenerNombreWEB(spn_Texto);
                                GlobalVar.direccion_servicio = spn_servicio.getSelectedItem().toString();
                                GlobalVar.direccion_servicio_local = spn_servicio_local.getSelectedItem().toString();

                                Log.i("Global var direccion_servicio",GlobalVar.direccion_servicio);
                                Log.i("Global var direccion_servicio_local",GlobalVar.direccion_servicio_local);

                                catalog = edt_nombrebd.getText().toString();
                                userid = edt_usuario.getText().toString();
                                contrasenaid = edt_contrasenia.getText().toString();
                                url = edt_servidor.getText().toString();
                                catalog_local = edt_nombrebd_local.getText().toString();
                                userid_local = edt_usuario_local.getText().toString();
                                contrasenaid_local = edt_contrasenia_local.getText().toString();
                                url_local = edt_servidor_local.getText().toString();

                                if (settings_spn_servicio.isChecked()) {
                                    editor.putBoolean("check_web", true);
                                    editor.putBoolean("check_local", false);
                                    GlobalVar.urlService = GlobalVar.direccion_servicio;
                                    GlobalVar.id_servicio = GlobalVar.INTERNET;
                                    servidorBD = url;
                                    nombreBD = catalog;
                                    usuarioBD = userid;
                                    contrasenaBD = contrasenaid;
                                } else {
                                    editor.putBoolean("check_web", false);
                                    editor.putBoolean("check_local", true);
                                    GlobalVar.urlService = GlobalVar.direccion_servicio_local;
                                    GlobalVar.id_servicio = GlobalVar.LOCAL;
                                    servidorBD = url_local;
                                    nombreBD = catalog_local;
                                    usuarioBD = userid_local;
                                    contrasenaBD = contrasenaid_local;
                                }

                                Log.i("GLOBAl var urlService", ""
                                        + GlobalVar.urlService);
                                Log.i("SincronizarActivity",
                                        "servicio (local=0,internet=1) "
                                                + GlobalVar.id_servicio);

                                // Solo si se da en aceptar se activa el boton
                                // sincronizar para evitar error
                                // de mala sincronizacion
                                btn_sincronizar.setEnabled(true);
                            }
                        });
                alertDialog.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        });
                alertDialog.show();
            }

        });

        spn_servicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int pos, long arg3) {

                int codigo = 0;
                spn_Texto = spn_servicio.getSelectedItem().toString();
                for (int i = 0; i < nomServicio.length; i++) {
                    if (spn_Texto.equals(nomServicio[i])) {
                        codigo = i;
                    }
                }

                cargarCampos(codServicio[codigo]);
                spn_servicio_local.setSelection(pos);

            }

            private void cargarCampos(String codigo) {
                DB_Servidor entityServidor = new DB_Servidor();
                entityServidor = _helper.getEntityServidorxCodigo(codigo);
                edt_servidor.setText(entityServidor.getTX_SERV_servidorBD());
                edt_nombrebd.setText(entityServidor.getTX_SERV_nombreBD());
                edt_usuario.setText(entityServidor.getTX_SERV_usuario());
                edt_contrasenia.setText(entityServidor.getTX_SERV_contrasena());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        spn_servicio_local
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int codigo = 0;
                        spn_Texto_local = spn_servicio_local.getSelectedItem().toString();
                        for (int i = 0; i < nomServicio_local.length; i++) {
                            if (spn_Texto_local.equals(nomServicio_local[i])) {
                                codigo = i;
                            }
                        }
                        if (codigo >= 0) {
                            cargarCampos(codServicio_local[codigo]);
                        }
                    }

                    private void cargarCampos(String codigo) {
                        // TODO Auto-generated method stub
                        DB_Servidor entityServidor = new DB_Servidor();
                        entityServidor = _helper
                                .getEntityServidorxCodigo(codigo);
                        edt_servidor_local.setText(entityServidor
                                .getTX_SERV_servidorBD());
                        edt_nombrebd_local.setText(entityServidor
                                .getTX_SERV_nombreBD());
                        edt_usuario_local.setText(entityServidor
                                .getTX_SERV_usuario());
                        edt_contrasenia_local.setText(entityServidor
                                .getTX_SERV_contrasena());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                });

        btn_cancelar2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // spn_servicio.setVisibility(0);
                // edt_servicio.setVisibility(View.GONE);
                // buscarServicios();
            }
        });

        btn_backUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //_okkk
//                Intent intent_backUp = new Intent(SincronizarActivity.this,Activity_EnviarBackUp.class);
//                startActivity(intent_backUp);
//                finish();
            }
        });

        showRequestPermisionToAccesStorage();
    }

    private void SeleccionarTablas(){
        alt_bld.setTitle("Seleccione tablas a Sincronizar");

        //
        if (origen.equals("LOGIN")) {
            lista = digitList2;

            alt_bld.setMultiChoiceItems(
                    lista,
                    new boolean[] { false, true, true, true, false,
                            false, false },
                    new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton, boolean isChecked) {

                            /*
                             * User clicked on a check box do some
                             * stuff
                             */
                        }
                    });

        } else {
            lista = digitList;

            alt_bld.setMultiChoiceItems(
                    lista,
                    new boolean[] { true, true, true, true, false,
                            false, false },
                    new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton, boolean isChecked) {

                            /*
                             * User clicked on a check box do some
                             * stuff
                             */
                        }
                    });
        }
        //

        alt_bld.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int whichButton) {
                        ListView list = ((AlertDialog) dialog).getListView();
                        StringBuilder sb = new StringBuilder();

                        for (int i = 0; i < list.getCount(); i++) {
                            boolean checked = list.isItemChecked(i);

                            if (checked) {
                                if (sb.length() > 0)
                                    sb.append(",");
                                sb.append(list.getItemIdAtPosition(i));
                            }
                        }

                        Log.w("TABLAS SINCRONIZAR","" + sb.toString());
                        modulos_seleccionados = sb.toString();

                        //Toast.makeText(getApplicationContext(),"Selected digit: " + sb.toString(),Toast.LENGTH_SHORT).show();
                        StringTokenizer stTexto = new StringTokenizer(sb.toString(), ",");

                        tablas = new String[stTexto.countTokens()];
                        int a = 0;
                        while (stTexto.hasMoreTokens()) {
                            tablas[a] = stTexto.nextToken()
                                    .toString();
                            String as = tablas[a].toString();

                            a = a + 1;
                            Log.w("TABLAS", "" + as);
                        }

                        // Generar backup antes de la sincornizacion
                        GlobalFunctions.backupdDatabase();

                        new asynclogin().execute("", "");

                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int whichButton) {
                            }
                        }).show();
    }
    private void buscarServicios() {
        mostrarServicios();
        result = buscarSecuenciaxServicio(servicio);
        if (result >= 0) {
            spn_servicio.setSelection(result);
        }
        Log.w("URL == 1 PREFERENCIAS: ", codven + url + catalog + userid + contrasenaid);
    }

    private void buscarServicios2() {
        mostrarServicios2();
        result_local = buscarSecuenciaxServicio(servicio_local);
        if (result_local >= 0) {
            spn_servicio_local.setSelection(result_local);
        }
    }

    public void cargarDialogoPreferencias2() {
        // TODO Auto-generated method stub
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SincronizarActivity.this);
        alertDialog.setTitle("PREFERENCIAS2");
        alertDialog
                .setMessage("Se guardarácomo preferencias los siguientes datos: \n"
                                + "Servicio:  "
                                + spn_Texto
                                + "\n"
                                + "Base Datos:  "
                                + edt_nombrebd.getText().toString()
                        //+ "\n"
                        //+ "Usuario:  "
                        //+ edt_usuario.getText().toString()
                        //+ "\n"
                        /* + "Contrase�a:  "+ edt_contrasenia.getText().toString() */);
        alertDialog.setIcon(R.drawable.ic_alert);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editor2 = prefs2.edit();

                        editor2.putString("url", edt_servidor.getText() .toString());
                        editor2.putString("catalog", edt_nombrebd.getText()
                                .toString());
                        editor2.putString("userid", edt_usuario.getText()
                                .toString());
                        editor2.putString("contrasenaid", edt_contrasenia
                                .getText().toString());
                        editor2.putString("servicio", spn_servicio
                                .getSelectedItem().toString());

                        editor2.putString("url_local", edt_servidor_local
                                .getText().toString());
                        editor2.putString("catalog_local", edt_nombrebd_local
                                .getText().toString());
                        editor2.putString("userid_local", edt_usuario_local
                                .getText().toString());
                        editor2.putString("contrasenaid_local",
                                edt_contrasenia_local.getText().toString());
                        editor2.putString("servicio_local", spn_servicio_local
                                .getSelectedItem().toString());

                        editor2.commit();

                        GlobalVar.NombreWEB = GlobalFunctions
                                .obtenerNombreWEB(spn_Texto);
                        GlobalVar.direccion_servicio = spn_servicio
                                .getSelectedItem().toString();
                        GlobalVar.direccion_servicio_local = spn_servicio_local
                                .getSelectedItem().toString();

                        Log.i("Global var direccion_servicio", spn_servicio
                                .getSelectedItem().toString());
                        Log.i("Global var direccion_servicio_local",
                                spn_servicio_local.getSelectedItem().toString());

                        catalog = edt_nombrebd.getText().toString();
                        userid = edt_usuario.getText().toString();
                        contrasenaid = edt_contrasenia.getText().toString();
                        url = edt_servidor.getText().toString();

                        catalog_local = edt_nombrebd_local.getText().toString();
                        userid_local = edt_usuario_local.getText().toString();
                        contrasenaid_local = edt_contrasenia_local.getText()
                                .toString();
                        url_local = edt_servidor_local.getText().toString();

                        if (settings_spn_servicio.isChecked()) {
                            GlobalVar.urlService = GlobalVar.direccion_servicio;
                            GlobalVar.id_servicio = GlobalVar.INTERNET;
                            servidorBD = url;
                            nombreBD = catalog;
                            usuarioBD = userid;
                            contrasenaBD = contrasenaid;
                        } else {
                            GlobalVar.urlService = GlobalVar.direccion_servicio_local;
                            GlobalVar.id_servicio = GlobalVar.LOCAL;
                            servidorBD = url_local;
                            nombreBD = catalog_local;
                            usuarioBD = userid_local;
                            contrasenaBD = contrasenaid_local;
                        }

                        Log.i("GLOBAl var urlService", ""
                                + GlobalVar.urlService);
                        Log.i("SincronizarActivity",
                                "servicio (local=0,internet=1) "
                                        + GlobalVar.id_servicio);

                    }
                });
        alertDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
        alertDialog.show();
    }



    /*
     * ********************************************** ASYNC
     * CLASS***********************************************************
     */
    class async_Preferencias extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(SincronizarActivity.this);
            pDialog.setMessage("Cargando Almacenes...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... parametro) {
            try {
                lista_Almacenes = _helper.obtenerListaAlmacenes();

            } catch (Exception e) {
                Log.d("doInBackground async_getAlmacenes", e.toString());
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            if (lista_Almacenes.isEmpty() == true) {
                Toast.makeText(
                        getApplicationContext(),
                        "No se encontraron almacenes, sincronice para actualizar...",
                        Toast.LENGTH_SHORT).show();
                /*
                 * DB_Almacenes objAlmacen = new DB_Almacenes();
                 * objAlmacen.setCodalm("00");
                 * objAlmacen.setDesalm("almacenGone");
                 * objAlmacen.setId_local("00");
                 * objAlmacen.setLocal("localGone");
                 * lista_Almacenes.add(objAlmacen);
                 */

            } else {
                pDialog.setTitle("Cargando");
                String idAlmacenSeleccionado;
                String vistaSeleccionado;
                boolean stockAuto, precioCero;

                /* Obtenemos las preferencias guardadas anteriormente */
                String preferenciasJSON = preferencias_configuracion.getString("preferenciasJSON", "no_preferencias");
                Log.d("preferenciasJSON", preferenciasJSON);

                if (preferenciasJSON.equals("no_preferencias")) {
                    vistaSeleccionado = "no_vista";
                    stockAuto = false;
                    idAlmacenSeleccionado = "0";
                    precioCero = false;
                } else {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<Map<String, Object>>() {
                    }.getType();

                    Map<String, Object> mapObject2 = gson.fromJson(preferenciasJSON, listType);
                    vistaSeleccionado = (String) mapObject2	.get("preferencias_vista");
                    stockAuto = (Boolean) mapObject2.get("preferencias_stock");
                    List<String> almacenAsociado = (List<String>) mapObject2.get("preferencias_almacen");
                    idAlmacenSeleccionado = almacenAsociado.get(0);

                    precioCero = preferencias_configuracion.getBoolean("preferencias_precioCero", false);
                    Log.d("DATOS:", "\nvista_Seleccionado: "+ vistaSeleccionado + "\nstock: " + stockAuto + "\nalmacen_seleccionado: " + almacenAsociado);
                }

                // ***** Llenamos manualmente las vistas que se mostraran el las
                // opciones
                ArrayList<String> lista_vistas = new ArrayList<String>();
                lista_vistas.add("Vista 1");
                lista_vistas.add("Vista 2");

                DialogFragment_preferencias dialog_preferencias = new DialogFragment_preferencias(lista_Almacenes, lista_vistas, idAlmacenSeleccionado,vistaSeleccionado, stockAuto, precioCero);
                dialog_preferencias.show(getSupportFragmentManager(), "dialogPreferencias");
                dialog_preferencias.setCancelable(false);

            }
            pDialog.dismiss();
        }
    }

    // ***************************************************************************************************************************
    class async_verificarServicio extends AsyncTask<Void, Void, Boolean>{
        ProgressDialog proDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            proDialog = new ProgressDialog(SincronizarActivity.this);
            //proDialog.setTitle("SAEMOVILES");
            proDialog.setMessage("Verificando...");
            proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            proDialog.setIndeterminate(true);
            proDialog.setCancelable(false);
            proDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isActivo;
            /*DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());
            ConnectionDetector connection = new ConnectionDetector(getApplicationContext());

            if (connection.hasActiveInternetConnection2()) {
                if (connection.hasActiveDominio()) {
                    try {
                        String ruc = database.getRucEmpresa();
                        String nombre = database.getNombreEmpresa();
                        isActivo = soap_manager.isServicioActivo(nombre,ruc);
                        return isActivo;
                    }catch(Exception e){
                        //e.printStackTrace();
                    }
                }else{
                    return true;//Si hay internet pero no se puede conectar al dominio, debe permitir sincronizar(Puede que el dominio est� ca�do)
                }
            }*/
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            proDialog.dismiss();
            if (result==true) {
                SeleccionarTablas();
            }else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(SincronizarActivity.this);
                alerta.setTitle("Importante");
                alerta.setMessage("Su servicio no esta disponible");
                alerta.setIcon(R.drawable.warning);
                alerta.setCancelable(false);
                alerta.setPositiveButton("Aceptar", null);
                alerta.show();
            }
        }
    }
    class asynclogin extends AsyncTask<String, String, String> {

        // String opcion,pass;
        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(SincronizarActivity.this);
            pDialog.setTitle(GlobalVar.urlService);
            pDialog.setMessage("Sincronizando....");
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setIndeterminate(false);
            // pDialog.setButton("Cancelar", (DialogInterface.OnClickListener)
            // null);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {

            // opcion=params[0];
            // pass=params[1];

            String error = "0";

            if (origen.equals("LOGIN")) {
                /* SINCRONIZACION DESDE EL LOGIN */
                if (cd.hasActiveInternetConnection2() || 5==5) {

                    for (int i = 0; i < tablas.length; i++) {
                        int valor = Integer.parseInt(tablas[i].toString());

                        switch (valor) {

                            case 0:

                                try {
                                    publishProgress("1");
                                    soap_manager.Sync_tabla_Servidor(servidorBD,nombreBD, usuarioBD, contrasenaBD);
                                    publishProgress("100");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    error = "3";
                                }

                                break;

                            case 1:

                                try {
                                    publishProgress("1");
                                    soap_manager.Sync_tabla_usuarios(servidorBD,nombreBD, usuarioBD, contrasenaBD);
                                    publishProgress("50");
                                    soap_manager.Sync_tabla_vendedores(servidorBD,nombreBD, usuarioBD, contrasenaBD);
                                    publishProgress("100");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    error = "1";
                                }

                        }
                    }
                } else {
                    error = "2";
                }

            } else {
                /* SINCRONIZACION DESDE LA APP, TABLAS */
                if (cd.hasActiveInternetConnection(getApplicationContext())) {

                    String datetime = "";

                    try {
                        datetime = soap_manager.obtenerHoraServBD(servidorBD,	nombreBD, usuarioBD, contrasenaBD);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("obtener_hora_servbd",	"No se pudo obtener la hora del servidor");
                    }

                    try {
                        soap_manager.Sync_tabla_configuracion(servidorBD,	nombreBD, usuarioBD, contrasenaBD);
                        soap_manager.Sync_tabla_registrosGeneralesMovil(servidorBD, nombreBD, usuarioBD, contrasenaBD);
                    } catch (Exception e) {
                        e.printStackTrace();
                        error = "configuracion";
                    }

                    for (int i = 0; i < tablas.length; i++) {

                        int valor = Integer.parseInt(tablas[i].toString());

                        switch (valor) {
                            case 0:
                                Log.w("GLOBAL VARIABLES", GlobalVar.urlService
                                        + "--" + GlobalVar.NombreWEB);

                                try {
                                    publishProgress("10");
                                    soap_manager.Sync_tabla_turno(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("20");
                                    publishProgress("33");
                                    //SERVER 212
                                    soap_manager.Sync_tabla_clientexVendedor(codven, servidorBD, nombreBD,	usuarioBD, contrasenaBD);
                                    publishProgress("40");
                                    //SERVER 212
                                    soap_manager.Sync_tabla_ZnfProgramacionClientes(codven, servidorBD, nombreBD,usuarioBD, contrasenaBD);
                                    publishProgress("50");
                                    soap_manager.Sync_tabla_locales(servidorBD,nombreBD, usuarioBD, contrasenaBD);
                                    publishProgress("59");
                                    soap_manager.Sync_tabla_obra(codven,servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("68");
                                    soap_manager.Sync_tabla_transporte(codven,servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("85");
                                    soap_manager.Sync_tabla_lugarEntrega(codven,servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("90");
                                    //SERVER 212
                                    soap_manager.Sync_tabla_direccion_cliente(codven, servidorBD, nombreBD,	usuarioBD, contrasenaBD);
                                    publishProgress("100");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    error = "1";
                                }

                                break;
                            case 1:
                                try {

                                    publishProgress("1");
                                    soap_manager.Sync_tabla_motivo_noventa(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("25");
                                    soap_manager.Sync_tabla_Zona(codven,	servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("30");
                                    soap_manager.Sync_tabla_Zona_XY(codven,servidorBD,nombreBD, usuarioBD, contrasenaBD);
                                    publishProgress("35");
                                    soap_manager.Sync_tabla_Ruta(codven,	servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("45");
                                    //SERVER 212
                                    soap_manager.Sync_tabla_vendedores(servidorBD,	nombreBD, usuarioBD, contrasenaBD);
                                    publishProgress("59");
                                    soap_manager.Sync_tabla_usuarios(servidorBD,nombreBD, usuarioBD, contrasenaBD);
                                    publishProgress("65");

                                    soap_manager.Sync_tabla_almacenes(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("76");
                                    soap_manager.Sync_tabla_formasPago(codven,servidorBD, nombreBD, usuarioBD, contrasenaBD);
                                    publishProgress("79");
                                    soap_manager.Sync_tabla_moneda(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("85");
                                    soap_manager.Sync_tabla_ObjPedido(codven,servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("90");
                                    //soap_manager.Sync_tabla_CuotaVendedor(codven,	servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    soap_manager.Sync_tabla_motivo(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("94");
                                    soap_manager.Sync_tabla_expectativa(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("96");
                                    soap_manager.Sync_tabla_RegistroBonificacionesPendientes(codven,servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("99");
                                    publishProgress("100");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    error = "1";
                                }

                                break;

                            case 2:

                                try {
                                    publishProgress("1");
                                    //SERVER .212
                                    soap_manager.Sync_tabla_producto(codven,servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("10");
                                    soap_manager.Sync_tabla_grupoProducto(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("20");
                                    soap_manager.Sync_tabla_tipoProducto(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("22");
                                    //soap_manager.Sync_tabla_mta_kardex(codven,servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("25");
                                    soap_manager.Sync_tabla_promocion_clientes(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("32");
                                    soap_manager.Sync_tabla_promocion_vendedor(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("40");
                                    soap_manager.Sync_tabla_promocion_ubigeo(codven,servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    //soap_manager.Sync_tabla_promocion_politica(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("45");
                                    soap_manager.Sync_tabla_PromocionDetalle(codven,servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("50");
                                    soap_manager.Sync_tabla_familia(servidorBD,nombreBD, usuarioBD, contrasenaBD);
                                    publishProgress("55");
                                    soap_manager.Sync_tabla_sub_familia(servidorBD,nombreBD, usuarioBD, contrasenaBD);
                                    publishProgress("60");
                                    soap_manager.Sync_tabla_unidad_medida(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("62");
                                    soap_manager.Sync_tabla_productoNoDescuento(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("75");
                                    //SERVER .212
                                    soap_manager.Sync_tabla_politica_cliente(codven, servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("80");
                                    //soap_manager.Sync_tabla_politica_precio1(servidorBD_ERP, nombreBD_ERP, usuarioBD_ERP, contrasenaBD_ERP);
//                                    publishProgress("75");
                                    //SERVER .212
                                    soap_manager.Sync_tabla_politica_precio2(codven, servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    publishProgress("95");
                                    //soap_manager.Sync_tabla_politica_vendedor(servidorBD, nombreBD, usuarioBD,contrasenaBD);
                                    soap_manager.Sync_Bonificacion_Colores(servidorBD, nombreBD, usuarioBD,contrasenaBD);

                                    publishProgress("100");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    error = "1";
                                }

                                break;

                            case 3:

                                try {
                                    publishProgress("1");

                                    soap_manager.Sync_tabla_formasPago(codven,
                                            servidorBD, nombreBD, usuarioBD,
                                            contrasenaBD);
                                    publishProgress("19");

                                    soap_manager.Sync_tabla_cta_ingresos_resumen(codven,
                                            servidorBD, nombreBD, usuarioBD,
                                            contrasenaBD);
                                    publishProgress("25");

                                    soap_manager.Sync_tabla_banco(servidorBD,
                                            nombreBD, usuarioBD, contrasenaBD);

                                    publishProgress("75");
                                    soap_manager.Sync_tabla_Nro_letras("10008728084", codven);

                                    publishProgress("90");
                                    soap_manager.Sync_tabla_ctas_xbanco(servidorBD,
                                            nombreBD, usuarioBD, contrasenaBD);
                                    publishProgress("100");
                                    soap_manager.Sync_tabla_ingresos(codven,
                                            servidorBD, nombreBD, usuarioBD,
                                            contrasenaBD);
                                    publishProgress("50");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    error = "1";
                                }

                                break;
                            default:

                                break;
                        }
                    }

                    //
                    publishProgress("1");

                    String fecha = GlobalFunctions
                            .getFecha_configuracion(getApplicationContext());
                    String descp = "";

                    if (error.equals("0")) {
                        descp = "OK";
                    } else {
                        descp = "ERROR";
                    }

                    descp = descp + " " + modulos_seleccionados;

                    try {
                        soap_manager.actualizarLogSincro(codven, datetime,
                                fecha, descp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        publishProgress("100");
                    }
                    //

                } else {

                    error = "2";

                }

            }

            return error;

        }

        protected void onProgressUpdate(String... progress) {
            Log.d("SINCRONIZAR ACTIVITY", progress[0]);
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute=", "" + result);

            if (result.equals("0")) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(
                        SincronizarActivity.this);
                alerta.setMessage("Sincronizacion correcta");
                alerta.setIcon(R.drawable.check);
                alerta.setCancelable(false);
                alerta.setPositiveButton("OK", null);
                alerta.show();

                editor_preferencias = preferencias_configuracion.edit();
                editor_preferencias.putBoolean("preferencias_sincronizacionCorrecta", true);
                editor_preferencias.commit();

            } else if (result.equals("2")) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(
                        SincronizarActivity.this);
                alerta.setMessage("Sin conexion al Servidor:\n"
                        + GlobalVar.urlService);
                alerta.setIcon(R.drawable.check);
                alerta.setCancelable(false);
                alerta.setPositiveButton("OK", null);
                alerta.show();

                editor_preferencias = preferencias_configuracion.edit();
                editor_preferencias.putBoolean("preferencias_sincronizacionCorrecta", false);
                editor_preferencias.commit();

            } else if (result.equals("3")) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(
                        SincronizarActivity.this);
                alerta.setMessage("No se pueden actualizar los servicios\n");
                alerta.setIcon(R.drawable.check);
                alerta.setCancelable(false);
                alerta.setPositiveButton("OK", null);
                alerta.show();

                editor_preferencias = preferencias_configuracion.edit();
                editor_preferencias.putBoolean("preferencias_sincronizacionCorrecta", false);
                editor_preferencias.commit();

            } else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(
                        SincronizarActivity.this);
                alerta.setMessage("Algunas tablas no se sincronizaron correctamente\nSincronice nuevamente");
                alerta.setIcon(R.drawable.check);
                alerta.setCancelable(false);
                alerta.setPositiveButton("OK", null);
                alerta.show();

                editor_preferencias = preferencias_configuracion.edit();
                editor_preferencias.putBoolean("preferencias_sincronizacionCorrecta", false);
                editor_preferencias.commit();
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soap_manager != null) {
            soap_manager.dbclass.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
    }

    public void mostrarServicios() {
        al_servidor = new ArrayList<DB_Servidor>();
        al_servidor = _helper.getServicios();

        Iterator<DB_Servidor> it = al_servidor.iterator();
        int i = 0;

        if (al_servidor.isEmpty()) {
            nomServicio = new String[1];
            codServicio = new String[1];
            nomServicio[i] = "Sin datos";
            codServicio[i] = "0";
        } else {
            nomServicio = new String[al_servidor.size()];
            codServicio = new String[al_servidor.size()];
        }

        while (it.hasNext()) {
            Object objeto = it.next();
            DB_Servidor dbbanco = (DB_Servidor) objeto;
            nomServicio[i] = dbbanco.getTX_SERV_servicioWeb();
            codServicio[i] = dbbanco.getIN_SERV_codigo_ID() + "";
            Log.d("SincronizarActivity:mostrarServicios:","nombreServicio: " + nomServicio[i]);
            Log.d("SincronizarActivity:mostrarServicios:","codigoServicio: " + codServicio[i]);
            i++;
        }

        for (int j = 0; j < nomServicio.length; j++) {
            Log.i("Sincronizar", "nombreServicio "+j+" : " + nomServicio[j]);
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,nomServicio);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spn_servicio.setAdapter(adapter);

        Log.d("SincronizarActivity:mostrarServicios:","Adapter colocado");
        // mostrarCondPago();
    }

    public void mostrarServicios2() {
        al_servidor = new ArrayList<DB_Servidor>();
        al_servidor = _helper.getServicios();

        Iterator<DB_Servidor> it = al_servidor.iterator();
        int i = 0;

        if (al_servidor.isEmpty()) {
            nomServicio_local = new String[1];
            codServicio_local = new String[1];
            nomServicio_local[i] = "Sin datos";
            codServicio_local[i] = "0";
        } else {
            nomServicio_local = new String[al_servidor.size()];
            codServicio_local = new String[al_servidor.size()];
        }

        while (it.hasNext()) {
            Object objeto = it.next();
            DB_Servidor dbbanco = (DB_Servidor) objeto;
            nomServicio_local[i] = dbbanco.getTX_SERV_servicioWeb();
            codServicio_local[i] = dbbanco.getIN_SERV_codigo_ID() + "";
            i++;
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,nomServicio_local);

        adapter.setDropDownViewResource(R.layout.spinner_item);
        spn_servicio_local.setAdapter(adapter);

        // mostrarCondPago();
    }

    public int buscarSecuenciaxServicio(String serv) {
        int result = -1;
        for (int i = 0; i < nomServicio.length; i++) {
            if (nomServicio[i].equalsIgnoreCase(serv)) {

                return i;
            }
        }
        return result;
    }

    @Override
    public void onBackPressed() {

        ConfirmarServicios();

    }
    void ConfirmarServicios(){
        if (!settings_spn_servicio.isChecked() && !chk_secundario.isChecked()) {

            AlertDialog.Builder dialogo = new AlertDialog.Builder(
                    SincronizarActivity.this);
            dialogo.setMessage("Debe seleccionar almenos un servicio");
            dialogo.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });
            dialogo.create();
            dialogo.show();

        } else {

            if (GlobalVar.urlService == null
                    || GlobalVar.urlService.length() == 0) {
                cargarDialogoPreferencias2();
            } else {
                // Verifico el origen de la activity, si es diferente de menu,
                // vuelvo al LOGIN 06-07-2013
                if (origen.equals("MENU") || origen.equals("LOGIN_FIRST")) {
                    finish();
                    Intent i = new Intent(getApplicationContext(),
                            MenuPrincipalActivity.class);
                    i.putExtra("codven", codven);

                    startActivity(i);
                } else if (origen.equals("LOGIN")) {
                    finish();
                }
            }

        }
    }
    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Alternativa 1
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_sincronizar, menu);
            return true;
        }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ConfirmarServicios();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cambiarServicio() {
        // TODO Auto-generated method stub
        dialogoCambiarServicio();
    }

    Dialog bdialog;

    EditText edt_nuevoServicio;
    Button btn_cambiar, btn_cancelar;
    String tipofiltro = "cliente";

    protected void dialogoCambiarServicio() {

        bdialog = new Dialog(SincronizarActivity.this,
                android.R.style.Theme_Translucent);
        bdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        bdialog.setCancelable(true);
        bdialog.setContentView(R.layout.dialog_cambiarservicio);

        edt_nuevoServicio = (EditText) bdialog
                .findViewById(R.id.dialog_cambiarServicio_edtServ);
        btn_cambiar = (Button) bdialog
                .findViewById(R.id.dialog_cambiarServicio_btnAceptar);
        btn_cancelar = (Button) bdialog
                .findViewById(R.id.dialog_cambiarServicio_btnCancelar);
        btn_cambiar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                edt_contrasenia.setEnabled(true);
                edt_servidor.setEnabled(true);
                edt_nombrebd.setEnabled(true);
                edt_usuario.setEnabled(true);

                edt_contrasenia.setText("");
                edt_servidor.setText("");
                edt_nombrebd.setText("");
                edt_usuario.setText("");
                spn_servicio.setVisibility(View.GONE);
                edt_servicio.setVisibility(View.GONE);
                edt_servicio.setText(edt_nuevoServicio.getText().toString());
                bdialog.dismiss();
            }
        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bdialog.dismiss();
                edt_contrasenia.setEnabled(false);
                edt_servidor.setEnabled(false);
                edt_nombrebd.setEnabled(false);
                edt_usuario.setEnabled(false);
            }
        });

        bdialog.show();

    }

    @Override
    public void onDialogClickPositivo(String idAlmacenSeleccionado,	String almacenSeleccionado, String vistaSeleccionado, boolean stockAuto, boolean precioCero) {

        Gson gson = new Gson();

        Map<String, Object> mapObject = new HashMap<>();
        List<Object> almacenAsociado = new ArrayList<Object>();
        almacenAsociado.add(idAlmacenSeleccionado);
        almacenAsociado.add(almacenSeleccionado);

        mapObject.put("preferencias_almacen", almacenAsociado);
        mapObject.put("preferencias_vista", vistaSeleccionado);
        mapObject.put("preferencias_stock", stockAuto);

        String preferenciasJSON = gson.toJson(mapObject);
        editor_preferencias = preferencias_configuracion.edit();
        editor_preferencias.putString("preferenciasJSON", preferenciasJSON);
        editor_preferencias.putBoolean("preferencias_precioCero", precioCero);
        editor_preferencias.commit();
        // Log.d("JSON:",cadena);

    }

    @Override
    public void onDialogClickNegativo() {

    }
    // ---------------------------------------------------

    private void showRequestPermisionToAccesStorage() {

        int permissionCheckFineLocation = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckCoarseLocation= ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int packag= PackageManager.PERMISSION_GRANTED;

        if (permissionCheckFineLocation !=  packag       &&         permissionCheckCoarseLocation!=packag) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, 100);
            } else {
                requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, 100);
            }
        } else {
            //Toast.makeText(getApplicationContext(), "Permiso ya fue Concedido", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults) {
        boolean ReqpuesPer=false;
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permiso","Permiso concedido");
                    //Toast.makeText(getApplicationContext(), "Permiso concedido", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "No podras generar backup ", Toast.LENGTH_SHORT).show();
                }
        }

    }

    private void requestPermission(String permissionName, String permissionName2, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName, permissionName2}, permissionRequestCode);
    }


    public void MENSAJES(String mensaje, String mensajeBTN, int error, boolean cancelable, final int action) {
        if (error == 1) mensaje = "Verifica tu conexion a internet\ny vuelva a intentar de nuevo.";
        else if (error == 2) mensaje = "No hay conexion al servidor.\nVuelva a intentar mas tarde";
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(SincronizarActivity.this);
        dialog.setCancelable(cancelable);
        dialog.setMessage(mensaje);
        dialog.setPositiveButton(""+mensajeBTN, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setNegativeButton("Cancelar", null);
        dialog.create();
        dialog.show();
    }

}
