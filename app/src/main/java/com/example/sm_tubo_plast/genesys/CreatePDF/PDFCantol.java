package com.example.sm_tubo_plast.genesys.CreatePDF;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.DataCabeceraPDF;
import com.example.sm_tubo_plast.genesys.CreatePDF.model.ReportePedidoDetallePDF;
import com.example.sm_tubo_plast.genesys.CreatePDF.pdf_html.util.UtilImagen;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.util.FormateadorNumero;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PDFCantol {
    public static  int ENVIO_A_CLIENTE = 1;
    public static  int ENVIO_A_INTERNO = 2;

    public static void createPdf(Context context,
                     String nombreArchivo,
                     DataCabeceraPDF dataCabecera,
                     ArrayList<ReportePedidoDetallePDF> _dataPedidoCabeceraDetalles,
                     double tasaCambioSolesToDolar,
                     int tipo_de_envio,
                    String[] listaCondDespacho
    ) throws FileNotFoundException{

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath,  ""+nombreArchivo);

        PdfWriter writer  = new PdfWriter(file);

        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument);
        document.setMargins(20, 20, 20, 20);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.banner_cantol_empresa);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);

        float[] widthsFila2x = { 7f, 3f };
        Table tableConFila2 = new Table(UnitValue.createPercentArray(widthsFila2x)).useAllAvailableWidth();
        float[] widthsFila1 = { 1.5f, 3.5f, 5f };
        Table fila1 = new Table(UnitValue.createPercentArray(widthsFila1)).useAllAvailableWidth();
        float[] widthsFila2 = { 1.5f, 3.5f, 2f, 3f };
        Table fila2 = new Table(UnitValue.createPercentArray(widthsFila2)).useAllAvailableWidth();

        Style estiloTitle = new Style()
                .setBorder(new SolidBorder(0.7f))
                .setPadding(4)
                .setFontSize(11);
        Style estilo = new Style()
                .setBorder(new SolidBorder(0.7f))
                .setPadding(4)
                .setFontSize(7);

        fila1.addCell(new Cell().add(new Paragraph("Razón social:").setBold()).addStyle(estilo));

        fila1.addCell( new Cell().add(new Paragraph(dataCabecera.getNomcli())).addStyle(estilo));

        fila1.addCell(new Cell().add(new Paragraph(dataCabecera.getTipoRegistro()+" N° "+dataCabecera.getOc_numero())
                                .setBold()
                                .setTextAlignment(TextAlignment.CENTER))
                        .addStyle(estiloTitle)
        );

