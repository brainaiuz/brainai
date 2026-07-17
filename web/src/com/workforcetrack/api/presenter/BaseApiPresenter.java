package com.workforcetrack.api.presenter;

import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 04/09/12
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
public class BaseApiPresenter {


    public static final String TOTAL_COUNT = "totalCount";
    public static final String ITEMS = "items";

    /*Project*/
    public static final String NUMBER = "number";
    public static final String CLIENT = "client";
    public static final String CLIENT_List = "clientList";
    public static final String CLIENT_ID = "clientId";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String LAST_MODIFIED = "lastModified";
    public static final String DUE_DATE = "dueDate";
    public static final String ACTUAL_HOURS_SPENT = "hoursSpent";
    public static final String CANCELLED_TASKS = "cancelledTasks";
    public static final String CLOSED_TASKS = "closedTasks";
    public static final String MEMBERS_INVOLVED = "membersInvolved";
    public static final String IN_PROGRESS_TASKS = "inProgressTasks";
    public static final String NOT_STARTED_TASKS = "notStartedTasks";
    public static final String COMPLETED_TASKS = "completedTasks";
    public static final String DEFAULT_PROJECTID = "defaultProjectId";
    public static final String ACTUAL_START_DATE = "actualStartDate";
    public static final String ACTUAL_END_DATE = "actualEndDate";
    public static final String CREATOR = "creator";
    public static final String CREATOR_ID = "creatorId";
    public static final String CREATION_DATE = "creationDate";
    public static final String LAST_UPDATER_NAME = "lastUpdaterName";
    public static final String LAST_UPDATE_TIME = "lastUpdateTime";
    public static final String LOCATION = "location";
    public static final String CUSTOM_FIELDS = "customFields";
    public static final String PERMISSIONS = "permissions";
    public static final String STATUS_NAME = "statusName";
    public static final String STATUS_LIST = "statusList";
    public static final String COMPLETE = "complete";
    public static final String PERCENT = "percent";
    public static final String PERMISSION = "permission";
    public static final String MANAGER_ID = "managerId";
    public static final String MANAGER_NAME = "managerName";
    public static final String BACKUP_MANAGER_ID = "backupManagerId";
    public static final String BACKUP_MANAGER_IDS = "backupManagerIDs";
    public static final String BACKUP_MANAGER_NAME = "backupManagerName";
    public static final String PROJECT_MEMBERS = "projectMembers";

    public static final String LOCATION_ID = "locationId";
    public static final String LOCATION_LIST = "locationList";
    public static final String IS_COPY_NEW_EMPLOYEES_TO_PROJECT_TASKS = "isCopyNewEmployeesToProjectTasks";
    public static final String COPY_NEW_EMPLOYEES_TO_PROJECT_TASKS_FIELD = "copyNewEmployeesToProjectTasksField";
    public static final String MEMBERS = "members";
    public static final String CHANGE_TASK_STATUS = "changeTaskStatus";
    public static final String ADDRESS = "address";
    public static final String ADDRESSB = "addressb";
    public static final String COMPANY_LIST = "companyList";

    /*END*/

    /*HistoryListItem*/
    public static final String OBJECT_ID = "objectID";
    public static final String ID = "id";
    public static final String SELECTED = "selected";
    public static final String COMMENT = "comment";
    public static final String SUBJECT = "subject";
    public static final String NOTES_COMMENTS = "notesComments";
    public static final String CHECKED = "checked";
    public static final String EMPLOYEE_PICTURE = "employeePicture";
    public static final String EVENT_DATE = "eventDate";
    public static final String EVENT_DESCRIPTION = "eventDescription";
    public static final String COMMENT_ID = "commentId";
    public static final String USER_NAME = "username";
    public static final String DATE = "date";
    public static final String EMPLOYEE_IMAGE_URL = "employeeImageUrl";
    public static final String EMPLOYEE = "employee";
    public static final String VISIBILITY = "visibility";
    public static final String EDITABLE = "editable";
    public static final String RELATED_ID = "relatedId";
    public static final String RELATED_NAME = "relatedName";
    public static final String RELATED_TO_ID = "relatedToId";
    public static final String RELATED_TO_NAME = "relatedToName";
    public static final String RELATED_TO_LINK = "relatedToLink";
    public static final String PROJECT_EMPLOYEES = "projectEmployees";
    public static final String ENTITY_ID = "entityId";
    public static final String SECTION_LINK = "sectionLink";
    /*END*/

