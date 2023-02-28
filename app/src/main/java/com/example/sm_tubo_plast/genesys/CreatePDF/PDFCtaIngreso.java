package com.example.sm_tubo_plast.genesys.CreatePDF;


import android.content.Context;
import android.os.Environment;

import com.example.sm_tubo_plast.genesys.CreatePDF.Decimales.GetDecimales;
import com.example.sm_tubo_plast.genesys.CreatePDF.GetDays.GetDate;
import com.example.sm_tubo_plast.genesys.CreatePDF.getComprobacion.Comprobaciones;
import com.example.sm_tubo_plast.genesys.CreatePDF.model.CTA_INGRESOSPDF;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
public class PDFCtaIngreso
{
    GetDate getDate = new GetDate();
    GetDecimales getDecimales = new GetDecimales();
    public void createCredit(String nombbreArchivo,
                             ArrayList<CTA_INGRESOSPDF> cta_ingresosArrayList,
                             String max_fecha,
                             String min_fecha) throws FileNotFoundException
    {

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, nombbreArchivo);
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.setDefaultPageSize(PageSize.A4.rotate());

        Document document = new Document(pdfDocument);
        document.setMargins(20, 20, 20, 20);

        float[] datesColumns = {20, 50, 20, 50};
        Table datesTable = new Table(datesColumns);

        datesTable.addCell(new Cell().add(new Paragraph("DESDE     :").setTextAlignment(TextAlignment.LEFT).setFontSize(7f)).setBorder(Border.NO_BORDER));
        datesTable.addCell(new Cell().add(new Paragraph(min_fecha).setTextAlignment(TextAlignment.LEFT).setFontSize(7f)).setBorder(Border.NO_BORDER));
        datesTable.addCell(new Cell().add(new Paragraph("HASTA     :").setTextAlignment(TextAlignment.LEFT).setFontSize(7f)).setBorder(Border.NO_BORDER));
        datesTable.addCell(new Cell().add(new Paragraph(max_fecha).setTextAlignment(TextAlignment.LEFT).setFontSize(7f)).setBorder(Border.NO_BORDER));

        float[] codigosColumns = {20, 200};
        Table codigosTable = new Table(codigosColumns);

