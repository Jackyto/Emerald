package com.emerald;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.emerald.containers.Album;
import com.emerald.containers.Artist;
import com.emerald.containers.Playlist;
import com.emerald.containers.Song;

public class MusicManager implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -839763128187787446L;

	private static Context	context;

	private static Artist					currentArtist;
	private static Album					currentAlbum;
	private static Song						currentSong;

	private static List<Artist>				artistList;
	private static List<Album>				albumList;
	private static List<Song>				songList;

	private static Playlist					currentPlaylist;
	private static List<Playlist>			userPlaylists;
	private static List<String>				playlistNames;

	private SQLiteDatabase 					database;
	private static PlaylistSQLiteHelper		dbHelper;

	private String[] 						allColumns = 
		{PlaylistSQLiteHelper.COLUMN_ID,
			PlaylistSQLiteHelper.COLUMN_TITLE,
			PlaylistSQLiteHelper.COLUMN_ARTIST,
			PlaylistSQLiteHelper.COLUMN_ALBUM,
			PlaylistSQLiteHelper.COLUMN_PATH,
			PlaylistSQLiteHelper.COLUMN_DURATION};

	private String[]						nameColumns =
		{PlaylistSQLiteHelper.COLUMN_ID,
			PlaylistSQLiteHelper.COLUMN_NAME
		};

	private static Utilities				utils;

	public static final String				CURRENT = "current";

	static int 						i;

	public MusicManager(Context context) {
		super();
		MusicManager.context = context;
		setUtils(new Utilities());

		setArtistList(new ArrayList<Artist>());
		setAlbumList(new ArrayList<Album>());
		setSongList(new ArrayList<Song>());
		setUserPlaylists(new ArrayList<Playlist>());
		setPlaylistNames(new ArrayList<String>());

		setCurrentPlaylist(new Playlist(new ArrayList<Song>(), 0, CURRENT));

		setDbHelper(new PlaylistSQLiteHelper(context));

		retrieveArtists();
		retrieveAlbums();
		retrieveSongs();
		retrivePlaylistNames();
	}

	private void retrivePlaylistNames() {
		open();
		try {
			Cursor cursor = database.query(PlaylistSQLiteHelper.TABLE_REF,
					nameColumns, null, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Playlist p = cursorToPlaylist(cursor);
					userPlaylists.add(p);
					System.out.println(p.getName());
					cursor.moveToNext();
				}
				// make sure to close the cursor
				cursor.close();
			}

		}  catch (SQLException e) {
			System.out.println("PAS GOOD");
			Log.e("Exception on query", e.toString());
		}
		close();
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	public void close() {
		dbHelper.close();
	}

	public void savePlaylistNames() {
		for (i = 0; i < userPlaylists.size(); i++) {
			ContentValues values = new ContentValues();

			values.put(PlaylistSQLiteHelper.COLUMN_NAME, userPlaylists.get(i).getName());

			database.insert(PlaylistSQLiteHelper.TABLE_REF, null, values);
		}
	}

	public void createPlaylistInDB(Playlist p) {
		System.out.println(p.getName());
		if (p.getPlaylist() != null) {
			int k;
			for (k = p.getIndex(), i = 0; i < p.getPlaylist().size(); i++) {

				if (k == p.getPlaylist().size())
					k = 0;

				ContentValues values = new ContentValues();

				values.put(PlaylistSQLiteHelper.COLUMN_TITLE, p.getPlaylist().get(i).getTitle());
				values.put(PlaylistSQLiteHelper.COLUMN_ARTIST, p.getPlaylist().get(i).getArtist());
				values.put(PlaylistSQLiteHelper.COLUMN_ALBUM, p.getPlaylist().get(i).getAlbum());
				values.put(PlaylistSQLiteHelper.COLUMN_PATH, p.getPlaylist().get(i).getPath());
				values.put(PlaylistSQLiteHelper.COLUMN_DURATION, String.valueOf(p.getPlaylist().get(i).getDuration()));

				String table = p.getName();
				System.out.println(p.getPlaylist().get(i).getTitle());
				database.insert(table, null, values);	
			}
		}
	}

	public Playlist createPlaylistFromDB(String tbl_name) {
		System.out.println("AFTER OPEN DB LOADING");
		System.out.println(tbl_name);

		Playlist p = new Playlist(new ArrayList<Song>(), 0, tbl_name);
		try {
			Cursor cursor = database.query(tbl_name,
					allColumns, null, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Song song = cursorToSong(cursor);
					System.out.println(song.getTitle());

					p.getPlaylist().add(song);
					cursor.moveToNext();
				}
				cursor.close();
			}

		}  catch (SQLException e) {
			System.out.println("PAS GOOD");
			Log.e("Exception on query", e.toString());
		}
		return p;
	}

	private Playlist cursorToPlaylist(Cursor cursor) {
		return new Playlist(null, 0, cursor.getString(1));
	}

	private Song cursorToSong(Cursor cursor) {
		return new Song(cursor.getInt(0),
				cursor.getString(1),
				cursor.getString(2),
				cursor.getString(3),
				"", 
				cursor.getString(4), 
				cursor.getInt(5));
	}

	private void		retrieveArtists(){		
		String		selection = MediaStore.Audio.Artists._ID + " != 0";
		String[]	projection = {
				"DISTINCT " + MediaStore.Audio.Artists._ID,
				MediaStore.Audio.Artists.ARTIST_KEY,
				MediaStore.Audio.Artists.ARTIST,
				MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
		};

		Cursor 		cursor = context.getContentResolver().query(
				MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
				projection, selection, null, MediaStore.Audio.Artists.ARTIST);

		Artist 		tmp = null;

		if (cursor.moveToFirst()) {
			do {
				tmp = new Artist(cursor.getInt(0),
						cursor.getString(1),
						cursor.getString(2), cursor.getInt(3));

				artistList.add(tmp);
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	private void		retrieveAlbums(){		
		String		selection = MediaStore.Audio.Albums._ID + " != 0";
		String[]	projection = {
				"DISTINCT " + MediaStore.Audio.Albums._ID,
				MediaStore.Audio.Albums.ALBUM_KEY,
				MediaStore.Audio.Albums.ALBUM, 
				MediaStore.Audio.Albums.ARTIST,
				MediaStore.Audio.Albums.NUMBER_OF_SONGS
		};

		Cursor 		cursor = context.getContentResolver().query(
				MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
				projection, selection, null, null);

		Album 		tmp = null;

		if (cursor.moveToFirst()) {
			do {

				tmp = new Album(cursor.getInt(0),
						cursor.getString(1),
						cursor.getString(2),
						cursor.getString(3),
						cursor.getInt(4));

				Uri sArtworkUri = Uri
						.parse("content://media/external/audio/albumart");
				Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, tmp.getId());

				Bitmap bitmap = null;
				try {
					bitmap = MediaStore.Images.Media.getBitmap(
							context.getContentResolver(), albumArtUri);

				} catch (FileNotFoundException exception) {
					exception.printStackTrace();
					bitmap = BitmapFactory.decodeResource(context.getResources(),
							R.drawable.ic_launcher);
				} catch (IOException e) {
					e.printStackTrace();
					bitmap = BitmapFactory.decodeResource(context.getResources(),
							R.drawable.ic_launcher);
					e.printStackTrace();
				}
				tmp.setArt(bitmap);

				albumList.add(tmp);
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	private void		retrieveSongs(){
		String		selection = MediaStore.Audio.Media._ID + " != 0";
		String[]	projection = {
				"DISTINCT " + MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ARTIST, 
				MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.TITLE_KEY,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DURATION
		};

		Cursor 		cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				projection, selection, null, null);

		Song 		tmp = null;

		if (cursor.moveToFirst()) {
			do {

				tmp = new Song(cursor.getInt(0),
						cursor.getString(1),
						cursor.getString(2),
						cursor.getString(3),
						cursor.getString(4),
						cursor.getString(5),
						cursor.getInt(6));

				songList.add(tmp);
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	public int		getSongIndex(Song song) {
		for (i = 0; i < currentPlaylist.getSize(); i++)
			if (currentPlaylist.getPlaylist().get(i).getPath() == song.getPath())
				return i;
		return 0;
	}

	public List<Song>  	getSongListFromArtist(Artist artist) {
		List<Song>		songListArtist = new ArrayList<Song>();

		for (i = 0; i < songList.size(); i++) {
			if (songList.get(i).getArtist().contentEquals(artist.getName()))
				songListArtist.add(songList.get(i));
		}
		return songListArtist;
	}	

	public List<Album> getAlbumListFromArtist(Artist artist) {
		List<Album>		artistAlbumList = new ArrayList<Album>();

		for (i = 0; i < albumList.size(); i++) 
			if (albumList.get(i).getArtist().contentEquals(artist.getName())) 
				artistAlbumList.add(albumList.get(i));

		return artistAlbumList;
	}	

	public List<Song> getSongListFromAlbum(Album album) {
		List<Song>		albumSongList = new ArrayList<Song>();

		for (i = 0; i < songList.size(); i++) 
			if (songList.get(i).getAlbum().equals(album.getName())) 
				albumSongList.add(songList.get(i));

		return albumSongList;
	}

	public static void createPlaylistFromSong(Song song) {

		for (i = 0; i < songList.size(); i++) 
			if (songList.get(i).getAlbum().contentEquals(song.getAlbum()))
				currentPlaylist.getPlaylist().add(songList.get(i));
	}

	public static Album		getAlbumFromSong(Song song) {		
		for (i = 0; i < albumList.size(); i++) 
			if (albumList.get(i).getName().contentEquals(song.getAlbum()))
				return albumList.get(i);
		return null;
	}

	public static Artist	getArtistFromAlbum(Album album) {
		for (i = 0; i < artistList.size(); i++)
			if (artistList.get(i).getName().contentEquals(album.getArtist()))
				return artistList.get(i);
		return null;
	}

	public static boolean 		isPlaylistExists(Playlist p) {
		for (i = 0; i < userPlaylists.size(); i++)
			if (p.getName().contentEquals(userPlaylists.get(i).getName()))
				return true;
		return false;
	}

	public List<Artist> getArtistList() {
		return artistList;
	}

	public void setArtistList(List<Artist> artistList) {
		MusicManager.artistList = artistList;
	}

	public List<Album> getAlbumList() {
		return albumList;
	}

	public static void setAlbumList(List<Album> albumList) {
		MusicManager.albumList = albumList;
	}

	public List<Song> getSongList() {
		return songList;
	}

	public static void setSongList(List<Song> songList) {
		MusicManager.songList = songList;
	}

	public static Artist getCurrentArtist() {
		return currentArtist;
	}

	public static void setCurrentArtist(Artist currentArtist) {
		MusicManager.currentArtist = currentArtist;
	}

	public static Album getCurrentAlbum() {
		return currentAlbum;
	}

	public static void setCurrentAlbum(Album currentAlbum) {
		MusicManager.currentAlbum = currentAlbum;
	}

	public static Song getCurrentSong() {
		return currentSong;
	}

	public static void setCurrentSong(Song currentSong) {
		MusicManager.currentSong = currentSong;
	}

	public static List<String> getPlaylistNames() {
		return playlistNames;
	}

	public static void setPlaylistNames(List<String> playlistNames) {
		MusicManager.playlistNames = playlistNames;
	}

	public static Playlist getCurrentPlaylist() {
		return currentPlaylist;
	}

	public static void setCurrentPlaylist(Playlist playlist) {
		MusicManager.currentPlaylist = playlist;
	}

	public static Utilities getUtils() {
		return utils;
	}

	public static List<Playlist> getUserPlaylists() {
		return userPlaylists;
	}

	public static void setUserPlaylists(List<Playlist> userPlaylists) {
		MusicManager.userPlaylists = userPlaylists;
	}

	public static void setUtils(Utilities utils) {
		MusicManager.utils = utils;
	}

	public static void fetchPlaylists() {

	}

	public static PlaylistSQLiteHelper getDbHelper() {
		return dbHelper;
	}

	public static void setDbHelper(PlaylistSQLiteHelper dbHelper) {
		MusicManager.dbHelper = dbHelper;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}
}
