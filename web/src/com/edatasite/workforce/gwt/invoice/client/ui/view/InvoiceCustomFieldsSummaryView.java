package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUploadItem;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER_TIME;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_QUOTE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_QUOTE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PURCHASE_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PURCHASE_ORDER_SUMMARY;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 3/2/12
 * Time: 11:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceCustomFieldsSummaryView {

    //    private PreviewSectionField customsDataView;
    private MaterialPanel container;
    private static final NumberFormat numberFormat = NumberFormat.getFormat("###.##");

    public InvoiceCustomFieldsSummaryView(List<CompanyCustomFieldItem> customFields) {
        initialize(customFields);
    }

    private void initialize(List<CompanyCustomFieldItem> customFields) {
//        customsDataView = new PreviewSectionField("15%", "18%");
        container = new MaterialPanel("wfmform__container");

        for (CompanyCustomFieldItem customField : customFields) {
            if (customField != null) {
                HTML html = new HTML();
                Widget widget = null;
                if (Constants.DATA_TYPE_DATE.equals(customField.getDataType())) {
                    if (UI_TYPE_DATEPICKER_TIME.equals(customField.getUiType())) {
                        html.setHTML(customField.getFieldDateNonConvertedValue() != null ? Utils.refactorDateTime(customField.getFieldDateNonConvertedValue().getNonConvertedDate(), false) : "");
                    } else {
                        html.setHTML(customField.getFieldDateNonConvertedValue() != null ? Utils.refactor(customField.getFieldDateNonConvertedValue().getNonConvertedDate(), false) : "");
                    }
                } else if (Constants.DATA_TYPE_NUMBER.equals(customField.getDataType())
                        && Constants.UI_TYPE_TEXTBOX.equals(customField.getUiType())) {
                    if (customField.getFieldStringValue() != null) {
                        try {
                            html.setHTML(numberFormat.format(Double.valueOf(customField.getFieldStringValue())));
                        } catch (NumberFormatException e) {
                        }
                    }
                } else if (Constants.DATA_TYPE_NUMBER.equals(customField.getDataType())
                        && Constants.UI_TYPE_PERCENTAGE.equals(customField.getUiType())) {
                    if (customField.getFieldStringValue() != null) {
                        try {
                            html.setHTML(numberFormat.format(Double.valueOf(customField.getFieldStringValue())) + " %");
                        } catch (NumberFormatException e) {
                        }
                    }
                } else if (Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(customField.getUiType()) || Constants.UI_TYPE_MULTI_LOOKUP.equals(customField.getUiType())) {
                    String finalValue = "";
                    if (customField.getSelectItems() != null && customField.getSelectItems().size() > 0) {
                        for (SelectItem item : customField.getSelectItems()) {
                            finalValue += item.getName() + "; ";
                        }
                    }
                    html.setHTML(finalValue);
                } else if (Constants.UI_TYPE_LOOKUP.equals(customField.getUiType())) {
                    if (CustomFieldLookUpTypeEnum.PROJECT.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("ProjectManagement.html#project|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        }
                    } else if (CustomFieldLookUpTypeEnum.TASK.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("ProjectManagement.html#task|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        }
                    } else if (CustomFieldLookUpTypeEnum.OPPORTUNITY.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("Crm.html#opportunity|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        }
                    } else if (CustomFieldLookUpTypeEnum.PURCHASE_ORDER.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null && Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_ORDER_SUMMARY : ACCOUNTING_PURCHASE_ORDER_SUMMARY)) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("Accounting.html#purchaseorder|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        } else {
                            html.setHTML(customField.getFieldStringValue());
                        }
                    } else if (CustomFieldLookUpTypeEnum.PURCHASE_INVOICE.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null && Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_INVOICE_SUMMARY : ACCOUNTING_PURCHASE_INVOICE_SUMMARY)) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("Accounting.html#purchaseinvoice|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        } else {
                            html.setHTML(customField.getFieldStringValue());
                        }
                    } else if (CustomFieldLookUpTypeEnum.SALES_QUOTE.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null && Utils.isCRM() ? Utils.hasPermission(CRM_SALES_QUOTE_SUMMARY) : (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_SUMMARY))) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("Accounting.html#salequote|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        } else {
                            html.setHTML(customField.getFieldStringValue());
                        }
                    } else if (CustomFieldLookUpTypeEnum.SALES_INVOICE.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null && Utils.isCRM() ? Utils.hasPermission(CRM_SALES_INVOICE_SUMMARY) : (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SUMMARY))) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("Accounting.html#saleinvoice|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        } else {
                            html.setHTML(customField.getFieldStringValue());
                        }
                    } else if (CustomFieldLookUpTypeEnum.CASE.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("Crm.html#case|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        } else {
                            html.setHTML(customField.getFieldStringValue());
                        }
                    } else if (CustomFieldLookUpTypeEnum.CONTACT.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("Crm.html#contact|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        } else {
                            html.setHTML(customField.getFieldStringValue());
                        }
                    } else if (CustomFieldLookUpTypeEnum.EMPLOYEE.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("Hrms.html#employeeProfile|employeeProfileView/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        } else {
                            html.setHTML(customField.getFieldStringValue());
                        }
                    } else if (CustomFieldLookUpTypeEnum.LEAD.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("Crm.html#lead|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        } else {
                            html.setHTML(customField.getFieldStringValue());
                        }
                    } else if (CustomFieldLookUpTypeEnum.CUSTOMER.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("Accounting.html#client|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        } else {
                            html.setHTML(customField.getFieldStringValue());
                        }
                    } else if (CustomFieldLookUpTypeEnum.SUPPLIER.equals(customField.getLookUpTypeEnum())) {
                        if (customField.getSelectedId() != null) {
                            html.addClickHandler(click -> {
                                Utils.openURLCurrentTab("Accounting.html#suppliersummary|summary/" + customField.getSelectedId());
                            });
                            html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                        } else {
                            html.setHTML(customField.getFieldStringValue());
                        }
                    } else {
                        html.setHTML(customField.getFieldStringValue());
                    }

                } else if (Constants.UI_TYPE_URL.equals(customField.getUiType())) {
                    if (customField.getFieldStringValue() != null) {
                        html.addClickHandler(click -> {
                            String url = "";
                            if (customField.getFieldStringValue().contains("https://")) {
                                url = customField.getFieldStringValue().split("https://")[1];
                            } else if (customField.getFieldStringValue().contains("http://")) {
                                url = customField.getFieldStringValue().split("http://")[1];
                            } else {
                                url = customField.getFieldStringValue();
                            }

                            Window.open("//" + url, "_blank", null);
                        });
                        html.setHTML(customField.getFieldStringValue() != null ? "<a href=\"javascript:\">" + customField.getFieldStringValue() + "</a>" : "");
                    }
                } else {
                    html.setHTML(customField.getFieldStringValue());
                }
