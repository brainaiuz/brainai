package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.employee;

import com.edatasite.workforce.gwt.core.client.ClipboardUtil;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.services.dto.EmployeeItem;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;

import java.util.Optional;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_PROFILE_VIEW;

public class EmployeePopup extends PopupPanel {

    @UiField
    FlowPanel container;
    @UiField
    FlowPanel avatar;
    @UiField
    ListItem employeeName;
    @UiField
    MaterialLink emailLink;
    @UiField
    MaterialLink tgLink;
    @UiField
    MaterialLink phoneLink;
    @UiField
    FlowPanel buttonContainer;
    @UiField
    Anchor emailCopy;
    @UiField
    Anchor tgCopy;
    @UiField
    Anchor phoneCopy;

    interface Binder extends UiBinder<Widget, EmployeePopup> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public EmployeePopup(EmployeeItem emp, String avatarUrl) {
        super(true, false);
        addStyleName("profilePop profilePop--orgchart panel");
        getElement().setAttribute("style", "display:block");

        setWidget(binder.createAndBindUi(this));

        if (avatarUrl == null || avatarUrl.isEmpty()) {
            String[] empNameArr = emp.getName().trim().replace("  ", " ").split(" ");
            StringBuilder name = new StringBuilder();
            if (empNameArr[0] != null && !empNameArr[0].trim().isEmpty()) {
                name.append(empNameArr[0].charAt(0));
            }
            if (empNameArr[1] != null && !empNameArr[1].trim().isEmpty()) {
                name.append(empNameArr[1].charAt(0));
            }
            Span span = new Span(name.toString().toUpperCase());
            span.addStyleName("avatar js-profile-avatar");
            avatar.add(span);
        } else {
            Span span = new Span();
            span.addStyleName("avatar js-profile-avatar");
            span.getElement().setAttribute("style", "background-image: url(" + avatarUrl + ")");
            MaterialImage avatarImage = new MaterialImage();
            avatarImage.setUrl(avatarUrl);
            span.add(avatarImage);
            avatar.add(span);
        }

        String fullName = Optional.ofNullable(emp.getName()).orElse("");
        Heading name = new Heading(HeadingSize.H3);
        name.setText(SafeHtmlUtils.fromString(fullName).asString());
        employeeName.add(name);

        SvgIcon emailIcon = new SvgIcon(SvgEnum.mail);
        Span email = new Span(SafeHtmlUtils.fromString(emp.getEmail() != null ? emp.getEmail() : "").asString());
        email.addStyleName("js-profile-email");
        emailLink.add(emailIcon);
        emailLink.add(email);

        ListItem tgItem = new ListItem();
        tgItem.addStyleName("profilePop__address");

        SvgIcon tgIcon = new SvgIcon(SvgEnum.telegram);
        Span telegram = new Span(SafeHtmlUtils.fromString(emp.getTgNumber() != null ? emp.getTgNumber() : "").asString());
        telegram.addStyleName("js-profile-telegram");
        tgLink.add(tgIcon);
        tgLink.add(telegram);

        SvgIcon phoneIcon = new SvgIcon(SvgEnum.phone);
        Span phone = new Span(SafeHtmlUtils.fromString(emp.getPhoneNumber() != null ? emp.getPhoneNumber() : "").asString());
        phone.addStyleName("js-profile-phone");
        phoneLink.add(phoneIcon);
        phoneLink.add(phone);

        if (emp.getEmail() != null && !emp.getEmail().isEmpty()) {
            emailCopy.addClickHandler(cl -> {
                boolean ok = ClipboardUtil.copy(emp.getEmail());
                if (ok) {
                    Info.show(hrmsStrings.copied());
                } else {
                    Info.show(hrmsStrings.copyFailed());
                }
            });
        } else {
            emailLink.setVisible(false);
            emailCopy.setVisible(false);
        }

        if (emp.getTgNumber() != null && !emp.getTgNumber().isEmpty()) {
            tgCopy.addClickHandler(cl -> {
                boolean ok = ClipboardUtil.copy(emp.getTgNumber());
                if (ok) {
                    Info.show(hrmsStrings.copied());
                } else {
                    Info.show(hrmsStrings.copyFailed());
                }
            });
        } else {
            tgLink.setVisible(false);
            tgCopy.setVisible(false);
        }

        if (emp.getPhoneNumber() != null && !emp.getPhoneNumber().isEmpty()) {
            phoneCopy.addClickHandler(cl -> {
                boolean ok = ClipboardUtil.copy(emp.getPhoneNumber());
                if (ok) {
                    Info.show(hrmsStrings.copied());
                } else {
                    Info.show(hrmsStrings.copyFailed());
                }
            });
        } else {
            phoneLink.setVisible(false);
            phoneCopy.setVisible(false);
        }

        WfmButton2 button = new WfmButton2(hrmsStrings.openProfile(), WfmButton2.BTN_PRIMARY, clickEvent -> goToEmployeeProfile(emp));
        buttonContainer.add(button);
    }

    private void goToEmployeeProfile(EmployeeItem emp) {
        SinksContainerFactory.entryPoint.onHistoryChanged("employeeProfile|" + EMPLOYEE_PROFILE_VIEW + "/" + emp.getId());
    }
}
