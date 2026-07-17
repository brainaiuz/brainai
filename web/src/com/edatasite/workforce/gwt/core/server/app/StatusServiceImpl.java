package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsDynamicQuery;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTimeTrack;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTimeTrack;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserLastRequest;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.core.domain.EdsUsersFingerPrint;
import com.edatasite.workforce.core.domain.EdsUsersFingerPrintAdjustment;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.enums.FingerprintSource;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Exceptions.GoogleAppsException;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.StatusService;
import com.edatasite.workforce.gwt.core.client.rpc.SwitchvoxContactItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.GoogleMarketPlaceEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.employee.GoogleMarketPlaceUser;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.DynamicQueryManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTimeTrackManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleMarketplaceManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.ServerUploadHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeTrackManager;
import com.edatasite.workforce.gwt.core.server.db.TimeZoneManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintmanager;
import com.edatasite.workforce.gwt.core.server.db.UserLastRequestManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.UserRequestItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.rpc.FingerPrintItem;
import com.edatasite.workforce.gwt.core.server.rpc.FingerPrintUserItem;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.dashboard.client.rpc.DashboardService;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.QueriesListDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.QueryNameDTO;
import com.google.api.services.admin.directory.model.User;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

@Transactional
@Service("statusService")
public class StatusServiceImpl implements StatusService, StatusServiceLocal, Constants {

