package com.edatasite.workforce.gwt.payroll.client.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar
 * Date: Jul 30, 2010
 * Time: 6:48:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollValidator {

    /**
     * Because we want to do more than simply
     * search for literal pieces of text,
     * we need to reserve certain characters
     * for special use. In the regex flavors discussed in this tutorial,
     * there are 11 characters with special meanings:
     * the opening square bracket - ( [ ),
     * the backslash - ( \ ),
     * the caret - ( ^ ),
     * the dollar sign - ( $ ),
     * the period or dot - ( . ),
     * the vertical bar or pipe symbol - ( | ),
     * the question mark - ( ? ),
     * the asterisk or star - ( * ),
     * the plus sign - ( + ),
     * the opening round bracket - ( ( ) and
     * the closing round bracket - ( ) ).
     * These special characters are often called "metacharacters".
     */
    // {(}  {[}  {{}  {\}  {^}  {-}  {$}  {|}  {]}  {}}  {)}  {?}  {*}  {+}  {.} (all)
    //{[} , {\} , {^} , {$} , {.} , {|} , {?} , {*} , {+} , {(} , {)} (something)

    /**
     * CHARACTER_SET_A - Full character set
     * Letters, lower case - a to z, Letters, upper case - A to Z, Numerals - 0 to 9,
     * Space character - (   ), Full stop - ( . ) , Comma - ( , ), Hyphen/minus sign - ( - ), Opening parentheses - ( ( ),
     * Closing parentheses - ( ) ), Oblique stroke/slash - ( / ), Equals sign - ( = ), Exclamation mark - ( ! ),
     * Quotation mark - ( " ), Percentage sign - ( % ), Ampersand - ( & ), Asterisk - ( * ), Semi-colon - ( ; ),
     * Less-than sign - ( < ), Greater-than sign - ( > ), Apostrophe - ( ' ), Plus sign - ( + ), Colon - ( : ), Question mark - ( ? )
     * NB - within XML and EDIFACT certain characters have additional functions.  Details can be found within the technical specification for the service.
     */
    private static final String CHARACTER_SET_A = "[a-zA-Z0-9 \\.,\\-\\(\\)/=!\\\"%&\\*;<>'\\+:\\?]+";//[a-z] and [A-Z] and [0-9] and [.],[,],[-],[(],[)],[/],[=],[!],["],[%],[&],[*],[;],[<],[>],['],[+],[:],[?];
    private static final String CHARACTER_SET_AA = "[ \\.,\\-\\(\\)/=!\\\"%&\\*;<>'\\+:\\?]+";//[.],[,],[-],[(],[)],[/],[=],[!],["],[%],[&],[*],[;],[<],[>],['],[+],[:],[?];
    /**
     * CHARACTER_SET_B - PAYE reference character set
     * A-Z, a-z, 0-9, ampersand (&), apostrophe ( ' ) opening parenthesis ( ( ), closing parenthesis ( ) ), full stop (.) , asterisk ( *), hyphen ( - ) and oblique ( / )
     * N. B. Space is not a valid character.
     */
    private static final String CHARACTER_SET_B = "[A-Za-z0-9&'\\(\\)\\.\\*\\-/]+";//[A-Z] and [a-z] and  [0-9] and [&],['],[(],[)],[.],[*],[-],[/];
    private static final String CHARACTER_SET_BB = "[&'\\(\\)\\.\\*\\-/]+";//[&],['],[(],[)],[.],[*],[-],[/];
    /**
     * CHARACTER_SET_C - Forename character set
     * A-Z, a-z, apostrophe ( ' ) and hyphen ( - ) N. B. Space is not a valid character.
     * '-
     */
    private static final String CHARACTER_SET_C = "[A-Za-z0-9'\\-]+";//[A-Z] and [a-z] and [0-9] and ['],[-];
    private static final String CHARACTER_SET_CC = "['\\-]+";//['][-];
    private static final String ONLY_CHARACTER_SET = "[a-zA-Z]";//[a-z] and [A-Z];

    private static final String ONLY_NUMBER_SET = "[0-9]+";//[0-9] == [0123456789];

    private static final String DOUBLE_NUMBERS_S = "^(0[1-9]|1[0-3])$";//[01...13];

    /**
     * CHARACTER_SET_D - Surname character set
     * A-Z, a-z, 0-9, comma (,), hyphen (-), apostrophe ('), ampersand (&), oblique (/), opening parenthesis ( ( ), closing parenthesis ( ) ), full stop (.) and space
     * A-Za-z0-9,-'&/().
     */
    private static final String CHARACTER_SET_D = "[A-Za-z0-9,\\-'&/\\(\\). ]+";//[A-Z] and [a-z] and [0-9] and [,],[-],['],[&],[/],[(],[)],[.],[ ];
    private static final String CHARACTER_SET_DD = "[,\\-'&/\\(\\). ]+";//[,],[-],['],[&],[/],[(],[)],[.],[ ];

    private static final String NINO_PREFIX = "AA|AB|AE|AH|AK|AL|AM|AP|AR|AS|AT|AW|AX|AY|AZ|BA|BB|BE|BH|BK|BL|BM|BT|CA|CB|CE|CH|CK|CL|CR|EA|EB|EE|EH|EK|EL|EM|EP|ER|ES|ET|EW|EX|EY|EZ|GY|HA|HB|HE|HH|HK|HL|HM|HP|HR|HS|HT|HW|HX|HY|HZ|JA|JB|JC|JE|JG|JH|JJ|JK|JL|JM|JN|JP|JR|JS|JT|JW|JX|JY|JZ|KA|KB|KE|KH|KK|KL|KM|KP|KR|KS|KT|KW|KX|KY|KZ|LA|LB|LE|LH|LK|LL|LM|LP|LR|LS|LT|LW|LX|LY|LZ|MA|MW|MXNA|NB|NE|NH|NL|NM|NP|NR|NS|NW|NX|NY|NZ|OA|OB|OE|OH|OK|OL|OM|OP|OR|OS|OX|PA|PB|PC|PE|PG|PH|PJ|PK|PL|PM|PN|PP|PR|PS|PT|PW|PX|PY|RA|RB|RE|RH|RK|RM|RP|RR|RS|RT|RW|RX|RY|RZ|SA|SB|SC|SE|SG|SH|SJ|SK|SL|SM|SN|SP|SR|SS|ST|SW|SX|SY|SZ|TA|TB|TE|TH|TK|TL|TM|TP|TR|TS|TT|TW|TX|TY|TZ|WA|WB|WE|WK|WL|WM|WP|YA|YB|YE|YH|YK|YL|YM|YP|YR|YS|YT|YW|YX|YY|YZ|ZA|ZB|ZE|ZH|ZK|ZL|ZM|ZP|ZR|ZS|ZT|ZW|ZX|ZY";
    private static final String NINO_MIDDLE = "^[0-9]{6}$";//Specifies the number character set which is a short form of interpreting /[000000...999999]/.
    private static final String NINO_SUFFIX = "[A-D]";//Specifies the upper case alphabet character set which is a short form of interpreting /[A,B,C,D]/.
    private static final String LOWER_CASE_PREFIX = "[a-z]";//Specifies the lower case alphabet character set which is a short form of interpreting  /[abcdefghijklmnopqrstuvwxyz]/.
    private static final String UPPER_CASE_PREFIX = "[A-Z]";//Specifies the lower case alphabet character set which is a short form of interpreting  /[ABCDEFGHIJKLMNOPQRSTUVWXYZ]/.
    private static final String DOUBLE_THREE_NUMBER = "^(00[1-9]|0[1-9][0-9]|[1-9][0-9][0-9])$";//Specifies the number character set which is a short form of interpreting /[001...999]/.
    private static final String DOUBLE_THREE_PREFIX = "AAA|BBB|CCC|DDD|EEE|FFF|GGG|HHH|III|JJJ|KKK|LLL|MMM|NNN|OOO|PPP|QQQ|RRR|SSS|TTT|UUU|VVV|WWW|XXX|YYY|ZZZ";
    private static final String NINE_NUMBER = "000000000|111111111|222222222|333333333|444444444|555555555|666666666|777777777|888888888|999999999";

    private static final String TAX_CODE_SIFFIX = "T|L|P|V|Y";//-T,L,P,V or Y.
    private static final String TAX_CODE_PREFIX = "^([1-9]|[1-9][0-9]|[1-9][0-9][0-9]|[1-9][0-9][0-9][0-9]|[1-9][0-9][0-9][0-9][0-9]|[1-9][0-9][0-9][0-9][0-9][0-9])$";//--/[1...999999]./
    private static final String TAX_CODE_TWO_ALP = "BR|0T|D0|NT|FT";//0-(zero)
    private static final String ECON_MIDDLE_NUMBER = "^(3[0-9]{6}|3[9]{6})$";//Specifies the number character set which is a short form of interpreting -- /[3000000-3999999]/.
    private static int MODULUS_19_CHECK_DIVISOR = 19;//ECON check divisor
    private static int ECON_FIXED_VALUE = 37;//ECON fixed number
    private static String[] MODULUS_19_TABLE_OF_CHECK_LETTERS = {"A", "B", "C", "D", "E", "F", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "T", "W", "X", "Y"};
    //                                                            0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14,  15,  16,  17,  18

    /**
     * The NINO should be in the format:-
     * - characters 1 & 2 must be alpha and must be one of the issued National Insurance Number prefixes (see appendix 2).
     * - characters 3 - 8 must be numeric.
     * - character 9 must be alpha in the range A - D or a space.
     *
     * @param niNO
     */
    public static boolean isValidNINO(String niNO) throws PayrollValidationException {
        if (niNO == null) {
            throw new PayrollValidationException("NI number is null");
        }
        if (niNO.length() < 9) {//max length == 9
            throw new PayrollValidationException("NI number <" + niNO + "> is too short");
        } else if (niNO.length() > 9) {//min length == 9
            throw new PayrollValidationException("NI number <" + niNO + "> is too long");
        }
        final String prefix = niNO.substring(0, 2);
        if (!prefix.matches(NINO_PREFIX)) {
            throw new PayrollValidationException("NI number <" + niNO + "> has invalid prefix <" + prefix + ">");
        }
        final String middle = niNO.substring(2, 8);
        if (!middle.matches(NINO_MIDDLE)) {
            System.err.println(middle);
            throw new PayrollValidationException("NI number <" + niNO + "> is invalid");
        }
        final String suffix = niNO.substring(8, 9);
        if (!" ".equals(suffix)) {//simple: " " (space)
            if (!suffix.matches(NINO_SUFFIX)) {
                throw new PayrollValidationException("NI number <" + niNO + "> has invalid suffix <" + suffix + ">");
            }
        }
        if (niNO.matches(NINE_NUMBER)) {
            throw new PayrollValidationException("NI number is invalid");
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            System.err.println(isValidECON("E3567891C"));
        } catch (PayrollValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validate is HMRC Office number
     *
     * @param officeNO
     * @return
     * @throws PayrollValidationException
     */
    public static boolean isValidHMRCOffNO(String officeNO) throws PayrollValidationException {
        if (officeNO == null) {
            throw new PayrollValidationException("HMRC office number is null");
        }
        if ("".equals(officeNO) || " ".equals(officeNO)) {//simple: " " (space)
            throw new PayrollValidationException("HMRC office number is blank");
        }
        if (officeNO.length() > 3) {
            throw new PayrollValidationException("HMRC office number <" + officeNO + "> is too long");
        } else if (officeNO.length() < 3) {
            throw new PayrollValidationException("HMRC office number <" + officeNO + "> is too short");
        }
        if (officeNO.matches(DOUBLE_THREE_PREFIX)) {
            throw new PayrollValidationException("HMRC office number <" + officeNO + "> is invalid");
        }
        if (!officeNO.matches(DOUBLE_THREE_NUMBER)) {
            throw new PayrollValidationException("HMRC office number <" + officeNO + "> is invalid");
        }
        if ("000".equals(officeNO)) {
            throw new PayrollValidationException("HMRC office number <" + officeNO + "> is invalid");
        }
        if ("/".equals(officeNO)) {
            throw new PayrollValidationException("HMRC office number <" + officeNO + "> is invalid");
        }
        return true;
    }

    /**
     * Validate is Employers PAYE reference
     *
     * @param referenceNO
     * @return
     * @throws PayrollValidationException
     */
    public static boolean isValidPAYERefNO(String referenceNO) throws PayrollValidationException {
        if (referenceNO == null) {
            throw new PayrollValidationException("PAYE reference number is null");
        } else if ("".equals(referenceNO) || " ".equals(referenceNO)) {//simple: " " (space)
            throw new PayrollValidationException("PAYE reference number is blank");
        } else if (",".equals(referenceNO)) {
            throw new PayrollValidationException("PAYE reference number <" + referenceNO + "> is invalid");
        }
        if (referenceNO.length() > 10) {
            throw new PayrollValidationException("PAYE reference number <" + referenceNO + "> is too long");
        } else if (referenceNO.length() < 1) {
            throw new PayrollValidationException("PAYE reference number <" + referenceNO + "> is too short");
        }
        final String firstNo = referenceNO.substring(0, 1);
        if ("".equals(firstNo) || " ".equals(firstNo)) {//simple: " " (space)
            throw new PayrollValidationException("PAYE reference number <" + referenceNO + "> - 1st character must not be a space");
        }
        if (!referenceNO.matches(CHARACTER_SET_B)) {
            throw new PayrollValidationException("PAYE reference number <" + referenceNO + "> is invalid");
        }
        return true;
    }

    /**
     * Validate is Employers name
     *
     * @param name
     * @return
     * @throws PayrollValidationException
     */
    public static boolean isValidEmployeeName(String name) throws PayrollValidationException {
        if (name == null) {
            throw new PayrollValidationException("Employee name is null");
        } else if ("".equals(name) || " ".equals(name)) {//simple: " " (space)
            throw new PayrollValidationException("Employee name is blank");
        }
        if (name.length() > 35) {//max length == 35
            throw new PayrollValidationException("Employee name <" + name + "> is too long");
        } else if (name.length() < 1) {//min length == 1
            throw new PayrollValidationException("Employee name <" + name + "> is too short");
        }
        final String firstNo = name.substring(0, 1);
        if (" ".equals(firstNo)) {//simple: " " (space)
            throw new PayrollValidationException("Employee name <" + name + "> - 1st character must not be space");
        }
        if (!name.matches(CHARACTER_SET_A)) {
            throw new PayrollValidationException("Employee name <" + name + "> is invalid");
        }
        return true;
    }

    /**
     * Validate is Employees surname
     *
     * @param surname
     * @return
     * @throws PayrollValidationException
     */
    public static boolean isValidEmployeeSurname(String surname) throws PayrollValidationException {
        if (surname == null) {
            throw new PayrollValidationException("Employee surname is null");
        } else if ("".equals(surname) || " ".equals(surname)) {//simple: " " (space)
            throw new PayrollValidationException("Employee surname is blank");
        }
        if (surname.length() > 35) {//max length == 35
            throw new PayrollValidationException("Employee surname <" + surname + "> is too long");
        } else if (surname.length() <= 1) {//min length == 1
            throw new PayrollValidationException("Employee surname <" + surname + "> is too short");
        }
        final String firstNo = surname.substring(0, 1);
        if (" ".equals(firstNo)) {//simple: " " (space)
            throw new PayrollValidationException("Employee surname <" + surname + "> - 1st character must not be space");
        }
        if ("*".equals(surname)) {
            throw new PayrollValidationException("Employee surname <" + surname + "> is not a valid character");
        }
        if (surname.length() == 2) {
            final String secondNo = surname.substring(1, 2);
            if ("0".equals(firstNo) && "'".equals(secondNo)) {
                throw new PayrollValidationException("Employee surname <" + surname + "> not a valid 1st character");
            }
            if (!firstNo.matches(ONLY_CHARACTER_SET) || (firstNo.matches(ONLY_CHARACTER_SET) && "*".equals(secondNo))) {
                throw new PayrollValidationException("Employee surname <" + surname + "> is not a valid character");
            }
        } else if (surname.length() > 2) {
            final String secondNo = surname.substring(1, 2);
            final String thirdNo = surname.substring(2, 3);
            if (firstNo.matches(CHARACTER_SET_DD) || (firstNo.matches(CHARACTER_SET_DD) && secondNo.matches(ONLY_CHARACTER_SET) && "'".equals(thirdNo))) {
                throw new PayrollValidationException("Employee surname <" + surname + "> is not a valid character");
            }
        }
        if (!surname.matches(CHARACTER_SET_D)) {
            throw new PayrollValidationException("Employee surname <" + surname + "> is invalid");
        }
        return true;
    }

    /**
     * Validate is Employees forename or  initial AND 2nd forename
     *
     * @param forenameOrInitial
     * @return
     * @throws PayrollValidationException
     */
    public static boolean isValidEmployeeForeNOrInitial(String forenameOrInitial) throws PayrollValidationException {
        if (forenameOrInitial == null) {
            throw new PayrollValidationException("Employee forename is null");
        } else if ("".equals(forenameOrInitial) || " ".equals(forenameOrInitial)) {//simple: " " (space)
            throw new PayrollValidationException("Employee forename is blank");
        }
        if (forenameOrInitial.length() > 35) {//max length == 35
            throw new PayrollValidationException("Employee forename <" + forenameOrInitial + "> is too long");
        } else if (forenameOrInitial.length() < 1) {//min length == 1
            throw new PayrollValidationException("Employee forename <" + forenameOrInitial + "> is too short");
        }
        final String firstNo = forenameOrInitial.substring(0, 1);
        if (" ".equals(firstNo)) {//simple: " " (space)
            throw new PayrollValidationException("Employee forename <" + forenameOrInitial + "> - 1st character must not be space");
        }
        final String lastNo = forenameOrInitial.substring(forenameOrInitial.length() - 1);
        if ("0".equals(lastNo)) {
            throw new PayrollValidationException("Employee forename <" + forenameOrInitial + "> is invalid");
        }
        if (!forenameOrInitial.matches(CHARACTER_SET_C)) {
            throw new PayrollValidationException("Employee forename <" + forenameOrInitial + "> is invalid");
        }
        return true;
    }
    // NI NO, NI category
    //Date of Birth, Gender, NINO/DOB/Gender, NI category letter, Date of  Starting, Date of  Leaving, Week1/Month 1 indicator - UI
    //
    // P35 front sheet checklist  and declarations - UI
    //

    /**
     * Validate is Final Tax Code
     *
     * @param taxCode
     * @return
     * @throws PayrollValidationException
     */
    public static boolean isValidFinalTaxCode(String taxCode) throws PayrollValidationException {
        if (taxCode == null) {
            throw new PayrollValidationException("Tax Code is null");
        }
        if (taxCode.length() > 7) {//max length == 7
            throw new PayrollValidationException("Tax Code <" + taxCode + "> is too long");
        } else if (taxCode.length() < 2) {//min length == 2
            throw new PayrollValidationException("Tax Code <" + taxCode + "> is too short");
        }
        if (taxCode.length() == 7) {
            final String prefix1 = taxCode.substring(0, 1);//simple: Knnnnnn -> K (only K alpha)
            final String suffix1 = taxCode.substring(1, 7);//simple: Knnnnnn -> nnnnnn
            if ("K".equals(prefix1)) {
                if (!suffix1.matches(TAX_CODE_PREFIX)) {
                    throw new PayrollValidationException("Tax Code <" + taxCode + "> has invalid suffix <" + suffix1 + ">");
                }
            } else {
                final String prefix2 = taxCode.substring(0, 6);//simple: nnnnnnX -> nnnnnn
                final String suffix2 = taxCode.substring(6, 7);//simple: nnnnnnX -> X (X equals T, L, P, V or Y)
                if (!prefix2.matches(TAX_CODE_PREFIX)) {
                    throw new PayrollValidationException("Tax Code <" + taxCode + "> has invalid prefix <" + prefix2 + ">");
                }
                if (!suffix2.matches(TAX_CODE_SIFFIX)) {
                    throw new PayrollValidationException("Tax Code <" + taxCode + "> has invalid suffix <" + suffix2 + ">");
                }
            }
        } else if (taxCode.length() == 2) {
            if (!taxCode.matches(TAX_CODE_TWO_ALP)) {
                throw new PayrollValidationException("Tax Code is invalid");
            }
        } else {
            if (taxCode.contains("M1")) {//simple: M1 month 1
                throw new PayrollValidationException("Tax basis M1(month 1) is not part of tax code: <" + taxCode + ">");
            } else if (taxCode.contains("W1")) {//simple: W1 week 1
                throw new PayrollValidationException("Tax basis W1(week 1) is not part of tax code: <" + taxCode + ">");
            } else if (taxCode.contains("O")) {//simple: O (character)
                throw new PayrollValidationException("O is not a valid character: <" + taxCode + ">");
            } else if (taxCode.contains(" ")) {//simple: " " (space)
                throw new PayrollValidationException("space not allowed: <" + taxCode + ">");
            }
            if (taxCode.length() == 5) {
                final String prefix = taxCode.substring(0, 2);//simple: K0 (only K alpha and 0 (zero))
                if ("K0".equals(prefix)) {
                    throw new PayrollValidationException("Tax Code is invalid");
                }
            }
        }
        return true;
    }

    //Accounts office reference number, Accounts office reference number (Continued), Accounts office reference number (continued) - RegExp

    /**
     * Validate is Accounts office reference number
     *
     * @param referenceNumber
     * @return
     * @throws PayrollValidationException
     */
    public static boolean isValidAOffRefNumber(String referenceNumber) throws PayrollValidationException {
        if (referenceNumber.length() == 13) {// length == 13
            final String firstThreeNumber = referenceNumber.substring(0, 3);
            if (!firstThreeNumber.matches(DOUBLE_THREE_NUMBER)) {
                throw new PayrollValidationException("First 3 characters must be in the range 001 - 999");
            }
            final String fourthCharacter = referenceNumber.substring(3, 4);
            if (!"P".equals(fourthCharacter)) {
                throw new PayrollValidationException("Fourth character must be P");
            }
            final String fifthCharacter = referenceNumber.substring(4, 5);
            if (!fifthCharacter.matches(UPPER_CASE_PREFIX)) {
                throw new PayrollValidationException("Fifth character must be alpha");
            }
            final String lastCharacters1 = referenceNumber.substring(5, 12);
            if (!lastCharacters1.matches(ONLY_NUMBER_SET)) {
                throw new PayrollValidationException("Characters 6  12 must be numeric");
            }
            if ("961".equals(firstThreeNumber)) {
                final String sixthCharacter = referenceNumber.substring(5, 6);
                if (!"0".equals(sixthCharacter)) {
                    throw new PayrollValidationException("Sixth character must be 0 (zero)");
                }
                final String lastCharacter = referenceNumber.substring(referenceNumber.length() - 1);
                if (!"X".equals(lastCharacter)) {
                    throw new PayrollValidationException("Character 13 must be X");
                }
            } else {
                final String lastCharacter = referenceNumber.substring(referenceNumber.length() - 1);
                if ("X".equals(lastCharacter)) {
                    throw new PayrollValidationException("Character 13 must be numeric when first 3 characters are NOT 961");
                }
            }
        } else if (referenceNumber.length() == 17) {// or length == 17
            final String firstThreeNumber = referenceNumber.substring(0, 3);
            if (!firstThreeNumber.matches(DOUBLE_THREE_NUMBER)) {
                throw new PayrollValidationException("First 3 characters must be in the range 001 - 999");
            }
            final String fourthCharacter = referenceNumber.substring(3, 4);
            if (!"P".equals(fourthCharacter)) {
                throw new PayrollValidationException("Fourth character must be P");
            }
            final String fifthCharacter = referenceNumber.substring(4, 5);
            if (!fifthCharacter.matches(UPPER_CASE_PREFIX)) {
                throw new PayrollValidationException("Fifth character must be alpha");
            }
            final String lastChar1 = referenceNumber.substring(13, 15);//characters 14 and 15 must be numeric
            if (!lastChar1.matches(ONLY_NUMBER_SET)) {
                throw new PayrollValidationException("Characters 14  15 must be numeric");
            }
            final String lastChar2 = referenceNumber.substring(15, 17);//characters 16 and 17 must be numeric
            if (!lastChar2.matches(DOUBLE_NUMBERS_S)) {
                throw new PayrollValidationException("Characters 16 and 17 must be in the range 01-13");
            }
            if ("961".equals(firstThreeNumber)) {
                final String sixthCharacter = referenceNumber.substring(5, 6);
                if (!"0".equals(sixthCharacter)) {
                    throw new PayrollValidationException("Sixth character must be 0 (zero)");
                }
            }
        } else {//or reference number == only incorrect character;
            throw new PayrollValidationException("Reference number must be either 13 or 17 characters");
        }
        return true;
    }

    /**
     * Validate is Incentive payment
     *
     * @param incentivePayment
     * @return
     * @throws PayrollValidationException
     */
    public static boolean isValidIncentivePayment(String incentivePayment) throws PayrollValidationException {
        if (incentivePayment == null) {
            throw new PayrollValidationException("Incentive payment is null");
        } else if ("".equals(incentivePayment) || " ".equals(incentivePayment)) {//simple: " " (space)
            throw new PayrollValidationException("Incentive payment is blank, minimum value is 0.00");
        }
        if (incentivePayment.length() < 0) {
            throw new PayrollValidationException("Incentive payment <" + incentivePayment + "> is too short");
        } else if (incentivePayment.length() > 6) {//simple: incentivePayment == 825.00
            throw new PayrollValidationException("Incentive payment <" + incentivePayment + "> is too long");
        }                         //incentivePayment contains { # }, { % }, { & }, { ( }, { - } characters;
        if (incentivePayment.contains("#") || incentivePayment.contains("%") ||
                incentivePayment.contains("&") || incentivePayment.contains("(") || incentivePayment.contains("-")) {
            throw new PayrollValidationException("Incentive payment <" + incentivePayment + "> must be numeric");
        }
        return true;
    }

    /**
     * Validate is Employer Contracted-out number (ECON)
     *
     * @param econ
     * @return
     * @throws PayrollValidationException
     */
    public static boolean isValidECON(String econ) throws PayrollValidationException {
        if (econ == null) {
            throw new PayrollValidationException("ECON is null");
        } else if ("".equals(econ) || " ".equals(econ)) {//simple: " " (space);
            throw new PayrollValidationException("ECON is blank");
        }
        if (econ.length() > 9) {//max length == 9
            throw new PayrollValidationException("ECON <" + econ + "> is too long");
        } else if (econ.length() < 9) {//min length == 9
            throw new PayrollValidationException("ECON <" + econ + "> is too short");
        }
        final String firstChar = econ.substring(0, 1);//1st character must be only -- 'E';
        if (!firstChar.matches(UPPER_CASE_PREFIX) && !("E".equals(firstChar) || "e".equals(firstChar))) {
            throw new PayrollValidationException("Character 1 - <" + firstChar + "> must be alpha - 'E'");
        }
        final String middleChar = econ.substring(1, 8);//2-8 characters must be numeric in the range 3000000 - 3999999;
        if (!middleChar.matches(ECON_MIDDLE_NUMBER)) {
            throw new PayrollValidationException("Characters 2 - 8 - <" + middleChar + "> must be numeric in the range 3000000 - 3999999");
        }
        final String ninthChar = econ.substring(8, 9);//9th character must be only -- alpha;
        if (!ninthChar.matches(ONLY_CHARACTER_SET)) {
            throw new PayrollValidationException("Character 9 - <" + ninthChar + "> must be Alpha");
        }
        /*Modulus 19 check*/
        int total = 0;
        for (int i = 0; i < middleChar.length(); i++) {
            total += Integer.parseInt(middleChar.substring(i, i + 1)) * (8 - i);
        }
        final int remainder = (total + ECON_FIXED_VALUE) % MODULUS_19_CHECK_DIVISOR;
        if (!MODULUS_19_TABLE_OF_CHECK_LETTERS[remainder].equalsIgnoreCase(ninthChar)) {
            throw new PayrollValidationException("Invalid suffix <" + ninthChar + "> for these numeric chars <" + middleChar + ">");
        }
        return true;
    }
}