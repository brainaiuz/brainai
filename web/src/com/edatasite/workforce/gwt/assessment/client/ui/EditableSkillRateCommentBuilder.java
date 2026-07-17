package com.edatasite.workforce.gwt.assessment.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Class intended for rendering control which allows to comment and rate control
 */
@SuppressWarnings({"MismatchedReadAndWriteOfArray"})
public class EditableSkillRateCommentBuilder extends Composite {

    private final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private Double[] rates;
    private Double[] weights;
    private double averageRate;
    private Double weight;
    private Double rate;
    private Double lastRate;
    private boolean simple = true;
    private boolean rateble = true;
    private Double selectedRate;
    private int maxRate = 7;
    private TextArea2 comment;
    private int width = 500;
    private RadioButton[] skillRates;
    private Integer skillRatingId;
    private String lastComment;
    private Integer skillID;

    private boolean isStaticGradePicker = false;

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate) {
        this.skillRatingId = skillRatingId;
        this.lastComment = lastComment;
        this.lastRate = lastRate == null ? 0d : lastRate;
        init();
    }

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate, boolean rateble, Double[] rates) {
        this.rates = rates;
        this.skillRatingId = skillRatingId;
        this.rateble = rateble;
        this.lastRate = lastRate == null ? 0 : lastRate;
        this.lastComment = lastComment;
        this.simple = false;
        calculateAverage();
        init();
    }

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate, boolean rateble, double averageRate, boolean isStaticGradePicker) {
        this.skillRatingId = skillRatingId;
        this.rateble = rateble;
        this.lastRate = lastRate == null ? 0 : lastRate;
        this.lastComment = lastComment;
        this.simple = false;
        this.averageRate = averageRate;
        this.rate = averageRate;
        this.isStaticGradePicker = isStaticGradePicker;
        init();
    }

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate, boolean rateble, double averageRate, boolean isStaticGradePicker, Integer skillID) {
        this.skillID = skillID;
        this.skillRatingId = skillRatingId;
        this.rateble = rateble;
        this.lastRate = lastRate == null ? 0 : lastRate;
        this.lastComment = lastComment;
        this.simple = false;
        this.averageRate = averageRate;
        this.rate = averageRate;
        this.isStaticGradePicker = isStaticGradePicker;
        init();
    }

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate, boolean rateble, double averageRate) {
        this(skillRatingId, lastComment, lastRate, rateble, averageRate, false);
    }

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate, boolean rateble) {
        this(skillRatingId, lastComment, lastRate, rateble, false);
    }

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate, Double weight, boolean rateble) {
        this(skillRatingId, lastComment, lastRate, weight, rateble, false);
    }

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate, Double weight, boolean rateble, Integer skillID) {
        this(skillRatingId, lastComment, lastRate, weight, rateble, false, skillID);
    }

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate, Double weight, boolean rateble, boolean isStaticGradePicker, Integer skillID) {
        this.weight = weight;
        this.skillID = skillID;
        this.skillRatingId = skillRatingId;
        this.lastComment = lastComment;
        this.lastRate = lastRate == null ? 0 : lastRate;
        this.rateble = rateble;
        this.isStaticGradePicker = isStaticGradePicker;
        init();
    }

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate, Double weight, boolean rateble, boolean isStaticGradePicker) {
        this.weight = weight;
        this.skillRatingId = skillRatingId;
        this.lastComment = lastComment;
        this.lastRate = lastRate == null ? 0 : lastRate;
        this.rateble = rateble;
        this.isStaticGradePicker = isStaticGradePicker;
        init();
    }

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate, boolean rateble, boolean isStaticGradePicker) {
        this.skillRatingId = skillRatingId;
        this.lastComment = lastComment;
        this.lastRate = lastRate == null ? 0 : lastRate;
        this.rateble = rateble;
        this.isStaticGradePicker = isStaticGradePicker;
        init();
    }

    public EditableSkillRateCommentBuilder(Integer skillRatingId, String lastComment, Double lastRate, boolean rateble, boolean isStaticGradePicker, Integer skillID) {
        this.skillRatingId = skillRatingId;
        this.skillID = skillID;
        this.lastComment = lastComment;
        this.lastRate = lastRate == null ? 0 : lastRate;
        this.rateble = rateble;
        this.isStaticGradePicker = isStaticGradePicker;
        init();
    }

    private void calculateAverage() {
        if (rates.length > 0) {
            float average = 0;
            for (int i = 0; i < rates.length; i++) {
                average += rates[i].intValue() * (weights[i].intValue() / 100);
            }
            averageRate = (double) average / (double) (rates.length);
            rate = (double) average / (double) (rates.length);
        }
    }

    private void init() {

        FlexTable table = new FlexTable();
        table.getCellFormatter().setWidth(0, 0, "70%");
        table.getCellFormatter().setWidth(0, 1, "30%");
        if (!simple) {
            table.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_TOP);
            table.getCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
            if (rateble) {
                table.setWidget(0, 0, new HTML("<b class=customTitle>" + hrmsStrings.averageRate() + "</b>"));
                table.setWidget(0, 1, new SkillGradePicker(rate.intValue(), "" + numberFormat.format(averageRate), true));

            }
            table.getCellFormatter().setWidth(1, 0, "70%");
            table.getCellFormatter().setWidth(1, 1, "30%");
            table.getCellFormatter().setAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
            table.getCellFormatter().setAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
            table.setWidget(1, 0, getCommentField());
            if (rateble) {
                table.setWidget(1, 1, getRatePicker(lastRate));
            }
        } else {
            table.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
            table.getCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
            table.setWidget(0, 0, getCommentField());
            if (rateble) {
                table.setWidget(0, 1, getRatePicker(lastRate));
            }

        }
        table.setWidth("100%");
        initWidget(table);
    }

    public void setEnabledComment(boolean isDisable) {
        comment.setEnabled(!isDisable);
    }

    private VerticalPanel getCommentField() {
        VerticalPanel commentField = new VerticalPanel();
        commentField.setSpacing(5);
        commentField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        commentField.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        commentField.add(new HTML("<b class=customTitle>" + hrmsStrings.yourComment() + "</b>"));
        comment = new TextArea2();
        setEnabledComment(isStaticGradePicker);

        if (lastComment != null) {
            comment.setText(lastComment);
        }
        comment.setWidth(width + "px");
        comment.setHeight("70px");
        commentField.add(comment);
        return commentField;
    }
