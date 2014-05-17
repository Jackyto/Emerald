package com.emerald;

import com.emerald.dialogs.SongDialog;
import com.emerald.fragments.AlbumFragment;
import com.emerald.fragments.ArtistFragment;
import com.emerald.fragments.HomeFragment;
import com.emerald.fragments.PlaylistFragment;
import com.emerald.fragments.RemoteFragment;
import com.emerald.fragments.SongFragment;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends Activity
implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnCompletionListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence 		mTitle;
	private static MusicManager manager;
	private static MusicService	mService;

	private static boolean		fromDrawer = false;
	private static boolean		fromNotif = false;
	private static boolean		fullAlbum = false;
	private static int			fragmentIndex = 0;

	private ImageButton			playButton, nextButton, prevButton, addButton;
	private TextView			songLabel;
	private static Activity			act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		act = this;

		setManager(new MusicManager(getApplicationContext()));

		loadPlaylists();

		if (MusicManager.getCurrentPlaylist().getPlaylist().size() > 0) {
			MusicManager.setCurrentSong(MusicManager.getCurrentPlaylist().getPlaylist().get(0));
			MusicManager.setCurrentAlbum(MusicManager.getAlbumFromSong(MusicManager.getCurrentSong()));
			MusicManager.getCurrentPlaylist().setIndex(0);
		}

		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		Intent intent= new Intent(this, MusicService.class);
		bindService(intent, mConnection,
				Context.BIND_AUTO_CREATE);

		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		playButton = (ImageButton) findViewById(R.id.playButton);
		nextButton = (ImageButton) findViewById(R.id.nextButton);
		prevButton = (ImageButton) findViewById(R.id.prevButton);
		addButton = (ImageButton) findViewById(R.id.addButton);
		songLabel = (TextView) findViewById(R.id.songLabel);

		if (MusicManager.getCurrentSong() != null) 
			songLabel.setText(MusicManager.getCurrentSong().getTitle() + " by " + MusicManager.getCurrentSong().getArtist());


		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MusicManager.getCurrentSong() != null) {
					if (MusicService.isPlaying())
						pause();
					else
						resume();
				}
			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MusicManager.getCurrentPlaylist().getPlaylist().size() > 0)
					next();
			}
		});

		prevButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MusicManager.getCurrentPlaylist().getPlaylist().size() > 0)
					prev();
			}
		});

		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MusicManager.getCurrentSong() != null) {
					SongDialog sd = new SongDialog(act, MusicManager.getCurrentSong());
					sd.show();
				}
			}

		});

	}

	public static void showNotif() {
		try {
			NotificationManager notificationManager = (NotificationManager) 
					act.getSystemService(NOTIFICATION_SERVICE); 

			Bitmap b = MusicManager.getAlbumFromSong(MusicManager.getCurrentSong()).getArt();
			Bitmap.createScaledBitmap(b, 128, 128, false);

			NotificationCompat.Builder nb = new NotificationCompat.Builder(act);

			nb.setContentTitle(act.getResources().getString(R.string.app_name));
			nb.setContentText(MusicManager.getCurrentSong().getTitle());
			nb.setSmallIcon(R.drawable.ic_launcher);
			nb.setLargeIcon(b);

			Intent prevRcv = new Intent(act, NotifButtonReceiver.class);
			prevRcv.setAction("PREV");
			PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(act, 0, prevRcv, PendingIntent.FLAG_UPDATE_CURRENT); 
			nb.addAction(android.R.drawable.ic_media_rew, null, pendingIntentPrev);

			Intent playRcv = new Intent(act, NotifButtonReceiver.class);
			playRcv.setAction("PLAY");
			PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(act, 0, playRcv, PendingIntent.FLAG_UPDATE_CURRENT);
			nb.addAction(android.R.drawable.ic_media_play, null, pendingIntentPlay);

			Intent nextRcv = new Intent(act, NotifButtonReceiver.class);
			nextRcv.setAction("NEXT");
			PendingIntent pendingIntentNext = PendingIntent.getBroadcast(act, 0, nextRcv, PendingIntent.FLAG_UPDATE_CURRENT);		
			nb.addAction(android.R.drawable.ic_media_ff, null, pendingIntentNext);

			Notification n = nb.build();

			n.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

			notificationManager.notify(1, n); 
		} catch (Exception e) {
			Log.e("Notification", e.getMessage());
		}
	}

	public static void cancelNotification(Context ctx, int notifyId) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
		nMgr.cancel(1);
	}


	public static class NotifButtonReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() != null ) {
				setFromNotif(true);
				if (intent.getAction().equals("PREV")) {
					mService.prev();
				}
				else if (intent.getAction().equals("PLAY")) {
					if (MusicManager.getCurrentSong() != null) {
						if (MusicService.isPlaying())
							mService.pause();
						else
							mService.resume();
					}
				}
				else if (intent.getAction().equals("NEXT"))
					mService.next();
				showNotif();
				((MainActivity) act).refreshPlayer();
			}
		}
	}

	public void next() {
		mService.next();
		songLabel.setText(MusicManager.getCurrentSong().getTitle() + " by " + MusicManager.getCurrentSong().getArtist());

		refreshPlayer();

		if (MusicService.isPlaying())
			playButton.setImageResource(android.R.drawable.ic_media_pause);
		else
			playButton.setImageResource(android.R.drawable.ic_media_play);

		mService.getPlayer().setOnCompletionListener(this);
		showNotif();

	}

	public void prev() {
		mService.prev();
		songLabel.setText(MusicManager.getCurrentSong().getTitle() + " by " + MusicManager.getCurrentSong().getArtist());

		refreshPlayer();

		if (MusicService.isPlaying())
			playButton.setImageResource(android.R.drawable.ic_media_pause);
		else
			playButton.setImageResource(android.R.drawable.ic_media_play);

		mService.getPlayer().setOnCompletionListener(this);
		showNotif();

	}

	public void resume() {
		playButton.setImageResource(android.R.drawable.ic_media_pause);
		mService.resume();
		mService.getPlayer().setOnCompletionListener(this);
		showNotif();
	}

	public void	play() {
		songLabel.setText(MusicManager.getCurrentSong().getTitle() + " by " + MusicManager.getCurrentSong().getArtist());
		playButton.setImageResource(android.R.drawable.ic_media_pause);

		mService.play();
		mService.getPlayer().setOnCompletionListener(this);
		showNotif();
	}

	public void pause() {
		playButton.setImageResource(android.R.drawable.ic_media_play);
		mService.pause();
		mService.getPlayer().setOnCompletionListener(this);
		cancelNotification(act, 1);
	}

	public void refreshPlayer() {
		changeView(0);
	}


	@Override
	public void onCompletion(MediaPlayer mp) {
		mService.setPlayer(mp);
		next();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, 
				IBinder binder) {
			MusicService.MyBinder b = (MusicService.MyBinder) binder;
			setmService(b.getService());
			if (MusicManager.getCurrentSong() != null) {
				Uri songUri = Uri.parse(MusicManager.getCurrentSong().getPath());
				getmService().setPlayer(MediaPlayer.create(getApplicationContext(), songUri));
				mService.getPlayer().setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						mService.setPlayer(mp);
						next();
					}
				});
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			setmService(null);
		}
	};

	public void changeView(int position) {
		fragmentIndex = position;

		if (isFromNotif()) {
			fromNotif = false;
		}

		FragmentManager fragmentManager = getFragmentManager();
		switch (position) {
		case 0 :
			mTitle = getString(R.string.title_home);
			fragmentManager.beginTransaction()
			.replace(R.id.container, HomeFragment.newInstance())
			.commit();
			break;
		case 1:
			mTitle = getString(R.string.title_artists); 
			fragmentManager.beginTransaction()
			.replace(R.id.container, ArtistFragment.newInstance())
			.commit();
			break;
		case 2:
			if (MusicManager.getCurrentArtist() == null
			|| isFromDrawer())
				mTitle = getString(R.string.title_albums);
			else
				mTitle = MusicManager.getCurrentArtist().getName();

			fragmentManager.beginTransaction()
			.replace(R.id.container, AlbumFragment.newInstance())
			.commit();
			break;
		case 3:
			if (MusicManager.getCurrentAlbum() == null
			|| isFromDrawer())
				mTitle = getString(R.string.title_songs);
			else
				mTitle = MusicManager.getCurrentAlbum().getName();
			fragmentManager.beginTransaction()
			.replace(R.id.container, SongFragment.newInstance())
			.commit();
			break;
		case 4:
			mTitle = getString(R.string.title_playlist);
			fragmentManager.beginTransaction()
			.replace(R.id.container, PlaylistFragment.newInstance())
			.commit();
			break;
		case 5:
			mTitle = getString(R.string.title_remote);
			fragmentManager.beginTransaction()
			.replace(R.id.container, RemoteFragment.newInstance())
			.commit();
			break;
		}
		getActionBar().setTitle(mTitle);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		setFromDrawer(true);
		changeView(position);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (fragmentIndex > 3)
			changeView(0);
		else {
			fragmentIndex--;
			if (MusicManager.getCurrentAlbum() == null && fragmentIndex == 2) {
				fragmentIndex = 1;
			}
			if (fragmentIndex < 0) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

				alertDialogBuilder
				.setMessage("Do you really want to quit Emerald ?")
				.setCancelable(false)
				.setPositiveButton("Run in background",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						moveTaskToBack(true); 
					}
				})
				.setNegativeButton("Quit",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						savePlaylists();
						if (mService != null)
							mService.stop();
						unbindService(mConnection);
						cancelNotification(act, 1);
						finish();
					}
				});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.setCanceledOnTouchOutside(true);
				alertDialog.show();

			} else
				changeView(fragmentIndex);
		}
	}

	private void savePlaylists() {
		getManager().open();

		MusicManager.getDbHelper().cleanUp(getManager().getDatabase());

		MusicManager.getDbHelper().createTablesFromPlaylists(getManager().getDatabase());

		getManager().savePlaylistNames();
		MusicManager.cleanCurrent(20);
		getManager().createPlaylistInDB(MusicManager.getCurrentPlaylist());

		int i;
		for (i = 0; i < MusicManager.getUserPlaylists().size(); i++)
			getManager().createPlaylistInDB(MusicManager.getUserPlaylists().get(i));

		getManager().close();
	}

	private void loadPlaylists() {
		getManager().open();

		try {
			MusicManager.setCurrentPlaylist(getManager().createPlaylistFromDB(MusicManager.CURRENT));
			if (MusicManager.getPlaylistNames() != null) {
				int i;
				for (i = 0; i < MusicManager.getPlaylistNames().size(); i++) {
					if (MusicManager.isPlaylistExists(MusicManager.fetchPlaylist(MusicManager.getPlaylistNames().get(i)))) {
						MusicManager.getUserPlaylists().set(i, getManager().createPlaylistFromDB(MusicManager.getPlaylistNames().get(i)));
					}	
				}
			} 
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		getManager().close();

	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		 */
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_example) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static MusicManager getManager() {
		return manager;
	}

	public static void setManager(MusicManager manager) {
		MainActivity.manager = manager;
	}

	public static boolean isFromDrawer() {
		return fromDrawer;
	}

	public static void setFromDrawer(boolean fromDrawer) {
		MainActivity.fromDrawer = fromDrawer;
	}

	public static MusicService getmService() {
		return mService;
	}

	public void setmService(MusicService mService) {
		MainActivity.mService = mService;
	}

	public static int getFragmentIndex() {
		return fragmentIndex;
	}

	public static void setFragmentIndex(int fragmentIndex) {
		MainActivity.fragmentIndex = fragmentIndex;
	}

	public static boolean isFullAlbum() {
		return fullAlbum;
	}

	public static void setFullAlbum(boolean fullAlbum) {
		MainActivity.fullAlbum = fullAlbum;
	}

	public static boolean isFromNotif() {
		return fromNotif;
	}

	public static void setFromNotif(boolean fromNotif) {
		MainActivity.fromNotif = fromNotif;
	}

}
