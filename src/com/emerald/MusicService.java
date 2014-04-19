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
		if (isPlaying) {
			stop();
			play();
		} else {
			Uri songUri = Uri.parse(MusicManager.getCurrentSong().getPath());
			player = MediaPlayer.create(this, songUri);
		}
	}

	public void next() {
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
	}

	public void pause() {
		if (isPlaying) {
			isPlaying = false;
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
			pause();
		}
		if (player != null) {
			player.release();
			player = null;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		stop();
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
