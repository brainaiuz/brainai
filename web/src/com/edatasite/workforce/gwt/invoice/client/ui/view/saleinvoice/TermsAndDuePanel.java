package com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.view.InvoiceTermsLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.TermPopupView;
import com.google.gwt.user.client.ui.HorizontalPanel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Span;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/11/12
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class TermsAndDuePanel extends HorizontalPanel implements AccountingConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DatePicker dueDatePicker;
    private InvoiceTermsLookUp termsLookUp;
    private final String type;
    private boolean isRFQ;

    private FormGroup formGroup;
    private MaterialLink dueDateLink;
    private MaterialLink termsLink;

    private TermsAndDueProvider termsAndDueProvider;

    private Integer dueOrTermsType = DUE_TYPE;

    public TermsAndDuePanel(String type) {
        this.type = type;
        initialize();
    }

    public TermsAndDuePanel(String type, boolean isRFQ) {
        this.type = type;
        this.isRFQ = isRFQ;
        initialize();
    }

    private void initialize() {
        dueDateLink = new MaterialLink(type);
        dueDateLink.addClickHandler(ch -> {
            dueOrTermsType = DUE_TYPE;
            onTypeChange();
        });
        termsLink = new MaterialLink(wfmStrings.terms());
        termsLink.addClickHandler(ch -> {
            dueOrTermsType = TERMS_TYPE;
            onTypeChange();
        });

        formGroup = new FormGroup();
        formGroup.getGroupLabel().addStyleName("label-group");
        {
            formGroup.getGroupLabel().add(new Span(type));
            formGroup.getGroupLabel().add(termsLink);
        }

        dueDatePicker = new DatePicker(true);
        dueDatePicker.ensureDebugId("dueOrValidDate");
        termsLookUp = new InvoiceTermsLookUp();
        termsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> termsAndDueProvider.applyPaymentInstructionData());
        if (!isRFQ) {
            dueDatePicker.addChangeHandler(event -> termsAndDueProvider.applyPaymentInstructionData());
        }

        onTypeChange();
    }

    public void setData(Integer type, Date dueDate, InvoiceTermsItem termsItem) {
        dueOrTermsType = type;

        if (DUE_TYPE.equals(type) && dueDate != null) {
            dueDatePicker.setDate(dueDate);
        } else if (TERMS_TYPE.equals(type) && termsItem != null) {
            termsLookUp.addTermsItem(termsItem);
        }
        onTypeChange();
    }

    private void onTypeChange() {
        /*termsPanel.clear();TODO not completed yet
        actionButton.setText((DUE_TYPE.equals(dueOrTermsType) ? accountingStrings.due() : accountingStrings.terms()));
        termsPanel.add(DUE_TYPE.equals(dueOrTermsType) ? dueDatePicker : termsLookUp);

        if (termsAndDueProvider != null) {
            termsAndDueProvider.setDueDateAndTermsLabel(TERMS_TYPE.equals(dueOrTermsType) ? accountingStrings.terms() : accountingStrings.validDate());

            if (!termsAndDueProvider.isEditForm()) {
                termsAndDueProvider.applyPaymentInstructionData();
            }
        }*/
        formGroup.getGroupLabel().clear();
        formGroup.getGroupLabel().add(DUE_TYPE.equals(dueOrTermsType) ? new Span(type) : dueDateLink);
        formGroup.getGroupLabel().add(TERMS_TYPE.equals(dueOrTermsType) ? new Span(wfmStrings.terms()) : termsLink);

        formGroup.getGroupContent().clear();
        formGroup.getGroupContent().add(DUE_TYPE.equals(dueOrTermsType) ? dueDatePicker : termsLookUp);
    }

    public Date getDueDate() {
        if (DUE_TYPE.equals(dueOrTermsType)) {
            return DateUtil.getDayLastTime(dueDatePicker.getDate());
        } else {
            Date dueDate = null;
            InvoiceTermsItem selectedTerms = termsLookUp.getSelectedData();
            if (selectedTerms != null) {
                dueDate = DateUtil.addDays(termsAndDueProvider.getInvoiceDate(), selectedTerms.getDays());
            }
            return Utils.getDateEndTime(dueDate);
        }
    }

    public void setTermsAndDueProvider(TermsAndDueProvider termsAndDueProvider) {
        this.termsAndDueProvider = termsAndDueProvider;
    }

    private void showAddTermsDialogBox() {
        final TermPopupView viewPopup = new TermPopupView();
        viewPopup.setExternalCommand(() -> {
            if (viewPopup.getItem() != null) {
                setData(TERMS_TYPE, null, viewPopup.getItem());
                if (termsAndDueProvider != null)
                    termsAndDueProvider.applyPaymentInstructionData();
            }
        });
    }

    public InvoiceTermsItem getInvoiceTerms() {
        return termsLookUp.getSelectedData();
    }

    public boolean isDueTypeSelected() {
        return DUE_TYPE.equals(dueOrTermsType);
    }

    public boolean validate() {
        if (isDueTypeSelected()) {
            return Validation.validateDate(dueDatePicker);
        } else {
            return Validation.validateLookUpRequired(termsLookUp);
        }
    }

    public MaterialLink getAddTermLink() {
        return null;//TODO addTermLink;
    }

    public void applyCustomerTerms(InvoiceTermsItem termsItem) {
        setData(TERMS_TYPE, null, termsItem);
    }

    public FormGroup getTermsDueAsField() {
        return formGroup;
    }
}