    /*FileResource*/
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CONTENT_TYPE = "contentType";
    public static final String FILE_SIZE = "fileSize";
    public static final String URL = "url";
    /*END*/

    /*User Info*/
    public static final String USER_ID = "userId";
    public static final String COMPANY_ID = "companyId";
    public static final String SESSION_ID = "sessionId";
    public static final String COMPANY_NAME = "companyName";
    public static final String COMPANY_DESCRIPTION = "companyDescription";
    public static final String LOGO = "logo";
    public static final String ROLE_ITEMS = "roleItems";
    public static final String ACTIVE = "active";
    public static final String IMAGE_URL = "imageUrl";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String MIRDDLE_NAME = "middleName";

    /*Employee*/
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    public static final String FROM_SDATE = "fromSDate";
    public static final String TO_SDATE = "toSDate";
    public static final String LEAVE_TYPE = "leaveType";
    public static final String IS_LINKABLE = "isLinkable";
    public static final String MIDDLE_NAME = "middleName";
    public static final String ROLE = "role";
    public static final String DEPARTMENT = "department";
    public static final String STATUS_CODE = "statusCode";
    public static final String DEFAULT_TEAM = "defaultTeam";
    public static final String TIME_SLOT = "timeSlot";
    public static final String HOME_ADDRESS = "homeAddress";
    public static final String REGION = "region";
    public static final String HOME_PHONE = "homePhone";
    public static final String WORK_PHONE = "workPhone";
    public static final String ANNUAL_ALLOWANCE = "annualAllowance";
    public static final String TRAINING_NEEDS = "trainingNeeds";
    public static final String GRADE = "grade";
    public static final String GENDER = "gender";
    public static final String BIRTH_DATE = "birthDate";
    public static final String MARTIAL_STATUS = "martialStatus";
    public static final String MARTIAL_STATUS_ID = "martialStatusId";
    public static final String MARTIAL_STATUS_LIST = "martialStatusList";
    public static final String SPOKEN_LANGUAGES = "spokenLanguages";
    public static final String HOME_ADDRESSES = "homeAddresses";
    public static final String WORK_ADDRESSES = "workAddresses";
    public static final String HOME_FAX = "homeFax";
    public static final String WORK_FAX = "workFax";
    public static final String OTHER_PHONE = "otherPhone";
    public static final String OTHER_ADDRESSES = "otherAddresses";
    public static final String WORK_EMAIL = "workEmail";
    public static final String HOME_EMAIL = "homeEmail";
    public static final String WAGE_RATE = "wageRate";
    public static final String CLIENT_CHARGE_RATE = "clientChargeRate";
    public static final String ROLE_LIST = "roleList";
    public static final String HOME_WEBSITE = "homeWebSite";
    public static final String WORK_WEBSITE = "workWebSite";
    public static final String HOME_PAGE = "contacthomePage";
    public static final String FTP = "ftp";
    public static final String BLOG = "blog";
    public static final String PROFILE_WEBSITE = "profileWebSite";
    public static final String OTHER_WEBSITE = "otherWebSite";
    public static final String GTALK = "gtalk";
    public static final String AIM = "aim";

    /*END*/

