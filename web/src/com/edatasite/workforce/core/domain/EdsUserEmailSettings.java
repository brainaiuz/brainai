package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 2, 2010
 * Time: 4:17:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "userEmailSettings")
public class EdsUserEmailSettings extends EdsObject {

    public static final Integer DEFAULT_NUMBER_OF_EMAILS = 2;

    /**
     * Current field is responsible to store localization data for certain
     * users. Each  user has its own country language and it has to be set
     * that language by him / herself. Then all related data will be shown
     * according to stored language customization. By default its value is
     * 'en' that means English for all users.
     * <p/>
     * For further info please refer Ruslan Muhammadov
     */
    private String internationalization;

    private Boolean isCalendarAutoSyncEnabled = false;

    private Boolean isFahrenheit = false;

    /**
     * Current variable defines how many fetched emails can be used by
     * users in Message Center. We cannot let them to fetch  unlimited
     * number of emails, it may corrupt  proper working of servers and
     * may  hang on  the  system. Thus, by  default  for  each  we are
     * allowing  to  fetch  only two  emails, for premium users we can
     * increase that number from 2 to 4 or 5.
     * <p/>
     * For further info please refer to Ruslan Muhammadov
     */
    @Column(name = "emailnumberlimit")
    private Integer limitForNumberOfEmails = DEFAULT_NUMBER_OF_EMAILS;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Boolean sendEmailNotification = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    private String weatherLocationId;

    @Column(columnDefinition = "boolean DEFAULT true")
    private boolean timesheetrequired = true;

    @Type(type = "text")
    private String startPage;

    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean syncFromDefaultCalendar = false;

    @Column
    private String contactSyncType;

    private String ganttChartColumns;

    @Column(name = "fingerPrintUserID")
    private Integer fingerPrint;

    @Column(name = "deviceUUID")
    private String deviceUUID;

    private String officeContactSyncType;

    public Integer getObjectID() {
        return objectID;
    }

    public String getInternationalization() {
        return internationalization;
    }

    public void setInternationalization(String internationalization) {
        this.internationalization = internationalization;
    }

    public Boolean isCalendarAutoSyncEnabled() {
        return isCalendarAutoSyncEnabled;
    }

    public void setCalendarAutoSyncEnabled(Boolean calendarAutoSyncEnabled) {
        isCalendarAutoSyncEnabled = calendarAutoSyncEnabled;
    }

    public Boolean isFahrenheit() {
        return isFahrenheit;
    }

    public void setFahrenheit(Boolean fahrenheit) {
        isFahrenheit = fahrenheit;
    }

    public Integer getLimitForNumberOfEmails() {
        return limitForNumberOfEmails;
    }

    public void setLimitForNumberOfEmails(Integer limitForNumberOfEmails) {
        this.limitForNumberOfEmails = limitForNumberOfEmails;
    }

    public Boolean isSendEmailNotification() {
        return sendEmailNotification;
    }

    public void setSendEmailNotification(Boolean sendEmailNotification) {
        this.sendEmailNotification = sendEmailNotification;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public String getWeatherLocationId() {
        return weatherLocationId;
    }

    public void setWeatherLocationId(String weatherLocationId) {
        this.weatherLocationId = weatherLocationId;
    }

    public boolean isTimesheetrequired() {
        return timesheetrequired;
    }

    public void setTimesheetrequired(boolean timesheetrequired) {
        this.timesheetrequired = timesheetrequired;
    }

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public boolean isSyncFromDefaultCalendar() {
        return syncFromDefaultCalendar;
    }

    public void setSyncFromDefaultCalendar(boolean syncFromDefaultCalendar) {
        this.syncFromDefaultCalendar = syncFromDefaultCalendar;
    }

    public String getGanttChartColumns() {
        return ganttChartColumns;
    }

    public void setGanttChartColumns(String ganttChartColumns) {
        this.ganttChartColumns = ganttChartColumns;
    }

    public Integer getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(Integer fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getContactSyncType() {
        return contactSyncType;
    }

    public void setContactSyncType(String contactSyncType) {
        this.contactSyncType = contactSyncType;
    }

    public String getOfficeContactSyncType() {
        return officeContactSyncType;
    }

    public void setOfficeContactSyncType(String officeContactSyncType) {
        this.officeContactSyncType = officeContactSyncType;
    }
}
