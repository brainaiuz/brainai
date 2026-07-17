package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

/**
 * Created by Dilshod Madrahimov on 12/02/2018.
 */
public class RelatedTaskTO extends TaskSearchItemTO {

    private String description;

    public RelatedTaskTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