//                if (Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(customField.getUiType())) {
//                    ProfileImage profileImage = new ProfileImage();
//                    profileImage.getImageById(customField.getProfielImageId(), "Profile", "Image", true);
//                    container.add(generateField(customField.getFieldName(), profileImage));
//                }
                if (Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(customField.getUiType())) {
                    GeneralFileUploadItem generalFileUploadItem = new GeneralFileUploadItem(Constants.F_CUSTOM_FIELD_ITEM);
                    generalFileUploadItem.setViewMode();
                    if (customField.getFieldStringValue() != null) {
                        generalFileUploadItem.setFiles(Double.valueOf(customField.getFieldStringValue()).intValue(), customField.getObjectId(), true);
                    }
                    container.add(generateField(customField.getFieldName(), generalFileUploadItem));
//                    customsDataView.addFileUploadField(Utils.refactor(customField.getFieldName(), false), generalFileUploadItem);
                } else {
                    if (html.getHTML() != null) {

                        widget = html;
                        widget.addStyleName("field");
                        widget.addStyleName("form-control");
                        widget.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.PRE_LINE);
                        if ((html.getHTML().contains("http:") || html.getHTML().contains("https:"))) {
                            final String finalValue = html.getHTML();
                            widget = new SimpleLink(html.getHTML());
                            ((SimpleLink) widget).addClickHandler(event -> Utils.openURL(finalValue));
                        }
                    } else {
                        widget = new HTML("N/A");
                        widget.addStyleName("field");
                    }
                    container.add(generateField(customField.getFieldName(), widget));
//                    customsDataView.addField(Utils.refactor(customField.getFieldName(), false), widget);
                }
            }
        }
    }

    private FormGroup generateField(String fieldName, Widget widget) {
        /*MaterialPanel formGroup = new MaterialPanel("form-group");

        Label fieldLabel = new Label(Utils.refactor(fieldName, false));
        fieldLabel.addStyleName("form-group__label");
        MaterialPanel wrapper = new MaterialPanel("control-wrapper");
        wrapper.add(widget);

        formGroup.add(fieldLabel);
        formGroup.add(wrapper);*/
        FormGroup formGroup = new FormGroup(fieldName, widget);
        if (fieldName.length() > 50) {
            formGroup.setWidth("334px");
        }
        formGroup.getGroupLabel().getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
        return formGroup;
    }

    public MaterialPanel getCustomsDataView() {
        return container;
    }
}
