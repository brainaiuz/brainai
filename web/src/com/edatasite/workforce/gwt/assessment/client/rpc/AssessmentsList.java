package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class AssessmentsList implements IsSerializable, Serializable {
    private AssessmentsListElem[] results;
    private int totalCount;

    public AssessmentsList() {

    }

    public AssessmentsList(AssessmentsListElem[] assessments, int count) {
        this.results = assessments;
        this.totalCount = count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public AssessmentsListElem[] getResults() {
        return results;
    }

    public void setResults(AssessmentsListElem[] results) {
        this.results = results;
    }

    public ListData getListData() {
        return new ListData(results, totalCount);
    }

}
