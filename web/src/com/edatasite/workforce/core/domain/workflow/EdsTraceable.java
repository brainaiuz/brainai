package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.Operands;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ID;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.INTEGRATION_ID;

/**
 * Created by Hayot on 4/7/2014.
 */
@MappedSuperclass
public class EdsTraceable extends EdsObject {

    @Transient
    protected Map<String, Object> fieldIDValue = new HashMap<>();

    @Transient
    Set<String> oldChanges = new HashSet<>();

    @Column(name = "version", columnDefinition = " integer default 0")
    private Integer version = 0;

    @Column(name = "lastChanges")
    @Type(type = "text")
    private String lastChanges;

    @Transient
    private String newChanges = "";

    @Column(name = "integrationid")
    private String integrationId;

    private static String replaceWithBooleans(String pattern, Map<Integer, Boolean> result) {
        if (result != null && !result.isEmpty()) {
            List<Integer> keys = new ArrayList<Integer>(result.keySet());
            Collections.sort(keys);
            ListIterator<Integer> li = keys.listIterator(keys.size());
            while (li.hasPrevious()) {
                Integer key = (Integer) li.previous();
                pattern = pattern.replace(String.valueOf(key), result.get(key) ? "t" : "f");
            }
        }
        return pattern.replaceAll("\\s", "").replaceAll("(?i)and", "&").replaceAll("(?i)or", "|");
    }

    public static String simplifyPattern(String pattern) {
        if (pattern.contains("(")) {
            int index = findInnerParantesis(pattern);
            if (index >= 0) {
                int index2 = pattern.indexOf(")");
                String pattern1 = pattern.substring(0, index);
                String innerParanthesis = pattern.substring(index + 1, index2);
                String pattern3 = pattern.substring(index2 + 1);
                pattern = pattern1 + resolvePatternalCondition(innerParanthesis) + pattern3;
            }
        }
        if (pattern.contains("(")) {
            return simplifyPattern(pattern);
        } else {
            return resolvePatternalCondition(pattern);
        }
    }

    private static String resolvePatternalCondition(String innerParanthesis) {
        if (innerParanthesis.contains("&") || innerParanthesis.contains("|")) {
            int index = findFirstOperandIndex(innerParanthesis);
            int nextOperandIndex = innerParanthesis.indexOf("&", index + 1);
            nextOperandIndex = nextOperandIndex < 0 || innerParanthesis.indexOf("|", index + 1) < nextOperandIndex ? innerParanthesis.indexOf("|", index + 1) : nextOperandIndex;
            String innerParanthesis1 = nextOperandIndex > -1 ? innerParanthesis.substring(0, nextOperandIndex) : innerParanthesis;
            String innerParanthesis2 = nextOperandIndex > -1 ? innerParanthesis.substring(nextOperandIndex) : "";
            boolean variable1 = innerParanthesis1.substring(0, 1).equalsIgnoreCase("t");
            String operand = innerParanthesis1.substring(1, 2);
            boolean variable2 = innerParanthesis1.substring(2, 3).equalsIgnoreCase("t");
            boolean result = (operand.equals("&") ? variable1 && variable2 : variable1 || variable2);
            innerParanthesis1 = (result ? "t" : "f") + (innerParanthesis1.substring(3));
            innerParanthesis = innerParanthesis1 + innerParanthesis2;
        }
        if (innerParanthesis.length() == 1) {
            return innerParanthesis;
        } else {
            return resolvePatternalCondition(innerParanthesis);
        }
    }

    private static int findFirstOperandIndex(String innerParanthesis) {
        int andIndex = innerParanthesis.indexOf("&");
        int orIndex = innerParanthesis.indexOf("|");
        if (andIndex < 0) {
            return orIndex;
        }
        if (orIndex < 0) {
            return andIndex;
        }
        return Math.min(andIndex, orIndex);
    }

    public static int findInnerParantesis(String pattern) {
        int start = -1;
        start = pattern.indexOf("(");
        while (start >= 0) {
            int end = pattern.indexOf(")", start);
            String part = pattern.substring(start + 1, end);
            if (!part.contains("(")) {
                break;
            } else {
                start = pattern.indexOf("(", start + 1);
            }
        }
        return start;
    }

    public Integer getObjectID() {
        return null;
    }

    public boolean isChanged(String field) {
        return field != null && !"".equals(field) && getChanges().contains(field);
    }