// ===== SEGUNDA FILA (4 COLUMNAS) =====
        fila2.addCell(
                new Cell().add(new Paragraph("RUC:").setBold())
                        .addStyle(estilo)
        );
        fila2.addCell(
                new Cell().add(new Paragraph(dataCabecera.getRuccli()))
                        .addStyle(estilo)
        );
        fila2.addCell(
                new Cell().add(new Paragraph("Fecha de Cotización:").setBold())
                        .addStyle(estilo)
        );
        fila2.addCell(
                new Cell().add(new Paragraph(dataCabecera.getFecha_oc())).addStyle(estilo)
        );

        //-------------------------------FILA 3----------------------------------------------------------------
        String observaciones2_convertir =dataCabecera.getObservacion2();
        ArrayList<String> observacion = VARIABLES.GetListString(observaciones2_convertir, 4);
        String nombreTransporte = observacion.size()>0?observacion.get(0):"";
        String direccionAgencia = observacion.size()>1?observacion.get(1):"";
        String nombreProyecto = observacion.size()>2?observacion.get(2):"";
        String direccionProyecto = observacion.size()>3?observacion.get(3):"";

        String atencion_telefono = dataCabecera.getObservacion();
        ArrayList<String> at_tel = VARIABLES.GetListString(atencion_telefono, 2);
        String clienteContacto = at_tel.size()>0?at_tel.get(0):"";
        String telefonoContacto = at_tel.size()>1?at_tel.get(1):"";


        ArrayList<Table> listFilas=new ArrayList<>();
        Table filax = new Table(UnitValue.createPercentArray(widthsFila2)).useAllAvailableWidth();
        filax.addCell(
                new Cell().add(new Paragraph("Proyecto:").setBold()).addStyle(estilo)
        );
        filax.addCell(
                new Cell().add(new Paragraph(nombreProyecto)).addStyle(estilo)
        );
        filax.addCell(
                new Cell().add(new Paragraph("Condición de pago:").setBold()).addStyle(estilo)
        );
        filax.addCell(
                new Cell().add(new Paragraph(dataCabecera.getDesforpag())).addStyle(estilo)
        );
        listFilas.add(filax);
        //-------------------------------NEW FILA----------------------------------------------------------------
        filax = new Table(UnitValue.createPercentArray(widthsFila2)).useAllAvailableWidth();

        filax.addCell(
                new Cell().add(new Paragraph("Dirección Fiscal:").setBold()).addStyle(estilo)
        );
        boolean isCotizacion= (dataCabecera.getTipoRegistro().equals(PedidosActivity.TIPO_COTIZACION));
        filax.addCell(
                new Cell().add(new Paragraph(dataCabecera.getDireccionFiscalCliente())).addStyle(estilo)
        );
        if(isCotizacion){
            filax.addCell(new Cell().add(new Paragraph("Validéz Oferta:").setBold()).addStyle(estilo)
            );
            filax.addCell(new Cell().add(new Paragraph(""+dataCabecera.getDiasVigencia()+" dias ")).addStyle(estilo)
                            .setTextAlignment(TextAlignment.CENTER)
            );
        }
        else{
            filax.addCell(
                    new Cell().add(new Paragraph("Fecha Entrega:").setBold()).addStyle(estilo)
            );
            filax.addCell(new Cell().add(new Paragraph(""+dataCabecera.getFecha_mxe())).addStyle(estilo)
                            .setTextAlignment(TextAlignment.CENTER)
            );
        }
        listFilas.add(filax);
        //-------------------------------NEW FILA----------------------------------------------------------------
        filax = new Table(UnitValue.createPercentArray(widthsFila2)).useAllAvailableWidth();
        filax.addCell(
                new Cell().add(new Paragraph("Contacto Cliente:").setBold()).addStyle(estilo)
        );
        filax.addCell(
                new Cell().add(new Paragraph(clienteContacto)).addStyle(estilo)
        );
        filax.addCell(
                new Cell().add(new Paragraph("Dirección Proyecto:").setBold()).addStyle(estilo)
        );
        filax.addCell(
                new Cell().add(new Paragraph(direccionProyecto)).addStyle(estilo)
        );
        listFilas.add(filax);
        //-------------------------------NEW FILA----------------------------------------------------------------
        filax = new Table(UnitValue.createPercentArray(widthsFila2)).useAllAvailableWidth();
        filax.addCell(getNombreCelda("E-mail Cliente:"));
        filax.addCell(getValorCelda(""+dataCabecera.getEmail_cliente()));
        filax.addCell(getNombreCelda("Telfono Contacto:"));
        filax.addCell(getValorCelda(telefonoContacto));
        listFilas.add(filax);
        //-------------------------------NEW FILA----------------------------------------------------------------
        float[] widthsColumns6 = { 0.6f, 4.4f, 2f,1f, 1f, 1f };
        filax = new Table(UnitValue.createPercentArray(widthsColumns6)).useAllAvailableWidth();
        //DeviceRgb gray = new DeviceRgb(243, 238, 238);
        filax.addCell(getNombreCelda("ITEM"));
        filax.addCell(getNombreCelda("DESCRIPCION"));
        filax.addCell(getNombreCelda("CODIGO"));
        filax.addCell(getNombreCelda("CANT."));
        filax.addCell(getNombreCelda("VALOR UNI. SIN IGV"));
        filax.addCell(getNombreCelda("VALOR TOTAL"));

        List<ReportePedidoDetallePDF> dataPedidoCabeceraDetalles=ReportePedidoDetallePDF.Companion.orderOcNumeroAndItem(_dataPedidoCabeceraDetalles);
        int cantidadUnds=0;
        for (int i = 0; i < dataPedidoCabeceraDetalles.size(); i++) {
            ReportePedidoDetallePDF detalle = dataPedidoCabeceraDetalles.get(i);
            cantidadUnds+=detalle.getCantidad();
                    //filax = new Table(UnitValue.createPercentArray(widthsColumns6)).useAllAvailableWidth();
            filax.addCell(getValorCeldaCenter(detalle.getItem()));
            filax.addCell(getValorCelda(detalle.getDespro()));
            filax.addCell(getValorCelda(detalle.getCodpro()));
            filax.addCell(getValorCeldaCenter(""+detalle.getCantidad()));
            String precio_bruto = FormateadorNumero.formatter3decimal(dataPedidoCabeceraDetalles.get(i).getPrecio_bruto());
            String precio_neto = FormateadorNumero.formatter3decimal(dataPedidoCabeceraDetalles.get(i).getPrecio_neto());
            filax.addCell(getValorCeldaEnd(precio_bruto));
            filax.addCell(getValorCeldaEnd(precio_neto));
        }
        poblarTotales(filax, estilo, cantidadUnds, dataCabecera);
        listFilas.add(filax);
        //observaciones
        filax = new Table(UnitValue.createPercentArray(new float[]{ 10f })).useAllAvailableWidth();
        filax.addCell(getCeldaTituloAndValor("OBSERVACIONES:", "\t"+dataCabecera.getObservacion3()));
        filax.addCell(getCeldaTituloAndValor("CONDICIONES COMERCIALES:",
                "\n"+getTextFromListaToString(dataCabecera.getCondicionComercial().split(VARIABLES.SEPARADOR_OBSERVACION))));
        filax.addCell(getCeldaTituloAndValor("CONDICIONES DE DESPACHO:",
                "\n"+getTextFromListaToString(listaCondDespacho)));
        listFilas.add(filax);
        //fotter
        tableConFila2.addCell(new Cell(2,1)
                .add(new Paragraph("\n" +
                        "Emitir OC a Distrimax SAC - RUC: 20514364665\n" +
                        "Cta. Cte. BCP Soles: 194-1553314-0-39\n" +
                        "Cta. Cte. Interbank Soles: 0593000717425\n" +
                        "Cta. Cte. BBVA Soles: 00110109630100053821\n" +
                        "\n"))
                .addStyle(estilo));
        tableConFila2.addCell(getNombreCelda("ACEPTADO CLIENTE").setTextAlignment(TextAlignment.CENTER));
        tableConFila2.addCell(getNombreCelda(""));
        tableConFila2.addCell(getCeldaTituloAndValor("Asesor Comercial:",dataCabecera.getNomven()
                +" / tel:"+dataCabecera.getTelefono_vendedor()
                +" / "+dataCabecera.getEmail_vendedor()));
        tableConFila2.addCell(getNombreCelda("Fecha, Nombre, Firma y Sello").setTextAlignment(TextAlignment.CENTER));
        listFilas.add(tableConFila2);

