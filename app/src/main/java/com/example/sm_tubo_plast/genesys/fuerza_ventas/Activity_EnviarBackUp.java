package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.adapters.AdapterBackupDB;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.FunctionsGlobales;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;



@SuppressLint("LongLogTag")
public class Activity_EnviarBackUp extends AppCompatActivity {

    private final static String TAG = "Activity_EnviarBackUp";
    Button btn_generarBackUp,btn_cargarBackUp;
    Button btn_enviaCorreo;

    AdapterBackupDB adapterBackupDB;
    LinearLayout linear_arriba;
    Button imgFinalizarSeleccion;
    TextView tv_folder,txtRuta;
    EditText txt_body;
    ImageView image;

    String KEY_TEXTPSS = "TEXTPSS";
    static final int CUSTOM_DIALOG_ID = 0;
    ListView dialog_ListView;

    File root;
    File curFolder;
    File seleccionado;
    ArrayList<Uri> uriForFile;


    String defaultRuta = "";
    String nombreVendedor="anonimo";
    String codigoVendedor="000000";
    String nombreEmpresa= "Empresa";
    String correo = "soporte@acgenesys.com";

    boolean RequestPermission=false; int REQUEST_CODE=101;

    SharedPreferences preferencias_generales;

    private File[] fileList = new File[0];
    DBclasses database;
    VARIABLES VAR=new VARIABLES();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_back_up);


        preferencias_generales =  getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        codigoVendedor = preferencias_generales.getString("codven", "Sin codigo");
        database = new DBclasses(getApplicationContext());
        nombreVendedor = database.getNombreVendedor(codigoVendedor);
        nombreEmpresa = database.getNombreEmpresa();
        correo = database.getEmailConfiguracion();

        showRequestPermisionToAccesStorage();

        if (correo.equals("") || correo.isEmpty()) {
            Toast.makeText(getApplicationContext(), "El correo esta vacio", Toast.LENGTH_SHORT).show();
            correo = "soporte@acgenesys.com";
        }

        txt_body = (EditText)findViewById(R.id.txt_body);
        txtRuta = (TextView)findViewById(R.id.txt_ruta);
        btn_generarBackUp = (Button) findViewById(R.id.btn_generarBackUp);
        btn_cargarBackUp = (Button) findViewById(R.id.btn_cargarBackUp);
        btn_enviaCorreo =(Button)findViewById(R.id.btn_enviarCorreo);

        defaultRuta = txtRuta.getText().toString();

        GlobalFunctions.ConfigurarRutasBackup(Activity_EnviarBackUp.this);
        btn_generarBackUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curFolder=GlobalFunctions.backupdDatabase(Activity_EnviarBackUp.this);
            }
        });
        btn_cargarBackUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              selectedDirecto(curFolder);
                showDialog(CUSTOM_DIALOG_ID);

            }
        });
        btn_enviaCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compartirPDF("");
                return;
//              if (defaultRuta.equals(txtRuta.getText().toString())) {
//                  GlobalFunctions.showCustomToast(Activity_EnviarBackUp.this, "Adjunte archivo", GlobalFunctions.TOAST_WARNING);
//              }else{
//                  EnviarBackUp_correo();
//
//              }

            }
        });

        root = GlobalVar.RUTA_BACKUP_RUTA_SELECTED;
        curFolder = GlobalVar.RUTA_BACKUP_RUTA_SELECTED;
    }
