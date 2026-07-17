package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Azam Ahmadjonov
 * Date: 10/14/19
 * Time: 10:58 PM
 */
public class CustomFormItemPDFTemplateSelector {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private KpiModal dialogBox;

    public CustomFormItemPDFTemplateSelector(String templateType, String formId, final ExtendedCommand listener) {
        AllInOneService.App.get().getCompanyPdfTemplatesWithFormId(templateType, formId, new AbstractAsyncCallback<CustomFormItemPdfTemplateList>() {
            @Override
            public void failure(Throwable throwable) {
                listener.execute(null);
            }

            @Override
            public void success(CustomFormItemPdfTemplateList itemList) {
                if (itemList != null && itemList.getItems() != null && itemList.getItems().length > 0) {
                    initForm(listener, itemList);
                } else {
                    listener.execute(null);
                }
            }
        });
    }

    private void initForm(final ExtendedCommand listener, CustomFormItemPdfTemplateList itemList) {
        dialogBox = new KpiModal();
        dialogBox.setWidth("325px");
        dialogBox.setTitle(wfmStrings.pleaseSelectTemplate());

        final WfmDropdown dropdown = new WfmDropdown();
        dropdown.setItems(Arrays.asList(itemList.getItems()));
        if (itemList.getDefaultTemplateID() != null) {
            dropdown.setSelected(itemList.getDefaultTemplateID());
        }
        dropdown.getElement().getStyle().setPaddingBottom(15, Style.Unit.PX);

        WfmButton2 generate = new WfmButton2(wfmStrings.generate(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            listener.execute(dropdown.getSelectedId());
            dialogBox.close();
        });
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), clickEvent -> dialogBox.close());

        FlexTable mainTable = new FlexTable();
        mainTable.setWidget(0, 0, new HTML("<b>" + wfmStrings.pleaseSelectTemplate() + "</b>"));
        mainTable.setWidget(1, 0, dropdown);

        dialogBox.add(dropdown);
        dialogBox.addButton(cancel);
        dialogBox.addButton(generate);

        dialogBox.open();
    }
}