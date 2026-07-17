package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Ilhom Lutfullaev on 03.11.2017.
 */
public class SpokenLanguageTO implements IsSerializable {

    private ArrayList<SelectItem> languages = new ArrayList<>();
    private ArrayList<SelectItem> languageLevels = new ArrayList<>();

    public ArrayList<SelectItem> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<SelectItem> languages) {
        this.languages = languages;
    }

    public ArrayList<SelectItem> getLanguageLevels() {
        return languageLevels;
    }

    public void setLanguageLevels(ArrayList<SelectItem> languageLevels) {
        this.languageLevels = languageLevels;
    }
}
