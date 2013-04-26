package edu.ncc.chats;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MessageDataSource {

	// Database fields
		private SQLiteDatabase database;
		private MessageDBHelper msgDbHelper;
		
		private String[] allColumns = { MessageDBHelper._ID, MessageDBHelper.NAME, MessageDBHelper.MESSAGE, };
		
		public MessageDataSource(Context context) {
			msgDbHelper = new MessageDBHelper(context);
		}
		
		public void open() throws SQLException {
			database = msgDbHelper.getWritableDatabase();
		}
		
		public void close() {
			msgDbHelper.close();
		}
		

		public void deleteMessage(MessageEntry msg) {
			long id = msg.getID();
			System.out.println("Comment deleted with id: " + id);
			database.delete(MessageDBHelper.TABLE_NAME, MessageDBHelper._ID
					+ " = " + id, null);
		}
		
		public MessageEntry addMessage(String name, String msg)
		{
			ContentValues values = new ContentValues();
			values.put(MessageDBHelper.NAME, name);
			values.put(MessageDBHelper.MESSAGE, msg);
			long insertId = database.insert(MessageDBHelper.TABLE_NAME, null, values);
			Cursor cursor = database.query(MessageDBHelper.TABLE_NAME, allColumns, MessageDBHelper._ID + " = " +insertId, null, null, null, null);
			cursor.moveToFirst();
			MessageEntry entry = cursorToEntry(cursor);
			cursor.close();
			Log.d("Message", "In addMessage");
			return entry;
		}
		public List<MessageEntry> getAllMessages() {
			List<MessageEntry> msg = new ArrayList<MessageEntry>();
			MessageEntry entry = new MessageEntry();
			Cursor cursor = database.query(MessageDBHelper.TABLE_NAME,
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
		
		private MessageEntry cursorToEntry(Cursor cursor) {
			MessageEntry entry = new MessageEntry();
			entry.setID(cursor.getLong(0));
			entry.setName(cursor.getString(1));
			entry.setMessage(cursor.getString(2));
			return entry;
		}
}
