package com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sm_tubo_plast.R
import com.example.sm_tubo_plast.databinding.ActivityReportesVentasVendedorBinding
import com.example.sm_tubo_plast.genesys.util.SharePrefencia.PreferenciaPrincipal


class ReportesWebVentasVendedorActivity : AppCompatActivity() {
    lateinit var binding: ActivityReportesVentasVendedorBinding;
    var filePathCallback: ValueCallback<Array<Uri>>? = null
    var FILE_CHOOSER_RESULT_CODE=1200;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReportesVentasVendedorBinding.inflate(layoutInflater);
        setContentView(binding.root);

        loadWebView();
    }

    private fun loadWebView() {
        binding.myWebVew.setWebViewClient(WebViewClient());
        val ruta="http://200.60.105.44/tuboplast?codven="+(PreferenciaPrincipal(this).getCodigoVendedor());
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
                openImageChooserActivity();
                return true;
            }
        }

        binding.swipeRefreshLayout.isEnabled = false;
    }

    private fun openImageChooserActivity() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "*/*"
        startActivityForResult(Intent.createChooser(i, "Seleccione un archivo"), FILE_CHOOSER_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            val result = if (data == null || resultCode != RESULT_OK) null else data.data
            var listaUri= ArrayList<Uri>();
            listaUri.add(result!!);
            filePathCallback!!.onReceiveValue(listaUri.toTypedArray());
            filePathCallback=null;
        }
    }
}