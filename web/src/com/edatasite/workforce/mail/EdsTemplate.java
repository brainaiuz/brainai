package com.edatasite.workforce.mail;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.DirectionUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: iskandar
 * Date: Oct 21, 2007
 * Time: 2:30:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class EdsTemplate {
    private static final Logger log = LoggerFactory.getLogger(EdsTemplates.class);

    private String content;

    public EdsTemplate(String content) {
       this.content = content;
    }

    public String process(ITextGenericPdfData itextGenericPdfData) throws Exception {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("itextGenericPdfData", itextGenericPdfData);
        velocityContext.put("directionUtils", new DirectionUtils());
        StringWriter writer = new StringWriter();
        Velocity.evaluate(velocityContext, writer, "Pdf Template LOG", content);
        return writer.toString();
    }

    public String process(String itextGenericPdfData) throws Exception {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("itextGenericPdfData", itextGenericPdfData);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(velocityContext, writer, "Pdf Template LOG", content);
        return writer.toString();
    }

    public String processContent(Map<String, Object> context) throws Exception{
        VelocityContext velocityContext = new VelocityContext();
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            velocityContext.put(entry.getKey(), entry.getValue());
        }
        StringWriter writer = new StringWriter();
        Velocity.evaluate(velocityContext, writer, "Address Template LOG", content);
        return writer.toString();
    }
}