    private Set<String> getChanges() {
        if (oldChanges.size() == 0) {
            if (lastChanges != null && !"".equals(lastChanges)) {
                for (String field : lastChanges.split(",")) {
                    if (field != null && !"".equals(field)) {
                        oldChanges.add(field);
                    }
                }
            }
        }
        return oldChanges;
    }

    public void addCustomFieldChanges(String changes) {
        if (StringUtils.isNotEmpty(changes)) {
            for (String change : changes.split(",")) {
                if (StringUtils.isNotEmpty(change)) {
                    addChange(change);
                }
            }
        }
    }

    public void addChange(String field) {
        if (field != null && !"".equals(field)) {
            if (getLastChanges() != null && !"".equals(getLastChanges())) {
                setLastChanges(getLastChanges() + "," + field);
            } else {
                setLastChanges(field);
            }
        }
    }

    public String getLastChanges() {
        return lastChanges == null ? "" : lastChanges;
    }

    public void setLastChanges(String changedFields) {
        this.newChanges = changedFields;
        this.lastChanges = this.newChanges;
        oldChanges.clear();
    }

    public Integer getVersion() {
        return version == null ? 0 : version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void clear() {
        setLastChanges("");
    }

    public boolean isInCondition(EdsWorkflowRule rule, List<Integer> availableItems, EdsUser user) {
        if (rule == null || rule.getConditions() == null) {
            return false;
        }
        if (rule.getExecutionCriteria() != null && WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_UPDATE_SPECIFIED_FIELD.equals(rule.getExecutionCriteria()) && (rule.getExecutionCriteriaUpdateField() == null || !isChanged(rule.getExecutionCriteriaUpdateField()))) {
            return false;
        }
        if (rule.isDynamicCondition()) {
            return availableItems.contains(getObjectID());
        } else {
            if (rule.getConditions().isEmpty()) {
                return true;
            }
            if (rule.getPattern() == null || "".equals(rule.getPattern().replaceAll("\\s", ""))) {
                return true;
            }
            Map<Integer, Boolean> result = new LinkedHashMap<>();
            for (EdsWorkflowCondition condition : rule.getConditions()) {
                result.put(condition.getConditionID(), checkCondition(condition, user));
            }
            return checkInPatter(result, rule.getPattern());
        }
    }

    private boolean checkInPatter(Map<Integer, Boolean> result, String pattern) {
        pattern = simplifyPattern(replaceWithBooleans(pattern, result));
        return pattern.equalsIgnoreCase("t");
    }

    private Boolean checkCondition(EdsWorkflowCondition condition, EdsUser user) {
        return checkCondition(condition.getColumn(), condition.getOperand(), condition.getValue(), user);
    }

    protected Boolean checkCondition(String fieldID, String operands, String value, EdsUser user) {
        if (fieldID != null) {
            if (!hasCustomConditionChecking(fieldID)) {
                Object realValue = getRealValue(fieldID);
                if (realValue != null) {
                    if (realValue instanceof String) {
                        value = value.contains("@") && value.indexOf("@") != value.lastIndexOf("@") ? value.substring(value.indexOf("@") + 1, value.lastIndexOf("@")) : value;
                        return checkStrings((String) realValue, operands, value);
                    }
                    if (realValue instanceof Number) {
                        value = value.contains("@") && value.indexOf("@") != value.lastIndexOf("@") ? value.substring(value.indexOf("@") + 1, value.lastIndexOf("@")) : value;
                        return checkNumbers((Number) realValue, operands, value != null ? Double.valueOf(value) : null);
                    }
                    if (realValue instanceof Date) {
                        return checkDates((Date) realValue, operands, value, user, fieldID);
                    } else if (realValue instanceof EdsReference) {
                        return checkReference((EdsReference) realValue, operands, value);
                    } else if (realValue instanceof EdsObject) {
                        return checkObject((EdsObject) realValue, operands, value);
                    } else if (realValue instanceof Boolean) {
                        return checkBooleans((Boolean) realValue, (Boolean.valueOf(value)));
                    } else if (realValue instanceof List) {
                        for (Object field : (List) realValue) {
                            if (field instanceof String) {
                                if (checkStrings((String) field, operands, value)) {
                                    return true;
                                }
                            }
                            if (field instanceof Number) {
                                if (checkNumbers(((BigDecimal) field).setScale(0), operands, value != null ? BigDecimal.valueOf(Double.valueOf((value))).setScale(0) : null)) {
                                    return true;
                                }
                            }
                            if (field instanceof Date) {
                                if (checkDates((Date) field, operands, (String) field, user, value)) {
                                    return true;
                                }
                            } else if (field instanceof EdsReference) {
                                if (checkReference((EdsReference) field, operands, value)) {
                                    return true;
                                }
                            } else if (field instanceof EdsObject) {
                                if (checkObject((EdsObject) field, operands, value)) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                } else {
                    if (value == null) {
                        return operands == null || Operands.Core.EQUAL.equals(operands) || Operands.DateT.IS.equals(operands) || Operands.StringT.MATCHES.equals(operands) || Operands.StringT.CONTAINS.equals(operands);
                    } if (value.isEmpty() && realValue == null) {
                        return true;
                    } else {
                        return operands == null || Operands.Core.NOT_EQUAL.equals(operands) || Operands.DateT.IS_NOT.equals(operands) || Operands.StringT.NOT_CONTAINS.equals(operands);
                    }
                }
            } else {
                return checkConditionCustom(fieldID, operands, value);
            }
        }
        return true;
    }

    private Boolean checkObject(EdsObject realValue, String operands, String value) {
        boolean result = false;
        if (realValue == null || value == null) {
            return Operands.Core.NOT_EQUAL.equals(operands);
        }
        String[] values = value.contains("@") ? value.split("@") : value.split("-> ");
        Integer id = values != null && values.length > 0 && values[0].matches(Constants.REGEX_INTEGER) ? Integer.valueOf(values[0]) : null;
        String name = values != null && values.length > 1 ? values[1] : values[0];
        String[] names = name.contains("->") ? name.split("->") : name.split(" - ");
        name = names != null && names.length > 1 ? names[1].trim() : names[0].trim();
        if (realValue instanceof EdsUser) {
            result = id != null && id.equals(realValue.getObjectID());
        } else {
            result = id != null ? id.equals(realValue.getObjectID()) : realValue.getName() != null && name.equals(realValue.getName());
        }
        return Operands.Core.NOT_EQUAL.equals(operands) != result;
    }

    private boolean checkReference(EdsReference realValue, String operands, String value) {
        if (realValue == null || value == null) {
            return Operands.Core.NOT_EQUAL.equals(operands);
        }
        String[] values = value.split("@");
        Integer id = values != null && values.length > 0 && values[0].matches(Constants.REGEX_INTEGER) ? Integer.valueOf(values[0]) : null;
        String name = values != null && values.length > 1 ? values[1] : null;
        String code = values != null && values.length > 2 ? values[2] : name;
        boolean result = EdsReference.checkEqaulity(realValue, id, code, name);
        return Operands.Core.NOT_EQUAL.equals(operands) != result;
    }

    protected boolean checkStrings(String realValue, String operands, String value) {
        if (realValue == null || value == null) {
            return Operands.Core.NOT_EQUAL.equals(operands) || Operands.StringT.NOT_CONTAINS.equals(operands);
        }
        if (Operands.Core.EQUAL.equals(operands)) {
            return realValue.equals(value);
        } else if (Operands.Core.NOT_EQUAL.equals(operands)) {
            return !realValue.equals(value);
        } else if (Operands.StringT.CONTAINS.equals(operands)) {
            return realValue.contains(value);
        } else if (Operands.StringT.MATCHES.equals(operands)) {
            return realValue.matches(value);
        } else if (Operands.StringT.NOT_CONTAINS.equals(operands)) {
            return !realValue.contains(value);
        }
        return false;
    }

    protected boolean checkDates(Date realValue, String operands, String value, EdsUser user, String field_ID) {
        boolean isBirthDay = CustomFormConstants.BIRTH_DAY.equals(field_ID);
        if (realValue == null || value == null) {
            return Operands.DateT.IS_NOT.equals(operands);
        }
        if (!isBirthDay) {
            realValue = ServerUtils.convertServerDateToUserDate(realValue, user.getUserTimezone());
        }
        Date now = ServerUtils.convertServerDateToUserDate(new Date(), user.getUserTimezone());
        if (Operands.DateT.IS.equals(operands)) {
            Date dateValue = ServerUtils.parseDate(value);
            if (isBirthDay) {
                return realValue.getMonth() == dateValue.getMonth() && realValue.getDate() == dateValue.getDate();
            }
            return DateUtils.isSameDay(realValue, dateValue);
        } else if (Operands.DateT.IS_NOT.equals(operands)) {
            Date dateValue = ServerUtils.parseDate(value);
            if (isBirthDay) {
                return realValue.getMonth() != dateValue.getMonth() || realValue.getDate() != dateValue.getDate();
            }
            return !DateUtils.isSameDay(realValue, dateValue);
        } else if (Operands.DateT.IS_AFTER.equals(operands)) {
            Date dateValue = ServerUtils.parseDate(value);
            if (isBirthDay) {
                return realValue.getMonth() > dateValue.getMonth() || (realValue.getMonth() == dateValue.getMonth() && realValue.getDate() > dateValue.getDate());
            }
            return !DateUtils.isSameDay(realValue, dateValue) && realValue.after(dateValue);
        } else if (Operands.DateT.IS_BEFORE.equals(operands)) {
            Date dateValue = ServerUtils.parseDate(value);
            if (isBirthDay) {
                return realValue.getMonth() < dateValue.getMonth() || (realValue.getMonth() == dateValue.getMonth() && realValue.getDate() < dateValue.getDate());
            }
            return !DateUtils.isSameDay(realValue, dateValue) && realValue.before(dateValue);
        } else if (Operands.DateT.TODAY.equals(operands)) {
            if (isBirthDay) {
                return realValue.getMonth() == now.getMonth() && realValue.getDate() == now.getDate();
            }
            return DateUtils.isSameDay(realValue, now);
        }    else if (Operands.DateT.CURRENT_DAY.equals(operands)) {
            return realValue.getMonth() == now.getMonth() && realValue.getDate() == now.getDate();
        } else if (Operands.DateT.YESTERDAY.equals(operands)) {
            if (isBirthDay) {
                return realValue.getMonth() == now.getMonth() && realValue.getDate() == now.getDate() - 1;
            }
            return realValue.getYear() == now.getYear() && realValue.getMonth() == now.getMonth() && realValue.getDate() == now.getDate() - 1;
        } else if (Operands.DateT.TOMORROW.equals(operands)) {
            if (isBirthDay) {
                return realValue.getMonth() == now.getMonth() && realValue.getDate() == now.getDate() + 1;
            }
            return realValue.getYear() == now.getYear() && realValue.getMonth() == now.getMonth() && realValue.getDate() == now.getDate() + 1;
        } else if (Operands.DateT.AGE_IN_DAYS.equals(operands) || Operands.DateT.AGE_IN_HOURS.equals(operands)) {
            if (value.contains("@")) {
                String[] days = value.split("@");
                String ranger = days[0];
                int day = Integer.parseInt(days[1]);
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(realValue);
                cal2.setTime(now);
                long range =  (DateUtils.isSameDay(realValue, now) ? 0 : daysBetween(cal1, cal2));
                if (Operands.DateT.AGE_IN_HOURS.equals(operands)) {
                    range = hoursBetween(cal1, cal2);
                }
                if (">".equals(ranger)) {
                    return range > day;
                } else if ("<".equals(ranger)) {
                    return range < day;
                } else if ("=<".equals(ranger)) {
                    return range <= day;
                } else if (">=".equals(ranger)) {
                    return range >= day;
                } else if ("=".equals(ranger)) {
                    return range == day;
                }
            }
        } else if (Operands.DateT.HAS_DAYS_LEFT.equals(operands)) {
            if (value.contains("@")) {
                String[] days = value.split("@");
                String ranger = days[0];
                int day = Integer.parseInt(days[1]);
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(realValue);
                cal2.setTime(now);
                int range = DateUtils.isSameDay(realValue, now) ? 0 : daysLeftBetween(cal1, cal2);
                if (">".equals(ranger)) {
                    return range > day;
                } else if ("<".equals(ranger)) {
                    return range < day;
                } else if ("=<".equals(ranger)) {
                    return range <= day;
                } else if (">=".equals(ranger)) {
                    return range >= day;
                } else if ("=".equals(ranger)) {
                    return range == day;
                }
            }
        } else if (Operands.DateT.BETWEEN.equals(operands) || Operands.DateT.NOT_BETWEEN.equals(operands)) {
            if (value.contains("@")) {
                String[] days = value.split("@");
                Date date1 = ServerUtils.parseDate(days[0]);
                Date date2 = ServerUtils.parseDate(days[1]);
                boolean between = false;
                if (isBirthDay) {
                    between = (realValue.getMonth() == date1.getMonth() && realValue.getDate() == date1.getDate()) ||
                            (realValue.getMonth() == date2.getMonth() && realValue.getDate() == date2.getDate()) ||
                            (realValue.getMonth() > date1.getMonth() && realValue.getMonth() < date2.getMonth()) ||
                            (realValue.getMonth() == date1.getMonth() && realValue.getMonth() == date2.getMonth() && realValue.getDate() > date1.getDate() && realValue.getDate() < date2.getDate()) ||
                            (realValue.getMonth() == date1.getMonth() && realValue.getMonth() < date2.getMonth() && realValue.getDate() > date1.getDate()) ||
                            (realValue.getMonth() == date2.getMonth() && realValue.getMonth() > date1.getMonth() && realValue.getDate() < date2.getDate());
                } else {
                    between = DateUtils.isSameDay(realValue, date1) || DateUtils.isSameDay(realValue, date2) || (realValue.after(date1) && realValue.before(date2));
                }
                return Operands.DateT.BETWEEN.equals(operands) == between;
            }
        }
        return false;
    }

    private long hoursBetween(Calendar start, Calendar end) {
        return Duration.between(start.toInstant(), end.toInstant()).toHours();
    }

    private Integer daysBetween(Calendar start, Calendar end) {
        Calendar date = (Calendar) start.clone();
        Integer daysBetween = 0;
        while (date.before(end)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    private Integer daysLeftBetween(Calendar start, Calendar end) {
        Calendar date = (Calendar) end.clone();
        Integer daysBetween = 0;
        while (date.before(start)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    protected boolean checkNumbers(Number realValue, String operands, Number value) {
        if (realValue == null || value == null) {
            return Operands.Core.NOT_EQUAL.equals(operands);
        }
        if (Operands.Core.EQUAL.equals(operands)) {
            return realValue.equals(value);
        } else if (Operands.Core.NOT_EQUAL.equals(operands)) {
            return !realValue.equals(value);
        } else if (Operands.NumberT.GREATER.equals(operands)) {
            return realValue.doubleValue() > value.doubleValue();
        } else if (Operands.NumberT.GREATER_OR_EQUAL.equals(operands)) {
            return realValue.doubleValue() >= value.doubleValue();
        } else if (Operands.NumberT.LOWER.equals(operands)) {
            return realValue.doubleValue() < value.doubleValue();
        } else if (Operands.NumberT.LOWER_OR_EQUAL.equals(operands)) {
            return realValue.doubleValue() <= value.doubleValue();
        }
        return false;
    }

    private boolean checkBooleans(Boolean realValue, Boolean value){
        if (realValue == null || value == null) {
            return false;
        }
        return realValue == value;
    }

    protected boolean checkConditionCustom(String fieldID, String operands, String value) {
        return false;
    }

    public Object getRealValue(String fieldID) {
        return null;
    }

    public String getShortDateFormat() {
        return null;
    }

    protected boolean hasCustomConditionChecking(String fieldID) {
        return false;
    }

    public Map<String, Object> getPropertyValueAsMap() {
        return fieldIDValue;
    }


    protected void addFieldIDValue(String fieldID, Object object) {
        if (fieldID != null && object != null) {
            fieldIDValue.put(fieldID, object);
            if (!fieldID.contains("{")) {
                fieldIDValue.put("${" + fieldID.toLowerCase() + "}", object);
            }
        }
    }

    public Map<String, Object> getFieldValueAsMap(Set<String> fieldIDs) {
        Map<String, Object> result = this.getPropertyValueAsMap();
        result.put(ID, getObjectID());
        if (fieldIDs != null && fieldIDs.size() > 0) {
            for (String fieldID : fieldIDs) {
                String realFieldID = fieldID;
                fieldID = "${" + fieldID.toLowerCase() + "}";
                if (!result.containsKey(fieldID)) {
                    result.put(fieldID, getStringValueByFieldID(realFieldID));
                }
            }
        }
        result.put("${" + INTEGRATION_ID.toLowerCase() + "}", getIntegrationId());
        return result;
    }

    public Map<String, Object> getItemFieldValuesAsMap(Set<String> fieldIDs) {
        Map<String, Object> result = this.getPropertyValueAsMap();
        result.put(ID, getObjectID());
        if (fieldIDs != null && fieldIDs.size() > 0) {
            for (String fieldID : fieldIDs) {
                String realFieldID = fieldID;
                fieldID = "${" + "item_" + fieldID.toLowerCase() + "}";
                if (!result.containsKey(fieldID)) {
                    result.put(fieldID, getStringValueByFieldID(realFieldID));
                }
            }
        }
        result.put("${" + INTEGRATION_ID.toLowerCase() + "}", getIntegrationId());
        return result;
    }

    public Map<String, Object> getCategoryFieldValueAsMap(Set<String> fieldIDs) {
        Map<String, Object> result = this.getPropertyValueAsMap();
        result.put(ID, getObjectID());
        if (fieldIDs != null && fieldIDs.size() > 0) {
            for (String fieldID : fieldIDs) {
                String realFieldID = fieldID;
                fieldID = "${" + "cc_" + fieldID.toLowerCase() + "}";
                if (!result.containsKey(fieldID)) {
                    result.put(fieldID, getStringValueByFieldID(realFieldID));
                }
            }
        }
        result.put("${" + INTEGRATION_ID.toLowerCase() + "}", getIntegrationId());
        return result;
    }

    protected String getStringValueByFieldID(String realFieldID) {
        return getStringValueOfObject(getRealValue(realFieldID));
    }

    protected String getStringValueOfObject(Object obj) {
        StringBuilder valueString = null;
        if (obj == null) {
            valueString = new StringBuilder();
        } else {
            if (obj instanceof EdsReference) {
                valueString = new StringBuilder(((EdsReference) obj).getName());
            } else if (obj instanceof EdsAddress) {
                valueString = new StringBuilder(((EdsAddress) obj).getAddressDataAsHTML());
            } else if (obj instanceof EdsObject) {
                valueString = new StringBuilder(((EdsObject) obj).getName());
            } else if (obj instanceof Collection) {
                valueString = new StringBuilder();
                boolean useDelimitr = false;
                for (Object single : (Collection) obj) {
                    String value = getStringValueOfObject(single);
                    if (value != null && !"".equals(value)) {
                        valueString.append(useDelimitr ? ", " : "").append(value);
                        useDelimitr = true;
                    }
                }
            } else if (obj instanceof Map) {
                valueString = new StringBuilder();
                boolean useDelimitr = false;
                for (Map.Entry entry : (Set<Map.Entry>) (((Map) obj).entrySet())) {
                    String value = getStringValueOfObject(entry.getValue());
                    if (value != null && !"".equals(value)) {
                        valueString.append(useDelimitr ? ", " : "").append(value);
                        useDelimitr = true;
                    }
                }
            } else if (obj instanceof Number) {
                DecimalFormat decimalFormat = ServerUtils.getDecimalFormat(2);
                valueString = new StringBuilder(decimalFormat.format(((Number) obj).doubleValue()));
            } else if (obj instanceof Date) {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime((Date) obj);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                if (hour > 0 || minute > 0) {
                    valueString = new StringBuilder(ServerUtils.getDateAsString((Date) obj, true));
                } else {
                    valueString = new StringBuilder(ServerUtils.getDateAsString((Date) obj));
                }
            } else {
                valueString = new StringBuilder(obj.toString());
            }
        }
        return valueString == null ? "" : valueString.toString();
    }

    public Date getCreationDate() {
        return null;
    }

    public Date getModificationDate() {
        return null;
    }

    public Date getWorkflowStartDate(String workflowStartDate) {
        Date result = null;
        if (Constants.WORKFLOW_START_TIME.TRIGGER_TIME.equals(workflowStartDate)) {
            result = new Date();
        } else if (Constants.WORKFLOW_START_TIME.ENTITY_CREATION_TIME.equals(workflowStartDate)) {
            result = getCreationDate();
        } else if (Constants.WORKFLOW_START_TIME.ENTITY_MODIFICATION_TIME.equals(workflowStartDate)) {
            result = getModificationDate();
        } else if (workflowStartDate != null) {
            result = (Date) getRealValue(workflowStartDate);
        }
        if (result == null) {
            result = new Date();
        }
        return result;
    }

    public Date getWorkflowDueDate(Date startDate, String workflowDueDateGranularity, Integer workflowDueDate) {
        if (startDate == null) {
            startDate = new Date();
        }
        if (workflowDueDateGranularity == null) {
            workflowDueDateGranularity = Constants.TIME_GRANULARITY.HOURS;
        }
        int granularity = workflowDueDateGranularity.equals(Constants.TIME_GRANULARITY.MINUTES) ? Calendar.MINUTE : (workflowDueDateGranularity.equals(Constants.TIME_GRANULARITY.DAYS) ? Calendar.DATE : Calendar.HOUR);
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(granularity, workflowDueDate);
        return c.getTime();
    }

    public void setValueForField(EdsModelField field, Object value) {

    }

    public String getStringValue(String fieldIdString) {
        return getStringValueOfObject(getRealValue(fieldIdString));
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }
}
