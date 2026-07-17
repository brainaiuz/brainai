package com.edatasite.workforce.gwt.payroll.client.ui.view.additionalpayment;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.view.EmployeeDataDetail;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentCalculationDetail;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.util.ArrayList;

public class PaymentCalculationSideNavBox extends KpiSideNavBox {

    public PaymentCalculationSideNavBox(ArrayList<PaymentCalculationDetail> calculationDetails, EmployeeDataDetail employeeDataDetail) {
        super(500);
        initialize(calculationDetails, employeeDataDetail);
    }


    private void initialize(ArrayList<PaymentCalculationDetail> calculationDetails, EmployeeDataDetail employeeDataDetail) {
        Heading header = new Heading(HeadingSize.H4);
        header.setText(wfmStrings.calculationDetails());
        addHeader(header);

        FlowPanel panel = new FlowPanel();
        if (calculationDetails != null) {
            for (PaymentCalculationDetail paymentCalculationDetail : calculationDetails) {
                String name = "";
                if (!Utils.isNullOrEmpty(paymentCalculationDetail.getName()) && paymentCalculationDetail.getName().contains("->")) {
                    String nameSecondElement = paymentCalculationDetail.getName().split("->")[1].trim();
                    if (!Utils.isNullOrEmpty(nameSecondElement)) {
                        String[] nameDetail = nameSecondElement.split(" ");
                        if (nameDetail.length == 6) {
                            for (int i = 0; i < nameDetail.length; i++) {
                                name = nameDetail[0] + " " + nameDetail[3] + " " + nameDetail[5];
                            }
                        } else {
                            name = nameSecondElement;
                        }
                    }
                } else {
                    name = paymentCalculationDetail.getName();
                }
                FormGroup nameFormGroup = new FormGroup("<b>" + wfmStrings.name() + "</b>", new HTML("<b>" + name + "</b>"));
                panel.add(nameFormGroup);

                FormGroup formulaFormGroup = new FormGroup(wfmStrings.formula(), new HTML(paymentCalculationDetail.getFormula()));
                panel.add(formulaFormGroup);

                FormGroup calFormGroup = new FormGroup(wfmStrings.calculate(), new HTML(paymentCalculationDetail.getCalculation() + "=" + PayrollClientUtils.format(paymentCalculationDetail.getAmount())));
                panel.add(calFormGroup);
            }
        }

        if (employeeDataDetail != null) {
            FormGroup positionFormGroup = new FormGroup(wfmStrings.position(), new HTML("<b>" + employeeDataDetail.getPostion() + "</b>"));
            positionFormGroup.setStyle("border-top: 5px solid #0da88c; padding-top: 10px;");
            panel.add(positionFormGroup);

            FormGroup departmentFormGroup = new FormGroup(wfmStrings.department(), new HTML("<b>" + employeeDataDetail.getDepartment() + "</b>"));
            panel.add(departmentFormGroup);

            FormGroup locationFormGroup = new FormGroup(wfmStrings.location(), new HTML("<b>" + employeeDataDetail.getLocation() + "</b>"));
            panel.add(locationFormGroup);

            FormGroup employmentModeFormGroup = new FormGroup(wfmStrings.employmentMode(), new HTML("<b>" + employeeDataDetail.getEmploymentMode() + "</b>"));
            panel.add(employmentModeFormGroup);

            NumberFormat extendedNumberFormat = NumberFormat.getFormat(",##0.00");
            FormGroup basicSalaryGroup = new FormGroup(wfmStrings.basicSalary(), new HTML("<b>" + extendedNumberFormat.format(employeeDataDetail.getBasicSalary()) + "</b>"));
            panel.add(basicSalaryGroup);

            FormGroup allowanceFormGroup = new FormGroup(wfmStrings.allowance(), new HTML("<b>" + employeeDataDetail.getAllowance() + "</b>"));
            panel.add(allowanceFormGroup);
        }

        addBody(panel);
    }
}
