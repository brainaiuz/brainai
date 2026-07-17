package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ilhom Lutfullaev on 03.11.2017.
 */

public class SpokenLanguagesWidget extends Composite {

    private final AllInOneServiceAsync allInOneService = AllInOneService.App.get();
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private SpokenLanguageTO languageList;
    public MultiTableNewUI table;
    private int count = 0;
    private boolean hideButtons = false;

    public SpokenLanguagesWidget(ArrayList<SpokenLanguageItem> languages) {
        onInitialize(languages);
    }

    private void onInitialize(ArrayList<SpokenLanguageItem> languages) {
        languageList = new SpokenLanguageTO();
        table = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getWidgets(null, null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> widgetsMap : table.getWidgets()) {
                    DataListBox languageBox = (DataListBox) widgetsMap.get(MultiTable.LIST_BOX);
                    if (languageBox.getSelectedId() != null) {
                        return true;
                    }
                }
                return false;
            }
        }, false);

        allInOneService.getLanguagesWithLevels(new AsyncCallback<SpokenLanguageTO>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SpokenLanguageTO spokenLanguageTO) {
                languageList = spokenLanguageTO;
                if (!table.getWidgets().isEmpty()) {
                    table.getWidgets().forEach(widgets-> {
                        DataListBox languageBox = (DataListBox) widgets.get("LANGUAGE");
                        if (languageBox != null) {
                            Integer selectedLanguage = languageBox.getSelectedItem() != null ? languageBox.getSelectedItem().getId() : null;
                            languageBox.setItems(languageList.getLanguages().toArray(new SelectItem[]{}));
                            languageBox.setSelected(selectedLanguage);
                        }
                        DataListBox languageLevelBox = (DataListBox) widgets.get("LEVEL");
                        if (languageLevelBox != null) {
                            Integer selectedLanguageLevel = languageLevelBox.getSelectedItem() != null ? languageLevelBox.getSelectedItem().getId() : null;
                            languageLevelBox.setItems(languageList.getLanguageLevels().toArray(new SelectItem[]{}));
                            languageLevelBox.setSelected(selectedLanguageLevel);
                        }
                    });
                }
            }
        });
        initWidget(table);
    }

    private WidgetsMap getWidgets(SelectItem language, SelectItem level) {
        WidgetsMap widgetsMap = new WidgetsMap();

        DataListBox languageBox = new DataListBox();
        if (languageList != null && languageList.getLanguages() != null) {
            languageBox.setItems(languageList.getLanguages().toArray(new SelectItem[]{}));
        }
        languageBox.setSelected(language);

        DataListBox levelBox = new DataListBox();
        if (languageList != null && languageList.getLanguageLevels() != null) {
            levelBox.setItems(languageList.getLanguageLevels().toArray(new SelectItem[]{}));
        }
        levelBox.setSelected(level);

        widgetsMap.addToLeft("LANGUAGE", languageBox);
        widgetsMap.addToRight("LEVEL", levelBox);
        return widgetsMap;
    }

    public ArrayList<SpokenLanguageItem> getLanguages() {
        ArrayList<SpokenLanguageItem> result = new ArrayList<>();
        if (!table.getWidgets().isEmpty()) {
            table.getWidgets().forEach(widgets-> {
                DataListBox languageBox = (DataListBox) widgets.get("LANGUAGE");
                if (languageBox != null) {
                    DataListBox languageLevelBox = (DataListBox) widgets.get("LEVEL");
                    Integer selectedLanguage = languageBox.getSelectedItem() != null ? languageBox.getSelectedItem().getId() : null;
                    Integer selectedLanguageLevel = languageLevelBox.getSelectedItem() != null ? languageLevelBox.getSelectedItem().getId() : null;
                    if (selectedLanguage != null) {
                        result.add(new SpokenLanguageItem(languageBox.getSelectedItem(), languageLevelBox.getSelectedItem()));
                    }
                }
            });
        }
        return result;
    }

    public MultiTableNewUI getContent() {
        return table;
    }


    public void setLanguages(ArrayList<SpokenLanguageItem> spokingLanguages) {
        if (spokingLanguages != null && !spokingLanguages.isEmpty()) {
            table.remove(0);
            count = 0;
            for (SpokenLanguageItem item : spokingLanguages) {
                table.addWidgets(getWidgets(item.getLanguage(), item.getLevel()));
            }
        }
    }
}
