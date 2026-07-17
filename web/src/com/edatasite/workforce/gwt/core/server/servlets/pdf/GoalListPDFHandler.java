package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.goal.EdsBusinessGoal;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.ComparatorFactory;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class GoalListPDFHandler extends AbstractITextPostPdfHandler implements Constants {
    private static final Map<String, ComparatorFactory<EdsBusinessGoal>> comparatorFactoriesCompanyGoal = new HashMap<>();

    @Autowired
    private HrmsService hrmsService;
    private GoalManager goalManager;

    public void setGoalManager(GoalManager goalManager) {
        this.goalManager = goalManager;
    }


    static {
        comparatorFactoriesCompanyGoal.put(GoalItem.COMPANY_GOAL_LIST_TITLE, sortOrder -> new AbstractComparator<EdsBusinessGoal>() {
            public int compare(EdsBusinessGoal o1, EdsBusinessGoal o2) {
                return internalCompare(o1.getTitle(), o2.getTitle(), sortOrder);
            }
        });

        comparatorFactoriesCompanyGoal.put(GoalItem.COMPANY_GOAL_LIST_DESCRIPTION, sortOrder -> new AbstractComparator<EdsBusinessGoal>() {
            public int compare(EdsBusinessGoal o1, EdsBusinessGoal o2) {
                return internalCompare(o1.getDescription(), o2.getDescription(), sortOrder);
            }
        });

        comparatorFactoriesCompanyGoal.put(GoalItem.COMPANY_GOAL_LIST_OUTCOME, sortOrder -> new AbstractComparator<EdsBusinessGoal>() {
            public int compare(EdsBusinessGoal o1, EdsBusinessGoal o2) {
                return internalCompare(o1.getOutcome(), o2.getOutcome(), sortOrder);
            }
        });

        comparatorFactoriesCompanyGoal.put(GoalItem.COMPANY_GOAL_LIST_FROM_DATE, sortOrder -> new AbstractComparator<EdsBusinessGoal>() {
            public int compare(EdsBusinessGoal o1, EdsBusinessGoal o2) {
                return internalCompare(o1.getFromDate(), o2.getFromDate(), sortOrder);
            }
        });

        comparatorFactoriesCompanyGoal.put(GoalItem.COMPANY_GOAL_LIST_TO_DATE, sortOrder -> new AbstractComparator<EdsBusinessGoal>() {
            public int compare(EdsBusinessGoal o1, EdsBusinessGoal o2) {
                return internalCompare(o1.getToDate(), o2.getToDate(), sortOrder);
            }
        });

        comparatorFactoriesCompanyGoal.put(GoalItem.COMPANY_GOAL_LIST_STATUS, sortOrder -> new AbstractComparator<EdsBusinessGoal>() {
            public int compare(EdsBusinessGoal o1, EdsBusinessGoal o2) {
                return internalCompare(o1.getStatus().getName(), o2.getStatus().getName(), sortOrder);
            }
        });
    }

    private static final Map<String, ComparatorFactory<EdsGoal>> comparatorFactoriesEmployeeGoal = new HashMap<>();

    static {

        comparatorFactoriesEmployeeGoal.put(GoalItem.EMPLOYEE_GOAL_LIST_GOAL_CATEGORY, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getGoalCategory().getName(), o2.getGoalCategory().getName(), sortOrder);
            }
        });

        comparatorFactoriesEmployeeGoal.put(GoalItem.EMPLOYEE_GOAL_LIST_STATUS, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getStatus().getName(), o2.getStatus().getName(), sortOrder);
            }
        });

        comparatorFactoriesEmployeeGoal.put(GoalItem.EMPLOYEE_GOAL_LIST_TITLE, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getTitle(), o2.getTitle(), sortOrder);
            }
        });

        comparatorFactoriesEmployeeGoal.put(GoalItem.EMPLOYEE_GOAL_LIST_DESCRIPTION, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getDescription(), o2.getDescription(), sortOrder);
            }
        });

        comparatorFactoriesEmployeeGoal.put(GoalItem.EMPLOYEE_GOAL_LIST_WEIGHT, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(String.valueOf(o1.getWeight()), String.valueOf(o2.getWeight()), sortOrder);
            }
        });

        comparatorFactoriesEmployeeGoal.put(GoalItem.EMPLOYEE_GOAL_LIST_ACTIONSTEPS, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getActionSteps(), o2.getActionSteps(), sortOrder);
            }
        });

        comparatorFactoriesEmployeeGoal.put(GoalItem.GOAL_NUMBER, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getNumberData(), o2.getNumberData(), sortOrder);
            }
        });
    }

    private static final Map<String, ComparatorFactory<EdsGoal>> comparatorFactoriesByGoal = new HashMap<>();

    static {
        comparatorFactoriesByGoal.put(GoalItem.GOAL_LIST_TITLE, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getTitle(), o2.getTitle(), sortOrder);
            }
        });
        comparatorFactoriesByGoal.put(GoalItem.GOAL_LIST_DESCRIPTION, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getDescription(), o2.getDescription(), sortOrder);
            }
        });
        comparatorFactoriesByGoal.put(GoalItem.GOAL_LIST_FROM_DATE, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getFromDate(), o2.getFromDate(), sortOrder);
            }
        });
        comparatorFactoriesByGoal.put(GoalItem.GOAL_LIST_TO_DATE, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getToDate(), o2.getToDate(), sortOrder);
            }
        });
        comparatorFactoriesByGoal.put(GoalItem.GOAL_LIST_WEIGHT, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getWeight(), o2.getWeight(), sortOrder);
            }
        });
        comparatorFactoriesByGoal.put(GoalItem.GOAL_LIST_PROJECT, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getProject().getName(), o2.getProject().getName(), sortOrder);
            }
        });

        comparatorFactoriesByGoal.put(GoalItem.GOAL_NUMBER, sortOrder -> new AbstractComparator<EdsGoal>() {
            public int compare(EdsGoal o1, EdsGoal o2) {
                return internalCompare(o1.getNumberData(), o2.getNumberData(), sortOrder);
            }
        });
    }

    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        EdsUser user = uploadManager.getUser();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setAllGoals(true);

        Map<String, CellData> mapColumnHeader = new HashMap<>();

        //Company Goal
        mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_TO_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.toDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_TITLE, new CellData(commonLocalizer.localize(PdfLocalizationName.title), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_FROM_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.fromDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_OUTCOME, new CellData(commonLocalizer.localize(PdfLocalizationName.outcome), Element.ALIGN_LEFT));

        //goal title
