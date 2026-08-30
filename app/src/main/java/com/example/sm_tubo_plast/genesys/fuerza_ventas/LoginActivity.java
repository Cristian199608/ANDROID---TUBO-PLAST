package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultLogin;
import com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultPrecioArticulo;
import com.example.sm_tubo_plast.genesys.Retrofit.RetrofilClientCantol;
import com.example.sm_tubo_plast.genesys.Retrofit.request.GetDataCantol;
import com.example.sm_tubo_plast.genesys.Retrofit.request.RequestCantol;
import com.example.sm_tubo_plast.genesys.Retrofit.request.producto.RequestProducto;
import com.example.sm_tubo_plast.genesys.Retrofit.util.WS_RetrofitCustom;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBUsuarios;
import com.example.sm_tubo_plast.genesys.datatypes.DB_Empresa;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog.BottomSheetDialogBuscarProductoVenta;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog.BottomSheetGeolocalizarCliente;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog.BottomSheetMapDialog;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.service.SampleAlarmReceiver;
import com.example.sm_tubo_plast.genesys.session.SessionManager;
import com.example.sm_tubo_plast.genesys.util.FontManager;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class LoginActivity extends AppCompatActivity {

    private ToggleButton tb_user;
    private Button boton;
    private EditText txtUsuario, txtPassword, txtPlaca;
    TextView txtRuc;
    private RelativeLayout lyt_form_login, Lyout_saemovil2018, RlayoutContactos;
    private ImageView logo;
    LinearLayout ly_placa;
    DBclasses dbusuarios;
    // DBclasses2 dbfuerza_ventasbk;
    ProgressDialog pDialog;
    String Usuario;
    String Contrasena;
    String codVendedor = "";
    SessionManager session;
    DBSync_soap_manager soap_manager;
    Spinner spn_placas;
    int auxi = 0;
    String rucString;
    TextView tv_titulo, txt_imei_celular, tv_app_version;
    int login = -1;
    SampleAlarmReceiver sar;

    FloatingActionButton myFABlogin_configuraciones;
    CheckBox ckRecordarInicioSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        // para que no salga el teclado al iniciar

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.lyt_form_login), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.RlayoutContactos), iconFont);

        //
        // dbfuerza_ventasbk = new DBclasses2(LoginActivity.this);
        //
