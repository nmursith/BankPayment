package Demo2;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;

import java.io.IOException;

/**
 * Created by nifras on 1/25/17.
 */
public class ResponseListener implements ISORequestListener, Configurable {

    private Space<String, ISOMsg> sp;

    @Override
    public boolean process(ISOSource source, ISOMsg msg) {
        System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
        try {
            msg.setResponseMTI ();
            msg.set (39, "96546465");
            source.send(msg);
            sp.out(msg.getString(11), msg);
        } catch (ISOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setConfiguration(Configuration conf)
            throws ConfigurationException {
        sp = SpaceFactory.getSpace(conf.get("space"));
    }

}