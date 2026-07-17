package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.TableRpc;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;

/**
 * Created by Virus on 8/27/14.
 */
public class SelectColumnsThead extends Composite {
	private static final WfmStrings wfmStrings = WfmStrings.App.get();

	public SelectColumnsThead() {
		initWidget(tr);
	}

	HtmlRow tr = new HtmlRow();
	HtmlColumn td = new HtmlColumn();
	HtmlColumn td1 = new HtmlColumn();
	HtmlColumn td2 = new HtmlColumn();
	HtmlSpan tabLeName = new HtmlSpan();
	HtmlDiv div1 = new HtmlDiv();
	HtmlDiv div2 = new HtmlDiv();
	HtmlLabel label1 = new HtmlLabel();
	HtmlLabel label2 = new HtmlLabel();
	private HtmlCheckBox selectAll = new HtmlCheckBox();
	private HtmlStrong selectAllText = new HtmlStrong();
	private HtmlCheckBox deselectAll = new HtmlCheckBox();
	private HtmlStrong deselectAllText = new HtmlStrong();

	public SelectColumnsThead(TableRpc rpc) {
		this();
		tr.add(td);
		td.add(tabLeName);
		tr.add(td1);
		td1.add(div1);
		div1.add(label1);
		label1.add(selectAll);
		label1.add(selectAllText);
		tr.add(td2);
		td2.add(div2);
		div2.add(label2);
		label2.add(deselectAll);
		label2.add(deselectAllText);

		tabLeName.setText(rpc.getTableName());
		selectAllText.setText(wfmStrings.selectAll());
		deselectAllText.setText(wfmStrings.deselectAll());

		tr.setStyleName("tbody_head");
		tabLeName.setStyleName("tbody_head_title");
		div1.setStyleName("checkbox");
		div2.setStyleName("checkbox");
	}

	public void addSelectAllClickHandler(final EventListener eventListener) {
		selectAll.setEventListener(new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				eventListener.onBrowserEvent(event);
				checked(true);
			}
		});
	}

	public void addDeSelectAllClickHandler(final EventListener eventListener) {
		deselectAll.setEventListener(new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				eventListener.onBrowserEvent(event);
				checked(false);
			}
		});

	}

	public void checked(boolean checked) {
		selectAll.setChecked(checked);
		deselectAll.setChecked(!checked);
		selectAll.setEnable(!checked);
		deselectAll.setEnable(checked);
	}
}