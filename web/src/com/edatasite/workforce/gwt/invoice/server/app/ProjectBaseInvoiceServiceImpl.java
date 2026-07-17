package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsProjectPosition;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByAssigneeEntry;
import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByProjectEntry;
import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByTaskEntry;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProjectEmployeeStruct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProjectEmployeeTaskStruct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProjectEmployeeValue;
import com.edatasite.workforce.gwt.accounting.client.rpc.TimeSpentRateValue;
import com.edatasite.workforce.gwt.accounting.client.rpc.TotalCostHours;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectPositionManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.ProjectBaseInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 12.05.2009
 * Time: 17:04:25
 */
@Transactional
@Service("projectBaseInvoiceService")
public class ProjectBaseInvoiceServiceImpl implements ProjectBaseInvoiceService {

    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private GenericSettingsManager genericSettings;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ProjectPositionManager projectPositionManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private CompanyCustomFieldsManager companyCustomFieldsManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectBaseInvoiceItem[] getClientRelatedProjectsForPBI(Integer clientID) {
        List<EdsProject> projects = projectManager.getProjectsByClientID(clientID);
        ProjectBaseInvoiceItem[] items = new ProjectBaseInvoiceItem[projects.size()];
        int i = 0; // Invoice qilingan projectlarni project based invoice qo`shishda dropdownda chiqarmaslik uchun qilindi
        for (EdsProject project : projects) {
            if (project.getLastInvoicedDate() != null) {
                List<EdsInvoiceItem> invoiceList = invoiceManager.getProjectInvoiceItems(project.getObjectID());
                if (invoiceList != null && !invoiceList.isEmpty()) {
                    Integer invoiceHours = 0;
                    for (EdsInvoiceItem invoice : invoiceList) {
                        invoiceHours += invoice.getQty() != null ? invoice.getQty().intValue() : 0;
                    }
                    Integer hours = timeSheetManager.getProjectApprovedTimesheetHours(project.getObjectID());
                    if (!hours.equals(invoiceHours)) {
                        items[i++] = new ProjectBaseInvoiceItem(project.getObjectID(), project.getName(), project.getLastInvoicedDate() != null ? new Date(project.getLastInvoicedDate().getTime()) : null);
                    }
                } else {
                    items[i++] = new ProjectBaseInvoiceItem(project.getObjectID(), project.getName(), project.getLastInvoicedDate() != null ? new Date(project.getLastInvoicedDate().getTime()) : null);
                }
            } else {
                items[i++] = new ProjectBaseInvoiceItem(project.getObjectID(), project.getName(), project.getLastInvoicedDate() != null ? new Date(project.getLastInvoicedDate().getTime()) : null);
            }
        }
        return items;
    }

    // 1st

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectBaseData[] getDetailedInvoice(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled, Integer crmAccountId) {
        boolean isEmployeeAssignmentEnable = genericSettings.isSettingsEnabled(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE);
        return getProjectBaseData(projects, from, to, isMonthlyTimeSheetEnabled, isEmployeeAssignmentEnable, crmAccountId).toArray(new ProjectBaseData[]{});
    }

    // 2nd

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<GroupByAssigneeEntry> getGroupedByAssignee(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled) {  //grouping in java code may be redundant, consider grouping in queries
        boolean isEmployeeAssignmentEnable = genericSettings.isSettingsEnabled(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE);
        List<ProjectBaseData> data = getProjectBaseData(projects, from, to, isMonthlyTimeSheetEnabled, isEmployeeAssignmentEnable, null);
        Map<ProjectEmployeeStruct, ProjectEmployeeValue> map = new LinkedHashMap<>();
        ProjectEmployeeValue value;
        for (ProjectBaseData pe : data) {
            ProjectEmployeeStruct keyStruct = new ProjectEmployeeStruct(new SelectItem(pe.getProjectId(), pe.getProjectName()), new SelectItem(pe.getEmployeeId(), pe.getEmployeeName()));
            value = map.get(keyStruct);
            if (value == null) {
                value = new ProjectEmployeeValue(new ArrayList<>());
            }
            value.setClientChargeRate(pe.getClientChargeRate());
            value.setHourSpent(value.getHourSpent() + pe.getTimespent());
            value.add(new SelectItem(pe.getTaskId(), pe.getTaskName()));

            if (value.getEntryIds() == null) {
                value.setEntryIds(new Integer[0]);
            }
            List<Integer> entryIds = new ArrayList<>(Arrays.asList(value.getEntryIds()));
            entryIds.add(pe.getTimesheetEntryId());
            value.setEntryIds(entryIds.toArray(new Integer[]{}));
            value.setTimesheetDescription(pe.getTimesheetDescription());

            map.put(keyStruct, value);
        }
        ArrayList<GroupByAssigneeEntry> list = new ArrayList<>();
        for (Map.Entry<ProjectEmployeeStruct, ProjectEmployeeValue> entry : map.entrySet()) {
            ProjectEmployeeValue eValue = entry.getValue();
            if (isEmployeeAssignmentEnable) {
                eValue.setClientChargeRate(eValue.getClientChargeRate() * (eValue.getHourSpent() / 60.0));
                eValue.setHourSpent(60);
            }
            list.add(new GroupByAssigneeEntry(entry.getKey(), eValue));
        }
        return list;
    }

