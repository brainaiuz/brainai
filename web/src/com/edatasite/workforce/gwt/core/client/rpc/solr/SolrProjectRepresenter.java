package com.edatasite.workforce.gwt.core.client.rpc.solr;

/**
 * Project related   : by default project related fields are not stored due to uselessness of returning of this data
 * User: Abdulaziz
 * Date: Nov 3, 2009
 * Time: 7:43:44 PM
 */
public class SolrProjectRepresenter extends SolrDocumentRepresenter {
    public static final String FIELD_PROJECT_MANAGER = "projectManager";
    public static final String FIELD_PROJECT_CLIENT = "projectClient";

    private String projectManager;
    private String projectClient;

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getProjectClient() {
        return projectClient;
    }

    public void setProjectClient(String projectClient) {
        this.projectClient = projectClient;
    }
}
