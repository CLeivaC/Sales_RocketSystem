package com.rocketsystem.coreapi.rocketsytem_sales_api.controllers;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.rocketsystem.coreapi.rocketsytem_sales_api.services.reports.ReportService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rocketsystem/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/sales")
    public void generateSalesReport(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(value = "pointSaleId", required = false) Integer pointSaleId,
            jakarta.servlet.http.HttpServletResponse response) {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=sales_report.pdf");
        reportService.exportSalesReportToPdf(startDate, endDate, pointSaleId, response);
    }
}
