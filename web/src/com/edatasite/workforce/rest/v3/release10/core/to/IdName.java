package com.edatasite.workforce.rest.v3.release10.core.to;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Normurod Buriev.
 * Date: 1/5/2021 7:22 PM
 */
public class IdName extends DynamicDto {
    private Integer id;
    private String name;
    @JsonIgnore
    private LocaleDto locale;

    public IdName() {
    }

    public IdName(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public IdName(Integer id, String name, LocaleDto locale) {
        this.id = id;
        this.name = name;
        this.locale = locale;
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

    public LocaleDto getLocale() {
        return locale;
    }

    public void setLocale(LocaleDto locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "IdName{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
