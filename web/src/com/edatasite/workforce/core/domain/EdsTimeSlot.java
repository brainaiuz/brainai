package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsAudit;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.enums.HistoryType;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ExceptionalTimeSlotItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * User: Lochin
 * Date: 28.03.2001
 * Time: 0:55:34
 */
//@Indexed
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "timeslot")
public class EdsTimeSlot extends EdsAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Boolean deleted = false;
    private String description;
    private String name;
    @Column(name = "short_name")
    private String shortName;

    @Column(name = "color")
    private String hexColor;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslotid")
    @Where(clause = "exceptionalDate is not null")
    private Set<EdsTimeSlotItem> exceptionalCaseItems = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslotid")
    @Where(clause = "exceptionalDate is null")
    private Set<EdsTimeSlotItem> items = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslotid")
    @Where(clause = "exceptionalDate is null and type = 'DAILY'")
    private Set<EdsTimeSlotItem> dailyItems = new HashSet<>();

    @Column(name = "type")
    private String type;

    @Column(name = "effective_from")
    private Date effectiveFrom;

    @Column(name = "additional_leave_days")
    private String additionalLeaveDays;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localeId")
    private EdsReferenceLocale locale;

    @LazyCollection(LazyCollectionOption.EXTRA)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "timeslot_leave_reasons",
            joinColumns = {@JoinColumn(name = "timeslot_id")},
            inverseJoinColumns = {@JoinColumn(name = "reason_id")}
    )
    private Set<EdsLeaveReason> selectedLeaveReasons = new HashSet<>();

    @Column(name = "late_minutes")
    private Integer lateMinutes;

    @Column(name = "early_leave_minutes")
    private Integer earlyLeaveMinutes;

    private Integer validInStart;
    private Integer validInEnd;

    private Integer validOutStart;
    private Integer validOutEnd;

    @Column(name = "auto_in_out_enabled")
    private Boolean autoInOutEnabled = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (!ServerUtils.equalsString(this.description, description)) {
            addHistoryChange("Description", this.description, description);
        }
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!ServerUtils.equalsString(this.name, name)) {
            addHistoryChange("Name", this.name, name);
        }
        this.name = name;
    }

    public HashMap<Date, EdsTimeSlotItem> getExceptionalTimeSlotItem() {
        HashMap<Date, EdsTimeSlotItem> dateEdsTimeSlotItemHashMap = new HashMap<>();
        Set<EdsTimeSlotItem> itemSet = getExceptionalCaseItems();
        if (itemSet != null && itemSet.size() > 0) {
            for (EdsTimeSlotItem edsTimeSlotItem : itemSet) {
                Date exceptionalDate = edsTimeSlotItem.getExceptionalDate();
                if (exceptionalDate != null && !dateEdsTimeSlotItemHashMap.containsKey(exceptionalDate)) {
                    dateEdsTimeSlotItemHashMap.put(exceptionalDate, edsTimeSlotItem);
                }
            }
        }
        return dateEdsTimeSlotItemHashMap;
    }

    public ArrayList<ExceptionalTimeSlotItem> getExceptionalCaseTimeSlotItems() {
        ArrayList<ExceptionalTimeSlotItem> exceptionalCases = new ArrayList<>();
        Set<EdsTimeSlotItem> exceptionalCaseItems = getExceptionalCaseItems();
        if (exceptionalCaseItems != null && exceptionalCaseItems.size() > 0) {
            ExceptionalTimeSlotItem eTsi;
            for (EdsTimeSlotItem exceptionalTsi : exceptionalCaseItems) {
                eTsi = new ExceptionalTimeSlotItem();
                eTsi.setObjectID(exceptionalTsi.getObjectID());
                eTsi.setName(getName());
                eTsi.setDescription(getDescription());
                eTsi.setExceptionalDate(new DateNonConvertable(exceptionalTsi.getExceptionalDate()));
                eTsi.setWeekDay(new int[]{exceptionalTsi.getStartTime(), exceptionalTsi.getEndTime()});
                eTsi.setLunch(new int[]{exceptionalTsi.getLunchStart(), exceptionalTsi.getLunchEnd()});
                eTsi.setCoffee(new int[]{exceptionalTsi.getCoffeeStart(), exceptionalTsi.getCoffeeEnd()});
                exceptionalCases.add(eTsi);
            }
        }
        return exceptionalCases;
    }

    public ArrayList<ExceptionalTimeSlotItem> getDailyTimeSlotItems() {
        ArrayList<ExceptionalTimeSlotItem> dailyItems = new ArrayList<>();
        Set<EdsTimeSlotItem> dailyItems1 = getDailyItems();
        if (dailyItems1 != null && dailyItems1.size() > 0) {
            ExceptionalTimeSlotItem dailyItem;
            for (EdsTimeSlotItem daily : dailyItems1) {
                dailyItem = new ExceptionalTimeSlotItem();
                dailyItem.setObjectID(daily.getObjectID());
                dailyItem.setDayNo(daily.getDay());
                dailyItem.setExceptionalDate(new DateNonConvertable(daily.getExceptionalDate()));
                dailyItem.setWeekDay(new int[]{daily.getStartTime(), daily.getEndTime()});
                dailyItem.setLunch(new int[]{daily.getLunchStart(), daily.getLunchEnd()});
                dailyItem.setCoffee(new int[]{daily.getCoffeeStart(), daily.getCoffeeEnd()});
                dailyItems.add(dailyItem);
            }
        }
        return dailyItems;
    }

    public TimeslotItem toRpc() {
        TimeslotItem timeslotItem = new TimeslotItem();
        timeslotItem.setDescription(getDescription());
        timeslotItem.setName(getName());
        timeslotItem.setShortName(getShortName());
        timeslotItem.setHexColor(getHexColor());
        timeslotItem.setLocaleItem(getLocale() != null ? getLocale().toRPC() : null);
        timeslotItem.setLateMinutes(getLateMinutes() != null ? getLateMinutes() : 0);

        return timeslotItem;
    }

    public Set<EdsTimeSlotItem> getExceptionalCaseItems() {
        return exceptionalCaseItems;
    }


    public ArrayList<Integer> getAdditionalLeaveDays() {
        String[] leaveDays = additionalLeaveDays != null && !additionalLeaveDays.equals("") ? additionalLeaveDays.split("") : null;
        ArrayList<Integer> days = new ArrayList<>();
        if (leaveDays != null) {
            for (String leaveDay : leaveDays) {
                days.add(Integer.parseInt(leaveDay));
            }
        } else {
            return null;
        }
        return days;
    }

    public void setAdditionalLeaveDays(ArrayList<Integer> additionalLeaveDays) {
        StringBuilder additionalLeaves = new StringBuilder();
        for (Integer dayNumber : additionalLeaveDays) {
            additionalLeaves.append(dayNumber);
        }
        this.additionalLeaveDays = additionalLeaves.toString();
    }

    public void setExceptionalCaseItems(Set<EdsTimeSlotItem> exceptionalCaseItems) {
        this.exceptionalCaseItems = exceptionalCaseItems;
    }

    public Set<EdsTimeSlotItem> getItems() {
        return items;
    }

    public void setItems(Set<EdsTimeSlotItem> items) {
        this.items = items;
    }

    public Set<EdsTimeSlotItem> getDailyItems() {
        return dailyItems;
    }

    public void setDailyItems(Set<EdsTimeSlotItem> dailyItems) {
        this.dailyItems = dailyItems;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(Date effectiveFrom) {
        if (!ServerUtils.equalsDate(this.effectiveFrom, effectiveFrom)) {
            addHistoryChange("Effective From", this.effectiveFrom, effectiveFrom);
        }
        this.effectiveFrom = effectiveFrom;
    }

    @Override
    public HistoryType getHistoryType() {
        return HistoryType.TIMESLOT;
    }

    public EdsReferenceLocale getLocale() {
        return locale;
    }

    public void setLocale(EdsReferenceLocale locale) {
        this.locale = locale;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public Set<EdsLeaveReason> getSelectedLeaveReasons() {
        return selectedLeaveReasons;
    }

    public void setSelectedLeaveReasons(Set<EdsLeaveReason> leaveReasons) {
        this.selectedLeaveReasons = leaveReasons;
    }

    public Integer getLateMinutes() {
        return lateMinutes == null ? 0 : lateMinutes;
    }

    public void setLateMinutes(Integer lateMinutes) {
        this.lateMinutes = lateMinutes;
    }

    public Integer getEarlyLeaveMinutes() {
        return earlyLeaveMinutes == null ? 0 : earlyLeaveMinutes;
    }

    public void setEarlyLeaveMinutes(Integer earlyLeaveMinutes) {
        this.earlyLeaveMinutes = earlyLeaveMinutes;
    }

    public Integer getValidInStart() {
        return validInStart;
    }

    public void setValidInStart(Integer validInStart) {
        this.validInStart = validInStart;
    }

    public Integer getValidInEnd() {
        return validInEnd;
    }

    public void setValidInEnd(Integer validInEnd) {
        this.validInEnd = validInEnd;
    }

    public Integer getValidOutStart() {
        return validOutStart;
    }

    public void setValidOutStart(Integer validOutStart) {
        this.validOutStart = validOutStart;
    }

    public Integer getValidOutEnd() {
        return validOutEnd;
    }

    public void setValidOutEnd(Integer validOutEnd) {
        this.validOutEnd = validOutEnd;
    }

    public Boolean isAutoInOutEnabled() {
        return autoInOutEnabled != null && autoInOutEnabled;
    }

    public void setAutoInOutEnabled(Boolean autoInOutEnabled) {
        this.autoInOutEnabled = autoInOutEnabled;
    }

    public ArrayList<SelectItem> getLeaveReasonsAsSelectItem() {
        ArrayList<SelectItem> list = new ArrayList<>();
        if (selectedLeaveReasons != null && !selectedLeaveReasons.isEmpty()) {
            for (EdsLeaveReason reason : selectedLeaveReasons) {
                if (!reason.getCode().equals("LR_TYPE_RESIGNED") && !reason.getCode().equals("LR_TYPE_HOLIDAY") && !reason.getCode().equals("LR_TYPE_DAY_OFF") && !reason.getCode().equals("TIMESLOT_NOT_STARTED")) {
                    String value = reason.getName() != null ? reason.getName() : reason.getRealName();
                    SelectItem item = new SelectItem(reason.getObjectID(), value, reason.getCode(), reason.getRedirectUrl());
                    String color = reason.getColor();
                    if (color != null) {
                        item.setColorHex(reason.getColor().replace("#", ""));
                    }
                    list.add(item);
                }
            }
        }
        return list;
    }
}
