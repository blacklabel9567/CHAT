package edu.ncc.chats;


import java.util.ArrayList;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;

public class NewMessageActivity extends Activity {
	protected EditText txtPhoneNo;
	protected EditText txtMessage;
	protected List<String> messageList;
	protected List<String> numberList;
	protected Bundle b;
	String tempNumber;
	String theNumber;
	String theMessage;
	private BroadcastReceiver send;
	private BroadcastReceiver deliver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
		
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
		
		// Show the Up button in the action bar.
		setupActionBar();
		b = this.getIntent().getExtras();
		
		txtPhoneNo = (EditText)findViewById(R.id.to);
		txtMessage = (EditText)findViewById(R.id.message);
		messageList = new ArrayList<String>();
		numberList = getIntent().getStringArrayListExtra("numList");


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

		theNumber = txtPhoneNo.getText().toString();
		theMessage = txtMessage.getText().toString();

		StringTokenizer st = new StringTokenizer(theNumber,",");
		while (st.hasMoreElements())
		{
			tempNumber = (String)st.nextElement();
			if(tempNumber.length()>0 && theMessage.trim().length()>0) {
				messageList.add(tempNumber);
				sendSMS(tempNumber, theMessage);
			}
			else {
				Toast.makeText(getBaseContext(), 
						"Please enter both the phone number and the message.", 
						Toast.LENGTH_SHORT).show();
			}
		}
		b = new Bundle();

		b.putString("message", theMessage);
		b.putString("number", theNumber);
		b.putStringArrayList("mList", (ArrayList<String>) messageList);
		b.putStringArrayList("nList", (ArrayList<String>) numberList);

		this.getIntent().putExtras(b);
		this.setResult(RESULT_OK, getIntent());
		finish();
		
		
		/*Bundle b = new Bundle();
    	Intent intent = new Intent(this, OnGoingMessage.class);
    	intent.putExtras(b); // put the bundle into the intent
		startActivityForResult(intent, 0);*/
	}//end sendMessage

		private void sendSMS(String number, String message){
			String msgSent ="SMS_SENT";
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
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewMessageActivity.this);
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

	public void onStop(){
		super.onStop();
		unregisterReceiver(send);
		unregisterReceiver(deliver);
	}

	public void events(View view){
    	Intent intent = new Intent(NewMessageActivity.this, Events.class);
    	startActivity(intent);
    }//end events 
}
