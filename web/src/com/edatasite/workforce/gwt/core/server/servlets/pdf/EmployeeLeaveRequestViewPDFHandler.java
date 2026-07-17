package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestComment;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeViewItem;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Azam on 8/15/2017.
 */
public class EmployeeLeaveRequestViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants, IPostPDFHandler {

    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private SickRequestManager sickRequestManager;

    @Autowired
    private BackupEmployeeManager backupEmployeeManager;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private LabourPeriodManager labourPeriodManager;
    @Autowired
    private MultiLeaveManager multiLeaveManager;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private SickRequestDurationManager sickRequestDurationManager;
    @Autowired
    private PropertManager propertManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }


    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        LeaveRequestObject requestObject = (LeaveRequestObject) dataClass;
        if (requestObject == null) {
            return null;
        }
        Integer requestID = requestObject.getObjectID();
        if (requestID == null) {
            return null;
        }

        StatisticsLeaveRequest employeeLeaveRequest = availabilityService.getLeaveRequest(requestID);
        Integer userID = requestObject.getUserID();
        if (userID == null) {
            userID = employeeLeaveRequest.getEmployeeId();
        }
        if (employeeLeaveRequest == null) {
            return null;
        }

        List<EdsLabourPeriod> laborPeriods = labourPeriodManager.sickRequestPeriods(employeeLeaveRequest.getObjectID(), false);

        EmployeeViewItem employeeViewItem = availabilityService.getEmployee(userID);
        EdsUser user = uploadManager.getUser();

        ProfileItem profileItem = hrmsServiceLocal.editProfile(userID);
        if (profileItem == null) {
            return null;
        }

        EdsLeaveReason leaveReason = leaveReasonManager.get(employeeLeaveRequest.getReasonId());

        String reasonEng = "";
        String reasonRu = "";
        String reasonUz = "";

        if (leaveReason != null && leaveReason.getLocale() != null) {
            reasonEng = leaveReason.getLocale().getEnglish();
            reasonRu = leaveReason.getLocale().getRussian();
            reasonUz = leaveReason.getLocale().getUzbek();
        }

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable leaveRequestTable = new CustomisedITextTable();
        leaveRequestTable.setName(employeeLeaveRequest.getEmployee());
        leaveRequestTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        leaveRequestTable.addRowWithCode("LEAVE_REQUEST_INFORMATION_TITLE", commonLocalizer.localize(PdfLocalizationName.leaveRequestInformation), "");
        leaveRequestTable.addRowWithCode(NAME, commonLocalizer.localize(PdfLocalizationName.name), escapeHtml(employeeLeaveRequest.getEmployee()));
        leaveRequestTable.addRowWithCode(CREATED_DATE, commonLocalizer.localize(PdfLocalizationName.createdDate), escapeHtml(longDateFormat(employeeLeaveRequest.getCreatedDate())));
        leaveRequestTable.addRowWithCode(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description), escapeHtml(employeeLeaveRequest.getDescription()));
        leaveRequestTable.addRowWithCode(PERIOD, commonLocalizer.localize(PdfLocalizationName.period), escapeHtml(longDateFormat(employeeLeaveRequest.getStartDDate().getNonConvertedDate(), true)));
        leaveRequestTable.addRowWithCode(TO_DATE, commonLocalizer.localize(PdfLocalizationName.toDate), escapeHtml(longDateFormat(employeeLeaveRequest.getEndDDate().getNonConvertedDate(), true)));
        leaveRequestTable.addRowWithCode(DURATION, commonLocalizer.localize(PdfLocalizationName.duration), escapeHtml(employeeLeaveRequest.getDuration()));
        leaveRequestTable.addRowWithCode(TYPE, commonLocalizer.localize(PdfLocalizationName.type), escapeHtml(employeeLeaveRequest.getType()));
        leaveRequestTable.addRowWithCode(REASON, commonLocalizer.localize(PdfLocalizationName.reason), escapeHtml(employeeLeaveRequest.getReason()));
        leaveRequestTable.addRowWithCode(STATUS, commonLocalizer.localize(PdfLocalizationName.status), employeeLeaveRequest.getOverallStatus() != null ? escapeHtml(employeeLeaveRequest.getOverallStatus().getName()) : "");
        leaveRequestTable.addRowWithCode(CREATOR, "", escapeHtml(employeeLeaveRequest.getCreator()));
        leaveRequestTable.addRowWithCode("CREATOR_POSITION", "", escapeHtml(employeeLeaveRequest.getCreatorPosition()));
        leaveRequestTable.addRowWithCode("CREATOR_DEPARTMENT", "", escapeHtml(employeeLeaveRequest.getCreatorDepartment()));
        leaveRequestTable.addRowWithCode("TAKE_BY_MONEY", "", escapeHtml(String.valueOf(employeeLeaveRequest.getTakeByMoney())));
        leaveRequestTable.addRowWithCode("REASON_ENG", "", reasonEng != null ? escapeHtml(reasonEng) : "");
        leaveRequestTable.addRowWithCode("REASON_RU", "", reasonRu != null ? escapeHtml(reasonRu) : "");
        leaveRequestTable.addRowWithCode("REASON_UZ", "", reasonUz != null ? escapeHtml(reasonUz) : "");

        if (requestObject.getSvg() != null) {
            leaveRequestTable.addRowWithCode("SVG", "", requestObject.getSvg());
        }
        customData.put("APPROVERS_INFORMATION", getApproversInformation(employeeLeaveRequest));
        customData.put("LEAVE_REQUEST_INFORMATION", leaveRequestTable);
        customData.put("EMPLOYEE_INFORMATION", getEmployeeInformation(profileItem, userID));
        customData.put("PERSONAL_INFORMATION", getPersonalInformation(profileItem));
        customData.put("EMPLOYEE_CUSTOM_FIELD", getEmployeeCustomField(profileItem));
        customData.put("LEAVE_REQUEST_CUSTOM_FIELD", getLeaveRequestCustomField(employeeLeaveRequest));
        customData.put("CUSTOM_EMPLOYEE_LOOKUP_INFORMATION", getCustomEmployeeLookupInformation(user, employeeLeaveRequest));
        customData.put("CURRENT_DATA", getCurrentData());
        customData.put("LEAVE_REQUESTS_BY_PERIOD", getLeaveRequestsByPeriodTable(laborPeriods, employeeLeaveRequest, requestID));
        customData.put("BACKUP_EMPLOYEES", getBackUpEmployees(employeeLeaveRequest));
        if (user != null) {
            pdfData.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));
        }
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        pdfData.setBaseInvoice(baseInvoice);
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(employeeLeaveRequest, employeeViewItem));
        baseInvoice.setNotes(getLeaveRequestComment(employeeLeaveRequest.getLeaveRequestComment()));

        pdfData.setCustomData(customData);
        return pdfData;
    }

    private CustomisedITextTable getLeaveRequestsByPeriodTable(List<EdsLabourPeriod> laborPeriods, StatisticsLeaveRequest employeeLeaveRequest, Integer requestId) {
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        CustomisedITextTable periodTable = new CustomisedITextTable();
        periodTable.addColumn("LABOR_PERIOD_START", "LABOR_PERIOD_START");
        periodTable.addColumn("LABOR_PERIOD_END", "LABOR_PERIOD_END");
        periodTable.addColumn("LEAVE_START", "LEAVE_START");
        periodTable.addColumn("LEAVE_END", "LEAVE_END");
        periodTable.addColumn("LEAVE_DAYS", "LEAVE_DAYS");
        periodTable.addColumn("LEAVE_DAYS_MONEY", "LEAVE_DAYS_MONEY");
        periodTable.addColumn("LEAVE_LEFT_DAYS", "LEAVE_LEFT_DAYS");
        periodTable.addColumn("EXIST_DAYS", "EXIST_DAYS");


        int index = 0;

        if (laborPeriods != null && laborPeriods.size() > 0) {
            for (EdsLabourPeriod periodRequest : laborPeriods) {
                Double sumDayDuration = labourPeriodManager.getTotalTakenLeaveDaysByPeriodId(periodRequest.getObjectID(), employeeLeaveRequest.getObjectID());
                Double sumMoneyDuration = 0d /*labourPeriodManager.getTotalTakenLeaveDaysByPeriodId(periodRequest.getObjectID(), false, Constants.MONEY)*/;
                ArrayList<String> row = new ArrayList<>();
                row.add(shortDateFormat.format(periodRequest.getStartDate()));
                row.add(shortDateFormat.format(periodRequest.getEndDate()));
                row.add(shortDateFormat.format(employeeLeaveRequest.getStartDDate().getNonConvertedDate()));
                row.add(shortDateFormat.format(availabilityService.getEndDate(employeeLeaveRequest.getEmployeeId(), employeeLeaveRequest.getStartDDate().getNonConvertedDate(), sumDayDuration + sumMoneyDuration, employeeLeaveRequest.getReasonCode())));
                row.add(String.valueOf(sumDayDuration));
                row.add(String.valueOf(sumMoneyDuration));
                row.add(String.valueOf(periodRequest.getAllowance() - (periodRequest.getOutOfSystemDays() != null ? periodRequest.getOutOfSystemDays() : 0) - sumDayDuration - sumMoneyDuration));

                List<Object[]> periodLeavesData = labourPeriodManager.getPeriodLeavesData(periodRequest.getObjectID(), LR_STATUS_SS_APPROVED.equals(employeeLeaveRequest.getOverallStatus().getCode()));
                    Integer takenDays = 0;
                    for (Object[] periodLeavesDatum : periodLeavesData) {
                        if ((employeeLeaveRequest.getEndDDate().getDate().compareTo(((Date) periodLeavesDatum[2])) > 0)  && !LR_STATUS_NOT_DEFINED.equals(sickRequestManager.get((Integer) periodLeavesDatum[0]).getOverallStatus().getCode()) && !DRAFT.equals(sickRequestManager.get((Integer) periodLeavesDatum[0]).getOverallStatus().getCode())) {
                            takenDays += ((BigDecimal) periodLeavesDatum[1]).intValue();
                        }
                    }
                if (periodRequest.getOutOfSystemDays() != null) {
                    takenDays += periodRequest.getOutOfSystemDays().intValue();
                }
                if (LR_STATUS_SS_APPROVED.equals(employeeLeaveRequest.getOverallStatus().getCode())) {
                    row.add(String.valueOf(periodRequest.getAllowance() - (takenDays - sumDayDuration)));
                } else {
                    row.add(String.valueOf(periodRequest.getAllowance() - takenDays ));
                }


                periodTable.addRowWithCode(String.valueOf(index), row.toArray(new String[]{}));

                index++;
            }
        }

        return periodTable;
    }

    private CustomisedITextTable getBackUpEmployees(StatisticsLeaveRequest employeeLeaveRequest) {
        CustomisedITextTable backupEmployeeTable = new CustomisedITextTable();
        EdsSickRequest sickRequest;
        Set<EdsEmployee> employees = new HashSet<>();
        if (employeeLeaveRequest.getObjectID() != null) {
            sickRequest = sickRequestManager.get(employeeLeaveRequest.getObjectID());
            List<EdsBackupEmployee> backupEmployeesBySickRequestId = backupEmployeeManager.getBackupEmployeesBySickRequestId(sickRequest.getObjectID());
            if (backupEmployeesBySickRequestId != null && backupEmployeesBySickRequestId.size() > 0) {
                for (EdsBackupEmployee backupEmployee : backupEmployeesBySickRequestId) {
                    employees.add(backupEmployee.getEmployee());
                }
            }
        }
        if (employees != null && employees.size() > 0) {
            for (EdsEmployee employee : employees) {
                backupEmployeeTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
                backupEmployeeTable.addRowWithCode("EMPLOYEE_FULL_NAME", "EMPLOYEE_FULL_NAME", escapeHtml(employee.getFullName()));
                backupEmployeeTable.addRowWithCode("EMPLOYEE_POSITION", "EMPLOYEE_POSITION", escapeHtml(employee.getPosition().getName()));

                ProfileItem profileItem = hrmsServiceLocal.editProfile(employee.getObjectID());
                if (profileItem.getCustomFields() != null) {
                    for (CompanyCustomFieldItem field : profileItem.getCustomFields()) {
                        if (field != null) {
                            if (field.getFieldName().equals("Имя (eng)") && !field.getFieldStringValue().equals("")) {
                                backupEmployeeTable.addRowWithCode("EMPLOYEE_ENG_NAME", "EMPLOYEE_ENG_NAME", escapeHtml(field.getFieldStringValue()));
                            }
                            if (field.getFieldName().equals("Фамилия (eng)") && !field.getFieldStringValue().equals("")) {
                                backupEmployeeTable.addRowWithCode("EMPLOYEE_ENG_LAST_NAME", "EMPLOYEE_ENG_LAST_NAME", escapeHtml(field.getFieldStringValue()));
                            }
                        }
                    }
                }
            }
        }
        return backupEmployeeTable;
    }

    private CustomisedITextTable getCurrentData() {
        CustomisedITextTable customTable = new CustomisedITextTable();
        customTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        Date currentDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        customTable.addRow(CURRENT_DATE, shortDateFormat.format(currentDate));
        customTable.addRow("CURRENT_YEAR", Calendar.getInstance().get(Calendar.YEAR) + "");
        customTable.addRow("CURRENT_TIME", timeFormat.format(userManager.getUser().getUserDate()));

        return customTable;
    }

    private CustomisedITextTable getApproversInformation(StatisticsLeaveRequest employeeLeaveRequest) {
        List<String> list = new ArrayList<>();
        CustomisedITextTable approversTable = new CustomisedITextTable();
        approversTable.addColumn(APPROVER, commonLocalizer.localize(PdfLocalizationName.approver));
        approversTable.addColumn("POSITION_EN", "Position En");
        approversTable.addColumn("POSITION_RU", "Position Ru");
        approversTable.addColumn("POSITION_UZ", "Position Uz");
        approversTable.addColumn(NUMBER, commonLocalizer.localize(PdfLocalizationName.number));
        List<ApproverItemMini> approversList = employeeLeaveRequest.getApprovers();
        if (approversList != null && approversList.size() > 0) {
            int i = 1;
            for (ApproverItemMini approver : approversList) {
                list.clear();
                if (approver.getExactEmployee() != null) {
                    EdsEmployee employee = employeeManager.get(approver.getExactEmployee().getId());
                    list.add(escapeHtml(employee.getLastName() != null && employee.getFirstName() != null && employee.getMiddleName() != null ?
                            employee.getLastName() + " " + employee.getFirstName() + " " + employee.getMiddleName() : ""));
                    EdsPosition edsPosition = employee.getPosition() != null ? positionManager.get(employee.getPosition().getObjectID()) : null;
                    String positionEn = "";
                    String positionRu = "";
                    String positionUz = "";
                    if (edsPosition != null && edsPosition.getLocale() != null) {
                        positionEn = edsPosition.getLocale().getEnglish();
                        positionRu = edsPosition.getLocale().getRussian();
                        positionUz = edsPosition.getLocale().getUzbek();
                    }
                    list.add(positionEn);
                    list.add(positionRu);
                    list.add(positionUz);

                }
                list.add(escapeHtml(String.valueOf(i)));
                approversTable.addRow(list.toArray(new String[]{}));
                i++;
            }
        }
        return approversTable;
    }

    private CustomisedITextTable getCustomEmployeeLookupInformation(EdsUser user, StatisticsLeaveRequest employeeLeaveRequest) {
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        Integer employeeId = null;
        int i = 0;
        String count = "";
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (employeeLeaveRequest != null && employeeLeaveRequest.getCustomFields() != null && employeeLeaveRequest.getCustomFields().size() > 0) {
            for (CompanyCustomFieldItem field : employeeLeaveRequest.getCustomFields()) {
                if (field.getLookUpTypeEnum() != null && field.getLookUpTypeEnum().name().equalsIgnoreCase("EMPLOYEE") && field.getSelectedId() != null) {
                    employeeId = field.getSelectedId();
                    if (employeeId == null) {
                        return null;
                    }
                    EdsEmployee employee = employeeManager.get(employeeId);
                    if (employee == null) {
                        return null;
                    }
                    String employeeName = escapeHtml(employee.getFullName());
                    String employeeFirstName = escapeHtml(employee.getFirstName());
                    String employeeLastName = escapeHtml(employee.getLastName());
                    String employeeMiddleName = escapeHtml(employee.getMiddleName());
                    String hireDate = employee.getStartDate() != null ? shortDateFormat.format(employee.getStartDate()) : "";
                    String empPosition = employee.getPosition() != null ? escapeHtml(employee.getPosition().getName()) : "";
                    String empDepartment = employee.getTeam() != null ? employee.getTeam().getName() : "";
                    String empPassportNumber = "";
                    String empPassportIssueBy = "";
                    String empPassportIssueDate = "";
                    if (employee.getProfile() != null) {
                        empPassportNumber = escapeHtml(employee.getProfile().getPassportNumber());
                        empPassportIssueBy = employee.getProfile().getCountry() != null ? escapeHtml(employee.getProfile().getCountry().getName()) : "";
                        empPassportIssueDate = employee.getProfile().getPassportIssueDate() != null ? shortDateFormat.format(employee.getProfile().getPassportIssueDate()) : "";
                    }

                    EdsDepartment edsDepartment = employee.getTeam() != null ? departmentManager.get(employee.getTeam().getObjectID()) : null;
                    String departmentEn = "";
                    String departmentRu = "";
                    String departmentUz = "";
                    if (edsDepartment != null && edsDepartment.getLocale() != null) {
                        departmentEn = edsDepartment.getLocale().getEnglish();
                        departmentRu = edsDepartment.getLocale().getRussian();
                        departmentUz = edsDepartment.getLocale().getUzbek();
                    }

                    EdsPosition edsPosition = employee.getPosition() != null ? positionManager.get(employee.getPosition().getObjectID()) : null;
                    String positionEn = "";
                    String positionRu = "";
                    String positionUz = "";
                    if (edsPosition != null && edsPosition.getLocale() != null) {
                        positionEn = edsPosition.getLocale().getEnglish();
                        positionRu = edsPosition.getLocale().getRussian();
                        positionUz = edsPosition.getLocale().getUzbek();
                    }

                    String parentDepartment = "";
                    String parentDepartmentUz = "";
                    String parentDepartment1 = "";
                    String parentDepartment1Uz = "";
                    String parentDepartment2 = "";
                    String parentDepartment2Uz = "";

                    if (departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment() != null) {
                        Integer parentDepartmentId = departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getId();
                        Integer parentDepartment1Id = departmentService.getTeam(parentDepartmentId).getParentDepartment().getId();

                        if (departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getName() != null) {
                            parentDepartment = departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getName();
                            if (departmentManager.getDeparmentLocalization(departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getId()) != null) {
                                parentDepartmentUz = departmentManager.getDeparmentLocalization(departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getId()).getUzbek();
                            }

                            if (departmentService.getTeam(parentDepartmentId).getParentDepartment() != null) {
                                if (departmentService.getTeam(parentDepartmentId).getParentDepartment().getName() != null) {
                                    parentDepartment1 = departmentService.getTeam(parentDepartmentId).getParentDepartment().getName();
                                    if (departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartmentId).getParentDepartment().getId()) != null) {
                                        parentDepartment1Uz = departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartmentId).getParentDepartment().getId()).getUzbek();
                                    }
                                    if (departmentService.getTeam(parentDepartment1Id).getParentDepartment() != null) {
                                        if (departmentService.getTeam(parentDepartment1Id).getParentDepartment().getName() != null) {
                                            parentDepartment2 = departmentService.getTeam(parentDepartment1Id).getParentDepartment().getName();
                                            if (departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartment1Id).getParentDepartment().getId()) != null) {
                                                parentDepartment2Uz = departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartment1Id).getParentDepartment().getId()).getUzbek();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                    if (i > 0) {
                        count = "_" + i;
                    }

                    table.addRowWithCode(EMPLOYEE_NAME + count, "", employeeName);
                    table.addRowWithCode("EMP_FIRST_NAME" + count, "", employeeFirstName);
                    table.addRowWithCode("EMP_LAST_NAME" + count, "", employeeLastName);
                    table.addRowWithCode("EMP_MIDDLE_NAME" + count, "", employeeMiddleName);
                    table.addRowWithCode("EMP_HIRE_DATE" + count, "", hireDate);
                    table.addRowWithCode("EMP_POSITION" + count, "", empPosition);
                    table.addRowWithCode("EMP_DEPARTMENT" + count, "", empDepartment);
                    table.addRowWithCode("EMP_PARENT_DEPARTMENT" + count, "", parentDepartment != null ? escapeHtml(parentDepartment) : "");
                    table.addRowWithCode("EMP_PARENT_DEPARTMENT_UZ" + count, "", parentDepartmentUz != null ? escapeHtml(parentDepartmentUz) : "");
                    table.addRowWithCode("EMP_PARENT1_DEPARTMENT" + count, "", parentDepartment1 != null ? escapeHtml(parentDepartment1) : "");
                    table.addRowWithCode("EMP_PARENT1_DEPARTMENT_UZ" + count, "", parentDepartment1Uz != null ? escapeHtml(parentDepartment1Uz) : "");
                    table.addRowWithCode("EMP_PARENT2_DEPARTMENT" + count, "", parentDepartment2 != null ? escapeHtml(parentDepartment2) : "");
                    table.addRowWithCode("EMP_PARENT2_DEPARTMENT_UZ" + count, "", parentDepartment2Uz != null ? escapeHtml(parentDepartment2Uz) : "");
                    table.addRowWithCode("EMP_PASSPORT_NUMBER" + count, "", empPassportNumber);
                    table.addRowWithCode("EMP_PASSPORT_ISSUE_DATE" + count, "", empPassportIssueDate);
                    table.addRowWithCode("EMP_PASSPORT_ISSUE_BY" + count, "", empPassportIssueBy);

                    table.addRowWithCode("EMP_DEPARTMENT_EN" + count, "", departmentEn);
                    table.addRowWithCode("EMP_DEPARTMENT_RU" + count, "", departmentRu);
                    table.addRowWithCode("EMP_DEPARTMENT_UZ" + count, "", departmentUz);

                    table.addRowWithCode("EMP_POSITION_EN" + count, "", positionEn);
                    table.addRowWithCode("EMP_POSITION_RU" + count, "", positionRu);
                    table.addRowWithCode("EMP_POSITION_UZ" + count, "", positionUz);
                    i++;
                    customFields = getLeaveRequestCustomField(employeeLeaveRequest).getCustomFields();
                }
            }
            table.setCustomFields(customFields);
        }
        return table;
    }

    private CustomisedITextTable getLeaveRequestCustomField(StatisticsLeaveRequest employeeLeaveRequest) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        EdsUser user = uploadManager.getUser();
        DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (employeeLeaveRequest.getCustomFields() != null && employeeLeaveRequest.getCustomFields().size() > 0) {
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem field : employeeLeaveRequest.getCustomFields()) {
                if (field != null) {
                    Map<String, String> cols = new HashMap<>();
                    boolean isDateUz = false;
                    cols.put(COLUMN_NAME, escapeHtml(field.getFieldName()));
                    if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                        String dateValue = "";
                        EdsCompany company = user.getCompany();
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                        if (field.getFieldDateNonConvertedValue() != null) {
                            if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                Locale ruLocale = new Locale("ru", "RU");
                                SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                                dateValue = field.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? ruDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            } else {
                                dateValue = field.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            }
                        }
                        cols.put(COLUMN_VALUE, dateValue);
                        if (company.getObjectID().equals(90826) && field.getFieldDateNonConvertedValue() != null) {
                            if (field.getFieldName() != null) {
                                itemCusFields.put(field.getFieldName(), cols);
                                isDateUz = true;
                            }
                            cols = new HashMap<>();
                            cols.put(COLUMN_NAME, escapeHtml(field.getFieldName()));
                            String shortDateFormatEn = user.getCompany().getCompanySettings().getShortDateFormat();
                            SimpleDateFormat formatEn = new SimpleDateFormat(shortDateFormatEn != null ? shortDateFormatEn : "MMM dd yyyy", Locale.ENGLISH);

                            String dateUz = escapeHtml(ServerUtils.convertToUzbDateFormat(formatEn.format(field.getFieldDateNonConvertedValue().getNonConvertedDate())));
                            cols.put(COLUMN_VALUE, dateUz);
                            if (field.getFieldName() != null) {
                                itemCusFields.put(field.getFieldName() + "_UZ", cols);
                            }
                        }
                    } else if (CompanyCustomFieldItem.NUMBER.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(decimalFormat.format(Double.valueOf(field.getFieldStringValue()))) : "—");
                    } else if (UI_TYPE_HTML_TEXTAREA.equals(field.getUiType())) {
                        if (field.getFieldStringValue() != null && !field.getFieldStringValue().isEmpty()) {
                            String html = field.getFieldStringValue();
                            org.jsoup.nodes.Document doc = Jsoup.parse(html);
                            StringBuilder textValue = new StringBuilder();
                            if (Objects.equals(user.getCompany().getObjectID(), 90826)) {
                                Elements pElements = doc.getElementsByTag("p");
                                for (Element element : pElements) {
                                    textValue.append(element.text()).append("<br/>");
                                }
                            } else {
                                textValue.append(doc.body().text());
                            }
                            cols.put(COLUMN_VALUE, textValue.toString());
                        } else {
                            cols.put(COLUMN_VALUE, "");
                        }
                    } else if (TYPE_ENTITY_LOOKUP.equals(field.getUiType())) {
                        String defaultValue = "";
                        if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                            Integer id = null;
                            try {
                                id = Integer.valueOf(field.getFieldStringValue());
                            } catch (final NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if (id != null && field.getQueryItems() != null) {
                                for (final SelectItem selectItem : field.getQueryItems()) {
                                    if (selectItem.getId().equals(id)) {
                                        defaultValue = escapeHtml(selectItem.getName());
                                        break;
                                    }
                                }
                            }
                        }
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(defaultValue) ? escapeHtml(defaultValue) : "—");
                    } else if (field.getUiType().equals(UI_TYPE_DROPDOWN) || field.getDataType().equals(UI_TYPE_DROPDOWN)) {
                        if (StringUtils.isNotEmpty(field.getFieldStringValue()) && field.getLocalization() != null) {
                            for (CustomFormLocalization children : field.getLocalization().getChildren()) {
                                String[] childrenLocaleValues = {children.getRussianName(), children.getUzbekName()};
                                boolean isLocaleSame = (Arrays.stream(childrenLocaleValues).anyMatch(childValue -> childValue.equals(field.getFieldStringValue())));
                                if (isLocaleSame) {
                                    StringBuilder column = new StringBuilder();
                                    // didn't use foreach loop or stream because last element does not need "-:-"
                                    for (int i = 0; i < childrenLocaleValues.length; i++) {
                                        column.append(StringUtils.isNotEmpty(childrenLocaleValues[i]) ? childrenLocaleValues[i] : "—").append("-:-");
                                    }
                                    cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(column.toString()) ? escapeHtml(column.toString()) : "—");
                                }
                            }
                        } else {
                            cols.put(COLUMN_VALUE, "—");
                        }
                    } else {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(field.getFieldStringValue()) : "—");
                    }
                    if (field.getFieldName() != null && !isDateUz) {
                        itemCusFields.put(field.getFieldName(), cols);
                    }
                }
            }
            customFields.put("LEAVE_REQUEST", itemCusFields);
            customFieldTable.setCustomFields(customFields);
        }

        return customFieldTable;
    }

    private CustomisedITextTable getEmployeeCustomField(ProfileItem item) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        EdsUser user = uploadManager.getUser();
        DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem field : item.getCustomFields()) {
                if (field != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, escapeHtml(field.getFieldName()));
                    if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                        String dateValue = "";
                        EdsCompany company = userManager.getUser().getCompany();
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                        if (field.getFieldDateNonConvertedValue() != null) {
                            if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                Locale ruLocale = new Locale("ru", "RU");
                                SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                                dateValue = field.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? ruDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            } else {
                                dateValue = field.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            }
                        }
                        cols.put(COLUMN_VALUE, dateValue);
                    } else if (CompanyCustomFieldItem.NUMBER.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(decimalFormat.format(Double.valueOf(field.getFieldStringValue()))) : "—");
                    } else if (UI_TYPE_HTML_TEXTAREA.equals(field.getUiType())) {
                        if (field.getFieldStringValue() != null && !field.getFieldStringValue().isEmpty()) {
                            String html = field.getFieldStringValue();
                            org.jsoup.nodes.Document doc = Jsoup.parse(html);
                            StringBuilder textValue = new StringBuilder();
                            if (Objects.equals(user.getCompany().getObjectID(), 90826)) {
                                Elements pElements = doc.getElementsByTag("p");
                                for (Element element : pElements) {
                                    textValue.append(element.text()).append("<br/>");
                                }
                            } else {
                                textValue.append(doc.body().text());
                            }
                            cols.put(COLUMN_VALUE, textValue.toString());
                        } else {
                            cols.put(COLUMN_VALUE, "");
                        }
                    } else {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(field.getFieldStringValue()) : "—");
                    }
                    if (field.getFieldName() != null) {
                        itemCusFields.put(field.getFieldName(), cols);
                    }
                }
            }
            customFields.put("EMPLOYEE", itemCusFields);
            customFieldTable.setCustomFields(customFields);
        }

        return customFieldTable;
    }


    private CustomisedITextTable getPersonalInformation(ProfileItem item) {
        CustomisedITextTable personalTable = new CustomisedITextTable();

        String passportNumber = escapeHtml(item.getPassportNumber());
        String passportIssue = item.getPassportIssueItem() != null ? escapeHtml(item.getPassportIssueItem().getName()) : "";
        String passportIssueDate = item.getPassportIssueDate() != null ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getPassportIssueDate().getDate())) : dateFormat(item.getPassportIssueDate().getDate()) : "";
        String insurenceNumber = escapeHtml(item.getInsuranceNumber());
        String insurenceExpiryDate = item.getMedicalInsuranceExpireDate() != null ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getMedicalInsuranceExpireDate().getDate())) : dateFormat(item.getMedicalInsuranceExpireDate().getDate()) : "";
        String visaNumber = escapeHtml(item.getVisaNumber());
        String visaIssueDate = item.getVisaIssueDate() != null ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getVisaIssueDate().getDate())) : dateFormat(item.getVisaIssueDate().getDate()) : "";
        String visaExpirationDate = item.getVisaExpirationDate() != null ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getVisaExpirationDate().getDate())) : dateFormat(item.getVisaExpirationDate().getDate()) : "";

        personalTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        personalTable.addRowWithCode(PASSPORT_NUMBER, commonLocalizer.localize(PdfLocalizationName.passportNumber), passportNumber);
        personalTable.addRowWithCode(PASSPORT_ISSUE, commonLocalizer.localize(PdfLocalizationName.passportIssueBy), passportIssue);
        personalTable.addRowWithCode(PASSPORT_ISSUE_DATE, commonLocalizer.localize(PdfLocalizationName.passportIssueDate), passportIssueDate);
        personalTable.addRowWithCode(INSURANCE_NUMBER, commonLocalizer.localize(PdfLocalizationName.insuranseNumber), insurenceNumber);
        personalTable.addRowWithCode(INSURANCE_EXPIRY_DATE, commonLocalizer.localize(PdfLocalizationName.insuranceExpiryDate), insurenceExpiryDate);
        personalTable.addRowWithCode(VISA_NUMBER, commonLocalizer.localize(PdfLocalizationName.visaNumber), visaNumber);
        personalTable.addRowWithCode(VISA_ISSUE_DATE, commonLocalizer.localize(PdfLocalizationName.visaIssueDate), visaIssueDate);
        personalTable.addRowWithCode(VISA_EXPIRY_DATE, commonLocalizer.localize(PdfLocalizationName.visaExpirationDate), visaExpirationDate);

        return personalTable;
    }

    private CustomisedITextTable getEmployeeInformation(ProfileItem item, Integer requestId) {
        CustomisedITextTable employeeTable = new CustomisedITextTable();

        String employeeImageURL = hrmsServiceLocal.getEmployeeImageURL(requestId);
        String profilePhoto = employeeImageURL != null ? employeeImageURL : "";
        String title = escapeHtml(item.getTitle());
        String firstName = escapeHtml(item.getFirstName());
        String middleName = escapeHtml(item.getMiddleName());
        String lastName = escapeHtml(item.getLastName());
        String employeeName = "";
        if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(middleName) && StringUtils.isNotEmpty(lastName)) {
            employeeName = lastName + " " + firstName + " " + middleName;
        } else if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(middleName)) {
            employeeName = firstName + " " + middleName;
        } else if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName)) {
            employeeName = firstName + " " + lastName;
        } else {
            employeeName = firstName;
        }
        String roleCodeAsString = "";
        String roleNameAsString = "";
        if (item.getEmployeeId() != null) {
            EdsEmployee edsEmployee = employeeManager.get(item.getEmployeeId());
            if (edsEmployee != null) {
                roleCodeAsString = !ServerUtils.isNullOrEmpty(edsEmployee.getRolesCodeAsString()) ? edsEmployee.getRolesCodeAsString().replace("'", "") : "";
                roleNameAsString = edsEmployee.getRolesNameAsString();
            }
        }

        String dateOfBirth = "";
        if (userManager.getUser().getObjectID().equals(requestId)) {
            dateOfBirth = item.getDob() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getDob().getNonConvertedDate())) : dateFormat(item.getDob().getNonConvertedDate())) : "";
        }
        String gender = escapeHtml(item.getGender());
        String maritalStatus = escapeHtml(item.getMartialStatus());

        String emplyeeCode = escapeHtml(item.getEmpCode());
        String employeeMode = escapeHtml(item.getEmpMode());
        String supervisor = escapeHtml(item.getReportsTo());
        String termsOfContracts = item.getTermsOfContract() != null ? item.getTermsOfContract().toString() : "";

        EdsDepartment edsDepartment = item.getPmDepartmentID() != null ? departmentManager.get(item.getPmDepartmentID()) : null;
        String departmentEn = "";
        String departmentRu = "";
        String departmentUz = "";
        if (edsDepartment != null && edsDepartment.getLocale() != null) {
            departmentEn = edsDepartment.getLocale().getEnglish();
            departmentRu = edsDepartment.getLocale().getRussian();
            departmentUz = edsDepartment.getLocale().getUzbek();
        }

        String parentDepartment = "";
        String parentDepartmentUz = "";
        Integer parentDepartmentId = null;
        if (item.getPmDepartmentID() != null && departmentService.getTeam(item.getPmDepartmentID()) != null && departmentService.getTeam(item.getPmDepartmentID()).getParentDepartment() != null) {
            parentDepartmentId = departmentService.getTeam(item.getPmDepartmentID()).getParentDepartment().getId();
        }

        String parentDepartment1 = "";
        String parentDepartment1Uz = "";
        Integer parentDepartment1Id = null;
        if (parentDepartmentId != null && departmentService.getTeam(parentDepartmentId) != null && departmentService.getTeam(parentDepartmentId).getParentDepartment() != null) {
            parentDepartment1Id = departmentService.getTeam(parentDepartmentId).getParentDepartment().getId();
        }

        String parentDepartment2 = "";
        String parentDepartment2Uz = "";

        if (item.getPmDepartmentID() != null && departmentService.getTeam(item.getPmDepartmentID()) != null && departmentService.getTeam(item.getPmDepartmentID()).getParentDepartment() != null) {
            if (departmentService.getTeam(item.getPmDepartmentID()).getParentDepartment().getName() != null) {
                parentDepartment = departmentService.getTeam(item.getPmDepartmentID()).getParentDepartment().getName();
                if (departmentService.getTeam(item.getPmDepartmentID()).getParentDepartment().getId() != null &&
                        departmentManager.getDeparmentLocalization(departmentService.getTeam(item.getPmDepartmentID()).getParentDepartment().getId()) != null &&
                        departmentManager.getDeparmentLocalization(departmentService.getTeam(item.getPmDepartmentID()).getParentDepartment().getId()).getUzbek() != null) {
                    parentDepartmentUz = departmentManager.getDeparmentLocalization(departmentService.getTeam(item.getPmDepartmentID()).getParentDepartment().getId()).getUzbek();
                }

                if (departmentService.getTeam(parentDepartmentId).getParentDepartment() != null) {
                    if (departmentService.getTeam(parentDepartmentId).getParentDepartment().getName() != null) {
                        parentDepartment1 = departmentService.getTeam(parentDepartmentId).getParentDepartment().getName();
                        if (departmentService.getTeam(parentDepartmentId).getParentDepartment().getId() != null &&
                                departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartmentId).getParentDepartment().getId()) != null &&
                                departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartmentId).getParentDepartment().getId()).getUzbek() != null) {
                            parentDepartment1Uz = departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartmentId).getParentDepartment().getId()).getUzbek();
                        }

                        if (departmentService.getTeam(parentDepartment1Id).getParentDepartment() != null) {
                            if (departmentService.getTeam(parentDepartment1Id).getParentDepartment().getName() != null) {
                                parentDepartment2 = departmentService.getTeam(parentDepartment1Id).getParentDepartment().getName();
                                if (departmentService.getTeam(parentDepartment1Id).getParentDepartment().getId() != null &&
                                        departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartment1Id).getParentDepartment().getId()) != null &&
                                        departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartment1Id).getParentDepartment().getId()).getUzbek() != null) {
                                    parentDepartment2Uz = departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartment1Id).getParentDepartment().getId()).getUzbek();
                                }

                            }
                        }
                    }
                }
            }
        }


        EdsPosition edsPosition = item.getPositionId() != null ? positionManager.get(item.getPositionId()) : null;
        String positionEn = "";
        String positionRu = "";
        String positionUz = "";
        if (edsPosition != null && edsPosition.getLocale() != null) {
            positionEn = edsPosition.getLocale().getEnglish();
            positionRu = edsPosition.getLocale().getRussian();
            positionUz = edsPosition.getLocale().getUzbek();
        }

        String departmentName = escapeHtml(item.getDepartment());
        String position = escapeHtml(item.getPosition());
        String location = escapeHtml(item.getLocationName());
        String hireDate = item.getHireDate() != null ? dateFormat(item.getHireDate().getNonConvertedDate()) : "";
        String resignationDate = item.getFireDate() != null ? dateFormat(item.getFireDate().getNonConvertedDate()) : "";
        String qualification = escapeHtml(item.getQualificationName());
        String primaryEmail = escapeHtml(item.getPrimaryEmail());

        employeeTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        employeeTable.addRowWithCode(EMPLOYEE_CODE, commonLocalizer.localize(PdfLocalizationName.employeeCode), emplyeeCode);
        employeeTable.addRowWithCode(EMPLOYEE_NAME, commonLocalizer.localize(PdfLocalizationName.employee), employeeName);
        employeeTable.addRowWithCode("EMPLOYEE_FIRST_NAME", "EMPLOYEE_FIRST_NAME", StringUtils.isNotEmpty(firstName) ? firstName : "");
        employeeTable.addRowWithCode("EMPLOYEE_LAST_NAME", "EMPLOYEE_LAST_NAME", StringUtils.isNotEmpty(lastName) ? lastName : "");
        employeeTable.addRowWithCode("EMPLOYEE_MIDDLE_NAME", "EMPLOYEE_MIDDLE_NAME", StringUtils.isNotEmpty(middleName) ? middleName : "");
        employeeTable.addRowWithCode(EMPLOYEE_PHOTO, "", profilePhoto);
        employeeTable.addRowWithCode(TITLE, commonLocalizer.localize(PdfLocalizationName.title), title);
        employeeTable.addRowWithCode(POSITION, commonLocalizer.localize(PdfLocalizationName.position), position);
        employeeTable.addRowWithCode(EMPLOYMENT_MODE, commonLocalizer.localize(PdfLocalizationName.employmentMode), employeeMode);
        employeeTable.addRowWithCode(DATE_OF_BIRTH, commonLocalizer.localize(PdfLocalizationName.dateOfBirth), dateOfBirth);
        employeeTable.addRowWithCode(MARITAL_STATUS, commonLocalizer.localize(PdfLocalizationName.maritalStatus), maritalStatus);
        employeeTable.addRowWithCode(SUPERVISOR, commonLocalizer.localize(PdfLocalizationName.supervisor), supervisor);
        employeeTable.addRowWithCode(DEPARTMENT, commonLocalizer.localize(PdfLocalizationName.department), departmentName);
        employeeTable.addRowWithCode(PARENT_NAME, commonLocalizer.localize(PdfLocalizationName.reportsTo), parentDepartment != null ? escapeHtml(parentDepartment) : "");
        employeeTable.addRowWithCode("PARENT_NAME_UZ", "PARENT_NAME_UZ", parentDepartmentUz != null ? escapeHtml(parentDepartmentUz) : "");
        employeeTable.addRowWithCode("PARENT_NAME1", "PARENT_NAME1", parentDepartment1 != null ? escapeHtml(parentDepartment1) : "");
        employeeTable.addRowWithCode("PARENT_NAME1_UZ", "PARENT_NAME1_UZ", parentDepartment1Uz != null ? escapeHtml(parentDepartment1Uz) : "");
        employeeTable.addRowWithCode("PARENT_NAME2", "PARENT_NAME2", parentDepartment2 != null ? escapeHtml(parentDepartment2) : "");
        employeeTable.addRowWithCode("PARENT_NAME2_UZ", "PARENT_NAME2_UZ", parentDepartment2Uz != null ? escapeHtml(parentDepartment2Uz) : "");

        employeeTable.addRowWithCode("DEPARTMENT_EN", "", departmentEn);
        employeeTable.addRowWithCode("DEPARTMENT_RU", "", departmentRu);
        employeeTable.addRowWithCode("DEPARTMENT_UZ", "", departmentUz);

        employeeTable.addRowWithCode("POSITION_EN", "", positionEn);
        employeeTable.addRowWithCode("POSITION_RU", "", positionRu);
        employeeTable.addRowWithCode("POSITION_UZ", "", positionUz);

        employeeTable.addRowWithCode(LOCATION, propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), location);
        employeeTable.addRowWithCode(HIRE_DATE, commonLocalizer.localize(PdfLocalizationName.hireDateField), hireDate);
        employeeTable.addRowWithCode(RESIGNATION_DATE, commonLocalizer.localize(PdfLocalizationName.resignationDate), resignationDate);
        employeeTable.addRowWithCode(TERMS_OF_CONTRACT, commonLocalizer.localize(PdfLocalizationName.termsOfContact), termsOfContracts);
        employeeTable.addRowWithCode(QUALIFICATION, commonLocalizer.localize(PdfLocalizationName.qualification), qualification);
        employeeTable.addRowWithCode(PRIMARY_EMAIL, commonLocalizer.localize(PdfLocalizationName.email), primaryEmail);
        employeeTable.addRowWithCode(CONTACT_INFORMATION, commonLocalizer.localize(PdfLocalizationName.contactInformation), "");
        employeeTable.addRowWithCode(EMPLOYMENT_INFORMATION, commonLocalizer.localize(PdfLocalizationName.employmentInformation), "");
        employeeTable.addRowWithCode(BANK_ACCOUNT_INFORMATION, commonLocalizer.localize(PdfLocalizationName.bankAccountInformation), "");
        employeeTable.addRowWithCode(ADDRESS_INFORMATION, commonLocalizer.localize(PdfLocalizationName.addressInformation), "");
        employeeTable.addRowWithCode(ADDITIONAL_INFORMATION, commonLocalizer.localize(PdfLocalizationName.additionalInformation), "");
        employeeTable.addRowWithCode(GENDER, commonLocalizer.localize(PdfLocalizationName.gender), gender);
        employeeTable.addRowWithCode("EMPLOYEE_ROLE_CODE", "", roleCodeAsString);
        employeeTable.addRowWithCode("EMPLOYEE_ROLE_NAME", "", roleNameAsString);

        return employeeTable;
    }

    private CustomisedITextTable getCustomNumberAndDatesTable(StatisticsLeaveRequest employeeLeaveRequest, EmployeeViewItem employee) {
        CustomisedITextTable numAndDates = new CustomisedITextTable();

        Date backToWork = null;

        Object[] workingDate = null;
        if (employeeLeaveRequest.getEndDDate() != null) {
            backToWork = ServerUtils.addDays(employeeLeaveRequest.getEndDDate().getNonConvertedDate(), 1);
        }
        workingDate = attendanceRawDataManager.getWorkingDate(employeeLeaveRequest.getEmployeeId(), backToWork);
        if (workingDate != null) {
            while (workingDate[0].equals(true) || workingDate[1].equals(true)) {
                backToWork = ServerUtils.addDays(backToWork, 1);
                workingDate = attendanceRawDataManager.getWorkingDate(employeeLeaveRequest.getEmployeeId(), backToWork);
            }
        }

        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat dateFormatUz = new SimpleDateFormat("yyyy.MM.dd");
        String leaveType = escapeHtml(employeeLeaveRequest.getReason());
        String description = escapeHtml(employeeLeaveRequest.getDescription());
        String createDate = dateFormat.format(employeeLeaveRequest.getCreatedDate());
        String createDateUz = dateFormatUz.format(employeeLeaveRequest.getCreatedDate());

        String fromDate = escapeHtml(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(employeeLeaveRequest.getStartDDate().getNonConvertedDate(), true)) : longDateFormat(employeeLeaveRequest.getStartDDate().getNonConvertedDate(), true));
        String toDate = escapeHtml(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(employeeLeaveRequest.getEndDDate().getNonConvertedDate(), true)) : longDateFormat(employeeLeaveRequest.getEndDDate().getNonConvertedDate(), true));

        EdsCompany company = userManager.getUser().getCompany();
        String fromDateEn = escapeHtml(ServerUtils.longDateFormat(employeeLeaveRequest.getStartDDate().getNonConvertedDate(), company, false));
        String toDateEn = escapeHtml(ServerUtils.longDateFormat(employeeLeaveRequest.getEndDDate().getNonConvertedDate(), company, false));

        String shortFromDate = escapeHtml(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(employeeLeaveRequest.getStartDDate().getNonConvertedDate())) : dateFormat(employeeLeaveRequest.getStartDDate().getNonConvertedDate(), true));
        String shortToDate = escapeHtml(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(employeeLeaveRequest.getEndDDate().getNonConvertedDate())) : dateFormat(employeeLeaveRequest.getEndDDate().getNonConvertedDate(), true));

        String fromDateUz = escapeHtml(ServerUtils.convertToUzbDateFormat(ServerUtils.longDateFormat(employeeLeaveRequest.getStartDDate().getNonConvertedDate(), company, false)));
        String toDateUz = escapeHtml(ServerUtils.convertToUzbDateFormat(ServerUtils.longDateFormat(employeeLeaveRequest.getEndDDate().getNonConvertedDate(), company, false)));

        String shortFromDateUz = escapeHtml(ServerUtils.convertToUzbDateFormat(dateFormat(employeeLeaveRequest.getStartDDate().getNonConvertedDate(), true)));
        String shortToDateUz = escapeHtml(ServerUtils.convertToUzbDateFormat(dateFormat(employeeLeaveRequest.getEndDDate().getNonConvertedDate(), true)));

        String startDate = dateFormat.format(employeeLeaveRequest.getStartDDate().getNonConvertedDate());
        String endDate = dateFormat.format(employeeLeaveRequest.getEndDDate().getNonConvertedDate());

        String startDateUz = dateFormatUz.format(employeeLeaveRequest.getStartDDate().getNonConvertedDate());
        String endDateUz = dateFormatUz.format(employeeLeaveRequest.getEndDDate().getNonConvertedDate());

        DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
        String backToWorkDate = escapeHtml(dateFormat1.format(backToWork));
        String duration = escapeHtml(employeeLeaveRequest.getDuration());
        StringBuilder backupEmployeeInfo = new StringBuilder();

        if (employeeLeaveRequest.getBackupEmployee() != null) {
            employeeLeaveRequest.getBackupEmployee().stream()
                    .map(b -> b.getParentBackupEmployee() != null ? backupEmployeeInfo.append(b.getParentBackupEmployee().getExactEmployee().getName()) : "");
        }
        String backupEmployee = backupEmployeeInfo.toString();

        ArrayList<String> backupEmployeeName = new ArrayList<>();

        ArrayList<String> positionEn = new ArrayList<>();
        ArrayList<String> positionRu = new ArrayList<>();
        ArrayList<String> positionUz = new ArrayList<>();


        ArrayList<String> departmentEn = new ArrayList<>();
        ArrayList<String> departmentRu = new ArrayList<>();
        ArrayList<String> departmentUz = new ArrayList<>();

        ArrayList<String> parentDepartment = new ArrayList<>();
        ArrayList<String> parentDepartmentUz = new ArrayList<>();

        ArrayList<String> parentDepartment1 = new ArrayList<>();
        ArrayList<String> parentDepartment1Uz = new ArrayList<>();

        ArrayList<String> parentDepartment2 = new ArrayList<>();
        ArrayList<String> parentDepartment2Uz = new ArrayList<>();

        EdsSickRequest sickRequest = null;
        if (employeeLeaveRequest.getObjectID() != null) {
            sickRequest = sickRequestManager.get(employeeLeaveRequest.getObjectID());
        }
        if (sickRequest != null) {
            List<EdsBackupEmployee> backupEmployeesBySickRequestId = backupEmployeeManager.getBackupEmployeesBySickRequestId(sickRequest.getObjectID());
            for (EdsBackupEmployee edsBackupEmployee : backupEmployeesBySickRequestId) {
                EdsEmployee employee1 = edsBackupEmployee.getEmployee();

                if (employee1.getLastName() != null && employee1.getFirstName() != null && employee1.getMiddleName() != null) {
                    backupEmployeeName.add(employee1.getLastName() + " " + employee1.getFirstName() + " " + employee1.getMiddleName());
                }

                EdsPosition edsPosition = employee1.getPosition() != null && employee1.getPosition().getObjectID() != null ? positionManager.get(employee1.getPosition().getObjectID()) : null;

                if (edsPosition != null && edsPosition.getLocale() != null) {
                    positionEn.add(edsPosition.getLocale().getEnglish());
                    positionRu.add(edsPosition.getLocale().getRussian());
                    positionUz.add(edsPosition.getLocale().getUzbek());
                }

                EdsDepartment edsDepartment = employee1.getTeam() != null && employee1.getTeam().getObjectID() != null ? departmentManager.get(employee1.getTeam().getObjectID()) : null;

                if (edsDepartment != null && edsDepartment.getLocale() != null) {
                    departmentEn.add(edsDepartment.getLocale().getEnglish());
                    departmentRu.add(edsDepartment.getLocale().getRussian());
                    departmentUz.add(edsDepartment.getLocale().getUzbek());
                }

                Integer parentDepartmentId = null;
                if (employee1.getEmployeeDepartment() != null && employee1.getEmployeeDepartment().getTeam() != null && employee1.getEmployeeDepartment().getTeam().getObjectID() != null && departmentService.getTeam(employee1.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment() != null) {
                    parentDepartmentId = departmentService.getTeam(employee1.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getId();
                }

                Integer parentDepartment1Id = null;
                if (parentDepartmentId != null && departmentService.getTeam(parentDepartmentId) != null && departmentService.getTeam(parentDepartmentId).getParentDepartment() != null) {
                    parentDepartment1Id = departmentService.getTeam(parentDepartmentId).getParentDepartment().getId();
                }

                EdsEmployeeDepartment edsEmployeeDepartment = employee1.getEmployeeDepartment();
                EdsDepartment edsTeam = edsEmployeeDepartment != null ? edsEmployeeDepartment.getTeam() : null;
                TeamListItem teamListItem = edsTeam != null && edsTeam.getObjectID() != null ? departmentService.getTeam(edsTeam.getObjectID()) : null;
                SelectItem departmentOfParent = teamListItem != null ? teamListItem.getParentDepartment() : null;
                String departmentOfParentName = departmentOfParent != null ? departmentOfParent.getName() : "";

                if (!ServerUtils.isNullOrEmpty(departmentOfParentName)) {
                    if (departmentService.getTeam(employee1.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getName() != null) {
                        parentDepartment.add(departmentOfParentName);

                        EdsReferenceLocale edsReferenceLocale = departmentOfParent != null && departmentOfParent.getId() != null ? departmentManager.getDeparmentLocalization(departmentOfParent.getId()) : null;
                        String departmentOfParentNameUzbek = edsReferenceLocale != null ? edsReferenceLocale.getUzbek() : "";
                        parentDepartmentUz.add(departmentOfParentNameUzbek);

                        TeamListItem parentParentTeamListItem = parentDepartmentId != null ? departmentService.getTeam(parentDepartmentId) : null;
                        SelectItem parentParentDepartment = parentParentTeamListItem != null ? parentParentTeamListItem.getParentDepartment() : null;

                        if (parentParentDepartment != null && parentParentDepartment.getName() != null) {
                            parentDepartment1.add(parentParentDepartment.getName());

                            EdsReferenceLocale edsReferenceLocale1 = parentParentDepartment.getId() != null ? departmentManager.getDeparmentLocalization(parentParentDepartment.getId()) : null;
                            String parentParentDepartmentNameUz = edsReferenceLocale1 != null ? edsReferenceLocale1.getUzbek() : "";
                            parentDepartment1Uz.add(parentParentDepartmentNameUz);

                            TeamListItem parentParentParentTeamListItem = parentDepartment1Id != null ? departmentService.getTeam(parentDepartment1Id) : null;
                            SelectItem parentParentParentDepartment = parentParentParentTeamListItem != null ? parentParentParentTeamListItem.getParentDepartment() : null;

                            if (parentParentParentDepartment != null) {
                                if (parentParentParentDepartment.getName() != null) {
                                    parentDepartment2.add(parentParentParentDepartment.getName());
                                    EdsReferenceLocale edsReferenceLocale2 = parentParentParentDepartment.getId() != null ? departmentManager.getDeparmentLocalization(parentParentParentDepartment.getId()) : null;
                                    if (edsReferenceLocale2 != null) {
                                        parentDepartment2Uz.add(edsReferenceLocale2.getUzbek());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        String type = escapeHtml(employeeLeaveRequest.getType());
        String approver = employeeLeaveRequest.getCurrentApprover() != null ? escapeHtml(employeeLeaveRequest.getCurrentApprover().toString()) : "";
        String status = employeeLeaveRequest.getOverallStatus() != null ? escapeHtml(employeeLeaveRequest.getOverallStatus().getName()) : "";
        String employeeName = escapeHtml(employeeLeaveRequest.getEmployee());
        String department = escapeHtml(employeeLeaveRequest.getDepartment());
        String takenFromAL = "";


        String employeeCode = escapeHtml(employee.getEmployeeCode());
        String supervisorName = escapeHtml(employee.getSupervisor());
        String supervisorCode = escapeHtml(employee.getSupervisorCode());

        String position = escapeHtml(employee.getPosition());
        String phone;
        phone = !Objects.equals(employee.getHomePhone(), PA_NOT_AVAILABLE_STRING) ? escapeHtml(employee.getHomePhone()) : "";
        phone += !Objects.equals(employee.getMobilePhone(), PA_NOT_AVAILABLE_STRING) ? " " + escapeHtml(employee.getMobilePhone()) : "";
        phone += !Objects.equals(employee.getWorkPhone(), PA_NOT_AVAILABLE_STRING) ? " " + escapeHtml(employee.getWorkPhone()) : "";
        String annualLeaveAllowance = escapeHtml(employeeLeaveRequest.getAnnualAllowance());
        String leaveTypeCode = escapeHtml(employeeLeaveRequest.getReasonCode());
        String leaveRequestNumber = employeeLeaveRequest.getNumberData() != null ? employeeLeaveRequest.getNumberData().getFirstNumberString() : "";

        String employeeFirstName = escapeHtml(employee.getFirstName());
        String employeeLastName = escapeHtml(employee.getLastName());
        String employeeMiddleName = escapeHtml(employee.getMiddleName());

        numAndDates.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        numAndDates.addRowWithCode(PDFConstants.EMPLOYEE_CODE, commonLocalizer.localize(PdfLocalizationName.employeeCode), employeeCode);
        numAndDates.addRowWithCode(EMPLOYEE_NAME, commonLocalizer.localize(PdfLocalizationName.employee), employeeName);
        numAndDates.addRowWithCode("EMPLOYEE_FIRST_NAME", commonLocalizer.localize(PdfLocalizationName.firstName), employeeFirstName);
        numAndDates.addRowWithCode("EMPLOYEE_LAST_NAME", commonLocalizer.localize(PdfLocalizationName.lastName), employeeLastName);
        numAndDates.addRowWithCode("EMPLOYEE_MIDDLE_NAME", commonLocalizer.localize(PdfLocalizationName.middleName), employeeMiddleName);
        numAndDates.addRowWithCode(PDFConstants.POSITION, commonLocalizer.localize(PdfLocalizationName.position), position);
        numAndDates.addRowWithCode(PDFConstants.ITEM_DEPARTMENT, commonLocalizer.localize(PdfLocalizationName.departmentEmployee), department);
        numAndDates.addRowWithCode(PDFConstants.SUPERVISOR, commonLocalizer.localize(PdfLocalizationName.supervisor), supervisorName);
        numAndDates.addRowWithCode(PDFConstants.SUPERVISOR_CODE, commonLocalizer.localize(PdfLocalizationName.supervisorCode), supervisorCode);
        numAndDates.addRowWithCode(PDFConstants.PHONE_NUMBER, commonLocalizer.localize(PdfLocalizationName.phone), phone);
        numAndDates.addRowWithCode(PDFConstants.LEAVE_TYPE, commonLocalizer.localize(PdfLocalizationName.leaveType), leaveType);
        numAndDates.addRowWithCode(PDFConstants.DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description), description);
        numAndDates.addRowWithCode(PDFConstants.CREATED_DATE, commonLocalizer.localize(PdfLocalizationName.createdDate), createDate);
        numAndDates.addRowWithCode(PDFConstants.FROM_DATE, commonLocalizer.localize(PdfLocalizationName.fromDate), fromDate);
        numAndDates.addRowWithCode(PDFConstants.TO_DATE, commonLocalizer.localize(PdfLocalizationName.toDate), toDate);
        numAndDates.addRowWithCode("FROM_DATE_UZ", commonLocalizer.localize(PdfLocalizationName.fromDate), fromDateUz);
        numAndDates.addRowWithCode("TO_DATE_UZ", commonLocalizer.localize(PdfLocalizationName.toDate), toDateUz);
        numAndDates.addRowWithCode("FROM_DATE_EN", commonLocalizer.localize(PdfLocalizationName.fromDate), fromDateEn);
        numAndDates.addRowWithCode("TO_DATE_EN", commonLocalizer.localize(PdfLocalizationName.toDate), toDateEn);
        numAndDates.addRowWithCode("SHORT_FROM_DATE", commonLocalizer.localize(PdfLocalizationName.fromDate), shortFromDate);
        numAndDates.addRowWithCode("SHORT_TO_DATE", commonLocalizer.localize(PdfLocalizationName.toDate), shortToDate);
        numAndDates.addRowWithCode("SHORT_FROM_DATE_UZ", commonLocalizer.localize(PdfLocalizationName.fromDate), shortFromDateUz);
        numAndDates.addRowWithCode("SHORT_TO_DATE_UZ", commonLocalizer.localize(PdfLocalizationName.toDate), shortToDateUz);
        numAndDates.addRowWithCode("SHORT_START_DATE", commonLocalizer.localize(PdfLocalizationName.fromDate), startDate);
        numAndDates.addRowWithCode("SHORT_END_DATE", commonLocalizer.localize(PdfLocalizationName.toDate), endDate);
        numAndDates.addRowWithCode("SHORT_START_DATE_UZ", commonLocalizer.localize(PdfLocalizationName.fromDate), startDateUz);
        numAndDates.addRowWithCode("SHORT_END_DATE_UZ", commonLocalizer.localize(PdfLocalizationName.toDate), endDateUz);
        numAndDates.addRowWithCode(PDFConstants.BACK_TO_WORK_DATE, commonLocalizer.localize(PdfLocalizationName.toDate), backToWorkDate);
        numAndDates.addRowWithCode(PDFConstants.DURATION, commonLocalizer.localize(PdfLocalizationName.duration), duration);
        numAndDates.addRowWithCode(PDFConstants.TYPE, commonLocalizer.localize(PdfLocalizationName.type), type);
        numAndDates.addRowWithCode(PDFConstants.APPROVER, commonLocalizer.localize(PdfLocalizationName.approver), approver);
        numAndDates.addRowWithCode(PDFConstants.STATUS, commonLocalizer.localize(PdfLocalizationName.status), status);
        numAndDates.addRowWithCode(PDFConstants.TAKEN_FROM_ALLOWANCE, commonLocalizer.localize(PdfLocalizationName.takenFromAllowance), takenFromAL);
        numAndDates.addRowWithCode(PDFConstants.ANNUAL_LEAVE_ALLOWANCE_DAYS, commonLocalizer.localize(PdfLocalizationName.annualLeaveAllowance), annualLeaveAllowance);
        numAndDates.addRowWithCode(PDFConstants.LEAVE_TYPE_CODE, commonLocalizer.localize(PdfLocalizationName.code), leaveTypeCode);
        numAndDates.addRowWithCode("LEAVE_REQUEST_NUMBER", commonLocalizer.localize(PdfLocalizationName.number), leaveRequestNumber);
        numAndDates.addRowWithCode("BACKUP_EMPLOYEE", "", backupEmployee != null ? backupEmployee : "");
        for (int i = 0; i < backupEmployeeName.size(); i++) {
            numAndDates.addRowWithCode("BACKUP_EMPLOYEE_FULLNAME_" + i, "", backupEmployeeName.get(i) != null ? escapeHtml(backupEmployeeName.get(i)) : "");
        }
        for (int i = 0; i < positionUz.size(); i++) {
            numAndDates.addRowWithCode("BACKUP_EMPLOYEE_POSITION_UZ_" + i, "", positionUz.get(i) != null ? escapeHtml(positionUz.get(i)) : "");
        }
        for (int i = 0; i < departmentUz.size(); i++) {
            numAndDates.addRowWithCode("BACKUP_EMPLOYEE_DEPARTMENT_UZ_" + i, "", departmentUz.get(i) != null ? escapeHtml(departmentUz.get(i)) : "");
        }
        for (int i = 0; i < positionUz.size(); i++) {
            numAndDates.addRowWithCode("BACKUP_EMPLOYEE_POSITION_EN_" + i, "", positionEn.get(i) != null ? escapeHtml(positionEn.get(i)) : "");
        }
        for (int i = 0; i < departmentUz.size(); i++) {
            numAndDates.addRowWithCode("BACKUP_EMPLOYEE_DEPARTMENT_EN_" + i, "", departmentEn.get(i) != null ? escapeHtml(departmentEn.get(i)) : "");
        }
        for (int i = 0; i < parentDepartmentUz.size(); i++) {
            numAndDates.addRowWithCode("BACKUP_EMPLOYEE_PARENT_DEPARTMENT_UZ_" + i, "", parentDepartmentUz.get(i) != null ? escapeHtml(parentDepartmentUz.get(i)) : "");
        }
        for (int i = 0; i < parentDepartment1Uz.size(); i++) {
            numAndDates.addRowWithCode("BACKUP_EMPLOYEE_PARENT1_DEPARTMENT_UZ_" + i, "", parentDepartment1Uz.get(i) != null ? escapeHtml(parentDepartment1Uz.get(i)) : "");
        }
        for (int i = 0; i < parentDepartment2Uz.size(); i++) {
            numAndDates.addRowWithCode("BACKUP_EMPLOYEE_PARENT2_DEPARTMENT_UZ_" + i, "", parentDepartment2Uz.get(i) != null ? escapeHtml(parentDepartment2Uz.get(i)) : "");
        }


        return numAndDates;
    }

    private List<String> getLeaveRequestComment(LeaveRequestComment[] comments) {
        if (comments == null) {
            return Collections.emptyList();
        }
        List<String> leaveRequestComments = new ArrayList<>();
        for (LeaveRequestComment lrComment : comments) {
            String text = escapeHtml(lrComment.getText());
            leaveRequestComments.add(text);
        }
        return leaveRequestComments;
    }

    protected Object getDataClass(HttpServletRequest request) {
        final LeaveRequestObject requestObject = new LeaveRequestObject();
        final String objectID = request.getParameter("objectID");
        final String userID = request.getParameter("userID");
        String sessionId = request.getParameter("sessionId");

        if (objectID != null) {
            requestObject.setObjectID(Integer.valueOf(objectID));
        }
        if (userID != null) {
            requestObject.setUserID(Integer.valueOf(userID));
        }
        if (StringUtils.isNotBlank(request.getParameter("pdfTemplateID"))) {
            requestObject.setPdfTemplateID(Integer.valueOf(request.getParameter("pdfTemplateID")));
        }
        if (StringUtils.isNotBlank(sessionId)) {
            ServerSecurityContext.getInstance().setSessionId(sessionId);
        }
        requestObject.setSvg(request.getParameter("svg"));
        return requestObject;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        LeaveRequestObject requestObject = (LeaveRequestObject) dataClass;
        Integer requestId = requestObject.getObjectID();
        StatisticsLeaveRequest employeeLeaveRequest = availabilityService.getLeaveRequest(requestId);
        if (employeeLeaveRequest == null) {
            setFileName(commonLocalizer.localize(PdfLocalizationName.leaveRequest));
        }
        String empName = escapeHtml(employeeLeaveRequest.getEmployee());
        String leaveReason = escapeHtml(employeeLeaveRequest.getReason());
        if (user == null) {
            setFileName(empName.concat("_").concat(leaveReason));
        }
        setFileName(empName.concat("_").concat(leaveReason).concat("_").concat(dateFormat(user.getUserDate())));
    }

    @Override
    protected String getTableName(Object dataClass) {
        LeaveRequestObject requestObject = (LeaveRequestObject) dataClass;
        Integer requestId = requestObject.getObjectID();
        StatisticsLeaveRequest employeeLeaveRequest = availabilityService.getLeaveRequest(requestId);
        if (employeeLeaveRequest == null) {
            return commonLocalizer.localize(PdfLocalizationName.leaveRequest);
        }
        String empName = escapeHtml(employeeLeaveRequest.getEmployee());
        String leaveReason = escapeHtml(employeeLeaveRequest.getReason());
        return empName.concat(" - ").concat(leaveReason).concat(" - ").concat(commonLocalizer.localize(PdfLocalizationName.leaveRequest));
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.LEAVE_REQUEST;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object != null && object instanceof LeaveRequestObject) {
            return ((LeaveRequestObject) object).getPdfTemplateID();
        }
        return null;
    }
}
