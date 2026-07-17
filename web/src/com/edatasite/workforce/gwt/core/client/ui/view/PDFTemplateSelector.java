package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.dom.client.Style;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 17, 2011
 * Time: 3:10:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class PDFTemplateSelector {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private KpiModal dialogBox;

    public PDFTemplateSelector(String templateType, final ExtendedCommand listener) {
        AllInOneService.App.get().getCompanyPdfTemplate(templateType, new AbstractAsyncCallback<PdfTemplateItemList>() {
            @Override
            public void failure(Throwable throwable) {
                listener.execute(null);
            }

            @Override
            public void success(PdfTemplateItemList itemList) {
                if (itemList != null && itemList.getItems() != null && itemList.getItems().length > 0) {
                    initForm(listener, itemList, null);
                } else {
                    listener.execute(null);
                }
            }
        });
    }

    public PDFTemplateSelector(String templateType, Integer templateId, final ExtendedCommand listener) {
        AllInOneService.App.get().getCompanyPdfTemplate(templateType, new AbstractAsyncCallback<PdfTemplateItemList>() {
            @Override
            public void failure(Throwable throwable) {
                listener.execute(null);
            }

            @Override
            public void success(PdfTemplateItemList itemList) {
                if (itemList != null && itemList.getItems() != null && itemList.getItems().length > 0) {
                    initForm(listener, itemList, templateId);
                } else {
                    listener.execute(null);
                }
            }
        });
    }

    public PDFTemplateSelector(PdfTemplateItemList itemList, ExtendedCommand listener) {
        if (itemList != null && itemList.getItems() != null && itemList.getItems().length > 0) {
            if (itemList.getItems().length == 1) {
                listener.execute(null);
            } else {
                initForm(listener, itemList, null);
            }
        }
    }

    private void initForm(final ExtendedCommand listener, PdfTemplateItemList itemList, Integer templateId) {
        dialogBox = new KpiModal();
        dialogBox.setWidth(325);
        dialogBox.setTitle(wfmStrings.pleaseSelectTemplate());

        final WfmDropdown dropdown = new WfmDropdown();
        dropdown.setWidth("270px");
        dropdown.setItems(Arrays.asList(itemList.getItems()));
        if (itemList.getItems().length == 1) {
//            dropdown.setSelected(itemList.getItems()[0].getId());
            listener.execute(itemList.getItems()[0].getId());
            dialogBox.close();
        }
        if (templateId != null) {
            dropdown.setSelected(templateId);
        } else if (itemList.getDefaultTemplateID() != null) {
            dropdown.setSelected(itemList.getDefaultTemplateID());
        }
        dropdown.getElement().getStyle().setPaddingBottom(15, Style.Unit.PX);

        WfmButton2 generate = new WfmButton2(wfmStrings.generate(), WfmButton2.BTN_PRIMARY);
        generate.addClickHandler(event -> {
            listener.execute(dropdown.getSelectedId());
            dialogBox.close();
        });


        dialogBox.add(dropdown);
        dialogBox.setCloseButton(true);
        dialogBox.addButton(generate);
        dialogBox.open();
    }
}
