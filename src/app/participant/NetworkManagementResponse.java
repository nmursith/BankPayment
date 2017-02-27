package app.participant;

import app.Model.Constant;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

import java.io.Serializable;

/**
 * Created by nifras on 2/7/17.
 */
public class NetworkManagementResponse implements TransactionParticipant {
    @Override
    public int prepare(long l, Serializable context) {
         try {

            ISOSource requester = (ISOSource) ((Context)context).get(Constant.ISOSOURCE);
            ISOMsg reqMsg = (ISOMsg) ((Context) context).get(Constant.REQUEST);
            ISOMsg respMsg = (ISOMsg) reqMsg.clone();
            respMsg.setResponseMTI();
            requester.send(respMsg);

            String bit124 = reqMsg.getString(124);
            String custId = bit124.substring(0, 5).trim();
           if(4>0){// if (custId != null && custId.length() > 0) {
                return PREPARED;
            }
        } catch (Exception e) {
            return ABORTED;
        }
        return ABORTED;

    }

    @Override
    public void commit(long l, Serializable serializable) {

    }

    @Override
    public void abort(long l, Serializable serializable) {

    }
}
