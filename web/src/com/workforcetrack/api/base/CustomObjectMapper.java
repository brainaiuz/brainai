package com.workforcetrack.api.base;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 01.05.12
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class CustomObjectMapper extends ObjectMapper {

    public CustomObjectMapper() {

        setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        enable(SerializationFeature.INDENT_OUTPUT);
//        configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
//        configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false);
        disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
        disable(SerializationFeature.CLOSE_CLOSEABLE);
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

//        setVisibilityChecker(getSerializationConfig().getDefaultVisibilityChecker()
//                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
//                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
//                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
//                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
//                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
        this.setDateFormat(new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT));
        registerModule(new JavaTimeModule());

        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        findAndRegisterModules();
//        this.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_DEFAULT);
//        this.getSerializationConfig().set(SerializationConfig.Feature.INDENT_OUTPUT, true);
//        this.getSerializationConfig().set(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
//        this.setDateFormat(new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT));
//        this.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
//        this.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        this.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);

        //getSerializationConfig().withDateFormat(new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT));
    }

//    this.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_DEFAULT);
//    this.getSerializationConfig().set(SerializationConfig.Feature.INDENT_OUTPUT, true);
//    this.getSerializationConfig().set(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
//    this.setDateFormat(new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT));
//    this.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);


}
