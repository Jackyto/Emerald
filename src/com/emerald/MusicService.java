package com.emerald;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
	private final IBinder 		mBinder = new MyBinder();
	private MediaPlayer			player;
	private static boolean		isPlaying = false;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	public class MyBinder extends Binder {
		MusicService getService() {
			return MusicService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return Service.START_STICKY;
	}

	public void prev() {
		MusicManager.getCurrentPlaylist().setIndex(MusicManager.getCurrentPlaylist().getIndex() - 1);
		if (MusicManager.getCurrentPlaylist().getIndex() < 0)
			MusicManager.getCurrentPlaylist().setIndex(MusicManager.getCurrentPlaylist().getSize() - 1);
		MusicManager.setCurrentSong(MusicManager.getCurrentPlaylist().getPlaylist().get(MusicManager.getCurrentPlaylist().getIndex()));

		if (isPlaying) {
			stop();
			play();
		} else {
			Uri songUri = Uri.parse(MusicManager.getCurrentSong().getPath());
			player = MediaPlayer.create(this, songUri);
		}
	}

	public void next() {
		MusicManager.getCurrentPlaylist().setIndex(MusicManager.getCurrentPlaylist().getIndex() + 1);
		if (MusicManager.getCurrentPlaylist().getIndex() == MusicManager.getCurrentPlaylist().getSize())
			MusicManager.getCurrentPlaylist().setIndex(0);

		MusicManager.setCurrentSong(MusicManager.getCurrentPlaylist().getPlaylist().get(MusicManager.getCurrentPlaylist().getIndex()));

		if (isPlaying) {
			stop();
			play();
		} else {
			Uri songUri = Uri.parse(MusicManager.getCurrentSong().getPath());
			player = MediaPlayer.create(this, songUri);
		}
	}



	public void play() {
		if (!isPlaying) {
			isPlaying = true;
			Uri songUri = Uri.parse(MusicManager.getCurrentSong().getPath());
			player = MediaPlayer.create(this, songUri);
			player.start();
		}
		else {
			stop();
			play();
		}
	}

	public void pause() {
		if (isPlaying) {
			isPlaying = false;
			stopForeground(true);
			player.pause();
		}
	}

	public void resume() {
		if (!isPlaying) {
			isPlaying = true;
			player.start();
		}
	}

	public void stop() {
		if (isPlaying) {
			isPlaying = false;
			stopForeground(true);
			pause();
		}
		if (player != null) {
			player.release();
			player = null;
		}
	}

	public MediaPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MediaPlayer player) {
		this.player = player;
	}

	public static boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		MusicService.isPlaying = isPlaying;
	}

}
