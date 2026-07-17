package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 11/9/12
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class SolrCourseBookingRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";

    public static final String FIELD_COURSE_BOOKING_ID = "courseBookingId";
    public static final String FIELD_COURSE_BOOKING_NUMBER = "courseBookingNumber";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_CUSTOMER_NAME = "customerName";
    public static final String FIELD_CUSTOMER_ID_NAME = "customerIdName";
    public static final String FIELD_MANAGER_ID = "mangerId";
    public static final String FIELD_MANAGER_NAME = "mangerName";
    public static final String FIELD_MANAGER_ID_NAME = "mangerIdName";
    public static final String FIELD_LOCATION_ID = "locationId";
    public static final String FIELD_LOCATION_NAME = "locationName";
    public static final String FIELD_LOCATION_ID_NAME = "locationIdName";
    public static final String FIELD_STATUS_ID = "statusId";
    public static final String FIELD_STATUS_NAME= "statusName";
    public static final String FIELD_STATUS_ID_NAME= "statusIdName";
    public static final String FIELD_STATUS_CODE = "statusCode";
    public static final String FIELD_TYPE_ID = "typeId";
    public static final String FIELD_TYPE_NAME = "typeName";
    public static final String FIELD_TYPE_ID_NAME = "typeIdName";
    public static final String FIELD_TYPE_CODE = "typeCode";
    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_CREATOR = "creatorName";
    public static final String FIELD_CREATOR_ID = "creatorId";
    public static final String FIELD_CREATOR_ID_NAME= "creatorIdName";
    public static final String FIELD_UPDATER = "updaterName";
    public static final String FIELD_UPDATER_ID = "updaterId";
    public static final String FIELD_COMPOSITE = "composite";

    public static final String SORTABLE_COURSE_BOOKING_NUMBER = "sortableCourseBookingNumber";
    public static final String SORTABLE_CUSTOMER_NAME = "sortableCustomerName";
    public static final String SORTABLE_LOCATION_NAME = "sortableLocationName";
    public static final String SORTABLE_STATUS_NAME = "sortableStatusName";
    public static final String SORTABLE_TYPE_NAME = "sortableTypeName";
}
