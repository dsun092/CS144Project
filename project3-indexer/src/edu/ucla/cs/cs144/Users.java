package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Users{

    public Users(){
    }
    
    public Users(String userID, String rating, String location, String country)
    {
        this.userID = userID;
        this.rating = rating;
        this.location = location;
        this.country = country;
    }
    
    public String getID(){
        return this.userID;
    }
    public String getRating(){
        return this.rating;
    }
    public String getLocation(){
        return this.location;
    }
    public String getCountry(){
        return this.country;
    }
    
    

    private String userID;
    private String rating;
    private String location;
    private String country;
}