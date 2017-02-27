package app.Model;

import java.sql.Date;

/**
 * Created by nifras on 2/27/17.
 */
public class Card {
    private String first_name ;
    private String middle_name ;
    private String last_name ;
    private int card_number;
    private Date date ;
    private Double balance ;
    private String card_type;
    short validation_pin ;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getCard_number() {
        return card_number;
    }

    public void setCard_number(int card_number) {
        this.card_number = card_number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public short getValidation_pin() {
        return validation_pin;
    }

    public void setValidation_pin(short validation_pin) {
        this.validation_pin = validation_pin;
    }
}
