package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart;

import com.edatasite.workforce.gwt.core.client.PanelUtils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiViewButton;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.DepartmentCard;
import com.edatasite.workforce.gwt.profile.client.rpc.request.CreateReferenceReq;
import com.edatasite.workforce.gwt.profile.client.services.impl.ReferenceRestClient;
import com.edatasite.workforce.gwt.team.client.services.client.impl.DepartmentRestClient;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialTooltip;


/**
 * Created by Sardorbek Juraboev on 21.11.25.
 */
public class OrgBoardPage extends Composite {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private final ReferenceRestClient referenceRestClient = GWT.create(ReferenceRestClient.class);
    private final DepartmentRestClient departmentRestClient = GWT.create(DepartmentRestClient.class);

    interface Binder extends UiBinder<Widget, OrgBoardPage> {
    }

    private static final Binder uiBinder = GWT.create(Binder.class);

    @UiField
    FlowPanel companyNameRow;
    @UiField
    FlowPanel companyNameContainer;
    @UiField
    Label companyName;
    @UiField
    FocusPanel companyNameEdit;
    @UiField
    MaterialButton refreshBtn;
    @UiField
    RangeSlider zoomSlider;
    @UiField
    Label zoomLabel;
    @UiField
    FlowPanel viewButtonHost;
    @UiField
    FlowPanel descriptRow;
    @UiField
    FlowPanel descriptionContainer;
    @UiField
    Label descriptionText;
    @UiField
    FocusPanel descriptionEdit;
    @UiField
    FlowPanel centerContent;

    private KpiViewButton viewButton;
    private OrgBoardViewMenu viewMenu;
    private boolean editingDescription = false;
    private TextArea descriptionEditor;
    private String originalDescriptionText;
    private boolean editingCompanyName = false;
    private TextBox companyNameEditor;
    private String originalCompanyName;
    private FlowPanel treeWrapper;
    private int treeBaseW = 0;
    private int treeBaseH = 0;

    private double zoom = 1.0;

    private boolean isDragging = false;
    private int dragStartX = 0;
    private int dragStartScrollLeft = 0;
    private int dragStartY = 0;
    private int dragStartScrollTop = 0;

