package com.edatasite.workforce.gwt.assessment.client.ui;


import com.edatasite.workforce.gwt.assessment.client.rpc.RatingComment;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Class intended for rendering html component which shows one of the three collaborators
 * (PEER, CLIENT, MANAGER) rate-comment or employee self rate-comment
 * Rate shows in gradient chart if skill has been rated or Not Rated text if skill has not assessed yet.
 * It will not show rate gradient chart if current skill not rateable.
 * It shows commenter name,team and comment
 */
public class SkillRateCommentBuilder extends Composite implements Constants {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static String CLIENT = Property.get(Constants.CLIENT_LIST, wfmStrings.customer());
    public static String MANAGER = wfmStrings.manager();
    public static String PEER = hrmsStrings.peer();
    public static String EMPLOYEE = hrmsStrings.employeesSelfReview();

    private static final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    private final Double rate;
    private Double weight;
    private int rating;
    private final String comment;
    private final String commenterName;
    private final String commenterType;
    private final String commenterTeam;
    private String skillName;
    private final String status;
    private String[] rateNames;
    private int[] rates;
    private int[] weights;
    private float average;
    private int width = 500;
    private FlexTable table;
    private VerticalPanel ratePanel;
    private VerticalPanel commentPanel;
    private boolean rateable = true;
    //	private String[] defaultRateNames = {"Unacceptable","Very weak","Weak","Satisfactory","Good","Very good","Exellent"};
    private final int maxRate = 7;
    private boolean reviewOnly = false;

    /**
     * @param comment        comment for skill given by commenter.
     * @param commenterName, commenter name
     * @param commenterType  (PEER, CLIENT, MANAGER, EMPLOYEE'S SELF REVIEW)
     * @param commenterTeam, commenter department
     * @param rate,          rate given by commenter
     * @param status,        Assessment curent status
     * @param rateable       if this skill rateable or not
     */
    public SkillRateCommentBuilder(String comment, String commenterName, String commenterType, String commenterTeam, Double rate, Double weight, String status, boolean rateable) {
//        MyDOM.setStyleName(getElement(), "bcg");
        this.weight = weight;
        this.comment = comment;
        this.commenterName = commenterName;
        this.commenterType = commenterType;
        this.commenterTeam = commenterTeam;
        this.status = status;
        this.rate = rate;
        this.rateable = rateable;
        init();
    }

    public SkillRateCommentBuilder(String comment, String commenterName, String commenterType, String commenterTeam, Double rate, String status, boolean rateable) {
//        MyDOM.setStyleName(getElement(), "bcg");
        this.comment = comment;
        this.commenterName = commenterName;
        this.commenterType = commenterType;
        this.commenterTeam = commenterTeam;
        this.status = status;
        this.rate = rate;
        this.rateable = rateable;
        init();
    }

    public SkillRateCommentBuilder(String comment, String commenterName, String commenterType, String commenterTeam, Double rate, Double weight, String status, boolean rateable, boolean reviewOnly) {
        this.weight = weight;
        this.reviewOnly = reviewOnly;
        this.comment = comment;
        this.commenterName = commenterName;
        this.commenterType = commenterType;
        this.commenterTeam = commenterTeam;
        this.status = status;
        this.rate = rate;
        this.rateable = rateable;
        init();
    }

    public SkillRateCommentBuilder(String comment, String commenterName, String commenterType, String commenterTeam, Double rate, String status, boolean rateable, boolean reviewOnly) {
//        MyDOM.setStyleName(getElement(), "bcg");
        this.reviewOnly = reviewOnly;
        this.comment = comment;
        this.commenterName = commenterName;
        this.commenterType = commenterType;
        this.commenterTeam = commenterTeam;
        this.status = status;
        this.rate = rate;
        this.rateable = rateable;
        init();
    }

