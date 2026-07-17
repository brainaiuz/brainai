package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
/**
 * Created by Abdurakhmonov Farrukh on 03/17/2018.
 */
public class RecurrenceTO extends ResponseData {
    private RecurrenceUntilTO until;
    private RecurrenceRepeatsTO repeats;

    public RecurrenceUntilTO getUntil() {
        return until;
    }

    public void setUntil(RecurrenceUntilTO until) {
        this.until = until;
    }

    public RecurrenceRepeatsTO getRepeats() {
        return repeats;
    }

    public void setRepeats(RecurrenceRepeatsTO repeats) {
        this.repeats = repeats;
    }
}
