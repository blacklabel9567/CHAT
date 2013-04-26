package edu.ncc.chats;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChatsMain extends ListActivity {

	List<String> messageList = new ArrayList<String>();
	List<String> numberList = new ArrayList<String>();
	String message = new String();
	String number = new String();
	List<String> tempList = new ArrayList<String>();
	
	private static String[] projection = { MessageDBHelper._ID, MessageDBHelper.NAME, MessageDBHelper.MESSAGE,};
	private static String orderBy = MessageDBHelper.NAME;

	private MessageDataSource datasource;
	ArrayAdapter<MessageEntry> adapter;
	List<MessageEntry> values;
	
//Intent filter to listen for incoming msgs	
IntentFilter intentFilter;
	


//receiver to handle incoming msgs
	private BroadcastReceiver intentReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			//display the mesg in the textview
			//TextView inText = (TextView)findViewById(R.id.listView1);
			//inText.setText(arg1.getExtras().getString("sms"));
			number = (String) arg1.getExtras().getString("sms");
			number = (String) number.substring(13, 23);
			message = (String) arg1.getExtras().getString("sms");
			message = (String) message.substring(25);
			
			displayListView(number,message);
			
			
//			messageList.add(message);
//			if(!numberList.contains((String)number))
//				numberList.add(number);
//			
//			displayListView();
		}
	};
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_main);
        
        datasource = new MessageDataSource(this);
		datasource.open();
		
		values = datasource.getAllMessages();
	
		adapter = new ArrayAdapter<MessageEntry>(this,android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);

		
		
		intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
             
        //Generate list View from ArrayList
        //displayListView();
        
    }
    
    
  
    public void newMessage(View view){
    	Bundle b = new Bundle();
    	Intent intent = new Intent(this, NewMessageActivity.class);
    	intent.putExtras(b); // put the bundle into the intent
    	intent.putStringArrayListExtra("numList", (ArrayList<String>)numberList);
		startActivityForResult(intent, 0); // the 0 corresponds to the
		// requestCode
		// in the onActivityResult parameter list
    }//end newMessage
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chats_main, menu);
        return true;
    }
    
    
    
    //receiver that listens for incoming msgs while in
    //the onResume state
    @Override
    protected void onResume(){
    	//register the receiver
    	registerReceiver(intentReceiver,intentFilter);
    	super.onResume();
    }
    @Override
    protected void  onPause(){
    	//unregister the receiver
    	unregisterReceiver(intentReceiver);
    	super.onPause();
    	}
    
   

    private void displayListView(String num, String msg) {
    	MessageEntry entry = datasource.addMessage(number, message);
		adapter.add(entry);

		////******Needs this to get the view to call the setOnClick****
		ListView list =  (ListView) adapter.getView(1,this, values);

		
		
		//    	  
//    	   
//    	  
//    	  listView.setOnItemClickListener(new OnItemClickListener() {
//    	   public void onItemClick(AdapterView<?> parent, View view,
//    	     int position, long id) {
//    	       // When clicked, start a new activity with a single conversation chat
//    	    onGoingMessage(view);
//    	   }
//    	  });
    	   
    	 }
    	
  
     
    public void onGoingMessage(View view){
    	Bundle b = new Bundle();
    	Intent intent = new Intent(this, OnGoingMessage.class);
    	intent.putExtras(b); // put the bundle into the intent
    	intent.putExtra("onGoMessage", message);
    	intent.putStringArrayListExtra("msgList", (ArrayList<String>)messageList);
    	intent.putExtra("onGoNumber", ((TextView) view).getText().toString());
		startActivity(intent); 
    }
    
   
    
    protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putStringArrayList("msgList", (ArrayList<String>) messageList);
		outState.putStringArrayList("numList", (ArrayList<String>)numberList);
		outState.putString("msg", message);

	}
    
    public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// restore the state of the game --
		// local variables = savedInstanceState.get...
		message = savedInstanceState
				.getString("msg");
		numberList = savedInstanceState.getStringArrayList("numList");
		messageList = savedInstanceState.getStringArrayList("msgList");
		
	}
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
			Bundle b = data.getExtras();
			message = b.getString("message");
			messageList.add(message);
			numberList = b.getStringArrayList("nList");
			tempList = b.getStringArrayList("mList");
			int ctr = 0;
			while(!tempList.isEmpty())
			{
				if(numberList.contains((String)tempList.get(ctr).substring(0, 10))){
					tempList.remove(ctr);
				}
				else
					numberList.add((String)tempList.remove(ctr).substring(0, 10));
			}
		//	displayListView();
			
		
	}
    
}
