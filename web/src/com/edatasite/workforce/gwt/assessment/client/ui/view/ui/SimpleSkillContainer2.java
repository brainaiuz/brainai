package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelectCallback;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelectShell;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 3/15/12
 * Time: 12:28 PM
 */
public class SimpleSkillContainer2 extends Composite implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final Integer employeeAssessmentID;
    private final boolean isCompetencyContainer;
    private boolean isWeighTable;
    private final boolean reviewOnly;
    private TemplateItem template;
    boolean isfromShift = false;
    private boolean overallIsRatable = true;
    private String overallStatus;
    private List<SingleSkillPanel2> skillPanelList;
    private Map<Integer, Double[]> skillRating;
    private ArrayList<Integer> skillRatingIDs;
    private SkillAssessmentElemsStruct skillStruct;
    private boolean userISEmployee;
    private boolean userISManager;
    private final AppraisalsSettingsItem settingsItem;
    private HTMLPanel contentPanel;
    private HTMLPanel addNewPanel;


    /**
     * crate constructor
     *
     * @param employeeAssessmentId  - employee assessment ID
     * @param reviewOnly            - review only
     * @param isCompetencyContainer -  isCompetencyContainer
     */
    public SimpleSkillContainer2(Integer employeeAssessmentId, boolean reviewOnly, boolean isCompetencyContainer, AppraisalsSettingsItem settingsItem) {
        this.employeeAssessmentID = employeeAssessmentId;
        this.reviewOnly = reviewOnly;
        this.isCompetencyContainer = isCompetencyContainer;
        this.settingsItem = settingsItem;

        VerticalPanel vp = new VerticalPanel();
        this.contentPanel = new HTMLPanel("");
        this.addNewPanel = new HTMLPanel("");

        vp.add(contentPanel);
        vp.add(addNewPanel);

        initWidget(vp);

        drawInitialize();
    }

    public SimpleSkillContainer2(boolean reviewOnly, boolean isCompetencyContainer,boolean isfromShift, AppraisalsSettingsItem settingsItem,TemplateItem template) {
        this.employeeAssessmentID = null;
        this.reviewOnly = reviewOnly;
        this.isCompetencyContainer = isCompetencyContainer;
        this.settingsItem = settingsItem;
        this.template = template;
        this.isfromShift = isfromShift;

        VerticalPanel vp = new VerticalPanel();
        this.contentPanel = new HTMLPanel("");
        this.addNewPanel = new HTMLPanel("");

        vp.add(contentPanel);
        vp.add(addNewPanel);

        initWidget(vp);

        drawInitialize();
    }

    public void addHeader(Widget widget) {
        contentPanel.add(widget);
    }

    public void addErrorStyleWeights() {
        for (SingleSkillPanel2 skillPanel : skillPanelList) {
            TextBox weightBox = skillPanel.getWeightBox();
            if (isWeighTable && weightBox != null) {
                weightBox.addStyleName("x-form-invalid");
            }
        }
    }

    public void clear() {
        contentPanel.clear();
    }

    public void init(SkillAssessmentElemsStruct skills) {
        this.skillStruct = skills;
        this.overallStatus = skills.getStatus();
        this.isWeighTable = skills.isWeightable();
        boolean isManager = skills.isCurrentUserReviewer();
        boolean isEmployee = skills.isCurrentUserEmployee();
        boolean isSupervisor = skills.isCurrentUserSupervisor();
        boolean isSelfUser = isManager || !isEmployee;
        this.userISManager = ((isManager || isSupervisor) && isSelfUser);

        userISEmployee = isEmployee;

        if (skillStruct.getElems() != null) {
            for (int i = 0; i < skillStruct.getElems().length; i++) {
                initSkillAssessmentRatingComments(skillStruct.getElems()[i]);
            }
        }
    }

    public void initCompetencyByTemplate(TemplateItem templateItem,InitiatedAssessmentItem assessmentItem) {
        skillStruct = new SkillAssessmentElemsStruct();
        skillStruct.setWeightable(false);
        skillStruct.setReviewerID(assessmentItem.getReviewer().getId());
        skillStruct.setReviewerName(assessmentItem.getReviewer().getName());
        skillStruct.setReviewerType(IS_MANAGER);
        skillStruct.setTurn(true);
        skillStruct.setEmployeeName(assessmentItem.getEmployee().getName());
        skillStruct.setAssessmentType("Competency");
        skillStruct.setStatus(DRAFT);
        skillStruct.setCurrentUserEmployee(false);
        skillStruct.setCurrentUserReviewer(true);
        skillStruct.setCurrentUserSupervisor(true);
        skillStruct.setElems(new SkillAssessmentElem[templateItem.getItems() != null ? templateItem.getItems().size() : 0]);


        if (templateItem.getItems() != null) {
            int counter = 0;
            for (WfmTreeItem item : templateItem.getItems()) {
                SkillAssessmentElem elem = new SkillAssessmentElem();
                elem.setSkillId(item.getId());
                elem.setSkillName(item.getName());
                elem.setSkillDescription(item.getDescription());
                elem.setWeight(item.getDoubleValue());
                skillStruct.getElems()[counter] = elem;
                initSkillAssessmentRatingComments(elem);
                counter++;
            }
        }
    }

    public void initAddNewField() {
        Anchor addNewAnchor = new Anchor("javascript:;");
        addNewAnchor.addStyleName("btn-small btn--primary skillRateItem-addNew");
        addNewAnchor.setHTML(wfmStrings.add());
        addNewAnchor.addClickHandler(event -> drawTemplateChooser());
        addNewPanel.add(addNewAnchor);
    }

    public SkillAssessmentElem[] getDataToSave(boolean isTypeSkill) {
        SkillAssessmentElem[] elements = new SkillAssessmentElem[skillPanelList.size()];
        for (int i = 0; i < skillPanelList.size(); i++) {
            elements[i] = new SkillAssessmentElem();
            SingleSkillPanel2 skillPanel = skillPanelList.get(i);

            SkillCommentItem lastCommentItem = new SkillCommentItem();

            if (userISEmployee) {
                elements[i].setTurn(EMPLOYEE_TURN);
                lastCommentItem.setEmployeeComment(skillPanel.getYourComment());
                lastCommentItem.setTypeSkill(isTypeSkill);
                elements[i].setLastRatingComment(lastCommentItem);
                elements[i].setEmployeeGrade(skillPanel.getYourGrade());
                elements[i].setEmployeeRating(skillPanel.getYourRate());
                if (isfromShift) {
                    elements[i].setSkillId(skillPanel.getSkillID());
                }
            } else {
                elements[i].setTurn(MANAGER_TURN);
                lastCommentItem.setReviewerComment(skillPanel.getYourComment());
                lastCommentItem.setTypeSkill(isTypeSkill);
                elements[i].setLastRatingComment(lastCommentItem);
                if (isfromShift) {
                    elements[i].setSkillId(skillPanel.getSkillID());
                }
                if (skillPanel.isRatable()) {
                    elements[i].setShowRadio(true);
                    elements[i].setManagersGrade(skillPanel.getYourGrade());
                    elements[i].setRaiting(skillPanel.getYourRate());
                } else {
                    elements[i].setShowRadio(false);
                    elements[i].setManagersGrade(null);
                    elements[i].setRaiting(0d);
                }
            }
            elements[i].setSkillRatingId(skillPanel.getSkillRatingID());

            TextBox weightBox = skillPanel.getWeightBox();
            double skillWeight = 0d;
            if (weightBox != null && weightBox.getText() != null && !"".equals(weightBox.getText())) {
                try {
                    skillWeight += Double.parseDouble(weightBox.getText());

                } catch (NumberFormatException ex) {
                    skillWeight += 0d;
                }
            }
            elements[i].setWeight(skillWeight);
        }
        return elements;
    }

    public void setSkillRating(Map<Integer, Double[]> skillRating) {
        this.skillRating = skillRating;
    }

    public void setSkillRatingIDs(ArrayList<Integer> skillRatingIDs) {
        this.skillRatingIDs = skillRatingIDs;
    }

    public boolean validateOverallWeight() {
        int errors = 0;
        int overallWeightCount = 0;
        for (SingleSkillPanel2 skillPanel : skillPanelList) {
            TextBox weightBox = skillPanel.getWeightBox();
            if (isWeighTable && weightBox != null && weightBox.getText() != null && !"".equals(weightBox.getText())) {
                try {
                    overallWeightCount += Double.valueOf(weightBox.getText());

                } catch (NumberFormatException ex) {
                    overallWeightCount += 0d;
                }
            }
        }
        if (isWeighTable && !skillPanelList.isEmpty() && overallWeightCount != 100) {
            errors++;
        }
        return errors == 0;
    }

    private void drawInitialize() {
        userISManager = (Utils.hasRole(ADMIN) || Utils.hasRole(DR) || Utils.hasRole(HR) || Utils.hasRole(TL));

        skillPanelList = new ArrayList<>();
    }

    private void drawTemplateChooser() {
        TreeSelectCallback treeSelectCallback = createTreeSelectCallback();
        final TreeSelectShell selectShell = createTreeSelectShell(treeSelectCallback);
        configureSelectShell(selectShell);
        setOnItemSelectedHandler(selectShell);
        selectShell.open();
        loadGroups(selectShell);
    }

    private void setOnItemSelectedHandler(TreeSelectShell selectShell) {
        selectShell.setOnItemSelected(items -> {
            BoolItem[] itemIds = new BoolItem[items.length];
            for (int i = 0; i < items.length; i++) {
                itemIds[i] = new BoolItem(items[i].getId(), overallIsRatable);
            }
            AssessmentService.App.get().addAssessmentSkills(employeeAssessmentID, itemIds, createAssessmentSkillsAsyncCallback());
        });
    }

    private TreeSelectCallback createTreeSelectCallback() {
        return (parent, command) -> AssessmentService.App.get().getSkills(parent.getItem().getId(), createSkillsAsyncCallback(parent, command));
    }

    private AsyncCallback<LinkedList<WfmTreeItem>> createSkillsAsyncCallback(NTreeSelectItem parent, Command command) {
        return new AbstractAsyncCallback<LinkedList<WfmTreeItem>>() {
            @Override
            public void failure(Throwable throwable) {
                command.execute();
            }

            @Override
            public void success(LinkedList<WfmTreeItem> result) {
                parent.addItems(result);
                command.execute();
            }
        };
    }

    private TreeSelectShell createTreeSelectShell(TreeSelectCallback treeSelectCallback) {
        final TreeSelectShell selectShell = new TreeSelectShell(wfmStrings.skills(), treeSelectCallback);
//        selectShell.setSize(300, 370);
        selectShell.getTreeSelect().setSearchText(wfmStrings.skills());
        selectShell.getTreeSelect().hideAvailablityCheckBox();
        selectShell.getTreeSelect().getSearchPanel().setVisible(false);
        selectShell.getTreeSelect().getTickAll().setValue(false);

        selectShell.addStyleName("skillsPopup");
        selectShell.getTreeSelect().getPanel().addStyleName("skillsPopupTreeSelectPanel");

        return selectShell;
    }

    private void configureSelectShell(TreeSelectShell selectShell) {
        selectShell.setOnItemSelected(this::handleItemSelected);
    }

    private void handleItemSelected(WfmTreeItem[] items) {
        BoolItem[] itemIds = new BoolItem[items.length];
        for (int i = 0; i < items.length; i++) {
            itemIds[i] = new BoolItem(items[i].getId(), overallIsRatable);
        }
        AssessmentService.App.get().addAssessmentSkills(employeeAssessmentID, itemIds, createAssessmentSkillsAsyncCallback());
    }

    private AsyncCallback<SkillRatingItem[]> createAssessmentSkillsAsyncCallback() {
        return new AbstractAsyncCallback<SkillRatingItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SkillRatingItem[] result) {
                handleSkillRatingItems(result);
                LoadingPanel.loading(false);
            }
        };
    }

    private void handleSkillRatingItems(SkillRatingItem[] result) {
        for (SkillRatingItem ratingItem : result) {
            SkillAssessmentElem elem = createSkillAssessmentElem(ratingItem);
            initSkillAssessmentRatingComments(elem);
            skillRatingIDs.add(ratingItem.getSkillId());
            double weight = elem.getWeight() != null ? elem.getWeight() : 0;
            skillRating.put(elem.getSkillId(), new Double[]{(Boolean.TRUE.equals(skillStruct.isWeightable()) ? weight : 0), elem.getRaiting()});
            Integer[] returnIntegers = new Integer[]{ratingItem.getSkillId(), 0, employeeAssessmentID, (isCompetencyContainer ? 1 : 0)};
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SKILL_WEIGHT_CHANGED, returnIntegers, SimpleSkillContainer2.this);
        }
        LoadingPanel.loading(false);
    }

    private SkillAssessmentElem createSkillAssessmentElem(SkillRatingItem ratingItem) {
        return new SkillAssessmentElem(
                ratingItem.getSkillRatingId(),
                ratingItem.getSkillDescription(),
                ratingItem.getSkillName(),
                0d,
                null,
                ratingItem.getSkillWeight(),
                null,
                null,
                ratingItem.isRateable(),
                ratingItem.getSkillId()
        );
    }

    private void loadGroups(TreeSelectShell selectShell) {
        LoadingPanel.loading(true);
        AssessmentService.App.get().getGroups(createGroupsAsyncCallback(selectShell));
    }

    private AsyncCallback<LinkedList<WfmTreeItem>> createGroupsAsyncCallback(TreeSelectShell selectShell) {
        return new AbstractAsyncCallback<LinkedList<WfmTreeItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(LinkedList<WfmTreeItem> result) {
                selectShell.addRootItems(result);
                LoadingPanel.loading(false);
            }
        };
    }


