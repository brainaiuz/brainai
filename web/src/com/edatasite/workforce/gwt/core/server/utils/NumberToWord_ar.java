package com.edatasite.workforce.gwt.core.server.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Azamjon Ahmadjonov
 * Date: 2/22/2017
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class NumberToWord_ar extends NumberToWord{

    public static String convertNumberToArabicWords(String number) throws NumberFormatException {

        Double numParsed = Double.parseDouble(number);
        int decimalInt = anyMethod(numParsed);


        // the floating point number
        String theFloat = number.substring(number.indexOf('.') + 1);
        // the number
        String theNumber = number.substring(0, number.indexOf('.'));

        // check if its floating point number or not
        if (number.contains(".") && decimalInt > 0) { // yes

            // check how many digits in the number 1:x 2:xx 3:xxx 4:xxxx 5:xxxxx
            // 6:xxxxxx

            return switch (theNumber.length()) {
                case 1 -> convertOneDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 2 -> convertTwoDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 3 -> convertThreeDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 4 -> convertFourDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 5 -> convertFiveDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 6 -> convertSixDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                default -> "";
            };
        } else if (number.contains(".") && decimalInt == 0 && theNumber.length() == 1) {
            return convertOnlyOneDigits(theNumber);
        } else {
            return switch (theNumber.length()) {
                case 1 -> convertOneDigits(theNumber);
                case 2 -> convertTwoDigits(theNumber);
                case 3 -> convertThreeDigits(theNumber);
                case 4 -> convertFourDigits(theNumber);
                case 5 -> convertFiveDigits(theNumber);
                case 6 -> convertSixDigits(theNumber);
                default -> "";
            };

        }
    }

    // -------------------------------------------

    public static int anyMethod(double a){

        return (int) (((a+0.001)*100)%100);
    }

    private static String convertOneDigits(String oneDigit) {
        return switch (Integer.parseInt(oneDigit)) {
            case 1 -> "واحد";
            case 2 -> "إثنان";
            case 3 -> "ثلاث";
            case 4 -> "اربع";
            case 5 -> "خمس";
            case 6 -> "ست";
            case 7 -> "سبع";
            case 8 -> "ثماني";
            case 9 -> "تسع";
            default -> "";
        };
    }

    private static String convertOnlyOneDigits(String oneDigit) {
        return switch (Integer.parseInt(oneDigit)) {
            case 1 -> "واحد";
            case 2 -> "إثنان";
            case 3 -> "ثلاثة";
            case 4 -> "اربعة";
            case 5 -> "خمسة";
            case 6 -> "ستة";
            case 7 -> "سبعة";
            case 8 -> "ثمانية";
            case 9 -> "تسعة";
            default -> "";
        };
    }

    private static String convertTwoDigits(String twoDigits) {
        if (twoDigits.length() < 2) {
            twoDigits += "0";
        }
        String returnAlpha = "00";
        // check if the first digit is 0 like 0x
        if (twoDigits.charAt(0) == '0' && twoDigits.charAt(1) != '0') { // yes
            // convert two digits to one
            return convertOneDigits(String.valueOf(twoDigits.charAt(1)));
        } else { // no
            // check the first digit 1x 2x 3x 4x 5x 6x 7x 8x 9x
            switch (getIntVal(twoDigits.charAt(0))) {
                case 1 -> { // 1x
                    if (getIntVal(twoDigits.charAt(1)) == 1) {
                        return "أحد عشر";
                    }
                    if (getIntVal(twoDigits.charAt(1)) == 2) {
                        return "إثنا عشر";
                    } else {
                        return convertOneDigits(String.valueOf(twoDigits.charAt(1))) + " " + "عشر";
                    }
                }
                case 2 -> // 2x x:not 0
                        returnAlpha = "عشرون";
                case 3 -> // 3x x:not 0
                        returnAlpha = "ثلاثون";
                case 4 -> // 4x x:not 0
                        returnAlpha = "أريعون";
                case 5 -> // 5x x:not 0
                        returnAlpha = "خمسون";
                case 6 -> // 6x x:not 0
                        returnAlpha = "ستون";
                case 7 -> // 7x x:not 0
                        returnAlpha = "سبعون";
                case 8 -> // 8x x:not 0
                        returnAlpha = "ثمانون";
                case 9 -> // 9x x:not 0
                        returnAlpha = "تسعون";
                default -> returnAlpha = "";
            }
        }

        // 20 - 99
        // x0 x:not 0,1
        if (convertOneDigits(String.valueOf(twoDigits.charAt(1))).length() == 0) {
            return returnAlpha;
        } else { // xx x:not 0
            return convertOneDigits(String.valueOf(twoDigits.charAt(1))) + " و " + returnAlpha;
        }
    }

    private static String convertThreeDigits(String threeDigits) {

        // check the first digit x00
        switch (getIntVal(threeDigits.charAt(0))) {
            case 1 -> { // 100 - 199
                if (getIntVal(threeDigits.charAt(1)) == 0) { // 10x
                    if (getIntVal(threeDigits.charAt(2)) == 0) { // 100
                        return "مائه";
                    } else { // 10x x: is not 0
                        return "مائه" + " و " + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// 1xx x: is not 0
                    return "مائه" + " و " + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            case 2 -> { // 200 - 299
                if (getIntVal(threeDigits.charAt(1)) == 0) { // 20x
                    if (getIntVal(threeDigits.charAt(2)) == 0) { // 200
                        return "مائتين";
                    } else { // 20x x:not 0
                        return "مائتين" + " و " + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else { // 2xx x:not 0
                    return "مائتين" + " و " + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            case 3, 4, 5, 6, 7, 8, 9 -> { // 300 - 999
                if (getIntVal(threeDigits.charAt(1)) == 0) { // x0x x:not 0
                    if (getIntVal(threeDigits.charAt(2)) == 0) { // x00 x:not 0
                        //return convertOneDigits(String.valueOf(threeDigits.charAt(1))) + "مائه";
                        return convertOneDigits(String.valueOf(threeDigits.charAt(0))) + "مائه";
                    } else { // x0x x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(0))) + "مائه" + " و "
                                + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else { // xxx x:not 0
                    return convertOneDigits(String.valueOf(threeDigits.charAt(0))) + "مائه" + " و "
                            + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            case 0 -> { // 000 - 099
                if (threeDigits.charAt(1) == '0') { // 00x
                    if (threeDigits.charAt(2) == '0') { // 000
                        return "";
                    } else { // 00x x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else { // 0xx x:not 0
                    return convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            default -> {
                return "";
            }
        }
    }

    private static String convertFourDigits(String fourDigits) {
        // xxxx
        switch (getIntVal(fourDigits.charAt(0))) {
            case 1 -> { // 1000 - 1999
                if (getIntVal(fourDigits.charAt(1)) == 0) { // 10xx x:not 0
                    if (getIntVal(fourDigits.charAt(2)) == 0) { // 100x x:not 0
                        if (getIntVal(fourDigits.charAt(3)) == 0) { // 1000
                            return "ألف";
                        } else { // 100x x:not 0
                            return "ألف" + " و " + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else { // 10xx x:not 0
                        return "ألف" + " و " + convertTwoDigits(fourDigits.substring(2, 4));
                    }
                } else { // 1xxx x:not 0
                    return "ألف" + " و " + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }
            case 2 -> { // 2000 - 2999
                if (getIntVal(fourDigits.charAt(1)) == 0) { // 20xx
                    if (getIntVal(fourDigits.charAt(2)) == 0) { // 200x
                        if (getIntVal(fourDigits.charAt(3)) == 0) { // 2000
                            return "ألفين";
                        } else { // 200x x:not 0
                            return "ألفين" + " و " + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else { // 20xx x:not 0
                        return "ألفين" + " و " + convertTwoDigits(fourDigits.substring(2, 4));
                    }
                } else { // 2xxx x:not 0
                    return "ألفين" + " و " + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }
            case 3, 4, 5, 6, 7, 8, 9 -> { // 3000 - 9999
                if (getIntVal(fourDigits.charAt(1)) == 0) { // x0xx x:not 0
                    if (getIntVal(fourDigits.charAt(2)) == 0) { // x00x x:not 0
                        if (getIntVal(fourDigits.charAt(3)) == 0) { // x000 x:not 0
                            return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف";
                        } else { // x00x x:not 0
                            return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف" + " و "
                                    + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else { // x0xx x:not 0
                        return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف" + " و "
                                + convertTwoDigits(fourDigits.substring(2, 4));
                    }
                } else { // xxxx x:not 0
                    return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف" + " و "
                            + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }
            default -> {
                return "";
            }
        }
    }

    private static String convertFiveDigits(String fiveDigits) {
        if (convertThreeDigits(fiveDigits.substring(2, 5)).length() == 0) { // xx000
            // x:not
            // 0
            return convertTwoDigits(fiveDigits.substring(0, 2)) + " ألف ";
        } else { // xxxxx x:not 0
            return convertTwoDigits(fiveDigits.substring(0, 2)) + " ألفا " + " و "
                    + convertThreeDigits(fiveDigits.substring(2, 5));
        }
    }

    private static String convertSixDigits(String sixDigits) {

        if (convertThreeDigits(sixDigits.substring(2, 5)).length() == 0) { // xxx000
            // x:not
            // 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ألف ";
        } else { // xxxxxx x:not 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ألفا " + " و "
                    + convertThreeDigits(sixDigits.substring(3, 6));
        }
    }

    private static int getIntVal(char c) {
        return Integer.parseInt(String.valueOf(c));
    }

    // ----------------------------------------------------------

    @Override
    public String toWord(Number number) {
        String str = String.valueOf(number.toString());
        return convertNumberToArabicWords(str);
    }

    @Override
    public String convert(Number number) {
        String str = String.valueOf(number.toString());
        return convertNumberToArabicWords(str);
    }
}
