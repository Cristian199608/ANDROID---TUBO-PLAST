package com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class ViewPdfActivity extends AppCompatActivity {

    private PDFView pdfView;
    String pacthPDF =  null;

    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        String nombreArchivo = (String) b.get("nombreArchivo");

        pdfView = findViewById(R.id.pdfview);

        //bundle recibir la ruta exacta del documento
        pacthPDF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/" + nombreArchivo + "";

        pdfView.fromFile(new File(pacthPDF))
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();

    }

    public void FABcompartirPDF(View view)
    {
        if (pacthPDF != null)
        {
            compartirPDF(pacthPDF);
        }
        else
        {
            Toast.makeText(this, "NO HAY ARCHIVO DISPONIBLE", Toast.LENGTH_SHORT).show();
        }
    }
    private void compartirPDF(String path)
    {
        Uri uriForFile = FileProvider.getUriForFile(ViewPdfActivity.this, GlobalVar.PACKAGE_NAME, new File(path));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriForFile);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("application/pdf");
        startActivity(Intent.createChooser(shareIntent, "Compartir..."));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (pacthPDF==null) return;

            File dir=new File(pacthPDF);
            if (dir.isDirectory()) {
                String[] hijos = dir.list();
                for (String hijo : hijos) {
                    new File(dir, hijo).delete();
                }
            }else {
                dir.delete();
            }
        }catch (Exception e){
            Toast.makeText(this, "No se ha podido eliminar archivos para la distribuciòn", Toast.LENGTH_SHORT).show();
        }

    }
}