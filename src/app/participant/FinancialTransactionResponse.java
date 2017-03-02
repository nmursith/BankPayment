package app.participant;

import app.Database.DatabaseConnection;
import app.Model.Connection;
import app.Model.Constant;
import org.bouncycastle.asn1.dvcs.Data;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.AbortParticipant;
import org.jpos.transaction.Context;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Created by nifras on 2/7/17.
 */
public class FinancialTransactionResponse implements AbortParticipant {

    @Override
    public void abort(long id, Serializable context) {
        try {
            ISOSource requester = (ISOSource) ((Context)context).get(Constant.ISOSOURCE);
            ISOMsg reqMsg = (ISOMsg) ((Context) context).get(Constant.REQUEST);
            ISOMsg respMsg = (ISOMsg) reqMsg.clone();
            respMsg.setResponseMTI();
            respMsg.set(39, "02");
            requester.send(respMsg);
        } catch (ISOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit(long id, Serializable context) {
        ISOMsg respMsg = (ISOMsg) ((Context) context).get(Constant.RESPONSE);

        try {
            String PAN = respMsg.getString(2);
            Double amount = Double.parseDouble(respMsg.getString(4));
            DatabaseConnection.updateBalance(PAN, amount);
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public int prepare(long id, Serializable context) {
        try {


            ISOMsg respMsg = (ISOMsg) ((Context) context).get(Constant.RESPONSE);
            ISOSource requester = (ISOSource) ((Context) context).get(Constant.ISOSOURCE);
            respMsg.setResponseMTI();
            Double amount = Double.parseDouble(respMsg.getString(4));


            respMsg.set(39, "00");

            requester.send(respMsg);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        HashMap<String, String> data = new HashMap<>();
                        data.put("Text", "Your A/C ***0424 debited "+ amount +" LKR via POSLanka");
                        data.put("phone", "+94719356519");


                        Connection connection = Connection.getInstance();
                        connection.post("", data);


                        //connection.sendPost();
                        //timer.sc
                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }
                }
            }).start();



            //Connection connection = Connection.getInstance();
            //results = connection.post("payment_process.php", data);

            return PREPARED;
        } catch (Exception e) {
            e.printStackTrace();
            return ABORTED;
        }
    }

    @Override
    public int prepareForAbort(long id, Serializable context) {
        return ABORTED;
    }

}