// Agregar tablas al documento
        document.add(image);
        document.add(fila1);
        document.add(fila2);
        for (int i = 0; i < listFilas.size(); i++) {
            document.add(listFilas.get(i));
        }
        document.close();
    }
    private static String getTextFromListaToString(String[] lista){
        StringBuilder listatxt=new StringBuilder();
        for (int i = 0; i < lista.length; i++) {
            if(listatxt.length()>0)listatxt.append("\n");
            listatxt.append(i + 1).append(") ").append(lista[i]);
        }
        return listatxt.toString();
    }
    private static Cell getCeldaTituloAndValor(String titulo, String valor){
        Cell celda = new Cell();
        Paragraph p = new Paragraph()
                .add(new Text(titulo).setBold())
                .add(new Text("\t"+valor));
        celda.add(p).setBorder(Border.NO_BORDER)
                .setFontSize(7);
        return celda;
    }
    private static void poblarTotales(Table filaTotales, Style estilo, int cantidadUnds, DataCabeceraPDF dataCabecera){
        filaTotales.addCell(new Cell(1,3)
                .add(new Paragraph("CANTIDAD TOTAL DE PRODUCTOS"))
                .setTextAlignment(TextAlignment.CENTER).setBold()
                .addStyle(estilo));

        filaTotales.addCell(getValorCeldaCenter(""+cantidadUnds));
        filaTotales.addCell(getNombreCelda("SUB TOTAL"));
        filaTotales.addCell(getValorCeldaEnd(FormateadorNumero.formatter3decimal(dataCabecera.getSubtotal())));

        filaTotales.addCell(new Cell(2,4)
                .add(new Paragraph(""))
                .setTextAlignment(TextAlignment.CENTER)
                .addStyle(estilo));

        filaTotales.addCell(getNombreCelda("IGV 18%"));
        filaTotales.addCell(getValorCeldaEnd(FormateadorNumero.formatter3decimal(dataCabecera.getValor_igv())));


        filaTotales.addCell(getNombreCelda("TOTAL%"));
        filaTotales.addCell(getValorCeldaEnd(FormateadorNumero.formatter3decimal(dataCabecera.getMonto_total())));

    }
    private static Cell getNombreCelda(String nombre) {
        return new Cell()
                .add(new Paragraph(nombre)
                        .setFontSize(7)
                        .setMargin(0))
                .setBold()
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(4)
                .setBorder(new SolidBorder(0.7f));
    }
    private static Cell  getValorCelda(String nombre){
            return new Cell().add(new Paragraph(nombre.trim())).addStyle(
                    new Style()
                            .setBorder(new SolidBorder(0.7f))
                            .setPadding(4)
                            .setFontSize(7)
            );
    }
    private static Cell  getValorCeldaCenter(String nombre){
        return new Cell().add(new Paragraph(nombre.trim()))
                .setTextAlignment(TextAlignment.CENTER)
                .addStyle(
                new Style()
                        .setBorder(new SolidBorder(0.7f))
                        .setPadding(4)
                        .setFontSize(7)
        );
    }
    private static Cell  getValorCeldaEnd(String nombre){
        return new Cell().add(new Paragraph(nombre.trim()))
                .setTextAlignment(TextAlignment.RIGHT)
                .addStyle(
                        new Style()
                                .setBorder(new SolidBorder(0.7f))
                                .setPadding(4)
                                .setFontSize(7)
                );
    }

}