//	private VerticalPanel buildRatesRadio(Integer lastRate){
//		VerticalPanel ratesPanel = new VerticalPanel();
//		if(rateNames==null){
//			skillRates = new RadioButton[defaultRateNames.length];
//			for(int i = 0; i<defaultRateNames.length;i++){
//				skillRates[i] = new RadioButton("rateGroup"+skillRatingId,(i+1)+"-"+ defaultRateNames[i]);
//				final int selected = i+1;
//				skillRates[i].addClickListener(new ClickListener(){
//					public void onClick(Widget sender) {
//						selectedRate = Integer.valueOf(selected);
//					}});
//				if(lastRate!=null&&(lastRate.intValue()-1)==i){
//					skillRates[i].setChecked(true);
//				}
//			}
//		}else{
//			skillRates = new RadioButton[rateNames.length];
//			for(int i = 0; i<rateNames.length;i++){
//				skillRates[i] = new RadioButton("rateGroup"+skillRatingId,(i+1)+"-"+ rateNames[i]);
//				final int selected = i+1;
//				skillRates[i].addClickListener(new ClickListener(){
//					public void onClick(Widget sender) {
//						selectedRate = Integer.valueOf(selected);
//					}});
//				if(lastRate!=null&&(lastRate.intValue()-1)==i){
//					skillRates[i].setChecked(true);
//				}
//			}
//		}
//		for(int i = skillRates.length-1;i>=0;i--){
//			ratesPanel.add(skillRates[i]);
//		}
//		return ratesPanel;
//	}

    private VerticalPanel ratePickerPanel;

    public void showRatePicker(boolean isShow) {
        if (isShow) {
            ratePickerPanel.setStyleName("show-rate");
        } else {
            ratePickerPanel.setStyleName("hide-rate");
        }
    }

    private VerticalPanel getRatePicker(Double lastRate) {
        selectedRate = lastRate;

        ratePickerPanel = new VerticalPanel();

        ratePickerPanel.add(new HTML("<b class=customTitle>" + hrmsStrings.yourRate() + "</b>"));
//        if (isKanOklaCompany) {
//            final TextBox rateTextBox = new TextBox();
//            rateTextBox.setWidth("100px");
//            rateTextBox.setTextAlignment(TextBox.ALIGN_RIGHT);
//            Validation.addNumericKeyboardListener(rateTextBox, 2);
//
//            if (isStaticGradePicker) {
//                rateTextBox.setText(lastRate > 0 ? numberFormat.format(lastRate) : "0.00");
//                rateTextBox.setReadOnly(true);
//            } else {
//                rateTextBox.setText(lastRate > 0 ? numberFormat.format(lastRate) : "");
//            }
//
//            rateTextBox.addKeyUpHandler(new KeyUpHandler() {
//                public void onKeyUp(KeyUpEvent event) {
//                    Double rate;
//                    try {
//                        rate = Double.parseDouble(rateTextBox.getText().trim());
//                    } catch (NumberFormatException e) {
//                        rate = 0d;
//                    }
//                    selectedRate = rate;
//                    String[] returnings = new String[]{skillID.toString(), rateTextBox.getText().trim()};
//                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SKILL_CHANGED, returnings, EditableSkillRateCommentBuilder.this);
//                }
//            });
//            ratePickerPanel.add(rateTextBox);
//            ratePickerPanel.setSpacing(5);
//        } else {
//            SkillGradePicker gradePicker;
//            final HTML rateName = new HTML();
//            if (isStaticGradePicker) {
//                gradePicker = new SkillGradePicker(lastRate.intValue(), true);
//            } else {
//                gradePicker = new SkillGradePicker(lastRate.intValue());
//            }
//            gradePicker.setFireEvent(new SkillGradePicker.PickerEvents() {
//                public void clicked(int rate) {
//                    selectedRate = (double) rate;
//                    Integer[] returnings = new Integer[]{skillID, rate};
//                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SKILL_CHANGED, returnings, EditableSkillRateCommentBuilder.this);
//                    rateName.setHTML("<b class=customTitle>" + AssessmentHelper.getRatingAsString((double) rate) + "</b>");
//                }
//
//                public void mouseOut(int rate) {
//                    rateName.setHTML("<b class=customTitle>" + AssessmentHelper.getRatingAsString(selectedRate) + "</b>");
//                }
//
//                public void mouseHovered(int rate) {
//                    rateName.setHTML("<b class=customTitle>" + AssessmentHelper.getRatingAsString((double) rate) + "</b>");
//                }
//            });
//            rateName.setHTML("<b class=customTitle>" + AssessmentHelper.getRatingAsString(lastRate) + "</b>");
//            ratePickerPanel.add(gradePicker);
//            ratePickerPanel.add(rateName);
//        }

        return ratePickerPanel;
    }
//    private FlexTable renderdRatesRadio(Integer lastRate){
//		//VerticalPanel ratesPanel = new VerticalPanel();
//        FlexTable ratesTable = new FlexTable();
//            skillRates = new RadioButton[7];
//			for(int i = 1; i<=7;i++){
//				skillRates[i-1] = new RadioButton("rateGroup"+skillRatingId,(i)+"-"+ AssessmentHelper.getRatingAsString(Integer.valueOf(i)));
//				final int selected = i;
//				skillRates[i-1].addClickListener(new ClickListener(){
//					public void onClick(Widget sender) {
//						selectedRate = Integer.valueOf(selected);
//					}});
//				if(lastRate!=null&&(lastRate.intValue())==i){
//					skillRates[i-1].setChecked(true);
//				}
//			}
//        int row = 0;
//        for(int i = skillRates.length-1;i>=0;i--){
//            if(i>2){
//                ratesTable.setWidget(row,0,skillRates[i]);
//            }else{
//                ratesTable.setWidget(row,1,skillRates[i]);
//            }
//            row++;
//            if(i==3){
//                row = 0;
//            }
//        }
//		return ratesTable;

    //	}

    public String getComment() {
        return comment.getText();
    }

    public Double getRate() {
        return selectedRate;
    }
}
