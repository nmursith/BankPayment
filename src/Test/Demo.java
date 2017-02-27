package Test;

import app.Model.CustomPackager;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;

/**
 * Created by nifras on 1/24/17.
 */
public class Demo {
    public static void main(String[]args){
        try {
            ISOPackager packager = new GenericPackager("./src/Test/iso93ascii.xml");
        } catch (ISOException e) {
            e.printStackTrace();
        }

// For packager of type class
        ISOPackager packager1 = new CustomPackager();
    }
}
