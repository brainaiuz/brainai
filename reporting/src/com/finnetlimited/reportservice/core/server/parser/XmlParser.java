package com.finnetlimited.reportservice.core.server.parser;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.reporting.EdsReportTemplate;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportTemplateManager;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingRolePermissionItem;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.TableRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.client.ui.Constants;
import com.finnetlimited.reportservice.core.server.utils.SqlQueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DATA_TYPE_FILE_UPLOAD;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DATA_TYPE_PROFILE_IMAGE;

/**
 * User: ${Dilsh0d}
 * Date: 18-Mar-2010
 * Time: 16:39:22
 */
public class XmlParser implements Constants {

    private ReportTemplateManager reportTemplateManager = (ReportTemplateManager) ApplicationContextProvider.applicationContext.getBean("reportTemplateManager");
    private CompanyCustomFieldsManager companyCFSettingsManager = (CompanyCustomFieldsManager) ApplicationContextProvider.applicationContext.getBean("companyCFSettingsManager");
    private CommonServiceLocal commonServiceLocal = (CommonServiceLocal) ApplicationContextProvider.applicationContext.getBean("commonService");

    private Log log = LogFactory.getLog(getClass());

    @Transactional
    public ViewRpc getViewStructure(String viewCode) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        ViewRpc view = new ViewRpc();
        ArrayList<ColumnRpc> hiddenColumns = new ArrayList<>();
        view.setHiddenColumns(hiddenColumns);
        String locale = ServerUtils.getUserLocale().getLanguage();
        LinkedList<ColumnRpc> filerColumns = new LinkedList<>();
        try {
            EdsReportTemplate template = reportTemplateManager.getByCode(viewCode);
            if (template != null) {
                ArrayList<TableRpc> tables = new ArrayList<>();
                view.setTables(tables);
                XMLStreamReader parser = factory.createXMLStreamReader(new StringReader(template.getBody()));
                while (parser.hasNext()) {
                    String tagName = "";
                    int CONST = parser.next();

                    if (CONST == XMLStreamReader.END_ELEMENT) {
                        continue;
                    }

                    if (parser.hasName()) {
                        tagName = parser.getName().toString();
                    }

                    // start select
                    if (CONST == XMLStreamReader.START_ELEMENT && "category".equals(tagName)) {
                        view.setEntityName(parser.getElementText());
                    } else if (CONST == XMLStreamReader.START_ELEMENT && "select".equals(tagName)) {
                        if (parser.getAttributeCount() != 0 && "distinct".equals(parser.getAttributeValue(0))) {
                            view.setAgregateFunction("distict");
                        }
                        while (parser.hasNext()) {
                            tagName = "";
                            CONST = parser.next();

                            if (parser.hasName()) {
                                tagName = parser.getName().toString();
                            }

                            if (CONST == XMLStreamReader.END_ELEMENT && "select".equals(tagName)) {
                                break;
                            }

                            // start table
                            if (CONST == XMLStreamReader.START_ELEMENT && "table".equals(tagName)) {


                                LinkedList<ColumnRpc> columns = new LinkedList<>();

                                TableRpc table = new TableRpc();
                                table.setTableName(parser.getAttributeValue(0));
                                table.setColumns(columns);
                                tables.add(table);

                                if (containsAttributes(parser, new String[]{"customFieldType"})) {
                                    table.setCustomFieldType(getAttributeValue(parser, "customFieldType"));
                                    table.setCustomFieldEntityName(getAttributeValue(parser, "customFieldEntityName"));
                                    table.setCustomFieldAlias(getAttributeValue(parser, "customFieldAlias"));
                                    table.setCustomFieldJoin(getAttributeValue(parser, "customFieldJoin"));
                                    if (table.getCustomFieldAlias() == null || "".equals(table.getCustomFieldAlias())) {
                                        table.setCustomFieldAlias("fcc");
                                    }
                                    if (table.getCustomFieldType() != null) {
                                        List<EdsCompanyCustomFieldsSettings> companyCustomFieldItems;
                                        if (table.getCustomFieldEntityName() != null) {
                                            companyCustomFieldItems = companyCFSettingsManager.getCompanyCustomFieldsWithCategory(table.getCustomFieldType(), table.getCustomFieldEntityName());
                                        } else if (template.getStepId() != null && ViewName.OnboardingStep.name().equals(table.getCustomFieldType())) {
                                            companyCustomFieldItems = commonServiceLocal.getEdsCompanyCustomFieldsSettingses(template.getStepId(), table.getCustomFieldType());
                                        } else {
                                            companyCustomFieldItems = companyCFSettingsManager.getCompanyCustomFieldsByEntityName(table.getCustomFieldType());
                                        }
                                        for (EdsCompanyCustomFieldsSettings customFieldsSetting : companyCustomFieldItems) {
                                            if (!(DATA_TYPE_FILE_UPLOAD.equals(customFieldsSetting.getDataType()) || DATA_TYPE_PROFILE_IMAGE.equals(customFieldsSetting.getDataType()))) {
                                                createTableColumFromCustomField(customFieldsSetting, filerColumns, columns, table);
                                            }
                                        }
                                    }
                                }

                                while (parser.hasNext()) {
                                    tagName = "";
                                    CONST = parser.next();

                                    if (parser.hasName()) {
                                        tagName = parser.getName().toString();
                                    }

                                    if (CONST == XMLStreamReader.END_ELEMENT && "table".equals(tagName)) {
                                        break;
                                    }

                                    if (CONST == XMLStreamReader.START_ELEMENT && "column".equals(tagName)) {
                                        parceTableColum(view, filerColumns, parser, columns, hiddenColumns, locale);
                                    }
                                }
                            }
                            view.setFilterColumns(filerColumns);
                        }
                    } else if (CONST == XMLStreamReader.START_ELEMENT && "customfilter".equals(tagName)) {
                        view.setCustomFilterEnabled(true);
                        LinkedList<ColumnRpc> customFilterColumns = new LinkedList<>();
                        ColumnRpc customFilterColumn;
                        while (parser.hasNext()) {
                            customFilterColumn = new ColumnRpc();
                            tagName = "";
                            CONST = parser.next();

                            if (parser.hasName()) {
                                tagName = parser.getName().toString();
                            }
                            if (CONST == XMLStreamReader.START_ELEMENT && "terms".equals(tagName)) {
                                for (int i = 0; i < parser.getAttributeCount(); i++) {
                                    String name = parser.getAttributeName(i).toString();
                                    String value = parser.getAttributeValue(i);
                                    if ("name".equals(name)) {
                                        customFilterColumn.setTitle(value);
                                        customFilterColumn.setName(value);
                                    } else if ("custom_field".equals(name)) {
                                        customFilterColumn.setCustomField(value);
                                    } else if ("first_value".equals(name)) {
                                        customFilterColumn.setFirstValue(value);
                                    } else if ("second_value".equals(name)) {
                                        customFilterColumn.setSecondValue(value);
                                    } else if ("display_items".equals(name)) {
                                        customFilterColumn.setDisplayItems(value);
                                    } else if ("result_items".equals(name)) {
                                        customFilterColumn.setReturningItems(value);
                                    } else if ("custom_query".equals(name)) {
                                        customFilterColumn.setCustomQuery(value);
                                    }
                                }
                            }

                            if (customFilterColumn.getCustomField() != null && !"".equals(customFilterColumn.getCustomField())
                                    && customFilterColumn.getFirstValue() != null && !"".equals(customFilterColumn.getFirstValue())
                                    | (customFilterColumn.getDisplayItems() != null && !"".equals(customFilterColumn.getDisplayItems())
                                    && customFilterColumn.getReturningItems() != null && !"".equals(customFilterColumn.getReturningItems())
                                    && customFilterColumn.getCustomQuery() != null && !"".equals(customFilterColumn.getCustomQuery()))
                            ) {
                                customFilterColumns.add(customFilterColumn);
                            }

                        }
                        view.setCustomFilterColumns(customFilterColumns);
                    } else if (CONST == XMLStreamReader.START_ELEMENT && "from".equals(tagName)) {
                        view.setQueries(parser.getElementText());
                    } else if (CONST == XMLStreamReader.START_ELEMENT && "join-conditions".equals(tagName)) {
                        HashMap<String, String> joins = new HashMap<>();
                        while (parser.hasNext()) {
                            tagName = "";
                            CONST = parser.next();

                            if (parser.hasName()) {
                                tagName = parser.getName().toString();
                            }

                            if (CONST == XMLStreamReader.END_ELEMENT && "join-conditions".equals(tagName)) {
                                break;
                            }

                            if (CONST == XMLStreamReader.START_ELEMENT && "join".equals(tagName)) {
                                String[] keys = parser.getAttributeValue(0).split(",");
                                String value = parser.getElementText();
                                for (String key : keys) {
                                    joins.put(key, value);
                                }
                            }
                        }
                        view.setJoins(joins);
                    } else if (CONST == XMLStreamReader.START_ELEMENT && "where".equals(tagName)) {
                        HashMap<String, String> customReplacements = new HashMap<>();
                        while (parser.hasNext()) {
                            tagName = "";
                            CONST = parser.next();

                            if (parser.hasName()) {
                                tagName = parser.getName().toString();
                            }

                            if (CONST == XMLStreamReader.END_ELEMENT && "where".equals(tagName)) {
                                view.setCustomReplacements(customReplacements);
                                break;
                            }

                            if (CONST == XMLStreamReader.START_ELEMENT && "terms".equals(tagName)) {
                                if ("userid".equals(parser.getAttributeValue(0))) {
                                    view.setIsAdmin("FALSE");
                                    view.setUserId(parser.getAttributeValue(1));
                                    view.setUCompanyId(parser.getAttributeValue(2));
                                } else if ("managerid".equals(parser.getAttributeValue(0))) {
                                    view.setIsAdmin("FALSE");
                                    view.setManagerId(parser.getAttributeValue(1));
                                    view.setMCompanyId(parser.getAttributeValue(2));
                                } else if ("leaderid".equals(parser.getAttributeValue(0))) {
                                    view.setIsAdmin("FALSE");
                                    view.setLeaderId(parser.getAttributeValue(1));
                                    view.setLCompanyId(parser.getAttributeValue(2));
                                } else if ("admin".equals(parser.getAttributeValue(0))) {
                                    view.setIsAdmin("TRUE");
                                    view.setACompanyId(parser.getAttributeValue(1));
                                } else if ("client".equals(parser.getAttributeValue(0))) {
                                    view.setIsAdmin("FALSE");
                                    view.setClientId(parser.getAttributeValue(1));
                                } else if ("base".equals(parser.getAttributeValue(0))) {
                                    if (parser.getAttributeCount() <= 2) {
                                        view.setWhereBase(parser.getAttributeValue(1));
                                    }
                                }
                            } else if (CONST == XMLStreamReader.START_ELEMENT && "replacement".equals(tagName)) {
                                if ("userid".equals(parser.getAttributeValue(0))) {
                                    view.setIsAdmin("FALSE");
                                    view.setReplacementUserId(parser.getAttributeValue(1));
                                } else {
                                    ReportingRolePermissionItem item = new ReportingRolePermissionItem();
                                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                                        String name = parser.getAttributeName(i).toString().toLowerCase();
                                        if ("code".equals(name)) {
                                            item.setCode(parser.getAttributeValue(i));
                                        } else if ("name".equals(name)) {
                                            item.setName(parser.getAttributeValue(i));
                                        } else if ("value".equals(name)) {
                                            item.setValue(parser.getAttributeValue(i));
                                        } else if ("permission".equals(name)) {
                                            item.setRole(parser.getAttributeValue(i));
                                        }
                                    }
                                    view.getRolePermissionFilterString().put(parser.getAttributeValue(0), item);
                                }
                                if ("value".equals(parser.getAttributeName(1).toString())) {
                                    customReplacements.put(parser.getAttributeValue(0), parser.getAttributeValue(1));
                                }
                            }
                        }

                    } else if (CONST == XMLStreamReader.START_ELEMENT && "group".equals(tagName)) {
                        view.setGroup(parser.getElementText());
                    } else if (CONST == XMLStreamReader.START_ELEMENT && "connection".equals(tagName)) {
                        while (parser.hasNext()) {
                            CONST = parser.next();
                            tagName = "";
                            if (parser.hasName()) {
                                tagName = parser.getName().toString();
                            }

                            if (CONST == XMLStreamReader.END_ELEMENT && "connection".equals(tagName)) {
                                break;
                            }
                            if (CONST == XMLStreamReader.START_ELEMENT && "url".equals(tagName)) {
                                view.setCustomUrl(parser.getAttributeValue(0));
                            } else if (CONST == XMLStreamReader.START_ELEMENT && "username".equals(tagName)) {
                                view.setCustomUsername(parser.getAttributeValue(0));
                            } else if (CONST == XMLStreamReader.START_ELEMENT && "password".equals(tagName)) {
                                view.setCustomPassword(parser.getAttributeValue(0));
                            }
                        }
                    } else if (CONST == XMLStreamReader.START_ELEMENT && "sqlquery".equals(tagName)) {
                        view.setNoTimezone((parser.getAttributeCount() > 0 && "notimezone".equals(parser.getAttributeName(0).toString()) && "true".equals(parser.getAttributeValue(0).toLowerCase())));
                    } else if (CONST == XMLStreamReader.START_ELEMENT && "conditions".equals(tagName)) {
                        while (parser.hasNext()) {
                            CONST = parser.next();
                            tagName = "";
                            if (parser.hasName()) {
                                tagName = parser.getName().toString();
                            }

                            if (CONST == XMLStreamReader.END_ELEMENT && "conditions".equals(tagName)) {
                                break;
                            }
                            if (CONST == XMLStreamReader.START_ELEMENT && "condition".equals(tagName)) {
                                view.setConditionValue(parser.getAttributeValue(1));
                                view.setConditionCode(parser.getAttributeValue(2));
                                view.setConditionType(parser.getAttributeValue(3));
                            }
                        }
                    } else if (CONST == XMLStreamReader.START_ELEMENT && "fromkpi".equals(tagName)) {
                        view.setFromKpi("1".equals(parser.getAttributeValue(0)));
                    }
                }
            }
        } catch (XMLStreamException e) {
            log.info("Read xml file exception:", e);
        }
        return view;
    }

    private String getAttributeValue(XMLStreamReader parser, String attributeName) {
        if (attributeName != null && !"".equals(attributeName)) {
            for (int i = 0; i < parser.getAttributeCount(); i++) {
                if (parser.hasName() && attributeName.equals(parser.getAttributeName(i).toString())) {
                    return parser.getAttributeValue(i);
                }
            }
        }
        return null;
    }

    private boolean containsAttributes(XMLStreamReader parser, String[] strings) {
        List<String> names = new ArrayList<>(Arrays.asList(strings));
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            if (parser.hasName()) {
                names.remove(parser.getAttributeName(i).toString());
            }
        }
        return names.isEmpty();
    }

    private void createTableColumFromCustomField(EdsCompanyCustomFieldsSettings customFieldSettings, LinkedList<ColumnRpc> filerColumns, LinkedList<ColumnRpc> columns, TableRpc table) {
        ColumnRpc column = new ColumnRpc();
        columns.add(column);
        column.setIsCustomField(true);
        column.setPrefix(table.getCustomFieldAlias());
        column.setCustomFieldJoin(table.getCustomFieldJoin());
        column.setDrillDownReport(false);
        column.setName(table.getCustomFieldAlias() + "." + customFieldSettings.getColumnCode());
        column.setTitle(customFieldSettings.getFieldName());
        column.setType(customFieldSettings.getDataType());
        if (com.edatasite.workforce.gwt.core.client.ui.Constants.DATA_TYPE_DATE.equals(customFieldSettings.getDataType())) {
            column.setType(SqlQueryUtil.ColumnFormat_DATE);
            column.setColumnFormat(SqlQueryUtil.ColumnFormat_DATE_WITHOUT_TIME_ZONE);
            column.setCustomDateFormat("short");
            if (com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER_TIME.equals(customFieldSettings.getUiType())) {
                column.setCustomDateFormat("long");
            }
        } else if (com.edatasite.workforce.gwt.core.client.ui.Constants.DATA_TYPE_NUMBER.equals(customFieldSettings.getDataType())) {
            column.setType(SqlQueryUtil.ColumnFormat_NUMBER);
            column.setColumnFormat(SqlQueryUtil.ColumnFormat_DOUBLE);
        } else if (DATA_TYPE_FILE_UPLOAD.equals(customFieldSettings.getDataType())) {
            column.setType(SqlQueryUtil.ColumnFormat_NUMBER);
            column.setColumnFormat(SqlQueryUtil.ColumnFormat_IMAGE);
        } else {
            column.setType(SqlQueryUtil.ColumnFormat_STRING);
            column.setColumnFormat(SqlQueryUtil.ColumnFormat_STRING);
        }
        if (com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_ENTITY_DROPDOWN.equals(customFieldSettings.getUiType())) {
            column.setLookupSql(customFieldSettings.getQuery());
        }
        filerColumns.add(column);
    }

    private void parceTableColum(ViewRpc view, LinkedList<ColumnRpc> filerColumns, XMLStreamReader parser, LinkedList<ColumnRpc> columns, ArrayList<ColumnRpc> hiddenColumns, String locale) {
        ColumnRpc column = new ColumnRpc();
        columns.add(column);
        column.setDrillDownReport(false);
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            String name = parser.getAttributeName(i).toString();
            String value = parser.getAttributeValue(i);
            if ("name".equals(name)) {
                column.setName(value);
            } else if ("name_uz".equals(name) && "uz".equalsIgnoreCase(locale)) {
                column.setName(value);
            } else if ("name_ru".equals(name) && "ru".equalsIgnoreCase(locale)) {
                column.setName(value);
            } else if ("title".equals(name)) {
                column.setTitle(value);
            } else if ("title_uz".equals(name) && "uz".equalsIgnoreCase(locale)) {
                column.setTitle(value);
            } else if ("title_ru".equals(name) && "ru".equalsIgnoreCase(locale)) {
                column.setTitle(value);
            } else if ("title_ar".equals(name) && "ar".equalsIgnoreCase(locale)) {
                column.setTitle(value);
            } else if ("custom_title".equals(name) && "cst".equalsIgnoreCase(locale)) {
                column.setTitle(value);
            } else if ("type".equals(name)) {
                column.setType(value);
            } else if ("formattype".equals(name)) {
                column.setColumnFormat(value);
                if ("hidden".equals(value)) {
                    columns.remove(column);
                    view.setId(parser.getAttributeValue("", "name"));
                } else if ("hidden2".equals(value)) {
                    columns.remove(column);
                    hiddenColumns.add(column);
                }
            } else if ("lookupfield".equals(name)) {
                column.setLookUpField(value);
            } else if ("lookupsql".equals(name)) {
                column.setLookupSql(value);
            } else if ("filterwidgettype".equals(name)) {
                view.setEnabledFilterWidget(true);
                column.setFilterWidgetType(value);
            } else if ("customdateformat".equals(name)) {
                column.setCustomDateFormat(value);
            } else if ("treeSelect".equals(name)) {
                column.setTreeSelect(value.toLowerCase().equals("true"));
            } else if ("table".equals(name)) {
                column.setTable(value);
            } else if ("column".equals(name)) {
                column.setColumn(value);
            } else if ("id".equals(name)) {
                column.setId(value);
            } else if ("parent".equals(name)) {
                column.setParent(value);
            } else if ("where".equals(name)) {
                column.setWhere(value);
            } else if ("splitter".equals(name)) {
                column.setSplitter(value);
            } else if ("reletedcolumn".equals(name) && !"".equals(value)) {
                column.setReletedColumn(value);
            } else if ("reletedprojectcolumn".equals(name) && !"".equals(value)) {
                column.setReletedProjectColumn(value);
            } else if ("basicfilter".equals(name) && "true".equals(value)) {
                filerColumns.add(column);
            } else if ("defaultselected".equals(name) && "true".equals(value)) {
                column.setChecked(true);
            } else if ("defaultfilter".equals(name) && null != value) {
                String[] filter = value.split("\\|");
                if (filter.length > 0) {
                    view.setFilterColumn(filter[0]);
                }
                if (filter.length > 1) {
                    view.setFilterOperation(filter[1]);
                }
                if (filter.length > 2) {
                    view.setFilterValue(filter[2]);
                }
            }
        }
    }

    /**
     * Read report settings from XML
     * if change need correction code in class
     * multidb/reporting/src/com/finnetlimited/reportservice/core/client/ui/xml/RpcConvertToXml.java
     */
    public ReportRpc getReportStructure() {
        return getReportStructure();
    }

    /**
     * Read report settings from XML
     * if change need correction code in class
     * multidb/reporting/src/com/finnetlimited/reportservice/core/client/ui/xml/RpcConvertToXml.java
     *
     * @param xmlText
     */
    public ReportRpc getReportStructure(String xmlText) {
        ReportRpc report = new ReportRpc();
        String locale = ServerUtils.getUserLocale().getLanguage();
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader xmlParser = factory.createXMLStreamReader(new StringReader(xmlText));
            while (xmlParser.hasNext()) {
                String tagName = "";
                int CONST = xmlParser.next();
                if (XMLStreamReader.END_ELEMENT == CONST) {
                    continue;
                }
                if (xmlParser.hasName()) {
                    tagName = xmlParser.getName().toString();
                }

                if ("id".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setId(Integer.valueOf(data));
                    }
                } else if ("name".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setName(data);
                    }
                } else if ("discreption".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setDiscreption(data);
                    }
                } else if ("folderid".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setFolderId(Integer.valueOf(data));
                    }
                } else if ("viewname".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setViewName(data);
                    }
                } else if ("tabletype".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setTableType(data);
                    }
                } else if ("sntfiltername".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setSntFilterName(data);
                    }
                } else if ("durationtype".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setDurationType(data);
                    }
                } else if ("startdate".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setStartDate(data);
                    }
                } else if ("enddate".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setEndDate(data);
                    }
                } else if ("sntfilternamechange".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setSntFilterNameChange(data);
                    }
                } else if ("durationtypechange".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setDurationTypeChange(data);
                    }
                } else if ("startdatechange".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setStartDateChange(data);
                    }
                } else if ("enddatechange".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setEndDateChange(data);
                    }
                } else if ("isdetailed".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setIsDetailed(Boolean.valueOf(data));
                    } else {
                        report.setIsDetailed(true);
                    }
                } else if ("showrowcount".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setShowRowCount(Boolean.valueOf(data));
                    }
                } else if ("rolechange".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setRoleChange(data);
                    }
                } else if ("templateid".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setPdfTemplateId(Integer.valueOf(data));
                    }
                } else if ("excelTemplateId".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setExcelTemplateId(Integer.valueOf(data));
                    }
                } else if ("sortByColumn".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setSortTableByColumn(data);
                    }
                } else if ("sortType".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (data != null && !"".equals(data)) {
                        report.setSortTableByColumnType(data);
                    }
                } else if ("sorttypes".equals(tagName)) {
                    ArrayList<String> list = getListByTagName(xmlParser, tagName);
                    report.setSortTypes(list);
                } else if ("rangetype".equals(tagName)) {
                    ArrayList<String> list = getListByTagName(xmlParser, tagName);
                    report.setRangeType(list);
                } else if ("sett".equals(tagName)) {
                    ArrayList<Integer> list = getSettListByTagName(xmlParser, tagName);
                    report.setSett(list);
                } else if ("fieldd".equals(tagName)) {
                    LinkedList<ColumnRpc> list = getColumnRpcByTagName(xmlParser, tagName, locale);
                    report.setFieldd(list);
                } else if ("operators".equals(tagName)) {
                    ArrayList<String> list = getListByTagName(xmlParser, tagName);
                    report.setOperators(list);
                } else if ("values".equals(tagName)) {
                    ArrayList<String> list = getListByTagName(xmlParser, tagName);
                    report.setValues(list);
                } else if ("boolType".equals(tagName)) {
                    ArrayList<String> list = getListByTagName(xmlParser, tagName);
                    report.setBoolType(list);
                } else if ("groupColumns".equals(tagName)) {
                    LinkedList<ColumnRpc> list = getColumnRpcByTagName(xmlParser, tagName, locale);
                    report.setGroupColumns(list);
                } else if ("sumaries".equals(tagName)) {
                    LinkedList<ColumnRpc> list = getColumnRpcByTagName(xmlParser, tagName, locale);
                    report.setSumaries(list);
                } else if ("selectedColumns".equals(tagName)) {
                    LinkedList<ColumnRpc> list = getColumnRpcByTagName(xmlParser, tagName, locale);
                    report.setSelectedColumns(list);
                } else if ("columnMap".equals(tagName)) {
                    HashMap<String, ColumnRpc> columnMap = getColumnMapByTagName(xmlParser, tagName, locale);
                    report.setColumnMap(columnMap);
                } else if ("limit".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setLimit(Integer.parseInt(data));
                    }
                } else if ("position".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setPosition(Integer.parseInt(data));
                    }
                } else if ("timeZone".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setBrowserTimeZone(data);
                    }
                } else if ("xmlTemplateId".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setXmlTemplateId(Integer.parseInt(data));
                    }
                } else if ("viewCode".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setViewCode(data);
                    }
                } else if ("sortByColumn".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setSortTableByColumn(data);
                    }
                } else if ("sortType".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setSortTableByColumnType(data);
                    }
                } else if ("customFilter".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        String[] customFilter = data.substring(data.indexOf("{") + 1, data.indexOf("}")).split(",");
                        HashMap<String, String> customFilters = new HashMap<>();
                        for (String aCustomFilter : customFilter) {
                            String[] filter = aCustomFilter.split("=");
                            if (filter.length < 2) {
                                customFilters.put(filter[0], "''");
                            } else {
                                customFilters.put(filter[0], filter[1]);
                            }
                        }
                        report.setCustomFilter(customFilters);
                    }
                } else if ("maxExcelRowCount".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setMaxExcelRowCount(Integer.parseInt(data));
                    }
                } else if ("landscape".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setLandscape(Boolean.parseBoolean(data));
                    }
                } else if ("filterPattern".equals(tagName)) {
                    String data = ServerUtils.decrypt(xmlParser.getElementText());
                    if (!"".equals(data)) {
                        report.setFilterPattern(data);
                    }
                }
            }
        } catch (XMLStreamException e) {
            log.info("Xml not correctly wrote:", e);
        }
        return report;
    }

    private ArrayList<String> getListByTagName(XMLStreamReader parser, String ownerTag) {
        ArrayList<String> values = new ArrayList<>();
        try {
            while (parser.hasNext()) {
                String tagName = "";
                int CONST = parser.next();
                if (parser.hasName()) {
                    tagName = parser.getName().toString();
                }
                if (ownerTag.equals(tagName) && XMLStreamReader.END_ELEMENT == CONST) {
                    break;
                }

                if (XMLStreamReader.START_ELEMENT == CONST && "value".equals(tagName)) {
                    values.add(ServerUtils.decrypt(parser.getElementText()));
                }
            }
        } catch (XMLStreamException e) {
            log.info("Set values in List Exception:", e);
        }
        return values;
    }

    private ArrayList<Integer> getSettListByTagName(XMLStreamReader parser, String ownerTag) {
        ArrayList<Integer> values = new ArrayList<>();
        try {
            while (parser.hasNext()) {
                String tagName = "";
                int CONST = parser.next();
                if (parser.hasName()) {
                    tagName = parser.getName().toString();
                }
                if (ownerTag.equals(tagName) && XMLStreamReader.END_ELEMENT == CONST) {
                    break;
                }

                if (XMLStreamReader.START_ELEMENT == CONST && "value".equals(tagName)) {
                    values.add(Integer.valueOf(ServerUtils.decrypt(parser.getElementText())));
                }
            }
        } catch (XMLStreamException e) {
            log.info("Set values in List Exception:", e);
        }
        return values;
    }

    private LinkedList<ColumnRpc> getColumnRpcByTagName(XMLStreamReader parser, String ownerTag, String locale) {
        LinkedList<ColumnRpc> values = new LinkedList<>();
        try {
            while (parser.hasNext()) {
                String tagName = "";
                int CONST = parser.next();
                if (parser.hasName()) {
                    tagName = parser.getName().toString();
                }
                if (ownerTag.equals(tagName) && XMLStreamReader.END_ELEMENT == CONST) {
                    break;
                }

                if (XMLStreamReader.START_ELEMENT == CONST && "column".equals(tagName)) {
                    ColumnRpc column = new ColumnRpc();
                    while (parser.hasNext()) {
                        tagName = "";
                        CONST = parser.next();
                        if (parser.hasName()) {
                            tagName = parser.getName().toString();
                        }
                        if ("column".equals(tagName) && XMLStreamReader.END_ELEMENT == CONST) {
                            break;
                        }
                        if (XMLStreamReader.END_ELEMENT == CONST) {
                            continue;
                        }

                        if ("name".equals(tagName)) {
                            column.setName(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("title".equals(tagName)) {
                            column.setTitle(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("title_uz".equals(tagName) && "uz".equalsIgnoreCase(locale)) {
                            column.setTitle(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("title_ru".equals(tagName) && "ru".equalsIgnoreCase(locale)) {
                            column.setTitle(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("title_ar".equals(tagName) && "ar".equalsIgnoreCase(locale)) {
                            column.setTitle(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("custom_title".equals(tagName) && "cst".equalsIgnoreCase(locale)) {
                            column.setTitle(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("type".equals(tagName)) {
                            String data = ServerUtils.decrypt(parser.getElementText());
                            if (!"".equals(data)) {
                                column.setType(data);
                            }
                        } else if ("format".equals(tagName)) {
                            String data = ServerUtils.decrypt(parser.getElementText());
                            if (!"".equals(data)) {
                                column.setColumnFormat(data);
                            }
                        } else if ("checked".equals(tagName)) {
                            column.setChecked(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("sum".equals(tagName)) {
                            column.setSum(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("avg".equals(tagName)) {
                            column.setAvg(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("largest".equals(tagName)) {
                            column.setLargest(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("smallest".equals(tagName)) {
                            column.setSmallest(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("count".equals(tagName)) {
                            column.setCount(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("reletedcolumn".equals(tagName)) {
                            String data = ServerUtils.decrypt(parser.getElementText());
                            if (data != null && !"".equals(data)) {
                                column.setReletedColumn(data);
                            }
                        } else if ("reletedprojectcolumn".equals(tagName)) {
                            String data = ServerUtils.decrypt(parser.getElementText());
                            if (data != null && !"".equals(data)) {
                                column.setReletedProjectColumn(data);
                            }
                        } else if ("customdateformat".equals(tagName)) {
                            column.setCustomDateFormat(ServerUtils.decrypt(parser.getElementText()));

                        } else if ("prefix".equals(tagName)) {
                            column.setPrefix(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("isCustomField".equals(tagName)) {
                            column.setIsCustomField(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("customFieldJoin".equals(tagName)) {
                            column.setCustomFieldJoin(ServerUtils.decrypt(parser.getElementText()));
                        }
                    }
                    values.add(column);
                }
            }
        } catch (XMLStreamException e) {
            log.info("Set values in List Exception:", e);
        }
        return values;
    }

    private HashMap<String, ColumnRpc> getColumnMapByTagName(XMLStreamReader parser, String ownerTag, String locale) {
        HashMap<String, ColumnRpc> values = new HashMap<>();
        try {
            while (parser.hasNext()) {
                String tagName = "";
                int CONST = parser.next();
                if (parser.hasName()) {
                    tagName = parser.getName().toString();
                }
                if (ownerTag.equals(tagName) && XMLStreamReader.END_ELEMENT == CONST) {
                    break;
                }

                if (XMLStreamReader.START_ELEMENT == CONST && "column".equals(tagName)) {
                    ColumnRpc column = new ColumnRpc();
                    String columnMapKey = "";
                    while (parser.hasNext()) {
                        tagName = "";
                        CONST = parser.next();
                        if (parser.hasName()) {
                            tagName = parser.getName().toString();
                        }
                        if ("column".equals(tagName) && XMLStreamReader.END_ELEMENT == CONST) {
                            break;
                        }
                        if (XMLStreamReader.END_ELEMENT == CONST) {
                            continue;
                        }

                        if ("columnMapKey".equals(tagName)) {
                            columnMapKey = ServerUtils.decrypt(parser.getElementText());
                        } else if ("name".equals(tagName)) {
                            column.setName(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("title".equals(tagName)) {
                            column.setTitle(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("title_uz".equals(tagName) && "uz".equalsIgnoreCase(locale)) {
                            column.setTitle(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("title_ru".equals(tagName) && "ru".equalsIgnoreCase(locale)) {
                            column.setTitle(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("title_ar".equals(tagName) && "ar".equalsIgnoreCase(locale)) {
                            column.setTitle(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("custom_title".equals(tagName) && "cst".equalsIgnoreCase(locale)) {
                            column.setTitle(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("type".equals(tagName)) {
                            String data = ServerUtils.decrypt(parser.getElementText());
                            if (!"".equals(data)) {
                                column.setType(data);
                            }
                        } else if ("format".equals(tagName)) {
                            String data = ServerUtils.decrypt(parser.getElementText());
                            if (!"".equals(data)) {
                                column.setColumnFormat(data);
                            }
                        } else if ("checked".equals(tagName)) {
                            column.setChecked(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("sum".equals(tagName)) {
                            column.setSum(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("avg".equals(tagName)) {
                            column.setAvg(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("largest".equals(tagName)) {
                            column.setLargest(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("smallest".equals(tagName)) {
                            column.setSmallest(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("count".equals(tagName)) {
                            column.setCount(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("reletedcolumn".equals(tagName)) {
                            String data = ServerUtils.decrypt(parser.getElementText());
                            if (data != null && !"".equals(data)) {
                                column.setReletedColumn(data);
                            }
                        } else if ("reletedprojectcolumn".equals(tagName)) {
                            String data = ServerUtils.decrypt(parser.getElementText());
                            if (data != null && !"".equals(data)) {
                                column.setReletedProjectColumn(data);
                            }
                        } else if ("customdateformat".equals(tagName)) {
                            column.setCustomDateFormat(ServerUtils.decrypt(parser.getElementText()));

                        } else if ("prefix".equals(tagName)) {
                            column.setPrefix(ServerUtils.decrypt(parser.getElementText()));
                        } else if ("isCustomField".equals(tagName)) {
                            column.setIsCustomField(Boolean.valueOf(ServerUtils.decrypt(parser.getElementText())));
                        } else if ("customFieldJoin".equals(tagName)) {
                            column.setCustomFieldJoin(ServerUtils.decrypt(parser.getElementText()));
                        }
                    }
                    values.put(columnMapKey, column);
                }
            }
        } catch (XMLStreamException e) {
            log.info("Set values in List Exception:", e);
        }
        return values;
    }
}
