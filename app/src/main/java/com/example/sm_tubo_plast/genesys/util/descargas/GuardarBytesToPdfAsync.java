package com.example.sm_tubo_plast.genesys.util.descargas;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

    public class GuardarBytesToPdfAsync {

        private static final String TAG = "GuardarPdfAsync";

        private final Context context;
        private final ExecutorService executorService;

        public GuardarBytesToPdfAsync(Context context) {
            this.context = context.getApplicationContext();
            this.executorService = Executors.newSingleThreadExecutor();
        }

        public void guardar(List<Integer> contenidoPdf, String nombreArchivo, Callback callback) {
            executorService.execute(() -> {
                try {
                    // 1. Convertir List<Integer> a byte[]
                    byte[] pdfBytes = convertirABytes(contenidoPdf);
                    // 2. Directorio privado de la aplicación
                    String directorioPatch = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                    File directorio = new File(directorioPatch);
                    if (!directorio.exists() && !directorio.mkdirs()) {
                        notificarError(callback, "No se pudo crear el directorio");
                        return;
                    }
                    // 3. Archivo final
                    File archivo = new File(directorio, nombreArchivo);
                    Log.i(TAG, "Guardando PDF: " + archivo.getAbsolutePath());
                    // 4. Guardar bytes
                    try (FileOutputStream outputStream =
                                 new FileOutputStream(archivo)) {
                        outputStream.write(pdfBytes);
                        outputStream.flush();
                    }
                    Log.i(TAG, "PDF guardado correctamente");
                    // 5. Callback en hilo principal
                    android.os.Handler handler =
                            new android.os.Handler(android.os.Looper.getMainLooper());

                    handler.post(() ->
                            callback.onSuccess(
                                    archivo,
                                    archivo.getAbsolutePath()
                            )
                    );

                } catch (Exception e) {

                    Log.e(TAG, "Error guardando PDF", e);

                    notificarError(callback, e.getMessage());
                }
            });
        }

        private byte[] convertirABytes(List<Integer> lista) {

            byte[] bytes = new byte[lista.size()];

            for (int i = 0; i < lista.size(); i++) {
                bytes[i] = (byte) (int) lista.get(i);
            }

            return bytes;
        }

        private void notificarError(Callback callback, String mensaje) {

            android.os.Handler handler =
                    new android.os.Handler(android.os.Looper.getMainLooper());

            handler.post(() ->
                    callback.onError(mensaje)
            );
        }

        public void cerrar() {
            executorService.shutdown();
        }

        public interface Callback {

            void onSuccess(File archivo, String ruta);

            void onError(String mensaje);
        }
}