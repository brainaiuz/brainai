package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.FREE_TRIAL_DAYS_LEFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.IS_PAID_COMPANY;

public class ChooseCompanyWidget extends Div {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final String COMPANY_ACTIVATOR = "compactivator";
    private MaterialDropDown companies;

    public ChooseCompanyWidget() {
        super("profile-companies");
        initSwitchCompanyList();
    }

    private List<UserCompanyDTO> companySortAction(List<UserCompanyDTO> companyList) {
        return companyList.stream()
                .sorted(Comparator.comparing(UserCompanyDTO::getCompanyName))
                .collect(Collectors.toList());
    }

    private void companyPanel(List<UserCompanyDTO> companyList) {
        if (companyList != null && companyList.size() > 0) {
            if (companyList.size() > 1) {
                MaterialLink companiesLink = new MaterialLink();
                companiesLink.setId("compLink");
                companiesLink.addStyleName("dropdown-button");
                Span span = new Span(wfmStrings.switchCompany());
                SvgIcon chevronIcon = new SvgIcon(SvgEnum.chevronRight);
                companiesLink.add(span);
                companiesLink.add(chevronIcon);
                add(companiesLink);
                for (UserCompanyDTO company : companyList) {
                    MaterialLink comp = new MaterialLink();
                    Span check = new Span();
                    check.addStyleName("prof-comp-list__check");
                    KpiRadioButton radio = new KpiRadioButton("lay_comp");
                    check.add(radio);
                    comp.add(check);
                    Span text = new Span(company.getCompanyName());
                    text.addStyleName("prof-comp-list__text");
                    comp.add(text);
                    Span mark = new Span();

                    mark.addStyleName("prof-comp-list__mark");
                    if ("expired".equals(company.getStatus())) {
                        comp.addStyleName("status--expired");
                    } else if ("active".equals(company.getStatus())) {
                        comp.addStyleName("status--active");
                    } else if ("free".equals(company.getStatus())) {
                        comp.addStyleName("status--free");
                    }
                    Icon i = new Icon();
                    mark.add(i);
                    comp.add(mark);
                    if (company.isCurrent()) {
                        comp.addStyleName("active");
                        radio.setValue(true);
                    } else {
                        comp.setHref(company.getClusterURL());
                    }
                    MainLayout.get().getSideNavBar().addToCompaniesDiv(companiesLink, comp);
                }
            }
            initCurrentCompany(companyList.stream().filter(UserCompanyDTO::isCurrent).findFirst().get());
        }
    }

    private void initCompanyPanel(LinkedHashMap<String, ArrayList<UserCompanyDTO>> companyList) {
        List<List<UserCompanyDTO>> companies = new ArrayList<>();
        companies.add(companyList.get("active"));
        companies.add(companyList.get("free"));
        companies.add(companyList.get("expired"));

        List<UserCompanyDTO> comList = new ArrayList<>();
        for (List<UserCompanyDTO> list : companies) {
            if (list != null) {
                comList.addAll(companySortAction(list));
            }
        }
        companyPanel(comList);
    }

    private void initCurrentCompany(UserCompanyDTO company) { //TODO wish I could remove this method
        Div cPlate = new Div("company-plate");

        boolean free = "free".equals(company.getStatus());
        boolean expired = "expired".equals(company.getStatus());
        boolean active = "active".equals(company.getStatus());

        if (free) {
            cPlate.addStyleName("company-plate--free-trial");
        } else if (active) {
            cPlate.addStyleName("company-plate--active");
        } else if (expired) {
            cPlate.addStyleName("company-plate--expired");
        }
        Div cPlateHead = new Div("company-plate__header");
        Div iconDiv = new Div();
        SvgIcon companyIcon = new SvgIcon(SvgEnum.company);
        iconDiv.add(companyIcon);
        cPlateHead.add(iconDiv);
        Boolean isPaidCompany = Utils.getParam(IS_PAID_COMPANY) != null ? Boolean.valueOf(Utils.getParam(IS_PAID_COMPANY)) : Boolean.FALSE;
        Integer days = Integer.valueOf(Utils.getParam(FREE_TRIAL_DAYS_LEFT));
        Span statusSpan = new Span();
        statusSpan.addStyleName("company-plate__status");
        if (free) {
            if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
                statusSpan.setText(wfmStrings.freeTrialButton());
            }
            cPlateHead.add(statusSpan);
        } else if (expired) {
            statusSpan.setText(wfmStrings.expired());
            cPlateHead.add(statusSpan);
        }
        Div cNameDiv = new Div("company-plate__name");
        Span cNameSpan = new Span(company.getCompanyName());
        Div compId = new Div("company-plate__id");
        compId.getElement().setInnerHTML(wfmMessages.id(company.getCompanyID()));
        cNameDiv.add(cNameSpan);
        cNameDiv.add(compId);
        cPlate.add(cPlateHead);
        cPlate.add(cNameDiv);
        if (Utils.hasRole(Constants.ADMIN) && (free || expired)) {
            if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
                WfmButton2 subscribeNow = new WfmButton2(wfmStrings.subscribeNow(), "company-plate__btn btn btn-medium", e -> Utils.redirect(GWT.getHostPageBaseURL() + "Myaccount.html"));
                cPlate.add(subscribeNow);
            }
        }
        add(cPlate);
    }

    protected void initSwitchCompanyList() {
        Scheduler.get().scheduleDeferred(() -> LoginService.App.get().getUserCompanyList(new AsyncCallback<LinkedHashMap<String, ArrayList<UserCompanyDTO>>>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(LinkedHashMap<String, ArrayList<UserCompanyDTO>> companyList) {
                initCompanyPanel(companyList);
            }
        }));
    }

    public void setCompanyList(ArrayList<UserCompanyDTO> list) {
        companies.clear();
    }

}
