package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.GroupPlacementManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.RotationItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupPlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupPlacementTableItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gson.Gson;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class GroupPlacementViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {
    @Autowired
    private GroupPlacementManager groupPlacementManager;
    @Autowired
    RotationItemTableManager rotationItemTableManager;
    @Autowired
    HrmsService hrmsService;
    @Autowired
    EmployeeManager employeeManager;
    @Autowired
    DepartmentManager departmentManager;
    @Autowired
    PositionManager positionManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private VacancyManager vacancyManager;


    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        EdsGroupPlacement groupPlacement = groupPlacementManager.get(filterParameter.getObjectId());
        GroupPlacementItem groupPlacementItem = hrmsService.getGroupPlacementItem(groupPlacement.getObjectID(), false);
        baseInvoice.setCustomNumberAndDatesTable(getGroupPlacementItemDetails(groupPlacementItem, filterParameter));
        baseInvoice.setCustomProductTable(getItemTable(groupPlacementItem));
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        customData.put("CUSTOM_FIELDS", getGroupPlacementCustomFields(groupPlacementItem));
        pdfData.setCustomData(customData);
        pdfData.setBaseInvoice(baseInvoice);
        return pdfData;
    }

    private CustomisedITextTable getGroupPlacementItemDetails(GroupPlacementItem groupPlacementItem, ListingFilterParameter filterParameter) {
        CustomisedITextTable customisedITextTable = new CustomisedITextTable();
        customisedITextTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        if (groupPlacementItem.getDate() != null) {
            customisedITextTable.addRowWithCode(DATE, "", format.format(groupPlacementItem.getDate()));
            customisedITextTable.addRowWithCode("DATE_RU", "", dateFormat.format(groupPlacementItem.getDate()));
        }
        customisedITextTable.addRowWithCode(NUMBER, "", groupPlacementItem.getPlacementCode());

        return customisedITextTable;
    }

    private CustomisedITextTable getGroupPlacementCustomFields(GroupPlacementItem rotationItem) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        ArrayList<CompanyCustomFieldItem> customFieldItems = rotationItem.getCustomFieldItems();
        for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
            switch (customFieldItem.getDataType()) {
                case CompanyCustomFieldItem.DATE -> {
                    String dateValue = "—";
                    EdsCompany company = userManager.getUser().getCompany();
                    if (customFieldItem.getFieldDateNonConvertedValue() != null) {
                        if ("DateTime".equals(customFieldItem.getUiType())) {
                            SimpleDateFormat longDateFormat = getCompanyLongDateFormat(company);
                            if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                Locale ruLocale = new Locale("ru", "RU");
                                SimpleDateFormat ruDateFormat = new SimpleDateFormat(longDateFormat.toPattern(), ruLocale);
                                dateValue = customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? ruDateFormat.format(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                            } else {
                                dateValue = customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? longDateFormat(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate(), true) : "—";
                            }
                        } else {
                            SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            dateValue = customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                        }
                    }
                    customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), dateValue);
                }
                case CompanyCustomFieldItem.TEXT -> {
                    if (UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
                        StringBuilder name = new StringBuilder();
                        if (StringUtils.isNotEmpty(customFieldItem.getFieldStringValue())) {
                            Gson gson = new Gson();
                            SelectItem[] object = gson.fromJson(customFieldItem.getFieldStringValue(), SelectItem[].class);
                            for (SelectItem data : object) {
                                name.append(data.getName().trim()).append(", ");
                            }
                            if (customFieldItem.getFieldName().equals("QR code link")) {
                                String qrCodeUrlLink = "" + customFieldItem.getFieldStringValue();
                                customFieldItem.setFieldStringValue(qrCodeUrlLink);
                            }
                        }
                        customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), name.toString().replaceAll(", $", ""));
                    } else if (TYPE_ENTITY_LOOKUP.equals(customFieldItem.getUiType())) {
                        String defaultValue = "";
                        if (StringUtils.isNotEmpty(customFieldItem.getFieldStringValue())) {
                            Integer id = null;
                            try {
                                id = Integer.valueOf(customFieldItem.getFieldStringValue());
                            } catch (final NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if (id != null && customFieldItem.getQueryItems() != null) {
                                for (final SelectItem selectItem : customFieldItem.getQueryItems()) {
                                    if (selectItem.getId().equals(id)) {
                                        defaultValue = escapeHtml(selectItem.getName());
                                        break;
                                    }
                                }
                            }
                        }
                        customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), escapeHtml(defaultValue));
                    } else if (customFieldItem.getUiType().equals(TYPE_ENTITY_MULTI_LOOKUP)) {
                        StringBuilder name = new StringBuilder();
                        if (StringUtils.isNotEmpty(customFieldItem.getFieldStringValue())) {
                            Gson gson = new Gson();
                            SelectItem[] object = gson.fromJson(customFieldItem.getFieldStringValue(), SelectItem[].class);
                            for (SelectItem data : object) {
                                name.append(data.getName().trim()).append(", ");
                            }
                            if (customFieldItem.getFieldName().equals("QR code link")) {
                                String qrCodeUrlLink = "" + customFieldItem.getFieldStringValue();
                                customFieldItem.setFieldStringValue(qrCodeUrlLink);
                            }
                        }
                        customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), name.toString().replaceAll(", $", ""));
                    } else {
                        if (customFieldItem.getLookUpTypeEnum() != null && customFieldItem.getLookUpTypeEnum().name().equals("DEPARTMENT") && customFieldItem.getSelectedId() != null) {
                            EdsEmployee leader = departmentManager.get(customFieldItem.getSelectedId()).getLeader();
                            customFieldTable.addRowWithCode(PDFConstants.DEPARTMENT_LEADER, "Department Leader", escapeHtml(leader.getFullName()));
                        }
                        String defaultValue = StringUtils.isNotBlank(customFieldItem.getFieldStringValue()) ? customFieldItem.getFieldStringValue() : "—";
                        customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), escapeHtml(defaultValue));
                    }
                }
                default -> {
                    String defaultValue = StringUtils.isNotBlank(customFieldItem.getFieldStringValue()) ? customFieldItem.getFieldStringValue() : "—";
                    customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), escapeHtml(defaultValue));
                }
            }

        }
        return customFieldTable;
    }


    private CustomisedITextTable getItemTable(GroupPlacementItem groupPlacementItem) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        CustomisedITextTable customisedITextTable = new CustomisedITextTable();
        customisedITextTable.addColumn(PDFConstants.LOCATION, propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location));
        customisedITextTable.addColumn(PDFConstants.TYPE, commonLocalizer.localize(PdfLocalizationName.type));
        customisedITextTable.addColumn(EMPLOYEE_NAME, commonLocalizer.localize(PdfLocalizationName.candidate));
        customisedITextTable.addColumn("DEPARTMENT", commonLocalizer.localize(PdfLocalizationName.department));
        customisedITextTable.addColumn("POSITION", commonLocalizer.localize(PdfLocalizationName.position));
        customisedITextTable.addColumn("VACANCY", commonLocalizer.localize(PdfLocalizationName.vacancy));
        customisedITextTable.addColumn("DATE", commonLocalizer.localize(PdfLocalizationName.effectiveDate));

        for (GroupPlacementTableItem item : groupPlacementItem.getGroupPlacementTableItems()) {
            ArrayList<String> row = new ArrayList<>();
            if (item.getLocation() != null) {
                EdsLocation location = locationManager.get(item.getLocation().getId());
                row.add(location.getName());
            } else {
                row.add("");
            }

            if (item.getType() != null) {
                String type = item.getType().equals(LookUpConstants.CANDIDATE_ID) ? commonLocalizer.localize("candidate") : commonLocalizer.localize("employee");
                row.add(type);

            } else {
                row.add("");
            }

            if (item.getType() != null && item.getCandidate() != null) {
                EdsCrmContact candidate = null;
                EdsEmployee employee = null;
                if (item.getType().equals(LookUpConstants.CANDIDATE_ID)) {
                    candidate = crmContactManager.get(item.getCandidate().getId());
                } else {
                    employee = employeeManager.get(item.getCandidate().getId());
                }

                row.add(candidate != null ? candidate.getFullName() : employee.getFullName());
            } else {
                row.add("");
            }

            if (item.getDepartment() != null) {
                EdsDepartment department = departmentManager.get(item.getDepartment().getId());
                row.add(department.getLocale() != null && department.getLocale().getEnglish() != null ? department.getLocale().getEnglish() : "");
            } else {
                row.add("");
            }

            if (item.getPosition() != null) {
                EdsPosition position = positionManager.get(item.getPosition().getId());
                row.add(position.getName());
            } else {
                row.add("");
            }
            if (item.getMatchedVacancy() != null) {
                EdsVacancy vacancy = vacancyManager.get(item.getMatchedVacancy().getId());
                row.add(vacancy.getName());
            } else {
                row.add("");
            }
            if (item.getEffectiveDate() != null) {
                row.add(dateFormat.format(item.getEffectiveDate().getDate()));
            } else {
                row.add("");
            }

            customisedITextTable.addRow(row.toArray(new String[]{}));

        }

        return customisedITextTable;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.isLandscape() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter requestObject = (ListingFilterParameter) dataClass;
        setFileName("Group Placement");
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof LeaveRequestObject) {
            return ((LeaveRequestObject) object).getPdfTemplateID();
        }
        return null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return "Group Placement";
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.GROUP_PLACEMENT;
    }
}
