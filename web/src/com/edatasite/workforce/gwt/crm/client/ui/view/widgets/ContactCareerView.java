package com.edatasite.workforce.gwt.crm.client.ui.view.widgets;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.ContactCareerItem;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddContactCareerView;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 27.04.12
 * Time: 15:32
 */
public class ContactCareerView {
    private final FlowPanel careerPanel;
    private final FlowPanel careerListPanel;
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final View view;
    private boolean isWrite;
    private final SimpleLink addCareerLink;
    private final Integer contactID;

    public ContactCareerView(final Integer contactID, View view) {
        this.contactID = contactID;
        this.view = view;
        careerPanel = new FlowPanel();
        careerListPanel = new FlowPanel();
        Image image = new Image();
        image.setSize("15px", "15px");
        addCareerLink = new SimpleLink(image, crmStrings.addCareer());
        addCareerLink.addStyleName("tabBarLinks");
        addCareerLink.addClickHandler(clickEvent -> new AddContactCareerView(null, contactID, true));
        careerPanel.add(careerListPanel);
        careerPanel.add(addCareerLink);
    }

    private void draw() {
        ContactService.App.get().getContactCareers(contactID, new AbstractAsyncCallback<ContactCareerItem[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
//                drawEmptyMessage();
//                getEmptyPanel(crmStrings.thereAreNoCareerInformationYet(), null, null);
            }

            @Override
            public void success(ContactCareerItem[] result) {
                LoadingPanel.loading(false);
                if (result != null && result.length > 0) {
                    drawCareerInformation(result);
                } //else {
//                    drawEmptyMessage();
//                }
            }
        });
    }

    public Widget getCareerPanel() {
        viewShow();
        return careerPanel;
    }

    private void drawCareerInformation(ContactCareerItem[] careerItems) {
        careerListPanel.clear();
        careerListPanel.setHeight("150px");
        careerListPanel.getElement().getStyle().setOverflowY(Style.Overflow.AUTO);
        for (final ContactCareerItem career : careerItems) {
            final FlexTable flexTable = new FlexTable();
            flexTable.setCellSpacing(5);
            flexTable.setCellPadding(5);
            flexTable.setWidth("100%");
            FlowPanel editPanel = new FlowPanel();
            MaterialLink editImage = new MaterialLink();
            editImage.setStyleName("pointer ficon--edit");
            editImage.getElement().getStyle().setMarginRight(3, Style.Unit.PX);
            editImage.setTitle(wfmStrings.edit());
            editImage.addClickHandler(event -> new AddContactCareerView(career.getCareerID(), career.getContactID(), false));
            MaterialLink deleteImage = new MaterialLink();
            deleteImage.setStyleName("pointer ficon--remove");
            deleteImage.setHeight("10px");
            deleteImage.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
            deleteImage.setTitle(wfmStrings.delete());
            deleteImage.addClickHandler(event -> confirmAndDeleteCareer(career.getCareerID(), flexTable));
            if (isWrite) {
                editPanel.add(editImage);
                editPanel.add(deleteImage);
            }

            flexTable.setHTML(0, 0, career.getJobTitle() + " " + crmStrings.atMessage() + " " + career.getCompanyName());
            flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
            flexTable.setHTML(1, 0, refactor(career.getIndustryName()) + " | " + refactor(career.getCity()) + ", " + refactor(career.getCountryName()));
            flexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
            String toYearDate = "";
            if (career.getToYear() != null && !career.isCurrentYear()) {
                toYearDate = DateUtils.format(career.getToYear());
            } else if (career.isCurrentYear()) {
                toYearDate = wfmStrings.current();
            }
            flexTable.setHTML(2, 0, DateUtils.format(career.getFromYear()) + " - " + toYearDate);
            flexTable.setWidget(2, 1, editPanel);
            flexTable.getFlexCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_RIGHT);
            FlowPanel fp = new FlowPanel();
            fp.getElement().setAttribute("style", "width:100%;border-bottom:1px dotted #667788;height:3px;");
            flexTable.setWidget(3, 0, fp);
            flexTable.getFlexCellFormatter().setColSpan(3, 0, 2);
            careerListPanel.add(flexTable);
        }
    }

    public void viewShow() {
        draw();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_CAREER_ADD_EDIT, view, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_CAREER_DELETE, view, (sender, args) -> draw());
    }

    private void confirmAndDeleteCareer(final Integer careerID, final FlexTable flexTable) {
        final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo,
                                                              crmStrings.areYouSureYouWanttoDeleteThiCareerInformation(), new CloseHandler() {
            @Override
            public void onCancel() {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSubmit() {
                ContactService.App.get().deleteContactCareer(careerID, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        flexTable.removeFromParent();
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_CAREER_DELETE, result, view);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.careerInformation()), Info.Type.INFO);
                    }
                });
            }
        });
        wfmMessageBox.setTitle(wfmStrings.confirmation());
        wfmMessageBox.open();
    }

    public String refactor(String s) {
        if (s != null && !"".equals(s)) {
            return s;
        }
        return wfmStrings.notAvailable();
    }

    public void setWrite(boolean write) {
        isWrite = write;
    }
}
