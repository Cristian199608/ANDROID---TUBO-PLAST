package com.example.sm_tubo_plast.genesys.util

import android.net.Uri
import java.util.HashMap

class FunctionsGlobales {
    fun getValuesHashMap(uriForFile: HashMap<String, Uri>): String {
        var texto="${uriForFile.size} archivos seleccionados";
        var index=0;
        for (mutableEntry in uriForFile) {
            index++;
            texto+="\n$index) "+mutableEntry.key;
        }
        return texto;
    }
}