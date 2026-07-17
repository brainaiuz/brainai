package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.NewPosition;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PositionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PositionPanel extends Composite {


    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private WfmButton2 button_cancel;
    private WfmButton2 button_save;
    private PositionLookUp position;
    private PositionProvider provider;
    private Widget additionalWidget;
    private final boolean canAddPosition = Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_POSITION);

    public PositionPanel() {
        init();
    }

    public PositionPanel(PositionProvider provider, Widget additionalWidget) {
        this.provider = provider;
        this.additionalWidget = additionalWidget;
        init();
    }

    public void clearSelected() {
        if (position != null) {
            position.clear();
        }
    }

    public PositionLookUp getPosition() {
        return position;
    }

    public Integer getSelectedPosition() {
        if (position != null) {
            return position.getSelectedItemID();
        }
        return null;
    }

    public String getSelectedPositionName() {
        if (position != null) {
            return position.getSelectedItem().getName();
        }
        return null;
    }

    public void setSelectedPosition(Integer id) {
        if (position != null) {
            position.setSelected(id);
        }
    }

    private void addPositionClicked() {
        final KpiModal shell = new KpiModal();
        shell.setTitle(wfmStrings.add() + " " + Property.get("position", wfmStrings.position()));
        shell.setWidth(330);
        shell.open();

        final TextBox positionName = new TextBox();
//        positionName.addStyleName(Constants.DEFAULT_WIDTH);
        TextArea2 positionDescription = new TextArea2(100);
        positionDescription.hideCharacterLimitPanel();
//        positionDescription.addStyleName(Constants.DEFAULT_WIDTH);

        HTML html = new HTML();

        //save button
        button_save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> {
            enabledButtons(false);
            if (positionName.getText() == null || "".equals(positionName.getText())) {
                positionName.setStyleName("x-form-invalid");
                enabledButtons(true);
                return;
            }
            LoadingPanel.loading(true);
            NewPosition pos = new NewPosition();
            pos.setName(positionName.getText());
            pos.setDescription(positionDescription.getText());
            provider.createPosition(pos, new PositionCallback() {

                public void onFailure() {
                    enabledButtons(true);
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.ERROR, Action.OK);
                    message.setTitle(wfmStrings.error());
                    message.setMessage(wfmStrings.errorOccurredSavingChanges());
                    LoadingPanel.loading(false);
                    shell.close();

                }

                public void onSuccess(Object result) {
                    if (result == null) {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK);
                        html.setText(wfmStrings.errorSameData());
                        html.getElement().getStyle().setMarginBottom(15, Style.Unit.PX);
                        html.getElement().setAttribute("style","position: relative;top: -18px;left: auto;");
                        html.getElement().getStyle().setColor("#D04B38");
                        enabledButtons(true);
                    } else {
                        enabledButtons(true);
                        Integer id = (Integer) result;
                        position.addItem(new SelectItem(id, positionName.getText()));
                        position.setSelected(id);
                        shell.close();
                    }
                    LoadingPanel.loading(false);
                }
            });
        });
        //cancel button
        button_cancel = new WfmButton2(wfmStrings.cancel(), event -> shell.close());

//        positionName.getElement().getStyle().setMarginBottom(1, Style.Unit.PX);

        shell.addWidget(positionName, wfmStrings.position());
        shell.add(html);
        shell.addWidget(positionDescription, wfmStrings.description());

        shell.addButton(button_cancel);
        shell.addButton(button_save);
    }

    private void enabledButtons(boolean b) {
        if (button_save != null) {
            button_save.setEnabled(b);
        }
        if (button_cancel != null) {
            button_cancel.setEnabled(b);
        }
    }

    private void init() {

        position = new PositionLookUp();
        //position.setAllowFirstItem(true);

        if (provider != null) {
            provider.employeePositions(new PositionCallback() {
                public void onFailure() {
                    position.addItem(new SelectItem(-1, wfmStrings.noPositions()));
                }

                public void onSuccess(Object result) {
                    position.setItems(null, (SelectItem[]) result);
                }
            });
        }

        AdvancedInputGroup inputGroup = null;
        if (additionalWidget != null) {
            inputGroup = new AdvancedInputGroup(position, additionalWidget);
        } else {
            inputGroup = new AdvancedInputGroup(position);
        }
        if (canAddPosition) {
            inputGroup.setAppender("ficon--plus");
            inputGroup.appenderClickHandler(this::addPositionClicked);
        }

        initWidget(inputGroup);
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
    }

    public boolean validate(final WfmForm.Field field, String errorMessage) {
        if (position.getSelectedItemID() == null) {
            field.setErrorMessage(errorMessage, "");
            position.addValueChangeHandler(event -> {
                if (position.getSelectedItemID() != null) {
                    field.setErrorMessage(null, "");
                }
            });
            return false;
        }
        return true;
    }

    public void setProvider(PositionProvider provider) {
        this.provider = provider;
        this.provider.employeePositions(new PositionCallback() {
            public void onFailure() {
                position.addItem(new SelectItem(-1, wfmStrings.noPositions()));
            }

            public void onSuccess(Object result) {
                position.setItems(null, (SelectItem[]) result);
            }
        });
    }

    public interface PositionProvider {
        void employeePositions(PositionCallback callback);

        void createPosition(NewPosition position, PositionCallback callback);
    }

    public interface PositionCallback {
        void onFailure();

        void onSuccess(Object result);
    }
}