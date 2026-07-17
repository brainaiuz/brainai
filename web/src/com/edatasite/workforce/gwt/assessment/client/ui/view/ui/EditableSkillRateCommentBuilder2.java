package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsScoreTypeItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import java.util.*;

/**
 * User: Ilhombek
 * Date: 3/17/12
 * Time: 4:24 PM
 */
public class EditableSkillRateCommentBuilder2 extends Composite {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private TextArea2 commentArea;
    private String lastComment;
    private Integer int_employeeAssessmentID;
    private boolean isCompetencyContainer;
    private String savedAsDraftComment;
    private String lastRate;
    private String lastGrade;
    private Double selectedRate;
    private boolean isStaticRateBuilder;
    private VerticalPanelDiv ratePickerPanel;
    private boolean ratable = true;
    private boolean simple = true;
    private boolean isManager;
    private Integer skillID;
    private Map<String, List<String>> rateByGrade;
    private DataListBox rateSelector;

    @UiField
    HTMLPanel commentAreaField;
    @UiField
    HTMLPanel rateBuilderField;
    private AppraisalsSettingsItem settingsItem;

    interface EditableSkillRateCommentBuilder2UiBinder extends UiBinder<HTMLPanel, EditableSkillRateCommentBuilder2> {
    }

    /**
     * Create default constructor
     */
    public EditableSkillRateCommentBuilder2() {
        EditableSkillRateCommentBuilder2UiBinder ourUiBinder = GWT.create(EditableSkillRateCommentBuilder2UiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    /**
     * Create constructor with params
     *
     * @param isCompetencyContainer    - isCompetencyContainer
     * @param int_employeeAssessmentID - employee assessment ID
     * @param savedAsDraftComment      - last saved as draft comment
     * @param lastComment              - last comment
     * @param lastRate                 - last rate
     * @param ratable                  - ratable
     * @param averageRate              - average rate
     * @param isStaticRateBuilder      - isStatic rateBuilder
     * @param skillID                  - skillID
     */
    public EditableSkillRateCommentBuilder2(boolean isCompetencyContainer, Integer int_employeeAssessmentID, String savedAsDraftComment, String lastComment, String lastGrade, boolean ratable, double averageRate, boolean isStaticRateBuilder, Integer skillID, AppraisalsSettingsItem settingsItem) {
        this();
        this.isCompetencyContainer = isCompetencyContainer;
        this.int_employeeAssessmentID = int_employeeAssessmentID;
        this.savedAsDraftComment = savedAsDraftComment;
        this.lastComment = lastComment;
        this.lastGrade = lastGrade;
        this.ratable = ratable;
        this.simple = false;
        this.isStaticRateBuilder = isStaticRateBuilder;
        this.skillID = skillID;
        this.settingsItem = settingsItem;
        drawInitialize();
    }

    /**
     * Create constructor with params
     *
     * @param isCompetencyContainer    - isCompetencyContainer
     * @param int_employeeAssessmentID - employee assessment ID
     * @param savedAsDraftComment      - last saved as draft comment
     * @param lastComment              - last comment
     * @param lastGrade                 - last grade
     * @param weight                   - weight
     * @param ratable                  - ratable
     * @param skillID                  - skillID
     */
    public EditableSkillRateCommentBuilder2(boolean isCompetencyContainer, Integer int_employeeAssessmentID, String savedAsDraftComment, String lastComment, String lastGrade, Double weight, boolean ratable, Integer skillID, AppraisalsSettingsItem settingsItem, boolean isManager) {
        this(isCompetencyContainer, int_employeeAssessmentID, savedAsDraftComment, lastComment, lastGrade, weight, ratable, false, skillID, settingsItem);
        this.isManager = isManager;
        drawInitialize();
    }


    public EditableSkillRateCommentBuilder2(boolean isCompetencyContainer, Integer int_employeeAssessmentID, String savedAsDraftComment, String lastComment, String grade, Double weight, boolean ratable, boolean isStaticRateBuilder, Integer skillID, AppraisalsSettingsItem settingsItem, boolean isManager) {
        this(isCompetencyContainer, int_employeeAssessmentID, savedAsDraftComment, lastComment, grade, ratable, isStaticRateBuilder, skillID, settingsItem);
        this.isManager = isManager;
    }

    /**
     * Create constructor with params
     *
     * @param isCompetencyContainer    - isCompetencyContainer
     * @param int_employeeAssessmentID - employee assessment ID
     * @param savedAsDraftComment      - last saved as draft comment
     * @param lastComment              - last comment
     * @param lastGrade                 - last grade
     * @param weight                   - weight
     * @param ratable                  - ratable
     * @param isStaticRateBuilder      - isStatic rateBuilder
     * @param skillID                  - skillID
     */
    public EditableSkillRateCommentBuilder2(boolean isCompetencyContainer, Integer int_employeeAssessmentID, String savedAsDraftComment, String lastComment, String lastGrade, Double weight, boolean ratable, boolean isStaticRateBuilder, Integer skillID, AppraisalsSettingsItem settingsItem) {
        this();
        this.isCompetencyContainer = isCompetencyContainer;
        this.int_employeeAssessmentID = int_employeeAssessmentID;
        this.savedAsDraftComment = savedAsDraftComment;
        this.lastComment = lastComment;
        this.lastGrade = lastGrade;
        this.ratable = ratable;
        this.isStaticRateBuilder = isStaticRateBuilder;
        this.skillID = skillID;
        this.settingsItem = settingsItem;
    }

    /**
     * Create constructor with params
     *
     * @param isCompetencyContainer    - isCompetencyContainer
     * @param int_employeeAssessmentID - employee assessment ID
     * @param savedAsDraftComment      - last saved as draft comment
     * @param lastComment              - last comment
     * @param grade                 - last grade
     * @param ratable                  - ratable
     * @param isStaticRateBuilder      - isStatic rateBuilder
     * @param skillID                  - skillID
     */
    public EditableSkillRateCommentBuilder2(boolean isCompetencyContainer, Integer int_employeeAssessmentID, String savedAsDraftComment, String lastComment, String grade, boolean ratable, boolean isStaticRateBuilder, Integer skillID, AppraisalsSettingsItem settingsItem) {
        this();
        this.isCompetencyContainer = isCompetencyContainer;
        this.int_employeeAssessmentID = int_employeeAssessmentID;
        this.savedAsDraftComment = savedAsDraftComment;
        this.lastComment = lastComment;
        this.lastGrade = grade;
        this.ratable = ratable;
        this.isStaticRateBuilder = isStaticRateBuilder;
        this.skillID = skillID;
        this.settingsItem = settingsItem;
        drawInitialize();
    }

    public String getComment() {
        return commentArea.getText();
    }

    public Double getRate() {
        if (selectedRate == null) {
            Info.show("Rate has not sat yet, please set from Settings", Info.Type.WARNING);
            return null;
        }
        return selectedRate;
    }

    public String getGrade() {
        SelectItem[] items = rateSelector.getItems();
        if (items == null || items.length == 0) {
            Info.show("Grade has not sat yet, please set from Settings", Info.Type.WARNING);
            return null;
        }
        return rateSelector.getSelectedItem().getName();
    }

    public void setEnabledComment(boolean isDisable) {
        commentArea.setEnabled(!isDisable);
    }

    public void showRatePicker(boolean isShow) {
        if (isShow) {
            ratePickerPanel.setStyleName("show-rate");
        } else {
            ratePickerPanel.setStyleName("hide-rate");
        }
    }

private void drawInitialize() {
    // Список значений для селекта
    getRateSelector();

    // 1) Группа "Comment"
    // Контентом будет панель с TextArea и счетчиком (без заголовка)
    HTMLPanel commentContent = getCommentPanelDiv();
    String commentLabel =  hrmsStrings.managersComment();
    FormGroup commentGroup = new FormGroup(commentLabel, commentContent);
    commentGroup.addStyleName("editableSkillRate"); // опционально, если хотите общий класс на группу

    // 2) Группа "Rate" (только если ratable)
    if (ratable) {
        VerticalPanelDiv rateContent = new VerticalPanelDiv();

        // Если раньше рядом с rate был селект — оставляем его
        if (!simple) {
            if (lastGrade != null) {
                rateSelector.setSelectedByValue(lastGrade);
            }
            rateContent.add(rateSelector);
        }

        // Панель с текущим названием оценки/оценкой (без заголовка)
        rateContent.add(getRatePicker(lastGrade));

        String rateLabel = hrmsStrings.managersRate();
        FormGroup rateGroup = new FormGroup(rateLabel, rateContent);
        rateGroup.addStyleName("editableSkillRate"); // опционально

        rateBuilderField.clear();
        rateBuilderField.add(rateGroup);
    } else {
        rateBuilderField.clear();
    }

    // Вставляем группу "Comment"
    commentAreaField.clear();
    commentAreaField.add(commentGroup);
}

private HTMLPanel getCommentPanelDiv() {
    HTMLPanel commentPanelDiv = new HTMLPanel("");

    commentArea = new TextArea2(); // ваш виджет с счетчиком
    // Граница как раньше
    commentArea.getTextArea().getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
    commentArea.getTextArea().getElement().getStyle().setBorderColor("#000000");
    commentArea.getTextArea().getElement().getStyle().setBorderWidth(1, Style.Unit.PX);

    // ВАЖНО: ширина теперь 100%, никаких 500px
    commentArea.getTextArea().getElement().getStyle().setWidth(100, Style.Unit.PCT);
    // Высоту можно оставить фиксированной
    commentArea.setHeight(70);

    setEnabledComment(isStaticRateBuilder);

    // Если статус "saved as draft"
    if (lastComment != null) {
        commentArea.setText(lastComment);
    }

    // Никаких заголовков через HTML(<b class="customTitle">…). Заголовок теперь рендерит FormGroup.
    commentPanelDiv.add(commentArea);

    return commentPanelDiv;
}

    private String formatString(String text) {
        return text != null && !"".equals(text) ? text.replace("\r\n", "<br/>").replace("\n", "<br/>") : text;
    }

private VerticalPanelDiv getRatePicker(String grade) {
    final Label rateName = new Label();   // было: HTML rateName = new HTML();
    String rateTitle = "";

    if (grade != null) {
        rateSelector.setSelectedByValue(grade);
        selectedRate = Double.valueOf(rateByGrade.get(grade).get(0));
        rateTitle = rateByGrade.get(grade).get(1);
    }

    ratePickerPanel = new VerticalPanelDiv();

    // УДАЛИТЬ заголовок типа:
    // ratePickerPanel.add(new HTML(AssessmentHelper.getCustomTITLE(isManager ? hrmsStrings.managersRate() : hrmsStrings.employeesRate())));
    // Заголовок теперь формирует FormGroup. Здесь только содержимое.

    // Текущее название выбранной градации — обычным текстом
    rateName.setText(rateTitle);
    ratePickerPanel.add(rateName);

    rateSelector.addValueChangeHandler(event -> {
        String gradeName = rateSelector.getSelectedItem().getName();
        selectedRate = Double.valueOf(rateByGrade.get(gradeName).get(0));
        String title = rateByGrade.get(gradeName).get(1);
        // БЕЗ AssessmentHelper.getCustomTITLE(...)
        rateName.setText(title);

        Double[] returnDoubles = new Double[]{skillID.doubleValue(), selectedRate, int_employeeAssessmentID.doubleValue(), (isCompetencyContainer ? (double) 1 : (double) 0)};
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SKILL_CHANGED, returnDoubles, EditableSkillRateCommentBuilder2.this);
    });


    if (isStaticRateBuilder) {
        rateSelector.setEnabled(false);
    } else {
        rateSelector.setEnabled(true);
    }

    ratePickerPanel.add(rateSelector);
    ratePickerPanel.add(rateName);
    return ratePickerPanel;
}

    private DataListBox getRateSelector() {
        rateSelector = new DataListBox();
        rateByGrade = new HashMap<>();
        List<SelectItem> selectItemList = new ArrayList<>();

        int i = 0;
        for (AppraisalsScoreTypeItem item : settingsItem.getScoreTypeItems()) {
            rateByGrade.put(item.getGrade(), new ArrayList<>(Arrays.asList(String.valueOf(item.getRate()), item.getName())));
            selectItemList.add(new SelectItem(i, item.getGrade()));
            i++;
        }

        rateSelector.setItems(selectItemList.toArray(new SelectItem[]{}));
        return rateSelector;
    }
}
