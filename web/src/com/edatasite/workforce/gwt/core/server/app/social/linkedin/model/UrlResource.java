package com.edatasite.workforce.gwt.core.server.app.social.linkedin.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Created by Anvar Akramov on 10/6/17.
 */
public class UrlResource extends LinkedInObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String url;

    public UrlResource() {

    }

    public UrlResource(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

}
