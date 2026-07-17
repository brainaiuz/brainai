package com.edatasite.workforce.gwt.team.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsDepartmentTree;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.customfields.EdsDepartmentCustomFields;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.solr.component.DepartmentSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeSolrComponent;
import com.edatasite.workforce.core.solr.document.DepartmentSolrDoc;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ChartNode;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.department.DepartmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrDepartmentRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.OrgChartUtils;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.RolePermissionServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentTreeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceLocaleManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.DepartmentCFManager;
import com.edatasite.workforce.gwt.core.server.db.goal.DepartmentGoalEmployeeMetricHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalAssigneesManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.DepartmentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EmployeeDepartmentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EmployeeEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.ComparatorFactory;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.location.server.LocationServiceLocal;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.NewTeam;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEPARTMENT_TITLES;

@Transactional
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService/*, IDepartmentService*/ {
    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    EmployeeTaskManager employeeTaskManager;
    @Autowired
    TimeSheetManager timesheetManager;
    @Autowired
    private DepartmentTreeManager departmentTreeManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private ReferenceLocaleManager referenceLocaleManager;
    @Autowired
    @Qualifier("rolePermissionService")
    private RolePermissionServiceLocal rolePermissionServiceLocal;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private DepartmentCFManager departmentCFManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserManager userManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private LocationServiceLocal locationServiceLocal;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;
    @Autowired
    private DepartmentSolrComponent departmentSolrComponent;
    @Autowired
    private GoalManager goalManager;
    @Autowired
    private DepartmentGoalEmployeeMetricHistoryManager employeeMetricHistoryManager;
    @Autowired
    private GoalAssigneesManager goalAssigneesManager;

    private static final Map<String, ComparatorFactory<EdsDepartment>> comparatorFactories = new HashMap<>();

    static {
        comparatorFactories.put(TeamListItem.NAME,
                sortOrder -> new AbstractComparator<EdsDepartment>() {
                    public int compare(EdsDepartment o1, EdsDepartment o2) {
                        return internalCompare(o1.getName(),
                                o2.getName(), sortOrder);
                    }
                });

        comparatorFactories.put(TeamListItem.DESCRIPTION,
                sortOrder -> new AbstractComparator<EdsDepartment>() {
                    public int compare(EdsDepartment o1, EdsDepartment o2) {
                        return internalCompare(o1.getDescription(),
                                o2.getDescription(), sortOrder);
                    }
                });

        comparatorFactories.put(TeamListItem.PARENT_DEPARTMENT,
                sortOrder -> new AbstractComparator<EdsDepartment>() {
                    public int compare(EdsDepartment o1, EdsDepartment o2) {
                        return internalCompare(o1.getName(),
                                o2.getName(), sortOrder);
                    }
                });

        comparatorFactories.put(TeamListItem.LEADER_NAME,
                sortOrder -> new AbstractComparator<EdsDepartment>() {
                    public int compare(EdsDepartment o1, EdsDepartment o2) {
                        return internalCompare(o1.getLeader() != null ? o1.getLeader().getName() : "",
                                o2.getLeader() != null ? o2.getLeader().getName() : "", sortOrder);
                    }
                });

        comparatorFactories.put(TeamListItem.START_DATE,
                sortOrder -> new AbstractComparator<EdsDepartment>() {
                    public int compare(EdsDepartment o1, EdsDepartment o2) {
                        return internalCompare(o1.getStartDate(),
                                o2.getStartDate(), sortOrder);
                    }
                });

        comparatorFactories.put(TeamListItem.END_DATE,
                sortOrder -> new AbstractComparator<EdsDepartment>() {
                    public int compare(EdsDepartment o1, EdsDepartment o2) {
                        return internalCompare(o1.getEndDate(),
                                o2.getEndDate(), sortOrder);
                    }
                });

    }


    public NumberData generateDepartmentNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = departmentManager.getDepartmentLastIntNumber();
        if (settings != null && settings.getDepartmentNumberingFormat() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getDepartmentNumberingFormat(), settings.getDelimetrDepartmentNumbering(), null, null, null, "department");
            numberData.setDelimiter(settings.getDelimetrDepartmentNumbering());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_DEPARTMENT_PREFIX /*true*/);
        }
    }

    private static Map<String, Double> getDepartmentSearchFields() {
        final Map<String, Double> fields = new HashMap<>();
        fields.put(SolrDepartmentRepresenter.FIELD_NUMBER, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_LOCATION_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_CREATED_BY_NAME, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_TYPE_NAME, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_TYPE_NAME_UZ, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_TYPE_NAME_RU, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_TYPE_NAME_AR, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_TYPE_NAME_EN, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    @Override
    public void saveEmployeeDepartment(HashSet<Integer> teamMembers, Integer teamID, boolean isChecked, boolean indexToSolr, boolean setNullToEmployeePosition) {
        saveEmployeeDepartment(teamMembers, teamID, isChecked, indexToSolr, null, setNullToEmployeePosition);
    }

    @Override
    @Transactional
    public void saveEmployeeDepartment(HashSet<Integer> teamMembers, Integer teamID, boolean isChecked, boolean indexToSolr, Date startDate, boolean setNullToEmployeePosition) {
        EdsUser user = departmentManager.getUser();
        EdsDepartment department = isChecked ? departmentManager.get(teamID) : user.getCompany().getDefaultDepartment();
        Date currentDate = user.getCompany().getCompanyDate();
        teamMembers.forEach(id -> {
            EdsEmployee employee = employeeManager.get(id);
            EdsEmployeeDepartment employeeDepartment = employee.getEmployeeTeam();
            if (employeeDepartment != null && employeeDepartment.getTeam() != null) {
                if (isChecked && teamID.equals(employeeDepartment.getTeam().getObjectID())) {
                    return;
                } else {
                    Date endDate = ServerUtils.addDays(startDate != null ? startDate : currentDate, -1);
                    endDate = ServerUtils.getEndDate(endDate);
                    employeeDepartmentManager.deleteEmployeeDepartment(employeeDepartment, endDate);
                    baseEventPostProcessor.registerEvent(EmployeeDepartmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, employeeDepartment, user);
                }
            }

            EdsEmployeeDepartment newEmployeeDepartment = new EdsEmployeeDepartment();
            newEmployeeDepartment.setStartDate(startDate != null ? startDate : currentDate);
            newEmployeeDepartment.setTeam(department);
            newEmployeeDepartment.setEmployee(employee);
            employeeDepartmentManager.create(newEmployeeDepartment);
            employee.setEmployeeTeam(newEmployeeDepartment);
            if (setNullToEmployeePosition){
                employee.setPosition(null);
            }
            employeeManager.update(employee);
            if (employee.getEmployeeTeam() != null) {
                baseEventPostProcessor.registerEvent(EmployeeDepartmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, employee.getEmployeeTeam(), user, employeeDepartment);
            }
            if (indexToSolr) {
                EdsBusinessEvent edsBusinessEvent = baseEventPostProcessor.registerEvent(EmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employee, user);
                edsBusinessEvent.setSolrIndexed(true);
                try {
//                    solrManager.addEmployeeToIndex(employee);
                    employeeSolrComponent.index(employee);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TeamListItem getTeamForEdit(Integer objectId, String actionType) {
        TeamListItem teamItem = new TeamListItem();
        if (objectId != null) {
            EdsDepartment team = departmentManager.get(objectId);
            if (team != null) {
                teamItem.setObjectID(team.getObjectID());
                teamItem.setExternalGUID(team.getExternalGUID());
                teamItem.setName(team.getName());
                teamItem.setEmail(team.getEmail());
                teamItem.setActive(team.isActive());
                teamItem.setDescription(team.getDescription());
                teamItem.setDescriptionLocale(team.getDescriptionLocaleMap());
                teamItem.setShortDescription(team.getShortDescription());
                teamItem.setShortDescriptonLocale(team.getShortDescriptionLocaleMap());
                Long headCount = employeeManager.getEmployeesCountByDepartment(team);
                teamItem.setHeadCount(headCount != null ? headCount.toString() : "");
                SelectItem parentItem = departmentTreeManager.getParentItem(team.getObjectID());
                if (team.getDepartmentName() != null) {
                    teamItem.setDepartmentName(team.getDepartmentName().getAsSelectItem());
                    teamItem.setName(team.getDepartmentName().getAsSelectItem().getName());
                } else if (referenceManager.getByName(team.getName()) != null) {
                    teamItem.setDepartmentName(referenceManager.getByName(team.getName()).getAsSelectItem());
                }
                teamItem.setParentDepartment(parentItem);

                if (parentItem != null && parentItem.getId() != null && departmentManager.getDeparmentLocalization(parentItem.getId()) != null) {
                    teamItem.getParentDepartment().setName(departmentManager.getDeparmentLocalization(parentItem.getId())
                            .getLocaleByCode(ServerUtils.getUserLocale().getLanguage().toLowerCase()));
                }

                if (team.getLocale() != null) {
                    teamItem.setLocaleItem(team.getLocale().toRPC());
                }
                if (team.getLocation() != null) {
                    teamItem.setLocation(team.getLocation().getAsSelectItem());
                }
                if (team.getLeader() != null) {
                    teamItem.setLeaderId(team.getLeader().getObjectID());
                    String code = team.getLeader().getProfile() != null ? team.getLeader().getProfile().getEmployeeCode() : null;
                    teamItem.setLeader(code != null ? (code + " - " + team.getLeader().getFullName()) : team.getLeader().getFullName());
                } else if (team.getLeader() == null && team.getLeaderIsVacant() != null && team.getLeaderIsVacant()) {
                    teamItem.setLeader(commonLocalizer.localize("vacant", "Vacant"));
                    teamItem.setLeaderId(-1);
                }

                if (team.getLeader2() != null) {
                    teamItem.setLeaderId2(team.getLeader2().getObjectID());
                    String code = team.getLeader2().getProfile() != null ? team.getLeader2().getProfile().getEmployeeCode() : null;
                    teamItem.setLeader2(code != null ? (code + " - " + team.getLeader2().getFullName()) : team.getLeader2().getFullName());
                }
                if (team.getLeader3() != null) {
                    teamItem.setLeaderId3(team.getLeader3().getObjectID());
                    String code = team.getLeader3().getProfile() != null ? team.getLeader3().getProfile().getEmployeeCode() : null;
                    teamItem.setLeader3(code != null ? (code + " - " + team.getLeader3().getFullName()) : team.getLeader3().getFullName());
                }

                if (team.getLeader4() != null) {
                    teamItem.setLeaderId4(team.getLeader4().getObjectID());
                    String code = team.getLeader4().getProfile() != null ? team.getLeader4().getProfile().getEmployeeCode() : null;
                    teamItem.setLeader4(code != null ? (code + " - " + team.getLeader4().getFullName()) : team.getLeader4().getFullName());
                }

                if (team.getLeader5() != null) {
                    teamItem.setLeaderId5(team.getLeader5().getObjectID());
                    String code = team.getLeader5().getProfile() != null ? team.getLeader5().getProfile().getEmployeeCode() : null;
                    teamItem.setLeader5(code != null ? (code + " - " + team.getLeader5().getFullName()) : team.getLeader5().getFullName());
                }

                if (team.getNumberData() != null) {
                    NumberData numberData = new NumberData();
                    numberData.setFirstNumberString(team.getNumberData());
                    numberData.setNumberFormat("_");
                    teamItem.setNumberData(numberData);
                }

                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Department);
                teamItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(team.getCustomFields(), customFieldsItems));

                if (team.getCreator() != null) {
                    teamItem.setCreator(new SelectItem(team.getCreator().getObjectID(), team.getCreator().getName()));
                }
                teamItem.setStartDate(team.getStartDate() != null ? new Date(team
                        .getStartDate().getTime()) : null);
                teamItem.setEndDate(team.getEndDate() != null ? new Date(team
                        .getEndDate().getTime()) : null);
                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsDepartment.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.VIEW);
                kpiLog.setEntityId(objectId);
                ServerUtils.kpiLog(log, kpiLog, "View department");
            }
        } else {
            teamItem.setNumberData(generateDepartmentNumber());
        }
        teamItem.setDepartmentfId(referenceManager.getByCode(DEPARTMENT_TITLES).getObjectID());

        return teamItem;

    }

    public Boolean checkAccess(Integer departmentId, String permission) {
        EdsUser user = employeeManager.getUser();
        EdsDepartment team = departmentManager.get(departmentId);
        if (team.getLeader() != null && user.getObjectID().equals(team.getLeader().getObjectID())) {
            user.addArtificialRole(roleManager.getByCode(Constants.DLOFPR));
        }
        return ServerUtils.hasPermission(permission);

    }

    @Override
    public HashSet<String> getDepartmentSpecificPermissions(Integer departmentID, String sectionContext) {
        EdsUser user = employeeManager.getUser();
        EdsDepartment department = departmentManager.get(departmentID);
        if (department.getLeader() != null && user.getObjectID().equals(department.getLeader().getObjectID())) {
            user.addArtificialRole(roleManager.getByCode(Constants.DLOFPR));
        }
        return rolePermissionServiceLocal.getPermissionList(sectionContext, user);
    }

    public Integer createTeam(NewTeam newTeam) {
        EdsUser user = employeeManager.getUser();
        NumberData numberData = newTeam.getNumberData();
        boolean isMultiDepartmentLeader = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_DEPARTMENT_LEADER);
        EdsDepartment team = new EdsDepartment();
        if (newTeam.getLeader() != null && newTeam.getLeader() == -1) {
            team.setLeaderIsVacant(true);
        } else if (newTeam.getLeader() != null) {
            EdsEmployee teamLeader = employeeManager.get(newTeam.getLeader());
            team.setLeader(teamLeader);
            if (!teamLeader.hasEitherRoles(Constants.TL)) {
                teamLeader.addRole(roleManager.get(Constants.TL));
            }
        }
        if (isMultiDepartmentLeader) {
            if (newTeam.getLeader2() != null) {
                EdsEmployee teamLeader2 = employeeManager.get(newTeam.getLeader2());
                team.setLeader2(teamLeader2);
                if (!teamLeader2.hasEitherRoles(Constants.TL)) {
                    teamLeader2.addRole(roleManager.get(Constants.TL));
                }
            }
            if (newTeam.getLeader3() != null) {
                EdsEmployee teamLeader3 = employeeManager.get(newTeam.getLeader3());
                team.setLeader3(teamLeader3);
                if (!teamLeader3.hasEitherRoles(Constants.TL)) {
                    teamLeader3.addRole(roleManager.get(Constants.TL));
                }
            }
            if (newTeam.getLeader4() != null) {
                EdsEmployee teamLeader4 = employeeManager.get(newTeam.getLeader4());
                team.setLeader4(teamLeader4);
                if (!teamLeader4.hasEitherRoles(Constants.TL)) {
                    teamLeader4.addRole(roleManager.get(Constants.TL));
                }
            }
            if (newTeam.getLeader5() != null) {
                EdsEmployee teamLeader5 = employeeManager.get(newTeam.getLeader5());
                team.setLeader5(teamLeader5);
                if (!teamLeader5.hasEitherRoles(Constants.TL)) {
                    teamLeader5.addRole(roleManager.get(Constants.TL));
                }
            }
        }
        UUID externalGUID = UUID.randomUUID();
        team.setExternalGUID(externalGUID.toString());
        team.setName(newTeam.getName());
        team.setActive(newTeam.isActive());
        team.setEmail(newTeam.getEmail());
        team.setCreationTime(user.getCompany().getCompanyDate());
        team.setStartDate(newTeam.getStartDate());

        team.setDescription(newTeam.getDescription());
        team.setShortDescription(newTeam.getShortDescription());

        team.setDescriptionLocale(
                processLocale(newTeam.getDescriptionLocale())
        );

        team.setShortDescriptionLocale(
                processLocale(newTeam.getShortDescriptionLocale())
        );

        team.setCreator(user);
        if (newTeam.getLocation() != null) {
            team.setLocation(locationManager.get(newTeam.getLocation().getId()));
        }
        NumberData newNumberData = numberData;
        if (departmentManager.isDepartmentNumberExist(numberData.getNumberString(), null)) {
            newNumberData = this.generateDepartmentNumber();
        }
        if (newNumberData != null) {
            team.setIntNumber(newNumberData.getIntNumber());
            team.setNumberData(newNumberData.getNumberString());
        }
        EdsReference edsReference;
        if (newTeam.getDepartmentName() == null) {
            EdsReference referenceDePartmentTitles = referenceManager.getByCode("DEPARTMENT_TITLES");
            ReferenceItem parentReferenceItem = referenceDePartmentTitles != null ? referenceDePartmentTitles.getRPC() : null;
            ReferenceItem childReferenceItem = new ReferenceItem();
            childReferenceItem.setName(newTeam.getName());
            childReferenceItem.setParentID(parentReferenceItem.getObjectID());
            childReferenceItem.setParent(parentReferenceItem.getName());
            childReferenceItem.setParentCode(parentReferenceItem.getCode());

            Integer newTeamDepartmentId = allInOneService.saveReference(childReferenceItem, null, true);

            edsReference = referenceManager.get(newTeamDepartmentId);
        } else {
            edsReference = referenceManager.get(newTeam.getDepartmentName().getId());
        }
        team.setDepartmentName(edsReference);
        if (edsReference.getLocale() != null) {
            team.setLocale(edsReference.getLocale());
        }
        EdsDepartmentCustomFields customFields = createDepartmentCustomFields(newTeam.getCustomFieldItems());
        team.setCustomFields(customFields);
        departmentManager.create(team);
        if (newTeam.getParent() != null) {
            departmentTreeManager.addChildTree(team.getObjectID(), newTeam.getParent().getId());
        }
        baseEventPostProcessor.registerEvent(DepartmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, team, user);

        if (newTeam.getTeamMembers() != null && !newTeam.getTeamMembers().isEmpty()) {
            saveEmployeeDepartment(newTeam.getTeamMembers(), team.getObjectID(), true, true, false);
        }
        try {
            departmentSolrComponent.index(team);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        if (team.getObjectID() != null) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsDepartment.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(team.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Add new department");
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, team, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_DEPARTMENT);
        }
        return team.getObjectID();
    }

    @Override
    public void createTeamItems(List<NewTeam> teamList, List<TeamListItem> teamListItems) {
        if (teamList != null && teamList.size() > 0) {
            teamList.forEach(t -> {
                t.setNumberData(generateDepartmentNumber());
                createTeam(t);
            });
        } else if (teamListItems != null && teamListItems.size() > 0) {
            teamListItems.forEach(t -> {
                t.setNumberData(generateDepartmentNumber());
                updateTeam(t);
            });
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectMember[] getTeamEmployees(Integer teamId) {
        List<EdsEmployeeDepartment> list = employeeDepartmentManager.getTeamEmployees(teamId);

        ProjectMember[] departmentMembers = new ProjectMember[list.size()];
        int i = 0;
        for (EdsEmployeeDepartment employeeDepartment : list) {
            departmentMembers[i] = new ProjectMember(employeeDepartment.getObjectID(),
                    employeeDepartment.getEmployee().getName(), employeeDepartment.getTeam().getName());
            i++;
        }
        return departmentMembers;
    }

    public void addMembers(Integer teamId, Integer[] members) {
        EdsDepartment team = departmentManager.get(teamId);
        for (Integer member : members) {
            EdsEmployee employee = employeeManager.get(member);
            EdsEmployeeDepartment empDept = new EdsEmployeeDepartment(team, employee);
            employeeDepartmentManager.create(empDept);
            employee.setEmployeeDepartmentId(empDept.getObjectID());
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getTeamsList() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(EdsRole.DR);
        List<EdsDepartment> teams = departmentManager.list(fp);
        SelectItem[] result = new SelectItem[teams.size()];

        int i = 0;
        for (EdsDepartment team : teams) {
            result[i] = new SelectItem();
            result[i].setId(team.getObjectID());
            result[i].setName(team.getName());
            i++;
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getDepartmentParents() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(EdsRole.DR);
        List<EdsDepartment> teams = departmentManager.list(fp);
        SelectItem[] result = new SelectItem[teams.size()];

        int i = 0;
        for (EdsDepartment team : teams) {
            result[i] = new SelectItem();
            result[i].setId(team.getObjectID());
            result[i].setName(team.getName());
            i++;
        }
        return result;
    }

    public void updateTeam(TeamListItem editTeam) {
        try {
            EdsDepartment team = departmentManager.get(editTeam.getObjectID());
            boolean isMultiDepartmentLeader = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_DEPARTMENT_LEADER);
            if (team.getExternalGUID() == null) {
                UUID externalGUID = UUID.randomUUID();
                team.setExternalGUID(externalGUID.toString());
            }
            if (editTeam.getName() != null) {
                team.setName(editTeam.getName());
            }
            if (editTeam.getEmail() != null) {
                team.setEmail(editTeam.getEmail());
            }
            if (editTeam.isActive() != null) {
                team.setActive(editTeam.isActive());
            }
            if (editTeam.getDescription() != null) {
                team.setDescription(editTeam.getDescription());
            }
            if (editTeam.getDescriptionLocale() != null) {
                EdsReferenceLocale descriptionLocale = team.getDescriptionLocale();
                if (descriptionLocale != null) {
                    team.setDescriptionLocale(processLocale(descriptionLocale, editTeam.getDescriptionLocale()));
                }else {
                    team.setDescriptionLocale(processLocale(editTeam.getDescriptionLocale()));
                }
            }
            if (editTeam.getShortDescription() != null) {
                team.setShortDescription(editTeam.getShortDescription());
            }
            if (editTeam.getShortDescriptonLocale() != null) {
                EdsReferenceLocale shortDescriptionLocale = team.getShortDescriptionLocale();
                if (shortDescriptionLocale != null) {
                    team.setShortDescriptionLocale(processLocale(shortDescriptionLocale, editTeam.getShortDescriptonLocale()));
                }else {
                    team.setShortDescriptionLocale(processLocale(editTeam.getShortDescriptonLocale()));
                }
            }

            if (editTeam.getStartDate() != null) {
                team.setStartDate(editTeam.getStartDate());
            }
            if (editTeam.getEndDate() != null) {
                team.setEndDate(editTeam.getEndDate());
            }

            EdsDepartmentCustomFields customFields = createDepartmentCustomFields(editTeam.getCustomFieldItems());
            team.setCustomFields(customFields);

            if (editTeam.getNumberData() != null) {
                team.setNumberData(editTeam.getNumberData().getNumberString());
            }

            if (editTeam.getLocation() != null) {
                team.setLocation(locationManager.get(editTeam.getLocation().getId()));
            }

            EdsReference edsReference = referenceManager.get(editTeam.getDepartmentName().getId());
            team.setDepartmentName(edsReference);
            if (edsReference.getLocale() != null) {
                team.setLocale(edsReference.getLocale());
            }

            Integer teamParentId = departmentTreeManager.getParent(team.getObjectID());
            if (editTeam.getParentDepartment() != null) {
                if (teamParentId != null && !teamParentId.equals(editTeam.getParentDepartment().getId())) {
                    departmentTreeManager.removeSubtreeFromParents(team.getObjectID());
                }
                if (teamParentId == null || !teamParentId.equals(editTeam.getParentDepartment().getId())) {
                    departmentTreeManager.addChildTree(team.getObjectID(), editTeam.getParentDepartment().getId());
                }
            } else if (teamParentId != null && editTeam.getParentDepartment() == null) {
                departmentTreeManager.removeSubtreeFromParents(team.getObjectID());
            }

            if (editTeam.getLeaderId() != null && editTeam.getLeaderId() == -1) {
                team.setLeaderIsVacant(true);
                team.setLeader(null);
                team.setLeaderId(null);
            } else if (editTeam.getLeaderId() != null) {
                EdsEmployee teamLeader = employeeManager.get(editTeam.getLeaderId());
                team.setLeader(teamLeader);
                roleManager.addRole(teamLeader, EdsRole.TL);
            }

            if (isMultiDepartmentLeader) {
                if (editTeam.getLeaderId2() != null) {
                    EdsEmployee teamLeader2 = employeeManager.get(editTeam.getLeaderId2());
                    team.setLeader2(teamLeader2);
                    if (!teamLeader2.hasEitherRoles(Constants.TL)) {
                        roleManager.addRole(teamLeader2, EdsRole.TL);
                    }
                }

                if (editTeam.getLeaderId3() != null) {
                    EdsEmployee teamLeader3 = employeeManager.get(editTeam.getLeaderId3());
                    team.setLeader3(teamLeader3);
                    if (!teamLeader3.hasEitherRoles(Constants.TL)) {
                        roleManager.addRole(teamLeader3, EdsRole.TL);
                    }
                }

                if (editTeam.getLeaderId4() != null) {
                    EdsEmployee teamLeader4 = employeeManager.get(editTeam.getLeaderId4());
                    team.setLeader4(teamLeader4);
                    if (!teamLeader4.hasEitherRoles(Constants.TL)) {
                        roleManager.addRole(teamLeader4, EdsRole.TL);
                    }
                }

                if (editTeam.getLeaderId5() != null) {
                    EdsEmployee teamLeader5 = employeeManager.get(editTeam.getLeaderId5());
                    team.setLeader5(teamLeader5);
                    if (!teamLeader5.hasEitherRoles(Constants.TL)) {
                        roleManager.addRole(teamLeader5, EdsRole.TL);
                    }
                }
            }
            if (editTeam.getUnSelectedEmployees() != null) {
                HashSet<Integer> ids = new HashSet<>(editTeam.getUnSelectedEmployees());
                saveEmployeeDepartment(ids, editTeam.getObjectID(), false, true,false);
            }
            departmentManager.update(team);

            try {
                departmentSolrComponent.index(team);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SolrServerException e) {
                e.printStackTrace();
            }

            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsDepartment.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(team.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Update department");
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, team, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_DEPARTMENT);
        } catch (Throwable t) {

            throw new RuntimeException(t);
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<TeamListItem> getTeams(ListingFilterParameter fp) {
        Page<DepartmentSolrDoc> departments = departmentSolrComponent.getList(fp);
        return getDepartmentsFromSolrResult(departments, fp);
    }

    private ListResult<TeamListItem> getDepartmentsFromSolrResult(Page<DepartmentSolrDoc> departmentSolrDocList, ListingFilterParameter fp) {
        ArrayList<TeamListItem> items = new ArrayList<>();
        ListPanelToolRpc panelSettings = fp.getListPanelTool();
        int totalCount = 0;
        if (departmentSolrDocList != null) {
            totalCount = (int) departmentSolrDocList.getTotalElements();
            String lang = ServerUtils.getUserLocale().getLanguage();
            for (DepartmentSolrDoc relevantDoc : departmentSolrDocList.getContent()) {
                TeamListItem item = new TeamListItem();
                item.setObjectID(relevantDoc.getDepartmentId());
                item.setNumberData(new NumberData(relevantDoc.getNumber(), -1));
                item.setDepartmentCode(relevantDoc.getNumber());
                String name = null, parentName = null;
                if (lang != null && !lang.isEmpty()) {
                    switch (lang) {
                        case "uz" -> {
                            name = relevantDoc.getNameUz();
                            parentName = relevantDoc.getParentDepartmentNameUz();
                        }
                        case "ru" -> {
                            name = relevantDoc.getNameRu();
                            parentName = relevantDoc.getParentDepartmentNameRu();
                        }
                        case "en" -> {
                            name = relevantDoc.getNameEn();
                            parentName = relevantDoc.getParentDepartmentNameEn();
                        }
                        case "ar" -> {
                            name = relevantDoc.getNameAr();
                            parentName = relevantDoc.getParentDepartmentNameAr();
                        }
                        default -> {
                            name = relevantDoc.getName();
                            parentName = relevantDoc.getParentDepartmentName();
                        }
                    }
                }
                item.setName(name == null ? relevantDoc.getName() : name);

                if (relevantDoc.getLocationId() != null) {
                    item.setLocation(new SelectItem(relevantDoc.getLocationId(), relevantDoc.getLocationName()));
                }
                if (relevantDoc.getLeaderId() != null) {
                    item.setLeader(relevantDoc.getLeaderName());
                } else if (relevantDoc.getLeaderIsVacant()) {
                    item.setLeader(commonLocalizer.localize("vacant", "Vacant"));
                }
                if (relevantDoc.getStartDate() != null) {
                    item.setStartDate(relevantDoc.getStartDate());
                }
                item.setEncryptedID(relevantDoc.getEncryptedId());
                item.setHeadCount(relevantDoc.getHeadCount());
                item.setActive(relevantDoc.getStatusName());

                if (relevantDoc.getParentDepartmentId() != null) {
                    item.setParentDepartment(new SelectItem(relevantDoc.getParentDepartmentId(), parentName == null ? relevantDoc.getParentDepartmentName() : parentName));
                }

                item.setCreatedBy(relevantDoc.getCreatedByName());
                item.setCreatedDate(relevantDoc.getCreatedDate());
                item.setModifiedBy(relevantDoc.getModifiedByName());
                item.setModifiedDate(relevantDoc.getModifiedDate());
                if (panelSettings != null) {
                    item.setCustomFieldValuesItems(CustomFieldsUtils.getBaseSolrDocDynamicFields(relevantDoc, panelSettings.getColumnCodeName()));
                }
                items.add(item);
            }
        }
        return new ListResult<>(items, totalCount);
    }

//    @Transactional
//    public SolrQuery getSolrQueryForDepartment(ListingFilterParameter fp) {
//        EdsUser user = userManager.getUser();
//        EdsCompany company = user.getCompany();
//
//        FacetFilterRpc departmentFacetFilter = fp.getFacetFilter();
//        if (departmentFacetFilter != null && !departmentFacetFilter.isFilterChanges()) {
//            departmentFacetFilter = commonServiceLocal.getUserFacetFilter(departmentFacetFilter);
//        }
//        if (fp.getLocationId() == null) {
//            fp.setLocationId(user.getLocation() != null ? user.getLocation().getObjectID() : null);
//        }
//
//        SolrQuery query = new SolrQuery();
//        query.setQuery(QueryBuilderForSolr.getDepartmentListCore(fp, user, company) + SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(departmentFacetFilter, company, null, null));
//        query.setStart(fp.getStart());
//        query.setParam(CommonParams.ROWS, String.valueOf(fp.getLimit()));
//
//        if (!fp.isSearchButton()) {
//            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
//                boolean desc = Constants.DESC == fp.asConfig().getSortDir();
//                if (TeamListItem.NAME.equals(fp.getSortField())) {
//                    query.setSort(SolrDepartmentRepresenter.SORTABLE_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (TeamListItem.CODE.equals(fp.getSortField())) {
//                    query.setSort(SolrDepartmentRepresenter.SORTABLE_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (TeamListItem.STATUS.equals(fp.getSortField())) {
//                    query.setSort(SolrDepartmentRepresenter.SORTABLE_STATUS_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                }  else if (TeamListItem.LOCATION.equals(fp.getSortField())) {
//                    query.setSort(SolrDepartmentRepresenter.SORTABLE_LOCATION_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                }  else if (TeamListItem.CREATED_DATE.equals(fp.getSortField())) {
//                    query.setSort(SolrDepartmentRepresenter.SORTABLE_CREATED_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (TeamListItem.CREATED_BY.equals(fp.getSortField())) {
//                    query.setSort(SolrDepartmentRepresenter.SORTABLE_CREATED_BY_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (TeamListItem.MODIFIED_DATE.equals(fp.getSortField())) {
//                    query.setSort(SolrDepartmentRepresenter.SORTABLE_MODIFIED_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (TeamListItem.MODIFIED_BY.equals(fp.getSortField())) {
//                    query.setSort(SolrDepartmentRepresenter.SORTABLE_MODIFIED_BY_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                }
//            } else {
//                query.setSort(SolrDepartmentRepresenter.SORTABLE_NUMBER, SolrQuery.ORDER.desc);
//            }
//        }
//        return query;
//    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getDepartmentFacetQuery(final ListingFilterParameter fp, final Integer companyId) {
        final StringBuffer sql = new StringBuffer();
        sql.append(SolrDepartmentRepresenter.FIELD_COMPANY_ID).append(":").append(companyId);

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND ").append(SolrDepartmentRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey()));
            if (!fp.isLookUp()) {
                final SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(sql, getDepartmentSearchFields(), fp.getSearchKey());
            }
            sql.append(")");
        }
        return sql.toString();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectMember[] getCompanyEmployees() {
        List<EdsEmployee> employees = employeeManager.getUnAssignedEmployees();
        ProjectMember[] projectMembers = new ProjectMember[employees.size()];
        int i = 0;
        for (EdsEmployee employee : employees) {
            if (employee == null || employee.getName() == null) {
                continue;
            }
            projectMembers[i] = new ProjectMember(employee.getObjectID(),
                    employee.getName(), "");
            i++;
        }
        return projectMembers;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TeamListItem getTeam(Integer departmentId) {
        TeamListItem teamListItem = new TeamListItem();
        EdsDepartment department = departmentManager.get(departmentId);
        teamListItem.setName(department.getName());
        teamListItem.setEmail(department.getEmail());
        teamListItem.setDescription(department.getDescription());
        teamListItem.setDescriptionLocale(department.getShortDescriptionLocaleMap());
        teamListItem.setShortDescription(department.getShortDescription());
        teamListItem.setShortDescriptonLocale(department.getShortDescriptionLocaleMap());
        teamListItem.setLeader(department.getLeader() != null ? department.getLeader().getName() : "N/A");
        teamListItem.setStartDate(department.getStartDate());
        teamListItem.setEndDate(department.getEndDate());
        if (department.getDepartmentName() != null) {
            teamListItem.setDepartmentNameid(department.getDepartmentName().getObjectID());
        }
        teamListItem.setEncryptedID(EncryptionHelper.encrypt("department/" + department.getObjectID().toString()));
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Department);
        teamListItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(department.getCustomFields(), customFieldsItems));
        if (department.getCreator() != null) {
            teamListItem.setCreator(new SelectItem(department.getCreator().getObjectID(), department.getCreator().getName()));
        }
        SelectItem parentItem = departmentTreeManager.getParentItem(department.getObjectID());
        teamListItem.setParentDepartment(parentItem);
        if (parentItem != null && parentItem.getId() != null && departmentManager.getDeparmentLocalization(parentItem.getId()) != null) {
            teamListItem.getParentDepartment().setName(departmentManager.getDeparmentLocalization(parentItem.getId())
                    .getLocaleByCode(ServerUtils.getUserLocale().getLanguage().toLowerCase()));
        }

        return teamListItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DepartmentItem[] getDepartmentsSelectItem() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(EdsRole.DR);
        List<EdsDepartment> list = departmentManager.list(fp);
        EdsUser user = employeeManager.getUser();
        Integer defaultDepartment = user.getCompany().getDefaultDepartment().getObjectID();
        DepartmentItem[] departmens = new DepartmentItem[list.size()];
        int i = 0;
        for (EdsDepartment department : list) {
            departmens[i] = new DepartmentItem();
            departmens[i].setDepartmentName(department.getName());
            departmens[i].setDepatmentID(department.getObjectID());
            departmens[i].setDefaultDepartmentId(defaultDepartment);
            i++;
        }

        return departmens;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getDepartmentsAsSelectItem(ListingFilterParameter fp) {
        EdsUser user = employeeManager.getUser();
        EdsRole maximumRole = user.getRolesSortedByPattern().get(0);
        if (fp.getEmployeeId() != null || !ServerUtils.hasPermission(PermissionConstants.HRMS_SEE_ALL_DEPARTMENT_LIST)) {
            EdsDepartment department = fp.getEmployeeId() != null ? employeeManager.get(fp.getEmployeeId()).getTeam() : employeeManager.get(user.getObjectID()).getTeam();
            if (department != null) {
                return new SelectItem[]{department.getAsSelectItem()};
            } else {
                return new SelectItem[0];
            }
        }
        fp.setViewAsId(maximumRole.getObjectID());
        fp.setAscending(false);
        fp.setSortField(TeamListItem.NAME);
        List<EdsDepartment> list = departmentManager.list(fp);
        SelectItem[] departments = new SelectItem[list.size()];
        int i = 0;
        for (EdsDepartment department : list) {
            departments[i] = department.getAsSelectItem();
            i++;
        }
        return departments;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean getNotRemoveDefaultDepartment(Integer departmentId) {
        EdsUser user = employeeManager.getUser();
        Integer defaultDepartment = user.getCompany().getDefaultDepartment().getObjectID();
        return defaultDepartment.equals(departmentId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getTeamName(Integer departmentId) {
        EdsDepartment department = departmentManager.get(departmentId);
        return department.getName();
    }

    public Boolean deleteDepartment(Integer departmentId, Integer newDepartmentId) {
        EdsUser user = employeeManager.getUser();
        EdsDepartment defaultTeam = user.getCompany().getDefaultDepartment();
        if (defaultTeam != null && defaultTeam.getObjectID().equals(departmentId) || !ServerUtils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_REMOVE)) {
            return false;
        }
        EdsDepartment department = departmentManager.get(departmentId);
        EdsDepartment newDepartment = null;
        if (newDepartmentId != null) {
            newDepartment = departmentManager.get(newDepartmentId);
        }


        department.setUpdater(user);
        department.setLastUpdateTime(new Date());

        List<EdsEmployeeDepartment> edList = employeeDepartmentManager.getTeamEmployees(department.getObjectID());
        HashSet<Integer> ids = edList.stream()
                .map(x -> x.getEmployee().getObjectID())
                .collect(Collectors.toCollection(HashSet::new));

        if (newDepartment != null && !ids.isEmpty()) {
            saveEmployeeDepartment(ids, newDepartment.getObjectID(), true, true,true); // emplotyee position will be set null
        }
        //delete role team
        if (department.getLeader() != null) {
            departmentManager.updateTeamRole(department.getLeader().getObjectID());
        }
        //delete team
        EdsReference departmentName = department.getDepartmentName();
        if (departmentName != null) {
            departmentName.setLocale(null);
            referenceManager.update(departmentName);
        }
        if (department.getLocale() != null) {
            referenceLocaleManager.delete(department.getLocale());
            department.setLocale(null);
        }

        List<EdsGoal> departmentGoals = goalManager.getDepartmentGoalsByDepartment(departmentId);

        for (EdsGoal goal : departmentGoals) {
            goalManager.deleteGoal(goal);
            goalAssigneesManager.deleteGoalAssignees(goal.getObjectID());
            employeeMetricHistoryManager.deleteEmployeeMetricHistoriesByDepartmentGaolId(goal.getObjectID());
        }

        departmentManager.deleteTeam(department);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsDepartment.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(departmentId);
        ServerUtils.kpiLog(log, kpiLog, "Delete department");
        try {
            solrManager.removeDepartment(departmentId, SecurityContext.getCompanyID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, department, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_DEPARTMENT);
        return true;
    }

    public void runMindsharePatch(Integer companyID, int oldEmployeeDepartmentID, int newEmployeeDepartmentID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID.toString());
        EdsEmployeeDepartment oldEmployeeDepartment = employeeDepartmentManager.get(oldEmployeeDepartmentID);
        EdsEmployeeDepartment newEmployeeDepartment = employeeDepartmentManager.get(newEmployeeDepartmentID);

        projectEmployeeManager.deleteAndCreateProjectEmployee(oldEmployeeDepartment, newEmployeeDepartment);
    }

    public void runMindshareCleanup(Integer companyID, Date start, ArrayList<Integer> employeeIDs, Integer oldDepartmentID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID.toString());
        Calendar startDate = new GregorianCalendar();
        startDate.setTime(start);
        startDate.set(Calendar.AM_PM, 0);
        startDate.set(Calendar.HOUR, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        Calendar endDate = new GregorianCalendar();
        endDate.set(Calendar.AM_PM, 0);
        endDate.set(Calendar.HOUR, 23);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND, 59);
        endDate.set(Calendar.MILLISECOND, 0);

        for (Integer employeeID : employeeIDs) {
            System.out.println("Cleanup for employee: " + employeeID);
            EdsEmployee employee = employeeManager.get(employeeID);
            List<EdsEmployeeTask> taskList = employeeTaskManager.listDueTasks(employee, startDate.getTime(), endDate.getTime(), new ListingFilterParameter());
            Map<String, EdsEmployeeTask> undeletedEmployeeTasks = new HashMap<>();
            for (EdsEmployeeTask employeeTask : taskList) {
                if (undeletedEmployeeTasks.containsKey(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID() + "/" + employeeTask.getTask().getObjectID())) {
                    continue;
                }
                undeletedEmployeeTasks.put(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID() + "/" + employeeTask.getTask().getObjectID(), employeeTask);
            }
            System.out.println("Employee Tasks: " + undeletedEmployeeTasks.size());
            List<EdsTimeSheet> timesheets = timesheetManager.list(employee, startDate.getTime(), endDate.getTime());
            System.out.println("Timesheet For Edit: " + timesheets.size());
            for (EdsTimeSheet timesheet : timesheets) {
                if (timesheet.getEmployeeTask().getDeleted()) {
                    if (undeletedEmployeeTasks.containsKey(timesheet.getEmployeeID() + "/" + timesheet.getTaskID()) && timesheet.getTeamID().equals(oldDepartmentID)) {
                        EdsEmployeeTask employeeTask = undeletedEmployeeTasks.get(timesheet.getEmployeeID() + "/" + timesheet.getTaskID());
                        System.out.println("Old ED ID: " + timesheet.getEmployeeTask().getObjectID() + " New ED ID: " + employeeTask.getObjectID());
                        System.out.println("Old Dept ID: " + timesheet.getTeamID() + " New Dept: " + employeeTask.getProjectEmployee().getEmployeeDepartment().getTeam().getObjectID());
                        timesheet.setEmployeeTask(employeeTask);
                        timesheet.setTeamID(employeeTask.getProjectEmployee().getEmployeeDepartment().getTeam().getObjectID());
                    }
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getDepartmentByNameAndId(String name, Integer objectId) {
        List<EdsDepartment> departmentName = departmentManager.getDepartmentByNameAndId(name, objectId);
        return departmentName.size();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getDepartmentByCodeAndId(String code, Integer objectId) {
        List<EdsDepartment> departmentCode = departmentManager.getDepartmentByCodeAndId(code, objectId);
        return departmentCode.size();
    }

    private Integer maxDepth = 0;

    @Override
    public Integer maxChildLevels() {
        maxDepth = 1;
        maxChildLevels(null);
        return maxDepth;
    }

    public void maxChildLevels(ChartNode node) {
        List<ChartNode> teamNodes;
        if (node == null) {
            teamNodes = departmentTreeManager.getTeamGraph(null, null);
        } else {
            teamNodes = node.getChildren();
        }
        for (ChartNode tn : teamNodes) {
            if (node != null && tn.getDepth() > maxDepth) {
                maxDepth = tn.getDepth();
            }
            maxChildLevels(tn);
        }

    }

    @Override
    public void saveCustomizationOrgChart(LinkedList<SelectItem> items, LinkedList<String> colorItems) {
        int i = 0;
        for (SelectItem item : items) {
            List<EdsDepartmentTree> edsDepartmentTreeList = departmentTreeManager.getDepartmentTreeByDepartmentID(item.getId());
            if (edsDepartmentTreeList != null && edsDepartmentTreeList.size() > 0) {
                for (EdsDepartmentTree treeItem : edsDepartmentTreeList) {
                    treeItem.setSorder(i);
                    departmentTreeManager.update(treeItem);
                }
            }
            i++;
        }
    }

    @Override
    public SelectItem[] getDepartmentsForCustomization(Integer parentId) {
        LinkedList<SelectItem> departments = departmentTreeManager.getDepartments(parentId);
        return departments.toArray(new SelectItem[]{});
    }

    private Integer parentChildSize = 2;

    private boolean showAll = false;
    private int selectedTeamDepth = 0;

    @Override
    public String getTeamGraphChart(boolean isShowView, Integer levelOptionList, boolean isFromUI, Integer parentId, Integer nodeId, Integer showAllId, boolean showExternalEmployee, Integer locationId, boolean fromResetButton) {
        List<ChartNode> teamNodes;
        String departmentLocale = referenceWfmMessageSource.localize("setPrentDepartment", "Set parent department");
        String vacantLocale = commonLocalizer.localize("vacant", "Vacant");
        String employeeLocale = referenceWfmMessageSource.localize("employeesOrgChart", "Employees");
        boolean departmentsHasLocation = true;
        if (!departmentManager.hasDepartmentsWithLocation()) {
            departmentsHasLocation = false;
            teamNodes = departmentTreeManager.getTeamGraph(parentId, null);
        } else if (locationId != null) {
            teamNodes = departmentTreeManager.getTeamGraph(parentId, locationId);
        } else {
            teamNodes = locationServiceLocal.getLocationNodes();
        }

        StringBuilder htmlTeamOrgChart = new StringBuilder();
        LinkedHashMap<Integer, Integer> noteChildCountMap = new LinkedHashMap<>();
        Map<Integer, Integer> nomeEmployeesCount = locationId != null || !departmentsHasLocation ? employeeManager.getTeamEmployeesCount() : departmentManager.getLocationAndTeamSize();
        parentChildSize = 2;
        EdsReference typeExternal = referenceManager.getByCode("TYPE_EXTERNAL");
        Integer typeExternalId = null;
        if (typeExternal != null) {
            typeExternalId = typeExternal.getObjectID();
        }
        for (ChartNode node : teamNodes) {
            calculateChildCounts(node, nomeEmployeesCount, noteChildCountMap);
            List<ChartNode> children = node.getChildren();
            Integer childrenSize = children.size();
            Integer employeeCount = nomeEmployeesCount.get(node.getId()) != null ? nomeEmployeesCount.get(node.getId()) : 0;
            Integer totalChildSize = noteChildCountMap.get(node.getId());
            String depthLevel = "level--1";


            if (showAllId != null) {
                if (!showAll && showAllId.equals(node.getId())) {
                    showAll = true;
                } else if (node.getDepth() <= selectedTeamDepth) {
                    showAll = false;
                }
            }

            children.sort(Comparator.comparing(ChartNode::getSorder));
            int colspan = children.size() * 2;
            // Tree Team Root
            getParentChildSize(node);
            htmlTeamOrgChart.append("<table class=\"nodeTable--ext hide-data ").append((showAllId != null && showAll && totalChildSize > 0) || (nodeId != null && nodeId.equals(node.getId()) && employeeCount > 0) ? "has-subNodes " : "").append("\" id=\"table-").append(node.getId()).append("\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tbody>");
            htmlTeamOrgChart.append(OrgChartUtils.getTeamNodesRow(colspan, 0, node.getId(), node.getName(), node.getDesc(), null,
                    null, departmentLocale,
                    isShowView, levelOptionList, isFromUI, childrenSize, locationId != null || !departmentsHasLocation ? totalChildSize : childrenSize, employeeCount, parentChildSize, nodeId, showAllId, depthLevel, locationId, departmentsHasLocation, nomeEmployeesCount));


            if (nodeId != null && nodeId.equals(node.getId())) {
                Map<Integer, SelectItem> resultMap = employeeManager.getTeamEmployeesForOrgChart(node.getId(), showExternalEmployee, typeExternalId);
                resultMap.forEach((id, item) -> {
                    if (StringUtils.isBlank(item.getNumber())) {
                        resultMap.get(id).setNumber("");
                    } else {
                        resultMap.get(id).setNumber(commonService.getImageUrl(Integer.parseInt(item.getNumber())));
                    }
                });

                if (resultMap.size() > 0) {
                    addAvailablePositionCount(resultMap, node.getId());
                    htmlTeamOrgChart.append(OrgChartUtils.getTeamTLinesRowEmployee(colspan, 0, isShowView, childrenSize, parentChildSize));
                    htmlTeamOrgChart.append(OrgChartUtils.getTeamEmployeeNodes(colspan, 0, node.getName(), node.getLeaderId(), vacantLocale, resultMap, employeeLocale, isShowView, levelOptionList, childrenSize, parentChildSize));
                }
            }


            if (showAll) {
                Map<Integer, SelectItem> resultMap = employeeManager.getTeamEmployeesForOrgChart(node.getId(), showExternalEmployee, typeExternalId);
                resultMap.forEach((id, item) -> {
                    if (StringUtils.isBlank(item.getNumber())) {
                        resultMap.get(id).setNumber("");
                    } else {
                        resultMap.get(id).setNumber(commonService.getImageUrl(Integer.parseInt(item.getNumber())));
                    }
                });

                if (resultMap.size() > 0) {
                    addAvailablePositionCount(resultMap, node.getId());
                    htmlTeamOrgChart.append(OrgChartUtils.getTeamTLinesRowEmployee(colspan, 0, isShowView, childrenSize, parentChildSize));
                    htmlTeamOrgChart.append(OrgChartUtils.getTeamEmployeeNodes(colspan, 0, node.getName(), node.getLeaderId(),
                            vacantLocale, resultMap,
                            employeeLocale,
                            isShowView, levelOptionList, childrenSize, parentChildSize));
                }
            }

            if (!children.isEmpty()) {
                htmlTeamOrgChart.append(OrgChartUtils.getTeamTLinesRow(colspan, 0, isShowView, childrenSize, parentChildSize));
                htmlTeamOrgChart.append(OrgChartUtils.getTeamVerticalLineRow(colspan, 0, isShowView, childrenSize, parentChildSize));
                htmlTeamOrgChart.append("<tr>");
                for (ChartNode child : children) {
                    child.setDepth(child.getDepth() - (node.getDepth() < child.getDepth() ? node.getDepth() - 1 : 0));
                    htmlTeamOrgChart.append("<td ").append("class='nodeSingleChildCell' ").append(" colspan=\"2\">").append(drawTeamChart(child, isShowView, levelOptionList, isFromUI, noteChildCountMap, parentChildSize, nodeId, showAllId, showExternalEmployee, locationId, departmentsHasLocation, nomeEmployeesCount)).append("</td>");
                }
                htmlTeamOrgChart.append("</tr>");
            }
            htmlTeamOrgChart.append("</tbody></table>" + "<br/>" + "<br/>");
        }
        this.showAll = false;
        this.selectedTeamDepth = 0;
        return htmlTeamOrgChart.toString();
    }

    private void getParentChildSize(ChartNode newDepth) {
        if (newDepth.getChildren() != null && newDepth.getChildren().size() > 0 && newDepth.getChildren().size() < 2) {
            this.parentChildSize = this.parentChildSize + 1;
            getParentChildSize(newDepth.getChildren().get(0));
        }
    }

    private void addAvailablePositionCount(Map<Integer, SelectItem> resultMap, Integer departmentId) {
        for (SelectItem selectItem : employeeManager.getAvailablePositionsForOrgChart(departmentId)) {
            resultMap.put(selectItem.getId(), selectItem);
        }
    }

    private String drawTeamChart(ChartNode node, boolean isShowView, Integer levelOptionList, boolean isFromUI, LinkedHashMap<Integer, Integer> noteChildCountMAp, Integer parentChildSize, Integer nodeId, Integer showAllId, boolean showExternalEmployee, Integer locationId, boolean departmentsHasLocation, Map<Integer, Integer> locationSizeMap) {
        List<ChartNode> children = node.getChildren();
        Integer childrenSize = children.size();
        Integer totalChildSize = noteChildCountMAp.get(node.getId());
        EdsReference typeExternal = referenceManager.getByCode("TYPE_EXTERNAL");
        Integer typeExternalId = null;
        if (typeExternal != null) {
            typeExternalId = typeExternal.getObjectID();
        }
        Map<Integer, SelectItem> resultMa2 = employeeManager.getTeamEmployeesForOrgChart(node.getId(), showExternalEmployee, typeExternalId);
        int employeeCount = resultMa2.size();
        String depthLevel = "level--" + node.getDepth();


        if (showAllId != null) {
            if (!showAll && showAllId.equals(node.getId())) {
                showAll = true;
                selectedTeamDepth = node.getDepth();
            } else if (node.getDepth() <= selectedTeamDepth) {
                showAll = false;
            }
        }

        children.sort(Comparator.comparing(ChartNode::getSorder));
        int colspan = children.size() * 2;
        StringBuilder htmlTeamOrgChart = new StringBuilder("<table class=\"nodeTable--int hide-data " + ((showAllId != null && showAll && totalChildSize > 0) || (nodeId != null && nodeId.equals(node.getId()) && employeeCount > 0) ? "has-subNodes " : "") + "\" id=\"table-" + node.getId() + "\" \" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tbody>");
        htmlTeamOrgChart.append(OrgChartUtils.getTeamNodesRow(colspan, node.getDepth(), node.getId(), node.getName(), node.getDesc(),
                node.getParent() != null ? node.getParent().getId() : null,
                node.getParent() != null ? node.getParent().getName() : null,
                referenceWfmMessageSource.localize("setPrentDepartment", "Set parent department"),
                isShowView, levelOptionList, isFromUI, childrenSize, locationId != null || !departmentsHasLocation ? totalChildSize : childrenSize, employeeCount, parentChildSize, nodeId, showAllId, depthLevel, locationId, departmentsHasLocation, locationSizeMap));

        if (nodeId != null && nodeId.equals(node.getId())) {
            Map<Integer, SelectItem> resultMap = employeeManager.getTeamEmployeesForOrgChart(node.getId(), showExternalEmployee, typeExternalId);
            resultMap.forEach((id, item) -> {
                if (StringUtils.isBlank(item.getNumber())) {
                    resultMap.get(id).setNumber(null);
                } else {
                    resultMap.get(id).setNumber(commonService.getImageUrl(Integer.parseInt(item.getNumber())));
                }
            });

            if (resultMap.size() > 0) {
                addAvailablePositionCount(resultMap, node.getId());
                htmlTeamOrgChart.append(OrgChartUtils.getTeamTLinesRowEmployee(colspan, node.getDepth(), isShowView, childrenSize, parentChildSize));
                htmlTeamOrgChart.append(OrgChartUtils.getTeamEmployeeNodes(colspan, node.getDepth(), node.getName(), node.getLeaderId(),
                        commonLocalizer.localize("vacant", "Vacant"), resultMap,
                        referenceWfmMessageSource.localize("employeesOrgChart", "Employees"),
                        isShowView, levelOptionList, childrenSize, parentChildSize));
            }
        }


        if (showAll) {
            Map<Integer, SelectItem> resultMap = employeeManager.getTeamEmployeesForOrgChart(node.getId(), showExternalEmployee, typeExternalId);
            resultMap.forEach((id, item) -> {
                if (StringUtils.isBlank(item.getNumber())) {
                    resultMap.get(id).setNumber(null);
                } else {
                    resultMap.get(id).setNumber(commonService.getImageUrl(Integer.parseInt(item.getNumber())));
                }
            });

            if (resultMap.size() > 0) {
                addAvailablePositionCount(resultMap, node.getId());
                htmlTeamOrgChart.append(OrgChartUtils.getTeamTLinesRowEmployee(colspan, node.getDepth(), isShowView, childrenSize, parentChildSize));
                htmlTeamOrgChart.append(OrgChartUtils.getTeamEmployeeNodes(colspan, node.getDepth(), node.getName(), node.getLeaderId(),
                        commonLocalizer.localize("vacant", "Vacant"), resultMap,
                        referenceWfmMessageSource.localize("employeesOrgChart", "Employees"),
                        isShowView, levelOptionList, childrenSize, parentChildSize));
            }
        }

        if (node.isRoot() || (levelOptionList != null && node.getDepth() >= levelOptionList)) {
            return (htmlTeamOrgChart.append("</tbody></table>").toString());
        } else {
            Integer depth = node.getDepth();
            if (!children.isEmpty()) {
                htmlTeamOrgChart.append(OrgChartUtils.getTeamTLinesRow(colspan, node.getDepth(), isShowView, childrenSize, parentChildSize));
                htmlTeamOrgChart.append(OrgChartUtils.getTeamVerticalLineRow(colspan, node.getDepth(), isShowView, childrenSize, parentChildSize));
            }
            if (isShowView && depth >= parentChildSize) {
                for (ChartNode childNode : node.getChildren()) {
                    htmlTeamOrgChart.append("<tr class='nextLevelRow ").append(node.getChildren().get(0).equals(childNode) ? "firstRow" : "otherRow").append("'").append(">");
//                    htmlTeamOrgChart.append("<td class=\"colSpacer\"></td>");
                    htmlTeamOrgChart.append("<td class=\"nextLevel\" colspan=\"2\">").append(drawTeamChart(childNode, isShowView, levelOptionList, isFromUI, noteChildCountMAp, parentChildSize, nodeId, showAllId, showExternalEmployee, locationId, departmentsHasLocation, locationSizeMap)).append("</td>");
                    htmlTeamOrgChart.append("<td class=\"colSpacer\"></td>");
                    htmlTeamOrgChart.append("</tr>");
                }
            } else {
                if (!children.isEmpty()) {
                    htmlTeamOrgChart.append("<tr class='nextLevelRow nextLevelRowAlt'>");
                    for (ChartNode childNode : node.getChildren()) {
                        childNode.setDepth(node.getDepth() + 1);
                        htmlTeamOrgChart.append("<td ").append((depth <= parentChildSize) ? "class='nodeSingleChildCell' " : "").append((node.getChildren().size() > 1 && isShowView) ? "verticalID" : "").append(" class='nextLevel nextLevelAlt' colspan=\"2\">").append(drawTeamChart(childNode, isShowView, levelOptionList, isFromUI, noteChildCountMAp, parentChildSize, nodeId, showAllId, showExternalEmployee, locationId, departmentsHasLocation, locationSizeMap)).append("</td>");
                    }
                    htmlTeamOrgChart.append("</tr>");
                } else {
                    return (htmlTeamOrgChart.append("</tbody></table>").toString());
                }
            }
            return (htmlTeamOrgChart.append("</tbody></table>").toString());
        }
    }

    private void calculateChildCounts(ChartNode node, Map<Integer, Integer> nomeEmployeesCountMap, LinkedHashMap<Integer, Integer> noteChildCountMap) {
        Integer empCount = nomeEmployeesCountMap.get(node.getId()) != null ? nomeEmployeesCountMap.get(node.getId()) : 0;
        noteChildCountMap.put(node.getId(), empCount);
        if (node.getParent() != null && empCount != null && empCount > 0) {
            addToParentChildCount(node.getParent(), empCount, noteChildCountMap);
        }
        for (ChartNode child : node.getChildren()) {
            calculateChildCounts(child, nomeEmployeesCountMap, noteChildCountMap);
        }
    }

    private void addToParentChildCount(ChartNode node, int empCount, LinkedHashMap<Integer, Integer> noteChildCountMap) {
        if (noteChildCountMap.get(node.getId()) != null) {
            Integer childCount = noteChildCountMap.get(node.getId()) + empCount;
            noteChildCountMap.put(node.getId(), childCount);
        } else {
            noteChildCountMap.put(node.getId(), empCount);
        }
        if (node.getParent() != null) {
            addToParentChildCount(node.getParent(), empCount, noteChildCountMap);
        }
    }


    @Override
    public void saveTeamParent(Integer teamId, Integer parentId) {
        EdsDepartment team = departmentManager.get(teamId);
        Integer teamParentId = departmentTreeManager.getParent(team.getObjectID());
        if (parentId != null) {
            if (teamParentId != null && !teamParentId.equals(parentId)) {
                departmentTreeManager.removeSubtreeFromParents(team.getObjectID());
            }
            if (teamParentId == null || !teamParentId.equals(parentId)) {
                departmentTreeManager.addChildTree(team.getObjectID(), parentId);
            }
        } else if (teamParentId != null) {
            departmentTreeManager.removeSubtreeFromParents(team.getObjectID());
        }
    }

    private EdsDepartmentCustomFields createDepartmentCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsDepartmentCustomFields departmentCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                departmentCustomFields = departmentCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                departmentCustomFields = new EdsDepartmentCustomFields();
                departmentCFManager.create(departmentCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(departmentCustomFields, customFieldItems);
            return departmentCustomFields;
        }
        return null;
    }


    @Override
    public String getChildDepartmentNames(Integer id) {
        List<Integer> childList = departmentTreeManager.getChildList(id);
        StringBuilder teams = new StringBuilder();
        if (childList != null && childList.size() > 0) {
            for (Integer teamID : childList) {
                EdsDepartment department = departmentManager.get(teamID);
                if (teams.length() > 0) {
                    teams.append(", ");
                }
                teams.append(department.getName());
            }
            return teams.toString();
        }
        return "";
    }

    @Override
    public String getChildDepartmentIds(Integer id, boolean needComma) {
        List<Integer> childList = departmentTreeManager.getChildList(id);
        if (childList != null && childList.size() > 0) {
            StringBuilder teams = new StringBuilder();
            for (Integer teamID : childList) {
                if (needComma || teams.length() > 0) {
                    teams.append(", ");
                }
                teams.append(teamID);
                List<Integer> childListMini = departmentTreeManager.getChildList(teamID);
                if (childListMini != null && childListMini.size() > 0) {
                    for (Integer teamMiniID : childListMini) {
                        getChildDepartmentIds(teamMiniID, true);
                    }
                }
            }
            return teams.toString();
        }
        return "";
    }

    @Override
    public SelectItem getLocationByDepartmentId(Integer departmentId) {
        return departmentManager.get(departmentId).getLocation() != null ? departmentManager.get(departmentId).getLocation().getAsSelectItem() : null;
    }

    private boolean checkReferenceLocale(ReferenceLocale referenceLocale) {
        return referenceLocale != null && (referenceLocale.getUzbek() != null ||
                referenceLocale.getRussian() != null ||
                referenceLocale.getEnglish() != null ||
                referenceLocale.getArabic() != null);
    }


    public void activateOrDisctivateTeam(Integer teamId, Boolean activate) {
        activateOrDisctivateTeam(teamId, activate, true, true);
    }

    private void activateOrDisctivateTeam(Integer teamId, Boolean activate, boolean b, boolean indexSolr) {
        try {
            EdsDepartment department = departmentManager.get(teamId);
            if (activate) {
                department.setActive(true);
                departmentManager.update(department);

                if (indexSolr) {
                    try {
                        departmentSolrComponent.index(department);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                department.setActive(false);
                departmentManager.update(department);
                if (indexSolr) {
                    try {
                        departmentSolrComponent.index(department);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EdsReferenceLocale processLocale(Map<String, String> localeMap) {
        if (localeMap == null) {
            return null;
        }

        EdsReferenceLocale locale = new EdsReferenceLocale();
        locale.setUzbek(localeMap.get("uz"));
        locale.setEnglish(localeMap.get("en"));
        locale.setRussian(localeMap.get("ru"));
        locale.setArabic(localeMap.get("ar"));

        referenceLocaleManager.create(locale);
        return locale;
    }

    private EdsReferenceLocale processLocale(EdsReferenceLocale locale,Map<String, String> localeMap) {
        locale.setUzbek(localeMap.get("uz"));
        locale.setEnglish(localeMap.get("en"));
        locale.setRussian(localeMap.get("ru"));
        locale.setArabic(localeMap.get("ar"));
        referenceLocaleManager.update(locale);
        return locale;
    }

}
