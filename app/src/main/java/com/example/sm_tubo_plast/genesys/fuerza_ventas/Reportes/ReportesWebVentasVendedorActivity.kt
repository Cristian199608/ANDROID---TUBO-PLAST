package com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sm_tubo_plast.R
import com.example.sm_tubo_plast.databinding.ActivityReportesVentasVendedorBinding
import com.example.sm_tubo_plast.genesys.session.SessionManager
import com.example.sm_tubo_plast.genesys.util.GlobalVar
import com.example.sm_tubo_plast.genesys.util.SharePrefencia.PreferenciaPrincipal


class ReportesWebVentasVendedorActivity : AppCompatActivity() {
    lateinit var binding: ActivityReportesVentasVendedorBinding;
    var filePathCallback: ValueCallback<Array<Uri>>? = null
    private var FILE_CHOOSER_RESULT_CODE=1200;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReportesVentasVendedorBinding.inflate(layoutInflater);
        setContentView(binding.root);
        loadWebView();
    }

    private fun loadWebView() {
        binding.myWebVew.setWebViewClient(WebViewClient());
        val ruta=GlobalVar.UrlBase()+"/tuboplast?codven="+(SessionManager(this).codigoVendedor);
        binding.myWebVew.loadUrl(ruta);
        val webSettings: WebSettings = binding.myWebVew.getSettings()
        webSettings.javaScriptEnabled = true

        binding.swipeRefreshLayout.setColorSchemeColors(
                resources.getColor(R.color.s1),
                resources.getColor(R.color.s2),
                resources.getColor(R.color.s3),
                resources.getColor(R.color.s4)
        )
        binding.myWebVew.webChromeClient = WebChromeClient()
        binding.myWebVew.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                binding.swipeRefreshLayout.setRefreshing(true)
                if (progress == 100) {
                    binding.swipeRefreshLayout.setRefreshing(false)
                }
            }

            override fun onShowFileChooser(webView: WebView?, _filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
                super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
                filePathCallback=_filePathCallback;
               var valor=""
                fileChooserParams!!.acceptTypes.forEach {
                    if(valor.length>0)valor+=",";
                    valor+= it.toString();

                }
                openImageChooserActivity(valor.toString());
                return true;
            }
        }

        binding.swipeRefreshLayout.isEnabled = false;
    }

    private fun openImageChooserActivity(aceptFiles:String) {

        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "*/$aceptFiles"
        startActivityForResult(Intent.createChooser(i, "Seleccione un archivo"), FILE_CHOOSER_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_RESULT_CODE && resultCode==RESULT_OK && data!=null) {
            val result = data.data;
            var listaUri= ArrayList<Uri>();
            listaUri.add(result!!);
            filePathCallback!!.onReceiveValue(listaUri.toTypedArray());
            filePathCallback=null;
        }
    }
}