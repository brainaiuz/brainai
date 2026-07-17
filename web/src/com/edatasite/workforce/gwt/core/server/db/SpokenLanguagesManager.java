package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSpokenLanguages;

import java.util.ArrayList;

/**
 * Created by Ilhom Lutfullaev on 03.11.2017.
 */

public interface SpokenLanguagesManager extends Manager<EdsSpokenLanguages> {

    ArrayList<EdsSpokenLanguages> getListByRelation(Integer entityId, String entityType);

    EdsSpokenLanguages getByRelation(Integer entityId, String entityType, Integer languageId);

    void removedLanguages(Integer entityId, String entityType, ArrayList<Integer> removedLanguagesList);

    void firstRemoveEmployeeLanguages(Integer entityId, String entityType);
}
