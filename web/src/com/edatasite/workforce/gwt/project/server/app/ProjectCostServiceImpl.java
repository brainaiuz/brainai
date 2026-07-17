package com.edatasite.workforce.gwt.project.server.app;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.projectcost.EdsCostPeriodDate;
import com.edatasite.workforce.core.domain.projectcost.EdsCostSheet;
import com.edatasite.workforce.core.domain.projectcost.EdsOtherCostItems;
import com.edatasite.workforce.core.domain.projectcost.EdsProjectCostItem;
import com.edatasite.workforce.core.domain.projectcost.EdsResource;
import com.edatasite.workforce.core.domain.projectcost.EdsResourcePool;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.CostPeriodDateManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.CostSheetManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.OtherCostItemsManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.ProjectCostItemManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.ResourceManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.ResourcePoolManager;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostAllDataItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostSelectItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectOtherCostItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Lochin
 * Date: 29-Apr-2010
 * Time: 17:22:33
 * To change this template use File | Settings | File Templates.
 */
@Service("projectCostService")
public class ProjectCostServiceImpl implements ProjectCostService {

    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CostSheetManager costSheetManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ResourcePoolManager resourcePoolManager;
    @Autowired
    private ResourceManager resourceManager;
    @Autowired
    private OtherCostItemsManager otherCostItemsManager;
    @Autowired
    private ProjectCostItemManager projectCostItemManager;
    @Autowired
    private CostPeriodDateManager costPeriodDateManager;
    @Autowired
    private CommonServiceLocal commonService;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectCostAllDataItem getProjectCostItems(ProjectCostAllDataItem costAllDataItem) {
        EdsReference standart = referenceManager.findReference(EdsProjectCostItem.COST_TYPE, EdsProjectCostItem.STANDART);
        List<Object[]> costSheetStandart = costSheetManager.getProjectStandartCostItems(costAllDataItem, standart.getObjectID());
        ProjectCostItem[] resources = new ProjectCostItem[costSheetStandart.size()];
        for (int i = 0; i < costSheetStandart.size(); i++) {
            Integer edsCostId = (Integer) costSheetStandart.get(i)[0];
            Float periodHours = (Float) costSheetStandart.get(i)[1];
            Float dailyHour = (Float) costSheetStandart.get(i)[2];
            Float rate = (Float) costSheetStandart.get(i)[3];
            Float plannedCost = (Float) costSheetStandart.get(i)[4];
            EdsProjectCostItem edsCostItem = projectCostItemManager.get(edsCostId);
            EdsResource resource = edsCostItem.getResource();
            Integer resourceId = null;
            if (resource != null) {
                resourceId = resource.getObjectID();
            }
            if (costAllDataItem.isEstemitedCost()) {
                resources[i] = new ProjectCostItem(edsCostItem.getObjectID(), resourceId, edsCostItem.getResourcePool().getObjectID(),
                        periodHours, dailyHour, rate, plannedCost);
            } else {
                resources[i] = new ProjectCostItem(edsCostItem.getObjectID(), resourceId, edsCostItem.getResourcePool().getObjectID());
                resources[i].setActualQuantity(periodHours);
                resources[i].setActualDaily(dailyHour);
                resources[i].setActualRate(rate);
                resources[i].setActualCost(plannedCost);
            }
        }

        EdsReference markup = referenceManager.findReference(EdsProjectCostItem.COST_TYPE, EdsProjectCostItem.MARKUP);
        List<Object[]> costSheetMarkup = costSheetManager.getProjectMarkupCostItems(costAllDataItem, markup.getObjectID());
        ProjectCostItem[] otherOverHead = new ProjectCostItem[costSheetMarkup.size()];
        for (int i = 0; i < costSheetMarkup.size(); i++) {
            Integer edsCostId = (Integer) costSheetMarkup.get(i)[0];
            Float otherAmount = (Float) costSheetMarkup.get(i)[1];
            Float otherPercent = (Float) costSheetMarkup.get(i)[2];
            Float otherPlanned = (Float) costSheetMarkup.get(i)[3];
            EdsProjectCostItem edsCostItem = projectCostItemManager.get(edsCostId);
            otherOverHead[i] = new ProjectCostItem(edsCostItem.getObjectID(), edsCostItem.getOtherCostItems().getObjectID(), otherAmount, otherPercent, otherPlanned, edsCostItem.isPercent());
        }
        Integer costPeriodId = costPeriodDateManager.getProjectCostPeriodId(costAllDataItem.getProjectId(), costAllDataItem.getTaskId(), costAllDataItem.getFrom(), costAllDataItem.getTo());
        ProjectCostAllDataItem costAllData = new ProjectCostAllDataItem();
        costAllData.setCostPeriodId(costPeriodId);
        costAllData.setResources(resources);
        costAllData.setOtherOverHeads(otherOverHead);

        return costAllData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectCostAllDataItem getProjectActualCostItems(ProjectCostAllDataItem costAllDataItem) {
        EdsReference standart = referenceManager.findReference(EdsProjectCostItem.COST_TYPE, EdsProjectCostItem.STANDART);
        List<EdsCostSheet> costSheetListStandart = costSheetManager.getProjectActualCostItems(costAllDataItem, standart);
        ProjectCostItem[] costItemStandart = new ProjectCostItem[costSheetListStandart.size()];
        int step = 0;
        for (EdsCostSheet edsCostSheet : costSheetListStandart) {
            EdsProjectCostItem edsCostItem = edsCostSheet.getProjectCostItem();
            EdsResource resource = edsCostItem.getResource();
            Integer resourceId = null;
            if (resource != null) {
                resourceId = resource.getObjectID();
            }
            costItemStandart[step] = new ProjectCostItem(edsCostSheet.getObjectID(), resourceId, edsCostItem.getResourcePool().getObjectID());
            costItemStandart[step].setActualCost(edsCostSheet.getActualCost());
            costItemStandart[step].setActualQuantity(edsCostSheet.getActualUnit());
            costItemStandart[step].setPlanned(edsCostItem.isPlanned());
            costItemStandart[step++].setActualRate(edsCostSheet.getActualRate());

        }
        EdsReference markup = referenceManager.findReference(EdsProjectCostItem.COST_TYPE, EdsProjectCostItem.MARKUP);
        List<EdsCostSheet> costSheetListMarkup = costSheetManager.getProjectActualCostItems(costAllDataItem, markup);
        ProjectCostItem[] costItemMarkup = new ProjectCostItem[costSheetListMarkup.size()];
        step = 0;
        for (EdsCostSheet edsCostSheet : costSheetListMarkup) {
            EdsProjectCostItem edsCostItem = edsCostSheet.getProjectCostItem();
            costItemMarkup[step] = new ProjectCostItem(edsCostSheet.getObjectID(), edsCostItem.getOtherCostItems().getObjectID(), edsCostSheet.getActualPercentCompleted(), edsCostSheet.getActualPercentCharge(), edsCostSheet.getActualCost(), edsCostItem.isPercent());
            costItemMarkup[step++].setPlanned(edsCostItem.isPlanned());
        }
        costAllDataItem.setResources(costItemStandart);
        costAllDataItem.setOtherOverHeads(costItemMarkup);
        return costAllDataItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer[] getByDateCompanyWorkingDate(Date from, Date to) {
        Map<Integer, Integer> weekWorkingDay = getCompanyWeeklyTimeSlot();
        int workingDays = ServerUtils.getCompanyWorkDay(from, to, weekWorkingDay);
        int allDay = (int) ((to.getTime() - from.getTime()) / (1000 * 60 * 60 * 24) + 1);
        Integer[] array = new Integer[2];
        array[0] = workingDays;
        array[1] = allDay - workingDays;
        return array;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Map<Integer, Integer> getCompanyWeeklyTimeSlot() {
        EdsCompany company = employeeManager.getUser().getCompany();
        Set<EdsTimeSlotItem> items = company.getDefaultTimeSlot().getItems();
        Map<Integer, Integer> weekWorkingDay = new HashMap<>();
        for (EdsTimeSlotItem item : items) {
            if (item.getStartTime() == null || item.getEndTime() == null || item.getEndTime() - item.getStartTime() == 0) {
                weekWorkingDay.put(item.getDay(), 0);
            } else {
                weekWorkingDay.put(item.getDay(), item.getEndTime() - item.getStartTime());
            }
        }
        return weekWorkingDay;
    }

    /**
     * Save and Update Estimeted and Actual cost
     *
     * @param costAllDataItem
     */
    @Transactional
    public void saveEstimateCostSheet(ProjectCostAllDataItem costAllDataItem) {
        if (costAllDataItem.getResources().length != 0 || costAllDataItem.getOtherOverHeads().length != 0) {
            EdsCompany company = employeeManager.getUser().getCompany();
            EdsTask task = taskManager.get(costAllDataItem.getTaskId());
            EdsProject project = projectManager.get(costAllDataItem.getProjectId());
            EdsReference resourceType = referenceManager.get(costAllDataItem.getResourceTypeId());

            Calendar fromTime = Calendar.getInstance();
            fromTime.setTime(costAllDataItem.getFrom());
            fromTime.set(Calendar.AM_PM, 0);
            fromTime.set(Calendar.HOUR_OF_DAY, 0);
            fromTime.set(Calendar.MINUTE, 0);
            fromTime.set(Calendar.SECOND, 0);
            fromTime.set(Calendar.MILLISECOND, 0);
            Calendar toTime = Calendar.getInstance();
            toTime.setTime(costAllDataItem.getTo());
            toTime.set(Calendar.AM_PM, 0);
            toTime.set(Calendar.HOUR_OF_DAY, 23);
            toTime.set(Calendar.MINUTE, 59);
            toTime.set(Calendar.SECOND, 59);
            toTime.set(Calendar.MILLISECOND, 0);


            if (costAllDataItem.getCostPeriodId() == null) {
                EdsCostPeriodDate edsCostPeriodDate = new EdsCostPeriodDate();
                edsCostPeriodDate.setProject(project);
                edsCostPeriodDate.setTask(task);
//                edsCostPeriodDate.setCompany(company);
                edsCostPeriodDate.setStartDate(fromTime.getTime());
                edsCostPeriodDate.setEndDate(toTime.getTime());
                costPeriodDateManager.create(edsCostPeriodDate);
            }

            EdsProjectCostItem edsProjectCostItem = new EdsProjectCostItem();
            edsProjectCostItem.setTask(task);
//            edsProjectCostItem.setCompany(company);
            edsProjectCostItem.setProject(project);
            edsProjectCostItem.setResourceType(resourceType);
            edsProjectCostItem.setStartDate(fromTime.getTime());
            edsProjectCostItem.setEndDate(toTime.getTime());

            Map<Integer, Integer> weeklyCompanyTimeSlot = getCompanyWeeklyTimeSlot();

            // create or update change employee resource cost
            createEmployeeResourceCost(costAllDataItem.getResources(), edsProjectCostItem, weeklyCompanyTimeSlot, fromTime.getTime(), toTime.getTime(), costAllDataItem.isEstemitedCost());
            // cretaet or update change other over head cost
            createOtherOverHeadCost(costAllDataItem.getOtherOverHeads(), edsProjectCostItem, weeklyCompanyTimeSlot, fromTime.getTime(), toTime.getTime(), costAllDataItem.isEstemitedCost());
        }
    }

    @Transactional
    public void saveActualCostSheet(ProjectCostAllDataItem costAllDataItem) {
        if (costAllDataItem.getResources().length != 0 || costAllDataItem.getOtherOverHeads().length != 0) {
            EdsCompany company = employeeManager.getUser().getCompany();
            EdsTask task = taskManager.get(costAllDataItem.getTaskId());
            EdsProject project = projectManager.get(costAllDataItem.getProjectId());
            EdsReference resourceType = referenceManager.get(costAllDataItem.getResourceTypeId());

            Calendar fromTime = Calendar.getInstance();
            fromTime.setTime(costAllDataItem.getFrom());
            fromTime.set(Calendar.AM_PM, 0);
            fromTime.set(Calendar.HOUR_OF_DAY, 0);
            fromTime.set(Calendar.MINUTE, 0);
            fromTime.set(Calendar.SECOND, 0);
            fromTime.set(Calendar.MILLISECOND, 0);

            EdsProjectCostItem edsProjectCostItem = new EdsProjectCostItem();
            edsProjectCostItem.setTask(task);
//            edsProjectCostItem.setCompany(company);
            edsProjectCostItem.setProject(project);
            edsProjectCostItem.setResourceType(resourceType);
            edsProjectCostItem.setStartDate(fromTime.getTime());
            edsProjectCostItem.setEndDate(fromTime.getTime());

            createEmployeeResourceActual(costAllDataItem.getResources(), edsProjectCostItem, fromTime.getTime());
            cretaeOtherOverHeadActual(costAllDataItem.getOtherOverHeads(), edsProjectCostItem, fromTime.getTime());
        }
    }

    @Transactional
    public void cretaeOtherOverHeadActual(ProjectCostItem[] otherOverHeads, EdsProjectCostItem edsProjectCostItem, Date time) {
        EdsReference markup = referenceManager.findReference(EdsProjectCostItem.COST_TYPE, EdsProjectCostItem.MARKUP);
        // Other over head cost
        for (ProjectCostItem item : otherOverHeads) {
            EdsCostSheet edsCostSheet;
            EdsProjectCostItem edsCostItemClone;
            if (item.getCostItemId() == null) {
                edsCostItemClone = edsProjectCostItem.cloneShallow();
                edsCostItemClone.setCostType(markup);
                edsCostItemClone.setOtherCostItems(otherCostItemsManager.get(item.getOtherCostItemId()));
                edsCostItemClone.setPlanned(false);
                edsCostItemClone.setPercent(item.isPercent());
                projectCostItemManager.create(edsCostItemClone);

                edsCostSheet = new EdsCostSheet();
                edsCostSheet.setProjectCostItem(edsCostItemClone);
                edsCostSheet.setDate(time);
                costSheetManager.create(edsCostSheet);
            } else {
                edsCostSheet = costSheetManager.get(item.getCostItemId());
                edsCostItemClone = edsCostSheet.getProjectCostItem();
                if (!edsCostItemClone.isPlanned()) {
                    edsCostItemClone.setOtherCostItems(otherCostItemsManager.get(item.getOtherCostItemId()));
                    projectCostItemManager.update(edsCostItemClone);
                }
            }

            if (item.isPercent()) {
                edsCostSheet.setActualPercentCharge(item.getOtherPercent());
            } else {
                edsCostSheet.setActualPercentCompleted(item.getOtherAmount());
            }
            edsCostSheet.setActualCost(item.getOtherPlannedAmount());
            costSheetManager.update(edsCostSheet);
        }
    }

    private void createEmployeeResourceActual(ProjectCostItem[] resources, EdsProjectCostItem edsProjectCostItem, Date time) {
        EdsReference standart = referenceManager.findReference(EdsProjectCostItem.COST_TYPE, EdsProjectCostItem.STANDART);
        // Employee Resource cost
        for (ProjectCostItem item : resources) {
            EdsCostSheet edsCostSheet;
            EdsProjectCostItem edsCostItemClone;
            if (item.getCostItemId() == null) {
                edsCostItemClone = edsProjectCostItem.cloneShallow();
                edsCostItemClone.setCostType(standart);
                if (item.getResourceId() != null) {
                    edsCostItemClone.setResource(resourceManager.get(item.getResourceId()));
                }
                edsCostItemClone.setResourcePool(resourcePoolManager.get(item.getResourcePoolId()));
                edsCostItemClone.setPlanned(false);
                projectCostItemManager.create(edsCostItemClone);
                edsCostSheet = new EdsCostSheet();
                edsCostSheet.setProjectCostItem(edsCostItemClone);
                edsCostSheet.setDate(time);
                costSheetManager.create(edsCostSheet);
            } else {
                edsCostSheet = costSheetManager.get(item.getCostItemId());
                edsCostItemClone = edsCostSheet.getProjectCostItem();
                if (!edsCostItemClone.isPlanned()) {
                    edsCostItemClone.setResource(resourceManager.get(item.getResourceId()));
                    edsCostItemClone.setResourcePool(resourcePoolManager.get(item.getResourcePoolId()));
                    projectCostItemManager.update(edsCostItemClone);
                }
            }
            if (item.getActualQuantity() != null) {
                edsCostSheet.setActualUnit(item.getActualQuantity());
            }
            if (item.getActualRate() != null) {
                edsCostSheet.setActualRate(item.getActualRate());
            }

            edsCostSheet.setActualCost(item.getActualCost());
            costSheetManager.update(edsCostSheet);
        }
    }

    /**
     * Save and Update Other Over head cost
     *
     * @param otherOverHeads
     * @param edsProjectCostItem
     * @param weeklyCompanyTimeSlot
     * @param fromTime
     * @param toTime
     * @param estemitedCost
     */
    @Transactional
    public void createOtherOverHeadCost(ProjectCostItem[] otherOverHeads, EdsProjectCostItem edsProjectCostItem, Map<Integer, Integer> weeklyCompanyTimeSlot, Date fromTime, Date toTime, boolean estemitedCost) {
        EdsReference markup = referenceManager.findReference(EdsProjectCostItem.COST_TYPE, EdsProjectCostItem.MARKUP);
        Integer[] workingDays = getByDateCompanyWorkingDate(fromTime, toTime);
        // Other over head cost
        for (ProjectCostItem item : otherOverHeads) {
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(fromTime);
            EdsProjectCostItem edsCostItemClone;
            Map<Date, EdsCostSheet> costSheetMap = new HashMap<>();
            if (item.getCostItemId() == null) {
                edsCostItemClone = edsProjectCostItem.cloneShallow();
                edsCostItemClone.setCostType(markup);
                edsCostItemClone.setOtherCostItems(otherCostItemsManager.get(item.getOtherCostItemId()));
                edsCostItemClone.setPlanned(estemitedCost);
                edsCostItemClone.setPercent(item.isPercent());
                projectCostItemManager.create(edsCostItemClone);
            } else {
                edsCostItemClone = projectCostItemManager.get(item.getCostItemId());
                edsCostItemClone.setOtherCostItems(otherCostItemsManager.get(item.getOtherCostItemId()));
                List<EdsCostSheet> costSheetList = costSheetManager.getCostSheetList(edsCostItemClone.getObjectID(), fromTime, toTime);
                for (EdsCostSheet costSheet : costSheetList) {
                    costSheetMap.put(costSheet.getDate(), costSheet);
                }
                projectCostItemManager.update(edsCostItemClone);
            }

            float sum = item.getOtherPlannedAmount() / workingDays[0];
            while (startDate.getTime().before(toTime)) {
                startDate.set(Calendar.AM_PM, 0);
                startDate.set(Calendar.HOUR_OF_DAY, 0);
                startDate.set(Calendar.MINUTE, 0);
                startDate.set(Calendar.SECOND, 0);
                startDate.set(Calendar.MILLISECOND, 0);

                if (weeklyCompanyTimeSlot.containsKey(startDate.get(Calendar.DAY_OF_WEEK) - 1) && !weeklyCompanyTimeSlot.get(startDate.get(Calendar.DAY_OF_WEEK) - 1).equals(0)) {
                    EdsCostSheet edsCostSheet;
                    if (costSheetMap.containsKey(startDate.getTime())) {
                        edsCostSheet = costSheetMap.get(startDate.getTime());
                    } else {
                        edsCostSheet = new EdsCostSheet();
                        edsCostSheet.setProjectCostItem(edsCostItemClone);
                        edsCostSheet.setDate(startDate.getTime());
                    }
                    if (estemitedCost) {// Estimete
                        if (item.isPercent()) {
                            edsCostSheet.setPlannedPercentCharge(item.getOtherPercent());
                        } else {
                            edsCostSheet.setPlannedPercentCompleted(item.getOtherAmount());
                        }
                        edsCostSheet.setPlannedCost(sum);
                    } else {// Actual
                        if (item.isPercent()) {
                            edsCostSheet.setActualPercentCharge(item.getOtherPercent());
                        } else {
                            edsCostSheet.setActualPercentCompleted(item.getOtherAmount());
                        }
                        edsCostSheet.setActualCost(sum);
                    }

                    if (costSheetMap.containsKey(startDate.getTime())) {
                        costSheetManager.update(edsCostSheet);
                    } else {
                        costSheetManager.create(edsCostSheet);
                    }

                }
                startDate.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
    }

    /**
     * Save and Update Resource cost
     *
     * @param resources
     * @param edsProjectCostItem
     * @param weeklyCompanyTimeSlot
     * @param fromTime
     * @param toTime
     * @param estemitedCost
     */
    @Transactional
    public void createEmployeeResourceCost(ProjectCostItem[] resources, EdsProjectCostItem edsProjectCostItem, Map<Integer, Integer> weeklyCompanyTimeSlot, Date fromTime, Date toTime, boolean estemitedCost) {
        EdsReference standart = referenceManager.findReference(EdsProjectCostItem.COST_TYPE, EdsProjectCostItem.STANDART);
        // Employee Resource cost
        for (ProjectCostItem item : resources) {
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(fromTime);
            EdsProjectCostItem edsCostItemClone;
            Map<Date, EdsCostSheet> costSheetMap = new HashMap<>();
            if (item.getCostItemId() == null) {
                edsCostItemClone = edsProjectCostItem.cloneShallow();
                edsCostItemClone.setCostType(standart);
                if (item.getResourceId() != null) {
                    edsCostItemClone.setResource(resourceManager.get(item.getResourceId()));
                }
                edsCostItemClone.setResourcePool(resourcePoolManager.get(item.getResourcePoolId()));
                edsCostItemClone.setPlanned(estemitedCost);
                projectCostItemManager.create(edsCostItemClone);
            } else {
                edsCostItemClone = projectCostItemManager.get(item.getCostItemId());
                if (item.getResourceId() != null) {
                    edsCostItemClone.setResource(resourceManager.get(item.getResourceId()));
                }
                edsCostItemClone.setResourcePool(resourcePoolManager.get(item.getResourcePoolId()));
                edsCostItemClone.setPlanned(true);
                List<EdsCostSheet> costSheetList = costSheetManager.getCostSheetList(edsCostItemClone.getObjectID(), fromTime, toTime);
                for (EdsCostSheet costSheet : costSheetList) {
                    costSheetMap.put(costSheet.getDate(), costSheet);
                }
                projectCostItemManager.update(edsCostItemClone);
            }

            float sum = estemitedCost ? item.getPlannedQuantity() : item.getActualQuantity();
            while (startDate.getTime().before(toTime)) {
                startDate.set(Calendar.AM_PM, 0);
                startDate.set(Calendar.HOUR_OF_DAY, 0);
                startDate.set(Calendar.MINUTE, 0);
                startDate.set(Calendar.SECOND, 0);
                startDate.set(Calendar.MILLISECOND, 0);

                if (weeklyCompanyTimeSlot.containsKey(startDate.get(Calendar.DAY_OF_WEEK) - 1) && !weeklyCompanyTimeSlot.get(startDate.get(Calendar.DAY_OF_WEEK) - 1).equals(0)) {
                    EdsCostSheet edsCostSheet;
                    if (costSheetMap.containsKey(startDate.getTime())) {
                        edsCostSheet = costSheetMap.get(startDate.getTime());
                    } else {
                        edsCostSheet = new EdsCostSheet();
                        edsCostSheet.setProjectCostItem(edsCostItemClone);
                        edsCostSheet.setDate(startDate.getTime());
                    }
                    if (estemitedCost) {// Estimete
                        if (sum > 0) {
                            if (sum > item.getPlannedDaily()) {
                                edsCostSheet.setPlannedUnit(item.getPlannedDaily());
                            } else {
                                edsCostSheet.setPlannedUnit(sum);
                            }
                        } else {
                            edsCostSheet.setPlannedUnit(0.0f);
                        }
                        edsCostSheet.setPlannedRate(item.getPlannedRate());
                        edsCostSheet.setPlannedCost(edsCostSheet.getPlannedRate() * edsCostSheet.getPlannedUnit());
                        sum -= item.getPlannedDaily();
                    } else {// Actual
                        if (sum > 0) {
                            if (sum > item.getActualDaily()) {
                                edsCostSheet.setActualUnit(item.getActualDaily());
                            } else {
                                edsCostSheet.setActualUnit(sum);
                            }
                        } else {
                            edsCostSheet.setActualUnit(0.0f);
                        }
                        edsCostSheet.setActualRate(item.getActualRate());
                        edsCostSheet.setActualCost(edsCostSheet.getActualRate() * edsCostSheet.getActualUnit());
                        sum -= item.getActualDaily();
                    }

                    if (costSheetMap.containsKey(startDate.getTime())) {
                        costSheetManager.update(edsCostSheet);
                    } else {
                        costSheetManager.create(edsCostSheet);
                    }

                }
                startDate.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
    }

    @Transactional
    public void saveResourcePool(Integer resourceTypeId, String name, Float rate) {
        EdsCompany company = resourcePoolManager.getUser().getCompany();
        EdsResourcePool edsResourePool = new EdsResourcePool();
        edsResourePool.setName(name);
        if (rate != null) {
            edsResourePool.setRateAvg(rate);
        }
        edsResourePool.setResourceType(referenceManager.get(resourceTypeId));
//        edsResourePool.setCompany(company);
        resourcePoolManager.create(edsResourePool);
    }

    @Transactional
    public void saveResource(String name, Float rate, Integer resourceTypeId, Integer resourcePoolId, Integer employeeId) {
        EdsCompany company = resourceManager.getUser().getCompany();
        EdsResource edsResource = new EdsResource();
        edsResource.setName(name);
        if (rate != null) {
            edsResource.setRate(rate);
        }
        edsResource.setResourceType(referenceManager.get(resourceTypeId));
        edsResource.setResourcePool(resourcePoolManager.get(resourcePoolId));
//        edsResource.setCompany(company);
        if (employeeId != null) {
            EdsEmployee edsEmployee = employeeManager.get(employeeId);
            resourceManager.create(edsResource);
            edsEmployee.setResource(edsResource);
        } else {
            resourceManager.create(edsResource);
        }
    }

    @Transactional
    public void saveOtherCostItem(ProjectOtherCostItem costItem) {
        EdsOtherCostItems otherCostItem = new EdsOtherCostItems();
        otherCostItem.setResourceType(referenceManager.get(costItem.getResourceTypeId()));
//        otherCostItem.setCompany(otherCostItemsManager.getUser().getCompany());
        otherCostItem.setName(costItem.getName());
        otherCostItem.setPercent(costItem.isPercent());
        if (costItem.isPercent()) {
            otherCostItem.setPercentCharge(costItem.getPercentCharge());
        } else {
            otherCostItem.setAmountCharge(costItem.getAmountCharge());
        }
        otherCostItemsManager.create(otherCostItem);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectCostSelectItem[] getOtherCostItemList(Integer resourceTypeId) {
        List<EdsOtherCostItems> list = otherCostItemsManager.list(resourceTypeId);
        ProjectCostSelectItem[] items = new ProjectCostSelectItem[list.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new ProjectCostSelectItem(list.get(i).getObjectID(), list.get(i).getName());
            if (list.get(i).isPercent()) {
                items[i].setPercent(list.get(i).getPercentCharge());
            } else {
                items[i].setAmount(list.get(i).getAmountCharge());
            }
            items[i].setLogicPercent(list.get(i).isPercent());
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProjectTasks(Integer projectId) {
        List<EdsTask> tasks = taskManager.listByProjectAndEmployee(projectId);
        SelectItem[] items = new SelectItem[tasks.size()];
        int i = 0;
        for (EdsTask task : tasks) {
            items[i] = new SelectItem(task.getObjectID(), task.getName());
            i++;
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getResourceTypes() {
//        List<EdsReference> types = referenceManager.listReferences(EdsResourcePool.RESOURCE_TYPE, false);
//        SelectItem[] result = new SelectItem[types.size()];
//        int i = 0;
//        for (EdsReference reference : types) {
//            String value = reference.getName();
//            result[i] = new SelectItem(reference.getObjectID(), value, reference.getCode());
//            i++;
//        }
//        return result;
        return commonService.convertReference2SelectItem(EdsResourcePool.RESOURCE_TYPE, false, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectCostSelectItem[] getResourcePoolItems(Integer resourceTypeId) {
        List<EdsResourcePool> edsResourcePools = resourcePoolManager.list(resourceTypeId);
        ProjectCostSelectItem[] selectItems = new ProjectCostSelectItem[edsResourcePools.size()];
        int i = 0;
        for (EdsResourcePool rp : edsResourcePools) {
            selectItems[i] = new ProjectCostSelectItem(rp.getObjectID(), rp.getName());
            if (rp.getRateAvg() != null) {
                selectItems[i].setRate(rp.getRateAvg());
            }
            i++;
        }
        return selectItems;
    }

    @Transactional
    public ProjectCostSelectItem[] getResources(Integer resourceTypeId, Integer resourcePoolId) {
        List<EdsResource> edsResources = resourceManager.list(resourceTypeId, resourcePoolId);
        ProjectCostSelectItem[] selectItems = new ProjectCostSelectItem[edsResources.size()];
        int i = 0;
        for (EdsResource r : edsResources) {
            selectItems[i] = new ProjectCostSelectItem(r.getObjectID(), r.getName());
            if (r.getRate() != null) {
                selectItems[i].setRate(r.getRate());
            }
            i++;
        }
        return selectItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyEmployeesResourceIdNull() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setResourceIdNull(true);
        List<EdsEmployee> edsEmployeeList = employeeManager.list(fp);
        SelectItem[] result = new SelectItem[edsEmployeeList.size()];
        int i = 0;
        for (EdsEmployee employee : edsEmployeeList) {
            if (!employee.getDeleted()) {
                result[i] = new SelectItem(employee.getObjectID(), employee.getFullName());
                i++;
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectCostSelectItem[] getProjectCostPeriodList(Integer projectId, Integer taskId) {
        DateFormat format = new SimpleDateFormat("MMMMM d, yyyy");
        List<EdsCostPeriodDate> costDateList = costPeriodDateManager.getProjectCostPeriodList(projectId, taskId);
        ProjectCostSelectItem[] costItemPeriodList = new ProjectCostSelectItem[costDateList.size()];
        int step = 0;
        for (EdsCostPeriodDate costDate : costDateList) {
            costItemPeriodList[step] = new ProjectCostSelectItem(costDate.getObjectID(), format.format(costDate.getStartDate()) + " - " + format.format(costDate.getEndDate()));
            costItemPeriodList[step].setStartDate(costDate.getStartDate());
            costItemPeriodList[step++].setEndDate(costDate.getEndDate());
        }
        return costItemPeriodList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean isEmpityPeriod(Integer projectId, Integer taskId, Date startDate, Date endDate) {
        Calendar fromTime = Calendar.getInstance();
        fromTime.setTime(startDate);
        fromTime.set(Calendar.AM_PM, 0);
        fromTime.set(Calendar.HOUR_OF_DAY, 0);
        fromTime.set(Calendar.MINUTE, 0);
        fromTime.set(Calendar.SECOND, 0);
        fromTime.set(Calendar.MILLISECOND, 0);
        Calendar toTime = Calendar.getInstance();
        toTime.setTime(endDate);
        toTime.set(Calendar.AM_PM, 0);
        toTime.set(Calendar.HOUR_OF_DAY, 23);
        toTime.set(Calendar.MINUTE, 59);
        toTime.set(Calendar.SECOND, 59);
        toTime.set(Calendar.MILLISECOND, 0);
        return costPeriodDateManager.isEmpityPeriod(projectId, taskId, fromTime.getTime(), toTime.getTime());
    }
}
