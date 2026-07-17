package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPaymentInstruction;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.InstructionData;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.PaymentInstructionManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/6/11
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("paymentInstructionManager")
public class PaymentInstructionManagerImpl extends BaseManager<EdsPaymentInstruction> implements PaymentInstructionManager{
    public PaymentInstructionManagerImpl() {
        super(EdsPaymentInstruction.class);
    }

    @Override
    public List<EdsPaymentInstruction> getInstructions(Integer type) {
        return find("select pi from EdsPaymentInstruction pi where pi.type = ? and pi.deleted<>true order by pi.objectID", type);
    }

    @Override
    public List<EdsPaymentInstruction> getInstructionsByTypes(List<Integer> types) {
        return find("select pi from EdsPaymentInstruction pi where pi.type in (" + ServerUtils.getAsCommoDelimited(types, "0") + ") and pi.deleted<>true order by pi.objectID");
    }

    @Override
    public InstructionData[] getInstructionsRPC(Integer type) {
        List<EdsPaymentInstruction> instructions = getInstructions(type);
        InstructionData[] dataArray = new InstructionData[instructions.size()];
        int i=0;
        for(EdsPaymentInstruction inst : instructions){
            InstructionData data = new InstructionData();
            data.setObjectID(inst.getObjectID());
            data.setText(inst.getText());
            dataArray[i++] = data;
        }
        return dataArray;
    }
}
