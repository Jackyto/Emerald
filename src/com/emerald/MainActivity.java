package com.emerald;

import com.emerald.containers.Song;
import com.emerald.fragments.AlbumFragment;
import com.emerald.fragments.ArtistFragment;
import com.emerald.fragments.HomeFragment;
import com.emerald.fragments.PlaylistFragment;
import com.emerald.fragments.SongFragment;

import android.app.Activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
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
	private static boolean		fullAlbum = false;
	private static int			fragmentIndex = 0;

	private ImageButton			playButton, nextButton, prevButton;
	private TextView			songLabel;

	private Utilities			utils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setManager(new MusicManager(getApplicationContext()));
		
		Song lastSong = MusicManager.getUtils().readFromInternalStorage(getApplicationContext());
		if (lastSong != null) {
			MusicManager.setCurrentSong(lastSong);
			MusicManager.setCurrentAlbum(MusicManager.getAlbumFromSong(lastSong));
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

		songLabel = (TextView) findViewById(R.id.songLabel);

		if (MusicManager.getCurrentSong() != null) {
			songLabel.setText(MusicManager.getCurrentSong().getTitle() + " by " + MusicManager.getCurrentSong().getArtist());
			MusicManager.createPlaylistFromSong(MusicManager.getCurrentSong());
		}

		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MusicService.isPlaying())
					pause();
				else
					resume();
			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				next();
			}
		});

		prevButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				prev();
			}
		});

	}

	public void next() {
		MusicManager.getPlaylist().setIndex(MusicManager.getPlaylist().getIndex() + 1);
		if (MusicManager.getPlaylist().getIndex() == MusicManager.getPlaylist().getSize())
			MusicManager.getPlaylist().setIndex(0);

		MusicManager.setCurrentSong(MusicManager.getPlaylist().getPlaylist().get(MusicManager.getPlaylist().getIndex()));

		mService.next();
		songLabel.setText(MusicManager.getCurrentSong().getTitle() + " by " + MusicManager.getCurrentSong().getArtist());

		refreshPlayer();

		if (MusicService.isPlaying())
			playButton.setImageResource(android.R.drawable.ic_media_pause);
		else
			playButton.setImageResource(android.R.drawable.ic_media_play);

		mService.getPlayer().setOnCompletionListener(this);
	}

	public void prev() {
		MusicManager.getPlaylist().setIndex(MusicManager.getPlaylist().getIndex() - 1);
		if (MusicManager.getPlaylist().getIndex() < 0)
			MusicManager.getPlaylist().setIndex(MusicManager.getPlaylist().getSize() - 1);

		MusicManager.setCurrentSong(MusicManager.getPlaylist().getPlaylist().get(MusicManager.getPlaylist().getIndex()));

		mService.prev();
		songLabel.setText(MusicManager.getCurrentSong().getTitle() + " by " + MusicManager.getCurrentSong().getArtist());

		refreshPlayer();

		if (MusicService.isPlaying())
			playButton.setImageResource(android.R.drawable.ic_media_pause);
		else
			playButton.setImageResource(android.R.drawable.ic_media_play);

		mService.getPlayer().setOnCompletionListener(this);
	}

	public void resume() {
		playButton.setImageResource(android.R.drawable.ic_media_pause);
		mService.resume();
		mService.getPlayer().setOnCompletionListener(this);
	}

	public void	play() {
		songLabel.setText(MusicManager.getCurrentSong().getTitle() + " by " + MusicManager.getCurrentSong().getArtist());
		playButton.setImageResource(android.R.drawable.ic_media_pause);
		mService.play();
		mService.getPlayer().setOnCompletionListener(this);
	}

	public void pause() {
		playButton.setImageResource(android.R.drawable.ic_media_play);
		mService.pause();
		mService.getPlayer().setOnCompletionListener(this);
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
		utils.saveToInternalStorage(getApplicationContext(), MusicManager.getCurrentSong());
		mService.stop();
		unbindService(mConnection);
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
		fragmentIndex--;
		if (fragmentIndex < 0)
			super.onBackPressed();
		else
			changeView(fragmentIndex);
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
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

}
