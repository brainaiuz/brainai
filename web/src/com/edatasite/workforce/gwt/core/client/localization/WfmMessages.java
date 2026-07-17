package com.edatasite.workforce.gwt.core.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface WfmMessages extends Messages {

    String address(String p0);

    String forwardedMessage();

    String from();

    String to();

    String employeeStepHasBeenApprovedSuccessfully();

    String employeeStepHasBeenRejectedSuccessfully();

    String noTasksText(String p0);

    String sureYouWantToDelete(String p0, String p1);

    String yourSomethingHasBeenDeleted(String p0);

    String conversionBalanceDate(String p0);

    String youDoNotHaveEnoughPermission(String p0, String p1);

    String pleaseSelectOneRow(String s);

    String pleaseSelectOneRowCopy(String s);

    String pleaseSelectOneRowMove(String s);

    String attachmentHelpText(String s0);

    String duplicateDetectedMessage(String p0, String p1);

    String messItemSucImported(String p0);

    String messImportItemError(String p0);

    String doYouWantToChangeStatusTo(String p0);

    String messImportName(String p0);

    String messImportDescription(String p0, String p1);

    String importingMessage(String p0);

    String pleaseSelectOneOrMoreRow(String p0);

    String pleaseSelectOneToDetectDuplicates(String p0);

    String displayItems(int i, int temp, int total);

    String issues(String p0);

    String helloFromKIA(String name);

    String poweredByAi();

    String aiTooltipInfo();

    String placeholderExample();

    String reportPrompt();

    String reportPlaceholder();

    String onlyForPaidSubscription();

    String publishedOn(String fullName);

    String paymentDeductionCannotBeDeleted(String p0);

    String creates(String p0);

    String thereAreNoRelatedSomethingItemsYet(String p0);

    String thereAreNoSomethingItemsYet(String p0);

    String pleaseChooseEmployeeToShare(String employeeText);

    String currentlyDonotHaveAny(String p0);

    String pleaseAddNew(String p0);

    String edit(String view);

    String add(String view);

    String view(String view);

    String pleaseSelect(String s);

    String sureYouWantToUnAssign(String s);

    String importEntity(String s);

    String copy(String s);

    String related(String s);

    String filter(String s);

    String welcomeXhr();

    String welcomeIframe();

    String sizeMB();

    String sizeKB();

    String cancel();

    String ready();

    String uploading();

    String extensionNotAllowed(String allowedExtensions);

    String fileSizeIsZero();

    String fileIsTooSmall(String minSize);

    String fileIsTooLarge(String minSize);

    String errorUploadingFile(String fileName);

    String paymentCategoryWithCodeAlreadyExist(String code);

    String deductionCategoryWithCodeAlreadyExist(String code);

    String taxCategoryWithCodeAlreadyExist(String code);

    String employerContributionCategoryWithCodeAlreadyExist(String code);

    String alreadyHaveSignature();

    String youDontHaveEnoughPermissons();

    String ofNumber(String numberOfPages);

    String invalidValuePleaseEnterNumber();

    String displayingItemsFirstOf(String p0, String p1, String p2);

    String addingByClicking(String p0);

    String sendingMessageWithoutSubjectWarn();

    String setPredecessorTo(String p0);

    String setSuccessorTo(String p0);

    String convertToProjectRequiresAccount(String p0);

    String thisCategoryIsAlreadyExist();

    String currentlyThereAreNotCategories();

    String successSMSTemplateSaved();

    String smsAlertDeleted();

    String telegramAlertDeleted();

    String smsAlertSuccesfully(String p0);

    String webHookSuccessfully(String p0);

    String workflowStepDeleted();

    String workflowStepSuccesfully(String p0);

    String workflowPushSuccesfully(String p0);

    String pushNotificationDeleted();

    String pleaseChooseOneOrMoreItemsToCopy();

    String pleaseChooseCompanyToCopyItemsFrom();

    String onboardingStepsHasBeenCopiedSucc();

    String errorOccuredWhileCopyingSteps();

    String pleaseChooseCompanyToCopyItemsTo();

    String deletePayrollBatchConfirmation(String p0);

    String dateShouldBeAfterClosedBeforeDate(String p0, String p1);

    String expireDateCannotBeEarliesThanIssueDate();

    String nSecondsLeft(String p0);

    String total(String p0);

    String currentlyLimitedContactExport(String p0);

    String employeeParticipatedInApprovalProcess(String p0, String p1);

    String savedSuccessfully(String p0);

    String changedSuccessfully(String p0);

    String hasBeenSavedSuccessfully(String p0);

    String deletedSuccessfully(String p0);

    String areYouSureYouWantToDeleteThe(String p0);

    String unitMeasurementIsUsedInItemNTimes(String p0, String p1);

    String notFilled(String p0);

    String notSelected(String p0);

    String asOfDateCannotBeEarlierThanOPeningDate(String p0);

    String sorryVoidDateCannotBeEarlierThanPrepaymentDate();

    String itemsSuccessfullyImported(String p0);

    String pleaseMakeSureIsNotZero(String s);

    String customPrice(String p0);

    String callTo(String p0);

    String smsTo(String p0);

    String setDashboardConfigINFO(String p0, String p1);

    String smbdyUnavavailableFromTo(String p0, String p1, String p2);

    String dataImportGuideInfo(String p0, String p1);

    String configureEmailDescription(String p0);

    String days(String p0);

    String day(Integer p0);

    String emails(String p0);

    String fromToOf(Integer p0, Integer p1, Integer p2);

    String notMatchingPrecisionAndScale(String fieldName, Integer precision, Integer scale);

    String withThisParameterDoesNotExist(String param);

    String nameStorage(String hostName);

    String areYouStillThere(String name);

    String sampleData(String productName);

    String id(Integer id);

    String removeSampleData1(String p0);

    String removeSampleData2(String p0);

    String removeSampleData3(String p0);

    String removeSampleData4(String p0, String p1);

    String removeSampleData5();

    String lastOneCompanyAdminDelete();

    String youCanNotAddMoreThanWidgets(String dashboardWidgetsMaxLimit);

    String usedMemory(String formatted, Double aDouble);

    String youCanEditAsteriksOrTwilio(String provider);

    String youCantConvert(String form);

    String selectedFiltr();

    String wayViceversaUpdatesInBothDirectionsKeepsHostNameContactsAndServerNameContactsInSynchWithEachOther(String p0, String p1);

    String wayServerNameMasterServerNameContactUpdatesAreAppliedToHostNameContactsOnly(String p0, String p1, String p2);

    String wayHostNameMasterHostNameUpdatesAreAppliedToServerNameContactsOnly(String p0, String p1, String p2);

    String infoAboutCustomFormAvaibility();

    String areYouSureWantToAddThisAdditionalPayment();

    String youWantToResetSettings(String val1, String val2);

    String youMustEnterMoreMinimumLeaveDays(String p0);

    String allowedCharLimit(String p0, String p1);

    String widgetRequiredFormat(String p0, String p1);

    String fieldRequired(String p0);

    String currentlyThereAreNo(String goalType);

    String youCanStartRegistering(String addNewGoal);

    String notPiadMember(String name);
    String pendingMember(String name);


    class App {
        private static WfmMessages instance;

        public static WfmMessages get() {
            if (instance == null) {
                instance = GWT.create(WfmMessages.class);
            }
            return instance;
        }
    }
}