//
//        Intent intent=new Intent(this, SeguimientoPedidoActivity.class);
//        intent.putExtra("ID_RRHH", "codcli");
//        intent.putExtra("CODIGO_CRM", "");
//        intent.putExtra("NOMBRE_INSTI", "Sin nombre");
//        intent.putExtra("OC_NUMERO", "");
//        intent.putExtra("COD_VEND", "codven");
//        intent.putExtra("ORIGEN", ClientesActivity.TAG);
//        startActivity(intent);





        dbusuarios = new DBclasses(LoginActivity.this);
        dbusuarios.EliminaOldDatabase();

        // txtPlaca=(EditText)findViewById(R.id.txtPlaca);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtRuc = (TextView) findViewById(R.id.txtRUC);
        tv_titulo = (TextView) findViewById(R.id.login_tvtitulo);

        boton = (Button) findViewById(R.id.btningresar);
        tb_user = (ToggleButton) findViewById(R.id.ToggleButton01);
        spn_placas = (Spinner) findViewById(R.id.main_spn_placa);
        // empieza animacion
        lyt_form_login = (RelativeLayout) findViewById(R.id.lyt_form_login);
        logo = (ImageView) findViewById(R.id.login_LogoPrincipal);
        tv_app_version = (TextView) findViewById(R.id.tv_app_version);
        ly_placa = (LinearLayout) findViewById(R.id.inicio_ly_placa);
        Lyout_saemovil2018 =  findViewById(R.id.Lyout_saemovil2018);
        myFABlogin_configuraciones =  findViewById(R.id.myFABlogin_configuraciones);
        RlayoutContactos =  findViewById(R.id.RlayoutContactos);
        txt_imei_celular =  findViewById(R.id.txt_imei_celular);
        ckRecordarInicioSession =  findViewById(R.id.ckRecordarInicioSession);
        Animation animacion = AnimationUtils.loadAnimation(this, R.anim.animacion);
        logo.startAnimation(animacion);

        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.animacion2);
        lyt_form_login.startAnimation(animacion2);
        // mostrarPlacas();
        // termina animacion
        rucString = dbusuarios.getRuc();


        myFABlogin_configuraciones.startAnimation(animacion2);
        Lyout_saemovil2018.startAnimation(animacion2);
        RlayoutContactos.startAnimation(animacion2);
        txt_imei_celular.startAnimation(animacion2);
        ckRecordarInicioSession.startAnimation(animacion2);


        Animation animacionforAppversion = AnimationUtils.loadAnimation(this, R.anim.anim_ocultar_aparecer);
        tv_titulo.startAnimation(animacionforAppversion);
        tv_app_version.startAnimation(animacionforAppversion);

        // dbfuerza_ventasbk.getClientexCodigo("");
        //

        txtRuc.setInputType(InputType.TYPE_CLASS_NUMBER);
        tb_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (((ToggleButton) v).isChecked()) {
                    DisplayToast("Usuario");
                    txtUsuario.setHint("Usuario");
                    ly_placa.setVisibility(View.VISIBLE);
                    // txtPlaca.setVisibility(8);

                } else {
                    // Chofer
                    txtUsuario.setHint("Chofer");
                    // txtPlaca.setVisibility(0);
                    ly_placa.setVisibility(View.GONE);
                    DisplayToast("Chofer");

                }
            }
        });

        // Session Manager
        session = new SessionManager(getApplicationContext());
        ckRecordarInicioSession.setChecked(session.getRecordarInicioSession());
        txtUsuario.setText(session.getUsuario());
        if (ckRecordarInicioSession.isChecked()){
            txtUsuario.setText(session.getUsuario());
            txtPassword.setText(session.getPassword());
        }

        testConeccionInternet(100);



        boton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // sar = new SampleAlarmReceiver();
                // sar.setAlarm(getApplicationContext());

                Usuario = txtUsuario.getText().toString().trim();
                Contrasena = txtPassword.getText().toString().trim();
                rucString = dbusuarios.getRuc();
                if(Usuario.isEmpty() || Contrasena.isEmpty()){
                    GlobalFunctions.showCustomToast(LoginActivity.this, "Ingrese usuario y/o contraseña", GlobalFunctions.TOAST_DONE);
                    return;
                }
                loginContol(Usuario, Contrasena);
                if(true)return;
                String ruc = "";
                // verificamos si estan en blanco
                if (checklogindata(Usuario, Contrasena) == true) {
                    if (login == 0 || login == 1) {
                        if (txtRuc.getText().toString().length() == 0) {
                            txtRuc.setError("Ingrese el ruc");
                        } else if (txtRuc.getText().toString().trim().length() != 11
                                && rucString.length() <= 1) {
                            txtRuc.setError("Ingrese 11 digitos");
                        } else {
                            // new asynclogin().execute(Usuario,Contrasena);
                            new asyncVerificarRuc().execute(txtRuc.getText()
                                    .toString());
                        }
                    } else {
                        // si pasamos esa validacion ejecutamos el asynctask
                        // pasando el usuario y clave como parametros
                        new asynclogin().execute(Usuario, Contrasena);
                    }

                } else {
                    // si detecto un error en la primera validacion vibrar y
                    // mostrar un Toast con un mensaje de error.
                    err_login();
                }

            }
        });



        DB_Empresa entityEmpresa = new DB_Empresa();
        entityEmpresa = dbusuarios.getEmpresa();

        if (rucString == null || rucString.length() <= 1
                || rucString.equalsIgnoreCase("error")) {
            auxi = 1;
            login = 0; // BD SQLite esta vacia
            tv_titulo.setVisibility(View.GONE);
        } else {
            txtRuc.setText(rucString);
            tv_titulo.setVisibility(View.VISIBLE);
            tv_titulo.setText(entityEmpresa.getEmpresa());
            txtRuc.setEnabled(false);

        }
    cambiarRutaHttp();
        setDataPrueba();
