package edu.ncc.chats;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class Events extends ListActivity {
	@Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_events);
    String[] values = new String[] { "Happy New Year", "Happy Valentine's Day", "Happy St. Paddy's Day",
        "Happy 4th of July", "Happy Thanksgiving", "Merry Christmas", "Happy Halloween", "Happy Chanukah",
        "Happy Kwanza", "Eid Mubarak" };
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, values);
    setListAdapter(adapter);
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    String item = (String) getListAdapter().getItem(position);
    Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
  }
} 