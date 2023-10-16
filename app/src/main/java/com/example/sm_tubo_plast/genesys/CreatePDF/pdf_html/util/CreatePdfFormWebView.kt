package com.example.sm_tubo_plast.genesys.CreatePDF.pdf_html.util

import android.app.Activity
import android.app.ProgressDialog
import android.os.Environment
import android.print.PdfView
import android.webkit.WebView
import com.example.sm_tubo_plast.genesys.util.VARIABLES
import java.io.File

class  CreatePdfFormWebView{
    var activity:Activity?=null
    var webView:WebView?=null
    var directory_container: String;
    constructor(activity: Activity?, webView: WebView, directory_container:String) {
        this.activity = activity
        this.webView = webView
        this.directory_container=directory_container;
    }

    constructor() {
        this.activity = null
        this.webView = null
        this.directory_container="";
    }

    fun getDatax():String{
        return this.directory_container;
    }

    fun createPDF( nombre_archivo: String, ddd: Callback){
        //val carpeta_contenedor =activity?.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + "/"+CONFIGURACION.CARPERTA_CONTENEDOR+"/"+CONFIGURACION.CARPERTA_CONTENEDOR_PDF+"/"

        val carpeta_contenedor =activity?.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + "/"+VARIABLES.CARPERTA_CONTENEDOR+"/"+VARIABLES.CARPERTA_CONTENEDOR_PDF+"/"
        val fileName = "$nombre_archivo"

        val dir = File(carpeta_contenedor);
        if (!dir.exists()){
            dir.mkdirs()
        }

        val file = File(dir, "")
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()
        activity?.let {
            webView?.let { it1 ->
                PdfView.createWebPdfJob(it, it1, file, fileName, object : PdfView.Callback {

                    override fun success(pahtOuputFile: String) {
                        progressDialog.dismiss()
                        ddd.success(pahtOuputFile);
                    }
                    override fun failure() {
                        progressDialog.dismiss()
                        ddd.failure()
                    }
                })
            }
        }
    }

    interface Callback {
        fun success(pahtOuputFile: String)
        fun failure()
    }

}