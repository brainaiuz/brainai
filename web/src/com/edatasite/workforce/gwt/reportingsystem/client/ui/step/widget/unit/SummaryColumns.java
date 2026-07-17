package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.ComplexPanel;

/**
 * Created by Virus on 8/28/14.
 */
public class SummaryColumns extends ComplexPanel {
    //	private final TableSectionElement tbody;
    private KpiCheckBox cCountCheck;
    private KpiCheckBox cSumCheck;
    private KpiCheckBox cAvgCheck;
    private KpiCheckBox cLargCheck;
    private KpiCheckBox cSmallCheck;
    private ColumnRpc columnRpc;
    private EventListener listener;

    public SummaryColumns(TableSectionElement tbody, ColumnRpc rpc) {
        setElement(tbody);
//		this.tbody = tbody;
        tbody.appendChild(addRow(rpc));
    }

    public void check(boolean b) {
        setChecked(cSumCheck, b);
        setChecked(cCountCheck, b);
        setChecked(cAvgCheck, b);
        setChecked(cLargCheck, b);
        setChecked(cSmallCheck, b);
    }

    private Element element = DOM.createTR();

    public Element addRow(ColumnRpc rpc) {
        this.columnRpc = rpc;

        Element columnName = DOM.createTD();
        columnName.setInnerText(rpc.getTitle());
        element.appendChild(columnName);

        Element cSum = DOM.createTD();
        cSumCheck = new KpiCheckBox();
        cSum.appendChild(cSumCheck.getElement());
        element.appendChild(cSum);

        Element cCount = DOM.createTD();
        cCountCheck = new KpiCheckBox();
        cCount.appendChild(cCountCheck.getElement());
        element.appendChild(cCount);

        Element cAvg = DOM.createTD();
        cAvgCheck = new KpiCheckBox();
        cAvg.appendChild(cAvgCheck.getElement());
        element.appendChild(cAvg);

        Element cLarg = DOM.createTD();
        cLargCheck = new KpiCheckBox();
        cLarg.appendChild(cLargCheck.getElement());
        element.appendChild(cLarg);

        Element cSmall = DOM.createTD();
        cSmallCheck = new KpiCheckBox();
        cSmall.appendChild(cSmallCheck.getElement());
        element.appendChild(cSmall);

        setChecked(cSumCheck, rpc.isSum());
        setChecked(cCountCheck, rpc.isCount());
        setChecked(cAvgCheck, rpc.isAvg());
        setChecked(cLargCheck, rpc.isLargest());
        setChecked(cSmallCheck, rpc.isSmallest());

        if ("number".equals(rpc.getType()) && "percent".equals(rpc.getColumnFormat())) {
            setEnable(cSumCheck, false);
//            setEnable(cCountCheck, false);
        } else {
            if ("string".equals(rpc.getType()) || "date".equals(rpc.getType())) {
                setEnable(cSumCheck, false);
                setEnable(cAvgCheck, false);
                setEnable(cLargCheck, false);
                setEnable(cSmallCheck, false);
            }
        }

        return element;
    }

    private void setEventListener(final KpiCheckBox element) {
        DOM.sinkEvents(element.getElement(), Event.ONCHANGE);
        DOM.setEventListener(element.getElement(), event -> {
            element.setValue(!element.getValue());
            setChecked(element, element.getValue());
        });
    }

    public void setHandler(EventListener listener) {
        this.listener = listener;
        setEventListener(cCountCheck);
        setEventListener(cSumCheck);
        setEventListener(cAvgCheck);
        setEventListener(cLargCheck);
        setEventListener(cSmallCheck);
    }

    private void setChecked(KpiCheckBox element, boolean check) {
        if (element.isEnabled()) {
            element.setValue(check);
            if (cCountCheck.equals(element)) {
                columnRpc.setCount(check);
            } else if (cSumCheck.equals(element)) {
                columnRpc.setSum(check);
            } else if (cAvgCheck.equals(element)) {
                columnRpc.setAvg(check);
            } else if (cLargCheck.equals(element)) {
                columnRpc.setLargest(check);
            } else if (cSmallCheck.equals(element)) {
                columnRpc.setSmallest(check);
            }
        }
        if (listener != null) {
            listener.onBrowserEvent(Event.getCurrentEvent());
        }
    }

    private void setEnable(KpiCheckBox element, boolean value) {
        element.setEnabled(value);
    }

    public ColumnRpc getColumnRpc() {
        if (columnRpc.isSum() || columnRpc.isAvg() || columnRpc.isCount() || columnRpc.isLargest() || columnRpc.isSmallest()) {
            return columnRpc;
        }
        return null;
    }
}
