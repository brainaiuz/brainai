package com.edatasite.workforce.gwt.assessment.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 27.11.2008
 * Time: 18:49:46
 * To change this template use File | Settings | File Templates.
 */

/**
 * Class uses for creating simple diagramm picker
 * Dynamically highlites on mouseover
 */

public class SkillGradePicker extends Widget {

    private final String fontStyle = "skill-font";

    private int maxRate;// = 7;
    private Element tbody;
    private int selectedRate;// = 0;
    private Element highlitedTD;
    private String highlitedColor;
    boolean isStatic;
    private String averageRate;

    /**
     * Default constructor
     */

    public SkillGradePicker() {
        this(0, null, false, null, 7);
    }

    /**
     * @param grade predefined rate
     */
    public SkillGradePicker(int grade) {
        this(grade, null, false, null, 7);
    }


    /**
     * @param grade    predefined grade
     * @param isStatic wheter is static that will show only grade or pickable that allows to pick grade
     */


    public SkillGradePicker(int grade, boolean isStatic) {
        this(grade, null, isStatic, null, 7);
    }

    /**
     * @param grade       predefined grade
     * @param averageRate calcuelated Average Rate
     */
    public SkillGradePicker(int grade, String averageRate) {
        this(grade, averageRate, false);
    }

    /**
     * @param grade       predefined grade
     * @param averageRate calculated Average Rate
     * @param isStatic    wheter is static that will show only grade or pickable that allows to pick grade
     */
    public SkillGradePicker(int grade, String averageRate, boolean isStatic) {
        this(grade, averageRate, isStatic, null, 7);
    }

    /**
     * @param grade     predefined grade
     * @param isStatic  isStatic wheter is static that will show only grade or pickable that allows to pick grade
     * @param fireEvent implemented PickerEvents event
     */
    public SkillGradePicker(int grade, String averageRate, boolean isStatic, PickerEvents fireEvent, int maxRate) {
        setFireEvent(fireEvent);
        this.isStatic = isStatic;
        this.selectedRate = grade;
        this.averageRate = averageRate;
        this.maxRate = maxRate;
        setElement(DOM.createTable());
        tbody = DOM.createTBody();
        DOM.appendChild(getElement(), tbody);
        init();
    }

//    public boolean isStatic() {
//        return isStatic;
//    }
//
//    public void setStatic(boolean aStatic) {
//        isStatic = aStatic;
//    }

    public void init() {
        int cellWidth = 30;
        int start = 224;
        int step = 150 / (maxRate + 1);
        String color = "";
        Element tr = DOM.createTR();
        DOM.setElementAttribute(tr, "align", "center");
//        MyDOM.addStyleName(tr, fontStyle);
        tr.addClassName(fontStyle);
        Element td;
        for (int i = 0; i <= maxRate; i++) {
            start -= step;
            color = start + "," + start + "," + start;
            if (i == selectedRate) {
                td = getSelectedCell(i, averageRate, cellWidth, color);
            } else {
                td = getCell(i, cellWidth, color);
            }
            if (!isStatic) {
                addListener(td, i, color);
            }
            DOM.appendChild(tr, td);

        }
        Element table = DOM.createTable();
//        MyDOM.addStyleName(table, fontStyle);
        table.addClassName(fontStyle);
        DOM.setElementAttribute(table, "width", "240px");
        DOM.setElementAttribute(table, "height", "21px");
        DOM.setElementAttribute(table, "cellSpacing", "0");
        DOM.setElementAttribute(table, "border-collapse", "collapse");
        DOM.setElementAttribute(table, "cellPadding", "0");
        DOM.setElementAttribute(table, "align", "center");
        Element body = DOM.createTBody();
        DOM.appendChild(table, body);
        DOM.appendChild(body, tr);

        Element tdl = DOM.createTD();
        //class = rate  defined in Assessment.css file
        DOM.setElementAttribute(tdl, "class", "rate");
        DOM.setStyleAttribute(tdl, "border", "1px solid");
        DOM.setElementAttribute(tdl, "height", "25");
        DOM.setElementAttribute(tdl, "width", "244");
        DOM.appendChild(tdl, table);
        Element trl = DOM.createTR();
        DOM.appendChild(trl, tdl);
        DOM.appendChild(tbody, trl);

    }
    //Interface for external uses

    public interface PickerEvents {
        void clicked(int rate);

        void mouseHovered(int rate);

        void mouseOut(int rate);
    }

    public PickerEvents getFireEvent() {
        return fireEvent;
    }

    public void setFireEvent(PickerEvents fireEvent) {
        this.fireEvent = fireEvent;
    }

    private PickerEvents fireEvent;

