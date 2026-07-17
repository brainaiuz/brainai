package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsCustomFormAttributes;

import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 11.10.2019 15:26
 */
public interface CustomFormAttributeManager extends Manager<EdsCustomFormAttributes> {

    EdsCustomFormAttributes findByFieldType(String fieldType);

    List<EdsCustomFormAttributes> getAttributesByFormId(String formId);

    List<EdsCustomFormAttributes> getAttByFormIdAndFieldType(String fieldType, String formId);

    EdsCustomFormAttributes findByFieldTypeAndFieldID(String fieldId, String fieldType);
}
