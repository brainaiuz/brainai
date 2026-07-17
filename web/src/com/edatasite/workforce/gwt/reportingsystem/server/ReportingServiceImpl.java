package com.edatasite.workforce.gwt.reportingsystem.server;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.reporting.EdsReportTemplate;
import com.edatasite.workforce.core.domain.reporting.EdsReportTemplateCategory;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportTemplateCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportTemplateManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.profile.server.app.TemporaryRecurrenceService;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.*;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.xml.RpcConvertToXmlLocal;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.TelegramRecurrenceMessage;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.ReportGenerateTableRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.UserSecuritryRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.CoreServiceLocal;
import com.finnetlimited.reportservice.core.server.ReportingRecurrencePdfService;
import com.finnetlimited.reportservice.core.server.db.schema.FoldersManager;
import com.finnetlimited.reportservice.core.server.db.schema.ReportingManager;
import com.finnetlimited.reportservice.core.server.db.schema.TelegramReportingRecurrenceManager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsFolders;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsTelegramReportingScheduleRule;
import com.finnetlimited.reportservice.core.server.generate.GenerateReportToCsv;
import com.finnetlimited.reportservice.core.server.handler.ExcelReportHandler;
import com.finnetlimited.reportservice.core.server.utils.SqlQueryUtil;
import com.finnetlimited.reportservice.core.server.utils.StrUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRING_REPORT;

/**
 * Created by Virus on 9/11/14.
 */
@Service("reportingService")
public class ReportingServiceImpl implements ReportingService, ReportingSerivceLocal, Constants {

    private static final Logger log = LoggerFactory.getLogger(ReportingServiceImpl.class);
    private static final String NUMBER_MONEY_PERCENT_TIME = "number,money,percent,time";
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    protected WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("reportingLocalizer")
    protected WfmMessageSource reportingLocalizer;
    @Autowired
    private ReportingManager reportingManager;
    @Autowired
    private TelegramChatManager telegramChatManager;
    @Autowired
    private TelegramReportingRecurrenceManager telegramReportingRecurrenceManager;
    @Autowired
    private FoldersManager foldersManager;
    @Autowired
    private ReportTemplateCategoryManager reportTemplateCategoryManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private CoreService reportingCoreService;
    @Autowired
    private ReportTemplateManager reportTemplateManager;
    @Autowired
    private CoreServiceLocal reportinCoreServiceLocal;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private TemporaryRecurrenceService temporaryRecurrenceService;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ReportingRecurrencePdfService recurrencePdfService;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private ModuleLocalizeManager moduleLocalizeManager;
    @Autowired
    private ReportGroupColumnManager reportGroupColumnManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private LocaleManager localeManager;
    @Autowired
    private CompanySettingsManager companySettingsManager;
    private LinkedHashMap<String, String> groupCol;
    private HashMap<String, ArrayList<String>> groupColNew;