    /**
     * Registers listener for this cell
     *
     * @param td    table cell
     * @param grade grade for this cell
     * @param color defined color
     */
    private void addListener(final Element td, final int grade, final String color) {
        DOM.sinkEvents(td, Event.MOUSEEVENTS | Event.ONCLICK | Event.ONFOCUS);
        DOM.setEventListener(td, event -> {
            switch (DOM.eventGetType(event)) {
                case Event.ONCLICK: {
                    if (grade != selectedRate) {
                        if (highlitedTD != null) {
                            //Setting previous cell to normal form
                            DOM.setStyleAttribute(highlitedTD, "background", "rgb(" + highlitedColor + ")");
                            DOM.setInnerText(highlitedTD, getNotSelectedGradeString(selectedRate));
                        }
                        selectedRate = grade;
                        highlitedTD = td;
                        highlitedColor = color;
                        // Firing event for outside (for external class to know whether rate was selected)
                        if (fireEvent != null) {
                            fireEvent.clicked(grade);
                        }
                    }
                    break;
                }
                case Event.ONFOCUS:
                case Event.ONMOUSEDOWN:
                case Event.ONMOUSEUP:
                case Event.ONMOUSEMOVE:
                case Event.ONMOUSEOVER: {
                    if (grade != selectedRate) {
//                              DOM.setStyleAttribute(td,"background",getColorByGrade(grade));
//                              DOM.setStyleAttribute(td,"color","#FFFFFF");
                        DOM.setStyleAttribute(td, "cursor", "pointer");
//                              DOM.setInnerText(td,getGradeString(grade));
                        DOM.setStyleAttribute(td, "backgroundImage", "url(\"/hrms/rates/rate-" + grade + ".png\")");
                        // Firing event for outside (for external class to know whether rate was selected)
                        if (fireEvent != null) {
                            fireEvent.mouseHovered(grade);
                        }
                    }
                    break;
                }

                case Event.ONMOUSEOUT: {
                    if (grade != selectedRate) {
                        DOM.setStyleAttribute(td, "background", "rgb(" + color + ")");
                        DOM.setInnerText(td, getNotSelectedGradeString(grade));
                        // Firing event for outside (for external class to know whether rate was selected)
                        if (fireEvent != null) {
                            fireEvent.mouseOut(grade);
                        }
                    }
                    break;
                }

            }
        });
    }

    /**
     * Creates table cell
     *
     * @param grade     grade which should be highlited
     * @param cellWidth with for cell
     * @param color     background color for this cell
     * @return returns table cell (TD)
     */
    public Element getCell(int grade, int cellWidth, String color) {
        Element td = DOM.createTD();
//        MyDOM.addStyleName(td, fontStyle);
        td.addClassName(fontStyle);
        DOM.setStyleAttribute(td, "width", cellWidth + "px");
        DOM.setStyleAttribute(td, "color", "#FFFFFF");
        DOM.setStyleAttribute(td, "background", "rgb(" + color + ")");
        DOM.setInnerText(td, getNotSelectedGradeString(grade));
//        sinkEvents(td,grade,color);
//        addListener(td,grade,color);
        return td;

    }

    /**
     * Creates table cell that already selected
     *
     * @param grade       grade which should be highlited
     * @param averageRate calculated average rate (float number converted to string)
     * @param cellWidth   with for cell
     * @param color       background color for this cell
     * @return returns table cell (TD)
     */
    public Element getSelectedCell(int grade, String averageRate, int cellWidth, String color) {
        Element td = DOM.createTD();

//        MyDOM.addStyleName(td, fontStyle);
        td.addClassName(fontStyle);
        DOM.setStyleAttribute(td, "width", cellWidth + "px");
        DOM.setStyleAttribute(td, "color", "#FFFFFF");
        DOM.setStyleAttribute(td, "background", getColorByGrade(grade));
        if (averageRate != null && !averageRate.equals("") && grade != 0) {
            DOM.setInnerText(td, averageRate);
        } else {
            DOM.setInnerText(td, getGradeString(grade));
        }
        highlitedTD = td;
        highlitedColor = color;
//        addListener(td,grade,color);
//        sinkEvents(td,grade,color);
        return td;
    }

    public String getGradeString(int grade) {
        if (grade == 0) {
            return "N/A";
        } else {
            return grade + "";
        }
    }

    public String getNotSelectedGradeString(int grade) {
        if (grade == 0) {
            return "N/A";
        } else {
            return "";
        }
    }

    public String getColorByGrade(int grade) {
        return AssessmentHelper.getColorByRate(grade);
//        String color="#FF0000";
//        switch (grade){
//            case 0:
//                color = "#0A7FFF";
//                break;
//            case 1:
//                color="#750000";
//                break;
//            case 2:
//                color="#BD0000";
//                break;
//            case 3:
//                color="#CC6E00";
//                break;
//            case 4:
//                color="#D79D00";
//                break;
//            case 5:
//                color="#D1CA00";
//                break;
//            case 6:
//                color="#859D00";
//                break;
//            case 7:
//                color="#3E8C0F";
//                break;
//        }
//        return color;
    }
}
