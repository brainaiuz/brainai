package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageItem;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class SpokenLanguageTO extends ResponseData {
    private Integer language_id;
    private Integer level_id;

    public SpokenLanguageTO() {
    }

    public SpokenLanguageTO(SpokenLanguageItem item) {
        if (item.getLanguage() != null) {
            setLanguage_id(item.getLanguage().getId());
        }
        if (item.getLevel() != null) {
            setLevel_id(item.getLevel().getId());
        }
    }

    public SpokenLanguageItem toLanguageItem() {
        SpokenLanguageItem item = new SpokenLanguageItem();
        if (getLanguage_id() != null) {
            item.setLanguage(new SelectItem(getLanguage_id()));
        }
        if (getLevel_id() != null) {
            item.setLevel(new SelectItem(getLevel_id()));
        }
        return item;
    }

    public Integer getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(Integer language_id) {
        this.language_id = language_id;
    }

    public Integer getLevel_id() {
        return level_id;
    }

    public void setLevel_id(Integer level_id) {
        this.level_id = level_id;
    }
}
