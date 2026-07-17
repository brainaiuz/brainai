package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Lochin
 * Date: 28.03.2001
 * Time: 1:21:16
 * Software Team
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeetask",
        indexes = {
                @Index(columnList = "projectEmployeeId", name="employeeTask_projectEmployeeId_idx"),
                @Index(columnList = "taskId", name="employeeTask_taskId_idx")
        })
public class EdsEmployeeTask extends EdsObject implements PropertyChangeListener, CalendarObject {
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_PERCENT = "percent";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "projectEmployeeId")
    private EdsProjectEmployee projectEmployee;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId")
    private EdsTask task;

    private Date startDate;
    private Date endDate;

    /**
     * Employee's Planned time in this task
     */
    private Integer estimatedTime;


    /**
     * Employee's actual time spent in this task
     */
    private Integer timeSpent;

    @Column(precision = 25, scale = 5)
    private BigDecimal taskAmount;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    private EdsReference status;
    private Float percent;

    private Boolean newTask = false;

    private Date actualStartDate;
    private Date actualEndDate;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeetaskId")
    @OrderBy("date ASC")
    private List<EdsTimeSheet> timeSheets = new ArrayList<>();

    @Transient
    private String timeSpentHM;

    @Transient
    private String completedby;

    private Boolean deleted = false;

    /**
     * Employee Planned time/(task start and end date days division)
     */
    private Integer dailyLoad;

    @Column(name = "googleId", length = 500)
    private String googleID;

    @Column(name = "officeid")
    @Type(type = "text")
    private String officeID;

    private Date lastModifiedDate;

    private Date completedDate;

    private Date closedDate;

    public EdsEmployeeTask() {

    }

    public interface ChangeListener {
        void onStatusChange(EdsReference value);

        void onPercentChange(Float percent);

    }

    private transient PropertyChangeSupport propertyChangeSupport;

    private transient ChangeListener changeListener;

    public void enableTaskChangeListener(ChangeListener changeListener) {
        if (changeListener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }
        this.changeListener = changeListener;
        propertyChangeSupport = new PropertyChangeSupport(this);
        propertyChangeSupport.addPropertyChangeListener(this);
    }

    public void disableTaskChangeListener() {
        this.changeListener = null;
        propertyChangeSupport.removePropertyChangeListener(this);
        this.propertyChangeSupport = null;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(FIELD_STATUS)) {
            changeListener.onStatusChange((EdsReference) evt.getNewValue());

        }
        if (evt.getPropertyName().equals(FIELD_PERCENT)) {
            changeListener.onPercentChange(this.percent);
        }
    }

    public List<EdsTimeSheet> getTimeSheets() {
        return timeSheets;
    }

    public void setTimeSheets(List<EdsTimeSheet> timeSheets) {
        this.timeSheets = timeSheets;
    }

    public EdsEmployeeTask(EdsTask task, EdsProjectEmployee projectEmployee) {
        this.task = task;
        this.projectEmployee = projectEmployee;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsProjectEmployee getProjectEmployee() {
        return projectEmployee;
    }

    public void setProjectEmployee(EdsProjectEmployee employee) {
        this.projectEmployee = employee;
    }

    public EdsTask getTask() {
        return task;
    }

    public void setTask(EdsTask task) {
        this.task = task;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getTimeSpent() {
        if (timeSpent != null) {
            return timeSpent;
        } else {
            return 0;
        }
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        EdsReference oldValue = this.status;
        this.status = status;
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange(FIELD_STATUS, oldValue, this.status);
        }
    }

    public Float getPercent() {
        return percent != null ? new BigDecimal(percent).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() : 0f;
    }

    public void setPercent(Float percent) {
        if (percent != null) {
            BigDecimal percentDecimal = new BigDecimal(percent).setScale(2, BigDecimal.ROUND_HALF_UP);
            Float oldValue = this.percent;
            this.percent = percentDecimal.floatValue();
            if (propertyChangeSupport != null) {
                propertyChangeSupport.firePropertyChange(FIELD_PERCENT, oldValue, this.percent);
            }
        } else {
            Float oldValue = this.percent;
            this.percent = percent;
            if (propertyChangeSupport != null) {
                propertyChangeSupport.firePropertyChange(FIELD_PERCENT, oldValue, this.percent);
            }
        }
    }

    public String getTimeSpentHM() {
        if (timeSpent == null) {
            return timeSpentHM = "00:00";
        }
        timeSpentHM = "";
        if (timeSpent / 60 < 10) {
            timeSpentHM = "0";
        }
        timeSpentHM = timeSpentHM + timeSpent / 60;
        timeSpentHM = timeSpentHM + ":";
        if (timeSpent % 60 < 10) {
            timeSpentHM = timeSpentHM + "0";
        }
        timeSpentHM = timeSpentHM + timeSpent % 60;
        return timeSpentHM;
    }

    public void setTimeSpentHM(String timeSpentHM) {
        this.timeSpentHM = timeSpentHM;
    }


    public boolean equals(Object o) {
        if (!(o instanceof EdsEmployeeTask)) {
            return false;
        }
        EdsEmployeeTask employeeTask = (EdsEmployeeTask) o;
        return getTask() != null && (getTask().equals(employeeTask) || getProjectEmployee() != null && getProjectEmployee().equals(employeeTask.getProjectEmployee()));
    }

    public int hashCode() {
        return getObjectID() == null ? 0 : getObjectID().hashCode();
    }

    public String getCompletedby() {
        return completedby;
    }

    public void setCompletedby(String completedby) {
        this.completedby = completedby;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isNewTask() {
        if (newTask == null) {
            newTask = false;
        }
        return newTask;
    }

    public void setNewTask(boolean newTask) {
        this.newTask = newTask;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Integer getEstimatedTime() {
        return estimatedTime != null ? estimatedTime : 0;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }


    private Double actualWageAmmount = 0.0;
    private Double actualClientChargeAmmount = 0.0;
    private Double plannedWageAmount = 0.0;
    private Double plannedClientChargeAmount = 0.0;

    public Double getActualWageAmmount() {
        return actualWageAmmount != null ? actualWageAmmount : 0.0;
    }

    public void setActualWageAmmount(Double actualWageAmmount) {
        this.actualWageAmmount = actualWageAmmount;
    }

    public Double getActualClientChargeAmmount() {
        return actualClientChargeAmmount != null ? actualClientChargeAmmount : 0.0;
    }

    public void setActualClientChargeAmmount(Double actualClientChargeAmmount) {
        this.actualClientChargeAmmount = actualClientChargeAmmount;
    }

    public Double getPlannedWageAmount() {
        return plannedWageAmount != null ? plannedWageAmount : 0.0;
    }

    public void setPlannedWageAmount(Double plannedWageAmount) {
        this.plannedWageAmount = plannedWageAmount;
    }

    public Double getPlannedClientChargeAmount() {
        return plannedClientChargeAmount != null ? plannedClientChargeAmount : 0.0;
    }

    public void setPlannedClientChargeAmount(Double plannedClientChargeAmount) {
        this.plannedClientChargeAmount = plannedClientChargeAmount;
    }

    public void updatePlannedWageAmount(double diff) {
        setPlannedWageAmount(getPlannedWageAmount() + diff);
    }

    public void updatePlannedClientChargeAmount(double diff) {
        setPlannedClientChargeAmount(getPlannedClientChargeAmount() + diff);
    }

    public void updateActualWageAmmount(double diff) {
        setActualWageAmmount(getActualWageAmmount() + diff);
    }

    public void updateActualClientChargeAmmount(double diff) {
        setActualClientChargeAmmount(getActualClientChargeAmmount() + diff);
    }

    public Integer getDailyLoad() {
        return dailyLoad;
    }

    public void setDailyLoad(Integer dailyLoad) {
        this.dailyLoad = dailyLoad;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getOfficeID() {
        return officeID;
    }

    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }


    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public BigDecimal getTaskAmount() {
        return taskAmount;
    }

    public void setTaskAmount(BigDecimal taskAmount) {
        this.taskAmount = taskAmount;
    }

    @Override
    public String toString() {
        if (projectEmployee != null && projectEmployee.getEmployeeDepartment() != null && projectEmployee.getEmployeeDepartment().getEmployee() != null && projectEmployee.getEmployeeDepartment().getEmployee().getName() != null) {
            return projectEmployee.getEmployeeDepartment().getEmployee().getName();    //To change body of overridden methods use File | Settings | File Templates.
        }
        return "";
    }

    public Double getRemainingWageAmount() {
        double remainingWageAmount = getPlannedWageAmount() - getProjectEmployee().getWageRate() * getTimeSpent()/60;
        return remainingWageAmount > 0 ? remainingWageAmount : 0;
    }

    public Double getRemainingClientChargeAmount() {
        double remainingClientChargeAmount = getPlannedClientChargeAmount() - getProjectEmployee().getClientChargeRate() * getTimeSpent()/60;
        return remainingClientChargeAmount > 0 ? remainingClientChargeAmount : 0;
    }
}
