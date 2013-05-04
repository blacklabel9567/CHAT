package edu.ncc.chats;


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
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class OnGoingMessage extends Activity {
	protected EditText txtMessage;
	String theMessage;
	String theNumber;
	String sId;
	long id;
	
	private BroadcastReceiver send;
		
	private UserDataSource datasource;
	private MessageDataSource msgsource;

	ArrayAdapter<MessageEntry> adapter;
	List<MessageEntry> values;

	//Intent filter to listen for incoming msgs	
	IntentFilter intentFilter;
	//receiver to handle incoming msgs
	private BroadcastReceiver intentReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			String number;
			String message;
			long inId;
			number = (String) arg1.getExtras().getString("sms");
			number = (String) number.substring(0, number.indexOf(":"));

			message = (String) arg1.getExtras().getString("sms");
			message = (String) message.substring(message.indexOf(":")+1);

			inId = datasource.checkEntry(number);

			if(inId==0){
				datasource.addUser(number, message);
				inId = datasource.checkEntry(number);
				msgsource.addMessage(message, inId);
				
			}else{
				datasource.changeMessage(inId,message);
				msgsource.addMessage(message, inId);				
			}

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

        datasource = new UserDataSource(this);
		datasource.open();

		msgsource = new MessageDataSource(this);
		msgsource.open();
		
		
		sId = getIntent().getStringExtra("id");
		theNumber = getIntent().getStringExtra("num");
		
		id = Long.parseLong(sId);
		//theNumber = datasource.getUserNumber(id);
		
		txtMessage = (EditText)findViewById(R.id.messageOnG);
		
		// Show the Up button in the action bar.
				setupActionBar();
		displayListView();


	}
	
	private void displayListView() {
   	 	values = msgsource.getAllMessages(id);
		adapter = new ArrayAdapter<MessageEntry>(this,R.layout.message_list, values);

		ListView listView = (ListView) findViewById(R.id.onGoingList);
		listView.setAdapter(adapter);

  	 }
	
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
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
			if( theMessage.trim().length()>0) {
				msgsource.addMessage(theMessage, id);
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
		unregisterReceiver(send);
		
	}//end sendMessage

		private void sendSMS(String number, String message){
String msgSent ="Message Sent!";
			

			PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(msgSent), 0);
			

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

			
			registerReceiver(send,new  IntentFilter(msgSent));//end registerReciever


			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(number, null, message, sentPI, null);
			
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
    	unregisterReceiver(intentReceiver);
    	super.onStop();
		
	}
    
	public void events(View view){
    	Intent intent = new Intent(this, Events.class);
    	startActivityForResult(intent, 0);
    }//end events 
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {

		case RESULT_CANCELED:
			break;
		case RESULT_OK:
			//put stuff that gets returned from events here
			Bundle b = data.getExtras();
			theMessage = b.getString("result");
			txtMessage.setText(theMessage + " <name>");
		case RESULT_FIRST_USER:
			Bundle c = data.getExtras();
		default :
			break;
		}
	}
}
