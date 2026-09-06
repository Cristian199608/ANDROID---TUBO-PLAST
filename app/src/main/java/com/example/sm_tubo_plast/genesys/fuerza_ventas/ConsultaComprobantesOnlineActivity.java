package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultComprobante;
import com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultComprobantesDTO;
import com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultCoprobanteBytes;
import com.example.sm_tubo_plast.genesys.Retrofit.RetrofilClientCantol;
import com.example.sm_tubo_plast.genesys.Retrofit.request.GetDataCantol;
import com.example.sm_tubo_plast.genesys.Retrofit.request.RequestCliente;
import com.example.sm_tubo_plast.genesys.Retrofit.util.WS_RetrofitCustom;
import com.example.sm_tubo_plast.genesys.adapters.ComprobantesRecyclerAdapter;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ViewPdfActivity;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.descargas.GuardarBytesToPdfAsync;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;

public class ConsultaComprobantesOnlineActivity extends AppCompatActivity {
    private static final String TAG = "ConsultaComprobantesOnlineActivity";
    String codcli="";

    private RecyclerView rvComprobantes;
    private ProgressBar progressBar;
    private TextView tvSinResultados, tvInfoResultado;
    MaterialToolbar toolbar;
    TextInputEditText etFechaDesde, etFechaHasta;
    Button btnBuscar;


    private ArrayList<ResultComprobante> listaComprobantes;
    private ComprobantesRecyclerAdapter adapter;

