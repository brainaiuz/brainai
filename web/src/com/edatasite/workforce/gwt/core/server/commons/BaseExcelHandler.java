/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/26 7:31:0                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class BaseExcelHandler implements HttpRequestHandler {

    private PropertyEditorRegistrar[] propertyEditorRegistrars;
    protected String filename;
    protected static final int LIMIT_EXCEL_ROW = 1000; //decreased the download limit as it slowed down the servers, if user wants more rows per export, he can use reporting tool

    private static final SimpleDateFormat fpDateParseFormat = new SimpleDateFormat("ddMMyyyy HH:mm:ss");

    protected ByteArrayOutputStream baos;
    @Autowired
    protected UserManager userManager;

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;

    @Autowired
    @Qualifier("crmLocalizer")
    protected WfmMessageSource crmLocalizer;

    @Autowired
    @Qualifier("hrmsLocalizer")
    protected WfmMessageSource hrmsLocalizer;

    @Autowired
    @Qualifier("availabilityLocalizer")
    protected WfmMessageSource availabilityLocalizer;

    @Autowired
    @Qualifier("pmLocalizer")
    protected WfmMessageSource pmLocalizer;

    @Autowired
    protected FinancialSettingsManager financialSettingsManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object dataClass = getDataClass(request);
        if (dataClass instanceof ListingFilterParameter) {
            ListingFilterParameter fp = (ListingFilterParameter) dataClass;
            fp.setFacetFilter(WfmJsonUtils.jsonConvertToFacetFilterRpc(fp.getFacetFilterJson()));
            fp.setListPanelTool(WfmJsonUtils.jsonConvertToListPanelToolRpc(fp.getListPanelToolJson()));
        }

        if (dataClass != null && prepareRequest(request)) {
            tryToBind(request, dataClass);
        }

        writeHeader(response, dataClass);
        prepareOutputStream(dataClass);
        returnResponse(response);
    }

    protected abstract void setFileName();

    protected void writeHeader(HttpServletResponse response, Object dataClass) {
        setFileName();
        setFileName(dataClass);
        if (filename == null) {
            filename = "file";
        }
        if (filename.contains(" ")) {
            filename = filename.replace(" ", "_");
        }
        if (filename.contains("/")) {
            filename = filename.replace("\\/", "_");
        }
        filename = ServerUtils.normalizeFileNameT(filename);
        response.setHeader("content-disposition", "attachment; filename=\"" + URLEncoder.encode(filename, StandardCharsets.UTF_8) + ".xls\"");
        response.setContentType(CONTENT_TYPE_EXCEL);
        response.setCharacterEncoding("UTF8");
    }

    public ExcelData getExcelDataHeader(ExcelData excel) {
        excel.setStyle(true);
        excel.setFontColor(HSSFColor.WHITE.index);
        excel.setBold(true);
        excel.setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
        excel.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        excel.setBgcolor(HSSFColor.GREY_25_PERCENT.index);
        excel.setFontSize(9);
        return excel;
    }

    public ExcelData getExcelRows(ExcelData excel) {
        excel.setStyle(true);
        excel.setHorizontalAlignment(HSSFCellStyle.ALIGN_LEFT);
        excel.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        excel.setFontSize(9);
        return excel;
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        return null;
    }

    protected void setFileName(Object object) {
    }

    protected String dateFormat(Date date, boolean... isServerTime) {
        return ServerUtils.shortDateFormat(date, userManager.getUser(), isServerTime == null || isServerTime.length <= 0 || !isServerTime[0]);
    }

    protected String longDateFormat(Date date, boolean... isServerTime) {
        return ServerUtils.longDateFormat(date, userManager.getUser(), isServerTime == null || isServerTime.length <= 0 || !isServerTime[0]);
    }

    public static final String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel";

    public static String getMoneyFormat(BigDecimal bigDecimal, Integer scale) {
        return Utils.formatDecimal(bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP));
    }

    public static String getMoneyFormat(BigDecimal bigDecimal) {
        return getMoneyFormat(bigDecimal, 2);
    }

    public static String getExtendedMoneyFormat(BigDecimal bigDecimal) {
        return Utils.formatExtendedDecimal(bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP));
    }

    public static String getMoneyFormat(double doubleValue) {
        return Utils.formatDouble(doubleValue);
    }

    //New changes

    /**
     * Generates and returns Excel as stream.
     *
     * @param object information that you need to generate excel.
     * @return outputStream excel as output stream
     */
    public ByteArrayOutputStream getExcelStream(Object object) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            prepareOutputStream(object);
            outputStream.write(baos.toByteArray());
            closeStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return outputStream;
    }

    /**
     * You should define object for autobinding.
     * All request parameters will be set to this object.
     *
     * @param request
     * @return your object
     */
    protected Object getDataClass(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        ListingFilterParameter fp = new ListingFilterParameter();
        HashMap<String, String> paramsMap = fp.getRequestParams();
        if (!paramsMap.containsKey("objectID")) {
            paramsMap.put("objectID", null);
            fp.setRequestParams(paramsMap);
        }
        Iterator<Map> iterator = filterMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put((String) entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        fp.setFacetFilter(WfmJsonUtils.jsonConvertToFacetFilterRpc(fp.getFacetFilterJson()));
        return fp;
    }

    private void prepareOutputStream(Object object) {

        baos = new ByteArrayOutputStream();

        try {
            HSSFWorkbook wb = getWorkBook(object);
            if (wb != null) {
                wb.write(baos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void returnResponse(HttpServletResponse response) {
        try {
            byte[] data = baos.toByteArray();
            response.setContentLength(data.length);
            response.getOutputStream().write(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * You can rewrite this method
     * if you want to parse request to take some parametrs.
     *
     * @param request
     * @return return true if you want PostPDFHandler to parse and bind your request.
     */
    protected boolean prepareRequest(HttpServletRequest request) {

        return true;
    }

    private final ServletRequestDataBinder tryToBind(HttpServletRequest request, Object command) {
        ServletRequestDataBinder binder = createBinder(request, command);
        binder.bind(request);
        return binder;
    }

    private ServletRequestDataBinder createBinder(HttpServletRequest request,
                                                  Object command) {
        ServletRequestDataBinder binder = new ServletRequestDataBinder(command);
        prepareBinder(binder);

        return binder;
    }

    private final void prepareBinder(ServletRequestDataBinder binder) {
        if (this.propertyEditorRegistrars != null) {
            for (int i = 0; i < this.propertyEditorRegistrars.length; i++) {
                this.propertyEditorRegistrars[i].registerCustomEditors(binder);
            }
        }
    }

    /**
     * Specify a single PropertyEditorRegistrar to be applied
     * to every DataBinder that this controller uses.
     * <p>Allows for factoring out the registration of PropertyEditors
     * to separate objects, as an alternative to {@link #initBinder}.
     *
     * @see #initBinder
     */
    public final void setPropertyEditorRegistrars(PropertyEditorRegistrar propertyEditorRegistrar) {
        this.propertyEditorRegistrars = new PropertyEditorRegistrar[]{propertyEditorRegistrar};
    }

    /**
     * Specify multiple PropertyEditorRegistrars to be applied
     * to every DataBinder that this controller uses.
     * <p>Allows for factoring out the registration of PropertyEditors
     * to separate objects, as an alternative to {@link #initBinder}.
     *
     * @see #initBinder
     */
    public final void setPropertyEditorRegistrars(PropertyEditorRegistrar[] propertyEditorRegistrars) {
        this.propertyEditorRegistrars = propertyEditorRegistrars;
    }

    /**
     * Return the PropertyEditorRegistrars (if any) to be applied
     * to every DataBinder that this controller uses.
     */
    public final PropertyEditorRegistrar[] getPropertyEditorRegistrars() {
        return this.propertyEditorRegistrars;
    }

    protected void closeStream() {
        try {
            baos.flush();
            baos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public DecimalFormat getPriceScaleNumberFormat(EdsFinancialSettings fs) {
        if (fs != null && fs.getCalculationScale() != null) {
            return getNewDecimalPoint(fs.getCalculationScale());
        } else {
            return new DecimalFormat(",##0.00");
        }
    }

    public DecimalFormat getNewDecimalPoint(Integer productPrice) {
        if (productPrice == 0) {
            return new DecimalFormat(",##0");
        } else {
            String s = ".";
            for (int i = 0; i < productPrice; i++) {
                s = s.concat("0");
            }
            return new DecimalFormat(",##0" + s);
        }
    }

    public Integer getCalculationScale(EdsFinancialSettings fs) {
        if (fs != null && fs.getCalculationScale() != null && fs.getCalculationScale() > 0) {
            return fs.getCalculationScale();
        } else {
            return 2;
        }
    }

    public Date parseFilterParameterDate(String dateAsString) {
        try {
            return (dateAsString != null && !dateAsString.trim().isEmpty()) ? fpDateParseFormat.parse(dateAsString) : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ExcelData[] generateOneRowWithValue(int columnCount, String data, HSSFSheet sheet, int row) {
        List<ExcelData> rowList = new ArrayList<ExcelData>();

        for (int i = 0; i < columnCount; i++) {
            ExcelData item = new ExcelData("", 0);
            item.setBold(true);
            item.setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
            rowList.add(item);
        }
        rowList.get(0).addMerging(sheet, row, 0, row, rowList.size() - 2);
        rowList.get(0).setValue(data);
        ExcelData[] cellDatas = new ExcelData[rowList.size()];
        rowList.toArray(cellDatas);
        return cellDatas;
    }
}
