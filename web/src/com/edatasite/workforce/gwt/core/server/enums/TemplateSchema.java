package com.edatasite.workforce.gwt.core.server.enums;

import java.util.Arrays;

public enum TemplateSchema {
    TEMPLATE("template_", "0_template", "0"),
    GYM("gym_", "0_gym", "321864"),
    TEXTILE_FINDS_STARTER("textile_finds_starter_", "0_textile_finds_starter", "328552"),
    TEXTILE_FINDS_PRO("textile_finds_pro_", "0_textile_finds_pro", "328766"),
    TEXTILE_FINDS_PREMIUM("textile_finds_premium_", "0_textile_finds_premium", "328767"),
    RETAIL_SIMPLIFIED("retail_simplified_", "328262", "328262"),
    RETAIL_CLASSIC("retail_classic_", "328264", "328264"),
    MANUFACTURING_SIMPLIFIED("manufacturing_simplified_", "328291", "328291"),
    MANUFACTURING_CLASSIC("manufacturing_classic_", "328290", "328290"),
    HEALTHCARE_MEDICINE_SIMPLIFIED("healthcare_medicine_simplified_", "328269", "328269"),
    HEALTHCARE_MEDICINE_CLASSIC("healthcare_medicine_classic_", "328270", "328270"),
    WHOLESALE_SIMPLIFIED("wholesale_simplified_", "328292", "328292"),
    WHOLESALE_CLASSIC("wholesale_classic_", "328280", "328280"),
    CONSTRUCTION_SIMPLIFIED("construction_simplified_", "328284", "328284"),
    CONSTRUCTION_CLASSIC("construction_classic_", "328282", "328282"),
    OTHER("other_", "328563", "328563");

    private final String pattern;
    private final String template;
    private final String schema;

    TemplateSchema(String pattern, String template, String schema) {
        this.pattern = pattern;
        this.template = template;
        this.schema = schema;

    }

    public static TemplateSchema of(String orgType) {
        return Arrays.stream(TemplateSchema.values())
                .filter(value -> value.name().equalsIgnoreCase(orgType))
                .findFirst()
                .orElse(TEMPLATE);
    }

    public static TemplateSchema getSchema(String name, String host) {
        if (name == null) {
            return TemplateSchema.TEMPLATE;
        }
        if (host.endsWith("praaktisgo.com") || host.equals("gym.kpi.com")) {
            return GYM;
        }
        if (host.equals("erp.textilefinds.com")) {
            return TEXTILE_FINDS_STARTER;
        }
        return TemplateSchema.TEMPLATE;
    }

    public String getPattern() {
        return pattern;
    }

    public String getTemplate() {
        return template;
    }

    public String getSchema() {
        return schema;
    }

    public String getSlashedSchema() {
        return "\"" + schema + "\"";
    }
}
