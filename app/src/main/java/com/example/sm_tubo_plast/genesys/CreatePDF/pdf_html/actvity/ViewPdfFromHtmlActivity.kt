package com.example.sm_tubo_plast.genesys.CreatePDF.pdf_html.actvity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.sm_tubo_plast.databinding.ActivityViewPdfFromHtmlBinding
import com.example.sm_tubo_plast.genesys.CreatePDF.pdf_html.util.CreatePdfFormWebView
import com.example.sm_tubo_plast.genesys.util.GlobalVar
import com.example.sm_tubo_plast.genesys.util.UtilView
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje
import java.io.File

public class ViewPdfFromHtmlActivity : AppCompatActivity() {
    //generar binding
    lateinit var binding: ActivityViewPdfFromHtmlBinding
    lateinit var htmlData:String
    lateinit var nombreArchivo:String
    lateinit var directoryFile:String
    lateinit var fullDirectoryFile:String
    //generar companion object para la invocacion de la actividad
    companion object{
        //definir clave de datos
        val CLAVE_HTML_DATA="htmlData";
        val CLAVE_NOMBRE_ARCHIVO="nombreArchivo";
        val CLAVE_DIRECTORY_ARCHIVO="directoryFile";
        fun startViewPdfFromHtmlActivity(activity: Activity,
                                         htmlData:String,
                                         directoryFile: String,
                                         nombreArchvio:String){
            val intent = Intent(activity, ViewPdfFromHtmlActivity::class.java)
            intent.putExtra(CLAVE_HTML_DATA,htmlData);
            intent.putExtra(CLAVE_NOMBRE_ARCHIVO,nombreArchvio);
            intent.putExtra(CLAVE_DIRECTORY_ARCHIVO,directoryFile);
            activity.startActivity(intent);
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityViewPdfFromHtmlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //obtener datos
        var bundle = intent.getExtras();
        if(bundle==null){
            //show error
            UtilView.showCustomToast(this,"Error al obtener datos", UtilView.TOAST_ERROR);
            return;
        }
        htmlData=bundle.getString(CLAVE_HTML_DATA, null);
        nombreArchivo=bundle.getString(CLAVE_NOMBRE_ARCHIVO, null);
        directoryFile=bundle.getString(CLAVE_DIRECTORY_ARCHIVO, null);
        //verficar datos que no sean nulos
        if(htmlData==null || nombreArchivo==null || directoryFile==null){
            //show error
            UtilView.showCustomToast(this,"Error al obtener datos", UtilView.TOAST_ERROR);
            return;
        }
        initActivity();

    }
    var  contado=1;
    fun initActivity(): Unit {
        val webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }

            override fun onPageFinished(webView: WebView, url: String) {
                if(contado==1)
                {contado++;
                    webView.loadData(htmlData, "text/HTML", "UTF-8")
                }
                if(contado==2)
                {
                    createPDF(webView)

                }
            }
        }
        webView.loadDataWithBaseURL(null, htmlData, "text/HTML", "UTF-8", null)
    }

    fun createPDF(webView: WebView) {
        val cc = CreatePdfFormWebView(this, webView, directoryFile)
        cc.createPDF(nombreArchivo, object : CreatePdfFormWebView.Callback {
            override fun success(pahtOuputFile: String) {
                fullDirectoryFile= pahtOuputFile;
                binding.pdfView.fromFile( File(fullDirectoryFile))
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .enableAntialiasing(true)
                    .load()
            }

            override fun failure() {
                UtilViewMensaje.MENSAJE_simple(
                    this@ViewPdfFromHtmlActivity,
                    "Error",
                    "No se ha podido generar el archivo PDF. Vuelva a intentarlo."
                )
            }
        })
    }

    private fun compartirPDF(path: String) {
        val uriForFile =
            FileProvider.getUriForFile(this@ViewPdfFromHtmlActivity, GlobalVar.PACKAGE_NAME, File(path))
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, "")
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriForFile)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.type = "application/pdf"
        startActivity(Intent.createChooser(shareIntent, "Compartir..."))
    }

    fun FABcompartirPDF(view: View?) {
        if (fullDirectoryFile != null) {
            compartirPDF(fullDirectoryFile);
        } else {
            UtilViewMensaje.MENSAJE_simple(
                this@ViewPdfFromHtmlActivity,
                "Error",
                "No hay archivo dispobible"
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            val dir: File = File(directoryFile)
            if (dir.isDirectory) {
                val hijos = dir.list()
                for (hijo in hijos) {
                    File(dir, hijo).delete()
                }
            } else {
                dir.delete()
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "No se ha podido eliminar archivos basura de pdf",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}