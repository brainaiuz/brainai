package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HHSalaryDto {
    private int from;
    private int to;
    private String currency;
    private boolean gross = true;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isGross() {
        return gross;
    }

    public void setGross(boolean gross) {
        this.gross = gross;
    }
}
