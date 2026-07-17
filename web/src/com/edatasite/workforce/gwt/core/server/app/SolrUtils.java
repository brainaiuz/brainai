package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.finnetlimited.reportservice.core.server.CoreServiceImpl;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CommonParams;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

public class SolrUtils implements Constants {

    private static final Logger log = LoggerFactory.getLogger(CoreServiceImpl.class);

    public static boolean isNotNull(SolrDocument doc, String fieldName) {
        return doc.getFirstValue(fieldName) != null;
    }

    public static boolean isNull(SolrDocument doc, String fieldName) {
        return doc.getFirstValue(fieldName) == null;
    }

    public static String asString(Object object) {
        return (String) object;
    }

    public static Integer asInteger(Object object) {
        if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof String) {
            try {
                return Integer.valueOf((String) object);
            } catch (ClassCastException ex) {
                log.error(ex.getMessage());
            }
        }
        return null;
    }

    public static Double asDouble(Object object) {
        return (Double) object;
    }

    public static Float asFloat(Object object) {
        return (Float) object;
    }

    public static Long asLong(Object object) {
        return (Long) object;
    }

    public static Date asDate(Object object) {
        return (Date) object;
    }

    public static Boolean asBoolean(Object object) {
        return (Boolean) object;
    }

    private static BigDecimal asBigDecimal(Object value) {
        return new BigDecimal(String.valueOf(value));
    }

    private static List<String> asListString(Object value) {
        return (List<String>) value;
    }

    private static ArrayList<Integer> asListInteger(Object value) {
        return (ArrayList<Integer>) value;
    }

    public static String asString(SolrDocument doc, String fieldName, String... insteadOfNull) {
        if (isNull(doc, fieldName)) {
            if (insteadOfNull != null && insteadOfNull.length > 0) {
                return insteadOfNull[0];
            }
            return null;
        }
        return asString(getValue(doc, fieldName));
    }

    public static String asString(String value, String... insteadOfNull) {
        if (value == null) {
            if (insteadOfNull != null && insteadOfNull.length > 0) {
                return insteadOfNull[0];
            }
            return null;
        }
        return value;
    }

    public static String asHighLitsString(SolrDocument doc, String fieldName, String... insteadOfNull) {
        if (isNull(doc, fieldName)) {
            if (insteadOfNull != null && insteadOfNull.length > 0) {
                return insteadOfNull[0];
            }
            return null;
        }
        return getValue(doc, fieldName).toString();
    }

    public static Integer asInteger(SolrDocument doc, String fieldName, Integer... insteadOfNull) {
        if (isNull(doc, fieldName)) {
            if (insteadOfNull != null && insteadOfNull.length > 0) {
                return insteadOfNull[0];
            }
            return null;
        }
        return asInteger(getValue(doc, fieldName));
    }

    public static Double asDouble(SolrDocument doc, String fieldName, Double... insteadOfNull) {
        if (isNull(doc, fieldName)) {
            if (insteadOfNull != null && insteadOfNull.length > 0) {
                return insteadOfNull[0];
            }
            return null;
        }
        return asDouble(getValue(doc, fieldName));
    }

    public static Float asFloat(SolrDocument doc, String fieldName, Float... insteadOfNull) {
        if (isNull(doc, fieldName)) {
            if (insteadOfNull != null && insteadOfNull.length > 0) {
                return insteadOfNull[0];
            }
            return null;
        }
        return asFloat(getValue(doc, fieldName));
    }

    public static Long asLong(SolrDocument doc, String fieldName, Long... insteadOfNull) {
        if (isNull(doc, fieldName)) {
            if (insteadOfNull != null && insteadOfNull.length > 0) {
                return insteadOfNull[0];
            }
            return null;
        }
        return asLong(getValue(doc, fieldName));
    }

    public static Date asDate(SolrDocument doc, String fieldName, Date... insteadOfNull) {
        if (isNull(doc, fieldName)) {
            if (insteadOfNull != null && insteadOfNull.length > 0) {
                return insteadOfNull[0];
            }
            return null;
        }
        return asDate(getValue(doc, fieldName));
    }

    public static boolean asBoolean(SolrDocument doc, String fieldName, boolean... insteadOfNull) {
        if (isNull(doc, fieldName)) {
            if (insteadOfNull != null && insteadOfNull.length > 0) {
                return insteadOfNull[0];
            }
            return false;
        }
        return asBoolean(getValue(doc, fieldName));
    }

    public static boolean asBoolean(Boolean value, boolean... insteadOfNull) {
        if (value == null) {
            if (insteadOfNull != null && insteadOfNull.length > 0) {
                return insteadOfNull[0];
            }
            return false;
        }
        return value.booleanValue();
    }

    private static Object getValue(SolrDocument doc, String fieldName) {
        return doc.getFieldValue(fieldName);
    }

    public static String getIdName(Integer objectID, String name) {
        return objectID.toString() + SolrContactRepresenter.SPLIT + name;
    }

    public static String getIdCodeName(Integer objectID, String code, String name) {
        return objectID.toString() + SolrContactRepresenter.SPLIT + code + SolrContactRepresenter.SPLIT + name;
    }

    public static String generatedOId(Integer... ids) {
        StringBuilder oid = new StringBuilder();
        for (Integer id : ids) {
            if (!oid.toString().equals("")) {
                oid.append("_");
            }
            oid.append(id);
        }
        return oid.toString();
    }

    public static <T extends EdsObject> String getIdName(T object) {
        if (object != null) {
            return object.getObjectID().toString() + SolrContactRepresenter.SPLIT + object.getName();
        }
        return null;
    }

    public static List<Integer> getIdsFromSolrDocument(String fieldID, SolrDocument... solrDocuments) {
        List<Integer> ids = new ArrayList<>();
        if (solrDocuments != null && solrDocuments.length > 0) {
            return getIdsFromSolrDocument(fieldID, Arrays.asList(solrDocuments));
        }
        return ids;
    }

    public static List<Integer> getIdsFromSolrDocument(String fieldID, List<SolrDocument> solrDocuments) {
        List<Integer> ids = new ArrayList<>();
        if (solrDocuments != null && solrDocuments.size() > 0) {
            for (SolrDocument document : solrDocuments) {
                Integer id = (Integer) document.getFieldValue(fieldID);
                if (!ids.contains(id) && id != null) {
                    ids.add(id);
                }
            }
        }
        return ids;
    }

    public static <T> List<Integer> getValuesFromField(String fieldName, Collection<T> objects) {
        List<Integer> values = new ArrayList<>();
        Set<Object> seenValues = new HashSet<>();

        if (objects != null) {
            for (T object : objects) {
                try {
                    Field field = object.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = field.get(object);

                    if (value != null && seenValues.add(value)) {
                        values.add((Integer) value);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return values;
    }

    public static BigDecimal asBigDecimal(SolrDocument doc, String fieldName, BigDecimal... insteadOfNull) {
        if (isNull(doc, fieldName)) {
            if (insteadOfNull != null && insteadOfNull.length > 0) {
                return insteadOfNull[0];
            }
            return null;
        }
        return asBigDecimal(getValue(doc, fieldName));
    }

    public static List<String> asListString(SolrDocument doc, String fieldsName, String... insteadofNull) {
        if (isNull(doc, fieldsName)) {
            return null;
        }
        return asListString(getValue(doc, fieldsName));
    }

    public static ArrayList<Integer> asListInteger(SolrDocument doc, String fieldsName) {
        if (isNull(doc, fieldsName)) {
            return null;
        }
        return asListInteger(getValue(doc, fieldsName));
    }

    public static int getListCount(SolrDocument doc, String fieldsName, String... insteadofNull) {
        if (isNull(doc, fieldsName)) {
            return 0;
        }
        return asListString(getValue(doc, fieldsName)).size();
    }

    public static SelectItem asSelectItem(SolrDocument doc, String idField, String nameField) {
        if (isNotNull(doc, idField) || isNotNull(doc, nameField)) {
            return new SelectItem(asInteger(doc, idField), asString(doc, nameField));
        }
        return null;
    }

    public static SelectItem asSelectItem(Integer idField, String nameField) {
        if (idField != null || nameField != null) {
            return new SelectItem(idField, nameField);
        }
        return null;
    }

    public static ReferenceItem asReferenceItem(SolrDocument doc, String idField, String codeField) {
        if (isNotNull(doc, idField) || isNotNull(doc, codeField)) {
            ReferenceItem item = new ReferenceItem(asInteger(doc, idField));
            item.setCode(asString(doc, codeField));
            return item;
        }
        return null;
    }

    public static ReferenceItem asReferenceItem(Integer idField, String codeField) {
        if (idField != null || codeField != null) {
            ReferenceItem item = new ReferenceItem(idField);
            item.setCode(codeField);
            return item;
        }
        return null;
    }

    public static SolrQuery defaultSolrQuery(String solrQuery, ListingFilterParameter fp, ListLoadConfig config, String solrSortField, String defaultSortField) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        config = config == null ? new ListLoadConfig() : config;
        query.setStart(config.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(config.getLimit()));

        if (!fp.isSearchButton() && !fp.isLookUp()) {
            if (config.getSortField() != null && !"".equals(config.getSortField())) {
                boolean desc = false;
                if (Constants.DESC == config.getSortDir()) {
                    desc = true;
                }
                if (solrSortField != null) {
                    query.setSort(solrSortField, desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc);
                } else {
                    CustomFieldsUtils.setCustomFieldsSortableNameToSolr(config.getSortField(), desc, query, true);
                }
            } else {
                query.setSort(defaultSortField, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }
}