    public OrgBoardPage() {
        initWidget(uiBinder.createAndBindUi(this));

        viewButton = new KpiViewButton("Вид", SvgEnum.verticalchart2, SvgEnum.chevronDown);
        viewButton.addStyleName("btn--lightgrey");

        viewButtonHost.add(viewButton);

        viewMenu = new OrgBoardViewMenu();
        viewMenu.addAutoHidePartner(viewButton.getElement());
        viewMenu.addCloseHandler(e -> viewButton.removeStyleName("active"));

        initIcons();
        initControls();
        initDragToScroll();
        getFormData();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADDED_TO_DEPARTMENT, OrgBoardPage.this, (sender, args) -> loadDepartmentTree());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GOAL_ADD, OrgBoardPage.this, (sender, args) -> loadDepartmentTree());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DEPARTMENT_DELETE, OrgBoardPage.this, (sender, args) -> loadDepartmentTree());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ORG_BOARD_SETTINGS_UPDATED, OrgBoardPage.this, (sender, args) -> loadDepartmentTree());
    }

    private void initIcons() {
        refreshBtn.ensureDebugId("reload_button");
        refreshBtn.addStyleName("btn--icon");
        refreshBtn.addClickHandler(clickEvent -> getFormData());
        refreshBtn.add(new SvgIcon(SvgEnum.rotateCw));
        new MaterialTooltip(refreshBtn, wfmStrings.refresh()).setPosition(Position.TOP);
        descriptionEdit.setWidget(new SvgIcon(SvgEnum.editPen));
        companyNameEdit.setWidget(new SvgIcon(SvgEnum.editPen));
    }

    private void initControls() {
        zoomSlider.setValue(100, false);
        zoomLabel.setText("100%");

        zoomSlider.addValueChangeHandler(e -> {
            int value = ((Number) e.getValue()).intValue();
            zoomLabel.setText(value + "%");
            zoom = value / 100.0;
            updateZoom();
        });

        descriptionContainer.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {
                e.stopPropagation();
                if (!editingDescription) {
                    enterDescriptionEditMode();
                }
            }
        }, ClickEvent.getType());

        descriptionText.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {
                e.stopPropagation();
                if (!editingDescription) {
                    enterDescriptionEditMode();
                }
            }
        });

        companyNameContainer.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {
                e.stopPropagation();
                if (!editingCompanyName) {
                    enterCompanyNameEditMode();
                }
            }
        }, ClickEvent.getType());

        companyName.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {
                e.stopPropagation();
                if (!editingCompanyName) {
                    enterCompanyNameEditMode();
                }
            }
        });

        viewButton.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                toggleViewMenu();
            }
        }, ClickEvent.getType());



    }

    private void initDragToScroll() {
        final FlowPanel scrollContainer = centerContent.getParent() instanceof FlowPanel
                ? (FlowPanel) centerContent.getParent()
                : null;

        if (scrollContainer == null) return;

        scrollContainer.addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                Element target = event.getNativeEvent().getEventTarget().cast();
                if (isInteractiveElement(target)) return;

                isDragging = true;
                dragStartX = event.getClientX();
                dragStartScrollLeft = scrollContainer.getElement().getScrollLeft();
                dragStartY = event.getClientY();
                dragStartScrollTop = scrollContainer.getElement().getScrollTop();

                scrollContainer.addStyleName("is-dragging");

                event.preventDefault();
                event.stopPropagation();
            }
        }, MouseDownEvent.getType());

        scrollContainer.addDomHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(MouseMoveEvent event) {
                if (!isDragging) return;

                int deltaX = event.getClientX() - dragStartX;
                int deltaY = event.getClientY() - dragStartY;

                scrollContainer.getElement().setScrollLeft(dragStartScrollLeft - deltaX);
                scrollContainer.getElement().setScrollTop(dragStartScrollTop - deltaY);

                event.preventDefault();
                event.stopPropagation();
            }
        }, MouseMoveEvent.getType());

        scrollContainer.addDomHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                isDragging = false;
                scrollContainer.removeStyleName("is-dragging");

                event.preventDefault();
                event.stopPropagation();
            }
        }, MouseUpEvent.getType());

        scrollContainer.addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                if (isDragging) {
                    isDragging = false;
                    scrollContainer.removeStyleName("is-dragging");
                }
            }
        }, MouseOutEvent.getType());
    }

    private boolean isInteractiveElement(Element target) {
        String tagName = target.getTagName().toLowerCase();

        if (tagName.equals("button") || tagName.equals("a") ||
                tagName.equals("input") || tagName.equals("textarea") ||
                tagName.equals("select")) {
            return true;
        }

        if (target.hasClassName("btn") || target.hasClassName("depCard") ||
                target.hasClassName("descriptRow__fieldAct") ||
                target.getParentElement() != null &&
                        target.getParentElement().hasClassName("depCard")) {
            return true;
        }

        Element parent = target;
        for (int i = 0; i < 5 && parent != null; i++) {
            if (parent.hasClassName("depCard")) return true;
            parent = parent.getParentElement();
        }

        return false;
    }


    @Override
    protected void onAttach() {
        super.onAttach();
        PanelUtils.getBody().addClassName("customContentScroll");
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        RootPanel.getBodyElement().removeClassName("customContentScroll");
    }


    private void recalcTreeBaseSize() {
        if (treeWrapper == null) return;

        final Style s = treeWrapper.getElement().getStyle();

        final String prevTransform = s.getProperty("transform");
        final String prevOrigin = s.getProperty("transformOrigin");

        s.clearProperty("transform");
        s.clearProperty("transformOrigin");
        s.clearProperty("width");
        s.clearProperty("height");

        treeBaseW = treeWrapper.getElement().getScrollWidth();
        treeBaseH = treeWrapper.getElement().getScrollHeight();


        if (prevOrigin != null && !prevOrigin.isEmpty()) s.setProperty("transformOrigin", prevOrigin);
        if (prevTransform != null && !prevTransform.isEmpty()) s.setProperty("transform", prevTransform);
    }


    private void updateZoom() {
        if (treeWrapper == null) return;

        recalcTreeBaseSize();

        final Style s = treeWrapper.getElement().getStyle();
        s.setProperty("transformOrigin", "0 0");
        s.setProperty("transform", "scale(" + zoom + ")");

        final int w = (int) Math.ceil(treeBaseW * zoom);
        final int h = (int) Math.ceil(treeBaseH * zoom);
        s.setPropertyPx("width", w);
        s.setPropertyPx("height", h);
    }

    private void toggleViewMenu() {
        if (viewMenu.isShowing()) {
            viewMenu.hide();
            viewButton.removeStyleName("active");
            return;
        }

        viewButton.addStyleName("active");

        viewMenu.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            int top = viewButton.getAbsoluteTop();

            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                if (offsetHeight + viewButton.getOffsetHeight() < Window.getClientHeight() - top) {
                    viewMenu.setPopupPosition(viewButton.getAbsoluteLeft() - 154, top + viewButton.getOffsetHeight());
                } else {
                    viewMenu.setPopupPosition(viewButton.getAbsoluteLeft() - 154, top - offsetHeight);
                }
            }
        });

        viewMenu.show();
    }

    private void resizeEditor() {
        if (descriptionEditor == null) return;

        Style editorStyle = descriptionEditor.getElement().getStyle();
        Style containerStyle = descriptionContainer.getElement().getStyle();

        editorStyle.setProperty("height", "auto");

        int finalHeight = descriptionEditor.getElement().getScrollHeight() + 2;

        editorStyle.setPropertyPx("height", finalHeight);
        containerStyle.setPropertyPx("height", finalHeight + 2);
    }

    @UiHandler("descriptionEdit")
    void onDescriptionEditClick(ClickEvent e) {
        e.stopPropagation();
        if (!editingDescription) {
            enterDescriptionEditMode();
        } else {
            saveDescriptionEdit();
        }
    }

    private void enterDescriptionEditMode() {
        editingDescription = true;
        descriptRow.addStyleName("active");

        int currentHeight = descriptionContainer.getElement().getOffsetHeight();
        descriptionContainer.getElement().getStyle().setPropertyPx("height", currentHeight);

        originalDescriptionText = descriptionText.getText();

        descriptionEditor = new TextArea();
        descriptionEditor.getElement().setAttribute("rows", "1");
        descriptionEditor.setText(originalDescriptionText);
        descriptionEditor.setStyleName("form-control");
        descriptionEditor.addStyleName("descriptRow-textInput");

        descriptionContainer.clear();
        descriptionContainer.add(descriptionEditor);

        descriptionEditor.getElement().getStyle().setProperty("minHeight", "unset");

        resizeEditor();

        Scheduler.get().scheduleDeferred(this::resizeEditor);

        descriptionEditor.addKeyUpHandler(event -> resizeEditor());
        descriptionEditor.setFocus(true);
        descriptionEditor.selectAll();

        descriptionEditor.addKeyDownHandler(event -> {
            int keyCode = event.getNativeKeyCode();
            if (keyCode == KeyCodes.KEY_ENTER) {
                saveDescriptionEdit();
            } else if (keyCode == KeyCodes.KEY_ESCAPE) {
                cancelDescriptionEdit();
            }
        });

        descriptionEdit.setWidget(new SvgIcon(SvgEnum.check));
        descriptionEdit.setTitle("Сохранить описание");
    }

    private void saveDescriptionEdit() {
        if (!editingDescription) return;
        editingDescription = false;

        descriptRow.removeStyleName("active");

        String newValue = descriptionEditor.getText();
        descriptionText.setText(newValue != null ? newValue : "");

        descriptionContainer.clear();
        descriptionContainer.add(descriptionText);

        descriptionEdit.setWidget(new SvgIcon(SvgEnum.editPen));
        descriptionEdit.setTitle("Редактировать описание");

        CreateReferenceReq dto = new CreateReferenceReq();
        dto.setDescription(descriptionText.getText());
        dto.setCode("ORG_STRUCTURE");

        referenceRestClient.updateReferenceByCode(dto, new AsyncCallback<ResultTO<ReferenceItem>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ResultTO<ReferenceItem> result) {
            }
        });
    }

    private void cancelDescriptionEdit() {
        if (!editingDescription) return;
        editingDescription = false;

        descriptRow.removeStyleName("active");

        descriptionText.setText(originalDescriptionText != null ? originalDescriptionText : "");

        descriptionContainer.clear();
        descriptionContainer.add(descriptionText);

        descriptionEdit.setWidget(new SvgIcon(SvgEnum.editPen));
        descriptionEdit.setTitle("Редактировать описание");
    }

    @UiHandler("companyNameEdit")
    void onCompanyNameEditClick(ClickEvent e) {
        e.stopPropagation();
        if (!editingCompanyName) {
            enterCompanyNameEditMode();
        } else {
            saveCompanyNameEdit();
        }
    }

