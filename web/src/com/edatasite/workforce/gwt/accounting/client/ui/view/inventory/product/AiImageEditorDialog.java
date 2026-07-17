package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product;

import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AiImageEditorDialog extends KpiModal {

    private static final String ACTION_ENHANCE = "ENHANCE";
    private static final String ACTION_REMOVE_BG = "REMOVE_BG";
    private static final String ACTION_PROMPT = "PROMPT";
    private static final String ACTION_ROTATE_RIGHT = "ROTATE_RIGHT";
    private static final String ACTION_ROTATE_LEFT = "ROTATE_LEFT";
    private static final String ACTION_ROTATE_180 = "ROTATE_180";

    private final ProductServiceAsync productService = ProductService.App.get();

    // Left panel - image thumbnails
    private final FlowPanel thumbList = new FlowPanel();

    // Center - preview
    private final Image previewImage = new Image();
    private final Div previewContainer = new Div();

    // Basic tools
    private final WfmButton2 rotateLeftBtn = new WfmButton2("↺", WfmButton2.BTN_LIGHTGREY);
    private final WfmButton2 rotateRightBtn = new WfmButton2("↻", WfmButton2.BTN_LIGHTGREY);
    private final WfmButton2 flipBtn = new WfmButton2("⇔", WfmButton2.BTN_LIGHTGREY);
    private final WfmButton2 zoomInBtn = new WfmButton2("+", WfmButton2.BTN_LIGHTGREY);
    private final WfmButton2 zoomOutBtn = new WfmButton2("−", WfmButton2.BTN_LIGHTGREY);
    private final ListBox aspectRatioList = new ListBox();

    // AI tools
    private final TextArea promptArea = new TextArea();
    private final ListBox savedPromptsBox = new ListBox();
    private final ListBox suggestedPromptsBox = new ListBox();
    private final WfmButton2 savePromptBtn = new WfmButton2("Save Prompt", WfmButton2.BTN_LIGHTGREY);
    private final WfmButton2 enhanceBtn = new WfmButton2("✨ AI Auto Enhance", WfmButton2.BTN_PRIMARY);
    private final WfmButton2 removeBgBtn = new WfmButton2("Remove Background", WfmButton2.BTN_DEFAULT);
    private final WfmButton2 generateBtn = new WfmButton2("Generate", WfmButton2.BTN_SUCCESS);
    private final Label aiUnavailableLabel = new Label("Currently unavailable, our team is working on resolving the case");
    private final WfmButton2 retryAiBtn = new WfmButton2("Retry", WfmButton2.BTN_LIGHTGREY);

    // Generated versions
    private final FlowPanel versionsPanel = new FlowPanel();
    private final List<ProductPicture> generatedVersions = new ArrayList<>();
    private final Map<ProductFile, List<ProductPicture>> generatedVersionsMap = new HashMap<>();
    private ProductPicture selectedPicture;

    // Footer buttons
    private final WfmButton2 resetBtn = new WfmButton2("Reset", WfmButton2.BTN_LIGHTGREY);
    private final WfmButton2 cancelBtn = new WfmButton2("Cancel", WfmButton2.BTN_DEFAULT);
    private final WfmButton2 saveBtn = new WfmButton2("Save", WfmButton2.BTN_PRIMARY);

    // State
    private ProductFile selectedFile;
    private String originalUrl;
    private int rotationDeg = 0;
    private boolean flipped = false;
    private double zoomScale = 1.0;
    private boolean aiAvailable = true;
    private AiSavedPrompt[] savedPrompts = new AiSavedPrompt[0];

    private Runnable onSaveCallback;

    public AiImageEditorDialog(List<ProductFile> files) {
        super();
        setTitle("AI Image Studio (Beta)");
        buildLayout(files);
        loadAiStatus();
        loadSavedPrompts();
        loadSuggestedPrompts();
        setWidth(1100);
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    // ── Layout ────────────────────────────────────────────────────────────────

    private void buildLayout(List<ProductFile> files) {
        HorizontalPanel root = new HorizontalPanel();
        root.setWidth("100%");
        root.getElement().getStyle().setProperty("alignItems", "flex-start");

        // Left: thumbnail list
        FlowPanel leftPanel = new FlowPanel();
        leftPanel.setStyleName("ai-studio__thumbs");
        leftPanel.getElement().getStyle().setWidth(160, Style.Unit.PX);
        leftPanel.getElement().getStyle().setProperty("minWidth", "160px");
        leftPanel.getElement().getStyle().setOverflowY(Style.Overflow.AUTO);
        leftPanel.getElement().getStyle().setProperty("maxHeight", "600px");
        leftPanel.getElement().getStyle().setMarginRight(16, Style.Unit.PX);
        leftPanel.add(thumbList);
        buildThumbnails(files);

        // Right: everything else
        FlowPanel rightPanel = new FlowPanel();
        rightPanel.setStyleName("ai-studio__main");
        rightPanel.getElement().getStyle().setProperty("flex", "1");

        rightPanel.add(buildPreviewSection());
        rightPanel.add(buildToolsSection());
        rightPanel.add(buildAiSection());
        rightPanel.add(buildVersionsSection());

        root.add(leftPanel);
        root.add(rightPanel);

        add(root);

        addButton(resetBtn);
        addButton(cancelBtn);
        addButton(saveBtn);

        wireFooterButtons();
    }

    private void buildThumbnails(List<ProductFile> files) {
        for (ProductFile file : files) {
            if (!file.isDone() || file.getPicture() == null) continue;
            addThumbnail(file);
            if (selectedFile == null) selectImage(file);
        }
    }

    private void addThumbnail(ProductFile file) {
        Div item = new Div();
        item.setStyleName("ai-studio__thumb");
        item.getElement().getStyle().setProperty("cursor", "pointer");
        item.getElement().getStyle().setMarginBottom(8, Style.Unit.PX);
        item.getElement().getStyle().setBorderWidth(2, Style.Unit.PX);
        item.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        item.getElement().getStyle().setBorderColor("transparent");

        Image thumb = new Image(file.getPicture().getUrl());
        thumb.setWidth("140px");
        thumb.getElement().getStyle().setProperty("objectFit", "cover");
        item.add(thumb);

        item.addClickHandler(event -> selectImage(file));
        thumbList.add(item);
    }

    private FlowPanel buildPreviewSection() {
        FlowPanel section = new FlowPanel();
        section.setStyleName("ai-studio__preview-section");
        section.getElement().getStyle().setMarginBottom(12, Style.Unit.PX);

        previewContainer.setStyleName("ai-studio__preview");
        previewContainer.getElement().getStyle().setProperty("display", "flex");
        previewContainer.getElement().getStyle().setProperty("justifyContent", "center");
        previewContainer.getElement().getStyle().setProperty("alignItems", "center");
        previewContainer.getElement().getStyle().setProperty("background", "#f5f5f5");
        previewContainer.getElement().getStyle().setHeight(320, Style.Unit.PX);
        previewContainer.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        previewContainer.getElement().getStyle().setProperty("borderRadius", "4px");

        previewImage.setStyleName("ai-studio__preview-img");
        previewImage.getElement().getStyle().setProperty("maxHeight", "300px");
        previewImage.getElement().getStyle().setProperty("maxWidth", "100%");
        previewImage.getElement().getStyle().setProperty("objectFit", "contain");
        previewImage.getElement().getStyle().setProperty("transition", "transform 0.2s");

        previewContainer.add(previewImage);
        section.add(previewContainer);
        return section;
    }

    private FlowPanel buildToolsSection() {
        FlowPanel section = new FlowPanel();
        section.setStyleName("ai-studio__tools");
        section.getElement().getStyle().setMarginBottom(12, Style.Unit.PX);

        Label toolsLabel = new Label("Basic Tools");
        toolsLabel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        toolsLabel.getElement().getStyle().setMarginBottom(6, Style.Unit.PX);
        toolsLabel.getElement().getStyle().setProperty("display", "block");

        HorizontalPanel toolBar = new HorizontalPanel();
        toolBar.setSpacing(4);

        rotateLeftBtn.setTitle("Rotate Left");
        rotateRightBtn.setTitle("Rotate Right");
        flipBtn.setTitle("Flip Horizontal");
        zoomInBtn.setTitle("Zoom In");
        zoomOutBtn.setTitle("Zoom Out");

        setupAspectRatioList();

        toolBar.add(rotateLeftBtn);
        toolBar.add(rotateRightBtn);
        toolBar.add(flipBtn);
        toolBar.add(zoomInBtn);
        toolBar.add(zoomOutBtn);
        toolBar.add(aspectRatioList);

        section.add(toolsLabel);
        section.add(toolBar);

        wireBasicTools();
        return section;
    }

    private FlowPanel buildAiSection() {
        FlowPanel section = new FlowPanel();
        section.setStyleName("ai-studio__ai");
        section.getElement().getStyle().setMarginBottom(12, Style.Unit.PX);
        section.getElement().getStyle().setPadding(12, Style.Unit.PX);
        section.getElement().getStyle().setProperty("background", "#fafafa");
        section.getElement().getStyle().setProperty("borderRadius", "4px");
        section.getElement().getStyle().setProperty("border", "1px solid #e0e0e0");

        Label aiLabel = new Label("AI Editing (Nano Banana)");
        aiLabel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        aiLabel.getElement().getStyle().setMarginBottom(8, Style.Unit.PX);
        aiLabel.getElement().getStyle().setProperty("display", "block");

        // Unavailability notice
        aiUnavailableLabel.setStyleName("ai-studio__unavailable");
        aiUnavailableLabel.getElement().getStyle().setColor("#d32f2f");
        aiUnavailableLabel.getElement().getStyle().setMarginBottom(8, Style.Unit.PX);
        aiUnavailableLabel.getElement().getStyle().setProperty("display", "block");
        aiUnavailableLabel.setVisible(false);

        retryAiBtn.setVisible(false);
        retryAiBtn.addClickHandler(e -> loadAiStatus());

        // Prompt textarea
        promptArea.setVisibleLines(3);
        promptArea.setCharacterWidth(60);
        promptArea.getElement().setAttribute("placeholder", "Type your editing instructions (e.g. remove background and place on white, improve lighting)");
        promptArea.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        promptArea.getElement().getStyle().setMarginBottom(8, Style.Unit.PX);
        promptArea.getElement().getStyle().setProperty("boxSizing", "border-box");

        // Saved prompts + suggestions row
        HorizontalPanel promptControls = new HorizontalPanel();
        promptControls.setSpacing(8);
        promptControls.getElement().getStyle().setMarginBottom(8, Style.Unit.PX);

        savedPromptsBox.addItem("Saved prompts...");
        savedPromptsBox.addChangeHandler(e -> {
            int idx = savedPromptsBox.getSelectedIndex();
            if (idx > 0 && savedPrompts != null && idx - 1 < savedPrompts.length) {
                promptArea.setText(savedPrompts[idx - 1].getText());
            }
        });

        suggestedPromptsBox.addItem("Suggested prompts...");
        suggestedPromptsBox.addChangeHandler(e -> {
            int idx = suggestedPromptsBox.getSelectedIndex();
            if (idx > 0) {
                promptArea.setText(suggestedPromptsBox.getValue(idx));
            }
        });

        savePromptBtn.addClickHandler(e -> saveCurrentPrompt());

        promptControls.add(savedPromptsBox);
        promptControls.add(suggestedPromptsBox);
        promptControls.add(savePromptBtn);

        // AI action buttons
        HorizontalPanel aiButtons = new HorizontalPanel();
        aiButtons.setSpacing(8);

        enhanceBtn.addClickHandler(e -> processAi(ACTION_ENHANCE, null));
        removeBgBtn.addClickHandler(e -> processAi(ACTION_REMOVE_BG, null));
        generateBtn.addClickHandler(e -> {
            String prompt = promptArea.getText().trim();
            if (prompt.isEmpty()) {
                Info.show("Please enter instructions before generating", Info.Type.WARNING);
                return;
            }
            processAi(ACTION_PROMPT, prompt);
        });

        aiButtons.add(enhanceBtn);
        aiButtons.add(removeBgBtn);
        aiButtons.add(generateBtn);

        section.add(aiLabel);
        section.add(aiUnavailableLabel);
        section.add(retryAiBtn);
        section.add(promptArea);
        section.add(promptControls);
        section.add(aiButtons);

        return section;
    }

    private FlowPanel buildVersionsSection() {
        FlowPanel section = new FlowPanel();
        section.setStyleName("ai-studio__versions");

        Label versionsLabel = new Label("Generated Versions");
        versionsLabel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        versionsLabel.getElement().getStyle().setMarginBottom(6, Style.Unit.PX);
        versionsLabel.getElement().getStyle().setProperty("display", "block");

        versionsPanel.getElement().getStyle().setProperty("display", "flex");
        versionsPanel.getElement().getStyle().setProperty("flexWrap", "wrap");
        versionsPanel.getElement().getStyle().setProperty("gap", "8px");
        versionsPanel.getElement().getStyle().setProperty("overflowX", "auto");
        versionsPanel.getElement().getStyle().setMarginTop(8, Style.Unit.PX);

        section.add(versionsLabel);
        section.add(versionsPanel);
        return section;
    }

    private void setupAspectRatioList() {
        aspectRatioList.addItem("Free", "free");
        aspectRatioList.addItem("1:1", "1:1");
        aspectRatioList.addItem("4:3", "4:3");
        aspectRatioList.addItem("16:9", "16:9");
        aspectRatioList.addItem("3:4", "3:4");
        aspectRatioList.addItem("9:16", "9:16");
        aspectRatioList.setTitle("Aspect Ratio");
        aspectRatioList.addChangeHandler(e -> applyAspectRatio(aspectRatioList.getSelectedValue()));
    }

    // ── Wire buttons ─────────────────────────────────────────────────────────

    private void wireBasicTools() {
        rotateLeftBtn.addClickHandler(e -> {
            rotationDeg = (rotationDeg - 90 + 360) % 360;
            applyTransform();
        });
        rotateRightBtn.addClickHandler(e -> {
            rotationDeg = (rotationDeg + 90) % 360;
            applyTransform();
        });
        flipBtn.addClickHandler(e -> {
            flipped = !flipped;
            applyTransform();
        });
        zoomInBtn.addClickHandler(e -> {
            zoomScale = Math.min(zoomScale + 0.2, 3.0);
            applyTransform();
        });
        zoomOutBtn.addClickHandler(e -> {
            zoomScale = Math.max(zoomScale - 0.2, 0.2);
            applyTransform();
        });
    }

    private void wireFooterButtons() {
        resetBtn.addClickHandler(e -> resetChanges());
        cancelBtn.addClickHandler(e -> close());
        saveBtn.addClickHandler(e -> saveResult());
    }

    // ── Image selection ───────────────────────────────────────────────────────

    private void selectImage(ProductFile file) {
        // save versions for the image we're leaving
        if (selectedFile != null) {
            generatedVersionsMap.put(selectedFile, new ArrayList<>(generatedVersions));
        }

        selectedFile = file;
        originalUrl = file.getPicture().getUrl();
        selectedPicture = null;
        previewImage.setUrl(originalUrl);
        rotationDeg = 0;
        flipped = false;
        zoomScale = 1.0;
        applyTransform();

        // restore versions for the newly selected image
        generatedVersions.clear();
        versionsPanel.clear();
        List<ProductPicture> saved = generatedVersionsMap.get(file);
        if (saved != null) {
            for (ProductPicture p : saved) {
                addGeneratedVersion(p);
            }
        }
    }

    // ── Transforms (client-side CSS only) ────────────────────────────────────

    private void applyTransform() {
        String transform = "rotate(" + rotationDeg + "deg)";
        if (flipped) transform += " scaleX(-1)";
        transform += " scale(" + zoomScale + ")";
        previewImage.getElement().getStyle().setProperty("transform", transform);
    }

    private void applyAspectRatio(String ratio) {
        if ("free".equals(ratio) || ratio == null) {
            previewContainer.getElement().getStyle().clearProperty("aspectRatio");
            previewImage.getElement().getStyle().clearProperty("maxWidth");
            return;
        }
        previewContainer.getElement().getStyle().setProperty("aspectRatio", ratio.replace(":", "/"));
    }

    // ── AI operations ─────────────────────────────────────────────────────────

    private void loadAiStatus() {
        productService.isAiAvailable(new AbstractAsyncCallback<Boolean>() {
            @Override
            public void success(Boolean available) {
                aiAvailable = available != null && available;
                updateAiUi();
            }

            @Override
            public void failure(Throwable caught) {
                aiAvailable = false;
                updateAiUi();
            }
        });
    }

    private void updateAiUi() {
        aiUnavailableLabel.setVisible(!aiAvailable);
        retryAiBtn.setVisible(!aiAvailable);
        enhanceBtn.setEnabled(aiAvailable);
        removeBgBtn.setEnabled(aiAvailable);
        generateBtn.setEnabled(aiAvailable);
        promptArea.setEnabled(aiAvailable);
    }

    private void loadSavedPrompts() {
        productService.getSavedAiPrompts(new AbstractAsyncCallback<AiSavedPrompt[]>() {
            @Override
            public void success(AiSavedPrompt[] prompts) {
                savedPrompts = prompts != null ? prompts : new AiSavedPrompt[0];
                savedPromptsBox.clear();
                savedPromptsBox.addItem("Saved prompts...");
                for (AiSavedPrompt p : savedPrompts) {
                    savedPromptsBox.addItem(truncate(p.getText(), 40), p.getText());
                }
            }
        });
    }

    private void loadSuggestedPrompts() {
        productService.getAiSuggestedPrompts(new AbstractAsyncCallback<String[]>() {
            @Override
            public void success(String[] suggestions) {
                suggestedPromptsBox.clear();
                suggestedPromptsBox.addItem("Suggested prompts...");
                if (suggestions != null) {
                    for (String s : suggestions) {
                        suggestedPromptsBox.addItem(truncate(s, 40), s);
                    }
                }
            }
        });
    }

    private void processAi(String actionType, String prompt) {
        if (selectedFile == null || selectedFile.getPicture() == null) {
            Info.show("Please select an image first", Info.Type.WARNING);
            return;
        }
        Integer pictureId = selectedFile.getPicture().getPictureID();
        if (pictureId == null) {
            Info.show("Image is not yet uploaded", Info.Type.WARNING);
            return;
        }

        String selectedRatio = aspectRatioList.getSelectedValue();
        String aspectRatio = ("free".equals(selectedRatio) || selectedRatio == null) ? null : selectedRatio;

        setAiButtonsEnabled(false);
        LoadingPanel.loading(true);

        productService.processAiImage(pictureId, prompt, actionType, aspectRatio, new AbstractAsyncCallback<AiImageResult>() {
            @Override
            public void success(AiImageResult result) {
                LoadingPanel.loading(false);
                setAiButtonsEnabled(true);

                if (result == null || !result.isAiAvailable()) {
                    aiAvailable = false;
                    updateAiUi();
                    return;
                }

                List<ProductPicture> pictures = result.getGeneratedPictures();
                if (pictures != null && !pictures.isEmpty()) {
                    for (ProductPicture picture : pictures) {
                        addGeneratedVersion(picture);
                    }
                    selectVersion(pictures.get(0));
                } else {
                    Info.show("No results returned. Try a different prompt.", Info.Type.WARNING);
                }
            }

            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                setAiButtonsEnabled(true);
                aiAvailable = false;
                updateAiUi();
            }
        });
    }

    private void addGeneratedVersion(ProductPicture picture) {
        generatedVersions.add(picture);

        Div versionItem = new Div();
        versionItem.getElement().getStyle().setProperty("cursor", "pointer");
        versionItem.getElement().getStyle().setBorderWidth(2, Style.Unit.PX);
        versionItem.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        versionItem.getElement().getStyle().setBorderColor("transparent");
        versionItem.getElement().getStyle().setProperty("borderRadius", "4px");
        versionItem.getElement().getStyle().setPadding(2, Style.Unit.PX);

        Image versionImg = new Image(picture.getUrl());
        versionImg.setHeight("80px");
        versionImg.getElement().getStyle().setProperty("objectFit", "cover");
        versionItem.add(versionImg);

        versionItem.addClickHandler(event -> {
            selectVersion(picture);
            // clear all highlights then highlight selected
            for (int i = 0; i < versionsPanel.getWidgetCount(); i++) {
                versionsPanel.getWidget(i).getElement().getStyle().setProperty("borderColor", "transparent");
            }
            versionItem.getElement().getStyle().setProperty("borderColor", "#1976d2");
        });

        versionsPanel.add(versionItem);
    }

    private void selectVersion(ProductPicture picture) {
        selectedPicture = picture;
        previewImage.setUrl(picture.getUrl());
        rotationDeg = 0;
        flipped = false;
        zoomScale = 1.0;
        applyTransform();
    }

    private void saveCurrentPrompt() {
        String text = promptArea.getText().trim();
        if (text.isEmpty()) return;

        productService.saveAiPrompt(text, new AbstractAsyncCallback<Void>() {
            @Override
            public void success(Void result) {
                Info.show("Prompt saved", Info.Type.INFO);
                loadSavedPrompts();
            }
        });
    }

    // ── Reset & Save ─────────────────────────────────────────────────────────

    private void resetChanges() {
        if (selectedFile != null && originalUrl != null) {
            previewImage.setUrl(originalUrl);
        }
        rotationDeg = 0;
        flipped = false;
        zoomScale = 1.0;
        selectedPicture = null;
        applyTransform();
        versionsPanel.clear();
        generatedVersions.clear();
        generatedVersionsMap.clear();
        aspectRatioList.setSelectedIndex(0);
        promptArea.setText("");
    }

    private void saveResult() {
        if (selectedPicture == null) {
            if (rotationDeg == 0 && !flipped) {
                // only zoom / aspect-ratio changed — nothing to persist
                close();
                return;
            }
            // rotation applied but no generated version yet — rotate server-side first
            applySaveWithRotation();
            return;
        }
        doSave();
    }

    private void applySaveWithRotation() {
        if (selectedFile == null || selectedFile.getPicture() == null || selectedFile.getPicture().getPictureID() == null) {
            close();
            return;
        }
        String actionType;
        if (rotationDeg == 90) actionType = ACTION_ROTATE_RIGHT;
        else if (rotationDeg == 270) actionType = ACTION_ROTATE_LEFT;
        else if (rotationDeg == 180) actionType = ACTION_ROTATE_180;
        else { close(); return; }

        Integer pictureId = selectedFile.getPicture().getPictureID();
        setAiButtonsEnabled(false);
        LoadingPanel.loading(true);

        productService.processAiImage(pictureId, null, actionType, null, new AbstractAsyncCallback<AiImageResult>() {
            @Override
            public void success(AiImageResult result) {
                LoadingPanel.loading(false);
                setAiButtonsEnabled(true);
                List<ProductPicture> pictures = result != null ? result.getGeneratedPictures() : null;
                if (pictures != null && !pictures.isEmpty()) {
                    addGeneratedVersion(pictures.get(0));
                    selectedPicture = pictures.get(0);
                    doSave();
                } else {
                    close();
                }
            }

            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                setAiButtonsEnabled(true);
                close();
            }
        });
    }

    private void doSave() {
        ProductPicture original = selectedFile != null ? selectedFile.getPicture() : null;
        Integer originalPictureId = original != null ? original.getPictureID() : null;
        boolean originalIsDefault = original != null && Boolean.TRUE.equals(original.isDefaultPicture());
        Integer productId = original != null ? original.getProductID() : null;
        Integer aiPictureId = selectedPicture.getPictureID();

        LoadingPanel.loading(true);

        productService.saveAiGeneratedImage(aiPictureId, new AbstractAsyncCallback<ProductPicture>() {
            @Override
            public void success(ProductPicture saved) {
                if (saved == null) {
                    LoadingPanel.loading(false);
                    Info.show("Failed to save image", Info.Type.WARNING);
                    return;
                }
                if (originalIsDefault && productId != null) {
                    productService.setDefaultProductPicture(aiPictureId, productId, new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void success(Boolean result) {
                            deleteOriginalAndFinish(originalPictureId);
                        }

                        @Override
                        public void failure(Throwable caught) {
                            deleteOriginalAndFinish(originalPictureId);
                        }
                    });
                } else {
                    deleteOriginalAndFinish(originalPictureId);
                }
            }

            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show("Failed to save image: " + caught.getMessage(), Info.Type.WARNING);
            }
        });
    }

    private void deleteOriginalAndFinish(Integer originalPictureId) {
        if (originalPictureId == null) {
            finishSave();
            return;
        }
        productService.deleteProductPicture(originalPictureId, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void success(Boolean result) {
                finishSave();
            }

            @Override
            public void failure(Throwable caught) {
                finishSave();
            }
        });
    }

    private void finishSave() {
        LoadingPanel.loading(false);
        Info.show("AI-generated image saved successfully", Info.Type.INFO);
        if (onSaveCallback != null) onSaveCallback.run();
        close();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void setAiButtonsEnabled(boolean enabled) {
        enhanceBtn.setEnabled(enabled && aiAvailable);
        removeBgBtn.setEnabled(enabled && aiAvailable);
        generateBtn.setEnabled(enabled && aiAvailable);
        promptArea.setEnabled(enabled && aiAvailable);
    }

    private String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() > max ? text.substring(0, max) + "…" : text;
    }
}
