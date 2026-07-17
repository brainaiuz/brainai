package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrChartOfAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrExpenseReportRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrLeaveRequestConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrOpportunityRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.context.support.WfmResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SimpleFacetFieldEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 03-Jun-2011
 * Time: 16:00:59
 */
public class SolrFacetUtils {

    public static final String SPLIT = "@";

    /**
     * <h1>... This is method generate facet filter  solr query ...</h1>
     * <br/>
     * <h2>... Write developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date { 16:48 03/06/2011 } ...</h3>
     *
     * @param facetFieldRpc
     * @param edsCompany
     * @param startDateSolrName
     * @param endDateSolrName
     * @return String solr query
     */
    public static String generatedFacetFilterSolrQuery(FacetFilterRpc facetFieldRpc, EdsCompany edsCompany, String startDateSolrName, String endDateSolrName, String... codeNameList) {
        StringBuilder solrQuery = new StringBuilder();
        if (facetFieldRpc != null) {
            Set<String> keySet = facetFieldRpc.getShowSolrFieldMap().keySet();
            if (codeNameList != null && codeNameList.length != 0) {
                keySet = new HashSet<>(Arrays.asList(codeNameList));
            }
            for (String codeName : keySet) {
                if (facetFieldRpc.getFacetContentMap().containsKey(codeName)) {
                    FacetContentRpc facetContent = facetFieldRpc.getFacetContentMap().get(codeName);
                    if (facetContent != null && facetContent.getFacetItems().length != 0) {
                        SelectItem[] items = facetContent.getFacetItems();
                        solrQuery.append(" AND (");
                        boolean appendOperator = false;
                        for (SelectItem item : items) {
                            if (appendOperator) {
                                solrQuery.append(" OR ");
                            } else {
                                appendOperator = true;
                            }
                            if (facetFieldRpc.getShowSolrFieldMap().get(codeName).isConditionItemId()) {
                                solrQuery.append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":").append(item.getId());
                            } else {
                                solrQuery.append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":(").append(QueryBuilderForSolr.normalaizeKeywordForFacet(item.getName())).append(")");
                            }
                        }
                        solrQuery.append(") ");
                    }
                }
            }

