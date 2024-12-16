package com.rocketsystem.coreapi.rocketsytem_sales_api.services.reports;

import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;

public interface ReportService {

    void exportSalesReportToPdf(Date startDate, Date endDate, Integer pointSaleId,HttpServletResponse response);
}
