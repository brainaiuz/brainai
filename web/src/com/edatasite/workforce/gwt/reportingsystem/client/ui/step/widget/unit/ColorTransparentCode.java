package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.Utils;

import java.util.HashMap;

public class ColorTransparentCode {
    public static HashMap<Integer, String> transparnentCodes;

    static {
        transparnentCodes = new HashMap<>();
        transparnentCodes.put(1, "03");
        transparnentCodes.put(2, "04");
        transparnentCodes.put(3, "08");
        transparnentCodes.put(4, "0A");
        transparnentCodes.put(5, "0D");
        transparnentCodes.put(6, "0F");
        transparnentCodes.put(7, "12");
        transparnentCodes.put(8, "14");
        transparnentCodes.put(9, "17");
        transparnentCodes.put(10, "1A");
        transparnentCodes.put(11, "1C");
        transparnentCodes.put(12, "1F");
        transparnentCodes.put(13, "21");
        transparnentCodes.put(14, "24");
        transparnentCodes.put(15, "26");
        transparnentCodes.put(16, "29");
        transparnentCodes.put(17, "2B");
        transparnentCodes.put(18, "2E");
        transparnentCodes.put(19, "30");
        transparnentCodes.put(20, "33");
        transparnentCodes.put(21, "36");
        transparnentCodes.put(22, "38");
        transparnentCodes.put(23, "3B");
        transparnentCodes.put(24, "3D");
        transparnentCodes.put(25, "40");
        transparnentCodes.put(26, "42");
        transparnentCodes.put(27, "45");
        transparnentCodes.put(28, "47");
        transparnentCodes.put(29, "4A");
        transparnentCodes.put(30, "4D");
        transparnentCodes.put(31, "4F");
        transparnentCodes.put(32, "52");
        transparnentCodes.put(33, "54");
        transparnentCodes.put(34, "57");
        transparnentCodes.put(35, "59");
        transparnentCodes.put(36, "5C");
        transparnentCodes.put(37, "5E");
        transparnentCodes.put(38, "61");
        transparnentCodes.put(39, "63");
        transparnentCodes.put(40, "66");
        transparnentCodes.put(41, "69");
        transparnentCodes.put(42, "6B");
        transparnentCodes.put(43, "6E");
        transparnentCodes.put(44, "70");
        transparnentCodes.put(45, "73");
        transparnentCodes.put(46, "75");
        transparnentCodes.put(47, "78");
        transparnentCodes.put(48, "7A");
        transparnentCodes.put(49, "7D");
        transparnentCodes.put(50, "80");
        transparnentCodes.put(51, "82");
        transparnentCodes.put(52, "85");
        transparnentCodes.put(53, "87");
        transparnentCodes.put(54, "8A");
        transparnentCodes.put(55, "8C");
        transparnentCodes.put(56, "8F");
        transparnentCodes.put(57, "91");
        transparnentCodes.put(58, "94");
        transparnentCodes.put(59, "96");
        transparnentCodes.put(60, "99");
        transparnentCodes.put(61, "9C");
        transparnentCodes.put(62, "9E");
        transparnentCodes.put(63, "A1");
        transparnentCodes.put(64, "A3");
        transparnentCodes.put(65, "A6");
        transparnentCodes.put(66, "A8");
        transparnentCodes.put(67, "AB");
        transparnentCodes.put(68, "AD");
        transparnentCodes.put(69, "B0");
        transparnentCodes.put(70, "B3");
        transparnentCodes.put(71, "B5");
        transparnentCodes.put(72, "B8");
        transparnentCodes.put(73, "BA");
        transparnentCodes.put(74, "BD");
        transparnentCodes.put(75, "BF");
        transparnentCodes.put(76, "C2");
        transparnentCodes.put(77, "C4");
        transparnentCodes.put(78, "C7");
        transparnentCodes.put(79, "C9");
        transparnentCodes.put(80, "CC");
        transparnentCodes.put(81, "CF");
        transparnentCodes.put(82, "D1");
        transparnentCodes.put(83, "D4");
        transparnentCodes.put(84, "D6");
        transparnentCodes.put(85, "D9");
        transparnentCodes.put(86, "DB");
        transparnentCodes.put(87, "DE");
        transparnentCodes.put(88, "E0");
        transparnentCodes.put(89, "E3");
        transparnentCodes.put(90, "E6");
        transparnentCodes.put(91, "E8");
        transparnentCodes.put(92, "EB");
        transparnentCodes.put(93, "ED");
        transparnentCodes.put(94, "F0");
        transparnentCodes.put(95, "F2");
        transparnentCodes.put(96, "F5");
        transparnentCodes.put(97, "F7");
        transparnentCodes.put(98, "FA");
        transparnentCodes.put(99, "FC");
        transparnentCodes.put(100, "FF");
    }

    public static String convertColorHexCodeWithTransparent(Integer percent, String color) {
        if (Utils.isNullOrEmpty(color)) {
            return color;
        }
        return color + transparnentCodes.get(percent);
    }
}
