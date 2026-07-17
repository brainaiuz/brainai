package com.finnetlimited.reportservice.core.server.domain.schema;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsTelegramChat;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * User: Sardorbek Khalimnboev
 * Date: 11-June-2021
 * Time: 14:57:33
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "telegram_reporting_schedule_rule")
public class EdsTelegramReportingScheduleRule extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectId;

    @Column(name = "rule_name")
    @Basic(optional = false)
    private String name;

    @Column(name = "bot_id")
    private Integer botId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "telegram_rule_chat",
            joinColumns = {@JoinColumn(name = "rule_id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_id")}
    )
    private Set<EdsTelegramChat> chats = new HashSet<>();

    private boolean isActive;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "recurrence_id")
    private Integer recurrenceId;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private EdsReport edsReport;

    @Column(name = "locale")
    private Locale locale;

    public EdsTelegramReportingScheduleRule() {
    }

    public EdsTelegramReportingScheduleRule(String name, Integer botId, Set<EdsTelegramChat> chats, boolean isActive, String message, Integer recurrenceId, EdsReport edsReport, Locale locale) {
        this.name = name;
        this.botId = botId;
        this.chats = chats;
        this.isActive = isActive;
        this.message = message;
        this.recurrenceId = recurrenceId;
        this.edsReport = edsReport;
        this.locale = locale;
    }

    @Override
    public Integer getObjectID() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EdsReport getEdsReport() {
        return edsReport;
    }

    public void setEdsReport(EdsReport edsReport) {
        this.edsReport = edsReport;
    }

    public Set<EdsTelegramChat> getChats() {
        return chats;
    }

    public void setChats(Set<EdsTelegramChat> chats) {
        this.chats = chats;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getBotId() {
        return botId;
    }

    public void setBotId(Integer botId) {
        this.botId = botId;
    }

    public Integer getRecurrenceId() {
        return recurrenceId;
    }

    public void setRecurrenceId(Integer recurrenceId) {
        this.recurrenceId = recurrenceId;
    }
}
