package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.WpsReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.WpsReportItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 3/28/16
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class SifFileGenerator implements HttpRequestHandler {

    private static final int BUFFER_SIZE = 1024 * 2;

    @Autowired
    PayrollService payrollService;

    @Autowired
    UserManager userManager;
    @Autowired
    FinancialSettingsManager financialSettingsManager;

    private SimpleDateFormat shortDateFormat;
    private Integer calculationScale;
    private WpsReportData data;
    private BigDecimal edrTotal;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ListingFilterParameter lfp = getDataClass(request);
        lfp.setFromExcelPDF(true);
        lfp.setFromSifFile(true);
        data = payrollService.getWpsReportData(lfp);

        shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();

        setCalculationScale(fs);
        StringBuffer stringBuffer = new StringBuffer();
        employeeDetatilRecord(stringBuffer);
        salaryControlRecord(stringBuffer);
        InputStream result = new ByteArrayInputStream(stringBuffer.toString().getBytes());
        response.setHeader("content-disposition", "attachment; filename=" + getFileName());
        doSendContent(result, response);
    }

    private void employeeDetatilRecord(StringBuffer stringBuffer) {
        edrTotal = BigDecimal.ZERO;
        for (WpsReportItem item : data.getWpsReportItems()) {
            stringBuffer.append("EDR").append(",");
            stringBuffer.append(item.getWpsNumber()).append(",");
            stringBuffer.append(item.getBankCode()).append(",");
            stringBuffer.append(getFormattedText(item.getIbanNumber(), "0", 23, false)).append(",");
            stringBuffer.append(shortDateFormat.format(item.getFromDate())).append(",");
            stringBuffer.append(shortDateFormat.format(item.getToDate())).append(",");
            stringBuffer.append(item.getWorkedDays() - item.getLeaveDays()).append(",");
            stringBuffer.append(item.getRecurringPayments().setScale(calculationScale, BigDecimal.ROUND_HALF_UP)).append(",");
            stringBuffer.append(item.getTotal().subtract(item.getRecurringPayments()).setScale(calculationScale, BigDecimal.ROUND_HALF_UP)).append(",");
            stringBuffer.append(item.getLeaveDays()).append("\r\n");
            edrTotal = edrTotal.add(item.getTotal());
        }
    }

    private void salaryControlRecord(StringBuffer stringBuffer) {
        Date currentDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
        EdsUser user = userManager.getUser();
        timeFormat.setTimeZone(TimeZone.getTimeZone(user.getTimezone()));
        stringBuffer.append("SCR").append(",");
        stringBuffer.append(data.getCompanyWpsNumber()).append(",");
        stringBuffer.append(data.getCompanyBankCode()).append(",");
        shortDateFormat.setTimeZone(TimeZone.getTimeZone(user.getTimezone()));
        stringBuffer.append(shortDateFormat.format(currentDate)).append(",");
        stringBuffer.append(timeFormat.format(currentDate)).append(",");
        stringBuffer.append(String.format("%02d%04d,", data.getMonthId(), data.getYear()));
        stringBuffer.append(data.getTotalCount()).append(",");
        stringBuffer.append(edrTotal.setScale(calculationScale, BigDecimal.ROUND_HALF_UP)).append(",");
        stringBuffer.append("AED,");
    }

    public void setCalculationScale(EdsFinancialSettings fs) {
        if (fs != null && fs.getCalculationScale() != null && fs.getCalculationScale() > 0) {
            calculationScale = fs.getCalculationScale();
        } else {
            calculationScale = 2;
        }
    }

    private String getFormattedText(String text, String padding, Integer length, boolean rightJustify) {
        text = text.substring(0, length < text.length() ? length : text.length());
        while (text.length() < length) {
            text = rightJustify ? padding + text : text + padding;
        }
        return text;
    }

    private String getFileName() {
        StringBuilder fileName = new StringBuilder();
        fileName.append(data.getCompanyWpsNumber());
        Date curDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        EdsUser user = userManager.getUser();
        timeFormat.setTimeZone(TimeZone.getTimeZone(user.getTimezone()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone(user.getTimezone()));
        fileName.append(dateFormat.format(curDate));
        fileName.append(timeFormat.format(curDate));
        fileName.append(".sif");

        return fileName.toString();
    }

    private void doSendContent(InputStream inputStream, HttpServletResponse response) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        OutputStream ois = response.getOutputStream();
        int count = 0;
        while ((count = inputStream.read(buffer)) > 0) {
            ois.write(buffer, 0, count);
        }
    }

    private boolean isValid(String... fields) {
        for (String s : fields) {
            if (s == null || s.trim().isEmpty())
                return false;
        }
        return true;
    }

    protected ListingFilterParameter getDataClass(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        ListingFilterParameter fp = new ListingFilterParameter();
        HashMap<String, String> paramsMap = fp.getRequestParams();
        Iterator<Map> entries = filterMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put((String) entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        fp.setFacetFilter(WfmJsonUtils.jsonConvertToFacetFilterRpc(fp.getFacetFilterJson()));
        return fp;
    }

}
