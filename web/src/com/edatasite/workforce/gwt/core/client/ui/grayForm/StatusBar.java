package com.edatasite.workforce.gwt.core.client.ui.grayForm;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.12.2008
 * Time: 16:05:46
 * To change this template use File | Settings | File Templates.
 */
public class StatusBar extends HorizontalPanel implements Constants {

    private static StatusBundle statusBundle = (StatusBundle) GWT.create(StatusBundle.class);

    Label message;
    Image image;

    public StatusBar() {

        super();
        super.setVisible(false);
        init();
    }

    public void showStatusPanel(String message, int status) {

        super.setVisible(true);

        switch (status) {
            case INFO: {
                info(message);
                break;
            }
            case SUCCESS: {
                success(message);
                break;
            }
            case WARNING: {
                warning(message);
                break;
            }
            case ERROR: {
                error(message);
                break;
            }
            case VALIDATION: {
                validation(message);
                break;
            }
            default: {
                break;
            }
        }
    }

    public void resetStatus() {

        super.setVisible(false);
    }

    public void info(String message) {

        changeStatus("info", message, statusBundle.info());
    }

    public void success(String message) {

        changeStatus("success", message, statusBundle.success());
    }

    public void warning(String message) {

        changeStatus("warning", message, statusBundle.warning());
    }

    public void error(String message) {

        changeStatus("error", message, statusBundle.error());
    }

    public void validation(String message) {

        changeStatus("validation", message, statusBundle.validation());
    }

    private void changeStatus(String styleName, String text, ImageResource imageBundle) {
        Image img = new Image(imageBundle);
        String url = img.getUrl();
        int width = img.getWidth();
        int height = img.getHeight();
        int left = img.getOriginLeft();
        int top = img.getOriginTop();

        super.setStyleName(styleName);
        super.addStyleName("status-style");
        image.setUrlAndVisibleRect(url, left, top, width, height);

        message.setText(text);
        message.setStyleName(styleName);
    }

    private void init() {

        HorizontalPanel innerPanel = new HorizontalPanel();
        innerPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
        innerPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        innerPanel.setSpacing(8);

        image = new Image();
        message = new Label();

        innerPanel.add(image);
        innerPanel.add(message);

        super.add(innerPanel);
    }

}