    // 3th

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<GroupByTaskEntry> getGroupedByTask(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled) { //grouping in java code may be redundant, consider grouping in queries
        boolean isEmployeeAssignmentEnable = genericSettings.isSettingsEnabled(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE);
        List<ProjectBaseData> data = getProjectBaseData(projects, from, to, isMonthlyTimeSheetEnabled, isEmployeeAssignmentEnable, null);
        Map<ProjectEmployeeTaskStruct, TimeSpentRateValue> map = new LinkedHashMap<>();
        TimeSpentRateValue value;
        for (ProjectBaseData pe : data) {
            ProjectEmployeeTaskStruct keyStruct = new ProjectEmployeeTaskStruct(new SelectItem(pe.getProjectId(), pe.getProjectName()), new SelectItem(pe.getEmployeeId(), pe.getEmployeeName()), new SelectItem(pe.getTaskId(), pe.getTaskName()));
            value = map.get(keyStruct);
            if (value == null) {
                value = new TimeSpentRateValue();
            }
            value.setClientChargeRate(pe.getClientChargeRate());
            value.setTimeSpent(value.getTimeSpent() + pe.getTimespent());

            if (value.getEntryIds() == null) {
                value.setEntryIds(new Integer[0]);
            }
            List<Integer> entryIds = new ArrayList<>(Arrays.asList(value.getEntryIds()));
            entryIds.add(pe.getTimesheetEntryId());
            value.setEntryIds(entryIds.toArray(new Integer[]{}));
            value.setTimesheetDescription(pe.getTimesheetDescription());

            map.put(keyStruct, value);
        }
        ArrayList<GroupByTaskEntry> list = new ArrayList<>();
        for (Map.Entry<ProjectEmployeeTaskStruct, TimeSpentRateValue> entry : map.entrySet()) {
            TimeSpentRateValue eValue = entry.getValue();
            if (isEmployeeAssignmentEnable) {
                eValue.setClientChargeRate(eValue.getClientChargeRate() * (eValue.getTimeSpent() / 60.0));
                eValue.setTimeSpent(60);
            }
            list.add(new GroupByTaskEntry(entry.getKey(), eValue));
        }
        return list;
    }

    // 4th

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<GroupByProjectEntry> getGroupedByProject(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled) {    //grouping in java code may be redundant, consider grouping in queries
        boolean isEmployeeAssignmentEnable = genericSettings.isSettingsEnabled(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE);
        List<ProjectBaseData> data = getProjectBaseData(projects, from, to, isMonthlyTimeSheetEnabled, isEmployeeAssignmentEnable, null);
        Map<SelectItem, TotalCostHours> map = new LinkedHashMap<>();
        TotalCostHours value;
        for (ProjectBaseData pe : data) {
            SelectItem key = new SelectItem(pe.getProjectId(), pe.getProjectName(), pe.getProjectDescription());
            value = map.get(key);
            if (value == null) {
                value = new TotalCostHours();
            }
            value.setTotalHours(value.getTotalHours() + pe.getTimespentInHours());
            value.setTotalCost(value.getTotalCost() + (pe.getTimespentInHours() * pe.getClientChargeRate()));

            if (value.getEntryIds() == null) {
                value.setEntryIds(new Integer[0]);
            }
            List<Integer> entryIds = new ArrayList<>(Arrays.asList(value.getEntryIds()));
            entryIds.add(pe.getTimesheetEntryId());
            value.setEntryIds(entryIds.toArray(new Integer[]{}));

            map.put(key, value);
        }
        ArrayList<GroupByProjectEntry> list = new ArrayList<>();
        for (Map.Entry<SelectItem, TotalCostHours> entry : map.entrySet()) {
            TotalCostHours eValue = entry.getValue();
            if (isEmployeeAssignmentEnable) {
                eValue.setTotalCost(eValue.getTotalCost() * (eValue.getTotalHours()));
            }
            list.add(new GroupByProjectEntry(entry.getKey(), eValue));
        }
        return list;
    }