    /*Task*/
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String PRIORITY_NAME = "priorityName";
    public static final String PROJECT_ID = "projectId";
    public static final String PROJECT_NAME = "projectName";
    public static final String ASSIGNED_TO = "assignedTo";
    public static final String NEW_TASK = "newTask";
    public static final String HIGH_LITE = "highlite";
    public static final String PRIORITY_ID = "priorityId";
    public static final String ESTIMATED = "estimated";
    public static final String BILLABLE = "billable";
    public static final String GOOGLE_ID = "googleId";
    public static final String PARENT_WORKSTREAM_NAME = "parentWorkstreamName";
    public static final String ALL_DAY = "allDay";
    public static final String TASK_CREATOR_ID = "taskCreatorId";
    public static final String QUICKBOOK_TASK_ID = "quickbookTaskID";
    public static final String QUICKBOOK_EDIT_SEQUENCE = "quickbookEditSequence";
    public static final String NUMBER_DATA = "numberData";
    public static final String ASSIGNEES = "assignees";
    public static final String MY_SELF = "mySelf";
    public static final String PARENT_WS_ITEM = "parentWSItem";
    public static final String IS_EMPLOYEE_TASK = "isEmployeeTask";
    public static final String EMPLOYEE_TASK_ID = "employeeTaskID";
    public static final String EMPLOYEE_ID = "employeeID";
    public static final String FOLDER_ID = "folderId";
    public static final String PROJECTS = "projects";
    /*Project*/

    public static final String NEW_PROJECT = "newProject";
    public static final String LAST_UPDATE = "lastUpdate";
    public static final String INVOICE_NUMBER = "invoiceNumber";
    public static final String HEAD_COUNT = "headCount";
    public static final String CRM_PROJECT_ID = "crmProjectId";
    public static final String DIFFERENCE = "difference";
    public static final String PROFIT = "profit";
    public static final String PLANED_PROFIT = "planedProfit";
    public static final String COST = "cost";
    public static final String INCOME = "income";
    public static final String PLANED_INCOME = "planedIncome";
    public static final String ESTIMATED_TIME = "estimatedTime";

    public static final String TEAMS = "teams";
    public static final String TASK_COUNT = "taskCount";
    public static final String STATUS_ID = "statusId";
    public static final String PROJECT_LOCATION = "projectLocation";
    public static final String PROJECT_LOCATION_ID = "projectLocationId";
    public static final String PROJECT_CREATOR_ID = "projectCreatorId";


    /*Client*/
    public static final String OWNER_NAME = "ownerName";
    public static final String OWNER_ID = "ownerID";
    public static final String ACCOUNT_TYPES = "accountTypes";
    public static final String OWNERSHIP_ID = "ownershipID";
    public static final String OWNERSHIP_CODE = "ownershipCode";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String FAX = "fax";
    public static final String WEBSITE = "website";
    public static final String RATING_ID = "ratingID";
    public static final String RATING = "rating";
    public static final String BILL_ADDRESSES = "billAddresses";
    public static final String MAIL_ADDRESSES = "mailAddresses";
    public static final String COUNTRY = "country";
    public static final String COUNTRY_ID = "countryId";
    public static final String STATE = "state";
    public static final String STATE_ID = "stateId";
    public static final String NOTE = "note";
    public static final String TITLE = "title";
    public static final String ORGANIZATION_TYPE_ID = "organizationTypeID";
    public static final String ORGANIZATION_TYPE = "organizationType";
    public static final String ORGANIZATION_TYPE_CODE = "organizationTypeCode";
    public static final String OTHER_ORGANIZATION_TYPE = "otherOrganizationType";
    public static final String INDUSTRY_ID = "industryID";
    public static final String INDUSTRY = "industry";
    public static final String INDUSTRY_CODE = "industryCode";
    public static final String OTHER_INDUSTRY = "otherIndustry";
    public static final String ANNUAL_REVENUE = "annualRevenue";
    public static final String ANNUAL_REVENUES = "annualRevenues";
    public static final String ANNUAL_REVENUE_ID = "annualRevenueID";
    public static final String ANNUAL_REVENUE_CODE = "annualRevenueCode";
    public static final String NUMBER_OF_EMPLOYEE = "numberOfEmployee";
    public static final String NUMBER_OF_EMPLOYEE_ID = "numberOfEmployeeID";
    public static final String NUMBER_OF_EMPLOYEE_CODE = "numberOfEmployeeCode";
    public static final String CURRENCY = "currency";
    public static final String CURRENCY_ID = "currencyID";
    public static final String PAYMENT_METHOD = "paymentMethod";
    public static final String PAYMENT_METHODS = "paymentMethods";
    public static final String PAYMENT_METHOD_ID = "paymentMethodID";
    public static final String VAT_NUMBER = "vatNumber";
    public static final String PRIMARY_CONTACT = "primaryContact";
    public static final String CONTACTS = "contacts";
    public static final String CREATED_DATE = "createdDate";
    public static final String LAST_UPDATE_DATE = "lastUpdatedDate";
    public static final String CONTACT_NAME = "contactName";
    public static final String MOBILE_PHONE = "mobilePhone";
    public static final String STREET = "street";
    public static final String CITY = "city";
    public static final String POST_CODE = "postCode";
    public static final String POSITION = "position";
    public static final String EMPLOYEE_CODE = "employeeCode";
    public static final String EMPLOYMENT_MODE = "employmentMode";
    public static final String EMPLOYMENT_MODEID = "employmentModeId";
    public static final String HIRE_DATE = "hireDate";
    public static final String TERMS_OF_CONTRACT = "termsOfContract";
    public static final String DEPARTMENT_ID = "departmentId";
    public static final String DEPARTMENT_LIST = "departmentList";
    public static final String POSITION_ID = "positionId";
    public static final String POSITION_LIST = "positionList";
    public static final String STATUS = "status";
    public static final String ROLE_ID = "roleId";

