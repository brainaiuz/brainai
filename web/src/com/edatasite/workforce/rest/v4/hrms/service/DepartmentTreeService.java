package com.edatasite.workforce.rest.v4.hrms.service;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.solr.component.DepartmentSolrComponent;
import com.edatasite.workforce.gwt.core.client.enums.ChildOrientation;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.services.dto.EmployeeItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.DepartmentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.team.client.rpc.request.CreateDepartmentReq;
import com.edatasite.workforce.gwt.team.client.services.dto.OrgBoardSettingsItem;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DepartmentTreeService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final DepartmentTreeManager departmentTreeManager;
    private final DepartmentManager departmentManager;
    private final CompanyManager companyManager;
    private final LocationManager locationManager;
    private final EmployeeDepartmentManager employeeDepartmentManager;
    private final GoalManager goalManager;
    private final OrgBoardSettingsService orgBoardSettingsService;
    private final UploadManager uploadManager;

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private DepartmentSolrComponent departmentSolrComponent;

    public DepartmentTreeService(DepartmentTreeManager departmentTreeManager, CompanyManager companyManager, EmployeeDepartmentManager employeeDepartmentManager, GoalManager goalManager, DepartmentManager departmentManager, OrgBoardSettingsService orgBoardSettingsService, LocationManager locationManager, UploadManager uploadManager) {
        this.departmentTreeManager = departmentTreeManager;
        this.companyManager = companyManager;
        this.employeeDepartmentManager = employeeDepartmentManager;
        this.goalManager = goalManager;
        this.departmentManager = departmentManager;
        this.orgBoardSettingsService = orgBoardSettingsService;
        this.locationManager=locationManager;
        this.uploadManager = uploadManager;
    }

    @Transactional(readOnly = true)
    public DepartmentNode getDepartmentTree() {
        Integer companyId = SecurityContext.getCompanyID();
        EdsCompany company = companyManager.get(companyId);
        Integer rootId = company.getDefaultDepartment().getObjectID();

        Integer userId = SecurityContext.getInstance().getUserId();
        OrgBoardSettingsItem orgBoardSettings = orgBoardSettingsService.getOrgBoardSettings(userId);

        // null = See All, non-null = filter by location
        Integer locationId = orgBoardSettings.getLocationId();
        EdsLocation rootLocation = locationManager.getRootLocation();
        List<Object[]> rows = departmentTreeManager.getFullSubtreeData(rootId, rootLocation != null ? rootLocation.getObjectID() : null, locationId);
        log.info("getDepartmentTree() --> rootDepId: {}, locationId: {}, rootLocationId: {}  ", rootId, locationId, rootLocation);
        if (rows.isEmpty()) {
            throw new RuntimeException("No subtree found for department: " + rootId);
        }
        return getDepartmentNode(rootId, true, rows);
    }

    private void fillEmployeesBatch(Collection<DepartmentNode> nodes) {
        Set<Integer> ids = nodes.stream().filter(d -> d.getId() != null).map(DepartmentNode::getId).collect(Collectors.toSet());
        Locale lang = ServerUtils.getUserLocale();
        String userLocale = "en";
        if (lang != null) {
            userLocale = lang.getLanguage();
        }
        List<Object[]> rows = employeeDepartmentManager.getEmployeesForDepartments(ids, userLocale.toLowerCase());

        Map<Integer, List<EmployeeItem>> map = new HashMap<>();

        for (Object[] r : rows) {
            EmployeeItem employee = new EmployeeItem();
            employee.setDepartment((Integer) r[0]);
            employee.setId((Integer) r[1]);
            employee.setName((String) r[2]);
            employee.setEmail((String) r[3]);
            employee.setPhoneNumber((String) r[4]);
            employee.setTgNumber((String) r[5]);
            employee.setImageUrl(null);
            if (r[6] != null) {
                EdsUpload upload = (EdsUpload) uploadManager.get((Integer) r[6]);
                String url = uploadManager.getFileURL(upload, false);
                employee.setImageUrl(url);
            }
            employee.setPosition((String) r[7]);

            map.computeIfAbsent((Integer) r[0], x -> new ArrayList<>())
                    .add(employee);
        }

        for (DepartmentNode n : nodes) {
            n.setEmployees(map.getOrDefault(n.getId(), new ArrayList<>()));
        }
    }

    private void fillGoalsBatch(Collection<DepartmentNode> nodes) {
        Set<Integer> ids = nodes.stream().filter(d -> d.getId() != null).map(DepartmentNode::getId).collect(Collectors.toSet());

        List<EdsGoal> goals = goalManager.getDepartmentGoalsByDepartments(ids);

        Map<Integer, List<SelectItem>> map = new HashMap<>();

        for (EdsGoal g : goals) {
            if (!EdsGoal.DEPARTMENT_GOAL.equals(g.getGoalCategory().getCode())) continue;

            Integer depId = g.getDepartment().getObjectID();

            map.computeIfAbsent(depId, x -> new ArrayList<>())
                    .add(new SelectItem(
                            g.getObjectID(),
                            g.getTitle(),
                            g.getDescription()
                    ));
        }

        for (DepartmentNode n : nodes) {
            n.setMetrics(map.getOrDefault(n.getId(), new ArrayList<>()));
        }
    }

    @Transactional
    public DepartmentNode sortDepartmentTree(DepartmentNode parent) {
        Integer parentId = parent.getId();
        if (parentId == null) {
            throw new IllegalArgumentException("Parent id must not be null");
        }

        List<Integer> orderedChildIds = parent.getChildren().stream()
                .map(DepartmentNode::getId)
                .toList();

        List<EdsDepartmentTree> links = departmentTreeManager.getDirectSubtreeByParent(parentId);


        int order = 1;
        for (Integer childId : orderedChildIds) {
            for (EdsDepartmentTree link : links) {
                if (link.getChildId().equals(childId)) {
                    link.setSorder(order++);
                    departmentTreeManager.update(link);
                    break;
                }
            }
        }
        List<Object[]> departmentWithDirectChildren = departmentTreeManager.getDepartmentWithDirectChildren(parentId);

        return getDepartmentNode(parentId, true, departmentWithDirectChildren);
    }

    @Transactional
    public DepartmentNode getDepartmentById(Integer departmentId) {
        List<Object[]> departmentWithDirectChildren = departmentTreeManager.getDepartmentWithDirectChildren(departmentId);
        return getDepartmentNode(departmentId, false, departmentWithDirectChildren);
    }

    private DepartmentNode getDepartmentNode(Integer parent, boolean isRoot, List<Object[]> treeData) {

        Map<Integer, DepartmentNode> nodeMap = new HashMap<>();
        List<DepartmentNode> roots = new ArrayList<>();

        Integer userId = SecurityContext.getInstance().getUserId();
        OrgBoardSettingsItem orgBoardSettings = orgBoardSettingsService.getOrgBoardSettings(userId);

        for (Object[] r : treeData) {

            Integer id = (Integer) r[0];
            String name = (String) r[1];
            String desc = (String) r[2];
            String shortDesc = (String) r[3];
            Integer hasManager = ((Integer) r[4]);
            Integer depth = ((Number) r[5]).intValue();
            String path = (String) r[6];
            Integer order = ((Number) r[7]).intValue();
            String numberData = (String) r[8];
            String orientRaw = (String) r[9];
            Integer location = (Integer) r[10];
            String color = (String) r[11];

            ChildOrientation orientation =
                    orientRaw != null ? ChildOrientation.valueOf(orientRaw) : ChildOrientation.HORIZONTAL;

            String[] pathArr = path.split(",");
            Integer childId = id;

            Integer parentId = null;
            if (pathArr.length > 1) {
                parentId = Integer.valueOf(pathArr[pathArr.length - 2]);
            }

            DepartmentNode node = nodeMap.computeIfAbsent(childId, x -> new DepartmentNode());

            node.setId(childId);
            node.setName(name);

            if (isRoot) {
                if (orgBoardSettings.getShowDescription()) {
                    node.setDescription(desc);
                }
                if (orgBoardSettings.getShowShortDescription()) {
                    node.setShortDescription(shortDesc);
                }
            } else {
                node.setDescription(desc);
                node.setShortDescription(shortDesc);
            }



            node.setManager(hasManager != null);
            node.setDepth(pathArr.length - 1);
            node.setOrder(order);
            node.setChildOrientation(orientation);
            node.setNumberData(numberData);
            node.setLocationId(location);
            node.setColor(color);

            if (parentId != null && !parentId.equals(childId)) {
                DepartmentNode parentNode = nodeMap.computeIfAbsent(parentId, x -> new DepartmentNode());
                node.setParentId(parentId);
                parentNode.getChildren().add(node);
            } else {
                roots.add(node);
            }
        }


        if (isRoot) {
            if (orgBoardSettings.getShowEmployees()) {
                fillEmployeesBatch(nodeMap.values());
            }
            if (orgBoardSettings.getShowGoals()) {
                fillGoalsBatch(nodeMap.values());
            }
        } else {
            fillEmployeesBatch(nodeMap.values());
            fillGoalsBatch(nodeMap.values());
        }


        DepartmentNode root = nodeMap.get(parent);
        if (root == null && !roots.isEmpty()) {
            root = roots.get(0);
        }
        if (root != null && isRoot) {
            root.setRoot(true);
        }

        for (DepartmentNode n : nodeMap.values()) {
            if (n.hasChildren()) {
                n.getChildren().sort(Comparator.comparingInt(DepartmentNode::getOrder));
                boolean isMultiChildParent = n.hasChildrenMoreThanOne();
                for (DepartmentNode child : n.getChildren()) {
                    child.setParentHasMoreThanOneChild(isMultiChildParent);
                }
            }
        }

        return root;
    }

    @Transactional
    public DepartmentNode moveDepartment(Integer currentDep, Integer parentDep, boolean shouldInheritColor) {
        if (currentDep == null || parentDep == null) {
            throw new IllegalArgumentException("currentDep and parentDep must not be null");
        }
        if (currentDep.equals(parentDep)) {
            throw new IllegalArgumentException("Cannot move department under itself");
        }

        List<Integer> subtreeIds = departmentTreeManager.getAllChildList(currentDep);
        if (subtreeIds != null && subtreeIds.contains(parentDep)) {
            throw new IllegalArgumentException("Cannot move department under its own descendant");
        }

        departmentTreeManager.removeSubtreeFromParents(currentDep);

        departmentTreeManager.addChildTree(currentDep, parentDep);

        if (shouldInheritColor) {
            inheritColorFromParent(parentDep, currentDep);
        }

        inheritLocationFromParent(parentDep, currentDep);

        return getDepartmentTree();
    }

    private void inheritColorFromParent(Integer parentDep, Integer currentDep) {
        EdsDepartment parent = departmentManager.get(parentDep);
        EdsDepartment child = departmentManager.get(currentDep);

        if (parent.getColor() != null) {
            child.setColor(parent.getColor());
            changeSubTreeColor(child.getObjectID(), parent.getColor());
            departmentManager.update(child);
        }
    }

    private void inheritLocationFromParent(Integer parentDep, Integer currentDep) {
        EdsDepartment parent = departmentManager.get(parentDep);
        EdsDepartment child = departmentManager.get(currentDep);
        child.setLocation(parent.getLocation());
        departmentManager.update(child);
        changeSubTreeLocation(child.getObjectID(), parent.getLocation());
    }

    private void changeSubTreeLocation(Integer depID, EdsLocation location) {
        List<Integer> allChildList = departmentTreeManager.getAllChildList(depID);
        if (allChildList.isEmpty()) return;
        List<EdsDepartment> depList = departmentManager.list(allChildList);
        if (depList.isEmpty()) return;
        for (EdsDepartment dep : depList) {
            dep.setLocation(location);
        }
        departmentManager.updateAll(depList, depList.size());
    }

    @Transactional
    public DepartmentNode createDapartment(CreateDepartmentReq departmentReq) {
        EdsUser user = employeeManager.getUser();
        EdsDepartment team = new EdsDepartment();
        UUID externalGUID = UUID.randomUUID();
        team.setExternalGUID(externalGUID.toString());
        team.setName(departmentReq.getName());
        team.setDescription(departmentReq.getDescription());
        team.setShortDescription(departmentReq.getShortDescription());
        team.setColor(departmentReq.getColor());
        team.setChildOrientation(departmentReq.getOrientation());
        team.setActive(true);
        team.setCreationTime(user.getCompany().getCompanyDate());
        team.setStartDate(new Date());
        team.setCreator(user);

        if (departmentReq.getParentId() != null) {
            EdsDepartment parent = departmentManager.get(departmentReq.getParentId());
            if (parent.getLocation() != null) {
                team.setLocation(parent.getLocation());
            }
        }

        NumberData newNumberData = generateDepartmentNumber();
        if (newNumberData != null) {
            team.setIntNumber(newNumberData.getIntNumber());
            team.setNumberData(newNumberData.getNumberString());
        }
        EdsReference edsReference;
        EdsReference referenceDePartmentTitles = referenceManager.getByCode("DEPARTMENT_TITLES");
        ReferenceItem parentReferenceItem = referenceDePartmentTitles != null ? referenceDePartmentTitles.getRPC() : null;
        ReferenceItem childReferenceItem = new ReferenceItem();
        childReferenceItem.setName(departmentReq.getName());
        childReferenceItem.setParentID(parentReferenceItem.getObjectID());
        childReferenceItem.setParent(parentReferenceItem.getName());
        childReferenceItem.setParentCode(parentReferenceItem.getCode());

        Integer newTeamDepartmentId = allInOneService.saveReference(childReferenceItem, null, true);

        edsReference = referenceManager.get(newTeamDepartmentId);

        team.setDepartmentName(edsReference);
        if (edsReference.getLocale() != null) {
            team.setLocale(edsReference.getLocale());
        }
        departmentManager.create(team);
        if (departmentReq.getParentId() != null) {
            departmentTreeManager.addChildTree(team.getObjectID(), departmentReq.getParentId());
        }
        baseEventPostProcessor.registerEvent(DepartmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, team, user);

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
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, team, user);
            workflowEvent.setEntityType(RelationItem.TYPE_DEPARTMENT);
        }
        return getDepartmentTree();
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

    @Transactional
    public DepartmentNode deleteDepartment(Integer departmentId) {
        Optional.of(departmentManager.get(departmentId)).orElseThrow(() -> {
            log.error("=========== Department {} is not found! ===========", departmentId);
            return new RuntimeException("=========== Department is not found! ===========");
        });

        return this.getDepartmentTree();
    }

    @Transactional(readOnly = true)
    public DepartmentNode getDepartment(Integer departmentId) {
        Optional.of(departmentManager.get(departmentId)).orElseThrow(() -> {
            log.error("=========== Department {} is not found! ===========", departmentId);
            return new RuntimeException("=========== Department is not found! ===========");
        });

        return this.getDepartmentById(departmentId);
    }

    @Transactional
    public DepartmentNode updateDepartment(DepartmentNode department) {
        EdsDepartment dep = Optional.of(departmentManager.get(department.getId())).orElseThrow(() -> {
            log.error("=========== Department {} is not found! ===========", department.getId());
            return new RuntimeException("=========== Department is not found! ===========");
        });

        dep.setShortDescription(department.getShortDescription());
        dep.setDescription(department.getDescription());
        if (department.isChangeSubDepColor()) {
            changeSubTreeColor(department.getId(), department.getColor());
        }
        dep.setColor(department.getColor());
        dep.setChildOrientation(department.getChildOrientation());

        departmentManager.update(dep);
        return this.getDepartmentTree();
    }

    private void changeSubTreeColor(Integer depID, String color) {
        List<Integer> allChildList = departmentTreeManager.getAllChildList(depID);
        if (allChildList.isEmpty()) return;

        List<EdsDepartment> depList = departmentManager.list(allChildList);
        if (depList.isEmpty()) return;

        for (EdsDepartment dep : depList) {
            dep.setColor(color);
        }

        departmentManager.updateAll(depList, depList.size());
    }
}