        codigosTable.addCell(new Cell().add(new Paragraph("CODIGO    :").setFontSize(7f)).setBorder(Border.NO_BORDER));
        codigosTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(0).getCodcli() + " - " + cta_ingresosArrayList.get(0).getNomcli()).setTextAlignment(TextAlignment.LEFT).setFontSize(7f)).setBorder(Border.NO_BORDER));

        float[] documentsColumns = {1000};
        Table documentsTable = new Table(documentsColumns);

        documentsTable.addCell(new Cell().add(new Paragraph("Documentos:").setFontSize(7f)).setBorder(Border.NO_BORDER));
        documentsTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        float[] dataColumns = {40, 70, 85, 100, 100, 100, 80, 50, 75, 104, 50, 50, 131, 50, 50};
        Table dataTable = new Table(dataColumns);

        dataTable.addCell(new Cell().add(new Paragraph("TD").setFontSize(7f)).setTextAlignment(TextAlignment.LEFT).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("N° DOC").setFontSize(7f)).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("FEC. DOC.").setFontSize(7f)).setTextAlignment(TextAlignment.LEFT).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("FEC. ENT.").setFontSize(7f)).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("ENT. CLI").setFontSize(7f)).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("FEC. VEN.").setFontSize(7f)).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("FEC. CAN.").setFontSize(7f)).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("TM").setFontSize(7f)).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("TOTAL US$").setFontSize(7f)).setTextAlignment(TextAlignment.RIGHT).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("TOTAL S/").setFontSize(7f)).setTextAlignment(TextAlignment.RIGHT).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("").setFontSize(7f)).setTextAlignment(TextAlignment.RIGHT).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("DEUDA").setFontSize(7f)).setTextAlignment(TextAlignment.RIGHT).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("OBLIGACIÓN").setFontSize(7f)).setTextAlignment(TextAlignment.RIGHT).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("EST").setFontSize(7f)).setTextAlignment(TextAlignment.RIGHT).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        dataTable.addCell(new Cell().add(new Paragraph("DÍAS").setFontSize(7f)).setTextAlignment(TextAlignment.RIGHT).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));

        double totalDolar= 0;
        double deudaDolar = 0;
        double obligacionDolar = 0;
        double totalSoles = 0;
        double deudaSoles = 0;
        double obligacionSoles = 0;
        String moneda ="";
        for (int i = 0; i < cta_ingresosArrayList.size(); i++) {

            if (cta_ingresosArrayList.get(i).getCodigo_equivalente().equals("USD")) {
                moneda = "ME";
                totalDolar = totalDolar + Double.parseDouble(cta_ingresosArrayList.get(i).getTotal());
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getSerie_doc()).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getNumero_factura()).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getFeccom()).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getFecha_despacho()).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getFecha_vencimiento()).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(moneda).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(Double.parseDouble(cta_ingresosArrayList.get(i).getTotal()))).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                String deudaC = Comprobaciones.getComprobacionDeuda(cta_ingresosArrayList.get(i).getDeuda());
                dataTable.addCell(new Cell().add(new Paragraph(deudaC).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                deudaDolar = deudaDolar + Double.parseDouble(deudaC);
                dataTable.addCell(new Cell().add(new Paragraph(Comprobaciones.getComprobacionObligacion(cta_ingresosArrayList.get(i).getObligacion())).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                obligacionDolar = obligacionDolar + Double.parseDouble(Comprobaciones.getComprobacionObligacion(cta_ingresosArrayList.get(i).getObligacion()));
                dataTable.addCell(new Cell().add(new Paragraph("P").setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                String fechaInicial = cta_ingresosArrayList.get(i).getFecha_despacho();
                String fechaFinal = cta_ingresosArrayList.get(i).getFecha_vencimiento();
                dataTable.addCell(new Cell().add(new Paragraph(getDate.getComprobacionFecha(fechaInicial, fechaFinal)).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

            } else if (cta_ingresosArrayList.get(i).getCodigo_equivalente().equals("PEN")) {
                moneda = "MN";
                totalSoles = totalSoles + Double.parseDouble(cta_ingresosArrayList.get(i).getTotal());
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getSerie_doc()).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getNumero_factura()).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getFeccom()).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getFecha_despacho()).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getFecha_vencimiento()).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(moneda).setFontSize(7f)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(Double.parseDouble(cta_ingresosArrayList.get(i).getTotal()))).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                String deudaC = Comprobaciones.getComprobacionDeuda(cta_ingresosArrayList.get(i).getDeuda());
                dataTable.addCell(new Cell().add(new Paragraph(deudaC).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                deudaSoles = deudaSoles + Double.parseDouble(deudaC);
                dataTable.addCell(new Cell().add(new Paragraph(Comprobaciones.getComprobacionObligacion(cta_ingresosArrayList.get(i).getObligacion())).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                obligacionSoles = obligacionSoles + Double.parseDouble(Comprobaciones.getComprobacionObligacion(cta_ingresosArrayList.get(i).getObligacion()));
                dataTable.addCell(new Cell().add(new Paragraph("P").setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                String fechaInicial = cta_ingresosArrayList.get(i).getFecha_despacho();
                String fechaFinal = cta_ingresosArrayList.get(i).getFecha_vencimiento();
                dataTable.addCell(new Cell().add(new Paragraph(getDate.getComprobacionFecha(fechaInicial, fechaFinal)).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
            } else {
                moneda = "OTRO";
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getSerie_doc())).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getNumero_factura())).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getFeccom())).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getFecha_despacho())).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getFecha_vencimiento())).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(moneda)).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph(cta_ingresosArrayList.get(i).getTotal())).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph("P")).setBorder(Border.NO_BORDER));
                dataTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
            }

        }

        float[] totalFinal = {675, 130, 100, 240, 190};
        Table tableTotal = new Table(totalFinal);

        tableTotal.addCell(new Cell().add(new Paragraph("TOTALES FINALES").setFontSize(7f)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
        tableTotal.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(totalDolar)).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
        tableTotal.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(totalSoles)).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
        tableTotal.addCell(new Cell().add(new Paragraph().setFontSize(7f)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
        tableTotal.addCell(new Cell().add(new Paragraph().setFontSize(7f)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));


        float[] finalColumn = {505, 80, 30, 80, 100, 107, 255};
        Table finalTable = new Table(finalColumn);

        finalTable.addCell(new Cell().add(new Paragraph("").setFontSize(7f)).setBorder(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("Deuda").setFontSize(7f)).setBorder(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("US$").setFontSize(7f)).setBorder(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(deudaDolar)).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)).setBorder(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("S/").setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(deudaSoles)).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)).setBorder(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("").setFontSize(7f)).setBorder(Border.NO_BORDER));

        finalTable.addCell(new Cell().add(new Paragraph("").setFontSize(7f)).setBorder(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("Obligación").setFontSize(7f)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("US$").setFontSize(7f)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(obligacionDolar)).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("S/").setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(obligacionSoles)).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("").setFontSize(7f)).setBorder(Border.NO_BORDER));

        double lessDolar = obligacionDolar + deudaDolar;
        double lessSoles = obligacionSoles + deudaSoles;

        finalTable.addCell(new Cell().add(new Paragraph("").setFontSize(7f)).setBorder(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("TOTAL").setFontSize(7f)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("US$").setFontSize(7f)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(lessDolar)).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("S/").setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(lessSoles)).setTextAlignment(TextAlignment.RIGHT).setFontSize(7f)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
        finalTable.addCell(new Cell().add(new Paragraph("").setFontSize(7f)).setBorder(Border.NO_BORDER));

        document.add(datesTable);
        document.add(codigosTable);
        document.add(documentsTable);
        document.add(dataTable);
        document.add(tableTotal);
        document.add(finalTable);
        document.close();

    }
}
