package com.edatasite.workforce.gwt.issue.client.factory;

import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.issue.client.history.IssueHistoryProcessor;

public class IssueSinksContainerFactory extends SinksContainerFactory {

    public IssueSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
    }

    public void initDefaultContainers() {

    }

    public void registerProcessors() {
        registerHistoryProcessor("issue", new IssueHistoryProcessor());
    }

    public void registerMenuItems() {

    }
}
