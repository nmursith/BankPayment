package Test;

/**
 * Created by nifras on 1/24/17.
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Martinus Ady H <mrt.itnewbies@gmail.com>
 */

public class ClientISO {

    private final static Integer PORT_SERVER = 12345;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket clientSocket = new Socket("localhost", PORT_SERVER);
        String networkRequest = buildNetworkReqMessage();

        PrintWriter outgoing = new PrintWriter(clientSocket.getOutputStream());
        InputStreamReader incoming = new InputStreamReader(clientSocket.getInputStream());

        outgoing.print(networkRequest);
        outgoing.flush();

        int data;
        StringBuffer sb = new StringBuffer();
        int counter = 0;
        // Additional 4 characters because msg msg header is a 4-digit length
        int lengthOfMsg = 4;
        while((data = incoming.read()) != 0) {
            counter++;
            sb.append((char) data);
            if (counter == 4) lengthOfMsg += Integer.valueOf(sb.toString());

            // Quote msg length of MTI to END OF MSG equal to the value
            // Header then proceed to processingMsg method ();
            if (lengthOfMsg == sb.toString().length()) {
                System.out.println("Rec. Msg ["+sb.toString()+"] len ["+sb.toString().length()+"]");
            }
        }

        outgoing.close();
        incoming.close();
        clientSocket.close();
    }

    private static String buildNetworkReqMessage() {
        StringBuilder networkReq = new StringBuilder();

        // MTI 1800
        networkReq.append("1800");
        // To request, DE active is DE [3,7,11,12,13,48 and 70]
        String bitmapReq = ISOUtil.getHexaBitmapFromActiveDE(new int[] {3,7,11,12,13,48,70});
        networkReq.append(bitmapReq);
        // DE 3 processing code
        networkReq.append("000001");
        // DE 7 transmission date and time
        networkReq.append(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        // DE 11 system trace audit number
        networkReq.append("000001");
        // DE 12 local time transaction
        networkReq.append(new SimpleDateFormat("HHmmss").format(new Date()));
        // DE 13 local time transaction
        networkReq.append(new SimpleDateFormat("MMdd").format(new Date()));
        // DE 48 Additional Private Data
        final String clientID = "CLNT001";
        // length de 48
        String lengthBit48 = "";
        if (clientID.length() < 10) lengthBit48 = "00" + clientID.length();
        if (clientID.length() < 100 && clientID.length() >= 10) lengthBit48 = "0" + clientID.length();
        if (clientID.length() == 100) lengthBit48 = String.valueOf(clientID.length());
        networkReq.append(lengthBit48);
        networkReq.append(clientID);

        // DE 70 Network Information Code
        networkReq.append("001");

        // Add 4 digit length of msg as headers
        String msgHeader = "";
        if (networkReq.toString().length() < 10) msgHeader = "000" + networkReq.toString().length();
        if (networkReq.toString().length() < 100 && networkReq.toString().length() >= 10) msgHeader = "00" + networkReq.toString().length();
        if (networkReq.toString().length() < 1000 && networkReq.toString().length() >= 100) msgHeader = "0" + networkReq.toString().length();
        if (networkReq.toString().length() >= 1000) msgHeader = String.valueOf(networkReq.toString().length());

        StringBuilder finalNetworkReqMsg =  new  StringBuilder ();
        finalNetworkReqMsg.append(msgHeader);
        finalNetworkReqMsg.append(networkReq.toString());

        return finalNetworkReqMsg.toString();
    }
}