    //CONTACT FIELDS
    public static final String PRIMARY_EMAIL = "lastName";
    public static final String PRIMARY_PHONE = "lastName";
    public static final String OWNER_ITEMS = "ownerItems";
    public static final String NAME_ID = "nameId";
    public static final String NUMBER_ID = "numberId";
    public static final String OWNERSHIPS = "ownerships";
    public static final String OWNERSHIP = "ownership";
    public static final String EMAIL_ID = "emailId";
    public static final String PHONE_ID = "phoneId";
    public static final String FAX_ID = "faxId";
    public static final String WEBSITE_ID = "websiteId";
    public static final String RATINGS = "ratings";
    public static final String COUNTRYS = "countrys";
    public static final String STATES = "states";
    public static final String HISTORY = "history";
    public static final String ALL_HISTORY = "allHistory";
    public static final String IMPORT_FILE_ID = "importFileId";
    public static final String TITLE_ID = "titleId";
    public static final String TITLE_LIST = "titleList";

    public static final String ORGANIZATION_TYPES = "organizationTypes";
    public static final String INDUSTRIES = "industries";
    public static final String NUMBER_OF_EMPLOYEES = "numberOfEmployees";
    public static final String CURRENCIES = "currencies";
    public static final String VAT_NUMBER_ID = "vatNumberId";
    public static final String LOGO_ID = "logoId";
    public static final String LOGO_URL = "logoUrl";
    public static final String DIRECTORIES = "directories";
    public static final String PARENT = "parent";
    public static final String PARENT_ID = "parentId";
    public static final String CHILDREN = "children";
    public static final String DO_NOT_SHOW = "doNotShow";
    public static final String CREDIT_LIMIT = "creditLimit";
    public static final String ATTACHMENTS = "attachments";
    public static final String FROM_SIGNUP = "fromSignUp";
    public static final String FROM_SAASU = "fromSaasu";

    /*Taxi Base Object Fields*/
    public static final String CAR_NEXT_ENDED = "CarNextEnded";
    public static final String DRIVERN = "DriverN";
    public static final String DATE_START = "DateStart";
    public static final String TIME_START = "TimeStart";
    public static final String DATE_END = "DateEnd";
    public static final String TIME_END = "TimeEnd";

