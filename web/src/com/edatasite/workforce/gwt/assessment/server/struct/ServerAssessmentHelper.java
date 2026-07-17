package com.edatasite.workforce.gwt.assessment.server.struct;

import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 9/14/12
 * Time: 12:43 PM
 */
public class ServerAssessmentHelper implements Constants {

    public static String getRatingAsString(HashMap<Double, String> ratingAsStrings, Double rating, AppraisalsSettingsItem settingsItem) {

        if (settingsItem.isCustomRateEnable()) {
            return getCustomRatingAsString(ratingAsStrings, rating, settingsItem.getCustomRates());
        }

        double range = settingsItem.getStepSize();
        if (rating >= range) {
            rating = rating / range;
        } else {
            rating = 1d;
        }
        return ratingAsStrings.get(rating);
    }

    private static String getRatingAsString(HashMap<Double, String> ratingAsStrings, Double rating) {
        return rating != null ? ratingAsStrings.get(rating) : "0";
    }


    private static String getCustomRatingAsString(HashMap<Double, String> ratingAsStrings, Double rating, Map<BigDecimal, String> ratings) {

        BigDecimal maxValue = getMaxValue(ratings);
        BigDecimal minValue = getMinValue(ratings);

        BigDecimal subtractedRate = new BigDecimal(rating).setScale(1, RoundingMode.HALF_UP);
        BigDecimal rate = subtractedRate;
        BigDecimal step = new BigDecimal("0.1").setScale(1, RoundingMode.HALF_UP);

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

        return getRatingAsString(ratingAsStrings, rating);
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
        BigDecimal minValue = new BigDecimal(100).setScale(1, RoundingMode.HALF_UP);
        for (BigDecimal value : customRates.keySet()) {
            if (value.compareTo(minValue) < 0) {
                minValue = value;
            }
        }
        return minValue;
    }
}
