package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.request.CreateReferenceReq;
import com.edatasite.workforce.gwt.profile.client.services.impl.ReferenceRestClient;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Div;

import java.util.Arrays;
import java.util.List;

public class OrgSetupModal extends Composite {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    interface Binder extends UiBinder<Widget, OrgSetupModal> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    private final ReferenceRestClient referenceRestClient = new ReferenceRestClient();

    @UiField
    HTMLPanel root;
    @UiField
    SimplePanel overlay;
    @UiField
    FlowPanel closeIcon;

    // color picker
    @UiField
    FlowPanel colorList;
    @UiField
    Div colorCurrent;

    @UiField
    Button colorBtn1;
    @UiField
    Button colorBtn2;
    @UiField
    Button colorBtn3;
    @UiField
    Button colorBtn4;
    @UiField
    Button colorBtn5;
    @UiField
    Button colorBtn6;
    @UiField
    Button colorBtn7;
    @UiField
    Button colorBtn8;
    @UiField
    Button colorBtn9;
    @UiField
    Button colorBtn10;

    @UiField
    TextBox nameInput;
    @UiField
    TextBox descInput;

    @UiField
    WfmButton2 createBtn;
    @UiField
    WfmButton2 cancelBtn;

    private static final String[] COLORS = new String[]{
            "#E3E3E3",
            "#D9F5FF",
            "#FFE3D0",
            "#DDFDD7",
            "#ECE6FF",
            "#FFF6CF",
            "#E5EAFF",
            "#FFE0F5",
            "#DDFDD7",
            "#D9F5FF"
    };

    private List<Button> colorButtons;

    public OrgSetupModal() {
        initWidget(binder.createAndBindUi(this));

        SvgIcon closeSvg = new SvgIcon(SvgEnum.x);
        closeIcon.add(closeSvg);

        colorButtons = Arrays.asList(
                colorBtn1, colorBtn2, colorBtn3, colorBtn4, colorBtn5,
                colorBtn6, colorBtn7, colorBtn8, colorBtn9, colorBtn10
        );

        initColorButtons();
        initHandlers();
        updateCreateState();
        createBtn.setText(wfmStrings.create());
        cancelBtn.setText(wfmStrings.cancel());
    }

    private void initColorButtons() {
        for (int i = 0; i < colorButtons.size(); i++) {
            Button b = colorButtons.get(i);
            String color = COLORS[i];

            b.getElement().setAttribute("type", "button");
            b.getElement().setAttribute("data-color", color);
            b.getElement().getStyle().setBackgroundColor(color);

            if (b.getStyleName().contains("active")) {
                colorCurrent.getElement().getStyle().setBackgroundColor(color);
            }
        }
    }

    private void initHandlers() {
        ClickHandler colorClick = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Button clicked = (Button) event.getSource();
                onColorClicked(clicked);
            }
        };
        for (Button b : colorButtons) {
            b.addClickHandler(colorClick);
        }
        nameInput.addKeyUpHandler(event -> updateCreateState());
        descInput.addKeyUpHandler(event -> updateCreateState());

        ClickHandler closeHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                event.preventDefault();
                close();
            }
        };
        overlay.addDomHandler(closeHandler, ClickEvent.getType());
        closeIcon.addDomHandler(closeHandler, ClickEvent.getType());
        cancelBtn.addClickHandler(closeHandler);

        createBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (createBtn.getStyleName().contains("disabled")) {
                    return;
                }
                event.preventDefault();

                String name = nameInput.getText();
                String desc = descInput.getText();
                String color = getSelectedColor();

                CreateReferenceReq req = new CreateReferenceReq();
                req.setName(name);
                req.setDescription(desc);
                req.setColor(color);
                req.setCode("ORG_STRUCTURE");

                referenceRestClient.createReference(req, new AsyncCallback<ResultTO<ReferenceItem>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void onSuccess(ResultTO<ReferenceItem> result) {
                        if (result != null && result.getData() != null) {
                            close();
                            SinksContainerFactory.entryPoint.onHistoryChanged("orgBoard|add/add");
                        }
                    }
                });
            }
        });
    }

    private void onColorClicked(Button clicked) {
        for (Button b : colorButtons) {
            b.removeStyleName("active");
        }
        clicked.addStyleName("active");

        String color = clicked.getElement().getAttribute("data-color");
        if (color != null && !color.isEmpty()) {
            colorCurrent.getElement().getStyle().setBackgroundColor(color);
        }

        updateCreateState();
    }

    private void updateCreateState() {
        boolean hasName = hasText(nameInput);
        boolean hasDesc = hasText(descInput);
        boolean hasColor = hasActiveColor();

        if (hasName && hasDesc && hasColor) {
            createBtn.removeStyleName("disabled");
        } else {
            createBtn.addStyleName("disabled");
        }
    }

    private boolean hasText(TextBox tb) {
        String t = tb.getText();
        return t != null && t.trim().length() > 0;
    }

    private boolean hasActiveColor() {
        for (Button b : colorButtons) {
            if (b.getStyleName().contains("active")) {
                return true;
            }
        }
        return false;
    }

    public String getSelectedColor() {
        for (Button b : colorButtons) {
            if (b.getStyleName().contains("active")) {
                return b.getElement().getAttribute("data-color");
            }
        }
        return null;
    }

    public void open() {
        root.addStyleName("active");
        RootLayoutPanel.get().add(this);
        overlay.getElement().getStyle().clearDisplay();
        nameInput.setFocus(true);
    }

    public void close() {
        root.removeStyleName("active");
        RootLayoutPanel.get().remove(this);
    }
}