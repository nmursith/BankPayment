package Test;

/**
 * Created by nifras on 1/24/17.
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Martinus Ady H <mrt.itnewbies@gmail.com>
 */
public class ServerSocket {

    private static final Integer PORT = 12345;
    private static final Map<String, Integer> mappingDENetworkMsg = new HashMap<String, Integer>();
   /* This method is used to initialize the data element and the length of each
    * Data -tiap active element */
    private static void initMappingDENetworkRequest() {
        /* [data-element] [panjang data element] */
        mappingDENetworkMsg.put("3", 6);
        mappingDENetworkMsg.put("7", 8);
        mappingDENetworkMsg.put("11", 6);
        mappingDENetworkMsg.put("12", 6);
        mappingDENetworkMsg.put("13", 4);
        mappingDENetworkMsg.put("39", 3);
        mappingDENetworkMsg.put("48", 999);
        mappingDENetworkMsg.put("70", 3);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        initMappingDENetworkRequest();
        java.net.ServerSocket serverSocket = new java.net.ServerSocket(PORT);
        System.out.println ( " Server ready to accept connections on port [ " + PORT + " ] " );
        Socket socket = serverSocket.accept();
        InputStreamReader inStreamReader = new InputStreamReader(socket.getInputStream());
        PrintWriter sendMsg = new PrintWriter(socket.getOutputStream());

        int data;
        StringBuffer sb = new StringBuffer();
        int counter = 0;

        // Additional 4 characters because msg msg header is a 4-digit length
        int lengthOfMsg = 4;
        while((data = inStreamReader.read()) != 0) {
            System.out.print((char)data);
/*            counter++;
            sb.append((char) data);
            if (counter == 4) lengthOfMsg += Integer.valueOf(sb.toString());

            // Quote msg length of MTI to END OF MSG equal to the value
            // Header then proceed to processingMsg method ();
            if (lengthOfMsg == sb.toString().length()) {
                System.out.println("Rec. Msg ["+sb.toString()+"] len ["+sb.toString().length()+"]");
                processingMsg(sb.toString(), sendMsg);
            }*/
        }
    }

    /* * Processing msg sent by the client based on the value of MTI.
            * @param data request msg yang berisi [header 4byte][MTI][BITMAP][DATA ELEMENT]
            * @param sendMsg object printWriter untuk menuliskan msg ke network stream
    */
    private static void processingMsg(String data, PrintWriter sendMsg) {
        // Msg.asli without 4 digit msg.header
        String origMsgWithoutMsgHeader = data.substring(4, data.length());

        // Check the value of MTI
        if (ISOUtil.findMTI(origMsgWithoutMsgHeader).equalsIgnoreCase("1800")) {
            handleNetworkMsg(origMsgWithoutMsgHeader, sendMsg);
        }

    }

