package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.customfields.EdsContractCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.ContractManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Azam on 2/20/19.
 */
public class ContractViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private ContractManager contractManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AllInOneService allInOneService;

    private final DecimalFormat numberFormat = new DecimalFormat("###.##");
    private final DecimalFormat priceFormat = new DecimalFormat(",##0.00");

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        RequestObject filterParametrs = (RequestObject) dataClass;
        return filterParametrs.getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        RequestObject requestObject = (RequestObject) dataClass;
        EdsContract edsContract = contractManager.get(requestObject.getObjectID());
        if (edsContract == null) {
            return null;
        }
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable contractTable = new CustomisedITextTable();

        String contractNumber = escapeHtml(edsContract.getNumber());
        String dateOfRegistration = edsContract.getCreationTime() != null ? shortDateFormat.format(edsContract.getCreationTime()) : "";
        String projectName = edsContract.getProject() != null ? escapeHtml(edsContract.getProject().getName()) : "";
        String projectNumber = edsContract.getProject() != null ? escapeHtml(edsContract.getProject().getNumber()) : "";
        String allowanceByCustomer = "";
        if (edsContract.getIsAccomodation() && edsContract.getIsFoot()) {
            allowanceByCustomer = allowanceByCustomer.concat("Accomodation, Food");
        } else if (edsContract.getIsAccomodation()) {
            allowanceByCustomer = allowanceByCustomer.concat("Accomodation");
        } else if (edsContract.getIsFoot()) {
            allowanceByCustomer = allowanceByCustomer.concat("Food");
        }
        String contractStartDate = edsContract.getStartDate() != null ? shortDateFormat.format(edsContract.getStartDate()) : "";
        String dueDate = edsContract.getDueDate() != null ? shortDateFormat.format(edsContract.getDueDate()) : "";

        String client = "";
        String clientContact = "";
        String clientBillAddress = "";
        String clientMailAddress = "";
        if (edsContract.getClient() != null) {
            client = escapeHtml(edsContract.getClient().getName());
            clientContact = edsContract.getClient().getPrimaryContact() != null ? escapeHtml(edsContract.getClient().getPrimaryContact().getName()) : "";
            List<EdsAddress> billAddress = edsContract.getClient().getBillingAddresses();
            List<EdsAddress> mailAddress = edsContract.getClient().getMailingAddresses();
            for (EdsAddress adrItem : billAddress) {
                clientBillAddress = escapeHtml(adrItem.getAddressDataAsHTML());
            }
            for (EdsAddress adrItem : mailAddress) {
                clientMailAddress = escapeHtml(adrItem.getAddressDataAsHTML());
            }
        }

        contractTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        contractTable.addRowWithCode("CONTRACT_NUMBER", commonLocalizer.localize(PdfLocalizationName.number), contractNumber);
        contractTable.addRowWithCode("CLIENT", commonLocalizer.localize(PdfLocalizationName.customer), client);
        contractTable.addRowWithCode("DATE_REGISTRATION", commonLocalizer.localize(PdfLocalizationName.dateOfRegistration), dateOfRegistration);
        contractTable.addRowWithCode(PROJECT_NAME, commonLocalizer.localize(PdfLocalizationName.project), projectName);
        contractTable.addRowWithCode(PROJECT_NUMBER, "", projectNumber);
        contractTable.addRowWithCode("ALLOWANCE_BY_CUSTOMER", commonLocalizer.localize("allowancebytheclient"), allowanceByCustomer);
        contractTable.addRowWithCode("CONTACT", "", clientContact);
        contractTable.addRowWithCode(BILL_ADDRESS, "", clientBillAddress);
        contractTable.addRowWithCode(MAIL_ADDRESS, "", clientMailAddress);
        contractTable.addRowWithCode(EXP_START_DATE, commonLocalizer.localize(PdfLocalizationName.startDate), contractStartDate);
        contractTable.addRowWithCode(EXP_END_DATE, commonLocalizer.localize(PdfLocalizationName.dueDate), dueDate);
        contractTable.addRowWithCode("CONTRACT_DETAILS", commonLocalizer.localize("contractDetails"), "");

        customData.put("CONTRACT_CONTENT_TABLE", contractTable);

        if (edsContract.getProjectPositions() != null && edsContract.getProjectPositions().size() > 0) {
            CustomisedITextTable projectPositonsTable = new CustomisedITextTable();
            projectPositonsTable.setName(commonLocalizer.localize("requirements"));
            projectPositonsTable.addColumn(POSITION, commonLocalizer.localize(PdfLocalizationName.position));
            projectPositonsTable.addColumn(CONTRACT_START_DATE, commonLocalizer.localize(PdfLocalizationName.contractStart));
            projectPositonsTable.addColumn(CONTRACT_END_DATE, commonLocalizer.localize(PdfLocalizationName.contractEnd));
            projectPositonsTable.addColumn("WORKERS_NO", pmLocalizer.localize("numberOfWorkers"));
            projectPositonsTable.addColumn("RATE", pmLocalizer.localize(PdfLocalizationName.rate));
            projectPositonsTable.addColumn("UNIT_QTY", pmLocalizer.localize("unitQTY"));

            for (EdsProjectPosition item : edsContract.getProjectPositions()) {
                String position = item.getPosition() != null ? escapeHtml(item.getPosition().getName()) : "";
                String startDate = item.getContractStartDate() != null ? shortDateFormat.format(item.getContractStartDate()) : "";
                String endDate = item.getContractEndDate() != null ? shortDateFormat.format(item.getContractEndDate()) : "";
                String workersNo = item.getNumberOfWorker() != null ? item.getNumberOfWorker() + "" : "";
                String rate = item.getUnitPrice() != null ? priceFormat.format(item.getUnitPrice()) : "";
                String unitQty = item.getUnitQTY() != null ? numberFormat.format(item.getUnitQTY()) : "";
                projectPositonsTable.addRow(position, startDate, endDate, workersNo, rate, unitQty);
            }
            customData.put("PROJECT_POSITION_TABLE", projectPositonsTable);
        }

        List<String> columnsValue = Lists.newArrayList();
        CustomisedITextTable notesTable = new CustomisedITextTable();
        notesTable.setName(pdfWfmMessageSource.localize("notesInformation"));
        notesTable.addColumn(SUBJECT, commonLocalizer.localize(PdfLocalizationName.subject));
        notesTable.addColumn(NAME, commonLocalizer.localize(PdfLocalizationName.name));
        notesTable.addColumn(DATE, commonLocalizer.localize(PdfLocalizationName.date));

        ArrayList<HistoryListItem> notes = allInOneService.getNotes(edsContract.getObjectID(), RelationItem.TYPE_CONTRACT);
        if (notes != null && notes.size() > 0) {
            for (HistoryListItem item : notes) {
                columnsValue.clear();
                columnsValue.add(item.getComment(true) != null ? getDescription(item.getComment(true)) : "");
                columnsValue.add(escapeHtml(item.getEmployee()));
                columnsValue.add(item.getEventDate() != null ? shortDateFormat.format(item.getEventDate()) : "");
                notesTable.addRow(columnsValue.toArray(new String[]{}));
            }
        }
        customData.put("NOTES_TABLE", notesTable);

        CustomisedITextTable contractCustomFieldTable = new CustomisedITextTable();
        contractCustomFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        contractCustomFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        EdsContractCustomFields edsContractCustomFields = edsContract.getContractCustomFields() != null ? edsContract.getContractCustomFields() : null;
        List<CompanyCustomFieldItem> customFieldItemsContract = CustomFieldsUtils.setRPCCustomFieldItems(edsContractCustomFields,
                                                               commonService.getCompanyCustomFields(ViewName.Contract));
        if (customFieldItemsContract != null && customFieldItemsContract.size() > 0) {
            for (CompanyCustomFieldItem customField : customFieldItemsContract) {
                switch (customField.getDataType()) {
                    case CompanyCustomFieldItem.DATE -> {
                        String dateValue = "";
                        if (customField.getFieldDateNonConvertedValue() != null) {
                            dateValue = shortDateFormat.format(customField.getFieldDateNonConvertedValue().getNonConvertedDate());
                        }
                        contractCustomFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), dateValue);
                    }
                    case CompanyCustomFieldItem.NUMBER -> {
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(customField.getFieldStringValue())) {
                            numberValue = numberFormat.format(Double.valueOf(customField.getFieldStringValue()));
                        }
                        contractCustomFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), numberValue);
                    }
                    default ->
                            contractCustomFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), escapeHtml(customField.getFieldStringValue()));
                }
            }
        }
        customData.put("CONTRACT_CUSTOM_FIELD", contractCustomFieldTable);
        pdfData.setCustomData(customData);

        return pdfData;
    }

    private String getDescription(String description) {
        if (!"".equals(description.trim())) {
            description = description.trim();
            org.jsoup.nodes.Document htmlDocument = Jsoup.parse(description);
            if (htmlDocument != null) {
                description = htmlDocument.text();
            }
        }
        return description;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(commonLocalizer.localize(PdfLocalizationName.contract) + "_" + dateFormat(new Date()));
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.CONTRACT;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        RequestObject requestObject = new RequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.contract);
    }
}
