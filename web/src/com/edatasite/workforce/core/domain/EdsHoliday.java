package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsAudit;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.enums.HistoryType;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
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
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA. User: Lochin Date: 28.03.2001 Time: 1:16:15
 * Software Team
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "holiday")
public class EdsHoliday extends EdsAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;
    @Column(name = "description", length = 3000)
    private String description;
    @Column(name = "date")
    private Date date;
    @Column(name = "enddate")
    private Date endDate;

    @Column(name = "takeAnnual")
    private Boolean takeAnnual = false;

    @Column(name = "dayOff")
    private Boolean dayOff = true;

    private Integer recurrenceID;

    /**
     * The locations in this holiday. A List so we can keep order.
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Where(clause = "deleted = 'false'")
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "holiday_location",
            joinColumns = {@JoinColumn(name = "holiday_id")},
            inverseJoinColumns = {@JoinColumn(name = "locations_id")}
    )
    private List<EdsLocation> locations = new ArrayList<>();

    @Column(name = "allDay")
    private Boolean allDay = false;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private Boolean deleted = false;

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
        if (!ServerUtils.equalsString(this.name, name)) {
            addHistoryChange("Name", this.name, name);
        }
        this.name = name;
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

    public Date getStartDate() {
        return date;
    }

    public void setStartDate(Date date) {
        if (!ServerUtils.equalsDate(this.date, date)) {
            addHistoryChange("Date", this.date, date);
        }
        this.date = date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (!ServerUtils.equalsDate(this.endDate, endDate)) {
            addHistoryChange("End Date", this.endDate, endDate);
        }
        this.endDate = endDate;
    }

    public Boolean isTakeAnnual() {
        return takeAnnual != null && takeAnnual;
    }

    public void setTakeAnnual(Boolean takeAnnual) {
        if (!Objects.equals(this.takeAnnual, takeAnnual)) {
            addHistoryChange("Take from annual", this.takeAnnual, takeAnnual);
        }
        this.takeAnnual = takeAnnual;
    }

    public Boolean isDayOff() {
        return dayOff;
    }

    public void setDayOff(Boolean dayOff) {
        if (!Objects.equals(this.dayOff, dayOff)) {
            addHistoryChange("Day Off", this.dayOff, dayOff);
        }
        this.dayOff = dayOff;
    }

    public Integer getRecurrenceID() {
        return recurrenceID;
    }

    public void setRecurrenceID(Integer recurrenceID) {
        this.recurrenceID = recurrenceID;
    }

    public List<EdsLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<EdsLocation> locations) {
        this.locations = locations;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public HistoryType getHistoryType() {
        return HistoryType.HOLIDAY;
    }

    public HolidayItem getRPC() {
        HolidayItem result = new HolidayItem();
        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setDescription(getDescription());
        result.setFrom(getStartDate() != null ? new DateNonConvertable(new Date(getStartDate().getTime())) : null);
        result.setTo(getEndDate() != null ? new DateNonConvertable(new Date(getEndDate().getTime())) : null);
        result.setAllDay(getAllDay() != null ? getAllDay() : false);
        result.setDayOff(isDayOff());
        result.setRepeat(getRecurrenceID() != null);
		List<EdsLocation> locationList = getLocations();
		StringBuilder location_s = new StringBuilder();
		if (locationList != null && locationList.size() > 0) {
			boolean isFirst = true;
			for (EdsLocation lo : locationList) {
				if (isFirst) {
					location_s.append(lo.getCountry().getName() + "," + lo.getCity());
					isFirst = false;
				} else {
					location_s.append("; " + lo.getCountry().getName() + "," + lo.getCity());
				}
			}
		}
		result.setLocationName(!"".equals(location_s.toString()) ? location_s.toString() : "N/A");
        return result;
    }
}
