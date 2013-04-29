package edu.ncc.chats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class MessageDBHelper extends SQLiteOpenHelper{
	// The URI scheme used for content URIs
    public static final String SCHEME = "content";

    // The provider's authority
    public static final String AUTHORITY = "edu.ncc.chats.ChatsMain";

    /**
     * The DataProvider content URI
     */
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);
	
    // table name
	public static final String TABLE_NAME = "chats";

	// columns in the table
	public static final String _ID = "_id";
	public static final String _CID = "c_Id";
	public static final String MESSAGE = "message";
	
	// database version and name
		private static final int DATABASE_VERSION = 3;
		private static final String DATABASE_NAME = "chats.db";
		
		public MessageDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + 
					" INTEGER PRIMARY KEY AUTOINCREMENT, " + _CID + 
			" TEXT, " + MESSAGE + " TEXT);");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
		
}
