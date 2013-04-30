package edu.ncc.chats;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Events extends Activity {
	private ListView EventListView;
	private ArrayAdapter<String> listAdapter;
	private ArrayList<String> EventList;
	private String result;
	protected Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
		EventList = new ArrayList<String>();
		EventList.add("Happy New Year");
		EventList.add("Happy Valentine's Day");
		EventList.add("Happy St. Paddy's Day");
		EventList.add("Happy Easter");
		EventList.add("Happy Passover");
		EventList.add( "Happy 4th of July");
		EventList.add("Happy Halloween");
		EventList.add("Happy Thanksgiving");
		EventList.add("Happy Chanakau");
		EventList.add("Merry Christmas");
		EventList.add("Eid Mubarak");
				 
		displayListView();
	}
	
	private void displayListView() {
		EventListView = (ListView) findViewById(R.id.EventListView);
		
		
		listAdapter = new ArrayAdapter<String>(this, R.layout.event_list, EventList); 
	
		EventListView.setAdapter(listAdapter);
		EventListView.setTextFilterEnabled(true);
		
		EventListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked return to the previous activity with the message picked
				b = new Bundle();
				result = listAdapter.getItem(position);
				b.putString("result", result);
				getIntent().putExtras(b);
				setResult(RESULT_OK, getIntent());
				finish();
				
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.custom)
		{
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Create Custom Message");
			alert.setMessage("Message");
			
			final EditText input = new EditText(this);
			alert.setView(input);
			
			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					EventList.add(input.getText().toString());
					displayListView();
				}
			});
			
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			alert.show();
			
		}
		return true;
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_main, menu);
		return true;
	}

}
