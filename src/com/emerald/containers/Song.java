package com.emerald.containers;

import java.io.Serializable;

public class Song implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1874016138516752931L;
	int		id;
	String	title;
	String	artist;
	String	album;
	String	key;
	String	path;
	int		duration;

	public Song(int id, String title, String artist, String album, String key,
			String path, int duration) {
		super();
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.key = key;
		this.path = path;
		this.duration = duration;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


}
