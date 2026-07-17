package com.edatasite.workforce.gwt.dashboardwidget.client.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardBirthdayItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardComboItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardNewsItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWeatherItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;
import gwt.material.design.client.ui.html.Heading;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 31.05.2018 19:25
 */
public class DashboardComboComponent extends DashboardBaseWidget {

    private Div weatherDiv;
    private Div messageDiv;
    private Div newsDiv;
    private Div birthdayDiv;

    private MaterialLink celsiusLink;
    private MaterialLink fahrenheitLink;
    private FigCaption temperatureFigCaption;
    private Element descriptionDd;

    private final String icon_01d = "mainStyles/images/weather/day_01d.svg"; //day clear sky
    private final String icon_01n = "mainStyles/images/weather/night_01n.svg"; //night clear sky
    private final String icon_02d = "mainStyles/images/weather/cloudy-day_02d.svg"; //day few clouds
    private final String icon_02n = "mainStyles/images/weather/cloudy-night_02n.svg"; //night  few clouds
    private final String icon_03d_03n_04d_04n = "mainStyles/images/weather/cloudy_03d_03n_04d_04n.svg"; //1. scattered clouds 2. broken clouds
    private final String icon_09d_09n = "mainStyles/images/weather/rainy_09d_09n.svg"; //shower rain
    private final String icon_10d_10n = "mainStyles/images/weather/rainy_10d_10n.svg"; //rain
    private final String icon_11d_11n = "mainStyles/images/weather/thunder_11d_11n.svg"; //thunderstorm
    private final String icon_13d_13n = "mainStyles/images/weather/snowy_13d_13n.svg";  //snow
    private final String icon_50d_50n = "mainStyles/images/weather/cloudy-day_50d_50n.svg"; //mist

    public DashboardComboComponent(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    protected void initInternal() {
        mainPanel.addStyleName("combo-widget");
        headerPanel.setVisible(false);

        weatherDiv = new Div("combo-widget-item combo-widget-weather");
        messageDiv = new Div("combo-widget-item combo-widget-messages");
        newsDiv = new Div("combo-widget-item combo-widget-news");
        birthdayDiv = new Div("combo-widget-item combo-widget-birthday");

        Div wrapperDiv = new Div("gwt-wrapper");
        Div contactDiv = new Div("widget-content");
        contactDiv.add(weatherDiv);
        contactDiv.add(messageDiv);
        contactDiv.add(newsDiv);
        contactDiv.add(birthdayDiv);
        wrapperDiv.add(contactDiv);

        contentPanel.add(wrapperDiv);
    }

    @Override
    protected void getData() {
        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().getComboData(new AbstractAsyncCallback<DashboardComboItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(DashboardComboItem result) {
                LoadingWidgets.get(getCode()).hide();
                clearPanel();
                drawContentPanel(result);
            }
        });
    }

