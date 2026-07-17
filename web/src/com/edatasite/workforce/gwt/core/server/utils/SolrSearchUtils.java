package com.edatasite.workforce.gwt.core.server.utils;

import java.util.Map;

/**
 * User: Rinat
 * Date: 23.02.12
 * Time: 14:29
 */

public class SolrSearchUtils {
    public static final String specialKeyCharacters = "+-&&||!(){}[]^\"~*?:/\\";
    public static final Double HIGH_PRIORITY = 20000.0;
    public static final Double ABOVE_NORMAL_PRIORITY = 1500.0;
    public static final Double NORMAL_PRIORITY = 100.0;
    public static final Double BELOW_NORMAL_PRIORITY = 5.0;
    public static final Double LOW_PRIORITY = 0.02;


    public StringBuffer generateApiSearchQuery(StringBuffer queryText, Map<String, Double> fields, String keyword) {
        keyword = normalaizeKeyword(keyword, true);
        if (fields != null && fields.size() != 0) {
            queryText.append(" OR (");
            String or = "";
            for (String s : fields.keySet()) {
                queryText.append(or);
                queryText.append("(");
                queryText.append("" + s + ":(" + keyword.toLowerCase() + ")");
                queryText.append(" OR " + s + ":(" + keyword.toLowerCase() + "*)");
                queryText.append(" OR " + s + ":(*" + keyword.toLowerCase() + "*)");
                queryText.append(")");
                or = " OR ";
            }
            queryText.append(")^" + HIGH_PRIORITY);
        }
        return queryText;
    }

    public StringBuilder generateAndSearchQuery(StringBuilder queryText, Map<String, Double> fields, String keyword) {
        keyword = normalaizeKeyword(keyword, true);
        if (fields != null && fields.size() != 0) {
            queryText.append(" AND (");
            String or = "";
            for (String s : fields.keySet()) {
                queryText.append(or);
                queryText.append("(" + s + ":(\"" + keyword.toLowerCase() + "\")^10");
                queryText.append(" OR " + s + ":(" + keyword.toLowerCase() + "*)^5");
                queryText.append(" OR " + s + ":(*" + keyword.toLowerCase() + "*)^1)^" + fields.get(s));
                or = " OR ";
            }
            queryText.append(")^" + HIGH_PRIORITY);
        }
        return queryText;
    }

    public StringBuffer generateSearchQuery(StringBuffer queryText, Map<String, Double> fields, String keyword) {
        keyword = normalaizeKeyword(keyword, true);
        if (fields != null && fields.size() != 0) {
            queryText.append(" OR (");
            String or = "";
            for (String s : fields.keySet()) {
                queryText.append(or);
                queryText.append("(")
                        .append(s).append(":(\"" + keyword.toLowerCase() + "\")")
                        .append(" OR " + s + ":(" + keyword.toLowerCase() + "*)")
                        .append(" OR " + s + ":(*" + keyword.toLowerCase() + "*)")
                        .append(")^" + fields.get(s));
                or = " OR ";
            }
            queryText.append(")^" + HIGH_PRIORITY);
        }
        return queryText;
    }

    public StringBuffer generateSearchQueryForEmployee(StringBuffer queryText, Map<String, Double> fields, String keyword) {
        keyword = normalaizeKeyword(keyword, true);
        if (fields != null && fields.size() != 0) {
            queryText.append(" OR (");
            String or = "";
            for (String s : fields.keySet()) {
                queryText.append(or);
                queryText.append("(" + s + ":(\"" + keyword.toLowerCase() + "\")^10");
                queryText.append(" OR " + s + ":(" + keyword.toLowerCase() + "*)^5");
                queryText.append(" OR " + s + ":(*" + keyword.toLowerCase() + "*)^1)^" + fields.get(s));
                or = " OR ";
            }
            queryText.append(")^" + HIGH_PRIORITY);
        }
        return queryText;
    }

    public StringBuilder generateSearchQueryForCustom(StringBuilder queryText, Map<String, Double> fields, String keyword) {
        keyword = normalaizeKeyword(keyword, true);
        if (fields != null && fields.size() != 0) {
            queryText.append(" OR (");
            String or = "";
            for (String s : fields.keySet()) {
                queryText.append(or);
                queryText.append("(" + s + ":(\"" + keyword.toLowerCase() + "\")^10");
                queryText.append(" OR " + s + ":(" + keyword.toLowerCase() + "*)^5");
                queryText.append(" OR " + s + ":(*" + keyword.toLowerCase() + "*)^1)^" + fields.get(s));
                or = " OR ";
            }
            queryText.append(")^" + HIGH_PRIORITY);
        }
        return queryText;
    }

    public static String normalaizeKeyword(String keyword) {
        return normalaizeKeyword(keyword, false);
    }

    public static String normalaizeKeyword(String keyword, boolean onlyShadowing) {
        StringBuffer searchKey = new StringBuffer();
        for (char ch : keyword.toCharArray()) {
            if (specialKeyCharacters.contains(String.valueOf(ch))) {
                searchKey.append("\\");
            }
            searchKey.append(ch);
        }

        keyword = searchKey.toString().trim().toLowerCase();
        if (!onlyShadowing) {
            searchKey = new StringBuffer();
            searchKey.append("((").append("\"").append(keyword).append("\"");
            searchKey.append(" OR ").append("*").append(keyword).append("* ");
            searchKey.append(")^").append(SolrSearchUtils.ABOVE_NORMAL_PRIORITY).append(" )^").append(SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        }
        return searchKey.toString();
    }
}
