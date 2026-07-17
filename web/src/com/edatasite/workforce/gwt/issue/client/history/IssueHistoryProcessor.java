package com.edatasite.workforce.gwt.issue.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.issue.client.IssueAddSinksContainer;
import com.edatasite.workforce.gwt.issue.client.IssueSinksContainer;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;

public class IssueHistoryProcessor implements HistoryProcessor {

    private final ProjectStrings projectStrings = ProjectStrings.App.get();
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new IssueSinksContainer(containerName + strings[0], Property.get(Constants.ISSUE, wfmStrings.summaryView(), wfmStrings.issue()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new IssueAddSinksContainer("issueadd", Property.get(Constants.ISSUE, projectStrings.addIssue(), wfmStrings.issue()), params);
    }
}