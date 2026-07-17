package com.edatasite.workforce.gwt.core.server.security;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Anvar Akramov on 10/4/17.
 */
public class XSSFilter {

    private ArrayList<String> exclusions;

    public XSSFilter() {
    }

    public void setExclusions(ArrayList<String> exclusions) {
        this.exclusions = exclusions;
    }

    private static Pattern[] patterns = new Pattern[]{
            // Avoid anything between script tags
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // Avoid anything in a src='...' type of expression
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid anything in a onfocus='...' type of expression
            Pattern.compile("onfocus[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onfocus[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Remove any lonesome </script> tag
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid eval(...) expressions
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid expression(...) expressions
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid javascript:... expressions
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // Avoid vbscript:... expressions
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            // Avoid onload= expressions
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            //Avoid iframes
            Pattern.compile("<iframe(.*?)>(.*?)</iframe>", Pattern.CASE_INSENSITIVE)
    };

    public String stripXSS(String value) {
        if (StringUtils.isNotBlank(value) && !isAllowed(value)) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
//            value = ESAPI.encoder().canonicalize(value);

            // Avoid null characters
            value = value.replace("\0", "");

            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns) {
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }

    private boolean isAllowed(String value) {
        if (exclusions != null && !exclusions.isEmpty()) {
            for (String exclusion : exclusions) {
                if (value.contains(exclusion)) {
                    return true;
                }
            }
        }
        return false;
    }

}
