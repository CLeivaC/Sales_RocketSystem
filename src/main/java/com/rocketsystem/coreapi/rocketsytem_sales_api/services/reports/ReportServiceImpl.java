package com.rocketsystem.coreapi.rocketsytem_sales_api.services.reports;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.SaleRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReportServiceImpl implements ReportService {


    @Autowired
    private SaleRepository saleRepository;


@Transactional(readOnly = true)
@Override
public void exportSalesReportToPdf(Date startDate, Date endDate, Integer pointSaleId, HttpServletResponse response) {
    try {
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Título del documento con fuente grande y negrita
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Reporte de Ventas", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Espacio debajo del título
        document.add(new Paragraph(" "));

        // Rango de fechas
        Font dateFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Paragraph dateRange = new Paragraph("Desde: " + startDate + " Hasta: " + endDate, dateFont);
        dateRange.setAlignment(Element.ALIGN_CENTER);
        document.add(dateRange);

        // Espacio debajo del rango de fechas
        document.add(new Paragraph(" "));

        // Crear tabla
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setSpacingAfter(15);

        // Colores y estilos para las celdas del encabezado
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        BaseColor headerBackgroundColor = new BaseColor(63, 81, 181); // Azul
        

        PdfPCell cell = new PdfPCell(new Paragraph("ID Venta", headerFont));
        cell.setBackgroundColor(headerBackgroundColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        cell.setBorderWidth(1);
        cell.setBorderColor(BaseColor.GRAY);
        cell.setPhrase(new Paragraph("ID Venta", headerFont));
        cell.setBackgroundColor(headerBackgroundColor);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Fecha de Venta", headerFont));
        cell.setBackgroundColor(headerBackgroundColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        cell.setBorderWidth(1);
        cell.setBorderColor(BaseColor.GRAY);
        cell.setPhrase(new Paragraph("Fecha de Venta", headerFont));
        cell.setBackgroundColor(headerBackgroundColor);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Total", headerFont));
        cell.setBackgroundColor(headerBackgroundColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        cell.setBorderWidth(1);
        cell.setBorderColor(BaseColor.GRAY);
        cell.setPhrase(new Paragraph("Total", headerFont));
        cell.setBackgroundColor(headerBackgroundColor);
        cell.setPadding(5);
        table.addCell(cell);

        // Filas de ventas con color alternado
        Font rowFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        boolean alternateColor = true;
        BaseColor rowColor1 = BaseColor.WHITE;
        BaseColor rowColor2 = new BaseColor(224, 224, 224); // Gris claro

        saleRepository.findSalesInPeriod(startDate, endDate, pointSaleId).forEach(sale -> {
            BaseColor rowBackgroundColor = alternateColor ? rowColor1 : rowColor2;
           

            PdfPCell saleIdCell = new PdfPCell(new Paragraph(String.valueOf(sale.getSaleId()), rowFont));
            saleIdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            saleIdCell.setBackgroundColor(rowBackgroundColor);
            saleIdCell.setPadding(8);
            table.addCell(saleIdCell);

            PdfPCell saleDateCell = new PdfPCell(new Paragraph(sale.getSaleDate().toString(), rowFont));
            saleDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            saleDateCell.setBackgroundColor(rowBackgroundColor);
            saleDateCell.setPadding(8);
            table.addCell(saleDateCell);

            PdfPCell totalCell = new PdfPCell(new Paragraph(String.valueOf(sale.getTotal()), rowFont));
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setBackgroundColor(rowBackgroundColor);
            totalCell.setPadding(8);
            table.addCell(totalCell);
        });

        document.add(table);

        // Total de ventas con estilo especial
        Font totalFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph totalParagraph = new Paragraph("Total de Ventas: " + saleRepository.findTotalSalesInPeriod(startDate, endDate, pointSaleId), totalFont);
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        totalParagraph.setSpacingBefore(20);
        document.add(totalParagraph);

        document.close();
    } catch (DocumentException | IOException e) {
        e.printStackTrace();
    }
}


}
