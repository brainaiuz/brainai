package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCsvTemplate;
import com.edatasite.workforce.core.domain.EdsCsvTemplateItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 22.04.13
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public interface CsvTemplateItemManager extends Manager<EdsCsvTemplateItem> {

    List<EdsCsvTemplateItem> getCsvTemplateItemsById(EdsCsvTemplate template);

    void deleteByTemplateId(Integer objectID);
}
