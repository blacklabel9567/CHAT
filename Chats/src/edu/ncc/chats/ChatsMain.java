package edu.ncc.chats;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ChatsMain extends Activity {

	List<String> messageList = new ArrayList<String>();
	List<String> numberList = new ArrayList<String>();
	String message = new String();
	String number = new String();
	List<String> tempList = new ArrayList<String>();
	
	
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
			
			messageList.add(message);
			if(!numberList.contains((String)number))
				numberList.add(number);
			
			displayListView();
		}
	};
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_main);
          
      //intent to filter for SMS messages received
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
    
   

    private void displayListView() {
    	 
    	  
    	   
    	  //create an ArrayAdaptar from the String Array
    	  ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    	    R.layout.message_list, numberList);
    	  ListView listView = (ListView) findViewById(R.id.listView1);
    	  // Assign adapter to ListView
    	  listView.setAdapter(dataAdapter);
    	   
    	  //enables filtering for the contents of the given ListView
    	  listView.setTextFilterEnabled(true);
    	 
    	  listView.setOnItemClickListener(new OnItemClickListener() {
    	   public void onItemClick(AdapterView<?> parent, View view,
    	     int position, long id) {
    	       // When clicked, start a new activity with a single conversation chat
    	    onGoingMessage(view);
    	   }
    	  });
    	   
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
			displayListView();
			
		
	}
    
}