// Константа для горизонтального padding input (left + right) в пикселях
// У вас в SCSS: $control-padding-x: .769230rem ≈ 9px при 12px шрифте
// Суммарный padding = 12px * 2 = 24px
private static final int INPUT_HORIZONTAL_PADDING = 17; //1px less

private void enterCompanyNameEditMode() {
    if (editingCompanyName) return;

    editingCompanyName = true;
    companyNameRow.addStyleName("active");

    originalCompanyName = companyName.getText();

    // === 1. Измеряем ширину label ===
    int labelWidth = companyName.getOffsetWidth();
    if (labelWidth < 45) labelWidth = 45; // страховка

    // Ширина контейнера = ширина текста + паддинги input + небольшой запас (5px для курсора)
    final int initialWidth = labelWidth + INPUT_HORIZONTAL_PADDING + 5;

    // === 2. Устанавливаем ширину контейнера ===
    Style containerStyle = companyNameContainer.getElement().getStyle();
    containerStyle.setProperty("width", initialWidth + "px");
    containerStyle.setProperty("minWidth", initialWidth + "px");

    // === 3. Создаём input ===
    companyNameEditor = new TextBox();
    companyNameEditor.setValue(originalCompanyName != null ? originalCompanyName : "");
    companyNameEditor.setStyleName("form-control orgBoardCompany__input");

    // === 4. Заменяем label на input ===
    companyName.setVisible(false);
    companyNameContainer.add(companyNameEditor);

    // === 5. Фокус ===
    Scheduler.get().scheduleDeferred(() -> {
        companyNameEditor.setFocus(true);
        companyNameEditor.selectAll();
    });

    // === 6. Авто-расширение и сжатие ===
    companyNameEditor.addKeyUpHandler(event -> {
        String text = companyNameEditor.getValue();
        if (text == null) text = "";

        // Измеряем ширину текста с помощью временного span
        Element measureSpan = DOM.createSpan();
        measureSpan.setInnerText(text);
        measureSpan.getStyle().setProperty("fontSize", "inherit");
        measureSpan.getStyle().setProperty("fontWeight", "500");
        measureSpan.getStyle().setProperty("fontFamily", "inherit");
        measureSpan.getStyle().setProperty("visibility", "hidden");
        measureSpan.getStyle().setProperty("position", "absolute");
        measureSpan.getStyle().setProperty("whiteSpace", "pre");
        measureSpan.getStyle().setProperty("padding", "0");
        measureSpan.getStyle().setProperty("margin", "0");
        measureSpan.getStyle().setProperty("border", "none");

        companyNameEditor.getElement().getParentElement().appendChild(measureSpan);
        int textWidth = measureSpan.getOffsetWidth();
        companyNameEditor.getElement().getParentElement().removeChild(measureSpan);

        // Нужная ширина = ширина текста + паддинги input + запас 12px (для курсора и комфорта)
        int neededWidth = Math.max(initialWidth, textWidth + INPUT_HORIZONTAL_PADDING + 12);

        int currentWidth = companyNameContainer.getOffsetWidth();
        if (Math.abs(neededWidth - currentWidth) >= 3) {
            containerStyle.setProperty("width", neededWidth + "px");
            containerStyle.setProperty("minWidth", neededWidth + "px");
        }
    });

    // === 7. Обработка клавиш ===
    companyNameEditor.addKeyDownHandler(event -> {
        int key = event.getNativeKeyCode();
        if (key == KeyCodes.KEY_ENTER) {
            saveCompanyNameEdit();
        } else if (key == KeyCodes.KEY_ESCAPE) {
            cancelCompanyNameEdit();
        }
    });

    companyNameEdit.setWidget(new SvgIcon(SvgEnum.check));
    companyNameEdit.setTitle("Сохранить название");
}

