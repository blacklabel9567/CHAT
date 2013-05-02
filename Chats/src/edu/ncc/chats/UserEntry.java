package edu.ncc.chats;

public class UserEntry {
	private long _id;
	private String name;
	private String number;
	
	public long getID(){
		return _id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getNumber(){
		return number;
	}
	
	public void setID(long id){
		this._id =  id;
	}
	
	public void setName(String nam){
		name = nam;
	}
	
	public void setNumber(String num){
		number = num;
	}
	
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return name + ": " + number;
	}
}
