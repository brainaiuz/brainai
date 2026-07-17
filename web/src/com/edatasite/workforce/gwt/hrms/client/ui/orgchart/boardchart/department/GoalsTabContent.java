package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.services.goal.GoalRestClient;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.goal.GoalActionMenu;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import gwt.material.design.client.ui.MaterialColumn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEPARTMENT_GOAL;

public class GoalsTabContent extends MaterialColumn {

    private final GoalRestClient restClient = new GoalRestClient();

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private final FlowPanel content;
    private final DepartmentNode department;

    public GoalsTabContent(DepartmentNode department) {
        this.department = department;
        content = new FlowPanel();

        add(content);
        init();
    }

    private void init() {
        LoadingPanel.loading(true);
        restClient.getDepartmentGoals(department.getId(), new AsyncCallback<ResultTO<List<SelectItem>>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<List<SelectItem>> result) {
                LoadingPanel.loading(false);
                content.clear();
                List<SelectItem> res = result != null && result.getData() != null ? result.getData() : new ArrayList<>();

                if (res.isEmpty()) {
                    addStyleName("is-empty"); // Добавляем маркер пустоты
                    getInitialPanel();
                } else {
                    removeStyleName("is-empty"); // Убираем маркер, если данные пришли
                    fillFields(res);
                }
            }
        });
    }

    private void fillFields(List<SelectItem> goals) {
        content.clear();

        WfmButton2 create = new WfmButton2(wfmStrings.create(), WfmButton2.BTN_PRIMARY, e -> goToCreateGoal());

// Вставляем иконку на 0-ю позицию (перед текстом)
        create.insert(new SvgIcon(SvgEnum.plusCircle), 0);
        content.add(create);

        // Обертка для списка метрик
        FlowPanel rowsWrapper = new FlowPanel();
        rowsWrapper.setStyleName("depMetricList"); // Наш новый класс-контейнер
        content.add(rowsWrapper);

        goals.sort(Comparator.comparing(SelectItem::getQtyAmount).reversed());
        SelectItem mainGoal = goals.isEmpty() ? null : goals.get(0);

        goals.forEach(g -> rowsWrapper.add(buildGoalRow(g, g == mainGoal)));
    }

    private FlowPanel buildGoalRow(SelectItem goal, boolean isMain) {
        FlowPanel row = new FlowPanel();
        row.setStyleName("depMetricRow");

        // 1. Заголовок
        Label title = new Label(goal.getName());
        title.setStyleName("depMetricRow__title");
        row.add(title);

        // 2. Обертка для иконки (для стабильного клика)
        FlowPanel iconWrapper = new FlowPanel();
        iconWrapper.setStyleName("depMetricRow__extLink"); // Добавь этот класс в CSS для отступов

        SvgIcon externalLinkIcon = new SvgIcon(SvgEnum.extLink);
        iconWrapper.add(externalLinkIcon);

        // Устанавливаем курсор через стиль элемента
        iconWrapper.getElement().getStyle().setCursor(com.google.gwt.dom.client.Style.Cursor.POINTER);

        // Используем SinkEvents и OnBrowserEvent для максимальной надежности в GWT
        iconWrapper.addDomHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                event.stopPropagation();
                if (Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_GOAL_SUMMARY)) {
                    summaryGoal(goal.getId());
                } else {
                    Info.warn(wfmStrings.youDontHavePermission());
                }
            }
        }, com.google.gwt.event.dom.client.ClickEvent.getType());

        row.add(iconWrapper);

        // 3. Остальные элементы (badge и веса)
        if (isMain) {
            Label mainBadge = new Label(hrmsStrings.mainMetric());
            mainBadge.setStyleName("badge depMetricRow__status");
            row.add(mainBadge);
        }

        if (goal.getQtyAmount() != null) {
            Label weight = new Label(goal.getQtyAmount().toString() + " / 100");
            weight.setStyleName("badge depMetricRow__weight");
            row.add(weight);
        }

        // 4. Кнопка действий (три точки)
        FlowPanel action = GoalActionMenu.createActionButton(new GoalActionMenu.ActionHandler() {
            @Override
            public void onGoalDelete() {
                deleteGoal(goal.getId());
            }

            @Override
            public void onGoalEdit() {
                editGoal(goal.getId());
            }
        });
        action.addStyleName("depMetricRow__action");
        row.add(action);

        return row;
    }

    private void getInitialPanel() {
        // Основной контейнер <figure class="addMetric">
        FlowPanel figure = new FlowPanel("figure");
        figure.setStyleName("addMetric");

        // Блок с иконкой <div class="figure-icon">
        FlowPanel iconWrapper = new FlowPanel();
        iconWrapper.setStyleName("figure-icon");

        // Вставляем твой SVG код напрямую
        String svgCode = "<svg width=\"185\" height=\"113\" viewBox=\"0 0 185 113\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">"
                + "<path fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M177.245 16.6692C181.528 16.6692 185 20.152 185 24.4482C185 28.7444 181.528 32.2272 177.245 32.2272H132.934C137.217 32.2272 140.689 35.71 140.689 40.0062C140.689 44.3024 137.217 47.7851 132.934 47.7851H157.305C161.588 47.7851 165.06 51.2679 165.06 55.5641C165.06 59.8603 161.588 63.3431 157.305 63.3431H146.035C140.635 63.3431 136.257 66.8259 136.257 71.1221C136.257 73.9862 138.473 76.5792 142.904 78.901C147.187 78.901 150.659 82.3838 150.659 86.68C150.659 90.9762 147.187 94.459 142.904 94.459H50.9581C46.6754 94.459 43.2036 90.9762 43.2036 86.68C43.2036 82.3838 46.6754 78.901 50.9581 78.901H7.75449C3.4718 78.901 0 75.4183 0 71.1221C0 66.8259 3.4718 63.3431 7.75449 63.3431H52.0659C56.3486 63.3431 59.8204 59.8603 59.8204 55.5641C59.8204 51.2679 56.3486 47.7851 52.0659 47.7851H24.3713C20.0886 47.7851 16.6168 44.3024 16.6168 40.0062C16.6168 35.71 20.0886 32.2272 24.3713 32.2272H68.6826C64.3999 32.2272 60.9281 28.7444 60.9281 24.4482C60.9281 20.152 64.3999 16.6692 68.6826 16.6692H177.245ZM177.245 47.7851C181.528 47.7851 185 51.2679 185 55.5641C185 59.8603 181.528 63.3431 177.245 63.3431C172.963 63.3431 169.491 59.8603 169.491 55.5641C169.491 51.2679 172.963 47.7851 177.245 47.7851Z\" fill=\"#EDEFF3\"/>"
                + "<path fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M118.198 15.5581L128.479 90.9282L129.402 98.4898C129.7 100.926 127.971 103.144 125.541 103.444L60.6439 111.457C58.2139 111.757 56.0029 110.025 55.7055 107.589L45.7441 26.0143C45.5953 24.7963 46.4597 23.6873 47.6747 23.5373C47.6823 23.5364 47.69 23.5355 47.6977 23.5346L53.0818 22.9271M57.4301 22.415L62.512 21.8419L57.4301 22.415Z\" fill=\"#EDEFF3\"/>"
                + "<path d=\"M119.573 15.3695C119.47 14.6098 118.77 14.0785 118.01 14.1827C117.251 14.2869 116.719 14.9872 116.823 15.7468L118.198 15.5581L119.573 15.3695ZM128.479 90.9282L129.856 90.7589C129.856 90.7525 129.855 90.746 129.854 90.7396L128.479 90.9282ZM129.402 98.4898L130.78 98.3204L129.402 98.4898ZM125.541 103.444L125.71 104.822L125.541 103.444ZM60.6439 111.457L60.4748 110.079L60.6439 111.457ZM55.7055 107.589L57.0833 107.42L55.7055 107.589ZM45.7441 26.0143L47.1219 25.845L45.7441 26.0143ZM47.6977 23.5346L47.8523 24.9141V24.9141L47.6977 23.5346ZM53.2364 24.3066C53.9982 24.2207 54.5466 23.5333 54.4612 22.7714C54.3758 22.0095 53.689 21.4616 52.9272 21.5475L53.0818 22.9271L53.2364 24.3066ZM57.2756 21.0355C56.5138 21.1214 55.9654 21.8087 56.0507 22.5706C56.1361 23.3325 56.8228 23.8805 57.5846 23.7946L57.4301 22.415L57.2756 21.0355ZM62.6665 23.2215C63.4283 23.1356 63.9767 22.4483 63.8914 21.6864C63.8061 20.9244 63.1193 20.3764 62.3575 20.4624L62.512 21.8419L62.6665 23.2215ZM118.198 15.5581L116.823 15.7468L127.103 91.1169L128.479 90.9282L129.854 90.7396L119.573 15.3695L118.198 15.5581ZM128.479 90.9282L127.101 91.0975L128.024 98.6591L129.402 98.4898L130.78 98.3204L129.856 90.7589L128.479 90.9282ZM129.402 98.4898L128.024 98.6591C128.229 100.337 127.038 101.86 125.372 102.066L125.541 103.444L125.71 104.822C128.903 104.427 131.17 101.514 130.78 98.3204L129.402 98.4898ZM125.541 103.444L125.372 102.066L60.4748 110.079L60.6439 111.457L60.8129 112.835L125.71 104.822L125.541 103.444ZM60.6439 111.457L60.4748 110.079C58.8091 110.285 57.2881 109.098 57.0833 107.42L55.7055 107.589L54.3276 107.759C54.7177 110.953 57.6188 113.229 60.8129 112.835L60.6439 111.457ZM55.7055 107.589L57.0833 107.42L47.1219 25.845L45.7441 26.0143L44.3663 26.1836L54.3276 107.759L55.7055 107.589ZM45.7441 26.0143L47.1219 25.845C47.0657 25.3847 47.3923 24.9709 47.8437 24.9151L47.6747 23.5373L47.5056 22.1595C45.5271 22.4038 44.125 24.2079 44.3663 26.1836L45.7441 26.0143ZM47.6747 23.5373L47.8437 24.9151C47.8466 24.9148 47.8494 24.9145 47.8523 24.9141L47.6977 23.5346L47.5431 22.155C47.5306 22.1564 47.5181 22.1579 47.5056 22.1595L47.6747 23.5373ZM47.6977 23.5346L47.8523 24.9141L53.2364 24.3066L53.0818 22.9271L52.9272 21.5475L47.5431 22.155L47.6977 23.5346ZM57.4301 22.415L57.5846 23.7946L62.6665 23.2215L62.512 21.8419L62.3575 20.4624L57.2756 21.0355L57.4301 22.415ZM62.512 21.8419L62.3575 20.4624L57.2756 21.0355L57.4301 22.415L57.5846 23.7946L62.6665 23.2215L62.512 21.8419Z\" fill=\"#0B71E4\"/>"
                + "<path fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M115.364 20.3022L124.691 88.628L125.53 95.4826C125.8 97.6909 124.256 99.6982 122.081 99.9661L63.9862 107.122C61.811 107.39 59.8285 105.817 59.5582 103.608L50.5296 29.8441C50.3806 28.6265 51.2467 27.5179 52.4642 27.3679L59.6457 26.4834\" fill=\"#EDEFF3\"/>"
                + "<path d=\"M70.5449 1.3877H120.992C121.803 1.38772 122.581 1.71085 123.154 2.28516L138.044 17.2129C138.615 17.7854 138.936 18.5615 138.936 19.3701V88.9053C138.936 90.5919 137.568 91.96 135.882 91.96H70.5449C68.8583 91.96 67.4902 90.5919 67.4902 88.9053V4.44238C67.4902 2.75571 68.8583 1.3877 70.5449 1.3877Z\" fill=\"#EDEFF3\" stroke=\"#0B71E4\" stroke-width=\"2.77635\"/>"
                + "<path d=\"M121.492 2.67022V15.5579C121.492 17.3992 122.98 18.8918 124.816 18.8918H133.604\" stroke=\"#0B71E4\" stroke-width=\"2.77635\" stroke-linecap=\"round\" stroke-linejoin=\"round\"/>"
                + "<path d=\"M78.6533 75.5672H107.456M78.6533 18.8918H107.456H78.6533ZM78.6533 32.2272H126.288H78.6533ZM78.6533 46.6739H126.288H78.6533ZM78.6533 61.1205H126.288H78.6533\" stroke=\"#0B71E4\" stroke-width=\"2.77635\" stroke-linecap=\"round\" stroke-linejoin=\"round\"/>"
                + "</svg>";

        // Используем InlineHTML для вставки сырого SVG
        InlineHTML svgWidget = new InlineHTML(svgCode);
        iconWrapper.add(svgWidget);
        figure.add(iconWrapper);

        // Блок подписей <figcaption>
        FlowPanel figcaption = new FlowPanel("figcaption");

        Label title = new Label(hrmsStrings.noMetrics());
        title.setStyleName("addMetric_title");

        Label subtitle = new Label(hrmsStrings.createNewOne());
        subtitle.setStyleName("addMetric_subtitle");

        figcaption.add(title);
        figcaption.add(subtitle);
        figure.add(figcaption);

        // Кнопка <button>
        WfmButton2 create = new WfmButton2(wfmStrings.create(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (department.hasEmployee()) {
                goToCreateGoal();
            } else {
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(hrmsStrings.pleaseAddEmployeeToAddMetric());
                messageBox.open();
            }
        });

        // Вставляем иконку на 0-ю позицию (перед текстом)
        create.insert(new SvgIcon(SvgEnum.plusCircle), 0);
        figure.add(create);
        content.clear();
        content.add(figure);
        content.getElement().getStyle().clearTextAlign();
    }

    private void goToCreateGoal() {
        StringBuilder history = new StringBuilder("departmentgoal|add/add//");
        history.append(DEPARTMENT_GOAL).append("/").append(department.getId());
        if (department.getLocationId() != null) {
            history.append("/").append(department.getLocationId());
        }
        SinksContainerFactory.entryPoint.onHistoryChanged(history.toString());
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SIDE_NAV_CLOSED, null, GoalsTabContent.this);
    }

    private void editGoal(Integer goalId) {
        StringBuilder history = new StringBuilder("goaledit|editgoal/");
        history.append(goalId).append("/").append(DEPARTMENT_GOAL);
        SinksContainerFactory.entryPoint.onHistoryChanged(history.toString());
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SIDE_NAV_CLOSED, null, GoalsTabContent.this);
    }

    private void summaryGoal(Integer goalId) {
        StringBuilder history = new StringBuilder("goal|summary/");
        history.append(goalId).append("/").append(DEPARTMENT_GOAL);
        SinksContainerFactory.entryPoint.onHistoryChanged(history.toString());
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SIDE_NAV_CLOSED, null, GoalsTabContent.this);
    }

    private void deleteGoal(Integer goalId) {
        final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        wfmMessageBox.setTitle(wfmStrings.warning());
        wfmMessageBox.setMessage(hrmsStrings.metricDeleteWarning());
        wfmMessageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                restClient.deleteGoal(goalId, DEPARTMENT_GOAL, new AsyncCallback<ResultTO<List<SelectItem>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(true);
                    }

                    @Override
                    public void onSuccess(ResultTO<List<SelectItem>> result) {
                        LoadingPanel.loading(true);
                        init();
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GOAL_ADD, result, GoalsTabContent.this);
                    }
                });
            }
        });

        wfmMessageBox.open();
    }

}