// ----- DEV ONLY helpers -------------------------------------------------------
// Force showing Delete link LOCALLY via URL param (BEFORE the #):
//   http://localhost:8080/Hrms.html?forceDelete=1#assessment/...
// Accepted values: 1 | true | yes
// On non-local hosts this flag is ignored.

    // Разрешаем форс‑показ только локально
private static boolean isLocalhost() {
        String host = Window.Location.getHostName();
        return "localhost".equals(host) || "127.0.0.1".equals(host);
    }

    // URL-параметр: ?forceDelete=1 / true / yes
private static boolean forceShowDeleteLink() {
    // читаем ?forceDelete=1 / true / yes
    String v = Window.Location.getParameter("forceDelete");
    if (v == null) v = Window.Location.getParameter("showDelete");
    if (v != null) {
        v = v.trim().toLowerCase();
        return "1".equals(v) || "true".equals(v) || "yes".equals(v);
    }
    // Дополнительно: поддержим флаг в hash (на случай, если вы добавите его после #)
    String hash = Window.Location.getHash();
    if (hash != null) {
        if (hash.startsWith("#")) hash = hash.substring(1);
        // допустим формы: #forceDelete=1, #.../forceDelete=1, #...;forceDelete=1
        String[] parts = hash.split("[/&;]");
        for (String p : parts) {
            if (p.startsWith("forceDelete=") || p.startsWith("showDelete=")) {
                String val = p.substring(p.indexOf('=') + 1).trim().toLowerCase();
                return "1".equals(val) || "true".equals(val) || "yes".equals(val);
            }
            if ("forceDelete".equals(p) || "showDelete".equals(p)) {
                return true;
            }
        }
    }
    return false;
}

