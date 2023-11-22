package com.example.sm_tubo_plast.genesys.CreatePDF;



import android.annotation.SuppressLint;
import android.os.Environment;

import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedido;
import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedidoDetalle;
import com.example.sm_tubo_plast.genesys.CreatePDF.Decimales.GetDecimales;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.SeguimientoPedidoActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PDFSeguimientoOp
{

    GetDecimales getDecimales = new GetDecimales();

    public void createPDFCabeceraDetalle(String nombreArchivo, ViewSeguimientoPedido dataCabe,  ArrayList<ViewSeguimientoPedidoDetalle> vspd) throws FileNotFoundException
    {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateAndTime = simpleDateFormat.format(new Date());

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, ""+nombreArchivo);
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument);
        document.setMargins(10, 10, 10, 10);

        float[] firstLine = {60, 250, 300, 300, 70, 250};
        Table firstLineTable = new Table(firstLine);

        firstLineTable.addCell(new Cell().add(new Paragraph("RUC :").setFontSize(8f).setTextAlignment(TextAlignment.LEFT).setBold()).setBorder(Border.NO_BORDER));
        firstLineTable.addCell(new Cell().add(new Paragraph(dataCabe.getCodcli()).setFontSize(8f).setTextAlignment(TextAlignment.LEFT).setBold()).setBorder(Border.NO_BORDER));
        firstLineTable.addCell(new Cell().add(new Paragraph().setFontSize(8f).setTextAlignment(TextAlignment.LEFT).setBold()).setBorder(Border.NO_BORDER));
        firstLineTable.addCell(new Cell().add(new Paragraph().setFontSize(8f).setTextAlignment(TextAlignment.LEFT).setBold()).setBorder(Border.NO_BORDER));
        firstLineTable.addCell(new Cell().add(new Paragraph("Fecha :").setFontSize(8f).setTextAlignment(TextAlignment.LEFT).setBold()).setBorder(Border.NO_BORDER));
        firstLineTable.addCell(new Cell().add(new Paragraph(currentDateAndTime).setFontSize(8f).setTextAlignment(TextAlignment.LEFT).setBold()).setBorder(Border.NO_BORDER));

        /**
         * INICIO DE LA CABECERA
         */

        float[] secondLine = {1000};
        Table secondLineTable = new Table(secondLine);

        String nombre = null;

        secondLineTable.addCell(new Cell().add(new Paragraph(vspd.get(0).getNombres()).setFontSize(8f).setTextAlignment(TextAlignment.CENTER).setBold()).setBorder(Border.NO_BORDER));
        secondLineTable.addCell(new Cell().add(new Paragraph(dataCabe.getCoddoc() + ""
                + dataCabe.getNum_op() + "   O/C "
                + dataCabe.getOrden_compra() + "   Recepcion "
                + dataCabe.getFecha_rect() + "   Autorizacion "
                + dataCabe.getFecha_autorizado()
        ).setFontSize(8f).setTextAlignment(TextAlignment.CENTER).setBold()).setBorder(Border.NO_BORDER));
        secondLineTable.addCell(new Cell().add(new Paragraph("TOTAL (" + dataCabe.getMoneda() + ") " + dataCabe.getTotal() +
                "    Despachado (" + dataCabe.getMoneda() + ") " + dataCabe.getMonto_total_entregado() + calculate1(String.valueOf(dataCabe.getMonto_total_entregado()), String.valueOf(dataCabe.getTotal())) +
                "    Saldo (" + dataCabe.getMoneda() + ") " + dataCabe.getSaldo() + calculate2(String.valueOf(dataCabe.getSaldo()), String.valueOf(dataCabe.getTotal()))
        ).setFontSize(8f).setTextAlignment(TextAlignment.CENTER).setBold()).setBorder(Border.NO_BORDER));
        secondLineTable.addCell(new Cell().add(new Paragraph("GROP (" + dataCabe.getMoneda() + ") "
                + dataCabe.getMonto_grop() + " "
                + calculate3(String.valueOf(dataCabe.getMonto_grop()), String.valueOf(dataCabe.getTotal()))
                + "  /  Guías" + dataCabe.getCant_guias() + "  /  Dias " + dataCabe.getDias()
        ).setFontSize(8f).setTextAlignment(TextAlignment.CENTER).setBold()).setBorder(Border.NO_BORDER));
        secondLineTable.addCell(new Cell().add(new Paragraph(
                "PRIMERA ENTREGA " + dataCabe.getPrimer_entrega()
                        + "  -  " +
                        "ULTIMA ENTREGA" + dataCabe.getUltima_entrega())
                .setFontSize(8f).setTextAlignment(TextAlignment.CENTER).setBold()).setBorder(Border.NO_BORDER));

        /**
         * FIN DE LA CABECERA
         */

        /**
         * INICIO DE DETALLE
         */

        float[] cabecera_detalle = {30, 70, 70, 70, 50, 440, 70};
        Table cabecera_detalleTable = new Table(cabecera_detalle);

        cabecera_detalleTable.addCell(new Cell().add(new Paragraph("Item").setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
        cabecera_detalleTable.addCell(new Cell().add(new Paragraph("Cantidad").setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
        cabecera_detalleTable.addCell(new Cell().add(new Paragraph("Entregado").setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
        cabecera_detalleTable.addCell(new Cell().add(new Paragraph("Saldo").setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
        cabecera_detalleTable.addCell(new Cell().add(new Paragraph("")).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
        cabecera_detalleTable.addCell(new Cell().add(new Paragraph("Articulo").setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
        cabecera_detalleTable.addCell(new Cell().add(new Paragraph("Monto").setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));

        Table opTable = doOpTable(vspd);
        Table grTable = doGRPTable(vspd);
        Table grTable2 = doGR02Table(vspd);

        /**
         * FIN DE DETALLE
         */

        document.add(firstLineTable);
        document.add(secondLineTable);
        document.add(cabecera_detalleTable);
        document.add(opTable);
        if(grTable != null) document.add(grTable);
        if(grTable2 != null) document.add(grTable2);
        document.close();

    }

    public String calculate1(String monto_total_entregado, String total)
    {
        double numberBack;
        numberBack = (Double.parseDouble(monto_total_entregado) * 100) / Double.parseDouble(total);
        return "[%" + getDecimales.obtieneDosDecimales(numberBack) + "]";
    }

    public String calculate2(String saldo, String total)
    {
        double numberBack;
        numberBack = (Double.parseDouble(saldo) * 100) / Double.parseDouble(total);
        return "[%" + getDecimales.obtieneDosDecimales(numberBack) + "]";
    }

    private String calculate3(String monto_grop, String total)
    {
        double numberBack;
        numberBack = (Double.parseDouble(monto_grop) * 100) / Double.parseDouble(total);
        return "[%" + getDecimales.obtieneDosDecimales(numberBack) + "]";
    }

    private Table doOpTable(ArrayList<ViewSeguimientoPedidoDetalle> vspd)
    {
        float[] op = {30, 70, 70, 70, 50, 440, 70};
        Table opTable = new Table(op);

        opTable.addCell(new Cell().add(new Paragraph(vspd.get(0).getCodigo_op()).setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        opTable.addCell(new Cell().add(new Paragraph(vspd.get(0).getNumero_op()).setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        opTable.addCell(new Cell().add(new Paragraph("OPED").setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        opTable.addCell(new Cell().add(new Paragraph().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        opTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        opTable.addCell(new Cell().add(new Paragraph(vspd.get(0).getFecha_apertura() + " " + vspd.get(0).getFecha_apertura()).setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        opTable.addCell(new Cell().add(new Paragraph().setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));

        double saldo;
        double total;

        for (int i = 0; i < vspd.size(); i++)
        {
            if (vspd.get(i).getMovimiento().equals(SeguimientoPedidoActivity.SEGUIMIENTO_PEDIDO_GENERADO))
            {
                saldo = vspd.get(i).getCantidad_comprado() - vspd.get(i).getCantidad_entregado();
                total = vspd.get(i).getMonto_total() - vspd.get(i).getMonto_saldo();

                opTable.addCell(new Cell().add(new Paragraph(String.valueOf(vspd.get(i).getItem())).setFontSize(8f).setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
                opTable.addCell(new Cell().add(new Paragraph(String.valueOf(vspd.get(i).getCantidad_comprado())).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                opTable.addCell(new Cell().add(new Paragraph(String.valueOf(vspd.get(i).getCantidad_entregado())).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                opTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(saldo)).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                opTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                opTable.addCell(new Cell().add(new Paragraph(vspd.get(i).getProducto()).setFontSize(8f).setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
                opTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(total)).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
            }
        }
        return opTable;
    }

    private Table doGRPTable(ArrayList<ViewSeguimientoPedidoDetalle> vspd)
    {

        float[] gr = {30, 70, 70, 70, 50, 440, 70};
        Table grTable = new Table(gr);

        double saldo;
        double total;

        String title = null;
        String number = null;
        String fecha = null;

        for (int i = 0; i < vspd.size(); i++)
        {
            if (vspd.get(i).getMovimiento().equals(SeguimientoPedidoActivity.SEGUIMIENTO_PEDIDO_DEVOLUCION))
            {
                if (!(vspd.get(i).getCodigo_op().trim().equals(title) && vspd.get(i).getNumero_op().equals(number)))
                {
                    title = vspd.get(i).getCodigo_op();
                    number = vspd.get(i).getNumero_op();
                    fecha = vspd.get(i).getFecha_apertura();

                    grTable.addCell(new Cell().add(new Paragraph(title).setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph(number).setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph("OPED").setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph().setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph(fecha + " " + fecha).setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph("").setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBold().setBorder(Border.NO_BORDER));

                }

                saldo = vspd.get(i).getCantidad_comprado() - vspd.get(i).getCantidad_entregado();
                total = vspd.get(i).getMonto_total() - vspd.get(i).getMonto_saldo();

                grTable.addCell(new Cell().add(new Paragraph(String.valueOf(vspd.get(i).getItem())).setFontSize(8f).setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph(String.valueOf(vspd.get(i).getCantidad_comprado())).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph(String.valueOf(vspd.get(i).getCantidad_entregado())).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(saldo)).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph(vspd.get(i).getProducto()).setFontSize(8f).setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(total)).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
            }
        }
        return grTable;
    }

    private Table doGR02Table(ArrayList<ViewSeguimientoPedidoDetalle> vspd)
    {
        float[] gr = {30, 70, 70, 70, 50, 440, 70};
        Table grTable = new Table(gr);

        double saldo;
        double total;

        String w_fecha = "";
        String w_coddoc = "";
        String w_numdoc = "";
        for (int i = 0; i < vspd.size(); i++)
        {
            if (vspd.get(i).getMovimiento().equals(SeguimientoPedidoActivity.SEGUIMIENTO_PEDIDO_ENTREGADO)){
                if(!(w_coddoc.equals(vspd.get(i).getCodigo_op()) && w_numdoc.equals(vspd.get(i).getNumero_op()))){
                    w_coddoc=vspd.get(i).getCodigo_op();
                    w_numdoc=vspd.get(i).getNumero_op();
                    w_fecha = vspd.get(i).getFecha_apertura();

                    grTable.addCell(new Cell().add(new Paragraph(w_coddoc).setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph(w_numdoc).setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph("OPED").setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph().setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph(w_fecha + " " + w_fecha).setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    grTable.addCell(new Cell().add(new Paragraph("").setFontSize(8f).setTextAlignment(TextAlignment.CENTER)).setBold().setBorder(Border.NO_BORDER));
                }
                saldo = vspd.get(i).getCantidad_comprado() - vspd.get(i).getCantidad_entregado();
                total = vspd.get(i).getMonto_total() - vspd.get(i).getMonto_saldo();
                grTable.addCell(new Cell().add(new Paragraph(String.valueOf(vspd.get(i).getItem())).setFontSize(8f).setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph(String.valueOf(vspd.get(i).getCantidad_comprado())).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph(String.valueOf(vspd.get(i).getCantidad_entregado())).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(saldo)).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph(vspd.get(i).getProducto()).setFontSize(8f).setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
                grTable.addCell(new Cell().add(new Paragraph(getDecimales.obtieneDosDecimales(total)).setFontSize(8f).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
            }
        }
        return grTable;
    }

}
