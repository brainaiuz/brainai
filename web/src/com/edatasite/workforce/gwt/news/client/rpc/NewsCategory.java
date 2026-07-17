package com.edatasite.workforce.gwt.news.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Nov 1, 2010
 * Time: 3:51:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewsCategory implements IsSerializable {
    private Integer id;
    private String name;
    private Integer parentId;


    public NewsCategory() {

    }

    public NewsCategory(Integer id) {
        this.id = id;
    }

    public NewsCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

}
