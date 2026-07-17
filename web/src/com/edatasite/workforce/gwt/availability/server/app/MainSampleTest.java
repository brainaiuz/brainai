package com.edatasite.workforce.gwt.availability.server.app;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Hurshid on 1/30/2019
 */
public class MainSampleTest {

    public static void main(String[] args) {
        double d = 122.18943212;
        NumberFormat formatter = new DecimalFormat("#0.00");
        System.out.println(formatter.format(d));
    }
}
