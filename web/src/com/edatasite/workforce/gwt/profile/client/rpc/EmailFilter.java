package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Mar 19, 2010
 * Time: 4:45:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailFilter implements IsSerializable {

    public final static String NAME = "NAME";
    public final static String TYPE = "TYPE";
    public final static String ISPARENT = "ISPARENT";

    public final static String CREATE_CASE = "CREATE_CASE";
    public final static String CREATE_TASK = "CREATE_TASK";
    public final static String CREATE_LEAD = "CREATE_LEAD";
    public final static String CREATE_CRMACCOUNT = "CREATE_CRMACCOUNT";
    public final static String CREATE_CONTACT = "CREATE_CONTACT";

    private final static String DD_EMAIL_FILTERS = "EMAIL_FILTERS";
    private final static String DD_EMAIL_TEMPLATES = "EMAIL_TEMPLATES";
    private final static String DD_PROJECTS = "PROJECTS";
    public final static String ID_PROJECT_TEMPLATE = "ID_PROJECT_TEMPLATE";
    public final static String ID_EMAIL_TEMPLATE = "ID_EMAIL_TEMPLATE";
    public final static String ID_ASSIGNEE = "ID_ASSIGNEE";
    public final static String ID_DEPARTMENT = "ID_DEPARTMENT";
    public final static String ID_SEND_AUTORESPONSE = "ID_SEND_AUTORESPONSE";
    public final static String ID_RESOLVER = "ID_RESOLVER";
    public final static String ID_TOTRASH = "ID_TOTRASH";

    private SelectItem parent;
    private ArrayList<EmailFilter> children;
    private SelectItem action;
    private ArrayList<String> rules;
    private Integer objectID;
    private boolean isParent;
    private boolean isRule;
    private String type;
    private String name;
    private ArrayList<EmailFilter> subFilters = new ArrayList<>();
    private HashMap<String, SelectItem[]> dropdownItems = new HashMap<>();
    private HashMap<String, Integer> objectIDs = new HashMap<>();
    private HashMap<String, String> names = new HashMap<>();
    private ArrayList<RelationItem> relationItems = new ArrayList<>();
    private boolean projectTemplateEnabled;

    public EmailFilter() {

    }

    public SelectItem getParent() {
        return parent;
    }

    public void setParent(SelectItem parent) {
        this.parent = parent;
    }

    public ArrayList<EmailFilter> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void addChild(EmailFilter child) {
        getChildren().add(child);
    }

    public void setChildren(ArrayList<EmailFilter> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SelectItem getAction() {
        return action;
    }

    public void setAction(SelectItem action) {
        this.action = action;
    }

    public ArrayList<String> getRules() {
        return rules;
    }

    public void setRules(ArrayList<String> rules) {
        this.rules = rules;
    }

    public String getRuleAsString(SelectItem emailPart, SelectItem emailAction, String searchText, Date date, SelectItem operator) {
        FilterRule rule = new FilterRule();
        rule.setEmailPart(emailPart);
        rule.setEmailAction(emailAction);
        rule.setWord(searchText != null && !"".equals(searchText) ? searchText : DateUtils.format(date));
        rule.setOperator(operator);
        return rule.toString();
    }

    public String getRulesAsString() {
        StringBuilder s = new StringBuilder();
        if (getRules() != null && getRules().size() > 0) {
            for (String rule : getRules()) {
                if (rule != null && !"".equals(rule)) {
                    s.append(s.toString().equals("") ? "" : EmailAccountItem.ROW_DELIMITR).append(rule);
                }
            }
        }
        return s.toString();
    }

    public FilterRule getRuleFromString(String rule) {
        if (rule != null && !"".equals(rule) && rule.contains(EmailAccountItem.DELIMITR)) {
            return new FilterRule(rule);
        }
        return null;
    }

    public ArrayList<FilterRule> getRulesFromString() {
        ArrayList<FilterRule> ruleList = new ArrayList<>();
        if (rules != null && rules.size() > 0) {
            for (String rule : rules) {
                if (rule != null && !"".equals(rule) && rule.contains(EmailAccountItem.DELIMITR)) {
                    ruleList.add(new FilterRule(rule));
                }
            }
        }
        return ruleList;
    }

    public SelectItem[] getEmailTemplates() {
        return dropdownItems.get(DD_EMAIL_TEMPLATES);
    }

    public void setEmailTemplates(SelectItem[] items) {
        dropdownItems.put(DD_EMAIL_TEMPLATES, items);
    }

    public SelectItem[] getEmailFilters() {
        return dropdownItems.get(DD_EMAIL_FILTERS);
    }

    public void setEmailFilters(SelectItem[] items) {
        dropdownItems.put(DD_EMAIL_FILTERS, items);
    }

    public SelectItem[] getProjects() {
        return dropdownItems.get(DD_PROJECTS);
    }

    public void setProjects(SelectItem[] items) {
        dropdownItems.put(DD_PROJECTS, items);
    }

    public String getAssigneeName() {
        return names.get(ID_ASSIGNEE);
    }

    public void setAssigneeName(String name) {
        names.put(ID_ASSIGNEE, name);
    }

    public String getDepartmentName() {
        return names.get(ID_DEPARTMENT);
    }

    public void setDepartmentName(String name) {
        names.put(ID_DEPARTMENT, name);
    }

    public String getResolverName() {
        return names.get(ID_RESOLVER);
    }

    public void setResolverName(String name) {
        names.put(ID_RESOLVER, name);
    }

    public Integer getEmailTemplateID() {
        return objectIDs.get(ID_EMAIL_TEMPLATE);
    }

    public void setEmailTemplateID(Integer objectID) {
        objectIDs.put(ID_EMAIL_TEMPLATE, objectID);
    }

    public Integer getProjectTemplateID() {
        return objectIDs.get(ID_PROJECT_TEMPLATE);
    }

    public void setProjectTemplateID(Integer objectID) {
        objectIDs.put(ID_PROJECT_TEMPLATE, objectID);
    }

    public Integer getAssigneeID() {
        return objectIDs.get(ID_ASSIGNEE);
    }

    public void setAssigneeID(Integer objectID) {
        objectIDs.put(ID_ASSIGNEE, objectID);
        if (objectID == null) {
            objectIDs.remove(ID_ASSIGNEE);
            names.remove(ID_ASSIGNEE);
        }
    }

    public Integer getDepartmentID() {
        return objectIDs.get(ID_DEPARTMENT);
    }

    public void setDepartmentID(Integer objectID) {
        objectIDs.put(ID_DEPARTMENT, objectID);
        if (objectID == null) {
            objectIDs.remove(ID_DEPARTMENT);
            names.remove(ID_DEPARTMENT);
        }
    }

    public void setSendAutoresponse(Boolean value) {
        objectIDs.put(ID_SEND_AUTORESPONSE, value ? 1 : 0);
    }

    public Boolean isToTrash() {
        return objectIDs.get(ID_TOTRASH) != null && objectIDs.get(ID_TOTRASH).equals(1);
    }

    public void setToTrash(Boolean value) {
        objectIDs.put(ID_TOTRASH, value ? 1 : 0);
    }

    public Integer getResolverID() {
        return objectIDs.get(ID_RESOLVER);
    }

    public void setResolverID(Integer objectID) {
        objectIDs.put(ID_RESOLVER, objectID);
    }

    public void addSubFilter(EmailFilter rpc) {
        if (!getSubFilters().contains(rpc)) {
            getSubFilters().add(rpc);
        }
    }

    public String getDefaultActions() {
        if (objectIDs.size() == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (HashMap.Entry<String, Integer> entry : objectIDs.entrySet()) {
            if (entry.getValue() != null) {
                result.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
            }
        }
        return result.toString();
    }

    public void setProjectTemplateEnabled(boolean projectTemplateEnabled) {
        this.projectTemplateEnabled = projectTemplateEnabled;
    }

    public boolean isProjectTemplateEnabled() {
        return projectTemplateEnabled;
    }

    public interface FILTER {
        interface RULE {
            SelectItem[] parts = new SelectItem[]{EMAIL_PARTS.SENDER, EMAIL_PARTS.RECIPIENT, EMAIL_PARTS.SUBJECT, EMAIL_PARTS.RECEIVED_DATE};

            interface EMAIL_ACTIONS {
                SelectItem CONTAINS = new SelectItem(1, "Contains");
                SelectItem NOT_CONTAINS = new SelectItem(2, "Does not contain");
                SelectItem MATCHES = new SelectItem(3, "Matches");
                SelectItem NOT_MATCHES = new SelectItem(4, "Does not match");
            }

            SelectItem[] receivedDateActions = new SelectItem[]{EMAIL_RECEIVED_DATE_ACTIONS.BEFORE, EMAIL_RECEIVED_DATE_ACTIONS.AFTER};

            interface OPERATORS {
                SelectItem AND = new SelectItem(1, "And");
                SelectItem OR = new SelectItem(2, "Or");
            }

            interface EMAIL_PARTS {
                SelectItem SENDER = new SelectItem(1, "Sender");
                SelectItem RECIPIENT = new SelectItem(2, "Recipient");
                SelectItem SUBJECT = new SelectItem(3, "Subject");
                SelectItem RECEIVED_DATE = new SelectItem(4, "Received date");
            }

            SelectItem[] actions = new SelectItem[]{EMAIL_ACTIONS.CONTAINS, EMAIL_ACTIONS.NOT_CONTAINS, EMAIL_ACTIONS.MATCHES, EMAIL_ACTIONS.NOT_MATCHES};

            interface EMAIL_RECEIVED_DATE_ACTIONS {
                SelectItem BEFORE = new SelectItem(5, "Before");
                SelectItem AFTER = new SelectItem(6, "After");
            }

            SelectItem[] operators = new SelectItem[]{OPERATORS.AND, OPERATORS.OR};
        }

        interface ACTIONS {
            SelectItem ASSIGN_TO = new SelectItem(1, "Assign To");
            SelectItem MOVE_TO = new SelectItem(2, "Move To");
            SelectItem REMOVE = new SelectItem(3, "Remove");
        }

        interface ACTIONS_TOS {
            SelectItem TRASH = new SelectItem(1, "Trash");
        }


        SelectItem[] actions = new SelectItem[]{ACTIONS.ASSIGN_TO, ACTIONS.MOVE_TO, ACTIONS.REMOVE};
    }

    private SelectItem getSelectItemByID(SelectItem[] selectItems, Integer selectedID) {
        if (selectItems != null && selectItems.length > 0 && selectedID != null) {
            for (SelectItem selectItem : selectItems) {
                if (selectedID.equals(selectItem.getId())) {
                    return selectItem;
                }
            }
        }
        return null;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public boolean isRule() {
        return isRule;
    }

    public void setRule(boolean rule) {
        isRule = rule;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<EmailFilter> getSubFilters() {
        if (subFilters == null) {
            subFilters = new ArrayList<>();
        }
        return subFilters;
    }

    public void setSubFilters(ArrayList<EmailFilter> subFilters) {
        this.subFilters = subFilters;
    }

    public ArrayList<RelationItem> getRelationItems() {
        return relationItems;
    }

    public void setRelationItems(ArrayList<RelationItem> relationItems) {
        this.relationItems = relationItems;
    }

    public class FilterRule {
        private SelectItem emailPart;
        private SelectItem emailAction;
        private String word;
        private SelectItem operator;

        public FilterRule() {

        }

        public FilterRule(String rule) {
            this();
            fromString(rule);
        }

        public SelectItem getEmailPart() {
            return emailPart;
        }

        public void setEmailPart(SelectItem emailPart) {
            this.emailPart = emailPart;
        }

        public SelectItem getEmailAction() {
            return emailAction;
        }

        public void setEmailAction(SelectItem emailAction) {
            this.emailAction = emailAction;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public SelectItem getOperator() {
            return operator;
        }

        public void setOperator(SelectItem operator) {
            this.operator = operator;
        }

        @Override
        public String toString() {
            String str = "";
            String delimitr = EmailAccountItem.DELIMITR;
            str += emailPart.getId() + delimitr + emailAction.getId() + delimitr + word + delimitr + (operator != null ? operator.getId() : "");
            return str;
        }

        private void fromString(String rule) {
            if (rule != null && !"".equals(rule)) {
                String[] columnsOfRule = rule.split(EmailAccountItem.DELIMITR);
                if (!(columnsOfRule.length < 3)) {
                    Integer emailPart = Integer.parseInt(columnsOfRule[0]);
                    Integer emailAction = Integer.parseInt(columnsOfRule[1]);
                    StringBuilder word = new StringBuilder();
                    Integer operator = null;
                    if (columnsOfRule.length > 3) {
                        int indicatorOfOperator = rule.endsWith(EmailAccountItem.DELIMITR) ? 1 : 0;
                        operator = indicatorOfOperator == 1 ? null : Integer.parseInt(columnsOfRule[columnsOfRule.length - 1 + indicatorOfOperator]);
                        for (int i = 2; i < columnsOfRule.length - 1 + indicatorOfOperator; i++) {
                            word.append(columnsOfRule[i]);
                        }
                    } else {
                        word = new StringBuilder(columnsOfRule[2]);
                    }
                    this.word = word.toString();
                    this.emailPart = getSelectItemByID(FILTER.RULE.parts, emailPart);
                    if (emailAction > 4) {
                        this.emailAction = getSelectItemByID(FILTER.RULE.receivedDateActions, emailAction);
                    } else {
                        this.emailAction = getSelectItemByID(FILTER.RULE.actions, emailAction);
                    }
                    this.operator = getSelectItemByID(FILTER.RULE.operators, operator);
                }
            }
        }

        public boolean isOperator(SelectItem operator) {
            return (operator == null && getOperator() == null) || (getOperator() != null && operator != null && getOperator().getId() != null && getOperator().getId().equals(operator.getId()));
        }
    }
}