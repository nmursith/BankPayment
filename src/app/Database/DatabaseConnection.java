package app.Database;

import app.Model.Card;

import java.sql.*;

/**
 * Created by nifras on 2/18/17.
 */
public class DatabaseConnection {
    private static PreparedStatement preparedStatement;

    private static Connection connection;
    public static void connect(){
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

    public static ResultSet verifyDetatils(String cardNumber ){

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
                String card_number= rset.getString("card_number");
                Date date = rset.getDate("expiry_date");
                Double balance = rset.getDouble("balance");
                String card_type;
                short validation_pin = rset.getShort("validation_pin");
                System.out.println(balance);
                // Display result in a dialog box

            }
            return rset;

        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return  null;
    }

    public static void updateBalance(String cardNumber, double amount){
        try {
            double balance= verifyDetatils(cardNumber).getDouble("balance");
            balance = balance - amount;
            String queryString = "UPDATE creditcard  SET " + "balance = ? WHERE card_number=?";
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setDouble(1, balance);
            preparedStatement.setString(2, cardNumber);

           preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }




    }
    public static void main(String []args){
        //DatabaseConnection.connect();
    }

}
