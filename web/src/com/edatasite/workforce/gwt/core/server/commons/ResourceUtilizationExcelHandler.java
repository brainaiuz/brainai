package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.EmployeeResourceUtilItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ExportToExcelItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ProjectTaskItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.TaskItem;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResourceUtilizationExcelHandler extends BaseExcelHandler {


    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;

    private CellStyle greenColor;
    private CellStyle redColor;
    private CellStyle cellStyle;
    private CellStyle cellWithBottomBorderStyle;
    private CellStyle styleBorderAll;
    private CellStyle rightBorderStyle;
    private CellStyle styleBorderBottomRight;

    private CellStyle styleSundayMonth;
    private CellStyle styleDefaultMonthOptimallyAllocatedDay;
    private CellStyle styleDefaultMonthUnderAllocatedDay;
    private CellStyle styleDefaultMonthOverAllocatedDay;
    private CellStyle styleWorkCompanyHoliday;
    private CellStyle styleWithLrDay;

    private static Logger log = LoggerFactory.getLogger(ResourceUtilizationExcelHandler.class);

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    private Boolean isEnableLongTimeFormatResourceUtilReportTable;
    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName() {
        filename = "ResourceUtilization";
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }

    protected HSSFWorkbook getWorkBook(Object object) {

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Resource Utilization");
        sheet.setDefaultColumnWidth(4);
        sheet.autoSizeColumn(0);
        generateEmployee(workbook, sheet, filterParametrs);

        return workbook;
    }

    private int getNum() {
        return 5;
    }

    private HSSFRow genetateOneRowWithEmpityCell(HSSFSheet sheet, int rowNumber, int month, boolean withBorderCell) {
        HSSFRow row = sheet.createRow(rowNumber);
        for (int i = 0; i <= (getNum() + month + 1); i++) {
            Cell cell = createCell(row, i, withBorderCell);
        }
        return row;
    }

    private void generateEmployee(HSSFWorkbook workbook, HSSFSheet sheet, ListingFilterParameter filterParametrs) {

        sheet.setRowSumsBelow(false);
        Integer departmentId = filterParametrs.getDepartmentId();
        Integer employeeId = filterParametrs.getEmployeeId();
        Integer projectId = filterParametrs.getProjectId();
        String startDate = filterParametrs.getStartDateNC();
        String endDate = filterParametrs.getEndDateNC();
        String positionIds = filterParametrs.getPositionIDs();
        boolean noPosition = filterParametrs.getNoPosition();
        boolean showFilledCells = filterParametrs.isShowFilledCells();
        boolean showOnlyActiveEmployees = filterParametrs.isShowActive();
        Integer month = 0;
        boolean timeSlotHours = false;
        boolean inOutHours = false;
        boolean timeSheetHours = false;
        boolean leaveRequestHours = false;

        String[] params = filterParametrs.getParams().split("@");
        month = Integer.parseInt(params[0]);
        timeSlotHours = Boolean.valueOf(params[1]);
        inOutHours = Boolean.valueOf(params[2]);
        timeSheetHours = Boolean.valueOf(params[3]);
        leaveRequestHours = Boolean.valueOf(params[4]);
        String currentDate = params[5];

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setProjectId(projectId);
        fp.setEmployeeId(employeeId);
        fp.setDepartmentId(departmentId);
        fp.setPositionIDs(positionIds);
        fp.setNoPosition(noPosition);
        fp.setShowFilledCells(showFilledCells);
        fp.setShowActive(showOnlyActiveEmployees);

        ExportToExcelItem excelData = allInOneService.getResourceUtilizationExcelData(fp, startDate, endDate, month);

        CellStyle styleName = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        styleName.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        styleName.setFont(font);

        CellStyle styleEmpName = workbook.createCellStyle();
        font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        styleEmpName.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        styleEmpName.setFont(font);
        HSSFColor color2 = setColor(workbook, (byte) 205, (byte) 207, (byte) 180);
        styleEmpName.setFillForegroundColor(color2.getIndex());
        styleEmpName.setFillPattern(CellStyle.SOLID_FOREGROUND);


        CellStyle timeslotStyle = workbook.createCellStyle();
        Font font2 = workbook.createFont();
        font2.setColor(HSSFColor.BLUE.index);
        timeslotStyle.setFont(font2);

        CellStyle greenStyle = workbook.createCellStyle();
        Font font3 = workbook.createFont();
        font3.setColor(HSSFColor.GREEN.index);
        greenStyle.setFont(font3);

        CellStyle redStyle = workbook.createCellStyle();
        Font font4 = workbook.createFont();
        redStyle.setAlignment(CellStyle.ALIGN_CENTER);
        font4.setColor(HSSFColor.RED.index);
        redStyle.setFont(font3);

        CellStyle styleAlign = workbook.createCellStyle();
        styleAlign.setAlignment(CellStyle.ALIGN_CENTER);
        styleAlign.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        CellStyle cellWithBorderStyle = workbook.createCellStyle();
        cellWithBorderStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellWithBorderStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellWithBorderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        CellStyle styleBorderRight = workbook.createCellStyle();
        styleBorderRight.setBorderRight(CellStyle.BORDER_THIN);
        styleBorderRight.setRightBorderColor(IndexedColors.BLACK.getIndex());

        CellStyle styleBorderBotomRight = workbook.createCellStyle();
        styleBorderBotomRight.setBorderRight(CellStyle.BORDER_THIN);
        styleBorderBotomRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderBotomRight.setBorderBottom(CellStyle.BORDER_THIN);
        styleBorderBotomRight.setBottomBorderColor(IndexedColors.BLACK.getIndex());


        CellStyle styleBorderBotomRightBold = workbook.createCellStyle();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        styleBorderBotomRightBold.setFont(font);
        styleBorderBotomRightBold.setBorderRight(CellStyle.BORDER_THIN);
        styleBorderBotomRightBold.setRightBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderBotomRightBold.setBorderBottom(CellStyle.BORDER_THIN);
        styleBorderBotomRightBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        CellStyle _styleBorderAll = workbook.createCellStyle();
        _styleBorderAll.setBorderBottom(CellStyle.BORDER_THIN);
        _styleBorderAll.setBorderRight(CellStyle.BORDER_THIN);
        _styleBorderAll.setBorderLeft(CellStyle.BORDER_THIN);
        _styleBorderAll.setBorderTop(CellStyle.BORDER_THIN);
        _styleBorderAll.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        _styleBorderAll.setRightBorderColor(IndexedColors.BLACK.getIndex());
        _styleBorderAll.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        _styleBorderAll.setTopBorderColor(IndexedColors.BLACK.getIndex());
        _styleBorderAll.setAlignment(CellStyle.ALIGN_CENTER);

        CellStyle stylewhite = workbook.createCellStyle();
        stylewhite.setFillForegroundColor((short) 9);
        stylewhite.setFillPattern(CellStyle.SOLID_FOREGROUND);
        stylewhite.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        stylewhite.setAlignment(CellStyle.ALIGN_CENTER);


        // HSSFColor color1 =  setColor(workbook,(byte) 244, (byte)244,(byte) 244);
        CellStyle _styleSundayMonth = workbook.createCellStyle();
        _styleSundayMonth.setFillPattern(CellStyle.SOLID_FOREGROUND);
        _styleSundayMonth.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        // HSSFColor color1_2 =  setColor(workbook,(byte)153, (byte)223,(byte)255);
        CellStyle _styleDefaultMonthOptimallyAllocatedDay = workbook.createCellStyle();
        _styleDefaultMonthOptimallyAllocatedDay.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        _styleDefaultMonthOptimallyAllocatedDay.setFillPattern(CellStyle.SOLID_FOREGROUND);

        //HSSFColor color1_3 =  setColor(workbook,(byte)210, (byte)237,(byte)166);
        CellStyle _styleDefaultMonthUnderAllocatedDay = workbook.createCellStyle();
        _styleDefaultMonthUnderAllocatedDay.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        _styleDefaultMonthUnderAllocatedDay.setFillPattern(CellStyle.SOLID_FOREGROUND);

        //HSSFColor color1_4 =  setColor(workbook,(byte)244, (byte)204,(byte)204);
        CellStyle _styleDefaultMonthOverAllocatedDay = workbook.createCellStyle();
        _styleDefaultMonthOverAllocatedDay.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        _styleDefaultMonthOverAllocatedDay.setFillPattern(CellStyle.SOLID_FOREGROUND);

        //HSSFColor color1_5 =  setColor(workbook,(byte)255, (byte)102,(byte)0);
        CellStyle _styleWorkCompanyHoliday = workbook.createCellStyle();
        _styleWorkCompanyHoliday = getLegendsStyle(workbook, (short) 52);

        //HSSFColor color1_6 =  setColor(workbook,(byte)110, (byte)166,(byte)77);
        CellStyle _styleWithLrDay = workbook.createCellStyle();
        _styleWithLrDay = getLegendsStyle(workbook, (short) 3);


        styleBorderAll = _styleBorderAll;
        greenColor = greenStyle;
        redColor = redStyle;
        cellStyle = styleAlign;
        rightBorderStyle = styleBorderRight;
        cellWithBottomBorderStyle = cellWithBorderStyle;
        styleBorderBottomRight = styleBorderBotomRight;
        styleSundayMonth = _styleSundayMonth;
        styleDefaultMonthOptimallyAllocatedDay = _styleDefaultMonthOptimallyAllocatedDay;
        styleDefaultMonthUnderAllocatedDay = _styleDefaultMonthUnderAllocatedDay;
        styleDefaultMonthOverAllocatedDay = _styleDefaultMonthOverAllocatedDay;
        styleWorkCompanyHoliday = _styleWorkCompanyHoliday;
        styleWithLrDay = _styleWithLrDay;

        HSSFRow row = genetateOneRowWithEmpityCell(sheet, 0, month, false);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        EdsUser user = userManager.getUser();
        String companyName = "";
        if (user != null) {
            sheet.getRow(0).getCell(0).setCellValue(user.getCompany().getName());
            sheet.getRow(0).getCell(0).setCellStyle(styleEmpName);
        }

        row = genetateOneRowWithEmpityCell(sheet, 1, month, false);

        row = genetateOneRowWithEmpityCell(sheet, 2, month.byteValue(), false);

        sheet.getRow(1).getCell(5).setCellStyle(styleBorderRight);
        sheet.getRow(2).getCell(5).setCellStyle(styleBorderRight);

        sheet.getRow(2).getCell(6).setCellValue("H");
        sheet.getRow(2).getCell(6).setCellStyle(getLegendsStyle(workbook, (short) 52));
        sheet.getRow(2).getCell(7).setCellValue("Holiday");
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 7, 8));

        sheet.getRow(2).getCell(11).setCellStyle(getLegendsStyle(workbook, (short) 3));
        sheet.getRow(2).getCell(11).setCellValue("LR");
        sheet.getRow(2).getCell(12).setCellValue("Leave Request");
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 12, 14));

        sheet.getRow(2).getCell(17).setCellStyle(_styleDefaultMonthOptimallyAllocatedDay);
        sheet.getRow(2).getCell(17).setCellValue("");
        sheet.getRow(2).getCell(18).setCellValue("Optimally");
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 18, 19));

        sheet.getRow(2).getCell(22).setCellStyle(_styleDefaultMonthUnderAllocatedDay);
        sheet.getRow(2).getCell(22).setCellValue("");
        sheet.getRow(2).getCell(23).setCellValue("Under Allocated");
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 23, 25));

        sheet.getRow(2).getCell(28).setCellStyle(_styleDefaultMonthOverAllocatedDay);
        sheet.getRow(2).getCell(28).setCellValue("");
        sheet.getRow(2).getCell(29).setCellValue("Over Allocated");
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 29, 31));

        row = genetateOneRowWithEmpityCell(sheet, 3, month, true);
        sheet.getRow(3).getCell(getNum()).setCellStyle(styleBorderBotomRight);

        row = genetateOneRowWithEmpityCell(sheet, 4, month, true);

        sheet.getRow(4).getCell(0).setCellValue("Employees / Projects");

        sheet.getRow(4).getCell(0).setCellStyle(stylewhite);
        sheet.getRow(4).getCell(getNum()).setCellStyle(styleBorderRight);
        sheet.getRow(4).getCell(getNum() + month + 1).setCellStyle(styleBorderBotomRight);

        row = genetateOneRowWithEmpityCell(sheet, 5, month, true);
        sheet.getRow(5).getCell(getNum()).setCellStyle(styleBorderBotomRight);

        sheet.addMergedRegion(new CellRangeAddress(4, 5, 0, getNum()));
        for (int i = 1; i <= month; i++) {
            sheet.getRow(5).getCell(getNum() + i).setCellValue(i);
            sheet.getRow(5).getCell(getNum() + i).setCellStyle(styleBorderAll);
        }

        Cell total = row.getCell(getNum() + month + 1);
        //total.setCellStyle(styleBorderBotomRightBold);
        total.setCellValue("Total");
        total.setCellStyle(styleEmpName);

        sheet.addMergedRegion(new CellRangeAddress(4, 4, getNum() + 1, (getNum() + month + 1)));
        sheet.getRow(4).getCell(getNum() + 1).setCellValue(currentDate.toUpperCase());
        int rownum = 6;

        LinkedHashMap<String, String> rowsData = excelData.getPrintOrderWithNames();
        LinkedHashMap<String, BigDecimal[][]> empTimesheetAndEstimateData = excelData.getEmployeeSum();
        LinkedHashMap<String, BigDecimal[][]> empLeaveTimeslotAndHolidaysData = excelData.getLeaveTimeslotSum();
        LinkedHashMap<String, BigDecimal[]> projEstimateData = excelData.getProjectSum();
        LinkedHashMap<String, BigDecimal[][]> taskEstimateAndTimesheetData = excelData.getTasksSummary();

        int employeeStartGroup = 0;
        int projectStartGroup = 0;
        boolean firstProject = true;
        for (Map.Entry<String, String> rowData : rowsData.entrySet()) {
            String rowKey = rowData.getKey();
            String rowName = rowData.getValue();

            //writes employee ESTIMATE, TIMESLOT, TIMESHEET and LEAVE HOURS
            if (empTimesheetAndEstimateData.containsKey(rowKey)) {

                firstProject = true;
                if (rownum != 6) {
                    sheet.groupRow(employeeStartGroup, rownum);
                    sheet.setRowGroupCollapsed(employeeStartGroup, true);

                    if (projectStartGroup != 0) {
                        sheet.groupRow(projectStartGroup, rownum);
                        sheet.setRowGroupCollapsed(projectStartGroup, true);
                        projectStartGroup = 0;
                    }
                }
                employeeStartGroup = rownum + 1;
                HSSFRow employeeRow = genetateOneRowWithEmpityCell(sheet, rownum, month, false);
                sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0, getNum()));
                employeeRow.getCell(getNum()).setCellStyle(rightBorderStyle);
                employeeRow.getCell(0).setCellValue(rowName);       //setting employee name
                employeeRow.getCell(0).setCellStyle(styleEmpName);
                rownum++;


                HSSFRow timeslotRow = genetateOneRowWithEmpityCell(sheet, rownum, month, false);
                if (timeSlotHours) {
                    sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0, getNum()));
                    timeslotRow.getCell(getNum()).setCellStyle(rightBorderStyle);
                    timeslotRow.getCell(0).setCellValue("Timeslot Hours");
                    timeslotRow.getCell(0).setCellStyle(timeslotStyle);
                    rownum++;
                }


                HSSFRow timesheetRow = genetateOneRowWithEmpityCell(sheet, rownum, month, false);
                if (timeSheetHours) {
                    sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0, getNum()));
                    timesheetRow.getCell(getNum()).setCellStyle(rightBorderStyle);
                    timesheetRow.getCell(0).setCellValue("Timesheet Summary");
                    timesheetRow.getCell(0).setCellStyle(greenStyle);
                    rownum++;
                }


                HSSFRow leaveRow = genetateOneRowWithEmpityCell(sheet, rownum, month, false);
                if (leaveRequestHours) {
                    sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0, getNum()));
                    leaveRow.getCell(getNum()).setCellStyle(rightBorderStyle);
                    leaveRow.getCell(0).setCellValue("Leave Hours");
                    rownum++;
                }

                BigDecimal[][] timesheetAndEstimate = empTimesheetAndEstimateData.get(rowKey);
                if (timesheetAndEstimate != null) {
                    for (int i = 0; i <= month; i++) {
                        boolean holiday = false, dayoff = false;

                        if (timesheetAndEstimate[4][i] != null && timesheetAndEstimate[4][i].intValue() == -1) {
                            holiday = true;
                        } else if (timesheetAndEstimate[4][i] != null && timesheetAndEstimate[4][i].intValue() == -2) {
                            dayoff = true;
                        }

                        int estimateValue = timesheetAndEstimate[1][i] != null ? timesheetAndEstimate[1][i].intValue() : 0;
                        if (estimateValue > 0) {
                            employeeRow.getCell(getNum() + 1 + i).setCellValue(getTotalHourMinuteWithDoubleFORMAT(estimateValue));
                        }

                        int timeslotValue = timesheetAndEstimate[3][i] != null ? timesheetAndEstimate[3][i].intValue() : 0;
                        if (timeslotValue > 0 && timeSlotHours) {
                            timeslotRow.getCell(getNum() + 1 + i).setCellValue(getTotalHourMinuteWithDoubleFORMAT(timeslotValue));
                            timeslotRow.getCell(getNum() + 1 + i).setCellStyle(timeslotStyle);
                        }

                        int timesheetValue = timesheetAndEstimate[0][i] != null ? timesheetAndEstimate[0][i].intValue() : 0;
                        if (timesheetValue > 0d && timeSheetHours) {
                            timesheetRow.getCell(getNum() + 1 + i).setCellValue(getTotalHourMinuteWithDoubleFORMAT(timesheetValue));
                            timesheetRow.getCell(getNum() + 1 + i).setCellStyle(greenStyle);

                        }

                        int leaveValue = timesheetAndEstimate[2][i] != null ? timesheetAndEstimate[2][i].intValue() : 0;
                        if (!dayoff && leaveValue > 0) {
                            leaveRow.getCell(getNum() + 1 + i).setCellValue(getTotalHourMinuteWithDoubleFORMAT(leaveValue));
                        }

                        //set cell style for under, over and optimally allocated hours

                        if (estimateValue > 0) {
                            if (estimateValue > timeslotValue) {
                                employeeRow.getCell(getNum() + 1 + i).setCellStyle(_styleDefaultMonthOverAllocatedDay);
                            } else if (estimateValue == timeslotValue) {
                                employeeRow.getCell(getNum() + 1 + i).setCellStyle(_styleDefaultMonthOptimallyAllocatedDay);
                            } else if (estimateValue < timeslotValue) {
                                employeeRow.getCell(getNum() + 1 + i).setCellStyle(_styleDefaultMonthUnderAllocatedDay);
                            }
                        }

                        //set cell style for holiday
                        if (holiday) {
                            if (estimateValue == 0) {
                                employeeRow.getCell(getNum() + 1 + i).setCellValue("H");
                                employeeRow.getCell(getNum() + 1 + i).setCellStyle(_styleWorkCompanyHoliday);
                            } else if (estimateValue > timeslotValue) {
                                employeeRow.getCell(getNum() + 1 + i).setCellStyle(_styleDefaultMonthOverAllocatedDay);
                            } else if (estimateValue == timeslotValue) {
                                employeeRow.getCell(getNum() + 1 + i).setCellStyle(_styleDefaultMonthOptimallyAllocatedDay);
                            } else if (estimateValue < timeslotValue) {
                                employeeRow.getCell(getNum() + 1 + i).setCellStyle(_styleDefaultMonthUnderAllocatedDay);
                            }
                            timeslotRow.getCell(getNum() + 1 + i).setCellStyle(_styleWorkCompanyHoliday);
                            if (timeslotValue == 0) {
                                timeslotRow.getCell(getNum() + 1 + i).setCellValue("H");
                            }

                            timesheetRow.getCell(getNum() + 1 + i).setCellStyle(_styleWorkCompanyHoliday);
                            if (timesheetValue == 0) {
                                timesheetRow.getCell(getNum() + 1 + i).setCellValue("H");
                            }
                            leaveRow.getCell(getNum() + 1 + i).setCellStyle(_styleWorkCompanyHoliday);

                            if (leaveValue == 0) {
                                leaveRow.getCell(getNum() + 1 + i).setCellValue("H");
                            }
                        }

                        //set cell style for leave reequest
                        if (!dayoff && leaveValue > 0 && i != month) {
                            if (estimateValue > 0) {
                                employeeRow.getCell(getNum() + 1 + i).setCellValue(getTotalHourMinuteWithDoubleFORMAT(estimateValue));
                            } else {
                                employeeRow.getCell(getNum() + 1 + i).setCellValue("LR");
                            }
                            employeeRow.getCell(getNum() + 1 + i).setCellStyle(_styleWithLrDay);

                            leaveRow.getCell(getNum() + 1 + i).setCellStyle(_styleWithLrDay);
                        }

                        if (dayoff) {
                            if (estimateValue != 0) {
                                if (estimateValue > timeslotValue) {
                                    employeeRow.getCell(getNum() + 1 + i).setCellStyle(_styleDefaultMonthOverAllocatedDay);
                                } else if (estimateValue == timeslotValue) {
                                    employeeRow.getCell(getNum() + 1 + i).setCellStyle(_styleDefaultMonthOptimallyAllocatedDay);
                                } else if (estimateValue < timeslotValue) {
                                    employeeRow.getCell(getNum() + 1 + i).setCellStyle(_styleDefaultMonthUnderAllocatedDay);
                                }
                            } else {
                                employeeRow.getCell(getNum() + 1 + i).setCellStyle(styleSundayMonth);
                            }
                            timeslotRow.getCell(getNum() + 1 + i).setCellStyle(styleSundayMonth);
                            timesheetRow.getCell(getNum() + 1 + i).setCellStyle(styleSundayMonth);
                            leaveRow.getCell(getNum() + 1 + i).setCellStyle(styleSundayMonth);
                        }

                        //style for tolal values
                        if (i == month) {
                            employeeRow.getCell(getNum() + 1 + i).setCellStyle(styleEmpName);
                            timeslotRow.getCell(getNum() + 1 + i).setCellStyle(styleEmpName);
                            timesheetRow.getCell(getNum() + 1 + i).setCellStyle(styleEmpName);
                            leaveRow.getCell(getNum() + 1 + i).setCellStyle(styleEmpName);
                        }
                    }
                }

            } else if (projEstimateData.containsKey(rowKey) && !rowKey.contains("||null")) {

                BigDecimal[][] timesheetAnnEstimate = empTimesheetAndEstimateData.get(rowKey);
                if (!firstProject) {
                    sheet.groupRow(projectStartGroup, rownum);
                    sheet.setRowGroupCollapsed(projectStartGroup, true);
                }
                firstProject = false;
                projectStartGroup = rownum + 1;
                HSSFRow projectRow = genetateOneRowWithEmpityCell(sheet, rownum, month, false);
                sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0, getNum()));
                projectRow.getCell(getNum()).setCellStyle(rightBorderStyle);
                projectRow.getCell(0).setCellValue(rowName);    //setting project name
                projectRow.getCell(0).setCellStyle(styleName);


                BigDecimal[] projectEstimate = projEstimateData.get(rowKey);
                if (projectEstimate != null) {
                    for (int i = 0; i <= (month); i++) {
                        int estimateValue = projectEstimate[i] != null ? projectEstimate[i].intValue() : 0;
                        if (estimateValue > 0) {
                            projectRow.getCell(getNum() + 1 + i).setCellValue(getTotalHourMinuteWithDoubleFORMAT(estimateValue));
                        }
                        if (i != month) {
                            projectRow.getCell(getNum() + 1 + i).setCellStyle(styleSundayMonth);
                        } else {
                            projectRow.getCell(getNum() + 1 + i).setCellStyle(styleEmpName);
                        }
                    }
                    rownum++;
                }


                //Task estimate and timesheet
            } else if (taskEstimateAndTimesheetData.containsKey(rowKey) && !rowKey.contains("||null||null")) {
                HSSFRow estimateRow = genetateOneRowWithEmpityCell(sheet, rownum, month, false);
                sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0, getNum()));
                estimateRow.getCell(getNum()).setCellStyle(rightBorderStyle);
                estimateRow.getCell(0).setCellValue(rowName);    //Task name
                estimateRow.getCell(0).setCellStyle(timeslotStyle);
                rownum++;

                HSSFRow timesheetRow = genetateOneRowWithEmpityCell(sheet, rownum, month, false);
                if (timeSheetHours) {
                    sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0, getNum()));
                    timesheetRow.getCell(getNum()).setCellStyle(rightBorderStyle);
                    timesheetRow.getCell(0).setCellValue("Timesheet Hours");
                    timesheetRow.getCell(0).setCellStyle(greenStyle);
                    rownum++;
                }

                BigDecimal[][] taskEstimateTimesheet = taskEstimateAndTimesheetData.get(rowKey);
                if (taskEstimateTimesheet != null) {
                    for (int i = 0; i <= (month); i++) {
                        int estimateValue = taskEstimateTimesheet[1][i] != null ? taskEstimateTimesheet[1][i].intValue() : 0;
                        if (estimateValue > 0) {
                            estimateRow.getCell(getNum() + 1 + i).setCellValue(getTotalHourMinuteWithDoubleFORMAT(estimateValue));
                        }
                        if (estimateValue == -2) {
                            estimateRow.getCell(getNum() + 1 + i).setCellStyle(styleSundayMonth);
                        }

                        if (estimateValue == -2 && timeSheetHours) {
                            timesheetRow.getCell(getNum() + 1 + i).setCellStyle(styleSundayMonth);
                        }

                        if (i == month) {
                            estimateRow.getCell(getNum() + 1 + i).setCellStyle(styleEmpName);
                            timesheetRow.getCell(getNum() + 1 + i).setCellStyle(styleEmpName);
                        }

                        if (timeSheetHours) {
                            int timesheetValue = taskEstimateTimesheet[0][i] != null ? taskEstimateTimesheet[0][i].intValue() : 0;
                            if (timesheetValue > 0) {
                                timesheetRow.getCell(getNum() + 1 + i).setCellValue(getTotalHourMinuteWithDoubleFORMAT(timesheetValue));
                            }
                        }
                    }
                }
            }
        }
        sheet.groupRow(employeeStartGroup, rownum);
        sheet.setRowGroupCollapsed(employeeStartGroup, true);
        if (projectStartGroup != 0) {
            sheet.groupRow(projectStartGroup, rownum);
            sheet.setRowGroupCollapsed(projectStartGroup, true);
            projectStartGroup = 0;
        }
    }

    private void generateEmployeeTasksReportTD(TaskItem taskRUItem, HSSFRow taskItemTR, HSSFRow taskTimeSheetHoursTR, int[] monthHoliday, int currentMonth) {
        int totalTimeSheetHoursINT = 0;
        int totalTaskAllocatedHoursINT = 0;

        for (int i = 1; i <= currentMonth; i++) {
            //register timeSheet hours
            Cell timeSheetHoursTD = null;
            if (taskTimeSheetHoursTR != null) {
                timeSheetHoursTD = taskTimeSheetHoursTR.getCell(getNum() + i);
            }
            //register daily employee task total hours
            Cell taskAllocatedHoursTD = taskItemTR.getCell(getNum() + i);

            if (monthHoliday[i] == 1) {//SUNDAY
                //task time sheet hours--------------------------
                if (timeSheetHoursTD != null) {
                    if (taskRUItem.getTotalTimeSheetHours()[i] == 0) {
                        timeSheetHoursTD.setCellValue("");
                    } else {
                        totalTimeSheetHoursINT += taskRUItem.getTotalTimeSheetHours()[i];
                        timeSheetHoursTD = generateCellValue(timeSheetHoursTD, taskRUItem.getTotalTimeSheetHours()[i]);
                    }
                }
                //task time spent hours----------------------------
                if (taskRUItem.getTotalEstimatedTime()[i] == 0) {
                    taskAllocatedHoursTD.setCellValue("");
                } else {
                    totalTaskAllocatedHoursINT += taskRUItem.getTotalEstimatedTime()[i];
                    taskAllocatedHoursTD = generateCellValue(taskAllocatedHoursTD, taskRUItem.getTotalEstimatedTime()[i]);
                }
                //-------------------------------------------------
            } else {
                if (monthHoliday[i] == 2) {//DAY OFF
                    //task time sheet hours--------------------------
                    if (timeSheetHoursTD != null) {
                        if (taskRUItem.getTotalTimeSheetHours()[i] == 0) {
                            timeSheetHoursTD.setCellValue("");
                        } else {
                            totalTimeSheetHoursINT += taskRUItem.getTotalTimeSheetHours()[i];
                            timeSheetHoursTD = generateCellValue(timeSheetHoursTD, taskRUItem.getTotalTimeSheetHours()[i]);
                        }
                    }
                    //task time spent hours----------------------------
                    if (taskRUItem.getTotalEstimatedTime()[i] == 0) {
                        taskAllocatedHoursTD.setCellValue("");
                    } else {
                        totalTaskAllocatedHoursINT += taskRUItem.getTotalEstimatedTime()[i];
                        taskAllocatedHoursTD = generateCellValue(taskAllocatedHoursTD, taskRUItem.getTotalEstimatedTime()[i]);
                    }
                } else {
                    if (monthHoliday[i] == 3) {//HOLIDAY
                        //task time sheet hours--------------------------
                        if (timeSheetHoursTD != null) {
                            if (taskRUItem.getTotalTimeSheetHours()[i] == 0) {
                                timeSheetHoursTD.setCellValue("");
                            } else {
                                totalTimeSheetHoursINT += taskRUItem.getTotalTimeSheetHours()[i];
                                timeSheetHoursTD = generateCellValue(timeSheetHoursTD, taskRUItem.getTotalTimeSheetHours()[i]);
                            }
                        }
                        //task time spent hours----------------------------
                        if (taskRUItem.getTotalEstimatedTime()[i] == 0) {
                            taskAllocatedHoursTD.setCellValue("");
                        } else {
                            totalTaskAllocatedHoursINT += taskRUItem.getTotalEstimatedTime()[i];
                            taskAllocatedHoursTD = generateCellValue(taskAllocatedHoursTD, taskRUItem.getTotalEstimatedTime()[i]);
                        }
                    } else {
                        //task time sheet hours----------------------------
                        if (timeSheetHoursTD != null) {
                            if (taskRUItem.getTotalTimeSheetHours()[i] == 0) {
                                timeSheetHoursTD.setCellValue("");
                            } else {
                                totalTimeSheetHoursINT += taskRUItem.getTotalTimeSheetHours()[i];
                                timeSheetHoursTD = generateCellValue(timeSheetHoursTD, taskRUItem.getTotalTimeSheetHours()[i]);
                            }
                        }
                        //task time spent hours----------------------------
                        if (taskRUItem.getTotalEstimatedTime()[i] == 0) {
                            taskAllocatedHoursTD.setCellValue("");
                        } else {
                            totalTaskAllocatedHoursINT += taskRUItem.getTotalEstimatedTime()[i];
                            taskAllocatedHoursTD = generateCellValue(taskAllocatedHoursTD, taskRUItem.getTotalEstimatedTime()[i]);
                        }
                    }
                }
            }
        }
        //total timeSheet hours TD element
        if (taskTimeSheetHoursTR != null) {
            Cell totalTimeSheetHoursTD = taskTimeSheetHoursTR.getCell(getNum() + currentMonth + 1);
            totalTimeSheetHoursTD = generateCellValue(totalTimeSheetHoursTD, totalTimeSheetHoursINT);
        }
        //total task allocated hours TD element
        Cell totalTaskAllocatedHoursTD = taskItemTR.getCell(getNum() + currentMonth + 1);
        totalTaskAllocatedHoursTD = generateCellValue(totalTaskAllocatedHoursTD, totalTaskAllocatedHoursINT);
    }

    private void generateEmployeeProjectsReportTD(ProjectTaskItem projectRUItem, HSSFRow projectNameElementTR, int[] monthHoliday, int currentMonth) {
        int totalProjectAllocatedHourINT = 0;
        for (int i = 1; i <= currentMonth; i++) {
            //register daily employee project total hours
            Cell projectAllocatedHoursTD = projectNameElementTR.getCell(i + getNum());

            if (monthHoliday[i] == 1) {//SUNDAY
                if (projectRUItem.getTotalEstimatedTime()[i] == 0) {
                    projectAllocatedHoursTD.setCellValue("");
                } else {
                    totalProjectAllocatedHourINT += projectRUItem.getTotalEstimatedTime()[i];
                    projectAllocatedHoursTD = generateCellValue(projectAllocatedHoursTD, projectRUItem.getTotalEstimatedTime()[i]);
                }
            } else {
                if (monthHoliday[i] == 2) {//DAY OFF
                    if (projectRUItem.getTotalEstimatedTime()[i] == 0) {
                        projectAllocatedHoursTD.setCellValue("");
                    } else {
                        totalProjectAllocatedHourINT += projectRUItem.getTotalEstimatedTime()[i];
                        projectAllocatedHoursTD = generateCellValue(projectAllocatedHoursTD, projectRUItem.getTotalEstimatedTime()[i]);
                    }
                } else {
                    if (monthHoliday[i] == 3) {//HOLIDAY
                        if (projectRUItem.getTotalEstimatedTime()[i] == 0) {
                            projectAllocatedHoursTD.setCellValue("");
                        } else {
                            totalProjectAllocatedHourINT += projectRUItem.getTotalEstimatedTime()[i];
                            projectAllocatedHoursTD = generateCellValue(projectAllocatedHoursTD, projectRUItem.getTotalEstimatedTime()[i]);
                        }
                    } else {
                        if (projectRUItem.getTotalEstimatedTime()[i] == 0) {
                            projectAllocatedHoursTD.setCellValue("");
                        } else {
                            totalProjectAllocatedHourINT += projectRUItem.getTotalEstimatedTime()[i];
                            projectAllocatedHoursTD = generateCellValue(projectAllocatedHoursTD, projectRUItem.getTotalEstimatedTime()[i]);
                        }
                    }
                }
            }
        }
        //total project allocated hours TD element
        Cell totalProjectAllocatedHoursTD = projectNameElementTR.getCell(getNum() + currentMonth + 1);
        totalProjectAllocatedHoursTD = generateCellValue(totalProjectAllocatedHoursTD, totalProjectAllocatedHourINT);
    }


    private void generateEmployeeReportTD(EmployeeResourceUtilItem employeeRUItem, HSSFRow employeeRow, HSSFRow employeeTimeSlotHoursTR, HSSFRow employeeOverAllTimeSheetHoursTR, HSSFRow lrHoursAndHolidayDaysTR, int[] monthHoliday, int currentMonth) {
        int totalTimeSlotHoursINT = 0;
        int totalOverAllTimeSheetHoursINT = 0;
        int totalLR_HoursAndHolidayDaysINT = 0;
        int totalEmployeeAllocatedHoursINT = 0;
        for (int i = 1; i <= currentMonth; i++) {
            Cell timSlotHoursTD = null;
            if (employeeTimeSlotHoursTR != null) {
                timSlotHoursTD = employeeTimeSlotHoursTR.getCell(getNum() + i);
            }
            Cell overAllTimeSheetHoursTD = null;
            if (employeeOverAllTimeSheetHoursTR != null) {
                overAllTimeSheetHoursTD = employeeOverAllTimeSheetHoursTR.getCell(getNum() + i);
            }
            Cell LR_HoursAndHolidayDaysTD = lrHoursAndHolidayDaysTR.getCell(getNum() + i);
            Cell employeeAllocatedHoursTD = employeeRow.getCell(getNum() + i);

            if (monthHoliday[i] == 1) {//SUNDAY
                //time slot hours------------------------------------------------------
                if (timSlotHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                        timSlotHoursTD.setCellValue("");
                    } else {
                        totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                        timSlotHoursTD = generateCellValue(timSlotHoursTD, employeeRUItem.getTotalTimeSlotHours()[i]);
                    }
                }
                //time sheet hours (overall)------------------------------------------
                if (overAllTimeSheetHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSheetHours()[i] == 0) {
                        overAllTimeSheetHoursTD.setCellValue("");
                    } else {
                        totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                        overAllTimeSheetHoursTD = generateCellValue(overAllTimeSheetHoursTD, employeeRUItem.getTotalTimeSheetHours()[i]);
                    }
                }

                //employee allocated hours--------------------------------------------
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    employeeAllocatedHoursTD.setCellValue("");
                } else {
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    employeeAllocatedHoursTD = generateCellValue(employeeAllocatedHoursTD, employeeRUItem.getTotalHours()[i]);
                }
                //-------------------------------------------------------------------
            } else if (monthHoliday[i] == 2) {//DAY OFF
                //time slot hours----------------------------------------------------
                if (timSlotHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                        timSlotHoursTD.setCellValue("");
                    } else {
                        totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                        timSlotHoursTD = generateCellValue(timSlotHoursTD, employeeRUItem.getTotalTimeSlotHours()[i]);
                    }
                }
                //time sheet hours (overall)----------------------------------------
                if (overAllTimeSheetHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSheetHours()[i] == 0) {
                        overAllTimeSheetHoursTD.setCellValue("");
                    } else {
                        totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                        overAllTimeSheetHoursTD = generateCellValue(overAllTimeSheetHoursTD, employeeRUItem.getTotalTimeSheetHours()[i]);
                    }
                }
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    employeeAllocatedHoursTD.setCellValue("");
                } else {
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    employeeAllocatedHoursTD = generateCellValue(employeeAllocatedHoursTD, employeeRUItem.getTotalHours()[i]);
                }
                //-----------------------------------------------------------------
            } else if (monthHoliday[i] == 3) {//HOLIDAY
                //time slot hours----------------------------------------------------
                if (timSlotHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                        timSlotHoursTD.setCellStyle(styleWorkCompanyHoliday);
                        timSlotHoursTD.setCellValue("H");
                    } else {
                        timSlotHoursTD.setCellStyle(styleWorkCompanyHoliday);
                        totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                        timSlotHoursTD = generateCellValue(timSlotHoursTD, employeeRUItem.getTotalTimeSlotHours()[i]);
                    }
                }
                //time sheet hours (overall)----------------------------------------
                if (overAllTimeSheetHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSheetHours()[i] == 0) {
                        overAllTimeSheetHoursTD.setCellStyle(styleWorkCompanyHoliday);
                        overAllTimeSheetHoursTD.setCellValue("H");
                    } else {
                        overAllTimeSheetHoursTD.setCellStyle(styleWorkCompanyHoliday);
                        totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                        overAllTimeSheetHoursTD = generateCellValue(overAllTimeSheetHoursTD, employeeRUItem.getTotalTimeSheetHours()[i]);
                    }
                }
                //Holiday days-------------------------------------------------------
                LR_HoursAndHolidayDaysTD.setCellValue("H");
                LR_HoursAndHolidayDaysTD.setCellStyle(styleWorkCompanyHoliday);
                //LR_HoursAndHolidayDaysTD.setCellStyle(redColor);

                //employee allocated hours-------------------------------------------
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    employeeAllocatedHoursTD.setCellStyle(styleWorkCompanyHoliday);
                    employeeAllocatedHoursTD.setCellValue("H");
                } else {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == employeeRUItem.getTotalHours()[i]) {//optimally allocated day
                        employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthOptimallyAllocatedDay);
                    } else {
                        if (employeeRUItem.getTotalTimeSlotHours()[i] > employeeRUItem.getTotalHours()[i]) {//under allocated day
                            employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthUnderAllocatedDay);
                        } else {
                            if (employeeRUItem.getTotalTimeSlotHours()[i] < employeeRUItem.getTotalHours()[i]) {//over allocated day
                                employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthOverAllocatedDay);
                            } else {
                                employeeAllocatedHoursTD.setCellStyle(cellStyle);
                            }
                        }
                    }
                    employeeAllocatedHoursTD.setCellStyle(styleWorkCompanyHoliday);
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    employeeAllocatedHoursTD = generateCellValue(employeeAllocatedHoursTD, employeeRUItem.getTotalHours()[i]);
                }
                //-------------------------------------------------------------------
            } else if (employeeRUItem.getWithHoliday_INT()[i] == 1) {
                //time slot hours----------------------------------------------------
                if (timSlotHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                        timSlotHoursTD.setCellStyle(styleWorkCompanyHoliday);
                        timSlotHoursTD.setCellValue("H");
                    } else {
                        timSlotHoursTD.setCellStyle(styleWorkCompanyHoliday);
                        totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                        timSlotHoursTD = generateCellValue(timSlotHoursTD, employeeRUItem.getTotalTimeSlotHours()[i]);
                    }
                }
                //time sheet hours (overall)----------------------------------------
                if (overAllTimeSheetHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSheetHours()[i] == 0) {
                        overAllTimeSheetHoursTD.setCellStyle(styleWorkCompanyHoliday);
                        overAllTimeSheetHoursTD.setCellValue("");
                    } else {
                        overAllTimeSheetHoursTD.setCellStyle(styleWorkCompanyHoliday);
                        totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                        overAllTimeSheetHoursTD = generateCellValue(overAllTimeSheetHoursTD, employeeRUItem.getTotalTimeSheetHours()[i]);
                    }
                }
                //Holiday days -----------------------------------------------------------
                LR_HoursAndHolidayDaysTD.setCellValue("H");
                LR_HoursAndHolidayDaysTD.setCellStyle(styleWorkCompanyHoliday);

                //employee allocated hours-------------------------------------------------
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    employeeAllocatedHoursTD.setCellStyle(styleWorkCompanyHoliday);
                    employeeAllocatedHoursTD.setCellValue("H");
                } else {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == employeeRUItem.getTotalHours()[i]) {//optimally allocated day
                        employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthOptimallyAllocatedDay);
                    } else {
                        if (employeeRUItem.getTotalTimeSlotHours()[i] > employeeRUItem.getTotalHours()[i]) {//under allocated day
                            employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthUnderAllocatedDay);
                        } else {
                            if (employeeRUItem.getTotalTimeSlotHours()[i] < employeeRUItem.getTotalHours()[i]) {//over allocated day
                                employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthOverAllocatedDay);
                            } else {
                                employeeAllocatedHoursTD.setCellStyle(cellStyle);
                            }
                        }
                    }
                    employeeAllocatedHoursTD.setCellStyle(styleWorkCompanyHoliday);
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    employeeAllocatedHoursTD = generateCellValue(employeeAllocatedHoursTD, employeeRUItem.getTotalHours()[i]);
                }
                //-------------------------------------------------------------------
            } else if (employeeRUItem.getWith_LR_INT()[i] > 0) {
                //time slot hours----------------------------------------------------
                if (timSlotHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                        //white back
                        timSlotHoursTD.setCellValue("LR");
                    } else {
                        //white back
                        totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                        timSlotHoursTD = generateCellValue(timSlotHoursTD, employeeRUItem.getTotalTimeSlotHours()[i]);
                    }
                }
                //time sheet hours (overall)-----------------------------------------
                if (overAllTimeSheetHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSheetHours()[i] == 0) {
                        //white back
                        overAllTimeSheetHoursTD.setCellValue("LR");
                    } else {
                        //white back
                        totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                        overAllTimeSheetHoursTD = generateCellValue(overAllTimeSheetHoursTD, employeeRUItem.getTotalTimeSheetHours()[i]);
                    }
                }
                //LR hours-----------------------------------------------------------
                LR_HoursAndHolidayDaysTD.setCellStyle(styleWithLrDay);
                totalLR_HoursAndHolidayDaysINT += employeeRUItem.getWith_LR_INT()[i];
                LR_HoursAndHolidayDaysTD = generateCellValue(LR_HoursAndHolidayDaysTD, employeeRUItem.getWith_LR_INT()[i]);
                if (employeeRUItem.getWith_LR_INT()[i] == employeeRUItem.getTotalTimeSlotHours()[i]) {
                    LR_HoursAndHolidayDaysTD.setCellValue("LR");
                    LR_HoursAndHolidayDaysTD.setCellStyle(greenColor);
                }
                //employee allocated hours-------------------------------------------------
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    employeeAllocatedHoursTD.setCellValue("LR");
                    employeeAllocatedHoursTD.setCellStyle(styleWithLrDay);
                } else {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == employeeRUItem.getTotalHours()[i]) {//optimally allocated day
                        employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthOptimallyAllocatedDay);
                    } else {
                        if (employeeRUItem.getTotalTimeSlotHours()[i] > employeeRUItem.getTotalHours()[i]) {//under allocated day
                            employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthUnderAllocatedDay);
                        } else {
                            if (employeeRUItem.getTotalTimeSlotHours()[i] < employeeRUItem.getTotalHours()[i]) {//over allocated day
                                employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthOverAllocatedDay);
                            } else {
                                employeeAllocatedHoursTD.setCellStyle(cellStyle);
                            }
                        }
                    }
                    employeeAllocatedHoursTD.setCellStyle(styleWithLrDay);
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    employeeAllocatedHoursTD = generateCellValue(employeeAllocatedHoursTD, employeeRUItem.getTotalHours()[i]);
                }
                //-------------------------------------------------------------------
            } else {
                //time slot hours----------------------------------------------------
                if (timSlotHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                        timSlotHoursTD.setCellValue("");
                    } else {
                        totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                        timSlotHoursTD = generateCellValue(timSlotHoursTD, employeeRUItem.getTotalTimeSlotHours()[i]);
                    }
                }
                //time sheet hours (overall)----------------------------------------
                if (overAllTimeSheetHoursTD != null) {
                    if (employeeRUItem.getTotalTimeSheetHours()[i] == 0) {
                        overAllTimeSheetHoursTD.setCellValue("");
                    } else {
                        totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                        overAllTimeSheetHoursTD = generateCellValue(overAllTimeSheetHoursTD, employeeRUItem.getTotalTimeSheetHours()[i]);
                    }
                }
                //employee allocated hours-------------------------------------------
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    employeeAllocatedHoursTD.setCellValue("");
                } else {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == employeeRUItem.getTotalHours()[i]) {//optimally allocated day
                        employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthOptimallyAllocatedDay);
                    } else {
                        if (employeeRUItem.getTotalTimeSlotHours()[i] > employeeRUItem.getTotalHours()[i]) {//under allocated day
                            employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthUnderAllocatedDay);
                        } else {
                            if (employeeRUItem.getTotalTimeSlotHours()[i] < employeeRUItem.getTotalHours()[i]) {//over allocated day
                                employeeAllocatedHoursTD.setCellStyle(styleDefaultMonthOverAllocatedDay);
                            } else {
                                employeeAllocatedHoursTD.setCellStyle(cellStyle);
                            }
                        }
                    }
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    employeeAllocatedHoursTD = generateCellValue(employeeAllocatedHoursTD, employeeRUItem.getTotalHours()[i]);
                }
                //-----------------------------------------------------------------
            }
        }
        //total timeSlot hours TD element
        if (employeeTimeSlotHoursTR != null) {
            Cell totalTimeSlotHoursTD = employeeTimeSlotHoursTR.getCell(getNum() + currentMonth + 1);
            totalTimeSlotHoursTD = generateCellValue(totalTimeSlotHoursTD, totalTimeSlotHoursINT);
        }

        //total overall timeSheet hours TD element
        if (employeeOverAllTimeSheetHoursTR != null) {
            Cell totalOverAllTimeSheetHoursTD = employeeOverAllTimeSheetHoursTR.getCell(getNum() + currentMonth + 1);
            totalOverAllTimeSheetHoursTD = generateCellValue(totalOverAllTimeSheetHoursTD, totalOverAllTimeSheetHoursINT);
        }

        //total LR hours and holiday days TD element
        Cell totalLR_HoursAndHolidayDaysTD = lrHoursAndHolidayDaysTR.getCell(getNum() + currentMonth + 1);
        totalLR_HoursAndHolidayDaysTD = generateCellValue(totalLR_HoursAndHolidayDaysTD, totalLR_HoursAndHolidayDaysINT);

        //total employee allocated hours TD element
        Cell totalEmployeeAllocatedHoursTD = employeeRow.getCell(getNum() + currentMonth + 1);
        totalEmployeeAllocatedHoursTD = generateCellValue(totalEmployeeAllocatedHoursTD, totalEmployeeAllocatedHoursINT);

    }

    private CellStyle getLegendsStyle(HSSFWorkbook workBook, short color) {
        CellStyle style = workBook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
        return style;
    }

    public HSSFColor setColor(HSSFWorkbook workbook, byte r, byte g, byte b) {
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor hssfColor = null;
        try {
            hssfColor = palette.findColor(r, g, b);
            if (hssfColor == null) {
                palette.setColorAtIndex(HSSFColor.LAVENDER.index, r, g, b);
                hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
            }
        } catch (Exception e) {

        }
        return hssfColor;
    }

    private Cell createCell(HSSFRow row, int columnIndex, boolean withBorder) {
        return createCell(row, columnIndex, withBorder, null);
    }

    private Cell createCell(HSSFRow row, int columnIndex, boolean withBorder, Integer rowNumber) {
        Cell cell = row.createCell(columnIndex);
        if (columnIndex > 0) {
            if (columnIndex == getNum()) {
                cell.setCellStyle(rightBorderStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
        }
        if (withBorder) {
            if (cellWithBottomBorderStyle != null) {
                cell.setCellStyle(cellWithBottomBorderStyle);
            }
        }
        return cell;
    }

    private String getTotalHourMinuteWithTimeFORMAT(int totalTime) {
        if (totalTime > 0) {
            String hour = (totalTime / 60) > 9 ? ((totalTime / 60) + "") : ("0" + (totalTime / 60));
            String minute = (totalTime % 60) > 9 ? ((totalTime % 60) + "") : ("0" + (totalTime % 60));
            return hour + ":" + minute;
        }
        return "";
    }

    private Cell generateCellValue(Cell cell, int totalTime) {
        if (isEnableLongTimeFormatResourceUtilReportTable()) {
            cell.setCellValue(getTotalHourMinuteWithTimeFORMAT(totalTime));
        } else {
            Double d = getTotalHourMinuteWithDoubleFORMAT(totalTime);
            if (d > 0) {
                cell.setCellValue(d);
            } else {
                cell.setCellValue("");
            }
        }
        return cell;
    }

    private Double getTotalHourMinuteWithDoubleFORMAT(int totalTime) {
        if (totalTime > 0) {
            String hour = "" + (totalTime / 60);
            String minute = getMinuteShortFormat(totalTime % 60);
            return Double.parseDouble(hour + "." + minute);
        }
        return 0.0;
    }

    private boolean isEnableLongTimeFormatResourceUtilReportTable() {
        if (isEnableLongTimeFormatResourceUtilReportTable == null) {
            isEnableLongTimeFormatResourceUtilReportTable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_LONG_TIME_FORMAT_RESOURCE_UTIL_REPORT_TABLE);
        }
        return isEnableLongTimeFormatResourceUtilReportTable;
    }

    private String getMinuteShortFormat(int minute) {
        DecimalFormat myFormatter = new DecimalFormat(",##0");
        return myFormatter.format((minute * 10L) / 60);
    }
}