//        mapColumnHeader.put(GoalItem.GOAL_LIST_TITLE, new CellData(commonLocalizer.localize(PdfLocalizationName.title), Element.ALIGN_LEFT));

        // goal project
        mapColumnHeader.put(GoalItem.GOAL_LIST_PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));  //project name

//        // goal description
//        mapColumnHeader.put(GoalItem.GOAL_LIST_DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));

        // from date
        mapColumnHeader.put(GoalItem.GOAL_LIST_FROM_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.startDate), Element.ALIGN_LEFT));

        //to date
        mapColumnHeader.put(GoalItem.GOAL_LIST_TO_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.endDate), Element.ALIGN_LEFT));

//        //manager
//        mapColumnHeader.put(GoalItem.GOAL_LIST_RESOVER, new CellData(commonLocalizer.localize(PdfLocalizationName.manager), Element.ALIGN_LEFT));

        //period
        mapColumnHeader.put(GoalItem.GOAL_LIST_VALIDITY_PERIOD, new CellData(commonLocalizer.localize(PdfLocalizationName.validityPeriod), Element.ALIGN_LEFT));

//        //Company Goal
//        mapColumnHeader.put(GoalItem.GOAL_LIST_STRATEGIC, new CellData(commonLocalizer.localize(PdfLocalizationName.companyGoal), Element.ALIGN_LEFT));

//        //Department
//        mapColumnHeader.put(GoalItem.GOAL_LIST_DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.department), Element.ALIGN_LEFT));

//       //Weight
//        mapColumnHeader.put(GoalItem.GOAL_LIST_WEIGHT, new CellData(commonLocalizer.localize(PdfLocalizationName.weight), Element.ALIGN_LEFT));

        //AssignedTo
        mapColumnHeader.put(GoalItem.GOAL_LIST_ASSIGN, new CellData(commonLocalizer.localize(PdfLocalizationName.assignee), Element.ALIGN_LEFT));

        //Status
        mapColumnHeader.put(GoalItem.GOAL_STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));

//        mapColumnHeader.put(GoalItem.GOAL_LIST_WEIGHT, new CellData(commonLocalizer.localize(PdfLocalizationName.score), Element.ALIGN_LEFT));
//        mapColumnHeader.put(GoalItem.GOAL_LIST_RESOVER, new CellData(commonLocalizer.localize(PdfLocalizationName.manager), Element.ALIGN_LEFT));