/*
    protected void EnviarBackUp_correo() {
        final String emailFrom = "ACGenesys.mail@gmail.com";
//        final String emailFrom = "japacristianadrian@gmail.com";
        final String password = "Genesys12";
//        final String password = "japa1234549";
        final String emailTo = correo;
        final String aliasSender = nombreVendedor+ " ("+codigoVendedor+")";

        final String subject = "Envio de BackUp "+nombreEmpresa+":"+nombreVendedor+ " ("+codigoVendedor+")";
        final String body =
                "Empresa: "+nombreEmpresa+
                        "\nnombre Vendedor: "+nombreVendedor+
                        "\nCódigo: "+codigoVendedor+
                        "\nBackUp adjunto: "+txtRuta.getText().toString()+
                        "\n\nInconveniente:"+
                        "\n"+txt_body.getText().toString();

        Log.d(TAG, body+"\ncorreo: "+emailTo);
        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

        new AsyncTask<Void, Void, Void>() {
            ProgressDialog progressDialog = new ProgressDialog(Activity_EnviarBackUp.this);
            boolean flagEnviado = false;

            @Override
            protected void onPreExecute()
            {
                progressDialog.setTitle("Envio de correo");
                progressDialog.setMessage("Enviando...");
                //progressDialog.setIndeterminate(false);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try {
                    GmailSender email = new GmailSender(emailFrom, password);
                    email.addAttachment(txtRuta.getText().toString(), "BackUp");
                    email.sendMail(subject,body,emailFrom,aliasSender,emailTo);
                    flagEnviado = true;
                } catch (Exception e) {
                    Log.e(TAG + " Mail","Ocurrió un problema al enviar email, con los datos: ");
                    flagEnviado = false;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void res)
            {
                progressDialog.dismiss();
                if (flagEnviado) {
                    GlobalFunctions.showCustomToast(Activity_EnviarBackUp.this, "Correo enviado !", GlobalFunctions.TOAST_DONE);
                }else{
                    GlobalFunctions.showCustomToast(Activity_EnviarBackUp.this, "Ocurrieron problemas al enviar el correo", GlobalFunctions.TOAST_ERROR);
                }

            }
        }.execute();

    }
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalFunctions.LimpiarRutasBackup();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;

        switch (id) {
            case CUSTOM_DIALOG_ID:

                dialog = new Dialog(Activity_EnviarBackUp.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_seleccionar_backup);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                tv_folder = (TextView) dialog.findViewById(R.id.tv_folder);
                TextView tvRutaActual = (TextView) dialog.findViewById(R.id.tvRutaActual);
                linear_arriba = (LinearLayout) dialog.findViewById(R.id.linear_arriba);
                imgFinalizarSeleccion = dialog.findViewById(R.id.imgFinalizarSeleccion);

                tv_folder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_folder.setText(""+GlobalVar.CambiarRutaBackup()+"\nToque para cambiar");
                        curFolder=GlobalVar.RUTA_BACKUP_RUTA_SELECTED;
                        tvRutaActual.setText(""+curFolder.getPath());
                        ListDir(curFolder);
                        tvRutaActual.setText(""+curFolder.getPath());
                    }
                });
                linear_arriba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                        tvRutaActual.setText(""+curFolder.getPath());
                    }
                });

                // Prepare ListView in dialog
                dialog_ListView = (ListView) dialog.findViewById(R.id.lv_hijos);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        File selected = fileList[position];
                        if (selected.isDirectory()) {
                            ListDir(selected);
                            tvRutaActual.setText(""+selected.getPath());
                        }
                    }
                });
//                ListDir(curFolder);
                tv_folder.setText(GlobalVar.GetOpcionRutaBackup()+"\nToque para cambiar");
                tvRutaActual.setText(""+curFolder.getPath());

                Dialog finalDialog = dialog;
                imgFinalizarSeleccion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        dismissDialog(CUSTOM_DIALOG_ID);
                        finalDialog.dismiss();
                        HashMap<String, Uri> uriForFilec=adapterBackupDB.GetArchivosSeleccionados();
                        uriForFile=new ArrayList<>();
                        uriForFile.addAll(uriForFilec.values());
                        GlobalFunctions.showCustomToast(Activity_EnviarBackUp.this, "Cantidad seleccionado "+uriForFile.size(), GlobalFunctions.TOAST_DONE);
                        String text=new FunctionsGlobales().getValuesHashMap(uriForFilec);
                        txtRuta.setText(text);

                    }
                });

                break;
        }

        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
        // TODO Auto-generated method stub
        super.onPrepareDialog(id, dialog, bundle);
        ListDir(curFolder);

    }
    private void compartirPDF(String path)
    {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_TITLE, "Backup .db");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Backup Saemovil - Fuerza Ventas");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Revisar estos archivos, porfavor\n\nMotivo:\n "+txt_body.getText().toString());
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriForFile);
//        shareIntent.setType("image/*");
        shareIntent.setType("application/db");
        startActivity(Intent.createChooser(shareIntent, "Compartir archivo"));


    }
    void selectedDirecto(File file){
        try{
            Uri newUri = FileProvider.getUriForFile(this, GlobalVar.PACKAGE_NAME, file);
            Intent intent = new Intent();
            intent.setData(newUri);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivity(Intent.createChooser(intent, "Select a File to Upload"));
        }catch(Exception e){
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    void OrdenarArchivos(){
        Arrays.sort(fileList, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.compare(f2.lastModified(), f1.lastModified());
            }
        });
    }
    void ListDir(File f) {
        if (f.equals(root)) {
            linear_arriba.setEnabled(false);
        } else {
            linear_arriba.setEnabled(true);
        }

        curFolder = f;

        fileList = f.listFiles();
        try {
            OrdenarArchivos();
            adapterBackupDB=new AdapterBackupDB(this, fileList);
            dialog_ListView.setAdapter(adapterBackupDB);
            adapterBackupDB.setMyCallback(cantidad -> imgFinalizarSeleccion.setText("Finalizar ("+cantidad+")"));


        }catch (Exception e){
            Toast.makeText(this, "No se ha encontrado ningun archivo", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "No se ha podido acceder a los dirrectorios del telefono");
        }

    }

    //MOSTRAR DIALOGO AL USUARIO PARA ACCEDER Y CREAR ARCHIVOS SU MEMORIA

    private void showRequestPermisionToAccesStorage() {

        int permissionCheckFineLocation = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckCoarseLocation= ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int packag= PackageManager.PERMISSION_GRANTED;

        if (permissionCheckFineLocation !=  packag       &&         permissionCheckCoarseLocation!=packag) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE);
            } else {
                requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE);


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
                    RequestPermission=true;

                } else {
                    Toast.makeText(getApplicationContext(), "No podras generar backup ", Toast.LENGTH_SHORT).show();
                    RequestPermission=false;
                }
        }

    }

    private void requestPermission(String permissionName, String permissionName2, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName, permissionName2}, permissionRequestCode);
    }
}
