package com.edatasite.workforce.gwt.accounting.client;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 5/17/12
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class NumberToWord_en extends NumberToWord {

    private static final String[] majorNames = {"", " thousand", " million", " billion", " trillion", " quadrillion", " quintillion"};

    private static final String[] tensNames = {"", " ten", " twenty", " thirty", " forty", " fifty", " sixty", " seventy", " eighty", " ninety"};

    private static final String[] numNames = {"", " one", " two", " three", " four", " five", " six", " seven", " eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen", " seventeen", " eighteen", " nineteen"};


    private String convertLessThanOneThousand(int number) {
        String word;
        if (number % 100 < 20) {
            word = numNames[(number % 100)];
            number /= 100;
        } else {
            word = numNames[(number % 10)];
            number /= 10;

            word = tensNames[(number % 10)] + word;
            number /= 10;
        }
        if (number == 0)
            return word;
        return numNames[number] + " hundred" + word;
    }

    @Override
    public String toWord(Number number) {
        if (number instanceof BigDecimal) {
            number = ((BigDecimal) number).setScale(3, BigDecimal.ROUND_HALF_UP);
        }

        String result = "";
        String prefix = "";
        StringBuffer postfix = new StringBuffer(" ");
        long num = number.longValue();
        String str = String.valueOf(number.toString());
        Integer pointIndex = str.indexOf('.');
        if (pointIndex > 0) {
            String decValue = str.substring(pointIndex + 1, str.length());
            postfix = postfix.append("and " + Integer.valueOf(decValue) + "/100");

        } else {
            postfix = postfix.append("0/100");
        }
        StringBuilder word = new StringBuilder();
        int place = 0;
        do {
            int n = (int) (num % 1000L);
            if (n != 0) {
                String s = convertLessThanOneThousand(n);
                word.insert(0, s + majorNames[place]);
            }
            place++;
            num /= 1000L;
        } while (num > 0L);
        result = (prefix + word + postfix).trim();
        char capLetter = Character.toUpperCase(result.charAt(0));
        result = capLetter + result.substring(1, result.length());
        return result;
    }
}
