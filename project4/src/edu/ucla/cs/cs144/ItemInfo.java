package edu.ucla.cs.cs144;

public class ItemInfo {
	private String itemId;
	private String name;
    
	public ItemInfo() {}
    
	public ItemInfo(String xml) {
		this.itemId = itemId;
		this.name = name;
	}
    
	public String getItemId() {
		return itemId;
	}
	
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}