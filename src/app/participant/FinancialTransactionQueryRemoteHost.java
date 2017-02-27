package app.participant;

import app.Model.ChannelManager;
import app.Model.Constant;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.AbortParticipant;
import org.jpos.transaction.Context;
import org.jpos.util.NameRegistrar;

import java.io.Serializable;

/**
 * Created by nifras on 2/7/17.
 */
public class FinancialTransactionQueryRemoteHost implements AbortParticipant {

    private ChannelManager channelManager;

    @Override
    public void abort(long id, Serializable context) { }

    @Override
    public void commit(long id, Serializable context) { }

    @Override
    public int prepare(long id, Serializable context) {
        try {
            channelManager = ((ChannelManager) NameRegistrar.get("manager"));
            ISOMsg reqMsg = (ISOMsg) ((Context)context).get(Constant.REQUEST);
            ISOMsg respMsg = (ISOMsg) reqMsg.clone();;//channelManager.sendMsg(reqMsg);
            ((Context)context).put(Constant.RESPONSE, respMsg);
            return PREPARED;
        } catch (NameRegistrar.NotFoundException e) {
            e.printStackTrace();
            return ABORTED;
        } catch (Throwable t) {
            t.printStackTrace();
            return ABORTED;
        }
    }

    @Override
    public int prepareForAbort(long id, Serializable context) {
        return ABORTED;
    }

}
