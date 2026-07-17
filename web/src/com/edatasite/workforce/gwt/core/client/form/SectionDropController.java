package com.edatasite.workforce.gwt.core.client.form;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Hurshid on 3/5/2018.
 */
public class SectionDropController extends VerticalPanelDropController {

    private List<DynamicSectionsRpc> list = new LinkedList<>();

    public SectionDropController(VerticalPanel dropTarget) {
        super(dropTarget);
    }

    @Override
    public void onDrop(DragContext context) {
        super.onDrop(context);

        VerticalPanel verticalPanel = (VerticalPanel) dropTarget;

        SectionSideNavBox.Section field = (SectionSideNavBox.Section) context.draggable;
        DynamicSectionsRpc rpc = field.getSectionsRpc();

        int index = verticalPanel.getWidgetIndex(field);

        list.remove(rpc);
        list.add(index, rpc);
    }

    public void setList(List<DynamicSectionsRpc> list) {
        this.list = list;
    }
}
