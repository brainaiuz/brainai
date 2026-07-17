package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Dynamic;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Dilsh0d Tadjiev on 18.09.2020 12:39.
 */
public abstract class ApproverBaseSolrDoc extends BaseSolrDoc {

    @Dynamic
    @Field("approverId_*")
    private Map<String, Integer> approverIdDynamic = new HashMap<>();

    @Dynamic
    @Field("approverName_*")
    private Map<String, String> approverNameDynamic = new HashMap<>();

    @Dynamic
    @Field("approverIdName_*")
    private Map<String, String> approverIdNameDynamic = new HashMap<>();

    @Dynamic
    @Field("approverStatusId_*")
    private Map<String, Integer> approverStatusIdDynamic = new HashMap<>();

    @Dynamic
    @Field("approverStatusCode_*")
    private Map<String, String> approverStatusCodeDynamic = new HashMap<>();

    @Dynamic
    @Field("approverExactEmployeeId_*")
    private Map<String, Integer> approverExactEmployeeIdDynamic = new HashMap<>();

    @Dynamic
    @Field("approverExactEmployeeName_*")
    private Map<String, String> approverExactEmployeeNameDynamic = new HashMap<>();

    public Map<String, Integer> getApproverIdDynamic() {
        return approverIdDynamic;
    }

    public void setApproverIdDynamic(Map<String, Integer> approverIdDynamic) {
        this.approverIdDynamic = approverIdDynamic;
    }

    public Map<String, String> getApproverNameDynamic() {
        return approverNameDynamic;
    }

    public void setApproverNameDynamic(Map<String, String> approverNameDynamic) {
        this.approverNameDynamic = approverNameDynamic;
    }

    public Map<String, String> getApproverIdNameDynamic() {
        return approverIdNameDynamic;
    }

    public void setApproverIdNameDynamic(Map<String, String> approverIdNameDynamic) {
        this.approverIdNameDynamic = approverIdNameDynamic;
    }

    public Map<String, Integer> getApproverStatusIdDynamic() {
        return approverStatusIdDynamic;
    }

    public void setApproverStatusIdDynamic(Map<String, Integer> approverStatusIdDynamic) {
        this.approverStatusIdDynamic = approverStatusIdDynamic;
    }

    public Map<String, String> getApproverStatusCodeDynamic() {
        return approverStatusCodeDynamic;
    }

    public void setApproverStatusCodeDynamic(Map<String, String> approverStatusCodeDynamic) {
        this.approverStatusCodeDynamic = approverStatusCodeDynamic;
    }

    public Map<String, Integer> getApproverExactEmployeeIdDynamic() {
        return approverExactEmployeeIdDynamic;
    }

    public void setApproverExactEmployeeIdDynamic(Map<String, Integer> approverExactEmployeeIdDynamic) {
        this.approverExactEmployeeIdDynamic = approverExactEmployeeIdDynamic;
    }

    public Map<String, String> getApproverExactEmployeeNameDynamic() {
        return approverExactEmployeeNameDynamic;
    }

    public void setApproverExactEmployeeNameDynamic(Map<String, String> approverExactEmployeeNameDynamic) {
        this.approverExactEmployeeNameDynamic = approverExactEmployeeNameDynamic;
    }
}
