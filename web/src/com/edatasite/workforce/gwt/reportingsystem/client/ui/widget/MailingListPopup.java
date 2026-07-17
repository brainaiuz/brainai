package com.edatasite.workforce.gwt.reportingsystem.client.ui.widget;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.MailListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 5/22/12
 * Time: 7:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class MailingListPopup extends KpiModal implements Constants {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private TextBox name;
    private TextArea description;
    private KpiCheckBox status;

    private ReportRpc report;
    private boolean initialized = false;

    public MailingListPopup(ReportRpc report) {
        this.report = report;
    }

    public void init(final String maillisttype) {
        setTitle("Create Maling List");

        final WfmForm form = new WfmForm(new String[]{"7%", "68%", "25%"});
        form.setCellPadding(0);
        form.setCellSpacing(5);
        form.setLabelSize("100px");
        form.setLabelAlignment(WfmForm.ALIGN_RIGHT);

        name = new TextBox();

        status = new KpiCheckBox();
        status.setValue(true);

        description = new TextArea();

        form.addField(wfmStrings.name(), name, true);
        form.addField(wfmStrings.description(), description, false);
        form.addField(wfmStrings.status(), status, false);
        form.setStyleName("mailinglist");

        Label note = new Label("  * " + wfmStrings.noteForCreateMailist());
        note.setWidth("370px");
        note.getElement().getStyle().setPaddingLeft(5, com.google.gwt.dom.client.Style.Unit.PX);
        note.getElement().setAttribute("style", "padding-top:10px;padding-bottom:10px;");
        VerticalPanel vp = new VerticalPanel();
        vp.setStyleName("workforce");
        vp.add(form);
        vp.add(note);

        Button btnOk = new Button(wfmStrings.createMailingList());
        btnOk.ensureDebugId("button_Create_Mailing_List");
        Button btnCancel = new Button(wfmStrings.cancel());
        btnCancel.ensureDebugId("button_cancel");

        btnOk.addClickHandler(event -> {
            if (name.getText() == null || "".equals(name.getText())) {
                Window.alert(wfmStrings.pleaseEnterMailingListName());
                return;
            }

            final MailListRpc item = new MailListRpc();
            item.setName(name.getText());
            item.setDescription(description.getText());
            item.setActive(status.getValue());
            item.setMailListType(maillisttype);

            LoadingPanel.loading(true);
            close();
            ReportingService.App.get().saveMailList(item, report, new AbstractAsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Window.alert(wfmStrings.errorOccurredSavingChanges()+":" + caught);
                }

                @Override
                public void onSuccess(Boolean result) {
                    LoadingPanel.loading(false);
                    name.setText("");
                    description.setText("");
                    status.setValue(true);
                }
            });
        });
        btnCancel.addClickHandler(event -> close());

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(5);
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnOk);
        btnOk.getElement().setAttribute("style", "margin-left:10px;");
        vp.add(buttonPanel);
        vp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);

        add(vp);
        initialized = true;
    }

    public boolean isInit() {
        return initialized;
    }

    public void setReport(ReportRpc report) {
        this.report = report;
    }


}
