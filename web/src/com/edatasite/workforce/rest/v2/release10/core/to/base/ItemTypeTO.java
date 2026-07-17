package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Anvar Akramov on 03/31/2018.
 */
public class ItemTypeTO extends TitleTO {

    private Integer id;
    private String code;

    public ItemTypeTO() {
    }

    public ItemTypeTO(Integer id, String code, String title) {
        super(title);
        this.id = id;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
