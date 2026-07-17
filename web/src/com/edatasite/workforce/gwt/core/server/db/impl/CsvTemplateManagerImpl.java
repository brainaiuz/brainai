package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCsvTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.db.CsvTemplateManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 22.04.13
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */

@Repository("templateManager")
public class CsvTemplateManagerImpl extends BaseManager<EdsCsvTemplate> implements CsvTemplateManager {

    public CsvTemplateManagerImpl() {
        super(EdsCsvTemplate.class);
    }

    @Override
    public List<SelectItem> getCsvTemplate(String templateType) {
        List<EdsCsvTemplate> csvTemplates = find("select ct from EdsCsvTemplate ct where ct.type = ? order by ct.objectID", templateType);
        List<SelectItem> selectItems = new ArrayList<>();
        for (EdsCsvTemplate csvt : csvTemplates) {
            selectItems.add(new SelectItem(csvt.getObjectID(), csvt.getName()));
        }
        return selectItems;
    }

    @Override
    public EdsCsvTemplate getCsvTemplateById(Integer templateId) {
        return (EdsCsvTemplate) findSingle("select csv from EdsCsvTemplate csv where csv.objectID = ?", templateId);
    }
}