    @Override
    protected void getSampleData(boolean nodata) {
        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().getWeather(true, new AbstractAsyncCallback<DashboardWeatherItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(DashboardWeatherItem weatherItem) {
                LoadingWidgets.get(getCode()).hide();
                clearPanel();

                DashboardComboItem result = new DashboardComboItem();

                if (weatherItem.getLocation() == null) {
                    weatherItem = new DashboardWeatherItem();
                    weatherItem.setLocation(getText(Utils.getUserCity()));
                    weatherItem.setDescription("Clouds");
                    weatherItem.setIcon(DashboardWeatherItem.IMG_02D);
                    weatherItem.setTemperature("95.0");
                }
                result.setWeatherItem(weatherItem);

                ArrayList<DashboardNewsItem> newsItems = new ArrayList<>();
                DashboardNewsItem newsItem = new DashboardNewsItem();
                newsItem.setSubject("Upcoming Wednesday at 3p.m, a meeting is going to be held with foreign clients.");
                newsItem.setPostedBy("James Morris");
                newsItems.add(newsItem);
                newsItem = new DashboardNewsItem();
                newsItem.setSubject("Due to sickness, Mac Clark (The Head of Accounting Department) will be non-attending for 3 days in a row.");
                newsItem.setPostedBy("James Morris");
                newsItems.add(newsItem);
                newsItem = new DashboardNewsItem();
                newsItem.setSubject("We attract investments of $5 million");
                newsItem.setPostedBy("James Morris");
                newsItems.add(newsItem);
                result.setNewsItems(newsItems);

                ArrayList<DashboardBirthdayItem> birthdayItems = new ArrayList<>();
                DashboardBirthdayItem birthdayItem = new DashboardBirthdayItem();
                birthdayItem.setFistName("James");
                birthdayItem.setLastName("Morris");
                birthdayItem.setPosition("HR manager");
                birthdayItem.setDate(new Date());
                birthdayItems.add(birthdayItem);
                birthdayItem = new DashboardBirthdayItem();
                birthdayItem.setFistName("Nelson");
                birthdayItem.setLastName("Mandela");
                birthdayItem.setPosition("Admin");
                birthdayItem.setDate(DateUtil.addDays(new Date(), 1));
                birthdayItems.add(birthdayItem);
                birthdayItem = new DashboardBirthdayItem();
                birthdayItem.setFistName("Daniel");
                birthdayItem.setLastName("Collins");
                birthdayItem.setPosition("Painter");
                birthdayItem.setDate(DateUtil.addDays(new Date(), 2));
                birthdayItems.add(birthdayItem);
                result.setBirthdayItems(birthdayItems);

                EmailAccountItem item = new EmailAccountItem();
                item.setUnreadCount(4L);
                item.setEmail("munir@kpi.com");
                result.setMessageItem(item);

                drawContentPanel(result);
            }
        });
    }

    private void drawContentPanel(DashboardComboItem data) {
        // weather
        DashboardWeatherItem weatherItem = data.getWeatherItem();
        DateTimeFormat dateFormat = DateTimeFormat.getFormat("EEEE dd MMM yyyy");
        weatherDiv.add(drawWeatherTitlePanel(dateFormat.format(new Date()) + ".", weatherItem.getLocation()));
        weatherDiv.add(drawWeatherBody(weatherItem));

        // message
        EmailAccountItem messageItem = data.getMessageItem();
        messageDiv.add(drawTitlePanel(wfmStrings.email(), messageItem.getEmail()));
        messageDiv.add(drawMessageBody(messageItem));

        // news
        List<DashboardNewsItem> newsItem = data.getNewsItems();
        newsDiv.add(drawTitlePanel(WfmStrings.App.get().news(), wfmStrings.companyNews()));
        newsDiv.add(drawNewsBody(newsItem));

        // birthday
        List<DashboardBirthdayItem> birthdayItems = data.getBirthdayItems();
        birthdayDiv.add(drawBirthdayTitlePanel(wfmStrings.birthDay(), wfmStrings.comingBirthdays()));
        birthdayDiv.add(drawBirthdayBody(birthdayItems));
    }

    private Div drawWeatherBody(DashboardWeatherItem item) {
        Div weatherBodyDiv = new Div("combo-widget__body");
        FigureWidget figure = new FigureWidget();

        Image image = new Image();
        if (item.getIcon() == null) {
            image.setUrl(icon_01d);
        }
        switch (item.getIcon()) {
            case DashboardWeatherItem.IMG_01D:
                image.setUrl(icon_01d);
                break;
            case DashboardWeatherItem.IMG_01N:
                image.setUrl(icon_01n);
                break;
            case DashboardWeatherItem.IMG_02D:
                image.setUrl(icon_02d);
                break;
            case DashboardWeatherItem.IMG_02N:
                image.setUrl(icon_02n);
                break;
            case DashboardWeatherItem.IMG_03D:
            case DashboardWeatherItem.IMG_03N:
            case DashboardWeatherItem.IMG_04D:
            case DashboardWeatherItem.IMG_04N:
                image.setUrl(icon_03d_03n_04d_04n);
                break;
            case DashboardWeatherItem.IMG_09D:
            case DashboardWeatherItem.IMG_09N:
                image.setUrl(icon_09d_09n);
                break;
            case DashboardWeatherItem.IMG_10D:
            case DashboardWeatherItem.IMG_10N:
                image.setUrl(icon_10d_10n);
                break;
            case DashboardWeatherItem.IMG_11D:
            case DashboardWeatherItem.IMG_11N:
                image.setUrl(icon_11d_11n);
                break;
            case DashboardWeatherItem.IMG_13D:
            case DashboardWeatherItem.IMG_13N:
                image.setUrl(icon_13d_13n);
                break;
            case DashboardWeatherItem.IMG_50D:
            case DashboardWeatherItem.IMG_50N:
                image.setUrl(icon_50d_50n);
                break;
            default:
                image.setUrl(icon_01d);
        }
        Element figureDl = Document.get().createElement("dl");
        figureDl.setClassName("figure__img");
        Element figureDt = Document.get().createElement("dt");
        figureDt.appendChild(image.getElement());
        figureDl.appendChild(figureDt);

        descriptionDd = Document.get().createElement("dd");
        figureDl.appendChild(descriptionDd);
        figure.getElement().appendChild(figureDl);

        temperatureFigCaption = new FigCaption();
        figure.add(temperatureFigCaption);

        if (item.isFahrenheit()) {
            fahrenheitLink.setStyleName("active");
            celsiusLink.removeStyleName("active");
        } else {
            celsiusLink.setStyleName("active");
            fahrenheitLink.removeStyleName("active");
        }
        setWeatherData(item);

        weatherBodyDiv.add(figure);
        return weatherBodyDiv;
    }

    private void setWeatherData(DashboardWeatherItem item) {
        String temperature = item.getTemperature();
        if (temperature != null) {
            float fahrenheit = Float.valueOf(item.getTemperature());
            if (item.isFahrenheit()) {
                temperature = String.valueOf(Math.round(fahrenheit));
            } else {
                temperature = String.valueOf(Math.round(Math.ceil(fahrenheit - 32) * 5 / 9));
            }
        }
        String icon = item.isFahrenheit() ? "&#176F" : "&#176C";
        temperatureFigCaption.getElement().setInnerHTML(temperature != null ? temperature + " " + icon : wfmStrings.notAvailable());
        if (item.getDescription() != null) {
            descriptionDd.setInnerText(getText(item.getDescription()));
        }
        if (item.isFahrenheit()) {
            fahrenheitLink.setStyleName("active");
            celsiusLink.removeStyleName("active");
        } else {
            celsiusLink.setStyleName("active");
            fahrenheitLink.removeStyleName("active");
        }
    }

    private Div drawMessageBody(EmailAccountItem messageItem) {
        Div messageBodyDiv = new Div("combo-widget__body");
        FigureWidget figure = new FigureWidget();
        Div imgDiv = new Div("figure__img");
        MaterialImage image = new MaterialImage();
        if (messageItem.getUnreadCount() != null && messageItem.getUnreadCount() > 0) {
            image.setUrl("mainStyles/images/combo-widget-message.png");
        } else {
            image.setUrl("mainStyles/images/combo-widget-message-non.png");
        }
        imgDiv.add(image);
        figure.add(imgDiv);

        FigCaption figCaption = new FigCaption();
        Element textDl = Document.get().createElement("dl");
        Element textDt = Document.get().createElement("dt");
        textDt.setInnerText(String.valueOf(messageItem.getUnreadCount() != null ? messageItem.getUnreadCount() : 0));
        textDl.appendChild(textDt);
        Element textDd = Document.get().createElement("dd");
        textDd.setInnerText(accountingStrings.unreadNewMail());
        textDl.appendChild(textDd);
        figCaption.getElement().appendChild(textDl);
        figure.add(figCaption);
        messageBodyDiv.add(figure);
        return messageBodyDiv;
    }

    private Div drawNewsBody(List<DashboardNewsItem> newsItems) {
        Div bodyDiv = new Div("combo-widget__body");
        Div listDiv = new Div("widget-list");
        for (DashboardNewsItem item : newsItems) {
            Element newsRowDiv = Document.get().createElement("dl");
            newsRowDiv.setClassName("widget-row");

            Element dt = Document.get().createElement("dt");
            dt.setInnerText(item.getSubject() != null ? item.getSubject() : "N/A");

            Element dd = Document.get().createElement("dd");
            Element span = Document.get().createElement("span");
            span.setInnerHTML(item.getPostedBy() != null ? item.getPostedBy() : "N/A");
            dd.appendChild(span);
            MaterialLink link = new MaterialLink(wfmStrings.readMore() + "...");
            DOM.sinkEvents(link.getElement().cast(), Event.ONCLICK);
            DOM.setEventListener(link.getElement().cast(), event -> {
                String url = GWT.getHostPageBaseURL() + "Hrms.html#news|summary/" + item.getObjectId();
                Window.open(url, "_self", null);
            });
            dd.appendChild(link.getElement());

            newsRowDiv.appendChild(dt);
            newsRowDiv.appendChild(dd);
            listDiv.getElement().appendChild(newsRowDiv);
        }
        bodyDiv.add(listDiv);
        return bodyDiv;
    }

    private Div drawBirthdayBody(List<DashboardBirthdayItem> birthdayItems) {
        Div bodyDiv = new Div("combo-widget__body");
        Div listDiv = new Div("widget-list");

        List<DashboardBirthdayItem> listItem = new ArrayList<>();
        List<Integer> listDays = new ArrayList<>();

        for (DashboardBirthdayItem item : birthdayItems) {
            listDays.add(getLeftDayToBirthDay(item));
            listDays.sort((o1, o2) -> {
                if (o1 > o2) return 1;
                else if (o1 < o2) return -1;
                else return 0;
            });
        }
        for (Integer day : listDays) {
            for (DashboardBirthdayItem item : birthdayItems) {
                if (day == getLeftDayToBirthDay(item) && !listItem.contains(item)) {
                    listItem.add(item);
                }
            }
        }
        for (DashboardBirthdayItem item : listItem) {
            Div rowDiv = new Div("widget-row");

            Div imgDiv = new Div("widget-row__img");
            FigureWidget figure = new FigureWidget();
            figure.addStyleName("img-group img-group--circle");
            Div imgGrp = new Div("img-group__img");
            if (item.getImageUrl() != null) {
                MaterialImage img = new MaterialImage(item.getImageUrl());
                imgGrp.add(img);
                figure.add(imgGrp);
            } else {
                FigCaption figCaption = new FigCaption();
                figCaption.setText(getImageFormat(item.getFistName(), item.getLastName()));
                figure.add(figCaption);
            }
            imgDiv.add(figure);

            Div textDiv = new Div("widget-row__text");
            Element textdl = Document.get().createElement("dl");
            Element textdt = Document.get().createElement("dt");
            if (Utils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEE_LIST,
                    PermissionConstants.SHOW_PROJECT_EMPLOYEE_LIST)
                    || item.isCurrentUser()) {
                AnchorElement employeeLink = Document.get().createAnchorElement();
                employeeLink.setInnerHTML("<h3>" + item.getFistName() + " " + item.getLastName() + "</h3>");
                employeeLink.setHref("Hrms.html#employeeProfile%7CemployeeProfileView/" + item.getObjectId());
                textdt.appendChild(employeeLink);
                textdt.addClassName("widget__title");
            } else {
                textdt.setInnerHTML(item.getFistName() + " " + item.getLastName());
            }
            textdl.appendChild(textdt);
            Element textdd = Document.get().createElement("dd");
            textdd.setInnerText(getText(item.getPosition()));
            textdl.appendChild(textdd);
            textDiv.getElement().appendChild(textdl);

            Div endDiv = new Div("widget-row__end");
            Element enddl = Document.get().createElement("dl");
            Element enddt = Document.get().createElement("dt");
            enddt.setInnerText(DateUtils.dayMonthFormat(item.getDate()));
            enddl.appendChild(enddt);
            Element enddd = Document.get().createElement("dd");
            String day = "";
            int a = getLeftDayToBirthDay(item);

            if (a == 1) {
                day = wfmStrings.tomorrow();
            } else if (a > 1) {
                day = a + " " + wfmStrings.daysLeft();
            } else {
                day = wfmStrings.today();
            }
            enddd.setInnerText(day);
            enddl.appendChild(enddd);
            endDiv.getElement().appendChild(enddl);

            rowDiv.add(imgDiv);
            rowDiv.add(textDiv);
            rowDiv.add(endDiv);
            listDiv.add(rowDiv);
        }

        bodyDiv.add(listDiv);
        return bodyDiv;
    }

    private int getLeftDayToBirthDay(DashboardBirthdayItem item) {
        Date birthdayDate = item.getDate();
        int a = 0;

        if (birthdayDate.getMonth() < new Date().getMonth()) {
            birthdayDate.setYear(new Date().getYear() + 1);
        } else if (birthdayDate.getMonth() == new Date().getMonth() && birthdayDate.getDate() < new Date().getDate()) {
            birthdayDate.setYear(new Date().getYear() + 1);
        } else {
            birthdayDate.setYear(new Date().getYear());
        }

        a = DateUtil.differenceInDays(birthdayDate, new Date());
        return a;
    }


    private Div drawTitlePanel(String firstTitle, String secondTitle) {
        Div titleDiv = new Div("combo-widget__title");
        Heading firstH = new Heading(HeadingSize.H3);
        firstH.getElement().setInnerText(getText(firstTitle));
        titleDiv.add(firstH);

        Heading secondH = new Heading(HeadingSize.H4);
        secondH.getElement().setInnerText(getText(secondTitle));
        titleDiv.add(secondH);
        return titleDiv;
    }

    private Div drawWeatherTitlePanel(String firstTitle, String secondTitle) {
        Div titleDiv = new Div("combo-widget__title");
        Div div = new Div();
        Heading firstH = new Heading(HeadingSize.H3);
        firstH.getElement().setInnerText(getText(firstTitle));
        div.add(firstH);

        Heading secondH = new Heading(HeadingSize.H4);
        secondH.getElement().setInnerText(getText(secondTitle));
        div.add(secondH);
        titleDiv.add(div);

        Element degreesSpan = Document.get().createElement("span");
        celsiusLink = new MaterialLink();
        celsiusLink.getElement().setInnerHTML("&#176C");
        DOM.sinkEvents(celsiusLink.getElement().cast(), Event.ONCLICK);
        DOM.setEventListener(celsiusLink.getElement().cast(), event -> {
            if (celsiusLink.getStyleName() != null && !celsiusLink.getStyleName().contains("active")) {
                saveWeatherSettings(false);
            }
        });
        fahrenheitLink = new MaterialLink();
        fahrenheitLink.getElement().setInnerHTML("&#176F");
        DOM.sinkEvents(fahrenheitLink.getElement().cast(), Event.ONCLICK);
        DOM.setEventListener(fahrenheitLink.getElement().cast(), event -> {
            if (fahrenheitLink.getStyleName() != null && !fahrenheitLink.getStyleName().contains("active")) {
                saveWeatherSettings(true);
            }
        });
        degreesSpan.appendChild(celsiusLink.getElement());
        degreesSpan.appendChild(fahrenheitLink.getElement());
        degreesSpan.setClassName("weather-degrees");
        titleDiv.getElement().appendChild(degreesSpan);

        return titleDiv;
    }

    private Div drawBirthdayTitlePanel(String firstTitle, String secondTitle) {
        Div titleDiv = new Div("combo-widget__title");

        FigureWidget figure = new FigureWidget();
        Div imgDiv = new Div("figure__img");
        Image image = new Image("mainStyles/images/combo-widget-birthday-title.png");
        imgDiv.add(image);
        figure.add(imgDiv);

        FigCaption figCaption = new FigCaption();
        Element textH3 = Document.get().createElement("h3");
        textH3.setInnerText(getText(firstTitle));
        figCaption.getElement().appendChild(textH3);
        Element textH4 = Document.get().createElement("h4");
        textH4.setInnerText(getText(secondTitle));
        figCaption.getElement().appendChild(textH4);
        figure.add(figCaption);
        titleDiv.add(figure);

        return titleDiv;
    }

    private String getImageFormat(String first, String second) {
        String result = "";
        if (first != null && !first.isEmpty()) {
            result = result.concat(String.valueOf(first.charAt(0)));
        }
        if (second != null && !second.isEmpty()) {
            result = result.concat(String.valueOf(second.charAt(0)));
        }
        return result.toUpperCase();
    }

    private String getText(String text) {
        if (text != null && !text.isEmpty()) {
            return text;
        } else {
            return wfmStrings.notAvailable();
        }
    }

    private void saveWeatherSettings(boolean isFahrenheit) {
        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().saveWeatherSettings(isFahrenheit, new AbstractAsyncCallback<DashboardWeatherItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(DashboardWeatherItem result) {
                LoadingWidgets.get(getCode()).hide();
                setWeatherData(result);
            }
        });
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.COMBO;
    }

    @Override
    protected String getEmptyText() {
        return null;
    }

    @Override
    protected void clearPanel() {
        weatherDiv.clear();
        messageDiv.clear();
        newsDiv.clear();
        birthdayDiv.clear();
    }
}
