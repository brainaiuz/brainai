package com.edatasite.workforce.gwt.trainingcenter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/27/12
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class TCHtmlTemplates {

    public interface TCHtmlTemplatesInterface extends SafeHtmlTemplates {
        @Template("<div class=\"value-style\">{0}</div>")
        SafeHtml value(String value);

        @Template("<div class=\"duration-style\">{0} hour(s)</div>")
        SafeHtml durationValue(String value);

        @Template("<div class=\"duration-style\">{0} month(s)</div>")
        SafeHtml validityValue(String value);

        @Template("<div class=\"title-style\">{0}: </div>")
        SafeHtml title(String value);

        @Template("<div class=\"title-style\"> You aren't Client of the System!!! </div>")
        SafeHtml clientErrInfo();

        @Template("<div class=\"title-style\">\n" +
                "\t<p>This will generate daily invoice per client per location for the scheduled courses which have been marked as \"Delivered\". </p>\n" +
                "\t<p>Please make sure you have updated your scheduled course status before using this feature.</p>\n" +
                "\t<br/>\n" +
                "</div>")
        SafeHtml invoiceGenerationInfo();



    }

    private static TCHtmlTemplatesInterface instance;
    public static TCHtmlTemplatesInterface getInstance() {
        if (instance == null) {
            instance = GWT.create(TCHtmlTemplatesInterface.class);
        }

        return instance;
    }
}
