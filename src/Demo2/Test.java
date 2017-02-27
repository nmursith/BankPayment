package Demo2;

import javafx.application.Platform;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.q2.Q2;
import org.jpos.util.NameRegistrar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nifras on 1/25/17.
 */
public class Test {
    public static void main(String []args){
        Q2 scanner = new Q2();
        scanner.start();

        ISOUtil.sleep(1000);
        JposClient client = (JposClient) NameRegistrar.getIfExists("requester");
        System.out.println(client);
        client.sendRequest(createReqMsg(), "Testing" , "mux.jpos-client-mux");
        ISOMsg response = client.getResponse("Testing"  );
        System.out.println(response);
    }
    static private ISOMsg createReqMsg() {
        ISOMsg networkReq = new ISOMsg();
        try {

            networkReq.setMTI("1800");
            networkReq.set(3, "123456");
            networkReq.set(7, new SimpleDateFormat("yyyyMMdd").format(new Date()));
            networkReq.set(11, "000001");
            networkReq.set(12, new SimpleDateFormat("HHmmss").format(new Date()));
            networkReq.set(13, new SimpleDateFormat("MMdd").format(new Date()));
            networkReq.set(48, "Tutorial ISO 8583  Java");
            networkReq.set(70, "001");
        } catch (ISOException e ) {
            e.printStackTrace();
        }
        return networkReq ;
    }
}