    @Override
    public ArrayList<GroupByProjectEntry> getGroupedByProjectFE(Integer employeeId, Integer[] projects, DateNonConvertable from, DateNonConvertable to) {
        List<ProjectBaseData> data = accountingManager.getProjectBaseDataFE(employeeId, Arrays.asList(projects), from.getNonConvertedDate(), to.getNonConvertedDate());

        Map<SelectItem, TotalCostHours> map = new LinkedHashMap<>();
        TotalCostHours value;
        for (ProjectBaseData pe : data) {
            SelectItem key = new SelectItem(pe.getProjectId(), pe.getProjectName(), pe.getProjectDescription());
            value = map.get(key);
            if (value == null) {
                value = new TotalCostHours();
            }

            value.setTotalHours(value.getTotalHours() + pe.getTimespentInHours());
            value.setTotalCost(value.getTotalCost() + (pe.getTimespentInHours() * pe.getClientChargeRate()));
            value.setTotalWageCost(value.getTotalWageCost() + (pe.getTimespentInHours() * pe.getWageRate()));

            if (value.getEntryIds() == null) {
                value.setEntryIds(new Integer[0]);
            }
            List<Integer> entryIds = new ArrayList<>(Arrays.asList(value.getEntryIds()));
            entryIds.add(pe.getTimesheetEntryId());
            value.setEntryIds(entryIds.toArray(new Integer[]{}));

            map.put(key, value);
        }

        ArrayList<GroupByProjectEntry> list = new ArrayList<>();
        for (Map.Entry<SelectItem, TotalCostHours> entry : map.entrySet()) {
            TotalCostHours eValue = entry.getValue();
            GroupByProjectEntry gbpEntry = new GroupByProjectEntry(entry.getKey(), eValue);

            EdsProject project = projectManager.get(entry.getKey().getId());
            if (project.getClient() != null) {
                gbpEntry.setClient(project.getClient().getAsSelectItem());
            }
            list.add(gbpEntry);
        }
        return list;
    }

