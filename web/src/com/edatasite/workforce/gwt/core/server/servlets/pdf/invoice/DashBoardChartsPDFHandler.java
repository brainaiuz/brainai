package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.DashboardChartsRequestObject;
import com.edatasite.workforce.gwt.core.server.filters.LocaleRequestWrapper;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

public class DashBoardChartsPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        DashboardChartsRequestObject requestObject = (DashboardChartsRequestObject) dataClass;
        List<NumericWidget> charts = new ArrayList<>();
        for (Map.Entry<String, String[]> chart : requestObject.getCharts().entrySet()) {
            if (!chart.getKey().equals("objectID") && !chart.getKey().equals("userID") && !chart.getKey().equals("IS_LANDSCAPE") && !chart.getKey().equals("dashboardName") && !chart.getKey().equals("ids")) {
                String[] keys = chart.getKey().split("@");
                if (keys.length == 5 && !chart.getValue()[0].contains("£")) {
                    NumericWidget widget = new NumericWidget();
                    widget.setTitle(keys[0]);
                    widget.setValue(chart.getValue()[0]);
                    widget.setX(Integer.parseInt(keys[1]));
                    widget.setY(Integer.parseInt(keys[2]));
                    widget.setWidth(Integer.parseInt(keys[3]));
                    widget.setHeight(Integer.parseInt(keys[4]));
                    widget.setType("CHART");
                    charts.add(widget);
                } else if (keys.length == 6) {
                    NumericWidget numericWidget = new NumericWidget();
                    numericWidget.setTitle(keys[0]);
                    String[] vals = chart.getValue()[0].split(Pattern.quote("."));
                    String whole = numberWithCommas(vals[0]);
                    String decimal = vals.length == 2 ? vals[1] : "";
                    String value = whole + "." + decimal;
                    numericWidget.setValue(value);
                    numericWidget.setColor(keys[1]);
                    numericWidget.setX(Integer.parseInt(keys[2]));
                    numericWidget.setY(Integer.parseInt(keys[3]));
                    numericWidget.setWidth(Integer.parseInt(keys[4]));
                    numericWidget.setHeight(Integer.parseInt(keys[5]));
                    numericWidget.setType("NUMERIC");
                    charts.add(numericWidget);
                } else {
                    NumericWidget tableWidget = new NumericWidget();
                    tableWidget.setTitle(keys[0]);
                    Map<String, String> tableItems = new LinkedHashMap<>();
                    String[] rows = chart.getValue()[0].split("£");
                    for (String row : rows) {
                        String[] values = row.split("~");
                        if (values.length == 2) {
                            tableItems.put(values[0], values[1]);
                        }
                    }
                    tableWidget.setTableItems(tableItems);
                    tableWidget.setX(Integer.parseInt(keys[1]));
                    tableWidget.setY(Integer.parseInt(keys[2]));
                    tableWidget.setWidth(Integer.parseInt(keys[3]));
                    tableWidget.setHeight(Integer.parseInt(keys[4]));
                    tableWidget.setType("TABLE");
                    charts.add(tableWidget);
                }
            }
        }
        pdfData.setCharts(charts);
        pdfData.setLandscape(requestObject.getIS_LANDSCAPE());
        return pdfData;
    }


    @Override
    protected Object getDataClass(HttpServletRequest request) {
        HashMap<String, String[]> charts = (HashMap<String, String[]>) ((LocaleRequestWrapper) request).getRequest().getParameterMap();
        DashboardChartsRequestObject requestObject = new DashboardChartsRequestObject(charts);
        return requestObject;
    }

    private String getFileName(Object dataClass) {
        LocalDate now = LocalDate.now();
        return "Dashboard-" + getTableName(dataClass) + "-" + now;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(getFileName(dataClass));
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.DASHBOARD_CHARTS;
    }

    @Override
    protected String getTableName(Object dataClass) {
        Map<String, String[]> requests = ((DashboardChartsRequestObject) dataClass).getCharts();
        String dashboardName = "";
        for (Map.Entry<String, String[]> request : requests.entrySet()) {
            if (request.getKey().equals("dashboardName")) {
                dashboardName = request.getValue()[0];
            }
        }
        return dashboardName;
    }

    private String numberWithCommas(String n) {
        int ni = Integer.parseInt(n);
        String str = String.format("%,d", ni);
        return str;
    }

}
