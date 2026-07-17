package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.pdf.EdsPdfReference;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.gwt.backend.client.rpc.PDFSettingsTransObject;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.PdfReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice.SavedSaleInvoiceViewPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.mail.EdsTemplate;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ErrorTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.pdf.ITextGenericDataTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.pdf.PDFDetails;
import com.edatasite.workforce.rest.v2.release10.core.to.base.pdf.VelocityProcessedDataTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Company User Groups", description = "Company User Groups API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCompanyGroupControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiCompanyGroupControllerV2.class);

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private BackendServiceLocal backendServiceLocal;
    @Autowired
    private PdfReferenceManager pdfReferenceManager;

    @Operation(summary = "Get User Involved Groups", description = "Retrieves information about user involved in Groups (User will be obtained from token)")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of taxes")})
    @RequestMapping(value = "/user_groups", method = RequestMethod.GET)
    public Object getUserGroups() throws RestException {
        EdsUser user = employeeManager.getUser();
        List<EdsGroup> groups = groupManager.getUserGroups(user.getObjectID());
        ArrayList<SelectItemTO> groupItemsList = new ArrayList<>();
        for (EdsGroup group : groups) {
            groupItemsList.add(new SelectItemTO(group.getObjectID(), group.getName(), group.getConstantName()));
        }
        ResponseListData<SelectItemTO> result = new ResponseListData<>();
        result.setList(groupItemsList);
        return successResponse(result);
    }

    @RequestMapping(value = "/pdf_fields/{item_type}/{item_id}", method = RequestMethod.GET)
    public Object getPdfFields(@PathVariable("item_type") String item_type,
                               @PathVariable("item_id") Integer itemId) throws RestException {

        ITextGenericPdfData iTextGenericPdfData = new ITextGenericPdfData();

        /*ITextPdfViewTypeEnum pdfViewPdfViewType = ITextPdfViewTypeEnum.LISTTABLE;

        ITextTableList listTable = new ITextTableList(10);// uses only lists template
        listTable.addPdfTableRows(new CellData("string"));
        listTable.addPdfTableRows(new CellData("string"));
        listTable.addPdfTableRows(new CellData("..."));

        ITextSummaryView summaryView = new ITextSummaryView();// uses only views template
        summaryView.addTable(listTable);
        summaryView.addTable(listTable);

        ITextSummaryView[] summaryViewArray = new ITextSummaryView[1];// uses only views template
        summaryViewArray[0] = summaryView;

        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();// uses only accounting templates
        baseInvoice.setAccount(listTable);
        baseInvoice.setBank(listTable);
        baseInvoice.setClientApproveData("");
        baseInvoice.setClientCode("");
        baseInvoice.setCurrency("");
        baseInvoice.setExchangeRate("");
        baseInvoice.setCurrencyName("");
        baseInvoice.setClientCode("");
        baseInvoice.setTotalDay(0);
        baseInvoice.setGoogleData(new ArrayList<>());
        baseInvoice.setPaypallData(new ArrayList<>());
        baseInvoice.setMasterCardData(new ArrayList<>());
        baseInvoice.setElavonData(new ArrayList<>());
        baseInvoice.setClientApproveData("");
        baseInvoice.setNumberAndDatesTableName("");// Number and Dates table name
        baseInvoice.setProductTableName("");// Product Table Name
        baseInvoice.setExpenseTableName("");// Product Table Name
        baseInvoice.setInvoiceTotalTableName("");// Invoice Total Table Name
        baseInvoice.setTermsConditionsName("");// Terms and Conditions Name
        baseInvoice.setIntroduction(listTable);// Sales Quote Introduction
        baseInvoice.setPoDataTable(listTable);
        baseInvoice.setTermsConditions(listTable); // Terms Conditions Table
        baseInvoice.setBank(listTable); // Bank table
        baseInvoice.setAccount(listTable); // Account table
        baseInvoice.setFooterData(listTable);
        baseInvoice.setNotes(new ArrayList<>());
        baseInvoice.setPackingSlipTitle("");
        baseInvoice.setPackingSlip(Boolean.FALSE);
        baseInvoice.setLetter(Boolean.FALSE);
    *//* ---------- For Default PDF Start ---------------*//*

        baseInvoice.setProductTable(listTable);// table product items
        baseInvoice.setExpenseTable(listTable);// table product items

        baseInvoice.setNumberAndDatesTable(listTable);// invoice,quote,.. numbers date
        baseInvoice.setClientSupplierData(new HashMap<>());// Client/Supplier Name, Contact and address data
        baseInvoice.setPurchaseClientData(new HashMap<>());//Purchase Invoice/Order Client Name and address data
        baseInvoice.setInvoiceTotalTable(listTable);// totals

    *//* ---------- For Default PDF End---------------*//*

         *//* ---------- For Customised PDF Start ---------------*//*
         *//*private CustomisedITextTable customProductTable;// table product items
        private CustomisedITextTable customExpenseTable;// table product items
        private CustomisedITextTable customNumberAndDatesTable;// invoice,quote,.. numbers date
        private CustomisedITextTable customBillToAddress;// Bill  address
        private CustomisedITextTable customPrimaryContactAddress;// Primary contact address
        private CustomisedITextTable customProductAssemblyItemsTable;
        private CustomisedITextTable customProductKitItemsTable;
        private CustomisedITextTable customTotalTable;// totals
        private CustomisedITextTable customBankTable;// Bank Data
        private CustomisedITextTable customAccountTable;// Account Data
        private CustomisedITextTable consignTable;
        private CustomisedITextTable customFooterData;
        private CustomisedITextTable customTermsConditions;
        private CustomisedITextTable customIntroduction;
        private CustomisedITextTable customPOTable;
        private CustomisedITextTable customClientSupplierEntityCustomFieldTable;//Entity Drop Down Custom Field
        private CustomisedITextTable customClientOrSupplierTypeTable;//Customer or Supplier types
        private CustomisedITextTable customGroupTaxRateTable;
        private CustomisedITextTable customDueAmountTable;
        private CustomisedITextTable customPrepaymentTable;
        private CustomisedITextTable customEmployeeTable;
        private List<CustomisedProductCategoriesITextTable> customProductCategoriesITextTables;
        private CustomisedITextTable customProductSerialTable;
        private CustomisedITextTable customProductArticleTable;
        private CustomisedITextTable customLandedCostTable;
    *//**//* ---------- For Customised PDF End ---------------*//**//*
        //for Payment History
        private CustomisedITextTable paymentHistoryTable;*//*

        ITextCustomView customView = new ITextCustomView();// uses only accounting templates
        ITextUserData userData = new ITextUserData();// user data tempalte
        ITextCompanyData companyData = new ITextCompanyData();// company data template

        ITextUserData creatorData = new ITextUserData(); // creator data template

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        HashMap<String, LinkedList<HashMap<String, CustomisedITextTable>>> customListData = new HashMap<>();
        List<CustomisedITextTable> customEntityTables = new ArrayList<>();

        iTextGenericPdfData.setListTable(listTable);
        iTextGenericPdfData.setSummaryView(summaryView);
        iTextGenericPdfData.setSummaryViewArray(summaryViewArray);
        iTextGenericPdfData.setBaseInvoice(baseInvoice);
        iTextGenericPdfData.setCustomView(customView);
        iTextGenericPdfData.setUserData(userData);
        iTextGenericPdfData.setCompanyData(companyData);
        iTextGenericPdfData.setCreatorData(creatorData);

        iTextGenericPdfData.setCustomData(customData);
        iTextGenericPdfData.setCustomListData(customListData);
        iTextGenericPdfData.setCustomEntityTables(customEntityTables);*/


        if ("sale_invoice".equalsIgnoreCase(item_type)) {
            try {
                InvoiceQuoteRequestObject dataClass = new InvoiceQuoteRequestObject();
                dataClass.setObjectID(itemId);
                SavedSaleInvoiceViewPDFHandler savedSaleInvoceViewPDFHandler = getTargetObject(ApplicationContextProvider.applicationContext.getBean("savedSaleInvoceViewPDFHandler"), SavedSaleInvoiceViewPDFHandler.class);
//                        SavedSaleInvoiceViewPDFHandler savedSaleInvoceViewPDFHandler = (SavedSaleInvoiceViewPDFHandler) ;
                iTextGenericPdfData = savedSaleInvoceViewPDFHandler.buildPdfDocument(dataClass, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return successResponse(new ITextGenericDataTO(iTextGenericPdfData));
    }


    @Transactional
    @RequestMapping(value = "/velocity/{item_type}/{item_id}", method = RequestMethod.POST, consumes = {MediaType.TEXT_PLAIN_VALUE})
    public Object processVelocity(@PathVariable("item_type") String item_type,
                                  @PathVariable("item_id") Integer itemId,
                                  @RequestBody String htmlTemplate) throws RestException {

        ITextGenericPdfData iTextGenericPdfData = new ITextGenericPdfData();
        EdsTemplate template = new EdsTemplate(htmlTemplate);

        if ("sale_invoice".equalsIgnoreCase(item_type)) {
            try {
                InvoiceQuoteRequestObject dataClass = new InvoiceQuoteRequestObject();
                dataClass.setObjectID(itemId);
                SavedSaleInvoiceViewPDFHandler savedSaleInvoceViewPDFHandler = getTargetObject(ApplicationContextProvider.applicationContext.getBean("savedSaleInvoceViewPDFHandler"), SavedSaleInvoiceViewPDFHandler.class);
//                        SavedSaleInvoiceViewPDFHandler savedSaleInvoceViewPDFHandler = (SavedSaleInvoiceViewPDFHandler) ;
//                iTextGenericPdfData = savedSaleInvoceViewPDFHandler.buildPdfDocument(dataClass, null, null);
                iTextGenericPdfData = savedSaleInvoceViewPDFHandler.buildPdfDocumentCustomise(dataClass, userManager.getUser().getCompany(), false);

                String processedHTML = template.process(iTextGenericPdfData);
                return successResponse(new VelocityProcessedDataTO(processedHTML));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return successResponse(new ResponseData());
    }

    @RequestMapping(value = "/pdf/types", method = RequestMethod.POST, consumes = {MediaType.TEXT_PLAIN_VALUE})
    public Object getPdfTypes() throws RestException {

        List<EdsPdfReference> references = pdfReferenceManager.getReferences();
        ArrayList<IdNameTO> types = new ArrayList<>(references.size());
        int i = 0;
        for (EdsPdfReference r : references) {
            types.add(new IdNameTO(r.getObjectID(), r.getName()));
            i++;
        }
        return successResponse(new ResponseListData<>(types));
    }

    @RequestMapping(value = "/pdf/{company_id}/{company_pdftemplate_id}", method = RequestMethod.GET)
    public Object getPdfTemplate(@PathVariable("company_id") Integer companyId, @PathVariable("company_pdftemplate_id") Integer companyPdfTemplateId) throws RestException {

        if (companyId != null && companyId > 0) {
            try {
                ServerSecurityContext.getInstance().setCompanyId(companyId);
                PDFSettingsTransObject transObject = backendServiceLocal.getCompanyPDFSettings(companyId, companyPdfTemplateId);
                if (transObject != null) {
                    PDFDetails pdfDetails = new PDFDetails();
                    pdfDetails.setId(transObject.getObjectID());
                    pdfDetails.setCompany_id(transObject.getCompanyID());
                    pdfDetails.setBrowser_version(transObject.isBrowserVersion());
                    pdfDetails.setContent(transObject.getContent());
                    pdfDetails.setDefault_template(transObject.isDefaultTemplate());
                    pdfDetails.setEx_num_format(transObject.getExNumFormat());
                    pdfDetails.setEx_num_format_dec_separator(transObject.getExNumFormatDecSeparator());
                    pdfDetails.setEx_num_format_group_separator(transObject.getExNumFormatGroupSeparator());
                    if (StringUtils.isNotBlank(transObject.getFontFileName())) {
                        pdfDetails.setFont_file_name(transObject.getFontFileName());
                    }
                    pdfDetails.setImage_name(transObject.getImageName());
                    pdfDetails.setNum_format(transObject.getNumFormat());
                    pdfDetails.setNum_format_dec_separator(transObject.getNumFormatDecSeparator());
                    pdfDetails.setNum_format_group_separator(transObject.getNumFormatGroupSeparator());
                    pdfDetails.setPdf_reference_id(transObject.getPdfReferenceID());
                    pdfDetails.setTemplate_name(transObject.getTemplateName());

                    ApiResult result = new ApiResult(pdfDetails);
                    result.setError(new ErrorTO());
                    return result;
                } else {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Something wrong", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "company_id is wrong.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/pdf/save", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object savePdfTemplate(@RequestBody PDFDetails pdfDetails) throws RestException {

        if (pdfDetails.getCompany_id() != null && pdfDetails.getCompany_id() > 0) {
            try {
                ServerSecurityContext.getInstance().setCompanyId(pdfDetails.getCompany_id());
                if (savePdfTemplateInternal(pdfDetails)) {
                    return successResponse(new ResponseData());
                } else {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Can not save PDF template.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "company_id is wrong.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    public boolean savePdfTemplateInternal(PDFDetails pdfDetails) {

        PDFSettingsTransObject transObject = new PDFSettingsTransObject();
        transObject.setObjectID(pdfDetails.getId());
        transObject.setCompanyID(pdfDetails.getCompany_id());
        transObject.setDefaultTemplate(pdfDetails.isDefault_template());
        transObject.setBrowserVersion(pdfDetails.isBrowser_version());
        transObject.setPdfReferenceID(pdfDetails.getPdf_reference_id());
        transObject.setTemplateName(pdfDetails.getTemplate_name());
        transObject.setFontFileName(pdfDetails.getFont_file_name());
        transObject.setContent(pdfDetails.getContent());
        transObject.setNumFormat(pdfDetails.getNum_format());
        transObject.setNumFormatDecSeparator(pdfDetails.getNum_format_dec_separator());
        transObject.setNumFormatGroupSeparator(pdfDetails.getNum_format_group_separator());
        transObject.setExNumFormat(pdfDetails.getEx_num_format());
        transObject.setExNumFormatDecSeparator(pdfDetails.getEx_num_format_dec_separator());
        transObject.setExNumFormatGroupSeparator(pdfDetails.getEx_num_format_group_separator());

        //Save PDF Template
        Integer id = backendServiceLocal.saveCompanyPdfTemplate(transObject);

        return id != null && id > 0;
    }

    private <T> T getTargetObject(Object proxy, Class<T> targetClass) throws Exception {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return (T) ((Advised) proxy).getTargetSource().getTarget();
        } else {
            return (T) proxy; // expected to be cglib proxy then, which is simply a specialized class
        }
    }

}
