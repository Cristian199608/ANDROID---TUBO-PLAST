package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.FormaPago;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistrosGeneralesMovil;
import com.example.sm_tubo_plast.genesys.datatypes.DBBanco;
import com.example.sm_tubo_plast.genesys.datatypes.DBCta_Ingresos;
import com.example.sm_tubo_plast.genesys.datatypes.DBCtas_xbanco;
import com.example.sm_tubo_plast.genesys.datatypes.DBIngresos;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesActivity;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.google.gson.JsonParseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

@SuppressLint("LongLogTag")
public class AmortizarCuentasXCobrarActivity2 extends AppCompatActivity implements OnClickListener {

    DBCta_Ingresos entityCtaIngresos_Actual;
    DBIngresos entityIngresos_Actual;
    DAO_RegistrosGeneralesMovil DAO_registrosGeneralesMovil;

    ArrayList<FormaPago> listaFormaPago;
    ArrayList<DBBanco> listaBancos;
    ArrayList<DBCtas_xbanco> listaCuentas_x_Banco;

    String nomban[];
    String codban[];
    String[] num_cta;
    String[] moneda;


    double acuenta2 = 1;
    double monto_afecto = 1;
    double saldoo = 0;

    String usuari, origen;
    String codcli, nomcli;
    String sec_ingresos, codmon = "";
    String codmondoc = "";
    String numfactura, saldo, total, acuenta_total, saldo_virtual,codigo_forma_pago,nombre_forma_pago,tipoDocumento,serie  , numero;
    String codven = "";
    String fecha_configuracion = "";
    String codigoBanco = "0";
    String codigoCuentaBanco = "0";

    int[] cod_cta;
    int sec_cta = 0;
    int id_banco = 0;

    LinearLayout ly_operacion;
    LinearLayout ly_banco;
    LinearLayout ly_ctaBanco;
    Button btn_agregar;

    Spinner spn_pago, spn_cta_x_banco, spn_banco;
    private static final int Fecha_Dialog_ID = 1;
    double valor_cambio;

    DBclasses obj_dbclasses;
    ProgressDialog pDialog;
    RadioGroup rb_moneda;
    RadioButton rbtn_soles;
    RadioButton rbtn_dolares;

    Calendar calendar = Calendar.getInstance();

    public EditText edt_FechaEntrega, edt_secuencia, edt_saldo, edt_acuenta,
            edt_numope, edt_igv;

    TextView lblSaldo ;

    // FECHA ACTUAL
    int año_act;
    int mes_act;
    int dia_act;
    int dia, mes, año;

    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            año = year;
            mes = monthOfYear;
            dia = dayOfMonth;

            int año_act = calendar.get(Calendar.YEAR);
            int mes_act = calendar.get(Calendar.MONTH);
            int dia_act = calendar.get(Calendar.DAY_OF_MONTH);

            edt_FechaEntrega.setText("");

