package edu.ncc.chats;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class OnGoingMessage extends Activity {

	protected EditText txtPhoneNo;
	protected Bundle b;
	protected EditText txtMessage;
	String theNumber;
	String theMessage;
	View v;
	List<String> onGoingList = new ArrayList<String>();
	private BroadcastReceiver send;
	private BroadcastReceiver deliver;
	
	//Intent filter to listen for incoming msgs	
	IntentFilter intentFilter;
	//receiver to handle incomong msgs
	private BroadcastReceiver intentReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			//display the mesg in the textview
			//TextView inText = (TextView)findViewById(R.id.messageOnG);
			//inText.setText(arg1.getExtras().getString("sms"));
			theMessage =(String)arg1.getExtras().getString("sms");
			theMessage = theMessage.substring(25);
			onGoingList.add(theMessage);
			displayListView();
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ongoing_message);
		
		//intent to filter for SMS messages received
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
		
        
        send = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {

				switch(getResultCode())
				{
				case Activity.RESULT_OK:
					Toast.makeText(OnGoingMessage.this,"Dispatch Sent!",Toast.LENGTH_LONG).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(),"GENERIC FAILURE!",Toast.LENGTH_LONG).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(),"NO SERVICE!",Toast.LENGTH_LONG).show();
					break;
				}
			}
		};
		
		deliver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent) {

				switch(getResultCode())
				{
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(),"Dispatch Delivered!",Toast.LENGTH_LONG).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(),"Dispatch NOT Delivered!",Toast.LENGTH_LONG).show();
					break;
				}
			}
		};
		
		onGoingList = getIntent().getStringArrayListExtra("msgList");
		theNumber = getIntent().getStringExtra("onGoNumber");
		theMessage = getIntent().getStringExtra("onGoMessage");
		txtMessage = (EditText)findViewById(R.id.messageOnG);
		// Show the Up button in the action bar.
				setupActionBar();
		displayListView();


	}
	
	private void displayListView() {
   	 
  	  
 	   
  	  //create an ArrayAdapter from the String Array
  	  ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
  	    R.layout.ongoing_message_list, onGoingList);
  	  ListView listView = (ListView) findViewById(R.id.onGoingList);
  	  // Assign adapter to ListView
  	  listView.setAdapter(dataAdapter);
  	   
  	  //enables filtering for the contents of the given ListView
  	  listView.setTextFilterEnabled(true);
  	 
  	  listView.setOnItemClickListener(new OnItemClickListener() {
  	   public void onItemClick(AdapterView<?> parent, View view,
  	     int position, long id) {
  	       // When clicked, start a new activity with a single conversation chat
  	    
  	   }
  	  });
  	   
  	 }
	
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void sendMessage(View view){

		
		theMessage = txtMessage.getText().toString();

		StringTokenizer st = new StringTokenizer(theNumber,",");
		while (st.hasMoreElements())
		{
			String tempNumber = (String)st.nextElement();
			if(tempNumber.length()>0 && theMessage.trim().length()>0) {
				Time now = new Time();
				now.setToNow();
				onGoingList.add(theMessage + "\n" + now.toString());
				sendSMS(tempNumber, theMessage);
			}
			else {
				Toast.makeText(getBaseContext(), 
						"Please enter the message.", 
						Toast.LENGTH_SHORT).show();
			}
		}
		
		displayListView();
		txtMessage.setText("");
	}//end sendMessage

		private void sendSMS(String number, String message){
			String msgSent ="Message Sent!";
			String msgDelivered = "Message Delivered";

			PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(msgSent), 0);
			PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(msgDelivered), 0);


			registerReceiver(send,new  IntentFilter(msgSent));//end registerReciever

			registerReceiver(deliver,new  IntentFilter(msgDelivered));//end registerReciever





			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(number, null, message, sentPI, deliveredPI);
			
		}//end sendSMS
	

	public void discard(View view) {
		//without using a dialog prompt
		//Intent intent = new Intent();
		//setResult(RESULT_OK, intent);
		//finish();

		{
			//making sure the user wants to discard the message
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(OnGoingMessage.this);
			alertDialog.setTitle(R.string.discardPrompt);
			alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					;
				}

			});
			alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
					Toast.makeText(getBaseContext(),"Dispatch has been discarded.",Toast.LENGTH_LONG).show();
				}

			});

			alertDialog.show();
		}
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
    public void onStop(){
		super.onStop();
		unregisterReceiver(send);
		unregisterReceiver(deliver);
	}
    
	public void events(View view){
    	Intent intent = new Intent(OnGoingMessage.this, Events.class);
    	startActivity(intent);
    }//end events 
}