    private static final Logger log = LoggerFactory.getLogger(StatusServiceImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private UserManager userManager;
    @Autowired
    private TimeTrackManager timeTrackManager;
    @Autowired
    private EmployeeTimeTrackManager employeeTimeTrackManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private GoogleMarketplaceManager googleMarketplaceManager;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private TimeZoneManager zoneManager;
    @Autowired
    private ServerUploadHistoryManager serverUploadHistoryManager;
    @Autowired
    private UserFingerPrintmanager userFingerPrintmanager;
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private UserSessionManager userSessionManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private DynamicQueryManager dynamicQueryManager;
    @Autowired
    private UserLastRequestManager userLastRequestManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private SolrManager solrManager;

    @Autowired
    @Qualifier("rabbitMQService")
    private RabbitMQService rabbitMQService;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private UserFingerPrintAdjustmentManager userFingerPrintAdjustmentManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;

    public String setUserStatus(String changeStatusCode, boolean timeSpentRequired, boolean logout) {
        EdsUser user = employeeManager.getUser();
        if (user == null) {
            return null;
        }
        String result = setUserStatus(user.getObjectID(), changeStatusCode, timeSpentRequired);
        if (logout) {
            ServerSecurityContext.getInstance().expireServerSession();
        }
        return result;
    }

    public String setUserStatus(Integer employeeId, String changeStatusCode, boolean timeSpentRequared) {

        EdsUser user = userManager.get(employeeId);
        if (changeStatusCode != null) {
            proceedUserAbsentStatus(changeStatusCode, user.getObjectID());
        }

        if (timeSpentRequared && (changeStatusCode == null || changeStatusCode.equals(AVAILABLE) || changeStatusCode.equals(BREAK))) {
            String spent = null;
            String today = dateFormat.format(user.getCompany().getCompanyDate());
            List<EdsTimeTrack> timeTrack = timeTrackManager.getTimeEntriesByToday(user.getObjectID(), today);
            if (!timeTrack.isEmpty()) {
                Iterator<EdsTimeTrack> iterator = timeTrack.iterator();
                EdsTimeTrack lastTrack = iterator.next();
                long time = 0;
                if (lastTrack != null && (lastTrack.getStatus().getCode().equals(AVAILABLE))) {
                    time = user.getCompany().getCompanyDate().getTime() - lastTrack.getStartDate().getTime();
                }
                while (iterator.hasNext()) {
                    EdsTimeTrack track = iterator.next();
                    if (track.getStartDate() != null && track.getEndDate() != null && (track.getStatus().getCode().equals(AVAILABLE))) {
                        time += track.getEndDate().getTime() - track.getStartDate().getTime();
                    }
                }
                spent = String.valueOf(time / (1000 * 60));
            }
            return spent;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getUserStatus() {
        EdsUser user = employeeManager.getUser();
        return getUserStatus(user.getObjectID());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getUserStatus(Integer userId) {
        try {
            EdsTimeTrack timeTrack = timeTrackManager.getLatestTimeEntry(userId);
            if (timeTrack == null) {
                return "Out";
            }
            if (AVAILABLE.equals(timeTrack.getStatus().getCode())) {
                return "In";
            } else if (NOT_AVAILABLE.equals(timeTrack.getStatus().getCode())) {
                return "Out";
            } else {
                return "Lunch";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insertEmployeeTimeTrack(Integer employeeID, String status, DateNonConvertable date, String location) {
        EdsEmployeeTimeTrack edsEmployeeTimeTrack = employeeTimeTrackManager.getEmployeeTimeTrack(employeeID, status, date);

        if (edsEmployeeTimeTrack == null) {
            edsEmployeeTimeTrack = new EdsEmployeeTimeTrack();
            edsEmployeeTimeTrack.setDate(date.getNonConvertedDate());
        }
        edsEmployeeTimeTrack.setEmployeeId(employeeID);

        switch (status) {
            case AVAILABLE -> edsEmployeeTimeTrack.setCheckIn(date.getNonConvertedDate());
            case BREAK -> edsEmployeeTimeTrack.setOnBreak(date.getNonConvertedDate());
            case ON_DUTY -> edsEmployeeTimeTrack.setOnDuty(date.getNonConvertedDate());
            case NOT_AVAILABLE -> edsEmployeeTimeTrack.setCheckOut(date.getNonConvertedDate());
        }
        if (StringUtils.isNotBlank(location)) {
            edsEmployeeTimeTrack.setLocation(location);
        }
        edsEmployeeTimeTrack.setCreationDate(new Date());
        employeeTimeTrackManager.createOrUpdate(edsEmployeeTimeTrack);
    }

    @Override
    public String runScripts(QueriesListDTO queries) throws Exception {
        for (QueryNameDTO queryName : queries.getQueryNames()) {
            EdsDynamicQuery dynamicQuery = dynamicQueryManager.getQueryByName(queryName.getQueryName());
            if (dynamicQuery != null) {
                String query = dynamicQuery.getQuery_text().replace("anv", ServerSecurityContext.getInstance().getCompanyId());
                if ("GLOBAL_AUTH".equals(queryName.getDbName())) {
                    globalAuthJdbcSpringManager.patchForGA(query, queryName.getParams());
                } else {
                    dynamicQueryManager.updateNativeByParams(query, queryName.getParams());
                }
                if (queryName.runWorkflow()) {
                    EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, queryName.getActionType(), allInOneServiceLocal.getTraceableDomainObject(queryName.getRelationId(), queryName.getRelationType()), userManager.getUser());
                    workflowEvent.setEntityType(queryName.getRelationType());
                }
                if (queryName.indexToSolr()) {
                    if (queryName.getSolrType() != null && queryName.getSolrType().equals("REMOVE")) {
                        solrManager.removeDynamicEntity(queryName.getRelationType(), queryName.getRelationId());
                    } else {
                        solrManager.indexDynamicEntity(queryName.getRelationType(), queryName.getRelationId());
                    }
                }
            }
        }
        return QB_SYNCH_COMPLETED_MESSAGE;
    }


    //POPUP
    public Boolean refreshSession(String sessionId, String encryptedPassword) {

        if (sessionId != null && StringUtils.isNotBlank(encryptedPassword)) {
            Integer companyId = Integer.parseInt(sessionId.split("\\$")[1]);
            SecurityContext.getInstance().setCompanyId(companyId);
            SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyClusterType(companyId));
            EdsUserSession session = userSessionManager.getUserSession(sessionId);
            EdsUser user = null;

            if (session != null && session.getUser() != null) {
                user = session.getUser();
                EdsUserLastRequest lastDate = userLastRequestManager.getLastDate(user.getObjectID());
                Date lastRequest;
                if (lastDate != null) {
                    lastRequest = lastDate.getRequestDate();
                } else {
                    lastRequest = new Date();
                }

                if (user.getEmployee() != null) {
                    EdsTimeTrack timeTrack = timeTrackManager.getLatestTimeEntry(user.getEmployee().getObjectID());
                    if (timeTrack != null) {

                        Date date = new Date();

                        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                        boolean isCorrect = globalAuthJdbcSpringManager.getAuthInfo(request.getServerName(), globalAuthJdbcSpringManager.getUsername(user.getCompany().getObjectID(), user.getObjectID()), ServerUtils.decrypt(encryptedPassword));

                        int rawOffset = 0;
                        if (user.getCompany() != null)
                            rawOffset = user.getCompany().getTimeZone().getRawOffset();

                        if (!ServerUtils.dateEqualWithTimeZone(timeTrack.getStartDate(), date, rawOffset)) {
                            Calendar outLogic = new GregorianCalendar();
                            outLogic.setTime(timeTrack.getStartDate());
                            outLogic.set(Calendar.HOUR_OF_DAY, 23);
                            outLogic.set(Calendar.MINUTE, 59);
                            outLogic.set(Calendar.SECOND, 59);
                            outLogic.set(Calendar.MILLISECOND, 0);

                            outLogic.setTime(new Date(outLogic.getTime().getTime() - rawOffset));
                            if (outLogic.getTime().getTime() < lastRequest.getTime()) {
                                timeTrack.setEndDate(outLogic.getTime());
                                timeTrack.setLocation("SET_23_59");
                                timeTrackManager.createOrUpdate(timeTrack);

                                EdsTimeTrack track = new EdsTimeTrack();
                                track.setFingerPrintUser(timeTrack.isFingerPrintUser());
                                track.setStatus(timeTrack.getStatus());
                                track.setEmployee(user.getEmployee());

                                outLogic.add(Calendar.SECOND, 1);
                                track.setStartDate(outLogic.getTime());
                                track.setInMethod("SET_00_01");
                                track.setEndDate(lastRequest);
                                track.setLocation("SET_00_01_AFTER_LASTREQUEST");
                                timeTrackManager.createOrUpdate(track);
//                                insertLastRequest(user, date, "NEXT_DAY_AND_SETOUT_LASTREQUEST", session);
                                if (isCorrect) {
                                    EdsTimeTrack track1 = new EdsTimeTrack();
                                    track1.setFingerPrintUser(timeTrack.isFingerPrintUser());
                                    track1.setStatus(timeTrack.getStatus());
                                    track1.setEmployee(user.getEmployee());
                                    track1.setStartDate(date);
                                    track1.setInMethod("IS_CORRECT_AND_NEXtDAY_POPUP_IN_2");
                                    timeTrackManager.createOrUpdate(track1);
                                    insertLastRequest(user, date, "NEXT_DAY_AND_CORRECT_PASSWORD", session);
                                }
                            } else {
                                timeTrack.setEndDate(lastRequest);
                                timeTrack.setLocation("LASTREQ_LASS_THAN_OUTLOGIC");
                                timeTrackManager.createOrUpdate(timeTrack);
//                                insertLastRequest(user, date, "LASTREQUEST_LESS_THAN_OUTDATE", session);
                                if (isCorrect) {
                                    EdsTimeTrack track1 = new EdsTimeTrack();
                                    track1.setFingerPrintUser(timeTrack.isFingerPrintUser());
                                    track1.setStatus(timeTrack.getStatus());
                                    track1.setEmployee(user.getEmployee());
                                    track1.setStartDate(date);
                                    track1.setInMethod("IS_CORRECT_AND_NEXtDAY_POPUP");
                                    timeTrackManager.createOrUpdate(track1);
                                    insertLastRequest(user, date, "LASTREQUEST_LESS_THAN_OUTDATE_AND_CORRECT_PASSWORD", session);
                                }
                            }
                        } else {
                            timeTrack.setEndDate(lastRequest);
                            timeTrack.setLocation("THIS_DAY_SET_LAST_REQUEST");
//                            insertLastRequest(user, date, "THIS_DAY_1", session);
                            timeTrackManager.createOrUpdate(timeTrack);
                            if (isCorrect) {
                                EdsTimeTrack track = new EdsTimeTrack();
                                track.setFingerPrintUser(timeTrack.isFingerPrintUser());
                                track.setStatus(timeTrack.getStatus());
                                track.setEmployee(user.getEmployee());
                                track.setStartDate(date);
                                track.setInMethod("IS_CORRECT_POPUP_IN_2");
                                timeTrackManager.createOrUpdate(track);
                                insertLastRequest(user, date, "THIS_DAY_1_CORRECT_PASSWORD", session);
                            }
                        }
                    }
                }
            }
            if (user == null) {
                try {
                    user = userManager.getUser();
                    if (user == null) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return globalAuthJdbcSpringManager.getAuthInfo(request.getServerName(), globalAuthJdbcSpringManager.getUsername(user.getCompany().getObjectID(), user.getObjectID()), ServerUtils.decrypt(encryptedPassword));
        } else {
            return false;
        }
    }


    // LOGOUT
    public void getDateAndSetEndDate(DateNonConvertable nonConvertable) {

        Date dateNow = new Date();
        EdsUser edsUser = this.userManager.getUser();

        if (edsUser != null) {
            if (edsUser.getEmployee() != null) {
                Integer userID = edsUser.getEmployee().getObjectID();
                EdsTimeTrack timeTrack = timeTrackManager.getLatestTimeEntry(userID);
                if (timeTrack != null) {

                    Calendar outLogic = new GregorianCalendar();

                    int rawOffset = 0;
                    if (edsUser.getCompany() != null)
                        rawOffset = edsUser.getCompany().getTimeZone().getRawOffset();

                    if (!ServerUtils.dateEqualWithTimeZone(timeTrack.getStartDate(), dateNow, rawOffset)) {

                        outLogic.setTime(timeTrack.getStartDate());
                        outLogic.set(Calendar.HOUR_OF_DAY, 23);
                        outLogic.set(Calendar.MINUTE, 59);
                        outLogic.set(Calendar.SECOND, 59);
                        outLogic.set(Calendar.MILLISECOND, 0);

                        outLogic.setTime(new Date(outLogic.getTime().getTime() - rawOffset));

                        timeTrack.setEndDate(outLogic.getTime());
                        timeTrack.setLocation("LOGOUT1");
                        timeTrackManager.createOrUpdate(timeTrack);

                        EdsTimeTrack track = new EdsTimeTrack();

                        outLogic.add(Calendar.SECOND, 1);
                        track.setStartDate(outLogic.getTime());
                        track.setInMethod("LOGOUT_IN_1_NEXTDAY");

                        track.setFingerPrintUser(timeTrack.isFingerPrintUser());
                        track.setStatus(timeTrack.getStatus());
                        track.setEmployee(edsUser.getEmployee());

                        track.setEndDate(dateNow);
                        track.setLocation("LOGOUT2");
                        timeTrackManager.createOrUpdate(track);
                    } else {
                        timeTrack.setEndDate(dateNow);
                        timeTrack.setLocation("LOGOUT3");
                        timeTrackManager.createOrUpdate(timeTrack);
                    }
                }
            }
        }
        EdsUserSession userSession = userSessionManager.getUserSession(ServerSecurityContext.getInstance().getSessionId());
        if (userSession != null) {
            userSession.setExpired(true);
        }
    }

    @Override
    public void insertUserRequest(String methodName) {
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        String sessionId = ServerSecurityContext.getInstance().getSessionId();
//        throw to RabbitMq
        if (user != null) {
            rabbitMQService.userRequestTrackingMQ(new UserRequestItemMQ(methodName, user.getObjectID(), sessionId, new Date()));
        }
    }


    //LOGIN
    @Transactional
    public void proceedUserAbsentStatus(String statusCode, Integer employeeId) {

        Date currentDate = new Date();

        EdsUser user = userManager.get(employeeId);
        EdsUserSession userSession = userSessionManager.getUserSession(ServerSecurityContext.getInstance().getSessionId());
        if ((userSession != null && userSession.isSuperUser()) || !user.isEmployee())
            return;

        int sessionValue = 0;
        if (user.getCompany() != null)
            sessionValue = getSessionValue(user.getCompany().getObjectID());

        EdsUserLastRequest lastDate = userLastRequestManager.getLastDate(employeeId);
        Date lastRequest;
        if (lastDate != null) {
            lastRequest = lastDate.getRequestDate();
        } else {
            lastRequest = new Date();
        }
        Date lastRequestWithSession = DateUtils.addMinutes(lastRequest, sessionValue);

        EdsTimeTrack timeTrack = timeTrackManager.getLatestTimeEntry(employeeId);
        EdsReference statusReference = referenceManager.findReference(TIME_TRACK_STATUS, statusCode);
        EdsEmployee employee = employeeManager.get(user.getObjectID());

        if (timeTrack != null) {
            int rawOffset = 0;

            if (user.getCompany() != null)
                rawOffset = user.getCompany().getTimeZone().getRawOffset();
            if (ServerUtils.dateEqualWithTimeZone(timeTrack.getStartDate(), currentDate, rawOffset)) {


                long l = currentDate.getTime() - lastRequest.getTime();
                long minut = (l / 1000) / 60;
                if (minut >= sessionValue) {
                    if (lastRequest.getTime() >= timeTrack.getStartDate().getTime()) {
                        timeTrack.setEndDate(lastRequest);
                        timeTrack.setLocation("NEXT_LOGIN_1");
                    } else {
                        timeTrack.setEndDate(lastRequestWithSession);
                        timeTrack.setLocation("NEXT_LOGIN_2");
                    }
                    timeTrackManager.createOrUpdate(timeTrack);

                    EdsTimeTrack nextTime = new EdsTimeTrack();
                    nextTime.setStartDate(currentDate);
                    nextTime.setStatus(statusReference);
                    nextTime.setEmployee(employee);
                    nextTime.setInMethod("NEXT_IN");
                    nextTime.setFingerPrintUser(false);
                    timeTrackManager.createOrUpdate(nextTime);
                }
                insertLastRequest(user, currentDate, "LOGIN_THIS_DAY", userSession);
                return;
            }

            if (userManager.getUser() != null) {
                if (lastRequest.getTime() > timeTrack.getStartDate().getTime()) {
                    timeTrack.setEndDate(lastRequest);
                    timeTrack.setLocation("LOGIN_WITH_LASTREQ");
                } else {
                    Date dateWithSession = DateUtils.addMinutes(timeTrack.getStartDate(), sessionValue);
                    timeTrack.setEndDate(dateWithSession);
                    timeTrack.setLocation("LOGIN_WITH_SESSION");
                }
                insertLastRequest(user, currentDate, "LOGIN_NEXT_DAY", userSession);
                inOutAttendanceRawDataCreateUpdate(statusCode, employeeId, lastRequest, false);
                timeTrackManager.update(timeTrack);
            }
        }
        employeeManager.flush();
        if (employee != null) {
            timeTrack = new EdsTimeTrack();
            timeTrack.setEmployee(employee);
            timeTrack.setInMethod("NEW_LOGIN");
            timeTrack.setStartDate(currentDate);
            timeTrack.setStatus(statusReference);
            timeTrack.setFingerPrintUser(false);
            timeTrackManager.create(timeTrack);
            insertLastRequest(user, currentDate, "NEW_LOGIN_DAY", userSession);
        } else {
            timeTrackManager.insertTimeTrack(user.getCompany().getObjectID(), user.getObjectID(), currentDate, statusReference.getObjectID(), false);
        }
    }

    private void inOutAttendanceRawDataCreateUpdate(String statusCode, Integer employeeId, Date endDate, boolean isFingerPrintUser) {
        long inTime = 0;
        if (BREAK.equals(statusCode) || NOT_AVAILABLE.equals(statusCode)) {

            EdsUser user = userManager.getUserByUserID(employeeId);
            //create new one year if clientToday's date is not in datejoin table
            dashboardService.lastEnteredDate(0, user.getCompany().getObjectID());
            //create attendance raw data for the current year and employee, if there is none
            availabilityService.createAttendaceRawDataRecords(employeeId, 0);

            List<EdsTimeTrack> onlyInTimeTrackList = timeTrackManager.getUserAvailableTimeTrackInPeriod(employeeId, endDate, endDate);//start and end dates are the same, it means the period is one day, the day we have updated.
            for (EdsTimeTrack inTimeTrack : onlyInTimeTrackList) {
                if (inTimeTrack != null && inTimeTrack.getEndDate() != null && inTimeTrack.getStartDate() != null) {
                    inTime += inTimeTrack.getEndDate().getTime() - inTimeTrack.getStartDate().getTime();
                }
            }
            int inMinutes = (int) Math.ceil((double) inTime / 60000);
            Calendar from = Calendar.getInstance();
            from.setTime(endDate);
            from.set(Calendar.AM_PM, 0);
            from.set(Calendar.HOUR_OF_DAY, 0);
            from.set(Calendar.MINUTE, 0);
            from.set(Calendar.SECOND, 0);
            from.set(Calendar.MILLISECOND, 0);
            EdsAttendanceRawData attendanceRawData = attendanceRawDataManager.getAttendanceRawDataByDate(from.getTime(), employeeId);
            if (attendanceRawData != null) {
                attendanceRawData.setInTime(inMinutes);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public GoogleMarketPlaceUser getGoogleMarketPlaceUsersFirstTime() throws GoogleAppsException {
        EdsUser domainUser = userManager.getUser();
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(domainUser.getCompany().getObjectID());

        if (companySystemSettings == null || companySystemSettings.isShowPopups() == null || !companySystemSettings.isShowPopups()) {
            return null;
        }
        return getGoogleAppsDomainUsers(domainUser, companySystemSettings);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public GoogleMarketPlaceUser getGoogleMarketPlaceUsers() throws GoogleAppsException {
        EdsUser domainUser = userManager.getUser();
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(domainUser.getCompany().getObjectID());
        return getGoogleAppsDomainUsers(domainUser, companySystemSettings);
    }

    private GoogleMarketPlaceUser getGoogleAppsDomainUsers(EdsUser domainUser, EdsCompanySystemSettings companySystemSettings) throws GoogleAppsException {
        if (companySystemSettings.getGoogleAppDomain() == null) {
            return null;
        }
        List<EdsEmployee> employees = employeeManager.getCompanyEmployees();

        String domainName = companySystemSettings.getGoogleAppDomain();
        String section = companySystemSettings.getGoogleAppSection();
        List<User> allUsers;
        try {
            allUsers = googleMarketplaceManager.getDomainUsers(domainName, section);
        } catch (Exception ex) {
            throw new GoogleAppsException(domainName);
        }
        if (allUsers == null) {
            throw new GoogleAppsException(domainName);
        }

        ArrayList<NewEmployee> compEmployees = new ArrayList<>();
        for (User userEntry1 : allUsers) {
            NewEmployee user = new NewEmployee();
            user.setFname(userEntry1.getName().getGivenName());
            user.setLname(userEntry1.getName().getFamilyName());
            user.setEmail(userEntry1.getPrimaryEmail());
            if (userEntry1.getThumbnailPhotoUrl() != null)
                user.setPhotoURL(userEntry1.getThumbnailPhotoUrl());
            else
                user.setPhotoURL("https://lh5.googleusercontent.com/-JAa1WBI0o0E/AAAAAAAAAAI/AAAAAAAAAAA/I1Q2JHHHFwo/s96-c/photo.jpg");//NO PHOTO
            //PHOTO

            boolean saved = false;
            for (EdsEmployee employee : employees) {
                if (!StringUtils.isEmpty(employee.getEmail()) && !StringUtils.isEmpty(user.getEmail()) && employee.getEmail().equals(user.getEmail())) {
                    saved = true;
                    break;
                }
            }

            if (!saved) {
                compEmployees.add(user);
            }
        }

        GoogleMarketPlaceUser result = new GoogleMarketPlaceUser();
        result.setCompanyName(domainUser.getCompany().getName());
        List<GoogleMarketPlaceEmployee> placeEmployees = new ArrayList<>();
        for (NewEmployee employee : compEmployees) {
            GoogleMarketPlaceEmployee placeEmployee = new GoogleMarketPlaceEmployee();
            placeEmployee.setPhotoURL(employee.getPhotoURL());
            placeEmployee.setEmail(employee.getEmail());
            placeEmployee.setFname(employee.getFname());
            placeEmployee.setLname(employee.getLname());
            placeEmployees.add(placeEmployee);
        }
        result.setEmployees(placeEmployees.toArray(new GoogleMarketPlaceEmployee[]{}));

        return result;

    }

    public Integer[] saveEmployees(GoogleMarketPlaceUser employees, boolean showPopup) {
        if (showPopup) {
            if (employees.getCompanyName() == null || employees.getCompanyPhone() == null || employees.getCountryID() == null) {
                return null;
            }
        }

        EdsCompany company = userManager.getUser().getCompany();
        if (showPopup) {
            company.setName(employees.getCompanyName());
            company.setPhone(employees.getCompanyPhone());

            EdsCountry country = countryManager.get(employees.getCountryID());
            if (!zoneManager.getCountryZones(country).isEmpty()) {
                company.setCountryZone(zoneManager.getCountryZones(country).get(0));
            }
            genericSettingsManager.saveGenericSettings(company.getObjectID(), GenericSettingsEnum.VAT_RETURN_ENABLE, "GB".equals(country.getCode()) ? EdsGenericSettings.YES : EdsGenericSettings.NO);
        }

        ArrayList<Integer> result = new ArrayList<>();
        for (GoogleMarketPlaceEmployee placeEmployee : employees.getEmployees()) {
            NewEmployee employee = new NewEmployee();
            employee.setLname(placeEmployee.getLname());
            employee.setFname(placeEmployee.getFname());
            employee.setEmail(placeEmployee.getEmail());
            employee.setPhotoURL(placeEmployee.getPhotoURL());
            employee.setCreatedFrom(EMPLOYEE_CREATED_GOOGLE_MARKET_PLACE);
            Integer id = employeeService.createEmployee(employee, false);

            EdsEmployee compEmployee = employeeManager.get(id);
            if (compEmployee != null) {
                compEmployee.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
            }

            result.add(id);
        }

        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(company.getObjectID());
        if (companySystemSettings != null && (employees.getCompanyName() != null || employees.getCompanyPhone() != null || employees.getCountryID() != null)) {
            companySystemSettings.setShowPopups(false);
        }

        return result.toArray(new Integer[]{});
    }

    /**
     * Returns the version of the latest upload. The
     * upload maybe LIVE or AWS.
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getLatestServerUploadVersion() {
        return serverUploadHistoryManager.getLatestUploadVersion();
    }

    @Override
    public SwitchvoxContactItem getIncomingCallerID() {
        SwitchvoxContactItem switchvoxContactItem = new SwitchvoxContactItem();
        ContactListItem contactListItem = crmServiceLocal.getIncomingCallerID();
        if (contactListItem == null) {
            return null;
        }
        switchvoxContactItem.setObjectId(contactListItem.getObjectId());
        switchvoxContactItem.setName(contactListItem.getContactName());
        if (contactListItem.getCrmAccount() != null) {
            switchvoxContactItem.setAccountObjectId(contactListItem.getCrmAccount().getObjectId());
        }
        if (contactListItem.getWorkPhone() != null && !contactListItem.getWorkPhone().isEmpty()) {
            switchvoxContactItem.setWorkPhone(contactListItem.getWorkPhone().get(0));
        }
        return switchvoxContactItem;
    }

    @Override
    public void addFingerPrintItemsToTimeTrackAll(FingerPrintItem printItem, CompanyDomain companyDomain) {
        log.info("Fingerprint process started! {}", new Date());
        EdsReference availableStatus = referenceManager.findReference(TIME_TRACK_STATUS, AVAILABLE);
        EdsReference notAvailableStatus = referenceManager.findReference(TIME_TRACK_STATUS, NOT_AVAILABLE);
        FingerprintSource source = FingerprintSource.from(ServerSecurityContext.getInstance().getSource());
        EdsReference[] statuses = new EdsReference[2];
        statuses[0] = availableStatus;
        statuses[1] = notAvailableStatus;
        for (int i = 0; i < printItem.getUsers().size(); i++) {
            proceedAPIFingerprintUsersAbsentStatus(printItem.getUsers().get(i), statuses, companyDomain);
            saveAttendanceDetails(companyDomain, printItem, printItem.getUsers().get(i), source);
            if (i % 500 == 0 && i > 0) {
                userFingerPrintmanager.flushAndClear();
                log.info("Transaction flushAndClear! {}", new Date());
            }
        }
        log.info("Fingerprint process is ended! {}", new Date());
    }

    private void saveAttendanceDetails(CompanyDomain companyDomain,
                                       FingerPrintItem printItem,
                                       FingerPrintUserItem fingerPrintItem,
                                       FingerprintSource source) {
        if (fingerPrintItem.getLatitude() == null
                && fingerPrintItem.getLongitude() == null
                && fingerPrintItem.getSnapshots() == null
                && !Boolean.TRUE.equals(fingerPrintItem.getIsAuto()))
            return;
        var latestTimeEntry = userFingerPrintmanager.getLatestTimeEntry(fingerPrintItem.getUserId(), fingerPrintItem.getLogDate(companyDomain.getFingerprintDateFormat()), companyDomain.getCompanyUniqueID());
        if (latestTimeEntry == null) return;
        var adjustment = userFingerPrintAdjustmentManager.getByFingerprint(latestTimeEntry.getObjectID());
        if (adjustment == null) adjustment = new EdsUsersFingerPrintAdjustment();
        adjustment.setFingerprint(latestTimeEntry);
        adjustment.setDescription(fingerPrintItem.getNote());
        adjustment.setLatitude(fingerPrintItem.getLatitude());
        adjustment.setLongitude(fingerPrintItem.getLongitude());
        adjustment.setSource(source);
        adjustment.setIsAuto(fingerPrintItem.getIsAuto());
        if (printItem.getProjectId() != null) { // TODO check if project exists by id
            adjustment.setProject(projectManager.load(printItem.getProjectId()));
        }
        if (printItem.getTaskId() != null) {
            adjustment.setTask(taskManager.load(printItem.getTaskId()));
        }
        userFingerPrintAdjustmentManager.createOrUpdate(adjustment);
        var adjustmentByFingerprint = userFingerPrintAdjustmentManager.getByFingerprint(latestTimeEntry.getObjectID());
        saveAttendanceAttachment(adjustmentByFingerprint.getObjectID(), fingerPrintItem.getSnapshots());
    }

    private void proceedAPIFingerprintUsersAbsentStatus(FingerPrintUserItem fingerPrintItem, EdsReference[] statuses, CompanyDomain companyDomain) {
        Date checkTime = fingerPrintItem.getLogDate(companyDomain.getFingerprintDateFormat());
        if (checkTime == null) return;

        if (companyDomain.getDynamicStatus()) {
            EdsUsersFingerPrint usersFingerPrint = userFingerPrintmanager.getLatestTimeEntry(fingerPrintItem.getUserId(), checkTime, companyDomain.getCompanyUniqueID());
            if (usersFingerPrint == null) {
                usersFingerPrint = new EdsUsersFingerPrint();
                usersFingerPrint.setFingerprintId(fingerPrintItem.getUserId());
                usersFingerPrint.setStartDate(checkTime);
                usersFingerPrint.setDeviceUUID(companyDomain.getCompanyUniqueID());
                usersFingerPrint.setDeviceName(companyDomain.getCompanyBranchName());
                usersFingerPrint.setStatusString(fingerPrintItem.getStatus());
                EdsReference changedStatusCode = statuses[0];
                if (fingerPrintItem.getStatus() != null && "1".equals(fingerPrintItem.getStatus().trim())) {
                    changedStatusCode = statuses[1];
                }
                usersFingerPrint.setStatus(changedStatusCode);
                userFingerPrintmanager.createOrUpdate(usersFingerPrint);
            }
        } else {
            EdsUsersFingerPrint usersFingerPrint = userFingerPrintmanager.getLatestTimeEntry(fingerPrintItem.getUserId(), checkTime, true);
            if (usersFingerPrint != null) {
                int diff = DateUtil.getMinutDiff(usersFingerPrint.getStartDate(), checkTime);
                if (diff > 3) {
                    usersFingerPrint.setEndDate(checkTime);
                    userFingerPrintmanager.update(usersFingerPrint);
                }
            } else {
                usersFingerPrint = userFingerPrintmanager.getLatestTimeEntry(fingerPrintItem.getUserId(), checkTime, false); //enddate is not null
                if (usersFingerPrint != null) { //if employee enters one more time in this day and minute difference is less than 1 minute
                    if (DateUtil.getMinutDiff(usersFingerPrint.getEndDate(), checkTime) > 3 && DateUtil.getMinutDiff(usersFingerPrint.getStartDate(), checkTime) > 3) {
                        createFingerPrintItem(checkTime, statuses[0], companyDomain, fingerPrintItem);
                    }
                } else {
                    createFingerPrintItem(checkTime, statuses[0], companyDomain, fingerPrintItem);
                }
            }
        }
    }


    private void saveAttendanceAttachment(Integer itemId, List<String> attachments) {
        if (attachments == null || attachments.isEmpty()) return;
        FileItem[] fileItems = attachments.stream()
                .map(f -> {
                    FileItem fileItem = new FileItem();
                    fileItem.setFileName(f);
                    return fileItem;
                })
                .toArray(FileItem[]::new);
        attachmentUtilsManager.saveAttachments(F_EMPLOYEE_ATTENDANCE, itemId, itemId, fileItems);
    }

    private void createFingerPrintItem(Date checkTime, EdsReference status, CompanyDomain companyDomain, FingerPrintUserItem fingerPrintItem) {
        EdsUsersFingerPrint usersFingerPrint = new EdsUsersFingerPrint();
        usersFingerPrint.setFingerprintId(fingerPrintItem.getUserId());
        usersFingerPrint.setStartDate(checkTime);
        usersFingerPrint.setStatus(status);
        usersFingerPrint.setDeviceUUID(companyDomain.getCompanyUniqueID());
        usersFingerPrint.setDeviceName(companyDomain.getCompanyBranchName());
        userFingerPrintmanager.create(usersFingerPrint);
    }

    public int getSessionValue(Integer companyid) {
        EdsCompanySystemSettings systemSettingsManagerByCompanyID = companySystemSettingsManager.findByCompanyID(companyid);
        if (systemSettingsManagerByCompanyID == null
                || systemSettingsManagerByCompanyID.getSessionLength() == null
                || systemSettingsManagerByCompanyID.getSessionLength().isEmpty()
                || systemSettingsManagerByCompanyID.getSessionLength().equals("null")) {
            return 0;
        }
        return Integer.parseInt(systemSettingsManagerByCompanyID.getSessionLength());
    }

    public void insertLastRequest(EdsUser user, Date date, String method, EdsUserSession userSession) {
        if ((userSession == null || userSession.isSuperUser()) || !user.isEmployee()) {
            return;
        }
        EdsUserLastRequest userLastRequest = new EdsUserLastRequest();
        userLastRequest.setEmployee(user);
        userLastRequest.setRequestDate(date);
        userLastRequest.setMethodName(method);
        userLastRequestManager.create(userLastRequest);
    }
}
