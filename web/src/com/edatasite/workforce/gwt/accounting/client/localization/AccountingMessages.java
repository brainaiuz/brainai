package com.edatasite.workforce.gwt.accounting.client.localization;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 02.11.2008
 * Time: 19:34:43
 * To change this template use File | Settings | File Templates.
 */

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface AccountingMessages extends Messages {

    String postedBy(String p0, String p1, String p2);

    String chooseInsertionType(String p0);

    String ifNotExistsInSystemChooseDefault(String p0);

    String deleteSelected(String p0);

    String pleaseSelectOneRow(String p0);

    String productsServices(String p0);

    String accounts(String p0);

    String invertoryItems(String p0);

    String foreignBalance(String p0);

    String adjustmentBalance(String p0);

    String exchangeGainLoss(String p0);

    String youDoNotHaveEnoughStockToBuild(String p0, String p1, String p2s);

    String errorOccuredWhileSavingProjectBalance();

    String thereWasReservedANonAvialableItem(String p0);

    String forSomething(String p0);

    String csvOfxSupported(String p0, String p1);

    String instructionForInventoryStockAdujustment(String p0, String p1, String p2, String p3, String p4, String p5, String p6, String p7, String p8);

    String forThePeriod(String p0, String p1);

    String approved(String p0);

    String declined(String p0);

    String submitted(String p0);

    String storefrontFeeInformation();

    String wfmJournalIDdata(String p0);

    String reversalOf(String p0);

    String transactionList(String p0);

    String taxRatesList(String p0);

    String paymentCreditDateMessage(String p0);

    String figuresConvertedIntoCurrency(String p0);

    String beginningPeriodRate(String p0, String p1);

    String currentPeriodRate(String p0, String p1);

    String endingBalance(String currencyName);

    String balance(String currencyName);

    String balanceValue(String currencyName);

    String total(String currencyName);

    String dynamicTotal(String p0);

    String dynamicSubTotal(String p0);

    String dynamicCurrencyView(String p0);

    String youDoNotHaveEnough(String p0);

    String youDoNotHaveEnoughSerail(String p0);

    String youDoNotHaveEnoughQuantity(String p0);

    String youDoNotHaveSufficientStock(String p0);

    String bookingReservation(String p0, String p1);

    String youDoNotHaveEnoughConsignmentQuantity(String p0);

    String youDoNotHaveEnoughQuantityToReserve(String p0);

    String youDoNotHaveEnoughQuantityToReserveSubmitToManager(String p0);

    String youDoNotHaveEnoughQuantityForConsignment(String p0);

////ClientSupplierView.java
////InvoiceListView.java

    String typeNumber(String p0);

    String typeDate(String p0);

    String paymentReceivedFromWithDate(String p0, String p1, String p2);

    String dynamicSentSuccessfully(String p0);

    String select(String p0);

    String invoiceWillBe(String p0);

    String overReceiveItems(String itemNames);

    String quotes(String p0);

    String orders(String p0);

    String refChequeNumber(String p0);

    String referenceWithNumberExists(String p0);

    String New(String s);

    String historyCreated(String p0);

    String historyUpdated(String p0);

    String updatedOn(String p0);

    String amountShouldBeEqualToItemsTotalAmount();

    String errorOccuredWhileSavingCheck();

    String checkSavedSuccessfully();

    String pleaseCheckItemsForSave();

    String salesQuoteConvertedToProject();

    String pleaseSpecifyRejectionReason();

    String currencyAdjusmentSavedSuccessfully();

    String theCurrencyOfTheManualEntryMust();

    String onlyOneForeignCurrencyAllowed();

    String pleaseSpecifyShipDate();

    String areYouSureYouWantToReverse(String p0);

    String reversedSuccessfully(String p0);

    String areYouSureYouWantToDelete(String p0);

    String deletedSuccessfully(String p0);

    String youCantAllocateMoreThanRemainingBalance();

    String wouldYouLikeToAllocate();

    String thereIsNoBidToConvert();

    String requestForQuoteSuccessfullyConverted();

    String pleaseSaveInvoiceFirst();

    String creditNoteWithThisNumberIsAlreadyExists();

    String youCantEditCNwhichHasPayment();

    String creditNoteTotalAmountShouldBeLess(String p0);

    String quoteWithThisNumberIsAlreadyExists();

    String orderWithThisNumberIsAlreadyExists();

    String gdnWithThisNumberIsAlreadyExist();

    String theAmountEnteredExceedsTheAmountDue();

    String thereAreNoAmountsEnteredToAllocate();

    String errorOccuredWhileAllocatingData();

    String allocatedSuccessfully();

    String theTotalAmountEnteredExceedsTheOutstandingCredit(String p0);

    String rfqDeletedSuccessfully();

    String rfpDeletedSuccessfully();

    String termDeletedSuccessfully();

    String thereIsNoDataToSave();

    String pleaseSelectConvertOption();

    String youCantEnterMoreThanRemainingPercent();

    String youCantEnterMoreThanRemainingAmount();

    String pleaseCheckAllEnteredFields();

    String thereIsNotEnoughQuantity();

    String currentlyYouDontHaveAnyPickLists();

    String totalSelectedWithMarkup();

    String remainingTotal(String p0);

    String currentlyYouDontHaveAnyShippingMethods();

    String shippingMethodHasBeenSuccessfullyDeleted();

    String thisShippingMethodIsInUse();

    String postedByOn(String p0, String p1);

    String afterTime(String p0);

    String pleaseEnterZIPCode(String p0);

    String digit(String p0);

    String pleaseEnter(String p0);

    String cannotBeZero(String p0);

    String mtNumberExists(String p0);

    String ended();

    String dynamicBankTax(String p0);

    String dynamicBaseTax(String p0);

    String dateShouldBeAfterClosedBeforeDate(String p0, String p1);

    String totalAmountCantLessThanZero();

    String creditLimitQuoteMessage(String p0, String p1, String p2);

    String creditLimitPicklistMessage(String p0, String p1, String p2);

    String datePaidMessage(String p0);

    String valueInNUmberFieldIsInvalid();

    String numberAlreadyExists(String param1, String param2);

    String overpaymant(String p0, String p1);

    String paymentAmountMustMatch();

    String youCantEnterShipQtyMoreThanOrderedQty();

    String youCantEnterBookReservationMoreThanOrderedQty();

    String youCantEnterReadyToShipShipQtyMoreThanOrderedQty();

    String youCannotShipMoreThanReadyToShip();

    String thereShouldBeAtLeastOneUnit();

    String confirmConvertingToPOMessage(String p0);

    String youDontHaveNecessaryPermissionsTo(String actionName);

    String dateShouldBeAfterconversionDate(String actionName);

    String saleBaseInvoice(String s);

    String saleOrderBaseInvoice();

    String groupedBy(String s);

    String minValue(String s);

    String maxInstallmentLimit(String limit);

    class App {
        private static AccountingMessages instance;

        public static AccountingMessages get() {
            if (instance == null) {
                instance = GWT.create(AccountingMessages.class);
            }
            return instance;
        }
    }

}
