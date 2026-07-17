package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class CategoryTO extends ResponseData {
    private Integer id;
    private String title;
    private String code;

    public CategoryTO() {
    }

    public CategoryTO(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public CategoryTO(Integer id, String title, String code) {
        this.id = id;
        this.title = title;
        this.code = code;
    }

    public CategoryTO(Integer id, String title, Integer companyId, String companyName) {
        this.id = id;
        this.title = title;
        this.addProperty("companyId", companyId);
        this.addProperty("companyName", companyName);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
