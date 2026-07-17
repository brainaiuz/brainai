package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCsvTemplate;
import com.edatasite.workforce.core.domain.EdsCsvTemplateItem;
import com.edatasite.workforce.gwt.core.server.db.CsvTemplateItemManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 26.04.13
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
@Repository("templateItemManager")
public class CsvTemplateItemManagerImpl extends BaseManager<EdsCsvTemplateItem> implements CsvTemplateItemManager {

    public CsvTemplateItemManagerImpl() {
        super(EdsCsvTemplateItem.class);
    }

    @Override
    public List<EdsCsvTemplateItem> getCsvTemplateItemsById(EdsCsvTemplate template) {
        return find("from EdsCsvTemplateItem csvItem where csvItem.csvTemplate = ?", template);
    }

    @Override
    public void deleteByTemplateId(Integer objectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("Delete from " + getCompanyId() + ".csvtemplateitem where csvtemplate_id =" + objectID);
        updateNative(sql.toString());
    }
}