            if (año > año_act) {
                ColocarFecha();
            } else if (año == año_act && mes > mes_act) {
                ColocarFecha();
            } else if (año == año_act && mes == mes_act && dia >= dia_act) {
                ColocarFecha();
            } else {
                error();
            }
        }

        public void error() {
            Toast.makeText(getApplicationContext(), "Fecha Invalida",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amortizar_cuentas_x_cobrar2);


        SharedPreferences prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);

        codven = prefs.getString("codven", "por_defecto");

        obj_dbclasses = new DBclasses(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        numfactura = "" + bundle.getString("NUMFACTURA");
        total = "" + bundle.getString("TOTAL");

        saldo = "" + bundle.getString("SALDO");
        saldo_virtual = "" + bundle.getString("SALDO_VIRTUAL");
        codcli = "" + bundle.getString("CODCLI");
        Log.d("Cobranza","codcli:"+codcli);

        nomcli = "" + bundle.getString("NOMCLI");
        acuenta_total = "" + bundle.getString("ACUENTA_TOTAL");
        origen = "" + bundle.getString("ORIGEN");

        sec_ingresos = "" + bundle.getString("SECUENCIA");

        numero = bundle.getString("NUMFACTURA");
        serie = bundle.getString("SERIE");
        tipoDocumento = bundle.getString("TIPO");

        Log.i("Se recibe el parametro ",numero+", serie:"+serie+", tipo:"+tipoDocumento);



        codmondoc = obj_dbclasses.getCodmonxSecuencia(sec_ingresos);

        edt_secuencia = (EditText) findViewById(R.id.amortizar_et_secuencia);
        edt_FechaEntrega = (EditText) findViewById(R.id.amortizar_edtFechaPago);
        edt_saldo = (EditText) findViewById(R.id.amortizar_et_saldo);
        edt_acuenta = (EditText) findViewById(R.id.amortizar_et_acuenta);
        spn_pago = (Spinner) findViewById(R.id.amortizar_spn_pago);
        spn_cta_x_banco = (Spinner) findViewById(R.id.amortizar_spn_cta);
        spn_banco = (Spinner) findViewById(R.id.amortizar_spn_banco);
        btn_agregar = (Button) findViewById(R.id.amortizar_btn_agregar);
        Button btn_cancelar = (Button) findViewById(R.id.amortizar_btn_cancelar);
        edt_numope = (EditText) findViewById(R.id.amortizar_et_operacion);
        edt_igv = (EditText) findViewById(R.id.edt_igv);
        lblSaldo = (TextView)findViewById(R.id.lblSaldo);

        btn_agregar.setOnClickListener(this);
        rb_moneda = (RadioGroup) findViewById(R.id.amortizar_rb_moneda);
        rbtn_soles = (RadioButton) findViewById(R.id.rb_soles);
        rbtn_dolares = (RadioButton) findViewById(R.id.rb_dolares);

        // Connection Detector
        cd = new ConnectionDetector(getApplicationContext());

        DAO_registrosGeneralesMovil = new DAO_RegistrosGeneralesMovil(
                getApplicationContext());

        // obtener la fecha de la tabla configuracion
        fecha_configuracion = GlobalFunctions.getFecha_configuracion(getApplicationContext());

        mes_act = Integer.parseInt(fecha_configuracion.substring(3, 5));
        dia_act = Integer.parseInt(fecha_configuracion.substring(0, 2));
        año_act = Integer.parseInt(fecha_configuracion.substring(6, 10));

        edt_secuencia.setText("M" + calcularSecuencia());
        edt_saldo.setText(saldo_virtual);

        ly_operacion = (LinearLayout) findViewById(R.id.ly_operacion);
        ly_banco = (LinearLayout) findViewById(R.id.ly_banco);
        ly_ctaBanco = (LinearLayout) findViewById(R.id.ly_Cta_banco);

        colocarFechaActual();
        String tipo_de_cambio  = obj_dbclasses.getCambio("Tipo_cambio");
        Log.d("tipo_de_cambio", "--->"+tipo_de_cambio);
        valor_cambio = Double.parseDouble(tipo_de_cambio);
        Log.d("tipo_de_cambio", "DOUBLE V --->"+valor_cambio);
        edt_igv.setText(tipo_de_cambio);

        if (codmondoc.equals("1")) {
            codmon="1";
            rb_moneda.check(R.id.rb_soles);
            setTitle("N° Documento: " + numfactura+" (Soles)");
            lblSaldo.setText("Saldo S/.:");
        } else {
            codmon="2";
            rb_moneda.check(R.id.rb_dolares);
            setTitle("N° Documento: " + numfactura+" (Dólares)");
            lblSaldo.setText("Saldo $:");
        }




        mostrarFormasPago();

        spn_pago.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                codigo_forma_pago = listaFormaPago.get(arg2).getCodigoFormaPago();
                nombre_forma_pago = " "+listaFormaPago.get(arg2).getDescripcionFormaPago();
                Log.e("PAGOS",nombre_forma_pago+" codigo:"+codigo_forma_pago);


                if(nombre_forma_pago.indexOf("EFECTIVO") > 0) {// Ocultar
                    Log.e("PAGOS","Es efectivo");

                    ly_operacion.setVisibility(View.GONE);
                    ly_ctaBanco.setVisibility(View.GONE);
                    ly_banco.setVisibility(View.GONE);

                    if (codmondoc.equals("1")) {
                        codmon="1";
                        rb_moneda.check(R.id.rb_soles);
                    } else {
                        codmon="2";
                        rb_moneda.check(R.id.rb_dolares);
                    }

                }else if(nombre_forma_pago.indexOf("CHEQUE") > 0 || nombre_forma_pago.indexOf("LETRA") > 0 ) {
                    ly_ctaBanco.setVisibility(View.GONE);
                    ly_operacion.setVisibility(View.VISIBLE);
                    if (codmondoc.equals("1")) {
                        codmon="1";
                        rb_moneda.check(R.id.rb_soles);
                    } else {
                        codmon="2";
                        rb_moneda.check(R.id.rb_dolares);
                    }
                }else{
                    Log.e("PAGOS","No es efectivo");
                    ly_operacion.setVisibility(View.VISIBLE);
                    ly_banco.setVisibility( View.VISIBLE);
                    ly_ctaBanco.setVisibility(View.VISIBLE);

                    if (codmondoc.equals("1")) {
                        codmon="1";
                        rb_moneda.check(R.id.rb_soles);
                    } else {
                        codmon="2";
                        rb_moneda.check(R.id.rb_dolares);
                    }
                }
                //verificar la forma de pago
                mostrarBancos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        spn_cta_x_banco.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int item, long arg3) {

                //codigoCuentaBanco = listaCuentas_x_Banco.get(item).getSecuencia();
                codigoCuentaBanco  = listaCuentas_x_Banco.get(item).getCuenta();

                String text = spn_cta_x_banco.getSelectedItem().toString();
                for (int i = 0; i < num_cta.length; i++) {
                    if (text.equals(num_cta[i])) {
                        sec_cta = i;
                    }
                }
                rb_moneda.setEnabled(true);


                if (moneda[sec_cta].equals("1")) {
                    rb_moneda.check(R.id.rb_soles);
                } else {
                    rb_moneda.check(R.id.rb_dolares);
                }

                Log.e("COBRANZA",  listaCuentas_x_Banco.get(item).getCodmon()+" "+listaCuentas_x_Banco.get(item).getCta_cte()+"  SEC:"+codigoCuentaBanco);
                rb_moneda.setEnabled(false);
                rbtn_soles.setEnabled(false);
                rbtn_dolares.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        spn_banco.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                String text = spn_banco.getSelectedItem().toString();
                for (int i = 0; i < nomban.length; i++) {
                    if (text.equals(nomban[i])) {
                        id_banco = i;
                    }
                }
                codigoBanco =codban[id_banco];
                mostrarCtas_xBanco(codigoBanco);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        rb_moneda.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // CAMBIOS NOVIEMBRE
                // Antes 01 :Dolares 02:Soles ::::> Ahora 1:Soles 2:Dolares
                switch (arg1) {
                    case R.id.rb_soles:
                        codmon = "1";
                        break;

                    case R.id.rb_dolares:
                        codmon = "2";
                        break;

                    default:
                        break;
                }
            }
        });

        btn_cancelar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (origen.equals("clienteac")) {
                    finish();
                } else if (origen.equals("REPORTES")) {
                    obj_dbclasses.actualizarCabecera_Ingreso(""
                            + entityCtaIngresos_Actual.getSecuencia(), ""
                            + entityCtaIngresos_Actual.getSaldo(), Double
                            .parseDouble(entityCtaIngresos_Actual.getAcuenta()));
                    finish();
                } else {
					/*Intent i = new Intent(getApplicationContext(),
							CobranzaActivity2.class);
					startActivity(i);*/
                    finish();
                }

            }
        });

        if (origen.equals("REPORTES")) {

            edt_secuencia.setText(bundle.getString("SECITM"));
            edt_acuenta.setText(bundle.getString("ACUENTA_TOTAL"));
            sec_ingresos = bundle.getString("SECUENCIA");

            // OBtenemos la cabecera del detalle
            entityCtaIngresos_Actual = new DBCta_Ingresos();
            entityCtaIngresos_Actual = obj_dbclasses
                    .getCtaIngresos(sec_ingresos);

            // Obtenemos el detalle
            entityIngresos_Actual = new DBIngresos();
            entityIngresos_Actual = obj_dbclasses.getIngresos2(sec_ingresos,
                    edt_secuencia.getText().toString());

            if (entityIngresos_Actual.getCodmon().equals("1")) {
                // edt_saldo.setText(calcularDeudaModificar(""+obj_dbclasses.getSaldo_virtualxSecuencia(sec_ingresos),
                // edt_acuenta.getText().toString()));
                edt_saldo.setText(calcularDeudaModificar(""
                                + entityCtaIngresos_Actual.getSaldo_virtual(),
                        edt_acuenta.getText().toString()));
                saldo_virtual = "" + edt_saldo.getText().toString();

                // obj_dbclasses.actualizarCabecera_Ingreso(sec_ingresos, saldo,
                // (Double.parseDouble(entityCtaIngresos_Actual.getAcuenta())-Double.parseDouble(entityIngresos_Actual.getMonto_afecto()))
                // );
                rb_moneda.check(R.id.rb_soles);
            } else {
                rb_moneda.check(R.id.rb_dolares);
                edt_saldo.setText(""
                        + (Double.parseDouble(entityIngresos_Actual
                        .getMonto_afecto()) + Double
                        .parseDouble(entityCtaIngresos_Actual
                                .getSaldo_virtual())));
                saldo_virtual = "" + edt_saldo.getText().toString();
                // obj_dbclasses.actualizarCabecera_Ingreso(sec_ingresos, saldo,
                // (Double.parseDouble(entityCtaIngresos_Actual.getAcuenta())-Double.parseDouble(entityIngresos_Actual.getMonto_afecto()))
                // );
            }

            if (entityIngresos_Actual.getCodforpag().equals("04")) {
                spn_pago.setSelection(1);
                edt_numope.setText(entityIngresos_Actual.getNumope());

            }

            total = entityIngresos_Actual.getTotal();
            codmondoc = entityCtaIngresos_Actual.getCodmon();
            btn_agregar.setText("Modificar");

            //setTitle("AMORTIZAR - N° Documento: "+ entityCtaIngresos_Actual.getNumero_factura());


        }

    }

    private void mostrarFormasPago() {
        // TODO Auto-generated method stub
        ArrayList<CharSequence> formasPago = new ArrayList<CharSequence>();
        listaFormaPago = DAO_registrosGeneralesMovil.getFormasPago();

        for (int i = 0; i < listaFormaPago.size(); i++) {
            formasPago.add(listaFormaPago.get(i).getDescripcionFormaPago());
        }

        ArrayAdapter<CharSequence> spinner_adapter = new ArrayAdapter<CharSequence>( getApplicationContext(), R.layout.spinner_item, formasPago );
        spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
        spn_pago.setAdapter(spinner_adapter);
    }

    public String calcularDeudaModificar(String saldo, String acuenta) {
        Log.d("COBRANZA", "Convirtiendo en double " + saldo + " -- " + acuenta);
        return "" + (Double.parseDouble(saldo) + Double.parseDouble(acuenta));
    }

    public String calcularSecuencia() {

        String mes_actual = fecha_configuracion.substring(3, 5);
        String dia_actual = fecha_configuracion.substring(0, 2);

        String sec = obj_dbclasses.getSecitm(sec_ingresos);

        int secactual = 0;
        String cero = "0";

        if (sec.length() < 10) {
            secactual = 1;
            if (mes_actual.length() < 2) {
                mes_actual = cero + mes_actual;
            }
            if (dia_actual.length() < 2) {
                dia_actual = cero + dia_actual;
            }
            sec = codven + mes_actual + dia_actual + cero + secactual;
            return sec;
        } else {
            int diat = Integer.parseInt(sec.substring(8, 10));
            int mest = Integer.parseInt(sec.substring(6, 8));
            int sectem = Integer.parseInt(sec.substring(10, 12));

            if (Integer.parseInt(mes_actual) <= mest) {
                if (Integer.parseInt(dia_actual) > diat) {
                    secactual = 1;
                } else
                    secactual = sectem + 1;

            } else {
                secactual = 1;
            }
        }

        if (mes_actual.length() < 2) {
            mes_actual = cero + mes_actual;
        }
        if (dia_actual.length() < 2) {
            dia_actual = cero + dia_actual;
        }

        if (secactual < 10) {
            sec = codven + mes_actual + dia_actual + cero + secactual;
        } else {
            sec = codven + mes_actual + dia_actual + secactual;
        }

        return sec;

    }

    public void ColocarFecha() {

        edt_FechaEntrega.setText(new StringBuilder().append(dia).append("/")
                .append(mes + 1).append("/").append(año));
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case Fecha_Dialog_ID:
                return new DatePickerDialog(this, dateSetListener, año, mes, dia);

            default:
                return null;
        }
    }

    // Picker Dialog funcion de button en layout
    public void Mostrar_date(View view) {

        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        año = calendar.get(Calendar.YEAR);

        showDialog(Fecha_Dialog_ID);
    }

    public void colocarFechaActual() {

        edt_FechaEntrega.setText(GlobalFunctions
                .getFecha_configuracion(getApplicationContext()));
        edt_FechaEntrega.setEnabled(false);
    }

    public DBIngresos guardarDetalle_Ingresos() {

        DBIngresos dbingresos = new DBIngresos();
        double acuenta = Double.parseDouble(edt_acuenta.getText().toString());
        double sald = Double.parseDouble(edt_saldo.getText().toString());

        dbingresos.setSecuencia(sec_ingresos);
        dbingresos.setSecitm(edt_secuencia.getText().toString());
        dbingresos.setFecpag(GlobalFunctions .getFecha_configuracion(getApplicationContext()) + " " 	+ GlobalFunctions.getHoraActual());
        dbingresos.setFecoperacion(GlobalFunctions.getFechaActual());

        dbingresos.setTotal(total);
        dbingresos.setAcuenta(acuenta + "");
        dbingresos.setSaldo(sald + "");
        dbingresos.setCodforpag(codigo_forma_pago);


        //Validar Datos respecto a la forma de pago seleccionada
        if (nombre_forma_pago.indexOf("EFECTIVO") > 0) {
            Log.e("PAGOS", "Es efectivo");

            dbingresos.setCodcue("0");
            dbingresos.setNumope("0");
            dbingresos.setCodigoBanco("0");

        } else if (nombre_forma_pago.indexOf("CHEQUE") > 0 	|| nombre_forma_pago.indexOf("LETRA") > 0) {
            dbingresos.setCodcue(" ");
            dbingresos.setNumope(edt_numope.getText().toString());
            dbingresos.setCodigoBanco(codigoBanco);
        } else {
            Log.e("PAGOS", "No es efectivo");
            dbingresos.setCodcue( codigoCuentaBanco);
            dbingresos.setNumope(edt_numope.getText().toString());

            dbingresos.setCodigoBanco(codigoBanco);
        }
        dbingresos.setTipo_cambio("" + valor_cambio);
        Log.d("tipo_de_cambio", "Enviando Serv --->"+valor_cambio);

        dbingresos.setCodmon(codmon);

        if (codmondoc.equals(codmon)) {
            dbingresos.setMonto_afecto(acuenta + "");
        } else if (codmondoc.equals("01") && codmon.equals("02")) {
            dbingresos.setMonto_afecto(redondear(acuenta / valor_cambio) + "");
        } else {
            dbingresos.setMonto_afecto(redondear(acuenta * valor_cambio) + "");
        }

        dbingresos.setUsername(codven);
        dbingresos.setFlag("P");
        dbingresos.setLatitud("0");
        dbingresos.setLongitu("0");
        dbingresos.setDT_INGR_FECHASERVIDOR("0");
        dbingresos.setEstado("G");
        dbingresos.setObservacion("ninguna");

        Log.d("Cobranza","codcli:"+codcli);
        dbingresos.setCodcli(codcli);

        dbingresos.setTipoDoc(tipoDocumento);
        dbingresos.setSerie(serie);
        dbingresos.setNumero(numero);

        if (origen.equals("REPORTES")) {
            dbingresos.setSecitm((edt_secuencia.getText().toString()));
            obj_dbclasses.updateIngresos2(dbingresos);
            Log.w("DETALLE INGRESO", "registrado");
        } else {

            obj_dbclasses.guardarDetalle_Ingreso(dbingresos);
            Log.w("DETALLE INGRESO", "registrado");

        }
        return dbingresos;
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.amortizar_btn_agregar:
                // Guardar Ingreso Movil
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        AmortizarCuentasXCobrarActivity2.this);
                builder.setTitle("Importante");
                builder.setIcon(R.drawable.ic_alert);
                builder.setMessage("Se guardaran todos los datos")
                        .setCancelable(false)
                        .setPositiveButton("Enviar al servidor",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        DBIngresos obj_ing = guardarDetalle_Ingresos();
                                        new asyncGuardarAmortizacion() .execute(obj_ing);

                                    }
                                })
                        .setNegativeButton("Local",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int id) {
                                        guardarIngresos2();
                                        dialog.dismiss();

                                        crear_dialogo_post_envio("EL INGRESO SE GUARDO LOCALMENTE",
                                                "La amortizacion fue registrada en la BD del móvil.",
                                                R.drawable.check);
                                    }
                                });
                // Aquiii--------------------------------------------------------------------------------------------------------
                saldoo = 0;
                double tota = Double.parseDouble(total);

                if (edt_acuenta.getText().toString() == null || edt_acuenta.getText().toString().matches("") || edt_acuenta.getText().toString().equals("0") || edt_acuenta.getText().toString().equals("0.0")) {
                    edt_acuenta.setError("Ingrese un valor");
                    edt_saldo.setText(saldo);
                    break;
                }

                acuenta2 = Double.parseDouble(edt_acuenta.getText().toString());
                Log.e("COBRANZA", "Monto a pagar" + acuenta2+" Moneda D"+codmondoc+" mon"+codmon);

                if (codmondoc.equals(codmon)) { // Si la moneda de pago es la misma que el documento AQUI*
                    monto_afecto = acuenta2;
                } else if (codmondoc.equals("2") && codmon.equals("1")) {
                    monto_afecto = acuenta2 / valor_cambio;
                } else if (codmondoc.equals("1") && codmon.equals("2")) {
                    monto_afecto = acuenta2 * valor_cambio;
                }

                Log.e("COBRANZA", "Monto a pagar" + monto_afecto+" DEUDA "+saldo_virtual);

                if (monto_afecto > Double.parseDouble(saldo_virtual)) {
                    edt_acuenta.setError("Acuenta mayor que la deuda");
                    edt_saldo.setText(saldo);
                    break;
                }
                if (ly_operacion.getVisibility() == View.VISIBLE && edt_numope.getText().toString().matches("")) {
                    edt_numope.setError("Ingrese N° operacion");
                    break;
                }
                saldoo = Double.parseDouble(saldo_virtual) - monto_afecto;
                edt_saldo.setText(redondear(saldoo) + "");
                Log.w("SALDO, MONTO_AFECTO", saldoo + " = " + saldo_virtual + "-"
                        + monto_afecto);

                AlertDialog alert = builder.create();
                alert.show();

        }
    }

    public void mostrarBancos() {
        listaBancos = new ArrayList<DBBanco>();
        listaBancos = obj_dbclasses.getBancos();

        Iterator<DBBanco> it = listaBancos.iterator();
        int i = 0;
        nomban = new String[listaBancos.size()];
        codban = new String[listaBancos.size()];
        while (it.hasNext()) {
            Object objeto = it.next();
            DBBanco dbbanco = (DBBanco) objeto;
            nomban[i] = dbbanco.getNomban();
            codban[i] = dbbanco.getCodban();
            i++;
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>( getApplicationContext(), android.R.layout.simple_spinner_item, nomban);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_banco.setAdapter(adapter);


    }

    private void mostrarCtas_xBanco(String cod) {

        listaCuentas_x_Banco = new ArrayList<DBCtas_xbanco>();
        listaCuentas_x_Banco = obj_dbclasses.getCtas_xBanco(cod);

        Iterator<DBCtas_xbanco> it = listaCuentas_x_Banco.iterator();
        int i = 0;
        num_cta = new String[listaCuentas_x_Banco.size()];
        cod_cta = new int[listaCuentas_x_Banco.size()];
        moneda = new String[listaCuentas_x_Banco.size()];

        while (it.hasNext()) {
            Object objeto = it.next();
            DBCtas_xbanco dbctas_xbanco = (DBCtas_xbanco) objeto;
            num_cta[i] = dbctas_xbanco.getCta_cte();
            cod_cta[i] = dbctas_xbanco.getItem();
            moneda[i] = dbctas_xbanco.getCodmon();
            i++;
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                getApplicationContext(), android.R.layout.simple_spinner_item,
                num_cta);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_cta_x_banco.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {

    }

    class asyncGuardarAmortizacion extends AsyncTask<DBIngresos, String, String> {

        String user, pass;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(AmortizarCuentasXCobrarActivity2.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Guardando....");
            pDialog.setIndeterminate(false);

            pDialog.show();
        }

        protected String doInBackground(DBIngresos... params) {

            DBIngresos obj_ing = params[0];
            String valor = "";

            DBSync_soap_manager soap_manager = new DBSync_soap_manager(
                    getApplicationContext());
            ConnectionDetector cd = new ConnectionDetector(
                    getApplicationContext());

            if (cd.hasActiveInternetConnection(getApplicationContext())) {

                try {
                    valor = soap_manager.actualizarIngresos2(obj_ing);
                } catch (JsonParseException e) {
                    // error al parsear respuesta json
                    valor = "error_2";
                } catch (Exception e) {
                    // cualquier otra excepcion al enviar datos
                    valor = "error_3";
                }

            } else {
                // Sin conexion al Servidor
                valor = "error_1";

            }

            return valor;
        }

        /*
         * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
         * a la sig. activity o mostramos error
         */

        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= DEPOSITOs", "" + result);

            if (result.equals("E")) {

                crear_dialogo_post_envio("ENVIO CORRECTO",
                        "La amortizacion fue ingresada al Servidor",
                        R.drawable.check);

            } else if (result.equals("I")) {

                crear_dialogo_post_envio("ATENCION",
                        "No se pudieron guardar todos los datos",
                        R.drawable.alert);

            } else if (result.equals("P")) {

                crear_dialogo_post_envio("ATENCION",
                        "El servidor no pudo ingresar esta amortizacion",
                        R.drawable.ic_alert);

            } else if (result.equals("T")) {

                crear_dialogo_post_envio(
                        "ATENCION",
                        "Esta amortizacion ya se encuentra en proceso de facturacion \nComuniquese con el administrador",
                        R.drawable.ic_alert);

            } else if (result.equals("error_1")) {

                crear_dialogo_post_envio(
                        "SIN CONEXION",
                        "Es probable que no tenga acceso a la red \nLa amortizacion se guardo localmente",
                        R.drawable.ic_alert);

            } else if (result.equals("error_2")) {

                crear_dialogo_post_envio(
                        "ATENCION",
                        "la amortizacion fue enviada pero no se pudo verificar \nVerifique manualmente",
                        R.drawable.alert);

            } else if (result.equals("error_3")) {

                crear_dialogo_post_envio(
                        "ERROR AL ENVIAR",
                        "No se pudo Enviar esta amortizacion \nSe guardo localmente",
                        R.drawable.ic_alert);

            }

        }

    }

    public String getHoraActual() {

        Calendar calendar1 = Calendar.getInstance();

        String hora = "" + calendar1.get(Calendar.HOUR_OF_DAY);
        String min = "" + calendar1.get(Calendar.MINUTE);
        String seg = "" + calendar1.get(Calendar.SECOND);

        if (hora.length() < 2) {
            hora = "0" + hora;
        }

        if (min.length() < 2) {
            min = "0" + min;
        }

        if (seg.length() < 2) {
            seg = "0" + seg;
        }

        return hora + ":" + min + ":" + seg;
    }

    public BigDecimal redondear(double val) {
        String r = val + "";
        BigDecimal big = new BigDecimal(r);
        big = big.setScale(2, RoundingMode.HALF_UP);
        return big;
    }

    public void guardarIngresos2() {

        DBIngresos dbingresos = new DBIngresos();
        double acuenta = Double.parseDouble(edt_acuenta.getText().toString());
        double sald = Double.parseDouble(edt_saldo.getText().toString());

        dbingresos.setSecuencia(sec_ingresos);
        dbingresos.setSecitm(edt_secuencia.getText().toString());
        dbingresos.setFecpag(GlobalFunctions .getFecha_configuracion(getApplicationContext()) + " " 	+ GlobalFunctions.getHoraActual());
        dbingresos.setFecoperacion(GlobalFunctions.getFechaActual());
        dbingresos.setTotal(total);
        dbingresos.setAcuenta(acuenta + "");
        dbingresos.setSaldo(sald + "");
        dbingresos.setCodforpag(codigo_forma_pago);

        //Validar Datos respecto a la forma de pago seleccionada
        if (nombre_forma_pago.indexOf("EFECTIVO") > 0) {
            Log.e("PAGOS", "Es efectivo");

            dbingresos.setCodcue("0");
            dbingresos.setNumope("0");
            dbingresos.setCodigoBanco("0");

        } else if (nombre_forma_pago.indexOf("CHEQUE") > 0 	|| nombre_forma_pago.indexOf("LETRA") > 0) {
            dbingresos.setCodcue("0");
            dbingresos.setNumope(edt_numope.getText().toString());
            dbingresos.setCodigoBanco(codigoBanco);
        } else {
            Log.e("PAGOS", "No es efectivo");
            dbingresos.setCodcue( codigoCuentaBanco);
            dbingresos.setNumope(edt_numope.getText().toString());

            dbingresos.setCodigoBanco(codigoBanco);
        }

        dbingresos.setTipo_cambio("" + valor_cambio);
        Log.d("tipo_de_cambio", "Enviando Loc --->"+valor_cambio);

        dbingresos.setCodmon(codmon);

        // 1:Soles 2:Dolares (Codificacion SUNAT)  // Moneda
        if (codmondoc.equals(codmon)) {
            dbingresos.setMonto_afecto(acuenta + "");
        } else if (codmondoc.equals("1") && codmon.equals("2")) {
            dbingresos.setMonto_afecto(redondear(acuenta * valor_cambio) + "");
        } else if (codmondoc.equals("2") && codmon.equals("1")) {
            dbingresos.setMonto_afecto(redondear(acuenta / valor_cambio) + "");
        }

        dbingresos.setUsername(codven);
        dbingresos.setFlag("P");
        dbingresos.setLatitud("0");
        dbingresos.setLongitu("0");
        dbingresos.setDT_INGR_FECHASERVIDOR("0");
        dbingresos.setEstado("G");
        dbingresos.setFlag("P");
        dbingresos.setObservacion("ninguna");
        dbingresos.setCodcli(codcli);

        dbingresos.setTipoDoc(tipoDocumento);
        dbingresos.setSerie(serie);
        dbingresos.setNumero(numero);


        //*******************************************************************


        if (origen.equals("REPORTES")) {
            obj_dbclasses.updateIngresos2(dbingresos);
        } else {
            obj_dbclasses.guardarDetalle_Ingreso(dbingresos);
        }
    }

    private void crear_dialogo_post_envio(String titulo, String mensaje,
                                          int icon) {

        AlertDialog alertDialog = new AlertDialog.Builder(
                AmortizarCuentasXCobrarActivity2.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(mensaje);
        alertDialog.setIcon(icon);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (origen.equals("clienteac")) {
                    finish();
                } else if (origen.equals("REPORTES")) {

                    finish();
                    Intent i = new Intent(getApplicationContext(),
                            ReportesActivity.class);
                    i.putExtra("ORIGEN", "COBRANZA_MODIFICAR");
                    startActivity(i);
                } else {

                    finish();
					/*Intent i = new Intent(getApplicationContext(),
							CobranzaActivity2.class);
					i.putExtra("CODCLI",
							obj_dbclasses.obtenerCodigoCliente(nomcli));
					i.putExtra("NOMCLI", nomcli);
					startActivity(i);*/

                }
            }
        });

        alertDialog.show();

    }

}
