package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Items{

    public Items(){
    }
    
    public Items(String itemID, String name, String description)
    {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        //this.start = start;
        //this.end = end;
        //this.sellerID = sellerID;
        //this.buy_price = buy_price;
    }
    
    public String getID(){
        return this.itemID;
    }
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
   /*
    public String getStart(){
        return this.start;
    }
    public String getEnd(){
        return this.end;
    }
    public String getSeller()
    {
        return this.sellerID;
    }
    public String getBuy()
    {
        return this.buy_price;
    }*/
    //FIGURE OUT HOW TO GET BIDDERS AS WELL
    
    private String itemID;
    private String name;
    private String description;
    //private String start;
    //private String end;
    //private String sellerID;
    //private String buy_price;
}