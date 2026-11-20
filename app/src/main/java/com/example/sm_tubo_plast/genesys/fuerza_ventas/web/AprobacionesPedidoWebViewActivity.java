package com.example.sm_tubo_plast.genesys.fuerza_ventas.web;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.databinding.ActivityAprobacionesPedidoWebViewBinding;

@SuppressLint("LongLogTag")
public class AprobacionesPedidoWebViewActivity extends AppCompatActivity {
    private static final String TAG = "AprobacionesPedidoWebViewActivity";

    private ActivityAprobacionesPedidoWebViewBinding binding;
    private int codven =0;
    private String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAprobacionesPedidoWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        codven = bundle.getInt("codven");
        url = bundle.getString("url");
        eventos();
        startView();
    }
    private void eventos(){
        binding.swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.s1),
                getResources().getColor(R.color.s2),
                getResources().getColor(R.color.s3),
                getResources().getColor(R.color.s4)
        );

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    binding.myWebVew.postUrl(url, getParametros());
                }catch (Exception e){
                    e.printStackTrace();
                    showError(e);
                }
            }
        });
    }
    private byte[] getParametros() throws Exception{
        String parametros =
                "codven=" + java.net.URLEncoder.encode(String.valueOf(codven), "UTF-8") +
                        "&usepas=" + java.net.URLEncoder.encode("", "UTF-8");
        return parametros.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }
    private void startView() {
        try {

            Log.i(TAG, "startView URL: " + url);

            binding.myWebVew.getSettings().setJavaScriptEnabled(true);

            binding.myWebVew.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    binding.swipeRefreshLayout.setVisibility(View.VISIBLE);
//                    binding.swipeRefreshLayout.setRefreshing(true);
                }
            });

            binding.myWebVew.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    if (progress >= 100) {
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });

            binding.swipeRefreshLayout.setRefreshing(true);
            binding.myWebVew.postUrl(url,getParametros());

        } catch (Exception e) {
            e.printStackTrace();
            showError(e);
        }
    }
    private void showError(Exception e){
        binding.swipeRefreshLayout.setRefreshing(false);
        Log.e(TAG, "Error al cargar aprobaciones", e);

        Toast.makeText(
                this,
                "Error al cargar la pantalla de aprobaciones",
                Toast.LENGTH_SHORT
        ).show();
    }
}