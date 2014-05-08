package com.emerald;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class MusicService extends Service {
	private final IBinder 		mBinder = new MyBinder();
	private MediaPlayer			player;
	private static boolean		isPlaying = false;
	private static int 			classID = 2;

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

	public void showNotif() {
		Intent intent = new Intent(this, MainActivity.class);

		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
				Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
		Bitmap b = MusicManager.getAlbumFromSong(MusicManager.getCurrentSong()).getArt();
		Bitmap.createScaledBitmap(b, 128, 128, false);
		Notification notification = new NotificationCompat.Builder(getApplicationContext())

		.setContentTitle("Emerald")
		.setContentText("Now Playing: " + MusicManager.getCurrentSong().getTitle())
		.setLargeIcon(b)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentIntent(pi)
		.build();
		
		startForeground(classID, notification);
	}
	
	public void play() {
		if (!isPlaying) {
			isPlaying = true;
			showNotif();

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
			
			showNotif();
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