    /* * This method will process the request and network management will add
    * 1 data element yaitu data element 39 (response code) 000 ke client/sender
    * @param networkMsg request msg yang berisi [header 4byte][MTI][BITMAP][DATA ELEMENT]
            * @param sendMsg object printWriter untuk menuliskan msg ke network stream
    */
    private static void handleNetworkMsg(String networkMsg, PrintWriter sendMsg) {
        int panjangBitmap = ISOUtil.findLengthOfBitmap(networkMsg);
        String hexaBitmap = networkMsg.substring(4, 4+panjangBitmap);

        // Calculate the bitmap
        String binaryBitmap = ISOUtil.findBinaryBitmapFromHexa(hexaBitmap);
        String[] activeDE = ISOUtil.findActiveDE(binaryBitmap).split(";");

        StringBuilder networkResp = new StringBuilder();

        // setting MTI untuk reply network request
        networkResp.append("1810");

        // To reply, DE active is DE [3,7,11,12,13,39,48 and 70]
        String bitmapReply = ISOUtil.getHexaBitmapFromActiveDE(new int[] {3,7,11,12,13,39,48, 70});
        networkResp.append(bitmapReply);

        // Index starting dr msg (MTI + 4 digits long bitmap index = DE to 3)
        int startIndexMsg = 4+ISOUtil.findLengthOfBitmap(networkMsg);
        int nextIndex = startIndexMsg;
        String sisaDefaultDE = "";

        // Grab DE same values first
        for (int i=0;i<activeDE.length;i++) {
            // Grab bits to 3
            if (activeDE[i].equalsIgnoreCase("3")) {
                nextIndex += mappingDENetworkMsg.get(activeDE[i]);
                networkResp.append(networkMsg.substring(startIndexMsg, nextIndex));
                debugMessage(3, networkMsg.substring(startIndexMsg, nextIndex));
            } else if(activeDE[i].equalsIgnoreCase("7")) {
                startIndexMsg = nextIndex;
                nextIndex += mappingDENetworkMsg.get(activeDE[i]);
                networkResp.append(networkMsg.substring(startIndexMsg, nextIndex));
                debugMessage(7, networkMsg.substring(startIndexMsg, nextIndex));
            } else if(activeDE[i].equalsIgnoreCase("11")) {
                startIndexMsg = nextIndex;
                nextIndex += mappingDENetworkMsg.get(activeDE[i]);
                networkResp.append(networkMsg.substring(startIndexMsg, nextIndex));
                debugMessage(11, networkMsg.substring(startIndexMsg, nextIndex));
            } else if(activeDE[i].equalsIgnoreCase("12")) {
                startIndexMsg = nextIndex;
                nextIndex += mappingDENetworkMsg.get(activeDE[i]);
                networkResp.append(networkMsg.substring(startIndexMsg, nextIndex));
                debugMessage(12, networkMsg.substring(startIndexMsg, nextIndex));
            } else if(activeDE[i].equalsIgnoreCase("13")) {
                startIndexMsg = nextIndex;
                nextIndex += mappingDENetworkMsg.get(activeDE[i]);
                networkResp.append(networkMsg.substring(startIndexMsg, nextIndex));
                debugMessage(13, networkMsg.substring(startIndexMsg, nextIndex));
            } else if(activeDE[i].equalsIgnoreCase("48")) {
                startIndexMsg = nextIndex;
                // Take the first var.len for DE 48
                int varLen = Integer.valueOf(networkMsg.substring(startIndexMsg, (startIndexMsg+3)));
                // 3 digit UTK only variabel
                //assets +=  3 ;
                nextIndex += varLen;
                sisaDefaultDE += networkMsg.substring(startIndexMsg, nextIndex);
                debugMessage(48, networkMsg.substring(startIndexMsg, nextIndex));
            } else if(activeDE[i].equalsIgnoreCase("70")) {
                startIndexMsg = nextIndex;
                nextIndex += mappingDENetworkMsg.get(activeDE[i]);
                sisaDefaultDE += networkMsg.substring(startIndexMsg, nextIndex);
                debugMessage(70, networkMsg.substring(startIndexMsg, nextIndex));
            }
        }

        // Love response code 39 success
        networkResp.append("000");
        // Add the rest of the default DE
        networkResp.append(sisaDefaultDE);

        // tambahkan length 4 digit utk msg.header
        String msgHeader = "";
        if (networkResp.length() < 10) msgHeader = "000" + networkResp.length();
        if (networkResp.length() < 100 && networkResp.length() >= 10) msgHeader = "00" + networkResp.length();
        if (networkResp.length() < 1000 && networkResp.length() >= 100) msgHeader = "0" + networkResp.length();
        if (networkResp.length() >= 1000) msgHeader = String.valueOf(networkResp.length());

        String finalMsg = msgHeader + networkResp.toString();

        // send to client
        sendMsg.print(finalMsg);
        sendMsg.flush();
    }

    private static void debugMessage(Integer fieldNo, String msg) {
        System.out.println("["+fieldNo+"] ["+msg+"]");
    }
}
