package com.edatasite.workforce.gwt.core.server.utils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 5/17/12
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class NumberToWord_ru extends NumberToWord {

    private static final String[] hundredsNames = {""," сто"," двести"," триста"," четыреста"," пятьсот"," шестьсот"," семьсот"," восемьсот"," девятьсот"};
    private static final String[] tensNames = {""," десять"," двадцать"," тридцать"," сорок"," пятьдесят"," шестьдесят"," семьдясят"," восемьдесят"," девяносто"};
    private static final List<Map<Gender, String>> numNames = CollectionUtils.list(
            CollectionUtils.map(Gender.masculine, "", Gender.feminine, "", Gender.neuter, ""),
            CollectionUtils.map(Gender.masculine, " один", Gender.feminine, " одна", Gender.neuter, " одно"),
            CollectionUtils.map(Gender.masculine, " два", Gender.feminine, " две", Gender.neuter, " два"),
            CollectionUtils.map(Gender.masculine, " три", Gender.feminine, " три", Gender.neuter, " три"),
            CollectionUtils.map(Gender.masculine, " четыре", Gender.feminine, " четыре", Gender.neuter, " четыре"),
            CollectionUtils.map(Gender.masculine, " пять", Gender.feminine, " пять", Gender.neuter, " пять"),
            CollectionUtils.map(Gender.masculine, " шесть", Gender.feminine, " шесть", Gender.neuter, " шесть"),
            CollectionUtils.map(Gender.masculine, " семь", Gender.feminine, " семь", Gender.neuter, " семь"),
            CollectionUtils.map(Gender.masculine, " восемь", Gender.feminine, " восемь", Gender.neuter, " восемь"),
            CollectionUtils.map(Gender.masculine, " девять", Gender.feminine, " девять", Gender.neuter, " девять"),
            CollectionUtils.map(Gender.masculine, " десять", Gender.feminine, " десять", Gender.neuter, " десять"),
            CollectionUtils.map(Gender.masculine, " одиннадцать", Gender.feminine, " одиннадцать", Gender.neuter, " одиннадцать"),
            CollectionUtils.map(Gender.masculine, " двенадцать", Gender.feminine, " двенадцать", Gender.neuter, " двенадцать"),
            CollectionUtils.map(Gender.masculine, " тринадцать", Gender.feminine, " тринадцать", Gender.neuter, " тринадцать"),
            CollectionUtils.map(Gender.masculine, " четырнадцать", Gender.feminine, " четырнадцать", Gender.neuter, " четырнадцать"),
            CollectionUtils.map(Gender.masculine, " пятнадцать", Gender.feminine, " пятнадцать", Gender.neuter, " пятнадцать"),
            CollectionUtils.map(Gender.masculine, " шестнадцать", Gender.feminine, " шестнадцать", Gender.neuter, " шестнадцать"),
            CollectionUtils.map(Gender.masculine, " семьнадцать", Gender.feminine, " семьнадцать", Gender.neuter, " семьнадцать"),
            CollectionUtils.map(Gender.masculine, " восемнадцать", Gender.feminine, " восемнадцать", Gender.neuter, " восемнадцать"),
            CollectionUtils.map(Gender.masculine, " девятнадцать", Gender.feminine, " девятнадцать", Gender.neuter, " девятнадцать")
    );

    private String convertLessThanOneThousand(int number, Gender gender) {
        String soFar;
        if (number % 100 < 20) {
            soFar = numNames.get((number % 100)).get(gender);
            number /= 100;
        } else {
            soFar = numNames.get((number % 10)).get(gender);
            number /= 10;

            soFar = tensNames[(number % 10)] + soFar;
            number /= 10;
        }
        if (number == 0)
            return soFar;
        return hundredsNames[number] + soFar;
    }

    @Override
    public String toWord(Number number) {
        long num = number.longValue();
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        String snumber = df.format(num);
        int length = snumber.length();
        String soFar = "";
        int billions = Integer.parseInt(snumber.substring(0, 3));
        // nnnXXXnnnnnn
        int millions = Integer.parseInt(snumber.substring(3, 6));
        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9, 12));

        Set<Integer> specialSuffexes = CollectionUtils.set(2, 3, 4);
        String tradBillions = billions == 0 ?
                "" :
                billions % 10 == 1 ?
                        convertLessThanOneThousand(billions, Gender.masculine) + " миллиард " :
                        specialSuffexes.contains(billions % 10) ?
                                convertLessThanOneThousand(billions, Gender.masculine) + " миллиарда " :
                                convertLessThanOneThousand(billions, Gender.masculine) + " миллиардов ";

        String tradMillions = millions == 0 ?
                "" :
                millions % 10 == 1 ?
                        convertLessThanOneThousand(millions, Gender.masculine) + " миллион " :
                        specialSuffexes.contains(millions % 10) ?
                                convertLessThanOneThousand(millions, Gender.masculine) + " миллиона " :
                                convertLessThanOneThousand(millions, Gender.masculine) + " миллионов ";

        String tradHundredThousands = hundredThousands == 0 ?
                "" :
                hundredThousands % 10 == 1 ?
                        convertLessThanOneThousand(hundredThousands, Gender.feminine) + " тысяча " :
                        specialSuffexes.contains(hundredThousands % 10) ?
                                convertLessThanOneThousand(hundredThousands, Gender.feminine) + " тысячи " :
                                convertLessThanOneThousand(hundredThousands, Gender.feminine) + " тысяч ";

        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands, Gender.masculine);
        String result = tradBillions + tradMillions + tradHundredThousands + tradThousand;

        result = result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ").trim();
        if (!result.isEmpty()) {
            char capLetter = Character.toUpperCase(result.charAt(0));
            result = capLetter + result.substring(1, result.length());
        }
        return result;
    }

    @Override
    public String convert(Number number) {
        long num = number.longValue();
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        String snumber = df.format(num);
        int length = snumber.length();
        String soFar = "";
        int billions = Integer.parseInt(snumber.substring(0, 3));
        // nnnXXXnnnnnn
        int millions = Integer.parseInt(snumber.substring(3, 6));
        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9, 12));

        Set<Integer> specialSuffexes = CollectionUtils.set(2, 3, 4);
        String tradBillions = billions == 0 ?
                "" :
                billions % 10 == 1 ?
                        convertLessThanOneThousand(billions, Gender.masculine) + " миллиард " :
                        specialSuffexes.contains(billions % 10) ?
                                convertLessThanOneThousand(billions, Gender.masculine) + " миллиарда " :
                                convertLessThanOneThousand(billions, Gender.masculine) + " миллиардов ";

        String tradMillions = millions == 0 ?
                "" :
                millions % 10 == 1 ?
                        convertLessThanOneThousand(millions, Gender.masculine) + " миллион " :
                        specialSuffexes.contains(millions % 10) ?
                                convertLessThanOneThousand(millions, Gender.masculine) + " миллиона " :
                                convertLessThanOneThousand(millions, Gender.masculine) + " миллионов ";

        String tradHundredThousands = hundredThousands == 0 ?
                "" :
                hundredThousands % 10 == 1 ?
                        convertLessThanOneThousand(hundredThousands, Gender.feminine) + " тысяча " :
                        specialSuffexes.contains(hundredThousands % 10) ?
                                convertLessThanOneThousand(hundredThousands, Gender.feminine) + " тысячи " :
                                convertLessThanOneThousand(hundredThousands, Gender.feminine) + " тысяч ";

        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands, Gender.masculine);
        String result = tradBillions + tradMillions + tradHundredThousands + tradThousand;

        result = result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ").trim();
        if (!result.isEmpty()) {
            char capLetter = Character.toUpperCase(result.charAt(0));
            result = capLetter + result.substring(1, result.length());
        }
        return result;
    }

    public enum Gender {

        masculine, feminine, neuter;
    }
}


