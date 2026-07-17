package com.edatasite.workforce.gwt.core.server.utils;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by User on 12/27/2016.
 */
public class CountryCallingCodeLayer {

    public static List<CountryCallCode> getCountryCallCodes() {
        ArrayList<CountryCallCode> result = new ArrayList<>();
        result.add( new CountryCallCode("BD", "880", "Bangladesh"));
        result.add( new CountryCallCode("BE", "32", "Belgium"));
        result.add( new CountryCallCode("BF", "226", "Burkina Faso"));
        result.add( new CountryCallCode("BG", "359", "Bulgaria"));
        result.add( new CountryCallCode("BA", "387", "Bosnia and Herzegovina"));
        result.add( new CountryCallCode("BB", "1-246", "Barbados"));
        result.add( new CountryCallCode("WF", "681", "Wallis and Futuna"));
        result.add( new CountryCallCode("BL", "590", "Saint Barthelemy"));
        result.add( new CountryCallCode("BM", "1-441", "Bermuda"));
        result.add( new CountryCallCode("BN", "673", "Brunei"));
        result.add( new CountryCallCode("BO", "591", "Bolivia"));
        result.add( new CountryCallCode("BH", "973", "Bahrain"));
        result.add( new CountryCallCode("BI", "257", "Burundi"));
        result.add( new CountryCallCode("BJ", "229", "Benin"));
        result.add( new CountryCallCode("BT", "975", "Bhutan"));
        result.add( new CountryCallCode("JM", "1-876", "Jamaica"));
        result.add( new CountryCallCode("BW", "267", "Botswana"));
        result.add( new CountryCallCode("WS", "685", "Samoa"));
        result.add( new CountryCallCode("BQ", "599", "Bonaire, Saint Eustatius and Saba"));
        result.add( new CountryCallCode("BR", "55", "Brazil"));
        result.add( new CountryCallCode("BS", "1-242", "Bahamas"));
        result.add( new CountryCallCode("JE", "44-1534", "Jersey"));
        result.add( new CountryCallCode("BY", "375", "Belarus"));
        result.add( new CountryCallCode("BZ", "501", "Belize"));
        result.add( new CountryCallCode("RU", "7", "Russia"));
        result.add( new CountryCallCode("RW", "250", "Rwanda"));
        result.add( new CountryCallCode("RS", "381", "Serbia"));
        result.add( new CountryCallCode("TL", "670", "East Timor"));
        result.add( new CountryCallCode("RE", "262", "Reunion"));
        result.add( new CountryCallCode("TM", "993", "Turkmenistan"));
        result.add( new CountryCallCode("TJ", "992", "Tajikistan"));
        result.add( new CountryCallCode("RO", "40", "Romania"));
        result.add( new CountryCallCode("TK", "690", "Tokelau"));
        result.add( new CountryCallCode("GW", "245", "Guinea-Bissau"));
        result.add( new CountryCallCode("GU", "1-671", "Guam"));
        result.add( new CountryCallCode("GT", "502", "Guatemala"));
        result.add( new CountryCallCode("GR", "30", "Greece"));
        result.add( new CountryCallCode("GQ", "240", "Equatorial Guinea"));
        result.add( new CountryCallCode("GP", "590", "Guadeloupe"));
        result.add( new CountryCallCode("JP", "81", "Japan"));
        result.add( new CountryCallCode("GY", "592", "Guyana"));
        result.add( new CountryCallCode("GG", "44-1481", "Guernsey"));
        result.add( new CountryCallCode("GF", "594", "French Guiana"));
        result.add( new CountryCallCode("GE", "995", "Georgia"));
        result.add( new CountryCallCode("GD", "1-473", "Grenada"));
        result.add( new CountryCallCode("GB", "44", "United Kingdom"));
        result.add( new CountryCallCode("CSI", "44", "Channel Islands"));
        result.add( new CountryCallCode("GA", "241", "Gabon"));
        result.add( new CountryCallCode("SV", "503", "El Salvador"));
        result.add( new CountryCallCode("GN", "224", "Guinea"));
        result.add( new CountryCallCode("GM", "220", "Gambia"));
        result.add( new CountryCallCode("GL", "299", "Greenland"));
        result.add( new CountryCallCode("GI", "350", "Gibraltar"));
        result.add( new CountryCallCode("GH", "233", "Ghana"));
        result.add( new CountryCallCode("OM", "968", "Oman"));
        result.add( new CountryCallCode("TN", "216", "Tunisia"));
        result.add( new CountryCallCode("JO", "962", "Jordan"));
        result.add( new CountryCallCode("HR", "385", "Croatia"));
        result.add( new CountryCallCode("HT", "509", "Haiti"));
        result.add( new CountryCallCode("HU", "36", "Hungary"));
        result.add( new CountryCallCode("HK", "852", "Hong Kong"));
        result.add( new CountryCallCode("HN", "504", "Honduras"));
        result.add( new CountryCallCode("VE", "58", "Venezuela"));
        result.add( new CountryCallCode("PR", "1-787", "Puerto Rico"));
        result.add( new CountryCallCode("PR", "1-939", "Puerto Rico"));
        result.add( new CountryCallCode("PS", "970", "Palestinian Territory"));
        result.add( new CountryCallCode("PW", "680", "Palau"));
        result.add( new CountryCallCode("PT", "351", "Portugal"));
        result.add( new CountryCallCode("SJ", "47", "Svalbard and Jan Mayen"));
        result.add( new CountryCallCode("PY", "595", "Paraguay"));
        result.add( new CountryCallCode("IQ", "964", "Iraq"));
        result.add( new CountryCallCode("PA", "507", "Panama"));
        result.add( new CountryCallCode("PF", "689", "French Polynesia"));
        result.add( new CountryCallCode("PG", "675", "Papua New Guinea"));
        result.add( new CountryCallCode("PE", "51", "Peru"));
        result.add( new CountryCallCode("PK", "92", "Pakistan"));
        result.add( new CountryCallCode("PH", "63", "Philippines"));
        result.add( new CountryCallCode("PN", "870", "Pitcairn"));
        result.add( new CountryCallCode("PL", "48", "Poland"));
        result.add( new CountryCallCode("PM", "508", "Saint Pierre and Miquelon"));
        result.add( new CountryCallCode("ZM", "260", "Zambia"));
        result.add( new CountryCallCode("EH", "212", "Western Sahara"));
        result.add( new CountryCallCode("EE", "372", "Estonia"));
        result.add( new CountryCallCode("EG", "20", "Egypt"));
        result.add( new CountryCallCode("ZA", "27", "South Africa"));
        result.add( new CountryCallCode("EC", "593", "Ecuador"));
        result.add( new CountryCallCode("IT", "39", "Italy"));
        result.add( new CountryCallCode("VN", "84", "Vietnam"));
        result.add( new CountryCallCode("SB", "677", "Solomon Islands"));
        result.add( new CountryCallCode("ET", "251", "Ethiopia"));
        result.add( new CountryCallCode("SO", "252", "Somalia"));
        result.add( new CountryCallCode("ZW", "263", "Zimbabwe"));
        result.add( new CountryCallCode("SA", "966", "Saudi Arabia"));
        result.add( new CountryCallCode("ES", "34", "Spain"));
        result.add( new CountryCallCode("ER", "291", "Eritrea"));
        result.add( new CountryCallCode("ME", "382", "Montenegro"));
        result.add( new CountryCallCode("MD", "373", "Moldova"));
        result.add( new CountryCallCode("MG", "261", "Madagascar"));
        result.add( new CountryCallCode("MF", "590", "Saint Martin"));
        result.add( new CountryCallCode("MA", "212", "Morocco"));
        result.add( new CountryCallCode("MC", "377", "Monaco"));
        result.add( new CountryCallCode("UZ", "998", "Uzbekistan"));
        result.add( new CountryCallCode("MM", "95", "Myanmar"));
        result.add( new CountryCallCode("BUM", "95", "Burma"));
        result.add( new CountryCallCode("ML", "223", "Mali"));
        result.add( new CountryCallCode("MO", "853", "Macao"));
        result.add( new CountryCallCode("MN", "976", "Mongolia"));
        result.add( new CountryCallCode("MH", "692", "Marshall Islands"));
        result.add( new CountryCallCode("MK", "389", "Macedonia"));
        result.add( new CountryCallCode("MU", "230", "Mauritius"));
        result.add( new CountryCallCode("MT", "356", "Malta"));
        result.add( new CountryCallCode("MW", "265", "Malawi"));
        result.add( new CountryCallCode("MV", "960", "Maldives"));
        result.add( new CountryCallCode("MQ", "596", "Martinique"));
        result.add( new CountryCallCode("MP", "1-670", "Northern Mariana Islands"));
        result.add( new CountryCallCode("MS", "1-664", "Montserrat"));
        result.add( new CountryCallCode("MR", "222", "Mauritania"));
        result.add( new CountryCallCode("IOM", "44-1624", "Isle of Man"));
        result.add( new CountryCallCode("UG", "256", "Uganda"));
        result.add( new CountryCallCode("TZ", "255", "Tanzania"));
        result.add( new CountryCallCode("MY", "60", "Malaysia"));
        result.add( new CountryCallCode("MX", "52", "Mexico"));
        result.add( new CountryCallCode("IL", "972", "Israel"));
        result.add( new CountryCallCode("FR", "33", "France"));
        result.add( new CountryCallCode("IO", "246", "British Indian Ocean Territory"));
        result.add( new CountryCallCode("SH", "290", "Saint Helena"));
        result.add( new CountryCallCode("FI", "358", "Finland"));
        result.add( new CountryCallCode("FJ", "679", "Fiji"));
        result.add( new CountryCallCode("FK", "500", "Falkland Islands"));
        result.add( new CountryCallCode("FM", "691", "Micronesia"));
        result.add( new CountryCallCode("FO", "298", "Faroe Islands"));
        result.add( new CountryCallCode("NI", "505", "Nicaragua"));
        result.add( new CountryCallCode("NL", "31", "Netherlands"));
        result.add( new CountryCallCode("NO", "47", "Norway"));
        result.add( new CountryCallCode("BV", "47", "Bouvet Island"));
        result.add( new CountryCallCode("NA", "264", "Namibia"));
        result.add( new CountryCallCode("VU", "678", "Vanuatu"));
        result.add( new CountryCallCode("NC", "687", "New Caledonia"));
        result.add( new CountryCallCode("NE", "227", "Niger"));
        result.add( new CountryCallCode("NF", "672", "Norfolk Island"));
        result.add( new CountryCallCode("NG", "234", "Nigeria"));
        result.add( new CountryCallCode("NZ", "64", "New Zealand"));
        result.add( new CountryCallCode("NP", "977", "Nepal"));
        result.add( new CountryCallCode("NR", "674", "Nauru"));
        result.add( new CountryCallCode("NU", "683", "Niue"));
        result.add( new CountryCallCode("CK", "682", "Cook Islands"));
        result.add( new CountryCallCode("IVO", "225", "Ivory Coast"));
        result.add( new CountryCallCode("CH", "41", "Switzerland"));
        result.add( new CountryCallCode("CO", "57", "Colombia"));
        result.add( new CountryCallCode("CN", "86", "China"));
        result.add( new CountryCallCode("CM", "237", "Cameroon"));
        result.add( new CountryCallCode("CL", "56", "Chile"));
        result.add( new CountryCallCode("CC", "61", "Cocos Islands"));
        result.add( new CountryCallCode("CA", "1", "Canada"));
        result.add( new CountryCallCode("CG", "242", "Republic of the Congo"));
        result.add( new CountryCallCode("CF", "236", "Central African Republic"));
        result.add( new CountryCallCode("CD", "243", "Democratic Republic of the Congo"));
        result.add( new CountryCallCode("CZ", "420", "Czech Republic"));
        result.add( new CountryCallCode("CY", "357", "Cyprus"));
        result.add( new CountryCallCode("CX", "61", "Christmas Island"));
        result.add( new CountryCallCode("CR", "506", "Costa Rica"));
        result.add( new CountryCallCode("CUR", "599", "Curacao"));
        result.add( new CountryCallCode("CV", "238", "Cape Verde"));
        result.add( new CountryCallCode("CUB", "53", "Cuba"));
        result.add( new CountryCallCode("SZ", "268", "Swaziland"));
        result.add( new CountryCallCode("SY", "963", "Syria"));
        result.add( new CountryCallCode("SX", "599", "Sint Maarten"));
        result.add( new CountryCallCode("KG", "996", "Kyrgyzstan"));
        result.add( new CountryCallCode("KE", "254", "Kenya"));
        result.add( new CountryCallCode("SS", "211", "South Sudan"));
        result.add( new CountryCallCode("SR", "597", "Suriname"));
        result.add( new CountryCallCode("KI", "686", "Kiribati"));
        result.add( new CountryCallCode("CBD", "855", "Cambodia"));
        result.add( new CountryCallCode("KN", "1-869", "Saint Kitts and Nevis"));
        result.add( new CountryCallCode("KM", "269", "Comoros"));
        result.add( new CountryCallCode("ST", "239", "Sao Tome and Principe"));
        result.add( new CountryCallCode("SK", "421", "Slovakia"));
        result.add( new CountryCallCode("KR", "82", "South Korea"));
        result.add( new CountryCallCode("SI", "386", "Slovenia"));
        result.add( new CountryCallCode("KP", "850", "North Korea"));
        result.add( new CountryCallCode("KW", "965", "Kuwait"));
        result.add( new CountryCallCode("SN", "221", "Senegal"));
        result.add( new CountryCallCode("SM", "378", "San Marino"));
        result.add( new CountryCallCode("SL", "232", "Sierra Leone"));
        result.add( new CountryCallCode("SC", "248", "Seychelles"));
        result.add( new CountryCallCode("KZ", "7", "Kazakhstan"));
        result.add( new CountryCallCode("KY", "1-345", "Cayman Islands"));
        result.add( new CountryCallCode("SG", "65", "Singapore"));
        result.add( new CountryCallCode("SE", "46", "Sweden"));
        result.add( new CountryCallCode("SD", "249", "Sudan"));
        result.add( new CountryCallCode("DO", "1-809", "Dominican Republic"));
        result.add( new CountryCallCode("DO", "1-829", "Dominican Republic"));
        result.add( new CountryCallCode("DM", "1-767", "Dominica"));
        result.add( new CountryCallCode("DJ", "253", "Djibouti"));
        result.add( new CountryCallCode("DK", "45", "Denmark"));
        result.add( new CountryCallCode("BVI", "1-284", "British Virgin Islands"));
        result.add( new CountryCallCode("DE", "49", "Germany"));
        result.add( new CountryCallCode("YE", "967", "Yemen"));
        result.add( new CountryCallCode("DZ", "213", "Algeria"));
        result.add( new CountryCallCode("US", "1", "United States"));
        result.add( new CountryCallCode("UY", "598", "Uruguay"));
        result.add( new CountryCallCode("YT", "262", "Mayotte"));
        result.add( new CountryCallCode("UM", "1", "United States Minor Outlying Islands"));
        result.add( new CountryCallCode("LB", "961", "Lebanon"));
        result.add( new CountryCallCode("LC", "1-758", "Saint Lucia"));
        result.add( new CountryCallCode("LA", "856", "Laos"));
        result.add( new CountryCallCode("TV", "688", "Tuvalu"));
        result.add( new CountryCallCode("TW", "886", "Taiwan"));
        result.add( new CountryCallCode("TT", "1-868", "Trinidad and Tobago"));
        result.add( new CountryCallCode("TR", "90", "Turkey"));
        result.add( new CountryCallCode("LK", "94", "Sri Lanka"));
        result.add( new CountryCallCode("LI", "423", "Liechtenstein"));
        result.add( new CountryCallCode("LV", "371", "Latvia"));
        result.add( new CountryCallCode("TO", "676", "Tonga"));
        result.add( new CountryCallCode("LT", "370", "Lithuania"));
        result.add( new CountryCallCode("LU", "352", "Luxembourg"));
        result.add( new CountryCallCode("LR", "231", "Liberia"));
        result.add( new CountryCallCode("LS", "266", "Lesotho"));
        result.add( new CountryCallCode("TH", "66", "Thailand"));
        result.add( new CountryCallCode("TG", "228", "Togo"));
        result.add( new CountryCallCode("TD", "235", "Chad"));
        result.add( new CountryCallCode("TC", "1-649", "Turks and Caicos Islands"));
        result.add( new CountryCallCode("LY", "218", "Libya"));
        result.add( new CountryCallCode("VA", "379", "Vatican"));
        result.add( new CountryCallCode("VC", "1-784", "Saint Vincent and the Grenadines"));
        result.add( new CountryCallCode("AE", "971", "United Arab Emirates"));
        result.add( new CountryCallCode("AD", "376", "Andorra"));
        result.add( new CountryCallCode("AG", "1-268", "Antigua and Barbuda"));
        result.add( new CountryCallCode("AF", "93", "Afghanistan"));
        result.add( new CountryCallCode("AI", "1-264", "Anguilla"));
        result.add( new CountryCallCode("VI", "1-340", "U.S. Virgin Islands"));
        result.add( new CountryCallCode("IS", "354", "Iceland"));
        result.add( new CountryCallCode("IRA", "98", "Iran"));
        result.add( new CountryCallCode("AM", "374", "Armenia"));
        result.add( new CountryCallCode("AL", "355", "Albania"));
        result.add( new CountryCallCode("AO", "244", "Angola"));
        result.add( new CountryCallCode("AS", "1-684", "American Samoa"));
        result.add( new CountryCallCode("AS", "684", "American Samoa"));
        result.add( new CountryCallCode("AR", "54", "Argentina"));
        result.add( new CountryCallCode("AU", "61", "Australia"));
        result.add( new CountryCallCode("AT", "43", "Austria"));
        result.add( new CountryCallCode("AW", "297", "Aruba"));
        result.add( new CountryCallCode("IN", "91", "India"));
        result.add( new CountryCallCode("IE", "353", "Ireland"));
        result.add( new CountryCallCode("ID", "62", "Indonesia"));
        result.add( new CountryCallCode("AZ", "994", "Azerbaijan"));
        result.add( new CountryCallCode("QA", "974", "Qatar"));
        result.add(new CountryCallCode("BOP", "27", "Bophuthatswana"));
        result.add(new CountryCallCode("CI", "225", "Cote d'Ivoire"));
        result.add(new CountryCallCode("KTO", "676", "Kingdom of Tonga"));
        result.add(new CountryCallCode("TF", "262", "French Southern Territories"));
        result.add(new CountryCallCode("LC", "1-758", "Saint Lucia"));
        result.sort(Comparator.comparing(CountryCallCode::getName));
        return result;
    }

    public static String getCountryCodeByCallCode(String callCode) {
        if (callCode != null && !callCode.isEmpty()) {
            List<CountryCallCode> codes = getCountryCallCodes();
            for (CountryCallCode code : codes) {
                if (callCode.equals(code.getCallCode())) {
                    return code.getCountryCode();
                }
            }
        }
        return null;
    }

    public static String getCallCodeByCountryCode(String countryCode) {
        if (countryCode != null && !countryCode.isEmpty()) {
            List<CountryCallCode> codes = getCountryCallCodes();
            for (CountryCallCode code : codes) {
                if (countryCode.equals(code.getCountryCode())) {
                    return code.getCallCode();
                }
            }
        }
        return null;
    }




    public static class CountryCallCode implements IsSerializable {
        private String countryCode;
        private String callCode;
        private String name;
        public CountryCallCode() {

        }

        public CountryCallCode(String countryCode, String callCode, String name) {
            this.countryCode = countryCode;
            this.callCode = callCode;
            this.name = name;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCallCode() {
            return callCode;
        }

        public void setCallCode(String callCode) {
            this.callCode = callCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "+" + callCode + " (" + name + ")";
        }
    }

}
