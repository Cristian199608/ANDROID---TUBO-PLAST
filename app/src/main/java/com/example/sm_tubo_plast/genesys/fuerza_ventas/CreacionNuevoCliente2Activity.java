package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class
CreacionNuevoCliente2Activity extends AppCompatActivity {

    WebView myWebVew;
    FloatingActionButton myFAB;
    SwipeRefreshLayout swipe_refresh=null;

    VARIABLES VAR=new VARIABLES();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_nuevo_cliente2);
        myWebVew=findViewById(R.id.myWebVew);
        myFAB=findViewById(R.id.myFAB);
        myFAB.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        String LATITUD = bundle.getString("LATITUD");
        String LONGITUD = bundle.getString("LONGITUD");
        String CODVEN = bundle.getString("CODVEN");

        DBclasses dBclasses=new DBclasses(this);
        String rucEmpresa=dBclasses.getRuc();
        String servicioWeb=dBclasses.getServicioCreacionCliente();


        myWebVew.setWebViewClient(new WebViewClient());
        if (servicioWeb.length()>0){
//        myWebVew.loadUrl("http://192.168.0.43/newclient/cn.php?emp="+rucEmpresa);
//        myWebVew.loadUrl("http://190.187.25.122/newclient/cn.php?emp="+rucEmpresa);
            servicioWeb=servicioWeb.replace("RUC_EMPRESA",rucEmpresa );
            servicioWeb=servicioWeb.replace("LATITUD",LATITUD );
            servicioWeb=servicioWeb.replace("LONGITUD",LONGITUD );
            servicioWeb=servicioWeb.replace("CODVEN",CODVEN );
            myWebVew.loadUrl(servicioWeb);
            WebSettings webSettings=myWebVew.getSettings();
            webSettings.setJavaScriptEnabled(true);

            swipe_refresh=(SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
            swipe_refresh.setColorSchemeColors(
                    getResources().getColor(R.color.s1),
                    getResources().getColor(R.color.s2),
                    getResources().getColor(R.color.s3),
                    getResources().getColor(R.color.s4)
            );

            /*import android.app.Activity;
            import android.os.Bundle;
            import android.webkit.WebView;
            import android.webkit.WebViewClient;
            public class MainActivity extends Activity {
                private WebView webView;
                private JavaScriptInterFace javaScriptInterFace; @Override protected void
                onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_main);
                    webView=(WebView)findViewById(R.id.webView);
                    javaScriptInterFace=new JavaScriptInterFace(this);
                    webView.addJavascriptInterface(javaScriptInterFace, "AndroidFunction");
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl("file:///android_asset/test.html");
                    webView.setWebViewClient(new WebViewClient()); } }*/

             /*import android.content.Context;
             import android.util.Log;
             import android.widget.Toast;
             public class JavaScriptInterFace {
                 Context mContext;
                 JavaScriptInterFace(Context c) {
                     mContext = c; }
                     public int changeImage(){
                     Log.e("Got", "it"+2); return 2;
                 }
                 public void showToast(){
                         Toast.makeText(mContext, "hi", Toast.LENGTH_SHORT).show();
                     }
             }
             */


            myWebVew.setWebChromeClient(new WebChromeClient());
            myWebVew.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    swipe_refresh.setRefreshing(true);
                    //MainActivity.this.setProgress(progress * 1000);
                    myFAB.setVisibility(View.GONE);

                    if (progress == 100) {
                        myFAB.setVisibility(View.VISIBLE);
                        swipe_refresh.setRefreshing(false);
                    }
                }
            });

            swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    myWebVew.reload();
                }
            });

            myFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else{
            Toast.makeText(this, "No se ha podido obtener la url del servicio para la creación de clientes.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (myWebVew.canGoBack()){
            myWebVew.goBack();
        }else{
            super.onBackPressed();
        }
    }
}
