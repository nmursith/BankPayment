package Test;

import org.jpos.iso.*;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.channel.XMLChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.iso.packager.XMLPackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;

import java.io.IOException;

/**
 * Created by nifras on 1/24/17.
 */
public class Server implements ISORequestListener{
    public Server () {
        super();
    }
    public boolean process (ISOSource source, ISOMsg m) {
        System.out.println(m);
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

    public static void main(String[] args){
        ISOPackager packager = null;
        try {
        //    packager = new GenericPackager("./src/Test/iso93ascii.xml");
         //   ASCIIChannel channel = new ASCIIChannel("localhost", 8080, packager);

            Logger logger = new Logger ();
            logger.addListener (new SimpleLogListener(System.out));
            ServerChannel channel = new XMLChannel (new XMLPackager());
            ((LogSource)channel).setLogger (logger, "channel");
            ISOServer server = new ISOServer(10000, channel, null);
            server.addISORequestListener (new Server ());
            new Thread(server).start();
        } catch (ISOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