    /*Taxi Trip Fields*/
    public static final String TRIP_NUMBER = "TripN";
    public static final String INC_TOTAL = "IncTotal";
    public static final String INC_EXTRA = "IncExtra";
    public static final String DIST_TOTAL = "DistTotal";
    public static final String DIST_HIRED = "DistHired";
    public static final String DIST_BLACK = "DistBlack";
    public static final String TYPE_TRIP = "TypeTrip";
    public static final String TYPE_PAYM = "TypePaym";
    public static final String CRED_CARDN = "CredCardN";
    public static final String EXPIRE_DATE = "ExpireDate";
    public static final String AUTHORIZATN = "Authorizatn";
    public static final String MAX_SPEED = "MaxSpeed";
    public static final String GPS_LONG_IT_START = "GPSLongITStart";
    public static final String GPS_LAT_ID_START = "GPSLatIDStart";
    public static final String IS_GPS_START_FIX = "ISGPSStartFix";
    public static final String GPS_LONG_IT_END = "GPSLongITEnd";
    public static final String GPS_LAT_ID_END = "GPSLatIDEnd";
    public static final String IS_GPS_END_FIX = "ISGPSEndTFix";
    public static final String LINC_EXTRA1 = "LincExtra1";
    public static final String LINC_EXTRA2 = "LincExtra2";
    public static final String LINC_EXTRA3 = "LincExtra3";
    public static final String LINC_EXTRA4 = "LincExtra4";
    public static final String LINC_EXTRA5 = "LincExtra5";
    public static final String LINC_EXTRA6 = "LincExtra6";
    public static final String LINC_EXTRA7 = "LincExtra7";
    public static final String LINC_EXTRA8 = "LincExtra8";
    public static final String LINC_EXTRA9 = "LincExtra9";
    public static final String LINC_EXTRA10 = "LincExtra10";
    public static final String LINC_EXTRA11 = "LincExtra11";
    public static final String LINC_EXTRA12 = "LincExtra12";
    public static final String LINC_EXTRA13 = "LincExtra13";
    public static final String LINC_EXTRA14 = "LincExtra14";
    public static final String LINC_EXTRA15 = "LincExtra15";
    public static final String LINC_EXTRA16 = "LincExtra16";
    public static final String LINC_EXTRA17 = "LincExtra17";
    public static final String LINC_EXTRA18 = "LincExtra18";
    public static final String LINC_EXTRA19 = "LincExtra19";
    public static final String LINC_EXTRA20 = "LincExtra20";
    public static final String PAYROLLN = "PayrollN";
    public static final String LINC_DISCOUNT = "LincDisCount";
    public static final String INC_PASSENG_FEE = "IncPassengFee";
    public static final String LINC_ACCOUNT = "LincAccount";
    public static final String LINC_ACCOUNT_TAX = "LincAccountTax";
    public static final String LINC_CUSTOM1 = "LincCustom1";
    public static final String LINC_CUSTOM2 = "LincCustom2";
    public static final String DIST_CUSTOM1 = "DistCustom1";
    public static final String DIST_CUSTOM2 = "DistCustom2";
    public static final String NCUSTOM1 = "NCustom1";
    public static final String TMCUSTOM1 = "TMCustom1";
    public static final String TMCUSTOM2 = "TMCustom2";
    public static final String CARD_NUMDCC = "CardNumDCC";
    public static final String SERIAL_NUMDCC = "SerialNumDCC";
    public static final String DATE_EXPDCC = "DateExpDCC";
    public static final String AUTHORN_DCC = "AuthornDCC";
    public static final String LINC_TARIFF1 = "LincTariff1";
    public static final String LINC_TARIFF2 = "LincTariff2";
    public static final String LINC_TARIFF3 = "LincTariff3";
    public static final String LINC_TARIFF4 = "LincTariff4";
    public static final String LINC_TARIFF5 = "LincTariff5";
    public static final String LINC_TARIFF6 = "LincTariff6";
    public static final String LINC_TARIFFX = "LincTariffX";
    public static final String NCUSTOMER_NUM = "NCustomerNum";

