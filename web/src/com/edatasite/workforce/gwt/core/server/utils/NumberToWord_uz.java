package com.edatasite.workforce.gwt.core.server.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberToWord_uz extends NumberToWord {
    private static final String[] majorNames = {"", " минг", " миллион", " миллиард", " триллион", " квадриллион", " квинтиллион"};

    private static final String[] tensNames = {"", " ўн", " йигирма", " ўттиз", " қирқ", " эллик", " олтмиш", " етмиш", " саксон", " тўқсон"};

    private static final String[] numNames = {"", " бир", " икки", " уч", " тўрт", " беш", " олти", " етти", " саккиз", " тўққиз"};

    private static final String[] fractions = {"", " ўнда", " юзда", " мингда", " ўн мингда", " юз мингда", " миллионда", " ўн миллионда", " юз миллионда", " миллиардда"};

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
            while (decValue.length() > 1 && decValue.endsWith("0")) {
                decValue = decValue.substring(0, decValue.length() - 1);
            }
            StringBuilder temp = new StringBuilder("/1");
            for (int i = 0; i < decValue.length(); i++) {
                temp.append("0");
            }
            postfix = postfix.append("ва " + decValue + temp);


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

    private String convertLessThanOneThousand(int number) {
        String word;
        if (number % 100 < 10) {
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
        return numNames[number] + " юз" + word;
    }

    @Override
    public String convert(Number number) {
        StringBuilder numberWord = new StringBuilder("");
        BigDecimal d = new BigDecimal(number.toString());
        String result = "";
        long num = d.longValue();
        numberWord.append(getWords(num));

        String str = String.valueOf(number.toString());
        Integer pointIndex = str.indexOf('.');
        if (pointIndex > 0 && (d.compareTo(d.setScale(0, RoundingMode.FLOOR)) != 0)) {
            BigDecimal dec = d.subtract(d.setScale(0, RoundingMode.FLOOR)).movePointRight(d.scale());
            String w = getWords(dec);
            String temp = "";
           /* if (w != "") {
                numberWord.append(" butun").append(w);
                temp = fractions[d.scale()];
                numberWord.append(temp);
            }*/
        }
        result = numberWord.toString();
        if (!result.isEmpty()) {
            char capLetter = Character.toUpperCase(result.charAt(0));
            result = capLetter + result.substring(1, result.length());
        }
        return result;
    }

    private String getWords(Number number) {
        long num = number.longValue();
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
        return word.toString();

    }
}
