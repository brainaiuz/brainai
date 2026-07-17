package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.tools.EdsSchemaUpdater;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CreateTemplateSchema {

    private static final Logger log = LoggerFactory.getLogger(CreateTemplateSchema.class);

    @Autowired
    JdbcSpringManager jdbcSpringManager;


    public Integer createSchemaTemplate(Integer limit, TemplateSchema templateSchema) {
        log.info("GenerateTemplateSchemaJob started: " + new Date());
        List<String> templates = jdbcSpringManager.getExistingTemplateSchemas(templateSchema.getPattern());//this returns template count numbers like "template_1" then "1" in descending order

        int createdSchema = 0;
        Integer iterator = 1;
        if (templates.size() > 0) {
            String s = templates.get(0);//get max
            try {
                iterator = Integer.valueOf(s) + 1;
            } catch (NumberFormatException e) {
                iterator = 1;
            }
        }

        if (templates.size() < limit) {
            int count = limit - templates.size();
            for (int i = iterator; i < iterator + count; i++) {
                String schemaName = templateSchema.getPattern() + i;
                log.info("Creating template: " + schemaName);
                try {
                    EdsSchemaUpdater.setDbUrl(null);
                    EdsSchemaUpdater.createAsSchema(schemaName, templateSchema.getTemplate());

                    EdsSchemaUpdater.updateSchema(schemaName);
                    createdSchema++;
                } catch (Throwable t) {
                    jdbcSpringManager.getSimJdbcOperations().update("DROP SCHEMA \"" + schemaName + "\" CASCADE");
                    log.error("Error During Schema Export with schema name - " + schemaName, t);
                }
            }
        }
        log.info("GenerateTemplateSchemaJob finished: " + new Date());
        return createdSchema;
    }
}
