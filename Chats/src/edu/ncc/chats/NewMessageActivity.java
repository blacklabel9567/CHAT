package edu.ncc.chats;

import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
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
	EditText txtPhoneNo;
	EditText txtMessage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
		// Show the Up button in the action bar.
		setupActionBar();
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
		String msgSent ="Message Sent!";
    	String msgDelivered = "Message Delivered";
    	
    	txtPhoneNo = (EditText)findViewById(R.id.to);
		txtMessage = (EditText)findViewById(R.id.message);
		
		String theNumber = txtPhoneNo.getText().toString();
		String theMessage = txtMessage.getText().toString();
    	
    	PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(msgSent), 0);
    	PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(msgDelivered), 0);
    	
    	registerReceiver(new BroadcastReceiver()
    	{
    		public void onReciever(Context arg0, Intent arg1)
    		{
    			switch(getResultCode())
    			{
    			case Activity.RESULT_OK:
    				Toast.makeText(NewMessageActivity.this,"SMS Sent",Toast.LENGTH_LONG).show();
    				break;
    			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
    				Toast.makeText(getBaseContext(),"Generic Failure",Toast.LENGTH_LONG).show();
    				break;
    			case SmsManager.RESULT_ERROR_NO_SERVICE:	
    				Toast.makeText(getBaseContext(),"No Service",Toast.LENGTH_LONG).show();
    				break;
    				
    		}
    	}

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				
			}
    	},new  IntentFilter(msgSent));//end registerReciever
    	
     	registerReceiver(new BroadcastReceiver()
    	{
    		public void onReciever(Context arg0, Intent arg1)
    		{
    			switch(getResultCode())
    			{
    			case Activity.RESULT_OK:
    				Toast.makeText(getBaseContext(),"SMS deleivered",Toast.LENGTH_LONG).show();
    				break;
    			case Activity.RESULT_CANCELED:
    				Toast.makeText(getBaseContext(),"SMS not deleivered",Toast.LENGTH_LONG).show();
    				break;
    		}
    	}

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				
			}
    	},new  IntentFilter(msgDelivered));//end registerReciever
    	
		
		
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(theNumber, null, theMessage, sentPI, deliveredPI);
	}//end sendMessage

}
