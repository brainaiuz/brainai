package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.StepEmployeeManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCustomView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Azazello on 10/21/15.
 */
public class EmployeeStepViewPDFHandler extends AbstractITextPostPdfHandler {
    private CommonServiceLocal commonService;
    @Autowired
    private StepEmployeeManager stepEmployeeManager;

    public void setCommonService(CommonServiceLocal commonService) {
        this.commonService = commonService;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        ITextGenericPdfData pdf = new ITextGenericPdfData();
        ITextCustomView pdfData = new ITextCustomView();
        pdf.setPdfViewType(ITextPdfViewTypeEnum.CUSTOMVIEW);
        EdsUser user = stepEmployeeManager.getUser();

        String fontName = getDefaultFont(user.getCompany());
        SimpleDateFormat format = getCompanyShortDateFormat(user.getCompany());

        float[] width = {2.5f, 2.5f, 2.5f, 2.5f};

        PdfPTable caseContainer = new PdfPTable(width);
        caseContainer.setWidthPercentage(100);

        Font lableFont = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 12, Font.BOLD);
        lableFont.setColor(76, 126, 173);

        Font font = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 11);

        PdfPCell cellSpace = new PdfPCell();
        cellSpace.setColspan(4);
        cellSpace.setHorizontalAlignment(1);
        cellSpace.setPadding(10f);
        cellSpace.setBorder(Rectangle.NO_BORDER);

        PdfPCell innerHeaderCell = new PdfPCell();
        innerHeaderCell.setHorizontalAlignment(0);
        innerHeaderCell.setVerticalAlignment(1);
        innerHeaderCell.setPadding(4);
        innerHeaderCell.setPaddingBottom(8);
        innerHeaderCell.setBorderColor(new Color(213, 213, 213));

        PdfPCell innerCell = new PdfPCell();
        innerCell.setHorizontalAlignment(0);
        innerCell.setVerticalAlignment(1);
        innerCell.setPadding(4);
        innerCell.setPaddingBottom(8);
        innerCell.setBorderColor(new Color(213, 213, 213));

        PdfPCell cellLabel = new PdfPCell();
        cellLabel.setVerticalAlignment(1);
        cellLabel.setBorder(Rectangle.NO_BORDER);

        EdsStepEmployee stepEmployee = stepEmployeeManager.get(((RequestObject) dataClass).getObjectID());
        boolean isCandidate = stepEmployee.getType() != null && EdsStepEmployee.CANDIDATE_TYPE.equals(stepEmployee.getType().getCode()) && stepEmployee.getCandidate() != null;

        cellSpace.setPhrase(new Phrase(stepEmployee.getOnboardingStep().getName(), FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 12, Font.BOLD)));
        caseContainer.addCell(cellSpace);
        cellSpace.setPadding(4);
        cellSpace.setPhrase(new Phrase(""));

        //Employee
        PdfPCell cell = generateCell();
        cellLabel.setPhrase(new Phrase(commonLocalizer.localize(stepEmployee.getType() != null && EdsStepEmployee.CANDIDATE_TYPE.equals(stepEmployee.getType().getCode()) ? PdfLocalizationName.candidate : PdfLocalizationName.employee) + ":", lableFont));
        caseContainer.addCell(cellLabel);

        String employeeCode = stepEmployee.getEmployee() != null && stepEmployee.getEmployee().getProfile() != null
                              && stepEmployee.getEmployee().getProfile().getEmployeeCode() != null
                              ? stepEmployee.getEmployee().getProfile().getEmployeeCode() + " - "
                              : "";
        cell.setPhrase(new Phrase(isCandidate ? stepEmployee.getCandidate().getName() : (stepEmployee.getEmployee() != null ? (employeeCode + stepEmployee.getEmployee().getFullName()) : ""), font));
        caseContainer.addCell(cell);
        //Status
        cell = generateCell();
        cellLabel.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.status) + ":", lableFont));
        caseContainer.addCell(cellLabel);

        cell.setPhrase(new Phrase(stepEmployee.getStatus().getName(), font));
        caseContainer.addCell(cell);
        caseContainer.addCell(cellSpace);

        //custom Fields
        List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(stepEmployee.getEmployeeStepCustomFields(), commonService.getCompanyCustomFieldsByCategory(ViewName.OnboardingStep, stepEmployee.getOnboardingStep().getViewName()));
        if(customFieldItems != null && customFieldItems.size() > 0){
            int i = 0;
            for(CompanyCustomFieldItem item : customFieldItems){
                if(item != null){
                    cell = generateCell();
                    cellLabel.setPhrase(new Phrase(item.getFieldName() != null ? escapeHtml(item.getFieldName()) : "", lableFont));
                    caseContainer.addCell(cellLabel);
                    if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                        cell.setPhrase(new Phrase(item.getFieldDateNonConvertedValue() != null ? escapeHtml(format.format(ServerUtils.convertServerDateToUserDate(item.getFieldDateNonConvertedValue().getNonConvertedDate(), user.getUserTimezone()))) : "", font));
                    } else {
                        cell.setPhrase(new Phrase(item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : "", font));
                    }
                    caseContainer.addCell(cell);
                    i++;
                    if (i%2 == 0) {
                        caseContainer.addCell(cellSpace);
                    }
                }
            }
            if(i%2 != 0){
                cell = generateCell();
                cellLabel.setPhrase(new Phrase("", lableFont));
                caseContainer.addCell(cellLabel);
                cell.setPhrase(new Phrase("", font));
                caseContainer.addCell(cell);
            }
            caseContainer.addCell(cellSpace);
        }
        pdfData.setCustomTable(caseContainer);
        pdf.setCustomView(pdfData);
        return pdf;
    }

    private PdfPCell generateCell() {
        PdfPCell cell = new PdfPCell();
        cell.setPaddingBottom(4f);
        cell.setPaddingLeft(20f);
        cell.setVerticalAlignment(1);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        EdsStepEmployee stepEmployee = stepEmployeeManager.get(((RequestObject) dataClass).getObjectID());
        String stepName = stepEmployee.getOnboardingStep().getName();
        boolean isCandidate = stepEmployee.getType() != null && EdsStepEmployee.CANDIDATE_TYPE.equals(stepEmployee.getType().getCode()) && stepEmployee.getCandidate() != null;
        String name = isCandidate ? stepEmployee.getCandidate().getName() : (stepEmployee.getEmployee() != null ? stepEmployee.getEmployee().getFullName() : "");
        setFileName(stepName + "_" + name);
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }
}
