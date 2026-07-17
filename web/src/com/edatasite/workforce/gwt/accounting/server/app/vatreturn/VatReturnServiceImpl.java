package com.edatasite.workforce.gwt.accounting.server.app.vatreturn;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.*;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.FraudPreventionData;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.UKVatReturn;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.hmrc.service.HmrcMtdServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.VatAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.VatReturnManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.VatReturnCustomEventListenerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("vatReturnService")
public class VatReturnServiceImpl implements VatReturnService, VatReturnServiceLocal {

    @Autowired
    private VatReturnManager vatReturnManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private VatAdjustmentManager vatAdjustmentManager;
    @Autowired
    private AccountingServiceLocal accountingService;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private HmrcMtdServiceLocal hmrcMtdServiceLocal;

    @Override
    public VATSettingsItem getVATSettings() {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        VATSettingsItem settingsItem = new VATSettingsItem();
        settingsItem.setTaxIdNumber(financialSettings.getTaxIdNumber());
        settingsItem.setTaxIdDesplayName(financialSettings.getTaxIdDisplayNumber());
        settingsItem.setEnableContractOutsite(financialSettings.isEnableContractOutsite());

        if (financialSettings.getTaxGenerationDate() != null) {
            settingsItem.setTaxGenerationDate(new DateNonConvertable(financialSettings.getTaxGenerationDate()));
        }
        EdsReference taxPeriod = financialSettings.getTaxPeriod();

        if (taxPeriod != null) {
            SelectItem period = taxPeriod.getAsSelectItem();
            period.setCode(taxPeriod.getCode());
            settingsItem.setTaxPeriod(period);
        } else {
            taxPeriod = referenceManager.findReference(EdsFinancialSettings.UAE_TAX_PERIOD, AccountingConstants.TAX_PERIOD.CUSTOM);
            SelectItem period = taxPeriod.getAsSelectItem();
            period.setCode(taxPeriod.getCode());
            settingsItem.setTaxPeriod(period);
        }
        EdsVatReturn lastGeneratedVAT = vatReturnManager.getLastGeneratedVATReturn();

        if (lastGeneratedVAT != null) {
            settingsItem.setLastTaxGeneratedDate(new DateNonConvertable(lastGeneratedVAT.getToDate()));

            SelectItem status = lastGeneratedVAT.getStatus().getAsSelectItem();
            status.setCode(lastGeneratedVAT.getStatus().getCode());
            settingsItem.setLastTaxGeneratedStatus(status);
        }
        settingsItem.setHasUnfiledReturn(hasUnfiledVatReturn());
        settingsItem.setSubmitVatManually(financialSettings.isSubmitVatManually());
        settingsItem.setHmrcAuthorized(financialSettings.isHmrcAuthorized());
        settingsItem.setAgent(financialSettings.isAgent());
        return settingsItem;
    }

    public ListResult<VatReturnItem> getVatReturnList(ListingFilterParameter fp, FraudPreventionData fraudPreventionData) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (!financialSettings.isSubmitVatManually() && financialSettings.isHmrcAuthorized()) {
            hmrcMtdServiceLocal.loadVatReturnsFromHMRC(fraudPreventionData);
        }
        List<EdsVatReturn> list = vatReturnManager.getVatReturnList();
        boolean firstItemRemovable = true;

        ArrayList<VatReturnItem> items = new ArrayList<>();

