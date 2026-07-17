package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPaymentInstruction;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.InstructionData;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/6/11
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PaymentInstructionManager extends Manager<EdsPaymentInstruction>{
    List<EdsPaymentInstruction> getInstructions(Integer type);

    List<EdsPaymentInstruction> getInstructionsByTypes(List<Integer> types);

    InstructionData[] getInstructionsRPC(Integer type);
}
