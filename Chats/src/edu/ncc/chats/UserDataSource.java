package edu.ncc.chats;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDataSource {

	// Database fields
	private SQLiteDatabase database;
	private UserDBHelper userDbHelper;

	private String[] allColumns = { UserDBHelper._ID, UserDBHelper.NAME, UserDBHelper.NUMBER, };

	public UserDataSource(Context context) {
		userDbHelper = new UserDBHelper(context);
	}

	public void open() throws SQLException {
		database = userDbHelper.getWritableDatabase();
	}

	public void close() {
		userDbHelper.close();
	}


	public void deleteMessage(UserEntry usr) {
		long id = usr.getID();
		System.out.println("Comment deleted with id: " + id);
		database.delete(UserDBHelper.TABLE_NAME, UserDBHelper._ID
				+ " = " + id, null);
	}

	public UserEntry addUser(String name, String num)
	{
		ContentValues values = new ContentValues();
		values.put(UserDBHelper.NAME, name);
		values.put(UserDBHelper.NUMBER, num);
		long insertId = database.insert(UserDBHelper.TABLE_NAME, null, values);
		Cursor cursor = database.query(UserDBHelper.TABLE_NAME, allColumns, UserDBHelper._ID + " = " +insertId, null, null, null, null);
		cursor.moveToFirst();
		UserEntry entry = cursorToEntry(cursor);
		cursor.close();
		Log.d("USER", "In addUser");
		return entry;
	}
	public void changeMessage(long id, String msg)
	{
		UserEntry entry = new UserEntry();
		
		Cursor cursor = database.query(UserDBHelper.TABLE_NAME, allColumns, UserDBHelper._ID + " = '" + id+"'", null, null, null, null);
				
		cursor.moveToFirst();
		entry = cursorToEntry(cursor);
		entry.setNumber(msg);
		Log.d("changeMessage", entry.toString());
		cursor.close();
	}
	public long checkEntry(String number){
		
		long id = 0;

		UserEntry entry = new UserEntry();
		
		Cursor cursor = database.query(UserDBHelper.TABLE_NAME, allColumns, UserDBHelper.NAME + " = '" + number+"'", null, null, null, null);
				
		cursor.moveToFirst();
		if(cursor.getCount()==0){
			cursor.close();
			return 0;
		}
				
		entry = cursorToEntry(cursor);
		id = entry.getID();
				
		cursor.close();
		return id;

	}
	public List<UserEntry> getAllUsers() {
		List<UserEntry> usr = new ArrayList<UserEntry>();
		UserEntry entry = new UserEntry();
		Cursor cursor = database.query(UserDBHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			entry = cursorToEntry(cursor);
			usr.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		return usr;
	}
	
	
	
//	public String getUserNumber(long i){
//		long  id = i+1;
//		String number;
//		Log.d("getUserNumber", String.valueOf(id));
//		UserEntry entry = new UserEntry();
//		Log.d("getUserNumber", "22222");
//		Cursor cursor = database.query(UserDBHelper.TABLE_NAME,
//				allColumns, UserDBHelper.NAME + " = '" + id+ "'", null, null, null, null);
//		Log.d("getUserNumber", "3333333");
//		cursor.moveToFirst();
//		Log.d("getUserNumber", "4444444444");
//			entry = cursorToEntry(cursor);
//			Log.d("getUserNumber", "5555555");
//			number = entry.getName();
//			Log.d("getUserNumber", "66666666");
//		cursor.close();
//		return number;
//		
//	}
	private UserEntry cursorToEntry(Cursor cursor) {
		UserEntry entry = new UserEntry();
		entry.setID(cursor.getLong(0));
		entry.setName(cursor.getString(1));
		entry.setNumber(cursor.getString(2));
		return entry;
	}
}
