package com.edatasite.workforce.rest.v3.release10.accounting.service;

import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrExpenseReportRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.expenses.client.rpc.*;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.enums.TaxTypeEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ExpenseDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ExpenseItemDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.PaymentDataDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.TAX_CALCULATION_EXCLUSIVE;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.TAX_CALCULATION_INCLUSIVE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

@Service
public class ApiExpenseService {

    private static final Logger log = LoggerFactory.getLogger(ApiExpenseService.class);
    private final ExpenseServiceLocal expenseServiceLocal;
    private final ExpenseReportManager expenseReportManager;
    private final AccountingManager accountingManager;
    private final CommonService commonService;
    private final WfmMessageSource referenceMessageLocalizer;
    private final CurrencyServiceLocal currencyServiceLocal;

    @Autowired
    public ApiExpenseService(ExpenseServiceLocal expenseServiceLocal,
                             ExpenseReportManager expenseReportManager,
                             AccountingManager accountingManager, CommonService commonService,
                             @Qualifier("referenceWfmMessageSource") WfmMessageSource wfmMessageSource, CurrencyServiceLocal currencyServiceLocal) {
        this.expenseServiceLocal = expenseServiceLocal;
        this.expenseReportManager = expenseReportManager;
        this.accountingManager = accountingManager;
        this.commonService = commonService;
        this.referenceMessageLocalizer = wfmMessageSource;
        this.currencyServiceLocal = currencyServiceLocal;
    }

