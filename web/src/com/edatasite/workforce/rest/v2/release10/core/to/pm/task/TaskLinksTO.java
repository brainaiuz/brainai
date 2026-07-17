package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Abdurakhmonov Farrukh on 12/26/2017.
 */
public class TaskLinksTO extends ResponseData {
    private String title;
    private String link;

    public TaskLinksTO() {
    }

    public TaskLinksTO(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
