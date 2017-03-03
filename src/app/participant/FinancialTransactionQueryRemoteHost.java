package app.participant;

import app.Database.DatabaseConnection;
import app.Model.ChannelManager;
import app.Model.Constant;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.AbortParticipant;
import org.jpos.transaction.Context;
import org.jpos.util.NameRegistrar;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;

/**
 * Created by nifras on 2/7/17.
 */
public class FinancialTransactionQueryRemoteHost implements AbortParticipant {

    private ChannelManager channelManager;

    @Override
    public void abort(long id, Serializable context) {
        try {
            ISOSource requester = (ISOSource) ((Context)context).get(Constant.ISOSOURCE);
            ISOMsg reqMsg = (ISOMsg) ((Context) context).get(Constant.REQUEST);
            ISOMsg respMsg = (ISOMsg) reqMsg.clone();
            respMsg.setResponseMTI();
            respMsg.set(39, "03");
            //requester.send(respMsg);
        } catch (ISOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void commit(long id, Serializable context) { }

    @Override
    public int prepare(long id, Serializable context) {
        try {
            channelManager = ((ChannelManager) NameRegistrar.get("manager"));
            ISOMsg reqMsg = (ISOMsg) ((Context)context).get(Constant.REQUEST);
            ISOMsg respMsg = (ISOMsg) reqMsg.clone();;//channelManager.sendMsg(reqMsg);
            Double amount = Double.parseDouble(respMsg.getString(4));
            ((Context)context).put(Constant.RESPONSE, respMsg);
            ResultSet resultSet = DatabaseConnection.verifyDetatils(respMsg.getString(2));
            if( amount > resultSet.getDouble("balance")){
                return ABORTED; 
            }

            return PREPARED;
        } catch (NameRegistrar.NotFoundException e) {
            e.printStackTrace();
            return ABORTED;
        } catch (Exception e){
            e.printStackTrace();

        } catch (Throwable t) {
            t.printStackTrace();

        }
        return ABORTED;
    }

    @Override
    public int prepareForAbort(long id, Serializable context) {
        return ABORTED;
    }

}
