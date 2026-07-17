package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.LocalizationManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class LocalizationExcelHandler extends BaseExcelHandler {
    @Autowired
    private LocalizationManager localizationManager;

    private static final Logger log = LoggerFactory.getLogger(TaskListExcelHandler.class);

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName() {
        filename = "Property";
    }

    private HSSFRow createRow(HSSFSheet sheet, int rowNum, String[] items) {
        HSSFRow row = sheet.createRow(rowNum);
        for (int i = 0; i < items.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            try {
                cell.setCellValue(items[i]);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return row;
    }

    private String writeToDB() {
        String filename = "D:\\project\\multidb\\war\\docs\\kpi.xls";
        List sheetData = new ArrayList();
        FileInputStream fis = null;
        StringBuilder Sums = new StringBuilder();
        StringBuilder error = new StringBuilder();
        String sheetName = "";
        try {

            fis = new FileInputStream(new File(filename));
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(fis));
            for (int i = 1; i <= 63; i++) {

                HSSFSheet sheet = workbook.getSheetAt(i);
                sheetName = sheet.getSheetName();
                Iterator rows = sheet.rowIterator();
                int iter = 0;
                /*if ("AccountingStrings".equals(sheetName) || "CoreStrings".equals(sheetName) || "CrmStrings".equals(sheetName)
                        || "InvoiceStrings".equals(sheetName) || "LocationStrings".equals(sheetName) || "ProjectStrings".equals(sheetName)
                        || "TaskStrings".equals(sheetName) || "WfmStrings".equals(sheetName))  {
                    continue;
                }*/

                Sums.append("---------------------").append(sheetName).append("---------------------\n");
                int arabicColumnNum = 0;
                while (rows.hasNext()) {
                    HSSFRow row = (HSSFRow) rows.next();
                    Iterator cells = row.cellIterator();
                    iter++;
                    if (iter == 2) {
                        int k = 0;
                        while (cells.hasNext()) {
                            k++;
                            HSSFCell cell = (HSSFCell) cells.next();
                            String columnName = cell.getStringCellValue().trim().toLowerCase();
                            if ("portuguese".equals(columnName)) {
                                arabicColumnNum = k;
                                break;
                            }
                        }
                    }
                    if (iter > 2) {
                        int index = 0;
                        String code = "";
                        while (cells.hasNext()) {
                            HSSFCell cell = (HSSFCell) cells.next();
                            String s = "";
                            try {
                                s = cell.getStringCellValue().trim();
                            } catch (IllegalStateException e) {
                                error.append(code).append("\n");
                            }
                            index++;
                            if (index == 1 && "".equals(code)) {
                                code = s;
                            }
                            if (index == arabicColumnNum) {
                                if (!"".equals(s) && s != null && !"null".equals(s)) {
                                    EdsLocalization localization = localizationManager.getLocalizationByCode(sheetName, s);
                                    String action = "EDIT-------------------------------------";
                                    if (localization == null) {
                                        List<EdsLocalization> localizationExist = localizationManager.listByPropertyCode(sheetName, code);
                                        if (localizationExist.size() > 0) {
                                            action = "ADD-------------------------------------";
                                        }
                                        Sums.append(action).append(" update localization set por=E'").append(s).append("' where propertycode='").append(sheetName.trim()).append("' and code='").append(code).append("'; \n");
                                        System.out.println(code + "\n");
                                    }

                                }
                            }

                        }
                    }
                }

            }
            String sss = "";
            sss = Sums.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return Sums.toString();
    }

    private Map<String, String> propertyFileList() {
        Map<String, String> propertyPath = new HashMap<>();
        propertyPath.put("AccountingMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\accounting\\client\\localization\\AccountingMessages");
        propertyPath.put("skill", "D:\\project\\multidb\\web\\WebContent\\WEB-INF\\classes\\localization\\hrms\\skill");
        propertyPath.put("CoreMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\localization\\CoreMessages");
        propertyPath.put("IssueStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\issue\\client\\localization\\IssueStrings");
        propertyPath.put("pdf", "D:\\project\\multidb\\web\\WebContent\\WEB-INF\\classes\\localization\\pdf\\pdf");
        propertyPath.put("ProjectStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\project\\client\\localization\\ProjectStrings");
        propertyPath.put("MyAccountStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\myaccount\\client\\localization\\MyAccountStrings");
        propertyPath.put("WebsiteStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\website\\client\\localization\\WebsiteStrings");
        propertyPath.put("account", "D:\\project\\multidb\\web\\WebContent\\WEB-INF\\classes\\localization\\accounting\\account");
        propertyPath.put("ProfileStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\profile\\client\\localization\\SettingStrings");
        propertyPath.put("GoogleCalendarStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\googlecalendar\\client\\localization\\GoogleCalendarStrings");
        propertyPath.put("AssessmentStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\assessment\\client\\localization\\AssessmentStrings");
        propertyPath.put("HrmsStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\hrms\\client\\localization\\HrmsStrings");
        propertyPath.put("CoreStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\localization\\CoreStrings");
        propertyPath.put("LocationStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\location\\client\\location\\LocationStrings");
        propertyPath.put("PayslipStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\payroll\\client\\localizaion\\PayslipStrings");
        propertyPath.put("ProfileMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\profile\\client\\localization\\ProfileMessages");
        propertyPath.put("WorkspaceMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\workspace\\client\\localization\\WorkspaceMessages");
        propertyPath.put("PaymentStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\payroll\\client\\localizaion\\PaymentStrings");
        propertyPath.put("GoogleCalendarMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\googlecalendar\\client\\localization\\GoogleCalendarMessages");
        propertyPath.put("WfmStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\localization\\WfmStrings");
        propertyPath.put("BackendStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\backend\\client\\localization\\BackendStrings");
        propertyPath.put("WftBackendMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\backend\\client\\localization\\WftBackendMessages");
        propertyPath.put("country", "D:\\project\\multidb\\web\\WebContent\\WEB-INF\\classes\\localization\\countries\\country");
        propertyPath.put("allreference", "D:\\project\\multidb\\web\\WebContent\\WEB-INF\\classes\\localization\\pdf\\allreference");
        propertyPath.put("CrmStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\crm\\client\\localization\\CrmStrings");
        propertyPath.put("AccountingStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\accounting\\client\\localization\\AccountingStrings");
        propertyPath.put("WorkspaceStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\workspace\\client\\localization\\WorkspaceStrings");
        propertyPath.put("AssessmentMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\assessment\\client\\localization\\AssessmentMessages");
        propertyPath.put("CrmMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\crm\\client\\localization\\CrmMessages");
        propertyPath.put("ExpenseStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\expenses\\client\\localization\\ExpenseStrings");
        propertyPath.put("reference", "D:\\project\\multidb\\web\\WebContent\\WEB-INF\\classes\\localization\\reference\\reference");
        propertyPath.put("InventoryStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\invoice\\client\\localization\\InventoryStrings");
        propertyPath.put("ClientMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\client\\client\\localization\\ClientMessages");
        propertyPath.put("messages", "D:\\project\\multidb\\web\\WebContent\\WEB-INF\\classes\\localization\\messages");
        propertyPath.put("TaskStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\task\\client\\localization\\TaskStrings");
        propertyPath.put("WfmMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\localization\\WfmMessages");
        propertyPath.put("ProjectMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\project\\client\\localization\\ProjectMessages");
        propertyPath.put("PayrollStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\payroll\\client\\localizaion\\PayrollStrings");
        propertyPath.put("RichTextToolbar$Strings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\ui\\RichToolbar\\RichTextToolbar$Strings");
        propertyPath.put("StatutoryPaymentStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\payroll\\client\\localizaion\\StatutoryPaymentStrings");
        propertyPath.put("common", "D:\\project\\multidb\\web\\WebContent\\WEB-INF\\classes\\localization\\common\\common");
        propertyPath.put("EmployeeMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\employee\\client\\localization\\EmployeeMessages");
        propertyPath.put("AvailabilityMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\availability\\client\\localization\\AvailabilityMessages");
        propertyPath.put("TeamMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\team\\client\\localization\\TeamMessages");
        propertyPath.put("InvoiceMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\invoice\\client\\localization\\InvoiceMessages");
        propertyPath.put("GoogleContactsStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\googlecontacts\\client\\localization\\GoogleContactsStrings");
        propertyPath.put("myactivity", "D:\\project\\multidb\\web\\WebContent\\WEB-INF\\classes\\localization\\myactivity\\myactivity");
        propertyPath.put("WfmConstantsWithLookup", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\localization\\WfmConstantsWithLookup");
        propertyPath.put("InvoiceStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\invoice\\client\\localization\\InvoiceStrings");
        propertyPath.put("TaskMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\task\\client\\localization\\TaskMessages");
        propertyPath.put("TeamStrings", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\team\\client\\localization\\TeamStrings");
        propertyPath.put("region", "D:\\project\\multidb\\web\\WebContent\\WEB-INF\\classes\\localization\\regions\\region");
        propertyPath.put("emailsubject", "D:\\project\\multidb\\web\\WebContent\\WEB-INF\\classes\\localization\\emailsubject\\emailsubject");
        propertyPath.put("MyAccountMessages", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\myaccount\\client\\localization\\MyAccountMessages");
        propertyPath.put("MeetingMinutesString", "D:\\project\\multidb\\web\\src\\com\\edatasite\\workforce\\gwt\\meetingMinutes\\client\\localization\\MeetingMinutesString");
        return propertyPath;
    }

    protected HSSFWorkbook getWorkBook(Object object) {

        HashMap<String, String> propertyPath = new HashMap<>();
        propertyPath.put("AccountingMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\accounting\\client\\localization\\");
        propertyPath.put("skill", "web\\WebContent\\WEB-INF\\classes\\localization\\hrms\\");
        propertyPath.put("CoreMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\localization\\");
        propertyPath.put("IssueStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\issue\\client\\localization\\");
        propertyPath.put("pdf", "web\\WebContent\\WEB-INF\\classes\\localization\\pdf\\");
        propertyPath.put("ProjectStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\project\\client\\localization\\");
        propertyPath.put("MyAccountStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\myaccount\\client\\localization\\");
        propertyPath.put("WebsiteStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\website\\client\\localization\\");
        propertyPath.put("account", "web\\WebContent\\WEB-INF\\classes\\localization\\accounting\\");
        propertyPath.put("ProfileStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\profile\\client\\localization\\");
        propertyPath.put("GoogleCalendarStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\googlecalendar\\client\\localization\\");
        propertyPath.put("AssessmentStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\assessment\\client\\localization\\");
        propertyPath.put("HrmsStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\hrms\\client\\localization\\");
        propertyPath.put("CoreStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\localization\\");
        propertyPath.put("LocationStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\location\\client\\location\\");
        propertyPath.put("PayslipStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\payroll\\client\\localizaion\\");
        propertyPath.put("ProfileMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\profile\\client\\localization\\");
        propertyPath.put("WorkspaceMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\workspace\\client\\localization\\");
        propertyPath.put("PaymentStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\payroll\\client\\localizaion\\");
        propertyPath.put("GoogleCalendarMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\googlecalendar\\client\\localization\\");
        propertyPath.put("WfmStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\localization\\");
        propertyPath.put("BackendStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\backend\\client\\localization\\");
        propertyPath.put("WftBackendMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\backend\\client\\localization\\");
        propertyPath.put("country", "web\\WebContent\\WEB-INF\\classes\\localization\\countries\\");
        propertyPath.put("allreference", "web\\WebContent\\WEB-INF\\classes\\localization\\pdf\\");
        propertyPath.put("CrmStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\crm\\client\\localization\\");
        propertyPath.put("AccountingStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\accounting\\client\\localization\\");
        propertyPath.put("WorkspaceStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\workspace\\client\\localization\\");
        propertyPath.put("AssessmentMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\assessment\\client\\localization\\");
        propertyPath.put("CrmMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\crm\\client\\localization\\");
        propertyPath.put("ExpenseStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\expenses\\client\\localization\\");
        propertyPath.put("reference", "web\\WebContent\\WEB-INF\\classes\\localization\\reference\\");
        propertyPath.put("InventoryStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\invoice\\client\\localization\\");
        propertyPath.put("DashboardStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\dashboard\\client\\localization\\");
        propertyPath.put("CompanySettingsStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\profile\\client\\localization\\");
        propertyPath.put("ClientMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\client\\client\\localization\\");
        propertyPath.put("messages", "web\\WebContent\\WEB-INF\\classes\\localization\\");
        propertyPath.put("TaskStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\task\\client\\localization\\");
        propertyPath.put("WfmMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\localization\\");
        propertyPath.put("ProjectMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\project\\client\\localization\\");
        propertyPath.put("PayrollStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\payroll\\client\\localizaion\\");
        propertyPath.put("RichTextToolbar$Strings", "web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\ui\\RichToolbar\\");
        propertyPath.put("StatutoryPaymentStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\payroll\\client\\localizaion\\");
        propertyPath.put("common", "web\\WebContent\\WEB-INF\\classes\\localization\\common\\");
        propertyPath.put("EmployeeMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\employee\\client\\localization\\");
        propertyPath.put("AvailabilityStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\availability\\client\\localization\\");
        propertyPath.put("AvailabilityMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\availability\\client\\localization\\");
        propertyPath.put("TeamMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\team\\client\\localization\\");
        propertyPath.put("InvoiceMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\invoice\\client\\localization\\");
        propertyPath.put("GoogleContactsStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\googlecontacts\\client\\localization\\");
        propertyPath.put("myactivity", "web\\WebContent\\WEB-INF\\classes\\localization\\myactivity\\");
        propertyPath.put("WfmConstantsWithLookup", "web\\src\\com\\edatasite\\workforce\\gwt\\core\\client\\localization\\");
        propertyPath.put("InvoiceStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\invoice\\client\\localization\\");
        propertyPath.put("TaskMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\task\\client\\localization\\");
        propertyPath.put("TeamStrings", "web\\src\\com\\edatasite\\workforce\\gwt\\team\\client\\localization\\");
        propertyPath.put("region", "web\\WebContent\\WEB-INF\\classes\\localization\\regions\\");
        propertyPath.put("emailsubject", "web\\WebContent\\WEB-INF\\classes\\localization\\emailsubject\\");
        propertyPath.put("MyAccountMessages", "web\\src\\com\\edatasite\\workforce\\gwt\\myaccount\\client\\localization\\");
        propertyPath.put("MeetingMinutesString", "web\\src\\com\\edatasite\\workforce\\gwt\\meetingMinutes\\client\\localization\\");
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        HSSFWorkbook wb = new HSSFWorkbook();

        Map<String, String> path = propertyFileList();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            String[] header = new String[4];
            header[0] = "Code";
            header[1] = "Default";
            header[2] = "English";
            header[3] = "Russian";
            /*header[4] = "Turkish";
            header[5] = "Spanish";
            header[6] = "Italian";
            header[7] = "French";
            header[8] = "Portuguese";
            header[9] = "Arabian";
            header[10] = "Dutch";
            header[11] = "Thai";*/

            /*List<EdsLocalization> propertyResult = new ArrayList<EdsLocalization>();
            for(String pathJava : path.keySet()){
                File f = new File(path.get(pathJava)+".java");
                List<EdsLocalization> property = localizationManager.list(pathJava);
                if (f.exists()){
                    BufferedReader reader = new BufferedReader(new FileReader(path.get(pathJava)+".java"));
                    String line = null;
                    ArrayList<String> listKey = new ArrayList<String>();
                    boolean t = true;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if  (line.indexOf("@Key(") >=0 && (line.indexOf("//") == -1 || line.indexOf("//") > 5)){
                           String val = line.substring(line.indexOf("@Key(")+6, line.indexOf("\")"));
                            listKey.add(val);
                            t = false;
                        }else{
                            if (t && (line.indexOf("//") == -1 || line.indexOf("//") > 5) && line.indexOf("String ") >= 0  && line.indexOf("(") >= 0){
                                String val = line.substring(line.indexOf("String ")+7, line.indexOf("("));
                                listKey.add(val);
                            }
                            t = true;
                        }
                    }
                    System.out.println("Jar="+listKey.size()+"--"+property.size()+"=Prop");
                    for(String itemKey : listKey){
                        for(EdsLocalization res : property){
                            if (res.getCode().trim().equals(itemKey.trim())){
                                propertyResult.add(res);
                            }
                        }
                    }
                }else {
                    for(EdsLocalization res : property){
                            propertyResult.add(res);
                    }
                }
            }*/
            List<EdsLocalization> propertyResult = localizationManager.list();
            String propertyName = "";
            ArrayList<String> existProperty = new ArrayList<>();
            HSSFSheet sheet = wb.createSheet("0");
            int i = 2;
            int stop = 0;
            for (EdsLocalization item : propertyResult) {
                if (!existProperty.contains(item.getPropertyCode())) {
                    if (stop == 0) {
                        wb.removeSheetAt(0);
                        stop++;
                    }
                    sheet = wb.createSheet(item.getPropertyCode());
                    createRow(sheet, 0, new String[]{propertyPath.get(item.getPropertyCode())});
                    createRow(sheet, 1, header);
                    existProperty.add(item.getPropertyCode());
                    i = 2;
                }
                if (StringUtils.isNotBlank(item.getDefaultText()) && StringUtils.isBlank(item.getRu())) {
                    String[] str = new String[4];
                    str[0] = item.getCode();
                    str[1] = item.getDefaultText();
                    str[2] = item.getEn();
                    str[3] = item.getRu();
                /*str[4] = item.getTr();
                str[5] = item.getSpa();
                str[6] = item.getIt();
                str[7] = item.getFr();
                str[8] = item.getPt();
                str[9] = item.getAr();
                str[10] = item.getNl();
                str[11] = item.getTh();*/
                    createRow(sheet, i, str);
                    i++;
                }

            }
            long timeStarted = System.currentTimeMillis();
            System.out.println("Localization excel Data generation, time spent:" + (System.currentTimeMillis() - timeStarted));
            //WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);
            return wb;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate localization excel report, exception: " + e);
        }
        return null;
    }
}
