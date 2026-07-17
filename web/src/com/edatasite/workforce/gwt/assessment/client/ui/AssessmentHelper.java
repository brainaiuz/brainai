package com.edatasite.workforce.gwt.assessment.client.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AssessmentHelper implements Constants {
    private static Map ratingAsString = new HashMap();
    private static Map gradeAsString = new HashMap();


    public static String getRatingAsString(Double rating, Double maxRating) {
        double range = maxRating / 7;
        if (rating >= range) {
            rating = rating / range;
        } else {
            rating = 0.0;
        }
        return (String) getRatingAsStrings().get(rating);
    }

    public static String getRatingAsString(Double rating, AppraisalsSettingsItem settingsItem) {

        if (Utils.isCustomRateEnable()) {
            return getCustomRatingAsString(rating, settingsItem.getCustomRates());
        }

        double range = settingsItem.getStepSize();
        if (rating >= range) {
            rating = rating / range;
        } else {
            rating = 0d;
        }
        return (String) getRatingAsStrings().get(rating);
    }

    static {
        HrmsService.App.get().getAssassmentRatings(new AbstractAsyncCallback<HashMap<Double, String>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(HashMap<Double, String> result) {
                ratingAsString.putAll(result);
            }
        });

        HrmsService.App.get().getAssassmentGrades(new AbstractAsyncCallback<HashMap<String, String[]>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(HashMap<String, String[]> result) {
                gradeAsString.putAll(result);
            }
        });

    }

    public static String getRatingAsString(Double rating) {
        return rating != null ? ((String) getRatingAsStrings().get(rating)) : "0";
    }

    private static Map getRatingAsStrings() {
        return ratingAsString;
    }

    public static String getGradeAsString(String grade) {
        String[] rateByGrade = (String[]) getGradeAsStrings().get(grade);
        return rateByGrade[1];
    }

    private static Map getGradeAsStrings() {
        return gradeAsString;
    }

    public static String getColorByRate(int rate) {
        String color = "#FF0000";
        switch (rate) {
            case 0:
                color = "#0A7FFF";
                break;
            case 1:
                color = "#750000";
                break;
            case 2:
                color = "#BD0000";
                break;
            case 3:
                color = "#CC6E00";
                break;
            case 4:
                color = "#D79D00";
                break;
            case 5:
                color = "#D1CA00";
                break;
            case 6:
                color = "#859D00";
                break;
            case 7:
                color = "#3E8C0F";
                break;
        }
        return color;
    }

    public static String getReviewLinkForUI(AssessmentsListElem employeeAssessment) {
        return "assessment/"
                + employeeAssessment.getEmployeeAssessmentId().toString() + "/"
                + employeeAssessment.getStatusCode() + "/" + ASSESSMENT_SIMPLE;

    }

    public static String getReviewLinkForUI(InProgressAssessmentListElem employeeAssessment) {
        return "assessment/"
                + employeeAssessment.getEmployeeAssessmentId().toString() + "/"
                + employeeAssessment.getStatus() + "/" + ASSESSMENT_SIMPLE;

    }

    public static String getReviewLinkForUI(Integer employeeAssessmentId, String status) {
        return "assessment/"
                + employeeAssessmentId.toString() + "/"
                + status + "/" + ASSESSMENT_SIMPLE;

    }


    public static String getCustomTITLE(String text) {
        return "<b class=customTitle>" + text + "</b>";
    }

    public static String getScoreGradeName(Double score, BonusSettingsItem settingsItem) {
        ScoreItem scoreItem = settingsItem.getScoreItem(score);
        if (scoreItem != null) {
            return "Grade " + scoreItem.getName();
        }
        return "";
    }

    public static String getCustomRatingAsString(Double rating, Map<BigDecimal, String> ratings) {

        BigDecimal maxValue = getMaxValue(ratings);
        BigDecimal minValue = getMinValue(ratings);

        BigDecimal subtractedRate = new BigDecimal(rating).setScale(1, BigDecimal.ROUND_HALF_UP);
        BigDecimal rate = subtractedRate;
        BigDecimal step = new BigDecimal(0.1).setScale(1, BigDecimal.ROUND_HALF_UP);

        if (ratings.containsKey(rate)) {
            return ratings.get(rate);
        }
        do {
            rate = rate.add(step);
            subtractedRate = subtractedRate.subtract(step);

            if (ratings.containsKey(rate)) {
                return ratings.get(rate);

            }
            if (ratings.containsKey(subtractedRate)) {
                return ratings.get(subtractedRate);
            }
        }
        while (rate.compareTo(maxValue) < 0 || subtractedRate.compareTo(minValue) > 0);

        return getRatingAsString(rating);
    }

    public static BigDecimal getMaxValue(Map<BigDecimal, String> customRates) {
        BigDecimal maxValue = BigDecimal.ZERO;
        for (BigDecimal value : customRates.keySet()) {
            if (value.compareTo(maxValue) > 0) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    public static BigDecimal getMinValue(Map<BigDecimal, String> customRates) {
        BigDecimal minValue = new BigDecimal(100).setScale(1, BigDecimal.ROUND_HALF_UP);
        for (BigDecimal value : customRates.keySet()) {
            if (value.compareTo(minValue) < 0) {
                minValue = value;
            }
        }
        return minValue;
    }
}
