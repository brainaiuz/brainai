package com.edatasite.workforce.gwt.core.server.office365.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by umidbekkarimov on 11/19/15.
 */
public class Office365ODataQuery {
    private static final String ODATA_SELECT = "$select";
    private static final String ODATA_EXPAND = "$expand";
    private static final String ODATA_ORDERBY = "$orderby";
    private static final String ODATA_FILTER = "$filter";
    private static final String ODATA_TOP = "$top";
    private static final String ODATA_SKIP = "$skip";
    private static final String ODATA_SKIP_TOKEN = "$skipTolen";
    private static final String ODATA_COUNT = "$count";

    public static final int TOP_LIMIT = 100;

    private ArrayList<String> select;
    private ArrayList<String> expand;
    private ArrayList<String> orderBy;


    /**
     * @see https://msdn.microsoft.com/en-us/library/hh169248(v=nav.90).aspx
     */
    private String filter;

    private Integer top;
    private Integer skip;
    private String skipToken;
    private Boolean count;

    public Office365ODataQuery() {
    }

    public void addSelect(String select) {
        if (this.select == null) {
            this.select = new ArrayList<>();
        }

        this.select.add(select);
    }

    public void setSelect(ArrayList<String> select) {
        this.select = select;
    }

    public void addExpand(String expand) {
        if (this.expand == null) {
            this.expand = new ArrayList<>();
        }

        this.expand.add(expand);
    }

    public void setExpand(ArrayList<String> expand) {
        this.expand = expand;
    }

    public void addOrderBy(String orderBy) {
        this.addOrderBy(orderBy, false);
    }

    public void addOrderBy(String orderBy, boolean isDesc) {
        if (this.orderBy == null) {
            this.orderBy = new ArrayList<>();
        }

        String query = orderBy;

        if (isDesc) {
            query += " desc";
        }

        this.orderBy.add(query);
    }

    public void setOrderBy(ArrayList<String> orderBy) {
        this.orderBy = orderBy;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public boolean isValidTop() {
        return this.top != null && this.top > 0 && this.top <= TOP_LIMIT;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public boolean isValidSkip() {
        return this.skip != null && this.skip >= 0;
    }

    public void setSkipToken(String skipToken) {
        this.skipToken = skipToken;
    }

    public void setCount(Boolean count) {
        this.count = count;
    }

    public List<NameValuePair> toNameValuePair() {
        ArrayList<NameValuePair> list = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(this.select)) {
            list.add(new BasicNameValuePair(ODATA_SELECT, StringUtils.join(this.select.toArray(), ",")));
        }

        if (CollectionUtils.isNotEmpty(this.expand)) {
            list.add(new BasicNameValuePair(ODATA_EXPAND, StringUtils.join(this.expand.toArray(), ",")));
        }

        if (CollectionUtils.isNotEmpty(this.orderBy)) {
            list.add(new BasicNameValuePair(ODATA_ORDERBY, StringUtils.join(this.orderBy.toArray(), ",")));
        }

        if (StringUtils.isNotEmpty(this.filter)) {
            list.add(new BasicNameValuePair(ODATA_FILTER, StringUtils.join(this.select.toArray(), ",")));
        }

        if (this.isValidTop()) {
            list.add(new BasicNameValuePair(ODATA_TOP, this.top.toString()));
        }

        if (this.isValidSkip()) {
            list.add(new BasicNameValuePair(ODATA_SKIP, this.skip.toString()));
        }

        if (StringUtils.isNotEmpty(this.skipToken)) {
            list.add(new BasicNameValuePair(ODATA_SKIP_TOKEN, this.skipToken));
        }

        if (this.count != null && this.count) {
            list.add(new BasicNameValuePair(ODATA_COUNT, ""));
        }

        return list;
    }
}
