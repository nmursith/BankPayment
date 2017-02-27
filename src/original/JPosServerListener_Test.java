package original; /**
 * Created by nifras on 1/14/17.
 */

import        org.jpos.iso.*;
import        org.jpos.util.*;
import        org.jpos.iso.channel.*;
import        org.jpos.iso.packager.*;

import java.io.IOException;

public class JPosServerListener_Test implements ISORequestListener {


    public JPosServerListener_Test() {
        super();
    }
    public boolean process (ISOSource source, ISOMsg m) {
        try {
            m.setResponseMTI ();
            m.set (39, "95");
            source.send (m);
        } catch (ISOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static void main (String[] args) throws Exception {
        Logger logger = new Logger ();
        logger.addListener (new SimpleLogListener (System.out));
        ServerChannel channel = new XMLChannel (new XMLPackager());
        ((LogSource)channel).setLogger (logger, "channel");
        ISOServer server = new ISOServer (8000, channel, null);


        server.setLogger (logger, "server");
        server.addISORequestListener (new JPosServerListener_Test());
        new SystemMonitor (60*60*1000, logger, "system-monitor");
        new Thread (server).start ();
    }
}