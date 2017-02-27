package Test;

import org.jpos.iso.*;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.channel.XMLChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.iso.packager.XMLPackager;
import org.jpos.q2.Q2;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.NameRegistrar;
import org.jpos.util.SimpleLogListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nifras on 1/24/17.
 */
public class Client {
    public static void main(String[] args){
        Q2 q = new Q2();
        q.start();
        ISOPackager packager = null;
        try {
//            packager = new GenericPackager("./src/Test/iso93ascii.xml");
//            ASCIIChannel channel = new ASCIIChannel("localhost", 8080, packager);

            Logger logger = new Logger ();
            logger.addListener (new SimpleLogListener(System.out));
/*
            ISOChannel channel = new ASCIIChannel(
                    "localhost", 10000, new ISO87APackager()
            );
*/

            ServerChannel channel = new XMLChannel (new XMLPackager());

            ISOMUX isoMux1 =  new  ISOMUX (channel)
            {
                @Override
                protected String getKey(ISOMsg m) throws ISOException {
                    return super.getKey(m);
                }
            };
            MUX isoMux = (MUX) NameRegistrar.get ("mux.jpos-client-mux");

// Jalankan ISOMUXnya
            //new Thread (isoMux).start ();
            NameRegistrar.register ("mux", isoMux);
            System.out.println(isoMux.isConnected());

            ISOMsg networkReq = new ISOMsg();
            networkReq.setMTI("1800");
            networkReq.set(3, "123456");
            networkReq.set(7, new SimpleDateFormat("yyyyMMdd").format(new Date()));
            networkReq.set(11, "000001");
            networkReq.set(12, new SimpleDateFormat("HHmmss").format(new Date()));
            networkReq.set(13, new SimpleDateFormat("MMdd").format(new Date()));
            networkReq.set(48, "Tutorial ISO 8583 Dengan Java");
            networkReq.set(70, "001");

            ISORequest req = new ISORequest(networkReq);
            //isoMux.queue(req);


            System.out.println("Sending");
           // ISOMsg reply = req.getResponse(50*1000);
            ISOMsg reply = isoMux.request(networkReq, 50*1000);
            if (reply != null) {
                System.out.println("Req ["+new String(networkReq.pack()) + "]");
                System.out.println("Res ["+new String(reply.pack()) + "]");
            }
            else {
                System.out.println("FUCK");
            }

        } catch (ISOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NameRegistrar.NotFoundException e) {
            e.printStackTrace();
        }


    }
}
