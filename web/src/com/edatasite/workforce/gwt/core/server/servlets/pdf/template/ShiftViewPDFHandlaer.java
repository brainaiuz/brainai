package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.edatasite.workforce.core.domain.EdsBrigada;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsShiftSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BrigadaManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.PdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftItemManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItems;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftTeamsItem;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShiftViewPDFHandlaer extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private ShiftManager shiftManager;
    @Autowired
    private ShiftItemManager shiftItemManager;
    @Autowired
    private BrigadaManager brigadaManager;
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Autowired
    protected PositionManager positionManager;
    @Autowired
    protected DepartmentManager departmentManager;
    @Autowired
    protected PdfTemplateManager pdfTemplateManager;
    @Autowired
    private ShiftSettingsManager shiftSettingsManager;

    private List<String> usersPerDays;
    private List<String> dailyTimes;

    private String summOfdailyTimes;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;

        ShiftItem shiftItem = hrmsService.getShiftItem(filterParameter.getObjectId(), false);
        pdfData.setCustomEntityTables(getShiftItemDetails(filterParameter, shiftItem));

        CustomisedITextTable countUserPerDay = new CustomisedITextTable();
        countUserPerDay.setColumnOrder(usersPerDays);
        CustomisedITextTable dailyTimesList = new CustomisedITextTable();
        dailyTimesList.setColumnOrder(dailyTimes);
        dailyTimesList.setName(summOfdailyTimes);

        CustomisedITextTable weekendsTable = new CustomisedITextTable();
        weekendsTable.addColumnOrder("WEEKENDS");
        List<Integer> weekends = getWeekendDays(shiftItem.getPeriod().getYear() + 1900, shiftItem.getPeriod().getMonth());
        for (Integer weekend : weekends) {
            weekendsTable.addRowWithCode("DAY" + weekend, String.valueOf(weekend));
        }

        Date date = shiftItem.getPeriod();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM/dd/yyyy");

        final HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        customData.put("CUSTOM_FIELD", customFieldData(shiftItem));
        customData.put("SHIFT_INFORMATION", getShiftInfo(shiftItem));
        customData.put("LABEL", getCustomDetails(filterParameter, shiftItem));
        customData.put("COUNT_USERS_PER_DAY", countUserPerDay);
        customData.put("DAILY_TIMES_LIST", dailyTimesList);
        customData.put("WEEKENDS", weekendsTable);
        customData.put("CUSTOM_DETAILS", getCustomTypeDetails(shiftItem));
        customData.put("CUSTOM_HEADERS", getCustomHeaders(shiftItem));

        baseInvoice.setCustomProductTableList(getCustomShiftEmployee(shiftItem));
        pdfData.setPeriod(simpleDateFormat.format(date));
        pdfData.setGroups(getGroups(filterParameter));
        pdfData.setCustomData(customData);
        pdfData.setBaseInvoice(baseInvoice);
        return pdfData;
    }

    private CustomisedITextTable getShiftInfo(ShiftItem shiftItem) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        CustomisedITextTable shiftTable = new CustomisedITextTable();
        shiftTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);

        if (shiftItem.getLookUpType() == LookUpConstants.OVERTIME) {
            shiftTable.setName("OVERTIME");
        } else if (shiftItem.getEndDate() != null) {
            shiftTable.setName("CUSTOM");
        }

        if (shiftItem.getCreator() != null && employeeManager.get(shiftItem.getCreator().getId()) != null) {
            EdsEmployee creator = employeeManager.get(shiftItem.getCreator().getId());
            if (creator.getTeam() != null) {
                EdsReferenceLocale locale = creator.getTeam().getLocale();
                if (locale != null) {
                    shiftTable.addRowWithCode("CREATOR_DEPARTMENT_ENG", "", locale.getEnglish());
                    shiftTable.addRowWithCode("CREATOR_DEPARTMENT_UZ", "", locale.getUzbek());
                    shiftTable.addRowWithCode("IS_BRIGADA_TEMPLATE", "", String.valueOf(shiftItem.getLookUpType().equals(LookUpConstants.BRIGADA_ID)));
                }
            }
        }

        if (shiftItem.getBirgada() != null && shiftItem.getBirgada().getId() != null && brigadaManager.get(shiftItem.getBirgada().getId()) != null) {
            EdsBrigada edsBrigada = brigadaManager.get(shiftItem.getBirgada().getId());
            EdsEmployee manager = edsBrigada.getManager();
            String formmattedName = manager.getFormmattedName();
            shiftTable.addRowWithCode("SELECTED_TEAM_NAME", "", shiftItem.getBirgada() != null ? shiftItem.getBirgada().getName() : "");
            shiftTable.addRowWithCode("TEAM_MANAGER", "", formmattedName != null ? formmattedName : "");
        }


        ZoneId zone = ZoneId.systemDefault();
        LocalDate startDate = shiftItem.getPeriod().toInstant().atZone(zone).toLocalDate().plusDays(1);
        LocalDate endDate = shiftItem.getEndDate() != null ? shiftItem.getEndDate().getDate().toInstant().atZone(zone).toLocalDate().plusDays(1) : null;

        shiftTable.addRowWithCode("START_DATE", "", shiftItem.getPeriod() != null ? startDate.format(formatter) : "");
        shiftTable.addRowWithCode("END_DATE", "", endDate != null ? endDate.format(formatter) : "");
        shiftTable.addRowWithCode("IS_BRIGADA_TEMPLATE", "", String.valueOf(shiftItem.getLookUpType().equals(LookUpConstants.BRIGADA_ID)));

        return shiftTable;
    }


    public CustomisedITextTable customFieldData(ShiftItem shiftItem) {

        EdsCompany company = userManager.getUser().getCompany();
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(company);
        DecimalFormat numberFormat = getPriceScaleNumberFormat(company, null);
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);

        if (shiftItem != null && shiftItem.getCustomFieldItems() != null && shiftItem.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem fieldItem : shiftItem.getCustomFieldItems()) {
                switch (fieldItem.getDataType()) {
                    case DATA_TYPE_DATE -> {
                        String dateValue = "";
                        String dateValueRu = "";
                        String dateValuePlus14Days = "";
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                        if (fieldItem.getFieldDateNonConvertedValue() != null) {
                            dateValue = fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            dateValueRu = fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? dateFormat1.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            if (fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate());
                                calendar.add(Calendar.DAY_OF_MONTH, 14);
                                dateValuePlus14Days = dateFormat1.format(calendar.getTime());
                            }
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), dateValue, DATA_TYPE_DATE);
                        customFieldTable.addRowWithCode(fieldItem.getAliasName() + "RU", fieldItem.getAliasName() + "RU", dateValueRu, DATA_TYPE_DATE);
                        customFieldTable.addRowWithCode(fieldItem.getAliasName() + "_RU_PLUS_14_DAYS", fieldItem.getAliasName() + "_RU_PLUS_14_DAYS", dateValuePlus14Days, DATA_TYPE_DATE);
                    }
                    case DATA_TYPE_NUMBER -> {
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(fieldItem.getFieldStringValue())) {
                            numberValue = escapeHtml(numberFormat.format(Double.valueOf(fieldItem.getFieldStringValue())));
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), numberValue, DATA_TYPE_NUMBER);
                    }
                    case DATA_TYPE_TEXT -> {
                        String textValue = "";
                        if (TYPE_ENTITY_LOOKUP.equals(fieldItem.getUiType())) {
                            String defaultValue = "";
                            if (StringUtils.isNotEmpty(fieldItem.getFieldStringValue())) {
                                Integer id = null;
                                try {
                                    id = Integer.valueOf(fieldItem.getFieldStringValue());
                                } catch (final NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                if (id != null && fieldItem.getQueryItems() != null) {
                                    for (final SelectItem selectItem : fieldItem.getQueryItems()) {
                                        if (selectItem.getId().equals(id)) {
                                            defaultValue = escapeHtml(selectItem.getName());
                                            break;
                                        }
                                    }
                                }
                            }
                            customFieldTable.addRowWithCode(fieldItem.getDefaultName(), fieldItem.getAliasName(), escapeHtml(defaultValue));
                        } else if (UI_TYPE_HTML_TEXTAREA.equals(fieldItem.getUiType())) {
                            String html = fieldItem.getFieldStringValue();
                            org.jsoup.nodes.Document doc = Jsoup.parse(html);
                            textValue = doc.body().text();
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(textValue), UI_TYPE_HTML_TEXTAREA);
                        } else {
                            textValue = fieldItem.getFieldStringValue();
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(textValue), UI_TYPE_HTML_TEXTAREA);
                        }
                    }
                    case DATA_TYPE_PROFILE_IMAGE -> {
                        String uploadImageId = "";
                        if (fieldItem.getProfielImageId() != null) {
                            uploadImageId = commonService.getImageUrl(fieldItem.getProfielImageId());
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), uploadImageId, UI_TYPE_PROFILE_IMAGE_WIDGET);
                    }
                    default ->
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(fieldItem.getFieldStringValue()), DATA_TYPE_TEXT);
                }

            }
        }

        return customFieldTable;
    }

    private ArrayList<CustomisedITextTable> getShiftItemDetails(ListingFilterParameter filterParameter, ShiftItem shiftItem) {
        ArrayList<CustomisedITextTable> list = new ArrayList<>();
        List<Integer> emptyListForMonthDate = getEmptyListForMonthDate(shiftItem.getShiftItems().get(0).getDayAndSelectedTimeSlotS().size());
        List<Integer> emptyListTest = getEmptyListForMonthDate(shiftItem.getShiftItems().get(0).getDayAndSelectedTimeSlotS().size());
        if (shiftItem.getShiftItems() != null && shiftItem.getLookUpType() != null) {
            int counter = 0;
            if (shiftItem.getLookUpType().equals(LookUpConstants.BRIGADA_ID)) {
                for (ShiftItems items : shiftItem.getShiftItems()) {
                    CustomisedITextTable customisedITextTable = new CustomisedITextTable();
                    customisedITextTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
                    if (counter == 0) {
                        customisedITextTable.addRowWithCode("GROUP_ID", "GROUP_ID", escapeHtml(items.getSelectedGroup().getName()));
                    }
                    items.getDayAndSelectedTimeSlotS().forEach((s, selectItem) -> {
                        if (selectItem != null) {
                            if (selectItem.getId() != null) {
                                EdsShiftSettings item = shiftSettingsManager.get(selectItem.getId());
                                double slot1 = Math.abs(item.getEndTime() - item.getStartTime());
                                slot1 = slot1 / 60;
                                customisedITextTable.addRowWithCode(s, selectItem.getName(), slot1 + "-" + item.getStartTime() + "-" + item.getEndTime() + "-" + selectItem.getId() + "-" + 1);
                                int i = Integer.parseInt(s) - 1;
                                emptyListForMonthDate.set(i, emptyListForMonthDate.get(i) + 1);
                                emptyListTest.set(i, emptyListTest.get(i) + 1);
                            }
                        } else {
                            customisedITextTable.addRowWithCode(s, " ", String.valueOf(0));
                        }
                    });
                    list.add(customisedITextTable);
                }
            } else {
                if (shiftItem.getEndDate() == null) {
                    for (ShiftItems items : shiftItem.getShiftItems()) {
                        EdsEmployee employee = employeeManager.get(items.getSelectedGroup().getId());
                        if (employee != null) {
                            CustomisedITextTable customisedITextTable = new CustomisedITextTable();
                            customisedITextTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
                            if (counter == 0) {
                                customisedITextTable.addRowWithCode("EMPLOYEE_NAME", "EMPLOYEE_NAME", escapeHtml(employee.getFormmattedName()));
                            }

                            EdsPosition position = employee.getPosition();
                            String posUzbek = "";
                            if (position != null && position.getLocale() != null && position.getLocale().getUzbek() != null) {
                                posUzbek = position.getLocale().getUzbek();
                            }
                            String departmentUz = "";
                            EdsDepartment department = employee.getTeam();
                            if (department != null && department.getLocale() != null && department.getLocale().getUzbek() != null) {
                                departmentUz = department.getLocale().getUzbek();
                            }
                            customisedITextTable.addRowWithCode("POSITION", "POSITION", posUzbek);
                            customisedITextTable.addRowWithCode("DEPARTMENT", "DEPARTMENT", departmentUz);
                            HashMap<String, Double> rates = items.getRates();
                            items.getDayAndSelectedTimeSlotS().forEach((s, selectItem) -> {
                                if (selectItem != null && selectItem.getId() != null) {
                                    EdsShiftSettings item = shiftSettingsManager.get(selectItem.getId());
                                    double slot1 = Math.abs(item.getEndTime() - item.getStartTime());
                                    slot1 = slot1 / 60;
                                    customisedITextTable.addRowWithCode(s, selectItem.getName(), slot1 + "-" + item.getStartTime() + "-" + item.getEndTime() + "-" + selectItem.getId() + "-" + 1);
                                    if (rates.get(s) != null) {
                                        customisedITextTable.addRowWithCode(s + "_RATE", s + "_RATE", String.format("%.2f", rates.get(s)));
                                    } else {
                                        customisedITextTable.addRowWithCode(s + "_RATE", s + "_RATE", 0 + "");
                                    }
                                    int i = Integer.parseInt(s) - 1;
                                    emptyListForMonthDate.set(i, emptyListForMonthDate.get(i) + 1);
                                    emptyListTest.set(i, (int) (emptyListTest.get(i) + slot1));
                                } else {
                                    try {
                                        int i = Integer.parseInt(s);
                                        if (items.getLeaveDays().get(i) != null) {
                                            customisedITextTable.addRowWithCode(s, items.getLeaveDays().get(i), items.getLeaveDays().get(i));
                                        } else {
                                            customisedITextTable.addRowWithCode(s, " ", String.valueOf(0));
                                        }
                                    } catch (Exception e) {
                                        customisedITextTable.addRowWithCode(s, " ", String.valueOf(0));
                                    }
                                }
                            });
                            list.add(customisedITextTable);
                        }
                    }
                }
            }
        }
        usersPerDays = new ArrayList<>();
        dailyTimes = new ArrayList<>();
        emptyListForMonthDate.forEach((a) -> usersPerDays.add(a == 0 ? "" : String.valueOf(a)));
        int summ = 0;
        for (Integer integer : emptyListTest) {
            dailyTimes.add(integer == 0 ? "" : String.valueOf(integer));
            summ += integer;
        }
        summOfdailyTimes = String.valueOf(summ);
        return list;
    }

    private CustomisedITextTable getCustomTypeDetails(ShiftItem shiftItem) {
        CustomisedITextTable ct = new CustomisedITextTable();
        ct.addColumn("EMPLOYEE", "EMPLOYEE");
        ct.addColumn("POSITION", "POSITION");
        ct.addColumn("DEPARTMENT", "DEPARTMENT");
        ct.addColumn("DATES", "DATES");
        ct.setRowsList(new ArrayList<>());

        for (ShiftItems item : shiftItem.getShiftItems()) {
            EdsEmployee employee = employeeManager.get(item.getSelectedGroup().getId());
            if (employee != null) {

                Map<String, List<CellData>> row = new LinkedHashMap<>();
                row.put("EMPLOYEE", List.of(new CellData(employee.getFormmattedName())));

                EdsPosition position = employee.getPosition();
                String posUzbek = "";
                if (position != null && position.getLocale() != null && position.getLocale().getUzbek() != null) {
                    posUzbek = position.getLocale().getUzbek();
                }
                row.put("POSITION", List.of(new CellData(posUzbek)));

                String departmentUz = "";
                EdsDepartment department = employee.getTeam();
                if (department != null && department.getLocale() != null && department.getLocale().getUzbek() != null) {
                    departmentUz = department.getLocale().getUzbek();
                }
                row.put("DEPARTMENT", List.of(new CellData(departmentUz)));

                List<CellData> dateList = new ArrayList<>();
                HashMap<String, SelectItem> dayAndSelectedTimeSlotS = item.getDayAndSelectedTimeSlotS();
                Date start = shiftItem.getPeriod();
                Date end = shiftItem.getEndDate() != null ? shiftItem.getEndDate().getDate() : start;
                LocalDate current = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
                LocalDate endLocal = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                while (!current.isAfter(endLocal)) {
                    String dateKey = current.format(formatter);
                    SelectItem timeslotItem = dayAndSelectedTimeSlotS.get(dateKey);
                    if (timeslotItem == null || timeslotItem.getId() == null) {
                        dateList.add(new CellData(""));
                    } else {
                        EdsShiftSettings shift = shiftSettingsManager.get(timeslotItem.getId());
                        dateList.add(new CellData(shift != null ? shift.getName() : ""));
                    }
                    current = current.plusDays(1);
                }
                row.put("DATES", dateList);
                ct.getRowsList().add(row);
            }
        }
        return ct;
    }

    private CustomisedITextTable getCustomHeaders(ShiftItem shiftItem) {
        String userLocale = ServerUtils.getUserLocale().getLanguage();
        CustomisedITextTable daysTable = new CustomisedITextTable();
        daysTable.addColumn("DAY_NUM", "DAY_NUM");
        daysTable.addColumn("DAY_NAME", "DAY_NAME");

        Date start = shiftItem.getPeriod();
        DateNonConvertable endDate = shiftItem.getEndDate();
        if (start == null || endDate == null) {
            return daysTable;
        }

        ZoneId zone = ZoneId.systemDefault();
        LocalDate startDate = start.toInstant().atZone(zone).toLocalDate().plusDays(1);
        LocalDate endLocalDate = endDate.getDate().toInstant().atZone(zone).toLocalDate().plusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Locale locale = new Locale(userLocale);

        while (!startDate.isAfter(endLocalDate)) {
            DayOfWeek dow = startDate.getDayOfWeek();
            daysTable.addRow(new String[]{startDate.format(formatter), dow.getDisplayName(TextStyle.SHORT, locale)});
            startDate = startDate.plusDays(1);
        }

        return daysTable;
    }

    private List<CustomisedITextTable> getCustomShiftEmployee(ShiftItem shiftItem) {
        List<CustomisedITextTable> list = new ArrayList<>();
        Map<Integer, List<Object[]>> rows = new LinkedHashMap<>();

        List<Object[]> customEmployeesShift = shiftManager.getCustomEmployeesShift(shiftItem.getId());
        for (Object[] objects : customEmployeesShift) {
            int empId = objects[0] != null ? ((Number) objects[0]).intValue() : 0;
            rows.computeIfAbsent(empId, k -> new ArrayList<>()).add(objects);
        }

        for (Map.Entry<Integer, List<Object[]>> entry : rows.entrySet()) {
            List<Object[]> object = entry.getValue();
            CustomisedITextTable shiftTable = new CustomisedITextTable();
            shiftTable.addColumn("EMP_NAME", "EMP_NAME");
            shiftTable.addColumn("EMP_POS", "EMP_POS");
            shiftTable.addColumn("EMP_DEVICE", "EMP_DEVICE");
            shiftTable.addColumn("DATE", "DATE");
            shiftTable.addColumn("EMP_LAT", "EMP_LAT");
            shiftTable.addColumn("EMP_LONG", "EMP_LONG");
            shiftTable.addColumn("MANAGER_NAME", "MANAGER_NAME");
            shiftTable.addColumn("MANAGER_DEVICE", "MANAGER_DEVICE");
            shiftTable.addColumn("MANAGER_DATE", "MANAGER_DATE");
            shiftTable.addColumn("MANAGER_LAT", "MANAGER_LAT");
            shiftTable.addColumn("MANAGER_LONG", "MANAGER_LONG");

            for (Object[] objects : object) {
                ArrayList<String> row = new ArrayList<>();
                int empId = objects[0] != null ? (Integer) objects[0] : 0;
                String empname = objects[1] != null ? (String) objects[1] : "";
                String empPos = objects[2] != null ? (String) objects[2] : "";
                String empDeviceId = objects[3] != null ? (String) objects[3] : "";
                Date shiftDate = objects[4] != null ? (Date) objects[4] : new Date();
                Timestamp employeeTime = objects[5] != null ? (Timestamp) objects[5] : new Timestamp(System.currentTimeMillis());
                Double empLat = objects[6] != null ? (Double) objects[6] : 0.0;
                Double empLong = objects[7] != null ? (Double) objects[7] : 0.0;
                int managerId = objects[8] != null ? (Integer) objects[8] : 0;
                String managerName = objects[9] != null ? (String) objects[9] : "";
                String managerDeviceId = objects[10] != null ? (String) objects[10] : "";
                Timestamp managerTime = objects[11] != null ? (Timestamp) objects[11] : new Timestamp(System.currentTimeMillis());
                Double managerLat = objects[12] != null ? (Double) objects[12] : 0.0;
                Double managerLong = objects[13] != null ? (Double) objects[13] : 0.0;

                row.add(empname);
                row.add(empPos);
                row.add(empDeviceId);
                row.add(employeeTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                row.add(String.valueOf(empLat));
                row.add(String.valueOf(empLong));
                row.add(managerName);
                row.add(managerDeviceId);
                row.add(managerTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                row.add(String.valueOf(managerLat));
                row.add(String.valueOf(managerLong));
                shiftTable.addRow(row.toArray(new String[]{}));

            }
            list.add(shiftTable);
        }

        return list;
    }


    private List<Integer> getWeekendDays(int year, int month) {
        List<Integer> weekends = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                weekends.add(day);
            }
        }
        return weekends;
    }

    private CustomisedITextTable getCustomDetails(ListingFilterParameter filterParameter, ShiftItem shiftItem) {
        EdsUser user = shiftManager.getUser();
        CustomisedITextTable customisedITextTable = new CustomisedITextTable();
        if (shiftItem != null && shiftItem.getShiftItems() != null) {
            customisedITextTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
            customisedITextTable.addRowWithCode("TEAM", "TEAM", commonLocalizer.localize(PdfLocalizationName.team));
            customisedITextTable.addRowWithCode("EMPLOYEE_CODE", "EMPLOYEE_CODE", commonLocalizer.localize(PdfLocalizationName.employeeCode));
            customisedITextTable.addRowWithCode("FULL_NAME", "FULL_NAME", commonLocalizer.localize(PdfLocalizationName.fullName));
            customisedITextTable.addRowWithCode("POSITION", "POSITION", commonLocalizer.localize(PdfLocalizationName.position));
            customisedITextTable.addRowWithCode("DEPARTMENT", "DEPARTMENT", commonLocalizer.localize(PdfLocalizationName.department));
            customisedITextTable.addRowWithCode("LABEL", "LABEL", commonLocalizer.localize(PdfLocalizationName.label));
            customisedITextTable.addRowWithCode("PERIOD", "PERIOD", commonLocalizer.localize(PdfLocalizationName.period));
            customisedITextTable.addRowWithCode("INFORMATION", "INFORMATION", commonLocalizer.localize(PdfLocalizationName.basicInformation));
            customisedITextTable.addRowWithCode("WORKING_HOURS", "WORKING_HOURS", commonLocalizer.localize(PdfLocalizationName.total) + " " + commonLocalizer.localize(PdfLocalizationName.workingHours).toLowerCase());
            customisedITextTable.addRowWithCode("SHIFT_CODE", commonLocalizer.localize(PdfLocalizationName.shift), shiftItem.getCode() != null ? shiftItem.getCode() : "-");
            customisedITextTable.addRowWithCode("STATUS", "STATUS", shiftItem.getStatusCode() != null ? shiftItem.getStatusCode() : "-");
            if (shiftItem.getApprovers().size() != 0) {
                String approvers = "";
                for (ApproverItemMini value : shiftItem.getApprovers()) {
                    approvers = value.getExactEmployee().getName() + "#";
                }
                customisedITextTable.addRowWithCode("APPROVERS", commonLocalizer.localize(PdfLocalizationName.approvers), approvers);
            } else {
                customisedITextTable.addRowWithCode("APPROVERS", commonLocalizer.localize(PdfLocalizationName.approvers), "");
            }
            String manager = shiftItem.getManager().replace("<br>", "#");
            if (shiftItem.getManager() != null) {
                customisedITextTable.addRowWithCode("MANAGER", commonLocalizer.localize(PdfLocalizationName.manager), manager);
            }
            if (shiftItem.getBackupManager() != null) {
                String backUpManager = shiftItem.getBackupManager().replace("<br>", "#");
                customisedITextTable.addRowWithCode("BACKUP_MANAGER", commonLocalizer.localize(PdfLocalizationName.backupManagers), backUpManager);
            }
            if (shiftItem.getDepartment() != null) {
                EdsDepartment edsDepartment = departmentManager.get(shiftItem.getDepartment().getId());
                EdsReference departmentName = edsDepartment.getDepartmentName();
                customisedITextTable.addRowWithCode("SHIFT_DEPARTMENT_UZ", "SHIFT_DEPARTMENT_UZ", departmentName != null && edsDepartment.getDepartmentName().getLocale() != null ? edsDepartment.getDepartmentName().getLocale().getUzbek() : edsDepartment.getName());
            }
        }
        return customisedITextTable;
    }


    private ArrayList<CustomisedITextTable> getGroups(ListingFilterParameter listingFilterParameter) {
        ArrayList<CustomisedITextTable> list = new ArrayList<>();
        ShiftItem shiftItem = hrmsService.getShiftItem(listingFilterParameter.getObjectId(), false);
        LinkedList<Integer> groupIds = new LinkedList<>();
        if (shiftItem.getShiftItems() != null) {
            for (ShiftItems items : shiftItem.getShiftItems()) {
                if (items.getSelectedGroup() != null) {
                    groupIds.add(items.getSelectedGroup().getId());
                }
            }
        }

        LinkedHashMap<Integer, List<ShiftTeamsItem>> projectEmployeesSelectItem = shiftItem.getShiftTeams();

        int i = 0;
        for (List<ShiftTeamsItem> value : projectEmployeesSelectItem.values()) {
            CustomisedITextTable customisedITextTable = new CustomisedITextTable();
            customisedITextTable.addColumn("TEAM", COLUMN_NAME);
            customisedITextTable.addColumn("FULL_NAME", COLUMN_VALUE);
            customisedITextTable.addColumn("CODE", COLUMN_VALUE);
            customisedITextTable.addColumn("POSITION", COLUMN_VALUE);
            customisedITextTable.addColumn("POSITION_UZ", COLUMN_VALUE);
            customisedITextTable.addColumn("DEPARTMENT", COLUMN_VALUE);
            customisedITextTable.addColumn("DEPARTMENT_UZ", COLUMN_VALUE);
            customisedITextTable.addColumn("LABEL", COLUMN_VALUE);
            customisedITextTable.addColumn("ADDITIONAL_POSITION", COLUMN_VALUE);

            for (int j = 0; j < value.size(); j++) {
                ArrayList<String> row = new ArrayList<>();
                row.add(value.get(j).getTeam().getName());
                row.add(value.get(j).getFullName().getName());
                row.add(value.get(j).getEmployeeCode());
                row.add(value.get(j).getPosition());
                EdsEmployee employeeByNumber = employeeManager.getEmployeeByNumber(value.get(j).getEmployeeCode());
                if (employeeByNumber != null) {
                    EdsPosition position = employeeByNumber.getPosition();
                    String posUzbek = "";
                    if (position != null && position.getLocale() != null && position.getLocale().getUzbek() != null) {
                        posUzbek = position.getLocale().getUzbek();
                    }
                    row.add(posUzbek);
                    row.add(value.get(j).getDepartment());
                    String departmentUz = "";
                    EdsDepartment department = employeeByNumber.getTeam();
                    if (department != null && department.getLocale() != null && department.getLocale().getUzbek() != null) {
                        departmentUz = department.getLocale().getUzbek();
                    }
                    row.add(departmentUz);
                    row.add(value.get(j).getLabel());
                    row.add(value.get(j).getAdditionalPosition());
                    customisedITextTable.addRow(row.toArray(new String[]{}));
                }
            }
            list.add(i, customisedITextTable);
            i++;
        }

        return list;
    }


    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.isLandscape() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter requestObject = (ListingFilterParameter) dataClass;
        if (requestObject.getStartDateNC() != null) {
            String startDate = requestObject.getStartDateNC();
            LocalDate localDate = Instant.ofEpochMilli(Long.parseLong(startDate)).atZone(ZoneId.systemDefault()).toLocalDate();
            String companyName = "";
            if (user != null) {
//            companyName = user.getCompany() != null && user.getCompany().getName() != null ? user.getCompany().getName() : "";
                setFileName("SHIFT_" + requestObject.getShiftType() + "_" + localDate.getYear() + "_" + localDate.getMonth().getValue());
            }
        } else {
            setFileName("SHIFT_" + requestObject.getShiftType());
        }
    }


    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof ListingFilterParameter) {
            return ((ListingFilterParameter) object).getTemplateID();
        }
        return null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return "shift";
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.SHIFT;
    }

    private List<Integer> getEmptyListForMonthDate(int maxDays) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < maxDays; i++) {
            list.add(0);
        }
        return list;
    }

}
