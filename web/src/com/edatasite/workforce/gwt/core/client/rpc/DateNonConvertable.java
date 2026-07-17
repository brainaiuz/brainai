package com.edatasite.workforce.gwt.core.client.rpc;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 23, 2010
 * Time: 12:50:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class DateNonConvertable implements Serializable {
    private Date date;
    private long dateLong;

    private int inTimeZoneOffSetMs; // timezoneOffset in ms while setting a new date to non-convertable date object
    private int outTimeZoneOffSetMs; // timezoneOffset in ms of the current environment while getting the non-convertable date

    public DateNonConvertable() {
        date = new Date();
        dateLong = date.getTime();
        inTimeZoneOffSetMs = date.getTimezoneOffset() * 60 * 1000;
    }

    public DateNonConvertable(Date date) {
        this();
        if (date != null) {
            this.date = date;
            this.dateLong = this.date.getTime();
            inTimeZoneOffSetMs = this.date.getTimezoneOffset() * 60 * 1000;
        }
    }

    /**
     * While setting a new date either from server side or client side, it should remember the timezoneOffset.
     * Once the date it moved to different environment from server side to clientside or vice versa, it will get the tiemzoneOffset
     * of the new environment and apply the timezoneOffset from both sides and thus keep the original date and time.
     * Note: it will assume to ignore the timezone, but keep the date and time the same while moving date from client to server or vice versa
     *
     * @param date
     */

    public void setDate(Date date) {
        this.date = date;
        this.dateLong = this.date.getTime();
        inTimeZoneOffSetMs = this.date.getTimezoneOffset() * 60 * 1000;
    }

    public Date getDate() {
        return date;
    }

    public Date getNonConvertedDate() {
        outTimeZoneOffSetMs = date.getTimezoneOffset() * 60 * 1000;
        return new Date(dateLong - inTimeZoneOffSetMs + outTimeZoneOffSetMs);
    }

    public long getDateLong() {
        return dateLong;
    }

    public void setDateLong(long dateLong) {
        this.dateLong = dateLong;
    }

    public int getInTimeZoneOffSetMs() {
        return inTimeZoneOffSetMs;
    }

    public void setInTimeZoneOffSetMs(int inTimeZoneOffSetMs) {
        this.inTimeZoneOffSetMs = inTimeZoneOffSetMs;
    }

    public int getOutTimeZoneOffSetMs() {
        return outTimeZoneOffSetMs;
    }

    public void setOutTimeZoneOffSetMs(int outTimeZoneOffSetMs) {
        this.outTimeZoneOffSetMs = outTimeZoneOffSetMs;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getNonConvertedDate().equals(((DateNonConvertable) obj).getNonConvertedDate());
    }
}