    private List<ProjectBaseData> getProjectBaseData(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled, boolean isEmployeeAssignmentEnable, Integer crmAccountId) {
        List<ProjectBaseData> data;
        String taskDiscountField = genericSettings.getValueByKey(GenericSettingsEnum.TASK_DISCOUNT_FIELD);
        HashMap<Integer, Double> taskDiscountMap = new HashMap<>();

        if (isMonthlyTimeSheetEnabled) {
            data = accountingManager.getProjectBaseMonthlyData(Arrays.asList(projects), from.getNonConvertedDate(), to.getNonConvertedDate());
        } else {
            data = accountingManager.getProjectBaseData(Arrays.asList(projects), from.getNonConvertedDate(), to.getNonConvertedDate());
        }
        boolean isBonnardEnabled = genericSettings.isSettingsEnabled(GenericSettingsEnum.ENABLE_BONNARD_CUSTOMIZATION);
        List<ProjectBaseData> result = new ArrayList<>();
        for (ProjectBaseData pd : data) {
            if (isBonnardEnabled) {
                getBonnardData(crmAccountId, pd);
            }
            String tsDescription = "Worked hours : " + pd.getTimespentInHours() + "\n";
            if (isMonthlyTimeSheetEnabled) {
                if (pd.getTotalDaysWorked() != null)
                    tsDescription = tsDescription.concat("Total worked days : " + pd.getTotalDaysWorked() + "\n");
                if (pd.getOvertime() != null)
                    tsDescription = tsDescription.concat("Overtime hours : " + pd.getOvertime() + "\n");
                if (pd.getWeekendOvertime() != null)
                    tsDescription = tsDescription.concat("Weekend overtime hours : " + pd.getWeekendOvertime() + "\n");
                if (pd.getHolidayOvertime() != null)
                    tsDescription = tsDescription.concat("Holiday overtime hours : " + pd.getHolidayOvertime() + "\n");
            }
            pd.setTimesheetDescription(tsDescription);

            if (isEmployeeAssignmentEnable) {
                EdsEmployee employee = employeeManager.get(pd.getEmployeeId());
                EdsProject project = projectManager.get(pd.getProjectId());
                EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(employee, project);
                EdsProjectPosition pPosition = projectPositionManager.getProjectPosition(pd.getProjectId(), projectEmployee.getPosition().getObjectID());

                Integer priceType = pPosition.getPriceType();
                if (priceType == null) {
                    priceType = 0;//set to default priceType (hour)
                }

                EdsEmployeePayrollSettings salary = employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), Constants.SALARY);
                // Set employee basic data
                if (salary != null) {
                    pd.setBasicSalary(Double.parseDouble(salary.getValue()));
                } else {
                    pd.setBasicSalary(0d);
                }

                pd.setPriceType(priceType);
                pd.setClientChargeRate(pPosition.getUnitPrice().doubleValue());
                pd.setOvertimeChargeRate(pPosition.getOvertimeRate());
                pd.setWeekendChargeRate(pPosition.getWeekendOvertimeRate());
                pd.setHolidayChargeRate(pPosition.getHolidayOvertimeRate());
                pd.setTsEntryDate((pd.getTsEntryDate() != null && pd.getTsEntryDate().getDate() != null) ? pd.getTsEntryDate().getNonConvertedDate() : from.getNonConvertedDate());

                calculateAmount(pd, from.getNonConvertedDate());
            }

