package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.commons.CreateTemplateSchema;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@DisallowConcurrentExecution
public class GenerateTemplateSchemaJob extends BaseRecurrenceJob {

    private static final String TEMPLATE_LIMIT = "bg_generateTemplateSchema.limit";

    @Autowired
    CreateTemplateSchema templateSchema;

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        super.execute(ctx);

        for (TemplateSchema t : TemplateSchema.values()) {
            int limit = getLimit();
            if (limit <= 0) {
                getLogger().debug("Skipping pool for {} (limit=0)", t);
                continue;
            }
            try {
                int created = templateSchema.createSchemaTemplate(limit, t);
                getLogger().info("Pool {} refill: created {} new schemas (limit {})", t, created, limit);
            } catch (Throwable th) {
                getLogger().error("Pool refill failed for {}", t, th);
            }
        }
    }

    private Integer getLimit() {
        Properties props = new Properties();
        String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Constants.DEFAULT_SPRING_PROFILES);
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + profile + "/bgconfig.properties");
        if (stream != null) {
            try {
                props.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Integer templateLimit = Integer.valueOf(props.getProperty(TEMPLATE_LIMIT));
        return templateLimit != null ? templateLimit : 0;
    }
}
