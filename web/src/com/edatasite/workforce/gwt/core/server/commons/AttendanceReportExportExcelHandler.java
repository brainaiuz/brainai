package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 23.09.2009
 * Time: 18:59:38
 * To change this template use File | Settings | File Templates.
 */
public class AttendanceReportExportExcelHandler extends BaseExcelHandler {

    private static final DateFormat format = new SimpleDateFormat("MMMM dd,yyyy", Locale.UK);
    private static final DateFormat format1 = new SimpleDateFormat("MMMM", Locale.UK);

    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private DepartmentManager departmentManager;

    protected HSSFWorkbook getWorkBook(Object object) {
        EmployeeAttendanceReport attendanceReport = null;
        ListingFilterParameter fp = ((ListingFilterParameter) object);
        int maxMonthDay = 0;
        int currentDay = fp.getDay();

        HSSFWorkbook workBook = null;
        HSSFSheet sheet = null;
        List<ExcelData[]> list = new LinkedList<>();
        try {
            maxMonthDay = fp.getParams() != null && !"".equals(fp.getParams()) ? Integer.parseInt(fp.getParams()) : -1;
            if (fp.getStartDateNC() != null && fp.getEndDateNC() != null && maxMonthDay != -1) {
                attendanceReport = availabilityService.getEmployeeAttendanceReport(fp, maxMonthDay);
                ExcelData[] excelData = new ExcelData[maxMonthDay + 7];
                for (int i = 0; i < (maxMonthDay + 7); i++) {
                    excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                }

                list.add(excelData);
                list.add(excelData);
                list.add(excelData);

                excelData = new ExcelData[maxMonthDay + 7];
                excelData[0] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);
                excelData[0].setFontColor(0);
                excelData[0].setFontSize(12);
                for (int i = 1; i < (maxMonthDay + 7); i++) {
                    excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);
                    excelData[i].setFontSize((short) 16);
                    excelData[i].setFontColor(0);
                }

                list.add(excelData);
                list.add(excelData);
                list.add(excelData);
                // Days
                excelData = new ExcelData[maxMonthDay + 7];
                excelData[0] = getExcelDataHeader(" " + commonLocalizer.localize(PdfLocalizationName.days), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);

                int[] monthHoliday = attendanceReport.getMonthHoliday();
                for (int i = 1; i <= maxMonthDay; i++) {
                    if (currentDay == i) {
                        excelData[i] = getExcelDataHeader(String.valueOf(i), ExcelData.STRING, HSSFColor.WHITE.index, HSSFColor.DARK_BLUE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else if (monthHoliday[i] == 1) {
                        excelData[i] = getExcelDataHeader(String.valueOf(i), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_GREEN.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else if (monthHoliday[i] == 2) {
                        excelData[i] = getExcelDataHeader(String.valueOf(i), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_BLUE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else {
                        excelData[i] = getExcelDataHeader(String.valueOf(i), ExcelData.STRING, HSSFColor.WHITE.index, HSSFColor.BLUE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    }
                }

                for (int i = maxMonthDay + 1; i < (maxMonthDay + 7); i++) {
                    excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                }
                list.add(excelData);

                //total absent
                excelData = new ExcelData[maxMonthDay + 7];
                excelData[0] = getExcelDataHeader(" " + commonLocalizer.localize(PdfLocalizationName.totalAbsent), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);

                int[] totalabsent = attendanceReport.getTotalAbsent();
                for (int i = 1; i <= maxMonthDay; i++) {
                    if (monthHoliday[i] == 1) {
                        excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_GREEN.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else if (monthHoliday[i] == 2) {
                        excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_BLUE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else if (totalabsent[i] != 0) {
                        excelData[i] = getExcelDataHeader(String.valueOf(totalabsent[i]), ExcelData.STRING, HSSFColor.WHITE.index, HSSFColor.AQUA.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else {
                        excelData[i] = getExcelDataHeader("0", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    }
                }

                for (int i = maxMonthDay + 1; i < (maxMonthDay + 7); i++) {
                    excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                }
                list.add(excelData);

                // Without LR
                excelData = new ExcelData[maxMonthDay + 7];
                excelData[0] = getExcelDataHeader(" " + commonLocalizer.localize(PdfLocalizationName.withoutLR), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);

                int[] withoutlr = attendanceReport.getWithoutLR();
                for (int i = 1; i <= maxMonthDay; i++) {
                    if (monthHoliday[i] == 1) {
                        excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_GREEN.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else if (monthHoliday[i] == 2) {
                        excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_BLUE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else if (withoutlr[i] != 0) {
                        excelData[i] = getExcelDataHeader(String.valueOf(withoutlr[i]), ExcelData.STRING, HSSFColor.YELLOW.index, HSSFColor.RED.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else {
                        excelData[i] = getExcelDataHeader("0", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    }
                }

                for (int i = maxMonthDay + 1; i < (maxMonthDay + 7); i++) {
                    excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                }
                list.add(excelData);

                // withlr
                excelData = new ExcelData[maxMonthDay + 7];
                excelData[0] = getExcelDataHeader(" " + commonLocalizer.localize(PdfLocalizationName.withLR), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);

                int[] withlr = attendanceReport.getWithLR();
                for (int i = 1; i <= maxMonthDay; i++) {
                    if (monthHoliday[i] == 1) {
                        excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_GREEN.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else if (monthHoliday[i] == 2) {
                        excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_BLUE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else if (withlr[i] != 0) {
                        excelData[i] = getExcelDataHeader(String.valueOf(withlr[i]), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREEN.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else {
                        excelData[i] = getExcelDataHeader("0", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    }
                }

                for (int i = maxMonthDay + 1; i < (maxMonthDay + 7); i++) {
                    excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                }
                list.add(excelData);

                // Waiting For Approval
                excelData = new ExcelData[maxMonthDay + 7];
                excelData[0] = getExcelDataHeader(" " + commonLocalizer.localize(PdfLocalizationName.waitingForApproval), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);

                int[] waitingForApproval = attendanceReport.getWaitingForAppRoval();
                for (int i = 1; i <= maxMonthDay; i++) {
                    if (monthHoliday[i] == 1) {
                        excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_GREEN.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else if (monthHoliday[i] == 2) {
                        excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_BLUE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else if (waitingForApproval[i] != 0) {
                        excelData[i] = getExcelDataHeader(String.valueOf(waitingForApproval[i]), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.YELLOW.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else {
                        excelData[i] = getExcelDataHeader("0", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    }
                }
                for (int i = maxMonthDay + 1; i < (maxMonthDay + 7); i++) {
                    excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                }
                list.add(excelData);
                // space
                excelData = new ExcelData[maxMonthDay + 7];
                for (int i = 0; i < (maxMonthDay + 7); i++) {
                    excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                }
                list.add(excelData);

                // employee title
                excelData = new ExcelData[maxMonthDay + 7];
                excelData[0] = getExcelDataHeader(" " + commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);

                for (int i = 1; i <= maxMonthDay; i++) {
                    if (monthHoliday[i] == 1) {
                        excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_GREEN.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else if (monthHoliday[i] == 2) {
                        excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_BLUE.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else {
                        excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
                        excelData[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    }
                }

                excelData[maxMonthDay + 1] = getExcelDataHeader(commonLocalizer.localize(PdfLocalizationName.inHours), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
                excelData[maxMonthDay + 2] = getExcelDataHeader(commonLocalizer.localize(PdfLocalizationName.overtime), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
                excelData[maxMonthDay + 3] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.AQUA.index);
                excelData[maxMonthDay + 4] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.RED.index);
                excelData[maxMonthDay + 5] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREEN.index);
                excelData[maxMonthDay + 6] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.YELLOW.index);

                list.add(excelData);
                EmployeeReport[] employeeReportArray = attendanceReport.getEmployeeReports();
                for (EmployeeReport employee : employeeReportArray) {
                    int[] al = employee.getAl();
                    int[] withHoliday = employee.getWithHoliday();
                    excelData = new ExcelData[maxMonthDay + 7];
                    excelData[0] = getExcelDataHeader(employee.getName(), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
                    for (int j = 1; j <= maxMonthDay; j++) {
                        if (monthHoliday[j] == 1) {
                            if (employee.getInOutHour()[j] == 0) {
                                excelData[j] = getExcelDataNormal("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_GREEN.index);
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            } else {
                                excelData[j] = getExcelDataNormal(getDateFormat(employee.getInOutHour()[j]), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_GREEN.index);
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            }
                        } else if (monthHoliday[j] == 2) {
                            if (employee.getInOutHour()[j] == 0) {
                                excelData[j] = getExcelDataNormal("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_BLUE.index);
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            } else {
                                excelData[j] = getExcelDataNormal(getDateFormat(employee.getInOutHour()[j]), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_BLUE.index);
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            }
                        } else if (monthHoliday[j] == 3) {
                            if (employee.getInOutHour()[j] == 0) {
                                excelData[j] = getExcelDataNormal("H", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_ORANGE.index);
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            } else {
                                excelData[j] = getExcelDataNormal(getDateFormat(employee.getInOutHour()[j]), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_ORANGE.index);
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            }
                        } else if (al[j] == 1) {
                            excelData[j] = getExcelDataNormal("L", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREEN.index);
                            excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                        } else if (al[j] == -1) {
                            excelData[j] = getExcelDataNormal("L", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.YELLOW.index);
                            excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                        } else if (al[j] == -2 || al[j] == 2) {
                            excelData[j] = getExcelDataNormal("A", ExcelData.STRING, HSSFColor.ORANGE.index, HSSFColor.RED.index);
                            excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                        } else if (withHoliday[j] == 1) {
                            if (employee.getInOutHour()[j] == 0) {
                                excelData[j] = getExcelDataNormal("H", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_ORANGE.index);
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            } else {
                                excelData[j] = getExcelDataNormal(getDateFormat(employee.getInOutHour()[j]), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.LIGHT_ORANGE.index);
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            }
                        } else {
                            excelData[j] = getExcelDataNormal(getDateFormat(employee.getInOutHour()[j]), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                            excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                        }
                    }
                    excelData[maxMonthDay + 1] = getExcelDataHeader(getInOutTotal(employee.getInhour()), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    excelData[maxMonthDay + 1].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    excelData[maxMonthDay + 2] = getExcelDataHeader(getInOutTotal(employee.getOvertime()), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    excelData[maxMonthDay + 2].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);

                    if (employee.getTotalmonth() != 0) {
                        excelData[maxMonthDay + 3] = getExcelDataHeader(String.valueOf(employee.getTotalmonth()), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.AQUA.index);
                        excelData[maxMonthDay + 3].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else {
                        excelData[maxMonthDay + 3] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    }
                    if (employee.getWithoutLR() != 0) {
                        excelData[maxMonthDay + 4] = getExcelDataHeader(String.valueOf(employee.getWithoutLR()), ExcelData.STRING, HSSFColor.YELLOW.index, HSSFColor.RED.index);
                        excelData[maxMonthDay + 4].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else {
                        excelData[maxMonthDay + 4] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    }
                    if (employee.getWithLR() != 0) {
                        excelData[maxMonthDay + 5] = getExcelDataHeader(String.valueOf(employee.getWithLR()), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREEN.index);
                        excelData[maxMonthDay + 5].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else {
                        excelData[maxMonthDay + 5] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    }
                    if (employee.getWaitingForApproval() != 0) {
                        excelData[maxMonthDay + 6] = getExcelDataHeader(String.valueOf(employee.getWaitingForApproval()), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.YELLOW.index);
                        excelData[maxMonthDay + 6].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    } else {
                        excelData[maxMonthDay + 6] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    }
                    list.add(excelData);
                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        workBook = new WorkBook(list, true, 13, 2).getWorkBook(filename, 0, 0, 0, list.size());

        drawExcelReport(list, workBook, sheet, maxMonthDay, fp.getLocationId(), fp.getStartDateNC(), fp.getEndDateNC(), attendanceReport);
        return workBook;
    }

    @Override
    public void setFileName() {
        filename = "AttendaceReport";
    }

    private void drawExcelReport(List<ExcelData[]> list, HSSFWorkbook workBook, HSSFSheet sheet, int maxMonthDay, Integer teamId, String startDate, String endDate, EmployeeAttendanceReport attendanceReport) {

        setCustomColors(workBook);

        sheet = workBook.getSheetAt(0);

        sheet.addMergedRegion(new Region(0, (short) 0, 2, (short) (maxMonthDay + 5)));
        // Month
        sheet.addMergedRegion(new Region(3, (short) 0, 5, (short) 0));
        // Date
        sheet.addMergedRegion(new Region(3, (short) 1, 5, (short) (maxMonthDay)));
        // merged column total in hour
        sheet.addMergedRegion(new Region(3, (short) (maxMonthDay + 1), 6, (short) (maxMonthDay + 1)));
        // total overtime
        sheet.addMergedRegion(new Region(3, (short) (maxMonthDay + 2), 6, (short) (maxMonthDay + 2)));
        // total in month
        sheet.addMergedRegion(new Region(3, (short) (maxMonthDay + 3), 6, (short) (maxMonthDay + 3)));
        // with out leave request
        sheet.addMergedRegion(new Region(3, (short) (maxMonthDay + 4), 5, (short) (maxMonthDay + 4)));
        // with leave request
        sheet.addMergedRegion(new Region(3, (short) (maxMonthDay + 5), 5, (short) (maxMonthDay + 5)));
        // Waiting for approval
        sheet.addMergedRegion(new Region(3, (short) (maxMonthDay + 6), 5, (short) (maxMonthDay + 6)));
        //Total inhours
        sheet.addMergedRegion(new Region(7, (short) (maxMonthDay + 1), 9, (short) (maxMonthDay + 1)));
        sheet.addMergedRegion(new Region(7, (short) (maxMonthDay + 2), 9, (short) (maxMonthDay + 2)));
        // merge employee
        sheet.addMergedRegion(new Region(11, (short) (0), 11, (short) (maxMonthDay + 5)));

        HSSFCell cell = getRow(0, sheet).getCell((short) 0);
        cell.getCellStyle().setFillForegroundColor(HSSFColor.WHITE.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        if (teamId == null) {
            cell.setCellValue("  " + departmentManager.getUser().getCompany().getName() + " " + commonLocalizer.localize(PdfLocalizationName.employeeHoursReport));
        } else {
            cell.setCellValue("  " + departmentManager.get(teamId).getName() + " " + commonLocalizer.localize(PdfLocalizationName.employeeHoursReport));
        }
        setCellFont(cell.getCellStyle(), (short) 20, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);

        cell = getRow(3, sheet).getCell((short) 0);
        cell.getCellStyle().setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellValue(" Month ");
        setCellFont(cell.getCellStyle(), (short) 16, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);
        sheet.setColumnWidth((short) (0), (short) (7000));

        Date start = new Date(Integer.parseInt(startDate.split("-")[0]) - 1900, Integer.parseInt(startDate.split("-")[1]) - 1, Integer.parseInt(startDate.split("-")[2]), 0, 0, 0);
        Date end = new Date(Integer.parseInt(endDate.split("-")[0]) - 1900, Integer.parseInt(endDate.split("-")[1]) - 1, Integer.parseInt(endDate.split("-")[2]),
                Integer.parseInt(endDate.split("-")[3]), Integer.parseInt(endDate.split("-")[4]), Integer.parseInt(endDate.split("-")[5]));


        cell = getRow(3, sheet).getCell((short) 1);
        cell.getCellStyle().setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellValue(format.format(start) + " - " + format.format(end));
        setCellFont(cell.getCellStyle(), (short) 16, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);

        // merged column total in hour
        cell = getRow(3, sheet).getCell((short) (maxMonthDay + 1));
        cell.getCellStyle().setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.totalInHours));
        setCellFont(cell.getCellStyle(), (short) 9, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);

        // total over time
        cell = getRow(3, sheet).getCell((short) (maxMonthDay + 2));
        cell.getCellStyle().setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.totalOverTime));
        setCellFont(cell.getCellStyle(), (short) 9, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);

        // total in month
        cell = getRow(3, sheet).getCell((short) (maxMonthDay + 3));
        cell.getCellStyle().setFillForegroundColor(HSSFColor.AQUA.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.totalIn) + " " + format1.format(start));
        setCellFont(cell.getCellStyle(), (short) 9, HSSFColor.WHITE.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);
        sheet.setColumnWidth((short) (maxMonthDay + 3), (short) (2500));

        // with out leave request
        cell = getRow(3, sheet).getCell((short) (maxMonthDay + 4));
//        cell.getCellStyle().setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cell.getCellStyle().setFillBackgroundColor(HSSFColor.RED.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.withoutLeaveRequest));
        setCellFont(cell.getCellStyle(), (short) 9, HSSFColor.YELLOW.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);
        sheet.setColumnWidth((short) (maxMonthDay + 4), (short) (2200));

        // with out leave A
        cell = getRow(6, sheet).getCell((short) (maxMonthDay + 4));
        cell.getCellStyle().setFillForegroundColor(HSSFColor.RED.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellValue("A");
        setCellFont(cell.getCellStyle(), (short) 9, HSSFColor.YELLOW.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);

        // with leave rquest
        cell = getRow(3, sheet).getCell((short) (maxMonthDay + 5));
        cell.getCellStyle().setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.withLeaveRequest));
        setCellFont(cell.getCellStyle(), (short) 9, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);
        sheet.setColumnWidth((short) (maxMonthDay + 5), (short) (2200));

        // whith leave  L
        cell = getRow(6, sheet).getCell((short) (maxMonthDay + 5));
        cell.getCellStyle().setFillForegroundColor(HSSFColor.GREEN.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellValue("L");
        setCellFont(cell.getCellStyle(), (short) 9, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);

        // Waiting For Approval
        cell = getRow(3, sheet).getCell((short) (maxMonthDay + 6));
//        cell.getCellStyle().setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cell.getCellStyle().setFillBackgroundColor(HSSFColor.YELLOW.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.waitingForApproval));
        setCellFont(cell.getCellStyle(), (short) 9, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);
        sheet.setColumnWidth((short) (maxMonthDay + 5), (short) (2200));

        // Waiting For Approval L
        cell = getRow(6, sheet).getCell((short) (maxMonthDay + 6));
        cell.getCellStyle().setFillBackgroundColor(HSSFColor.YELLOW.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellValue("L");
        setCellFont(cell.getCellStyle(), (short) 9, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);

        // set Total hours and overtime
        getRow(7, sheet).getCell((short) (maxMonthDay + 1)).setCellValue(getDateFormat(attendanceReport.getTotalInHour()));
        getRow(7, sheet).getCell((short) (maxMonthDay + 2)).setCellValue(getDateFormat(attendanceReport.getOverTime()));
        getRow(7, sheet).getCell((short) (maxMonthDay + 1)).getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        getRow(7, sheet).getCell((short) (maxMonthDay + 2)).getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        //Total in
        getRow(7, sheet).getCell((short) (maxMonthDay + 3)).setCellValue(attendanceReport.getTotalAbsentSum());
        //WithOutLeave Request
        getRow(8, sheet).getCell((short) (maxMonthDay + 4)).setCellValue(attendanceReport.getWithoutLRSum());
        // With leave request
        getRow(9, sheet).getCell((short) (maxMonthDay + 5)).setCellValue(attendanceReport.getWithLRSum());
        getRow(10, sheet).getCell((short) (maxMonthDay + 6)).setCellValue(attendanceReport.getWaitingForApprovalSum());

    }

    private void setCustomColors(HSSFWorkbook workBook) {
        HSSFPalette palette = workBook.getCustomPalette();

        palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 79, (byte) 129, (byte) 189);
        palette.setColorAtIndex(HSSFColor.YELLOW.index, (byte) 242, (byte) 213, (byte) 3);
        palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 146, (byte) 208, (byte) 81);
        palette.setColorAtIndex(HSSFColor.LIGHT_BLUE.index, (byte) 149, (byte) 179, (byte) 215);
        palette.setColorAtIndex(HSSFColor.RED.index, (byte) 193, (byte) 0, (byte) 1);
        palette.setColorAtIndex(HSSFColor.AQUA.index, (byte) 121, (byte) 167, (byte) 229);
        palette.setColorAtIndex(HSSFColor.LIGHT_GREEN.index, (byte) 194, (byte) 215, (byte) 155);
        palette.setColorAtIndex(HSSFColor.BROWN.index, (byte) 219, (byte) 229, (byte) 241);
        palette.setColorAtIndex(HSSFColor.DARK_BLUE.index, (byte) 31, (byte) 105, (byte) 218);
    }

    private HSSFRow getRow(int rowNumber, HSSFSheet sheet) {
        return sheet.getRow(rowNumber);
    }

    private void setCellFont(HSSFCellStyle style, short size, short color, short font_weight, HSSFWorkbook workBook) {
        HSSFFont font = workBook.createFont();
        font.setFontHeightInPoints(size);
        font.setColor(color);
        font.setBoldweight(font_weight);
        style.setFont(font);
    }

    private String getDateFormat(int number) {
        return (number < 0 ? "-" : "")
                + (Math.abs(number) / 60 > 9 ? (Math.abs(number) / 60) + "" : "0" + (Math.abs(number) / 60))
                + ":"
                + (Math.abs(number) % 60 > 9 ? (Math.abs(number) % 60) + "" : "0" + (Math.abs(number) % 60));
    }

    private String getInOutTotal(int number) {
        return (number < 0 ? "-" : "")
                + (Math.abs(number) / 60 > 9 ? (Math.abs(number) / 60) + "" : "0" + (Math.abs(number) / 60))
                + ":"
                + (Math.abs(number) % 60 > 9 ? (Math.abs(number) % 60) + "" : "0" + (Math.abs(number) % 60));
    }

    private ExcelData getExcelDataHeader(String value, int type, short fontcolor, short bgcolor) {
        ExcelData excel = new ExcelData(value, type, 13, true, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        excel.setStyle(true);
        excel.setFontColor(fontcolor);
        excel.setBgcolor(bgcolor);
        excel.setFontSize(9);
        return excel;
    }

    private ExcelData getExcelDataNormal(String value, int type, short fontcolor, short bgcolor) {
        ExcelData excel = new ExcelData(value, type, 11, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        excel.setStyle(true);
        excel.setFontColor(fontcolor);
        excel.setBgcolor(bgcolor);
        excel.setFontSize(8);
        return excel;
    }
}
