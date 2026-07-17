package com.edatasite.workforce.gwt.core.client.ui.landing;

import com.edatasite.workforce.gwt.core.client.Utils;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DDElement;
import com.google.gwt.dom.client.DListElement;
import com.google.gwt.dom.client.DTElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/13/12
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class WestPanelHelp2 extends Composite {


	private static final WfmStrings wfmStrings = WfmStrings.App.get();
	private List<Element> elementList;
	private String headerHelp;
	private TextBox searchBox;
	private ActionButton searchBtn;
	private String searchMORE_URL;

	@UiField
	HTMLPanel content;
	@UiField
	SpanElement headerMessage;
	@UiField
	DListElement dListItem;
	@UiField
	HTMLPanel searchPanel;
	@UiField
	Anchor moreHelp;


	interface WestPanelHelp2UiBinder extends UiBinder<HTMLPanel, WestPanelHelp2> {
	}

	public WestPanelHelp2() {
		this(null);
	}

	public WestPanelHelp2(String header) {
		this.headerHelp = header;
		WestPanelHelp2UiBinder ourUiBinder = GWT.create(WestPanelHelp2UiBinder.class);
		initWidget(ourUiBinder.createAndBindUi(this));
		drawInitialize();
	}

    public void addHelpItem(String titleMessage) {
        addHelpItem(titleMessage, null, null);
    }

	public void addHelpItem(String titleMessage, String innerMessage) {
		addHelpItem(titleMessage, innerMessage, null);
	}

	public void addHelpItem(String titleMessage, String innerMessage, String innerMoreURL) {
		//register dt element
		DTElement titleElement = new DTElement();
		titleElement.setInnerHTML(titleMessage);
		//insert dt element in dl element
		insertDOMElementDList(titleElement.getElement());
		elementList.add(titleElement.getElement());

		//register dd element
		DDElement innerElement = new DDElement();
		innerElement.setInnerHTML(getInnerMessage(innerMessage, innerMoreURL));
		//insert dd element in dl element
		insertDOMElementDList(innerElement.getElement());
		elementList.add(innerElement.getElement());

		//register element listener
		addListener(titleElement.getElement(), innerElement.getElement());
	}

	public void addMoreHelpLink(final String generalMoreHelpURL) {
		moreHelp.setHTML(wfmStrings.moreHelp());
		moreHelp.addClickHandler(event -> Window.open(generalMoreHelpURL, "_blank", Constants.commonParamForUrl));
	}

	public void addSearchBox() {
		searchBox = new TextBox();
		searchBox.setSize("100px", "20px");
		searchBox.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
		searchBtn = new ActionButton("&nbsp;", "btnSearch");
		addListenerSearch();
//		searchPanel.addStyleName("searchForm");
		searchPanel.add(searchBox);
		searchPanel.add(searchBtn);
	}

	private void insertDOMElement(Element parent, Element child) {
		DOM.insertChild(parent, child, elementList.size());
	}

	private void insertDOMElementDList(Element child) {
		insertDOMElement(dListItem.cast(), child);
	}

	private void addListener(final Element linkageElement, final Element innerElement) {
		DOM.sinkEvents(linkageElement, Event.ONCLICK | Event.ONBLUR);
		DOM.setEventListener(linkageElement, event -> {
            switch (DOM.eventGetType(event)) {
                case Event.ONCLICK: {
                    //register event listener
                    for (Element actElem : elementList) {
                        if (!actElem.equals(linkageElement) && actElem.getClassName() != null && actElem.getClassName().contains("expandElement")) {
                            actElem.removeClassName("expandElement");
                        }
                        if (!actElem.equals(innerElement) && actElem.getClassName() != null && actElem.getClassName().contains("activeElement")) {
                            actElem.removeClassName("activeElement");
                        }
                    }
                    if (linkageElement.getClassName() != null && linkageElement.getClassName().contains("expandElement")) {
                        linkageElement.removeClassName("expandElement");
                    } else {
                        linkageElement.addClassName("expandElement");
                    }
                    if (innerElement.getClassName() != null && innerElement.getClassName().contains("activeElement")) {
                        innerElement.removeClassName("activeElement");
                    } else {
                        innerElement.addClassName("activeElement");
                    }
                    break;
                }
            }
        });
	}

	private void addListenerSearch() {
		searchBox.setText(wfmStrings.search());
		searchBox.getElement().getStyle().setColor("darkgray");
		searchBox.addBlurHandler(event -> {
            if (searchBox.getText() == null || "".equals(searchBox.getText())) {
                searchBox.getElement().getStyle().setColor("darkgray");
                searchBox.setText(wfmStrings.search());
            }
        });
		searchBox.addFocusHandler(event -> {
            if (searchBox.getText().equals(wfmStrings.search())) {
                searchBox.getElement().getStyle().setColor("black");
                searchBox.setText("");
            }
        });
		searchBox.addValueChangeHandler(stringValueChangeEvent -> searchMORE_URL = "http://www." + Utils.getHelpHost() + "/search/node/" + searchBox.getText() + "");
		searchBox.addKeyPressHandler(event -> {
            if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                if (searchMORE_URL != null && searchBox.getText() != null &&
                        !"".equals(searchBox.getText()) && !searchBox.getText().equals(wfmStrings.search())) {
                    Window.open(searchMORE_URL, "_blank", Constants.commonParamForUrl);
                }
            }
        });
		searchBtn.addClickHandler(event -> {
            if (searchMORE_URL != null && searchBox.getText() != null &&
                    !"".equals(searchBox.getText()) && !searchBox.getText().equals(wfmStrings.search())) {
                Window.open(searchMORE_URL, "_blank", Constants.commonParamForUrl);
            }
        });
	}

	private void drawInitialize() {
		content.addStyleName("GTLP2WPH"); //G wesT paneL helP 2 West Panel Help
		elementList = new ArrayList<>();
		if (headerHelp != null) {
			headerMessage.setInnerHTML(headerHelp);
		}
	}

	private String getInnerMessage(String innerMessage, String innerMoreURL) {
		if (innerMoreURL != null && !"".equals(innerMoreURL)) {
			String moreS = "<a style=\"text-decoration:none;\" href=\"" + innerMoreURL +
					"\" alt=\"\" target=\"_blank\">" + " " + wfmStrings.actions().toLowerCase() + "..." + "</a>";
			innerMessage += moreS;
		}
		return innerMessage;
	}
}