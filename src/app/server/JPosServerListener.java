package app.server;

import app.Database.DatabaseConnection;
import app.Model.Constant;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.q2.Q2;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;


/**
 * Created by nifras on 2/7/17.
 */
public class JPosServerListener implements ISORequestListener, Configurable {

    private String queueName;
    private Space<String, Context> sp;
    private Long timeout;

    public JPosServerListener() {
        super();
    }

    public static void main(String[] args) {
        DatabaseConnection.connect();
        Q2 q2 = new Q2();
        q2.start();

    }

    @Override
    public boolean process(ISOSource isoSrc, ISOMsg isoMsg) {
        Context ctx = new Context();
        ctx.put(Constant.REQUEST, isoMsg);
        ctx.put(Constant.ISOSOURCE, isoSrc);
        sp.out(queueName, ctx, timeout);
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setConfiguration(Configuration cfg)
            throws ConfigurationException {
        queueName = cfg.get("queue");
        timeout = cfg.getLong("timeout");
        sp =  SpaceFactory.getSpace(cfg.get("space"));
    }

}
