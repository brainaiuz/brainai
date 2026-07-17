package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCsvTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 22.04.13
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */
public interface CsvTemplateManager extends Manager<EdsCsvTemplate> {

    List<SelectItem> getCsvTemplate(String templateType);

    EdsCsvTemplate getCsvTemplateById(Integer templateId);
}