    @Transactional(readOnly = true)
    public ExpenseDto getById(final Integer id) throws RestException {
        Optional.ofNullable(expenseReportManager.get(id)).orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Expense with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST));
        return toDto(expenseServiceLocal.getReportSummaryData(id));
    }

    public ListResultTO<ExpenseDto> getExpenseList(ListingFilterParameter filterParameter) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EXPENSE_REPORT_CLAIMS_CORE);
        QueryResponse resp = null;
        try {
            String solrQuery = expenseServiceLocal.getExpenseReportsCoreSolrQuery(filterParameter, expenseReportManager.getUser()) + SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(
                    filterParameter.getFacetFilter(),
                    FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6]) +
                    SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(
                            filterParameter.getFacetFilter(),
                            expenseReportManager.getUser().getCompany(),
                            SolrExpenseReportRepresenter.FIELD_START_DATE,
                            SolrExpenseReportRepresenter.FIELD_START_DATE,
                            FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6]
                    );
            resp = server.query(expenseServiceLocal.getExpenseReportsSolrQuery(filterParameter, solrQuery));
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        ListResultTO<ExpenseDto> resultTO = new ListResultTO<>();
        if (resp != null) {
            List<Integer> ids = resp.getResults().stream().map(doc -> SolrUtils.asInteger(doc, SolrExpenseReportRepresenter.FIELD_REPORT_ID)).collect(Collectors.toList());
            resultTO.setTotalNumber(ids.size());
            ArrayList<ExpenseDto> expenses = new ArrayList<>();
            ids.forEach(id -> {
                expenses.add(toDto(expenseServiceLocal.getReportSummaryData(id)));
                resultTO.setItems(expenses);
            });
        }

        return resultTO;
    }
    public ListResultTO<ExpenseDto> getSimpleExpenseList(ListingFilterParameter filterParameter) {
        var list = expenseServiceLocal.getExpenseReportsDataFromSolr(filterParameter);
        if (list == null && list.getList() == null) return new ListResultTO<>();
        var result = list.getList().stream()
                .map(ConvertUtils::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
        ListResultTO<ExpenseDto> resultTO = new ListResultTO<>();
        resultTO.setTotalNumber(result.size());
        resultTO.setItems(result);
        return resultTO;
    }

    public Integer update(ExpenseDto expenseDto) throws RestException {
        EdsExpenseReport edsExpenseReport = null;
        if (expenseDto.getId() != null) {
            edsExpenseReport = Optional.ofNullable(expenseReportManager.getExpenseReport(expenseDto.getId()))
                    .orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Expense with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST));
            EdsReference status = edsExpenseReport.getStatus();
            if (status == null || (!EXPENSE_DRAFT.equals(status.getCode()))) {
                throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "You cannot update this expense", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
        }

        ExpenseReportViewParameters expenseReportViewParameters = new ExpenseReportViewParameters();
        expenseReportViewParameters.setObjectID(expenseDto.getId());
        ReportData reportData = expenseServiceLocal.getReportData(expenseReportViewParameters);
        ExpenseReportsListItem expenseReportsListItem = reportData.getReport();

        BigDecimal exchangeRate = BigDecimal.ONE;
        CurrencyItem baseCurrency = currencyServiceLocal.getBaseCurrency();
        expenseReportsListItem.setBaseCurrency(baseCurrency);
        expenseReportsListItem.setExchangeRate(exchangeRate);

        if (expenseDto.getCurrency() != null && expenseDto.getCurrency().getId() != null) {
            CurrencyItem currency = currencyServiceLocal.getCurrency(expenseDto.getCurrency().getId());
            if (currency != null) {
                expenseReportsListItem.setExpenseCurrency(currency);
                exchangeRate = BigDecimal.valueOf(currencyServiceLocal.getCurrencyRateByDate(currency.getId(), new DateNonConvertable(new Date())).getExchangeRate());
                expenseReportsListItem.setExchangeRate(exchangeRate);
            }
        }

        Optional.ofNullable(expenseDto.getSupplier())
                .map(e -> new SelectItem(e.getId(), e.getName()))
                .ifPresentOrElse(expenseReportsListItem::setSupplier, () -> expenseReportsListItem.setSupplier(null));

        Optional.ofNullable(expenseDto.getDate()).ifPresent(date -> expenseReportsListItem.setStartDate(new DateNonConvertable(date)));

        if (StringUtils.isNotBlank(expenseDto.getNumber())) {
            expenseReportsListItem.setExpenseNumber(expenseDto.getNumber());
            try {
                BankTransferNumberData expenseNumberData = expenseReportsListItem.getExpenseNumberData();
                if (expenseNumberData.getFourDigitNumber() != null && expenseNumberData.getFourDigitNumber().matches("^[0-9]*$")) {
                    expenseReportsListItem.setIntNumber(Integer.parseInt(expenseNumberData.getFourDigitNumber()));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        Optional.ofNullable(expenseDto.getCreatedAt()).ifPresent(date -> expenseReportsListItem.setCreatedDate(new DateNonConvertable(date)));
        expenseReportsListItem.setUpdatedDate(new DateNonConvertable());

        Optional.ofNullable(expenseDto.getReportTitle()).ifPresent(expenseReportsListItem::setTitle);
        Optional.ofNullable(expenseDto.getAmounts()).ifPresent(amounts -> expenseReportsListItem.setTaxCalculationType(TaxTypeEnum.valueOf(amounts).getId()));
        Optional.ofNullable(expenseDto.getApprover()).ifPresent(approver -> {
            ApproverItemMini approverItemMini = new ApproverItemMini();
            approverItemMini.setObjectID(approver.getId());
            expenseReportsListItem.setApprovers(Lists.newArrayList(approverItemMini));
        });
        Optional.ofNullable(expenseDto.getEmployee()).ifPresent(employee -> expenseReportsListItem.setReporterId(employee.getId()));
        Optional.ofNullable(expenseDto.getFixedAsset()).ifPresent(fixedAsset -> expenseReportsListItem.setFixedAsset(new SelectItem(fixedAsset.getId(), fixedAsset.getCode())));
        Optional.ofNullable(expenseDto.getCompanyExpense()).ifPresent(expenseReportsListItem::setCompanyExpense);
        Optional.ofNullable(expenseDto.getOpportunity()).ifPresent(opportunity -> expenseReportsListItem.setOpportunity(new SelectItem(opportunity.getId(), opportunity.getCode())));
        Optional.ofNullable(expenseDto.getRelatedProject()).ifPresent(project -> expenseReportsListItem.setProject(new SelectItem(project.getId(), project.getCode())));
        Optional.ofNullable(expenseDto.getRelatedPO()).ifPresent(po -> expenseReportsListItem.setPurchaseOrder(new SelectItem(po.getId(), po.getCode())));

        Optional.ofNullable(expenseDto.getAccountsPayable()).ifPresent(accountPayable -> {
            EdsAccount account = null;
            if (accountPayable.getId() != null) {
                account = accountingManager.get(accountPayable.getId());
            } else if (StringUtils.isNotBlank(accountPayable.getCode())) {
                account = accountingManager.getAccountByCode(accountPayable.getCode());
            }
            if (account != null) {
                expenseReportsListItem.setPayableAccount(account.createAccountItem());
            }
        });

        Optional.ofNullable(expenseDto.getStatus()).ifPresent(expenseReportsListItem::setStatusCode);

        if (expenseDto.getItems() != null) {
            List<ExpenseListItem> updatedItems = new ArrayList<>();

            for (ExpenseItemDto dtoItem : expenseDto.getItems()) {
                ExpenseListItem expenseListItem = new ExpenseListItem();
                if (dtoItem.getId() != null) expenseListItem.setId(dtoItem.getId());

                Optional.ofNullable(dtoItem.getCategory()).ifPresent(category -> {
                    if (category.getId() != null) {
                        expenseListItem.setCategoryId(category.getId());
                        expenseListItem.setAccountId(category.getId());
                    }
                    expenseListItem.setCategoryName(category.getCode());
                });
                Optional.ofNullable(dtoItem.getDescription()).ifPresent(expenseListItem::setDescription);
                Optional.ofNullable(dtoItem.getQuantity()).ifPresent(expenseListItem::setUnits);
                Optional.ofNullable(dtoItem.getUnitPrice()).ifPresent(expenseListItem::setCostPerUnit);

                if (expenseListItem.getUnits() != null && expenseListItem.getCostPerUnit() != null) {
                    expenseListItem.setSubtotal(expenseListItem.getUnits().multiply(expenseListItem.getCostPerUnit()));
                } else {
                    expenseListItem.setSubtotal(BigDecimal.ZERO);
                }
                updatedItems.add(expenseListItem);
            }
            expenseReportsListItem.setItems(updatedItems.toArray(new ExpenseListItem[0]));

            // Tax & totals
            BigDecimal taxTotal = BigDecimal.ZERO, subTotalAll = BigDecimal.ZERO, totalAll = BigDecimal.ZERO, baseTotalAll;
            int taxCalculationType = expenseReportsListItem.getTaxCalculationType();
            for (ExpenseListItem item : expenseReportsListItem.getItems()) {
                BigDecimal net = item.getSubtotal();
                BigDecimal taxAmount = BigDecimal.ZERO;

                if (expenseReportsListItem.isCompanyExpense()) {
                    if (expenseReportsListItem.getExchangeRate().compareTo(BigDecimal.ZERO) != 0 && item.getTaxAmountInTc() != null) {
                        taxAmount = taxAmount.add(item.getTaxAmountInTc());
                    } else if (item.getTaxAmountInBase() != null) {
                        taxAmount = taxAmount.add(item.getTaxAmountInBase());
                    }
                    if (reportData.isDoubleTaxEnabled() && item.getDoubleTaxAmountInBase() != null) {
                        taxAmount = taxAmount.add(
                                expenseReportsListItem.getExchangeRate().compareTo(BigDecimal.ZERO) != 0 ?
                                        item.getDoubleTaxAmountInBase().multiply(expenseReportsListItem.getExchangeRate()) :
                                        item.getDoubleTaxAmountInBase()
                        );
                    }
                    taxAmount = taxAmount.setScale(5, BigDecimal.ROUND_HALF_UP);
                }
                taxTotal = taxTotal.add(taxAmount);
                subTotalAll = subTotalAll.add(net);

                if (expenseReportsListItem.isReversechargeApplicable()) {
                    totalAll = totalAll.add(
                            TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType) ? net.subtract(taxAmount) : net
                    );
                } else {
                    totalAll = totalAll.add(net.add(
                            TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType) ? taxAmount : BigDecimal.ZERO
                    ));
                }
            }
            baseTotalAll = totalAll.divide(expenseReportsListItem.getExchangeRate(), 5, BigDecimal.ROUND_HALF_UP);
            expenseReportsListItem.setTaxTotal(taxTotal);
            expenseReportsListItem.setTotal(totalAll);
            expenseReportsListItem.setBaseTotal(baseTotalAll);
        }

        if (expenseDto.getCustomFields() != null && !expenseDto.getCustomFields().isEmpty()) {
            expenseReportsListItem.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(
                    expenseDto.getCustomFields(),
                    commonService.getCompanyCustomFields(ViewName.ExpenceReportView),
                    edsExpenseReport != null ? edsExpenseReport.getCustomFields() : null
            ));
        }

        return expenseServiceLocal.saveReport(expenseReportsListItem);
    }

    @Transactional
    public Integer save(ExpenseDto expenseDto) throws RestException {
        EdsExpenseReport edsExpenseReport = null;
        if (expenseDto.getId() != null) {
            edsExpenseReport = Optional.ofNullable(expenseReportManager.getExpenseReport(expenseDto.getId()))
                    .orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Expense with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST));
            EdsReference status = edsExpenseReport.getStatus();
            if (status == null || (!EXPENSE_SUBMITTED.equals(status.getCode()) && !EXPENSE_APPROVED.equals(status.getCode()))) {
                throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "You cannot update this expense", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
        }
        ExpenseReportViewParameters expenseReportViewParameters = new ExpenseReportViewParameters();
        expenseReportViewParameters.setObjectID(expenseDto.getId());
        ReportData reportData = expenseServiceLocal.getReportData(expenseReportViewParameters);
        ExpenseReportsListItem expenseReportsListItem = reportData.getReport();

        BigDecimal exchangeRate = BigDecimal.ONE;
        CurrencyItem baseCurrency = currencyServiceLocal.getBaseCurrency();
        expenseReportsListItem.setBaseCurrency(baseCurrency);
        expenseReportsListItem.setExchangeRate(exchangeRate);

        if (expenseDto.getCurrency() != null && expenseDto.getCurrency().getId() != null) {
            CurrencyItem currency = currencyServiceLocal.getCurrency(expenseDto.getCurrency().getId());
            if (currency != null){
                expenseReportsListItem.setExpenseCurrency(currency);
                exchangeRate = BigDecimal.valueOf(currencyServiceLocal.getCurrencyRateByDate(currency.getId(), new DateNonConvertable(new Date())).getExchangeRate());
                expenseReportsListItem.setExchangeRate(exchangeRate);
            }
        }

        Optional.ofNullable(expenseDto.getSupplier()).ifPresent(supplier -> expenseReportsListItem.setSupplier(new SelectItem(supplier.getId(), supplier.getName())));
        Optional.ofNullable(expenseDto.getDate()).ifPresent(date -> expenseReportsListItem.setStartDate(new DateNonConvertable(date)));
        if (StringUtils.isNotBlank(expenseDto.getNumber())) {
            expenseReportsListItem.setExpenseNumber(expenseDto.getNumber());
        } else {
            try {
                BankTransferNumberData expenseNumberData = expenseReportsListItem.getExpenseNumberData();
                if (expenseNumberData.getFourDigitNumber() != null && expenseNumberData.getFourDigitNumber().matches("^[0-9]*$")) {
                    expenseReportsListItem.setIntNumber(Integer.parseInt(expenseNumberData.getFourDigitNumber()));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        Optional.ofNullable(expenseDto.getReportTitle()).ifPresent(expenseReportsListItem::setTitle);
        Optional.ofNullable(expenseDto.getAmounts()).ifPresent(amounts -> expenseReportsListItem.setTaxCalculationType(TaxTypeEnum.valueOf(amounts).getId()));
        Optional.ofNullable(expenseDto.getApprover()).ifPresent(approver -> {
            ApproverItemMini approverItemMini = new ApproverItemMini();
            approverItemMini.setObjectID(approver.getId());
            expenseReportsListItem.setApprovers(Lists.newArrayList(approverItemMini));
        });
        Optional.ofNullable(expenseDto.getEmployee()).ifPresent(employee -> expenseReportsListItem.setEmployeeId(employee.getId()));
        Optional.ofNullable(expenseDto.getFixedAsset()).ifPresent(fixedAsset -> expenseReportsListItem.setFixedAsset(new SelectItem(fixedAsset.getId(), fixedAsset.getCode())));
        Optional.ofNullable(expenseDto.getCompanyExpense()).ifPresent(expenseReportsListItem::setCompanyExpense);
        Optional.ofNullable(expenseDto.getOpportunity()).ifPresent(opportunity -> expenseReportsListItem.setOpportunity(new SelectItem(opportunity.getId(), opportunity.getCode())));
        Optional.ofNullable(expenseDto.getRelatedProject()).ifPresent(project -> expenseReportsListItem.setProject(new SelectItem(project.getId(), project.getCode())));
        Optional.ofNullable(expenseDto.getRelatedPO()).ifPresent(po -> expenseReportsListItem.setPurchaseOrder(new SelectItem(po.getId(), po.getCode())));
        Optional.ofNullable(expenseDto.getAccountsPayable()).ifPresent(accountPayable -> {
            EdsAccount account = null;
            if (accountPayable.getId() != null) {
                account = accountingManager.get(accountPayable.getId());
            } else if (StringUtils.isNotBlank(accountPayable.getCode())) {
                account = accountingManager.getAccountByCode(accountPayable.getCode());
            }

            if (account != null) {
                expenseReportsListItem.setPayableAccount(account.createAccountItem());
            }
        });
        Optional.ofNullable(expenseDto.getStatus()).ifPresent(expenseReportsListItem::setStatusCode);

        if (expenseDto.getItems() != null && !expenseDto.getItems().isEmpty()) {
            List<ExpenseListItem> listItems = expenseReportsListItem.getItems() != null ? new ArrayList<>(Arrays.asList(expenseReportsListItem.getItems())) : new ArrayList<>();
            for (ExpenseItemDto expenseItemDto : expenseDto.getItems()) {
                ExpenseListItem expenseListItem = listItems.stream().filter(listItem -> listItem.getId() != null && listItem.getId().equals(expenseItemDto.getId())).findAny().orElse(new ExpenseListItem());
                int index = listItems.indexOf(expenseListItem);
                Optional.ofNullable(expenseItemDto.getCategory()).ifPresent(category -> {
                    if (category.getId() != null) {
                        expenseListItem.setCategoryId(category.getId());
                        expenseListItem.setAccountId(category.getId());
                    }
                    expenseListItem.setCategoryName(category.getCode());
                });
                Optional.ofNullable(expenseItemDto.getDescription()).ifPresent(expenseListItem::setDescription);
                Optional.ofNullable(expenseItemDto.getQuantity()).ifPresent(expenseListItem::setUnits);
                Optional.ofNullable(expenseItemDto.getUnitPrice()).ifPresent(expenseListItem::setCostPerUnit);
                expenseListItem.setSubtotal(expenseListItem.getUnits().multiply(expenseListItem.getCostPerUnit()));
                if (expenseListItem.getId() == null) {
                    listItems.add(expenseListItem);
                } else {
                    listItems.set(index, expenseListItem);
                }
            }
            expenseReportsListItem.setItems(listItems.toArray(new ExpenseListItem[]{}));

            BigDecimal taxTotal = BigDecimal.ZERO, subTotalAll = BigDecimal.ZERO, totalAll = BigDecimal.ZERO, baseTotalAll;
            int taxCalculationType = (expenseReportsListItem.getTaxCalculationType() != null)
                    ? expenseReportsListItem.getTaxCalculationType()
                    : TAX_CALCULATION_EXCLUSIVE;
            for (ExpenseListItem item : expenseReportsListItem.getItems()) {
                BigDecimal net = item.getSubtotal();
                BigDecimal taxAmount = BigDecimal.ZERO;

                if (expenseReportsListItem.isCompanyExpense()) {
                    if (expenseReportsListItem.getExchangeRate().compareTo(BigDecimal.ZERO) != 0 && item.getTaxAmountInTc() != null) {
                        taxAmount = taxAmount.add(item.getTaxAmountInTc());
                    } else if (item.getTaxAmountInBase() != null) {
                        taxAmount = taxAmount.add(item.getTaxAmountInBase());
                    }

                    if (reportData.isDoubleTaxEnabled()) {
                        if (item.getDoubleTaxAmountInBase() != null) {
                            if (expenseReportsListItem.getExchangeRate().compareTo(BigDecimal.ZERO) != 0) {
                                taxAmount = taxAmount.add(item.getDoubleTaxAmountInBase().multiply(expenseReportsListItem.getExchangeRate()));
                            } else {
                                taxAmount = taxAmount.add(item.getDoubleTaxAmountInBase());
                            }
                        }
                    }
                    taxAmount = taxAmount.setScale(5, BigDecimal.ROUND_HALF_UP);
                }
                taxTotal = taxTotal.add(taxAmount);
                subTotalAll = subTotalAll.add(net);

                if (expenseReportsListItem.isReversechargeApplicable()) {
                    if (TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
                        totalAll = totalAll.add(net.subtract(taxAmount));
                    } else {
                        totalAll = totalAll.add(net);
                    }
                } else {
                    totalAll = totalAll.add(net.add(TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType) ? taxAmount : BigDecimal.ZERO));
                }
            }
            baseTotalAll = totalAll.divide(expenseReportsListItem.getExchangeRate(), 5, BigDecimal.ROUND_HALF_UP);
            expenseReportsListItem.setTaxTotal(taxTotal);
            expenseReportsListItem.setTotal(totalAll);
            expenseReportsListItem.setBaseTotal(baseTotalAll);
        }
        if (expenseDto.getCustomFields() != null && !expenseDto.getCustomFields().isEmpty()) {
            expenseReportsListItem.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(expenseDto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.ExpenceReportView), edsExpenseReport != null ? edsExpenseReport.getCustomFields() : null));
        }
        Integer savedReportId = expenseServiceLocal.saveReport(expenseReportsListItem);
        return savedReportId;
    }


    public ExpenseDto toDto(ReportData reportData) {
        ExpenseDto expenseDto = new ExpenseDto();
        ExpenseReportsListItem report = reportData.getReport();
        expenseDto.setId(report.getId());
        expenseDto.setNumber(report.getExpenseNumber());
        expenseDto.setReportTitle(report.getTitle());
        if (report.getTaxCalculationType() != null) {
            expenseDto.setAmounts(TaxTypeEnum.getTaxTypeById(report.getTaxCalculationType()).getName());
        }
        expenseDto.setCreatedAt(report.getCreatedDate() != null ? report.getCreatedDate().getDate() : null);
        expenseDto.setUpdatedAt(report.getUpdatedDate() != null ? report.getUpdatedDate().getDate() : null);
        Optional.ofNullable(report.getStartDate()).ifPresent(x -> expenseDto.setDate(x.getDate()));
        if (report.getSupplier() != null) {
            IdName supplier = new IdName(report.getSupplier().getId(), report.getSupplier().getName());
            supplier.addProperty("imgUrl", commonService.getEmployeeImageURL(report.getSupplier().getId()));
            expenseDto.setSupplier(supplier);
        }
        if (report.getExpenseCurrency() != null) {
            expenseDto.setCurrency(new IdCode(report.getExpenseCurrency().getId(), report.getExpenseCurrency().getName()));
        }
        if (report.getCurrentApprover() != null) {
            IdName approver = new IdName(report.getCurrentApproverEmployeeID(), report.getCurrentApproverEmployeeName());
            approver.addProperty("imgUrl", commonService.getEmployeeImageURL(report.getCurrentApproverEmployeeID()));
            expenseDto.setApprover(approver);
        }
        if (report.getReporterId() != null) {
            IdName employee = new IdName(report.getReporterId(), report.getReporterName());
            employee.addProperty("imgUrl", commonService.getEmployeeImageURL(report.getReporterId()));
            expenseDto.setEmployee(employee);
        }
        if (report.getFixedAsset() != null) {
            expenseDto.setFixedAsset(new IdCode(report.getFixedAsset().getId(), report.getFixedAsset().getName()));
        }
        if (report.getOpportunity() != null) {
            expenseDto.setOpportunity(new IdCode(report.getOpportunity().getId(), report.getOpportunity().getName()));
        }
        if (report.getProject() != null) {
            expenseDto.setRelatedProject(new IdCode(report.getProject().getId(), report.getProject().getName()));
        }
        if (report.getPurchaseOrder() != null) {
            expenseDto.setRelatedPO(new IdCode(report.getPurchaseOrder().getId(), report.getPurchaseOrder().getName()));
        }
        if (report.getPayableAccount() != null) {
            expenseDto.setAccountsPayable(new IdCode(report.getPayableAccount().getId(), report.getPayableAccount().getName()));
        }
        if (report.getCustomFieldItems() != null && !report.getCustomFieldItems().isEmpty()) {
            expenseDto.setCustomFields(report.getCustomFieldItems().stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }
        List<ExpenseItemDto> expenseItems = new ArrayList<>();
        if (report.getItems() != null && report.getItems().length > 0) {
            for (ExpenseListItem expenseListItem : report.getItems()) {
                ExpenseItemDto expenseItemDto = new ExpenseItemDto();
                expenseItemDto.setId(expenseListItem.getId());
                EdsAccount edsAccount = accountingManager.get(expenseListItem.getAccountId());
                if (edsAccount != null) {
                    IdCode idCode = new IdCode(edsAccount.getObjectID(), edsAccount.getName());
                    idCode.addProperty("parentCode", edsAccount.getParent() != null ? edsAccount.getParent().getAccountCode() : null);
                    expenseItemDto.setCategory(idCode);
                } else {
                    expenseItemDto.setCategory(new IdCode(expenseListItem.getAccountId(), expenseListItem.getAccountName()));
                }
                expenseItemDto.setDescription(expenseListItem.getDescription());
                expenseItemDto.setQuantity(expenseListItem.getUnits());
                expenseItemDto.setUnitPrice(expenseListItem.getCostPerUnit());
                if (expenseListItem.getTax() != null) {
                    expenseItemDto.setTax(new IdName(expenseListItem.getTax().getId(), expenseListItem.getTax().getName()));
                }
                expenseItemDto.addProperty("total", expenseListItem.getSubtotal());
                expenseItemDto.addProperty("baseTotal", expenseListItem.getBaseSubtotal());
                if (expenseListItem.getDepartment() != null) {
                    expenseItemDto.setDepartment(new IdName(expenseListItem.getDepartment().getId(), expenseListItem.getDepartment().getName()));
                }
                if (expenseListItem.getCustomFieldItems() != null && !expenseListItem.getCustomFieldItems().isEmpty()) {
                    expenseItemDto.setCustomFields(expenseListItem.getCustomFieldItems().stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
                }
                expenseItems.add(expenseItemDto);

            }
        }
        expenseDto.setItems(expenseItems);

        if (reportData.getReport().getCustomFieldItems() != null) {
            expenseDto.setCustomFields(reportData.getReport().getCustomFieldItems().stream().map(CustomFieldsUtils::getCustomFieldDto).collect(Collectors.toList()));
        }
        List<PaymentDataDto> paymentDataList = new ArrayList<>();
        if (report.getPaymentItems() != null && report.getPaymentItems().length > 0) {
            for (ExpensePaymentData expensePaymentData : report.getPaymentItems()) {
                PaymentDataDto paymentDataDto = new PaymentDataDto();
                paymentDataDto.setId(expensePaymentData.getObjectID());
                paymentDataDto.setAmount(expensePaymentData.getPaymentAmount());
                paymentDataDto.setPaymentDate(expensePaymentData.getDate().getDate());
                if (expensePaymentData.getPaymentAccount() != null) {
                    paymentDataDto.setPaymentAccount(new IdName(expensePaymentData.getPaymentAccount().getId(), expensePaymentData.getPaymentAccount().getName()));
                }
                paymentDataList.add(paymentDataDto);
            }
        }
        if (report.getStatusCode() != null) {
            expenseDto.setStatus(report.getStatusCode());
            ApproverListStatusTO statusTO = new ApproverListStatusTO(
                    report.getStatusCode(),
                    referenceMessageLocalizer.localize(report.getStatusCode(), report.getStatusCode()));
            expenseDto.addProperty("statusItem", statusTO);
        }
        expenseDto.addProperty("payments", paymentDataList);
        expenseDto.addProperty("total", report.getTotal());
        expenseDto.addProperty("taxTotal", report.getTaxTotal());
        expenseDto.addProperty("baseTotal", report.getBaseTotal());
        expenseDto.addProperty("dueTotal", report.getDueTotal());
        expenseDto.addProperty("statusColor", report.getStatusColor());
        if (report.getBaseCurrency() != null) {
            expenseDto.addProperty("baseCurrency", report.getBaseCurrency().getName());
        }

        return expenseDto;
    }
}
