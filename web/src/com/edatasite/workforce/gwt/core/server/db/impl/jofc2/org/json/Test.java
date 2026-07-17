package com.edatasite.workforce.gwt.core.server.db.impl.jofc2.org.json;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 16.12.2009
 * Time: 15:14:45
 * To change this template use File | Settings | File Templates.
 */

import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.myaccount.client.PricingUtils;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanPrice;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CountriesListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactAddressAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.AddContactTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.AddZapierContactTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Test class. This file is not formally a member of the org.json library.
 * It is just a casual test tool.
 */
@SuppressWarnings("unchecked")
public class Test {

    public static void main(String args[]) {
        AddZapierContactTO addZapierContactTO = new AddZapierContactTO();
        addZapierContactTO.setId(0);
        addZapierContactTO.setCompany_name("companyname");
        addZapierContactTO.setPhone_number("phonenumber");
        addZapierContactTO.setEmail("email");
        addZapierContactTO.setFirst_name("firstname");
        addZapierContactTO.setLast_name("lastname");
        addZapierContactTO.setNote("note");
        try {
            System.out.println(new ObjectMapper().writeValueAsString(addZapierContactTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
