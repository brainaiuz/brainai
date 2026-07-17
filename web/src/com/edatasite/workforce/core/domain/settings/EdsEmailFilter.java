package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.profile.client.rpc.EmailFilter;
import jakarta.mail.Message;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.FromStringTerm;
import jakarta.mail.search.NotTerm;
import jakarta.mail.search.OrTerm;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.RecipientStringTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Apr 30, 2010
 * Time: 7:17:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "emailfilters")
public class EdsEmailFilter extends EdsObject {
    public final static String CREATE_CASE = EmailFilter.CREATE_CASE;
    public final static String CREATE_TASK = EmailFilter.CREATE_TASK;
    public final static String CREATE_LEAD = EmailFilter.CREATE_LEAD;
    public final static String CREATE_CRMACCOUNT = EmailFilter.CREATE_CRMACCOUNT;
    public final static String CREATE_CONTACT = EmailFilter.CREATE_CONTACT;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Type(type = "text")
    private String rules;

    private String name;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private Boolean deleted = false;

    @Column(name = "isparent", columnDefinition = " boolean default false ")
    private Boolean isParent = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private EdsEmailFilter parent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Where(clause = " (deleted = 'false' or deleted is null) ")
    private Set<EdsEmailFilter> subFilters = new HashSet<>();

    private String type;

    @Column(name = "defaultactions")
    @Type(type = "text")
    private String parametrs;

    @Column(name = "isrule", columnDefinition = "boolean default false")
    private boolean isRule = false;

    @Transient
    SearchTerm searchTerm;

    @Transient
    boolean isDefaultActionsAddedToMap = false;

    @Transient
    Map<String, Integer> objectIDs = new HashMap<>();


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRules() {
        return rules;
    }

    public EdsEmailFilter getParent() {
        return parent;
    }

    public void setParent(EdsEmailFilter parent) {
        this.parent = parent;
    }