    private static String getColumnByName(String columnName, List<String> columns) {
        if (!StrUtils.isEmpty(columnName)) {
            for (String column : columns) {
                if (columnName.equals(column) || columnName.replace("_", ".").equals(column.replace("_", "."))) {
                    return column;
                }
            }
        }
        return null;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public RepRpc queryForReportsByCategory(Integer categoryId) {
        RepRpc result = new RepRpc();
        String rolesCodeAsString = getRolesCodeAsString();
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setCategoryID(categoryId);
        filter.setSubscriptionTypeName(getUser().getDomainName());
        filter.setCompanyID(getUser().getCompanyId());
        filter.setUserID(getUser().getUserId());
        filter.setRoles(rolesCodeAsString);
        List<Object[]> list = reportingManager.listObject(filter);
        ArrayList<SelectListRpc> favouriteReports = new ArrayList<>();
        for (Object[] item : list) {
            ReportItem reportItem = new ReportItem(item).invoke();
            SelectListRpc selectRpc = reportItem.toSelectListRpc();
            selectRpc.setName(referenceWfmMessageSource.localize((String) item[2], (String) item[1]));
            selectRpc.setDescription(referenceWfmMessageSource.localize(item[2] + "_Description", (String) item[14]));
            if (reportItem.getNewcategoryId() == null || reportItem.getNewcategoryName() == null || reportItem.getNewcategoryName().isEmpty()) {
                continue;
            }
            if (categoryId != 0) {
                if (result.getFolders().containsKey(reportItem.getFolderId())) {
                    FolderRpc folder = result.getFolders().get(reportItem.getFolderId());
                    folder.getReports().add(selectRpc);
                } else {
                    FolderRpc folder = reportItem.getFolder();
                    folder.setName(referenceWfmMessageSource.localize(item[2] + "_FolderName", (String) item[9]));
                    folder.setDescription(referenceWfmMessageSource.localize(item[2] + "_FolderDescription", (String) item[13]));
                    folder.getReports().add(selectRpc);
                    result.getFolders().put(folder.getId(), folder);
                }
            }
            if (categoryId == 0) {
                if (selectRpc.isFavourited()) {
                    favouriteReports.add(selectRpc);
                }
            }
        }
        if (categoryId == 0) {
            FolderRpc favFolder = new FolderRpc();
            favFolder.setId(0);
            favFolder.setName(commonLocalizer.localize("myFavouriteReports"));
            favFolder.setDescription(commonLocalizer.localize("favouriteDescription"));
            favFolder.setReports(favouriteReports);
            favFolder.setIcon("ficon--report-favorit");
            favFolder.setFolderSorder(0);
            result.getFolders().put(favFolder.getId(), favFolder);
        }
        if (result.getFolders() != null && !result.getFolders().isEmpty()) {
            LinkedHashMap<Integer, FolderRpc> fMap = new LinkedHashMap<>();
            result.getFolders().values().stream().sorted(Comparator.comparing(FolderRpc::getFolderSorder)).toList().forEach(f -> fMap.put(f.getId(), f));
            result.setFolders(fMap);
        }
        return result;
    }

    public ArrayList<SelectItem> lookupForReportItems(ListingFilterParameter filter) {
        filter.setSubscriptionTypeName(getUser().getDomainName());
        filter.setCompanyID(getUser().getCompanyId());
        filter.setUserID(getUser().getUserId());
        String rolesCodeAsString = getRolesCodeAsString();
        filter.setRoles(rolesCodeAsString);
        return reportingManager.getMinimizedReportList(filter);
    }

    @Override
    public KpiWidgetData getKpiWidgetData(ReportRpc report, boolean isFromRefresh) {
        return reportingCoreService.getKpiWidgetData(report, isFromRefresh);
    }

    @Override
    public void deleteReportingXMLTemplateFromCompany(Integer templateId) {
        reportingCoreService.deleteReportingXMLTemplateFromCompany(templateId, null);
        reportingCoreService.deleteReportingXMLTemplateFromCompany(templateId, null);
    }

    @Override
    public Integer createReportXmlTemplateFromFile(ImportFile importFile) {
        return reportinCoreServiceLocal.createReportXmlTemplate(importFile);
    }

    @Override
    @Transactional
    public ArrayList<String> deleteTelegramReportingRecurrenceRule(Integer ruleId) {
        return recurrenceService.deleteTelegramRecurrenceRule(ruleId);
    }

    @Override
    public ArrayList<String> saveTelegramReportingRecurrence(TelegramRecurrenceMessage telegramRecurrenceMessage, boolean isNew) {
        RecurrenceJobItem recurrenceJobItem = telegramRecurrenceMessage.getRecurrenceJobItem();
        if (!isNew) {
            Integer ruleId = telegramRecurrenceMessage.getRuleId();
            recurrenceService.deleteTelegramRecurrenceRule(ruleId);
            recurrenceJobItem.setObjectId(null);
        }
        String name = telegramRecurrenceMessage.getRuleName();
        Integer botId = telegramRecurrenceMessage.getTelegramSettingsItem().getId();
        Set<EdsTelegramChat> chats = new HashSet<>();
        List<TelegramChatListItem> chatListItems = telegramRecurrenceMessage.getTelegramChatListItems();
        for (TelegramChatListItem chatListItem : chatListItems) {
            EdsTelegramChat edsTelegramChat = telegramChatManager.getById(chatListItem.getObjectId());
            chats.add(edsTelegramChat);
        }
        String message = telegramRecurrenceMessage.getContent();
        recurrenceJobItem.setBusObjectParams("TELEGRAM_RECURRENCE");
        recurrenceJobItem.setBusObjectId(telegramRecurrenceMessage.getReportId());
        recurrenceJobItem.setJobType(RECURRING_REPORT);
        Locale locale = ServerSecurityContext.getInstance().getUserLocale();
        Integer reportId = telegramRecurrenceMessage.getReportId();
        EdsReport edsReport = reportingManager.get(reportId);
        boolean isActive = telegramRecurrenceMessage.getStatus();
        Integer recurrenceId = null;
        if (isActive) {
            recurrenceId = recurrenceService.saveRecurrenceJob(recurrenceJobItem);
        } else {
            recurrenceId = temporaryRecurrenceService.saveRecurrenceJob(recurrenceJobItem);
        }
        EdsTelegramReportingScheduleRule rule = new EdsTelegramReportingScheduleRule(name, botId, chats, isActive, message, recurrenceId, edsReport, locale);
        telegramReportingRecurrenceManager.createOrUpdate(rule);
        ArrayList<String> ruleNames = telegramReportingRecurrenceManager.getAllRuleNames(reportId);
        return ruleNames;
    }

    @Override
    public List<SelectItem> getGroupColumnByReportCodeList(String reportCode) {
        List<EdsReportGroupColumn> groupColumn = reportGroupColumnManager.getGroupColumnByReportCode(reportCode);
        List<SelectItem> selectItems = new ArrayList<>();
        for (EdsReportGroupColumn reportGroupColumn : groupColumn) {
            SelectItem selectItem = new SelectItem(reportGroupColumn.getObjectID());
            selectItem.setName(reportGroupColumn.getGroupName());
            selectItem.setDescription(reportGroupColumn.getGroupOfColumns());
            selectItems.add(selectItem);
        }
        return selectItems;
    }

    @Override
    public TelegramRecurrenceMessage getRuleByName(Integer reportId, String name) {
        EdsTelegramReportingScheduleRule rule = telegramReportingRecurrenceManager.getRuleByReportIdAndName(reportId, name);
        TelegramRecurrenceMessage message = new TelegramRecurrenceMessage();

        message.setRuleId(rule.getObjectID());
        message.setRuleName(rule.getName());

        TelegramSettingsItem telegramSettingsItem = globalAuthJdbcSpringManager.getTelegramSettingsItem(rule.getBotId());
        message.setTelegramSettingsItem(telegramSettingsItem);

        List<TelegramChatListItem> chatListItems = new ArrayList<>();
        Set<EdsTelegramChat> chats = rule.getChats();
        for (EdsTelegramChat chat : chats) {
            TelegramChatListItem chatListItem = new TelegramChatListItem();
            chatListItem.setObjectId(chat.getObjectID());
            chatListItem.setChatName(chat.getChatName());
            chatListItem.setTelegramBotId(chat.getTelegramBotId());
            chatListItem.setTelegramBotToken(chat.getTelegramBotToken());
            chatListItems.add(chatListItem);
        }
        message.setTelegramChatListItems(chatListItems);

        boolean isActive = rule.isActive();
        message.setStatus(isActive);
        message.setContent(rule.getMessage());

        RecurrenceJobItem recurrenceJobItem = null;
        if (isActive) {
            recurrenceJobItem = recurrenceService.createRecurrenceItemByRule(rule.getRecurrenceId(), RECURRING_REPORT);
        } else {
            recurrenceJobItem = temporaryRecurrenceService.createRecurrenceItemByRule(rule.getRecurrenceId(), RECURRING_REPORT);
        }

        message.setRecurrenceJobItem(recurrenceJobItem);

        message.setReportId(rule.getEdsReport().getObjectID());
        return message;
    }

    @Override
    public String getReportType(Integer reportId) {
        ReportRpc reportRpc = getReport(reportId);
        if (reportRpc != null) {
            return reportRpc.getTableType();
        }
        return ReportType.TABULAR.name();
    }

    @Override
    public SelectItem saveNewGroupColumns(SelectItem item) {
        if (item != null) {
            EdsReportGroupColumn reportGroupColumn = new EdsReportGroupColumn();
            reportGroupColumn.setGroupName(item.getName());
            reportGroupColumn.setGroupOfColumns(item.getDescription());
            reportGroupColumn.setReportCode(item.getCode());
            try {
                reportGroupColumnManager.create(reportGroupColumn);
                return item;
            } catch (Exception e) {
                return null;
            }
        }
        return new SelectItem();
    }

    @Override
    @Transactional
    public void saveReportGroups(String reportCode, LinkedHashMap<String, String> nameListByPanel) {

        reportGroupColumnManager.deleteByReportCode(reportCode);

        LinkedHashMap<String, StringBuilder> columnNames = new LinkedHashMap<>();

        if (nameListByPanel != null && !nameListByPanel.isEmpty()) {
            nameListByPanel.forEach((k, v) -> {
                if (columnNames.get(nameListByPanel.get(k)) != null) {
                    columnNames.get(nameListByPanel.get(k)).append(",").append(k);
                } else {
                    StringBuilder columns = new StringBuilder();
                    columns.append(k);
                    columnNames.put(nameListByPanel.get(k), columns);
                }
            });
            columnNames.forEach((k, v) -> {
                EdsReportGroupColumn reportGroupColumn = new EdsReportGroupColumn();
                reportGroupColumn.setGroupName(k);
                reportGroupColumn.setGroupOfColumns(columnNames.get(k).toString());
                reportGroupColumn.setReportCode(reportCode);
                reportGroupColumnManager.create(reportGroupColumn);
            });
        }
    }

    public ArrayList<ReportingCategoryRPC> getCategories() {
        ArrayList<ReportingCategoryRPC> result = new ArrayList<>();
        result.add(new ReportingCategoryRPC(0, commonLocalizer.localize("favourite")));
        for (Object[] l : reportingManager.getCategories()) {
            Integer categoryId = (Integer) l[0];
            String categoryCode = (String) l[1];
            String categoryName = (String) l[2];
            if (categoryCode != null) {
                if (hasPermissionToReportingCategories(categoryCode)) {
                    EdsModuleLocalize byModuleCode = moduleLocalizeManager.getByModuleCode(categoryCode.toLowerCase());
                    result.add(new ReportingCategoryRPC(categoryId, byModuleCode != null ? byModuleCode.getName() : getReportingSectionName(categoryCode.toLowerCase())));
                }
            } else {
                result.add(new ReportingCategoryRPC(categoryId, categoryName));
            }
        }
        return result;
    }

    private String getReportingSectionName(String section) {
        String moduleName = null;
        if (ModuleEnum.ACCOUNTING.getCode().equals(section)) {
            moduleName = commonLocalizer.localize("accountS");
        } else if (ModuleEnum.CRM.getCode().equals(section)) {
            moduleName = commonLocalizer.localize("crm");
        } else if (ModuleEnum.HRMS.getCode().equals(section)) {
            moduleName = commonLocalizer.localize("hrms");
        } else if (ModuleEnum.PM.getCode().equals(section)) {
            moduleName = commonLocalizer.localize("projects");
        } else if (ModuleEnum.PAYROLL.getCode().equals(section)) {
            moduleName = commonLocalizer.localize("payrollOnly");
        }
        return moduleName;
    }

    public ListResult<SelectListRpc> getReportList(ListingFilterParameter filter) {
        ArrayList<SelectListRpc> list = reportingManager.getReports(filter);
        Integer count = reportingManager.getReportsCount(filter);
        return new ListResult<>(list, count);
    }

    public ListResult<SelectListRpc> getReports(ListingFilterParameter filterParameter) {
        ArrayList<SelectListRpc> list = getReportList(filterParameter).getList();
        ArrayList<SelectListRpc> reports = (ArrayList<SelectListRpc>) list.stream().filter(a -> !a.isFakeReport()).collect(Collectors.toList());
        ListResult<SelectListRpc> listResult = new ListResult<>();
        listResult.setList(reports);
        return listResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<TableRpc> getTableColumns(ReportRpc report) {
        return reportingCoreService.getTableColumns(report);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReportRpc getReport(Integer id) {
        ReportRpc report = reportingCoreService.getReport(id, true);
        List<SelectItem> groupColumnByReportCodeList = getGroupColumnByReportCodeList(report.getCode());

        if (groupColumnByReportCodeList != null && !groupColumnByReportCodeList.isEmpty()) {
            LinkedHashMap<String, String> groupMap = new LinkedHashMap<>();
            groupColumnByReportCodeList.forEach(selectItem -> {
                ArrayList<String> columns = (ArrayList<String>) Arrays.stream(selectItem.getDescription().split(",")).map(String::trim).collect(Collectors.toList());
                columns.forEach(i -> groupMap.put(i, selectItem.getName()));
            });
            report.setColumnsByGroupMap(groupMap);
        }
        return report;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReportRpc getReport(String uuid) {
        ReportRpc report = RedisClient.getKey(uuid, ReportRpc.class);
        return report;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ReportRpc getReportStructure(ReportRpc report, Integer userId) {
        return reportingCoreService.getReportStructure(report, userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public String generateReport(ReportRpc report) {

        if (report != null && report.getColumnMap() != null && !report.getColumnMap().isEmpty()) {
            for (ColumnRpc columnRpc : report.getColumnMap().values()) {
                columnRpc.setTitle(reportingLocalizer.localize(columnRpc.getTitle().trim().replace(" ", "_").toLowerCase(), columnRpc.getTitle()));
            }
        }

        EdsUser edsUser = ((EdsUser) ServerSecurityContext.getInstance().getUser());
        if (edsUser != null) {
            report.setUserID(edsUser.getObjectID());
        }
        ResultSet result = null;
        if (report.getGroupColumns().isEmpty()) {
            report.setTableType(ReportType.TABULAR.name());
        }

        reportinCoreServiceLocal.setDailyRateRate();

        this.groupCol = new LinkedHashMap<>();
        this.groupColNew = new HashMap<>();

        if (report.getColumnsByGroupMap() != null) {
            groupCol.putAll(report.getColumnsByGroupMap());

            report.getColumnsByGroupMap().forEach((k, v) -> {
                if (groupColNew.get(report.getColumnsByGroupMap().get(k)) != null) {
                    groupColNew.get(report.getColumnsByGroupMap().get(k)).add(k);
                } else {
                    ArrayList<String> columns = new ArrayList<>();
                    columns.add(k);
                    groupColNew.put(report.getColumnsByGroupMap().get(k), columns);
                }
            });
        }

        if (report.getTableType().equals(ReportType.SUMMARY.name())) {
            try {
                result = reportinCoreServiceLocal.getSummaryReportResult(report, report.getUserID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            result = reportinCoreServiceLocal.getTabularReportResult(report, report.getUserID());
        }
        ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
        int hideColumnCount = ReportType.TABULAR.name().equals(report.getTableType()) ? viewRpc.getHiddenColumnCount() : 0;
        StringBuilder footerRow;
        StringBuilder reportContent = new StringBuilder();
        try {
            if (report.getTableType().equals(ReportType.SUMMARY.name())) {
                footerRow = renderSummaryReportColumnsFooter(result, report, hideColumnCount);
                reportContent = renderSummaryTableHeads(report);
                ArrayList<String> selectedColumnNames = new ArrayList<>();
                for (int i = 0; i < report.getSelectedColumns().size(); i++) {
                    selectedColumnNames.add(report.getSelectedColumns().get(i).getName());
                }
                ArrayList<String> summaryColumns = new ArrayList<>();
                for (int i = 0; i < report.getSumaries().size(); i++) {
                    summaryColumns.add(report.getSumaries().get(i).getName());
                }

                renderSummary(report, result, reportContent, report.getGroupColumns().size() + 1, selectedColumnNames, summaryColumns, selectedColumnNames.size() + 1);
            } else {
                footerRow = renderColumnsFooter(result, report, hideColumnCount);
                reportContent = renderColumnsHeads(report);
                renderTabular(report, result, reportContent, report.getSelectedColumns().size() + 1, hideColumnCount);
            }
            reportContent.append(footerRow);
            if (!result.isClosed()) {
                result.close();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return reportContent.toString();
    }

    private void renderTabular(ReportRpc report, ResultSet resultSet, StringBuilder reportContent, Integer columnCount, int hideColumnCount) throws SQLException {
        StringBuilder builder = new StringBuilder(1000);
        int count = (hideColumnCount > 0 ? (columnCount + hideColumnCount) : columnCount);
        int id = 1 + (Math.max(hideColumnCount, 0));
        EdsCompanySettings companySettings = companySettingsManager.getCompanySettings(reportingCoreService.getUser().getCompanyId());
        SimpleDateFormat dateFormat = null;
        SimpleDateFormat shortDateFormat = null;
        if (companySettings.getLongDateFormat() != null) {
            dateFormat = new SimpleDateFormat(companySettings.getLongDateFormat());
            shortDateFormat = new SimpleDateFormat(companySettings.getShortDateFormat());
        }
        SimpleDateFormat dateDateBaseFormatForShort = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateDateBaseFormatForLong = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        Locale locale = Locale.ENGLISH;
        if (user != null && user.getCompany() != null && user.getCompany().getLocale() != null) {
            EdsLocale userLocale = localeManager.getLocaleBylanguageCode(user.getCompany().getLocale());
            if (userLocale != null && userLocale.getCountry() != null) {
                locale = new Locale(userLocale.getLanguageCode(), userLocale.getCountry());
            } else {
                locale = new Locale(user.getCompany().getLocale());
            }
        }
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        String groupStyle = "";
        String groupName = "";
        String lastGroupName = "";
        String startGroupName = "";

        while (resultSet.next()) {
            builder.append("<tr>");
            for (int i = id; i < count; i++) {
                ColumnRpc selectedColumn = report.getSelectedColumns().get(i - id);
                String value = resultSet.getString(i);
                switch (selectedColumn.getColumnFormat()) {
                    case SqlQueryUtil.ColumnFormat_MONEY -> {
                        if (value == null || value.trim().isEmpty()) {
                            value = "n/a";
                        } else
                            try {
                                value = numberFormat.format(Double.valueOf(value));
                            } catch (Exception e) {
                            }
                    }
                    case SqlQueryUtil.ColumnFormat_IMAGE -> {
                        if (value == null || value.trim().isEmpty()) {
                            value = "n/a";
                        } else {
                            value = value.trim().replace(",", "");
                            if (!value.trim().isEmpty()) {
                                value = getFileUrl(Integer.valueOf(value));
                            } else {
                                value = "<p>No file</p>";
                            }
                        }
                    }
                    case SqlQueryUtil.ColumnFormat_DATE, SqlQueryUtil.ColumnFormat_DATE_WITHOUT_TIME_ZONE, SqlQueryUtil.ColumnFormat_WITHOUT_TIME_ZONE -> {
                        if (value == null || value.trim().isEmpty()) {
                            value = "<font color='red'>--:--</font>";
                        } else
                            try {
                                if ("short".equals(selectedColumn.getCustomDateFormat()) || selectedColumn.getCustomDateFormat() == null) {
                                    Date date = dateDateBaseFormatForShort.parse(value);
                                    value = shortDateFormat.format(date);
                                } else if ("long".equals(selectedColumn.getCustomDateFormat())) {
                                    Date longDate = dateDateBaseFormatForLong.parse(value);
                                    value = dateFormat.format(ServerUtils.convertServerDateToUserDate(longDate, user.getUserTimezone()));
                                }
                            } catch (Exception ignored) {
                            }
                    }
                    default -> {
                        if (value == null || value.trim().isEmpty()) {
                            value = companySettings.getReportingEmptyValueString();
                        }
                    }
                }
                if (groupCol.get(selectedColumn.getName()) != null) {
                    groupName = groupCol.get(selectedColumn.getName());
                    groupStyle = groupName.toLowerCase().trim().replace(" ", "_");
                    List<String> columnList = groupColNew.get(groupName);
                    lastGroupName = columnList.get(columnList.size() - 1);
                    startGroupName = columnList.get(0);
                }

                if (startGroupName.equals(selectedColumn.getName())) {
                    builder.append("<td class='groupCol groupCol--start td-" + groupStyle + " '").append(NUMBER_MONEY_PERCENT_TIME.contains(selectedColumn.getType()) ? "class='text-right'" : "").append("><span>").
                            append(value).
                            append("</span></td>");
                } else if (groupCol.get(selectedColumn.getName()) != null) {
                    builder.append("<td class='groupCol--off  td-" + groupStyle + " '").append(NUMBER_MONEY_PERCENT_TIME.contains(selectedColumn.getType()) ? "class='text-right'" : "").append("><span>").
                            append(value).
                            append("</span></td>");
                } else {
                    builder.append("<td ").append(NUMBER_MONEY_PERCENT_TIME.contains(selectedColumn.getType()) ? "class='text-right'" : "").append("><span>").
                            append(value).
                            append("</span></td>");
                }
            }
            builder.append("</tr>");
        }

        reportContent.append(builder.isEmpty() ? "<tr style='text-align: center;'><td class='' colspan='" + report.getSelectedColumns().size() + "' style='height: 250px;line-height: 250px;border: none;text-align:center;'>" + commonLocalizer.localize("noResultsFoundForTheProvidedSearchCriteria", "No results found for the provided search criteria") + "</td></tr>" : builder);
    }

    private StringBuilder renderColumnsHeads(ReportRpc report) {
        StringBuilder builder = new StringBuilder(100);
        builder.append("<table class='table table-groupCol table_tree' cellspacing='0' cellpadding='0'>");
        builder.append("<thead class='point_affix_top ' point_affix_top_below_selector='.pageContent_reporting .operPanel'>");
        builder.append("<tr class='groupMarkRow'>");

        report.getSelectedColumns();
        String groupName = "";
        String lastGroupName = "";
        String startGroupName = "";
        String groupStyle = "";
        for (ColumnRpc column : report.getSelectedColumns()) {
            System.out.println("field " + column.getName());
            if (!groupCol.isEmpty() && !groupColNew.isEmpty()) {
                groupName = groupCol.get(column.getName()) != null ? groupCol.get(column.getName()) : "";
                groupStyle = groupName.toLowerCase().trim().replace(" ", "_");
                if (!groupName.isEmpty()) {
                    List<String> columnList = groupColNew.get(groupName);
                    lastGroupName = columnList.get(columnList.size() - 1);
                    startGroupName = columnList.get(0);
                }
            }
            if (startGroupName != null && startGroupName.equals(column.getName())) {
                builder.append("<th class='groupCol groupCol--start thg-" + groupStyle + "'>"
                        + "<div class='groupMarkRow__opt'>"
                        + "<span>" + groupName + "</span>"
                        + "<i id='group_id-" + groupStyle + "' class='btn--circle plusMinus collapsed'></i>"
                        + "</div>"
                        + "</th>");
            } else if (groupCol.get(column.getName()) != null && lastGroupName.equals(column.getName())) {
                builder.append("<th class='groupCol--end groupCol--off thg-" + groupStyle + "'>" + "<div></div>" + "</th>");
            } else if (groupCol.get(column.getName()) != null) {
                builder.append("<th class='groupCol--off thg-" + groupStyle + "'>" + "<div></div>" + "</th>");
            } else {
                builder.append("<th><div></div> </th>");
            }
        }
        builder.append("</tr>");
        builder.append("<tr>");

        for (ColumnRpc column : report.getSelectedColumns()) {

            if (groupCol.get(column.getName()) != null) {
                groupName = groupCol.get(column.getName());
                groupStyle = groupName.toLowerCase().trim().replace(" ", "_");
                List<String> columnList = groupColNew.get(groupName);
                lastGroupName = columnList.get(columnList.size() - 1);
                startGroupName = columnList.get(0);
            }

            String minWidth;
            if (SqlColumnType.TIME.getName().equals(column.getType()) || SqlColumnType.TIME.getName().equals(column.getColumnFormat())) {
                minWidth = "style='min-width: 50px;'";
            } else if (SqlColumnType.DATE.getName().equals(column.getType()) || SqlColumnType.NUMBER.getName().equals(column.getColumnFormat())) {
                minWidth = "style='min-width: 70px;'";
            } else if (SqlColumnType.NUMBER.getName().equals(column.getType()) || SqlColumnType.NUMBER.getName().equals(column.getColumnFormat())) {
                minWidth = "style='min-width: 90px;'";
            } else if (SqlColumnType.STRING.getName().equals(column.getType()) || SqlColumnType.STRING.getName().equals(column.getColumnFormat())) {
                minWidth = "style='min-width: 110px;'";
            } else {
                minWidth = "style='min-width: 110px;'";
            }


            if (groupCol.get(column.getName()) != null && !lastGroupName.equals(column.getName()) && !startGroupName.equals(column.getName())) {
                builder.append("<th class='stickerCell groupCol--off th-" + groupStyle).append("number,money,percent,time".contains(column.getType()) ? " text-right" : "").append("' ").append(" >");
            } else if (lastGroupName.equals(column.getName())) {
                builder.append("<th class='stickerCell groupCol--end groupCol--off th-" + groupStyle).append("number,money,percent,time".contains(column.getType()) ? " text-right" : "").append("' ").append(" >");
            } else if (startGroupName.equals(column.getName())) {
                builder.append("<th class='stickerCell groupCol groupCol--start ")
                        .append("th-" + groupStyle)
                        .append("number,money,percent,time".contains(column.getType()) ? " text-right" : "").append("' ").append(" >");
            } else {
                builder.append("<th class='stickerCell ").append("number,money,percent,time".contains(column.getType()) ? " text-right" : "").append("' ").append(" >");
            }

            String sortTypeClass = "";
            if (column.getName().replace(".", "_").equals(report.getSortTableByColumn())) {
                if ("ASC".equals(report.getSortTableByColumnType())) {
                    sortTypeClass = "fonticon-sort-alpha-asc state_origin";
                } else {
                    sortTypeClass = "fonticon-sort-alpha-desc state_origin";
                }
            }
            builder.append("<div class='frame_affix_top' ").append(minWidth).
                    append("><a href='javascript:void();'id='").
                    append(column.getAlias()).append("-sort' title='").
                    append(column.getTitle()).append("'>").
                    append(column.getTitle()).append("</a> <span class='").
                    append(sortTypeClass).append("'></span></div>");
            builder.append("</th>");
        }
        builder.append("</tr></thead>");
        return builder;
    }

    private StringBuilder renderColumnsFooter(ResultSet result, ReportRpc report, int hideColumnCount) throws SQLException {
        String local = ServerUtils.getUserLocale().getLanguage();
        result.next();
        StringBuilder footerRow = new StringBuilder("<tr class='total_row test--footerRow'>");
        boolean hasData = false;
        if (ReportType.TABULAR.name().equals(report.getTableType())) {
            footerRow.append("<td></td>");
        } else {
            String grandTotal;
            if ("ru".equals(local)) {
                grandTotal = "ОБЩИЙ ИТОГ:";
            } else if ("uz".equals(local)) {
                grandTotal = "JAMI:";
            } else {
                grandTotal = "GRAND TOTAL:";
            }
            footerRow.append("<td><b>").append(grandTotal).append(result.getString(1 + hideColumnCount)).append("</b></td>");
            hasData = true;
        }
        for (int i = 2 + hideColumnCount; i <= report.getSelectedColumns().size() + hideColumnCount; i++) {
            ColumnRpc selectedColumn = report.getSelectedColumns().get(i - 1 - hideColumnCount);
            if (report.getSumaries().contains(selectedColumn)) {
                String value = result.getString(i);
                if (value == null || "null".equals(value)) {
                    value = "";
                } else {
                    hasData = true;
                }
                footerRow.append("<td ").append("number,money,percent,time".contains(selectedColumn.getType()) ? "class='text-right'" : "").append(" ><b>")
                        .append(value).append("</b></td>");
            } else {
                footerRow.append("<td></td>");
            }
        }
        footerRow.append("</tr></table>");
        return hasData ? footerRow : new StringBuilder();
    }

    private StringBuilder renderSummaryReportColumnsFooter(ResultSet result, ReportRpc report, int hideColumnCount) throws SQLException {
        result.next();
        if (report.getSumaries() == null || report.getSumaries().isEmpty()) return new StringBuilder();
        String local = ServerUtils.getUserLocale().getLanguage();
        Integer sumOfColumnsSize = (report.getSelectedColumns().size() - 1) * 160 + 182;
        StringBuilder footerRow = new StringBuilder("<table class='tf tf--level-1 expanded tf-grandTotal' style=width:" + sumOfColumnsSize + "px>");
        footerRow.append("<tr class='total_row test--footerRow'>");
        String grandTotal;
        if ("ru".equals(local)) {
            grandTotal = "ОБЩИЙ ИТОГ";
        } else if ("uz".equals(local)) {
            grandTotal = "JAMI";
        } else {
            grandTotal = "GRAND TOTAL";
        }
        footerRow.append("<td class='tf-col-0'>");
        footerRow.append("<b>").append(grandTotal).append("</b>");
        footerRow.append("</td>");
        for (int i = 2 + hideColumnCount; i <= report.getSelectedColumns().size() + hideColumnCount; i++) {
            ColumnRpc selectedColumn = report.getSelectedColumns().get(i - 1 - hideColumnCount);
            footerRow.append("<td style='width: 160px' class='tf-col ").append("number,money,percent,time".contains(selectedColumn.getType()) ? " text-right" : "").append("' >");
            if (report.getSumaries().contains(selectedColumn)) {
                String value = result.getString(i);
                if (value == null || "null".equals(value)) {
                    value = "";
                }
                footerRow.append("<b>").append(value).append("</b>");

            } else {
                footerRow.append("<b></b>");
            }
            footerRow.append("</td>");
        }
        footerRow.append("</tr></table>");
        return footerRow;
    }

    private StringBuilder renderSummaryTableHeads(ReportRpc report) {
        List<String> groupColumns = report.getGroupColumns().stream().map(ColumnRpc::getName).collect(Collectors.toList());
        StringBuilder builder = new StringBuilder(100);
        Integer sumOfColumnsSize = (report.getSelectedColumns().size() - 1) * 160 + 182;
        builder.append("<table class='kpi-thead__table' style=width:").append(sumOfColumnsSize).append("px>");
        builder.append("<thead class='kpi-thead'><tr>");
        String local = ServerUtils.getUserLocale().getLanguage();
        int i = 0;
        String class__;
        String style__;
        for (ColumnRpc column : report.getSelectedColumns()) {
            class__ = " tf-col ";
            style__ = " style='width: 160px' ";
            if (i == 0) {
                class__ = " tf-col-0 ";
                style__ = " style='width: 182px' ";
            }
            String aggStrings = "", dlmtr = "", agg;
            if (column.isSum()) {
                aggStrings += dlmtr;
                if ("ru".equals(local)) {
                    agg = "Сумма";
                } else if ("uz".equals(local)) {
                    agg = "Summa";
                } else {
                    agg = "Sum";
                }
                aggStrings += agg;
                dlmtr = "/";
            }
            if (column.isCount()) {
                aggStrings += dlmtr;
                if ("ru".equals(local)) {
                    agg = "Кол-во";
                } else if ("uz".equals(local)) {
                    agg = "Soni";
                } else {
                    agg = "Count";
                }
                aggStrings += agg;
                dlmtr = "/";
            }
            if (column.isAvg()) {
                aggStrings += dlmtr;
                if ("ru".equals(local)) {
                    agg = "Среднее";
                } else if ("uz".equals(local)) {
                    agg = "O'rtacha";
                } else {
                    agg = "Avg";
                }
                aggStrings += agg;
                dlmtr = "/";
            }
            if (column.isLargest()) {
                aggStrings += dlmtr;
                if ("ru".equals(local)) {
                    agg = "Макс.";
                } else if ("uz".equals(local)) {
                    agg = "Eng katta";
                } else {
                    agg = "Max";
                }
                aggStrings += agg;
                dlmtr = "/";
            }
            if (column.isSmallest()) {
                aggStrings += dlmtr;
                if ("ru".equals(local)) {
                    agg = "Мин.";
                } else if ("uz".equals(local)) {
                    agg = "Eng kichik";
                } else {
                    agg = "Min";
                }
                aggStrings += agg;
            }

            String fulllTr = column.getTitle();
            if (!aggStrings.trim().isEmpty()) {
                fulllTr += "<br> (" + aggStrings + ")";
            }

            builder.append("<th id='").append(column.getAlias()).append("-sort' ").append(style__).append(" class='")
                    .append(class__).append(" ").append("number,money,percent,time".contains(column.getType()) ? "text-right" : "").append("' ").append(" >");

            StringBuilder sb = new StringBuilder();
            sb.append("<i id='theadercollase-")
                    .append(i)
                    .append("' class='btn--circle plusMinus btn-small ");
            if (groupColumns.contains(column.getName()) && i == 0) {
                String collapsed = report.getViewTypes().isEmpty() || report.getViewTypes().get(i).equals("Collapsible") ? "collapsed" : "expanded";
                sb.append(collapsed).append("'></i>");
                builder.append(sb);
            } else if (groupColumns.contains(column.getName()) && i > 0) {
                String currentView = "collapsed";
                if (!report.getViewTypes().isEmpty() && report.getViewTypes().size() > i) {
                    currentView = "Collapsible".equals(report.getViewTypes().get(i)) ? "collapsed" : "expanded";
                }

                boolean isCollapsed = false;
                if (!report.getViewTypes().isEmpty() && i - 1 >= 0 && report.getViewTypes().size() > i - 1) {
                    isCollapsed = "Collapsible".equals(report.getViewTypes().get(i - 1));
                }
                if (i == 2) {
                    boolean firstLayerCollapsed = false;
                    if (!report.getViewTypes().isEmpty()) {
                        firstLayerCollapsed = "Collapsible".equals(report.getViewTypes().get(0));
                    }
                    if (firstLayerCollapsed) {
                        sb.append(currentView).append("' style='display: ").append("none;'></i>");
                    } else {
                        sb.append(currentView).append("' style='display: ").append(isCollapsed ? "none" : "inline-block").append(";'></i>");
                    }
                } else {
                    sb.append(currentView).append("' style='display: ").append(isCollapsed ? "none" : "inline-block").append(";'></i>");
                }
                builder.append(sb);
            }
            builder.append("<span title='").append(fulllTr).append("'>").append(fulllTr).append("</span>");
            builder.append("</th>");
            i++;
        }
        builder.append("</tr></thead></table>");
        return builder;
    }

    private void renderSummary(ReportRpc report, ResultSet resultSet, StringBuilder reportContent, int maxDepth, ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer columnCount) {

        EdsCompanySettings companySettings = companySettingsManager.getCompanySettings(reportingCoreService.getUser().getCompanyId());
        SimpleDateFormat dateFormat = null;
        SimpleDateFormat shortDateFormat = null;
        if (companySettings.getLongDateFormat() != null) {
            dateFormat = new SimpleDateFormat(companySettings.getLongDateFormat());
            shortDateFormat = new SimpleDateFormat(companySettings.getShortDateFormat());
        }
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        Locale locale = Locale.ENGLISH;
        if (user != null && user.getCompany() != null && user.getCompany().getLocale() != null) {
            EdsLocale userLocale = localeManager.getLocaleBylanguageCode(user.getCompany().getLocale());
            if (userLocale != null && userLocale.getCountry() != null) {
                locale = new Locale(userLocale.getLanguageCode(), userLocale.getCountry());
            } else {
                locale = new Locale(user.getCompany().getLocale());
            }
        }
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        try {
            StringBuilder builder;
            if (maxDepth == 2 && !report.getIsDetailed()) {
                builder = noGroupingTable(report, resultSet, selectedColumns, columnCount, dateFormat, shortDateFormat, user, numberFormat, companySettings.getReportingEmptyValueString());
            } else if (maxDepth == 2 || (maxDepth == 3 && !report.getIsDetailed())) {
                builder = oneLevelGrouping(report, resultSet, selectedColumns, summaryColumns, columnCount, dateFormat, shortDateFormat, user, numberFormat, companySettings.getReportingEmptyValueString());
            } else if (maxDepth == 3 || (maxDepth == 4 && !report.getIsDetailed())) {
                builder = twoLevelGrouping(report, resultSet, selectedColumns, summaryColumns, columnCount, dateFormat, shortDateFormat, user, numberFormat, companySettings.getReportingEmptyValueString());
            } else {
                builder = thirtLevelGrouping(report, resultSet, selectedColumns, summaryColumns, columnCount, dateFormat, shortDateFormat, user, numberFormat, companySettings.getReportingEmptyValueString());
            }
            reportContent.append(builder.isEmpty() ? "<tr style='text-align: center;'><td class='' colspan='" + report.getSelectedColumns().size() + "' style='height: 250px;line-height: 250px;border: none;text-align:center;'>" + commonLocalizer.localize("noResultsFoundForTheProvidedSearchCriteria", "No results found for the provided search criteria") + "</td></tr>" : builder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder noGroupingTable(ReportRpc report, ResultSet resultSet, ArrayList<String> selectedColumns, Integer columnCount, SimpleDateFormat dateFormat, SimpleDateFormat shortDateFormat, EdsUser user, NumberFormat numberFormat, String reportingEmptyValueString) throws SQLException {
        StringBuilder builder = new StringBuilder(1000);
        String sortableColumn = getColumnByName(report.getSortTableByColumn(), selectedColumns);
        int depth;
        builder.append("<table class='tf tf--level-1 tf--level-last expanded' style='width:").append((report.getSelectedColumns().size() - 1) * 160 + 182).append("px'>");
        while (resultSet.next()) {
            if (sortableColumn != null) {
                depth = resultSet.getInt(columnCount + 1) + 1;
            } else {
                depth = resultSet.getInt(columnCount) + 1;
            }
            builder.append("<tr>");
            String value;
            for (int i = depth; i < columnCount; i++) {
                ColumnRpc selectedColumn = report.getSelectedColumns().get(i - 1);
                String className = "number,money,percent,time".contains(selectedColumn.getType()) ? "tf__toggle text-right" : "tf__toggle ";
                value = resultSet.getString(i);
                value = getFormattedValue(dateFormat, shortDateFormat, user, numberFormat, value, selectedColumn, reportingEmptyValueString);
                if (i == depth) {
                    builder.append("<td class='tf-col-0 ").append(className).append("'><span>").append(value).append("</span></td>");
                } else {
                    builder.append("<td style='width: 160px' class='tf-col ").append(className).append("'><span>").append(value).append("</span></td>");
                }
            }
            builder.append("</tr>");
        }
        builder.append("</table>");
        return builder;
    }

    private StringBuilder oneLevelGrouping(ReportRpc report, ResultSet resultSet, ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer columnCount, SimpleDateFormat dateFormat, SimpleDateFormat shortDateFormat, EdsUser user, NumberFormat numberFormat, String reportingEmptyValueString) throws SQLException {
        StringBuilder builder = new StringBuilder(1000);
        String sortableColumn = getColumnByName(report.getSortTableByColumn(), selectedColumns);
        int depth, trIndex = 0, lastGorder = 1;
        boolean collapceOpen = false;
        String collapceClose = "</tbody></table></td></tr></tbody>";
        String footerTag = "<tfoot><tr><td><table><tbody><tr class='tf__totalRow'>";
        String closeFooterTag = "</tr></tbody></table></td></tr></tfoot></table></td></tr>";
        int sumOfColumnsSize = (report.getSelectedColumns().size() - 1) * 160;
        String viewType1 = !report.getViewTypes().isEmpty() && !report.getViewTypes().isEmpty() && report.getViewTypes().get(0).equals("Collapsible") ? "collapsed" : "expanded";
        while (resultSet.next()) {
            if (sortableColumn != null) {
                depth = resultSet.getInt(columnCount + 1) + 1;
            } else {
                depth = resultSet.getInt(columnCount) + 1;
            }
            int tdIndex = 1;
            if (depth == 2) {
                if (lastGorder == 1) {
                    if (collapceOpen) {
                        builder.append("</tbody>");
                        builder.append("</table>");
                    }
                    builder.append("<table id='trIndex-").append(trIndex).append(tdIndex).append("' class='tf tf--level-1 " + viewType1 + "' style='width:").append(sumOfColumnsSize + 182).append("px'><tbody><tr>");
                    collapceOpen = true;
                } else {
                    builder.append("<tr>");
                }
            } else {
                builder.append(collapceClose);
                builder.append(footerTag);
            }
            String value;
            String viewType3 = !report.getViewTypes().isEmpty() && report.getViewTypes().size() > 2 && report.getViewTypes().get(2).equals("Collapsible") ? "collapsed" : "expanded";
            for (int i = lastGorder; i < columnCount; i++) {
                ColumnRpc selectedColumn = report.getSelectedColumns().get(i - 1);
                String simpleclass__ = "number,money,percent,time".contains(selectedColumn.getType()) ? " class='tf-col text-right' " : " class='tf-col' ";
                String style__ = " style='width: 160px' ";
                value = resultSet.getString(i);
                value = getFormattedValue(dateFormat, shortDateFormat, user, numberFormat, value, selectedColumn, reportingEmptyValueString);
                String simpleOpen = "<td rowspan='2' class='tf__obj' style='width: " + sumOfColumnsSize + "px'><table><tbody><tr><td><table class='tf tf--level-3 tf--level-last " + viewType3 + "'><tbody><tr>";
                String circleButton = "<i id='tbodycollase-" + trIndex + tdIndex + "' class='btn--circle plusMinus btn-small'></i>";

                if (depth == 2) {
                    if (lastGorder == 1) {
                        if (tdIndex == 1) {
                            builder.append("<td class='tf__toggle tf-col-0'>").append(circleButton).append("<span>").append(value).append("</span></td>");
                        } else {
                            if (tdIndex == 2) {
                                builder.append(simpleOpen);
                            }
                            builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                        }
                    } else {
                        builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                    }
                } else {
                    if (!summaryColumns.contains(selectedColumns.get(i - 1))) {
                        value = "";
                    }
                    builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                }
                tdIndex++;
            }
            String local = ServerUtils.getUserLocale().getLanguage();
            String total;
            if ("ru".equals(local)) {
                total = "ИТОГ:";
            } else if ("uz".equals(local)) {
                total = "JAMI:";
            } else {
                total = "TOTAL:";
            }
            if (depth == 1) {
                builder.append(closeFooterTag);
                builder.append("<tr><td class='tf__totalLabel'><div>").append(total).append("</div></td></tr>");
            } else {
                builder.append("</tr>");
            }

            lastGorder = depth;
            trIndex++;
        }
        builder.append("</tbody>");
        builder.append("</table>");
        return builder;
    }

    private StringBuilder twoLevelGrouping(ReportRpc report, ResultSet resultSet, ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer columnCount, SimpleDateFormat dateFormat, SimpleDateFormat shortDateFormat, EdsUser user, NumberFormat numberFormat, String reportingEmptyValueString) throws SQLException {
        StringBuilder builder = new StringBuilder(1000);
        String sortableColumn = getColumnByName(report.getSortTableByColumn(), selectedColumns);
        int depth, trIndex = 0, lastGorder = 1;
        boolean collapceOpen = false;
        String collapceClose = "</tbody></table></td></tr></tbody>";
        String footerTag = "<tfoot><tr><td><table><tbody><tr class='tf__totalRow'>";
        String closeFooterTag = "</tr></tbody></table></td></tr></tfoot></table></td></tr>";
        int sumOfColumnsSize = (report.getSelectedColumns().size() - 1) * 160;
        String viewType2 = !report.getViewTypes().isEmpty() && report.getViewTypes().size() > 1 && report.getViewTypes().get(1).equals("Collapsible") ? "collapsed" : "expanded";
        String viewType1 = !report.getViewTypes().isEmpty() && report.getViewTypes().size() >= 1 && report.getViewTypes().get(0).equals("Collapsible") ? "collapsed" : "expanded";
        while (resultSet.next()) {
            if (sortableColumn != null) {
                depth = resultSet.getInt(columnCount + 1) + 1;
            } else {
                depth = resultSet.getInt(columnCount) + 1;
            }
            int tdIndex = 1;
            if (depth == 3) {
                if (lastGorder == 1 || lastGorder == 2) {
                    if (collapceOpen) {
                        builder.append("</tbody>");
                        builder.append("</table>");
                    }
                    if (lastGorder == 1) {
                        builder.append("<table id='trIndex-").append(trIndex).append(tdIndex).append("' class='tf tf--level-1 ").append(viewType1).append("' style='width:").append(sumOfColumnsSize + 182).append("px'><tbody><tr>");
                    } else {
                        builder.append("<table id='trIndex-").append(trIndex).append(tdIndex).append("' class='tf tf--level-2 tf--level-last ").append(viewType2).append("'><tbody><tr>");
                    }
                    collapceOpen = true;

                } else {
                    builder.append("<tr>");
                }
            }
            if (depth == 2 || depth == 1) {
                builder.append(collapceClose);
                builder.append(footerTag);
            }
            String value;
            for (int i = lastGorder; i < columnCount; i++) {
                ColumnRpc selectedColumn = report.getSelectedColumns().get(i - 1);
                String class__ = "number,money,percent,time".contains(selectedColumn.getType()) ? " class='tf__toggle tf-col text-right' " : " class='tf__toggle tf-col' ";
                String simpleclass__ = "number,money,percent,time".contains(selectedColumn.getType()) ? " class='tf-col text-right' " : " class='tf-col' ";
                String style__ = " style='width: 160px' ";
                value = resultSet.getString(i);
                value = getFormattedValue(dateFormat, shortDateFormat, user, numberFormat, value, selectedColumn, reportingEmptyValueString);
                String collapseLevel2 = "<td rowspan='2' class='tf__obj' style='width: " + sumOfColumnsSize + "px'><table><tbody><tr><td><table id='trIndex-" + trIndex + tdIndex + "' class='tf tf--level-2 tf--level-last " + viewType2 + "'><tbody><tr>";
                String simpleOpen = "<td rowspan='2' class='tf__obj'><table><tbody><tr><td><table><tbody><tr>";
                String circleButton = "<i id='tbodycollase-" + trIndex + tdIndex + "' class='btn--circle plusMinus btn-small'></i>";

                if (depth == 3) {
                    if (lastGorder == 1) {
                        if (tdIndex == 1) {
                            builder.append("<td class='tf__toggle tf-col-0'>").append(circleButton).append("<span>").append(value).append("</span></td>");
                        } else if (tdIndex == 2) {
                            builder.append(collapseLevel2);
                            builder.append("<td ").append(class__).append(style__).append(">").append(circleButton).append("<span>").append(value).append("</span></td>");
                        } else {
                            if (tdIndex == 3) {
                                builder.append(simpleOpen);
                            }
                            builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                        }
                    } else if (lastGorder == 2) {
                        if (tdIndex == 1) {
                            builder.append("<td ").append(class__).append(style__).append(">").append(circleButton).append("<span>").append(value).append("</span></td>");
                        } else {
                            if (tdIndex == 2) {
                                builder.append(simpleOpen);
                            }
                            builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                        }
                    } else {
                        builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                    }
                } else if (depth == 2 || depth == 1) {
                    if (!summaryColumns.contains(selectedColumns.get(i - 1))) {
                        value = "";
                    }
                    builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                }
                tdIndex++;
            }
            String local = ServerUtils.getUserLocale().getLanguage();
            String total;
            if ("ru".equals(local)) {
                total = "ИТОГ:";
            } else if ("uz".equals(local)) {
                total = "JAMI:";
            } else {
                total = "TOTAL:";
            }
            if (depth == 2 || depth == 1) {
                builder.append(closeFooterTag);
                builder.append("<tr><td class='tf__totalLabel'><div>").append(total).append("</div></td></tr>");
            } else {
                builder.append("</tr>");
            }

            lastGorder = depth;
            trIndex++;
        }
        builder.append("</tbody>");
        builder.append("</table>");
        return builder;
    }

    private StringBuilder thirtLevelGrouping(ReportRpc report, ResultSet resultSet, ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer columnCount, SimpleDateFormat dateFormat, SimpleDateFormat shortDateFormat, EdsUser user, NumberFormat numberFormat, String reportingEmptyValueString) throws SQLException {
        StringBuilder builder = new StringBuilder(1000);
        String sortableColumn = getColumnByName(report.getSortTableByColumn(), selectedColumns);
        int depth, trIndex = 0, lastGorder = 1;
        boolean collapceOpen = false;
        String collapceClose = "</tbody></table></td></tr></tbody>";
        String footerTag = "<tfoot><tr><td><table><tbody><tr class='tf__totalRow'>";
        String closeFooterTag = "</tr></tbody></table></td></tr></tfoot></table></td></tr>";
        int sumOfColumnsSize = (report.getSelectedColumns().size() - 1) * 160;
        String viewType3 = !report.getViewTypes().isEmpty() && report.getViewTypes().size() > 2 && report.getViewTypes().get(2).equals("Collapsible") ? "collapsed" : "expanded";
        String viewType2 = !report.getViewTypes().isEmpty() && report.getViewTypes().size() > 1 && report.getViewTypes().get(1).equals("Collapsible") ? "collapsed" : "expanded";
        String viewType1 = !report.getViewTypes().isEmpty() && report.getViewTypes().size() >= 1 && report.getViewTypes().get(0).equals("Collapsible") ? "collapsed" : "expanded";
        while (resultSet.next()) {
            if (sortableColumn != null) {
                depth = resultSet.getInt(columnCount + 1) + 1;
            } else {
                depth = resultSet.getInt(columnCount) + 1;
            }
            int tdIndex = 1;
            if (depth == 4) {
                if (lastGorder == 1 || lastGorder == 3 || lastGorder == 2) {
                    if (collapceOpen) {
                        builder.append("</tbody>");
                        builder.append("</table>");
                    }
                    if (lastGorder == 1) {
                        builder.append("<table id='trIndex-").append(trIndex).append(tdIndex).append("' class='tf tf--level-1 ").append(viewType1).append("' style=width:").append(sumOfColumnsSize + 182).append("px><tbody><tr>");
                    } else if (lastGorder == 2) {
                        builder.append("<table id='trIndex-").append(trIndex).append(tdIndex).append("' class='tf tf--level-2 ").append(viewType2).append("'><tbody><tr>");
                    } else {
                        builder.append("<table id='trIndex-").append(trIndex).append(tdIndex).append("' class='tf tf--level-3 tf--level-last ").append(viewType3).append("'><tbody><tr>");
                    }
                    collapceOpen = true;

                } else {
                    builder.append("<tr>");
                }
            }
            if (depth == 3 || depth == 2 || depth == 1) {
                builder.append(collapceClose);
                builder.append(footerTag);
            }
            String value;
            for (int i = lastGorder; i < columnCount; i++) {
                ColumnRpc selectedColumn = report.getSelectedColumns().get(i - 1);
                String class__ = "number,money,percent,time".contains(selectedColumn.getType()) ? " class='tf__toggle tf-col text-right' " : " class='tf__toggle tf-col' ";
                String simpleclass__ = "number,money,percent,time".contains(selectedColumn.getType()) ? " class='tf-col text-right' " : " class='tf-col' ";
                String style__ = " style='width: 160px' ";
                value = resultSet.getString(i);
                value = getFormattedValue(dateFormat, shortDateFormat, user, numberFormat, value, selectedColumn, reportingEmptyValueString);
                String collapseLevel2 = "<td rowspan='2' class='tf__obj' style='width: " + sumOfColumnsSize + "px'><table><tbody><tr><td><table id='trIndex-" + trIndex + tdIndex + "' class='tf tf--level-2 " + viewType2 + "'><tbody><tr>";
                String collapseLevel3 = "<td rowspan='2' class='tf__obj'><table><tbody><tr><td><table id='trIndex-" + trIndex + tdIndex + "' class='tf tf--level-3 tf--level-last " + viewType3 + "'><tbody><tr>";
                String simpleOpen = "<td rowspan='2' class='tf__obj'><table><tbody><tr><td><table><tbody><tr>";
                String circleButton = "<i id='tbodycollase-" + trIndex + tdIndex + "' class='btn--circle plusMinus btn-small'></i>";

                if (depth == 4) {
                    if (lastGorder == 1) {
                        if (tdIndex == 1) {
                            builder.append("<td class='tf__toggle tf-col-0'>").append(circleButton).append("<span>").append(value).append("</span></td>");
                        } else if (tdIndex == 2) {
                            builder.append(collapseLevel2);
                            builder.append("<td ").append(class__).append(style__).append(">").append(circleButton).append("<span>").append(value).append("</span></td>");
                        } else if (tdIndex == 3) {
                            builder.append(collapseLevel3);
                            builder.append("<td ").append(class__).append(style__).append(">").append(circleButton).append("<span>").append(value).append("</span></td>");
                        } else {
                            if (tdIndex == 4) {
                                builder.append(simpleOpen);
                            }
                            builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                        }
                    } else if (lastGorder == 2) {
                        if (tdIndex == 1) {
                            builder.append("<td ").append(class__).append(style__).append(">").append(circleButton).append("<span>").append(value).append("</span></td>");
                        } else if (tdIndex == 2) {
                            builder.append(collapseLevel3);
                            builder.append("<td ").append(class__).append(style__).append(">").append(circleButton).append("<span>").append(value).append("</span></td>");
                        } else {
                            if (tdIndex == 3) {
                                builder.append(simpleOpen);
                            }
                            builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                        }
                    } else if (lastGorder == 3) {
                        if (tdIndex == 1) {
                            builder.append("<td ").append(class__).append(style__).append(">").append(circleButton).append("<span>").append(value).append("</span></td>");
                        } else {
                            if (tdIndex == 2) {
                                builder.append(simpleOpen);
                            }
                            builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                        }
                    } else {
                        builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                    }
                } else if (depth == 3 || depth == 2 || depth == 1) {
                    if (!summaryColumns.contains(selectedColumns.get(i - 1))) {
                        value = "";
                    }
                    builder.append("<td ").append(simpleclass__).append(style__).append("><span>").append(value).append("</span></td>");
                }
                tdIndex++;
            }
            String local = ServerUtils.getUserLocale().getLanguage();
            String total;
            if ("ru".equals(local)) {
                total = "ИТОГ:";
            } else if ("uz".equals(local)) {
                total = "JAMI:";
            } else {
                total = "TOTAL:";
            }
            if (depth == 3 || depth == 2 || depth == 1) {
                builder.append(closeFooterTag);
                builder.append("<tr><td class='tf__totalLabel'><div>").append(total).append("</div></td></tr>");
            } else {
                builder.append("</tr>");
            }

            lastGorder = depth;
            trIndex++;
        }
        builder.append("</tbody>");
        builder.append("</table>");
        return builder;
    }

    private String getFormattedValue(SimpleDateFormat dateFormat, SimpleDateFormat shortDateFormat, EdsUser user, NumberFormat numberFormat, String value, ColumnRpc selectedColumn, String reportingEmptyValueString) {
        switch (selectedColumn.getColumnFormat()) {
            case SqlQueryUtil.ColumnFormat_MONEY -> {
                if (value == null) {
                    value = "n/a";
                } else
                    try {
                        value = numberFormat.format(Double.valueOf(value));
                    } catch (Exception ignored) {
                    }
            }
            case SqlQueryUtil.ColumnFormat_IMAGE -> {
                if (value == null || value.trim().isEmpty()) {
                    value = "n/a";
                } else {
                    value = value.trim().replace(",", "");
                    if (!value.trim().isEmpty()) {
                        Integer fileId;
                        try {
                            fileId = Integer.valueOf(value);
                            value = getFileUrl(fileId);
                        } catch (NumberFormatException ignored) {
                        }
                    } else {
                        value = "<p>No file</p>";
                    }
                }
            }
            case SqlQueryUtil.ColumnFormat_DATE, SqlQueryUtil.ColumnFormat_DATE_WITHOUT_TIME_ZONE, SqlQueryUtil.ColumnFormat_WITHOUT_TIME_ZONE -> {
                if (value == null) {
                    value = "<font color='red'>--:--</font>";
                } else
                    try {
                        if ("short".equals(selectedColumn.getCustomDateFormat()) || selectedColumn.getCustomDateFormat() == null) {
                            DateFormat shortDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date shortDate = shortDateFormatter.parse(value);
                            value = shortDateFormat.format(shortDate);
                        } else if ("long".equals(selectedColumn.getCustomDateFormat())) {
                            DateFormat longDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date longDate = longDateFormatter.parse(value);
                            value = dateFormat.format(ServerUtils.convertServerDateToUserDate(longDate, user.getUserTimezone()));
                        }
                    } catch (ParseException e) {
                        try {
                            if ("short".equals(selectedColumn.getCustomDateFormat()) || selectedColumn.getCustomDateFormat() == null) {
                                Date shortDate = new Date(value);
                                value = shortDateFormat.format(shortDate);
                            } else if ("long".equals(selectedColumn.getCustomDateFormat())) {
                                Date longDate = new Date(value);
                                value = dateFormat.format(ServerUtils.convertServerDateToUserDate(longDate, user.getUserTimezone()));
                            }
                        } catch (Exception ignored) {
                        }
                    }
            }
            default -> {
                if (value == null || value.trim().isEmpty()) {
                    value = reportingEmptyValueString;
                }
            }
        }
        return value;
    }

    public String getFileUrl(Integer fileId) {
        String url;
        EdsUpload upload = (EdsUpload) uploadManager.get(fileId);
        if (upload != null) {
            String getLink = uploadManager.getFileURL(upload);
            if (!StringUtil.isEmpty(getLink)) {
                if (upload.getContentType() != null && upload.getContentType().startsWith("image/") || upload.getOriginalName().endsWith(".jpe")
                        || upload.getOriginalName().endsWith(".jpg") || upload.getOriginalName().endsWith(".jpeg")
                        || upload.getOriginalName().endsWith(".ico") || upload.getOriginalName().endsWith(".png")
                        || upload.getOriginalName().endsWith(".bmp") || upload.getOriginalName().endsWith(".gif")) {
                    url = "<img src='" + getLink + "'>";
                } else {
                    url = "<a href='" + getLink + "'>" + upload.getOriginalName() + "</a>";
                }
            } else {
                url = "<p>File not found</p>";
            }
        } else {
            url = "<a href='" + EdsContextParams.getFullHost() + "common/downloadFile?id=" + fileId + "'>File</a>";
        }
        return url;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ReportRpc getQueryTotalResult(ReportRpc report, Integer userId) {
        return reportingCoreService.getQueryTotalResult(report, userId);
    }

    @Transactional
    @Override
    public boolean deleteReport(Integer id) {
        return reportingCoreService.deleteReport(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public FolderRpc[] getFolders(Integer categoryId) {
        List<EdsFolders> folders;
        if (categoryId == null) {
            UserSecuritryRpc user = getUser();
            folders = foldersManager.list(user.getDomainName(), user.getCompanyId(), user.getUserId());
        } else {
            EdsReportTemplateCategory category = reportTemplateCategoryManager.get(categoryId);
            UserSecuritryRpc user = getUser();
            folders = foldersManager.getByCategory(category, user.getDomainName(), user.getCompanyId(), user.getUserId());
        }
        LinkedList<FolderRpc> items = new LinkedList<>();
        Map<String, EdsReportTemplateCategory> reportTemplateCategoryMap = reportTemplateCategoryManager.getReportTemplateCategoryListMap();
        for (EdsFolders item : folders) {
            FolderRpc rpc = item.toRpc();
            String categoryCode = item.getCategoryCode();
            if (categoryCode != null && hasPermissionToReportingCategories(categoryCode)) {
                rpc.setCategoryId(reportTemplateCategoryMap.get(categoryCode).getObjectID());
                EdsReference reference = item.getCode() != null ? referenceManager.findReferenceByCode(item.getCode()) : null;
                rpc.setCategoryName(referenceWfmMessageSource.localize(categoryCode));
                rpc.setName(reference != null ? reference.getName() : rpc.getName());
                items.add(rpc);
            }
        }
        return items.toArray(new FolderRpc[0]);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public FolderRpc[] searchFolders(String searchKey) {
        List<EdsFolders> folders;
        UserSecuritryRpc user = getUser();
        folders = foldersManager.search(searchKey, user.getDomainName(), user.getCompanyId(), user.getUserId());
        LinkedList<FolderRpc> items = new LinkedList<>();
        Map<String, EdsReportTemplateCategory> reportTemplateCategoryMap = reportTemplateCategoryManager.getReportTemplateCategoryListMap();
        for (EdsFolders item : folders) {
            FolderRpc rpc = item.toRpc();
            if (item.getCategoryCode() != null) {
                rpc.setCategoryId(reportTemplateCategoryMap.get(item.getCategoryCode()).getObjectID());
                rpc.setCategoryName(reportTemplateCategoryMap.get(item.getCategoryCode()).getName());
            }
            items.add(rpc);
        }
        return items.toArray(new FolderRpc[0]);
    }

    @Transactional
    @Override
    public Boolean saveFolder(FolderRpc rpc) {
        if (rpc.getId() == null) {
            return reportingCoreService.saveFolder(rpc);
        } else {
            return reportingCoreService.updateFolder(rpc);
        }
    }

    @Transactional
    @Override
    public Boolean deleteFolder(Integer id) {
        return reportingCoreService.deleteFolder(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public FolderRpc getFolder(Integer id) {
        return reportingCoreService.getFolder(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public SelectListRpc[] searchReport(String searchText) {
        String rolesCodeAsString = getRolesCodeAsString();
        ArrayList<SelectListRpc> list = new ArrayList<>();
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setSubscriptionTypeName(getUser().getDomainName());
        filter.setCompanyID(getUser().getCompanyId());
        filter.setUserID(getUser().getUserId());
        filter.setRoles(rolesCodeAsString);
        filter.setSearchKey(searchText);
        List<Object[]> items = reportingManager.listObject(filter);
        for (Object[] item : items) {
            ReportItem reportItem = new ReportItem(item).invoke();
            SelectListRpc selectListRpc = reportItem.toSelectListRpc();
            list.add(selectListRpc);
        }
        return list.toArray(new SelectListRpc[0]);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public SelectItem[] getFilterSelectItems(String searchKey, ColumnRpc columnRpc, ReportRpc report) {
        LinkedList<SelectItem> list = reportingCoreService.getFilterSelectItems(searchKey, report, columnRpc, false);
        return list.toArray(new SelectItem[0]);
    }

    @Transactional
    @Override
    public Boolean createFavouriteReportTemplate(Integer id) {
        return reportingCoreService.createFavouriteReportTemplate(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ReportRpc getReportResult(ReportRpc report) {
        ReportGenerateTableRpc temp = reportingCoreService.getReportResult(report);
        return temp != null ? temp.getReport() : null;
    }

    @Override
    public ChartData getReportChartData(ReportRpc report, boolean isFromRefresh) {
        return reportingCoreService.getReportChartData(report, isFromRefresh);
    }

    @Override
    public ChartData getReportChartDataForAi(String uuid, ChartTypeEnum chartType) {
        ReportRpc report = getReport(uuid);
        report.getChartConf().setType(chartType);
        RedisClient.removeKey(uuid);
        RedisClient.setKey(uuid,report,ReportRpc.class,1800);
        return reportingCoreService.getReportChartData(report, true);
    }

    @Override
    public ChartData getReportChartData(DashboardComponentItem gridItemConfig) {
        return reportingCoreService.getReportChartData(getReport(gridItemConfig.getReportId()), gridItemConfig.isFromResetButton());
    }

    @Override
    public KpiWidgetData getKpiWidgetData(DashboardComponentItem gridItemConfig) {
        return reportingCoreService.getKpiWidgetData(getReport(gridItemConfig.getReportId()), gridItemConfig.isFromResetButton());
    }

    @Transactional
    @Override
    public Integer saveReport(ReportRpc report) {
        String code;
        if (report.getId() == null) {
            code = report.getName().replaceAll("[^\\p{L}\\p{Nd}]|[\\p{InLatin-1Supplement}]+", "").toUpperCase();
            report.setCode(code);
        }
        this.saveReportGroups(report.getCode(), report.getColumnsByGroupMap());
        return reportingCoreService.saveReport(report);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<ReportDirectoryPathRpc> getReportTemplateList(ListingFilterParameter filter) {
        return reportingCoreService.getReportTemplateList(filter);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getXmlString(Integer reportId) {
        RpcConvertToXmlLocal rpcToXml = null;
        ReportRpc reportRpc = null;
        try {
            EdsReport report = reportingManager.get(reportId);
            report.getFakeReport();
            if (!report.getFakeReport()) {
                reportRpc = report.toRPC();
            }
            rpcToXml = new RpcConvertToXmlLocal(reportRpc);

            return rpcToXml.generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public byte[] getReportFile(Integer reportId, String type) {
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        Integer companyId = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        EdsCompany company = companyManager.getCompany(companyId);

        byte[] bytes;
        EdsReport report = reportingManager.get(reportId);

        ExcelReportHandler excelReportHandler = (ExcelReportHandler) ApplicationContextProvider.applicationContext.getBean("excelReportHandler");
        switch (type) {
            case "PDF" -> {
                try (ByteArrayOutputStream byteArrayOutputStream = recurrencePdfService.generatePDFFromHTML(report.toRPC(), report, company, PdfReferenceCodeNameEnum.REPORTING_SYSTEM.getUrl(), null)) {
                    bytes = byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "CSV" -> {
                ByteArrayOutputStream byteArrayOutputStream = excelReportHandler.run(getXmlString(reportId), companyId, user);
                GenerateReportToCsv csv = new GenerateReportToCsv(report.toRPC(), null, byteArrayOutputStream);
                bytes = csv.getStream().toByteArray();
            }
            default -> bytes = excelReportHandler.run(getXmlString(reportId), companyId, user).toByteArray();
        }


        return bytes;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ReportRpc getReportStructure(Integer xmlTemplateId) {
        EdsReportTemplate reportTemplate = reportTemplateManager.get(xmlTemplateId);
        if (reportTemplate == null) {
            return null;
        }
        ViewRpc viewRpc = reportingCoreService.getReportStructure(reportTemplate.getCode());
        ReportRpc reportRpc = new ReportRpc();

        for (TableRpc tableRpc : viewRpc.getTables()) {
            for (ColumnRpc columnRpc : tableRpc.getColumns()) {
                columnRpc.setChecked(true);
                reportRpc.getColumnMap().put(columnRpc.getName(), columnRpc);
            }
        }

        //Default Filter Column
        reportRpc.setFilterColumn(viewRpc.getFilterColumn());
        reportRpc.setFilterOperation(viewRpc.getFilterOperation());
        reportRpc.setFilterValue(viewRpc.getFilterValue());

        reportRpc.setViewName(reportTemplate.getName());
        reportRpc.setViewCode(reportTemplate.getCode());
        reportRpc.setXmlTemplateId(reportTemplate.getObjectID());
        reportRpc.setSelectedColumns(new LinkedList<>(reportRpc.getColumnMap().values()));

        if (getUser() != null) {
            reportRpc.setCompanyId(getUser().getCompanyId());
        }
        reportRpc.setFromKpi(viewRpc.isFromKpi());
        return reportRpc;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<Integer> getEmployeeIDsByReportID(Integer id) {
        return reportingCoreService.getEmployeeIDsByReportID(id);
    }

    @Transactional
    @Override
    public void changeFolderOfReport(Integer companyID, Integer reportId, String folderName) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsReport edsReport = reportingManager.get(reportId);
        String[] temp = folderName.split("->");
        EdsFolders edsFolders = foldersManager.getByCategoryAndName(temp[0].trim(), temp[1].trim());
        edsReport.setFolder(edsFolders);
        reportingManager.update(edsReport);
        log.info("Reporting Update! " + edsReport.getName() + " folder changed to" + edsFolders.getName());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public LinkedHashMap<String, LinkedHashMap<String, SelectItem>> getReportingTopMenu() {

        LinkedHashMap<String, LinkedHashMap<String, SelectItem>> map = new LinkedHashMap<>();
        LinkedHashMap<String, SelectItem> favouriteList = new LinkedHashMap<>();

        String rolesCodeAsString = getRolesCodeAsString();
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setSubscriptionTypeName(getUser().getDomainName());
        filter.setCompanyID(getUser().getCompanyId());
        filter.setUserID(getUser().getUserId());
        filter.setRoles(rolesCodeAsString);
        List<Object[]> list = reportingManager.listObject(filter);
        for (Object[] item : list) {
            ReportItem reportItem = new ReportItem(item).invoke();

            if (reportItem.getFavouriteId() != null) {

                String urlParams = "reporting|stepControl/" + reportItem.getReportId() + "/savedreport";
                SelectItem selectItem = new SelectItem(reportItem.getReportId(), reportItem.getReportName(), reportItem.getTargetLink(), reportItem.getNewcategoryName());
                selectItem.setParam(EncryptionHelper.encodeURL(EncryptionHelper.encryptURL(urlParams)));
                favouriteList.put(reportItem.getReportName(), selectItem);
            }
            if (reportItem.getNewcategoryName() != null && !map.containsKey(reportItem.getNewcategoryName())) {
                map.put(reportItem.getNewcategoryName(), null);
            }
        }
        map.put(MY_FAVOURITE_REPORTS, favouriteList);

        return map;
    }

    @Transactional
    @Override
    public Boolean saveMailList(MailListRpc item, ReportRpc report) {
        return reportingCoreService.saveMailList(item, report);
    }

    @Transactional
    @Override
    public String addOrRemoveProject(Integer reportId, boolean addProject) {
        reportingManager.addOrRemoveProject(reportId, addProject);
        return null;
    }

    private String getRolesCodeAsString() {
        boolean isAdmin = roleManager.hasRole(userManager.getUser(), Constants.ADMIN);
        String rolesCodeAsString = userManager.getUser().getRolesCodeAsString();
        if (isAdmin) {
            rolesCodeAsString = "";
        }
        return rolesCodeAsString;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserSecuritryRpc getUser() {
        return reportingCoreService.getUser();
    }

    private boolean hasPermissionToReportingCategories(String categoryCode) {
        if ("ACCOUNTING".equals(categoryCode) && ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            return true;
        }
        if ("CRM".equals(categoryCode) && ServerUtils.hasPermission(PermissionConstants.CRM_MAIN_MENU)) {
            return true;
        }
        if ("PM".equals(categoryCode) && ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            return true;
        }
        if ("HRMS".equals(categoryCode) && ServerUtils.hasPermission(PermissionConstants.HRMS_MAIN_MENU)) {
            return true;
        }
        if ("PAYROLL".equals(categoryCode) && ServerUtils.hasPermission(PermissionConstants.PAYROLL_MAIN_MENU)) {
            return true;
        }
        if ("DASHBOARD".equals(categoryCode) && ServerUtils.hasPermission(PermissionConstants.DASHBOARD_MAIN_MENU)) {
            return true;
        }
        if ("DOCUMENTS".equals(categoryCode) && ServerUtils.hasPermission(PermissionConstants.DOCUMENTS_MAIN_MENU)) {
            return true;
        }
        if ("SETTINGS".equals(categoryCode) && ServerUtils.hasPermission(PermissionConstants.SETTINGS_MAIN_MENU)) {
            return true;
        }
        if ("REPORTING".equals(categoryCode) && ServerUtils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU)) {
            return true;
        }
        if ("MYACCOUNT".equals(categoryCode) && ServerUtils.hasPermission(PermissionConstants.MYACCOUNT_MAIN_MENU)) {
            return true;
        }
        return "TC".equals(categoryCode) && ServerUtils.hasPermission(PermissionConstants.TC_MAIN_MENU);
    }
}
