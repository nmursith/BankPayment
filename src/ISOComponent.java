import org.jpos.iso.ISOException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Hashtable;

/**
 * Created by nifras on 1/6/17.
 */

public abstract class ISOComponent implements Cloneable {
/*    public void set (ISOComponent c) throws ISOException;
    public void unset (int fldno) throws ISOException;
    public ISOComponent getComposite();
    public Object getKey() throws ISOException;
    public Object getValue() throws ISOException;
    public byte[] getBytes() throws ISOException;
    public int getMaxField();
    public Hashtable getChildren();*/
    public abstract void setFieldNumber (int fieldNumber);
    public abstract void setValue(Object obj) throws ISOException;
    public abstract byte[] pack() throws ISOException;
    public abstract int unpack(byte[] b) throws ISOException;
    public abstract void dump (PrintStream p, String indent);
    public abstract void pack (OutputStream out) throws IOException, ISOException;
    public abstract void unpack (InputStream in) throws IOException, ISOException;
}