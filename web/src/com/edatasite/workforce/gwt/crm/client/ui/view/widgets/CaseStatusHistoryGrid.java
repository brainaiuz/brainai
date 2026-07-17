package com.edatasite.workforce.gwt.crm.client.ui.view.widgets;

import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CaseList;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 11.04.12
 * Time: 12:27
 */

public class CaseStatusHistoryGrid extends AbstractDataGrid<CaseItem> {
    private Integer caseID;

    public CaseStatusHistoryGrid(Integer caseID) {
        this();
        this.caseID = caseID;
        initialize();
    }

    public CaseStatusHistoryGrid() {
        super();
    }

    @Override
    protected void addColums() {

        Column<CaseItem, SafeHtml> status = new Column<CaseItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final CaseItem item) {
                return () -> "<span>" + (item.getStatus().getId() != null ? item.getStatus().getName() : wfmStrings.notAvailable()) + "</span>";
            }
        };
        addColumn(status, wfmStrings.status());
        setColumnWidth(status, "400px");

        Column<CaseItem, SafeHtml> note = new Column<CaseItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final CaseItem item) {
                return () -> "<span>" + (item.getStatusChangedNote() != null ? item.getStatusChangedNote() : wfmStrings.notAvailable()) + "</span>";
            }
        };
        addColumn(note, wfmStrings.note());
        setColumnWidth(note,"450px");


        addColumn(new Column<CaseItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final CaseItem item) {
                return () -> "<span>" + (item.getAuditInfoResource() != null && item.getAuditInfoResource().getModificationDate() != null ? DateUtils.formatInternal(item.getAuditInfoResource().getModificationDate()) : wfmStrings.notAvailable()) + "</span>";
            }
        }, wfmStrings.modifiedDate());

        addColumn(new Column<CaseItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final CaseItem item) {
                return () -> "<span>" + (item.getAuditInfoResource() != null && item.getAuditInfoResource().getModifiedBy() != null ? item.getAuditInfoResource().getModifiedBy().getFullName() : wfmStrings.notAvailable()) + "</span>";
            }
        }, wfmStrings.modifiedBy());
    }

    @Override
    public void refresher() {
        CRMService.App.get().getCaseChangeHistory(caseID, new AsyncCallback<CaseList>() {
            @Override
            public void onFailure(Throwable caught) {
                WfmWindow.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(CaseList result) {
                if (result != null && result.getList() != null && result.getList().size() > 0) {
                    supplyProvider(result.getList().toArray(new CaseItem[]{}));
                    reDrawItems();
                }
            }
        });
    }
}