    ProgressDialog pDialog;
    GuardarBytesToPdfAsync guardarPdf=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_comprobantes_online);
        getParmetros();
        configView();
    }
    private void getParmetros(){
        Bundle bundle = getIntent().getExtras();
        codcli = bundle.getString("codcli", null);
    }
    private void configView(){
        rvComprobantes = findViewById(R.id.rvComprobantes);
        progressBar = findViewById(R.id.progressBar);
        tvInfoResultado =  findViewById(R.id.tvInfoResultado);
        tvSinResultados = findViewById(R.id.tvSinResultados);
        toolbar = findViewById(R.id.toolbar);
        etFechaDesde = findViewById(R.id.etFechaDesde);
        etFechaHasta = findViewById(R.id.etFechaHasta);
        btnBuscar = findViewById(R.id.btnBuscar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setTitle("Consulta Comprobante");
        toolbar.setSubtitle("Cliente los rosales sac");

        seleccionarFecha(etFechaDesde);
        seleccionarFecha(etFechaHasta);

        listaComprobantes = new ArrayList<>();
        eventos();
        inicializarRecylcler();
        cargarDatos();
    }
    private void inicializarRecylcler(){
        adapter = new ComprobantesRecyclerAdapter(
                listaComprobantes,
                new ComprobantesRecyclerAdapter.OnComprobanteClickListener() {

                    @Override
                    public void onProcesarClick(
                            ResultComprobante comprobante) {
                        requestDescargaBytesPDF(comprobante);
//                        Log.i(TAG, "comprobante "+comprobante.getTipo_documento()+" "+comprobante.getSerie()+"-"+comprobante.getNumero());
//                        Toast.makeText(ConsultaComprobantesOnlineActivity.this, "descargar pdf o xml", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        rvComprobantes.setLayoutManager(new LinearLayoutManager(this));
        rvComprobantes.setAdapter(adapter);
    }


    private void eventos(){
        btnBuscar.setOnClickListener(v->{
            requestConsulta();
        });
    }
    private void cargarDatos(){
        if (listaComprobantes.size()>0) {
            tvInfoResultado.setText("Se encontró "+listaComprobantes.size()+" comprobantes");
        }else tvInfoResultado.setText("Seleccione fechas para buscar");
        adapter.notifyDataSetChanged();
        validarLista();
    }

    private void seleccionarFecha(TextInputEditText editText) {

        Calendar calendario = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String fecha = String.format(
                            Locale.US,
                            "%04d-%02d-%02d",
                            year,
                            month + 1,
                            dayOfMonth
                    );
                    editText.setText(fecha);
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        );
        editText.setOnClickListener(v -> {
            dialog.show();
        });


    }

    private void validarLista() {
        if (listaComprobantes.isEmpty()) {
            rvComprobantes.setVisibility(View.GONE);
            tvSinResultados.setVisibility(View.VISIBLE);
        } else {
            rvComprobantes.setVisibility(View.VISIBLE);
            tvSinResultados.setVisibility(View.GONE);
        }
    }

    private boolean validarDato(){
        int canErro=0;
        etFechaDesde.setError(null);
        etFechaHasta.setError(null);
        if (etFechaDesde.getText().toString().length()==0) {
            etFechaDesde.setError("?");
            canErro++;
        }
        if (etFechaHasta.getText().toString().length()==0) {
            etFechaHasta.setError("?");
            canErro++;
        }

        return canErro==0;
    }
    private void requestConsulta(){
        if(!validarDato()) return;
        listaComprobantes.clear();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Consultando comprobantes...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        String fechaStartYYMMDD = etFechaDesde.getText().toString();
        String fechaEndYYMMDD =etFechaHasta.getText().toString();;///"2026-09-05";
        String urlReq= RetrofilClientCantol.UrlPeticiones.getConsultaComprobanteCliente();
        RequestBody body = RetrofilClientCantol.createBodyJson(RequestCliente.Companion.listaComprobantexCliente(urlReq, codcli, fechaStartYYMMDD, fechaEndYYMMDD));
        Call<Object> call = RetrofilClientCantol.getRetrofitInstanceCantolWithToken(this)
                .create(GetDataCantol.class).getCliente(body);
        WS_RetrofitCustom ws_retrofitCustom= new WS_RetrofitCustom(this);
        ws_retrofitCustom.StartPeticion(call, new WS_RetrofitCustom.MyListener() {
            @Override
            public void StartFinish(boolean isFinish) {
                if (!isFinish) pDialog.show();
                else pDialog.dismiss();
            }
            @Override
            public void Result(boolean isOk, String mensaje, Object data) {
                if(!isOk){
                    GlobalFunctions.showCustomToast(
                            ConsultaComprobantesOnlineActivity.this,
                            mensaje,
                            GlobalFunctions.TOAST_ERROR);
                    return;
                }
                Gson gson=new Gson();
                ResultComprobantesDTO resultComprobantes=gson.fromJson(gson.toJson(data), ResultComprobantesDTO.class);
                if (!resultComprobantes.getEstado().equalsIgnoreCase("ok")) {
                    GlobalFunctions.showCustomToast(
                            ConsultaComprobantesOnlineActivity.this,
                            "Api de consultas ha devuelto un error: "+resultComprobantes.getEstado(),
                            GlobalFunctions.TOAST_ERROR);
                    return;
                }
                listaComprobantes.clear();
                listaComprobantes.addAll(resultComprobantes.getComprobantes());
                cargarDatos();

            }
        });
    }

    private void requestDescargaBytesPDF(ResultComprobante comprobante){
        if(!validarDato()) return;
        listaComprobantes.clear();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Descargando archivo pdf...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        String tipoDocumento=comprobante.getTipo_documento().equalsIgnoreCase("factura")?"01":"03";
        String serieDoc=comprobante.getSerie().trim();
        String numeroDoc=comprobante.getNumero().trim();

        String urlReq= RetrofilClientCantol.UrlPeticiones.getConsultaDescargaBytePDF();
        RequestBody body = RetrofilClientCantol.createBodyJson(RequestCliente.Companion.descargaComprobanteBytes(urlReq,tipoDocumento, serieDoc, numeroDoc));
        Call<Object> call = RetrofilClientCantol.getRetrofitInstanceCantolWithToken(this)
                .create(GetDataCantol.class).getCliente(body);
        WS_RetrofitCustom ws_retrofitCustom= new WS_RetrofitCustom(this);
        ws_retrofitCustom.StartPeticion(call, new WS_RetrofitCustom.MyListener() {
            @Override
            public void StartFinish(boolean isFinish) {
                if (!isFinish) pDialog.show();
            }
            @Override
            public void Result(boolean isOk, String mensaje, Object data) {
                if(!isOk){
                    pDialog.dismiss();
                    GlobalFunctions.showCustomToast(
                            ConsultaComprobantesOnlineActivity.this,
                            mensaje,
                            GlobalFunctions.TOAST_ERROR);
                    return;
                }
                Gson gson=new Gson();
                ResultCoprobanteBytes resultComprobantes=gson.fromJson(gson.toJson(data), ResultCoprobanteBytes.class);
                if (!resultComprobantes.isSuccess()) {
                    GlobalFunctions.showCustomToast(
                            ConsultaComprobantesOnlineActivity.this,
                            "Api de consultas ha devuelto un error: "+resultComprobantes.getMensaje(),
                            GlobalFunctions.TOAST_ERROR);
                    return;
                }
                requestDescargaPDF(pDialog,resultComprobantes);

            }
        });
    }

    private void requestDescargaPDF(final ProgressDialog pdialog, ResultCoprobanteBytes resultComprobantes) {
        guardarPdf = new GuardarBytesToPdfAsync(this);
        guardarPdf.guardar(
                resultComprobantes.getContenido_pdf(),
                resultComprobantes.getNombre_archivo(),
                new GuardarBytesToPdfAsync.Callback() {
                    @Override
                    public void onSuccess(File archivo, String ruta) {
                        pdialog.dismiss();
                        Log.i("PDF", "Guardado: " + ruta);
                        GlobalFunctions.showCustomToast(
                                ConsultaComprobantesOnlineActivity.this,
                                "PDF Descargado correctamente",
                                GlobalFunctions.TOAST_DONE);
                        Intent i = new Intent(ConsultaComprobantesOnlineActivity.this, ViewPdfActivity.class);
                        i.putExtra("nombreArchivo", resultComprobantes.getNombre_archivo());
                        startActivity(i);
                    }
                    @Override
                    public void onError(String mensaje) {
                        pdialog.dismiss();
                        Log.e("PDF", "Error: " + mensaje);
                        GlobalFunctions.showCustomToast(
                                ConsultaComprobantesOnlineActivity.this,
                                "Error "+mensaje,
                                GlobalFunctions.TOAST_ERROR);
                    }
                }
        );
    }

}