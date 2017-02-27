package app.participant;

import app.Model.Constant;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by nifras on 2/7/17.
 */
public class FinancialTransactionValidateMessage implements TransactionParticipant {

    @Override
    public void abort(long id, Serializable context) {
        try {
            ISOSource requester = (ISOSource) ((Context)context).get(Constant.ISOSOURCE);
            ISOMsg reqMsg = (ISOMsg) ((Context) context).get(Constant.REQUEST);
            ISOMsg respMsg = (ISOMsg) reqMsg.clone();
            respMsg.setResponseMTI();
            respMsg.set(39, "01");
            requester.send(respMsg);
        } catch (ISOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit(long id, Serializable context) { }

    @Override
    public int prepare(long id, Serializable context) {
        try {
            ISOMsg reqMsg = (ISOMsg) ((Context) context).get(Constant.REQUEST);
//            String bit124 = reqMsg.getString(124);
//            String custId = bit124.substring(0, 5).trim();
            if(4>0){// if (custId != null && custId.length() > 0) {
                return PREPARED;
            }
        } catch (Exception e) {
            return ABORTED;
        }
        return ABORTED;
    }

}
