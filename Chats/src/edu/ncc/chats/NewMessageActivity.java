package edu.ncc.chats;



import java.util.List;
import java.util.StringTokenizer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.telephony.SmsManager;

public class NewMessageActivity extends Activity {
	protected EditText txtPhoneNo;
	protected EditText txtMessage;
	
	protected Bundle b;
	String tempNumber;
	String theNumber;
	String theMessage;
	String tempMessage;
	private BroadcastReceiver send;
	
	private UserDataSource datasource;
	private MessageDataSource msgsource;

	ArrayAdapter<MessageEntry> adapter;
	List<MessageEntry> values;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
		
		datasource = new UserDataSource(this);
		datasource.open();

		msgsource = new MessageDataSource(this);
		msgsource.open();
		
		
		// Show the Up button in the action bar.
		setupActionBar();
		b = this.getIntent().getExtras();
		
		txtPhoneNo = (EditText)findViewById(R.id.to);
		txtMessage = (EditText)findViewById(R.id.message);
		


	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void sendMessage(View view){
		long inId;
		theNumber = txtPhoneNo.getText().toString();
		theMessage = txtMessage.getText().toString();
		tempMessage = theMessage;
		StringTokenizer st = new StringTokenizer(theNumber,",");
		while (st.hasMoreElements())
		{
			
			tempNumber = (String)st.nextElement();
			if(tempNumber.length()>0 && theMessage.trim().length()>0) {
				inId = datasource.checkEntry(tempNumber);
				if(inId==0){
					if(theMessage.trim().contains((CharSequence)"<name>")){
						tempMessage = theMessage.replace((CharSequence)"<name>", (CharSequence)tempNumber);
						datasource.addUser(tempNumber, tempMessage);
						inId = datasource.checkEntry(tempNumber);
						msgsource.addMessage(tempMessage, inId);
						sendSMS(tempNumber, tempMessage);
					}else{
					datasource.addUser(tempNumber, theMessage);
					inId = datasource.checkEntry(tempNumber);
					msgsource.addMessage(theMessage, inId);
					sendSMS(tempNumber, theMessage);
					}
				}else{
					if(theMessage.trim().contains((CharSequence)"<name>")){
						tempMessage = theMessage.replace((CharSequence)"<name>", (CharSequence)tempNumber);
					datasource.changeMessage(inId,tempMessage);
					msgsource.addMessage(tempMessage, inId);
					sendSMS(tempNumber,tempMessage);
					}else{
						datasource.changeMessage(inId,theMessage);
						msgsource.addMessage(theMessage, inId);
						sendSMS(tempNumber, theMessage);
					}
				}
						
			}
			else {
				Toast.makeText(getBaseContext(), 
						"Please enter both the phone number and the message.", 
						Toast.LENGTH_SHORT).show();
			}
		}
			

		
		unregisterReceiver(send);
		this.setResult(RESULT_OK, getIntent());
		finish();
	
	}//end sendMessage

		private void sendSMS(String number, String message){
			String msgSent ="SMS_SENT";
			//String msgDelivered = "Message Delivered";
	
			PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(msgSent), 0);
		
		
			send = new BroadcastReceiver(){

				@Override
				public void onReceive(Context context, Intent intent) {

					switch(getResultCode())
					{
					case Activity.RESULT_OK:
						Toast.makeText(NewMessageActivity.this,"Dispatch Sent!",Toast.LENGTH_LONG).show();
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
			sms.sendTextMessage(number, null, message, sentPI,null);
		
		}//end sendSMS
		
		
	public void discard(View view) {
	

		{
			//making sure the user wants to discard the message
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewMessageActivity.this);
			alertDialog.setTitle(R.string.discardPrompt);
			alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					;
				}

			});
			alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					setResult(RESULT_CANCELED, getIntent());
					finish();
					Toast.makeText(getBaseContext(),"Dispatch has been discarded.",Toast.LENGTH_LONG).show();
				}

			});

			alertDialog.show();
		}
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
	public void onStop(){
		super.onStop();
		}
	
	public void onPause(){
		super.onPause();
	}
}
