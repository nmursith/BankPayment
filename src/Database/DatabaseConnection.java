package Database;

import app.Model.Card;

import java.sql.*;

/**
 * Created by nifras on 2/18/17.
 */
public class DatabaseConnection {
    private static PreparedStatement preparedStatement;

    private static Connection connection;
    public  void connect(){
        try {
        Class.forName("com.mysql.jdbc.Driver");
//      Class.forName("oracle.jdbc.driver.OracleDriver");
        System.out.println("Driver loaded");

            connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost/BankMUSD", "root", "");

        // Establish a connection

//    ("jdbc:oracle:thin:@liang.armstrong.edu:1521:orcl",
//     "scott", "tiger");
        System.out.println("Database connected");


    }
    catch (Exception ex) {
        ex.printStackTrace();
    }
    }

    public static boolean verifyDetatils(String cardNumber, Card card ){

        String queryString = "select *from creditcard " + "where card_number = ?";


        // Create a statement


        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, cardNumber);


            ResultSet rset = preparedStatement.executeQuery();

            if (rset.next()) {
                String first_name = rset.getString("first_name");
                String middle_name = rset.getString("middle_name");
                String last_name = rset.getString("last_name");
                int card_number= rset.getInt("card_number");
                Date date = rset.getDate("expiry_date");
                Double balance = rset.getDouble("balance");
                String card_type;
                short validation_pin = rset.getShort("validation_pin");


                System.out.println(balance);
                // Display result in a dialog box

            }

        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return  false;
    }

    public static void updateBalance(String cardNumber, double amount){



    }
    public static void main(String []args){
        new DatabaseConnection().connect();
    }

}
