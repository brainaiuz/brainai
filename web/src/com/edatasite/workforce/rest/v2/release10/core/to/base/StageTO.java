package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class StageTO extends CategoryTO {

    private String percentage;

    public StageTO(String percentage) {
        this.percentage = percentage;
    }

    public StageTO(Integer id, String title, String percentage) {
        super(id, title);
        this.percentage = percentage;
    }

    public StageTO(Integer id, String title, String code, String percentage) {
        super(id, title, code);
        this.percentage = percentage;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