private void saveCompanyNameEdit() {
    if (!editingCompanyName) return;
    editingCompanyName = false;

    companyNameRow.removeStyleName("active");

    String newValue = companyNameEditor.getText();
    companyName.setText(newValue != null ? newValue : "");

    // Удаляем input, показываем label
    companyNameContainer.remove(companyNameEditor);
    companyName.setVisible(true);

    // Полный сброс стилей контейнера
    Style containerStyle = companyNameContainer.getElement().getStyle();
    containerStyle.clearProperty("width");
    containerStyle.clearProperty("minWidth");

    companyNameEdit.setWidget(new SvgIcon(SvgEnum.editPen));
    companyNameEdit.setTitle("Редактировать название");

    companyNameEditor = null;

    // Сохранение на сервер
    CreateReferenceReq dto = new CreateReferenceReq();
    dto.setName(companyName.getText());
    dto.setCode("ORG_STRUCTURE_NAME");
    referenceRestClient.updateReferenceByCode(dto, new AsyncCallback<ResultTO<ReferenceItem>>() {
        @Override
        public void onFailure(Throwable caught) {}
        @Override
        public void onSuccess(ResultTO<ReferenceItem> result) {}
    });
}

private void cancelCompanyNameEdit() {
    if (!editingCompanyName) return;
    editingCompanyName = false;

    companyNameRow.removeStyleName("active");

    companyName.setText(originalCompanyName != null ? originalCompanyName : "");

    // Удаляем input, показываем label
    companyNameContainer.remove(companyNameEditor);
    companyName.setVisible(true);

    // Полный сброс стилей контейнера
    Style containerStyle = companyNameContainer.getElement().getStyle();
    containerStyle.clearProperty("width");
    containerStyle.clearProperty("minWidth");

    companyNameEdit.setWidget(new SvgIcon(SvgEnum.editPen));
    companyNameEdit.setTitle("Редактировать название");

    companyNameEditor = null;
}