        for (EdsVatReturn vr : list) {
            VatReturnItem vri = vr.getRPC();

            if (vr.getStatus() == null || !AccountingConstants.VAT_RETURN_STATUS.FILED.equalsIgnoreCase(vr.getStatus().getCode())) {
                VatReturnData returnData = generateVatReturn(vr.getObjectID());

                BigDecimal payableTaxTotal = BigDecimal.ZERO
                        .add(returnData.getPayableTaxTotal().compareTo(BigDecimal.ZERO) >= 0 ? returnData.getPayableTaxTotal() : BigDecimal.ZERO)
                        .add(returnData.getReclaimableTaxTotal().compareTo(BigDecimal.ZERO) < 0 ? returnData.getReclaimableTaxTotal().abs() : BigDecimal.ZERO);
                BigDecimal reclaimableTaxTotal = BigDecimal.ZERO
                        .add(returnData.getReclaimableTaxTotal().compareTo(BigDecimal.ZERO) >= 0 ? returnData.getReclaimableTaxTotal() : BigDecimal.ZERO)
                        .add(returnData.getPayableTaxTotal().compareTo(BigDecimal.ZERO) < 0 ? returnData.getPayableTaxTotal().abs() : BigDecimal.ZERO);

                vri.setPayableTaxTotal(payableTaxTotal);
                vri.setReclaimableTaxTotal(reclaimableTaxTotal);
            }
            if (firstItemRemovable) {
                vri.setRemovable(true);
                firstItemRemovable = false;
            }
            items.add(vri);
        }
        return new ListResult<>(items, list.size());
    }

    @Transactional(readOnly = true)
    @Override
    public VatReturnItem getVatReturn(Integer objectId) {

        if (objectId == null) {
            return null;
        }
        EdsVatReturn edsVatReturn = vatReturnManager.get(objectId);

        if (edsVatReturn == null) {
            return null;
        }
        return edsVatReturn.getRPC();
    }

    @Transactional
    public VatReturnItem createVatReturn(DateNonConvertable fromDate, DateNonConvertable toDate) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsVatReturn lastGeneratedVAT = vatReturnManager.getLastGeneratedVATReturn();

        EdsVatReturn vatReturn = new EdsVatReturn();
        vatReturn.setFromDate(lastGeneratedVAT == null && financialSettings.getTaxGenerationDate() != null ? financialSettings.getTaxGenerationDate() : fromDate.getNonConvertedDate());
        vatReturn.setToDate(toDate.getNonConvertedDate());
        vatReturn.setStatus(referenceManager.findReference(EdsVatReturn.VAT_RETURN_STATUS, EdsVatReturn.UNFILED));
        vatReturnManager.create(vatReturn);

        return vatReturn.getRPC();
    }

    @Transactional(readOnly = true)
    public <T extends VatReturnData> T generateVatReturn(Integer vatReturnId) {
        VatReturnGenerationService<T> service = getVatReturnGenerationService();
        return service == null ? null : service.generateVatReturn(vatReturnId);
    }

    @Transactional(readOnly = true)
    public Boolean hasUnfiledVatReturn() {
        EdsVatReturn unfiledVATReturn = vatReturnManager.getUnfiledVATReturn();
        return unfiledVATReturn != null && unfiledVATReturn.getObjectID() > 0;
    }

    @Override
    @Transactional
    public VatReturnItem fileUkVatReturn(Integer vatReturnId, FraudPreventionData fraudPreventionData) throws RuntimeException {

        UKVatReturn ukVatReturn = generateVatReturn(vatReturnId);
        EdsVatReturn vatReturn = vatReturnManager.get(vatReturnId);

        hmrcMtdServiceLocal.submitVatReturnForPeriod(ukVatReturn, vatReturn, fraudPreventionData);

        vatReturn.setStatus(referenceManager.findReference(EdsVatReturn.VAT_RETURN_STATUS, EdsVatReturn.FILED));
        vatReturn.setFiledOn(new Date());

        //file all the related transactions
        baseEventPostProcessor.registerEvent(VatReturnCustomEventListenerImpl.TYPE, VatReturnCustomEventListenerImpl.EVENT_FILE, vatReturn, vatReturnManager.getUser());

        vatReturnManager.createOrUpdate(vatReturn);
        return vatReturn.getRPC();
    }

    @Transactional
    public VatReturnItem fileUnfileVatReturn(Integer vatReturnId, DateNonConvertable dateOfFiling, boolean file) {
        EdsVatReturn vatReturn = vatReturnManager.get(vatReturnId);

        if (file) {
            vatReturn.setStatus(referenceManager.findReference(EdsVatReturn.VAT_RETURN_STATUS, EdsVatReturn.FILED));
            vatReturn.setFiledOn(dateOfFiling != null ? dateOfFiling.getNonConvertedDate() : new Date());

            //file all the related transactions
            baseEventPostProcessor.registerEvent(VatReturnCustomEventListenerImpl.TYPE, VatReturnCustomEventListenerImpl.EVENT_FILE, vatReturn, vatReturnManager.getUser());
        } else {
            vatReturn.setStatus(referenceManager.findReference(EdsVatReturn.VAT_RETURN_STATUS, EdsVatReturn.UNFILED));
            vatReturn.setFiledOn(null);

            //unfile all the filed transactions
            baseEventPostProcessor.registerEvent(VatReturnCustomEventListenerImpl.TYPE, VatReturnCustomEventListenerImpl.EVENT_UNFILE, vatReturn, vatReturnManager.getUser());
        }
        vatReturnManager.createOrUpdate(vatReturn);
        return vatReturn.getRPC();
    }

    @Transactional
    public void deleteVatReturn(Integer vatReturnId) {
        EdsVatReturn vatReturn = vatReturnManager.get(vatReturnId);
        vatReturn.setDeleted(true);
        vatReturnManager.createOrUpdate(vatReturn);

        List<EdsVatAdjustment> adjustments = vatReturnManager.getVatAdjustmentList(vatReturnId);
        for (EdsVatAdjustment adjustment : adjustments) {
            EdsTransaction transaction = transactionManager.getTransactionByVatAdjustmentId(adjustment.getObjectID());

            if (transaction != null) {
                transaction.setDeleted(true);
            }
            adjustment.setDeleted(true);
        }

        if (EdsVatReturn.FILED.equalsIgnoreCase(vatReturn.getStatus().getCode())) {
            fileUnfileVatReturn(vatReturnId, null, false);
        }
    }

    @Transactional(readOnly = true)
    public ArrayList<VatReturnTransactionItem> getReturnTransactionsByBox(Integer vatReturnId, VatReturnBox box) {
        return (ArrayList<VatReturnTransactionItem>) getVatReturnGenerationService().getReturnTransactionsByBox(vatReturnId, box);
    }

    @Transactional
    public void createVatAdjustment(Integer vatReturnId, VatAdjustmentItem adjustmentItem) {
        EdsVatAdjustment edsVatAdjustment = new EdsVatAdjustment();
        edsVatAdjustment.setDate(adjustmentItem.getDate().getNonConvertedDate());
        edsVatAdjustment.setReference(adjustmentItem.getReference());
        edsVatAdjustment.setAmount(adjustmentItem.getAmount());

        if (adjustmentItem.getAccount() != null) {
            edsVatAdjustment.setAccountId(adjustmentItem.getAccount().getId());
            edsVatAdjustment.setAccount(accountingManager.get(adjustmentItem.getAccount().getId()));
        }
        edsVatAdjustment.setReason(adjustmentItem.getReason());
        edsVatAdjustment.setVatReturn(vatReturnManager.get(vatReturnId));
        vatAdjustmentManager.create(edsVatAdjustment);

        accountingService.createTransactionForVatAdjustment(edsVatAdjustment);
    }

    @Override
    public void createTransactionForVatReturn(EdsVatReturn vatReturn, BigDecimal vatOutputAmount, BigDecimal vatInputAmout, EdsUser user) {
        if ((vatOutputAmount == null || vatOutputAmount.compareTo(BigDecimal.ZERO) <= 0)
                && (vatInputAmout == null || vatInputAmout.compareTo(BigDecimal.ZERO) <= 0)) {
            return;
        }
        EdsVatReturnTransaction transaction = new EdsVatReturnTransaction(); //creating new transaction
        transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID(user.getCompany()) + 1);
        transaction.setName("VAT RETURN FOR  " + new SimpleDateFormat("MMM yyyy").format(vatReturn.getToDate()));
        transaction.setVatReturnId(vatReturn.getObjectID());//transaction belongs to invoice
        transaction.setJournalDate(vatReturn.getFiledOn());
        transaction.setPostedBy(user);
        transaction.setPostedDate(user.getCompany().getCompanyDate());
        transaction.setReference(transaction.getName());
        transactionManager.createOrUpdate(transaction);

        vatOutputAmount = vatOutputAmount != null ? vatOutputAmount : BigDecimal.ZERO;
        vatInputAmout = vatInputAmout != null ? vatInputAmout : BigDecimal.ZERO;
        BigDecimal negativeVatOutputAmount = vatOutputAmount.compareTo(BigDecimal.ZERO) < 0 ? vatOutputAmount.negate() : BigDecimal.ZERO;
        BigDecimal negativeVatInputAmout = vatInputAmout.compareTo(BigDecimal.ZERO) < 0 ? vatInputAmout.negate() : BigDecimal.ZERO;

        BigDecimal finalVatOutputAmount = vatOutputAmount.add(negativeVatInputAmout);
        BigDecimal finalVatInputAmout = vatInputAmout.add(negativeVatOutputAmount);
        EdsTransactionItem transactionItem = null;
        if (finalVatOutputAmount.compareTo(BigDecimal.ZERO) > 0) {
            transactionItem = new EdsTransactionItem();
            transactionItem.setAccount(accountingManager.getAccountByKey(EdsAccount.VAT_OUTPUT));
            transactionItem.setDebit(finalVatOutputAmount);
            transaction.addTransactionItem(transactionItem);
        }

        if (finalVatInputAmout.compareTo(BigDecimal.ZERO) > 0) {
            transactionItem = new EdsTransactionItem();
            transactionItem.setAccount(accountingManager.getAccountByKey(EdsAccount.VAT_INPUT));
            transactionItem.setCredit(finalVatInputAmout);
            transaction.addTransactionItem(transactionItem);
        }

        BigDecimal vatDiff = vatOutputAmount.subtract(vatInputAmout);
        if (vatDiff.compareTo(BigDecimal.ZERO) != 0) {
            transactionItem = new EdsTransactionItem();
            transactionItem.setAccount(accountingManager.getAccountByKey(EdsAccount.VAT_PAYABLE));

            if (vatDiff.compareTo(BigDecimal.ZERO) > 0) {
                transactionItem.setCredit(vatDiff);
            } else {
                transactionItem.setDebit(vatDiff.abs());
            }
            transaction.addTransactionItem(transactionItem);
        }
    }

    VatReturnGenerationService getVatReturnGenerationService() {
        ApplicationContext applicationContext = ApplicationContextProvider.applicationContext;
        EdsCompany company = vatReturnManager.getUser().getCompany();

        if (ServerUtils.isUAECompany(company)) {
            return (VatReturnGenerationService) applicationContext.getBean("uaeVatReturnGenerationService");
        } else if (ServerUtils.isKSACompany(company)) {
            return (VatReturnGenerationService) applicationContext.getBean("ksaVatReturnGenerationService");
        } else if (ServerUtils.isUKCompany(company)) {
            return (VatReturnGenerationService) applicationContext.getBean("ukVatReturnGenerationService");
        }

        return null;
    }
}
