package com.edatasite.workforce.gwt.team.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.ProfileMessages;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentServiceAsync;
import com.edatasite.workforce.gwt.team.client.services.client.impl.DepartmentRestClient;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.html.UnorderedList;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: sasna
 * Date: 02.04.2009
 * Time: 13:40:59
 * To change this template use File | Settings | File Templates.
 */

public class DepartmentRemovePopup extends KpiModal {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final ProfileMessages profileMessages = ProfileMessages.App.get();
    private static final DepartmentServiceAsync departmentService = DepartmentService.App.get();
    private final DepartmentRestClient departmentRestClient = new DepartmentRestClient();

    private Integer departmentId;
    private Integer employeesCount;
    private ListingPanel listPanel;
    private WfmButton2 ok;
    private WfmButton2 cancel;
    private KpiModal shell;
    private String teamName = "";
    private Command command;

    private DepartmentNode selectedDepartment;
    private FlowPanel treeContent;
    private FlowPanel content;
    private TextBox departmentTextBox;

    public DepartmentRemovePopup(Integer departmentId, Integer employeesCount, ListingPanel listPanel) {
        this.departmentId = departmentId;
        this.employeesCount = employeesCount;
        this.listPanel = listPanel;
        this.getElement().addClassName("modal--hasPop orgBoardPopup");
    }

    public interface DialogBoxClickHandler {
        void widgetSelected(ClickEvent event);
    }

    public void selectitionListener(ClickEvent event) {
        if (notice != null) {
            notice.widgetSelected(event);
        }
        init();
    }

    public DialogBoxClickHandler notice;

    public DialogBoxClickHandler getHandler() {
        return notice;
    }

    public void setHandler(DialogBoxClickHandler notice) {
        this.notice = notice;
    }

