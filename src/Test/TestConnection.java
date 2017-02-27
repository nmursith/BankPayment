package Test;

import app.Model.Connection;

import java.util.HashMap;

/**
 * Created by nifras on 2/20/17.
 */
public class TestConnection {
    public static void main(String []args){
        try{
            HashMap<String, String> data = new HashMap<>();


            data.put("Text", "Your A/C ***0424 debited 150.00 LKR via POSLanka");
            data.put("phone", "+94719356519");


            Connection connection = Connection.getInstance();
             String results = connection.post("", data);
           // connection.sendPost();
            //timer.sc
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }
}
