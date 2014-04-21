package com.emerald;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.emerald.containers.Playlist;
import com.emerald.containers.Song;

import android.content.Context;
import android.util.Log;

public class Utilities {

	public void savePlaylistToStorage(Context c, Playlist p, String playlistName) {
		try {
			FileOutputStream fos;
			fos = c.openFileOutput(playlistName, Context.MODE_PRIVATE);
			ObjectOutputStream of = new ObjectOutputStream(fos);
			of.writeObject(p.getPlaylist());
			of.flush();
			of.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	public List<Song> readPlaylistFromStorage(Context c, String playlistName) {
		List<Song> playlist = new ArrayList<Song>();
		FileInputStream fis;
		try {
			fis = c.openFileInput("lastSong");
			ObjectInputStream oi = new ObjectInputStream(fis);
			playlist = (List<Song>) oi.readObject();
			oi.close();
		} catch (FileNotFoundException e) {
			Log.e("InternalStorage", e.getMessage());
		} catch (IOException e) {
			Log.e("InternalStorage", e.getMessage()); 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return playlist;	
	}
	*/
	public void saveToInternalStorage(Context c, Song song) {
		try {
			FileOutputStream fos;
			fos = c.openFileOutput("lastSong", Context.MODE_PRIVATE);
			ObjectOutputStream of = new ObjectOutputStream(fos);
			of.writeObject(song);
			of.flush();
			of.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Song readFromInternalStorage(Context c) {
		Song toReturn = null;
		FileInputStream fis;
		try {
			fis = c.openFileInput("lastSong");
			ObjectInputStream oi = new ObjectInputStream(fis);
			toReturn = (Song) oi.readObject();
			oi.close();
		} catch (FileNotFoundException e) {
			Log.e("InternalStorage", e.getMessage());
		} catch (IOException e) {
			Log.e("InternalStorage", e.getMessage()); 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	} 

	/**
	 * Function to convert milliseconds time to
	 * Timer Format
	 * Hours:Minutes:Seconds
	 * */
	public String milliSecondsToTimer(long milliseconds){
		String finalTimerString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int)( milliseconds / (1000*60*60));
		int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		// Add hours if there
		if(hours > 0){
			finalTimerString = hours + ":";
		}

		// Prepending 0 to seconds if it is one digit
		if(seconds < 10 && seconds > 0){
			secondsString = "0" + seconds;
		} 
		else{
			secondsString = "" + seconds;
		}

		finalTimerString = finalTimerString + minutes + " min " + secondsString + " sec ";

		// return timer string
		return finalTimerString;
	}

	public String milliSecondsToClock(long milliseconds) {
		String finalTimerString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int)( milliseconds / (1000*60*60));
		int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		// Add hours if there
		if (hours > 0){
			finalTimerString = hours + ":";
		}
		if(seconds < 10 && seconds > 0){
			secondsString = "0" + seconds;
		} 
		else{
			secondsString = "" + seconds;
		}
		return finalTimerString + minutes + ":" + secondsString;
	}
	/**
	 * Function to get Progress percentage
	 * @param currentDuration
	 * @param totalDuration
	 * */
	public int getProgressPercentage(long currentDuration, long totalDuration){
		Double percentage = (double) 0;

		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);

		// calculating percentage
		percentage =(((double)currentSeconds)/totalSeconds)*100;

		// return percentage
		return percentage.intValue();
	}

	/**
	 * Function to change progress to timer
	 * @param progress -
	 * @param totalDuration
	 * returns current duration in milliseconds
	 * */
	public int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double)progress) / 100) * totalDuration);

		// return current duration in milliseconds
		return currentDuration * 1000;
	}
}
