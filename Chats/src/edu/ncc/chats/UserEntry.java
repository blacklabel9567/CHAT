package edu.ncc.chats;

public class UserEntry {
	private long _id;
	private String name;
	private String message;
	
	public long getID(){
		return _id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setID(long id){
		this._id =  id;
	}
	
	public void setName(String nam){
		name = nam;
	}
	
	public void setMessage(String msg){
		message = msg;
	}
	
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return name + ": " + message;
	}
}
