package Demo2;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOResponseListener;
import org.jpos.q2.QBeanSupport;
import org.jpos.space.Space;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOResponseListener;
import org.jpos.iso.MUX;
import org.jpos.q2.QBeanSupport;
import org.jpos.q2.iso.QMUX;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.util.NameRegistrar;
import org.jpos.util.NameRegistrar.NotFoundException;
/**
 * Created by nifras on 1/25/17.
 */



    public class JposClient extends QBeanSupport implements ISOResponseListener {

        private Long timeout ;
        private Space<String, ISOMsg> responseMap ;

        @SuppressWarnings("unchecked" )
        @Override
        protected void initService() throws Exception {
            this.timeout = cfg .getLong("timeout");
            this.responseMap = SpaceFactory.getSpace (cfg .get("spaceName" ));
            NameRegistrar. register(getName(), this);
        }

        /**
         * send request asynchronous way
         * @param handback
         * @param muxName
         * @return
         */
        public void sendRequest(ISOMsg reqMsg, String handback, String muxName) {
            try {
                MUX mux = QMUX. getMUX(muxName);
                mux.request( reqMsg, timeout, this, handback );
            } catch (NotFoundException e ) {
                e. printStackTrace();
            } catch (ISOException e ) {
                e.printStackTrace();
            }
        }

        /**
         * getting response
         * @param handback
         * @return
         */
        public ISOMsg getResponse(String handback ){
            return responseMap.in(handback , timeout );
        }

        @Override
        public void responseReceived(ISOMsg resp, Object handBack) {
            responseMap.out(String.valueOf( handBack), resp, timeout);
        }

        @Override
        public void expired(Object handBack) {
            System. out.println("Request " +handBack +" is timeout" );
        }
    }

