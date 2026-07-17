package com.edatasite.workforce.gwt.core.client.ui.customfields;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 6/14/12
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ValidationType {
    BeforeDate("Before date validation", 1),
    AfterRequired("After required validation", 2),
    IsEmail("Is email validation", 3),
    IsEmpty("Is Empty validation", 4);

    private Integer id;
    private String title;

    ValidationType(String title, int id) {
        this.title = title;
        this.id = id;
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
}
