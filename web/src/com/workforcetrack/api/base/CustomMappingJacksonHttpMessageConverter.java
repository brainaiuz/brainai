package com.workforcetrack.api.base;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 02.05.12
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class CustomMappingJacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private ObjectMapper objectMapper = new CustomObjectMapper();

    private boolean prefixJson = false;

    private boolean prettyPrint = false;


    public CustomMappingJacksonHttpMessageConverter() {
        super();
    }

    public CustomMappingJacksonHttpMessageConverter(boolean prettyPrint) {
        super();
        this.prettyPrint = prettyPrint;
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        JsonEncoding encoding = getEncoding(outputMessage.getHeaders().getContentType());
        //getObjectMapper().getSerializationConfig().withDateFormat(new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT));
        JsonGenerator jsonGenerator = getObjectMapper().getJsonFactory().createJsonGenerator(outputMessage.getBody(), encoding);
        try {
            if (this.prefixJson) {
                jsonGenerator.writeRaw("{} && ");
            }
            if (isPrettyPrint()) {
                jsonGenerator.useDefaultPrettyPrinter();
            }
            getObjectMapper().writeValue(jsonGenerator, o);
        } catch (JsonGenerationException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    private JsonEncoding getEncoding(MediaType contentType) {
//        if (contentType != null && contentType.getCharSet() != null) {
//            Charset charset = contentType.getCharSet();
//            for (JsonEncoding encoding : JsonEncoding.values()) {
//                if (charset.name().equals(encoding.getJavaName())) {
//                    return encoding;
//                }
//            }
//        }
        return JsonEncoding.UTF8;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }
}