    private void init() {
        if (employeesCount != null && employeesCount > 0) {
            departmentRestClient.getDepartmentTree(new AsyncCallback<ResultTO<DepartmentNode>>() {
                @Override
                public void onFailure(Throwable caught) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(ResultTO<DepartmentNode> result) {
                    DepartmentNode rootNode = result.getData();
                    DepartmentNode currentNode = findDepartmentById(rootNode, departmentId);
                    if (currentNode != null) {
                        teamName = Optional.ofNullable(currentNode.getName()).orElse("");
                    }
                    popupView(rootNode);
                }
            });
        } else {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.deleteThe(), wfmStrings.department()));
            messageBox.setMessage(profileMessages.messageText(teamName));
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    remove(departmentId);
                }
            });
            messageBox.open();
        }
    }

    private void popupView(DepartmentNode rootNode) {
        shell = new KpiModal();
        shell.getElement().addClassName("modal--hasPop orgBoardPopup orgBoardPopup-del  orgBoardPopup-del--hasEmpl");

        shell.setTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.deleteThe(), wfmStrings.department()));
        shell.open();

        // 1. Заголовок
        Heading headerQuestion = new Heading(HeadingSize.H6);
        headerQuestion.addStyleName("sureNote");
        headerQuestion.getElement().setInnerHTML(profileMessages.messageText(teamName));

        VerticalPanelDiv formGroup = new VerticalPanelDiv();
        formGroup.addStyleName("form-group");


        FlowPanel labelContainer = new FlowPanel();
        labelContainer.addStyleName("form-group__label");
        labelContainer.getElement().setInnerHTML("<div class='div sureNote'>" + profileMessages.listBoxLabelText(teamName) + "</div>");

        VerticalPanelDiv formContent = new VerticalPanelDiv();
        formContent.addStyleName("form-group__content");

        content = new FlowPanel();
        content.setStyleName("customSelect customSelect--choosed");

        departmentTextBox = new TextBox();
        departmentTextBox.addStyleName("gwt-SuggestBox form-control");
        departmentTextBox.setPlaceHolder(wfmStrings.pleaseSelect());

        FlowPanel controlPanel = new FlowPanel();
        controlPanel.setStyleName("customControl");
        controlPanel.add(departmentTextBox);

        Span caret = new Span();
        caret.setStyleName("caret");
        controlPanel.add(caret);

        Span clearBox = new Span();
        clearBox.setStyleName("controlClear");
        clearBox.add(new SvgIcon(SvgEnum.x));
        controlPanel.add(clearBox);

        treeContent = new FlowPanel();
        treeContent.setStyleName("customSelect-dropBox");

        Set<Integer> forbiddenIds = new HashSet<>();
        forbiddenIds.add(departmentId);

        departmentTextBox.addClickHandler(event -> {
            boolean isOpen = content.getElement().getClassName().contains("is-open");
            if (isOpen) {
                content.removeStyleName("is-open");
            } else {
                content.setStyleName("is-open", true);
                String query = departmentTextBox.getText() != null ? departmentTextBox.getText().trim() : "";
                buildTree(rootNode, query, forbiddenIds, this::onDepartmentSelected);
            }
        });

        clearBox.addClickHandler(event -> {
            selectedDepartment = null;
            departmentTextBox.setText("");
            if (content.getElement().getClassName().contains("is-open")) {
                buildTree(rootNode, "", forbiddenIds, this::onDepartmentSelected);
            }
            content.removeStyleName("customSelect--choosed");
        });

        departmentTextBox.addKeyUpHandler(event -> {
            String query = departmentTextBox.getText() != null ? departmentTextBox.getText().trim() : "";
            if (query.isEmpty()) {
                selectedDepartment = null;
                content.removeStyleName("customSelect--choosed");
            }
            if (content.getElement().getClassName().contains("is-open")) {
                controlPanel.addStyleName("customControl--search");
                buildTree(rootNode, query, forbiddenIds, this::onDepartmentSelected);
            }
        });

        content.add(controlPanel);
        content.add(treeContent);

        formContent.add(content);
        formGroup.add(labelContainer);
        formGroup.add(formContent);

        shell.add(headerQuestion);
        shell.add(formGroup);

        shell.getFooter().addStyleName("text-right");
        ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY);
        cancel = new WfmButton2(wfmStrings.no(), WfmButton2.BTN_RESET);

        ok.addClickHandler(event -> {
            if (validate()) {
                ok.setEnabled(false);
                cancel.setEnabled(false);
                remove(selectedDepartment.getId());
            }
        });

        cancel.addClickHandler(event -> shell.close());

        shell.addButton(ok);
        shell.addButton(cancel);
    }

    private void onDepartmentSelected(DepartmentNode node) {
        selectedDepartment = node;
        content.removeStyleName("is-open");
        content.setStyleName("customSelect--choosed", true);
        departmentTextBox.setText(Optional.ofNullable(node.getName()).orElse("—"));
    }

    private void buildTree(DepartmentNode root, String filter, Set<Integer> forbiddenIds, NodeCallback callback) {
        treeContent.clear();
        UnorderedList ul = new UnorderedList();
        ul.addStyleName("treeFold");
        treeContent.add(ul);
        String normalizedFilter = filter == null ? "" : filter.trim().toLowerCase();
        buildNode(root, ul, forbiddenIds, normalizedFilter, callback);
    }

    private boolean buildNode(DepartmentNode node, UnorderedList parentUl, Set<Integer> forbiddenIds,
                              String filter, NodeCallback callback) {
        boolean forbidden = node.getId() != null && forbiddenIds.contains(node.getId());
        boolean hasChildren = node.getChildren() != null && !node.getChildren().isEmpty();

        String name = Optional.ofNullable(node.getName()).orElse("—");
        boolean selfMatches = filter.isEmpty() || name.toLowerCase().contains(filter);

        UnorderedList childUl = null;
        boolean anyChildVisible = false;

        if (hasChildren) {
            childUl = new UnorderedList();
            childUl.addStyleName("treeFold__childs");
            for (DepartmentNode child : node.getChildren()) {
                boolean childVisible = buildNode(child, childUl, forbiddenIds, filter, callback);
                anyChildVisible = anyChildVisible || childVisible;
            }
        }

        boolean shouldShow = selfMatches || anyChildVisible;
        if (!shouldShow) return false;

        ListItem li = new ListItem();
        li.addStyleName("treeFold__li treeFold__li--open");
        if (forbidden) {
            li.addStyleName("treeFold__li--disabled org-tree-node-disabled");
        }
        parentUl.add(li);

        FlowPanel cur = new FlowPanel();
        cur.addStyleName("treeFold__cur");
        li.add(cur);

        Span toggle = new Span();
        toggle.getElement().setAttribute("data-id", "owner");
        cur.add(toggle);

        Span label = new Span();
        label.addStyleName("treeFold__text");
        label.setText(name);
        cur.add(label);

        label.addDomHandler((ClickEvent e) -> {
            if (forbidden) return;
            callback.onSelected(node);
        }, ClickEvent.getType());

        if (hasChildren && anyChildVisible && childUl != null) {
            final UnorderedList finalChildUl = childUl;
            toggle.addStyleName("treeFold__toggle expandedElement");
            li.add(finalChildUl);
            toggle.addDomHandler((ClickEvent e) -> {
                e.getNativeEvent().preventDefault();
                e.getNativeEvent().stopPropagation();
                boolean isOpen = li.getStyleName().contains("treeFold__li--open");
                if (isOpen) {
                    li.removeStyleName("treeFold__li--open");
                    toggle.removeStyleName("expandedElement");
                    finalChildUl.getElement().getStyle().setDisplay(Style.Display.NONE);
                } else {
                    li.addStyleName("treeFold__li--open");
                    toggle.addStyleName("expandedElement");
                    finalChildUl.getElement().getStyle().setDisplay(Style.Display.BLOCK);
                }
            }, ClickEvent.getType());
        } else {
            toggle.addStyleName("treeFold__toggle treeFold__toggle--leaf");
        }

        return true;
    }

    private DepartmentNode findDepartmentById(DepartmentNode root, Integer id) {
        if (root == null || id == null) return null;
        if (id.equals(root.getId())) return root;
        if (root.getChildren() != null) {
            for (DepartmentNode child : root.getChildren()) {
                DepartmentNode found = findDepartmentById(child, id);
                if (found != null) return found;
            }
        }
        return null;
    }

    public boolean validate() {
        if (selectedDepartment != null) {
            if (departmentTextBox != null) {
                departmentTextBox.removeStyleName("x-form-invalid");
            }
            return true;
        } else {
            if (departmentTextBox != null) {
                departmentTextBox.addStyleName("x-form-invalid");
            }
            return false;
        }
    }

    public void remove(Integer defaultDepartmentId) {
        departmentService.deleteDepartment(departmentId, defaultDepartmentId, new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable caught) {
                if (shell != null) {
                    shell.close();
                }
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Boolean result) {
                if (!result) {
                    Info.show(Property.get(Constants.DEPARTMENT_LIST, settingsStrings.youCantDeleteDefaultDepartment(), wfmStrings.department()), Info.Type.WARNING);
                    if (shell != null) {
                        ok.setEnabled(true);
                        cancel.setEnabled(true);
                    }
                } else {
                    if (shell != null) {
                        shell.close();
                    }
                    if (listPanel != null) {
                        listPanel.reloadPage();
                    }
                    if (command != null) {
                        command.execute();
                    }
                    Info.show(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.department()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DEPARTMENT_DELETE, result, DepartmentRemovePopup.this);
                }
            }
        });
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    @FunctionalInterface
    private interface NodeCallback {
        void onSelected(DepartmentNode node);
    }
}
