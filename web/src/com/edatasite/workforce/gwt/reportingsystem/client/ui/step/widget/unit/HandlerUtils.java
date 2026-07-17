package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

/**
 * Created by Virus on 10/3/14.
 */
public class HandlerUtils {
    private static final String RECURRENCE_TYPE_PANEL = "recurrenceTypePanel";
    private static final String RECURRENCE_TYPE = "recurrenceType";

    public static void click(Element element, EventListener listener) {
        DOM.sinkEvents(element, Event.ONCLICK);
        DOM.setEventListener(element, listener);
    }

    public static void change(Element element, EventListener listener) {
        DOM.sinkEvents(element, Event.ONCHANGE);
        DOM.setEventListener(element, listener);
    }

    public static void monthlyRepeatsDayRadioSelect(Node element, final InputElement monthlyRepeatsDayRadio) {
        click(element.cast(), event -> monthlyRepeatsDayRadio.setChecked(true));
    }

    public static void monthlyRepeatsTheRadioSelect(Node element, final InputElement monthlyRepeatsTheRadio) {
        click(element.cast(), event -> monthlyRepeatsTheRadio.setChecked(true));
    }

    public static void occurencesClick(Element element, final InputElement afterRadio) {
        click(element, event -> afterRadio.setChecked(true));
    }

    public static void recurrenceTypaChangeHandler(final InputElement radio, final ReportingStepControlView view, final int recurringType) {
        change(radio, new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                RecurrenceJobItem job = view.getReport().getRecurrenceJobItem();
                if (job == null) {
                    job = new RecurrenceJobItem();
                    view.getReport().setRecurrenceJobItem(job);
                }
                job.setType(recurringType);

                click(0);
                click(1);
                click(2);
                click(3);
            }

            private void click(int widgetIndex) {
                Element panel = Utils.getElementsByName(RECURRENCE_TYPE_PANEL).getItem(widgetIndex).cast();
                Element target = Utils.getElementsByName(RECURRENCE_TYPE).getItem(widgetIndex).cast();
                if (radio.isChecked() && radio.equals(target)) {
                    panel.removeClassName("hide");
                } else {
                    panel.addClassName("hide");
                }
            }
        });
    }

    public static void yearlyRepeatEveryHandler(Element element, final InputElement repeatsYearlyEveryRadio) {
        click(element, event -> repeatsYearlyEveryRadio.setChecked(true));
    }

    public static void yearlyRepeatTheHandler(Element element, final InputElement repeatsYearlyTheRadio) {
        click(element, event -> repeatsYearlyTheRadio.setChecked(true));
    }

    public static void endDateAfter(Element element, final InputElement afterRadio) {
        click(element, event -> afterRadio.setChecked(true));
    }
}
