package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abdulaziz
 * Date: Nov 3, 2009
 * Time: 4:11:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentsSearchItem implements IsSerializable {

    private String keyword;
    private int start;
    private int limit;
    private int sectionName = -1;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getSectionName() {
        return sectionName;
    }

    public void setSectionName(int sectionName) {
        this.sectionName = sectionName;
    }
}