//
        mapColumnHeader.put(GoalItem.GOAL_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_GOAL_CATEGORY, new CellData(commonLocalizer.localize(PdfLocalizationName.goalCategory), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.PROJECT_GOAL, new CellData(hrmsLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_TITLE, new CellData(commonLocalizer.localize(PdfLocalizationName.title), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_WEIGHT, new CellData(commonLocalizer.localize(PdfLocalizationName.score), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_ACTIONSTEPS, new CellData(commonLocalizer.localize(PdfLocalizationName.actionSteps), Element.ALIGN_LEFT));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_RESOLVER, new CellData(commonLocalizer.localize(PdfLocalizationName.resolver), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        ListResult<GoalItem> goalListing = new ListResult<>();
        if (StringUtils.isNotEmpty(filterParametrs.getStatusValues())) {
            filterParametrs.setAllGoals(true);
            //company goals

            if (filterParametrs.getStatusValues().equals(COMPANY_GOAL)) {

                setFileName(user.getFirstName() + "_" + user.getLastName() + "_goal_list");
                goalListing = hrmsService.getCompanyGoalList(filterParametrs);

                if (goalListing != null && goalListing.getList() != null) {

                    for (GoalItem goal : goalListing.getList()) {
                        Map<String, CellData> columnMap = new HashMap<>();

                        // company / goal name
                        if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_TITLE)) { //Goal Name
                            columnMap.put(GoalItem.COMPANY_GOAL_LIST_TITLE, new CellData(getResultOrLongDash(goal.getTitle()), Element.ALIGN_LEFT)); //get data
                        }
                        // company / description
                        if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_DESCRIPTION)) {
                            columnMap.put(GoalItem.COMPANY_GOAL_LIST_DESCRIPTION, new CellData(getResultOrLongDash(goal.getDescription()), Element.ALIGN_LEFT));
                        }
                        // company / outcome
                        if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_OUTCOME)) {
                            columnMap.put(GoalItem.COMPANY_GOAL_LIST_OUTCOME, new CellData(getResultOrLongDash(goal.getOutcome()), Element.ALIGN_LEFT));
                        }
                        // company / from date
                        if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_FROM_DATE)) {
                            columnMap.put(GoalItem.COMPANY_GOAL_LIST_FROM_DATE, goal.getFromDate() != null ? new CellData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(goal.getFromDate().getNonConvertedDate())) : dateFormat(goal.getFromDate().getNonConvertedDate(), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                        }
                        // company / to date
                        if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_TO_DATE)) {
                            columnMap.put(GoalItem.COMPANY_GOAL_LIST_TO_DATE, goal.getToDate() != null ? new CellData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(goal.getToDate().getNonConvertedDate())) : dateFormat(goal.getToDate().getNonConvertedDate(), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                        }
                        // company / validity period
                        if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_VALIDITY_PERIOD)) {
                            columnMap.put(GoalItem.GOAL_LIST_VALIDITY_PERIOD, new CellData(getResultOrLongDash(goal.getValidityPeriodItem() != null ? goal.getValidityPeriodItem().getName() : " "), Element.ALIGN_LEFT));
                        }
                        // company / status
                        if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_STATUS)) {
                            columnMap.put(GoalItem.COMPANY_GOAL_LIST_STATUS, new CellData(getResultOrLongDash(goal.getStatus()), Element.ALIGN_LEFT));
                        }


                        // goal number
                        if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_NUMBER)) {
                            columnMap.put(GoalItem.GOAL_NUMBER,new CellData(getResultOrLongDash(goal.getGoalNumber().getNumberString()), Element.ALIGN_LEFT));
                        }
                        // project name
                        if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_PROJECT)) {
                            columnMap.put(GoalItem.GOAL_LIST_PROJECT, new CellData(getResultOrLongDash(goal.getProject()), Element.ALIGN_LEFT));
                        }
                        // Department
                        if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_DEPARTMENT)) {
                            columnMap.put(GoalItem.GOAL_LIST_DEPARTMENT, new CellData(getResultOrLongDash(goal.getDepartment()), Element.ALIGN_LEFT));
                        }
                        // weighted score
                        if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_WEIGHT)) {
                            columnMap.put(GoalItem.GOAL_LIST_WEIGHT, new CellData(getResultOrLongDash(goal.getWeightString() != null ? String.valueOf(goal.getWeight()) : " "), Element.ALIGN_LEFT));
                        }
                        // project goal
                        if (panelTools.getColumnCodeName().contains(GoalItem.PROJECT_GOAL)) {
                            columnMap.put(GoalItem.PROJECT_GOAL, new CellData(getResultOrLongDash(goal.getProjectGoalTitle()), Element.ALIGN_LEFT));
                        }






                        List<CellData> columns = panelTools.getColumnCodeName().stream()
                                .filter(columnCode -> columnMap.containsKey(columnCode))
                                .map(columnCode -> columnMap.get(columnCode))
                                .collect(Collectors.toList());
                        tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
                    }
                }
            }
            else if (filterParametrs.getStatusValues().equals(PERSONAL_GOAL)) {
                goalListing = hrmsService.getPersonalGoalList(filterParametrs);
            }
            else if (filterParametrs.getStatusValues().equals(DEPARTMENT_GOAL)) {
                goalListing = hrmsService.getDepartmentGoalList(filterParametrs);
            }
            else if (filterParametrs.getStatusValues().equals(PROJECT_GOAL)) {
                goalListing = hrmsService.getProjectGoalList(filterParametrs);
            }
            else if (filterParametrs.getStatusValues().equals(BUSINESS_GOAL)) {
                goalListing = hrmsService.getBusinGoalList(filterParametrs);
            }
        } else {
            // employee goals...
            if (filterParametrs.getEmployeeId() == null) {
                filterParametrs.setEmployeeId(user.getObjectID());
            } else {
                user = userManager.get(filterParametrs.getEmployeeId());
            }
            EdsReference ref_per = referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PERSONAL_GOAL);
            List<EdsGoal> goalList = goalManager.getOwnGoalList(filterParametrs, ref_per);

            ComparatorFactory factory = StringUtils.isNotEmpty(filterParametrs.getSortField()) ? comparatorFactoriesEmployeeGoal.get(filterParametrs.getSortField()) : comparatorFactoriesEmployeeGoal.get(GoalItem.EMPLOYEE_GOAL_LIST_GOAL_CATEGORY);
            int sortDir = StringUtils.isNotEmpty(filterParametrs.getSortField()) ? filterParametrs.getSortDir() : Constants.DESC;
            goalList.sort(factory.createComparator(sortDir));

            ListLoadConfig config = new ListLoadConfig();
            if (config.getLimit() > 0) {
                goalList = ListUtils.getSublist(goalList, config.getStart(), config.getLimit());
            }

            for (EdsGoal goal : goalList) {
                Map<String, CellData> columnMap = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_NUMBER)) {
                    columnMap.put(GoalItem.GOAL_NUMBER, goal.getNumberData() != null ? new CellData(goal.getNumberData(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.EMPLOYEE_GOAL_LIST_GOAL_CATEGORY)) {
                    columnMap.put(GoalItem.EMPLOYEE_GOAL_LIST_GOAL_CATEGORY, goal.getGoalCategory().getName() != null ? new CellData(goal.getGoalCategory().getName(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.PROJECT_GOAL)) {
                    columnMap.put(GoalItem.PROJECT_GOAL, new CellData(getResultOrLongDash(goal.getProjectGoal() != null ? goal.getProjectGoal().getName() : ""), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.EMPLOYEE_GOAL_LIST_STATUS)) {
                    columnMap.put(GoalItem.EMPLOYEE_GOAL_LIST_STATUS, goal.getStatus() != null ? new CellData(referenceWfmMessageSource.localizeRef(goal.getStatus()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.EMPLOYEE_GOAL_LIST_TITLE)) {
                    columnMap.put(GoalItem.EMPLOYEE_GOAL_LIST_TITLE, new CellData(getResultOrLongDash(goal.getTitle()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.EMPLOYEE_GOAL_LIST_DESCRIPTION)) {
                    columnMap.put(GoalItem.EMPLOYEE_GOAL_LIST_DESCRIPTION, new CellData(getResultOrLongDash(goal.getDescription()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.EMPLOYEE_GOAL_LIST_WEIGHT)) {
                    columnMap.put(GoalItem.EMPLOYEE_GOAL_LIST_WEIGHT, new CellData(String.valueOf(goal.getWeight()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.EMPLOYEE_GOAL_LIST_ACTIONSTEPS)) {
                    columnMap.put(GoalItem.EMPLOYEE_GOAL_LIST_ACTIONSTEPS, goal.getActionSteps() != null && !goal.getActionSteps().equals("null") ? new CellData(goal.getActionSteps(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.EMPLOYEE_GOAL_LIST_RESOLVER)) {
                    columnMap.put(GoalItem.EMPLOYEE_GOAL_LIST_RESOLVER, goal.getResolver() != null ? new CellData(getResultOrLongDash(goal.getResolver().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }

                List<CellData> columns = panelTools.getColumnCodeName().stream()
                        .filter(columnCode -> columnMap.containsKey(columnCode))
                        .map(columnCode -> columnMap.get(columnCode))
                        .collect(Collectors.toList());
                tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
            }
        }

        //for personal, department, project, business goal
        if (goalListing != null && goalListing.getList() != null) {
            for (GoalItem goal : goalListing.getList()) {
                Map<String, CellData> columnMap = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_NUMBER)) {
                    columnMap.put(GoalItem.GOAL_NUMBER, new CellData(getResultOrLongDash(goal.getGoalNumber().getNumberString()), Element.ALIGN_LEFT));
                }

                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_TITLE)) {
                    columnMap.put(GoalItem.GOAL_LIST_TITLE, new CellData(getResultOrLongDash(goal.getTitle()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.PROJECT_GOAL)) {
                    columnMap.put(GoalItem.PROJECT_GOAL, new CellData(getResultOrLongDash(goal.getProjectGoalTitle()), Element.ALIGN_LEFT));
                }

//                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_DESCRIPTION)) {
//                    columnMap.put(GoalItem.GOAL_LIST_DESCRIPTION, new CellData(getResultOrLongDash(goal.getDescription()), Element.ALIGN_LEFT));
//                }
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_FROM_DATE)) {
                    columnMap.put(GoalItem.GOAL_LIST_FROM_DATE, goal.getFromDate() != null ? new CellData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(goal.getFromDate().getNonConvertedDate())) : dateFormat(goal.getFromDate().getNonConvertedDate()), Element.ALIGN_LEFT) : new CellData("—"));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_TO_DATE)) {
                    columnMap.put(GoalItem.GOAL_LIST_TO_DATE, goal.getToDate() != null ? new CellData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(goal.getToDate().getNonConvertedDate())) : dateFormat(goal.getToDate().getNonConvertedDate())) : new CellData("—"));
                }
//                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_WEIGHT)) {
//                    columnMap.put(GoalItem.GOAL_LIST_WEIGHT, new CellData(goal.getWeightString(), Element.ALIGN_LEFT));
//                }
//                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_RESOVER)) {
//                    columnMap.put(GoalItem.GOAL_LIST_RESOVER, new CellData(getResultOrLongDash(goal.getResolver()), Element.ALIGN_LEFT));
//                }
                    if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_STATUS)) {
                        columnMap.put(GoalItem.GOAL_STATUS, new CellData(getResultOrLongDash(goal.getStatus()), Element.ALIGN_LEFT));
                    }
//                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_DEPARTMENT)) {
//                    columnMap.put(GoalItem.GOAL_LIST_DEPARTMENT, new CellData(getResultOrLongDash(goal.getDepartment()), Element.ALIGN_LEFT));
//                }
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_PROJECT)) {
                    columnMap.put(GoalItem.GOAL_LIST_PROJECT, new CellData(getResultOrLongDash(goal.getProject()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_ASSIGN)) {
                    columnMap.put(GoalItem.GOAL_LIST_ASSIGN, new CellData(getResultOrLongDash(goal.getGoalAssignedTo()), Element.ALIGN_LEFT));
                }
//                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_VALIDITY_PERIOD)) {
//                    columnMap.put(GoalItem.GOAL_LIST_VALIDITY_PERIOD, goal.getValidityPeriodItem() != null ? new CellData(getResultOrLongDash(goal.getValidityPeriodItem().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
//                }
                CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), columnMap, panelTools.getColumnCodeName(), goal, company);

                List<CellData> columns = panelTools.getColumnCodeName().stream()
                        .filter(columnCode -> columnMap.containsKey(columnCode))
                        .map(columnCode -> columnMap.get(columnCode))
                        .collect(Collectors.toList());
                tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
            }
        }
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }


    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        if (fp.getPropertyCode().equals("businessgoal")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("businessGoal");
        } else if (fp.getPropertyCode().equals("personalgoal")) {
            return property != null ? property.getPlural() : (pdfWfmMessageSource.localize("personalGoals"));
        } else if (fp.getPropertyCode().equals("projectgoal")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("projectGoals");
        } else if (fp.getPropertyCode().equals("companygoal")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("companyGoals");
        } else if (fp.getPropertyCode().equals("departmentgoal")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("departmentGoals");
        }
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_goal_list");
    }
}
