package com.edatasite.workforce.gwt.accounting.client.ui.view.widgets;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCommentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCommentList;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Mirjalol
 * Date: 20.06.13
 * Time: 2:11
 * To change this template use File | Settings | File Templates.
 */
public class ProductCommentPanel extends VerticalPanel {

    private WfmStrings wfmStrings = WfmStrings.App.get();
    private AccountingStrings accountingStrings = AccountingStrings.App.get();




    private ProductCommentList productCommentList;
    private Label commentLabel;
    private TextArea2 commentArea;
    private Button commentButton;
    private HTMLPanel statusField;
    private FlexTable commentList;

    protected interface Resource extends ClientBundle {
        @CssResource.NotStrict
        @Source("productComment.css")
        CssResource productComment();
    }

    public ProductCommentPanel(ProductCommentList productCommentList) {
        super();
        this.productCommentList = productCommentList;
        ((Resource) GWT.create(Resource.class)).productComment().ensureInjected();
        initialize();
    }

    private void initialize() {
        setSpacing(10);
        setStyleName("productCommentPanel");
        commentLabel = new Label();
        commentLabel.setTextAsHtml("<b>" + wfmStrings.addYourComment() + "</b>");
        commentArea = new TextArea2(5000);
        commentArea.setHeight(100);
        commentArea.setWidth(450);
        commentArea.getTextArea().addKeyUpHandler(event -> commentButton.setEnabled(commentArea.getText().length() > 0));
        commentButton = new Button(wfmStrings.addComment());
        commentButton.setEnabled(false);
        commentButton.addClickHandler(event -> {
            final ProductCommentItem commentItem = new ProductCommentItem();
            commentItem.setProductId(productCommentList.getObjectId());
            commentItem.setUserId(productCommentList.getUserId());
            commentItem.setUserFullName(productCommentList.getUserFullName());
            commentItem.setUserPictureUrl(productCommentList.getUserPictureUrl());
            commentItem.setDate(new Date());
            commentItem.setText(commentArea.getText().replace("\n", "<br />"));
            ProductService.App.get().saveProductComment(commentItem, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Integer objectId) {
                    commentItem.setObjectId(objectId);
                    createComment(commentItem);
                    commentArea.getTextArea().setText("");
                    commentArea.setItemFocus(true);
                    commentButton.setEnabled(false);
                }
            });
        });
        setCellHorizontalAlignment(commentButton, HasHorizontalAlignment.ALIGN_RIGHT);
        statusField = new HTMLPanel("center", "");
        commentList = new FlexTable();
        commentList.setWidth("100%");
        commentList.setCellSpacing(5);
        drawPanel();
    }

    private void drawPanel() {
        add(commentLabel);
        add(commentArea);
        add(commentButton);
        add(statusField);
        add(commentList);
        drawExistComments();
    }

    private void createComment(ProductCommentItem commentItem) {
        ProductCommentListItem item = new ProductCommentListItem(commentItem);
        item.setStyleName("productCommentItem");
        commentList.setWidget(commentList.insertRow(0), 0, item);
    }

    private void drawExistComments() {
        statusField.clear();
        if (productCommentList.getItems().length > 0) {
            for (ProductCommentItem commentItem : productCommentList.getItems()) {
                createComment(commentItem);
            }
        } else {
            statusField.add(new HTML(wfmStrings.thereAreNoCommentsYet()));
        }

    }

    protected class ProductCommentListItem extends HorizontalPanel {

        private ProductCommentItem commentItem;
        private HTML commentText;
        private TextArea2 editArea;
        private boolean isEditMode = false;
        private SimpleLink editSaveLink;
        private SimpleLink deleteCancelLink;

        public ProductCommentListItem(ProductCommentItem item) {
            this.commentItem = item;
            createCommentItem();
            setSpacing(15);
            if (productCommentList.getUserId() == commentItem.getUserId()) {
                editSaveLink.setVisible(true);
                deleteCancelLink.setVisible(true);
            }
        }

        private void createCommentItem() {
            DateTimeFormat format = DateTimeFormat.getFormat("MMMM dd, yyyy HH:mm:ss");
            FlowPanel imagePanel = new FlowPanel();
            Image userImage = null;
            if (commentItem.getUserPictureUrl() != null) {
                userImage = new Image(commentItem.getUserPictureUrl());
            } else {
                userImage = new Image();
            }
            userImage.setStyleName("commentorPicture");
            imagePanel.add(userImage);
            add(imagePanel);
            setCellWidth(imagePanel, "15%");

            VerticalPanel panel = new VerticalPanel();
            panel.setSpacing(8);
            panel.setWidth("100%");
            panel.add(new HTML("<b>" + commentItem.getUserFullName() + "</b>"));
            commentText = new HTML(commentItem.getText());
            panel.add(commentText);
            editArea = new TextArea2(5000);
            editArea.setSize(500, 60);
            editArea.setVisible(false);
            panel.add(editArea);
            HorizontalPanel bottomPanel = new HorizontalPanel();
            bottomPanel.setWidth("100%");
            bottomPanel.add(new HTML("<b>" + format.format(commentItem.getDate()) + "</b>"));
            editSaveLink = new SimpleLink(wfmStrings.edit());
            editSaveLink.addClickHandler(event -> {
                if (isEditMode) {
                    isEditMode = false;
                    ProductCommentItem updateItem = new ProductCommentItem();
                    updateItem.setObjectId(commentItem.getObjectId());
                    updateItem.setText(editArea.getTextArea().getText().replace("\n", "<br />"));
                    ProductService.App.get().updateProductComment(updateItem, new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void onSuccess(Void result) {
                            editSaveLink.getElement().getFirstChildElement().setInnerText(wfmStrings.edit());
                            deleteCancelLink.getElement().getFirstChildElement().setInnerText(wfmStrings.delete());
                            commentText.setHTML(editArea.getTextArea().getText().replace("\n", "<br />"));
                            editArea.setVisible(false);
                            commentText.setVisible(true);
                        }
                    });
                } else {
                    isEditMode = true;
                    editSaveLink.getElement().getFirstChildElement().setInnerText(wfmStrings.save());
                    deleteCancelLink.getElement().getFirstChildElement().setInnerText(wfmStrings.cancel());
                    editArea.getTextArea().setText(commentText.getHTML().replace("<br>", "\n"));
                    commentText.setVisible(false);
                    editArea.setVisible(true);
                    editArea.getTextArea().setFocus(true);
                }
            });
            editSaveLink.setVisible(false);
            deleteCancelLink = new SimpleLink(wfmStrings.delete());
            deleteCancelLink.addClickHandler(event -> {
                if (isEditMode) {
                    isEditMode = false;
                    editSaveLink.getElement().getFirstChildElement().setInnerText(wfmStrings.edit());
                    deleteCancelLink.getElement().getFirstChildElement().setInnerText(wfmStrings.delete());
                    editArea.setVisible(false);
                    commentText.setVisible(true);
                } else {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, accountingStrings.areYouSureYouWantDeleteThisComment(),
                            new CloseHandler() {
                                @Override
                                public void onCancel() {
                                }

                                @Override
                                public void onSubmit() {
                                    ProductService.App.get().deleteProductComment(commentItem.getObjectId(), new AsyncCallback<Void>() {
                                        @Override
                                        public void onFailure(Throwable caught) {
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        @Override
                                        public void onSuccess(Void result) {
                                            ProductCommentListItem.this.getElement().getParentElement().getParentElement().removeFromParent();
                                        }
                                    });
                                }
                            });
                    messageBox.setTitle(wfmStrings.confirmation());
                    messageBox.open();
                }
            });
            deleteCancelLink.setVisible(false);
            bottomPanel.add(editSaveLink);
            bottomPanel.add(deleteCancelLink);
            panel.add(bottomPanel);
            statusField.clear();
            add(panel);
            setCellWidth(panel, "85%");
        }
    }
}
