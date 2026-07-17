package com.edatasite.workforce.gwt.core.client.ui.resourceUtil;

import com.edatasite.workforce.gwt.core.client.Utils;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * User: Ilhombek
 * Date: 6/12/12
 * Time: 8:52 AM
 */
public class ResourceUtil extends PopupPanel implements Constants {

	private static final WfmStrings wfmStrings = WfmStrings.App.get();
	private ChangeDailyTimeListener changeDailyTimeListener;
	private int currentEstimatedTime;
	private Element selectedElement;
	private TextBox timeBox;

	public interface ChangeDailyTimeListener {
        void dailyTimeListener(int changedEstimatedTime);
    }

	public ResourceUtil(Element selectedElement, int currentEstimatedTime) {
		super(true, true);
		this.selectedElement = selectedElement;
		this.currentEstimatedTime = currentEstimatedTime;
		drawInitialize();
	}

	public void setChangeDailyTimeListener(ChangeDailyTimeListener changeDailyTimeListener) {
		this.changeDailyTimeListener = changeDailyTimeListener;
	}

	private boolean applyTime() {

		try {
			if (!"".equals(timeBox.getText())) {
				int lastMinutes = Utils.parseMinutes(timeBox.getText());
				if (lastMinutes < 0 || !Utils.correctFormat) {
                    Info.show(wfmStrings.timeFormats(), Info.Type.WARNING);
                    return false;
				}

				//apply daily time spent hour
				if (changeDailyTimeListener != null) {
					if (currentEstimatedTime != lastMinutes) {
						changeDailyTimeListener.dailyTimeListener(lastMinutes);
					}
				}
			}

		} catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
            Info.show(wfmStrings.timeFormats(), Info.Type.WARNING);
            return false;
		}
        return true;
	}

	@Override
	protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		if (Event.ONKEYUP == event.getTypeInt()) {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
				hide();
			}
		}
	}

	private void drawInitialize() {
		//register popup panel
		this.addCloseHandler(popupPanelCloseEvent -> {
            //register something logic
            applyTime();
        });

		//register time box
		timeBox = new TextBox();
		timeBox.setWidth("80px");
		timeBox.setText(Utils.formatMinutes(currentEstimatedTime));
		timeBox.setAlignment(ValueBoxBase.TextAlignment.CENTER);
		timeBox.addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                hide();
            }
        });
		add(timeBox);
		show();
	}

	public void show() {
		setPopupPosition(selectedElement.getAbsoluteLeft() - 10, selectedElement.getAbsoluteTop());
		timeBox.setFocus(true);
		Utils.setFocus(timeBox.getElement(), true);
		if (Utils.isIE()) {
			timeBox.getElement().focus();
		}
		timeBox.setCursorPos(timeBox.getText().length());
		super.show();
	}
}