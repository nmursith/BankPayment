/**
 * Created by nifras on 1/6/17.
 */

import org.jpos.iso.*;
import org.jpos.util.*;
import org.jpos.iso.channel.*;
import org.jpos.iso.packager.*;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws Exception {
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener(System.out));
        ISOChannel channel = new ASCIIChannel(
                "localhost", 8000, new ISO87APackager()
        );
        ((LogSource) channel).setLogger(logger, "test-channel");
        /*    int ports[] = {21, 80, 443, 631, 3306, 5355, 6942, 7534, 38501, 63342};
         for(int i=0; i<ports.length; i++){
             //       System.out.println("PORT " + i);
            try{
                channel = new ASCIIChannel(
                        "localhost", ports[i], new ISO87APackager()
                );
                channel.connect();
                if(channel.isConnected())
                System.out.println("Connected PORT " + ports[i]);
                //channel.disconnect();

                ISOMsg m = new ISOMsg();
                m.setMTI("0800");
                m.set(3, "000000");
                m.set(41, "00000001");
                m.set(70, "301");
                channel.send(m);


                ISOMsg r = channel.receive();
                System.out.println(r +"    "+ i);
                channel.disconnect();
                //break;
            }
            catch (IOException e){
            e.printStackTrace();
            }
                    catch (ISOException e){
            e.printStackTrace();
                    }
        }*/

        channel.connect();
        ISOMsg m = new ISOMsg();
        m.setMTI("0800");
        m.set(3, "000000");
        m.set(41, "00000001");
        m.set(70, "301");
        ISOMUX mux = new ISOMUX(channel);

        //MUX mux = (MUX) NameRegistrar.get ("mux.mymultiplexer");
        ISOMsg response = mux.request (m, 30000);
        if (response != null) {
// you've got a response
            System.out.println(response);
        } else {

// request has timed out
// you may want to reverse or retransmit
        }
        channel.send(m);
        ISOMsg r = channel.receive();
        channel.disconnect();

    }
}