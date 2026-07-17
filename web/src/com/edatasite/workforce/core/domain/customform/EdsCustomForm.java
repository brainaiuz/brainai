package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.core.client.form.formbuild.CustomFormItem;
import com.edatasite.workforce.gwt.core.client.form.formbuild.CustomFormRuleItem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "custom_form")
public class EdsCustomForm extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_property")
    private EdsProperty property;

    @Column(name = "form_id")
    private String formID;

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "custom_form_role", joinColumns = @JoinColumn(name = "custom_form_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<EdsRole> roles = new ArrayList<>();


    @Embedded
    private EdsAuditInfo auditInfo;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "memorized_item_id")
    private Integer memorizedItemId;

    @Column(name = "quota_per_user")
    private Integer quotaPerUser;

    @Column(name = "quota_per_form")
    private Integer quotaPerForm;

    @Column(name = "condition_type")
    private String conditionType;

    @Column(name = "range")
    private String range;

    @Column(name = "start_date")
    private Long startDate;

    @Column(name = "end_date")
    private Long endDate;

    @Column(name = "condition_value")
    private Integer conditionValue;

    @Column(name = "attempt")
    private Integer attempt;

    @Column(name = "timer")
    private String timer;

    @Column(name = "timer_started_at")
    private String timerStartedAt;

    @Column(name = "welcome_message")
    private String welcomeMessage;

    @Column(name = "end_time_message")
    private String endTimeMessage;

    @Column(name = "is_quiz")
    private Boolean isQuiz;

    @Column(name = "is_anonymous", columnDefinition = "boolean default false")
    private boolean isAnonymous;


    public EdsProperty getProperty() {
        if (this.property == null) {
            this.property = new EdsProperty();
        }
        return this.property;
    }

    public EdsAuditInfo getAuditInfo() {
        if (this.auditInfo == null) {
            this.auditInfo = new EdsAuditInfo();
        }
        return this.auditInfo;
    }


    public CustomFormItem toRpc(final boolean extended) {
        final CustomFormItem item = new CustomFormItem();
        item.setObjectId(this.getObjectID());
        item.setName(this.getName());
        if (this.getProperty() != null) {
            item.setPlural(this.getProperty().getPlural());
            item.setShortName(this.getProperty().getShortcut());
            item.setContext(this.getProperty().getModuleCode());
            item.setCustom(this.getProperty().getCustom());
            item.setType(this.getProperty().getFormType());
        }
        item.setQuotaPerUser(getQuotaPerUser());
        item.setQuotaPerForm(getQuotaPerForm());
        item.setQuizForm(getQuiz() != null ? getQuiz() : false);
        item.setAnonymousForm(isAnonymous());
        if (getTimer() != null) {
            String[] time = getTimer().split(",");
            int[] timer = {Integer.parseInt(time[0]), Integer.parseInt(time[1])};
            item.setTimer(timer);
        }
        item.setWelcomeMessage(getWelcomeMessage());
        item.setEndTimeMessage(getEndTimeMessage());

        if (getConditionType() == null && getConditionValue() == null && getStartDate() == null && getEndDate() == null && getRange() == null){
            item.setRuleItem(null);
        }else{
            CustomFormRuleItem ruleItem = new CustomFormRuleItem();
            ruleItem.setConditionType(getConditionType());
            ruleItem.setConditionValue(getConditionValue());
            ruleItem.setStartDate(getStartDate());
            ruleItem.setEndDate(getEndDate());
            ruleItem.setRange(getRange());
            item.setRuleItem(ruleItem);
        }
        if (getProperty().getLName() != null) {
            item.setlName(getProperty().getLName().getRPC());
        }
        if (getProperty().getlPlural() != null) {
            item.setlPlural(getProperty().getlPlural().getRPC());
        }
        if (getProperty().getlShort() != null) {
            item.setlShort(getProperty().getlShort().getRPC());
        }

        return item;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperty(EdsProperty property) {
        this.property = property;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public List<EdsRole> getRoles() {
        return roles;
    }

    public void setRoles(List<EdsRole> roles) {
        this.roles = roles;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getMemorizedItemId() {
        return memorizedItemId;
    }

    public void setMemorizedItemId(Integer memorizedItemId) {
        this.memorizedItemId = memorizedItemId;
    }

    public Integer getQuotaPerUser() {
        return quotaPerUser;
    }

    public void setQuotaPerUser(Integer quotaPerUser) {
        this.quotaPerUser = quotaPerUser;
    }

    public Integer getQuotaPerForm() {
        return quotaPerForm;
    }

    public void setQuotaPerForm(Integer quotaPerForm) {
        this.quotaPerForm = quotaPerForm;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Integer getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(Integer conditionValue) {
        this.conditionValue = conditionValue;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getTimerStartedAt() {
        return timerStartedAt;
    }

    public void setTimerStartedAt(String timerStartedAt) {
        this.timerStartedAt = timerStartedAt;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getEndTimeMessage() {
        return endTimeMessage;
    }

    public void setEndTimeMessage(String endTimeMessage) {
        this.endTimeMessage = endTimeMessage;
    }

    public Boolean getQuiz() {
        return isQuiz;
    }

    public void setQuiz(Boolean quiz) {
        this.isQuiz = quiz;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.isAnonymous = anonymous;
    }
}
