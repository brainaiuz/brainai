package com.edatasite.workforce.gwt.core.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * User: Employee
 * Date: Nov 10, 2009
 * Time: 3:08:57 PM
 */
public interface CoreMessages extends Messages {

    String callUs(String p0);

    String youDontHaveEnoughPermissionToSomethingSelectedTasks(String p0);

    String doYouWantToChangeStartDateOfTask(String p0);

    String doYouWantToChangeEndDateOfTask(String p0);

    String youCanTasteKPIDOTCOM(String p0);

    String tryNowEMmarkNoteNoCreditCardsRequired(String p0, String p1);

    String exceededUserLimit(String p0);

    String exceededNoAccessUserLimit(String p0);

    String exceededEssUserLimit(String p0);

    String validationPast(String p0);

    String validationFuture(String p0);

    String valueCannotBeEmpty(String p0);

    String xDaysLeft(String days);

    String paidDaysLeft(String days);

    String tapButtonToCreate();

    String pdfPagination(String p0, String p1);

    String perfomanceNoteListTableName(String p0);

    String departmentEmployee(String p0);

    String promotionsPenaltiesListTableName(String p0);

    String bonusRecommendationsListTableName(String p0);

    String shellWasDeactivated(String p0);

    String shellWasClosed(String p0);

    String shellWasActivated(String p0);

    String message1(String p0);

    String message1HideLink(String p0);

    String thisAccountIsToExpireWithinDaysForAdmin(String p0);

    String thisAccountIsToExpireWithinDaysForAdminHideLink(String p0);

    String thisAccountIsToExpireWithinDays(String p0);


    String imageCropFailed(String p0);

    String numberFromTo(String p0, String p1);

    String areYouSureYouWantToResetLogo(String p0);


    String doYouWantToRemoveProfileImage(String p0);


    String valueIsNotAvailable(String p0);


    String fieldShouldBeNumberFormat(String p0);

    String valueAlreadyExists(String p0, String p1);

    String dontHaveLicense(String p0);

    String paidAccountIsToExpireWithinDays(String days);

    String trialAccountIsToExpireWithinDays(String days);


    class App {
        private static CoreMessages instance;

        public static CoreMessages get() {
            if (instance == null) {
                instance = GWT.create(CoreMessages.class);
            }
            return instance;
        }
    }

}
