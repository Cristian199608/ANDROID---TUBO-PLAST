package com.example.sm_tubo_plast.genesys.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(var context: Context) {
    var prefs: SharedPreferences?
    var editor: SharedPreferences.Editor? = null
    init {
        prefs = context.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    }
    private fun openEdit() {
        editor = prefs!!.edit()
    }

    private fun ApplyCloseEdit() {
        editor!!.apply()
        editor!!.clear()
        editor = null
    }

    val codigoVendedor: String? get() = if (prefs != null) prefs!!.getString("codven", "por_defecto") else ""
    val usuario: String? get() = if (prefs != null) prefs!!.getString("usuario", "") else ""
    val password: String?  get() = if (prefs != null) prefs!!.getString("pass", "") else ""

    var codigoNivel: String?
        get() = if (prefs != null) prefs!!.getString("codigoRol", "por_defecto") else ""
        set(valor) {
            openEdit()
            editor!!.putString("codigoRol", valor)
            ApplyCloseEdit()
        }

    var recordarInicioSession: Boolean
        get() = if (prefs != null) prefs!!.getBoolean("recordarInicioSession", false) else false
        set(recordar) {
            openEdit()
            editor!!.putBoolean("recordarInicioSession", recordar)
            ApplyCloseEdit()
        }

    fun createLoginSession(user: String?, pass: String?) {
        openEdit()
        editor!!.putString("usuario", user)
        editor!!.putString("pass", pass)
        ApplyCloseEdit()
    }

    
}