    /**
     * @param commenterType (PEER, CLIENT, MANAGER, EMPLOYEE'S SELF REVIEW)
     * @param comment,      RatingComment data structure which contains all information
     */
    public SkillRateCommentBuilder(String commenterType, RatingComment comment) {
//        MyDOM.setStyleName(getElement(), "bcg");
        this.comment = comment.getComment();//!=null?comment.getComment():"This is auto generated comment for biten comments bla bla bla lalalalalallalalalalalallalalalalallaallalalalalallalaallalallalala";
        this.commenterName = comment.getName();
        this.commenterType = commenterType;
        this.commenterTeam = comment.getEmployeeTeam();
        this.status = comment.getStatus();
        this.rate = comment.getRating();
        this.rateable = comment.isRateable() != null && comment.isRateable();
        init();
    }

    public SkillRateCommentBuilder(String commenterType, RatingComment comment, boolean reviewOnly) {
//        MyDOM.setStyleName(getElement(), "bcg");
        this.reviewOnly = reviewOnly;
        this.comment = comment.getComment();//!=null?comment.getComment():"This is auto generated comment for biten comments bla bla bla lalalalalallalalalalalallalalalalallaallalalalalallalaallalallalala";
        this.commenterName = comment.getName();
        this.commenterType = commenterType;
        this.commenterTeam = comment.getEmployeeTeam();
        this.status = comment.getStatus();
        this.rate = comment.getRating();
        this.rateable = comment.isRateable() != null && comment.isRateable();
        init();
    }

    /**
     * @param comment        comment for skill given by commenter.
     * @param commenterName, commenter name
     * @param commenterType  (PEER, CLIENT, MANAGER, EMPLOYEE'S SELF REVIEW)
     * @param commenterTeam, commenter department
     * @param rate,          rate given by commenter
     * @param status,        Assessment curent status
     * @param width,         widget width
     */
    public SkillRateCommentBuilder(String comment, String commenterName, String commenterType, String commenterTeam, Double rate, String status, int width) {
//        MyDOM.setStyleName(getElement(), "bcg");
        this.comment = comment;
        this.commenterName = commenterName;
        this.commenterType = commenterType;
        this.commenterTeam = commenterTeam;
        this.status = status;
        this.width = width;
        this.rate = rate;
        init();
    }

    public void init() {
        ratePanel = new VerticalPanel();
        commentPanel = new VerticalPanel();
        commentPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        ratePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        ratePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        commentPanel.setSpacing(5);
        commentPanel.setWidth(width + "px");
        table = new FlexTable();
        table.setHTML(0, 0, "<div class=\"line\"></div>");
        table.getFlexCellFormatter().setColSpan(0, 0, 2);
        table.getCellFormatter().setWidth(1, 0, "70%");
        table.getCellFormatter().setAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);

        table.getCellFormatter().setWidth(1, 1, "30%");
        table.getCellFormatter().setAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
        table.setWidget(1, 0, commentPanel);
        table.setWidget(1, 1, ratePanel);
        //table.setCellSpacing(5);
        if (rateable) {
            initRate();
        }

        initComment();

//	      MyDOM.setStyleName(getElement(), "bcg");
        table.setStyleName("bcg");
        table.setWidth("100%");

        initWidget(table);
        //DOM.setAttribute(getElement(),"class","bcg");
        // DOM.setStyleAttribute(getElement(),"class","bcg");


    }

    /**
     * Builds rate gradient-chart
     */
    public void initRate() {
        if ((status != null) && (!status.equals(INITIATED)) && (rate != null)) {
                ratePanel.add(new HTML("<b class=customTitle>" + AssessmentHelper.getRatingAsString(rate) + "</b>"));
                ratePanel.add(new SkillGradePicker(rate.intValue(), "", true));
        } else {
            ratePanel.add(new HTML("<b class=customTitle>" + hrmsStrings.notRated() + "</b>"));
        }
    }

    public void initComment() {
        commentPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        commentPanel.add(new HTML("<b class=customTitle>" + commenterType + "</b>"));
        SkillComment com;
        if (!status.equals(INITIATED)) {
            com = new SkillComment(commenterName, commenterTeam, comment, (table.getOffsetWidth() / 10) * 7);
        } else {
            com = new SkillComment(commenterName, commenterTeam, "", (table.getOffsetWidth() / 10) * 7);
        }
        commentPanel.add(com);


    }
}