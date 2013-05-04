package edu.ncc.chats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;


//I think/know the problem is that this is not getting executed
//not sure why be because the manifest is set up to listen for 
//this receiver.
public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		//get message passed in
		Bundle bundle = arg1.getExtras();
		SmsMessage[] messages = null;
		String str = "";
		
		
		if(bundle != null)
		{
			Object[] pdus = (Object[]) bundle.get("pdus");
			messages = new SmsMessage[pdus.length];
			for(int i = 0; i<messages.length;i++)
			{
				messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				str += messages[i].getOriginatingAddress();
				str += ":";
				str += messages[i].getMessageBody().toString();
				str += "\n";
			}
			//display the message
			Toast.makeText(arg0, str, Toast.LENGTH_LONG).show();
			
			//send a broad cast Intent to update the SMS receiver in the in ongoing/and main
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms",str);
            arg0.sendBroadcast(broadcastIntent);
		}

	}

}