//
//        String carpeta_contenedor =getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + "/"+ VARIABLES.CARPERTA_CONTENEDOR+"/"+VARIABLES.CARPERTA_CONTENEDOR_PDF+"/";
//        String nombreArchivo="pdfckdnkcnxxdkcn.pdf";
//        String htmlData= PDF.createHtml(this);
//
//        ViewPdfFromHtmlActivity.Companion.startViewPdfFromHtmlActivity(
//                this,
//                ""+htmlData,
//                ""+carpeta_contenedor,
//                ""+nombreArchivo
//        );


    }
    private void cambiarRutaHttp(){
        dbusuarios.cambiarRutaHttpServicioWeb();
    }
    private void setDataPrueba(){
        boolean prueba = VARIABLES.isSetDataPruebas;
        if(!prueba) return;
        dbusuarios.setDataPruebas();
        testMapa();
//        DAO_RegistroBonificaciones daoReg=new DAO_RegistroBonificaciones(getApplicationContext());
//        ArrayList<DB_RegistroBonificaciones>  lis=daoReg.getRegistroBonificacionesClonarBy("V3225082903");

        txtUsuario.setText("SAEMOVIL");
        txtPassword.setText("XQC2WYG");

//        txtUsuario.setText("SAEMOVIL_INST");
//        txtPassword.setText("5QUOLZU");
//        sincronizarProductoPrecio();
        //testMapa();

    }
    public void GoSincronizarInicial(View view){
        Intent iconfig = new Intent(getApplicationContext(),SincronizarActivity.class);
        // Guardo el origen de la SincronizacionActivity, para luego
        // poder saber a que activity regresar 06-07-2013
        iconfig.putExtra("ORIGEN", "LOGIN");
        //
        startActivity(iconfig);
    }
    private void DisplayToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showCustomDialog() {
        Dialog dialog = new Dialog(this, R.style.Theme_CustomDialog);

        dialog.setContentView(R.layout.amu_webview  );
        // ...

        dialog.show();
    }

    public int loginstatus(String username, String password) {
        int p = 0;
        // DBclasses dbhelper=new DBclasses(getApplicationContext());
        // dbhelper.getReadableDatabase();

        ArrayList<DBUsuarios> list_Usuarios = new ArrayList<DBUsuarios>();
        list_Usuarios = dbusuarios.getUsuarios(username, password);

        if (list_Usuarios.size() != 0) {
            codVendedor = dbusuarios.getCodVendedor(username, password);
            dbusuarios.close();
            if (codVendedor.length() != 0) {
                p = 1; // Existe vendedor y usuario
            } else
                p = 3; // No existe vendeor pero si usuario
        }

        else {
            p = 2; // No Existe usuario
        }

        return p;
    }

    public void err_login() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast toast1 = Toast.makeText(getApplicationContext(),
                "Error:Nombre de usuario o password incorrectos",
                Toast.LENGTH_SHORT);
        toast1.show();
    }

    // validamos si no hay ningun campo en blanco
    public boolean checklogindata(String username, String password) {

        if (username.equals("") || password.equals("")) {
            Log.e("Login ui", "checklogindata user or pass error");
            return false;

        } else {

            return true;
        }
    }

    class asynclogin extends AsyncTask<String, String, String> {

        String user, pass;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass
            user = params[0];
            pass = params[1];

            // enviamos y recibimos y analizamos los datos en segundo plano.
            if (loginstatus(user, pass) != 2) {// Si el usuario Existe

                if (loginstatus(user, pass) == 1) {// Es un vendedor
                    ConnectionDetector connection = new ConnectionDetector(
                            getApplicationContext());
                    return "vendedor";
                } else {

                    return "chofer";
                }
            } else {
                return "error"; // login invalido
            }

        }

        /*
         * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
         * a la sig. activity o mostramos error
         */
        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute=", "" + result);


            String mensajeLicenciaUso= dbusuarios.getConfiguracionByName("mensaje_licencia_uso", "");

            if (mensajeLicenciaUso.length()>0){
                UtilViewMensaje.MENSAJE_simple(LoginActivity.this, "AVISO", mensajeLicenciaUso
                        +"\n\nSincronize usuario/configuracion para volver a validar.");
                return;
            }

            if (result.equals("vendedor")) {
                guardarDatosSession(user, pass, codVendedor, "", "");
            } else if (result.equals("chofer")) {
                Intent intentChofer = new Intent(getApplicationContext(),
                        MenuLiquidacionActivity.class);
                intentChofer.putExtra("codven", codVendedor);
                startActivity(intentChofer);
            }

            else {
                err_login();
            }

        }
    }

    private void guardarDatosSession(String user, String pass, String codVendedor, String nombreVendedor, String token){
        session.setRecordarInicioSession(ckRecordarInicioSession.isChecked());
        session.createLoginSession(user, pass);
        session.setCodigoVendedor(codVendedor);
        session.setNombreVendedor(nombreVendedor);
        session.setToken(token);

        dbusuarios.registrarDatosUsuarioLogin(codVendedor, nombreVendedor);
        SincronizarActivity.AsignarPreferenciaCodigoNivel(dbusuarios, LoginActivity.this);

        Intent intentVendedor = new Intent(getApplicationContext(),
                MenuPrincipalActivity.class);
        intentVendedor.putExtra("codven", codVendedor);

        startActivity(intentVendedor);
    }

    // ??
    /*
     * public void mostrarPlacas(){
     *
     * String placas[] = dbusuarios.getPlacas();
     *
     * ArrayAdapter<CharSequence> adapter = new
     * ArrayAdapter<CharSequence>(getApplicationContext(),
     * android.R.layout.simple_spinner_item, placas);
     *
     * adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item
     * ); spn_placas.setAdapter(adapter);
     *
     * }
     */

    class asyncVerificarRuc extends AsyncTask<String, String, String> {

        String ruc;
        int valor = 0;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass
            ruc = params[0];
            DBSync_soap_manager _soap = new DBSync_soap_manager(
                    LoginActivity.this);

            ConnectionDetector connection = new ConnectionDetector(
                    getApplicationContext());
            valor = 0;
            DB_Empresa entityEmpresa = new DB_Empresa();
            if (connection
                    .hasActiveInternetConnection2() == true) {
                if (login == 0) {
                    valor = _soap.getEmpresa(ruc);
                    if (valor == 1) {
                        entityEmpresa = dbusuarios.getEmpresa();
                        valor = valor
                                + _soap.loginOnline(entityEmpresa.getUsuario(),
                                entityEmpresa.getClave(),
                                entityEmpresa.getBd(), Usuario,
                                Contrasena);
                        if (valor == 2) {
                            _soap.Sync_tabla_vendedoresOnline("localhost",
                                    entityEmpresa.getBd(),
                                    entityEmpresa.getUsuario(),
                                    entityEmpresa.getClave());
                            _soap.Sync_tabla_usuarios_Online("localhost",
                                    entityEmpresa.getBd(),
                                    entityEmpresa.getUsuario(),
                                    entityEmpresa.getClave());
                            codVendedor = dbusuarios.getCodVendedor(Usuario,
                                    Contrasena);
                        }
                    }
                }

                if (login == 1) {
                    valor = 1;
                    entityEmpresa = dbusuarios.getEmpresa();
                    valor = valor
                            + _soap.loginOnline(entityEmpresa.getUsuario(),
                            entityEmpresa.getClave(),
                            entityEmpresa.getBd(), Usuario, Contrasena);
                }
            }
            return "" + valor;
        }

        protected void onPostExecute(String result) {

            pDialog.dismiss();
            Log.e("onPostExecute=", "" + result);
            DB_Empresa entityEmpresa = new DB_Empresa();
            if (result.equals("1")) {
                login = 1;

                entityEmpresa = dbusuarios.getEmpresa();
                txtRuc.setText(entityEmpresa.getEmpresa());
                txtRuc.setEnabled(false);
                tv_titulo.setVisibility(View.VISIBLE);
                tv_titulo.setText(entityEmpresa.getEmpresa());
            } else if (result.equals("2")) {
                login = 2;

                tv_titulo.setVisibility(View.VISIBLE);
                tv_titulo.setText(entityEmpresa.getEmpresa());
                entityEmpresa = dbusuarios.getEmpresa();
                txtRuc.setText(entityEmpresa.getEmpresa());
                // loginstatus(Usuario,Contrasena);

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        LoginActivity.this);
                alertDialog.setTitle("Verificacion Correcta");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Empresa: " + entityEmpresa.getEmpresa()
                        + "\n" + "Sincronize informaci�n.");
                alertDialog.setIcon(R.drawable.ic_check);
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();
                                Intent i = new Intent(getApplicationContext(),
                                        SincronizarActivity.class);
                                i.putExtra("ORIGEN", "LOGIN_FIRST");
                                i.putExtra("codven", codVendedor);
                                startActivity(i);
                            }
                        });
                alertDialog.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        });
                alertDialog.show();

            } else {
                tv_titulo.setVisibility(View.GONE);
                txtRuc.setError("ruc incorrecto");
                login = 0;
                Log.w("INCORRECTO", "");
            }

        }
    }

    private void testConeccionInternet(int contador){

        /*
        Calendar fecha1 = Calendar.getInstance();
        fecha1.set(2023, 1, 1); // 1 de marzo de 2023

        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2023, 3, 1); // 1 de abril de 2023

        // Se convierten los Calendar a Date para obtener la diferencia en milisegundos

        Date fecha1Date = fecha1.getTime();
        Date fecha2Date = fecha2.getTime();

        long diffMilisegundos = fecha2Date.getTime() - fecha1Date.getTime();
        long diffDias = diffMilisegundos / (24 * 60 * 60 * 1000); // Se divide entre los milisegundos de un día

        Log.i("DIAS", "diffDias: "+diffDias);
*/
        if (contador>0) return;

        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Enviando datos al sistema.... Cont: "+contador);
        pDialog.setIndeterminate(false);
        pDialog.show();
        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... voids) {

                ConnectionDetector cd = new ConnectionDetector(LoginActivity.this);
                 if (cd.hasActiveInternetConnection(LoginActivity.this)) {
                     Log.i("TEST INTERNET", "start");
                     try {
                         Thread.sleep(500);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.i("TEST INTERNET", "finish");
                pDialog.dismiss();
                if(contador>=0)testConeccionInternet(contador-1);
            }
        }.execute();
    }

    private void loginContol(String user, String pass){

        RequestBody body = RetrofilClientCantol.createBodyJson(RequestCantol.login(user, pass));
        Call<Object> call = RetrofilClientCantol.getRetrofitInstance().create(GetDataCantol.class).getLoginToken(body);

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Autenticando...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        WS_RetrofitCustom ws = new WS_RetrofitCustom(this);
        ws.setIsLogin(true);
        ws.StartPeticion(call, new WS_RetrofitCustom.MyListener() {
            @Override
            public void StartFinish(boolean isFinish) {
                if (!isFinish) pDialog.show();
                else pDialog.dismiss();
            }
            @Override
            public void Result(boolean isOk, String mensajeError, Object data) {
                if(!isOk){
                    GlobalFunctions.showCustomToast(
                            LoginActivity.this,
                            mensajeError,
                            GlobalFunctions.TOAST_ERROR);
                    return;
                }
                Gson gson=new Gson();
                ResultLogin resultLogin= gson.fromJson(gson.toJson(data), ResultLogin.class);
                guardarDatosSession(user, pass,
                        String.valueOf((int) Double.parseDouble(resultLogin.getVendedor().getCodigo())),
                        resultLogin.getVendedor().getNombre(),
                        resultLogin.getToken()
                        );
            }
        });
    }


    private void testMapa(){
        final Intent i = new Intent(getApplicationContext(),PedidosActivity.class);
        i.putExtra("origen", "CLIENTES");
        i.putExtra("nombreCliente", "COMERCIAL CASA FUERTE S.A.C.");
        i.putExtra("codcli", "C20610544461");
        i.putExtra("codigoVendedor", "33");
        i.putExtra("tipoRegistro", PedidosActivity.TIPO_PEDIDO);
        startActivity(i);
//        BottomSheetDialogBuscarProductoVenta dd=BottomSheetDialogBuscarProductoVenta.newInstance(
//                "33", "oc_numero", "FERRETERIA A");
//        dd.show(getSupportFragmentManager(), "ddd");
    }

}
//0199631