private void initSkillAssessmentRatingComments(final SkillAssessmentElem skillAssessmentElem) {
    final HTMLPanel skillRatePanel = new HTMLPanel("Div", "");
    skillRatePanel.addStyleName("skillRateItem");

    overallIsRatable &= skillAssessmentElem.isShowRadio() == null || skillAssessmentElem.isShowRadio();

    // основной блок с комментом/оценкой
    final SingleSkillPanel2 singleSkillPanel = new SingleSkillPanel2(
            isCompetencyContainer, employeeAssessmentID, skillStruct, skillAssessmentElem,
            userISManager, userISEmployee, reviewOnly, false, settingsItem,isfromShift
    );
    skillRatePanel.add(singleSkillPanel);

// Show Delete when user has rights OR (we are on localhost AND forceDelete is set in URL).
// This is for local testing only; prod/dev behavior is unchanged.
    boolean hasRights = Utils.adminOrDirector() && !userISEmployee && !APPROVED.equals(overallStatus) && !reviewOnly;
    boolean debugForce = isLocalhost() && forceShowDeleteLink(); // локально + ?forceDelete=1

    if (hasRights || debugForce) {
        Anchor removeAnchor = new Anchor("javascript:;");
        removeAnchor.addStyleName("btn-small btn-flat skillRateItem-remove");
        removeAnchor.setHTML(wfmStrings.delete());

        removeAnchor.addClickHandler(event -> {
            LoadingPanel.loading(true);
            AssessmentService.App.get().removeAssessmentSkillRating(
                    employeeAssessmentID,
                    skillAssessmentElem.getSkillRatingId(),
                    isCompetencyContainer,
                    new AbstractAsyncCallback<Void>() {
                        @Override public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }
                        @Override public void success(Void result) {
                            LoadingPanel.loading(false);
                            Double[] returnDoubles = new Double[]{
                                    skillAssessmentElem.getSkillId().doubleValue(),
                                    (double) -1,
                                    employeeAssessmentID.doubleValue(),
                                    (isCompetencyContainer ? (double) 1 : (double) 0)
                            };
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SKILL_CHANGED, returnDoubles, SimpleSkillContainer2.this);
                            contentPanel.remove(skillRatePanel);
                            skillPanelList.remove(singleSkillPanel);
                        }
                    });
        });

        // Кнопку оставляем там же (в конце .skillRateItem) — вы её позиционируете CSS
        skillRatePanel.add(removeAnchor);
    }

    skillPanelList.add(singleSkillPanel);
    contentPanel.addStyleName("skills-wrapper");
    contentPanel.add(skillRatePanel);
}

    public boolean validateRateContainer() {
        int valid = 0;

        for (SingleSkillPanel2 skillPanel2 : skillPanelList) {
            if ("".equals(skillPanel2.getYourComment()) || skillPanel2.getYourRate() == null) {
                valid++;
            }
        }

        return valid == 0;
    }

}
