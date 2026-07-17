package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.dashboard.EdsDashboardComponents;
import com.edatasite.workforce.core.domain.dashboard.EdsDefaultComponents;
import com.edatasite.workforce.core.domain.dashboard.EdsUserDashboardSettings;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardService;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.dashboard.DashboardComponentsManager;
import com.edatasite.workforce.gwt.core.server.db.dashboard.UserDashboardSettingsManager;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.PagedResultDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.google.gwt.i18n.client.DateTimeFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Tag(name = "Dashboard widget", description = "Dashboard widget API")
@RestController
@RequestMapping(value = "/dashboard-widgets", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiDashboardWidgetControllerV3 {
    private final DashboardWidgetService dashboardWidgetService;
    private final ModuleDashboardService moduleDashboardService;
    private final UserManager userManager;
    private final UserEmailSettingsManager userEmailSettingsManager;
    private final DashboardComponentsManager dashboardComponentsManager;
    private final UserDashboardSettingsManager userDashboardSettingsManager;

    public ApiDashboardWidgetControllerV3(DashboardWidgetService dashboardWidgetService,
                                          ModuleDashboardService moduleDashboardService,
                                          UserManager userManager,
                                          UserEmailSettingsManager userEmailSettingsManager,
                                          DashboardComponentsManager dashboardComponentsManager,
                                          UserDashboardSettingsManager userDashboardSettingsManager) {
        this.dashboardWidgetService = dashboardWidgetService;
        this.moduleDashboardService = moduleDashboardService;
        this.userManager = userManager;
        this.userEmailSettingsManager = userEmailSettingsManager;
        this.dashboardComponentsManager = dashboardComponentsManager;
        this.userDashboardSettingsManager = userDashboardSettingsManager;
    }

    @Operation(summary = "Get dashboard widgets")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Object> getDashboardWidgets(@RequestParam(value = "id", required = false) Integer id,
                                                @RequestParam(value = "url", required = false) String url) throws RestException {
        if (id == null && url == null) {
            throw new RestException("Dashboard id is required", "Dashboard id is required", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (id == null && url.contains("_")) {
            String[] s = url.split("_");
            try {
                id = Integer.parseInt(s[s.length - 1]);
            } catch (NumberFormatException e) {
                //ignore
            }
        }
        if (id == null) {
            throw new RestException("Dashboard id is required", "Dashboard id is required", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsUser loggedUser = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(loggedUser);

        List<DashboardComponentItem> userDashboardSettingsItems = moduleDashboardService.getUserDashboardSettings(id).getActiveComponents();
        List<Object> chartData = userDashboardSettingsItems.stream()
                .map(c -> {
                    try {
                        return generateComponent(c, userSettings);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .toList();
        return ResultTO.success(chartData);
    }

    @Operation(summary = "Get single dashboard widget detail by widget id")
    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Object> getWidgetDetail(
            @RequestParam(value = "dashboardUrl") String dashboardUrl,
            @RequestParam(value = "id") Integer id) throws RestException {
        Integer dashboardId = parseDashboardId(dashboardUrl);
        EdsDashboardComponents dc = dashboardComponentsManager.getByIdAndDashboardId(id, dashboardId);
        if (dc == null) {
            throw new RestException("Widget not found", "Widget not found", ApiConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        EdsUser loggedUser = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(loggedUser);
        return ResultTO.success(generateComponent(dc.getRPC(), userSettings));
    }

    @Operation(summary = "Get widget titles and ids by dashboard url")
    @GetMapping(value = "/widget-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<PagedResultDTO<IdName>> getWidgetList(
            @RequestParam(value = "dashboardUrl") String dashboardUrl,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) throws RestException {
        Integer dashboardId = parseDashboardId(dashboardUrl);
        EdsUser loggedUser = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(loggedUser);
        String languageCode = userSettings != null ? userSettings.getInternationalization() : "";

        List<EdsUserDashboardSettings> userSettingsList = userDashboardSettingsManager.getListWithLocalizationByDashboardIdAndUserId(dashboardId, loggedUser.getObjectID());
        if (userSettingsList != null && !userSettingsList.isEmpty()) {
            long totalCount = userSettingsList.size();
            List<IdName> result = userSettingsList.stream()
                    .skip((long) page * size)
                    .limit(size)
                    .map(s -> new IdName(s.getObjectID(), title(s, languageCode)))
                    .toList();
            return ResultTO.success(new PagedResultDTO<>(result, totalCount));
        }

        List<EdsDashboardComponents> defaults = dashboardComponentsManager.getListWithComponentByDashboardId(dashboardId);
        long totalCount = defaults.size();
        List<IdName> result = defaults.stream()
                .skip((long) page * size)
                .limit(size)
                .map(c -> new IdName(c.getObjectID(), componentTitle(c.getComponent(), languageCode)))
                .toList();
        return ResultTO.success(new PagedResultDTO<>(result, totalCount));
    }

    private String title(EdsUserDashboardSettings settings, String languageCode) {
        if (settings.getComponentName() != null) {
            return settings.getComponentName();
        }
        return componentTitle(settings.getComponent(), languageCode);
    }

    private String componentTitle(EdsDefaultComponents component, String languageCode) {
        if (component == null) {
            return "";
        }
        if (component.getKpiWidget() != null && component.getKpiWidget().getCustomFormLocalization() != null) {
            return component.getKpiWidget().getCustomFormLocalization().getNameLocalization(languageCode);
        }
        if (component.getReport() != null && component.getReport().getChartConfig() != null
                && component.getReport().getChartConfig().getCustomFormLocalization() != null) {
            return component.getReport().getChartConfig().getCustomFormLocalization().getNameLocalization(languageCode);
        }
        return component.getComponentName();
    }

    private Integer parseDashboardId(String dashboardUrl) throws RestException {
        if (!dashboardUrl.contains("_")) {
            throw new RestException("Dashboard id is required", "Dashboard id is required", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }
        String[] parts = dashboardUrl.split("_");
        try {
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            throw new RestException("Dashboard id is required", "Dashboard id is required", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }

    private Object generateComponent(DashboardComponentItem componentConf, EdsUserEmailSettings userSettings) {
        if (componentConf == null) {
            return null;
        }
        if (Constants.DASHBOARD_WIDGET_CODE.TODO_LIST.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getTodoListRpc();
        } else if (Constants.DASHBOARD_WIDGET_CODE.MY_CONTACTS.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getContactData();
        } else if (Constants.DASHBOARD_WIDGET_CODE.INCOME_VS_EXPENSE.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getIncomeExpensesData(new DateNonConvertable(new Date()));
        } else if (Constants.DASHBOARD_WIDGET_CODE.AGED_REPORTS.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getAgingData(new DateNonConvertable(new Date()));
        } else if (Constants.DASHBOARD_WIDGET_CODE.PROJET_OVERVIEW.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.geProjectOverviewData();
        } else if (Constants.DASHBOARD_WIDGET_CODE.PROJECT_DUE_THIS_MONTH.equals(componentConf.getComponentCode())) {
            Date startTime = DateUtil.getMonthFirstDay(new Date());
            Date end = DateUtil.getMonthLastDate(new Date());

            Date startDate = new Date(startTime.getYear(), startTime.getMonth(), startTime.getDate(), 0, 0, 0);
            Date endDate = new Date(end.getYear(), end.getMonth(), end.getDate(), 23, 59, 59);

            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStartDate(startDate);
            fp.setEndDate(endDate);
            fp.setStart(0);
            fp.setLimit(20);
            fp.setShowProject(true);

            return dashboardWidgetService.getProjectsByDate(fp);
        } else if (Constants.DASHBOARD_WIDGET_CODE.TASKS_DUE_TODAY.equals(componentConf.getComponentCode())) {
            Date date = new Date();
            Date startDate = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
            Date end = new Date(date.getYear(), date.getMonth(), date.getDate(), 23, 59, 59);

            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStartDate(startDate);
            fp.setEndDate(end);
            fp.setStart(0);
            fp.setLimit(20);
            return dashboardWidgetService.getTasksByDate(fp);
        } else if (Constants.DASHBOARD_WIDGET_CODE.UNAVAILABLE_EMPLOYEES_SUPERVISION.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getCustomReasons();
        } else if (Constants.DASHBOARD_WIDGET_CODE.MY_CALENDAR.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getMyCalendarDays(new DateNonConvertable(new Date()));
        } else if (Constants.DASHBOARD_WIDGET_CODE.GENDER_RATIO.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getEmployeesGenderRatio();
        } else if (Constants.DASHBOARD_WIDGET_CODE.EXPIRY_DOCUMENTS.equals(componentConf.getComponentCode()) || Constants.DASHBOARD_WIDGET_CODE.HOLIDAY.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getCompanyHolidaysForUser();
        } else if (Constants.DASHBOARD_WIDGET_CODE.PAYROLL_YTD.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getCompanyPaymentDeductionData();
        } else if (Constants.DASHBOARD_WIDGET_CODE.PAYROLL_EMPLOYEE_YTD.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getEmployeePaymentDeductionData();
        } else if (Constants.DASHBOARD_WIDGET_CODE.COMBO.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getComboData();
        } else if (Constants.DASHBOARD_WIDGET_CODE.LEAVE_REASON_STATUS.equals(componentConf.getComponentCode())) {
            ListingFilterParameter fp = new ListingFilterParameter();
            DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
            fp.setYear(Integer.parseInt(yearFormat.format(new Date())));
            return dashboardWidgetService.getLeaveRequestDays(fp);
        } else if (Constants.DASHBOARD_WIDGET_CODE.TIMESLOT.equals(componentConf.getComponentCode())) {
            return dashboardWidgetService.getEmpTimeslot();
        } else {
            if (componentConf.getReportId() != null && componentConf.getReportId() > 0) {
                if (componentConf.getReportWidgetId() == null) {
                    ChartData chartData = dashboardWidgetService.getDynamicComponentData(componentConf);
                    if (chartData != null && chartData.getConf() != null && chartData.getConf().getLocalization() != null && userSettings.getInternationalization() != null) {
                        switch (userSettings.getInternationalization()) {
                            case "en" ->
                                    chartData.getConf().setTitle(chartData.getConf().getLocalization().getEnglishName());
                            case "ar" ->
                                    chartData.getConf().setTitle(chartData.getConf().getLocalization().getArabicName());
                            case "ru" ->
                                    chartData.getConf().setTitle(chartData.getConf().getLocalization().getRussianName());
                            case "uz" ->
                                    chartData.getConf().setTitle(chartData.getConf().getLocalization().getUzbekName());
                        }
                    }
                    return chartData;
                } else if (componentConf.getReportWidgetId() != null) {
                    KpiWidgetData kpiWidgetData = dashboardWidgetService.getDynamicWidgetComponentData(componentConf);
                    if (kpiWidgetData != null && kpiWidgetData.getLocalization() != null && userSettings.getInternationalization() != null) {
                        switch (userSettings.getInternationalization()) {
                            case "en" ->
                                    kpiWidgetData.setChartDataTitle(kpiWidgetData.getLocalization().getEnglishName());
                            case "ar" ->
                                    kpiWidgetData.setChartDataTitle(kpiWidgetData.getLocalization().getArabicName());
                            case "ru" ->
                                    kpiWidgetData.setChartDataTitle(kpiWidgetData.getLocalization().getRussianName());
                            case "uz" ->
                                    kpiWidgetData.setChartDataTitle(kpiWidgetData.getLocalization().getUzbekName());
                        }
                    }
                    return kpiWidgetData;
                }
            }
        }
        return null;
    }
}
