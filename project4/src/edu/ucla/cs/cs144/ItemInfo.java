package edu.ucla.cs.cs144;

public class ItemInfo {
	private String UserID;
	private String Rating;
    private String Time;
    private String Amount;
    private String Location;
    private String Country;
    
	public ItemInfo() {

	}
    
	public String getUserID() {
		return UserID;
	}
	
	public String getID() {
		return UserID;
	}
    
    public String getRating(){
        return Rating;
    }
    public String getTime(){
        return Time;
    }
    public String getAmount(){
        return Amount;
    }
    public String getLocation(){
        return Location;
    }
    public String getCountry(){
        return Country;
    }
	
	public void setID(String UserID) {
		this.UserID = UserID;
	}
    public void setRating(String Rating) {
		this.Rating = Rating;
	}
    public void setTime(String time){
        this.Time = time;
    }
    public void setAmount(String amount){
        this.Amount = amount;
    }
    public void setLocation(String location){
        this.Location = location;
    }
    public void setCountry(String country){
        this.Country = country;
    }
}