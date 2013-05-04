package edu.ncc.chats;


import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.AdapterView.OnItemClickListener;

public class ChatsMain extends Activity {

	
	String message = new String();
	String number = new String();
	



	private UserDataSource datasource;
	private MessageDataSource msgsource;

	ArrayAdapter<UserEntry> adapter;
	List<UserEntry> values;

	//Intent filter to listen for incoming msgs	
	IntentFilter intentFilter;



	//receiver to handle incoming msgs
	private BroadcastReceiver intentReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			long id;
			number = (String) arg1.getExtras().getString("sms");
			number = (String) number.substring(0,number.indexOf(":"));

			message = (String) arg1.getExtras().getString("sms");
			message = (String) message.substring(message.indexOf(":")+1);

			id = datasource.checkEntry(number);

			if(id==0){
				datasource.addUser(number, message);
				id = datasource.checkEntry(number);
				msgsource.addMessage(message, id);
				
			}else{
				datasource.changeMessage(id,message);
				msgsource.addMessage(message, id);				
			}

			displayListView();

		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats_main);

		datasource = new UserDataSource(this);
		datasource.open();

		msgsource = new MessageDataSource(this);
		msgsource.open();

		intentFilter = new IntentFilter();
		intentFilter.addAction("SMS_RECEIVED_ACTION");

		//Generate list View from ArrayList
		displayListView();

	}



	public void newMessage(View view){
		Intent intent = new Intent(this, NewMessageActivity.class);
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



	private void displayListView() {

		values = datasource.getAllUsers();
		adapter = new ArrayAdapter<UserEntry>(this,R.layout.message_list, values);

		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);

		//enables filtering for the contents of the given ListView
		//	listView.setTextFilterEnabled(true);

		Log.d("DISPLAY VIEW","BEFORE ONCLICK LISTENER" );
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("DISPLAY VIEW",Long.toString(id) );
				Log.d("DISPLAY VIEW","INN ONCLICK LISTENER" );
				// When clicked, start a new activity with a single conversation chat
				onGoingMessage(view,id+1);
			}
		});

	}



	public void onGoingMessage(View view,long id){
		Bundle b = new Bundle();
		
		Intent intent = new Intent(this, OnGoingMessage.class);
		intent.putExtras(b); // put the bundle into the intent
		intent.putExtra("id", String.valueOf(id));
		intent.putExtra("num", number);
		startActivity(intent); 
	}



	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		//displayListView();

	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			switch (resultCode) {

		case RESULT_CANCELED:
			
			break;
		case RESULT_OK:
				displayListView();
		default :
			break;
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



}
