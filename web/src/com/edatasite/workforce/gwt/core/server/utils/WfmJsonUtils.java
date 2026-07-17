package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetSettingRpc;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnTool;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is class working with JSON data.
 * Convert java objects to JSON data or
 * convert JSON data to java class.
 * <p/>
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 21-Jun-2011
 * Time: 21:06:22
 */
public class WfmJsonUtils {

    private static Logger log = LoggerFactory.getLogger(WfmJsonUtils.class);

    /**
     * <h1>... This is method List Panel Tools Json Data convert to ListPanelTools java object ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {21:45 31/05/2011} ...</h3>
     *
     * @param json
     * @return
     */
    public static ListPanelToolRpc jsonDataConvertToListPanelToolsRpc(String json) {
        ListPanelToolRpc listPanelTools = new ListPanelToolRpc();
        try {
            JSONObject parentJSONObject = (JSONObject) new JSONParser().parse(json);
            JSONObject innerJSONObject = (JSONObject) parentJSONObject.get("listPanelTools");
            Long pageSize = (Long) innerJSONObject.get("pageSize");
            boolean showPopup = parentJSONObject.get("showPopup") != null && "true".equalsIgnoreCase(parentJSONObject.get("showPopup").toString());
            JSONObject columnsToolObject = (JSONObject) innerJSONObject.get("colunmsTool");
            JSONObject columnCodeNameObject = (JSONObject) innerJSONObject.get("columnCodeName");
            String order = (String) innerJSONObject.get("order");
            ArrayList<String> columnCodeName = new ArrayList<>();
            LinkedHashMap<String, ColumnTool> columnToolMap = new LinkedHashMap<>();
            for (int i = 0; i < columnCodeNameObject.size(); i++) {
                String codeName = (String) columnCodeNameObject.get(String.valueOf(i));
                columnCodeName.add(codeName);
                JSONObject columnToolObject = (JSONObject) columnsToolObject.get(codeName);
                ColumnTool columnTool = new ColumnTool();
                columnTool.setColumnWidth(Integer.parseInt(columnToolObject.get("cw").toString()));
                JSONObject colors = (JSONObject) columnToolObject.get("colors");
                if (colors != null && colors.size() > 0) {
                    for (Object entry : colors.values()) {
                        JSONObject colorJson = (JSONObject) entry;
                        if (colorJson != null && colorJson.get("w") != null && colorJson.get("c") != null) {
                            columnTool.addColor(new ColumnColor(colorJson.get("w").toString(), colorJson.get("t").toString(), colorJson.get("c").toString()));
                        }
                    }
                }
                columnToolMap.put(codeName, columnTool);
            }
            listPanelTools.setPageSize(pageSize.intValue());
            listPanelTools.setColumnCodeName(columnCodeName);
            listPanelTools.setColunmsTool(columnToolMap);
            listPanelTools.setShowPopup(showPopup);
            listPanelTools.setSortByType(order);
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return listPanelTools;
    }

    /**
     * <h1>... This is method convert ListPanelTools java object to JSON Data ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {21:45 31/05/2011} ...</h3>
     *
     * @param listPanelTools
     * @return
     */
    public static String listPanelToolsConvertToJsonData(ListPanelToolRpc listPanelTools) {
        JSONObject parentJSON = new JSONObject();
        JSONObject innerJSON = new JSONObject();
        JSONObject codeNameJSON = new JSONObject();
        JSONObject columnsToolMapJSON = new JSONObject();
        parentJSON.put("showPopup", listPanelTools.isShowPopup());
        parentJSON.put("listPanelTools", innerJSON);
        innerJSON.put("pageSize", listPanelTools.getPageSize());
        innerJSON.put("colunmsTool", columnsToolMapJSON);
        innerJSON.put("columnCodeName", codeNameJSON);
        innerJSON.put("order", listPanelTools.getSortByType());
        int index = 0;
        for (int i = 0; i < listPanelTools.getColumnCodeName().size(); i++) {
            String codeName = listPanelTools.getColumnCodeName().get(i);
            codeNameJSON.put(index++, codeName);
            JSONObject columnToolJSON = new JSONObject();
            columnsToolMapJSON.put(codeName, columnToolJSON);
            columnToolJSON.put("cw", listPanelTools.getColunmsTool().get(codeName).getColumnWidth());
            if (!listPanelTools.getColunmsTool().get(codeName).getColors().isEmpty()) {
                JSONObject colorsJson = new JSONObject();
                for (Map.Entry<String, ColumnColor> colorEntry : listPanelTools.getColunmsTool().get(codeName).getColors().entrySet()) {
                    ColumnColor columnColor = colorEntry.getValue();
                    JSONObject colorJson = new JSONObject();
                    colorJson.put("c", columnColor.getColor());
                    colorJson.put("t", columnColor.getTarget());
                    colorJson.put("w", columnColor.getCondition());
                    colorsJson.put("" + colorsJson.size(), colorJson);
                }
                columnToolJSON.put("colors", colorsJson);
            }
        }
        return parentJSON.toJSONString();
    }

    /**
     * <h1>... This  is method conavert java List<String> to JSON data ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {15:10 31/05/2011} ...</h3>
     *
     * @param fieldCodeList
     * @return
     */
    public static String collectionListConvertToJsonData(List<String> fieldCodeList) {
        JSONObject parentJSON = new JSONObject();
        JSONObject fieldCodeJSON = new JSONObject();
        parentJSON.put("fieldsCodeName", fieldCodeJSON);
        int index = 0;
        for (String fieldCode : fieldCodeList) {
            fieldCodeJSON.put(index++, fieldCode);
        }
        return parentJSON.toJSONString();
    }

    /**
     * <h1>... This is method JSON List<String> data convert to java List<String> object ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {15:15 31/05/2011} ...</h3>
     *
     * @param json
     * @return
     */
    public static List<String> jsonDataConvertToCollectionList(String json) {
        List<String> fieldCodeList = null;
        if (json != null) {
            fieldCodeList = new ArrayList<>();
            try {
                JSONObject parentJSON = (JSONObject) new JSONParser().parse(json);
                JSONObject innerJSON = (JSONObject) parentJSON.get("fieldsCodeName");
                for (int i = 0; i < innerJSON.size(); i++) {
                    fieldCodeList.add(i, (String) innerJSON.get(String.valueOf(i)));
                }
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
        }
        return fieldCodeList;
    }

    /**
     * <h1>... This is method JSON Facet Filter data convert to java FacetFilterRpc object ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {15:15 31/05/2011} ...</h3>
     *
     * @param json
     * @return
     */
    public static FacetFilterRpc jsonConvertToFacetFilterRpc(String json) {
        if (json != null) {
            FacetFilterRpc facetFilter = new FacetFilterRpc();
            try {
                JSONObject parentObjectJSON = (JSONObject) new JSONParser().parse(json);
                JSONObject innerObjectJSON = (JSONObject) parentObjectJSON.get("facetFilter");
                if (innerObjectJSON.containsKey("objectID")) {
                    facetFilter.setObjectID(Integer.valueOf((String) innerObjectJSON.get("objectID")));
                }
                if (innerObjectJSON.containsKey("type")) {
                    facetFilter.setType(ListPanelType.valueOf(innerObjectJSON.get("type").toString()));
                }
                if (innerObjectJSON.containsKey("typeId")) {
                    facetFilter.setTypeId(Integer.valueOf((String) innerObjectJSON.get("typeId")));
                }
                if (innerObjectJSON.containsKey("filterChanges")) {
                    facetFilter.setFilterChanges(Boolean.valueOf(innerObjectJSON.get("filterChanges").toString()));
                }
                if (innerObjectJSON.containsKey("startDate")) {
                    String startDate = (String) innerObjectJSON.get("startDate");
                    if (startDate != null && !"".equals(startDate)) {
                        Date start = ServerUtils.parseDate(startDate, "yyyy-MM-dd HH:mm:ss");
                        facetFilter.setStartDate(start);
                    }
                }
                if (innerObjectJSON.containsKey("endDate")) {
                    String endDate = (String) innerObjectJSON.get("endDate");
                    if (endDate != null && !"".equals(endDate)) {
                        Date end = ServerUtils.parseDate(endDate, "yyyy-MM-dd HH:mm:ss");
                        facetFilter.setEndDate(end);
                    }
                }
                String selectedDateSolrCode = (String) innerObjectJSON.get("selectedDateSolrCode");
                if (selectedDateSolrCode != null && !"".equals(selectedDateSolrCode)) {
                    facetFilter.setSelectedDateSolrCodeName(selectedDateSolrCode);
                }
                facetFilter.setFilterChanges(Boolean.valueOf((String) innerObjectJSON.get("filterChanges")));
                JSONObject facetContentList = (JSONObject) innerObjectJSON.get("facetContentList");
                if (facetContentList != null) {
                    HashMap<String, FacetContentRpc> facetContents = new HashMap<>();
                    for (Object key : facetContentList.keySet()) {
                        FacetContentRpc contents = new FacetContentRpc();
                        HashMap<Integer, Integer> savedItems = new HashMap<>();
                        JSONObject listData = (JSONObject) facetContentList.get(String.valueOf(key));
                        SelectItem[] items = new SelectItem[listData.size()];
                        for (int j = 0; j < listData.size(); j++) {
                            JSONObject itemData = (JSONObject) listData.get(String.valueOf(j));
                            Integer id = Integer.valueOf((String) itemData.get("id"));
                            String name = (String) itemData.get("name");
                            String descreption = (String) itemData.get("description");
                            items[j] = new SelectItem(id, name);
                            items[j].setDescription(descreption);
                            savedItems.put(items[j].getId(), items[j].getId());
                        }
                        contents.setFacetItems(items);
                        contents.setSavedItems(savedItems);
                        facetContents.put(String.valueOf(key), contents);
                    }
                    facetFilter.setFacetContentMap(facetContents);
                }
                JSONObject facetCustomList = (JSONObject) innerObjectJSON.get("facetCustomList");
                if (facetCustomList != null) {
                    HashMap<String, String> customData = new HashMap<>();
                    for (Object key : facetCustomList.keySet()) {
                        customData.put(key.toString(), (String) facetCustomList.get(key));
                    }
                    facetFilter.setCustomData(customData);
                }
                JSONObject solrFieldList = (JSONObject) innerObjectJSON.get("solrFieldList");
                if (solrFieldList != null) {
                    HashMap<String, FacetSolrField> facetSolrMap = new HashMap<>();
                    for (Object key : solrFieldList.keySet()) {
                        FacetSolrField solrField = new FacetSolrField();
                        JSONObject facetSolrJSON = (JSONObject) solrFieldList.get(key.toString());
                        solrField.setConditionItemId(Boolean.valueOf((String) facetSolrJSON.get("isCondationId")));
                        solrField.setSolrFacetFieldName((String) facetSolrJSON.get("facetField"));
                        solrField.setSolrFieldCriteriaName((String) facetSolrJSON.get("criteriaName"));
                        solrField.setWithID(facetSolrJSON.get("isWithID") != null ? Boolean.valueOf((String) facetSolrJSON.get("isWithID")) : false);
                        facetSolrMap.put(key.toString(), solrField);
                    }
                    facetFilter.setShowSolrFieldMap(facetSolrMap);
                }
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
            return facetFilter;
        }
        return null;
    }

    /**
     * <h1>... This is method java FacetFilterRpc object convert to JSON data ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {15:34 31/05/2011} ...</h3>
     *
     * @param facetFilter
     * @return
     */
    public static String facetFilrerConvertToJsonData(FacetFilterRpc facetFilter) {
        if (facetFilter != null) {
            JSONObject parentJSON = new JSONObject();
            JSONObject innerJSON = new JSONObject();
            JSONObject contentListJSON = new JSONObject();
            JSONObject oneTimeFilterSavedListJSON = new JSONObject();
            JSONObject settingListJSON = new JSONObject();
            JSONObject customListJSON = new JSONObject();
            parentJSON.put("facetFilter", innerJSON);
            innerJSON.put("facetSettingList", settingListJSON);
            innerJSON.put("facetContentList", contentListJSON);
            innerJSON.put("facetOneTimeFilterList", oneTimeFilterSavedListJSON);
            innerJSON.put("facetCustomList", customListJSON);
            innerJSON.put("selectedDateSolrCode", facetFilter.getSelectedDateSolrCodeName());
            HashMap<String, FacetSettingRpc> settingMap = facetFilter.getFacetSettingMap();
            for (String key : settingMap.keySet()) {
                FacetSettingRpc facetSettingRpc = settingMap.get(key);
                JSONObject facetSettingJSON = new JSONObject();
                facetSettingJSON.put("trow", String.valueOf(facetSettingRpc.getRow()));
                facetSettingJSON.put("tcell", String.valueOf(facetSettingRpc.getCell()));
                settingListJSON.put(key, facetSettingJSON);

            }
            HashMap<String, FacetContentRpc> items = facetFilter.getFacetContentMap();
            for (String key : items.keySet()) {// content list convert to json data
                JSONObject itemsListJSON = new JSONObject();
                contentListJSON.put(key, itemsListJSON);
                SelectItem[] itemArray = items.get(key).getFacetItems();
                for (int j = 0; j < itemArray.length; j++) {
                    JSONObject itemJSON = new JSONObject();
                    itemJSON.put("id", String.valueOf(itemArray[j].getId()));
                    itemJSON.put("name", itemArray[j].getName());
                    itemsListJSON.put(String.valueOf(j), itemJSON);
                }
            }
            //By Anvar for Stepan OneTimeFilter
             try {
                if( ListPanelType.LeadListPanelOTF.equals(facetFilter.getType()) ) {
                     for (String key : items.keySet()) {// content list convert to json data

                         JSONObject itemsListJSON = (JSONObject) oneTimeFilterSavedListJSON.get(key);

                         if(itemsListJSON == null) {
                             itemsListJSON = new JSONObject();
                             oneTimeFilterSavedListJSON.put(key, itemsListJSON);
                         }

                         Map<Integer, Integer> savedItems = items.get(key).getSavedItems();

                         SelectItem[] facetItems = items.get(key).getFacetItems();
                         Map<Integer, SelectItem> facetItemsMap = new HashMap<>();
                         try {
                             if(facetItems!=null && facetItems.length>0) {
                                 facetItemsMap = Arrays.stream(facetItems).collect(Collectors.toMap(SelectItem::getId, i->i,
                                         (status1, status2) -> {
                                             System.out.println("duplicate key found! - " + status1);
                                             return status1;
                                         }));
                             }
                         } catch (Exception e) {
                             log.error("", e);
                         }

                         int j = itemsListJSON.size();
                         for ( Map.Entry<Integer, Integer> entry : savedItems.entrySet() ) {
                             JSONObject itemJSON = new JSONObject();
                             itemJSON.put("id", String.valueOf(entry.getKey()));

                             if(facetItemsMap.get(entry.getKey())!=null) {
                                 itemJSON.put("name", facetItemsMap.get(entry.getKey()).getName());
                             } else {
                                 itemJSON.put("name", entry.getKey().toString());
                             }
                             itemsListJSON.put(String.valueOf(j), itemJSON);
                             j++;
                         }
                     }
                 }
             } catch (Exception e) {
                log.error("", e);
             }
            //End Of for Stepan OneTimeFilter
            Map<String, String> customData = facetFilter.getCustomData();
            for (String key : customData.keySet()) {// custom data conver to json data
                customListJSON.put(key, customData.get(key));
            }
            return parentJSON.toJSONString();
        }
        return "";
    }

    /**
     * <h1>... This is method JSON Facet Filter data convert to java FacetFilterRpc object ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {16:34 03/06/2011} ...</h3>
     *
     * @param json
     * @return
     */
    public static FacetFilterRpc jsonConvertToFacetFilterRpc(String json, Set<String> codeNameList) {
        if (json != null) {
            FacetFilterRpc facetFilter = new FacetFilterRpc();
            try {
                JSONObject parentObjectJSON = (JSONObject) new JSONParser().parse(json);
                JSONObject innerObjectJSON = (JSONObject) parentObjectJSON.get("facetFilter");
                facetFilter.setFilterChanges(Boolean.valueOf((String) innerObjectJSON.get("filterChanges")));
                JSONObject facetSettingList = (JSONObject) innerObjectJSON.get("facetSettingList");
                JSONObject facetContentList = (JSONObject) innerObjectJSON.get("facetContentList");
                JSONObject facetOneTimeFilterList = (JSONObject) innerObjectJSON.get("facetOneTimeFilterList");
                String selectedDateSolrCode = (String) innerObjectJSON.get("selectedDateSolrCode");
                if (selectedDateSolrCode != null && !"".equals(selectedDateSolrCode)) {
                    facetFilter.setSelectedDateSolrCodeName(selectedDateSolrCode);
                }
                if (facetSettingList != null) {
                    HashMap<String, FacetSettingRpc> facetSettingMap = new HashMap<>();

                    for (Object keyObject : facetSettingList.keySet()) {
                        if( (codeNameList!=null && codeNameList.contains(keyObject.toString())) || FacetSettingRpc.FACET_DATE_PERIOD.equalsIgnoreCase(keyObject.toString()) ) {
                            JSONObject facetSetting = (JSONObject) facetSettingList.get(keyObject);
                            FacetSettingRpc facetSettingRpc = new FacetSettingRpc( Integer.parseInt(facetSetting.get("trow").toString()),
                            Integer.parseInt(facetSetting.get("tcell").toString())  );

                            facetSettingMap.put(keyObject.toString(), facetSettingRpc);
                        }
                    }
                    if (facetSettingMap.size() > 0) {
                        codeNameList = new HashSet<>(facetSettingMap.keySet());
                        codeNameList.remove(FacetSettingRpc.FACET_DATE_PERIOD);
                        facetFilter.setShowFacetCodeName(new ArrayList<>(codeNameList));
                    }
                    facetFilter.setFacetSettingMap(facetSettingMap);
                }
                if (facetContentList != null) {
                    HashMap<String, FacetContentRpc> facetContents = new HashMap<>();
                    for (String key : codeNameList) {
                        FacetContentRpc contents = new FacetContentRpc();
                        HashMap<Integer, Integer> savedItems = new HashMap<>();
                        JSONObject listData = (JSONObject) facetContentList.get(key);
                        if (listData != null) {
                            SelectItem[] items = new SelectItem[listData.size()];
                            for (int j = 0; j < listData.size(); j++) {
                                JSONObject itemData = (JSONObject) listData.get(String.valueOf(j));
                                Integer id = Integer.valueOf((String) itemData.get("id"));
                                String name = (String) itemData.get("name");
                                if("95".equalsIgnoreCase(name) && "jobtitle".equalsIgnoreCase(key)) {
                                    name="";
                                }
                                String description = (String) itemData.get("description");
                                items[j] = new SelectItem(id, name);
                                items[j].setDescription(description);
                                savedItems.put(items[j].getId(), items[j].getId());
                            }
                            contents.setFacetItems(items);
                            contents.setSavedItems(savedItems);
                            facetContents.put(key, contents);
                        }

                    }
                    facetFilter.setFacetContentMap(facetContents);
                }
                //OneTimeFilter Specific
                if (facetOneTimeFilterList != null && facetOneTimeFilterList.size()>0) {
                    HashMap<String, FacetContentRpc> facetContents = new HashMap<>();
                    for (String key : codeNameList) {

                        FacetContentRpc contents = facetFilter.getFacetContentMap().computeIfAbsent(key, k -> new FacetContentRpc());


                        HashMap<Integer, Integer> savedItems = new HashMap<>();
                        JSONObject listData = (JSONObject) facetOneTimeFilterList.get(key);

                        if (listData != null) {
                            SelectItem[] items = new SelectItem[listData.size()];
                            for (int j = 0; j < listData.size(); j++) {
                                JSONObject itemData = (JSONObject) listData.get(String.valueOf(j));
                                Integer id = Integer.valueOf((String) itemData.get("id"));
                                String name = (String) itemData.get("name");
                                if("95".equalsIgnoreCase(name) && "jobtitle".equalsIgnoreCase(key)) {
                                    name="";
                                }
                                String description = (String) itemData.get("description");
                                items[j] = new SelectItem(id, name);
                                items[j].setDescription(description);
                                savedItems.put(items[j].getId(), items[j].getId());
                            }
                            contents.setFacetItems(items);
                            contents.setSavedItems(savedItems);
                            //facetContents.put(key, contents);
                        }
                    }
//                    facetFilter.setFacetContentMap(facetContents);
                }
                //OneTimeFilter Specific
                JSONObject facetCustomList = (JSONObject) innerObjectJSON.get("facetCustomList");
                if (facetCustomList != null) {
                    HashMap<String, String> customData = new HashMap<>();
                    for (Object key : facetCustomList.keySet()) {
                        customData.put(key.toString(), facetCustomList.get(key).toString());
                    }
                    facetFilter.setCustomData(customData);
                }
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
            return facetFilter;
        }
        return null;
    }

    /**
     * <h1>... This is method JSON List Panel Column Code data convert to java ListPanelToolRpc object ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {17:45 13/06/2011} ...</h3>
     *
     * @param json listPanelToolJson
     * @return
     */
    public static ListPanelToolRpc jsonConvertToListPanelToolRpc(String json) {
        if (json != null) {
            ListPanelToolRpc listPanelToolRpc = new ListPanelToolRpc();
            try {
                JSONObject parentObjectJSON = (JSONObject) new JSONParser().parse(json);
                JSONObject innerObjectJSON = (JSONObject) parentObjectJSON.get("listPanelTool");
                if (innerObjectJSON.containsKey("type")) {
                    listPanelToolRpc.setType(ListPanelType.valueOf(innerObjectJSON.get("type").toString()));
                }
                if (innerObjectJSON.containsKey("typeId")) {
                    listPanelToolRpc.setTypeId(Integer.valueOf((String) innerObjectJSON.get("typeId")));
                }
                if (innerObjectJSON.containsKey("columnCode")) {
                    JSONObject columnCodeJSON = (JSONObject) innerObjectJSON.get("columnCode");
                    ArrayList<String> columnCode = new ArrayList<>();
                    for (int i = 0; i < columnCodeJSON.size(); i++) {
                        columnCode.add((String) columnCodeJSON.get(String.valueOf(i)));
                    }
                    listPanelToolRpc.setColumnCodeName(columnCode);
                }
                if (innerObjectJSON.containsKey("customFieldList")) {
                    JSONObject customFieldListJSON = (JSONObject) innerObjectJSON.get("customFieldList");
                    ArrayList<CompanyCustomFieldItem> companyCustomFieldList = new ArrayList<>();
                    for (int i = 0; i < customFieldListJSON.size(); i++) {
                        CompanyCustomFieldItem customField = new CompanyCustomFieldItem();
                        JSONObject customFieldJSON = (JSONObject) customFieldListJSON.get(String.valueOf(i));
                        customField.setUiType((String) customFieldJSON.get("uiType"));
                        customField.setDataType((String) customFieldJSON.get("dataType"));
                        customField.setFieldName((String) customFieldJSON.get("fieldName"));
                        customField.setEntityName((String) customFieldJSON.get("entityName"));
                        customField.setColumnCode((String) customFieldJSON.get("columnCode"));
                        companyCustomFieldList.add(customField);
                    }
                    listPanelToolRpc.setListViewCustomFields(companyCustomFieldList);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return listPanelToolRpc;
        }
        return null;
    }

    public static String objectConvertToJsonString(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> T jsonStringConvertToObject(String json, Class<T> clazz) {
        if (json == null || "".equals(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
        }
        return null;
    }
}
