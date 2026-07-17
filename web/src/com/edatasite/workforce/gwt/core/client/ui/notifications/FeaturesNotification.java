package com.edatasite.workforce.gwt.core.client.ui.notifications;

import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 12.03.12
 * Time: 17:55
 * To change this template use File | Settings | File Templates.
 */
public class FeaturesNotification extends InfoOld {

	public interface Bundle extends ClientBundle {
		@CssResource.NotStrict
		@Source("com/edatasite/workforce/gwt/core/client/ui/notifications/notification.css")
		CssResource infoCss();
	}

	private final Bundle css = GWT.create(Bundle.class);

	public FeaturesNotification() {
	}

	public void show(final String message_code, final String message, final Widget widget) {
		ReportService.App.get().isFeatureShown(message_code, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable throwable) {
			}

			@Override
			public void onSuccess(Boolean aBoolean) {
				if (!aBoolean) {
					css.infoCss().ensureInjected();
					final Integer initialZIndex = Integer.valueOf(!"".equals(widget.getElement().getStyle().getZIndex()) ? widget.getElement().getStyle().getZIndex() : "0");
					final String initialPosition = widget.getElement().getStyle().getPosition();
					getElement().getStyle().setWidth(250, Style.Unit.PX);
					widget.getElement().getStyle().setZIndex(100);
					widget.getElement().getStyle().setPosition(Style.Position.RELATIVE);
					setMessage(message);
					sinkEvents(Event.ONCLICK);
					setWidget(new HTMLPanel("<p><div class=\"dark-mask\"></div></p><p style=\"width: 226px; font-size: 13px;\">" + message + "</p>"));
					setType(Type.FEATURES);
					addCloseHandler(popupPanelCloseEvent -> {
                        if (initialZIndex != null) {
                            widget.getElement().getStyle().setZIndex(initialZIndex);
                        }
                        if (!"".equals(initialPosition) && initialPosition != null) {
                            widget.getElement().getStyle().setPosition(Style.Position.valueOf(initialPosition));
                        }
                    });
					showRelativeTo(widget);
				}
			}
		});
	}

	public void show(final String message_code, final String message, final UIObject object) {
		css.infoCss().ensureInjected();
		ReportService.App.get().isFeatureShown(message_code, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable throwable) {
			}

			@Override
			public void onSuccess(Boolean aBoolean) {
				if (!aBoolean) {
					final Integer initialZIndex = Integer.valueOf(!"".equals(object.getElement().getStyle().getZIndex()) ? object.getElement().getStyle().getZIndex() : "0");
					final String initialPosition = object.getElement().getStyle().getPosition();
					getElement().getStyle().setWidth(250, Style.Unit.PX);
					object.getElement().getStyle().setZIndex(100);
					object.getElement().getStyle().setPosition(Style.Position.RELATIVE);
					setMessage(message);
					sinkEvents(Event.ONCLICK);
					setWidget(new HTMLPanel("<p><div class=\"dark-mask\"></div></p><p style=\"width: 226px; font-size: 13px;\">" + message + "</p>"));
					setType(Type.FEATURES);
					addCloseHandler(popupPanelCloseEvent -> {
                        if (initialZIndex != null) {
                            object.getElement().getStyle().setZIndex(initialZIndex);
                        }
                        if (!"".equals(initialPosition) && initialPosition != null) {
                            object.getElement().getStyle().setPosition(Style.Position.valueOf(initialPosition));
                        }
                    });
					showRelativeTo(object);
				}
			}
		});
	}

	public void show(final String message_code, final String message, boolean tr) {
		css.infoCss().ensureInjected();
		ReportService.App.get().isFeatureShown(message_code, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable throwable) {
			}

			@Override
			public void onSuccess(Boolean aBoolean) {
				if (!aBoolean) {
					getElement().getStyle().setWidth(250, Style.Unit.PX);
					setMessage(message);
					sinkEvents(Event.ONCLICK);
					setWidget(new HTMLPanel("<p><div class=\"dark-mask\"></div></p><p style=\"width: 226px; font-size: 13px;\">" + message + "</p>"));
					setType(Type.FEATURES);
					setPositionAndShow(Position.CENTERED);
				}
			}
		});
	}

	public static void showModal(final String message_code, Widget modal) {
		ReportService.App.get().isFeatureShown(message_code, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable throwable) {
			}

			@Override
			public void onSuccess(Boolean aBoolean) {
				if (!aBoolean) {
					if (modal instanceof KpiModal) {
						((KpiModal) modal).open();
					}
				}
			}
		});
	}
}
