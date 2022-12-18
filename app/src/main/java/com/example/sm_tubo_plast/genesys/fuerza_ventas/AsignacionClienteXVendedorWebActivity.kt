package com.example.sm_tubo_plast.genesys.fuerza_ventas

import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sm_tubo_plast.R
import com.example.sm_tubo_plast.databinding.ActivityAsignacionClienteXvendedorWebBinding
import com.example.sm_tubo_plast.genesys.fuerza_ventas.web.WebAppAsgnacionClienteInterface
import com.example.sm_tubo_plast.genesys.session.SessionManager
import com.example.sm_tubo_plast.genesys.util.GlobalVar

class AsignacionClienteXVendedorWebActivity : AppCompatActivity() {
    lateinit var binding: ActivityAsignacionClienteXvendedorWebBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAsignacionClienteXvendedorWebBinding.inflate(layoutInflater);
        setContentView(binding.root);
        loadWebView();
    }
    private fun loadWebView() {
        binding.myWebVew.setWebViewClient(WebViewClient());
        val ruta= GlobalVar.UrlBase()+"?codven="+(SessionManager(this).codigoVendedor);
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
        binding.myWebVew.addJavascriptInterface( WebAppAsgnacionClienteInterface(this),"Android") ;

        binding.myWebVew.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                binding.swipeRefreshLayout.setRefreshing(true)
                if (progress == 100) {
                    binding.swipeRefreshLayout.setRefreshing(false)
                }
            }
        }
        binding.swipeRefreshLayout.isEnabled = false;
    }

    override fun onBackPressed() {
        binding.myWebVew.loadUrl("javascript:FinalizarVentanaAsignacionCliente()")
    }

}