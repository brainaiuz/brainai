package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.SkillCommentItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.MyDisclosurePanelImages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DisclosurePanelImages;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.List;

/**
 * User: Ilhombek
 * Date: 12/12/12
 * Time: 10:08 PM
 */
public class SkillCommentHistoryPanel extends Composite {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private boolean isFirstEmployeeComment;
    private List<SkillCommentItem> ratingCommentItems;

    @UiField
    HTMLPanel content;

    interface SkillCommentHistoryPanelUiBinder extends UiBinder<HTMLPanel, SkillCommentHistoryPanel> {
    }

    public SkillCommentHistoryPanel(List<SkillCommentItem> ratingCommentItems, boolean isFirstEmployeeComment) {
        SkillCommentHistoryPanelUiBinder ourUiBinder = GWT.create(SkillCommentHistoryPanelUiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));
        this.ratingCommentItems = ratingCommentItems;
        this.isFirstEmployeeComment = isFirstEmployeeComment;

        drawInitialize();
    }

    private void drawInitialize() {

        DisclosurePanelImages images = GWT.create(MyDisclosurePanelImages.class);
        DisclosurePanel commentsHistoryPanel = new DisclosurePanel(images, "Comment history", false);
        commentsHistoryPanel.setWidth("100%");
        content.add(commentsHistoryPanel);

        VerticalPanel commentHP = new VerticalPanel();
        commentHP.setWidth("100%");
        commentsHistoryPanel.add(commentHP);
        for (SkillCommentItem commentItem : ratingCommentItems) {
            //
            if (isFirstEmployeeComment) {
                if (commentItem.getEmployeeComment() != null && !"".equals(commentItem.getEmployeeComment())) {
                    addCommentToPanel("Employee's Comment", commentItem.getEmployeeComment(), DateUtils.formatInternal(commentItem.getCreatedDate()), commentHP);
                } else if (commentItem.getReviewerComment() != null && !"".equals(commentItem.getReviewerComment())) {
                    addCommentToPanel("Manager's Comment", commentItem.getReviewerComment(), DateUtils.formatInternal(commentItem.getCreatedDate()), commentHP);
                }
            } else {
                if (commentItem.getReviewerComment() != null && !"".equals(commentItem.getReviewerComment())) {
                    addCommentToPanel("Manager's Comment", commentItem.getReviewerComment(), DateUtils.formatInternal(commentItem.getCreatedDate()), commentHP);
                } else if (commentItem.getEmployeeComment() != null && !"".equals(commentItem.getEmployeeComment())) {
                    addCommentToPanel("Employee's Comment", commentItem.getEmployeeComment(), DateUtils.formatInternal(commentItem.getCreatedDate()), commentHP);
                }
            }
        }

    }

    private void addCommentToPanel(String commenterType, String comment, String date, VerticalPanel commentHP) {
        FlexTable table = new FlexTable();
        table.getElement().addClassName("noteTable");
        table.setWidth("100%");

        HTML commentHTML = new HTML("<b class=customTitle>" + commenterType + "</b></br>" + formatString(comment));
        commentHTML.setWidth("100%");
        commentHTML.getElement().addClassName("notesContent");

        table.setWidget(0, 0, commentHTML);
        table.getCellFormatter().setWidth(0, 0, "100%");
        final FlowPanel f = new FlowPanel();
        f.addStyleName("notesFooter");
        HTML dateHTML = new HTML(date);
        dateHTML.addStyleName("noteDate");
        f.add(dateHTML);
        table.setWidget(1, 0, f);
        table.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LOCALE_END);
        commentHP.add(table);
    }

    private String formatString(String text) {
        return text != null && !"".equals(text) ? text.replace("\r\n", "<br/>").replace("\n", "<br/>") : text;
    }
}
