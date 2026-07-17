package com.edatasite.workforce.gwt.core.client.ui.crm;

import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid's
 * Date: Aug 15, 2010
 * Time: 8:18:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CrmConstants extends LookUpConstants {
    //Contact Types
    int TYPE_CRM_CONTACT = 1;
    int TYPE_CLIENT_CONTACT = 2;
    int TYPE_SUPPLIER_CONTACT = 3;
    int TYPE_EMPLOYEE_CONTACT = 4;
    int TYPE_LEAD_CONTACT = 5;
    int TYPE_CANDIDATE = 6;
    int TYPE_STUDENT_CONTACT = 7;
    int TYPE_SALE_QUOTE = 25;
    int TYPE_ACCOUNT = 8;

    String SUCCESS = "Success";
    String FAILURE = "Failure";
    String AUTH_FAILED = "Authentication failed";

	String LEAD_SOURCE_OTHER = "_LEAD_SOURCE_OTHER";

    String TREE_LEVEL_1 = "TREE_LEVEL_1";
    String TREE_LEVEL_2 = "TREE_LEVEL_2";
    String TREE_LEVEL_3 = "TREE_LEVEL_3";

    //please do not rename or delete this static fields...
    String ANALYST = "ANALYST";
    String COMPETITOR = "COMPETITOR";
    String CONSIGNOR = "CONSIGNOR";
    String CONSIGNEE = "CONSIGNEE";
    String CUSTOMER = "CUSTOMER";
    String DISTRIBUTOR = "DISTRIBUTOR";
    String INTEGRATOR = "INTEGRATOR";
    String OTHER = "OTHER";
    String PARTNER = "PARTNER";
    String PRESS = "PRESS";
    String PROSPECT = "PROSPECT";
    String RESELLER = "RESELLER";
    String SUPPLIER = "SUPPLIER";
    String VENDOR = "VENDOR";
    String LEAD = "LEAD";
    String COLUMN_GROUPING = "COLUMN_GROUPING";

    public static long KANBAN_ORDER_GAP = 65535L;
}
