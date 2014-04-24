package com.emerald;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlaylistSQLiteHelper extends SQLiteOpenHelper {

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "_title";
	public static final String COLUMN_ARTIST = "_artist";
	public static final String COLUMN_ALBUM = "_album";
	public static final String COLUMN_PATH = "_path";
	public static final String COLUMN_NAME = "_name";
	public static final String COLUMN_DURATION = "_duration";

	public static final String TABLE_REF = "playlist_ref";

	private static final String DATABASE_NAME = "playlists.db";
	private static final int DATABASE_VERSION = 1;


	// Database creation sql statement
	private static final String CREATE_QUERY = 
			"(" + COLUMN_ID	+ " integer primary key autoincrement, "
					+ COLUMN_TITLE + " text not null, "
					+ COLUMN_ARTIST + " text not null, "
					+ COLUMN_ALBUM + " text not null, "
					+ COLUMN_PATH + " text not null, "
					+ COLUMN_DURATION + " text not null);";

	private static final String CREATE_REF = "create table if not exists " + TABLE_REF + 
			"(" + COLUMN_ID	+ " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null);";

	static int		i;

	public PlaylistSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		//createTablesFromPlaylists(database);
	}

	public void createTablesFromPlaylists(SQLiteDatabase database) {
		try {
			for (i = 0; i < MusicManager.getUserPlaylists().size(); i++) 
				database.execSQL("DROP TABLE IF EXISTS " + MusicManager.getUserPlaylists().get(i).getName());
			database.execSQL("DROP TABLE IF EXISTS current");
			database.execSQL("DROP TABLE IF EXISTS " + TABLE_REF);
			
			for (i = 0; i < MusicManager.getUserPlaylists().size(); i++) 
				database.execSQL("create table if not exists " + MusicManager.getUserPlaylists().get(i).getName() + CREATE_QUERY);	

			database.execSQL(CREATE_REF);
			database.execSQL("create table if not exists current" + CREATE_QUERY);
		}
		catch (RuntimeException e) {
			System.out.println("Exception => " + e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.w(PlaylistSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		
		onCreate(db);
	}
}	