// Вспомогательный метод
private int parsePixelValue(String value) {
    if (value == null || value.isEmpty()) return 0;
    try {
        if (value.endsWith("px")) {
            return Integer.parseInt(value.substring(0, value.length() - 2));
        } else {
            return Integer.parseInt(value);
        }
    } catch (NumberFormatException e) {
        return 0;
    }
}

    private void getFormData() {
        referenceRestClient.getOrCreateOrgBoardReference("ORG_STRUCTURE_NAME", new AsyncCallback<ResultTO<ReferenceItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                loadOrgStructureData(null);
            }

            @Override
            public void onSuccess(ResultTO<ReferenceItem> result) {
                String name = (result != null && result.getData() != null) ? result.getData().getName() : null;
                loadOrgStructureData(name);
            }
        });
    }

    private void loadOrgStructureData(String orgStructureName) {
        referenceRestClient.getOrCreateOrgBoardReference("ORG_STRUCTURE", new AsyncCallback<ResultTO<ReferenceItem>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ResultTO<ReferenceItem> result) {
                if (result != null && result.getData() != null) {
                    ReferenceItem data = result.getData();
                    companyName.setText(orgStructureName != null ? orgStructureName : data.getName());
                    descriptionText.setText(data.getDescription());
                    loadDepartmentTree();
                }
            }
        });
    }

    private void loadDepartmentTree() {
        LoadingPanel.loading(true);
        departmentRestClient.getDepartmentTree(new AsyncCallback<ResultTO<DepartmentNode>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<DepartmentNode> result) {
                LoadingPanel.loading(false);
                if (result != null && result.getData() != null) {
                    drawDepartmentTree(result.getData());
                }
            }
        });
    }

    private void drawDepartmentTree(DepartmentNode rootNode) {
        centerContent.clear();
        DepartmentCard rootCard = new DepartmentCard(
                rootNode,
                rootNode,
                true,
                this::drawDepartmentTree
        );
        treeWrapper = new FlowPanel();
        treeWrapper.setStyleName("depTree");
        treeWrapper.add(rootCard);
        centerContent.add(treeWrapper);

        Scheduler.get().scheduleDeferred(this::updateZoom);
    }

}
