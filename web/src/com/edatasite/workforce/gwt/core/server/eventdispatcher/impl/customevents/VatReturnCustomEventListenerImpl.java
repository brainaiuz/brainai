package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.UKVatReturn;
import com.edatasite.workforce.gwt.accounting.server.app.vatreturn.VatReturnServiceLocal;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.KsaVatReturnManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.UaeVatReturnManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.VatReturnManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 13-Aug-2010
 * Time: 14:56:17
 */

@Transactional
public class VatReturnCustomEventListenerImpl extends CustomBusinessEventListenerAdapter implements Constants {
    public static WfmType<EdsVatReturn> TYPE = new WfmType<>(EventTypes.vatReturnCustomEventListener);
    public static String EVENT_FILE = "FILE";
    public static String EVENT_UNFILE = "UNFILE";

    @Autowired
    private VatReturnManager vatReturnManager;
    @Autowired
    private UaeVatReturnManager uaeVatReturnManager;
    @Autowired
    private KsaVatReturnManager ksaVatReturnManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private VatReturnServiceLocal vatReturnService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TransactionManager transactionManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

        if (EVENT_FILE.equals(event.getEventType())) {
            onFileEvent(event);
        } else if (EVENT_UNFILE.equals(event.getEventType())) {
            onUnFileEvent(event);
        }
    }

    protected void onFileEvent(EdsBusinessEvent event) {
        EdsVatReturn vatReturn = (EdsVatReturn) vatReturnManager.get(event.getEntityID());
        EdsCountry country = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())).getCountry();

        if (Constants.SA.equalsIgnoreCase(country.getCode())) {
            onFileKsaVatEvent(vatReturn);
        } else if (Constants.AE.equalsIgnoreCase(country.getCode())) {
            onFileUaeVatEvent(vatReturn);
        }
        Pair<BigDecimal, BigDecimal> taxTotal;
        if (Constants.UK.equals(country.getCode())) {
            UKVatReturn ukVatReturn = vatReturnService.generateVatReturn(event.getEntityID());
            taxTotal = Pair.of(ukVatReturn.getVatOnSales(), ukVatReturn.getVatOnPurchase());
        } else {
            taxTotal = vatReturnManager.getTotalTaxAmounts(vatReturn.getObjectID());
        }

        vatReturnService.createTransactionForVatReturn(vatReturn, taxTotal.getLeft(), taxTotal.getRight(), userManager.get(event.getSourceID()));

        BigDecimal payableTaxTotal = BigDecimal.ZERO
                .add(taxTotal.getLeft().compareTo(BigDecimal.ZERO) >= 0 ? taxTotal.getLeft() : BigDecimal.ZERO)
                .add(taxTotal.getRight().compareTo(BigDecimal.ZERO) < 0 ? taxTotal.getRight().abs() : BigDecimal.ZERO);
        BigDecimal reclaimableTaxTotal = BigDecimal.ZERO
                .add(taxTotal.getRight().compareTo(BigDecimal.ZERO) >= 0 ? taxTotal.getRight() : BigDecimal.ZERO)
                .add(taxTotal.getLeft().compareTo(BigDecimal.ZERO) < 0 ? taxTotal.getLeft().abs() : BigDecimal.ZERO);

        vatReturn.setPayableTaxTotal(payableTaxTotal);
        vatReturn.setReclaimableTaxTotal(reclaimableTaxTotal);
        event.setStatus(EventStatus.COMPLETED.name());
        vatReturnManager.update(vatReturn);
    }

    protected void onUnFileEvent(EdsBusinessEvent event) {
        EdsVatReturn vatReturn = (EdsVatReturn) vatReturnManager.get(event.getEntityID());
        vatReturnManager.unfileVatReturnTransactions(vatReturn.getObjectID());
        event.setStatus(EventStatus.COMPLETED.name());
        EdsTransaction transaction = transactionManager.getTransactionByVatReturnId(vatReturn.getObjectID());
        if (transaction != null) {
            transaction.setDeleted(Boolean.TRUE);
            transactionManager.update(transaction);
        }
    }

    void onFileUaeVatEvent(EdsVatReturn vatReturn) {
        Set<Integer> transactionIds = new HashSet<>();
        {
            List<Object[]> salesAndOtherOutputs = uaeVatReturnManager.getSalesAndOtherOutputs(vatReturn.getToDate(), null, null, null);
            salesAndOtherOutputs.forEach(objs -> transactionIds.add((Integer) objs[6]));
        }

        reverseCharge:
        {
            List<Object[]> reverseCharges = uaeVatReturnManager.getReverseCharges(vatReturn.getToDate(), null);
            reverseCharges.forEach(objs -> transactionIds.add((Integer) objs[6]));


        }
        goodsImports:
        {
            List<Object[]> importedGoods = uaeVatReturnManager.geteGoodsImported(vatReturn.getToDate(), null);
            importedGoods.forEach(objs -> transactionIds.add((Integer) objs[5]));
        }
        vatOnExpensesAndAllOtherInputs:
        {
            List<Object[]> standardRatedExpenses = uaeVatReturnManager.getStandardRatedExpenses(vatReturn.getToDate(), null);
            standardRatedExpenses.forEach(objs -> transactionIds.add((Integer) objs[6]));
        }
        vatReturnManager.fileVatReturnTransactions(Lists.newArrayList(transactionIds), vatReturn.getObjectID());
    }

    void onFileKsaVatEvent(EdsVatReturn vatReturn) {
        Set<Integer> transactionIds = new HashSet<>();

        fileVatOnSalesAndAllOtherOutputs:
        {
            List<Object[]> salesAndOtherOutputs = ksaVatReturnManager.getInvoiceTaxableTransactions(vatReturn.getToDate(), null, null, RECEIVABLE, true);
            salesAndOtherOutputs.forEach(objs -> fillTransactionIds(objs[6], transactionIds));
            salesAndOtherOutputs = ksaVatReturnManager.getInvoiceTaxableTransactions(vatReturn.getToDate(), null, null, RECEIVABLE, false);
            salesAndOtherOutputs.forEach(objs -> fillTransactionIds(objs[6], transactionIds));
        }

        fileVatOnPurchaseAndOtherInputs:
        {
            List<Object[]> purchaseInvoces = ksaVatReturnManager.getInvoiceTaxableTransactions(vatReturn.getToDate(), null, null, PAYABLE, true);
            purchaseInvoces.forEach(objs -> fillTransactionIds(objs[6], transactionIds));

            List<Object[]> expenses = ksaVatReturnManager.getExpenseTaxableTransactions(vatReturn.getToDate(), null, null);
            expenses.forEach(objs -> transactionIds.add((Integer) objs[6]));
        }

        reverseCharge:
        {
            List<Object[]> reverseCharges = ksaVatReturnManager.getReverseChargeApplicableTransactions(vatReturn.getToDate(), null);
            reverseCharges.forEach(objs -> transactionIds.add((Integer) objs[6]));
        }
        vatReturnManager.fileVatReturnTransactions(Lists.newArrayList(transactionIds), vatReturn.getObjectID());
    }

    void fillTransactionIds(Object object, Set<Integer> transactionIds) {
        if (object == null) {
            return;
        }
        if (object instanceof Integer) {
            transactionIds.add((Integer) object);
        } else {
            transactionIds
                    .addAll(Stream.of(String.valueOf(object).split("[,]")).map(obj -> Integer.valueOf(obj)).collect(Collectors.toList()));
        }
    }
}
