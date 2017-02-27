package app.Model;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.GroupSelector;

import java.io.Serializable;

/**
 * Created by nifras on 2/7/17.
 */
public class Switch implements GroupSelector, Configurable {
    private Configuration cfg;

    @Override
    public void abort(long id, Serializable context) { }

    @Override
    public void commit(long id, Serializable context) { }

    @Override
    public int prepare(long id, Serializable context) {
        return PREPARED | READONLY | NO_JOIN;
    }

    @Override
    public String select(long id, Serializable context) {
        try {
            ISOMsg reqMsg = (ISOMsg) ((Context)context).get(Constant.REQUEST);
            return cfg.get(reqMsg.getMTI(), null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setConfiguration(Configuration cfg)
            throws ConfigurationException {
        this.cfg = cfg;
    }

}