            if (taskDiscountField != null && !taskDiscountField.isEmpty()) {
                if (taskDiscountMap.get(pd.getTaskId()) == null) {
                    Double discount = taskManager.getTaskFieldValue(pd.getTaskId(), taskDiscountField);
                    taskDiscountMap.put(pd.getTaskId(), discount != null ? discount : 0d);
                }
                pd.setDiscount(taskDiscountMap.get(pd.getTaskId()));
            }
            result.add(pd);
        }
        return result;
    }

    private void getBonnardData(Integer crmAccountId, ProjectBaseData pd) {
        EdsCompanyCustomFieldsSettings ccfs = companyCustomFieldsManager.getCompanyCustomFieldByEntityNameAndFieldName(CustomFieldSection.Project.getTitle(), "Currency");
        if (crmAccountId != null && ccfs != null && ccfs.getColumnCode() != null) {
            EdsProject proj = projectManager.get(pd.getProjectId());
            String currenyCode = proj.getProjectCustomFields().getStringValue(ccfs.getColumnCode());
            if (currenyCode == null || "".equals(currenyCode)) {
                return;
            }
            EdsCurrency currency = currencyManager.getCurrency(currenyCode);
            EdsCrmAccount crmAccount = crmAccountManager.get(crmAccountId);
            if (currency != null) {
                pd.setCurrency(currency.getAsSelectItem());
                if (crmAccount.getCurrency() != null && Objects.equals(crmAccount.getCurrency().getObjectID(), currency.getObjectID())) {
                    pd.setIgnoreExRate(true); //TODO Omon
                } else {
                    pd.setIgnoreExRate(true);
                }
            }
        }
    }

    private void calculateAmount(ProjectBaseData pd, Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        final BigDecimal MONTHDAYS = BigDecimal.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        final BigDecimal DAYHOURS = new BigDecimal(8);

        BigDecimal amount;
        BigDecimal rate = BigDecimal.valueOf(pd.getClientChargeRate());
        BigDecimal workedHours = pd.getTimespentInHours() != null ? BigDecimal.valueOf(pd.getTimespentInHours()) : BigDecimal.ZERO;
        BigDecimal workedDays = pd.getTotalDaysWorked() != null ? new BigDecimal(pd.getTotalDaysWorked()) : BigDecimal.ZERO;
        BigDecimal overtimeHours = pd.getOvertime() != null ? new BigDecimal(pd.getOvertime()) : BigDecimal.ZERO;
        BigDecimal wOvertimeHours = pd.getWeekendOvertime() != null ? new BigDecimal(pd.getWeekendOvertime()) : BigDecimal.ZERO;
        BigDecimal hOvertimeHours = pd.getHolidayOvertime() != null ? new BigDecimal(pd.getHolidayOvertime()) : BigDecimal.ZERO;
        //Overtime Rates
        BigDecimal otRate = pd.getOvertimeChargeRate() != null ? pd.getOvertimeChargeRate() : BigDecimal.ZERO;
        BigDecimal wotRate = pd.getWeekendChargeRate() != null ? pd.getWeekendChargeRate() : BigDecimal.ZERO;
        BigDecimal hotRate = pd.getHolidayChargeRate() != null ? pd.getHolidayChargeRate() : BigDecimal.ZERO;
        //For Markup
        BigDecimal basicSalary = pd.getBasicSalary() != null ? BigDecimal.valueOf(pd.getBasicSalary()) : BigDecimal.ZERO;

        if (pd.getPriceType().equals(0)) { //HOUR
            amount = rate.multiply(workedHours);
            amount = amount.add(rate.multiply(otRate).multiply(overtimeHours)); //calc overtime hours
            amount = amount.add(rate.multiply(wotRate).multiply(wOvertimeHours)); //calc weekendCvertime hours
            amount = amount.add(rate.multiply(hotRate).multiply(hOvertimeHours)); //calc holidayOvertime hours
            pd.setClientChargeRate(amount.doubleValue());
            pd.setTimespent(60);

        } else if (pd.getPriceType().equals(1)) { //MONTH and OT
            BigDecimal hourlyRate = rate.divide(MONTHDAYS, 10, RoundingMode.HALF_UP).divide(DAYHOURS, 10, RoundingMode.HALF_UP);

            amount = rate.multiply(workedDays).divide(MONTHDAYS, 10, RoundingMode.HALF_UP);
            amount = amount.add(hourlyRate.multiply(otRate).multiply(overtimeHours)); //calc overtime hours
            amount = amount.add(hourlyRate.multiply(wotRate).multiply(wOvertimeHours)); //calc weekendCvertime hours
            amount = amount.add(hourlyRate.multiply(hotRate).multiply(hOvertimeHours)); //calc holidayOvertime hours
            pd.setClientChargeRate(amount.doubleValue());
            pd.setTimespent(60);  //in minutes

        } else if (pd.getPriceType().equals(2) || pd.getPriceType().equals(3)) { //MARKUP and OT
            BigDecimal hourlyRate = basicSalary.divide(MONTHDAYS, 10, RoundingMode.HALF_UP).divide(DAYHOURS, 10, RoundingMode.HALF_UP);

            amount = rate.multiply(workedDays).divide(MONTHDAYS, 10, RoundingMode.HALF_UP);
            amount = amount.add(hourlyRate.multiply(otRate).multiply(overtimeHours)); //calc overtime hours
            amount = amount.add(hourlyRate.multiply(wotRate).multiply(wOvertimeHours)); //calc weekendCvertime hours
            amount = amount.add(hourlyRate.multiply(hotRate).multiply(hOvertimeHours)); //calc holidayOvertime hours
            pd.setClientChargeRate(amount.doubleValue());
            pd.setTimespent(60);  //in minutes

        } else if (pd.getPriceType().equals(4) || pd.getPriceType().equals(5)) { //DAY and OT
            BigDecimal hourlyRate = rate.divide(MONTHDAYS, 10, RoundingMode.HALF_UP).divide(DAYHOURS, 10, RoundingMode.HALF_UP);

            amount = rate.multiply(workedDays).divide(MONTHDAYS, 10, RoundingMode.HALF_UP);
            amount = amount.add(hourlyRate.multiply(otRate).multiply(overtimeHours)); //calc overtime hours
            amount = amount.add(hourlyRate.multiply(wotRate).multiply(wOvertimeHours)); //calc weekendCvertime hours
            amount = amount.add(hourlyRate.multiply(hotRate).multiply(hOvertimeHours)); //calc holidayOvertime hours
            pd.setClientChargeRate(amount.doubleValue());
            pd.setTimespent(60);  //in minutes
        }
    }
}