    public static final String SHIFT_NUMBER = "ShiftN";
    public static final String SHIFT_TYPE = "Type";
    public static final String QTRIP = "QTrip";
    public static final String QUNIT = "QUnit";
    public static final String DIST_FH = "DistFH";
    public static final String DIST_OVSPED = "DistOvsped";
    public static final String INC_FARE = "IncFare";
    public static final String INC_CRED = "IncCred";
    public static final String INC_TAX = "IncTax";
    public static final String INC_DISCOUNT = "IncDiscount";
    public static final String INC_NOTONMTR = "IncNotOnMtr";
    public static final String INC_VAUCHER = "IncVaucher";
    public static final String INC_COMPLIM = "IncComplim";
    public static final String INC_AIRP_TAX = "IncAirpTax";
    public static final String INC_CASH_TURNIN = "IncCashTurnIn";
    public static final String INC_LESS_TRIPS = "IncLessTrips";
    public static final String INC_DRIVER_WAGES = "IncDriverWages";
    public static final String INC_TOTLEFT = "IncTotLeft";
    public static final String AVG_INC_FARE_DIST = "AvgIncFareDist";
    public static final String TMMVFH = "TMMVFH";
    public static final String TMMVHD = "TMMVHD";
    public static final String TMWT = "TMWT";
    public static final String TMTOTAL = "TMTotal";
    public static final String INCT1 = "Inct1";
    public static final String INCT2 = "Inct2";
    public static final String INCT3 = "Inct3";
    public static final String INCT4 = "Inct4";
    public static final String INCT5 = "Inct5";
    public static final String INCT6 = "Inct6";
    public static final String INCTX = "InctX";
    public static final String UNITT1 = "UnitT1";
    public static final String UNITT2 = "UnitT2";
    public static final String UNITT3 = "UnitT3";
    public static final String UNITT4 = "UnitT4";
    public static final String UNITT5 = "UnitT5";
    public static final String UNITT6 = "UnitT6";
    public static final String UNITTX = "UnitTX";
    public static final String DISTT1 = "DistT1";
    public static final String DISTT2 = "DistT2";
    public static final String DISTT3 = "DistT3";
    public static final String DISTT4 = "DistT4";
    public static final String DISTT5 = "DistT5";
    public static final String DISTT6 = "DistT6";
    public static final String DISTTX = "DistTX";
    public static final String CUSTOM1 = "Custom1";
    public static final String CUSTOM2 = "Custom2";
    public static final String CUSTOM3 = "Custom3";
    public static final String CUSTOM4 = "Custom4";
    public static final String CUSTOM5 = "Custom5";
    public static final String CUSTOM6 = "Custom6";
    public static final String CUSTOM7 = "Custom7";
    public static final String CUSTOM8 = "Custom8";

    public static final String SER_DATE_DWNL = "SerDateDWNL";
    public static final String ROM_CODE_CRC = "RomCodeCRC";
    public static final String INC_CUSTOM1 = "IncCustom1";
    public static final String INC_CUSTOM2 = "IncCustom2";
    /*Taxi Shift Fields*/


    public static final ArrayList<MSelectItem> toMSelectItemList(SelectItem[] items) {
        ArrayList<MSelectItem> list = new ArrayList<>();
        for (SelectItem item : items) {
            MSelectItem mItem = new MSelectItem(item);
            list.add(mItem);
        }
        return list;
    }

    public static final ArrayList<MSelectItem> toMSelectItemList(ArrayList<SelectItem> items) {
        ArrayList<MSelectItem> list = new ArrayList<>();
        for (SelectItem item : items) {
            MSelectItem mItem = new MSelectItem(item);
            list.add(mItem);
        }
        return list;
    }

    public Address convertToAddressItem(Map<String, Object> map) throws ParseException, ClassCastException {
        Address address = new Address();
        if (map != null) {
            address.setObjectID((Integer) map.get(OBJECT_ID));
            address.setAddress((String) map.get(ADDRESS));
            address.setAddressb((String) map.get(ADDRESSB));
            address.setCity((String) map.get(CITY));
            address.setCountry((String) map.get(COUNTRY));
            address.setCountryId(map.get(COUNTRY_ID) != null ? (Integer) map.get(COUNTRY_ID) : null);
            address.setState((String) map.get(STATE));
            address.setStateId(map.get(STATE_ID) != null ? (Integer) map.get(STATE_ID) : null);
            address.setZipCode((String) map.get(POST_CODE));
        }
        return address;
    }

    public static SelectItem[] convertToSelectItemList(List<Map<String, Object>> map) {
        ArrayList<SelectItem> list = new ArrayList<>();
        if (map != null) {
            for (Map<String, Object> mapItem : map) {
                SelectItem item = new SelectItem((Integer) mapItem.get(ID), (String) mapItem.get(NAME), (String) mapItem.get(DESCRIPTION));
                item.setSelected(mapItem.get(SELECTED) != null ? (Boolean) mapItem.get(SELECTED) : false);
                list.add(item);
            }
            return list.toArray(new SelectItem[]{});
        }
        return null;
    }
}