            if (facetFieldRpc.getStartDate() != null && facetFieldRpc.getEndDate() != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                if (!ServerUtils.isNullOrEmpty(facetFieldRpc.getSelectedDateSolrCodeName())) {
                    solrQuery.append(" AND (").append(facetFieldRpc.getSelectedDateSolrCodeName()).append(":[")
                            .append(format.format(facetFieldRpc.getStartDate())).append(" TO ").append(format.format(facetFieldRpc.getEndDate())).append(" ])");
                } else {
                    if (startDateSolrName != null && endDateSolrName != null) {
                        solrQuery.append(" AND ((").append(startDateSolrName).append(":[ * TO ")
                                .append(format.format(facetFieldRpc.getEndDate())).append(" ]) AND ");
                        solrQuery.append(" (").append(endDateSolrName).append(":[ ")
                                .append(format.format(facetFieldRpc.getStartDate())).append(" TO * ]))");
                    } else if (startDateSolrName != null) {
                        solrQuery.append(" AND (").append(startDateSolrName).append(":[ ").append(format.format(facetFieldRpc.getStartDate()))
                                .append(" TO ").append(format.format(facetFieldRpc.getEndDate())).append(" ]").append(")");
                    } else if (endDateSolrName != null) {
                        solrQuery.append(" AND (").append(endDateSolrName).append(":[ ").append(format.format(facetFieldRpc.getStartDate()))
                                .append(" TO ").append(format.format(facetFieldRpc.getEndDate())).append(" ]").append(")");
                    }
                }
            }
        }
        return solrQuery.toString();
    }


    /**
     * <h1>... This is method fill FacetFilterRpc object ...</h1>
     * <br/>
     * <h2>... Write developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date { 16:53 03/06/2011 } ...</h3>
     *
     * @param resp
     * @param facetFilter
     * @return FacetFilterRpc
     */
    public static FacetFilterRpc fillFacetFilterData(QueryResponse resp, FacetFilterRpc facetFilter) {
        return fillFacetFilterData(resp, facetFilter, null);
    }

    /**
     * <h1>... This is method fill FacetFilterRpc object ...</h1>
     * <br/>
     * <h2>... Write developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date { 16:55 03/06/2011 } ...</h3>
     *
     * @param resp
     * @param facetFilter
     * @return FacetFilterRpc
     */
    public static FacetFilterRpc fillFacetFilterData(QueryResponse resp, FacetFilterRpc facetFilter, String[] codeNameList) {
        Set<String> keySet = facetFilter.getFacetContentMap().keySet();
        if (codeNameList != null) {
            keySet = new HashSet<>(Arrays.asList(codeNameList));
        }
        for (String key : keySet) {
            if (facetFilter.getShowSolrFieldMap().containsKey(key)) {
                FacetSolrField solrFields = facetFilter.getShowSolrFieldMap().get(key);
                if (solrFields != null) {
                    String facetFiled = solrFields.getSolrFacetFieldName();
                    if (solrFields.isConditionItemId()) {
                        facetFilter.getFacetContentMap().get(key).setFacetItems(getFacetFieldSplitData(resp.getFacetField(facetFiled)));
                    } else {
                        facetFilter.getFacetContentMap().get(key).setFacetItems(getFacetFieldData(resp.getFacetField(facetFiled)));
                    }
                }
            }
        }
        return facetFilter;
    }

    private static SelectItem[] getFacetFieldData(FacetField facetField) {
        if (facetField != null && facetField.getValues() != null) {
            int num = 0;
            if (SolrEventRepresenter.FIELD_CALL_TYPE.equals(facetField.getName()) && ApplicationContextProvider.applicationContext != null) {
                WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");

                SelectItem[] facetItem = new SelectItem[facetField.getValueCount()];
                for (FacetField.Count count : facetField.getValues()) {
                    if ("MISSED".equals(count.getName())) {
                        facetItem[num] = new SelectItem(("_" + count.getName()).hashCode(), count.getName());
                        facetItem[num].setDescription(commonLocalizer.localize("missed", "Missed") + " ( <b>" + count.getCount() + "</b> )");
                    } else if ("INBOUND".equals(count.getName())) {
                        facetItem[num] = new SelectItem(("_" + count.getName()).hashCode(), count.getName());
                        facetItem[num].setDescription(commonLocalizer.localize("inbound", "Inbound") + " ( <b>" + count.getCount() + "</b> )");
                    } else if ("OUTBOUND".equals(count.getName())) {
                        facetItem[num] = new SelectItem(("_" + count.getName()).hashCode(), count.getName());
                        facetItem[num].setDescription(commonLocalizer.localize("outbound", "Outbound") + " ( <b>" + count.getCount() + "</b> )");
                    } else {
                        facetItem[num] = new SelectItem(("_" + count.getName()).hashCode(), count.getName());
                        facetItem[num].setDescription(facetItem[num].getName() + " ( <b>" + count.getCount() + "</b> )");
                    }
                    num++;
                }
                return ServerUtils.sortSelectItemByDesc(facetItem);
            } else {
                SelectItem[] facetItem = new SelectItem[facetField.getValueCount()];
                for (FacetField.Count count : facetField.getValues()) {
                    facetItem[num] = new SelectItem(("_" + count.getName()).hashCode(), count.getName());
                    facetItem[num].setDescription(facetItem[num].getName() + " ( <b>" + count.getCount() + "</b> )");
                    num++;
                }
                return ServerUtils.sortSelectItemByDesc(facetItem);
            }
        } else {
            return new SelectItem[0];
        }
    }

    private static SelectItem[] getFacetFieldSplitData(FacetField facetField) {
        if (facetField != null && facetField.getValues() != null) {
            int num = 0;
            SelectItem[] facetItem = new SelectItem[facetField.getValueCount()];
            for (FacetField.Count count : facetField.getValues()) {
                String[] data = count.getName().split(SPLIT);
                facetItem[num] = new SelectItem();
                facetItem[num].setId(Integer.valueOf(data[0]));
                facetItem[num].setName(ServerUtils.concatArray(data));
                facetItem[num].setDescription(facetItem[num].getName() + " ( <b>" + count.getCount() + "</b> )");
                num++;
            }
            return ServerUtils.sortSelectItemByDesc(facetItem);
        } else {
            return new SelectItem[0];
        }
    }

    /**
     * <h1>... This is method uses only saleinvoice facet filter ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {20:01 10/06/2011} ...</h3>
     *
     * @param facetFieldRpc
     * @param codeNameList
     * @return
     */
    public static String generateSaleInvoiceDuePaidAmountFacet(FacetFilterRpc facetFieldRpc, String... codeNameList) {
        StringBuilder solrQuery = new StringBuilder();
        if (facetFieldRpc != null) {
            Set<String> keySet = facetFieldRpc.getShowSolrFieldMap().keySet();
            if (codeNameList != null && codeNameList.length != 0) {
                keySet = new HashSet<>(Arrays.asList(codeNameList));
            }
            for (String codeName : keySet) {
                if (facetFieldRpc.getFacetContentMap().containsKey(codeName)) {
                    FacetContentRpc facetContent = facetFieldRpc.getFacetContentMap().get(codeName);
                    if (facetContent != null && facetContent.getFacetItems().length != 0) {
                        SelectItem[] items = facetContent.getFacetItems();
                        solrQuery.append(" AND (");
                        boolean appendOperator = false;
                        for (SelectItem item : items) {
                            if (appendOperator) {
                                solrQuery.append(" OR ");
                            } else {
                                appendOperator = true;
                            }
                            solrQuery.append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":").append(item.getName());
                        }
                        solrQuery.append(") ");
                    }
                }
            }
        }
        return solrQuery.toString();
    }

    public static String generateForPricesFacet(FacetFilterRpc facetFieldRpc, String... codeNameList) {
        StringBuilder solrQuery = new StringBuilder();
        if (facetFieldRpc != null) {
            Set<String> keySet = facetFieldRpc.getShowSolrFieldMap().keySet();
            if (codeNameList != null && codeNameList.length != 0) {
                keySet = new HashSet<>(Arrays.asList(codeNameList));
            }
            for (String codeName : keySet) {
                if (facetFieldRpc.getFacetContentMap().containsKey(codeName)) {
                    FacetContentRpc facetContent = facetFieldRpc.getFacetContentMap().get(codeName);
                    if (facetContent != null && facetContent.getFacetItems().length != 0) {
                        SelectItem[] items = facetContent.getFacetItems();
                        solrQuery.append(" AND (");
                        boolean appendOperator = false;
                        for (SelectItem item : items) {
                            if (appendOperator) {
                                solrQuery.append(" OR ");
                            } else {
                                appendOperator = true;
                            }
                            solrQuery.append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":").append(item.getName());
                        }
                        solrQuery.append(") ");
                    }
                }
            }
        }
        return solrQuery.toString();
    }

    /**
     * <h1>... This is method generate facet filter  solr query with 'N/A' ...</h1>
     * <br/>
     * <h2>... Write developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date { 20:06 10/06/2011 } ...</h3>
     *
     * @param facetFieldRpc
     * @param edsCompany
     * @param startDateSolrName
     * @param endDateSolrName
     * @return String solr query
     */
    @Deprecated
    public static String generatedFacetFilterSolrQueryWithNA(FacetFilterRpc facetFieldRpc, EdsCompany edsCompany, String startDateSolrName, String endDateSolrName, String... removeAmount) {
        StringBuilder solrQuery = new StringBuilder();
        CommonServiceLocal commonServiceLocal = StaticContextAccessor.getBean(CommonServiceLocal.class);
        List<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();
        if (facetFieldRpc != null && facetFieldRpc.getType() != null) {
            customFieldItems = commonServiceLocal.getCompanyCustomFields(facetFieldRpc.getType().getViewName());
        }
        for (CompanyCustomFieldItem item : customFieldItems) {
            HashMap<String, FacetContentRpc> map = facetFieldRpc.getFacetContentMap();
            if (Constants.UI_TYPE_LOOKUP.equals(item.getUiType()) && (CustomFieldLookUpTypeEnum.POSITION.equals(item.getLookUpTypeEnum()) || CustomFieldLookUpTypeEnum.DEPARTMENT.equals(item.getLookUpTypeEnum()))) {
                if (map != null && map.containsKey(item.getColumnCode())) {
                    SelectItem[] rpc = map.get(item.getColumnCode()).getFacetItems();
                    for (SelectItem selectItem : rpc) {
                        selectItem.setName(String.valueOf(selectItem.getId()));
                    }
                }
            }
        }
        if (facetFieldRpc != null) {
            Set<String> keySet = facetFieldRpc.getShowSolrFieldMap().keySet();
            if (facetFieldRpc.getSolrFieldMapCodeList() != null && facetFieldRpc.getSolrFieldMapCodeList().length != 0) {
                keySet = new HashSet<>(Arrays.asList(facetFieldRpc.getSolrFieldMapCodeList(removeAmount)));
            }
            for (String codeName : keySet) {
                boolean isMonthWithYear = false;
                String yearValue = "";
                if (codeName.equals("month") && facetFieldRpc.getFacetContentMap().get("month").getFacetItems().length > 0) {
                    if (facetFieldRpc.getFacetContentMap().get("month").getFacetItems()[0].getValueMap().get("name").split("-").length > 1) {
                        yearValue = facetFieldRpc.getFacetContentMap().get("month").getFacetItems()[0].getValueMap().get("name").split("-")[1];
                        isMonthWithYear = true;
                    }
                }
                if (facetFieldRpc.getFacetContentMap().containsKey(codeName)) {
                    FacetContentRpc facetContent = facetFieldRpc.getFacetContentMap().get(codeName);
                    if (facetContent != null && facetContent.getFacetItems().length != 0) {
                        SelectItem[] items = facetContent.getFacetItems();
                        solrQuery.append(" AND (");
                        boolean appendOperator = false;
                        boolean isConditionItemID = facetFieldRpc.getShowSolrFieldMap().get(codeName) != null && facetFieldRpc.getShowSolrFieldMap().get(codeName).isConditionItemId();
                        boolean isFirst = true;
                        StringBuffer nullQuery = null;
                        for (SelectItem item : items) {
                            if (appendOperator) {
                                solrQuery.append(isConditionItemID ? " " : " OR ");
                            } else {
                                appendOperator = true;
                            }
                            if (item.getId() != null && item.getId() == -1) {
                                if (!isConditionItemID) {
                                    solrQuery.append("(-").append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":[* TO *] AND *:*)");
                                } else {
                                    if (nullQuery == null) {
                                        nullQuery = new StringBuffer();
                                    }
                                    nullQuery.append(" (-").append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":[* TO *] AND *:*)");
                                }
                            } else if (facetFieldRpc.getShowSolrFieldMap().get(codeName).isConditionItemId()) {
                                if (isFirst) {
                                    solrQuery.append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":(");
                                }
                                isFirst = false;
                                if (codeName.contains("string_value") || codeName.contains("date_value") || codeName.contains("double_value")) {
                                    solrQuery.append("\"").append(item.getCategory() != null ? item.getCategory() : item.getName()).append("\"");
                                } else {
                                    boolean isWithID = facetFieldRpc.getShowSolrFieldMap().get(codeName) == null || facetFieldRpc.getShowSolrFieldMap().get(codeName).isWithID();
                                    if (isWithID) {
                                        if ("year".equals(codeName)) {
                                            solrQuery.append(item.getName());
                                        } else {
                                            solrQuery.append(item.getId());
                                        }
                                    } else {
                                        solrQuery.append("\"").append(item.getCategory() != null ? item.getCategory() : item.getName()).append("\"");
                                    }
                                }
                            } else {
                                isFirst = false;
                                solrQuery.append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":(\"").append(item.getCategory() != null ? item.getCategory() : item.getName()).append("\")");
                            }
                        }
                        if (isConditionItemID) {
                            if (!isFirst) {
                                solrQuery.append(")");
                                if (isMonthWithYear) {
                                    solrQuery.append(" AND YEAR:(" + yearValue + ")");
                                }
                            }
                            if (nullQuery != null) {
                                if (!isFirst) {
                                    solrQuery.append(" OR ");
                                }
                                solrQuery.append(nullQuery);
                            }
                        }
                        solrQuery.append(") ");
                    }
                }
            }

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            if (facetFieldRpc.getStartDate() != null && !ServerUtils.isNullOrEmpty(facetFieldRpc.getSelectedDateSolrCodeName())
                    && !"dueDate".equals(facetFieldRpc.getSelectedDateSolrCodeName())) {
                solrQuery.append(" AND (").append(facetFieldRpc.getSelectedDateSolrCodeName()).append(":[")
                        .append(format.format(facetFieldRpc.getStartDate())).append(" TO ").append(facetFieldRpc.getEndDate() != null ? format.format(facetFieldRpc.getEndDate()) : "*").append(" ])");
            } else {
                if (facetFieldRpc.getStartDate() != null || facetFieldRpc.getEndDate() != null) {
                    if (startDateSolrName != null && endDateSolrName != null) {
                        if (facetFieldRpc.getEndDate() != null) {
                            solrQuery.append(" AND (").append(startDateSolrName).append(":[ * TO ")
                                    .append(format.format(facetFieldRpc.getEndDate())).append(" ]) ");
                        }
                        if (facetFieldRpc.getStartDate() != null) {
                            solrQuery.append(" AND (").append(endDateSolrName).append(":[ ")
                                    .append(format.format(facetFieldRpc.getStartDate())).append(" TO * ])");
                        }
                    } else if (startDateSolrName != null) {
                        solrQuery.append(" AND (").append(startDateSolrName).append(":[ ").append(format.format(facetFieldRpc.getStartDate()))
                                .append(" TO ").append(format.format(facetFieldRpc.getEndDate())).append(" ]").append(")");
                    } else if (endDateSolrName != null) {
                        solrQuery.append(" AND (").append(endDateSolrName).append(":[ ").append(format.format(facetFieldRpc.getStartDate()))
                                .append(" TO ").append(format.format(facetFieldRpc.getEndDate())).append(" ]").append(")");
                    }
                }
            }
        }
        return solrQuery.toString();
    }

    public static String generatedFacetFilterSolrQueryWithNAForCF(FacetFilterRpc facetFieldRpc, EdsCompany edsCompany, ArrayList<CompanyCustomFieldItem> customFieldItems) {
        StringBuilder solrQuery = new StringBuilder();
        if (facetFieldRpc != null) {
            for (CompanyCustomFieldItem item : customFieldItems) {
                HashMap<String, FacetContentRpc> map = facetFieldRpc.getFacetContentMap();
                if (Constants.UI_TYPE_LOOKUP.equals(item.getUiType()) && (CustomFieldLookUpTypeEnum.POSITION.equals(item.getLookUpTypeEnum()) || CustomFieldLookUpTypeEnum.DEPARTMENT.equals(item.getLookUpTypeEnum()))) {
                    if (map != null && map.containsKey(item.getColumnCode())) {
                        SelectItem[] rpc = map.get(item.getColumnCode()).getFacetItems();
                        for (SelectItem selectItem : rpc) {
                            selectItem.setName(String.valueOf(selectItem.getId()));
                        }
                    }
                }
            }

            Set<String> keySet = facetFieldRpc.getShowSolrFieldMap().keySet();
            if (facetFieldRpc.getSolrFieldMapCodeList() != null && facetFieldRpc.getSolrFieldMapCodeList().length != 0) {
                keySet = new HashSet<>(Arrays.asList(facetFieldRpc.getSolrFieldMapCodeList(null)));
            }
            for (String codeName : keySet) {
                boolean isMonthWithYear = false;
                String yearValue = "";
                if (codeName.equals("month") && facetFieldRpc.getFacetContentMap().get("month").getFacetItems().length > 0) {
                    if (facetFieldRpc.getFacetContentMap().get("month").getFacetItems()[0].getValueMap().get("name").split("-").length > 1) {
                        yearValue = facetFieldRpc.getFacetContentMap().get("month").getFacetItems()[0].getValueMap().get("name").split("-")[1];
                        isMonthWithYear = true;
                    }
                }
                if (facetFieldRpc.getFacetContentMap().containsKey(codeName)) {
                    FacetContentRpc facetContent = facetFieldRpc.getFacetContentMap().get(codeName);
                    if (facetContent != null && facetContent.getFacetItems().length != 0) {
                        SelectItem[] items = facetContent.getFacetItems();
                        solrQuery.append(" AND (");
                        boolean appendOperator = false;
                        boolean isConditionItemID = facetFieldRpc.getShowSolrFieldMap().get(codeName) != null && facetFieldRpc.getShowSolrFieldMap().get(codeName).isConditionItemId();
                        boolean isFirst = true;
                        StringBuffer nullQuery = null;
                        for (SelectItem item : items) {
                            if (appendOperator) {
                                solrQuery.append(isConditionItemID ? " " : " OR ");
                            } else {
                                appendOperator = true;
                            }
                            if (item.getId() != null && item.getId() == -1) {
                                if (!isConditionItemID) {
                                    solrQuery.append("(-").append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":[* TO *] AND *:*)");
                                } else {
                                    if (nullQuery == null) {
                                        nullQuery = new StringBuffer();
                                    }
                                    nullQuery.append(" (-").append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":[* TO *] AND *:*)");
                                }
                            } else if (facetFieldRpc.getShowSolrFieldMap().get(codeName).isConditionItemId()) {
                                if (isFirst) {
                                    solrQuery.append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":(");
                                }
                                isFirst = false;
                                if (codeName.contains("string_value") || codeName.contains("date_value") || codeName.contains("double_value")) {
                                    solrQuery.append("\"").append(item.getCategory() != null ? item.getCategory() : item.getName()).append("\"");
                                } else {
                                    boolean isWithID = facetFieldRpc.getShowSolrFieldMap().get(codeName) == null || facetFieldRpc.getShowSolrFieldMap().get(codeName).isWithID();
                                    if (isWithID) {
                                        solrQuery.append(item.getId());
                                    } else {
                                        solrQuery.append("\"").append(item.getCategory() != null ? item.getCategory() : item.getName()).append("\"");
                                    }
                                }
                            } else {
                                isFirst = false;
                                solrQuery.append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":(\"").append(item.getCategory() != null ? item.getCategory() : item.getName()).append("\")");
                            }
                        }
                        if (isConditionItemID) {
                            if (!isFirst) {
                                solrQuery.append(")");
                                if (isMonthWithYear) {
                                    solrQuery.append(" AND YEAR:(" + yearValue + ")");
                                }
                            }
                            if (nullQuery != null) {
                                if (!isFirst) {
                                    solrQuery.append(" OR ");
                                }
                                solrQuery.append(nullQuery);
                            }
                        }
                        solrQuery.append(") ");
                    }
                }
            }

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            if (facetFieldRpc.getStartDate() != null && !ServerUtils.isNullOrEmpty(facetFieldRpc.getSelectedDateSolrCodeName())
                    && !"DUE_DATE".equals(facetFieldRpc.getSelectedDateSolrCodeName())) {
                solrQuery.append(" AND (").append(facetFieldRpc.getSelectedDateSolrCodeName()).append(":[")
                        .append(format.format(facetFieldRpc.getStartDate())).append(" TO ").append(facetFieldRpc.getEndDate() != null ? format.format(facetFieldRpc.getEndDate()) : "*").append(" ])");
            }
        }
        return solrQuery.toString();
    }

    /**
     * <h1>... This is method fill FacetFilterRpc object With 'N/A' ...</h1>
     * <br/>
     * <h2>... Write developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date { 20:56 10/06/2011 } ...</h3>
     *
     * @param resp
     * @param facetFilter
     * @return FacetFilterRpc
     */
    @Deprecated
    public static FacetFilterRpc fillFacetFilterDataWithNA(QueryResponse resp, FacetFilterRpc facetFilter, String... codeNameList) {
        Set<String> keySet = facetFilter.getFacetContentMap().keySet();
        CommonServiceLocal serviceLocal = StaticContextAccessor.getBean(CommonServiceLocal.class);
        if (codeNameList != null && codeNameList.length != 0) {
            keySet = new HashSet<>(Arrays.asList(codeNameList));
        }
        for (String key : keySet) {
            if (facetFilter.getShowSolrFieldMap().containsKey(key)) {
                FacetSolrField solrFields = facetFilter.getShowSolrFieldMap().get(key);
                if (solrFields != null && facetFilter.getFacetContentMap().containsKey(key)) {
                    facetFilter.getFacetContentMap().get(key).setFacetItems(getFacetFieldDataWithNA(resp, solrFields));
                }
            }
        }
        List<CompanyCustomFieldItem> customFieldItems = facetFilter.getType() != null ? serviceLocal.getCompanyCustomFields(facetFilter.getType().getViewName()) : new ArrayList<>();
        serviceLocal.getFacetFilterWithLocale(facetFilter.getFacetContentMap(), customFieldItems);
        return facetFilter;
    }

    /**
     * <h1>... This is method fill FacetFilterRpc object With 'N/A' ...</h1>
     * <br/>
     * <h2>... Write developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date { 20:56 10/06/2011 } ...</h3>
     *
     * @param resp
     * @param facetFilter
     * @return FacetFilterRpc
     */
    public static FacetFilterRpc fillFacetFilterDataWithNA(QueryResponse resp, FacetFilterRpc facetFilter, ListingFilterParameter fp, String... codeNameList) {
        String entityCategoryName = null;

        if (fp != null && fp.getForm() != null && fp.getForm().contains("FORM")) {
            String formId = fp.getForm().replaceFirst("_FORM", "");
            entityCategoryName = Constants.CUSTOM_VIEW + formId;
        }
        Set<String> keySet = facetFilter.getFacetContentMap().keySet();
        if (codeNameList != null && codeNameList.length != 0) {
            keySet = new HashSet<>(Arrays.asList(codeNameList));
        }
        for (String key : keySet) {
            if (facetFilter.getShowSolrFieldMap().containsKey(key)) {
                FacetSolrField solrFields = facetFilter.getShowSolrFieldMap().get(key);
                if (solrFields != null && facetFilter.getFacetContentMap().containsKey(key)) {
                    if (solrFields.getSolrFacetFieldName().contains("STRING_VALUE")) {
                        CompanyCustomFieldsManager companyCFSettingsManager = (CompanyCustomFieldsManager) ApplicationContextProvider.applicationContext.getBean("companyCFSettingsManager");
                        EdsCompanyCustomFieldsSettings edsCompanyCustomFieldsSettings = companyCFSettingsManager.getCompanyCustomField(ViewName.CustomFormItems.name(), entityCategoryName, solrFields.getSolrFacetFieldName().toLowerCase());
                        if (edsCompanyCustomFieldsSettings != null && Constants.TYPE_ENTITY_LOOKUP.equals(edsCompanyCustomFieldsSettings.getUiType())) {
                            SelectItem[] selectItems = companyCFSettingsManager.getCustomFieldDataByQuery(SecurityContext.getCompanyID(), edsCompanyCustomFieldsSettings.getQuery());
                            facetFilter.getFacetContentMap().get(key).setFacetItems(getFacetFieldDataWithNA(resp, solrFields, selectItems));
                        } else {
                            facetFilter.getFacetContentMap().get(key).setFacetItems(getFacetFieldDataWithNA(resp, solrFields));
                        }
                    } else {
                        facetFilter.getFacetContentMap().get(key).setFacetItems(getFacetFieldDataWithNA(resp, solrFields));
                    }
                }
            }
        }
        return facetFilter;
    }

    public static FacetFilterRpc fillNewSolr86FacetFilterDataWithNA(FacetPage facetPage, FacetFilterRpc facetFilter, String... codeNameList) {
        Set<String> keySet = facetFilter.getFacetContentMap().keySet();
        CommonServiceLocal serviceLocal = StaticContextAccessor.getBean(CommonServiceLocal.class);
        if (codeNameList != null && codeNameList.length != 0) {
            keySet = new HashSet<>(Arrays.asList(codeNameList));
        }
        for (String key : keySet) {
            if (facetFilter.getShowSolrFieldMap().containsKey(key)) {
                FacetSolrField solrFields = facetFilter.getShowSolrFieldMap().get(key);
                if (solrFields != null && facetFilter.getFacetContentMap().containsKey(key)) {
                    facetFilter.getFacetContentMap().get(key).setFacetItems(getNewSolr86FacetFieldDataWithNA(facetPage, solrFields));
                }
            }
        }
        List<CompanyCustomFieldItem> customFieldItems = facetFilter.getType() != null ? serviceLocal.getCompanyCustomFields(facetFilter.getType().getViewName()) : new ArrayList<>();
        serviceLocal.getFacetFilterWithLocale(facetFilter.getFacetContentMap(), customFieldItems);
        return facetFilter;
    }

    private static SelectItem[] getNewSolr86FacetFieldDataWithNA(FacetPage facetPage, FacetSolrField solrFields) {
        Page<SimpleFacetFieldEntry> facetFields = facetPage.getFacetResultPage(solrFields.getSolrFacetFieldName());
        boolean isCriteriaItemId = solrFields.isConditionItemId();
        LocalizationType localizationType = solrFields.getLocalizationType();
        if (!facetFields.isEmpty()) {
            SelectItem naItem = null;
            List<SelectItem> facetItem = new ArrayList<>();
            for (SimpleFacetFieldEntry facetFieldEntry : facetFields.getContent()) {
                if (facetFieldEntry.getValue() == null) {
                    if (facetFieldEntry.getValueCount() > 0) {
                        naItem = new SelectItem(-1, "N/A");
                        naItem.setDescription("N/A ( <b>" + facetFieldEntry.getValueCount() + "</b> )");
                        naItem.setTotalCount(facetFieldEntry.getValueCount());
                    }
                } else {
                    SelectItem sItem = null;
                    String name = null;
                    if (isCriteriaItemId && facetFieldEntry.getValue().contains(SPLIT)) {
                        String[] data = facetFieldEntry.getValue().split(SPLIT);
                        if (data.length > 2 && ServerUtils.convertMonthToInterfaceLanguage(data[1]) != null) {
                            data[1] = ServerUtils.convertMonthToInterfaceLanguage(data[1]).concat("-" + data[2]);
                        }
                        try {
                            if (SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE_NAME.equals(facetFieldEntry.getKey().getName()) ||
                                    SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID_CODE.equals(facetFieldEntry.getKey().getName()) ||
                                    SolrContactRepresenter.FIELD_LEAD_SOURCE_ID_CODE_NAME.equals(facetFieldEntry.getKey().getName())) {
                                if (ApplicationContextProvider.applicationContext != null && data.length > 1) {
                                    ReferenceManager referenceManager = (ReferenceManager) ApplicationContextProvider.applicationContext.getBean("referenceManager");
                                    WfmResourceBundleMessageSource referenceWfmMessageSource = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("referenceWfmMessageSource");
                                    EdsReference statusFromDB = referenceManager.get(Integer.valueOf(data[0]));
                                    String localizeName = null;
                                    if (statusFromDB != null) {
                                        localizeName = statusFromDB.getName();
                                        localizeName = referenceWfmMessageSource.localize(data[1], localizeName);
                                        sItem = new SelectItem(statusFromDB.getObjectID(), localizeName);
                                    } else {
                                        localizeName = referenceWfmMessageSource.localize(data[1], data[1]);
                                        sItem = new SelectItem(Integer.valueOf(data[0]), localizeName);
                                    }
                                } /*else {
                                    sItem = new SelectItem(Integer.valueOf(data[0]), name);
                                }*/

                            } else {

                                name = localize(data, localizationType);
                                sItem = new SelectItem(Integer.valueOf(data[0]), name);
                            }
                        } catch (Exception e) {
//                            e.printStackTrace();
                            sItem = new SelectItem(("_" + facetFieldEntry.getValue()).hashCode(), facetFieldEntry.getValue());
                        }
                        //Below will work for lead statuses, we must return status colors and sortorder
                        /*
                        we cant save below data into solr
                        if(SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE_NAME.equals(facetField.getName()) && data.length==7) {
                            try {
                                sItem.setOrderId(Integer.valueOf(data[3]));
                                sItem.setColorId(Integer.valueOf(data[4]));
                                sItem.setColorName(data[5]);
                                sItem.setColorHex(data[6]);
                            } catch(Exception e) {
                                //We will ignore exception
                            }
                        }*/
                    } else {
                        //We are appending underscore because if value is empty then hashcode become 0
                        // but we cant set it to 0 because from API it equal to -1
                        sItem = new SelectItem(("_" + facetFieldEntry.getValue()).hashCode(), facetFieldEntry.getValue());
                    }

                    WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
                    if (SolrSaleInvoiceRepresenter.FIELD_GDN_IS_SALES_ORDER.equals(facetFieldEntry.getKey().getName()) && facetFieldEntry.getValue() != null && ApplicationContextProvider.applicationContext != null) {
                        PropertManager propertManager = (PropertManager) ApplicationContextProvider.applicationContext.getBean("PropertyManager");
                        EdsProperty propertySO = propertManager.findByCode(Constants.SALE_ORDER_CODE);
                        EdsProperty propertySQ = propertManager.findByCode(Constants.SALE_QUOTE);
                        String nameField = "";
                        nameField = facetFieldEntry.getValue().equals("true") ?
                                propertySO != null && propertySO.getSingular() != null ? propertySO.getSingular() : "Sales Order"
                                : facetFieldEntry.getValue().equals("false") ?
                                propertySQ != null && propertySQ.getSingular() != null ? propertySQ.getSingular() : "Sales Quote"
                                : facetFieldEntry.getValue();

                        sItem.setDescription(nameField + " ( <b>" + facetFieldEntry.getValueCount() + "</b> )");
                    } else if (SolrOpportunityRepresenter.HAS_ATTACHMENT.equals(facetFieldEntry.getKey().getName()) && facetFieldEntry.getValue() != null && ApplicationContextProvider.applicationContext != null) {
                        String nameField = "";
                        nameField = commonLocalizer.localize(facetFieldEntry.getValue().equals("true") ? "yes" : "no", facetFieldEntry.getValue().equals("true") ? "Yes" : "No");
                        sItem.setDescription(nameField + " ( <b>" + facetFieldEntry.getValueCount() + "</b> )");
                    } else if ((SolrProductServiceRepresenter.FIELD_PRODUCT_ACTIVE.equals(facetFieldEntry.getKey().getName())
                            || SolrChartOfAccountRepresenter.FIELD_ACTIVE.equals(facetFieldEntry.getKey().getName()))
                            && facetFieldEntry.getValue() != null) {
                        String nameField = "";
                        nameField = commonLocalizer.localize(facetFieldEntry.getValue().equals("true") ? "yes" : "no", facetFieldEntry.getValue().equals("true") ? "Active" : "Inactive");
                        sItem.setDescription(nameField + " ( <b>" + facetFieldEntry.getValueCount() + "</b> )");
                    } else if (SolrLeaveRequestConst.FIELD_REASON_ID_NAME.equals(facetFieldEntry.getKey().getName()) && ApplicationContextProvider.applicationContext != null && facetFieldEntry.getValue() != null) {
                        LeaveReasonManager reasonManager = (LeaveReasonManager) ApplicationContextProvider.applicationContext.getBean("leaveReasonManager");
                        String[] data = facetFieldEntry.getValue().split(SPLIT);
                        EdsLeaveReason byCode = reasonManager.get(Integer.valueOf(data[0]));
                        if (byCode != null) {
                            EdsReferenceLocale locale = byCode.getLocale();
                            if (locale != null) {
                                String lang = ServerUtils.getUserLocale().getLanguage();
                                if (StringUtils.isNotBlank(locale.getLocaleByCode(lang))) {
                                    facetFieldEntry.setField(locale.getLocaleByCode(lang));
                                    sItem.setDescription(facetFieldEntry.getValue() + " ( <b>" + facetFieldEntry.getValueCount() + "</b> )");
                                }
                            } else {
                                sItem.setDescription(byCode.getName() + " ( <b>" + facetFieldEntry.getValueCount() + "</b> )");
                            }
                        }
                    } else if (SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE.equals(facetFieldEntry.getKey().getName()) && facetFieldEntry.getValue() != null) {
                        if ("false".equalsIgnoreCase(facetFieldEntry.getValue())) {
                            sItem.setDescription("Employee Expenses" + " ( <b>" + facetFieldEntry.getValueCount() + "</b> )");
                        } else if ("true".equalsIgnoreCase(facetFieldEntry.getValue())) {
                            sItem.setDescription("Company Expenses" + " ( <b>" + facetFieldEntry.getValueCount() + "</b> )");

                        }

                    } else if (SolrCrmAccountRepresenter.FIELD_BLOCKED.equals(facetFieldEntry.getKey().getName()) && facetFieldEntry.getValue() != null) {

                        String nameField = commonLocalizer.localize(facetFieldEntry.getValue().equals("true") ? "inactive" : "active", facetFieldEntry.getValue().equals("true") ? "Inactive" : "Active");
                        sItem.setDescription(nameField + " ( <b>" + facetFieldEntry.getValueCount() + "</b> )");
                    } else {
                        sItem.setDescription(sItem.getName() + " ( <b>" + facetFieldEntry.getValueCount() + "</b> )");
                    }
                    sItem.setTotalCount(facetFieldEntry.getValueCount());

                    facetItem.add(sItem);
                }
            }
            SelectItem[] items = facetItem.toArray(new SelectItem[]{});
            ServerUtils.sortSelectItemByDesc(items);
            if (naItem != null) {
                SelectItem[] itemWithNa = new SelectItem[items.length + 1];
                itemWithNa[0] = naItem;
                System.arraycopy(items, 0, itemWithNa, 1, itemWithNa.length - 1);
                return itemWithNa;
            }
            return items;
        } else {
            return new SelectItem[0];
        }
    }

    /**
     * <h1>... This is method read in solr field value and build SelectItem rpc object with 'N/A' ...</h1>
     * <br/>
     * <h2>... Write developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date { 20:56 10/06/2011 } ...</h3>
     *
     * @return
     */
    @Deprecated
    private static SelectItem[] getFacetFieldDataWithNA(QueryResponse resp, FacetSolrField solrFields) {
        FacetField facetField = resp.getFacetField(solrFields.getSolrFacetFieldName());
        boolean isCriteriaItemId = solrFields.isConditionItemId();
        LocalizationType localizationType = solrFields.getLocalizationType();
        if (facetField != null && facetField.getValues() != null) {
            SelectItem naItem = null;
            List<SelectItem> facetItem = new ArrayList<>();
            for (FacetField.Count count : facetField.getValues()) {
                if (count.getName() == null) {
                    if (count.getCount() > 0) {
                        naItem = new SelectItem(-1, "N/A");
                        naItem.setDescription("N/A ( <b>" + count.getCount() + "</b> )");
                        naItem.setTotalCount(count.getCount());
                    }
                } else {
                    SelectItem sItem = null;
                    String name = null;
                    if (isCriteriaItemId && count.getName().contains(SPLIT)) {
                        String[] data = count.getName().split(SPLIT);
                        if (data.length > 1 && ServerUtils.convertMonthToInterfaceLanguage(data[1]) != null) {
                            data[1] = ServerUtils.convertMonthToInterfaceLanguage(data[1]).concat("-" + data[2]);
                        }
                        try {
                            if (SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE_NAME.equals(facetField.getName()) ||
                                    SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID_CODE.equals(facetField.getName()) ||
                                    SolrTaskRepresenter.FIELD_TASK_STATUS_ID_CODE_NAME.equals(facetField.getName()) ||
                                    SolrContactRepresenter.FIELD_LEAD_SOURCE_ID_CODE_NAME.equals(facetField.getName())) {
                                if (ApplicationContextProvider.applicationContext != null && data.length > 1) {
                                    ReferenceManager referenceManager = (ReferenceManager) ApplicationContextProvider.applicationContext.getBean("referenceManager");
                                    WfmResourceBundleMessageSource referenceWfmMessageSource = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("referenceWfmMessageSource");
                                    EdsReference statusFromDB = referenceManager.get(Integer.valueOf(data[0]));
                                    String localizeName = null;
                                    if (statusFromDB != null) {
                                        Locale userLocale = ServerSecurityContext.getInstance().getUserLocale();
                                        if (statusFromDB.getLocale() != null && statusFromDB.getLocale().getLocaleByCode(userLocale.getLanguage()) != null) {
                                            localizeName = statusFromDB.getLocale().getLocaleByCode(userLocale.getLanguage());
                                        } else {
                                            localizeName = !statusFromDB.isChanged() ? referenceWfmMessageSource.localize(data[1], statusFromDB.getName()) : statusFromDB.getName();
                                        }
                                        sItem = new SelectItem(statusFromDB.getObjectID(), localizeName);
                                    } else {
                                        localizeName = referenceWfmMessageSource.localize(data[1], data[1]);
                                        sItem = new SelectItem(Integer.valueOf(data[0]), localizeName);
                                    }
                                }
                            } else {

                                name = localize(data, localizationType);
                                sItem = new SelectItem(Integer.valueOf(data[0]), name);
                            }
                        } catch (Exception e) {
//                            e.printStackTrace();
                            sItem = new SelectItem(("_" + count.getName()).hashCode(), count.getName());
                        }
                        //Below will work for lead statuses, we must return status colors and sortorder
                        /*
                        we cant save below data into solr
                        if(SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE_NAME.equals(facetField.getName()) && data.length==7) {
                            try {
                                sItem.setOrderId(Integer.valueOf(data[3]));
                                sItem.setColorId(Integer.valueOf(data[4]));
                                sItem.setColorName(data[5]);
                                sItem.setColorHex(data[6]);
                            } catch(Exception e) {
                                //We will ignore exception
                            }
                        }*/
                    } else {
                        //We are appending underscore because if value is empty then hashcode become 0
                        // but we cant set it to 0 because from API it equal to -1
                        sItem = new SelectItem(("_" + count.getName()).hashCode(), count.getName());
                    }

                    if (SolrSaleInvoiceRepresenter.FIELD_GDN_IS_SALES_ORDER.equals(facetField.getName()) && count.getName() != null && ApplicationContextProvider.applicationContext != null) {
                        PropertManager propertManager = (PropertManager) ApplicationContextProvider.applicationContext.getBean("PropertyManager");
                        EdsProperty propertySO = propertManager.findByCode(Constants.SALE_ORDER_CODE);
                        EdsProperty propertySQ = propertManager.findByCode(Constants.SALE_QUOTE);
                        String nameField = "";
                        nameField = count.getName().equals("true") ?
                                propertySO != null && propertySO.getSingular() != null ? propertySO.getSingular() : "Sales Order"
                                : count.getName().equals("false") ?
                                propertySQ != null && propertySQ.getSingular() != null ? propertySQ.getSingular() : "Sales Quote"
                                : count.getName();

                        sItem.setDescription(nameField + " ( <b>" + count.getCount() + "</b> )");
                    } else if (SolrOpportunityRepresenter.HAS_ATTACHMENT.equals(facetField.getName()) && count.getName() != null && ApplicationContextProvider.applicationContext != null) {
                        WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
                        String nameField = "";
                        nameField = commonLocalizer.localize(count.getName().equals("true") ? "yes" : "no", count.getName().equals("true") ? "Yes" : "No");

                        sItem.setDescription(nameField + " ( <b>" + count.getCount() + "</b> )");
                    } else if (SolrProductServiceRepresenter.FIELD_PRODUCT_ACTIVE.equals(facetField.getName())
                            || SolrChartOfAccountRepresenter.FIELD_ACTIVE.equals(facetField.getName())) {
                        WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
                        String nameField = "";
                        nameField = commonLocalizer.localize(count.getName().equals("true") ? "yes" : "no", count.getName().equals("true") ? "Active" : "Inactive");

                        sItem.setDescription(nameField + " ( <b>" + count.getCount() + "</b> )");
                    } else if (SolrLeaveRequestConst.FIELD_REASON_ID_NAME.equals(facetField.getName()) && ApplicationContextProvider.applicationContext != null) {
                        LeaveReasonManager reasonManager = (LeaveReasonManager) ApplicationContextProvider.applicationContext.getBean("leaveReasonManager");
                        String[] data = count.getName().split(SPLIT);
                        EdsLeaveReason byCode = reasonManager.get(Integer.valueOf(data[0]));
                        if (byCode != null) {
                            EdsReferenceLocale locale = byCode.getLocale();
                            if (locale != null) {
                                String lang = ServerUtils.getUserLocale().getLanguage();
                                if (StringUtils.isNotBlank(locale.getLocaleByCode(lang))) {
                                    count.setName(locale.getLocaleByCode(lang));
                                    sItem.setDescription(count.getName() + " ( <b>" + count.getCount() + "</b> )");
                                }
                            } else {
                                sItem.setDescription(byCode.getName() + " ( <b>" + count.getCount() + "</b> )");
                            }
                        }
                    } else if (SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE.equals(facetField.getName())) {
                        if ("false".equalsIgnoreCase(count.getName())) {
                            sItem.setDescription("Employee Expenses" + " ( <b>" + count.getCount() + "</b> )");
                        } else if ("true".equalsIgnoreCase(count.getName())) {
                            sItem.setDescription("Company Expenses" + " ( <b>" + count.getCount() + "</b> )");

                        }

                    } else if (SolrCrmAccountRepresenter.FIELD_BLOCKED.equals(facetField.getName())) {

                        WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");

                        String nameField = commonLocalizer.localize(count.getName().equals("true") ? "inactive" : "active", count.getName().equals("true") ? "Inactive" : "Active");
                        sItem.setDescription(nameField + " ( <b>" + count.getCount() + "</b> )");
                    } else {
                        sItem.setDescription(sItem.getName() + " ( <b>" + count.getCount() + "</b> )");
                    }
                    sItem.setTotalCount(count.getCount());

                    facetItem.add(sItem);
                }
            }
            SelectItem[] items = facetItem.toArray(new SelectItem[]{});
            ServerUtils.sortSelectItemByDesc(items);
            if (naItem != null) {
                SelectItem[] itemWithNa = new SelectItem[items.length + 1];
                itemWithNa[0] = naItem;
                System.arraycopy(items, 0, itemWithNa, 1, itemWithNa.length - 1);
                return itemWithNa;
            }
            return items;
        } else {
            return new SelectItem[0];
        }
    }

    private static SelectItem[] getFacetFieldDataWithNA(QueryResponse resp, FacetSolrField solrFields, SelectItem[] selectItems) {
        FacetField facetField = resp.getFacetField(solrFields.getSolrFacetFieldName());

        if (facetField != null && facetField.getValues() != null) {
            SelectItem naItem = null;
            List<SelectItem> facetItem = new ArrayList<>();
            for (FacetField.Count count : facetField.getValues()) {
                if (count.getName() == null) {
                    if (count.getCount() > 0) {
                        naItem = new SelectItem(-1, "N/A");
                        naItem.setDescription("N/A ( <b>" + count.getCount() + "</b> )");
                        naItem.setTotalCount(count.getCount());
                    }
                } else {
                    SelectItem sItem = null;
                    String name = null;

                    if (selectItems != null) {
                        for (SelectItem selectItem : selectItems) {
                            if (count.getName().equals(String.valueOf(selectItem.getId()))) {
                                name = selectItem.getName();
                                break;
                            }
                        }
                    }
                    sItem = new SelectItem(("_" + name).hashCode(), name);
                    sItem.setDescription(sItem.getName() + " ( <b>" + count.getCount() + "</b> )");
                    sItem.setTotalCount(count.getCount());
                    sItem.setCategory(count.getName());

                    facetItem.add(sItem);
                }
            }
            SelectItem[] items = facetItem.toArray(new SelectItem[]{});
            ServerUtils.sortSelectItemByDesc(items);
            if (naItem != null) {
                SelectItem[] itemWithNa = new SelectItem[items.length + 1];
                itemWithNa[0] = naItem;
                System.arraycopy(items, 0, itemWithNa, 1, itemWithNa.length - 1);
                return itemWithNa;
            }
            return items;
        } else {
            return new SelectItem[0];
        }
    }

    private static String localize(String[] codes, LocalizationType type) {
        String code = codes[1];
        String name = codes.length > 2 ? codes[2] : null;
        if (ApplicationContextProvider.applicationContext != null) {
            if (type == LocalizationType.REFERENCE || type == null) {
                WfmResourceBundleMessageSource referenceWfmMessageSource = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("referenceWfmMessageSource");
                if (type == null) {
                    return referenceWfmMessageSource.localize(code.replace(" ", "_"), code);
                }
                return referenceWfmMessageSource.localize(code, name != null ? name : code);
            }
            if (type == LocalizationType.COUNTRY) {
                WfmResourceBundleMessageSource countryLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("countryLocalizer");
                return countryLocalizer.localize(code, name != null ? name : code);
            }
        }
        return code;
    }
}
