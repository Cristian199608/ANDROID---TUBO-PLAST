package com.example.sm_tubo_plast.genesys.CreatePDF;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.DataCabeceraPDF;
import com.example.sm_tubo_plast.genesys.BEAN.ReportePedidoDetallePDF;
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
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class PDF {
    public static  int ENVIO_A_CLIENTE = 1;
    public static  int ENVIO_A_INTERNO = 2;
    public static void createPdf(Context context,
                                 String nombreArchivo,
                                 /*
                                 String oc_numero,String tipoRegistro, String ruccli, String codven, String nomcli,
                                 String telefono, String nomven, String direccion,
                                 String email_cliente, String email_vendedor,
                                 String desforpag, String monto_total,
                                 String valor_igv, String sub_total, String peso_total,
                                 String fecha_oc, String fecha_mxe,*/
                                 DataCabeceraPDF dataCabecera,
                                 ArrayList<ReportePedidoDetallePDF> dataPedidoCabeceraDetalles,
                                 double tasaCambioSolesToDolar,
                                 int tipo_de_envio) throws FileNotFoundException
    {



        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath,  nombreArchivo);
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
        document.setMargins(20,20,20,20);

        /***
         * ADD COLOR
         */
        DeviceRgb yellow = new DeviceRgb(255,255,0);
        DeviceRgb blue = new DeviceRgb(153, 204,255);
        DeviceRgb gray = new DeviceRgb(192,192,192);

        /***
         * Formatter
         */
        String subtotalFormateado = FormateadorNumero.formatter2decimal(dataCabecera.getSubtotal());
        String igvFormateado = FormateadorNumero.formatter2decimal(dataCabecera.getValor_igv());
        String total_netoFormateado = FormateadorNumero.formatter2decimal(dataCabecera.getMonto_total());

        /***
         * PRINCIPAL IMAGE
         */
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tuboplast_baner_logo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);

        /***
         * TABLE N° 1
         */
        float columnWidth[] = {300,50,300};
        Table table = new Table(columnWidth);

        table.addCell(new Cell().setBorder(Border.NO_BORDER));
        table.addCell(new Cell().setBorder(Border.NO_BORDER));
        table.addCell(new Cell().setBorder(Border.NO_BORDER));

        table.addCell(new Cell().setBackgroundColor(yellow).add(new Paragraph(""+dataCabecera.getTipoRegistro()).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().setBold().setBackgroundColor(gray).add(new Paragraph("N°").setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().setBackgroundColor(yellow).add(new Paragraph(dataCabecera.getOc_numero()).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().setBorder(Border.NO_BORDER));
        table.addCell(new Cell().setBorder(Border.NO_BORDER));
        table.addCell(new Cell().setBorder(Border.NO_BORDER));

        /***
         * SPLIT
         */

        String atencion_telefono = dataCabecera.getObservacion();
        ArrayList<String> at_tel = VARIABLES.GetListString(atencion_telefono, 2);
        String clienteContacto = at_tel.size()>0?at_tel.get(0):"";
        String clienteTelefono = at_tel.size()>1?at_tel.get(1):"";

        /***
         * TABLE N° 2
         */


        float columnWidth1[] = {130, 150, 90, 90, 100, 140};
        Table table1 = new Table(columnWidth1);
        //TABLE 2 ----- 01
        table1.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("RUC CLIENTE: ").setFontSize(7f)));
        table1.addCell(new Cell().add(new Paragraph(dataCabecera.getRuccli()).setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table1.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("FECHA: ").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table1.addCell(new Cell().add(new Paragraph(dataCabecera.getFecha_oc()).setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table1.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("COD VEND: ").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table1.addCell(new Cell().add(new Paragraph(dataCabecera.getCodven()).setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));

        //TABLE 2 ----- 02
        table1.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("RAZON SOCIAL:").setFontSize(7f)));
        table1.addCell(new Cell().add(new Paragraph(dataCabecera.getNomcli()).setFontSize(7f)));
        table1.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("TELEFONO:").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table1.addCell(new Cell().add(new Paragraph(dataCabecera.getTelefono()).setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table1.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("VENDEDOR:").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table1.addCell(new Cell().add(new Paragraph(dataCabecera.getNomven()).setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));

        //TABLE 2 ----- 03
        table1.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("ATENCIÓN:").setFontSize(7f)));
        table1.addCell(new Cell().add(new Paragraph(clienteContacto).setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table1.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("TELEFONO:").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table1.addCell(new Cell().add(new Paragraph(clienteTelefono).setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table1.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("TELEFONO").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table1.addCell(new Cell().add(new Paragraph(dataCabecera.getTelefono_vendedor()).setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));

        /***
         * TABLE N° 3
         */
        float columnWidth2[] = {147, 420, 123, 158};
        Table table2 = new Table(columnWidth2);
        //table2.setFixedLayout();
        Log.i("PDF CREATE", "WITHS "+table2.getWidth());

        //TABLE 3 ----- 01
        table2.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("DIRECCION").setFontSize(7f)));
        table2.addCell(new Cell().add(new Paragraph(dataCabecera.getDireccion()).setTextAlignment(TextAlignment.LEFT).setFontSize(7f)));
        table2.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("AREA").setFontSize(7f).setTextAlignment(TextAlignment.CENTER)));
        table2.addCell(new Cell().add(new Paragraph(dataCabecera.getText_area()).setTextAlignment(TextAlignment.CENTER).setFontSize(6f)));

        //TABLE 3 ----- 02
        table2.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("E-MAIL").setFontSize(7f)));
        table2.addCell(new Cell().add(new Paragraph(dataCabecera.getEmail_cliente()).setFontSize(7f)));
        table2.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("E-MAIL").setFontSize(7f).setTextAlignment(TextAlignment.CENTER)));
        table2.addCell(new Cell().add(new Paragraph(dataCabecera.getEmail_vendedor()).setFontSize(7f).setTextAlignment(TextAlignment.CENTER)));

        //TABLE 3 ----- 03
        table2.addCell(new Cell().setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().setBorder(Border.NO_BORDER));


        /***
         * SPLIT
         */
        String observaciones2_convertir =dataCabecera.getObservacion2();
        ArrayList<String> observacion = VARIABLES.GetListString(observaciones2_convertir, 3);
        String nombreTransporte = observacion.size()>0?observacion.get(0):"";
        String direccionAgencia = observacion.size()>1?observacion.get(1):"";
        String nombreProyecto = observacion.size()>2?observacion.get(2):"";
        /***
         * TABLE N° 4
         */
        float columnWidth3[] = {128, 652};
        Table table3 = new Table(columnWidth3);

        //TABLE 3 ----- 01
        table3.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("TRANSPORTE / AGENCIA: ").setFontSize(7f)));
        table3.addCell(new Cell().add(new Paragraph(nombreTransporte).setTextAlignment(TextAlignment.LEFT).setFontSize(7f)));

        //TABLE 3 ----- 02
        table3.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("DIRECCION AGENCIA").setFontSize(7f)));
        table3.addCell(new Cell().add(new Paragraph(direccionAgencia).setTextAlignment(TextAlignment.LEFT).setFontSize(7f)));

        //TABLE 3 ----- 03
        table3.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("PROYECTO").setFontSize(7f)));
        table3.addCell(new Cell().add(new Paragraph(nombreProyecto)).setTextAlignment(TextAlignment.LEFT).setFontSize(7f));

        //TABLE 3 ----- 04
        table3.addCell(new Cell().setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().setBorder(Border.NO_BORDER));

        /**+
         * TABLE N° 6
         */
        float columnWidth6[] = {160, 185, 135, 460};
        Table table6 = new Table(columnWidth6);

        //TABLE 6 ----- 01
        table6.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("FORMA DE PAGO").setFontSize(7f)));
        table6.addCell(new Cell().add(new Paragraph(dataCabecera.getDesforpag()).setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table6.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("PLAZO ENTREGA").setFontSize(7f)));
        table6.addCell(new Cell().add(new Paragraph("48 HRS.").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));

        //TABLE 6 ----- 02
        table6.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("FECHA ENTREGA").setFontSize(7f)));
        table6.addCell(new Cell().add(new Paragraph(dataCabecera.getFecha_mxe()).setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
        table6.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("VALIDEZ OFERTA").setFontSize(7f)));
        table6.addCell(new Cell().add(new Paragraph(dataCabecera.getDiasVigencia()+" DÍAS").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));

        table6.addCell(new Cell().setBorder(Border.NO_BORDER));
        table6.addCell(new Cell().setBorder(Border.NO_BORDER));
        table6.addCell(new Cell().setBorder(Border.NO_BORDER));
        table6.addCell(new Cell().setBorder(Border.NO_BORDER));

        /***
         * VALIDAMOS MONEDA Y OBSERVACIONES
         */

        String moneda;
        double tipoCambio=-1;
        String observaciones;

        if (dataCabecera.getMoneda().equals("1"))
        {
            moneda = "S/. ";
            tipoCambio=tasaCambioSolesToDolar;
            observaciones = "Precio Expresado en MN SOLES S/. ";
        }
        else if (dataCabecera.getMoneda().equals("2"))
        {
            moneda = "US$ ";
            tipoCambio=1;
            observaciones = "Precio Expresado en DÓLARES AMERICANOS US$ ";
        }
        else
        {
            moneda = dataCabecera.getMoneda();
            tipoCambio=1;
            observaciones = "Precio Expresado en moneda "+dataCabecera.getMoneda();
        }

        /***
         * TABLE ITEMS
         */
        Table item;
        if (tipo_de_envio == ENVIO_A_CLIENTE)
        {
            float columnWidthItems[] = {20, 100, 80, 80, 320, 70, 100};
            Table tableItems = new Table(columnWidthItems);

            //TABLE ITEMS ----- 01
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("ITEM").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("CODIGO").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("CANT").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("UM").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("DESCRIPCION").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("PRECIO UNITARIO").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("SUB TOTAL").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));

            for (int i = 0; i < dataPedidoCabeceraDetalles.size(); i++)
            {
                String itemDetalle = dataPedidoCabeceraDetalles.get(i).getItem().length()>0?dataPedidoCabeceraDetalles.get(i).getItem(): String.valueOf(i + 1);
                tableItems.addCell(new Cell().add(new Paragraph(itemDetalle).setFontSize(7f)));
                tableItems.addCell(new Cell().add(new Paragraph(dataPedidoCabeceraDetalles.get(i).getCodpro())).setTextAlignment(TextAlignment.CENTER).setFontSize(7f));
                tableItems.addCell(new Cell().add(new Paragraph(String.valueOf(dataPedidoCabeceraDetalles.get(i).getCantidad()))).setTextAlignment(TextAlignment.CENTER).setFontSize(7f));
                tableItems.addCell(new Cell().add(new Paragraph(dataPedidoCabeceraDetalles.get(i).getUnidad_medida())).setTextAlignment(TextAlignment.CENTER).setFontSize(7f));
                tableItems.addCell(new Cell().add(new Paragraph(dataPedidoCabeceraDetalles.get(i).getDespro())).setTextAlignment(TextAlignment.LEFT).setFontSize(7f));
                String precio_bruto = FormateadorNumero.formatter2decimal(dataPedidoCabeceraDetalles.get(i).getPrecio_bruto());
                tableItems.addCell(new Cell().add(new Paragraph(precio_bruto).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));
                String precio_neto = FormateadorNumero.formatter2decimal(dataPedidoCabeceraDetalles.get(i).getPrecio_neto());
                tableItems.addCell(new Cell().add(new Paragraph(precio_neto))).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f);
            }

            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));

            item = tableItems;

        }
        else if (tipo_de_envio == ENVIO_A_INTERNO)
        {
            float columnWidthItems[] = {20, 105, 75, 40, 340, 70, 50, 50,50, 100};
            Table tableItems = new Table(columnWidthItems);

            //TABLE ITEMS ----- 01
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("ITEM").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("CODIGO").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("CANT").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("UM").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("DESCRIPCION").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("PRECIO UNITARIO").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("PK ($)").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("DESC \n %").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("DESC \n EXTRA %").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));
            tableItems.addCell(new Cell().setBackgroundColor(blue).add(new Paragraph("SUB TOTAL").setTextAlignment(TextAlignment.CENTER).setFontSize(7f)));


            for (int i = 0; i < dataPedidoCabeceraDetalles.size(); i++)
            {
                String itemDetalle = dataPedidoCabeceraDetalles.get(i).getItem().length()>0?dataPedidoCabeceraDetalles.get(i).getItem(): String.valueOf(i + 1);
                tableItems.addCell(new Cell().add(new Paragraph(itemDetalle).setFontSize(7f)));
                tableItems.addCell(new Cell().add(new Paragraph(dataPedidoCabeceraDetalles.get(i).getCodpro())).setTextAlignment(TextAlignment.CENTER).setFontSize(7f));
                tableItems.addCell(new Cell().add(new Paragraph(String.valueOf(dataPedidoCabeceraDetalles.get(i).getCantidad()))).setTextAlignment(TextAlignment.CENTER).setFontSize(7f));
                tableItems.addCell(new Cell().add(new Paragraph(dataPedidoCabeceraDetalles.get(i).getUnidad_medida())).setTextAlignment(TextAlignment.CENTER).setFontSize(7f));
                tableItems.addCell(new Cell().add(new Paragraph(dataPedidoCabeceraDetalles.get(i).getDespro())).setTextAlignment(TextAlignment.LEFT).setFontSize(7f));
                String precio_bruto = FormateadorNumero.formatter2decimal(dataPedidoCabeceraDetalles.get(i).getPrecio_bruto());
                tableItems.addCell(new Cell().add(new Paragraph(precio_bruto).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));
                String precioKilo = FormateadorNumero.formatter2decimal((Double.parseDouble(dataPedidoCabeceraDetalles.get(i).getPrecio_neto())/dataPedidoCabeceraDetalles.get(i).getPesoTotalProducto())/tipoCambio);
                tableItems.addCell(new Cell().add(new Paragraph(precioKilo).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));
                tableItems.addCell(new Cell().add(new Paragraph(String.valueOf(FormateadorNumero.formatter2decimal(dataPedidoCabeceraDetalles.get(i).getPorcentaje_desc())))).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f));
                tableItems.addCell(new Cell().add(new Paragraph(String.valueOf(FormateadorNumero.formatter2decimal(dataPedidoCabeceraDetalles.get(i).getPorcentaje_desc_extra())))).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f));
                String precio_neto = FormateadorNumero.formatter2decimal(dataPedidoCabeceraDetalles.get(i).getPrecio_neto());
                tableItems.addCell(new Cell().add(new Paragraph(precio_neto))).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f);
            }

            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));
            tableItems.addCell(new Cell().setBorder(Border.NO_BORDER));

            item = tableItems;
        }
        else
        {
            item = null;
        }

        /***
         * OBSERVACIONES
         */
        float[] columnWidthObservaciones = {139, 457, 145, 150};
        Table tableObservaciones = new Table(columnWidthObservaciones);
        float[] columnWidthData = {590, 140, 145};
        Table tableData = new Table(columnWidthData);

        //TABLE OBSERVACIONES
        tableObservaciones.addCell(new Cell().setBackgroundColor(gray).add(new Paragraph("Observaciones:").setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(7f)));
        tableObservaciones.addCell(new Cell());
        tableObservaciones.addCell(new Cell().add(new Paragraph("Sub Total").setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));
        tableObservaciones.addCell(new Cell().add(new Paragraph(moneda + subtotalFormateado).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));

        //TABLE DATA ----- 01
        tableData.addCell(new Cell().add(new Paragraph(dataCabecera.getObservacion3()).setTextAlignment(TextAlignment.LEFT).setFontSize(7f)));
        tableData.addCell(new Cell().add(new Paragraph("IGV").setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));
        tableData.addCell(new Cell().add(new Paragraph(moneda + igvFormateado).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));

        //TABLE DATA ----- 02
        tableData.addCell(new Cell());
        tableData.addCell(new Cell().add(new Paragraph("TOTAL").setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));
        tableData.addCell(new Cell().add(new Paragraph(moneda + total_netoFormateado).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));

        //TABLE DATA ----- 03
        String pa = FormateadorNumero.formatter2decimal(dataCabecera.getPeso_total());
        tableData.addCell(new Cell());
        tableData.addCell(new Cell().add(new Paragraph("Peso Aprox.").setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));
        tableData.addCell(new Cell().add(new Paragraph("KG " + pa).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));

        //TABLE DATA ----- 04
        tableData.addCell(new Cell().add(new Paragraph(observaciones).setFontSize(7f)));
        if (tipo_de_envio == ENVIO_A_INTERNO){
            double pkDolar=Double.parseDouble(dataCabecera.getSubtotal())/Double.parseDouble(dataCabecera.getPeso_total())/tipoCambio;
            tableData.addCell(new Cell().add(new Paragraph("Precio Kilo (sin igv $)").setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));
            tableData.addCell(new Cell().add(new Paragraph(" "+(FormateadorNumero.formatter2decimal(pkDolar)) ).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)));
        }

        /***
         * BANCO
         */
        float[] columnWidthBanco = {192, 170, 250, 170, 380};
        Table tableBanco = new Table(columnWidthBanco);

        //TABLE BANCO ----- 01
        tableBanco.addCell(new Cell().setBackgroundColor(gray).add(new Paragraph("BANCO").setFontSize(7f)));
        tableBanco.addCell(new Cell().setBackgroundColor(gray).add(new Paragraph("CUENTA DÓLARES ($)").setFontSize(7f)));
        tableBanco.addCell(new Cell().setBackgroundColor(gray).add(new Paragraph("CUENTA SOLES (S/").setFontSize(7f)));
        tableBanco.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableBanco.addCell(new Cell().setBackgroundColor(gray).add(new Paragraph("CONSIDERACIONEs").setFontSize(7f)));

        //TABLE BANCO -----02
        tableBanco.addCell(new Cell().add(new Paragraph("Banco de Crédito").setFontSize(7f)));
        tableBanco.addCell(new Cell().add(new Paragraph("194-2009913-1-60").setFontSize(7f)));
        tableBanco.addCell(new Cell().add(new Paragraph("197-2012209-0-42").setFontSize(7f)));
        tableBanco.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableBanco.addCell(new Cell().add(new Paragraph("Garantía por 50 años").setFontSize(7f)));

        //TABLE BANCO -----03
        tableBanco.addCell(new Cell().add(new Paragraph("Banco Continental").setFontSize(7f)));
        tableBanco.addCell(new Cell().add(new Paragraph("CUENTA REC. USD 7918").setFontSize(7f)));
        tableBanco.addCell(new Cell().add(new Paragraph("CUENTA REC. S/. 7197").setFontSize(7f)));
        tableBanco.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableBanco.addCell(new Cell().add(new Paragraph("Asistencia Técnica Constante").setFontSize(7f)));

        //TABLE BANCO -----04
        tableBanco.addCell(new Cell().add(new Paragraph("Banco de la Nación").setFontSize(7f)));
        tableBanco.addCell(new Cell().add(new Paragraph("")));
        tableBanco.addCell(new Cell().add(new Paragraph("00-000-310514").setFontSize(7f)));
        tableBanco.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableBanco.addCell(new Cell().add(new Paragraph("Certificaciones ISO 9001, ISO 14001").setFontSize(7f)));

        //TABLE BANCO ----- 05
        tableBanco.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableBanco.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableBanco.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableBanco.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableBanco.addCell(new Cell().setBorder(Border.NO_BORDER));

        document.add(image);
        document.add(table);
        document.add(table1);
        document.add(table2);
        Log.i("PDF CREATE", "WITHS 2 "+table2.getWidth());
        document.add(table3);
        document.add(table6);
        document.add(item);
        document.add(tableObservaciones);
        document.add(tableData);
        document.add(new Paragraph("\n"));
        document.add(tableBanco);
        document.add(new Paragraph("OBSERVACIONES EN SUMINISTRO").setBold());
        document.add(new Paragraph("1. PARA PEDIDOS PUESTOS EN OBRA, SE ENVIARÁN LOS CAMIONES CON MATERIAL CUBICADO, CLIENTE ASUME COSTO DEL FLETE POR \n" +
                "LOS PRODUCTOS SOBRANTES (O SALDOS) QUE SE ENVIARÁN POR AGENCIA").setFontSize(7f));
        document.add(new Paragraph("2. LA MERCADERIA VIAJA POR CUENTA Y RIESGO DEL CLIENTE, NO SE ACEPTAN CAMBIOS NI DEVOLUCIONES").setFontSize(7f));
        document.add(new Paragraph("3. INDICAR N° RUC O DNI HAL HACER EL DEPÓSITO EN LAS CUENTAS DE TUBOPLAST").setFontSize(7f));
        document.add(new Paragraph("www.tuboplastperu.com | 01 326-1146 | Anexo 127 - 130").setTextAlignment(TextAlignment.CENTER));
        document.close();
    }

}
