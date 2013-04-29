package edu.ncc.chats;

public class MessageEntry {
		private long _id;
		private long c_Id;
		private String message;
		
		public long getID(){
			return _id;
		}
		
		public long getC_Id(){
			return c_Id;
		}
		
		public String getMessage(){
			return message;
		}
		
		public void setID(long id){
			this._id =  id;
		}
		
		public void setC_Id(long cid){
			c_Id = cid;
		}
		
		public void setMessage(String msg){
			message = msg;
		}
		
		
		// Will be used by the ArrayAdapter in the ListView
		@Override
		public String toString() {
			return  message;
		}
	}

