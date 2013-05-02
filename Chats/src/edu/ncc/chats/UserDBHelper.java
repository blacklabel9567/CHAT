package edu.ncc.chats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class UserDBHelper extends SQLiteOpenHelper  {
	// The URI scheme used for content URIs
    public static final String SCHEME = "content";

    // The provider's authority
    public static final String AUTHORITY = "edu.ncc.chats.ChatsMain";

    /**
     * The DataProvider content URI
     */
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);
	
    // table name
	public static final String TABLE_NAME = "users";

	// columns in the table
	public static final String _ID = "_id";
	public static final String NAME = "name";
	public static final String NUMBER = "number";
	
	// database version and name
		private static final int DATABASE_VERSION = 3;
		private static final String DATABASE_NAME = "chats.db";
		
		public UserDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + 
					" INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + 
			" TEXT, " + NUMBER + " TEXT);");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
		
}