    public Boolean isParent() {
        return isParent == null ? Boolean.FALSE : isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public Set<EdsEmailFilter> getSubFilters() {
        return subFilters;
    }

    public void setSubFilters(Set<EdsEmailFilter> subFilters) {
        this.subFilters = subFilters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SearchTerm getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(SearchTerm searchTerm) {
        this.searchTerm = searchTerm;
    }

    public boolean isRule() {
        return isRule;
    }

    public void setRule(boolean rule) {
        isRule = rule;
    }

    public ArrayList<String> getRulesAsList() {
        ArrayList<String> rules = new ArrayList<>();
        if (getRules() != null && !"".equals(getRules())) {
            if (getRules().contains(EmailAccountItem.ROW_DELIMITR)) {
                for (String rule : getRules().split(EmailAccountItem.ROW_DELIMITR)) {
                    if (!rule.isEmpty() && !rule.trim().isEmpty() && !rules.contains(rule)) {
                        rules.add(rule);
                    }
                }
            } else {
                rules.add(getRules());
            }
        }
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Boolean isDeleted() {
        return deleted == null ? false : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getParametrs() {
        return parametrs;
    }

    public void setParametrs(String parametrs) {
        this.parametrs = parametrs;
    }

    public EmailFilter getRPC(EmailFilter item) {
        if (item == null) {
            item = new EmailFilter();
        }
        item.setObjectID(getObjectID());
        item.setRules(getRulesAsList());
        item.setParent(getParent() != null ? getParent().getAsSelectItem() : null);
        item.setType(getType());
        item.setName(getName());
        item.setParent(isParent());
        item.setEmailTemplateID(getEmailTemplateID());
        item.setProjectTemplateID(getProjectTemplateID());
        item.setResolverID(getResolverID());
        item.setAssigneeID(getAssigneeID());
        item.setDepartmentID(getDepartmentID());
        item.setSendAutoresponse(isSendAutoresponse());
        item.setToTrash(isToTrash());
        if (getSubFilters() != null && getSubFilters().size() > 0) {
            for (EdsEmailFilter subFilter : getSubFilters()) {
                item.addSubFilter(subFilter.getRPC(null));
            }
        }
        return item;
    }

    private void initDefaultActions() {
        if (isDefaultActionsAddedToMap) {
            return;
        }
        isDefaultActionsAddedToMap = true;
        if (getParametrs() != null && !"".equals(getParametrs())) {
            String[] ids = getParametrs().split(";");
            if (ids != null && ids.length > 0) {
                for (String keyValue : ids) {
                    if (keyValue != null && !"".equals(keyValue)) {
                        String[] keyValue_ = keyValue.split(":");
                        if (keyValue_ != null && keyValue_.length > 0) {
                            objectIDs.put(keyValue_[0], Integer.valueOf(keyValue_[1]));
                        }
                    }
                }
            }
        }
    }

    public Integer getEmailTemplateID() {
        initDefaultActions();
        return objectIDs.get(EmailFilter.ID_EMAIL_TEMPLATE);
    }

    public void setEmailTemplateID(Integer objectID) {
        objectIDs.put(EmailFilter.ID_EMAIL_TEMPLATE, objectID);
    }

    public Integer getProjectTemplateID() {
        initDefaultActions();
        return objectIDs.get(EmailFilter.ID_PROJECT_TEMPLATE);
    }

    public void setProjectTemplateID(Integer objectID) {
        objectIDs.put(EmailFilter.ID_PROJECT_TEMPLATE, objectID);
    }

    public Integer getAssigneeID() {
        initDefaultActions();
        return objectIDs.get(EmailFilter.ID_ASSIGNEE);
    }

    public void setAssigneeID(Integer objectID) {
        objectIDs.put(EmailFilter.ID_ASSIGNEE, objectID);
    }

    public Integer getDepartmentID() {
        initDefaultActions();
        return objectIDs.get(EmailFilter.ID_DEPARTMENT);
    }

    public void setDepartmentID(Integer objectID) {
        objectIDs.put(EmailFilter.ID_DEPARTMENT, objectID);
    }

    public Integer getResolverID() {
        initDefaultActions();
        return objectIDs.get(EmailFilter.ID_RESOLVER);
    }

    public void setResolverID(Integer objectID) {
        objectIDs.put(EmailFilter.ID_RESOLVER, objectID);
    }

    public Boolean isSendAutoresponse() {
        initDefaultActions();
        return objectIDs.get(EmailFilter.ID_SEND_AUTORESPONSE) != null && objectIDs.get(EmailFilter.ID_SEND_AUTORESPONSE).equals(1);
    }

    public void setAutoresponseID(Integer objectID) {
        objectIDs.put(EmailFilter.ID_SEND_AUTORESPONSE, objectID);
    }

    public Boolean isToTrash() {
        initDefaultActions();
        return objectIDs.get(EmailFilter.ID_TOTRASH) != null && objectIDs.get(EmailFilter.ID_TOTRASH).equals(1);
    }

    public void setToTrash(Integer objectID) {
        objectIDs.put(EmailFilter.ID_TOTRASH, objectID);
    }

    private static List<EmailFilter.FilterRule> reGroupORs(List<EmailFilter.FilterRule> rules) {
        List<EmailFilter.FilterRule> ors = new ArrayList<>();
        if (rules != null && rules.size() > 0) {
            EmailFilter.FilterRule lastFilter = null;
            for (EmailFilter.FilterRule rule : rules) {
                if (rules.size() == 1 || ((lastFilter == null || !lastFilter.isOperator(EmailFilter.FILTER.RULE.OPERATORS.AND)) && !rule.isOperator(EmailFilter.FILTER.RULE.OPERATORS.AND))) {
                    ors.add(rule);
                }
                lastFilter = rule;
            }
        }
        return ors;
    }

    private static Map<Integer, List<EmailFilter.FilterRule>> reGroupANDs(List<EmailFilter.FilterRule> rules) {
        Map<Integer, List<EmailFilter.FilterRule>> ands = new HashMap<>();
        if (rules != null && rules.size() > 0) {
            EmailFilter.FilterRule lastRule = null;
            int index = ands.size();
            boolean addedAndRule = false;
            for (EmailFilter.FilterRule rule : rules) {
                if (rule.isOperator(EmailFilter.FILTER.RULE.OPERATORS.AND) || (lastRule != null && lastRule.isOperator(EmailFilter.FILTER.RULE.OPERATORS.AND))) {
                    if (!ands.containsKey(index)) {
                        ands.put(index, new ArrayList<>());
                    }
                    ands.get(index).add(rule);
                    if (!rule.isOperator(EmailFilter.FILTER.RULE.OPERATORS.AND) && addedAndRule) {
                        index++;
                        addedAndRule = false;
                    } else {
                        addedAndRule = true;
                    }
                }
                lastRule = rule;
            }
        }
        return ands;
    }

    public SearchTerm asSearchTerm() {
        if (getSearchTerm() == null) {
            SearchTerm searchTerm = null;
            EmailFilter emailFilter = getRPC(null);
            SearchTerm orTerms = null;
            SearchTerm andTerms = null;
            List<EmailFilter.FilterRule> orRules = reGroupORs(emailFilter.getRulesFromString());
            if (orRules != null && orRules.size() > 0) {
                orTerms = getOrTerms(orRules);
            }
            List<List<EmailFilter.FilterRule>> andRules = new ArrayList<>();
            Map<Integer, List<EmailFilter.FilterRule>> andsMap = reGroupANDs(emailFilter.getRulesFromString());
            if (andsMap != null && andsMap.size() > 0) {
                for (Map.Entry<Integer, List<EmailFilter.FilterRule>> entry : andsMap.entrySet()) {
                    if (entry.getValue() != null && entry.getValue().size() > 0) {
                        andRules.add(entry.getValue());
                    }
                }
            }
            if (andRules.size() > 0) {
                andTerms = getAndTerms(andRules);
            }
            if (orTerms != null && andTerms != null) {
                searchTerm = new OrTerm(orTerms, andTerms);
            } else if (orTerms != null) {
                searchTerm = orTerms;
            } else {
                searchTerm = andTerms;
            }
            setSearchTerm(searchTerm);
        }
        return getSearchTerm();
    }

    private SearchTerm getAndTerms(List<List<EmailFilter.FilterRule>> andRules) {
        List<AndTerm> andTerms = new ArrayList<>();
        if (andRules.size() > 0) {
            for (List<EmailFilter.FilterRule> andRule : andRules) {
                if (andRule != null) {
                    AndTerm andTerm = getAndTerm(andRule);
                    if (andTerm != null) {
                        andTerms.add(andTerm);
                    }
                }
            }
        }
        if (andTerms.size() > 0) {
            return new AndTerm(andTerms.toArray(new AndTerm[]{}));
        }
        return null;
    }

    private AndTerm getAndTerm(List<EmailFilter.FilterRule> rules) {
        if (rules != null && rules.size() > 0) {
            List<SearchTerm> terms = new ArrayList<>();
            for (EmailFilter.FilterRule rule : rules) {
                SearchTerm term = getAsTerm(rule);
                if (term != null) {
                    terms.add(term);
                }
            }
            if (terms.size() > 0) {
                return new AndTerm(terms.toArray(new SearchTerm[]{}));
            }
        }
        return null;
    }

    private SearchTerm getOrTerms(List<EmailFilter.FilterRule> rules) {
        List<SearchTerm> orTerms = new ArrayList<>();
        if (rules != null && rules.size() > 0) {
            for (EmailFilter.FilterRule rule : rules) {
                SearchTerm term = getAsTerm(rule);
                if (term != null) {
                    orTerms.add(term);
                }
            }
        }
        if (orTerms.size() > 0) {
            return new OrTerm(orTerms.toArray(new SearchTerm[]{}));
        }
        return null;
    }

    private SearchTerm getAsTerm(EmailFilter.FilterRule rule) {
        SearchTerm term = null;
        if (rule.getEmailPart() != null && rule.getEmailAction() != null) {
            SelectItem emailPart = rule.getEmailPart();
            SelectItem emailAction = rule.getEmailAction();
            String word = rule.getWord();
            if (emailPart != null && emailPart.getId() != null) {
                word = matchOrContain(emailAction, word);
                if (EmailFilter.FILTER.RULE.EMAIL_PARTS.SENDER.getId().equals(emailPart.getId())) {
                    term = new FromStringTerm(word);
                } else if (EmailFilter.FILTER.RULE.EMAIL_PARTS.RECIPIENT.getId().equals(emailPart.getId())) {
                    term = new OrTerm(new SearchTerm[]{new RecipientStringTerm(Message.RecipientType.TO, word), new RecipientStringTerm(Message.RecipientType.CC, word), new RecipientStringTerm(Message.RecipientType.BCC, word)});
                } else if (EmailFilter.FILTER.RULE.EMAIL_PARTS.SUBJECT.getId().equals(emailPart.getId())) {
                    term = new SubjectTerm(word);
                } else if (EmailFilter.FILTER.RULE.EMAIL_PARTS.RECEIVED_DATE.getId().equals(emailPart.getId())) {
                    try {
                        term = new ReceivedDateTerm(EmailFilter.FILTER.RULE.EMAIL_RECEIVED_DATE_ACTIONS.BEFORE.getId().equals(rule.getEmailAction().getId()) ? 2 : 5, DateUtils.parse(word));
                    } catch (Exception ignored) {
                    }
                }
                boolean not = EmailFilter.FILTER.RULE.EMAIL_ACTIONS.NOT_CONTAINS.getId().equals(emailAction.getId()) || EmailFilter.FILTER.RULE.EMAIL_ACTIONS.NOT_MATCHES.getId().equals(emailAction.getId());
                if (not) {
                    return new NotTerm(term);
                }
            }
        }
        return term;
    }

    private String matchOrContain(SelectItem emailAction, String word) {
        if (emailAction != null) {
            if (EmailFilter.FILTER.RULE.EMAIL_ACTIONS.MATCHES.equals(emailAction.getId()) || EmailFilter.FILTER.RULE.EMAIL_ACTIONS.NOT_MATCHES.equals(emailAction.getId())) {
                word = "^" + word + "$";
            }
        }
        return word;
    }

    public static ArrayList<SelectItem> asSelectItems(List<EdsEmailFilter> parentsOnly) {
        ArrayList<SelectItem> items = new ArrayList<>();
        if (parentsOnly != null && parentsOnly.size() > 0) {
            for (EdsEmailFilter filter : parentsOnly) {
                items.add(filter.getAsSelectItem());
            }
        }
        return items;
    }
}
