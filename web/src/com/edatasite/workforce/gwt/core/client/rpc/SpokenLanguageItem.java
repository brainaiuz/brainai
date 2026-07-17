package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Ilhom Lutfullaev on 03.11.2017.
 */

public class SpokenLanguageItem implements IsSerializable {

    private SelectItem language;
    private SelectItem level;

    public SpokenLanguageItem() {
    }

    public SpokenLanguageItem(SelectItem language, SelectItem level) {
        this.language = language;
        this.level = level;
    }

    public SelectItem getLanguage() {
        return language;
    }

    public void setLanguage(SelectItem language) {
        this.language = language;
    }

    public SelectItem getLevel() {
        return level;
    }

    public void setLevel(SelectItem level) {
        this.level = level;
    }
}
