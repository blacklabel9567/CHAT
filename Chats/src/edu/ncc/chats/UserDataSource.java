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
		private UserDBHelper msgDbHelper;
		
		private String[] allColumns = { UserDBHelper._ID, UserDBHelper.NAME, UserDBHelper.MESSAGE, };
		
		public UserDataSource(Context context) {
			msgDbHelper = new UserDBHelper(context);
		}
		
		public void open() throws SQLException {
			database = msgDbHelper.getWritableDatabase();
		}
		
		public void close() {
			msgDbHelper.close();
		}
		

		public void deleteMessage(UserEntry msg) {
			long id = msg.getID();
			System.out.println("Comment deleted with id: " + id);
			database.delete(UserDBHelper.TABLE_NAME, UserDBHelper._ID
					+ " = " + id, null);
		}
		
		public UserEntry addMessage(String name, String msg)
		{
			ContentValues values = new ContentValues();
			values.put(UserDBHelper.NAME, name);
			values.put(UserDBHelper.MESSAGE, msg);
			long insertId = database.insert(UserDBHelper.TABLE_NAME, null, values);
			Cursor cursor = database.query(UserDBHelper.TABLE_NAME, allColumns, UserDBHelper._ID + " = " +insertId, null, null, null, null);
			cursor.moveToFirst();
			UserEntry entry = cursorToEntry(cursor);
			cursor.close();
			Log.d("Message", "In addMessage");
			return entry;
		}
		public List<UserEntry> getAllMessages() {
			List<UserEntry> msg = new ArrayList<UserEntry>();
			UserEntry entry = new UserEntry();
			Cursor cursor = database.query(UserDBHelper.TABLE_NAME,
					allColumns, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				entry = cursorToEntry(cursor);
				msg.add(entry);
				cursor.moveToNext();
			}
			cursor.close();
			return msg;
		}
		
		private UserEntry cursorToEntry(Cursor cursor) {
			UserEntry entry = new UserEntry();
			entry.setID(cursor.getLong(0));
			entry.setName(cursor.getString(1));
			entry.setMessage(cursor.getString(2));
			return entry;
		}
}
